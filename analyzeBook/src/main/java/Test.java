import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


//sparse problem solution source
//많이 안빌린책 추천하는 테스트 코드
/**
 * Created by jykang on 15. 3. 28..
 */
public class Test {

    public static void main(String []args) throws IOException {
        String code = "b1487991";
        BufferedReader bk = new BufferedReader(new FileReader("/Users/jykang/Bigdata/BookData.txt"));
        String d ;
        String kToken[];
        double ddc=0;
        while((d=bk.readLine())!= null){ //DDC 값 찾음.
            kToken = d.split("\t");
            if(code.equals(kToken[13]))
            {
                ddc = Double.parseDouble(kToken[8]);
                break;
            }
            else
            {
                ddc = 0;
            }
        }

        int nCount=0;// n의 갯수 세는 변수
        int chDDC = (int)ddc/10;

        ArrayList<String> recBookList = new ArrayList<>();

        BufferedReader bt = new BufferedReader(new FileReader("/Users/jykang/Bigdata/top10"));
        String z;
        String zToken[];
        while((z=bt.readLine())!=null)
        {
            if(z.trim().equals("n"))
                nCount++;

            else if(chDDC == nCount-1) // 같은 분류 번호일 때 읽어오기!...
            {
                zToken = z.split(" ");
          //      System.out.println(" dd : " + zToken[0] + " count " + zToken[1]);
                recBookList.add(zToken[0]);
            }
        }
        for( String a : recBookList)
        {
            System.out.println(a);
        }

    }
}
