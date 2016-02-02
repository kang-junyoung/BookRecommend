<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.*" %>

<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 3. 29.
  Time: ���� 4:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
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

        #cen {
            position: relative;
            top: 50px;
            left: 300px;
        }

        #res {
            position: relative;
            top: 70px;
            left: 170px;
        }
    </style>
</head>

<body>
<div id="cen">
    <span style="color:blue; font-size:55">R</span>
    <span style="color:orange; font-size:45">e</span>
    <span style="color:red; font-size:45">c</span>
    <span style="color:yellow; font-size:45">o</span>
    <span style="color:blue; font-size:45">m</span>
    <span style="color:green; font-size:45">m</span>
    <span style="color:red; font-size:45">e</span>
    <span style="color:pink; font-size:45">n</span>
    <span style="color:orange; font-size:45">d</span>
    &nbsp&nbsp&nbsp&nbsp&nbsp
    <span style="color:black; font-size:55">R</span>
    <span style="color:black; font-size:45">e</span>
    <span style="color:black; font-size:45">s</span>
    <span style="color:black; font-size:45">u</span>
    <span style="color:black; font-size:45">l</span>
    <span style="color:black; font-size:45">t</span>
</div>

<div id="res">
    <table border="1">
        <tr style="background: #FFFFFF;">
            <td>
                Book Name
            </td>
            <td>
                Author
            </td>
            <td>
                Publish Company
            </td>
            <td>
                Publish Date
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

        String sql = " select * from Recommend where Code = '" + code + "';";

        rs = stmt.executeQuery(sql);

        if (rs.next()) {
          ArrayList<String> rcmdList = new ArrayList<String>();
          for (int i = 1; i < 11; i++) {
            String rcode = rs.getString(i + 1);
            rcmdList.add(rcode);

          }

          for (String rcmd : rcmdList) { // 추천 책 코드 읽는 반복문
            stmt = conn.createStatement();

            if (rcmd != null) {
              String sql2 = " select * from BookList where Code = '" + rcmd + "';";
              rs2 = stmt.executeQuery(sql2); // recommend에서 추천 책을 읽어서 booklist가서 책을 읽어오는 쿼리

              if(rs2.next()){
                String title = rs2.getString("Title");
                String author = rs2.getString("Author");
                String publisher = rs2.getString("Publisher");
                String year = rs2.getString("Year");

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
            stmt = conn.createStatement();

            sql = " select * from BookList where Code = '" + code + "';";

            rs = stmt.executeQuery(sql);
            int DDC2=0;
            if(rs.next()){
                float DDC = rs.getFloat("DDC");
                DDC2 = (int)DDC;
                DDC2 = DDC2/10;
                DDC2 = DDC2*10;

            }
            stmt = conn.createStatement();

            sql = " select * from Top10 where DDC = '" + DDC2 + "';";

            rs = stmt.executeQuery(sql);

            if(rs.next()){
                ArrayList<String> rcmdList = new ArrayList<String>();
                for (int i = 1; i < 11; i++) {
                    String rcode = rs.getString(i + 1);
                    rcmdList.add(rcode);
                }

                for (String rcmd : rcmdList) { // 추천 책 코드 읽는 반복문
                    stmt = conn.createStatement();

                    if (rcmd != null) {
                       String sql2 = " select * from BookList where Code = '" + rcmd + "';";
                       rs2 = stmt.executeQuery(sql2); // recommend에서 추천 책을 읽어서 booklist가서 책을 읽어오는 쿼리

                        if(rs2.next()){
                            String title = rs2.getString("Title");
                            String author = rs2.getString("Author");
                            String publisher = rs2.getString("Publisher");
                            String year = rs2.getString("Year");

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

</div>
</body>
</html>