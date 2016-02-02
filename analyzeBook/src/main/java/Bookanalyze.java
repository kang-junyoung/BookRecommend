import oracle.jrockit.jfr.events.JavaProducerDescriptor;
import org.apache.commons.cli.*;
import org.apache.hadoop.util.hash.Hash;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.*;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import scala.Tuple3;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by jykang on 15. 2. 16..
 */
public class Bookanalyze {
    public static void main(String[] args) throws ParseException {
        String INPUT_PATH = "", OUTPUT_PATH = "";

        //Commandline Parsing
        Options options = new Options();
        options.addOption("i", "input", true, "input path(HDFS)");
        options.addOption("o", "output", true, "output path(HDFS)");


        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("i")) {
            INPUT_PATH = cmd.getOptionValue("i");
        } else {
            System.err.println("Input path is invalid");
        }

        if (cmd.hasOption("o")) {
            OUTPUT_PATH = cmd.getOptionValue("o");
        } else {
            System.err.println("Output path is invalid");
        }

//"yarn-cluster"
        SparkConf conf = new SparkConf().setAppName("Book-recommendation").setMaster("local").set("spark.executor.memory", "2g");
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> data = context.textFile(INPUT_PATH);//"/Users/jykang/Copy/BookSample");


        /** A 1
         *  A 2
         *  A 3
         *  B 2 .....
         *  이런 형식으로 만들어줌.**/
        JavaPairRDD<String, String> bookdef = data.mapToPair((s) -> {

            String[] tokens = s.split("\t");
            return new Tuple2<>(tokens[0], tokens[13]);
        });//tokens[0] = user, tokens[1] = booknum


        /** 전체 아이템 갯수 **/
        Broadcast<ArrayList<String>> maxitemList
                = context.broadcast((ArrayList<String>) bookdef.map(m -> m._2()).distinct().collect());
                // collect() : rdd 를 list 로 묶어주는거

        /** (A ,(1,2,3))
         *  (B ,(2,4,6))
         *  (C ,(2,1,5))
         *  (D ,(3,5,6))
         *  **/
        JavaPairRDD<String, Iterable<String>> bookpair = bookdef.groupByKey(); // key별로 묶어줌


        /** (1,2) , (1,3) , (2,3)
         *  (2,4) , (2,6) , (4,6)
         *  ... **/
        JavaPairRDD<String, String> coItemPair = bookpair.flatMapToPair(line -> {
            Iterable<String> list = line._2(); // 아이템 리스트를 받아옴 (1,2,3)
            ArrayList<String> itemList = new ArrayList<>(); // arraylist itemlist = 아이템목록
            for (String elem : list)
                itemList.add(elem); // Iterable to Arraylist


            //cartesian
            ArrayList<Tuple2<String, String>> itemTuples = new ArrayList<>(); // itemTuple
            for (int i = 0; i < itemList.size(); i++) {
                for (int j = 0; j < itemList.size(); j++) {
                    if (i == j) continue;

                    itemTuples.add(new Tuple2<>(itemList.get(i), itemList.get(j)));

                }
            }
            return itemTuples;

        }).distinct(); //distinct() : 중복된것 없애줌.


