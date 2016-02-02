import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.*;

/**
 * Created by jykang on 15. 12. 9..
 */
public class DBbookData {
    public static void main(String []args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/BookData.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jykang/Bigdata/DBbookData.txt"));

        String s =null;
        String []tokens;
        String tmp = null;
        while((s=br.readLine())!=null){
            tokens = s.split("\t");
            tmp = tokens[13]+"\t"+tokens[4]+"\t"+tokens[5]+"\t"+tokens[6]+"\t"+tokens[7]+"\t"+tokens[8];
            bw.write(tmp+"\n");

        }
        bw.close();
    }
}
