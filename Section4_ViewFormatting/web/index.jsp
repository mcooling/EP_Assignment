<%--
  Created by IntelliJ IDEA.
  User: mcooling
  Date: 20/02/2021
  Time: 23:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Hello World</title>
  </head>
  <body>
    <h1>Hello World</h1>
    <tr>
      <td><a href='${pageContext.request.contextPath}/GetAllFilms'>Get All Films Servlet</a></td>
      <br/>
      <td><a href='${pageContext.request.contextPath}/GetFilms'>Get Films Servlet</a></td>
    </tr>
  </body>
</html>
