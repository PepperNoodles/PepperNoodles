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

<style>
	.header {
			background-color: #000000;
			}
	a{
			color:#000000;	
			}
	a:hover{
			color:blue;
			}
	tr:hover{
			background-color:#BEBEBE;
			}

	td>img{
			height: 100px;
			}
	.nav-link{
		color:#000000;	
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
	<div class="container mt-10" style="width:80%;height:100vh">
					<!--有照片的那個bar  -->
					<div class="d-flex">
						<div class="p-2">
							<img style="height: 100px"
								src="<c:url value='/userProtrait/${userAccount.userAccountDetail.useretailId}'/>">
						</div>

						<div class="p-2 flex-fill align-self-end justify-content-center">
							<h1>${userAccount.userAccountDetail.nickName}</h1>
						</div>
						

					</div>
					<div class="flex-fill bg-secondary p-1 mb-5">						
					</div>
					
					<!--左邊的分隔用-->
					<div class="d-flex">
					<div class="nav flex-column nav-pills col-3" id="v-pills-tab" role="tablist" aria-orientation="vertical">
						<a class="nav-link active" id="v-pills-home-tab"    data-toggle="pill" href="#v-pills-home"		      role="tab" aria-controls="v-pills-home" aria-selected="true"><i class="fas fa-home"></i>Home</a>
						<a class="nav-link" id="v-pills-friend-tab" 	    data-toggle="pill" href="#v-pills-friend" 		  role="tab" aria-controls="v-pills-friend" aria-selected="false"><i class="fas fa-users"></i>好友</a>
						<a class="nav-link" id="v-pills-aboutUser-tab" 	    data-toggle="pill" href="#v-pills-aboutUser"	  role="tab" aria-controls="v-pills-aboutUser" aria-selected="false"><i class="fas fa-file-alt"></i>關於我</a>
						<a class="nav-link" id="v-pills-userMessage-tab"    data-toggle="pill" href="#v-pills-userMessage" 	  role="tab" aria-controls="v-pills-userMessage" aria-selected="false"><i class="fas fa-comments"></i>留言區</a>
						<a class="nav-link" id="v-pills-userCollection-tab" data-toggle="pill" href="#v-pills-userCollection" role="tab" aria-controls="v-pills-userCollection" aria-selected="false"><i class="fas fa-heart"></i>收藏區</a>

						</div>
							<div class="tab-content" id="v-pills-tabContent col-9">
								<div class="tab-pane fade show active" id="v-pills-home" role="tabpanel" aria-labelledby="v-pills-home-tab">
									<h2>基本資料</h2>
									<p id="accountIndex">email: ${userAccount.accountIndex} </p>
									<p>性別：${userAccount.userAccountDetail.gender}</p>
									<p>地區：${userAccount.userAccountDetail.location}</p>
								</div>
								<div class="tab-pane fade" id="v-pills-friend" role="tabpanel" aria-labelledby="v-pills-friend-tab">
									
									
									<!--好友分區用-->
									<nav>
										<div class="nav nav-tabs" id="nav-tab" role="tablist">
										  <a class="nav-item nav-link active" id="nav-myFriend-tab" data-toggle="tab" href="#nav-myFriend" role="tab" aria-controls="nav-myFriend" aria-selected="true">
											<button id="checkFriendList" style="color:black">我的好友</button></a>
										  <a class="nav-item nav-link" id="nav-searchFriend-tab" data-toggle="tab" href="#nav-searchFriend" role="tab" aria-controls="nav-searchFriend" aria-selected="false">
											<button id="checkFriendList" style="color:black">搜尋使用者</button></a>
										  <a class="nav-item nav-link" id="nav-friendQequest-tab" data-toggle="tab" href="#nav-friendQequest" role="tab" aria-controls="nav-friendQequest" aria-selected="false">
											<button class="btn-link" id="checkRequestList" style="color:black">查看邀請</button></a>
										</div>
									  </nav>
									  <div class="tab-content" id="nav-tabContent">
										<div class="tab-pane fade show active" id="nav-myFriend" role="tabpanel" aria-labelledby="nav-myFriend-tab">
											<!--<button id="checkFriendList" style="color:black">我的好友</button>-->
											<div id="userFriendList">
											</div>
											
											</div>
										<div class="tab-pane fade" id="nav-searchFriend" role="tabpanel" aria-labelledby="nav-searchFriend-tab">
											<div class="d-flex mt-3">
												<input class="m-2" id="nameSearch" type="search" placeholder="Search By nickName" aria-label="Search">
												<button class="btn btn-primary my-2 my-sm-0 " id="btn-search" >Search</button>
											</div>					
											<div id="searchResult">
											</div>
										</div>
										
										
										<div class="tab-pane fade" id="nav-friendQequest" role="tabpanel" aria-labelledby="nav-friendQequest-tab">
											<h6>好友邀請</h6>
											<!--<button id="checkRequestList" style="color:black">查看邀請</button>-->
											<div id="friendRequest">	
										</div>
									  </div>
								</div>
								</div>	
								<div class="tab-pane fade" id="v-pills-aboutUser" role="tabpanel" aria-labelledby="v-pills-aboutUser-tab">
									<h2>關於我</h2>
								</div>
								<div class="tab-pane fade" id="v-pills-userMessage" role="tabpanel" aria-labelledby="v-pills-userMessage-tab">
									<h2>userMessage</h2>

								</div>
								<div class="tab-pane fade" id="v-pills-userCollection" role="tabpanel" aria-labelledby="v-pills-userCollection-tab">
									<h2>userCollection</h2>

								</div>
					</div>		
					</div>		
			 </div>
			</div>		

				<!-- Scroll Up -->
				<div id="back-top">
					<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
				</div>



				<script>
					$(window).on('load', function () {

						let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='findMainfriend'/>";
							names="${userAccount.accountIndex}";
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
						$(".header-sticky").addClass("sticky-bar");
						$(".header-sticky").css("height", "90px");
						//$(".header-sticky").css("position","static ")

						$('#preloader-active').delay(450).fadeOut('slow');
						$('body').delay(450).css({
							'overflow': 'visible'
						});
						
					
						
						$("#checkFriendList").on('click',function(){
							let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='findMainfriend'/>";
							names="${userAccount.accountIndex}";
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
					
						})
						
						
						
						
							//按下nickName搜尋功能
						$("#btn-search").on('click',function(){
							let names=document.getElementById("nameSearch").value;
							console.log(names);
							let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='searchUserByNick'/>";
							urls+="?name="+names;
							console.log(urls);
							$.ajax({
								type: "GET",
								url: urls,							
								dataType: "json",
								success: function (response) {
									console.log(response);
									let id="#searchResult";
									showSearchList(response,id);
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
						})

					
						function showSearchList(response,id){
							$(id).html("");
							//let result = JSON.stringify(response);
							console.log(response[0]);
							let table =  document.createElement("table");
							table.border="1";

							let length = Object.keys(response).length;
							for (let i =0;i< length;i++){
								let tr  = document.createElement("tr");
								let td1 = document.createElement("td");
								let td2 = document.createElement("td");
								let td3 = document.createElement("td");
								let img = document.createElement("img");
								img.class="tdimg";
								let imgSrc="${pageContext.request.contextPath}/userProtrait/"+response[i].userAccountDetail.useretailId;
								img.src="<c:url value='"+imgSrc+"'/>";
								td3.appendChild(img);
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

						//按下搜尋好友請求功能
					$("#checkRequestList").on('click',checkRequestList);
					
					function checkRequestList(){
						let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='findWhoWannaJoinMe'/>";
							names="${userAccount.accountIndex}";
							urls+="?mainUserIdx="+names;
							console.log(urls);
						$.ajax({
								type: "GET",
								url: urls,				
								dataType: "json",
								success: function (response) {
									console.log(response);
									showRequestList(response);
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
					}
					
					function showRequestList(response){
							$("#friendRequest").html("");
							//let result = JSON.stringify(response);
							console.log(response[0]);
							let table =  document.createElement("table");
							table.border="1";

							let length = Object.keys(response).length;
							for (let i =0;i< length;i++){
								let tr  = document.createElement("tr");
								//td1 mail,td2 nick name ,td3 pic,td4 button
								let td1 = document.createElement("td");
								let td2 = document.createElement("td");
								let td3 = document.createElement("td");
								let td4 = document.createElement("td");
								let img = document.createElement("img");
								let imgSrc="${pageContext.request.contextPath}/userProtrait/"+response[i].userAccountDetail.useretailId;
								img.src="<c:url value='"+imgSrc+"'/>";
								td3.appendChild(img);
								td1.innerHTML=response[i].accountIndex;
								console.log(response[i].accountIndex);
								console.log(response[i].userAccountDetail);
								
								//幫nickname建立連結
								let a =document.createElement("a");
								a.href="${pageContext.request.contextPath}/userView/"+response[i].accountIndex;
								a.innerText =response[i].userAccountDetail.nickName;
								
								td2.appendChild(a);
								//新增確認與取消按鈕
								let btn_comfirm = document.createElement("button");
								btn_comfirm.innerHTML="AddFriend";
								btn_comfirm.value=response[i].accountIndex;
								btn_comfirm.addEventListener("click",addFriend);
								// a1_comfirm.href="${pageContext.request.contextPath}/"+"/${userAccount.accountIndex}"+"/"+response[i].accountIndex;
								// a1_comfirm.innerHTML="AddFriend";

								// let a1_cancel =document.createElement("a");
								 td4.appendChild(btn_comfirm);
								
								//放圖,放account,放nickName
								tr.appendChild(td3);
								tr.appendChild(td1);
								tr.appendChild(td2);
								tr.appendChild(td4);
								table.appendChild(tr);
							}
							$("#friendRequest").append(table);
							
						}
						//addFriend按鈕的function
						function addFriend(){
							alert(this.value);
							let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='MainUserAddFriendwithIndex'/>";
							urls+="/${userAccount.accountIndex}"
							urls+="/"+this.value;
							console.log(urls);
						$.ajax({
								type: "GET",
								url: urls,				
								dataType: "text",
								success: function (response) {
									console.log(response);
									checkRequestList();
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
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
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>
	<script src="<c:url value='/scripts/popper.min.js' />"></script>


</body>
</html>