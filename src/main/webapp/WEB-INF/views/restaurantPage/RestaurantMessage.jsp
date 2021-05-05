<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />

<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>

<script>
$(document).ready(function(){
	findMenus();
	findAllMessage();
	$("#add").click(function(){
		$("#comMessage").val('歡迎大家來我們店用餐，10人以上建議提前一天預約呦!!!');
		$("#userMessage").val('老闆人超好，東西又好吃，直接列入口袋名單!!');
	});
	
	$("#addUserMessage").click(function(){
		var userMessage = $("#userMessage").val();
		console.log("新增使用者留言");
		if(userMessage == null || userMessage ==""){
			text="請輸入訊息...";
			$("#messageResult").css({"color":"red","font-size":"small"});
		}
		else{
			addUserMessageBox();
			findAllMessage();
			$("#userMessage").val('');
			text="";
		}
		$("#messageResult").html(text);
	});
	
	$("#addComMessage").click(function(){
		var comMessage = $("#comMessage").val();
		console.log("新增企業留言");
		if(comMessage == null || comMessage ==""){
			text="請輸入訊息...";
			$("#messageResult").css({"color":"red","font-size":"small"});
		}
		else{
			addComMessageBox();
			findAllMessage();
			$("#comMessage").val('');
			text="";
		}
		$("#messageResult").html(text);
	});
	
	
	$("#allMessageDiv").on('click','.replyMessage',function(){
		$("#a~div").toggle();
	});
	
	//回覆留言
	$("#allMessageDiv").on('click','.getReplyMessage',function(){
		var messageIdValue = $(this).next().text();
		var text=$(this).prev().val();
		userId="${userAccount.accountId}";
		userIndex="${userAccount.accountIndex}";
		console.log("回覆留言的人ID:"+userId);
		console.log("被回覆的留言的ID:"+messageIdValue);
		data ={
				"replyNetizenAccountId": userId,
				"replyMessageId": messageIdValue,
				"replyText": text,
				"time": null,
				"likeAmount": null,
				"userAccount": null,
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
				findAllMessage();
			},
	        error: function (thrownError) {
				console.log(thrownError);
			}
		});
	});
	
	//刪除留言
	$("#allMessageDiv").on('click','button[name^="deleteMessage"]',function(){
		if (confirm('確定刪除此筆評論? ')) {
			var urls = "/PepperNoodles/deleteMessage/";
			var id =$(this).next().text();
			console.log("要刪除的ID:"+id);
			urls += id;
			$.ajax({
				type:"GET",
				url: urls ,
				dataType: "text",
				success: function (result) {
					findAllMessage();
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		}
    	return false;
	});
	
	//刪除回覆留言
	$("#allMessageDiv").on('click','button[name^="deleteReplyMessage"]',function(){
		if (confirm('確定刪除此筆回覆? ')) {
			var urls = "/PepperNoodles/deleteReplyMessage/";
			var id =$(this).next().text();
			console.log("要刪除的回覆ID:"+id);
			urls += id;
			$.ajax({
				type:"GET",
				url: urls ,
				dataType: "text",
				success: function (result) {
					findAllMessage();
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		}
    	return false;
	});

});
	
//新增訊息-使用者
function addUserMessageBox(){
	urls  = "/PepperNoodles/addRestaurantMessage";
	console.log(urls);
	console.log("新增使用者留言的function");
	data = new FormData();
	data.append('restMessageInfo',new Blob([JSON.stringify( {"netizenAccount":"${userAccount.accountIndex}",
															 "restaurantId": ${rest.restaurantId},
		   											   		 "text": $("#userMessage").val()
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

//新增訊息-店家
function addComMessageBox(){
	urls  = "/PepperNoodles/addRestaurantMessage";
	console.log(urls);
	console.log("新增企業留言的function");
	data = new FormData();
	data.append('restMessageInfo',new Blob([JSON.stringify( {"netizenAccount":"${comDetail.userAccount.accountIndex}",
															 "restaurantId": ${rest.restaurantId},
		   											   		 "text": $("#comMessage").val()
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
			console.log(allMessage.length);
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
	        		
	        		text +="<div class='row d-flex align-items-center justify-content-center mx-5'>";
					text +="	<div class='container border-left border-info mr-3' >";
					text +="		<div class='media'>";
					if(allMessage[i].userAccount.userAccountDetail == null){
					text +="			<img class='rounded border mr-3' src=' <c:url value='/getComPicture/"+allMessage[i].userAccount.companyDetail.companyDetailId+"'/> ' width='125px' height=125px>";
					text +="			<div class='media-body'>";
// 					text +="				<h5 class='mt-0' style='color:#BB5E00;'><img style='margin:0' height=24px src='<c:url value='/images/company/shop.png' />'>  "+allMessage[i].userAccount.companyDetail.realname;
					text +="				<h5 class='mt-0' style='color:#BF0060;'><i class='fas fa-home'></i>  "+allMessage[i].userAccount.companyDetail.realname;
						if(allMessage[i].userAccount.companyDetail.companyDetailId=="${comDetail.companyDetailId}" ){
					text +="					<button class='ml-2 mb-1 close' data-dismiss='modal' aria-label='Close' name='deleteMessage"+allMessage[i].restaurantMessageId+"'>&times;</button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
						}
					text +="				</h5>";
					text +="				<span class='float-right'>"+formatPrint;
					text +="					<button class='ml-1 text-dark'><i class='fas fa-thumbs-up'></i></button>";
					text +="					<button class='text-dark replyMessage' data-toggle='collapse' data-target='#collapseExample"+i+"' ><i class='fas fa-reply' >回覆</i></button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
					text +="				</span>";
					text +="				<br>";
					text +="				<div class='ml-5'>"+allMessage[i].text+"</div>";
					text +="			</div>";
					text +="		</div>";
					}
						else{
					text +="			<img class='rounded border mr-3' src=' <c:url value='/userProtrait/"+allMessage[i].userAccount.userAccountDetail.useretailId+"'/> ' width='125px' height=125px>";
					text +="			<div class='media-body'>";
					text +="				<h5 class='mt-0' style='color:#005AB5;'><i class='fas fa-user'></i> "+allMessage[i].userAccount.userAccountDetail.nickName;
							if("${userAccount.userAccountDetail.useretailId}" == allMessage[i].userAccount.userAccountDetail.useretailId){
					text +="					<button class='ml-2 mb-1 close' data-dismiss='modal' aria-label='Close' name='deleteMessage"+allMessage[i].restaurantMessageId+"'>&times;</button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
							}
					text +="				</h5>";
					text +="				<span class='float-right'>"+formatPrint;
					text +="					<button class='ml-1 text-dark'><i class='fas fa-thumbs-up'></i></button>";
					text +="					<button class='text-dark replyMessage' data-toggle='collapse' data-target='#collapseExample"+i+"' ><i class='fas fa-reply' >回覆</i></button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
					text +="				</span>";
					text +="				<br>";
					text +="				<div class='ml-5'>"+allMessage[i].text+"</div>";
					text +="			</div>";
					text +="		</div>";
						}
					//回覆留言的div
					text +="		<div class='media collapse ml-5' id='collapseExample"+i+"'>";
					text +="  			<input type='text' size='100%' class='form-control ml-5' placeholder='回覆...'>";
					text +="			<button class='bg-secondary getReplyMessage' style='height:38px'>reply</button><span style='display:none'>" + allMessage[i].restaurantMessageId + "</span>";
					text +="		</div>";
					text +="	</div>";
					text +="</div>";
					text +="<br>";
					text +="<div class='row flex-row-reverse mx-5' id='reply'>";
					for( j =0; j<allMessage[i].restaurantMessage.length; j++){
						var formatReplyDate   =(new Date(allMessage[i].restaurantMessage[j].time)).toString().substring( 4 , 21 );
	        			var formatReplyString = formatDate.split(' ');
	        			var formatReplyPrint  = formatString[0]+ '/' + formatString[1] + '/'+ formatString[2] + '&emsp;' +formatString[3];
	        		text +="	<div class='media mr-5 border-left border-danger mt-1' style='width: 80%'>";
						if(allMessage[i].restaurantMessage[j].userAccount.userAccountDetail == null){
					text +="		<img class='rounded border mr-3'  src=' <c:url value='/getComPicture/"+allMessage[i].restaurantMessage[j].userAccount.companyDetail.companyDetailId+"'/> ' width='125px' height=125px>";
					text +="		<div class='media-body'>";
					text +="			<h6 class='mt-0' style='color:#F00078;'><i class='fas fa-home'></i> "+allMessage[i].restaurantMessage[j].userAccount.companyDetail.realname;
							if("${comDetail.companyDetailId}" == allMessage[i].restaurantMessage[j].userAccount.companyDetail.companyDetailId){
					text +="				<button class='ml-2 mb-1 close' data-dismiss='modal' aria-label='Close' name='deleteReplyMessage"+allMessage[i].restaurantMessage[j].restaurantReplyId+"'>&times;</button><span style='display:none'>" + allMessage[i].restaurantMessage[j].restaurantReplyId + "</span>";
							}
					text +="			</h6>";
						}
						else{
					text +="		<img class='rounded border mr-3'  src=' <c:url value='/userProtrait/"+allMessage[i].restaurantMessage[j].userAccount.userAccountDetail.useretailId+"'/> ' width='125px' height=125px>";
					text +="		<div class='media-body'>";
					text +="			<h6 class='mt-0' style='color:#5A5AAD;'><i class='fas fa-user'></i> "+allMessage[i].restaurantMessage[j].userAccount.userAccountDetail.nickName;
							if("${userAccount.userAccountDetail.useretailId}" == allMessage[i].restaurantMessage[j].userAccount.userAccountDetail.useretailId){
					text +="				<button class='ml-2 mb-1 close' data-dismiss='modal' aria-label='Close' name='deleteReplyMessage"+allMessage[i].restaurantMessage[j].restaurantReplyId+"'>&times;</button><span style='display:none'>" + allMessage[i].restaurantMessage[j].restaurantReplyId + "</span>";
							}
					text +="			</h6>";
						}
					text +="				<span class='float-right'>"+formatReplyPrint;
					text +="				</span>";
					text +="				<br>";
					text +="			<div class='ml-5'>"+allMessage[i].restaurantMessage[j].replyText+"</div>";
					text +="		</div>";
					text +="	</div>";
					}
					text +="	<hr>";
					text +="</div>";
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
	 background-color:#f7f7f7;
	 height: 1px;
}
</style>
</head>
<body>
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
	  		<div class="col-sm-7 mt-30">
				<table class="table">
					<thead>
						<tr>
					    	<th colspan="2" scope="col"><i class='fas fa-home'></i> ${rest.restaurantName}</th>			
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
					      		<div id="${rest.restaurantId}" name="restid"></div>
					        </td>
					    </tr>
					    <tr>
					    	<th scope="row">收藏</th>
					      	<td id="collect">
					      		<button type="button" id="collectbutton" class="text-primary" >新增收藏</button> <button type="button" id="collectbuttonCancel" class="text-primary" >取消收藏</button>
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
	  			<a id="add">一鍵新增評論</a>
	  		</div>
	  		<br>
	    </div>	
		<c:choose>
 			<c:when test="${userAccount==null}">
				<div class="row">
	  		 		<div class="container  p-8">
	  	 				<div class="media mx-5">
		  	 				<img class="rounded border" src=" <c:url value="/getComPicture/${comDetail.companyDetailId}"/> "style="margin-top: 3px" width="125px" height=125px>
	  	 					<textarea class="form-control" rows="5" cols="60" id="comMessage" placeholder='發表您的評論吧....' ></textarea>
	  	 					<button class="bg-secondary" id="addComMessage" style="height:134px">送出</button>
	  	 				</div>
	  	 				<div>
	  	 				</div>
	  	 			</div>
				</div>
 			</c:when>
 			<c:otherwise>
	  	 		<div class="row">
	  	 			<div class="container  p-8">
	  	 				<div class="media mx-5">
		  	 				<img class="rounded border" src=" <c:url value="/userProtrait/${userAccount.userAccountDetail.useretailId}"/> "style="margin-top: 3px" width="125px" height=125px>
	  	 					<textarea class="form-control" rows="5" cols="60" id="userMessage" placeholder='分享你的用餐經驗吧....' ></textarea>
	  	 					<button class="bg-secondary" id="addUserMessage" style="height:134px">送出</button>
	  	 				</div>
	  	 				<div>
	  	 				</div>
	  	 			</div>
	  	 		</div>
 			</c:otherwise>
 		</c:choose>
 		<div class="ml-5" style="padding-left: 150px;" id="messageResult"></div>
	  	<hr>
			<div  id="allMessageDiv">
	<!-- 大迴圈結束 -->
<!-- 		 	<div class="row d-flex align-items-center justify-content-center mx-5"> -->
<!-- 		 		<div class="container border-left border-info mr-3"> -->
<!-- 		 			<div class="media"> -->
		 			
<%-- 			 			<img class="rounded border mr-3" src=" <c:url value="/userProtrait/${userAccount.userAccountDetail.useretailId}"/> " width="125px" height=125px> --%>
<!-- 			 			<div class="media-body"> -->
<!-- 			 				 <h5 class="mt-0 text-info">BBB</h5> -->
<!-- 			 				 <span class="float-right">2021/01/01 -->
<!-- 			 				 	<button class="ml-1 text-dark"><i class="fas fa-thumbs-up"></i></button> -->
<!-- 		  					 	<button class="text-dark" data-toggle="collapse" data-target="#collapseExample"><i class="fas fa-reply"></i></button> -->
<!-- 		  					 </span> -->
<!-- 		 				     <p>QQQ QAQ</p> -->
<!-- 		 				</div>   -->
<!-- 		 			</div>	      -->
<!-- 		 		回覆留言的div	    -->
<!-- 		 			<div class='media collapse ml-5' id='collapseExample'> -->
<!-- 		 				<input type='text' size='100%' class='form-control ml-5' placeholder='回覆...'> -->
<!-- 		 				<button class='bg-secondary getReplyMessage' style='height:38px'>reply</button> -->
<!-- 		 			</div>   -->
<!-- 		 		</div> -->
<!-- 		 	</div> -->
<!-- 		 	<br> -->
<!-- 		 	<div class="row flex-row-reverse mx-5 "> -->
<!-- 		 	內迴圈 -->
<!-- 				<div class='media mr-5 border-left border-danger mt-1' style='width: 80%'> -->
<!-- 					<img  class="rounded border mr-3" src='https://bootdey.com/img/Content/user_2.jpg'  width="125px" height=125px> -->
<!-- 					<div class='media-body'> -->
<!--         				<h5 class='mt-0'>Media heading</h5> -->
<!--         				<p>QQQ QAQ</p> -->
<!--       				</div> -->
<!-- 		 		</div> -->
<!-- 		 	內迴圈結束 -->
<!-- 		 	<hr> -->
<!-- 		 	</div>	 -->
		 	<!-- 大迴圈結束 -->
			</div>
		</div>
		<br>
	</div>
<script>
	$(window).on('load', function() {
		checkCollectionButton();

		
		$("#toggleMenu").click(function() {
      	  $( "#menuArea" ).slideToggle("slow")
 		});
 			
// 		//讓bar固定在上面以及設定高度
		$(".header-sticky").addClass("sticky-bar");
 		$(".header-sticky").css("height", "90px");
		$(".header-sticky").css("position","static")

		//讓loading圖動起來
 		$('#preloader-active').delay(450).fadeOut('slow');
 		$('body').delay(450).css({
 			'overflow' : 'visible'
 		});		
 		
 		//抓餐廳tag
		let n = $("div[name='restid']");
		//         console.log($("input[name='restid']"));
		console.log("--------------------"+n.length);
		console.log(n[0].id);
// 		for (let i = 0; i < n.length; i++) {
		var urls  = "${pageContext.request.contextPath}/";
			urls += "<c:url value='restTag2/'/>" + n[0].id;
		// 		console.log(urls);
		$.ajax({
				type : "GET",
				url : urls,
				dataType : "text",
				success : function(response) {
					var divFoodTag = document.getElementById(n[0].id);
					var jsontxt = JSON.parse(response);
					for (i = 0; i < jsontxt.length; i++) {
						$(divFoodTag).append("<i style='color:#EA0000;' class='fas fa-tag'> "+jsontxt[i].foodTagName+"</i>&emsp;");
					}
				},
				error : function(thrownError) {
					console.log(thrownError);
				}
			});
// 		}
			
	});
	
	
	$('body').on('click','button[id^="collectbutton"]' ,function(e){
		e.preventDefault;
		var urls = '/PepperNoodles/user/addRestaurantCollection?resID=';
		var resID = '${rest.restaurantId}' ;
		urls += resID;
		$.ajax({
			
			method:'GET',
			url:urls,
			dataType:'text',
			success:function(result){
				alert(result);
				$('#collectbutton').toggle();
				$('#collectbuttonCancel').toggle();
			},
			error:function(result){
				alert(result);

			}
			
		});
		
	});
	
	function checkCollectionButton() {
		var collectionButton = $('#collectbutton');
		var collectionButtonCancel = $('#collectbuttonCancel');
		var urls = '/PepperNoodles/user/checkRestaurantCollection?resID=';
		var resID = '${rest.restaurantId}' ;
		urls += resID;

	$.ajax({
		method:'GET',
		url: urls,
		dataType: 'text',
		success:function(result){
			if(result=='true'){
				alert('已經有收藏了!! ');

				$('#collectbutton').hide();
				$('#collectbuttonCancel').show();
			}else{
				alert('沒有收藏!! ');

				$('#collectbutton').show();
				$('#collectbuttonCancel').hide();
				
			}
		},
		error:function(result){
		alert(result);
		}
	});

};
	
</script>
</body>
</html>