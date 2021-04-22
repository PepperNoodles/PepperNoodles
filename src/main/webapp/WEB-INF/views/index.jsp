<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to PepperNoodles</title>
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

</head>
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
				<div class="header-bottom  header-sticky">
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
<!-- 											<li><a href="index.html">Home</a></li> -->
<!-- 											<li><a href="about.html">About</a></li> -->
<!-- 											<li><a href="#">城市</a> -->
<!-- 												<ul class="submenu"> -->
<!-- 													<li><a href="blog.html">台北</a></li> -->
<!-- 													<li><a href="blog_details.html">新北</a></li> -->
<!-- 													<li><a href="elements.html">基隆</a></li> -->
<!-- 													<li><a href="listing_details.html">桃園</a></li> -->
<!-- 												</ul></li> -->
<!-- 											<li><a href="#">美食</a> -->
<!-- 												<ul class="submenu"> -->
<!-- 													<li><a href="blog.html">美式</a></li> -->
<!-- 													<li><a href="blog_details.html">日式燒烤</a></li> -->
<!-- 													<li><a href="elements.html">韓式</a></li> -->
<!-- 													<li><a href="listing_details.html">炸物</a></li> -->
<!-- 												</ul></li> -->
<!-- 											<li><a href="#">排行榜</a> -->
<!-- 												<ul class="submenu"> -->
<!-- 													<li><a href="blog.html">免費排行</a></li> -->
<!-- 													<li><a href="blog_details.html">付費排行</a></li> -->
<!-- 													<li><a href="elements.html">周排行</a></li> -->
<!-- 													<li><a href="listing_details.html">綜合排行</a></li> -->
<!-- 												</ul></li> -->
<!-- 											<li><a href="about.html">論壇</a></li> -->
<!-- 											<li><a href="#">最新消息</a> -->
<!-- 												<ul class="submenu"> -->
<!-- 													<li><a href="blog.html">菜色新聞</a></li> -->
<!-- 													<li><a href="blog_details.html">最新優惠</a></li> -->
<!-- 													<li><a href="elements.html">新開幕</a></li> -->
<%-- 													<li><a href="<c:url value='/loginSystem/normaluser'/>">normal --%>
<!-- 															user page</a></li> -->
<!-- 													<li><a -->
<%-- 														href="<c:url value='/loginSystem/companyuser'/>">company --%>
<!-- 															page</a></li> -->
<%-- 													<li><a href="<c:url value='/loginSystem/admin'/>">admin --%>
<!-- 															page</a></li> -->

<!-- 												</ul></li> -->
<!-- 											<li><a href="about.html">發表食記</a></li> -->
<!-- 											<li><a href="shoppingSystem/ShoppingMall">商城</a></li> -->
<!--                                             <li><a href="rearStage/rearStage">後台</a></li> -->
<!-- 											<li><a href="contact.html">Contact</a></li> -->
<!-- 											<li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->

<%-- 											<c:choose> --%>
<%-- 												<c:when test="${pageContext['request'].userPrincipal == null}"><li class="login"><a href="loginSystem/loginPage"> <i class="ti-user"></i> Sign in or Register</a></li></c:when> --%>
<%-- 												<c:otherwise><li class="login"><sec:authorize access="isAuthenticated()"><a href="personalPage/edit"><i class="ti-user"></i><sec:authentication   property="principal.username" /> </a></sec:authorize></li></c:otherwise> --%>
<%-- 											</c:choose> --%>
											<li><a href="#">最新消息</a>
												<ul class="submenu">
													<li><a href="blog.html">菜色新聞</a></li>
													<li><a href="blog_details.html">最新優惠</a></li>
													<li><a href="elements.html">新開幕</a></li>
													<li><a href="<c:url value='/loginSystem/normaluser'/>">normal
															user page</a></li>
													<li><a
														href="<c:url value='/loginSystem/companyuser'/>">company
															page</a></li>
													<li><a href="<c:url value='/loginSystem/admin'/>">admin
															page</a></li>

												</ul></li>
											<li><a href="about.html">發表食記</a></li>
											<li><a href="shoppingSystem/ShoppingMall">商城</a></li>
                                            <li><a href="rearStage/rearStage">後台</a></li>
											<!-- <li><a href="contact.html">Contact</a></li> -->
											<!-- <li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->

											<c:choose>
<<<<<<< HEAD
											
												<c:when test="${pageContext['request'].userPrincipal == null}"><li class="login"><a href="loginSystem/loginPage"> <i class="ti-user"></i> Sign in or Register</a></li></c:when>
												<c:otherwise><li><a href="#">個人資訊</a><ul class="submenu">
													<li class="login"><sec:authorize access="isAuthenticated()"><a href="personalPage/edit"><!-- <i class="ti-user"></i> --><sec:authentication   property="principal.username" /> </a></sec:authorize></li>
													<li><a href="blog.html">登出</a></li>
													</ul></li></c:otherwise>
											
