<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Map</title>
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
<link rel="stylesheet" href="<c:url value='/css/slicknav.css' />">
<link rel="stylesheet" href="<c:url value='/css/flaticon.css' />">
<link rel="stylesheet" href="<c:url value='/css/animate.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />">
<link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />">
<link rel="stylesheet" href="<c:url value='/css/slick.css' />">
<link rel="stylesheet" href="<c:url value='/css/nice-select.css' />">

<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<style>
.header {
	background-color: #000000;
}
</style>
<script>





</script>
</head>
<body>
	<!-- 讀取圖案 -->
	<div id="preloader-active">
		<div
			class="preloader d-flex align-items-center justify-content-center">
			<div class="preloader-inner position-relative">
				<div class="preloader-circle"
					style="background-color: rgb(102, 102, 102);"></div>
				<div class="preloader-img pere-text">
					<img id="userProtrait" src="<c:url value="/images/logo/peppernoodle.png"/>" alt="">
				</div>
			</div>
		</div>
	</div>

	<!-- 最上層bar -->
	<header>
		<!-- Header Start -->
		<!-- 覆蓋用 -->
		<div style="height: 90px"></div>

		<div class="header-area header">
			<!--  header-transparent -->
			<div class="main-header sticky-top">
				<div class="header-bottom header-sticky">
					<div class="container-fluid">
						<div class="row align-items-center">
							<!-- Logo -->
							<div class="col-xl-2 col-lg-2 col-md-1">
								<div class="logo">
									<a href="/PepperNoodles"><img style="height: 80px"
										src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""></a>
								</div>
							</div>
							<div class="col-xl-10 col-lg-10 col-md-8">
								<!-- Main-menu -->
								<div class="main-menu f-right d-none d-lg-block">
									<nav>
										<ul id="navigation">
											<li><a href="index.html">Home</a></li>
											<li><a href="about.html">About</a></li>
											<li><a href="#">城市</a>
												<ul class="submenu">
													<li><a href="blog.html">台北</a></li>
													<li><a href="blog_details.html">新北</a></li>
													<li><a href="elements.html">基隆</a></li>
													<li><a href="listing_details.html">桃園</a></li>
												</ul></li>
											<li><a href="#">美食</a>
												<ul class="submenu">
													<li><a href="blog.html">美式</a></li>
													<li><a href="blog_details.html">日式燒烤</a></li>
													<li><a href="elements.html">韓式</a></li>
													<li><a href="listing_details.html">炸物</a></li>
												</ul></li>
											<li><a href="#">排行榜</a>
												<ul class="submenu">
													<li><a href="blog.html">免費排行</a></li>
													<li><a href="blog_details.html">付費排行</a></li>
													<li><a href="elements.html">周排行</a></li>
													<li><a href="listing_details.html">綜合排行</a></li>
												</ul></li>
											<li><a href="about.html">論壇</a></li>
											<li><a href="#">最新消息</a>
												<ul class="submenu">
													<li><a href="blog.html">菜色新聞</a></li>
													<li><a href="blog_details.html">最新優惠</a></li>
													<li><a href="elements.html">新開幕</a></li>
												</ul></li>
											<li><a href="about.html">發表食記</a></li>
											<!-- <li><a href="contact.html">Contact</a></li> -->
											<!-- <li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->
											<li class="login"><a href="loginSystem/loginPage"> <i
													class="ti-user"></i> Sign in or Register
											</a></li>
										</ul>
									</nav>
								</div>
							</div>
							<!-- Mobile Menu -->
							<div class="col-12">
								<div class="mobile_menu d-block d-lg-none"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Header End -->
	</header>


	<div class="container mt-10" style="width:50%">
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



	<footer>
		<!-- Footer Start-->
		<div class="footer-area">
			<div class="container">
				<div class="footer-top footer-padding">
					<div class="row justify-content-between">
						<div class="col-xl-3 col-lg-4 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="single-footer-caption mb-30">
									<!-- logo -->
									<div class="footer-logo">
										<a href="index.html"><img
											src="<c:url value='/images/logo/peppernoodle.png'/>" alt=""></a>
									</div>
								</div>
							</div>
						</div>
						<div class="col-xl-2 col-lg-2 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Quick Link</h4>
									<ul>
										<li><a href="#">Home</a></li>
										<li><a href="#">Listing</a></li>
										<li><a href="#">About</a></li>
										<li><a href="#">Contact</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xl-2 col-lg-3 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Categories</h4>
									<ul>
										<li><a href="#">台北美食</a></li>
										<li><a href="#">熱門餐廳</a></li>
										<li><a href="#">點券優惠</a></li>
										<li><a href="#">每周排行</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xl-3 col-lg-3 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Download App</h4>
									<ul>
										<li class="app-log"><a href="#"><img
												src="<c:url value='/images/gallery/app-logo.png'/>" alt=""></a></li>
										<li><a href="#"><img
												src="<c:url value='/images/gallery/app-logo2.png'/>" alt=""></a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="footer-bottom">
					<div class="row d-flex justify-content-between align-items-center">
						<div class="col-xl-9 col-lg-8">
							<div class="footer-copy-right">
								<p>
									Copyright &copy;
									<script>
										document
												.write(new Date().getFullYear());
									</script>
									All rights reserved | U copy <i class="fa fa-heart"
										aria-hidden="true"></i> <a href="https://colorlib.com"
										target="_blank">U died</a>
								</p>
							</div>
						</div>
						<div class="col-xl-3 col-lg-4">
							<!-- Footer Social -->
							<div class="footer-social f-right">
								<a href="#"><i class="fab fa-facebook-f"></i></a> <a href="#"><i
									class="fab fa-instagram"></i></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Footer End-->
	</footer>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

	<script>
		$(window).on('load', function() {
			$(".header-sticky").addClass("sticky-bar");
			$(".header-sticky").css("height", "90px");
			//$(".header-sticky").css("position","static ")

			$('#preloader-active').delay(450).fadeOut('slow');
			$('body').delay(450).css({
				'overflow' : 'visible'
			});

			//利用ajax送出請求判斷是否可以加好友
			judgeRelation();

			function judgeRelation(){
				let mainUser = "${userAccount.accountIndex}";
				let viewUser = "${viewUserAccount.accountIndex}";
				let urls="${pageContext.request.contextPath}/";
					urls+="<c:url value='judgeFriendShip'/>";
					urls+="?userIndex="+mainUser+"&friendIndex="+viewUser;
					console.log(urls);
			$.ajax({
					type: "GET",
					url: urls,							
					dataType: "text",
					success: function (response) {		
					if (response =="noFriendShip"){
						$("#AddFriendChecker").html("addFriend");
						$("#AddFriendChecker").on("click",sendFriendRequest);
					}else if(response == "wait_check"){
						$("#AddFriendChecker").html("RequestSended");
					}else if(response =="friend"){
						$("#AddFriendChecker").html("Friend");
					}else if(response == "reject"){
						$("#AddFriendChecker").html("rejected");
					}else{
						console.log("somethingWrong")
					}
					},
						error: function (thrownError) {
						console.log(thrownError);
					}
	    			});
			}
			
			

			function sendFriendRequest(){
				alert("送出邀請");
				let mainUser = "${userAccount.accountIndex}";
				let viewUser = "${viewUserAccount.accountIndex}";
				let urls="${pageContext.request.contextPath}/";
					urls+="<c:url value='addfriend'/>";
					urls+="/"+mainUser+"/"+viewUser;
				$.ajax({
					type: "GET",
					url: urls,							
					dataType: "text",
					success: function (response) {		
						alert(response);
						judgeRelation();
					},
						error: function (thrownError) {
						console.log(thrownError);
					}
	    			});
			}
			

		});
	</script>

	<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>

	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>

		<script src="<c:url value='/scripts/popper.min.js' />"></script>

	<!-- 	<script type="text/javascript" -->
			src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
	<!-- Jquery Mobile Menu -->
		<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script>
	
	<!-- Jquery Slick , Owl-Carousel Plugins -->
