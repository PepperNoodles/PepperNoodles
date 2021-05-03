<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改店家基本資料</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
<!--     Fonts and icons     -->
<!-- <link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet"> -->
<!-- <script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script> -->
<!-- CSS Files -->
<%-- <script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script> --%>
<!-- 有她左邊的BAR會變小 -->
<%-- <link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" /> --%>
<%-- <link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" /> --%>

<style>
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
</style>
</head>
<body>
<%@include file="../includePage/includeNav.jsp" %>
	
<div class="container-fluid">
	<div class="row">
    <!-- 左邊的Bar -->
    	<div class="col-lg-2 nopadding">
      		<br>
        	<div class="list-group">
        		<%@include file="../company/left.jsp" %>
        	</div>
        </div>
        
		<div class="col-lg-10 nopadding " >
			<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/login/noodles.jpg"/>)">
				<%@include file="RestaurantMessage.jsp" %>
			</div>
		<!--  big container -->
		</div>
	</div>
</div>
<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

	<script>
 		$(window).on('load', function() {
			
// 			//讓bar固定在上面以及設定高度
			$(".header-sticky").addClass("sticky-bar");
 			$(".header-sticky").css("height", "90px");
			$(".header-sticky").css("position","static")

 			//讓loading圖動起來
 			$('#preloader-active').delay(450).fadeOut('slow');
 			$('body').delay(450).css({
 				'overflow' : 'visible'
 		});			
			
 		});
 	</script>
	<!-- JS here -->
</body>
<!--   Core JS Files   -->
<script src="<c:url value='/scripts/jquery.bootstrap.wizard.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value="/scripts/bootstrap2.min.js"/>"></script>
<!--  Plugin for the Wizard -->
<script src="<c:url value='/scripts/gsdk-bootstrap-wizard.js'/>"></script>

</html>