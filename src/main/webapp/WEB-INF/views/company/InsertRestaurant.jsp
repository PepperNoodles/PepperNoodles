<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>restaurantCRUD</title>
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
<%-- <link rel="stylesheet" href="<c:url value='/css/slicknav.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/flaticon.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/animate.min.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/slick.css' />"> --%>
<%-- <link rel="stylesheet" href="<c:url value='/css/nice-select.css' />"> --%>

<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<style>
.header {
	background-color: #000000;
}

.rest-infobox {
	float: left;
	width: 30%;
}

.rest-picbox {
	float: right;
	margin: auto;
	width: 60%;
	border: 3px solid #73AD21;
	padding: 10px 10px 10px 10px;
	border: 3px solid #73AD21;
	padding: 10px 10px 10px 10px;
	height: 50%;
}

#restaurantPicturePreview {
	width: 100%;
	heght: 100%;
	object-fit: contain;
}

footer {
	clear: both;
	/* 	清除上面float影響 */
}

.toshow {
	display: block;
}

.tohide {
	display: none;
}
</style>
<!--餐廳地址Ajax驗證不可重複 -->
<script>
		$(function() {

			var Rid=${restaurant.restaurantId};
			
			${"#RName"}.click(function(){
				
				
				alert("good");
				
				
				
			}
					
		});
		
	
	
	</script>
</head>
<body>
	<!-- 讀取圖案 -->
	<!-- 	<div id="preloader-active"> -->
	<!-- 		<div -->
	<!-- 			class="preloader d-flex align-items-center justify-content-center"> -->
	<!-- 			<div class="preloader-inner position-relative"> -->
	<!-- 				<div class="preloader-circle" -->
	<!-- 					style="background-color: rgb(102, 102, 102);"></div> -->
	<!-- 				<div class="preloader-img pere-text"> -->
	<%-- 					<img src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""> --%>
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
	<!-- 	</div> -->

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

	<!-- 餐廳管理頁面 -->
	<!-- 背景圖片+自動填滿 -->
	<div class="image-container set-full-height"
		style="background-image: url(<c:url value="/images/restaurantCRUD/background_1.jpg"/>) ;background-size:cover">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 col-sm-offset-2">
					<form:form method="POST" modelAttribute="restaurant"
						enctype='multipart/form-data'>
						<!--綁定model中的restaurant-->

						<div class="rest-infobox">
							<h1 style="color: red">add rest</h1>
							<h5 style="color: #FF1493">餐廳名稱：</h5>
							<form:input path='restaurantName' id='RName' />
							<form:errors path="restaurantName" cssClass="error" />
							<br>
							<h5 style="color: #FF1493">餐廳地址：</h5>
							<form:input path='restaurantAddress' id="RAdd" />
							<form:errors path="restaurantAddress" cssClass="error" />
							<br>
							<div class="toshow">
								<h5 style="color: #FF1493">經度：</h5>
								<form:input path='longitude' id="RLong" />
								<br>
								<h5 style="color: #FF1493">緯度：</h5>
								<form:input path='latitude' id="RLati" />
								<br>
							</div>
							<h5 style="color: #FF1493">聯絡方式：</h5>
							<form:input path='restaurantContact' />
							<form:errors path="restaurantContact" cssClass="error" />
							<br>
							<h5 style="color: #FF1493">餐廳網站：</h5>
							<form:input path='restaurantWebsite' />
							<form:errors path="restaurantWebsite" cssClass="error" />
							<br>
							<h5 style="color: #FF1493">餐廳照片：</h5>
							<form:input path='productImage' type='file'
								id="restaurant-picture" accept="image/*" />
							<form:errors path="productImage" cssClass="error" />
							<br> <input type='submit' id="checkRAddressBeforeSubmit">
						</div>
						<div class="rest-picbox">
							<img src="<c:url value="/images/NoImage/restaurantdefault.png"/>"
								class="picture-src" id="restaurantPicturePreview" />
						</div>


					</form:form>
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
		});
	</script>

	<!-- JS here -->
	<!--預覽照片 -->
	<script>
		$(function() {
			$("#restaurant-picture").change(
					function() {
						if (this.files && this.files[0]) {
							var reader = new FileReader();

							reader.onload = function(e) {
								$('#restaurantPicturePreview').attr('src',
										e.target.result);
							}

							reader.readAsDataURL(this.files[0]);
						}
					});
		});
	</script>
	<!--地址轉座標 -->
	<script>
		var map;

		function initMap() {
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 25.033710,
					lng : 121.564718
				},
				zoom : 15
			});
			var marker = new google.maps.Marker({
				position : {
					lat : 25.033710,
					lng : 121.564718
				},
				map : map
			});
		}

		function startTrans() {
			let addr = document.getElementById("RAdd").value;
			console.log(addr);
			addressToLocation(addr);
// 			console.log(document.getElementById("lat").value);      

		}

		function addressToLocation(addr) {
			let longitude;
			let latitude;
			let loca;
			var geocoder = new google.maps.Geocoder();
			geocoder.geocode({
				'address' : addr
			}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					longitude = results[0].geometry.location.lng();

					latitude = results[0].geometry.location.lat();

					let lat = document.getElementById("RLati");
					lat.value = latitude;
					let lng = document.getElementById("RLong");
					lng.value = longitude;
				} else {
				}
			})

		}

		document.getElementById("RAdd").addEventListener("change", startTrans);
	</script>

	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBTUCen4YixtEKjNBAL4CX5xkW1QQAembQ&callback=initMap"
		async defer>
	</script>
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>

	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>

	<%-- 	<script src="<c:url value='/scripts/popper.min.js' />"></script> --%>

	<!-- 	<script type="text/javascript" -->
	<%-- 		src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script> --%>
	<!-- Jquery Mobile Menu -->
	<%-- 	<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script> --%>

	<!-- Jquery Slick , Owl-Carousel Plugins -->
	<%-- 	<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script> --%>


	<%-- 	<script src="<c:url value='/scripts/slick.min.js' />"></script> --%>

	<!-- One Page, Animated-HeadLin -->
	<%-- 	<script src="<c:url value='/scripts/wow.min.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/animated.headline.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script> --%>
	<!-- Nice-select, sticky -->
	<%-- 	<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/jquery.sticky.js' />"></script> --%>
	<!-- contact js -->
	<%-- 	<script src="<c:url value='/scripts/contact.js' />"></script> --%>

	<%-- 	<script src="<c:url value='/scripts/jquery.form.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/jquery.validate.min.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/mail-script.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script> --%>

	<!-- Jquery Plugins, main Jquery -->
	<script src="<c:url value='/scripts/plugins.js' />"></script>
	<%-- 	<script src="<c:url value='/scripts/main.js' />"></script> --%>
</body>
</html>