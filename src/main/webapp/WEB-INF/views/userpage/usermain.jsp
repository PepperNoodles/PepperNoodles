<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="org.hibernate.Hibernate,java.util.Set,java.util.HashSet,com.infotran.springboot.commonmodel.FoodTagUser,com.infotran.springboot.commonmodel.UserAccount"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="../includePage/includeNav.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Template For inClude</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
<script src="<c:url value='/scripts/popper.min.js' />"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+TC&display=swap');

.header {
	background-color: #000000;
}
a {
	color: black;
}
.nav a {
	color: black;
}

a:hover {
	color: blue;
}

tr:hover {
	background-color: #BEBEBE;
}

td>img {
	height: 100px;
}

table {
	border-collapse: separate;
	border: solid black 1px;
	border-radius: 6px;
	-moz-border-radius: 6px;
}
.display{
 	font-family: 'Noto Serif TC', serif;
 	font-size: 15px; 

button{
color: black;
}
/*  td, th {  */
/*      border-left:solid black 1px;  */
/*      border-top:solid black 1px;  */
/*  }  */

/*  th {  */
/*      background-color: blue;  */
/*     border-top: none;  */
/* }  */

/*  td:first-child, th:first-child {  */
/*       border-left: none;  */
/*  }  */
</style>
<script>
	window.onload = fackbooklike;
		function fackbooklike() {
			var fbLike = document.getElementById("fbLike");
		if (fbLike){
			document.getElementById("fbLike").src = "http://www.facebook.com/plugins/like.php?href=" + location.href + "&layout=standard&show_faces=true&width=350&action=like&colorscheme=light&height=25";
			}
		}
</script>
</head>
<body>
	<!-- 讀取圖案 -->
	<div id="preloader-active" >
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

	<div class="container-fluid">

		<div class="container mt-10" style="width: 80%; height: 100%">
			<!--有照片的那個bar  -->
			<div class="d-flex">
				<div class="p-2">
					<img id="replacepic" style="height: 100px"
						src="<c:url value='/userProtrait/${userAccount.userAccountDetail.useretailId}'/>">
				</div>

				<div class="p-2 flex-fill align-self-end justify-content-center">
					<h1>${userAccount.userAccountDetail.nickName}</h1>
				</div>


			</div>
			<div class="flex-fill bg-secondary p-1 mb-5"></div>

			<!--左邊的分隔用-->
			<div class="d-flex" >
				<div class="nav flex-column nav-pills col-3" id="v-pills-tab" role="tablist" aria-orientation="vertical">
					<a class="nav-link active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home" role="tab" aria-controls="v-pills-home" aria-selected="true">
						<i class="fas fa-home"></i>Home</a> 
					<a  class="nav-link" id="v-pills-friend-tab" data-toggle="pill" href="#v-pills-friend" role="tab" aria-controls="v-pills-friend" aria-selected="false">
						<i class="fas fa-users"></i>好友</a> 
					<a class="nav-link" id="v-pills-aboutUser-tab" data-toggle="pill" href="#v-pills-aboutUser" role="tab" aria-controls="v-pills-aboutUser" aria-selected="false">
						<i class="fas fa-file-alt"></i>關於我</a>
					<a class="nav-link" id="v-pills-userMessage-tab" data-toggle="pill" href="#v-pills-userMessage" role="tab" aria-controls="v-pills-userMessage" aria-selected="false">
						<i class="fas fa-comments"></i>留言區</a>
					<a class="nav-link" id="v-pills-userCollection-tab" data-toggle="pill" href="#v-pills-userCollection" role="tab" aria-controls="v-pills-userCollection" aria-selected="false">
						<i class="fas fa-heart"></i>收藏區</a>
					<a class="nav-link" id="v-pills-userOrderList-tab" data-toggle="pill" href="#v-pills-userOrderList" role="tab" aria-controls="v-pills-userOrderList" aria-selected="false">
						<i class="fas fa-file-alt"></i>查詢訂單</a>

				</div>
				<div class="tab-content" id="v-pills-tabContent col-9">
					<div class="tab-pane fade show active" id="v-pills-home" role="tabpanel" aria-labelledby="v-pills-home-tab">
						<h2>基本資料</h2>
						<table border='1'  class='table table-hover table-bordered ' style='font-size: 8px border-collapse:separate; border:solid #F0F0F0 1px;border-radius:6px;-moz-border-radius:6px;'>
						<tr>
						<td><span id="accountIndex">email:</span></td><td><span > ${userAccount.accountIndex}</span><button   style="color: black;display:none ">確認</button></td ></tr>
						<tr>
						<td  ><span >照片修改：</span></td><td ><input class='genric-btn default circle arrow' style='color:black' type="file" 	disabled="disabled" id="wizard-picture" accept="image/*" name="photo"></td><td id="showtable9" style="display:none"><button id="confirmPhoto" class='genric-btn default circle arrow'  style="color: black;">確認</button></td></tr>
						<tr>
						<td><span >綽號：</span></td><td><span id="nickNameSpan" >${userAccount.userAccountDetail.nickName}</span><input value="${userAccount.userAccountDetail.nickName}" style="display:none" id="updateinputBasic2NickName"></input><button id="updateBaisc2NickName" class='genric-btn default circle arrow' style="color: black;display:none">確認</button></td><td id="showtable2" style="display:none"><button class='genric-btn default circle arrow' id="change2NickName"  style="color: black;">修改</button></td></tr>
						<tr>
						<td><span >生日：</span></td><td><span id="birthDaySpan" >${userAccount.userAccountDetail.birthDay}</span><input  value="${userAccount.userAccountDetail.birthDay}" style="display:none" id="updateinputBasic2birthDay" ></input><button  id="updateBaisc2birthDay" class='genric-btn default circle arrow' style="color: black;display:none">確認</button></td><td id="showtable3" style="display:none"><button class='genric-btn default circle arrow' id="change2birthDay"  style="color: black;">修改</button></td></tr>
						<tr>
						<td><span >性別：</span></td><td><span id="sexValueSpan" >${userAccount.userAccountDetail.gender}</span><input   value="${userAccount.userAccountDetail.gender}" style="display:none" id="updateinputBasic2gender" ></input><button  id="updateBaisc2gender" class='genric-btn default circle arrow' style="color: black;display:none">確認</button></td><td id="showtable4" style="display:none"><button id="change2gender" class='genric-btn default circle arrow'  style="color: black;">修改</button></td></tr>
						<tr>
						<td><span >地區：</span></td><td><span id="locationSpan" >${userAccount.userAccountDetail.location}</span><input   value="${userAccount.userAccountDetail.location}" style="display:none" id="updateinputBasic2location" ></input><button id="updateBaisc2location" class='genric-btn default circle arrow' style="color: black;display:none">確認</button></td><td id="showtable5" style="display:none"><button id="change2location" class='genric-btn default circle arrow' style="color: black;">修改</button></td></tr>
						<tr>
						<td><span >電話：</span></td><td><span id="phoneNumberSpan" >${userAccount.userAccountDetail.phoneNumber}</span><input   value="${userAccount.userAccountDetail.phoneNumber}" style="display:none" id="updateinputBasic2phoneNumber" ></input><button id="updateBaisc2phoneNumber" class='genric-btn default circle arrow' style="color: black;display:none">確認</button></td><td id="showtable6" style="display:none"><button id="change2phoneNumber" class='genric-btn default circle arrow' style="color: black;">修改</button></td></tr>
						<tr>
						<td><span >真實姓名：</span></td><td><span id="realNameSpan" >${userAccount.userAccountDetail.realName}</span><input   value="${userAccount.userAccountDetail.realName}" style="display:none"  id="updateinputBasic2RealName"></input><button  id="updateBaisc2realName" class='genric-btn default circle arrow'  style="color: black;display:none">確認</button></td><td id="showtable7" style="display:none"><button id="change2realName" class='genric-btn default circle arrow' style="color: black;">修改</button></td></tr>
						<tr>
						<td><span >興趣：</span></td><td><span  id="userTagsSpan" ><c:forEach items="${userAccount.userTags}" var="hobby">${hobby.fkfoodtagid.foodTagName} </c:forEach></span><span   id="updateinputBasic3FoodTagNames" style="color: black;display:none"></span> &nbsp; &nbsp;<button class='genric-btn default circle arrow' id="FoodTagNames" style="color: black;display:none">確認</button></td><td id="showtable8" style="display:none"><button id="change3FoodTagNames"  class='genric-btn default circle arrow' style="color: black;">修改</button></td></tr>
						
						</table>
												<button class='genric-btn default circle arrow' id="openchange" style="color: black;">修改基本資料</button><button class='genric-btn default circle arrow' id="closechange" style="color: black;">取消</button>
						
					</div>
					<div class="tab-pane fade" id="v-pills-friend" role="tabpanel"
						aria-labelledby="v-pills-friend-tab">


						<!--好友分區用-->
						<nav>
							<div class="nav nav-tabs" id="nav-tab" role="tablist">
								<a class="nav-item nav-link active" id="nav-myFriend-tab"
									data-toggle="tab" href="#nav-myFriend" role="tab"
									aria-controls="nav-myFriend" aria-selected="true">
									<button id="checkFriendList" style="color: black">我的好友</button>
								</a> <a class="nav-item nav-link" id="nav-searchFriend-tab"
									data-toggle="tab" href="#nav-searchFriend" role="tab"
									aria-controls="nav-searchFriend" aria-selected="false">
									<button id="checkFriendList" style="color: black">搜尋使用者</button>
								</a> <a class="nav-item nav-link" id="nav-friendQequest-tab"
									data-toggle="tab" href="#nav-friendQequest" role="tab"
									aria-controls="nav-friendQequest" aria-selected="false">
									<button class="btn-link" id="checkRequestList"
										style="color: black">查看邀請</button>
								</a>
								<a class="nav-item nav-link" id="nav-message-tab"
									data-toggle="tab" href="#nav-message" role="tab"
									aria-controls="nav-message" aria-selected="false">
									<button class="btn-link" id="checkmessage"
										style="color: black"><a href="<c:url value='/user/websocket'/>">聊天室</a> </button>
								</a>
							</div>
						</nav>
						<div class="tab-content" id="nav-tabContent">
							<div class="tab-pane fade show active" id="nav-myFriend"
								role="tabpanel" aria-labelledby="nav-myFriend-tab">
								<!--<button id="checkFriendList" style="color:black">我的好友</button>-->
								<div id="userFriendList"></div>

							</div>
							<div class="tab-pane fade" id="nav-searchFriend" role="tabpanel"
								aria-labelledby="nav-searchFriend-tab">
								<div class="d-flex mt-3">
									<input class="m-2" id="nameSearch" type="search"
										placeholder="Search By nickName" aria-label="Search">
									<button class="btn btn-primary my-2 my-sm-0 " id="btn-search">Search</button>
								</div>
								<div id="searchResult"></div>
							</div>


							<div class="tab-pane fade" id="nav-friendQequest" role="tabpanel"
								aria-labelledby="nav-friendQequest-tab">
								<h6>好友邀請</h6>
								<!--<button id="checkRequestList" style="color:black">查看邀請</button>-->
								<div id="friendRequest"></div>
							</div>
						</div>
					</div>
					<div class="tab-pane fade" id="v-pills-aboutUser" role="tabpanel"
						aria-labelledby="v-pills-aboutUser-tab">
						<h2>關於我</h2>
					</div>
					<div class="tab-pane fade" id="v-pills-userMessage" role="tabpanel"
						aria-labelledby="v-pills-userMessage-tab">

						<h2>${userAccount.userAccountDetail.nickName}的留言區</h2>


						<iframe allowtransparency="" frameborder="0" id="fbLike"
							scrolling="no" src=""
							style="border-bottom: medium none; border-left: medium none; width: 250px; height: 30px; overflow: hidden; border-top: medium none; border-right: medium none"></iframe>


						<!-- 						新增主要留言input & 按鈕 -->
						<input placeholder='Hello....' id="commentInput"></input>
						<button type="button" class="genric-btn default circle arrow"
							id="addNewComment">新增留言</button>

						<!-- 						使用Ajax的方法 -->
						<div class="container-fluid"
							style="overflow: scroll; height: 400px;" id="commentsForUser"
							class="table"></div>


						<!--      使用jstl的方法 -->
						<%--      <c:choose> --%>
						<%--       <c:when test="${not empty userAccount.msnBox}"> --%>
						<!--        <br> -->
						<%--        <c:forEach var='msn' items='${userAccount.msnBox}' varStatus='ms'> --%>
						<%--         <c:if test="${msn.messageBox == null }"> --%>

						<!--          <div class="row " style="border: 1px solid red"> -->
						<!--           <img> -->
						<%--           <h4>留言${ms.count }</h4> --%>
						<%--           &nbsp; <span>${msn.netizenAccount.userAccountDetail.nickName }</span>&nbsp;<span>於 --%>
						<%--            ${msn.time }</span> --%>
						<!--           <div class="col-12 "> -->
						<%--            <blockquote class="generic-blockquote">${msn.text}</blockquote> --%>
						<!--            <button class="pull-right" value="" style="color: black">回覆留言</button> -->
						<%--            <c:forEach var='msnReply' items='${msn.replyMessageBoxes}' --%>
						<%--             varStatus='rms'> --%>
						<!--             <img> -->
						<%--             <h5>回覆${rms.count}</h5>&nbsp; --%>
						<%--           <span>${msnReply.netizenAccount.userAccountDetail.nickName}</span>&nbsp;<span>於 --%>
						<%--              ${msnReply.time }</span> --%>
						<!--             <div class="col-10"> -->
						<%--              <blockquote class="generic-blockquote">${msnReply.text}</blockquote> --%>
						<!--             </div> -->
						<%--            </c:forEach> --%>
						<!--           </div> -->
						<!--          </div> -->
						<%--         </c:if> --%>

						<%--        </c:forEach> --%>

						<%--       </c:when> --%>
						<%--       <c:otherwise> --%>
						<!--           沒有留言給您 -->
						<%--       </c:otherwise> --%>
						<%--      </c:choose> --%>
					</div>
					<div class="tab-pane fade" id="v-pills-userCollection"
						role="tabpanel" aria-labelledby="v-pills-userCollection-tab">
						<h2>userCollection</h2>
					</div>
					
					<!-- 訂單表 -->
					<div class="tab-pane fade" id="v-pills-userOrderList" role="tabpanel" aria-labelledby="v-pills-userOrderList-tab">
						<h2>訂單明細</h2>
						<table  id="orderlist" class="display">
							<thead>
								<tr>
									<th>編號</th>
									<th>訂單時間</th>
									<th>收件人</th>
									<th>電話</th>
									<th>收件地址</th>
									<th>價格</th>
								</tr>
							</thead>
							<tbody>
								<tr>
								</tr>
							</tbody>
						</table>
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
			$(document).ready(function () {
				
					var Table = $("#orderlist").DataTable({
						 language: {
						        "processing": "處理中...",
						        "loadingRecords": "載入中...",
						        "lengthMenu": "顯示 _MENU_ 項結果",
						        "zeroRecords": "沒有符合的結果",
						        "info": "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
						        "infoEmpty": "顯示第 0 至 0 項結果，共 0 項",
						        "infoFiltered": "(從 _MAX_ 項結果中過濾)",
						        "infoPostFix": "",
						        "search": "搜尋:",
						        "paginate": {
						            "first": "第一頁",
						            "previous": "上一頁",
						            "next": "下一頁",
						            "last": "最後一頁"
						        },
						        "aria": {
						            "sortAscending": ": 升冪排列",
						            "sortDescending": ": 降冪排列"
						        }
						    },
						    data:[],
						    columns: [
				                { "data": "uuid"  },
				                { "data": "orderCreatedDate" ,
			                	  "render": function (data, type, row, meta) {
			                       return data.substr(0,4)+'/'+data.substr(5,2)+'/'+data.substr(8,2)}},
				                { "data": "receiveName" },
				                { "data": "receivePhone" },
				                { "data": "receiveAddress" },
				                { "data": "totalCost" }
						    ],
						    filter: true,
						    bPaginate: true,
						    info: true,
						    ordering: true,
						    processing: true,
						    retrieve: true,
						    searching: true, //關閉filter功能
			                columnDefs: [{
			                    targets: [3],
			                    orderable: true,
			                }]
						});
					
						
						$.ajax({
							method:"GET",	
							url:"/PepperNoodles/user/getOrderList",
							contentType: 'application/json; charset=utf-8',
							dataType:'json',
					        async : true,
					        cache: false,
					        success:function(result){
					        	console.log("yes123");
					        	console.log(JSON.stringify(result));
					        	console.log(result.AccountMemberOrderList);
					        	Table.clear().draw();
					            Table.rows.add(result.AccountMemberOrderList).draw();
					        },
					        error: function (result) {
					        	console.log("有問題");
					        }
						});
						
						
						
						///////////////////////////////////////////////
	
// 					$(document).ready(function(){ 
					$(window).on('load', function () {
						showAllComments();

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
// 							alert(this.value);
							let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='MainUserAddFriendwithIndex'/>";
							urls+="/${userAccount.accountIndex}"
							urls+="/"+this.value;
							console.log(urls);
							
							
						$.ajax({
								type: "GET",
								url: urls,	
								data:	Object,
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
						


							
						});//onload end
					
					
// 					顯示留言
						function showAllComments(){
					var commentsForUser = document.getElementById("commentsForUser")
					var segment="";
					commentsLength = 0;
					$.ajax({
						method:"GET",
						url:"/PepperNoodles/user/showAllCommentAjax",
//							cache: false,  //不做快取
//					        async : true,
//							processData: false,
//							contentType: "application/json",
//							dataType:"text",
//							data: JSON.stringify(RequestContent),
				        success: function (result) {
// 			        		alert(result.length);
			        		commentsLength =result.length;
//				        		alert(result[0].replyMessageBoxes);
			        		console.log(JSON.stringify(result));
// 			        		console.log(JSON.stringify(result[0].time));
			        		 var aDay = new Date();
			        		 aDay.setHours(9);
			        		 aDay.setMinutes(0);
			        		 aDay.setSeconds(0);
// 			        		 var sun = result[0].time;
			        		
				        	for( i =0; i<result.length; i++){
				        		
				        		var formatDate   =(new Date(result[i].time)).toString().substring( 4 , 21 );
				        		var formatString = formatDate.split(' ');
				        		var formatPrint  = formatString[0]+ '/' + formatString[1] + '/'+ formatString[2] + '/' +formatString[3];
				        		
				        		segment +="<table border='1' class='table table-hover table-bordered ' style='font-size: 8px border-collapse:separate; border:solid blue 1px;border-radius:6px;-moz-border-radius:6px;'>";//<th>留言數</th><th>留言者</th><th>時間</th><th>讚數</th><th>留言內容</th><th>讚</th><th>編輯</th>";
				        		segment += "<tr class='table-primary'><td>留言" + (i+1) + "</td><td>" ;
				        		segment += result[i].netizenAccount.userAccountDetail.nickName + "</td><td>" ;
				        		segment += formatPrint + "</td><td>" ;
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
						        		var formatPrint  = formatString[0]+ '/' + formatString[1] + '/'+ formatString[2] + '/' +formatString[3];
									
										segment += "<tr class='table-info'><td>回覆" + (j+1) + "</td><td>" ;
						        		segment += result[i].replyMessageBoxes[j].netizenAccount.userAccountDetail.nickName  + "</td><td>" ;
						        		segment += formatPrint  + "</td><td>" ;
						        		segment += result[i].replyMessageBoxes[j].likeAmount + "</td><td>";
						        		segment += "<input disabled='disabled'size='20' value='" + result[i].replyMessageBoxes[j].text +"'>" ;
										segment +=  "</input><button name='updateComment' " + i + j +" style='display:none;color:black' >確定修改</button>";
					        			segment += "<span  style='display:none;visibility:hidden'>" + result[i].replyMessageBoxes[j].text + "</span>";
					        			segment += "<span  style='display:none'>" + result[i].replyMessageBoxes[j].time + "</span><span  style='display:none'>" + result[i].replyMessageBoxes[j].likeAmount + "</span>";
					        			segment += "<span  style='display:none'>" + result[i].replyMessageBoxes[j].userMessageId + "</span></td><td>";
					        			segment += "<button type='button' class='genric-btn default-border circle' style='color: black' name  ='likeButton" + i + j +"' >like</button><button type='button' class='genric-btn default circle arrow' style='color: black ;display:none' name  ='dislikeButton"  + i + j +"' >withdraw</button></td>";
					        			segment += "<td><button class='genric-btn default circle arrow' style='color: black' name='edit"  + i + j + "'>edit</button></td><td>";
					        			segment +="<button class='genric-btn default circle arrow' type='button' style='color: black' name  ='deleteComment" + i + j +	"' >delete</button><span  style='display:none'>" + result[i].replyMessageBoxes[j].userMessageId + "</span></tr>";
									}
				        		segment += "</table><span class='mt-0 pt-0'><input placeholder='我想吃....'></input><button type='button' class='genric-btn default circle arrow' style='color: black' name='addreply"  + i +"' >reply</button><span  style='display:none'>" + result[i].userMessageId + "</span></span><br><br><br>";
				        		;
// 				        		segment +="<button data-toggle='modal' data-target='#exampleModal' type='button' style='color: black' name  ='updateComment" + i +"' >更新主要留言</button><span  style='display:none'>" + result[i].text + "</span><span  style='display:none'>" + result[i].time + "</span><span  style='display:none'>" + result[i].likeAmount + "</span><br><br><hr>";
				        	
				        	}
			        		commentsForUser.innerHTML = segment;
				        	
				        },
				        error: function (result) {
// 				        	alert(result);
				            $("#commentsForUser").text("fail"); //填入提示訊息到result標籤內
				        }
					});
						}
						
						
						//新增留言(usermain頁面的 useraccount 之後會抓預設的值)
						$("#addNewComment").on('click',function(){
							
							var commentValue = document.getElementById("commentInput").value;
							var urls="/PepperNoodles/user/addNewCommentAjax/?comment="+commentValue  ;
//	 						+ "&useraccount=chrislo5311@gmail.com"
							$.ajax({
								type:"GET",
								url:  urls,
								dataType: "text",
								success: function (result) {
// 									alert(result);
									showAllComments();
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
						});
						
						//新增回覆留言
						$('body').on('click','button[name^="addreply"]',function(e){
							
							var messageIdValue = $(this).next().text();
							var urls           ="/PepperNoodles/user/addNewReplyCommentAjax/" + messageIdValue;
							var text           =$(this).prev().val();
// 							alert(messageIdValue);
// 							alert(text);
							
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
							$.ajax({
								type:"POST",
								url:  urls,
								contentType:'application/json;charset=UTF-8',
								dataType: "text",
								data:JSON.stringify(data),
								
								success: function (result) {
// 									alert(result);
									showAllComments();
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
						});
						
						$('body').on('click','button[name^="deleteCo"]',function(e){
		        			e.preventDefault;
		        			var urls = "/PepperNoodles/user/deleteCommentAjax?id=";
							var id =$(this).next().text();
// 							alert(id);
							urls += id;
							$.ajax({
								type:"GET",
								url: urls ,
								dataType: "text",
								success: function (result) {
// 									alert(result);
									showAllComments();
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
// 							urls += id;
// 							var id2        =parseInt(id);
							var text       =$(this).prev().val();
							var time       =$(this).next().next().text();
							var likeAmount =$(this).next().next().next().text();
							var toHide         =$(this).next();
							alert(id);
							alert(text);
							alert(time);
							alert(likeAmount);
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
									alert(result);
									showAllComments();

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
							alert(id);

							
							$.ajax({
								type:"GET",
								url: urls ,
								dataType: "text",
								success: function (result) {
									alert(result);


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
							alert(id);

							
							$.ajax({
								type:"GET",
								url: urls ,
								dataType: "text",
								success: function (result) {
									alert(result);


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
					
					
					//開啟修改使用者基本資料的按鍵
					$('body').on('click','button[id^="change2"]',function(e){
	        			e.preventDefault;
	        			var openEdit = $(this).parent().prevAll().children("input:eq(0)");
	        			var confirmEdit =openEdit.next();
	        			var hide =openEdit.prev();


	        			openEdit.toggle();
	        			confirmEdit.toggle();
	        			hide.toggle();

					});
					
					//修改使用者基本資料
					$('body').on('click','button[id^="updateBaisc2"]',function(e){
						e.preventDefault;
						var realName = document.getElementById("updateinputBasic2RealName").value;
						var nickname = document.getElementById("updateinputBasic2NickName").value;
						var phoneNumber = document.getElementById("updateinputBasic2phoneNumber").value;
						var birthDay = document.getElementById("updateinputBasic2birthDay").value;
						var sexValue = document.getElementById("updateinputBasic2gender").value;
						var location = document.getElementById("updateinputBasic2location").value;
	        			var confirmEdit = $(this);
	        			var input =$(this).prev();
	        			var hide =confirmEdit.prev().prev();
	        			
	        			confirmEdit.toggle();
	        			input.toggle();
	        			hide.toggle();
	        			changevalue();
	        			
	        			
						function changevalue(){
						  	  document.getElementById("realNameSpan").innerHTML = document.getElementById("updateinputBasic2RealName").value;
							  document.getElementById("nickNameSpan").innerHTML = document.getElementById("updateinputBasic2NickName").value;
							  document.getElementById("phoneNumberSpan").innerHTML = document.getElementById("updateinputBasic2phoneNumber").value;
							  document.getElementById("birthDaySpan").innerHTML = document.getElementById("updateinputBasic2birthDay").value;
							  document.getElementById("sexValueSpan").innerHTML = document.getElementById("updateinputBasic2gender").value;
							  document.getElementById("locationSpan").innerHTML = document.getElementById("updateinputBasic2location").value;
						   };
						
	        			var urls = "/PepperNoodles/user/changeBasicDetails";
	        			var data =
						{
	        			
	        					"useretailId":null,
								"realName" : realName,
								"nickName" : nickname,
								"phoneNumber" :phoneNumber,
								"birthDay" : birthDay,
								"userPhotoName":null,
								"userPhoto":null,
								"gender" :sexValue,
								"location": location,
								"purseID":null
						};
	        			
						$.ajax({
							type:"POST",
							url: urls ,
							data:JSON.stringify(data),
							contentType:'application/json;charset=UTF-8',
							dataType: "text",
							success: function (result) {
								alert(result);

							},

						error: function (thrownError) {
								console.log(thrownError);
								alert(thrownError);
							}
						});
	        		});	
					
					
					//開啟修改使用者興趣的按鍵
					$('body').on('click','button[id^="change3"]',function(e){
	        			e.preventDefault;
	        			var hide = $("#userTagsSpan");
	        			var checkboxs =hide.next();
	        			var confirmEdit =checkboxs.next();

	        			checkboxs.toggle();
	        			confirmEdit.toggle();
	        			hide.toggle();
	        			
							$.ajax({
								method:"GET",
								url:"/PepperNoodles/user/getFoodTagsAjax",

						        success: function (result) {
					        		alert(result[0].foodTagName);
					        		console.log(JSON.stringify(result));
					        		checkboxs.empty();

						        	for( i =0; i<result.length; i++){	
						        		checkboxs.append("<input type='checkbox' name='hobby' value='" + result[i].foodTagName + "'>"+ result[i].foodTagName+ " &nbsp; &nbsp;");

						        	}
						        		

						        },
						        error: function (result) {
						        	alert("fail");
						        }
							});
					});
					
					

					//確認修改使用者興趣的按鍵
					$('body').on('click','button[id^="FoodTagNames"]',function(e){
	        			e.preventDefault;
						var hobby = document.getElementsByName("hobby");
						console.log(hobby.length);
						var hobbyVal = [];
						for (var i = 0; i< hobby.length; i++) {
					  		if (hobby[i].checked) {
					  			hobbyVal.push(hobby[i].value);
					  		}
						}
				  		console.log(hobbyVal);
	        			
	        			var toshow = $(this).prev().prev();
	        			var checkboxs =$(this).prev();
	        			var confirmEdit =$(this);

	        			checkboxs.toggle();
	        			confirmEdit.toggle();
	        			toshow.toggle();
	        			alert(confirmEdit.value)
	        			
							$.ajax({
								type:"POST",
								url:"/PepperNoodles/user/confirmFoodTagsChangeAjax",
								data:JSON.stringify(hobbyVal),
								contentType:'application/json;charset=UTF-8',
								dataType: "text",
								success: function (result) {
									console.log(result);


								        	},
						        error: function (result) {
						        	alert("fail");
						        }
							});
							
		        		});	
				
					$('body').on('click','button[id^="confirmPhoto"]',function(e){
						data = new FormData();
				    	data.append('file', $('#wizard-picture')[0].files[0]);
						
						$.ajax({
							method:"POST",
							url:"/PepperNoodles/user/confirmPhotoChangeAjax",
							data:data,
							processData: false,
							contentType: false, 
							cache: false,  //不做快取
					        async : true,
					        success: function (result) {
								alert("photo changed");
								
								location.reload();
								
// 								var replacepic = document.getElementById("replacepic");
// 								$.ajax({
									
// 									method:"GET",
// 									url:"/PepperNoodles/user/replacePhotoAjax",

// 									cache: false,  //不做快取
// 							        async : true,
// 									success:function(result){}
								
								
					        },
					        error: function (result) {
					        	alert(result);					        
					        }
						});

					});	
						
					
					$('body').on('click','button[id^="openchange"]',function(e){
						var openchangebutton2 = document.getElementById("showtable2");
						var openchangebutton3 = document.getElementById("showtable3");
						var openchangebutton4 = document.getElementById("showtable4");
						var openchangebutton5 = document.getElementById("showtable5");
						var openchangebutton6 = document.getElementById("showtable6");
						var openchangebutton7 = document.getElementById("showtable7");
						var openchangebutton8 = document.getElementById("showtable8");
						var openchangebutton9 = document.getElementById("showtable9");


						openchangebutton2.style.display="inline-block";
						openchangebutton3.style.display="inline-block";
						openchangebutton4.style.display="inline-block";
						openchangebutton5.style.display="inline-block";
						openchangebutton6.style.display="inline-block";
						openchangebutton7.style.display="inline-block";
						openchangebutton8.style.display="inline-block";
						openchangebutton9.style.display="inline-block";
						document.getElementById('wizard-picture').disabled=false;
					});
					
					$('body').on('click','button[id^="closechange"]',function(e){
						var openchangebutton2 = document.getElementById("showtable2");
						var openchangebutton3 = document.getElementById("showtable3");
						var openchangebutton4 = document.getElementById("showtable4");
						var openchangebutton5 = document.getElementById("showtable5");
						var openchangebutton6 = document.getElementById("showtable6");
						var openchangebutton7 = document.getElementById("showtable7");
						var openchangebutton8 = document.getElementById("showtable8");
						var openchangebutton9 = document.getElementById("showtable9");

						openchangebutton2.style.display="none";
						openchangebutton3.style.display="none";
						openchangebutton4.style.display="none";
						openchangebutton5.style.display="none";
						openchangebutton6.style.display="none";
						openchangebutton7.style.display="none";
						openchangebutton8.style.display="none";
						openchangebutton9.style.display="none";
						document.getElementById('wizard-picture').disabled=true;

					});


	
				</script>




	<%@include file="../includePage/includeFooter.jsp"%>
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



</body>
</html>




