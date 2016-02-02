package com.jy.kang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jykang on 15. 11. 11..
 */
public class secondtest {
    public static void main(String[] args) throws IOException {


        String code = "b1792162";
        ArrayList<String> recBookList = new ArrayList<String>();


        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/LastRecommend/BookRec"));
        String s;
        String LRtoken[] = null;

        while ((s = br.readLine()) != null) {
            LRtoken = s.substring(1, s.length() - 2).split(",");
            LRtoken[1] = LRtoken[1].substring(1); //tokens [1~] : 추천 북코드 담고있음.
            for (int i = 2; i < LRtoken.length; i++) {
                LRtoken[i] = LRtoken[i].trim(); // 공백제거 함수
            }

            if (code.equals(LRtoken[0])) {
                for (int p = 1; p < LRtoken.length; p++)
                    recBookList.add(LRtoken[p]);

            }
        }
        BufferedReader bn = new BufferedReader(new FileReader("/Users/jykang/Bigdata/newBookList.txt"));

        String n = null;
        String nTokens[];
        while ((n = bn.readLine()) != null) {
            nTokens = n.split("\t");
            for (String aRecBookList : recBookList) {
                if (aRecBookList.equals(nTokens[4])) {
                    for (int k = 0; k < 4; k++) //코사인 높은거 5개 추천함
                        System.out.print(nTokens[k]);
                    System.out.println();
                }
            }
        }

    }
}


//
//if (code.equals(LRtoken[0])) { //클릭한 코드랑 파일에서 첫번째 토큰이랑 같다면!!!
//        String str = null;
//        String tokens2[] = null; // bf 에서 열은거 담을꺼요. // 0: 도서명 1 : 저자 2 : 출판사 3: 발행년도 4: 북코드
//
//        for(int i=1; i<LRtoken.length;i++)
//        {
//        while ((str = bf.readLine()) != null) {
//        tokens2 = str.split("\t");
//        if(tokens2[4].contains(LRtoken[i])){
//        for (int m=0; m< 4; m++) {
//        System.out.print(tokens2[m]+ " ");
//        }
//        System.out.println();
//        break;
//        }
//        }
//        }
//
//        }
