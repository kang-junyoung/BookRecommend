import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import scala.Tuple2;

/**
 * Created by jykang on 15. 2. 17..
 */
public class CollaborativeFiltering {
    public static void main(String []args) throws ParseException {
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

        SparkConf conf = new SparkConf().setAppName("Collaborative").setMaster("yarn-cluster");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> data = sc.textFile(INPUT_PATH);
        JavaRDD<Rating> rating = data.map((s)-> {
            String []tokens = s.split("::");
            return new Rating(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]));
        });

        //Build the recommendation model using ALS
        int rank = 10;
        int numIterations= 20;
        MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(rating), rank, numIterations, 0.01);

        //Evluate the model on rating data
        JavaRDD<Tuple2<Object, Object>> userProducts = rating.map(
                new Function<Rating, Tuple2<Object, Object>>() {
                    public Tuple2<Object, Object> call(Rating r) {
                        return new Tuple2<Object, Object>(r.user(), r.product());
                    }
                }
        );
        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
                model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD().map(
                        new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
                            public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating r) {
                                return new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating());
                            }
                        }
                ));

        JavaRDD<Tuple2<Double, Double>> ratesAndPreds =
                JavaPairRDD.fromJavaRDD(rating.map(
                        new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
                            public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating r){
                                return new Tuple2<Tuple2<Integer, Integer>, Double>(
                                        new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating());
                            }
                        }
                )).join(predictions).values();
        JavaPairRDD<Integer, Tuple2<Integer, Double>> userPredictions = JavaPairRDD.fromJavaRDD(predictions.map(
                new Function<Tuple2<Tuple2<Integer, Integer>, Double>, Tuple2<Integer, Tuple2<Integer, Double>>>() {
                    @Override
                    public Tuple2<Integer, Tuple2<Integer, Double>> call(Tuple2<Tuple2<Integer, Integer>, Double> v1) throws Exception {
                        return new Tuple2<>(v1._1()._1(), new Tuple2<>(v1._1()._2(), v1._2()));
                    }
                }
        ));

        //Sort by key & Save
        userPredictions.sortByKey(true).saveAsTextFile(OUTPUT_PATH);
        sc.stop();
    }
//        double MSE = JavaDoubleRDD.fromRDD(ratesAndPreds.map(
//                new Function<Tuple2<Double, Double>, Object>() {
//                    public Object call(Tuple2<Double, Double> pair) {
//                    Double err =pair._1() - pair._2();
//                    return err * err;
//                    }
//                }
//        ).rdd()).mean();
//        System.out.println("Mean Squared Error = " +MSE);



//        double MSE = JavaDoubleRDD.fromRDD(ratesAndPreds.map(
//                new Function<Tuple2<Double, Double>, Object>() {
//                    public Object call(Tuple2<Double, Double> pair) {
//                        Double err = pair._1() - pair._2();
//                        return err * err;
//                    }
//                }
//        ).rdd()).mean();
//        System.out.println("Mean Squared Error = " + MSE);
//    }

}

