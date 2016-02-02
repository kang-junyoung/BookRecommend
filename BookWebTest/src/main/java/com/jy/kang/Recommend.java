package com.jy.kang;

/**
 * Created by jykang on 15. 3. 1..
 */
/**
 * Created by jykang on 15. 2. 23..
 */
import java.io.*;
import java.util.Scanner;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;

public class Recommend {
    public String rec() throws ParseException {
        String INPUT_PATH = "hdfs://localhost:9000/book_output", OUTPUT_PATH = "hdfs://localhost:9000/last", BOOK_CODE="";

        /*Commandline Parsing
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption("i", "input", true, "input path(HDFS)");
        options.addOption("o", "output", true, "output path(HDFS)");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("i")) {
            INPUT_PATH = cmd.getOptionValue("i");
        } else {
            System.err.println("Input path is invalid");
        }

        if (cmd.hasOption("o")) {
            OUTPUT_PATH = cmd.getOptionValue("o");
        } else {
            System.err.println("Output path is invalid");
        }

        */

        String RecommendBook =null;



        Scanner in = new Scanner(System.in);
        System.out.print("Input Book Code : ");
        BOOK_CODE = in.next();

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
                String tokens[];
                String line=null;

                while ((line=br.readLine()) != null)
                {
                    tokens = line.substring(1, line.length()-1).split(",");
                    if(tokens[0].equals(BOOK_CODE))
                    {
                        RecommendBook = tokens[1];
                        break;
                    }
                }
            }

            fs.close();
        } catch (Exception ignored) {
            System.out.println("input is Wrong");
            ignored.printStackTrace();
        }

        try {
            Path py = new Path(OUTPUT_PATH);
            Configuration conf1 = new Configuration();
            conf1.set("fs.default.name", "hdfs://localhost:9000");
            FileSystem fi = FileSystem.get(conf1);

            if ( fi.exists( py ))
            {
                fi.delete( py, true );
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fi.create(py)));

            bw.write(BOOK_CODE +"책을 빌렸으면 "+ RecommendBook+" 책을 추천합니다"+"\n");
            bw.close();
            fi.close();

        }
        catch(Exception ignored){
            System.out.println("Output is wrong");

        }
        return (BOOK_CODE +"책을 빌렸으면 "+ RecommendBook+" 책을 추천합니다"+"\n");

    }
}

