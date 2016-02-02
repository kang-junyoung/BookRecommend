<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>

<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 3. 29.
  Time: 오후 4:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Book Recommend System</title>
<head>
  <style type="text/css">
    * {
      margin: 0;
      padding: 0;
    }

    body {
      margin: 0 auto;
      width: 960px;
    }
  </style>
</head>
<body>


<br>
<br>
<header>
  <div align = "center"><h3 style="color: #FFFFFF ; background: rgb(0,0,0); "> Book Recommend Result</h3> <br>
  </div>

</header>

<br>


<table border="1">
  <tr style="background: #FFFFFF;">
    <td>
      도서명
    </td>
    <td>
      저자
    </td>
    <td>
      출판사
    </td>
    <td>
      출판 날짜
    </td>


  </tr>
  <%
    String code = request.getParameter("value");

    if(code!=null) {
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
      //DDC =8,  bookcode =13
      if(recBookList.isEmpty()) // 만약 추천목록에 내가 클릭한 책이 없다면.
      {
        BufferedReader bk = new BufferedReader(new FileReader("/Users/jykang/Bigdata/BookData.txt"));
        String d ;
        String kToken[];
        double ddc=0;
        while((d=bk.readLine())!= null){ //DDC 값 찾음.
          kToken = d.split("\t");
          if(code.equals(kToken[13]))
          {
            ddc = Double.parseDouble(kToken[8]);
            break;
          }
          else
          {
            ddc = 0;
          }
        }

        int nCount=0;// n의 갯수 세는 변수
        int chDDC = (int)ddc/10;

        BufferedReader bt = new BufferedReader(new FileReader("/Users/jykang/Bigdata/top10"));
        String z;
        String zToken[];
        while((z=bt.readLine())!=null)
        {
          if(z.trim().equals("n"))
            nCount++;

          else if(chDDC == nCount-1) // 같은 분류 번호일 때 읽어오기!...
          {
            zToken = z.split(" ");
            recBookList.add(zToken[0]);
          }
        }
      }


      //arrayList에 있는 북코드를 읽어서 책 목록 보여준다.
      BufferedReader bn = new BufferedReader(new FileReader("/Users/jykang/Bigdata/newBookList.txt"));

      String n = null;
      String nTokens[];
      while ((n = bn.readLine()) != null) {
        nTokens = n.split("\t");
        for (String aRecBookList : recBookList) {
          if (aRecBookList.equals(nTokens[4])) {
            out.print("<tr>");
            for (int k = 0; k < 4; k++) {
              out.print("<td>" + nTokens[k] + "</td>");
            }
            out.print("</tr>");
          }
        }
      }
    }
    else
      out.println("transporting code error");
  %>



</table>
</body>
</html>

