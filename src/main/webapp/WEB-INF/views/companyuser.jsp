<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome companyuser</title>
</head>
<body>
<div align="center">
Welcome companyuser<br/>
</div>
<div id="time">3</div>
<script>
$(window).on('load', function () {
	let urls="${pageContext.request.contextPath}/";
	urls+="<c:url value='userLoggin/getName'/>";
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
		window.location.href = "${pageContext.request.contextPath}/user/login";
		} else {
		$("#time").text(time);
		}
		}
});
</script>

</body>
</html>