        /** coItemPair.groupByKey() : (1,(2,3,5)) -> tmpvec : (1,(0,1,1,0,1,0)) **/
        JavaPairRDD<String, ArrayList<Integer>> tmpvec = coItemPair.groupByKey().mapToPair(t -> {

            String Item = t._1();
            ArrayList<String> groupVec = new ArrayList<>();
            if (t._2() != null) {
                for (String s : t._2()) {
                    groupVec.add(s);
                }
            } // groupVec : Iterable to ArrayList<String> // (2,3,5)

            ArrayList<Integer> vec = new ArrayList<>();

            for (int i = 0; i < maxitemList.value().size(); i++) {
                boolean flag = false;
                for (int j = 0; j < groupVec.size(); j++) {
                    if (maxitemList.value().get(i).equals(groupVec.get(j))) { //Record 하는 부분임
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    vec.add(1);
                else
                    vec.add(0);
            }
            return new Tuple2<>(Item, vec);
        });

/**          1. tmpvec을 해쉬에 넣음
 *           2. sampleBook(1580개) 책을 cartesian돌림.
 *           3. 그리고 난 후에 해쉬값을 찾아서 코사인 값을 계산함.
 */
        tmpvec.persist(StorageLevel.MEMORY_AND_DISK());

/**
 * tmpvec에서 첫번째 튜플 값을 이용해서 카제잔 돌림
 * 1580개 일때 돌아감.
 */

        JavaRDD<String> bookCode = tmpvec.map(Tuple2::_1);
        JavaPairRDD<String, String> bookCodePair = bookCode.cartesian(bookCode);
        bookCodePair.persist(StorageLevel.MEMORY_ONLY_SER());
        //북코드를 카제잔 한거임 완료


        Map tmpVecHashMap = tmpvec.collectAsMap();

        JavaRDD<Tuple3<String, String, Double >> cosineS = bookCodePair.map(v-> {
            Double cosine =0.0;
            CosineSimilarity c = new CosineSimilarity();
            cosine =  c.calConsineSimilarity
                    (((ArrayList<Integer>)tmpVecHashMap.get(v._1())), (ArrayList<Integer>)tmpVecHashMap.get(v._2()));

            String firstItem = v._1();
            String secondItem = v._2();

            return new Tuple3<>(firstItem, secondItem, cosine);
        });
        cosineS.saveAsTextFile(OUTPUT_PATH);

        /** groupBy 결과
         * * : (1,[(1,5,0.5773502691896258),(1,3,0.5773502691896258), (1,4,0.40824829046386296), (1,6,0.8660254037844387), (1,2,0.5163977794943222)])**/
        /** lastRDD return : (1, ( 추천책목록 )) **/

        JavaRDD<Tuple3<String,ArrayList<String>,ArrayList<Double>>> last = cosineS.groupBy(line -> line._1()).map(m -> {

            Iterable<Tuple3<String, String, Double>> list = m._2();

            ArrayList<Tuple3<String, String, Double>> tmparray = new ArrayList<>();
            for (Tuple3 elem : list)
                tmparray.add(elem); // Iterable to Arraylist , size 하려고 만든거임


            ArrayList<Double> max = new ArrayList<>(); // max 값 최대값 뽑아내려고 한거요

            for (int i = 0; i < tmparray.size(); i++) // max 배열은 cosine 계산 값을 담는 배열임
            {
                max.add(tmparray.get(i)._3());
            }

            Collections.sort(max); // 코사인값 오름차순

            String selBook = null;//내가 고른책
            ArrayList<String> recBook = new ArrayList<String>();//추천하는책
            ArrayList<Double> check = new ArrayList<Double>();

            // 똑같은거 제거하기위해 max배열에 1인 값을 제거해야함. 그래서 0번째 인덱스가 아니라 1번째 인덱스부터 시작.
            for (int i = max.size() - 2; i > max.size() - 7; i--) {
                for (int j = 0; j < tmparray.size(); j++) {
                    if (Objects.equals(max.get(i), tmparray.get(j)._3())) {

                        selBook = tmparray.get(j)._1(); // 내가 고른책 초기화
                        recBook.add(tmparray.get(j)._2()); // 추천 책 목록 배열에 담는다.
                        check.add(tmparray.get(j)._3());
                    }
                }
            }
            return new Tuple3<>(selBook, recBook, check);

        });
        last.saveAsTextFile(OUTPUT_PATH);
//        JavaRDD<String> data2 = context.textFile("/sampleBook");
//        JavaRDD<Tuple3<String, String, Double>> cosineS  = data2.cartesian(data2).map(v->{
//
//            Double cosine = 0.0;
//            CosineSimilarity C = new CosineSimilarity();
//
//            Iterator<String> iter = tmpVecHashMap.keySet().iterator();
//
//            ArrayList<Integer> one =new ArrayList<>();
//            ArrayList<Integer> two =new ArrayList<>();
//
//            while(iter.hasNext()) {
//                String keys = iter.next();
//                if(keys.equals(v._1())){
//                    one.add((Integer) tmpVecHashMap.get(keys));
//                }
//                if(keys.equals(v._2())){
//                    two.add((Integer) tmpVecHashMap.get(keys));
//                }
//            }
//
//
//            cosine = C.calConsineSimilarity(one,two);
//            String firstItem = v._1();
//            String secondItem = v._2();
//
//            return new Tuple3<>(firstItem, secondItem, cosine);
//
//        });

        /**<(1,(0,1,1,0,1,0)),(2,(1,0,1,1,1,1))> -> (1,2,0.5163977794943222) **/
//        JavaPairRDD<Tuple2<String,ArrayList<Integer>>,Tuple2<String,ArrayList<Integer>>> ca =
//                tmpvec.cartesian(tmpvec);//.filter((line -> !line._1()._1().equals(line._2()._1())));
//        ca.saveAsTextFile("/1580cartesian");
////
//        System.out.println(ca.context());



//        JavaRDD<Tuple3<String, String, Double>>cosineS = tmpvec.cartesian(tmpvec).filter(line ->
//                        !line._1()._1().equals(line._2()._1())).map(v -> {
//                    Double cosine = 0.0;
//                    CosineSimilarity C = new CosineSimilarity();
//                    cosine = C.calConsineSimilarity(v._1()._2(), v._2()._2());
//                    String firstItem = v._1()._1();
//                    String secondItem = v._2()._1();
//
//                    return new Tuple3<>(firstItem, secondItem, cosine);
//
//                });

        /** groupBy 결과 : (1,[(1,5,0.5773502691896258),(1,3,0.5773502691896258), (1,4,0.40824829046386296), (1,6,0.8660254037844387), (1,2,0.5163977794943222)])**/
        /** lastRDD return : (1,6,0.8660254037844387) **/
//
//        JavaRDD<Tuple3<String, String, Double>> last = cosineS.groupBy(line -> line._1()).map(m -> {
//            Iterable<Tuple3<String, String, Double>> list = m._2();
//            ArrayList<Tuple3<String, String, Double>> tmparray = new ArrayList<>();
//            for (Tuple3 elem : list)
//                tmparray.add(elem); // Iterable to Arraylist , size 하려고 만든거임
//
//
//            ArrayList<Double> max = new ArrayList<>(); // max 값 최대값 뽑아내려고 한거요
//
//            for (int i = 0; i < tmparray.size(); i++) // max array는 cosine 계산 값을 담는 배열임
//            {
//                max.add(tmparray.get(i)._3());
//            }
//
//            Double dmax = Collections.max(max); // 최대값
//
//            int i = 0;
//            for (i = 0; i < tmparray.size(); i++) {
//                if (max.get(i) == dmax)
//                    break;
//            }
//            return new Tuple3<>(m._1(), tmparray.get(i)._2(), dmax);
//
//        });

//          last.saveAsTextFile(OUTPUT_PATH);
    }



    /** ------------------------------미사용 ---------------------------------**/

//        (k ->{
//            Broadcast<List<Tuple2<String, String>>> broadvar = context.broadcast(coItemPair.collect());
//        });

    // for(String Item : )
    //(1,(0,1,1,0,1,0)) 되게 해야함 (Item , vector)
//        HashMap<String, Integer> vec1 = new HashMap<>();
//        HashMap<String, Integer> vec2 = new HashMap<>();
//
//        //이제 코사인이용해서 계산해야합니다. ((1,2), 0.xx)나와야함 , cal.calConsineSimilarity(vec1, vec2);
//        CosineSimilarity cal = new CosineSimilarity();
//        cal.calConsineSimilarity(vec1,vec2);
//
//        vector.saveAsTextFile(OUTPUT_PATH);


//        HashMap<JavaPairRDD<String,String>,Integer> mat = new HashMap<JavaPairRDD<String,String>,Integer>();
//        for(int i=0; i<coItemPair.count(); i++){
//            mat.put(coItemPair, 1);
//        }

//        Iterator<Integer> it;
//        Collection<Integer> matvalue = mat.values();
//        it = matvalue.iterator();

//        context.parallelize(); //
//        JavaPairRDD<String, String> vector = coItemPair.mapToPair(s -> new Tuple2(s,HashMap<> ));

//        context.sc().persistRDD();

}
