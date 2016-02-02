import java.io.*;

/**
 * Created by jykang on 15. 12. 9..
 */
public class DBtop10 {
    public static void main(String []args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/toptmp.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jykang/Bigdata/DBtop.txt"));

        String s =null;
        String []tokens;
        String tmp = null;
        int nCount=-10;
        while((s=br.readLine())!=null){
            if(s.trim().equals("n")){
                if(tmp!=null)
                    bw.write(tmp+"\n");
                nCount +=10;
                tmp = Integer.toString(nCount);
            }
            else{
                tokens = s.split(" ");
                tmp = tmp+ "\t"+ tokens[0];
            }
        }
        bw.close();
    }
}
