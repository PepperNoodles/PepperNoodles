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
	findAllMessage();
	
	$("#addMessage").click(function(){
		var message = $("#message").val();
		
		if(message == null || message ==""){
			text="請輸入訊息...";
			$("#messageResult").css({"color":"red","font-size":"small"});
		}
		else{
			addMessageBox();
			findAllMessage();
			$("#message").val('');
			text="";
		}
		$("#messageResult").html(text);
	});
	
	
	$("#allMessageDiv").on('click','.replyMessage',function(){
		//被回覆的留言ID
// 		$("").nextSibling()
		$("#a~div").toggle();
		
	});
	//回覆留言
	$("#allMessageDiv").on('click','.getReplyMessage',function(){
		var messageIdValue = $(this).next().text();
		var text=$(this).prev().val();
		userId="${userAccount.accountId}";
		userIndex="${userAccount.accountIndex}";
		console.log(userId);
		console.log(messageIdValue);
// 		alert(messageIdValue);
// 		alert(text);
		data ={
				"replyNetizenAccountId": userId,
				"replyMessageId": messageIdValue,
				"replyText": text,
				"time": null,
				"likeAmount": null,
				"userAccount": "${userAccount}",
				"restaurantMessageBox": null
			  };

		$.ajax({
			type:"POST",
			url: "/PepperNoodles/addReplyRestaurantMessage",
			contentType:'application/json;charset=UTF-8',
			dataType: "text",
			data:JSON.stringify(data),
			success: function (response) {
				console.log("新增回覆訊息回傳:"+response);
			},
	        error: function (thrownError) {
				console.log(thrownError);
			}
		});
		
		
		
	});
	

});
	




//新增訊息
function addMessageBox(){
	urls  = "/PepperNoodles/addRestaurantMessage";
	console.log(urls);
	data = new FormData();
	data.append('restMessageInfo',new Blob([JSON.stringify( {"netizenAccount":"${userAccount.accountIndex}",
															 "restaurantId": ${rest.restaurantId},
		   											   		 "text": $("#message").val()
		   											   		 } )],{type: "application/json"}));
	$.ajax({
		method:"POST",
		url:urls,
		data:data,
		processData: false,
		contentType: false, 
		cache: false,  //不做快取
		async : true,
		success: function (response) {
			console.log("新增訊息回傳:"+response);
			findAllMessage();
		},
        error: function (thrownError) {
			console.log(thrownError);
		}
	});
}

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
			menu = JSON.parse(response);
// 			console.log(menu[0].menuDetailId);
			console.log(menu.length);
			var text="";
			if(menu.length == 0){
				text="目前沒有菜單";
			}
			else{
				for(i=0;i<menu.length;i++){
				text += "<a href='<c:url value='/rest/getMenuPicture/"+menu[i].menuDetailId+"'/>'>;";
				text += "<img width='20%' src='<c:url value='/rest/getMenuPicture/"+menu[i].menuDetailId+"'/>' />";
				text += "</a>";
				}
			}
			console.log(text);

				$("#menuArea").html(text);
		},
		error: function (thrownError) {
			console.log(thrownError);
		}
	});
}

//ajax取留言
function findAllMessage(){
	let urls="Http://localhost:433";
    urls+="<c:url value='/allRestaurantMessage/' />";
    urls+=${rest.restaurantId};
	console.log(urls);
$.ajax({
		type: "GET",
		url: urls,				
		dataType: "text",
		success: function (response) {
			console.log(response);
			allMessage = JSON.parse(response);
// 			console.log(allMessage.length);
			var text="";
			var allMessageLength = allMessage.length
			if(allMessage.length == 0){
				text="目前沒有留言";
			}
			else{
				for(i=0;i<allMessage.length;i++){
					var formatDate   =(new Date(allMessage[i].time)).toString().substring( 4 , 21 );
	        		var formatString = formatDate.split(' ');
	        		var formatPrint  = formatString[0]+ '/' + formatString[1] + '/'+ formatString[2] + '&emsp;' +formatString[3];
					text +="<div class='container border-left border-info mr-3'>";
					text +="	<div class='media' id='a'>";
					text +="		<img class='rounded border mr-3' src=' <c:url value='/userProtrait/"+allMessage[i].userAccount.userAccountDetail.useretailId+"'/> ' width='125px' height=125px>";
					text +="		<div class='media-body'>";
					text +="			<h5 class='mt-0 text-info'>"+allMessage[i].userAccount.userAccountDetail.nickName+"</h5>";
					text +="			<span class='float-right'>"+formatPrint;
					text +="				<button class='ml-1 text-dark'><i class='fas fa-thumbs-up'></i></button>";
					text +="				<button class='text-dark replyMessage' ><i class='fas fa-reply' >回覆</i></button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
					text +="			</span>";
					text +="			<p>"+allMessage[i].text+"</p>";
					text +="		</div>";
					text +="	</div>";
					//回覆留言的div
					text +="	<div class='media  replyInput"+i+"'>";
// 					text +="	<div style='display:none' class='media replyInput"+i+"'>";
// 					text +="  		<div class='input-group-append'>";
					text +="  			<input type='text' size='100%' class='form-control ml-5' placeholder='回覆...'>";
					text +="			<button class='bg-secondary getReplyMessage' style='height:38px'>reply</button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
// 					text +="		</div>";
					text +="	</div>";
					
					
					text +="</div>";
					text +="<hr>";
				}
			}
// 			console.log(text);
			$("#allMessageDiv").html(text);
		},
		error: function (thrownError) {
			console.log(thrownError);
		}
	});
}


	




</script>

