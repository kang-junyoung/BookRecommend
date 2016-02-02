import java.io.*;
import java.util.ArrayList;

/**
 * Created by jykang on 15. 11. 12..
 */
public class DBParsing {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/LastRecommend/BookRec"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jykang/GradProject/DBParsingData"));


        String s;
        String LRtoken[] = null;

        while ((s = br.readLine()) != null) {
            LRtoken = s.substring(1, s.length() - 2).split(",");
            LRtoken[1] = LRtoken[1].substring(1); //tokens [1~] : 추천 북코드 담고있음.
            for (int i = 2; i < LRtoken.length; i++) {
                LRtoken[i] = LRtoken[i].trim(); // 공백제거 함수
            }
            for(int i=0; i<LRtoken.length;i++)
            {
                bw.write(LRtoken[i]+"\t");
            }
            bw.write("\n");
        }
        bw.close();
    }
}


