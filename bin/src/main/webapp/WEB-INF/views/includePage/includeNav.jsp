<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>includeNav</title>
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
</head>

<body>
<!-- 最上層bar -->
	<header>
		<!-- Header Start -->
		<!-- 覆蓋用 -->
		<!--  <div style="height: 90px"></div>-->

		<div class="header-area header ">
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
											<li><a href="index.html">Home111</a></li>
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
	
	
		</script>

	<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>

	<!-- Jquery, Popper, Bootstrap -->
		<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>

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
		<script src="<c:url value='/scripts/animated.headline.js' />"></script>
		<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script>
	<!--  Nice-select, sticky-->
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