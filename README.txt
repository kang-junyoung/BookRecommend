README

**파일 설명 **

1.JSP File
webapps 폴더 : 웹프로젝트 파일
index.jsp : 검색을 위한 메인 페이지
res.jsp : 책 검색 후 책의 목록을 보여주는 페이지
second.jsp : 해당 책의 추천 결과를 보여주는 페이지

2.Data File
BookData.txt : 원본 데이터
LastRecommend : Collaborative Filtering 과정을 통한 추천 도서 목록 데이터(코사인 유사도 값으로 오름차순)
top10 : DDC 별로 가장 많이 빌린 도서 10권 목록인 데이터
bookcode22 : 176,583권의 도서 대출정보 중에서 bookcode만을 가지고 있는 파일
sampleBook : HowManyBook 파일 결과 데이터
SampleBookList : sampleBook 파일을 이용하여 MatchSample 파일 결과 데이터
DBbookData.txt : BookData 파일을 DB에 올리기 위한 파싱한 데이터
DBParsingData.txt : LastRecommend 파일을 DB에 올리기 위한 파싱한 데이터
DBtop.txt : top10을 DB에 올리기 위한 파싱한 데이터


3. Java File
Bookanalyze.java : Spark를 이용한 Collaborative Filtering 코딩 파일
CosineSimilarity.java : 코사인 유사도 파일 
BookList.java : 원본데이터에서 bookcode값만 읽어오는 코드 파일(중복삭제)
DBbookData.java : BookData 파일을 DB에 올리기위해 파싱한 코드 파일(res.jsp에서 이용)
DBParsing.java : LastRecommend 파일을 DB에 올리기 위해 파싱한 코드 파일
DBtop10.java : top10 파일을 DB에 올리기 위해 파싱한 코드 파일
HowManyBook.java : 12권 이상 빌린 도서의 bookcode를 구하는 코드 파일
MatchSample.java : HowManyBook에서 뽑아온 bookcode를 이용해서 원본데이터와 같은 형식의 파일을 만드는 코드 파일 

4. Xml File
pom.xml : Maven 파일


5. Jar File 
Bookanalyze-1.0-SANPSHOT.jar : 실행파일

** 실행 환경 **

1. 알고리즘 동작 컴퓨터 환경
CPU : 2.6GHz Intel Core i5 RAM : 8GBOS : OS X Yosemite 10.10.4 Language : JavaPlatform : Hadoop-2.5.2, Spark-1.2.0, MAVEN, Java 1.8.0_66Data Storage : HDFSDevelopment tool : JetBrains Intellij IDEA 15

2. 서버 컴퓨터 환경
Platform : Java 1.8.0_66Web Server : Apache 2.2Web Aplication Server : Tomcat 7
Database : MySQL 5.6.27Database connector : mysql-connector-java-5.1.38 
Server 사양- CPU : Intel® Xeon® CPU E5-2676 v3 @ 2.40HZ - RAM : 1GB- OS : Ubuntu 14.04.2
- Amazon AWS EC2



** 실행방법 **

1. 알고리즘 동작 컴퓨터환경과 서버 컴퓨터 환경을 설정한다.
2. 알고리즘 동작 컴퓨터에서 HDFS로 SampleBookList와 LastRecommend 파일을 업로드한다.
3. Bookanalyze-1.0-SANPSHOT.jar 을 서버에 업로드한다.
4. start-dfs.sh&&start-yarn.sh 입력하여 Hadoop을 실행시킨다.
5. 알고리즘 동작 컴퓨터에서 spark-submit --class Bookanalyze --master yarn-cluster Bookanalyze-1.0-SNAPSHOT.jar -i /SampleBookList -o /LastRecommend 명령어를 입력 후 실행한다. 
6. DBbookData.txt, DBParsingData.txt, DBtop.txt을 Database에 해당 테이블에 삽입한다.
7. jsp 코드 파일을 /var/lib/tomcat7/webapps/ 에 capstone(임시) 폴더를 생성한다.
8. capstone폴더에 jsp파일을 삽입한다.
9. capstone폴더에 WEB-INF폴더를 생성후 lib 폴더 생성후 lib 폴더 안에 mysql-connector-java-5.1.38-bin.jar파일을 삽입한다. 
10. 서버주소/capstone/(ex. 52.33.194.233/capstone/)을 입력하여 사이트에 접속 후 이용한다.






