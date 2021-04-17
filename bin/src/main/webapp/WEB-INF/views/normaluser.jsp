<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome normaluser</title>
</head>
<body>
Welcome normaluser  <br/>
<sec:authorize access="isAuthenticated()">
    authenticated as <sec:authentication property="principal.username" /> 
</sec:authorize>
</body>
</html>