<%-- 		<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script> --%>


<%-- 		<script src="<c:url value='/scripts/slick.min.js' />"></script> --%>

<!-- 	<!-- One Page, Animated-HeadLin --> -->
<%-- 		<script src="<c:url value='/scripts/wow.min.js' />"></script> --%>
<%-- 		<script src="<c:url value='/scripts/animated.headline.js' />"></script> --%>
<%-- 		<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script> --%>
<!-- 	<!-- Nice-select, sticky --> -->
<%-- 		<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script> --%>
<%-- 		<script src="<c:url value='/scripts/jquery.sticky.js' />"></script> --%>
<!-- 	<!-- contact js --> -->
<%-- 		<script src="<c:url value='/scripts/contact.js' />"></script> --%>

<%-- 		<script src="<c:url value='/scripts/jquery.form.js' />"></script> --%>
<%-- 		<script src="<c:url value='/scripts/jquery.validate.min.js' />"></script> --%>
<%-- 		<script src="<c:url value='/scripts/mail-script.js' />"></script> --%>
<%-- 		<script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script> --%>

<!-- 	<!-- Jquery Plugins, main Jquery --> -->
<%-- 	<script src="<c:url value='/scripts/plugins.js' />"></script> --%>
<!--		<script src="<c:url value='/scripts/main.js' />"></script> -->
</body>
</html>