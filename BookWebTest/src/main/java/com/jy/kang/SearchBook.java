package com.jy.kang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by jykang on 15. 3. 9..
 */
public class SearchBook {

    public static void main(String []args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/BookData.txt"));

        String bookname;
        Scanner sc = new Scanner(System.in);
        System.out.print("도서명을 검색 : ");
        bookname = sc.nextLine();


        String s =null;
        String tokens[];
        //4 : 도서명 , 5: 작가 , 6: 출판사, 7: 출판날짜, 13 : 책코
        while((s=br.readLine())!=null) {
            tokens = s.split("\t");
            if(tokens[4].contains(bookname)){
                System.out.println("도서명 : " + tokens[4] + " 작가 : " + tokens[5]);
            }

        }
        System.out.println("nothing");
    }
}