<style>
	img{
		margin: 10px;
	}
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
		  			<figure class="m-3 rounded justify-content-center" style="border: 1px solid #9D9D9D">
		  				<img class="m-2 rounded" width="250px" src="<c:url value="/restpicture/${rest.restaurantId}"/>">
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
						      <td>
							      到時候要從資料庫抓
						      </td>
						
						    </tr>
						    <tr>
						      <th scope="row">類型</th>

						      <td id="foodTag">
						      	到時候要從資料庫抓
						      
						      
						      </td>
						
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
		  		<br>
		  		<div class="col-2 d-flex justify-content-start">		  				
		  				 <h5 class="ml-4">新增評論</h5>
		  		</div>
		  		<br>
		  </div>	
		  	
		  	 <div class="row">
		  	 	<div class="container  p-8">
		  	 		<div class="media mx-5">
			  	 		<img class="rounded border" src=" <c:url value="/userProtrait/${userAccount.userAccountDetail.useretailId}"/> "style="margin-top: 3px" width="125px" height=125px>
		  	 			<textarea class="form-control" rows="5" cols="60" id="message" placeholder='分享你的用餐經驗吧....' ></textarea>
		  	 			<button class="bg-secondary" id="addMessage" style="height:134px">送出</button>
		  	 		</div>
		  	 		<div>
		  	 		</div>
		  	 	</div>
		  	 </div>
		  	<hr>
		  	
<!-- 		  	<div class="row d-flex align-items-center justify-content-center ml-5" id="allMessageDiv"> -->
<!-- 		  	one comment  -->
<!-- 		  		<div class="container row mt-1"> 			 -->
<!-- 		  			<div class="col-2 d-flex justify-content-start">		  				 -->
<!-- 		  				<img src="https://bootdey.com/img/Content/avatar/avatar1.png" height=100px>		 	 -->
<!-- 		  			</div> -->
<!-- 		  			<div class="col-10">	 -->
<!-- 		  				<div class="d-flex justify-content-between font-weight-light"> -->
<!-- 		  					<span class="d-flex">BBB</span> -->
<!-- 		  				    <span>2021/01/01<button class="ml-1 text-dark"><i class="fas fa-thumbs-up"></i></button> -->
<!-- 		  					<button class="text-dark"><i class="fas fa-reply"></i></button></span> -->
<!-- 		  				</div> -->
<!-- 						<p>QQQ QAQ </p>	  -->
<!-- 		  			</div>			 -->
<!-- 		  	  	</div>	 -->
<!-- 		  	  -------end of one comment------- 		  	  -->
<!-- 		  	  	if have reply 		  	  	 -->
<!-- 				  	  <div class="container row"> -->
<!-- 				  				<div class="col-2"> -->
<!-- 				  				</div> -->
<!-- 				  				<div class="col-1 d-flex justify-content-start">		  				 -->
<!-- 				  					<img src="https://bootdey.com/img/Content/user_2.jpg" height=70px> -->
				  							 	
<!-- 				  				</div> -->
<!-- 				  				<div class="col-9">					  					 -->
<!-- 				  					<p>I am reply</p> -->
<!-- 				  				</div> -->
<!-- 				  	   </div>	  				  		 -->
<!-- 				   end of one reply		 -->
<!-- 				<hr> -->
<!-- 		    </div> -->

		 	<div class="row d-flex align-items-center justify-content-center mx-5" id="allMessageDiv">
		 		<div class="container border-left border-info mr-3">
		 			<div class="media">
			 			<img class="rounded border mr-3" src=" <c:url value="/userProtrait/${userAccount.userAccountDetail.useretailId}"/> " width="125px" height=125px>
			 			<div class="media-body">
			 				 <h5 class="mt-0 text-info">BBB</h5>
			 				 <span class="float-right">2021/01/01
			 				 	<button class="ml-1 text-dark"><i class="fas fa-thumbs-up"></i></button>
		  					 	<button class="text-dark"><i class="fas fa-reply"></i></button>
		  					 </span>
		 				     <p>QQQ QAQ</p>
				 			<div class="media mt-3">
				 				<img src="https://bootdey.com/img/Content/user_2.jpg" alt="...">
				 				<div class="media-body">
        							<h5 class="mt-0">Media heading</h5>
        							<p>QQQ QAQ</p>
      							</div>
			 				</div>
			 			</div>
		 			</div>
		 		</div>
		 		<hr>
		 	</div>
		 	
		  
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
 				  $( "#menuArea" ).slideToggle("slow")
 				 });
//  			let menusjson = ${menu}+"";
//  			menusjson=menusjson.split(",");
//  			let tagjson = ${tagsjson}+"";
//  			console.log("tagjson:"+tagjson)
 			
//  			createMenuArea();
//  			createTag();
//  			function createMenuArea(){
//  				let menuArea = $("#menuArea"); 
//  				console.log(menuArea)
//  				for(let i= 0;i<menusjson.length;i++){
//  					console.log(menusjson);
//  					console.log(menusjson[i]);
//  					let menuImg = document.createElement("img");
//  					let url = "http://localhost:433/PepperNoodles/rest/getMenuPicture";
//  					menuImg.src=url+"/"+menusjson[i];
//  					menuArea.append(menuImg);
 					
//  				}
//  			}
 			
//  			function createTag(){
//  				let tagArea = $("#foodTag");
//  				for(let i= 0;i<tagjson.length;i++){
//  					console.log("tagName: "+tagjson[i]);
//  					let button = document.createElement("button");
//  					button.html()=tagjson[i];
//  					tagArea.append(button);
//  				}
 				
//  			}
 			
 			
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