import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by jykang on 15. 2. 17..
 */
public class WordCount {
    public static void main(String []args) throws ParseException {
        String INPUT_PATH="", OUTPUT_PATH="";

        //Commandline Parsing
        Options options = new Options();
        options.addOption("i","input",true,"input path(HDFS)");
        options.addOption("o","output",true,"output path(HDFS)");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options,args);
        if(cmd.hasOption("i")){
            INPUT_PATH = cmd.getOptionValue("i");
        } else{
            System.err.println("Input path is invalid");
        }

        if(cmd.hasOption("o")){
            OUTPUT_PATH = cmd.getOptionValue("o");
        } else{
            System.err.println("Output path is invalid");
        }

        SparkConf conf = new SparkConf().setAppName("WordCount").setMaster("yarn-cluster");
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> lines = context.textFile(INPUT_PATH); //default 가 라인
        JavaRDD<String> words = lines.flatMap((s) -> Arrays.asList(s.split(" ")));

        JavaPairRDD<String, Integer> oneWord = words.mapToPair(s -> new Tuple2(s,1));
        JavaPairRDD<String, Integer> reduce = oneWord.reduceByKey((int1, int2)-> int1 + int2);

        reduce.saveAsTextFile(OUTPUT_PATH);

//        SparkConf conf = new SparkConf().setAppName("Book-recommendation").setMaster("yarn-cluster");
//        JavaSparkContext context = new JavaSparkContext(conf);

    }
}
