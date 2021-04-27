<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<!-- <link rel='stylesheet' -->
<%-- 	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" /> --%>
<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />
		<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>
	<script src="<c:url value='/scripts/popper.min.js' />"></script>
<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js' />"></script>
<title>forgotPassword</title>
<style>

.header {
	background-color: #000000;
	
}
#body{
	height:100vh;
	 background-image: url(
	 "https://images.unsplash.com/photo-1505935428862-770b6f24f629?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1047&q=80"
    );
    background-size: cover;
    background-position: center center;
    background-attachment: fixed;
}
#main{
	height:100vh;
}
</style>

</head>


<body id="body">
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

	<%@include file="includePage/includeNav.jsp" %>
<div>
	<div id="main" class="container"> <!-- style="border: 1px solid red"> -->
		<div class="row justify-content-center align-items-center pt-100">
			<div id="firstdiv"
				class="search-box align-items-center">
				<span>請輸入您的註冊Email :</span>
				<div class="mt-10 col-36 ">
					<input type="text" id="username" name="username" placeholder="E-mail:" required
						class="single-input">
				</div>
				<div class="mt-10 ">
					<button id="verificationbutton" type="submit" value="login"
						class="genric-btn danger radius">送出</button>

				</div>
			</div>
			<form id="secondform" style="display:none" action="<c:url value='/login/page'/>" method="post"
				class="search-box align-items-center">
				<span>請輸新密碼並登入:</span>
				<div class="mt-10 col-36 ">
					<input id="newusername" type="text" name="username" placeholder="E-mail:" required
						class="single-input">
				</div>
				<div class="mt-10 col-24">
					<input type="password" name="password" placeholder="Password:" required
						class="single-input ">
				</div>
				<div class="mt-10 ">
					<button type="submit" value="login"
						class="genric-btn danger radius">Login</button>

				</div>
			</form>
			
		</div>
	</div>
	<%@include file="includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>
</div>
	<script>
// 		$(document).ready(function(){ 
			
			
// 		}
// 		var $j = jQuery.noConflict();

		$("#verificationbutton").on('click',function(){
			$("#firstdiv").hide();
			$("#secondform").show();
			var dataString = $("#username").val();
			$("#newusername").val(dataString);
			
			var data={
					"text": dataString,
			}
			
			var urls = "/PepperNoodles/sendVerificationPassword"
			$.ajax({
				type:"POST",
				data:dataString,
				contentType:'text/plain',
				url: urls ,
				success: function (result) {
					alert(result.verificationpassword);

				},
			error: function (thrownError) {
					alert("fail");
				}
			});

			
			
		});
		
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

</body>
</html>