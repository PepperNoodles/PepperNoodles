<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View User</title>
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
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
	<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>
	<script src="<c:url value='/scripts/popper.min.js' />"></script>
	<script type="text/javascript"
	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
	
	
	
	<script type="text/javascript">

	$(window).on('load', function() {
			
		let urlss="${pageContext.request.contextPath}/";
		urlss+="<c:url value='userLoggin/getName'/>";
		console.log(urlss);
		
		$.ajax({
			type: "GET",
			url: urlss,	
// 			async:true
			dataType: "text",
			success: function (response) {
				console.log(response);	
			},
			error: function (thrownError) {
				console.log(thrownError);
			}
			
		});
		
	});

</script>

<style>
	#body{
	min-height: 100vh;
    background-image: url(
    "https://images.unsplash.com/photo-1483137140003-ae073b395549?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80"
    );
    background-size: cover;
    background-position: center center;
    background-attachment: fixed;
	}
	#main{
	background-color:#FCFCFC		;
	}
	.butt{
	height:60px;
	width:105px;
	}
	.friendsysImg {
	height: 120px; /*can be anything*/
	width: 160px; /*can be anything*/
	position: relative;
	}

	.friendsysImg img {
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
	.friendList a{
	color:#000000;
	}
	.friendList a:hover{
	color:blue;
	}
	
	tr:hover{
	background-color: 	#E0E0E0;
	}
	
</style>
</head>
<body id="body" >
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

	<div id="main" class="container mt-5" style="width: 80%; min-height: 100vh">
<%-- 			<h5>我是被看的喔,現在登入的 ${userAccount.accountIndex}</h5> --%>
 			<h5>您好${userAccount.accountIndex},我是:</h5>
			<!-- 圖片+姓名bar-->
			<div class="d-flex justify-content-start">
				<div class="p-2 bg-secondary">
					
					<img style="height: 100px" src="<c:url value='/userProtrait/${viewUserAccountDetail.useretailId}'/>">
				</div>

				<div id="nickname" class="p-2 flex-fill align-self-end ">
					<h1>${viewUserAccountDetail.nickName}</h1>
				</div>

				<div id="" class="align-self-end justify-content-start">
					<button class="butt" style="color:#4a4aFF" id="AddFriendChecker">加好友</button>
				</div>
				

			</div>
			<div class="d-flex ml-5 mt-3 bg-secondary" style="height: 5px" >
				
			</div>
				<div class="d-flex mt-1 mr-5 bg-info" style="height: 5px" >
				
			</div>
			
			
			<div class="d-flex mt-3" style="min-height: 70vh">
 				<div class="nav flex-column nav-pills p-2 bg-dark" id="nav-tab" role="tablist" aria-orientation="horizontal">		
				
					<a class="nav-link active" id="v-pills-aboutme-tab" data-toggle="pill" href="#v-pills-aboutme" role="tab" aria-controls="v-pills-aboutme" aria-selected="false"><i class="fas fa-file-alt"></i>關於我</a>							
				
					<a class="nav-link " id="v-pills-friend-tab" data-toggle="pill" href="#v-pills-friend" role="tab" aria-controls="v-pills-friend" aria-selected="true"><i class="fas fa-users"></i>好友</a>	
				
					<a class="nav-link" id="v-pills-comments-tab" data-toggle="pill" href="#v-pills-comments" role="tab" aria-controls="v-pills-comments" aria-selected="false"><i class="fas fa-comments"></i>留言區</a>							
								
					<a class="nav-link" id="v-pills-collection-tab" data-toggle="pill" href="#v-pills-collection" role="tab" aria-controls="v-pills-collection" aria-selected="false"><i class="fas fa-heart"></i>收藏區</a>								
				
				</div>
				
			
			
			<div class="tab-content" id="v-pills-tabContent col-9">
			<div class="tab-pane fade " id="v-pills-friend" role="tabpanel" aria-labelledby="v-pills-friend-tab">
				<h2>${viewUserAccountDetail.nickName}的好友</h2>
				<div class="friendList m-3" id="userFriendList" style="height:400px;overflow-x:hidden;overflow-y:auto;">
					
				
				</div>
				
				

			</div>	

			<div class="tab-pane fade show active mx-5" id="v-pills-aboutme" role="tabpanel" aria-labelledby="v-pills-aboutme-tab">
				<h2>基本資料</h2>
				<table class="table ">
					 
					  <tbody>
					    <tr>					    
					      <td>email:</td>
					      <td>${viewUserAccount.accountIndex}</td>					    
					    </tr>
					    <tr>					      
					      <td>暱稱:</td>
					      <td>${viewUserAccountDetail.nickName}</td>					   
					    </tr>
					    <tr>					     
					      <td>生日:</td>
					      <td>${viewUserAccountDetail.birthDay}</td>
					    </tr>
					    <tr>					     
					      <td>性別:</td>
					      <td>${viewUserAccountDetail.gender}</td>
					    </tr>
					    <tr>					     
					      <td>地區:</td>
					      <td>${viewUserAccountDetail.location}</td>
					    </tr>
					  </tbody>
					</table>
				
				
<%-- 				<p>email: ${viewUserAccount.accountIndex} </p> --%>
<%-- 				<p>暱稱：${viewUserAccountDetail.nickName}</p> --%>
<%-- 				<p>生日：${viewUserAccountDetail.birthDay}</p> --%>
<%-- 				<p>性別：${viewUserAccountDetail.gender}</p> --%>
<%-- 				<p>地區：${viewUserAccountDetail.location}</p> --%>
			</div>	
			
			<div class="tab-pane fade" id="v-pills-comments" role="tabpanel" aria-labelledby="v-pills-aboutme-tab">
										<h2>${viewUserAccount.userAccountDetail.nickName}的留言區</h2>


						<iframe allowtransparency="" frameborder="0" id="fbLike" scrolling="no" src=""	style="border-bottom: medium none; border-left: medium none; width: 250px; height: 30px; overflow: hidden; border-top: medium none; border-right: medium none"></iframe>


						<!-- 						新增主要留言input & 按鈕 -->
						<input placeholder='Hello....' id="commentInput"></input>
						<button type="button" class="genric-btn default circle arrow"
							id="addNewComment">新增留言</button><span style="display:none" id="messagePrompt"></span>

						<!-- 						使用Ajax的方法 -->
						<div class="table rounded border border-warning container-fluid table"
							style="overflow-y: auto; height:400px;" id="commentsForViewUser"
							></div>
			</div>	
			
			<div class="tab-pane fade" id="v-pills-collection" role="tabpanel" aria-labelledby="v-pills-collection-tab">
										<h2>${viewUserAccount.userAccountDetail.nickName}的餐廳收藏</h2>
			<div class="memoBoard" id="memoBoard"
			>
			</div>
			</div>
			</div>
	</div>
	
	
	
	</div>

	<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>
	
	
						  	<div aria-live="polite" data-autohide="true" aria-atomic="true" style="position: relative; min-height: 200px;">
								  <div class="toast" style="position: fixed;top:20%;left:50%; ">
								    <div class="toast-header">
								      <strong class="mr-auto">貼心提醒</strong>
								      <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
								        <span aria-hidden="true">&times;</span>
								      </button>
								    </div>
								    <div class="toast-body">
								    	<p></p>
								    </div>
								  </div>
							</div>

	<script>
 		$(window).on('load', function() {
			showAllComments();
 			createOthersSideMemo();
 			checkFriendList();
 			//
			$("#v-pills-aboutme-tab").addClass("active");
 			//讓bar固定在上面以及設定高度
			$(".header-sticky").addClass("sticky-bar");
 			$(".header-sticky").css("height", "90px");
			$(".header-sticky").css("position","static")

 			//讓loading圖動起來
 			$('#preloader-active').delay(450).fadeOut('slow');
 			$('body').delay(450).css({
 				'overflow' : 'visible'
 			});			
			
 			judgeRelation();
			
 			//判斷關係決定右上角按鈕
			function judgeRelation(){
				let mainUser = "${userAccount.accountIndex}";
				let viewUser = "${viewUserAccount.accountIndex}";
				let urls="${pageContext.request.contextPath}/";
					urls+="<c:url value='judgeFriendShip'/>";
					urls+="?userIndex="+mainUser+"&friendIndex="+viewUser;
				//	console.log(urls);
			$.ajax({
					type: "GET",
					url: urls,							
					dataType: "text",
					success: function (response) {		
					if (response =="noFriendShip"){
						$("#AddFriendChecker").html("addFriend");
						$("#AddFriendChecker").on("click",sendFriendRequest);
					}else if(response == "wait_check"){
						$("#AddFriendChecker").html("RequestSended");
					}else if(response =="friend"){
						$("#AddFriendChecker").html("Friend");
					}else if(response == "reject"){
						$("#AddFriendChecker").html("rejected");
					}else{
						console.log("somethingWrong")
					}
					},
						error: function (thrownError) {
						console.log(thrownError);
					}
	    			});
			}
			
 			//好友列表
			function checkFriendList(){
				let urls="${pageContext.request.contextPath}/";
				urls+="<c:url value='findMainfriend'/>";
				names="${viewUserAccount.accountIndex}";
				urls+="/"+names;
				console.log(urls);
				$.ajax({
						type: "GET",
						url: urls,				
						dataType: "json",
						success: function (response) {
							console.log(response);
							showSearchList(response,"#userFriendList");
						},
						error: function (thrownError) {
							console.log(thrownError);
						}
					});
		
				};
 			//顯示table
				function showSearchList(response,id){
					$(id).html("");
					//let result = JSON.stringify(response);
					//console.log(response[0]);
					let table =  document.createElement("table");
					table.border="1";
					let trh = document.createElement("tr");
						let th1 = document.createElement("th");
						th1.innerHTML= "照片"
						let th2 = document.createElement("th");
						th2.innerHTML = "帳號"
						let th3 = document.createElement("th");
						th3.innerHTML = "名稱"
						trh.appendChild(th1);
						trh.appendChild(th2);
						trh.appendChild(th3);
						table.appendChild(trh);
					
					let length = Object.keys(response).length;
					for (let i =0;i< length;i++){
						let tr  = document.createElement("tr");
						let td1 = document.createElement("td");
						let td2 = document.createElement("td");
						let td3 = document.createElement("td");
						let img = document.createElement("img");
						
						let divFrame = document.createElement("div");
						divFrame.className="friendsysImg";
						
						img.class="tdimg";
						let imgSrc="${pageContext.request.contextPath}/userProtrait/"+response[i].userAccountDetail.useretailId;
						img.src="<c:url value='"+imgSrc+"'/>";
						
						divFrame.append(img);
						
						td3.appendChild(divFrame);
						
						td1.innerHTML=response[i].accountIndex;
						console.log(response[i].accountIndex);
						console.log(response[i].userAccountDetail);								
						//幫nickname建立連結
						let a =document.createElement("a");
						a.href="${pageContext.request.contextPath}/userView/"+response[i].accountIndex;
						a.innerText =response[i].userAccountDetail.nickName;								
						td2.appendChild(a);
						//放圖,放account,放nickName
						tr.appendChild(td3);
						tr.appendChild(td1);
						tr.appendChild(td2);
						table.appendChild(tr);
					}
					$(id).append(table);
					
				}
				
				
			
			//按下送出邀請
			function sendFriendRequest(){
				//alert("送出邀請");
				let mainUser = "${userAccount.accountIndex}";
				let viewUser = "${viewUserAccount.accountIndex}";
				let urls="${pageContext.request.contextPath}/";
					urls+="<c:url value='addfriend'/>";
					urls+="/"+mainUser+"/"+viewUser;
				$.ajax({
					type: "GET",
					url: urls,							
					dataType: "text",
					success: function (response) {						
						if(response=="request is sending"){
							alert(response);
							judgeRelation();
						}else if(response=="request is already send"){
							alert(response);
						}
				
						
					},
						error: function (thrownError) {
						console.log(thrownError);
					}
	    			});
			}
 		});
 		
//			顯示留言
		function showAllComments(){
	var commentsForViewUser = document.getElementById("commentsForViewUser")
	var viewUserAccount = "${viewUserAccount.accountIndex}";
	var urls = "/PepperNoodles/user/showOtherPageAllCommentAjax?viewUserAccount=";
	urls += viewUserAccount;
	var segment="";
	commentsLength = 0;
	$.ajax({
		method:"GET",
		url:urls,

        success: function (result) {
//     		alert(result.length);
    		commentsLength =result.length;
//        		alert(result[0].replyMessageBoxes);
    		console.log(JSON.stringify(result));
//     		console.log(JSON.stringify(result[0].time));
    		 var aDay = new Date();
    		 aDay.setHours(9);
    		 aDay.setMinutes(0);
    		 aDay.setSeconds(0);
    		 var sun = result[0].time;
    		
        	for( i =0; i<result.length; i++){
        		
        		var formatDate   =(new Date(result[i].time)).toString().substring( 4 , 21 );
        		var formatString = formatDate.split(' ');
        		if(formatString[0]=="Jan"){
        			formatString[0] = "1";
        		}else if(formatString[0]=="Feb"){
        			formatString[0] = "2";
        		}else if(formatString[0]=="Mar"){
        			formatString[0] = "3";
        		}else if(formatString[0]=="Apr"){
        			formatString[0] = "4";
        		}else if(formatString[0]=="May"){
        			formatString[0] = "5";

        		}else if(formatString[0]=="Jun"){
        			formatString[0] = "6";
        		}else if(formatString[0]=="Jul"){
        			formatString[0] = "7";
        		}else if(formatString[0]=="Aug"){
        			formatString[0] = "8";
        		}else if(formatString[0]=="Sep"){
        			formatString[0] = "9";
        		}else if(formatString[0]=="Oct"){
        			formatString[0] = "10";
        		}else if(formatString[0]=="Nov"){
        			formatString[0] = "11";
        		}else if(formatString[0]=="Dec"){
        			formatString[0] = "12";
        		}else {
        			formatString[0] = "";
        		}
//	        		formatString[2] + '<br>' +
        		var formatPrint  =  formatString[0]+ '/' + formatString[1] +  '<br>' + formatString[3];
        		
        		segment +="<table border='1' class='table table-hover table-bordered ' style='font-size: 8px border-collapse:separate; border:solid blue 1px;border-radius:6px;-moz-border-radius:6px;'>";//<th>留言數</th><th>留言者</th><th>時間</th><th>讚數</th><th>留言內容</th><th>讚</th><th>編輯</th>";
        		segment += "<tr class='table-primary'><td>留言" + (i+1) + "</td><td>" ;
        		segment += result[i].netizenAccount.userAccountDetail.nickName + "</td><td >" ;
        		segment += formatPrint + "</td><td id='likeof" + i +"'>" ;
        		segment += result[i].likeAmount + "</td><td><input  class='selectedinput ' disabled='disabled'size='20' value='" + result[i].text +"'>" ;
    			segment +=  "</input><button  name='updateComment' "+ i +" class='genric-btn default circle arrow' style='display:none;color:black' >confirm</button>";
    			segment += "<span  style='display:none;visibility:hidden'>" + result[i].text + "</span>";
    			segment += "<span  style='display:none'>" + result[i].time + "</span><span  style='display:none'>" + result[i].likeAmount + "</span>";
    			segment += "<span  style='display:none'>" + result[i].userMessageId + "</span></td><td>";
    			segment += "<button type='button' class='genric-btn default circle arrow' style='color: black' name  ='likeButton" + i +"' >like</button><button type='button' class='genric-btn default circle arrow' style='color: black ;display:none' name  ='dislikeButton" + i +"' >withdraw</button></td>";
    			segment += "<td><button class='genric-btn default circle arrow' style='color: black' name='edit" + i + "'>edit</button></td><td>	";
    			segment +="<button class='genric-btn default circle arrow' type='button'  style='color: black' name  ='deleteComment"  + i +"' >delete</button><span  style='display:none'>" + result[i].userMessageId + "</span></tr>"

					for( j =0; j<result[i].replyMessageBoxes.length; j++){
					
						var formatDate   =(new Date(result[i].replyMessageBoxes[j].time)).toString().substring( 4 , 21 );
		        		var formatString = formatDate.split(' ');
		        		if(formatString[0]=="Jan"){
		        			formatString[0] = "1";
		        		}else if(formatString[0]=="Feb"){
		        			formatString[0] = "2";
		        		}else if(formatString[0]=="Mar"){
		        			formatString[0] = "3";
		        		}else if(formatString[0]=="Apr"){
		        			formatString[0] = "4";
		        		}else if(formatString[0]=="May"){
		        			formatString[0] = "5";

		        		}else if(formatString[0]=="Jun"){
		        			formatString[0] = "6";
		        		}else if(formatString[0]=="Jul"){
		        			formatString[0] = "7";
		        		}else if(formatString[0]=="Aug"){
		        			formatString[0] = "8";
		        		}else if(formatString[0]=="Sep"){
		        			formatString[0] = "9";
		        		}else if(formatString[0]=="Oct"){
		        			formatString[0] = "10";
		        		}else if(formatString[0]=="Nov"){
		        			formatString[0] = "11";
		        		}else if(formatString[0]=="Dec"){
		        			formatString[0] = "12";
		        		}else {
		        			formatString[0] = "";
		        		}
//			        		formatString[2] + '<br>' +
		        		var formatPrint  =  formatString[0]+ '/' + formatString[1] +  '<br>' + formatString[3];
					
						segment += "<tr class='table-info'><td>回覆" + (j+1) + "</td><td>" ;
		        		segment += result[i].replyMessageBoxes[j].netizenAccount.userAccountDetail.nickName  + "</td><td>" ;
		        		segment += formatPrint  + "</td><td id='likeof" + i + j +"'>" ;
		        		segment += result[i].replyMessageBoxes[j].likeAmount + "</td><td>";
		        		segment += "<input disabled='disabled'size='20' value='" + result[i].replyMessageBoxes[j].text +"'>" ;
						segment +=  "</input><button name='updateComment' " + i + j +" style='display:none;color:black' class='genric-btn default circle arrow' >confirm</button>";
	        			segment += "<span  style='display:none;visibility:hidden'>" + result[i].replyMessageBoxes[j].text + "</span>";
	        			segment += "<span  style='display:none'>" + result[i].replyMessageBoxes[j].time + "</span><span  style='display:none'>" + result[i].replyMessageBoxes[j].likeAmount + "</span>";
	        			segment += "<span  style='display:none'>" + result[i].replyMessageBoxes[j].userMessageId + "</span></td><td>";
	        			segment += "<button type='button' class='genric-btn default-border circle' style='color: black' name  ='likeButton" + i + j +"' >like</button><button type='button' class='genric-btn default circle arrow' style='color: black ;display:none' name  ='dislikeButton"  + i + j +"' >withdraw</button></td>";
	        			segment += "<td><button class='genric-btn default circle arrow' style='color: black' name='edit"  + i + j + "'>edit</button></td><td>";
	        			segment +="<button class='genric-btn default circle arrow' type='button' style='color: black' name  ='deleteComment" + i + j +	"' >delete</button><span  style='display:none'>" + result[i].replyMessageBoxes[j].userMessageId + "</span></tr>";
					}
        		segment += "</table><span class='mt-0 pt-0'><input placeholder='我想吃....'></input><button type='button' class='genric-btn default circle arrow' style='color: black' name='addreply"  + i +"' >reply</button><span  style='display:none'>" + result[i].userMessageId + "</span></span><br><br><br>";
        		;
//	        		segment +="<button data-toggle='modal' data-target='#exampleModal' type='button' style='color: black' name  ='updateComment" + i +"' >更新主要留言</button><span  style='display:none'>" + result[i].text + "</span><span  style='display:none'>" + result[i].time + "</span><span  style='display:none'>" + result[i].likeAmount + "</span><br><br><hr>";
        	
        	}
        	commentsForViewUser.innerHTML = segment;
        	
        },
        error: function (result) {
        	alert(result);
            $("#commentsForViewUser").text("fail"); //填入提示訊息到result標籤內
        }
	});
		}
		
		//留言滑到底的時候顯示
		  $("#commentsForViewUser").scroll(function(){
			    var h = $(this).height();//div可视区域的高度
			    var sh = $(this)[0].scrollHeight;//滚动的高度，$(this)指代jQuery对象，而$(this)[0]指代的是dom节点
			    var st =$(this)[0].scrollTop;//滚动条的高度，即滚动条的当前位置到div顶部的距离
			    if(h+st>=sh){
			    //上面的代码是判断滚动条滑到底部的代码
			      //alert("滑到底部了");
			      $("#commentsForViewUser").append("<i>沒有留言囉~~</i>"+"<br>");//滚动条滑到底部时，只要继续滚动滚动条，就会触发这条代码.一直滑动滚动条，就一直执行这条代码。
			    }
			  })
		
		
		//新增留言(usermain頁面的 useraccount 之後會抓預設的值)
		$("#addNewComment").on('click',function(){
			
			var commentValue = document.getElementById("commentInput").value;
			var viewUserAccount = "${viewUserAccount.accountIndex}";

			var urls="/PepperNoodles/user/addNewOtherCommentAjax/?comment="+commentValue +"&viewUserAccount="+ viewUserAccount;

			$.ajax({
				type:"GET",
				url:  urls,
				dataType: "text",
				success: function (result) {
// 					alert(result);
					showAllComments();
					$('.toast-body p').text(result);
					$('.toast').toast({delay: 900});
					$('.toast').toast('show');
					
					$("#messagePrompt").text(result);
					$("#messagePrompt").css("color", "#007500");
					$("#messagePrompt").toggle();
					setTimeout(function() { $("#messagePrompt").hide(); }, 900);
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		});
		
		//新增回覆留言()
		$('body').on('click','button[name^="addreply"]',function(e){
			
			var messageIdValue = $(this).next().text();
			var urls           ="/PepperNoodles/user/addNewReplyCommentAjax/" + messageIdValue;
			var text           =$(this).prev().val();
// 			alert(messageIdValue);
// 			alert(text);
			
			var data =
			{
					"userMessageId": null,
							 "text": text,
						     "time": null,
					   "likeAmount": null,
				   "netizenAccount": null,
					  "userAccount": null,
			           "messageBox": null,
			    "replyMessageBoxes": null
			};
//				+ "&useraccount=chrislo5311@gmail.com"
			$.ajax({
				type:"POST",
				url:  urls,
				contentType:'application/json;charset=UTF-8',
				dataType: "text",
				data:JSON.stringify(data),
				
				success: function (result) {
// 					alert(result);
					showAllComments();
					
					$('.toast-body p').text(result);
					$('.toast').toast({delay: 900});
					$('.toast').toast('show');
					
					$("#messagePrompt").text(result);
					$("#messagePrompt").css("color", "#007500");
					$("#messagePrompt").toggle();
					setTimeout(function() { $("#messagePrompt").hide(); }, 900);
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		});
		
		//delete 留言
		$('body').on('click','button[name^="deleteCo"]',function(e){
			e.preventDefault;
			var urls = "/PepperNoodles/user/deleteCommentAjax?id=";
			var id =$(this).next().text();
// 			alert(id);
			urls += id;
			$.ajax({
				type:"GET",
				url: urls ,
				dataType: "text",
				success: function (result) {
// 					alert(result);
					showAllComments();
					if(result.length<7){
						
						$('.toast-body p').text(result);
						$('.toast').toast({delay: 900});
						$('.toast').toast('show');
						
						$("#messagePrompt").text(result);
						$("#messagePrompt").css("color", "#007500");
						$("#messagePrompt").toggle();
						setTimeout(function() { $("#messagePrompt").hide(); }, 900);
						}else{
							
							$('.toast-body p').text(result);
							$('.toast').toast({delay: 900});
							$('.toast').toast('show');
							
							$("#messagePrompt").text(result);
							$("#messagePrompt").css("color", "#FF2D2D");
							$("#messagePrompt").toggle();
							setTimeout(function() { $("#messagePrompt").hide(); }, 900);
						}
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
			});
		});
		
		
		
		
		
		//開啟編輯的按鍵
		$('body').on('click','button[name^="edit"]',function(e){
			e.preventDefault;
			var openEdit = $(this).parent().prevAll().children(":input");
			var confirmEdit =openEdit.next();
			var hide =openEdit.next();
			openEdit.attr('disabled', false);
			confirmEdit.show();

		});
	
	//update 修改一則留言(主要、回覆)
	$('body').on('click','button[name^="updateCo"]',function(e){
			e.preventDefault;
			var urls = "/PepperNoodles/user/updateCommentAjax/";
			var id         =$(this).next().next().next().next().text();
//				urls += id;
//				var id2        =parseInt(id);
			var text       =$(this).prev().val();
			var time       =$(this).next().next().text();
			var likeAmount =$(this).next().next().next().text();
			var toHide         =$(this).next();
// 			alert(id);
// 			alert(text);
// 			alert(time);
// 			alert(likeAmount);
			var data =
			{
					"userMessageId": id,
							 "text": text,
						     "time": time,
					   "likeAmount": likeAmount,
				   "netizenAccount": null,
					  "userAccount": null,
			           "messageBox": null,
			    "replyMessageBoxes": null
			};
			
			$.ajax({
				type:"POST",
				data:JSON.stringify(data),
				contentType:'application/json;charset=UTF-8',
				url: urls ,
				dataType: "text",
				success: function (result) {
// 					alert(result);
					showAllComments();
					if(result.length<7){
						
						$('.toast-body p').text(result);
						$('.toast').toast({delay: 900});
						$('.toast').toast('show');
						
						$("#messagePrompt").text(result);
						$("#messagePrompt").css("color", "#007500");
						$("#messagePrompt").toggle();
						setTimeout(function() { $("#messagePrompt").hide(); }, 900);
						}else{
							
							
							$('.toast-body p').text(result);
							$('.toast').toast({delay: 900});
							$('.toast').toast('show');
							
							$("#messagePrompt").text(result);
							$("#messagePrompt").css("color", "#FF2D2D");
							$("#messagePrompt").toggle();
							setTimeout(function() { $("#messagePrompt").hide(); }, 900);
						}

				},
			error: function (thrownError) {
					console.log(thrownError);
					alert(thrownError);
				}
			});
		});
	
	
	//留言按讚
	$('body').on('click','button[name^="likeBut"]',function(e){
			e.preventDefault;
			var urls = "/PepperNoodles/user/updateLikeCommentAjax/?id=";
			var button = $(this);
			var id         =$(this).parent().prevAll().children("td span:eq(3)").text();
			var changeLikeAmount = $(this).parent().prevAll("td:eq(1)");
			urls += id;
// 			alert(id);

			
			$.ajax({
				type:"GET",
				url: urls ,
				dataType: "text",
				success: function (result) {
// 					alert(result);


				},
				complete: function(result){
					button.toggle();
					button.next().toggle();
					
					changeLikeAmount.text(result.responseText);
				},
			error: function (thrownError) {
					console.log(thrownError);
					alert(thrownError);
				}
			});
		});
	
	//收回讚
	$('body').on('click','button[name^="dislikeBut"]',function(e){
			e.preventDefault;
			var urls = "/PepperNoodles/user/updateDisLikeCommentAjax/?id=";
			var button = $(this);
			var id         =$(this).parent().prevAll().children("td span:eq(3)").text();
			var changeLikeAmount = $(this).parent().prevAll("td:eq(1)");
			urls += id;
// 			alert(id);

			
			$.ajax({
				type:"GET",
				url: urls ,
				dataType: "text",
				success: function (result) {
// 					alert(result);


				},
				complete: function(result){
					button.toggle();
					button.prev().toggle();
					
					changeLikeAmount.text(result.responseText);
				},
			error: function (thrownError) {
					console.log(thrownError);
					alert(thrownError);
				}
			});
		});	
	
//		id='likeof"  看誰按讚的功能 直接點讚數
	$('body').on('mouseenter','td[id^="likeof"]',function(e){
		e.preventDefault;
		var likebutton = $(this);
		var msnID = $(this).next().next().next().next().children("span").text();

		$.ajax({
			type:"GET",
			url:"/PepperNoodles/user/showWhoLikeAjax?msnID=" + msnID,
			dataType: "json",
			success: function (result) {
// 				console.log(result);
// 				console.log(JSON.stringify(result));
				var x ="按讚的人有:\n\r" ;
				if(result.length==0){
					x ="還沒有人按讚唷傻逼~"
				}else{
					for( i =0;i<result.length;i++){
						x += result[i].userAccountDetail.nickName;
						x += "\n\r";
					}
				}
				likebutton.attr("title",x)
			        	},
	        error: function (result) {
	        	alert("fail");
	        }
		});

	});
		
		//建立餐廳按鈕的funciton
		function positionShow(){
			console.log(this.id);
			let restId = this.id.slice(4,);
		
			let url = "<c:url value='/restSearch/userSingleRestPage/' />" + restId;
			features = "width="+1200+",height="+600+",top="+50+",left="+50; 
			window.open(url,"toolbar=no,location=no,directories=no",features);
		}	
		
		//建立隔壁memo的function
		function createOthersSideMemo(){
			//memo
		var viewUserAccount = "${viewUserAccount.accountIndex}";
		var urls = "/PepperNoodles/user/showOtherUserCollections?viewUserAccount="; 		
		urls += viewUserAccount;
			$.ajax({
				method:'GET',
				url: urls,
				dataType:'json',
				success:function(result){
					console.log(JSON.stringify(result));
				
	 				var memo=document.getElementById("memoBoard");
	 				var memosheet = document.createElement("table");
	 				var HTMLtable="";
				
					if(result.length>0){
	 					for(let i=0;i<result.length;i++){
	 						
	 						//建立td1和roleSpan
	 					    let tr1 = document.createElement("tr");
	 					    // td1.className="tdtop";
	 					    let td1=document.createElement("td");
	 					    td1.rowSpan="4";	   
	 					    let img =document.createElement("img");	   
	 					    let url ="<c:url value='/restSearch/restPicByid'/>"+"/"+result[i].restaurantId;
	 					    img.src=url;
	 					    img.name=result[i].restaurantName;
	 					    img.style.height="80px";
	 					    img.id=result[i].restaurantId;
	 					    let imgframe =document.createElement("div");
	 					    imgframe.className='frame2';
	 					    imgframe.appendChild(img);
	 					    
	 					    td1.appendChild(imgframe);
	 					    tr1.appendChild(td1);
	 					    
	 					    let tr2 = document.createElement("tr");
	 					    let td2=document.createElement("td");
	 					    let restAnchor = document.createElement("a");
	 					    
	 					    let resturl ="<c:url value='/userPage/'/>"+result[i].restaurantId;
	 					    restAnchor.href=resturl;
	 					    
	 					    restAnchor.innerHTML=result[i].restaurantName;
	 					    restAnchor.style.color="#0000C6";
	 					    td2.appendChild(restAnchor);
	 					    tr2.appendChild(td2);     			        
	 					    
	 					    let td2_2=document.createElement("td");
	 					    td2_2.rowSpan="3";	   
	 					    td2_2.innerHTML = '<button class="getPositionButton" style="background-color:#00008B;border-radius:15px;" id=rest'+result[i].restaurantId+'><i class="fas fa-map-marker-alt"></i></button>'
	 					   	tr2.appendChild(td2_2);     	
	 					    $(".getPositionButton").click(positionShow);
	 					    
	 					    
	 					    
	 					    let tr3 = document.createElement("tr");
	 					    let td3=document.createElement("td");
	 					    td3.innerHTML=result[i].restaurantAddress;
	 					        tr3.appendChild(td3);
	 					        
	 					    //標籤列表
	 					    let tr4 = document.createElement("tr");
	 					    let td4=document.createElement("td");
	 					    td4.innerHTML="Tags:&nbsp"
	 					    td4.className="tdbot";
	 					    
	 					    for(let j = 0; j<result[i].foodTag.length;j++){
	 					    	let tagAnchor = document.createElement("a");
	 							tagAnchor.href="#";
	 							
	 							tagAnchor.innerHTML+=result[i].foodTag[j].foodTagName+"&nbsp&nbsp";
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
	 					$(".getPositionButton").click(positionShow);
	 				}else{
	 					memo.innerHTML="<h2>${viewUserAccountDetail.nickName}還沒有收藏餐廳唷!</h2>";
	 				}
				},
				error:function(result){
					}
				});
			};
			
			

 		</script>

</body>
</html>