<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登入</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/slicknav.css' />">
<link rel="stylesheet" href="<c:url value='/css/flaticon.css' />">
<link rel="stylesheet" href="<c:url value='/css/animate.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />">
<link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />">
<link rel="stylesheet" href="<c:url value='/css/slick.css' />">
<link rel="stylesheet" href="<c:url value='/css/nice-select.css' />">

<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<!-- Bootstrap core CSS -->
<link href="<c:url value='/vendor/bootstrap/css/bootstrap.min.css' />" rel="stylesheet">


<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />



<style>
.header {
	background-color: #000000;
}
#page2{
	background-color: blue;
	padding:0;
	margin: 0;
}
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
</style>
</head>
<body>
	<!-- 讀取圖案 -->
	<div id="preloader-active">
		<div class="preloader d-flex align-items-center justify-content-center">
			<div class="preloader-inner position-relative">
				<div class="preloader-circle" style="background-color: rgb(102, 102, 102);"></div>
				<div class="preloader-img pere-text">
					<img src="<c:url value="/images/logo/peppernoodle.png"/>" alt="">
				</div>
			</div>
		</div>
	</div>

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
							<div class="col-xl-10 col-lg-10 col-md-8 nopadding" >
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
											<li class="login">
											<a href="<c:url value='/loginSystem/loginPage' />">  
												<i class="ti-user"></i> Sign in or Register
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

		
<div class="container-fluid">
    <div class="row">
    <!-- 左邊的Bar -->
      <div class="col-lg-2 nopadding">
      	<br>
        <h3 class="container">${comDetail.realname}</h3>
        <div class="list-group">
        	<%@include file="left.jsp" %>
        </div>
      </div>
    <!-- 右邊顯示的資料 -->  
      <div class="col-lg-10 nopadding " >
      	<!-- 背景圖 -->
      	<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/login/noodles.jpg"/>)">
      		<div class="container">
			<div class="row d-flex align-items-center justify-content-center">
				<div class="col-sm-8 col-sm-offset-2">
					<!--      Wizard container        -->
					<div class="wizard-container">
						<div class="card wizard-card " data-color="blue" id="wizardProfile">
							<div class="wizard-header ">
								<h3>
									<b>店家資料</b><br>
								</h3>
							</div>
							<br>
							<form method="GET" name="form1" enctype='multipart/form-data'>
								<div class="">
									<!-- 整大包的div -->
									<div class="" id="accoutPage1">
										<div>
											<c:if test='${comDetail.companyDetailId == null}'>
												<br>帳號：<br>&nbsp;
												<input path='realname' />
											</c:if>
										</div>
										<div class="row ml-5">
											<c:if test='${comDetail.companyDetailId != null}'>
											
											<div class="col-sm-4 col-sm-offset-1">
												<br>
												<img src="<c:url value="/getComPicture/${comDetail.companyDetailId}"/>" width="100%"  />
											</div>
											<div class="col-sm-7">
												<div class="form-group">
													<label><h5>會員帳號 :&nbsp;${comDetail.userAccount.accountIndex}</h5></label>
													&nbsp;&nbsp; 
													<a href="<c:url value='/' />updateComPwd/${comDetail.companyDetailId}">修改密碼</a>
												</div>
												
												<div class="form-group">
													<label><h5>企業名稱 :&nbsp;${comDetail.realname}</h5></label>
												</div>
												<div class="form-group">
													<label><h5>連絡電話 :&nbsp;${comDetail.phonenumber}</h5></label>
												</div>
												
												<div class="form-group">
													<label><h5>地址 :&nbsp;${comDetail.location}</h5></label>
												</div>
												
												<div class="pull-right">
													<a class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
													   style="margin-bottom: 20px; margin-top: 10px"
													   href="<c:url value='/' />updateCom/${comDetail.companyDetailId}">修改資料</a>
												</div>
												
											</div>
											</c:if>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- wizard container -->
      	<!-- 背景圖結束 -->	
      	</div>
      </div>
    </div>
    <!-- /.row -->
</div>
</div>
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
										document.write(new Date().getFullYear());
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
<%-- <script src="<c:url value='/scripts/main.js' />"></script>  --%>
 <!-- Bootstrap core JavaScript -->
<%--   <script src="<c:url value='/vendor/vendor/jquery/jquery.min.js' />"></script> --%>
<%--   <script src="<c:url value='/vendor/bootstrap/js/bootstrap.bundle.min.js' />"></script> --%>
  
  
</body>
</html>