<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>企業會員</title>
</head>
<body>
<div align='center'>
<h3>會員資料</h3>
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
<!-- 			   <th width='64'>企業等級</th> -->
			   <th colspan='2' width='72'>資料維護</th>
			</tr>
			<c:forEach var='companys' items='${companys}'>
				<tr>
					<td>${companys.companyDetailId}</td>
					<td>${companys.userphoto}</td>
					<td>${companys.realname}</td>
					<td>${companys.phonenumber}</td>
					<td style="text-align:left">${companys.location}</td>
<%-- 					<td style="text-align:center">${companys.level}</td> --%>
					<td>
						<a href="<c:url value='/' />updateCom/${companys.companyDetailId}">修改</a>
                    </td>
                    <td>
                    	<a class='deletelink' href="<c:url value='/' />_02_member/mem/${companys.companyDetailId}">刪除</a>
                    </td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
<hr>
</div>
<script type='text/javascript'>
    $(document).ready(function() {
        $('.deletelink').click(function() {
        	if (confirm('確定刪除此筆紀錄? ')) {
        		var href = $(this).attr('href');
                $('form').attr('action', href).submit();
        	} 
        	return false;
            
        });
    })
</script>
</body>
</html>