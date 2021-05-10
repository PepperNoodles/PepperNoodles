<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>UserLogin</title>
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
<link rel="stylesheet"
	href="<c:url value='/css/owl.carousel.min.css' />">
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
<!-- 	<script src="https://www.google.com/recaptcha/api.js" ></script> -->
		<div class="row justify-content-center align-items-center pt-100">
			<form id="loginForm" action="<c:url value='/login/page'/>" method="post"
				class="search-box align-items-center">
				<div class="mt-10 col-36 ">
					<input id="username" type="text" name="username" placeholder="E-mail:" required
						class="single-input">
				</div>
				<div class="mt-10 col-24">
					<input id="password" type="password" name="password" placeholder="Password:" required
						class="single-input ">
				</div>

				<div class="mt-10 col-24">
					RememberMe&nbsp;&nbsp;<input type="checkbox" name="remember-me">
					<br><a href="<c:url value='/forgotPassword'/>" ><span style="color:blue; hover:background-color: blue;">Forgot Password?</span></a>
				</div>
				<div class="mt-10 ">
					<button type="submit" id="loginButton" value="login"
						class="genric-btn danger radius">Login</button>

<!-- 					<button type="submit" id="loginButton" value="login" -->
<!-- 						class="g-recaptcha genric-btn danger radius" -->
<!-- 						 data-sitekey="6LeiSMcaAAAAAEC4fWA0-CNm2mxSqJxKpfg6A0KP" -->
<!-- 						 data-callback="onSubmit" -->
<!-- 						 data-action="submit">Login</button> -->
					<br><br><br>
					<div class="pull-right">
						<a href="#" id="user">會員一鍵登入</a>&emsp;&emsp;&emsp;
						<a href="#"  id="company">企業一鍵登入</a>
					</div>
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
<script type="text/javascript">
function onSubmit(token){
	document.getElementById("loginForm").submit();
}
</script>
	<script>
		$(window).on('load', function() {

			$(".header-sticky").css("height", "90px");

			$('#preloader-active').delay(450).fadeOut('slow');
			$('body').delay(450).css({
				'overflow' : 'visible'
			});
			
			//一鍵新增-企業
			$("#company").click(function(){
				$("#password").val('123!Q123');
				$("#username").val('ting0420a@gmail.com');
			});
			//一鍵新增-會員
			$("#user").click(function(){
				$("#password").val('a123456@');
				$("#username").val('chrismeow5311@gmail.com');
			});
			
			$('form').submit(function(ev){
				 ev.preventDefault();
				 let username = $("#username").val();
				// console.log(username);
				 let urls = "<c:url value='/userPreLoggin/getName'/>";
				 urls +="?username="+username;
				 $.ajax({
						type: "GET",
						url: urls,				
						dataType: "text",
						success: function (response) {
							console.log(response);	
							if (response == 'true'){
								//console.log('ok');	
								$('form').unbind('submit').submit()
							}
							$('form').unbind('submit').submit()
						},
						error: function (thrownError) {
							console.log(thrownError);
						}
					});
				 
		         //later you decide you want to submit
		         //			
			})
			
			
		});
	</script>

</body>
</html>