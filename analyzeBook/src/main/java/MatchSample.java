import java.io.*;

/**
 * Created by jykang on 15. 3. 19..
 */
// Sampling 된 책 목록 총괄
//HowManyBook에서 뽑아온 북코드를 이용해서 원래 데이터에서 뽑아온 북코드와 같은 책들의 목록을 만들어주는 class.
public class MatchSample {
    public static void main(String []args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/BookData.txt"));//전체책목록
        BufferedReader br2 = new BufferedReader(new FileReader("/Users/jykang/Bigdata/sampleBook"));// 샘플링된 북코드임
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jykang/Bigdata/SampleBookList")); //새로 샘플링 책목록을 만들꺼임

        String tokens[];
        String s =null;//전체책목록읽을꺼
        String s2=null;//샘플링읽을 목록

        int count =0;
        while((s2=br2.readLine())!=null){
            while((s=br.readLine())!=null){
                tokens = s.split("\t");
                if(tokens[13].equals(s2)){
                    for(String st : tokens){
                    //    System.out.print(st+"\t");
                        bw.write(st+"\t");
                    }
                  //  System.out.print("\n");
                    bw.write("\n");
                }
            }
            br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/BookData.txt"));//전체책목록
            count++;
            System.out.println("진행 중  : " + count);
        }
        bw.close();
        System.out.println("끝");
    }
}
