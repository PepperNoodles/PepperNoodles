<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登入</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%-- 	<script src="<c:url value='/webjars/jquery/3.5.1/jquery.js'/>"></script> --%>
<!-- site.webmanifest run offline -->
<!-- <link rel="manifest" href="site.webmanifest"> -->
<!-- favicon的圖-每頁都要加 -->
<%-- <link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />"> --%>
<%-- <link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" /> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" /> --%>
<%-- <script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script> --%>
<%-- <script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/owl.carousel.min.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/slicknav.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/flaticon.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/animate.min.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/slick.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/nice-select.css' />"> --%>
<style>
body {
    background-color:#ECF5FF;
}
.nav {
    background-color:#ECF5FF;
}
.nav-item a:hover{
	background-color:#BEBEBE;
}
.leftBar{
	color:#000000;
	background-color:#FFFFFF;
}

h3{
	text-align: center;
}

.nav-item .restEvent , .nav-item .messageByRest{
 	display:none; 
}
.nav-item .rest {
	background-color: #DDDDDD;
	font-size:15px;
}

</style>

</head>
<body>
 <h3 class="container">${comDetail.realname}</h3>
<ul class="nav flex-column nav-justified">
  <li class="nav-item">
    <a class="nav-link leftBar" href="<c:url value='/' />Company/company/">基本資料</a>
    
  </li>
  <li class="nav-item">
    <a class="nav-link leftBar"  href="<c:url value='/showAllrestByComId/${comDetail.companyDetailId}' />">我的餐廳</a>
  </li>

  <li class="nav-item">
  	<ul>
  		<li>
		    <a class="nav-link leftBar addRestEvent" href="#">新增餐廳活動</a>
  		</li>
  	</ul>
    <ul class="list-group restEvent">
	  <c:choose>
	  	<c:when test="${empty rests}">
	  		<li class="nav-item">
    			<a class="nav-link leftBar rest">尚未有餐廳</a>
  			</li>
	  	</c:when>
	  	<c:otherwise>
	  		<c:forEach var='rests' items='${rests}'>
  				<li class="nav-item">
    				<a class="nav-link leftBar rest" href="<c:url value='/event/${rests.restaurantId}' />"> → ${rests.restaurantName}</a>
  				</li>	
	  		</c:forEach>
	  	</c:otherwise>
	  </c:choose>
	</ul>
  </li>
  
  <li class="nav-item">
    <a class="nav-link leftBar restMessage" href="#">餐廳評論</a>
    <ul class="list-group messageByRest">
	  <c:choose>
	  	<c:when test="${empty rests}">
	  		<li class="nav-item">
    			<a class="nav-link leftBar rest">尚未有餐廳</a>
  			</li>
	  	</c:when>
	  	<c:otherwise>
	  		<c:forEach var='rests' items='${rests}'>
  				<li class="nav-item">
    				<a class="nav-link leftBar rest" href="<c:url value='/restPage/${rests.restaurantId}' />"> → ${rests.restaurantName}</a>
  				</li>	
	  		</c:forEach>
	  	</c:otherwise>
	  </c:choose>
	</ul>
  </li>

  
</ul>


<script>
$(document).ready(function(){
	
	$(".addRestEvent").click(function(){
// 		$(".nav-item").find(".rest").slideToggle();
		$(".messageByRest").slideUp("slow");
		$(".restEvent").slideToggle("slow");
	});
	$(".restMessage").click(function(){
		$(".restEvent").slideUp("slow");
		$(".messageByRest").slideToggle("slow");
	});
	
	
	
});

</script>
	
</body>
</html>