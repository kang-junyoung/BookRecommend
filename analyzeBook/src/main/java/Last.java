import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple3;
import scala.Tuple2;

import java.util.*;
/**
 * Created by jykang on 15. 3. 28..
 */
public class Last {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("Last-recommendation").setMaster("local").set("spark.executor.memory", "2g");
        JavaSparkContext context = new JavaSparkContext(conf);
        JavaRDD<String> data = context.textFile("/sample_o");

        JavaRDD<Tuple3<String, String, Double>> tt = data.map(d -> {
            String [] tokens;
            tokens = d.substring(1,d.length()-1).split(",");
            double cosine = Double.parseDouble(tokens[2]);
            return new Tuple3<>(tokens[0], tokens[1], cosine);
        });

        /** groupBy 결과 : (1,[(1,5,0.5773502691896258),(1,3,0.5773502691896258), (1,4,0.40824829046386296), (1,6,0.8660254037844387), (1,2,0.5163977794943222)])**/
        /** lastRDD return : (1, ( 추천책목록 )) **/

        JavaRDD<Tuple3<String,ArrayList<String>,ArrayList<Double>>> last = tt.groupBy(line -> line._1()).map(m -> {

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

            String selBook=null ;//내가 고른책
            ArrayList<String> recBook = new ArrayList<String>();//추천하는책
            ArrayList<Double> check = new ArrayList<Double>();

            for (int i = max.size()-2; i > max.size()-7  ; i--) { // 똑같은거 제거하기위해 max배열에 1인 값을 제거해야함. 그래서 0번째 인덱스가 아니라 1번째 인덱스부터 시작.
                for (int j = 0; j < tmparray.size(); j++) {
                    if (Objects.equals(max.get(i), tmparray.get(j)._3())) { //String이랑 Double이 같은지 확인하는 부분

                        selBook = tmparray.get(j)._1(); // 내가 고른책 초기화
                        recBook.add(tmparray.get(j)._2()); // 추천 책 목록 배열에 담는다.
                        check.add(tmparray.get(j)._3());
                    }
                }
            }
            return new Tuple3<>(selBook, recBook, check);

        });

        last.saveAsTextFile("/check");


//
//
//        JavaRDD<Tuple2<String,ArrayList<String>>> last = tt.groupBy(line -> line._1()).map(m -> {
//
//            Iterable<Tuple3<String, String, Double>> list = m._2();
//
//            ArrayList<Tuple3<String, String, Double>> tmparray = new ArrayList<>();
//            for (Tuple3 elem : list)
//                tmparray.add(elem); // Iterable to Arraylist , size 하려고 만든거임
//
//
//            ArrayList<Double> max = new ArrayList<>(); // max 값 최대값 뽑아내려고 한거요
//
//            for (int i = 0; i < tmparray.size(); i++) // max 배열은 cosine 계산 값을 담는 배열임
//            {
//                max.add(tmparray.get(i)._3());
//            }
//
//            Collections.sort(max); // 코사인값 오름차순
//
//            String selBook=null ;//내가 고른책
//            ArrayList<String> recBook = new ArrayList<String>();//추천하는책
//
//            int bookCount = 0; // 책갯수 (몇권 뽑아낼지 정하는 변수)
//            boolean flag = true;
//            for (int i = max.size()-2; i > max.size()-7  ; i--) { // 똑같은거 제거하기위해 max배열에 1인 값을 제거해야함. 그래서 0번째 인덱스가 아니라 1번째 인덱스부터 시작.
//                for (int j = 0; j < tmparray.size(); j++) {
//                    if (Objects.equals(max.get(i), tmparray.get(j)._3())) {
//
//                        selBook = tmparray.get(j)._1(); // 내가 고른책 초기화
//                        recBook.add(tmparray.get(j)._2()); // 추천 책 목록 배열에 담는다.
//
//                    }
//                }
//            }
//            return new Tuple2<>(selBook, recBook);
//
//        });
//
//        last.saveAsTextFile("/LastRecommend");


    }

}


//      Double dmax = Collections.max(max); // 최대값

//            int i = 0;
//            for (i = 0; i < tmparray.size(); i++) {
//                if (max.get(i) == dmax)
//                    break;
//            }
//            return new Tuple3<>(m._1(), tmparray.get(i)._2(), dmax);