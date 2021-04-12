<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form" %>

<html>
<head>
<style type="text/css">
   span.error {
	color: red;
	display: inline-block;
	font-size: 5pt;
}
</style>
<meta charset="UTF-8">
</head>
<body>
<fieldset>
	<legend >更新會員資料(Company)</legend>
	<form:form method="POST" modelAttribute="comDetail" enctype='multipart/form-data'>
	<Table style="align-self: center;"  >
	<c:if test='${comDetail.companyDetailId == null}'>
		<br>
	 	<tr>
	      <td>帳號：<br>&nbsp;</td>
	   	  <td width='360'>
	   	  	<form:input path='realname'/><br>&nbsp;
	   	   	<form:errors path="realname" cssClass="error"/>
	   	  </td>
	   </tr>
    </c:if>	   
    	<c:if test='${comDetail.companyDetailId != null}'>
    	<br>
	 	<tr>
	      <td>企業等級：<br>&nbsp;</td>
	   	  <td>
	   	  	<form:hidden path='level'/>${comDetail.level}<br>&nbsp;
	   	  </td>
	   	</tr>
	   	<tr>
	   	  <td>會員帳號(E-mail)：<br>&nbsp;</td>
	   	  <td>
	   	  	<form:hidden path='userAccount.accountIndex'/>${comDetail.userAccount.accountIndex}<br>&nbsp;
	   	  </td>
	   </tr>
    </c:if>	
    	<tr>
	      <td>企業名稱：<br>&nbsp;</td>
		  <td  width='360'>
		  	<form:input path='realname' /><br>&nbsp;	
		    <form:errors path='realname' cssClass="error"/>
		  </td>
		  <td>連絡電話:<br>&nbsp;</td>
	      <td  width='360'>
	      	<form:input path="phonenumber"/><br>&nbsp;	
		    <form:errors path='phonenumber' cssClass="error"/>
		  </td>
	   </tr>
	   <tr>
	      <td>地址：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="location"/><br>&nbsp;	
		    <form:errors path='location' cssClass="error"/>
		  </td>
		  <td>照片：<br>&nbsp;</td>
	   	  <td>
	   	  	 <form:input path="userphoto" type='file'/><br>&nbsp;
	   	  	 <form:errors path="userphoto"  cssClass="error" />
	   	  </td>
	   </tr>	  
	   <tr>
	   <td colspan='4' align='center'><br>&nbsp;
	   	<input type='submit'>
       </td>
	   </tr>
	</Table>
	</form:form>
	
</fieldset>
<br>
<div align="center">
</div>
</body>
</html>