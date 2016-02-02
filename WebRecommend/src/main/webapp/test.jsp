<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 12. 9.
  Time: ¿ÀÈÄ 3:05
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=euc-kr"%>
<%@page import="java.sql.*"%>
<HTML>
<HEAD><TITLE>database connect</TITLE></HEAD>
<BODY>
<H3>test</H3>
<%
  Class.forName("com.mysql.jdbc.Driver");
  Connection conn = DriverManager.getConnection("jdbc:mysql://52.33.194.233:3306/Book","root","1111");
  if(conn != null){
    out.println("connect success.<BR>");
    conn.close();
    out.println("disconnect database.<BR>");
  }
  else{
    out.println("cant do connect.<BR>");
  }
%>
</BODY>
</HTML>