=======
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
												
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git
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

		<!-- Hero Area Start-->
		<div class="slider-area hero-overly"
			style="background-image:url(<c:url value="/images/hero/frechfries.jpg"/>)">
			<div
				class="single-slider hero-overly  slider-height d-flex align-items-center">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-xl-8 col-lg-9">
							<!-- Hero Caption -->
							<div class="hero__caption">
								<span>Explore the Food</span>
								<h1>走吧!美食之旅!</h1>
							</div>
							<!--Hero form -->
							<form action="#" class="search-box">
								<div class="input-form">
									<input type="text" placeholder="今晚我想來點...">
								</div>
								<div class="select-form">
									<div class="select-itms">
										<select name="select" id="select1">
											<option value="">全部</option>
											<option value="">胡椒麵</option>
											<option value="">日式炸雞</option>
											<option value="">泰式料理</option>
											<option value="">燒仙草</option>
										</select>
									</div>
								</div>
								<div class="search-form">
									<a href="#">Search</a>
								</div>
							</form>
						</div>
					</div>
				</div>

			</div>
		</div>
		<!--Hero Area End-->
		<!-- Popular Locations Start -->
		<div class="popular-location section-padding30">
			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<!-- Section Tittle -->
						<div class="section-tittle text-center mb-80">
							<span>Most visited restaurants</span>
							<h2>熱門地點</h2>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-6 col-sm-6">
						<div class="single-location mb-30">
							<div class="location-img">
								<img src="<c:url value="/images/gallery/location1.jpg"/>" alt=""
									height="230px" style="overflow: hidden;">
							</div>
							<div class="location-details">
								<p>大安</p>
								<a href="#" class="location-btn">65 <i class="ti-plus"></i>
									地點
								</a>
							</div>
						</div>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-6">
						<div class="single-location mb-30">
							<div class="location-img">
								<img src="<c:url value="/images/gallery/location2.jpg"/>" alt=""
									height="230px" style="overflow: hidden;">
							</div>
							<div class="location-details">
								<p>信義</p>
								<a href="#" class="location-btn">60 <i class="ti-plus"></i>
									地點
								</a>
							</div>
						</div>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-6">
						<div class="single-location mb-30">
							<div class="location-img">
								<img src="<c:url value="/images/gallery/location3.jpg"/>" alt=""
									height="230px" style="overflow: hidden;">
							</div>
							<div class="location-details">
								<p>西門</p>
								<a href="#" class="location-btn">50 <i class="ti-plus"></i>
									地點
								</a>
							</div>
						</div>
					</div>
					<div class="col-lg-4 col-md-6 col-sm-6">
						<div class="single-location mb-30">
							<div class="location-img">
								<img src="<c:url value="/images/gallery/location4.jpg"/>" alt=""
									height="230px" style="overflow: hidden;">
							</div>
							<div class="location-details">
								<p>中山</p>
								<a href="#" class="location-btn">28 <i class="ti-plus"></i>
									地點
								</a>
							</div>
						</div>
					</div>

					<div class="col-lg-4 col-md-6 col-sm-6">
						<div class="single-location mb-30">
							<div class="location-img">
								<img src="<c:url value="/images/gallery/location5.jpg"/>" alt=""
									height="230px" style="overflow: hidden;">
							</div>
							<div class="location-details">
								<p>士林</p>
								<a href="#" class="location-btn">78 <i class="ti-plus"></i>
									地點
								</a>
							</div>
						</div>
					</div>

					<div class="col-lg-4 col-md-6 col-sm-6">
						<div class="single-location mb-30">
							<div class="location-img">
								<img src="<c:url value="/images/gallery/location6.jpg"/>" alt=""
									height="230px" style="overflow: hidden;">
							</div>
							<div class="location-details">
								<p>內湖</p>
								<a href="#" class="location-btn">78 <i class="ti-plus"></i>
									地點
								</a>
							</div>
						</div>
					</div>
				</div>
				<!-- More Btn -->
				<div class="row justify-content-center">
					<div class="room-btn pt-20">
						<a href="catagori.html" class="btn view-btn1">查看所有地點</a>
					</div>
				</div>
			</div>
		</div>
		<!-- Popular Locations End -->
		<!-- Services Area Start -->
		<div class="services-area pt-150 pb-150 section-bg"
			data-background="<c:url value="/images/gallery/section_bg02.jpg"/>"
			style="opacity: 0.8;">

			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<!-- Section Tittle -->
						<div class="section-tittle section-tittle2 text-center mb-80">
							<span>Easy to explore</span>
							<h2>如何搜尋</h2>
						</div>
					</div>
				</div>
				<div class="row justify-content-between">
					<div class="col-lg-3 col-md-6">
						<div class="single-services text-center mb-50">
							<div class="services-icon">
								<!--放小圖-->
								<span></span>
							</div>
							<div class="services-cap">
								<h5>
									<a href="#">1. 選擇喜歡的餐廳種類</a>
								</h5>
								<p>泰式 越式 美式 日式 中式 韓式 台式 我全都要!!!</p>
							</div>
							<!-- Shpape -->
							<img class="shape1 d-none d-lg-block"
								src="<c:url value='/images/icon/serices1.png'/>" alt="">
						</div>
					</div>
					<div class="col-lg-3 col-md-6">
						<div class="single-services text-center mb-50">
							<div class="services-icon">
								<!--放小圖-->
								<span></span>
							</div>
							<div class="services-cap">
								<h5>
									<a href="#">2. 選擇喜歡的地點</a>
								</h5>
								<p>大安 101 北車 中山 西門 美麗華 東區 吃遍全台北!!!</p>
							</div>
							<img class="shape2 d-none d-lg-block"
								src="<c:url value='/images/icon/serices2.png'/>" alt="">
						</div>
					</div>
					<div class="col-lg-3 col-md-6">
						<div class="single-services text-center mb-50">
							<div class="services-icon">
								<!--放小圖-->
								<span></span>
							</div>
							<div class="services-cap">
								<h5>
									<a href="#">3. 立馬ㄘㄨ花!!!享食趣</a>
								</h5>
								<p>約會、聚會、找餐廳不採雷</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Services Area End -->
		<!-- Categories Area Start -->
		<div class="categories-area section-padding30">
			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<!-- Section Tittle -->
						<div class="section-tittle text-center mb-80">
							<span>We are offering for you</span>
							<h2>美食優惠</h2>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-3 col-md-6 col-sm-6">
						<div class="single-cat text-center mb-50">
							<div class="cat-icon">
								<!-- <span class="flaticon-drink"></span>  -->
								<img src="<c:url value='/fonts/salmon.svg'/>">
								<!-- <span class="flaticon-salmon"></span>                               -->
							</div>
							<div class="cat-cap">
								<h5>
									<a href="catagori.html">鮭魚之夢</a>
								</h5>
								<p>姓名同音同字享「鮭魚就是我，我是鮭仙人」</p>
								<a href="catagori.html">查看詳情</a>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-6">
						<div class="single-cat text-center mb-50">
							<div class="cat-icon">
								<!-- <span class="flaticon-drink"></span> -->
								<img src="<c:url value='/fonts/bubble-tea.svg'/>">
							</div>
							<div class="cat-cap">
								<h5>
									<a href="catagori.html">QQㄋㄟㄋㄟ好喝到咩噗茶</a>
								</h5>
								<p>這裡沒有賣奶茶，不要肖想了!!!</p>
								<a href="catagori.html">查看詳情</a>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-6">
						<div class="single-cat text-center mb-50">
							<div class="cat-icon">
								<!-- <span class="flaticon-home"></span> -->
								<img src="<c:url value='/fonts/hot-pot.svg'/>">
							</div>
							<div class="cat-cap">
								<h5>
									<a href="catagori.html">一代一代一代</a>
								</h5>
								<p>快來蒐集全台北最好吃的火鍋，保證五星好評推薦!!!</p>
								<a href="catagori.html">查看詳情</a>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-6">
						<div class="single-cat text-center mb-50">
							<div class="cat-icon">
								<!-- <span class="flaticon-food"></span> -->
								<img src="<c:url value='/fonts/coupon.svg'/>">
							</div>
							<div class="cat-cap">
								<h5>
									<a href="catagori.html">優惠券</a>
								</h5>
								<p>0周年紀念，買十送一，不要拉倒</p>
								<a href="catagori.html">查看詳情</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Categories Area End -->
		<!-- peoples-visit Start -->
		<div class="peoples-visit dining-padding-top">
			<!-- Single Left img -->
			<div class="single-visit left-img">
				<div class="container">
					<div class="row justify-content-end">
						<div class="col-lg-8">
							<div class="visit-caption">
								<span>We are offering for you</span>
								<h3>每個月，數以萬計的人潮來灑胡椒麵，今天中午我想來點...</h3>
								<p>最真實的評論、最好吃的餐廳、最優惠的價格</p>
								<!--Single Visit categories -->
								<div class="visit-categories mb-40">
									<div class="visit-location">
										<span class="flaticon-travel"></span>
									</div>
									<div class="visit-cap">
										<h4>囊括全台北最好吃的餐廳</h4>
										<p>真的嗎?歐真的</p>
									</div>
								</div>
								<!--Single Visit categories -->
								<div class="visit-categories">
									<div class="visit-location">
										<span class="flaticon-work"></span>
									</div>
									<div class="visit-cap">
										<h4>最多分類可以選</h4>
										<p>蒸的好蚌蚌</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- peoples-visit End -->
		<!-- Testimonial Start -->
		<div class="testimonial-area testimonial-padding">
			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<!-- Section Tittle -->
						<div class="section-tittle text-center mb-80">
							<span>What our client say</span>
							<h2>客戶評論</h2>
						</div>
					</div>
				</div>
				<div class="row align-items-center">
					<div class="col-lg-11 col-md-11">
						<div class="h1-testimonial-active">
							<!-- Single Testimonial -->
							<div class="single-testimonial text-center">
								<!-- Testimonial Content -->
								<div class="testimonial-caption ">
									<div class="testimonial-top-cap">
										<p>今天早餐店，老闆娘忘記幫我加蛋，可是我多付了十塊，嗚嗚嗚嗚，新台幣的味道</p>
										<p>今天早餐店，老闆娘忘記幫我加蛋，可是我多付了十塊，嗚嗚嗚嗚，新台幣的味道</p>
									</div>
									<!-- founder -->
									<div
										class="testimonial-founder d-flex align-items-center justify-content-center mb-30">
										<div class="founder-img">
											<img
												src="<c:url value='/images/testmonial/Homepage_testi.png'/>"
												alt="" width="250px" height="250px">
										</div>
										<div class="founder-text">
											<span>Steve</span>
											<p>Grass Bag</p>
										</div>
									</div>
								</div>
							</div>
							<!-- Single Testimonial -->
							<div class="single-testimonial text-center">
								<!-- Testimonial Content -->
								<div class="testimonial-caption ">
									<div class="testimonial-top-cap">
										<p>Spring MVC 好多好煩歐</p>
										<p>Spring MVC 好多好煩歐</p>

									</div>
									<!-- founder -->
									<div
										class="testimonial-founder d-flex align-items-center justify-content-center mb-30">
										<div class="founder-img">
											<img
												src="<c:url value='/images/testmonial/Homepage_testi.png'/>"
												alt="" width="250px" height="250px">
										</div>
										<div class="founder-text">
											<span>Mike Oslong</span>
											<p>Java Designer</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Testimonial End -->
		<!-- Subscribe Area Start -->
		<!-- <div class="subscribe-area section-bg pt-150 pb-150" data-background="assets/img/gallery/section_bg04.jpg">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-xl-6 col-lg-8"> -->
		<!-- Section Tittle -->
		<!-- <div class="section-tittle section-tittle2 text-center mb-40">
                            <span>Subscribe out newslatter</span>
                            <h2>Subscribe For Newsletter</h2> -->
		<!-- </div>  -->
		<!--Hero form -->
		<!-- <form action="#" class="search-box">
                            <div class="input-form">
                                <input type="text" placeholder="What are you looking for?">
                            </div>
                            <div class="search-form">
                                <a href="#">Send Now</a>
                            </div>	
                        </form>	 -->
		<!-- </div>
                </div>
            </div>
        </div> -->
		<!-- Subscribe Area End -->
		<!-- Blog Ara Start -->
		<div class="home-blog-area section-padding30">
			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<!-- Section Tittle -->
						<div class="section-tittle text-center mb-70">
							<span>What's News</span>
							<h2>專欄文章</h2>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
						<div class="single-team mb-30">
							<div class="team-img">
								<img src="<c:url value='/images/gallery/home_blog1.png'/>"
									alt="">
							</div>
							<div class="team-caption">
								<span>美食專欄</span>
								<h3>
									<a href="blog.html"> 本週
										3/13、3/14有趣市集一次集結60家知名美食！炸雞界街頭痞子《週末炸雞俱樂部》</a>
								</h3>
								<p>October 6, 2020by Steve</p>
							</div>
						</div>
					</div>
					<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
						<div class="single-team mb-30">
							<div class="team-img">
								<img src="<c:url value='/images/gallery/home_blog2.png'/>"
									alt="">
							</div>
							<div class="team-caption">
								<span>美食專欄</span>
								<h3>
									<a href="blog.html"> 【台北異國餐廳推薦｜七個不同國家】既然不能出國，那就一起用舌尖來環遊世界吧！</a>
								</h3>
								<p>October 6, 2020by Steve</p>
							</div>
						</div>
					</div>
					<div class="col-xl-4 col-lg-4 col-md-6 col-sm-6">
						<div class="single-team mb-30">
							<div class="team-img">
								<img src="<c:url value='/images/gallery/home_blog3.png'/>"
									alt="">
							</div>
							<div class="team-caption">
								<span>美食專欄</span>
								<h3>
									<a href="blog.html"> 台北八間必勝景觀餐廳！征服你眼目與味蕾，來一場難以忘懷的高空美食之旅吧！</a>
								</h3>
								<p>October 6, 2020by Steve</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Blog Ara End -->

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