<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View User</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet'
	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript"
	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>

<style>
	#body{
	height: 100vh;
    background-image: url(
    "https://images.unsplash.com/photo-1495195134817-aeb325a55b65?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1055&q=80"
    );
    background-size: cover;
    background-position: center center;
    background-attachment: fixed;
	}
	#main{
	background-color:#FFFFFF;
	}
</style>
</head>
<body id="body">
 	<%@include file="../includePage/includeNav.jsp" %>
	<!-- 讀取圖案 -->
	<div id="preloader-active">
		<div
			class="preloader d-flex align-items-center justify-content-center">
			<div class="preloader-inner position-relative">
				<div class="preloader-circle"
					style="background-color: rgb(102, 102, 102);"></div>
				<div class="preloader-img pere-text">
					<img src="<c:url value="/images/logo/peppernoodle.png"/>" alt="">
				</div>
			</div>
		</div>
	</div>

	<div id="main" class="container mt-10" style="width:50%">
			<h1>我是被看的喔,現在登入的 ${userAccount.accountIndex}</h1>
			<!-- 圖片+姓名bar-->
			<div class="d-flex">
				<div class="p-2 bg-info">
					
					<img style="height: 100px" src="<c:url value='/userProtrait/${viewUserAccountDetail.useretailId}'/>">
				</div>

				<div id="nickname" class="p-2 flex-fill align-self-end justify-content-start">
					<h1>${viewUserAccountDetail.nickName}</h1>
				</div>

				<div id="" class="align-self-start justify-content-start">
					<button style="color:#4a4aFF" id="AddFriendChecker">加好友</button>
				</div>
				

			</div>
			
			<div class="d-flex">
				<div class="p-2  flex-fill bg-dark">
					<a href="#"><i class="fas fa-users"></i>好友</a>		
				</div>
				<div class="p-2  flex-fill bg-dark">
					<a href="#"><i class="fas fa-file-alt"></i>關於我</a>							
				</div>
				<div class="p-2  flex-fill bg-dark">
					<a href="#"><i class="fas fa-comments"></i>留言區</a>							
				</div>					
				<div class="p-2  flex-fill bg-dark">
					<a href="#"><i class="fas fa-heart"></i>收藏區</a>								
				</div>	
			</div>
			
			<div class="mt-5" id="basicInfo">
				<h2>基本資料</h2>
				<p>email: ${viewUserAccount.accountIndex} </p>
				<p>性別：${viewUserAccountDetail.gender}</p>
				<p>地區：${viewUserAccountDetail.location}</p>
			</div>	

			<div class="" id="friend">
				<h2>好友區</h2>			
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
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>
	<script src="<c:url value='/scripts/popper.min.js' />"></script>


</body>
</html>