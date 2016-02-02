<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 11. 12.
  Time: 오후 2:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.lang.String" %>
<html>
<title>도서 추천 시스템</title>
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

<header>

</header>

<br>

<h3 style="color: #FFFFFF ; background: rgb(0,0,255);"> 검색된 책과 연관된 도서를 추천합니다. </h3> <br>
<hr>
<form name ="frm" method="get" action="index.jsp">
  <input type="text" name="name" placeholder="도서 제목 검색"/>
  <input type="submit" value="search"/>
</form>
<br>
<br>


<div id="result" style="display: block">

  <%--<table border="1">--%>
  <%--<tr style="background: #8EDEFF;">--%>
  <%--<td>--%>
  <%--도서명--%>
  <%--</td>--%>
  <%--<td>--%>
  <%--저자--%>
  <%--</td>--%>
  <%--<td>--%>
  <%--출판사--%>
  <%--</td>--%>
  <%--<td>--%>
  <%--발행 년도--%>
  <%--</td>--%>


  <%--</tr>--%>


  <%
    BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/newBookList.txt"));

    String inputbookname = request.getParameter("name");

    String s = null;

    String tokens[]; // 0: 도서명 1 : 저자 2 : 출판사 3: 발행년도 4: 북코드
    if (inputbookname != null) {
      while ((s = br.readLine()) != null) {
        tokens = s.split("\t");
        if (tokens[0].contains(inputbookname)) { //책 이름 검색..

          String bookcode=tokens[4]; //클릭했을 때 책코드 값을 저장하는 변수

          out.print("<tr>");

          //하이퍼링크로 전달할꺼임요
          out.print("<td> <a href = \"second.jsp?value="+ bookcode+ "\">" + tokens[0] +"</a> </td>");
          for (int i = 1; i < 4; i++) {
            out.print("<td>" + tokens[i] + "</td>");
          }

          out.print("</tr>");
        }

      }
    }
  %>

  </table>
</div>
</body>
</html>

