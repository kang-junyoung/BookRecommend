<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 12. 9.
  Time: 오전 1:55
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.lang.String" %>
<%@ page import="java.sql.*" %>
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
    #cen{
      position:relative; left:300px;
    }
    #res{
      position:relative; left:120px;
    }
  </style>
</head>

<body>

<div id="cen">
  <span style="color:black; font-size:55">B</span>
  <span style="color:black; font-size:45">o</span>
  <span style="color:black; font-size:45">o</span>
  <span style="color:black; font-size:45">k</span>
  &nbsp&nbsp&nbsp&nbsp&nbsp
  <span style="color:blue; font-size:55">R</span>
  <span style="color:orange; font-size:45">e</span>
  <span style="color:red; font-size:45">c</span>
  <span style="color:yellow; font-size:45">o</span>
  <span style="color:blue; font-size:45">m</span>
  <span style="color:green; font-size:45">m</span>
  <span style="color:red; font-size:45">e</span>
  <span style="color:pink; font-size:45">n</span>
  <span style="color:orange; font-size:45">d</span>
</div>

<div id="res">

  <%

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    final String USER = "root";
    final String PASSWORD = "1111";

    final String DB_url = "jdbc:mysql://52.33.194.233:3306/Book";
    String inputbookname = request.getParameter("name");

    try {
      Class.forName("com.mysql.jdbc.Driver");

      conn= DriverManager.getConnection(DB_url, USER, PASSWORD);


      stmt = conn.createStatement();

      String sql = " select * from BookList where Title Like '%" + inputbookname +"%';";

      rs = stmt.executeQuery(sql);

      out.print("<table border=\"1\">");
      out.print("<tr style=\"background: #FFFFFF;\">");
      out.print("<td>Book Name</td>");
      out.print("<td>Author</td>");
      out.print("<td>Publish Company</td>");
      out.print("<td>Publish Year</td>");
      out.print("</tr>");

      while(rs.next()){
        String code = rs.getString("Code");
        String title = rs.getString("Title");
        String author = rs.getString("Author");
        String publisher = rs.getString("Publisher");
        String year = rs.getString("Year");
        float DDC = rs.getFloat("DDC");

        //tr : 행 td :열
        out.print("<tr>");
        out.print("<td> <a href = \"second.jsp?value=" + code + "\">" + title + "</a> </td>");
        out.print("<td>" + author + "</td>");
        out.print("<td>" + publisher + "</td>");
        out.print("<td>" + year + "</td>");
        out.print("</tr>");

      }
      out.print("</table>");


    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    finally {
      conn.close();

    }

  %>




</div>
</body>

</html>