<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登入</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/slicknav.css' />">
<link rel="stylesheet" href="<c:url value='/css/flaticon.css' />">
<link rel="stylesheet" href="<c:url value='/css/animate.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />">
<link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />">
<link rel="stylesheet" href="<c:url value='/css/slick.css' />">
<link rel="stylesheet" href="<c:url value='/css/nice-select.css' />">
<style>
	li a:hover{
		color:#FFFFFF;
		background-color:#BEBEBE;
	}


</style>

</head>
<body>

<ul class="nav flex-column nav-justified">
  <li class="nav-item">
    <a class="nav-link" href="<c:url value='/' />Company/company/${comDetail.companyDetailId}">基本資料</a>
    
  </li>
  <li class="nav-item">
    <a class="nav-link"  href="#">我的餐廳</a>
   
  </li>
  <li class="nav-item">
    <a class="nav-link" href="#">活動資訊</a>
  </li>
  <li class="nav-item">
    <a class="nav-link " href="#">評論專區</a>
  </li>
  <li class="nav-item">
    <a class="nav-link " href="#">效益分析</a>
  </li>
</ul>

<!--  
<script>
	$(".li").click(function(){
		
	});

</script>
-->
		
</body>
</html>