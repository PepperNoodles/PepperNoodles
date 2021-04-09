<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>template</title>
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

<!-- for map package usage -->
<link rel="stylesheet"
	href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"></script>
<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"
	integrity="sha512-XQoYMqMTK8LvdxXYG3nZ448hOEQiglfqkJs1NOQV44cWnUrBc8PkAOcXy20w0vlaXaVUearIOBhiXZ5V3ynxwA=="
	crossorigin=""></script>


<style>
.header {
	background-color: #000000;
}

html, body {
	width: 100%;
	height: 100%;
}

#map {
	width: 70%;
	height: 70%;
}
</style>
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
					<img src="<c:url value="/images/logo/peppernoodle.png"/>" alt="">
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

	<div id="memoBoard"
		style="float: left; height: 100%; overflow: scroll;"></div>

	<button onclick="getcenter()" value="cc">cc</button>
	<form name="centerSearch" action="CenterSearch" method="post">
		<input type="text" name="ctPositon">


	</form>
	<div id="map" style="float: left;"></div>
	<p style="clear: left;"></p>
	<br>


	<h1>測試用</h1>



	<script>
let loca=[{"names":"藝啟開心．心中有你","longitude":"121.454084","latitude":"25.02507","cityName":"新北市  板橋區(板橋市)"},
    {"names":"身心靈的歡慶力量","longitude":"121.457717","latitude":"25.022708","cityName":"新北市  板橋區(板橋市)"},
    {"names":"肢體智能","longitude":"121.457717","latitude":"25.022708","cityName":"新北市  板橋區(板橋市)"},
    {"names":"歡樂新學園","longitude":"121.462428","latitude":"25.026103","cityName":"新北市  板橋區(板橋市)"},
    {"names":"美化我們的生活學習空間－地中海風情釉下彩瓷版壁畫","longitude":"121.46255","latitude":"25.02565","cityName":"新北市  板橋區(板橋市)"},
    {"names":"慾望在飛行","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"板橋印象─呷飽沒","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"穿梭時空的八種元素","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"千禧萬象迎新紀","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"鐵道馳騁","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"夢蝶","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"花源","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"板橋印象─回家路上","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"板橋印象─畢業班2010","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"火車要開了","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"迷宮","longitude":"121.462703","latitude":"25.015695","cityName":"新北市  板橋區(板橋市)"},
    {"names":"昇華","longitude":"121.46425","latitude":"25.011457","cityName":"新北市  板橋區(板橋市)"},
    {"names":"太極","longitude":"121.46425","latitude":"25.011457","cityName":"新北市  板橋區(板橋市)"},
    {"names":"新北市政府文化局","longitude":"121.464262","latitude":"25.011357","cityName":"新北市  板橋區(板橋市)"},
    {"names":"鳳凰日晷 Sundial of Phoenix","longitude":"121.465651","latitude":"25.012102","cityName":"新北市  板橋區(板橋市)"},
    {"names":"野趣 Joy from Nature","longitude":"121.465651","latitude":"25.012102","cityName":"新北市  板橋區(板橋市)"},
    {"names":"穿廊門聯","longitude":"121.468335","latitude":"25.017023","cityName":"新北市  板橋區(板橋市)"},
    {"names":"江翠之水","longitude":"121.468459","latitude":"25.028865","cityName":"新北市  板橋區(板橋市)"},
    {"names":"海底奇觀","longitude":"121.47089","latitude":"25.01015","cityName":"新北市  板橋區(板橋市)"},
    {"names":"育翼","longitude":"121.471773","latitude":"25.00941","cityName":"新北市  板橋區(板橋市)"},
    {"names":"新北市立藝文中心","longitude":"121.471903","latitude":"25.026351","cityName":"新北市  板橋區(板橋市)"},
    {"names":"看見需要，付出愛！","longitude":"121.47413","latitude":"25.03232","cityName":"新北市  板橋區(板橋市)"},
    {"names":"江翠心．百年情","longitude":"121.474131","latitude":"25.032372","cityName":"新北市  板橋區(板橋市)"},
    {"names":"mo一脈","longitude":"121.48221","latitude":"25.01661","cityName":"新北市  板橋區(板橋市)"}]


