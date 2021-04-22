<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome companyuser</title>

<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
</head>
<body>
<div align="center">
Welcome companyuser<br/>
</div>
<div id="time">2</div>
<script>
$(window).on('load', function () {
	let urls="${pageContext.request.contextPath}/";
	urls+="<c:url value='Company/login/company'/>";
	console.log(urls);
	
	$.ajax({
		type: "GET",
		url: urls,				
		dataType: "text",
		success: function (response) {
			console.log(response);	
			$(function () {
				setInterval(ChangeTime, 500);
				});		
		},
		error: function (thrownError) {
			console.log(thrownError);
		}
	});

	
	
	function ChangeTime() {
		var time;
		time = $("#time").text();
		time = parseInt(time);
		time--;
		if (time <= 0) {
		window.location.href = "${pageContext.request.contextPath}/Company/company";
		} else {
		$("#time").text(time);
		}
		}
});
</script>

</body>
</html>