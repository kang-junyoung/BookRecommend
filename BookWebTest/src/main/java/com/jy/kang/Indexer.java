package com.jy.kang;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by jykang on 15. 3. 5..
 */
public class Indexer {
   private Indexer() {}
    public static void main(String []args) throws IOException {
        /**
         * 1. 경로지정
         * 2. IndexWrite 생성
         * 3. IndexWriterConfig
         * 4. Index 생성
         */
        String dataDir = "/Users/jykang/Bigdata/BookSearch/data";
        String indexDir = "/Users/jykang/Bigdata/BookSearch/index";

        Indexer indexer = new Indexer();
        indexer.createIndex(dataDir, indexDir);
    }

    private void createIndex(String dataDir, String indexDir) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);

        boolean create = true;
        if(create){
            writerConfig.setOpenMode(OpenMode.CREATE);
        }
        else{
            writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }

        IndexWriter indexWriter = new IndexWriter(dir, writerConfig);

//        indexWriter.deleteAll(); // 생성되었던 인덱스를 모두 삭제

        indexDocument(indexWriter, new File(dataDir));
        indexWriter.close();
        //close를 안하면 write.lock 파일이 생긴다(아직 읽는중 이라는,,,)
    }

    private void indexDocument(IndexWriter indexWriter, File currentFile) throws IOException {
        if(currentFile.canRead())
        {
            if(currentFile.isDirectory()) //현재 읽은 파일이 디렉토리일떄
            {
                File[] files = currentFile.listFiles();
                if(files != null)
                {
                    for(File cFile : files)
                    {
                        indexDocument(indexWriter, cFile); //재귀 호출을 통해 파일이 디렉토리가 아닌 경우 문서를 색인
                    }
                }
            }
            else // 디렉토리가 아닐때(파일일때)
            {
                Document document = new Document();

                Field pathField = new StringField("path",currentFile.getPath(), Field.Store.YES);
                document.add(pathField);
                Field contentField = new TextField("content",new BufferedReader(new InputStreamReader(new FileInputStream(currentFile))));
                document.add(contentField);

                indexWriter.addDocument(document);

                System.out.println("Added : " + currentFile);

                //indexWriter.close();
            }
        }
    }
}

