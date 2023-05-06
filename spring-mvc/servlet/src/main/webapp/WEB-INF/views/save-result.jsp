<%--<%@ page import="com.biggy.servlet.domain.member.Member" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
성공
<ul>
<%--  <li>id = <%=((Member) request.getAttribute("member")).getId()%></li>--%>
<%--  <li>username = <%=((Member) request.getAttribute("member")).getUsername()%></li>--%>
<%--  <li>age = <%=((Member) request.getAttribute("member")).getAge()%></li>--%>

  <!-- JSP에서 제공해주는 표현식을 사용하면 훨씬 간결하게 적을 수 있다. (프로퍼티 접근법) -->
  <li>id = ${member.id}</li>
  <li>username = ${member.username}</li>
  <li>age = ${member.age}</li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
