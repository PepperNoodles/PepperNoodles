<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Template For inClude</title>
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
	
	<!-- for map package usage -->
<link rel="stylesheet"
	href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"></script>
<script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"
	integrity="sha512-XQoYMqMTK8LvdxXYG3nZ448hOEQiglfqkJs1NOQV44cWnUrBc8PkAOcXy20w0vlaXaVUearIOBhiXZ5V3ynxwA=="
	crossorigin=""></script>
<!--  end of map package usage -->

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
	
	<div id="memoBoard"
		style="float: left; height: 100vh; overflow: scroll;"></div>

	<button onclick="getcenter()" value="cc">cc</button>
	<form name="centerSearch" action="CenterSearch" method="post">
		<input type="text" name="ctPositon">


	</form>
	<div id="map" style="float: left;"></div>
	<p style="clear: left;"></p>
	<br>


	<h1>測試用</h1>
	
	
	<script>
	
	let loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":2,"restaurantName":"小李子清粥小菜","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區復興南路二段142之1號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5431836","latitude":"25.0288295","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":6,"foodTagName":"springRoll","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":3,"restaurantName":"泰讚泰式料理","totalScore":null,"rankAmount":null,"restaurantAddress":"100台北市中正區信義路二段285號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.531794","latitude":"25.033419","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":1,"foodTagName":"curry","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];

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



</body>
</html>