<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="org.hibernate.Hibernate,java.util.Set,java.util.HashSet,com.infotran.springboot.commonmodel.FoodTagUser,com.infotran.springboot.commonmodel.UserAccount"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="../includePage/includeNav.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>UserMain</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
<script src="<c:url value='/scripts/popper.min.js' />"></script>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
<style>

#body{
	height: 100vh;
   background-image: url(
   "https://images.unsplash.com/photo-1523294587484-bae6cc870010?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1464&q=80"
   );
   background-size: cover;
   background-position: center center;
   background-attachment: fixed;
}

#main{
	background-color:#FCFCFC;
}
@import	url('https://fonts.googleapis.com/css2?family=Noto+Serif+TC&display=swap');
.header {
	background-color: #000000;
}
td a {
	color: #0000C6;
}
.searchButton {
	height: 30px;
	border-radius: 5px;
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
	padding:5px;
	border-collapse: separate;
	border: solid black 1px;
	border-radius: 6px;
	-moz-border-radius: 6px;
}
.display {
	font-family: 'Noto Serif TC', serif;
}

.collumntogreen {
	color: green;
}
.collumntored {
	red;
}
table a {
	color: #0000C6;
}
.collumntogreen {
	color: green;
}
.collumntored {
	red;
}
table {
	border-collapse: separate;
	border: solid black 1px;
	border-radius: 6px;
	-moz-border-radius: 6px;
}
.display{
	font-family: 'Noto Serif TC', serif;
	font-size: 10px; 
}
button{
	color: black;
}
table a{
	color:#0000C6;
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
#uuidclick{
	cursor: pointer;
}
#uuidclick:hover{
	text-decoration: underline;
}
.nav-link{
	width:270px;
}

