<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: jykang
  Date: 15. 3. 10.
  Time: 오전 1:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<h3 style="color: #FFFFFF ; background: rgb(0,0,255);"> 추천 도서 목록입니다. </h3> <br>

<table border="1">
    <tr style="background: #8EDEFF;">
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
            File path = new File("/Users/jykang/Bigdata/LastRecommend");
            String files[] = path.list();

            ArrayList<String> recBookList = new ArrayList<String>(); //추천 책목록 담는 배열리스트

            boolean flag = false;

            for (int k = 0; k < files.length; k++)
            {
                BufferedReader br = new BufferedReader(new FileReader("/Users/jykang/Bigdata/LastRecommend/" + files[k]));
                String s;
                String LRtoken[] = null;

                while ((s = br.readLine()) != null)
                {
                    LRtoken = s.substring(1, s.length() - 2).split(",");
                    LRtoken[1] = LRtoken[1].substring(1); //tokens [1~] : 추천 북코드 담고있음.
                    for(int i=2; i< LRtoken.length;i++)
                    {
                        LRtoken[i] = LRtoken[i].trim(); // 공백제거 함수
                    }

                    if(code.equals(LRtoken[0])){
                        for(int p = 1 ; p< LRtoken.length;p++)
                            recBookList.add(LRtoken[p]);

                        flag = true;
                        break;
                    }
                }
                if(flag)
                    break;
            }
            BufferedReader bn = new BufferedReader(new FileReader("/Users/jykang/Bigdata/newBookList.txt"));

            String n =null;
            String nTokens[];
            while((n=bn.readLine())!=null)
            {
                nTokens = n.split("\t");
                for (String aRecBookList : recBookList) {
                    if (aRecBookList.equals(nTokens[4])) {
                        out.print("<tr>");
                        for(int k=0; k<4;k++){
                            out.print("<td>" + nTokens[k] +"</td>");
                        }
                        out.print("</tr>");
                    }
                }
            }



        }
        else
            out.println("??");
    %>



</table>
</body>
</html>
