<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Single Rest</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel='stylesheet'
	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
	
<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript"
	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.bundle.min.js'/>"></script>
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
	width: 25%;
	height: 80%;
	}

tr a:hover{
	color:#FFFFFF;
	background-color:#FFBB77;
}
.nice-select{
 	position:relative;
 	z-index:1020;
}
	.frame2 {  
   		 height: 60px; /*can be anything*/
    	 width: 90px; /*can be anything*/
   		 position: relative;
	}	


	
	.frame2 img{  
		object-fit: cover; 
	    max-height: 100%;  
	    max-width: 100%; 
	    width: auto;
	    height: auto;
	    position: absolute;  
	    top: 0;   
	    bottom: 0;  
	    left: 0;  
	    right: 0;  
	    margin: auto;   
  		display: block;
	}
	
	.memoBoard{
		overflow-x:hidden;
		overflow-y:auto;
	}

.buttonblack{
	color:#8E8E8E;
}
.buttonblack:hover{
	color:#3C3C3C;
}


</style>
</head>
<body>
<%-- 		<%@include file="../restaurantPage/restaurantPage.jsp" %> --%>
		

		
		
		<div style="height: 5px;clear: left;"></div>
		
<!-- 		<p style="clear: left;"></p> -->
		
	<div id="map" style="float: left;">
	
	</div>
	

	<div style="height:80vh; float: left;overflow: scroll;">
		
		<%@include file="../restaurantPage/RestaurantMessage.jsp" %>

	<br>
	</div>	

	<script>
	
	$(document).ready(function() {
		let currentCenterLong=0;
		let currentCenterLat=0;
		let restlng = (${rest.longitude});		
		restlng+=0.000356;
		
		let restlat = (${rest.latitude});
		restlat-=0.0001360;	
		
		let restName= "${rest.restaurantName}";
		let restId= "${rest.restaurantId}";

		
		var loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];
		//let loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":2,"restaurantName":"小李子清粥小菜","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區復興南路二段142之1號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5431836","latitude":"25.0288295","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":6,"foodTagName":"springRoll","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":3,"restaurantName":"泰讚泰式料理","totalScore":null,"rankAmount":null,"restaurantAddress":"100台北市中正區信義路二段285號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.531794","latitude":"25.033419","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":1,"foodTagName":"curry","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];
				//新增icon圖案 有用到可以呼叫
		var restIcon= new L.Icon({
	        iconUrl:"<c:url value='/images/restaurantCRUD/restmarker.png' />",
		   	iconSize:[40,40],iconAnchor:[14,44],popupAncher:[1,-34],shadowSize:[41,41]});

		var restSelectIcon= new L.Icon({
		        iconUrl:"<c:url value='/images/restaurantCRUD/restmarker_blue.png' />",
			   	iconSize:[50,50],iconAnchor:[20,50],popupAncher:[1,-34],shadowSize:[41,41]
			});
		//定map和center(地點先寫死的)
		//var map = L.map('map', {center: [25.0333, 121.5358], zoom: 16});	
		var map = L.map('map', {center: [restlat, restlng], zoom: 20});	
		//定義圖磚加到 var map
		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
		}).addTo(map);
		
		 var marker = L.marker([restlat,restlng],{icon:restSelectIcon}).addTo(map);
		 
		 	let url ="<c:url value='/restSearch/restPicByid'/>"+"/"+restId;
		    marker.bindPopup("<b>"+restName+"</b>"+"<div class='frame2'><img src='"+url+"'></div>")
		    	  .bindTooltip(restName, {
						     direction: 'bottom', // right、left、top、bottom、center。default: auto
						     sticky: true, // true 跟著滑鼠移動。default: false
						     permanent: false, // 是滑鼠移過才出現，還是一直出現
						     opacity: 0.8
						    	})
				  .openTooltip();

		    		
	
		
		
		var selectedMarker;
		

		
		
		let markerArray = []; //create new markers array
		const popup = L.popup();
		
		
	});
	
	</script>
</body>
</html>