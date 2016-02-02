import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple5;

/**
 * Created by jykang on 15. 3. 2..
 */
// 북코드만 읽어옴.
public class BookList {
    public static void main(String args[]){
        String INPUT_PATH="/realBook", OUTPUT_PATH="/bookcode2";

        SparkConf conf = new SparkConf().setAppName("Book-List").setMaster("yarn-cluster");
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> data = context.textFile(INPUT_PATH);

        JavaRDD<String> bookCode = data.map(p -> {
            String tokens[] = p.split("\t");
            return (tokens[13]);
        }).distinct();

        bookCode.saveAsTextFile(OUTPUT_PATH);
    }
}