/* border radius example is drawn from this pen: https://codepen.io/shshaw/pen/MqMZGR
I've added a few comments on why we're using certain properties
*/
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
		
		$(window).on('load', function() {
			
			let urlss="${pageContext.request.contextPath}/";
			urlss+="<c:url value='userLoggin/getName'/>";
			console.log(urlss);
			
			$.ajax({
				type: "GET",
				url: urlss,	
				async:false,
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
</head>
<body id="body">

	<!-- ???????????? -->
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
	<div id="main" class="container mt-5 pb-5" style="min-height: 100vh;margin-bottom: 5%;border-radius: 15px;border-style: outset">

<!-- 	<div class="listing-area pt-30 pb-30"></div> -->

	<!-- <div class="container"> -->

		<div class="container " style=" height: 100%">
			<!--??????????????????bar  -->
			<div class="d-flex">
				<div class="p-2">
					<img id="replacepic" style="height: 100px"
						src="<c:url value='/userProtrait/${userAccount.userAccountDetail.useretailId}'/>">
				</div>

				<div class="p-2 flex-fill align-self-end justify-content-center">
					<h1><strong>${userAccount.userAccountDetail.nickName}</strong></h1>
				</div>


			</div>
			<div class="flex-fill bg-secondary p-1 mb-5"></div>

			<!--??????????????????-->
			<div class="d-flex" style="min-height:900px ; " >
				<div class="nav flex-column nav-pills col-3" id="v-pills-tab"
					role="tablist" aria-orientation="vertical" style="background-color: rgba(1, 0, 0, 0.1);font-size: 20px;border-radius: 10px;">
					<a class="nav-link active" id="v-pills-aboutUser-tab"
						data-toggle="pill" href="#v-pills-aboutUser" role="tab"
						aria-controls="v-pills-aboutUser" aria-selected="true"><i
						class="fas fa-file-alt"></i>?????????
					</a> <a class="nav-link" id="v-pills-friend-tab" data-toggle="pill"
						href="#v-pills-friend" role="tab" aria-controls="v-pills-friend"
						aria-selected="false"> <i class="fas fa-users"></i>??????
					</a> <a class="nav-link" id="v-pills-userMessage-tab"
						data-toggle="pill" href="#v-pills-userMessage" role="tab"
						aria-controls="v-pills-userMessage" aria-selected="false"> <i
						class="fas fa-comments"></i>?????????
					</a> <a class="nav-link" id="v-pills-userCollection-tab"
						data-toggle="pill" href="#v-pills-userCollection" role="tab"
						aria-controls="v-pills-userCollection" aria-selected="false">
						<i class="fas fa-heart"></i>?????????
					</a> <a class="nav-link" id="v-pills-userOrderList-tab"
						data-toggle="pill" href="#v-pills-userOrderList" role="tab"
						aria-controls="v-pills-userOrderList" aria-selected="false"> <i
						class="fas fa-file-alt"></i>????????????
					</a>

				</div>

				<div class="tab-content col-9" id="v-pills-tabContent col-9" >
					<div class="tab-pane fade show active" id="v-pills-aboutUser"
						role="tabpanel" aria-labelledby="v-pills-aboutUser-tab" style="" align="center">
						<h2><strong>?????????</strong></h2>
						<table border='1' class='table table-hover table-bordered '
							style='font-weight:bolder;font-size: 13px border-collapse:separate; border: solid #F0F0F0 2px; border-radius: 6px; -moz-border-radius: 6px;'>
							<tr>
								<td><span id="accountIndex">email:</span></td>
								<td><span> ${userAccount.accountIndex}</span>
								<button style="color: black; display: none">??????</button></td>
							</tr>
							<tr>
								<td><span>???????????????</span></td>
								<td><input class='genric-btn default circle arrow'
									style='color: black' type="file" disabled="disabled"
									id="wizard-picture" accept="image/*" name="photo"></td>
								<td id="showtable9" style="display: none"><button
										id="confirmPhoto" class='genric-btn default circle arrow'
										style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>?????????</span></td>
								<td><span id="nickNameSpan">${userAccount.userAccountDetail.nickName}</span><input
									value="${userAccount.userAccountDetail.nickName}"
									style="display: none" id="updateinputBasic2NickName"></input>
								<button id="updateBaisc2NickName"
										class='genric-btn default circle arrow'
										style="color: black; display: none">??????</button></td>
								<td id="showtable2" style="display: none"><button
										class='genric-btn default circle arrow' id="change2NickName"
										style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>?????????</span></td>
								<td><span id="birthDaySpan">${userAccount.userAccountDetail.birthDay}</span><input
									value="${userAccount.userAccountDetail.birthDay}"
									style="display: none" id="updateinputBasic2birthDay"></input>
								<button id="updateBaisc2birthDay"
										class='genric-btn default circle arrow'
										style="color: black; display: none">??????</button></td>
								<td id="showtable3" style="display: none"><button
										class='genric-btn default circle arrow' id="change2birthDay"
										style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>?????????</span></td>
								<td><span id="sexValueSpan">${userAccount.userAccountDetail.gender}</span><input
									value="${userAccount.userAccountDetail.gender}"
									style="display: none" id="updateinputBasic2gender"></input>
								<button id="updateBaisc2gender"
										class='genric-btn default circle arrow'
										style="color: black; display: none">??????</button></td>
								<td id="showtable4" style="display: none"><button
										id="change2gender" class='genric-btn default circle arrow'
										style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>?????????</span></td>
								<td><span id="locationSpan">${userAccount.userAccountDetail.location}</span><input
									value="${userAccount.userAccountDetail.location}"
									style="display: none" id="updateinputBasic2location"></input>
								<button id="updateBaisc2location"
										class='genric-btn default circle arrow'
										style="color: black; display: none">??????</button></td>
								<td id="showtable5" style="display: none"><button
										id="change2location" class='genric-btn default circle arrow'
										style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>?????????</span></td>
								<td><span id="phoneNumberSpan">${userAccount.userAccountDetail.phoneNumber}</span><input
									value="${userAccount.userAccountDetail.phoneNumber}"
									style="display: none" id="updateinputBasic2phoneNumber"></input>
								<button id="updateBaisc2phoneNumber"
										class='genric-btn default circle arrow'
										style="color: black; display: none">??????</button></td>
								<td id="showtable6" style="display: none"><button
										id="change2phoneNumber"
										class='genric-btn default circle arrow' style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>???????????????</span></td>
								<td><span id="realNameSpan">${userAccount.userAccountDetail.realName}</span><input
									value="${userAccount.userAccountDetail.realName}"
									style="display: none" id="updateinputBasic2RealName"></input>
								<button id="updateBaisc2realName"
										class='genric-btn default circle arrow'
										style="color: black; display: none">??????</button></td>
								<td id="showtable7" style="display: none"><button
										id="change2realName" class='genric-btn default circle arrow'
										style="color: black;">??????</button></td>
							</tr>
							<tr>
								<td><span>?????????</span></td>
								<td><span id="userTagsSpan"><c:forEach
											items="${userAccount.userTags}" var="hobby">${hobby.fkfoodtagid.foodTagName} </c:forEach></span><span
									id="updateinputBasic3FoodTagNames"
									style="color: black; display: none"></span> &nbsp; &nbsp;
									<button class='genric-btn default circle arrow'
										id="FoodTagNames" style="color: black; display: none">??????</button></td>
								<td id="showtable8" style="display: none"><button
										id="change3FoodTagNames"
										class='genric-btn default circle arrow' style="color: black;">??????</button></td>
							</tr>

						</table>
						<button class='genric-btn  primary medium' id="openchange"
							style=";">??????????????????</button>
						<button class='genric-btn primary medium' id="closechange"
							style="">??????</button>

					</div>
					<div class="tab-pane fade" id="v-pills-friend" role="tabpanel"
						aria-labelledby="v-pills-friend-tab">


						<!--???????????????-->
						<nav>
							<div class="nav nav-tabs" id="nav-tab" role="tablist">
								<a class="nav-item nav-link active" id="nav-myFriend-tab"
									data-toggle="tab" href="#nav-myFriend" role="tab"
									aria-controls="nav-myFriend" aria-selected="true" style="width: 130px;">
									<button id="checkFriendList" style="color: black;">????????????</button>
								</a> 
								<a class="nav-item nav-link" id="nav-searchFriend-tab"
									data-toggle="tab" href="#nav-searchFriend" role="tab"
									aria-controls="nav-searchFriend" aria-selected="false" style="width: 130px;">
									<button id="checkFriendList" style="color: black">???????????????</button>
								</a> 
								<a class="nav-item nav-link" id="nav-friendQequest-tab"
									data-toggle="tab" href="#nav-friendQequest" role="tab"
									aria-controls="nav-friendQequest" aria-selected="false" style="width: 130px;">
									<button class="btn-link" id="checkRequestList"
										style="color: black">????????????</button>
								</a> 
								<a class="nav-item nav-link" id="nav-message-tab"
									data-toggle="tab" href="#nav-message" role="tab"
									aria-controls="nav-message" aria-selected="false" style="width: 130px;">
									<button
										onclick="location.href='<c:url value='/user/websocket'/>'"
										class="btn-link" id="checkmessage" style="color: black">?????????
									</button> <%-- 										<a href="<c:url value='/user/websocket'/>">?????????</a> --%>
								</a>
							</div>
						</nav>
						<div class="tab-content" id="nav-tabContent">
							<div class="tab-pane fade show active" id="nav-myFriend"
								role="tabpanel" aria-labelledby="nav-myFriend-tab">
								<!--<button id="checkFriendList" style="color:black">????????????</button>-->
								<div class="friendsys m-3" id="userFriendList"></div>

							</div>
							<div class="tab-pane fade" id="nav-searchFriend" role="tabpanel"
								aria-labelledby="nav-searchFriend-tab">
								<div class="d-flex mt-3">
									<input class="m-2" id="nameSearch" type="search"
										placeholder="Search By nickName" aria-label="Search">
									<button class="searchButton btn-link mt-1" id="btn-search">Search</button>
								</div>
								<div class="friendsys m-3" id="searchResult"></div>
							</div>


							<div class="tab-pane fade" id="nav-friendQequest" role="tabpanel"
								aria-labelledby="nav-friendQequest-tab">
								<h6>????????????</h6>
								<!--<button id="checkRequestList" style="color:black">????????????</button>-->
								<div class="friendsys m-3" id="friendRequest"></div>
							</div>
						</div>
					</div>

					<div class="tab-pane fade" id="v-pills-userMessage" role="tabpanel"
						aria-labelledby="v-pills-userMessage-tab">

						<h2>${userAccount.userAccountDetail.nickName}????????????</h2>


						<iframe allowtransparency="" frameborder="0" id="fbLike"
							scrolling="no" src=""
							style="border-bottom: medium none; border-left: medium none; width: 250px; height: 30px; overflow: hidden; border-top: medium none; border-right: medium none"></iframe>


						<!-- 						??????????????????input & ?????? -->
						<input placeholder='Hello....' id="commentInput"></input>
						<button type="button" class="genric-btn  primary medium"
							id="addNewComment">????????????</button><span style="display:none" id="messagePrompt"></span>
			
						<!-- 						??????Ajax????????? -->
						<div class="rounded border border-warning  table "
							style="overflow-y: auto; height: 300px;margin-top: 15px;" id="commentsForUser"></div>


						<!--      ??????jstl????????? -->
						<%--      <c:choose> --%>
						<%--       <c:when test="${not empty userAccount.msnBox}"> --%>
						<!--        <br> -->
						<%--        <c:forEach var='msn' items='${userAccount.msnBox}' varStatus='ms'> --%>
						<%--         <c:if test="${msn.messageBox == null }"> --%>

						<!--          <div class="row " style="border: 1px solid red"> -->
						<!--           <img> -->
						<%--           <h4>??????${ms.count }</h4> --%>
						<%--           &nbsp; <span>${msn.netizenAccount.userAccountDetail.nickName }</span>&nbsp;<span>??? --%>
						<%--            ${msn.time }</span> --%>
						<!--           <div class="col-12 "> -->
						<%--            <blockquote class="generic-blockquote">${msn.text}</blockquote> --%>
						<!--            <button class="pull-right" value="" style="color: black">????????????</button> -->
						<%--            <c:forEach var='msnReply' items='${msn.replyMessageBoxes}' --%>
						<%--             varStatus='rms'> --%>
						<!--             <img> -->
						<%--             <h5>??????${rms.count}</h5>&nbsp; --%>
						<%--           <span>${msnReply.netizenAccount.userAccountDetail.nickName}</span>&nbsp;<span>??? --%>
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
						<!--           ?????????????????? -->
						<%--       </c:otherwise> --%>
						<%--      </c:choose> --%>
					</div>


					<div class="tab-pane fade" id="v-pills-userCollection"
						role="tabpanel" aria-labelledby="v-pills-userCollection-tab">
						<h2>??????????????????</h2>
						<a href="<c:url value='/restSearch/map'/>"><button
								type="button" class="genric-btn  primary medium"
								id="addNewComment">????????????</button></a><br><br>

			<div class="memoBoard" id="memoBoard"></div>

					</div>

					<!-- ????????? -->
					<div class="tab-pane fade" id="v-pills-userOrderList" role="tabpanel" aria-labelledby="v-pills-userOrderList-tab">
						<h2>????????????</h2>
						<table id="orderlist" class="display" style="font-size: 13px;">
							<thead>
								<tr>
									<th>??????</th>
									<th>?????????</th>
									<th>????????????</th>
									<th>?????????</th>
									<th>??????</th>
									<th>??????</th>
									<th>??????</th>
									<th>????????????</th>
								</tr>
							</thead>
							<tbody>
								<tr>
								</tr>
							</tbody>
						</table>
						
						<div class="col-12" style="margin-top: 20px;font-size: 15px;display: none;" id="orderdetailforeach">
							<h4><strong>????????????</strong></h4>
							<div class="table-responsive-sm">
								<table class="table table-hover text-info text-justify " style="font-size: 15px;">
									<thead>
										<tr class=" Active">
											<th>#??????</th>
											<th>?????????</th>
											<th>??????</th>
											<th>??????</th>
										</tr>
									</thead>
									<tbody id="tbodyid">
										<!-- product here -->
									</tbody>
								</table>
							</div>
						</div>
						
					</div>
					<!-- ????????? -->
				</div>
			</div>
		</div>
	<!-- </div> -->
</div>

<!-- 	<div class="listing-area pt-120 pb-120"></div> -->

	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>
	
	<!-- Modal HTML -->
		<div class="modal fade" id="confrimPaidAlready" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
		  <div class="modal-dialog modal-dialog-centered" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalCenterTitle"></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		      <!-- write here -->
		       <div class="col-12" id="modalContent"> 
		       		
		       </div>		      	
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="genric-btn danger-border circle arrow small" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>        
		<!-- Modal HTML -->

						  	<div aria-live="polite" data-autohide="true" aria-atomic="true" style="position: absolute; min-height: 200px;">
								  <div class="toast" style="position: fixed;top:160px;;left:50%;width: 200px;text-align: center ">
								    <div class="toast-header">
								      <strong class="mr-auto">????????????</strong>
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
	
	//??????????????????????????????
	  $("#sssssss").scroll(function(){
		    var h = $(this).height();//div?????????????????????
		    var sh = $(this)[0].scrollHeight;//??????????????????$(this)??????jQuery????????????$(this)[0]????????????dom??????
		    var st =$(this)[0].scrollTop;//???????????????????????????????????????????????????div???????????????
		    if(h+st>=sh){
		    //??????????????????????????????????????????????????????
		      alert("???????????????");
		      $("#commentsForUser").append("<i>???????????????~~</i>"+"<br>");//?????????????????????????????????????????????????????????????????????????????????.??????????????????????????????????????????????????????
		    }
		  })
			$(document).ready(function () {			
				
					var Table = $("#orderlist").DataTable({
						 language: {
						        "processing": "?????????...",
						        "loadingRecords": "?????????...",
						        "lengthMenu": "?????? _MENU_ ?????????",
						        "zeroRecords": "?????????????????????",
						        "info": "????????? _START_ ??? _END_ ??????????????? _TOTAL_ ???",
						        "infoEmpty": "????????? 0 ??? 0 ??????????????? 0 ???",
						        "infoFiltered": "(??? _MAX_ ??????????????????)",
						        "infoPostFix": "",
						        "search": "??????:",
						        "paginate": {
						            "first": "?????????",
						            "previous": "?????????",
						            "next": "?????????",
						            "last": "????????????"
						        },
						        "aria": {
						            "sortAscending": ": ????????????",
						            "sortDescending": ": ????????????"
						        }
						    },
						    data:[],
						    columns: [
						    	{ "data": "orderId" },
				                { "data": "uuid" ,
						    	  "render": function(data,type,row,meta){
						    		return "<font color='#191970' id='uuidclick'>"+data+"</font>";
						    	  }},
				                { "data": "orderCreatedDate" ,
			                	  "render": function (data, type, row, meta) {
			                	   var time2 = new Date(data).format("yyyy-MM-dd hh:mm:ss");
			                       return time2.substr(0,4)+'/'+time2.substr(5,2)+'/'+time2.substr(8,2)+' '+time2.substr(11,5)}},
				                { "data": "receiveName" },
				                { "data": "receivePhone" },
				                { "data": "totalCost" ,
				                  "render": function(data,type,row,meta){
									return "$"+data;				                	  
				                  }},
				                { "data": "status"},
				                { "render": function(data,type,row,meta){
				                  return "<td><button style='background-color:#00008B;border-radius:15px;' id='cheqGreenMonster' data-toggle='modal' data-target='#confrimPaidAlready'><i class='far fa-credit-card'></i></button></td>";
				                  }}
						    ],
						    filter: true,
						    bPaginate: true,
						    info: true,
						    ordering: true,
						    processing: true,
						    retrieve: true,
						    searching: true, //??????filter??????
						});
					
					Date.prototype.format = function (fmt) {
						  var o = {
						    "M+": this.getMonth() + 1, //??????
						    "d+": this.getDate(), //???
						    "h+": this.getHours(), //??????
						    "m+": this.getMinutes(), //???
						    "s+": this.getSeconds(), //???
						    "q+": Math.floor((this.getMonth() + 3) / 3), //??????
						    "S": this.getMilliseconds() //??????
						  };
						  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
						  for (var k in o)
						  if (new RegExp("(" +  k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" +  o[k]).substr(("" + o[k]).length)));
						  return fmt;
						}
						///////////////////////////////////////////////////////
						$.ajax({
							method:"GET",	
							url:"/PepperNoodles/user/getOrderList",
							contentType: 'application/json; charset=utf-8',
							dataType:'json',
					        async : true,
					        cache: false,
					        success:function(result){
					        	var orderlist = result.AccountMemberOrderList;
					        	Table.clear().draw();
					            Table.rows.add(result.AccountMemberOrderList).draw();
					        },
					        error: function (result) {
					        	console.log("?????????");
					        }
						});
						
						$('body').on('click','#cheqGreenMonster',function(e){
							e.preventDefault();
							$("#exampleModalCenterTitle").empty();
							$("#modalContent").empty();
							var status = $(this).parent().prev().text();
							var orderid = $(this).parent().prevAll("td:eq(6)").text();
							if (status == '????????????'){
								data = new FormData();
								data.append("orderid",orderid);
								$.ajax({
									method:"POST",
									url:"/PepperNoodles/user/reCheqoutToGreenMonster",
									data:data,
									contentType: false, 
									processData: false,
									cache: false,  //????????????
							        async : true,
							        success: function (response) {
							        	localStorage.setItem("RecheckECpayform",response.ecpayform);
							        	window.open("http://localhost:433/PepperNoodles/shoppingSystem/RecheckFormECpay2", '_blank');
							        },
							        error: function (url) {
							        	console.log("???????????????");
							        }	
								});    
							} else if (status == "??????????????????"){
								$("#exampleModalCenterTitle").text("?????????????????????????????????");
								$("#modalContent").append("??????"+orderid+"???????????????????????????");
							} else {
								$("#exampleModalCenterTitle").text("???????????????????????????");
								$("#modalContent").append(orderid+"?????????????????????");
							}
							
						});
						
						$("body").on("click","#uuidclick",function(){
							var orderid = parseInt($(this).parent().prev().text(),10);		
							console.log(orderid);
							$.ajax({
								method:"GET",	
								url:"/PepperNoodles/user/getDetailForEachOrder?orderid="+orderid+"",
								contentType: 'application/json; charset=utf-8',
								dataType:'json',
						        async : true,
						        cache: false,
						        success:function(result){
						        	$("#orderdetailforeach").toggle("slow");
						        	$("#tbodyid").empty();
						        	console.log(result.pname);
						        	console.log(result.pprice);
						        	console.log(result.pamount);
						        	var pname = result.pname;
						        	var pprice = result.pprice;
						        	var pamount = result.pamount;
						        	for (var i=0;i<pname.length;i++){
						        		var tr   = $('<tr></tr>');
						        		var col1 = $('<td>#'+(i+1)+'</td>');
						        		var col2 = $('<td>'+pname[i]+'</td>');
						        		var col3 = $('<td>'+pprice[i]+'</td>');
						        		var col4 = $('<td>'+pamount[i]+'</td>');
						        		tr.append(col1).append(col2).append(col3).append(col4);
						        		$('#tbodyid').append(tr);
						        	}
						        
						        },
						        error: function (result) {
						        	console.log("?????????");
						        }
							});
							
						});
						
						
						///////////////////////////////////////////////
// 					$(document).ready(function(){ 
// 					$(window).on('load', function () {
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
					
						});
						
						
						
						
							//??????nickName????????????
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
							//console.log(response[0]);
							let table =  document.createElement("table");
							table.border="1";
							let trh = document.createElement("tr");
 							let th1 = document.createElement("th");
 							th1.innerHTML= "??????"
 							let th2 = document.createElement("th");
 							th2.innerHTML = "??????"
 							let th3 = document.createElement("th");
 							th3.innerHTML = "??????"
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
								//???nickname????????????
								let a =document.createElement("a");
								a.href="${pageContext.request.contextPath}/userView/"+response[i].accountIndex;
								a.innerText =response[i].userAccountDetail.nickName;								
								td2.appendChild(a);
								//??????,???account,???nickName
								tr.appendChild(td3);
								tr.appendChild(td1);
								tr.appendChild(td2);
								table.appendChild(tr);
							}
							$(id).append(table);
							
						}

						//??????????????????????????????
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
									if(response.length>0){
										//$("#checkRequestList").append("!!");
										showRequestList(response);
									}else{
										showRequestList(response);
									}
									
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
					}
					
					function showRequestList(response){
							$("#friendRequest").html("");
							//let result = JSON.stringify(response);
							//console.log(response[0]);
							if(response.length>0){
								let table =  document.createElement("table");
								table.border="1";
	 							let trh = document.createElement("tr");
	 							let th1 = document.createElement("th");
	 							th1.innerHTML= "??????"
	 							let th2 = document.createElement("th");
	 							th2.innerHTML = "??????"
	 							let th3 = document.createElement("th");
	 							th3.innerHTML = "??????"
	 							let th4 = document.createElement("th");
	 							th4.innerHTML = "?????????";
	 							
	 							trh.appendChild(th1);
	 							trh.appendChild(th2);
	 							trh.appendChild(th3);
	 							trh.appendChild(th4);
	 							table.appendChild(trh);
	 							
								let length = Object.keys(response).length;
								for (let i =0;i< length;i++){
									let tr  = document.createElement("tr");
									//td1 mail,td2 nick name ,td3 pic,td4 button
									let td1 = document.createElement("td");
									let td2 = document.createElement("td");
									let td3 = document.createElement("td");
									let td4 = document.createElement("td");
									let divFrame = document.createElement("div");
									divFrame.className="friendsysImg";
									let img = document.createElement("img");
									let imgSrc="${pageContext.request.contextPath}/userProtrait/"+response[i].userAccountDetail.useretailId;
									img.src="<c:url value='"+imgSrc+"'/>";
									divFrame.append(img);
									
									td3.appendChild(divFrame);								
									
									td1.innerHTML=response[i].accountIndex;
									console.log(response[i].accountIndex);
									console.log(response[i].userAccountDetail);
									
									//???nickname????????????
									let a =document.createElement("a");
									a.href="${pageContext.request.contextPath}/userView/"+response[i].accountIndex;
									a.innerText =response[i].userAccountDetail.nickName;
									
									td2.appendChild(a);
									//???????????????????????????
									let btn_comfirm = document.createElement("button");
									btn_comfirm.innerHTML="AddFriend";
									btn_comfirm.style.color="#D26900";
									btn_comfirm.value=response[i].accountIndex;
									btn_comfirm.addEventListener("click",addFriend);
									// a1_comfirm.href="${pageContext.request.contextPath}/"+"/${userAccount.accountIndex}"+"/"+response[i].accountIndex;
									// a1_comfirm.innerHTML="AddFriend";

									// let a1_cancel =document.createElement("a");
									 td4.appendChild(btn_comfirm);
									
									//??????,???account,???nickName
									tr.appendChild(td3);
									tr.appendChild(td1);
									tr.appendChild(td2);
									tr.appendChild(td4);
									table.appendChild(tr);
								}
								$("#friendRequest").append(table);
							}
							

							
						}
						//addFriend?????????function
						function addFriend(){
// 							alert(this.value);
							let urls="${pageContext.request.contextPath}/";
							urls+="<c:url value='MainUserAddFriendwithIndex'/>";
							urls+="/${userAccount.accountIndex}"
							urls+="/"+this.value;
//							console.log(urls);
							
							
						$.ajax({
								type: "GET",
								url: urls,	
								data:	Object,
								dataType: "text",
								success: function (response) {
									alert("???????????????");
									console.log(response);
									checkRequestList();
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
						 }
						
						//???????????????????????????funciton
						//????????????
						 
		
						
						
						

							
						});//onload end
					
					
// 					????????????
						function showAllComments(){
					var commentsForUser = document.getElementById("commentsForUser")
					var segment="";
					commentsLength = 0;
					$.ajax({
						method:"GET",
						url:"/PepperNoodles/user/showAllCommentAjax",
//							cache: false,  //????????????
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
// 				        		formatString[2] + '<br>' +
				        		var formatPrint  =  formatString[0]+ '/' + formatString[1] +  '<br>' + formatString[3];
				        		
				        		segment +="<table border='1' class='table table-hover table-bordered ' style='font-size: 8px border-collapse:separate; border:solid blue 1px;border-radius:6px;-moz-border-radius:6px;'>";//<th>?????????</th><th>?????????</th><th>??????</th><th>??????</th><th>????????????</th><th>???</th><th>??????</th>";
				        		segment += "<tr class='table-primary'><td>??????" + (i+1) + "</td><td>" ;
				        		segment += result[i].netizenAccount.userAccountDetail.nickName + "</td><td>" ;
				        		segment += formatPrint + "</td><td data-toggle='tooltip' id='likeof" + i + "'  >" ;
				        		segment += result[i].likeAmount + "</td><td><input  class='selectedinput ' disabled='disabled'size='20' value='" + result[i].text +"'>" ;
			        			segment +=  "</input><button  name='updateComment' "+ i +" class='genric-btn default circle arrow' style='display:none;color:black' >confirm</button>";
			        			segment += "<span  style='display:none;visibility:hidden'>" + result[i].text + "</span>";
			        			segment += "<span  style='display:none'>" + result[i].time + "</span><span  style='display:none'>" + result[i].likeAmount + "</span>";
			        			segment += "<span class='userMessageId'  style='display:none'>" + result[i].userMessageId + "</span></td><td>";
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
// 						        		formatString[2] + '<br>' +
						        		
						        		var formatPrint  =  formatString[0]+ '/' + formatString[1] +  '<br>' + formatString[3];
									
										segment += "<tr class='table-info'><td>??????" + (j+1) + "</td><td>" ;
						        		segment += result[i].replyMessageBoxes[j].netizenAccount.userAccountDetail.nickName  + "</td><td>" ;
						        		segment += formatPrint  + "</td><td data-toggle='tooltip' id='likeof" + i + j +"'>" ;
						        		segment += result[i].replyMessageBoxes[j].likeAmount + "</td><td>";
						        		segment += "<input disabled='disabled'size='20' value='" + result[i].replyMessageBoxes[j].text +"'>" ;
										segment +=  "</input><button name='updateComment' " + i + j +" style='display:none;color:black'  class='genric-btn default circle arrow' >confirm</button>";
					        			segment += "<span  style='display:none;visibility:hidden'>" + result[i].replyMessageBoxes[j].text + "</span>";
					        			segment += "<span  style='display:none'>" + result[i].replyMessageBoxes[j].time + "</span><span  style='display:none'>" + result[i].replyMessageBoxes[j].likeAmount + "</span>";
					        			segment += "<span  style='display:none'>" + result[i].replyMessageBoxes[j].userMessageId + "</span></td><td>";
					        			segment += "<button type='button' class='genric-btn default-border circle' style='color: black' name  ='likeButton" + i + j +"' >like</button><button type='button' class='genric-btn default circle arrow' style='color: black ;display:none' name  ='dislikeButton"  + i + j +"' >withdraw</button></td>";
					        			segment += "<td><button class='genric-btn default circle arrow' style='color: black' name='edit"  + i + j + "'>edit</button></td><td>";
					        			segment +="<button class='genric-btn default circle arrow' type='button' style='color: black' name  ='deleteComment" + i + j +	"' >delete</button><span  style='display:none'>" + result[i].replyMessageBoxes[j].userMessageId + "</span></tr>";
									}
				        		segment += "</table><span class='mt-0 pt-0'><input placeholder='?????????....'></input><button type='button' class='genric-btn primary medium' style='margin-left:10px;' name='addreply"  + i +"' >reply</button><span  style='display:none'>" + result[i].userMessageId + "</span></span><br><br><br>";
				        		
// 				        		segment +="<button data-toggle='modal' data-target='#exampleModal' type='button' style='color: black' name  ='updateComment" + i +"' >??????????????????</button><span  style='display:none'>" + result[i].text + "</span><span  style='display:none'>" + result[i].time + "</span><span  style='display:none'>" + result[i].likeAmount + "</span><br><br><hr>";
				        	
				        	}

			        		commentsForUser.innerHTML = segment;
				        	
				        },
				        error: function (result) {
// 				        	alert(result);
				            $("#commentsForUser").text("fail"); //?????????????????????result?????????
				        }
					});
						}
						
						//??????????????????????????????
						  $("#commentsForUser").scroll(function(){
							    var h = $(this).height();//div?????????????????????
							    var sh = $(this)[0].scrollHeight;//??????????????????$(this)??????jQuery????????????$(this)[0]????????????dom??????
							    var st =$(this)[0].scrollTop;//???????????????????????????????????????????????????div???????????????
							    if(h+st>=sh){
							    //??????????????????????????????????????????????????????
							      $("#commentsForUser").append("<i>???????????????~~</i>"+"<br>");//?????????????????????????????????????????????????????????????????????????????????.??????????????????????????????????????????????????????
							    }
							  })
						
						
						//????????????(usermain????????? useraccount ????????????????????????)
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
						
						//??????????????????
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
						
						
						
						
						
						//?????????????????????
						$('body').on('click','button[name^="edit"]',function(e){
		        			e.preventDefault;
		        			var openEdit = $(this).parent().prevAll().children(":input");
		        			var confirmEdit =openEdit.next();
		        			var hide =openEdit.next();
		        			openEdit.attr('disabled', false);
		        			confirmEdit.show();

						});
					
					//update ??????????????????(???????????????)
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
// 							alert(id);
// 							alert(text);
// 							alert(time);
// 							alert(likeAmount);

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
// 									alert(result);
									showAllComments();
// 									alert(result.length);
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
// 									alert(thrownError);
								}
							});
		        		});
					

					//????????????
					$('body').on('click','button[name^="likeBut"]',function(e){
		        			e.preventDefault;
		        			var urls = "/PepperNoodles/user/updateLikeCommentAjax/?id=";
		        			var button = $(this);
							var id         =$(this).parent().prevAll().children("td span:eq(3)").text();
							var changeLikeAmount = $(this).parent().prevAll("td:eq(1)");
							urls += id;
// 							alert(id);

							
							$.ajax({
								type:"GET",
								url: urls ,
								dataType: "text",
								success: function (result) {
// 									alert(result);


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
					
					//?????????
					$('body').on('click','button[name^="dislikeBut"]',function(e){
		        			e.preventDefault;
		        			var urls = "/PepperNoodles/user/updateDisLikeCommentAjax/?id=";
		        			var button = $(this);
							var id         =$(this).parent().prevAll().children("td span:eq(3)").text();
							var changeLikeAmount = $(this).parent().prevAll("td:eq(1)");
							urls += id;
// 							alert(id);

							
							$.ajax({
								type:"GET",
								url: urls ,
								dataType: "text",
								success: function (result) {
// 									alert(result);


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
					
// 					id='likeof"  ????????????????????? ???????????????
					$('body').on('mouseenter','td[data-toggle="tooltip"]',function(e){
	        			e.preventDefault;

	        			var likebutton = $(this);
	        			var msnID = $(this).next().next().next().next().children("span").text();

// 						likebutton.tooltip();

	        			$.ajax('/PepperNoodles/user/showWhoLikeAjax?msnID='+ msnID).always(function(result) {
	        				var x ="???????????????:\n\r" ;
								if(result.length==0){
									x ="???????????????????????????~"
								}else{
									for( i =0;i<result.length;i++){
										x += result[i].userAccountDetail.nickName;
										x += "\n\r";
									}
								}
								likebutton.attr("title",x)
// 								likebutton.tooltip();

		        			}
	        	         );
// 						$.ajax({
// 							type:"GET",
// 							url:"/PepperNoodles/user/showWhoLikeAjax?msnID=" + msnID,
// 							dataType: "json",
// 							success: function (result) {
// 								var x ="???????????????:" ;
								
// 								if(result.length==0){
// 									x ="???????????????????????????~"
// 								}else{
// 									for( i =0;i<result.length;i++){
// 										x += result[i].userAccountDetail.nickName;
// 										x += " ";
// 									}
// 								}
								
// 								$(this).attr('title', result);
// 								$(this).tooltip({ position: "bottom right", opacity: 1 });
// 								alert(x);
// 								$(this).tooltip();
// 							        	},
							        	
// 					        error: function (result) {
// 					        	alert("fail");
// 					        }
// 						});

					});

					
					
					//??????????????????????????????????????????
					$('body').on('click','button[id^="change2"]',function(e){
	        			e.preventDefault;
	        			var openEdit = $(this).parent().prevAll().children("input:eq(0)");
	        			var confirmEdit =openEdit.next();
	        			var hide =openEdit.prev();


	        			openEdit.toggle();
	        			confirmEdit.toggle();
	        			hide.toggle();

					});
					
					//???????????????????????????
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
// 								alert(result);

							},

						error: function (thrownError) {
								console.log(thrownError);
								alert(thrownError);
							}
						});
	        		});	
					
					
					//????????????????????????????????????
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
// 					        		alert(result[0].foodTagName);
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
					
					

					//????????????????????????????????????
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
// 	        			alert(confirmEdit.value)
	        			
							$.ajax({
								type:"POST",
								url:"/PepperNoodles/user/confirmFoodTagsChangeAjax",
								data:JSON.stringify(hobbyVal),
								contentType:'application/json;charset=UTF-8',
								dataType: "json",
								success: function (result) {
									console.log(result);
									$("#userTagsSpan").text("");
									for( i =0;i<result.length;i++){
										console.log(result[i].foodTagIid);
										

										$("#userTagsSpan").append(result[i].foodTagName);
										$("#userTagsSpan").append(" ");

									}

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
							cache: false,  //????????????
					        async : true,
					        success: function (result) {
// 								alert("photo changed");
								
								location.reload();
								
// 								var replacepic = document.getElementById("replacepic");
// 								$.ajax({
									
// 									method:"GET",
// 									url:"/PepperNoodles/user/replacePhotoAjax",

// 									cache: false,  //????????????
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
 			createSideMemo();
// 			let urlss="${pageContext.request.contextPath}/";
// 			urlss+="<c:url value='userLoggin/getName'/>";
// 			console.log(urlss);
			
// 			$.ajax({
// 				type: "GET",
// 				url: urlss,				
// 				dataType: "text",
// 				success: function (response) {
// 					console.log(response);	
// 				},
// 				error: function (thrownError) {
// 					console.log(thrownError);
// 				}
				
// 			});
 		
			
// 			//???bar?????????????????????????????????
			$(".header-sticky").addClass("sticky-bar");
 			$(".header-sticky").css("height", "90px");
			$(".header-sticky").css("position","static")

 			//???loading????????????
 			$('#preloader-active').delay(450).fadeOut('slow');
 			$('body').delay(450).css({
 				'overflow' : 'visible'
 		});			
			
 		});
 		
 		
			function positionShow(){
				console.log(this.id);
				let restId = this.id.slice(4,);
				
				let url = "<c:url value='/restSearch/userSingleRestPage/' />" + restId;
				features = "width="+1200+",height="+600+",top="+50+",left="+50; 
				window.open(url,"toolbar=no,location=no,directories=no",features);
			}
		
 		
 		
 			//????????????memo???function
 			function createSideMemo(){
 				//memo
				urls = "/PepperNoodles/user/showUserCollections"; 				
 				$.ajax({
 					method:'GET',
 					url: urls,
 					dataType:'json',
 					success:function(result){
 						console.log(JSON.stringify(result));
 					
 		 				var memo=document.getElementById("memoBoard");
 		 				var memosheet = document.createElement("table");
		 					memosheet.style.margin='10px';


 		 				var HTMLtable="";
 					
 						if(result.length>0){
 		 					for(let i=0;i<result.length;i++){
 		 						
 		 						//??????td1???roleSpan
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
 		 					        
 		 					    //????????????
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

 		 					memosheet.style.padding='5px';
 		 					$(".getPositionButton").click(positionShow);
 		 					
 		 				}else{
 		 					memo.innerHTML="<h2>???????????????????????????!</h2>";
 		 				}
 					},
 					error:function(result){
 						}
 					});
 				};
 		
 	</script>



</body>
</html>




