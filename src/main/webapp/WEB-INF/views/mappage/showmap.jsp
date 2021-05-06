<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search For Food</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
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
	width: 70%;
	height: 70%;
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
	
	<nav class="navbar navbar-light bg-light justify-content-center">
	<span>區域:</span>
	<select id="directSelect">
	<option value="NULL">不分區</option> 
    <option>大安區</option>
    <option>信義區</option>
    <option>大同區</option>
    <option>中正區</option>
    <option>松山區</option>
    <option>萬華區</option>
    <option>士林區</option>
    <option>北投區</option>
    <option>內湖區</option>
    <option>南港區</option>
    <option>文山區</option>
	</select>
	
	<span>種類:</span>
	<div id="tagSelect">
	</div>	
	
	
    <input id="keyWord" class="mr-sm-2" type="search" placeholder="Search" aria-label="Search">
    <button class="buttonblack" id="keyWordSearch">Search</button>
	</nav>
	
	
<div class="modal" id="modal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">貼心提示</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p>很抱歉,沒有符合的結果😅</p>
      </div>
    </div>
  </div>
</div>
		
		
			<div class="memoBoard" id="memoBoard"
			style="float: left; height: 70%;width:28%; border-bottom-width:1px; border-bottom-style:solid; border-bottom-color:black">
				<h2 class="m-2">搜尋結果區</h2>
			</div>
		

		<div id="map" style="float: left;">
	
		</div>
		<div style="height: 5px;clear: left;"></div>
		
		<div id="pageButton" style="float: left;">
		</div>
		
	
	
		<p style="clear: left;"></p>

	<br>

	<h5 style="display: none;" id="indexSearch">${mapRests}</h5>
	<h5 style="display: none;" id="currentPosition">${current}</h5>
	
	
	
	
	<script>
	
	$(document).ready(function() {
		let currentCenterLong=0;
		let currentCenterLat=0;
		
		
		var loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];
		//let loca=[{"restaurantId":1,"restaurantName":"餵我早餐","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區信義路四段26號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5442443","latitude":"25.0332834","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":5,"foodTagName":"hamburger","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":2,"restaurantName":"小李子清粥小菜","totalScore":null,"rankAmount":null,"restaurantAddress":"106台北市大安區復興南路二段142之1號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.5431836","latitude":"25.0288295","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":6,"foodTagName":"springRoll","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]},{"restaurantId":3,"restaurantName":"泰讚泰式料理","totalScore":null,"rankAmount":null,"restaurantAddress":"100台北市中正區信義路二段285號","restaurantContact":"0912345678","restaurantWebsite":"www.google.com","restaurantPhoto":null,"productImage":null,"longitude":"121.531794","latitude":"25.033419","userAccountId":null,"userAccount":null,"restaurantMessageBox":[],"eventList":[],"foodTag":[{"foodTagIid":3,"foodTagName":"pizza","forums":[],"foodTagUsers":[],"product":[]},{"foodTagIid":1,"foodTagName":"curry","forums":[],"foodTagUsers":[],"product":[]}],"products":[],"menus":[],"restaurantBusinHour":[]}];
		
		//定map和center(地點先寫死的)
		var map = L.map('map', {center: [25.0333, 121.5358], zoom: 16});	
		//定義圖磚加到 var map
		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
		}).addTo(map);
		
		var selectedMarker;
		var circleMarker;
		
		//新增icon圖案 有用到可以呼叫
		var restIcon= new L.Icon({
	        iconUrl:"<c:url value='/images/restaurantCRUD/restmarker.png' />",
		   	iconSize:[40,40],iconAnchor:[14,44],popupAncher:[1,-34],shadowSize:[41,41]});

		var restSelectIcon= new L.Icon({
		        iconUrl:"<c:url value='/images/restaurantCRUD/restmarker_blue.png' />",
			   	iconSize:[33,33],iconAnchor:[12,41],popupAncher:[1,-34],shadowSize:[41,41]
			});
		var userIcon= new L.Icon({
	        iconUrl:"<c:url value='/images/restaurantCRUD/ruserloca.png.png' />",
		   	iconSize:[33,33],iconAnchor:[12,41],popupAncher:[1,-34],shadowSize:[41,41]
		});
		
		
		$('#toast').toast('show')
		let markerArray = []; //create new markers array
		const popup = L.popup();
		
		//從首頁傳值
		if ($("#indexSearch").html().length>5){
			console.log($("#indexSearch").html().length);
			let indexResult = $("#indexSearch").html();
			indexResult=JSON.parse(indexResult);
			if(indexResult){
				//console.log("首頁功能:"+indexResult);
				//console.log("首頁功能:"+indexResult[0].restaurantName);
				
				loca=[];
				loca=indexResult;
				createSideMemo(loca,1);
				addMarker(loca);
			}
		}else{
			loca=[];
		}
		//從首頁不傳值
		if($("#currentPosition").html().length>2){			
			
			map.locate({setView: true, maxZoom: 20});

			map.on('locationfound', onLocationFound);
			
			function onLocationFound(e) {
			    var radius = e.accuracy;

			    L.marker(e.latlng).addTo(map)
			     .bindPopup("你現在的位置在這附近").openPopup();
				
			    console.log(e.latitude);
			    console.log(e.longitude);
			    circleMarker = L.circle(e.latlng, radius).addTo(map);
			    
			    setTimeout(cleanCircle, 2000);
				
			    let urls="Http://localhost:433";
			      	urls+="<c:url value='/restSearch/centerLocation' />";
				  	urls+="/"+e.latitude+"/"+e.longitude;
				    console.log(urls);
				    nearselect(urls);
				    
			    function cleanCircle() {
			    	map.removeLayer(circleMarker);
			    }
			}

		
			
			//失敗
				function errorHandler() {
					alert('無法取得位置,預設位置為大安森林公園');
					let lat=25.0333;
					let lng=121.5358;
					var marker = L.marker([lat,lng]).addTo(map).openPopup();
					map.panTo([lat,lng]);
					
					 let urls="Http://localhost:433";
				         urls+="<c:url value='/restSearch/centerLocation' />";
					  	 urls+="/"+lat+"/"+lng;
					  	 nearselect(urls);
					
					setTimeout(function(){
						map.removeLayer(marker);
					}, 2000);
					
				}
				map.on('locationerror', errorHandler);
		}
		
		
		
		//createSideMemo(loca,1);
		//addMarker(loca);
		
		map.on('click', onMapClick);
		$("#keyWordSearch").on('click',keyWordSearch);
		tagCreater();
	//function 區
	
		//點圖片function
		function showMemo(){
			//console.log(this.id-1);
			let urls="Http://localhost:433";
		        urls+="<c:url value='/restSearch/restId' />";
		        urls+="/"+this.id;
		    //    console.log(urls);
			 $.ajax({
					type: "GET",
					url: urls,				
					dataType: "json",
					success: function (response) {
						if(selectedMarker){
							map.removeLayer(selectedMarker)
						}
						//console.log(response);
						let rest = response;
						//console.log(location[0].restaurantName);
						let lng=parseFloat(rest.longitude);
							lng+=0.000356;	
						let lat=parseFloat(rest.latitude);
							lat-=0.0001360;	
						let name = rest.restaurantName;
						selectedMarker = L.marker([lat,lng],{icon:restIcon});
						 
						selectedMarker.bindTooltip(name, {
						     direction: 'bottom', // right、left、top、bottom、center。default: auto
						     sticky: true, // true 跟著滑鼠移動。default: false
						     permanent: false, // 是滑鼠移過才出現，還是一直出現
						     opacity: 0.8
						    	}).openTooltip();
						 
						
						selectedMarker.addTo(map).openPopup();
					 	 map.panTo([lat,lng]);
						
					},
					error: function (thrownError) {
						console.log(thrownError);
					}
				});
		
		}
		//產生按鈕
		function buttonGenerater(page){
			$("#pageButton").html("");
			for(let i = 0;i<page;i++){
				let button = document.createElement("button");
				button.id="pageButton"+i;
				button.value=(i+1);
				button.className="buttonblack";
				button.innerHTML="<span>"+(i+1)+"</span>";
				button.addEventListener('click',showSideBar);
				$("#pageButton").append(button);
			}
		}	
	
		function showSideBar(){
			let page = this.value;
			//console.log(page);
			createSideMemo(loca,page);
		}
		
		//////////////////////////目前最新的function
		//關鍵字查詢
		function keyWordSearch(){
			if(selectedMarker){
				map.removeLayer(selectedMarker)
			}
			
			console.log("keyWordSearch function");
						
			let keyWord = $("#keyWord").val();
			let tag =$("#tagSelecter").val();
			let dist = $("#directSelect").val();
			
			console.log("dist: "+dist);				
			console.log("tag: "+tag);
			if(keyWord){
				console.log("keyWord: "+keyWord);
			}
			
			
			
			let urls="Http://localhost:433";
	        urls+="<c:url value='/restSearch' />";
	        if(keyWord){
	        	if(keyWord&&dist!="NULL"&&tag!="NULL"){
	        		
	        		console.log("keydistagAllllll");
	        		 urls+="/restSearchandDistandTag";
	        		 urls+="?restName="+keyWord;
	        		 urls+="&searchDist="+dist;
	        		 urls+="&searchTag="+tag;
	        		 
	        	}else if(keyWord&&dist!="NULL"){
	        		 //console.log("keydisttttt");
	        		 urls+="/restSearchandDist";
	        		 urls+="?restName="+keyWord;
	        		 urls+="&searchDist="+dist;	        		
	        		
	        	}else if(keyWord&&tag!="NULL"){
	        		//console.log("keytaggggg");
	        		 urls+="/restSearchandTag";
	        		 urls+="?restName="+keyWord;	        		
	        		 urls+="&searchTag="+tag;
	        		
	        		
	        	}else{
	        		urls+="/restName";
	        		urls+="?restName="+keyWord;
	        	}   		
	        		
	        		
	        }else if(tag!="NULL"){
	        	
	        	if(tag!="NULL"&&dist!="NULL"){
	        		
	        	    //console.log("tagdistttttt");
	        	    urls+="/restSearchandDistandTag";	        		
	        		urls+="?searchDist="+dist;
	        		urls+="&searchTag="+tag;
	        		urls+="&restName=";
	        	    
	        	}else{
	        		
	        		//console.log("taggggggggg");
	        		 urls+="/restSearchandTag";	        		        		
	        		 urls+="?searchTag="+tag;
	        		 urls+="&restName=";
	        	}
	        	
	        }else if(dist!="NULL"){
	        	
	        	//console.log("distttt");
	        	 urls+="/restSearchandDist";        		 
        		 urls+="?searchDist="+dist;	  
        		 urls+="&restName=";
	        	
	        }else{
	        	
	        	//console.log("000000");
	        	urls+="/restName";        		
	        }
	        
	       
	        console.log(urls);
		 $.ajax({
				type: "GET",
				url: urls,				
				dataType: "json",
				success: function (response) {
					console.log(response);
					if (response.length>0){
						loca = response;	
						//addMapMarker(loca);
						addMarker(loca);
						createSideMemo(loca,1);
						let page =Math.ceil(loca.length/6);
						buttonGenerater(page);
					}else{
					//	console.log("okkkkkkk")
						$('#modal').modal('show')
					}					
					
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		}	
	
	
		
	
	//點地圖function 點完後會去撈附近餐廳,////////////////很吃效能的功能
		function onMapClick(e) {
			if(selectedMarker){
				map.removeLayer(selectedMarker)
			}
			
			
		  let lat = e.latlng.lat; // 緯度
		  let lng = e.latlng.lng; // 經度
		
		  //設定距離多遠在抓
		  let disLat=Math.abs(lat-currentCenterLat);
		  let disLong=Math.abs(lng-currentCenterLong);
		  //console.log("distance: lat: "+disLat+" long: "+disLong);
		  if(disLat > 0.0045 ||  disLong> 0.0055 || (disLat+disLong)>0.007){			 
				  currentCenterLat=lat;
				  currentCenterLong=lng;
				  
				  
				  let bound = map.getBounds()
	//	 		  popup
	//	  		    .setLatLng(e.latlng)		    
	//	  		    .setContent("緯度："+lat+"<br/>經度："+lng)
	//	 		    .openOn(map);	  
				  
				  map.panTo(e.latlng); 		  
				  //ajax取地圖		  
				  let urls="Http://localhost:433";
				      urls+="<c:url value='/restSearch/restNear' />";
					  urls+="/"+bound._northEast.lat+"/"+bound._southWest.lat+"/"+bound._northEast.lng+"/"+bound._southWest.lng;
					 // console.log(urls);
				  nearselect(urls);
				 
		  	}else{
			  
			  map.panTo([lat,lng]);
		  }
		  

		}
	
		function nearselect(urls){
			  $.ajax({
					type: "GET",
					url: urls,				
					dataType: "text",
					success: function (response) {
						//console.log(response);
						loca=[];
						loca = JSON.parse(response);
						if(loca.length>0){
					//		console.log(loca[0].restaurantName);
					//		console.log(loca.length);
							
							createSideMemo(loca,1);
							addMapMarker(loca);
							let page =Math.ceil(loca.length/6);
							buttonGenerater(page);
						}else{
							let memo=document.getElementById("memoBoard");
							memo.innerHTML="";
							$("#pageButton").html("");
						}
						
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
				lat-=0.0001360;	
			    let long=parseFloat(loca[i].longitude);	
			 	long+=0.000356;
			    
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
			    	lat-=0.0001360;
			    let long=parseFloat(loca[i].longitude);	
			    	long+=0.000356;
			    
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
			
			map.panTo(center)
		}
	
		//建立隔壁memo的function
		function createSideMemo(loca,page){
			//memo
			let memo=document.getElementById("memoBoard");
			memo.innerHTML="";
			let memosheet = document.createElement("table");
			let HTMLtable="";
			let start = 6*(page-1)
			
			let end = Math.ceil(loca.length/6)==(page) ? loca.length : 6*page;
// 			console.log(start+":"+end);
// 			console.log("loca長度:=="+loca.length)
			if(loca.length>0){
				for(let i=start;i<end;i++){
					
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
				    let imgframe =document.createElement("div");
				    imgframe.className='frame2';
				    imgframe.appendChild(img);
				    
				    td1.appendChild(imgframe);
				    
				    
				    img.removeEventListener("click",showMemo);
				    img.addEventListener("click",showMemo);	    
				    tr1.appendChild(td1);
				    
				    let tr2 = document.createElement("tr");
				    let td2=document.createElement("td");
				    let restAnchor = document.createElement("a");
				    
				    let resturl ="<c:url value='/userPage/'/>"+loca[i].restaurantId;
				    restAnchor.href=resturl;
				    
				    restAnchor.innerHTML=loca[i].restaurantName;
				    restAnchor.style.color="#0000C6";
				    td2.appendChild(restAnchor);
				    tr2.appendChild(td2);     			        
				        
				    let tr3 = document.createElement("tr");
				    let td3=document.createElement("td");
				    td3.innerHTML=loca[i].restaurantAddress;
				        tr3.appendChild(td3);
				        
				    //標籤列表
				    let tr4 = document.createElement("tr");
				    let td4=document.createElement("td");
				    td4.innerHTML="Tags:&nbsp"
				    td4.className="tdbot";
				    
				    for(let j = 0; j<loca[i].foodTag.length;j++){
				    	let tagAnchor = document.createElement("a");
						tagAnchor.href="#";
						
						tagAnchor.innerHTML+=loca[i].foodTag[j].foodTagName+"&nbsp&nbsp";
						tagAnchor.style.color="#A23400";
						td4.appendChild(tagAnchor);
				    }
				    
				    
				    //td4.innerHTML=loca[i].restaurantAddress;
				    tr4.appendChild(td4);
					
				    let tr5= document.createElement("tr");
				    tr5.style.height="5px";
				    tr5.style.backgroundColor="#9D9D9D";
				    tr5.appendChild(document.createElement("td"));
				    tr5.appendChild(document.createElement("td"));
				    tr5.appendChild(document.createElement("td"));
				    
				    memosheet.appendChild(tr1);
				    memosheet.appendChild(tr2);
				    memosheet.appendChild(tr3);
				    memosheet.appendChild(tr4);    
				    memosheet.appendChild(tr5); 
				    }
				memo.appendChild(memosheet);
			}else{
				memo.innerHTML="<h2>沒有符合的餐廳</h2>";
			}
			
		
		}	
		
		//創立tag選擇器
		function tagCreater(){
			let urls="Http://localhost:433";
				urls+="<c:url value='/restSearch/tagAll' />";
			$.ajax({
					type: "GET",
					url: urls,				
					dataType: "json",
					success: function (response) {
						//console.log(response);
						let tags = response;
						let select = document.createElement("select");
						select.classList.add("form-control")
						select.id = ("tagSelecter");
						for (let i = 0;i<tags.length+1;i++){
							
							let option = document.createElement("option");
							
							if (i == 0){								
								option.innerHTML="All";
								option.value = "NULL";
							}else{
								option.innerHTML=tags[i-1];
							}	
							select.appendChild(option);						
							
							//console.log(tags[i-1]);
						}
						
						let divSelect = document.getElementById("tagSelect");
						divSelect.appendChild(select);
						
					},
					error: function (thrownError) {
						console.log(thrownError);
					}
				})
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