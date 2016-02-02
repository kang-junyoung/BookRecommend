<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.lang.String" %>
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
        #img{
            width : 100%;
            height : 100%;

        }
    </style>
</head>

<br>
<br>
<header>
    <div align = "center"><h3 style="color: #FFFFFF ; background: rgb(0,0,0); "> Book Recommend </h3> <br>
    </div>
    <hr>
    <form name="frm" method="get" action="index.jsp">
        <input type="text" name="name" placeholder="도서 제목 검색"/>
        <input type="submit" value="search"/>
    </form>
</header>

<br>
<br>
<br>

<body>


<div id="result" style="display: block">

    <%
        BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/newBookList.txt"));

        String inputbookname = request.getParameter("name");

        String s = null;

        String tokens[]; // 0: 도서명 1 : 저자 2 : 출판사 3: 발행년도 4: 북코드
        if (inputbookname != null) {
            out.print("<table border=\"1\">");
            out.print("<tr style=\"background: #FFFFFF;\">");
            out.print("<td>도서명</td>");
            out.print("<td>저자</td>");
            out.print("<td>출판사</td>");
            out.print("<td>발행년도</td>");
            out.print("</tr>");
            while ((s = br.readLine()) != null) {
                tokens = s.split("\t");
                if (tokens[0].contains(inputbookname)) { //책 이름 검색..

                    String bookcode = tokens[4]; //클릭했을 때 책코드 값을 저장하는 변수

                    out.print("<tr>");

                    //하이퍼링크로 전달할꺼임요
                    out.print("<td> <a href = \"second.jsp?value=" + bookcode + "\">" + tokens[0] + "</a> </td>");
                    for (int i = 1; i < 4; i++) {
                        out.print("<td>" + tokens[i] + "</td>");
                    }

                    out.print("</tr>");
                }

            }
            out.print("</table>");
        }

    %>

</div>


<div id="img">
    <img class ="bg" src="http://cfile5.uf.tistory.com/image/221AE23756442E7C039EA4">
</div>
</body>

</html>
