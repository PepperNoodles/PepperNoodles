<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>企業會員</title>
<style>
body{
	background-color: 	#FFFAF4;
}
</style>
</head>
<body>
<div align='center'>
<h3>企業會員資料</h3>
<hr>
<form method='POST'>
	<input type='hidden' name='_method' value='DELETE'>
</form>

<c:choose>
	<c:when test="${empty companys}">
	    沒有任何企業會員<br> 
	</c:when>
	<c:otherwise>
		<table border='1' cellpadding="3" cellspacing="1"  style="text-align:center">
			<tr>
			   <th width='70'>會員編號</th>
			   <th width='150'>企業照片</th>
			   <th width='140'>企業名稱</th>
			   <th width='180'>連絡電話</th>
			   <th width='300'>公司地址</th>
			   <th width='130'>權限狀況</th>
<!-- 			   <th width='64'>企業等級</th> -->
			   <th colspan='2' width='72'>資料維護</th>
			</tr>
			<c:forEach var='companys' items='${companys}'>
				<tr>
					<td>${companys.companyDetailId}</td>
					<td><img width='120' src="<c:url value="/getComPicture/${companys.companyDetailId}"/>" /></td>
					<td>${companys.realname}</td>
					<td>${companys.phonenumber}</td>
					<td style="text-align:left">${companys.location}</td>
					<td>
					<c:choose>
						<c:when test="${companys.userAccount.enabled == true}">
							開啟
						</c:when>
						<c:otherwise>
							關閉
						</c:otherwise>
					</c:choose>
					</td>
<%-- 					<td>${companys.userAccount.enabled}</td> --%>
<%-- 					<td style="text-align:center">${companys.level}</td> --%>
					<td>
						<a href="<c:url value='/updateCom/${companys.companyDetailId}' />">修改企業</a>
                    </td>
                    <td>
                    	<a class='enabled' id="test" href="<c:url value='/enabled/${companys.userAccount.accountIndex}' />">權限異動</a>
                    </td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
<hr>
</div>
</body>
</html>