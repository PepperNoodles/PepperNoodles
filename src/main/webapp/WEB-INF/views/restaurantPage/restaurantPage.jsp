<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ViewRestaurant</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<!-- <link rel="manifest" href="site.webmanifest"> -->
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />

<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>

<style>
img{
	margin: 10px;
}
#wholeBody{
	height: 100%;
	background-image: url("https://images.unsplash.com/photo-1531685250784-7569952593d2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1267&q=80");
	background-size: cover;
	background-position: center center;
	background-attachment: fixed;
}
#body{
	background-color:#FFFFFF; 
}
#menuArea{
	display: none;
}
hr {
	 border: 0;
	 clear:both;
	 display:block;
	 width: 96%;               
	 background-color:#f7f7f7;
	 height: 1px;
}
</style>
</head>
<body>
<%@include file="../includePage/includeNav.jsp" %>
<!-- 讀取圖案 -->
<div id="preloader-active">
	<div class="preloader d-flex align-items-center justify-content-center">
		<div class="preloader-inner position-relative">
			<div class="preloader-circle" style="background-color: rgb(102, 102, 102);"></div>
			<div class="preloader-img pere-text">
				<img src="<c:url value="/images/logo/peppernoodle.png"/>" alt="">
			</div>
		</div>
	</div>
</div>

<div>
<%@include file="RestaurantMessage.jsp" %>
</div>



<%@include file="../includePage/includeFooter.jsp" %>
<!-- Scroll Up -->
<div id="back-top">
	<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>


</body>
</html>