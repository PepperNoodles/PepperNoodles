<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Logout</title>
</head>
<body>
<form action="<c:url value='/logout'/>" method="post">
   <table>
      <tr>
         <td>Logout</td>
         <td><button type="submit" value="logout">Logout</button></td>
      </tr>
   </table>
</form>
</body>
</html>