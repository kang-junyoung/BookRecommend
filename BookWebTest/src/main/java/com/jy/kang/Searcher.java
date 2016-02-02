package com.jy.kang;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by jykang on 15. 3. 6..
 */
public class Searcher {

    private Searcher() {}

    public static void main(String[] args) throws IOException, ParseException {

        /**
         * 1. 인덱스의 경로 지정
         * 2. 사용자로부터 검색 퀴리를 입력받는다
         * 3. 검색을 수행
         */

        String indexDir = "/Users/jykang/Bigdata/BookSearch/index";
        String field = "contents"; // 키워드로 검색할 필?
        String query =  "NGO";  //"사용자로부터 입력받은 쿼리!"

        int hits = 10;  //한번에 가져올 최대 검색결과

        //Index 파일로 부터 검색 하기위한 IndexSearcher를 생성
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new StandardAnalyzer(); //검색 키워드를 분석할 Analyzer 를 생성


        //쿼리식으로부터 실제 루씬에서 사용될 Query 객체를 만들어 반환하는 QueryParser를 생성한다.
        QueryParser queryParser = new QueryParser(field, analyzer);
        Query parsedQuery = queryParser.parse(query);

        System.out.println("query : " + query);
        System.out.println("parsedQuery : " + parsedQuery.toString());
        System.out.println("Searching for : "+ parsedQuery.toString(field));

        //검색을 시작. 이 때의 랭킹은 루씬에서 주어짐

        TopDocs topDocs = searcher.search(parsedQuery, hits*2);
        ScoreDoc[] results = topDocs.scoreDocs;

        int numTotalHits = topDocs.totalHits;
        System.out.println(numTotalHits + "total matching documents");

        for(int i=0; i< numTotalHits; i++){
            System.out.println("doc = " + results[i].doc +" score = " +results[i].score );
        }
//        Searcher searcher = new Searcher();
//        searcher.search(indexDir, query, hits);

    }

//    private void search(String indexDir, String query, int hits) throws IOException, ParseException {
//        Directory directory = FSDirectory.open(Paths.get(indexDir));
//        IndexReader indexReader = DirectoryReader.open(directory);
//        IndexSearcher searcher = new IndexSearcher(indexReader);
//
//        QueryParser queryParser = new QueryParser("content",new StandardAnalyzer());
//
//        Query parsedQuery = queryParser.parse(query);
//
//        System.out.println("Searching for: " + parsedQuery.toString(query));
//
//
//        TopDocs topDocs = searcher.search(parsedQuery, hits);
//        ScoreDoc[] results = topDocs.scoreDocs;
//
//        for(ScoreDoc result : results)
//        {
////            System.out.println("doc="+result.doc+" score="+result.score);
//
//            int id = result.doc;
//            Document document = searcher.doc(id);
//            System.out.println(document.get("path"));
//
//
//        }
//    }


}

