import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by jykang on 15. 3. 19..
 */
public class Cartesian {
    public static void main(String []args) throws IOException {
        String INPUT_PATH = "hdfs://localhost:9000/bookcode", OUTPUT_PATH = "/Users/jykang/Bigdata/cartesian";
        int count =0;
        ArrayList<String> arr_bookcode = new ArrayList<String>();
        try {

            Path pt = new Path(INPUT_PATH);

            Configuration conf = new Configuration();
            conf.set("fs.default.name", "hdfs://localhost:9000");

            FileSystem fs = FileSystem.get(conf);

            FileStatus[] status = fs.listStatus(pt);


            for (int i=0;i<status.length;i++)
            {

                BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(status[i].getPath()))); //파일 읽어옴
                System.out.println("filename : "+ status[i].getPath());


                String line=null;

                while ((line=br.readLine()) != null)
                {
                    arr_bookcode.add(line);
                    count ++;
                }
            }

            fs.close();
        } catch (Exception ignored) {
            System.out.println("input is Wrong");
            ignored.printStackTrace();
        }

        for(String s : arr_bookcode){
            System.out.println(s);
        }
        System.out.println("Count: " + count );


            int limit =5000;

            BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH));

            for (int i = 0; i < limit; i++) {
                for (int j = 0; j < arr_bookcode.size(); j++) {
                    if (i == j)
                        continue;
                    else
                        bw.write(arr_bookcode.get(i) + " " + arr_bookcode.get(j)+"\n");

                }
                System.out.println("i : " + i);
            }
            bw.close();

        }


}
