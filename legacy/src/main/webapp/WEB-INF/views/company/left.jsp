<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登入</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
/* body { */
/*     background-color:#ECF5FF; */
/* } */
/* .nav { */
/*     background-color:#ECF5FF; */
/* } */
.nav-item .item:hover{
	color:#46A3FF;
	background-color:#E0E0E0;
}
.nav-item .item{
	font-size:20px;
}
.leftBar{
	color:#000000;
	background-color:#FFFFFF;
}

h3{
	text-align: center;
}

.nav-item .restEvent , .nav-item .messageByRest , .nav-item .restMenu{
 	display:none; 
}
.nav-item .rest {
/* 	background-color: #DDDDDD; */
	font-size:15px;
	color: #4F4F4F;
}
.nav-item .rest:hover{
	color:#1c91ff;
 	background-color:#F0F0F0; 
}

</style>

</head>
<body>
 <h3 class="container">${comDetail.realname}</h3>
 <br>
<ul class="nav flex-column mx-5">
  <li class="nav-item">
    <a class="nav-link leftBar item" href="<c:url value='/' />Company/company/">
    	<i class="fas fa-home"></i> 基本資料
    </a>
    
  </li>
  <li class="nav-item">
    <a class="nav-link leftBar item"  href="<c:url value='/showAllrestByComId/${comDetail.companyDetailId}' />">
    	<i class="fas fa-utensils"></i> 我的餐廳
    </a>
  </li>
  
  <li class="nav-item">
  <ul>
  		<li>
		    <a class="nav-link leftBar addMenu item" href="#">
		    	<i class="fab fa-leanpub"></i> 新增菜單
		    </a>
  		</li>
  	</ul>
    <ul class="list-group restMenu">
	  <c:choose>
	  	<c:when test="${empty rests}">
	  		<li class="nav-item">
    			<a class="nav-link leftBar rest">&emsp;尚未有餐廳</a>
  			</li>
	  	</c:when>
	  	<c:otherwise>
	  		<c:forEach var='rests' items='${rests}'>
  				<li class="nav-item">
    				<a class="nav-link leftBar rest" href="<c:url value='/rest/menu/${rests.restaurantId}' />">
    					&emsp;<i class="fas fa-utensil-spoon"></i> ${rests.restaurantName}
    				</a>
  				</li>	
	  		</c:forEach>
	  	</c:otherwise>
	  </c:choose>
	</ul>
  </li>
  

  <li class="nav-item">
  	<ul>
  		<li>
		    <a class="nav-link leftBar addRestEvent item" href="#">
		  	  <i class="far fa-calendar-alt"></i> 新增活動
		    </a>
  		</li>
  	</ul>
    <ul class="list-group restEvent">
	  <c:choose>
	  	<c:when test="${empty rests}">
	  		<li class="nav-item">
    			<a class="nav-link leftBar rest">&emsp;尚未有餐廳</a>
  			</li>
	  	</c:when>
	  	<c:otherwise>
	  		<c:forEach var='rests' items='${rests}'>
  				<li class="nav-item">
    				<a class="nav-link leftBar rest" href="<c:url value='/event/${rests.restaurantId}' />"> 
    					&emsp;<i class="fas fa-utensil-spoon"></i> ${rests.restaurantName}
    				</a>
  				</li>	
	  		</c:forEach>
	  	</c:otherwise>
	  </c:choose>
	</ul>
  </li>
  
  <li class="nav-item">
  <ul>
  		<li>
		    <a class="nav-link leftBar restMessage item" href="#">
		    	<i class="far fa-comments"></i> 餐廳評論
		    </a>
  		</li>
  	</ul>
    <ul class="list-group messageByRest">
	  <c:choose>
	  	<c:when test="${empty rests}">
	  		<li class="nav-item">
    			<a class="nav-link leftBar rest">&emsp;尚未有餐廳</a>
  			</li>
	  	</c:when>
	  	<c:otherwise>
	  		<c:forEach var='rests' items='${rests}'>
  				<li class="nav-item">
    				<a class="nav-link leftBar rest" href="<c:url value='/restPage/${rests.restaurantId}' />">
    					&emsp;<i class="fas fa-utensil-spoon"></i>  ${rests.restaurantName}
    				</a>
  				</li>	
	  		</c:forEach>
	  	</c:otherwise>
	  </c:choose>
	</ul>
  </li>

  
</ul>


<script>
$(document).ready(function(){
	
	$(".addMenu").click(function(){
		$(".restEvent").slideUp("slow");
		$(".messageByRest").slideUp("slow");
		$(".restMenu").slideToggle("slow");
	});
	$(".addRestEvent").click(function(){
		$(".restMenu").slideUp("slow");
		$(".messageByRest").slideUp("slow");
		$(".restEvent").slideToggle("slow");
	});
	$(".restMessage").click(function(){
		$(".restMenu").slideUp("slow");
		$(".restEvent").slideUp("slow");
		$(".messageByRest").slideToggle("slow");
	});
	
	
});

</script>
	
</body>
</html>