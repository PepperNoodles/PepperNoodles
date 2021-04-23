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
tr a:hover{
	color:#FFFFFF;
	background-color:#FFBB77;
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
	
	<nav class="navbar navbar-light bg-light justify-content-between">
	<div>搜尋結果</div> 	
    <input class="mr-sm-2" type="search" placeholder="Search" aria-label="Search">
    <button>Search</button>
	</nav>
	
	
	<div id="memoBoard"
		style="float: left; height: 100vh;width:28%;  overflow: scroll;"></div>


	<div id="map" style="float: left;"></div>
	<p style="clear: left;"></p>
	<br>


	<h1>測試用</h1>
	
	
	<script>
	
	$(document).ready(function() {
		var loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];
		//let loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":2,"restaurantName":"小李子清粥小菜","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區復興南路二段142之1號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5431836","latitude":"25.0288295","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":6,"foodTagName":"springRoll","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":3,"restaurantName":"泰讚泰式料理","totalScore":null,"rankAmount":null,"restaurantAddress":"100台北市中正區信義路二段285號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.531794","latitude":"25.033419","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":1,"foodTagName":"curry","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];
		
		//定map和center(地點先寫死的)
		var map = L.map('map', {center: [25.0333, 121.5358], zoom: 16});	
		//定義圖磚加到 var map
		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
		}).addTo(map);
		
		//新增icon圖案 有用到可以呼叫
		var restIcon= new L.Icon({
	        iconUrl:"<c:url value='/images/restaurantCRUD/restmarker.png' />",
		   	iconSize:[34,34],iconAnchor:[12,41],popupAncher:[1,-34],shadowSize:[41,41]});

		var restSelectIcon= new L.Icon({
		        iconUrl:"<c:url value='/images/restaurantCRUD/restmarker_blue.png' />",
			   	iconSize:[33,33],iconAnchor:[12,41],popupAncher:[1,-34],shadowSize:[41,41]
			});
		
		
		
		let markerArray = []; //create new markers array
		const popup = L.popup();
		
		createSideMemo(loca);
		addMarker(loca);
		
		map.on('click', onMapClick);
		
		
	//function 區
	
		//點圖片function
		function showMemo(){
			//console.log(this.id-1);
			let urls="Http://localhost:9090";
		        urls+="<c:url value='/restSearch/restId' />";
		        urls+="/"+this.id;
		    //    console.log(urls);
			 $.ajax({
					type: "GET",
					url: urls,				
					dataType: "json",
					success: function (response) {
						//console.log(response);
						let rest = response;
						//console.log(location[0].restaurantName);
						let lng=parseFloat(rest.longitude);
						let lat=parseFloat(rest.latitude);
						let name = rest.restaurantName;
						var marker = L.marker([lat,lng],{icon:restIcon});
						 
						 marker.bindTooltip(name, {
						     direction: 'bottom', // right、left、top、bottom、center。default: auto
						     sticky: true, // true 跟著滑鼠移動。default: false
						     permanent: false, // 是滑鼠移過才出現，還是一直出現
						     opacity: 0.8
						    	}).openTooltip();
						 
						
						 marker.addTo(map).openPopup();
					 	 map.panTo([lat,lng]);
						
					},
					error: function (thrownError) {
						console.log(thrownError);
					}
				});
			
			
			
			
		 	 //map.setZoom(16);
		 	 //map.remove(marker);
		}  

	
	
		//點地圖function 點完後會去撈附近餐廳
		function onMapClick(e) {
		  let lat = e.latlng.lat; // 緯度
		  let lng = e.latlng.lng; // 經度
		
// 		  popup
// 		    .setLatLng(e.latlng)
// 		    .setContent("緯度："+lat+"<br/>經度："+lng)
// 		    .openOn(map);	  
		  let bound = map.getBounds()
		  //console.log(bound);	
		  //console.log(bound._northEast.lat);
		  //console.log(bound._northEast.lng);
		  //console.log(bound._southWest.lat);
		  //console.log(bound._southWest.lng);
		  //L.circle([lat,lng], 200).addTo(map);	  
		  map.panTo(e.latlng); 		  
		  //ajax取地圖		  
		  let urls="Http://localhost:9090";
		      urls+="<c:url value='/restSearch/restNear' />";
			  urls+="/"+bound._northEast.lat+"/"+bound._southWest.lat+"/"+bound._northEast.lng+"/"+bound._southWest.lng;
			  console.log(urls);
		  $.ajax({
				type: "GET",
				url: urls,				
				dataType: "text",
				success: function (response) {
					//console.log(response);
					loca=[];
					loca = JSON.parse(response);
					//console.log(location[0].restaurantName);
					createSideMemo(loca);
					addMapMarker(loca);
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		}
		//將餐廳物件陣列丟進去,不要亂改位置
		function addMapMarker(loca){			
			
			for(let i=0; i <loca.length;i++){
			    let lat=parseFloat(loca[i].latitude);
			    let long=parseFloat(loca[i].longitude);	
			    
			    var marker = L.marker([lat,long],{icon:restSelectIcon});
			    
			    let name=loca[i].restaurantName;
			    marker.addTo(map).openPopup();
			    
			    marker.bindTooltip(name, {
			    	  direction: 'bottom', // right、left、top、bottom、center。default: auto
			    	  sticky: true, // true 跟著滑鼠移動。default: false
			    	  permanent: false, // 是滑鼠移過才出現，還是一直出現
			    	  opacity: 1.0
			    	}).openTooltip();
				}
		}
		
		//將餐廳物件陣列丟進去做事,把所有點pop並設定中心位置
		function addMarker(loca){
			let count = 0;
			let alat=0;
			let along=0;
			
			for(let i=0; i <loca.length;i++){
			    let lat=parseFloat(loca[i].latitude);
			    let long=parseFloat(loca[i].longitude);	
			    
			    var marker = L.marker([lat,long],{icon:restSelectIcon});
			    
			    let name=loca[i].restaurantName;
			    marker.addTo(map).openPopup();
			    markerArray.push(marker); //add each markers to array
			
			    marker.bindTooltip(name, {
			    	  direction: 'bottom', // right、left、top、bottom、center。default: auto
			    	  sticky: true, // true 跟著滑鼠移動。default: false
			    	  permanent: false, // 是滑鼠移過才出現，還是一直出現
			    	  opacity: 0.8
			    	}).openTooltip();
			    
			    
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
		}
	
		//見隔壁memo的function
		function createSideMemo(loca){
			//memo
			let memo=document.getElementById("memoBoard");
			memo.innerHTML="";
			let memosheet = document.createElement("table");
			let HTMLtable="";
			for(let i=0;i<loca.length;i++){
			
				//建立td1和roleSpan
			    let tr1 = document.createElement("tr");
			    // td1.className="tdtop";
			    let td1=document.createElement("td");
			    td1.rowSpan="4";	   
			    let img =document.createElement("img");	   
			    let url ="<c:url value='/restSearch/restPicByid'/>"+"/"+loca[i].restaurantId;
			    img.src=url;
			    img.name=loca[i].restaurantName;
			    img.style.height="80px";
			    img.id=loca[i].restaurantId;
			    td1.appendChild(img);
			    img.removeEventListener("click",showMemo);
			    img.addEventListener("click",showMemo);	    
			    tr1.appendChild(td1);
			    
			    let tr2 = document.createElement("tr");
			    let td2=document.createElement("td");
			    let restAnchor = document.createElement("a");
			    restAnchor.href="#";
			    restAnchor.innerHTML=loca[i].restaurantName;
			    restAnchor.style.color="#0000C6";
			    td2.appendChild(restAnchor);
			    tr2.appendChild(td2);     			        
			        
			    let tr3 = document.createElement("tr");
			    let td3=document.createElement("td");
			    td3.innerHTML=loca[i].latitude;
			        tr3.appendChild(td3);
			    let tr4 = document.createElement("tr");
			    let td4=document.createElement("td");
			    td4.className="tdbot";
			    td4.innerHTML=loca[i].restaurantAddress;
			    tr4.appendChild(td4);
				
			    memosheet.appendChild(tr1);
			    memosheet.appendChild(tr2);
			    memosheet.appendChild(tr3);
			    memosheet.appendChild(tr4);    
			    }
			memo.appendChild(memosheet);
		}	
	});
	
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