//memo
let memo=document.getElementById("memoBoard");
let memosheet = document.createElement("table");
let HTMLtable="";
for(let i=0;i<loca.length;i++){

    let tr1 = document.createElement("tr");
    // td1.className="tdtop";
    let td1=document.createElement("td");
    td1.rowSpan="4",
    td1.name=`toMap`;
    td1.value=`${i}`;
    td1.innerHTML="<img src=https://m-miya.net/blog/wp-content/uploads/2014/09/956-4.min_-546x546.jpg style=\"width:80px ;\">";
    td1.addEventListener("click",showMemo);
        tr1.appendChild(td1);
    let tr2 = document.createElement("tr");
    let td2=document.createElement("td");            
    td2.innerHTML=`<a href=https://www.google.com.tw/>${loca[i].names}</a>`;
        tr2.appendChild(td2);
    let tr3 = document.createElement("tr");
    let td3=document.createElement("td");
    td3.innerHTML=loca[i].latitude;
        tr3.appendChild(td3);
    let tr4 = document.createElement("tr");
    let td4=document.createElement("td");
    td4.className="tdbot";
    td4.innerHTML=loca[i].cityName;
    tr4.appendChild(td4);

    memosheet.appendChild(tr1);
    memosheet.appendChild(tr2);
    memosheet.appendChild(tr3);
    memosheet.appendChild(tr4);    
    }
memo.appendChild(memosheet);

//定map和center
var map = L.map('map', {center: [25.0333, 121.5358], zoom: 16});

var greenIcon= new L.Icon({
    iconUrl:"https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png",
    iconSize:[25,41],
    iconAnchor:[12,41],
    popupAncher:[1,-34],
    shadowSize:[41,41]
});


//定義圖磚加到 varrmap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

let count = 0;
let alat=0;
let along=0;

var markerArray = []; //create new markers array

for(let i=0; i <loca.length;i++){
    let lat=parseFloat(loca[i].latitude);
    console.log(lat);
    let long=parseFloat(loca[i].longitude);
    console.log(long);
    var marker = L.marker([lat,long]);
    let name=loca[i].names;
    marker.addTo(map).bindPopup(name).openPopup();
    markerArray.push(marker); //add each markers to array

    if(i==loca.length-1){//this is the case when all the markers would be added to array
    var group = L.featureGroup(markerArray); //add markers array to featureGroup
    map.fitBounds(group.getBounds());   
    }


    alat+=lat;
    along+=long;
    count++;
}

let center=[alat/count,along/count];

map.panTo(center);
console.log(center);
map.setZoom(16);

L.marker([25.0333, 121.5358]).addTo(map).bindPopup("aa").openPopup();
L.marker([25.033674, 121.540411],{icon:greenIcon}).addTo(map).openPopup();


//按左邊彈出視窗
function showMemo(){
console.log("ok");
let lng=parseFloat(loca[this.value].longitude);
let lat=parseFloat(loca[this.value].latitude);
popup.setLatLng([lat,lng]);
popup.setContent(loca[this.value].names)
     .openOn(map)
//  var marker = L.marker();

//  marker.bindPopup(loca[this.value]).openPopup();
}  



</script>
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
	<!-- All JS Custom Plugins Link Here here -->
	<%-- 	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script> --%>

	<!-- 	<!-- Jquery, Popper, Bootstrap -->
	-->
	<%-- 	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script> --%>

	<%-- 	<script src="<c:url value='/scripts/popper.min.js' />"></script> --%>

	<!-- 	<script type="text/javascript" -->
	<%-- 		src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script> --%>
	<!-- 	<!-- Jquery Mobile Menu -->
	-->
	<%-- 	<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script> --%>

	<!-- 	<!-- Jquery Slick , Owl-Carousel Plugins -->
	-->
	<%-- 	<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script> --%>


	<%-- 	<script src="<c:url value='/scripts/slick.min.js' />"></script> --%>

	<!-- 	<!-- One Page, Animated-HeadLin -->
	-->
	<%-- 	<script src="<c:url value='/scripts/wow.min.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/animated.headline.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script> --%>
	<!-- 	<!-- Nice-select, sticky -->
	-->
	<%-- 	<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script> --%>
	<%-- 	<script src="<c:url value='/scripts/jquery.sticky.js' />"></script> --%>
	<!-- 	<!-- contact js -->
	-->
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