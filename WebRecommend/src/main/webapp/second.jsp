<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.*" %>
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
    <div align="center"><h3 style="color: #FFFFFF ; background: rgb(0,0,0); "> Book Recommend Result</h3> <br>
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

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        final String USER = "root";
        final String PASSWORD = "1111";

        final String DB_url = "jdbc:mysql://52.33.194.233:3306/Book";


        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_url, USER, PASSWORD);

            stmt = conn.createStatement();

            String sql = " select * from Recommend where code = '" + code + "';";

            rs = stmt.executeQuery(sql);

            out.print("<table border=\"1\">");
            out.print("<tr style=\"background: #FFFFFF;\">");
            out.print("<td>Book Name</td>");
            out.print("<td>Author</td>");
            out.print("<td>Publish Company</td>");
            out.print("<td>Publish Year</td>");
            out.print("</tr>");


            if (rs.next()) {
                ArrayList<String> rcmdList = new ArrayList<String>();
                for (int i = 0; i < 11; i++) {
                    String rcode = rs.getString(i + 1);
                    rcmdList.add(rcode);
                }

                for (String rcmd : rcmdList) { // 추천 책 코드 읽는 반복문
                    stmt = conn.createStatement();

                    if (rcmd != null) {
                        String sql2 = " select * from BookList where code = '" + rcmd + "';";
                        rs2 = stmt.executeQuery(sql2); // recommend에서 추천 책을 읽어서 booklist가서 책을 읽어오는 쿼리

                        if(rs2.next()){
                            String title = rs.getString("Title");
                            String author = rs.getString("Author");
                            String publisher = rs.getString("Publisher");
                            String year = rs.getString("Year");

                            //tr : 행 td :열
                            out.print("<tr>");
                            out.print("<td>" + title + "</td>");
                            out.print("<td>" + author + "</td>");
                            out.print("<td>" + publisher + "</td>");
                            out.print("<td>" + year + "</td>");
                            out.print("</tr>");

                        }
                    }
                }
            }
            else{  // 추천북 없는 애들 DDC로 추천한다

            }
            out.print("</table>");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();

        }

    %>


</table>
</body>
</html>

