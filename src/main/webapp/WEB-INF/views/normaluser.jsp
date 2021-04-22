<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<<<<<<< HEAD
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
=======
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome normaluser</title>
<<<<<<< HEAD
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0'name='viewport' />
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css"	rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" />
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />
<script >
</script>
=======
<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git
</head>
<body>
Welcome normaluser  <br/>
<sec:authorize access="isAuthenticated()">
<<<<<<< HEAD
authenticated as <span id="authvalue"><sec:authentication  property="principal.username" /> </span>
</sec:authorize> <br/>

<form action="<c:url value='/user/loginMVC'/>" method="post">
<button id="nextPage"  >to personal page</button>
</form>
=======
    authenticated as <sec:authentication property="principal.username" /> 
</sec:authorize>
<div id="time">2</div>

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
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

</body>
</html>