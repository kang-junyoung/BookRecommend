import scala.Tuple2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jykang on 15. 3. 19..
 */

//많이 빌린 북코드를 몇권이상 빌린 책 코드를 뽑아옴 -> Sampling
public class HowManyBook {
    public static void main(String []args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/bookcode22"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jykang/Bigdata/sampleBook"));

        ArrayList<String> arr_book = new ArrayList<>();

        String s =null;
        while((s=br.readLine())!=null){
            arr_book.add(s);//(s.substring(1)));
        }
        Collections.sort(arr_book); //오름 차순으로 정렬

        HashMap<String, Integer> arr_hash = new HashMap<>();

        int count=1;
        for(int i=0; i< arr_book.size();i++){ // 중복된걸 체크해서 갯수랑 책이름을 해쉬에넣음.
            if(i==176582)
                break;
            if(arr_book.get(i).equals(arr_book.get(i+1))) //중복되는지 확인
            {
                count++;
            }
            else {
                arr_hash.put(arr_book.get(i),count);
                count =1;
            }

        }

        //몇개 이상 중복된 책을 출력해주는 코드임
        int c =0;
        Iterator<String> iter = arr_hash.keySet().iterator();
        while(iter.hasNext()){
            String keys = iter.next();
            if(arr_hash.get(keys)>11) // 12권이상 빌린책들을 추려냄 약 1580권 정도 나옴.
            {
                c++;
                System.out.println("count : " + c + " " + keys + " " + arr_hash.get(keys));
                bw.write(keys +"\n");

            }

        }
        bw.close();

    }
}
