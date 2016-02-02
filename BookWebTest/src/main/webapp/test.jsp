<%@ page import="com.jy.kang.TestClass" %>
<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 3. 4.
  Time: 오후 12:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    TestClass testClass = new TestClass();
%>
<html>
<head>
    <title>Test</title>
</head>
<body>
<input type="button" value="click" id="show" />

    <table>
        <tr>
        <th></th>
        </tr>
        <%
            for(int name : testClass.getResult()){
                out.println("<tr>");
                out.println("<td>"+name+"</td>");
                out.println("</tr>");
            }
        %>
    </table>
</body>
</html>
