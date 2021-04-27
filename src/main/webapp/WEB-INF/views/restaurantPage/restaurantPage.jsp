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
		  				<img class="m-3" src="data:image/jpeg;base64,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">
		  		 		<figcaption class="ml-5"><i>restaurant picture</i></figcaption>	  		
		  			</figure>
		  		</div>
		  	</div>
		  	<div class="col-sm-7 mt-20">
					<table class="table">
						  <thead>
						    <tr>
						      <th colspan="2" scope="col">餐廳名</th>			
						   
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <th scope="row">地址</th>
						      <td>測試用地址</td>
	
						    </tr>
						    <tr>
						      <th scope="row">連絡電話</th>
						      <td>0912-345-678</td>
						
						    </tr>
						    <tr>
						      <th scope="row">營業時間</th>
						      <td>到時候要從資料庫抓</td>
						
						    </tr>
						    <tr>
						      <th scope="row">類型</th>
						      <td>也要從資料庫抓</td>
						
						    </tr>
						  </tbody>
					</table>
		  	</div>		  
		  </div>
		  
		  
		 	<div class="container">
				<button class="text-primary" id="toggleMenu">顯示菜單</button>
				<div class="" id="menuArea">
				MenuArea
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
			
 			$( "#toggleMenu" ).click(function() {
 				  $( "#menuArea" ).slideToggle("fast")
 				 });
 			
 			
 			
 			
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