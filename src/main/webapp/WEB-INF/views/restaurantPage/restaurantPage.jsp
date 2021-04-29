<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ViewRestaurant</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<!-- <link rel="manifest" href="site.webmanifest"> -->
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet'
	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />

<script type="text/javascript"
	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
	
<script>
$(document).ready(function(){
	findMenus();

//ajax取Menus
function findMenus() {
let urls="Http://localhost:433";
    urls+="<c:url value='/restauranMenu/' />";
    urls+=${rest.restaurantId};
	console.log(urls);
$.ajax({
		type: "GET",
		url: urls,				
		dataType: "text",
		success: function (response) {
			console.log(response);
			menuId = JSON.parse(response);
			console.log(menuId[0].menuDetailId);
			console.log(menuId.length);
		},
		error: function (thrownError) {
			console.log(thrownError);
		}
	});
}




});
</script>

<style>
	#wholeBody{
	 		height: 100%;
            background-image: url("https://images.unsplash.com/photo-1531685250784-7569952593d2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1267&q=80");
            background-size: cover;
            background-position: center center;
            background-attachment: fixed;
	}
	
	#body{
		background-color:#FFFFFF; 
	}
	
	#menuArea{
		display: none;
	}
	hr {
	  border: 0;
	  clear:both;
	  display:block;
	  width: 96%;               
	  background-color:#FF0000;
	  height: 1px;
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

<div>
		
	<div class="container-fluid" id="wholeBody">
		<div class="container" id="body">
		  <div class="row">
		  	<div class="col-sm-5 mt-10 ">
		  		<div class="d-flex align-items-center justify-content-center">
		  			<figure class="m-3 justify-content-center" style="border: 1px solid #9D9D9D">
		  				<img class="m-3" width="250px" src="<c:url value="/restpicture/${rest.restaurantId}"/>">
<!-- 		  		 		<figcaption class="ml-5"><i>restaurant picture</i></figcaption>	  		 -->
		  			</figure>
		  		</div>
		  	</div>
		  	<div class="col-sm-7 mt-20">
					<table class="table">
						  <thead>
						    <tr>
						      <th colspan="2" scope="col">${rest.restaurantName}</th>			
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <th scope="row">地址</th>
						   	  <td>${rest.restaurantAddress}</td>
	
						    </tr>
						    <tr>
						      <th scope="row">連絡電話</th>
						      <td>${rest.restaurantContact}</td>
						    </tr>
						    <tr>
						      <th scope="row">營業時間</th>
						      <td>到時候要從資料庫抓</td>
						
						    </tr>
						    <tr>
						      <th scope="row">類型</th>
						      <td id="foodTag">到時候要從資料庫抓</td>
						
						    </tr>
						  </tbody>
					</table>
		  	</div>		  
		  </div>
		  
		  
		 	<div class="container">
				<button class="text-primary" id="toggleMenu">顯示菜單</button>
				<div class="" id="menuArea">
				MenuArea
				
<%-- 					<c:choose> --%>
<%-- 						<c:when test="${empty rest.Menus}"> --%>
<!-- 							沒有任何菜單 -->
<%-- 						</c:when> --%>
<%-- 						<c:otherwise> --%>
<%-- 							<c:forEach var='menus' items='${rest.Menus}'> --%>
<%-- 								<a href="<c:url value="/rest/getMenuPicture/${rest.Menus.menuDetailId}"/>"> --%>
<%-- 									<img width='20%' src="<c:url value="/rest/getMenuPicture/${rest.Menus.menuDetailId}"/>" /> --%>
<!-- 								</a> -->
<%-- 							</c:forEach> --%>
<%-- 						</c:otherwise> --%>
<%-- 					</c:choose> --%>
				
				
				</div>
				
			
			</div>
					  
		  <div class="d-flex container mt-1">	
		  		<div class="col-2 d-flex justify-content-start">		  				
		  				 <h5 class="ml-4">新增評論</h5>
		  		</div>
		  		<div class="col-6 d-flex justify-content-end">
		  				<button class="text--dark bg-secondary">送出</button>
		  		</div>
		  </div>	
		  <div class="row d-flex align-items-center justify-content-center ml-5">				
		  		
		  		
		  		<div class="container row mt-1"> 			
		  			
		  			<div class="col-2 d-flex justify-content-start">		  				
		  					<img src="https://bootdey.com/img/Content/avatar/avatar1.png" height=100px>		 	
		  			</div>
		  			<div class="col-10">
		  				<textarea rows="4" cols="60">
		  				
		  				
		  				</textarea>			
		  			
		  			</div>
		  		
		  		</div>
		  	<hr>
		  	<!-- one comment  -->
		  		<div class="container row mt-1"> 			
		  			
		  			<div class="col-2 d-flex justify-content-start">		  				
		  					<img src="https://bootdey.com/img/Content/avatar/avatar1.png" height=100px>		 	
		  			</div>
		  			
		  			
		  			<div class="col-10">	
		  				<div class="d-flex justify-content-between font-weight-light">
		  					<span class="d-flex">BBB</span>
		  				    <span>2021/01/01<button class="ml-1 text-dark"><i class="fas fa-thumbs-up"></i></button>
		  				<button class="text-dark"><i class="fas fa-reply"></i></button></span>
		  				
		  				</div>
						<p>QQQ QAQ </p>	 
		  			</div>			
		  	  </div>	
		  	  <!-- end of one comment  -->		  	 
		  	  	<!-- if have reply  -->		  	  	
				  	  <div class="container row">
				  				<div class="col-2">
				  				</div>
				  				<div class="col-1 d-flex justify-content-start">		  				
				  					<img src="https://bootdey.com/img/Content/user_2.jpg" height=70px>		 	
				  				</div>
				  				<div class="col-9">					  					
				  					<p>I am reply</p>
				  				</div>
				  	   </div>	  				  		
				   <!-- end of one reply -->		
		     </div>
		 	<hr>
		  
		</div>
	</div>


</div>

	<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

	<script>
 		$(window).on('load', function() {
			
 			$("#toggleMenu").click(function() {
 				  $( "#menuArea" ).slideToggle("fast")
 				 });
 			let menusjson = ${menu}+"";
 			menusjson=menusjson.split(",");
 			let tagjson = ${tagsjson}+"";
 			console.log("tagjson:"+tagjson)
 			
 			createMenuArea();
 			createTag();
 			function createMenuArea(){
 				let menuArea = $("#menuArea"); 
 				console.log(menuArea)
 				for(let i= 0;i<menusjson.length;i++){
 					console.log(menusjson);
 					console.log(menusjson[i]);
 					let menuImg = document.createElement("img");
 					let url = "http://localhost:433/PepperNoodles/rest/getMenuPicture";
 					menuImg.src=url+"/"+menusjson[i];
 					menuArea.append(menuImg);
 					
 				}
 			}
 			
 			function createTag(){
 				let tagArea = $("#foodTag");
 				for(let i= 0;i<tagjson.length;i++){
 					console.log("tagName: "+tagjson[i]);
 					let button = document.createElement("button");
 					button.html()=tagjson[i];
 					tagArea.append(button);
 				}
 				
 			}
 			
 			
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