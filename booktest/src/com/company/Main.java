package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Downloads/재학생_대출데이터_2014_Tab.txt"));

        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jykang/Downloads/도서목록정리.txt"));

        String s =null;

        int booknum=1;

        ArrayList<String> booklist = new ArrayList<String>();
        while((s=br.readLine())!=null)
        {
            String tokens[] = s.split("\t");
            if(tokens[4].equals("TITLE"));
            else
            {
                   booklist.add(tokens[4]);
            }
        }

        ArrayList result_List = new ArrayList(); //결과를 담을 ArrayList
        HashSet hs = new HashSet(booklist);

        Iterator it = hs.iterator();
        while(it.hasNext()){
            result_List.add(it.next());
        }
        for(Object str : result_List)
        {
            System.out.println(booknum + " : " + str);

            bw.append(booknum + "\t" + str + "\n");
            booknum++;
        }


    }
}
