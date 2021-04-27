<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>confirm Order And Invoice</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet'	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet"	href="<c:url value='/css/fontawesome-all.css' />" />
<link rel="stylesheet"	href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/slicknav.css' />">
<link rel="stylesheet" href="<c:url value='/css/flaticon.css' />">
<link rel="stylesheet" href="<c:url value='/css/animate.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />">
<link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />">
<link rel="stylesheet" href="<c:url value='/css/slick.css' />">
<link rel="stylesheet" href="<c:url value='/css/nice-select.css' />">
<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<link rel="stylesheet" href="<c:url value='/css/price_rangs.css' />">
</head>
<script type="text/javascript">
	$(document).ready(function(){
		
		$('body').on('click','#toquery',function(e){
			e.preventDefault();
			window.location.href = "http://localhost:433/PepperNoodles/user/getOrderList";
			
			
		});
	
	


	});
</script>
<style>


</style>
<body>
	<!-- Preloader Start -->
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
	<!-- Preloader Start -->
	<header>
		<!-- Header Start -->
		<div class="header-area header-transparent">
			<div class="main-header">
				<div class="header-bottom  ">
					<div class="container-fluid">
						<div class="row align-items-center">
							<!-- Logo -->
							<div class="col-xl-2 col-lg-2 col-md-1">
								<div class="logo">
									<a href="/PepperNoodles"><img
										src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""></a>
								</div>
							</div>
							<div class="col-xl-10 col-lg-10 col-md-8">
								<!-- Main-menu -->
								<div class="main-menu f-right d-none d-lg-block">
									<nav>
										<ul id="navigation">
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
											<li><a href="/PepperNoodles/shoppingSystem/ShoppingMall"
												id="shoppingMall">商城</a></li>
											<c:choose>
												<c:when test="${pageContext['request'].userPrincipal == null}"><li class="login">
												<a href="loginSystem/loginPage"> <i class="ti-user">												
												</i> Sign in or Register</a>
												<ul class="submenu">
													<li><a href="<c:url value='/loginSystem/loginPage'/>">註冊</a></li>
													<li><a href="<c:url value='/loginSystem/normaluser'/>">使用者登入</a></li>
													<li><a href="<c:url value='/loginSystem/companyuser'/>">企業登入</a></li>
												</ul>	
												</li></c:when>
												<c:otherwise><li class="login"><sec:authorize access="isAuthenticated()">
												<a href="personalPage/edit"><i class="ti-user"></i><sec:authentication   property="principal.username" /> </a></sec:authorize>
												<ul class="submenu">
													<li><a href="<c:url value='/user/login'/>">個人頁面</a></li>
													<li><a href="<c:url value='/logout/page'/>">登出</a></li>
												</ul>	
												</li></c:otherwise>
												
											</c:choose>
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
	<main>
		<!-- Hero Start-->
		<div class="hero-area3 hero-overly2 d-flex align-items-center "
			style="background-image:url(<c:url value="/images/hero/frechfries.jpg"/>);">
			<div class="container">
				<div class="row justify-content-center">
					<!--new section here -->
					<div class="col-xl-8 col-lg-9">
						<div class="hero-cap text-center pt-50 pb-20 ">
							<h3>Hope You Enjoy</h3>
							<h2>Checkout products</h2>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--Hero End -->
		<div class="listing-area pt-120 pb-120"></div>
	
		<div class="listing-area pt-120 pb-120">
			<div class="container">
				<div class="row">
					<div class="col-12">
						<h2><strong>付款成功</strong></h2>					
					</div>
				</div>
			</div>
			<div class="container">
				<div class="row">
					<div class="col-12">
						<div style="text-align: left;">
							<a href="#" class="genric-btn primary medium" id="toquery">查詢訂單</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		
		<div class="listing-area pt-120 pb-120"></div>
	
	</main>
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
									<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
									Copyright &copy;
									<script>
										document
												.write(new Date().getFullYear());
									</script>
									All rights reserved | This template is made with <i
										class="fa fa-heart" aria-hidden="true"></i> by <a
										href="https://colorlib.com" target="_blank">Colorlib</a>
									<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
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
	
	<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script type="text/javascript"	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
	<script type="text/javascript"	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/popper.min.js' />"></script>
	<script type="text/javascript"
		src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
	<!-- Jquery Mobile Menu -->
	<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script>
	<!-- Jquery Slick , Owl-Carousel Plugins -->
	<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script>
	<script src="<c:url value='/scripts/slick.min.js' />"></script>
	<!-- One Page, Animated-HeadLin -->
	<script src="<c:url value='/scripts/wow.min.js' />"></script>
	<script src="<c:url value='/scripts/price-range.js' />"></script>
	<script src="<c:url value='/scripts/animated.headline.js' />"></script>
	<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script>
	<!-- Nice-select, sticky -->
	<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script>
	<script src="<c:url value='/scripts/jquery.sticky.js' />"></script>
	<!-- contact js -->
	<script src="<c:url value='/scripts/contact.js' />"></script>
	<script src="<c:url value='/scripts/jquery.form.js' />"></script>
	<script src="<c:url value='/scripts/jquery.validate.min.js' />"></script>
	<script src="<c:url value='/scripts/mail-script.js' />"></script>
	<script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script>
	<!-- Jquery Plugins, main Jquery -->
	<script src="<c:url value='/scripts/plugins.js' />"></script>
	<script src="<c:url value='/scripts/main.js' />"></script>
</body>
</html>
	
	
	
	