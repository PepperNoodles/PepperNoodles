<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<<<<<<< HEAD
=======
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
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD
<head>
=======
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
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD
<!--抓取原本路徑用-->
<base localhref= />

<meta charset="UTF-8">
<title>userMain</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->

<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.js'/>"></script>

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

<link rel="stylesheet"
	href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/slicknav.css' />">
<link rel="stylesheet" href="<c:url value='/css/flaticon.css' />">
<link rel="stylesheet" href="<c:url value='/css/animate.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />">
<link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />">
<link rel="stylesheet" href="<c:url value='/css/slick.css' />">
<link rel="stylesheet" href="<c:url value='/css/nice-select.css' />">
<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<style>
.header {
	background-color: #000000;
}

a {
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
     border-collapse:separate; 
    border:solid black 1px; 
     border-radius:6px; 
     -moz-border-radius:6px; 
 } 
/* td, th { */
/*     border-left:solid black 1px; */
/*     border-top:solid black 1px; */
/* } */

/* th { */
/*     background-color: blue; */
/*     border-top: none; */
/* } */

/* td:first-child, th:first-child { */
/*      border-left: none; */
/* } */
</style>
<script type="text/javascript" language="javascript">
window.onload = fackbooklike;
function fackbooklike() {
var fbLike = document.getElementById("fbLike");
if (fbLike)
{
document.getElementById("fbLike").src = "http://www.facebook.com/plugins/like.php?href=" + location.href + "&layout=standard&show_faces=true&width=350&action=like&colorscheme=light&height=25";
}
}
</script>
</head>

<body>
	<!-- 讀取圖案 -->
	<div id="preloader-active">
		<div
			class="preloader d-flex align-items-center justify-content-center">
			<div class="preloader-inner position-relative">
				<div class="preloader-circle"
					style="background-color: rgb(102, 102, 102);"></div>
				<div class="preloader-img pere-text">
					<img src="<c:url value="/images/logo/peppernoodle.png" />" alt="">
				</div>
			</div>
		</div>
	</div>

	<!-- 最上層bar -->
	<header>
		<!-- Header Start -->
		<!-- 覆蓋用 -->
		<div style="height: 90px"></div>

		<div class="header-area header">
			<!--  header-transparent -->
			<div class="main-header sticky-top">
				<div class="header-bottom header-sticky">
					<div class="container-fluid">
						<div class="row align-items-center">
							<!-- Logo -->
							<div class="col-xl-2 col-lg-2 col-md-1">
								<div class="logo">
									<a href="/PepperNoodles"><img style="height: 80px"
										src="<c:url value="/images/logo/peppernoodle.png" />" alt=""></a>
								</div>
							</div>
							<div class="col-xl-10 col-lg-10 col-md-8">
								<!-- Main-menu -->
								<div class="main-menu f-right d-none d-lg-block">
									<nav>
										<ul id="navigation">
											<li><a href="index.html">Home</a></li>
											<li><a href="about.html">About</a></li>
											<li><a href="#">城市</a>
												<ul class="submenu">
													<li><a href="blog.html">台北</a></li>
													<li><a href="blog_details.html">新北</a></li>
													<li><a href="elements.html">基隆</a></li>
													<li><a href="listing_details.html">桃園</a></li>
												</ul></li>
											<li><a href="#">美食</a>
												<ul class="submenu">
													<li><a href="blog.html">美式</a></li>
													<li><a href="blog_details.html">日式燒烤</a></li>
													<li><a href="elements.html">韓式</a></li>
													<li><a href="listing_details.html">炸物</a></li>
												</ul></li>
											<li><a href="#">排行榜</a>
												<ul class="submenu">
													<li><a href="blog.html">免費排行</a></li>
													<li><a href="blog_details.html">付費排行</a></li>
													<li><a href="elements.html">周排行</a></li>
													<li><a href="listing_details.html">綜合排行</a></li>
												</ul></li>
											<li><a href="about.html">論壇</a></li>
											<li><a href="#">最新消息</a>
												<ul class="submenu">
													<li><a href="blog.html">菜色新聞</a></li>
													<li><a href="blog_details.html">最新優惠</a></li>
													<li><a href="elements.html">新開幕</a></li>
												</ul></li>
											<li><a href="about.html">發表食記</a></li>
											<!-- <li><a href="contact.html">Contact</a></li> -->
											<!-- <li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->
											<li class="login"><a href="loginSystem/loginPage"> <i
													class="ti-user"></i> Sign in or Register
											</a></li>
										</ul>
									</nav>
								</div>
							</div>
							<!-- Mobile Menu -->
							<div class="col-12">
								<div class="mobile_menu d-block d-lg-none"></div>
							</div>
						</div>
					</div>
=======
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
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git
				</div>
			</div>
		</div>
<<<<<<< HEAD
		<!-- Header End -->
	</header>
=======
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
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD


	<div class="container mt-10" style="width: 80%">
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
		<div class="flex-fill bg-secondary p-1 mb-5"></div>

		<!--左邊的分隔用-->
		<div class="d-flex">
			<div class="nav flex-column nav-pills col-3" id="v-pills-tab"
				role="tablist" aria-orientation="vertical">
				<a class="nav-link active" id="v-pills-home-tab" data-toggle="pill"
					href="#v-pills-home" role="tab" aria-controls="v-pills-home"
					aria-selected="true"><i class="fas fa-home"></i>Home</a> <a
					class="nav-link" id="v-pills-friend-tab" data-toggle="pill"
					href="#v-pills-friend" role="tab" aria-controls="v-pills-friend"
					aria-selected="false"><i class="fas fa-users"></i>好友</a> <a
					class="nav-link" id="v-pills-aboutUser-tab" data-toggle="pill"
					href="#v-pills-aboutUser" role="tab"
					aria-controls="v-pills-aboutUser" aria-selected="false"><i
					class="fas fa-file-alt"></i>關於我</a> <a class="nav-link"
					id="v-pills-userMessage-tab" data-toggle="pill"
					href="#v-pills-userMessage" role="tab"
					aria-controls="v-pills-userMessage" aria-selected="false"><i
					class="fas fa-comments"></i>留言區</a> <a class="nav-link"
					id="v-pills-userCollection-tab" data-toggle="pill"
					href="#v-pills-userCollection" role="tab"
					aria-controls="v-pills-userCollection" aria-selected="false"><i
					class="fas fa-heart"></i>收藏區</a>

			</div>
			<div class="tab-content" id="v-pills-tabContent col-9">
				<div class="tab-pane fade show active" id="v-pills-home"
					role="tabpanel" aria-labelledby="v-pills-home-tab">
					<h2>基本資料</h2>
					<p id="accountIndex">email: ${userAccount.accountIndex}</p>
					<p>性別：${userAccount.userAccountDetail.gender}</p>
					<p>地區：${userAccount.userAccountDetail.location}</p>
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


					<!-- 新增主要留言input & 按鈕 -->
					<input placeholder='Hello....' id="commentInput"></input>
					<button type="button" class="genric-btn default circle arrow" id="addNewComment">新增留言</button>

					<!-- 使用Ajax的方法 -->
					<div class="container-fluid" style="overflow: scroll; height: 400px;"
						id="commentsForUser" class="table"></div>


					<!-- 					使用jstl的方法 -->
					<%-- 					<c:choose> --%>
					<%-- 						<c:when test="${not empty userAccount.msnBox}"> --%>
					<!-- 							<br> -->
					<%-- 							<c:forEach var='msn' items='${userAccount.msnBox}' varStatus='ms'> --%>
					<%-- 								<c:if test="${msn.messageBox == null }"> --%>

					<!-- 									<div class="row " style="border: 1px solid red"> -->
					<!-- 										<img> -->
					<%-- 										<h4>留言${ms.count }</h4> --%>
					<%-- 										&nbsp; <span>${msn.netizenAccount.userAccountDetail.nickName }</span>&nbsp;<span>於 --%>
					<%-- 											${msn.time }</span> --%>
					<!-- 										<div class="col-12 "> -->
					<%-- 											<blockquote class="generic-blockquote">${msn.text}</blockquote> --%>
					<!-- 											<button class="pull-right" value="" style="color: black">回覆留言</button> -->
					<%-- 											<c:forEach var='msnReply' items='${msn.replyMessageBoxes}' --%>
					<%-- 												varStatus='rms'> --%>
					<!-- 												<img> -->
					<%-- 												<h5>回覆${rms.count}</h5>&nbsp; --%>
					<%-- 										<span>${msnReply.netizenAccount.userAccountDetail.nickName}</span>&nbsp;<span>於 --%>
					<%-- 													${msnReply.time }</span> --%>
					<!-- 												<div class="col-10"> -->
					<%-- 													<blockquote class="generic-blockquote">${msnReply.text}</blockquote> --%>
					<!-- 												</div> -->
					<%-- 											</c:forEach> --%>
					<!-- 										</div> -->
					<!-- 									</div> -->
					<%-- 								</c:if> --%>

					<%-- 							</c:forEach> --%>

					<%-- 						</c:when> --%>
					<%-- 						<c:otherwise> --%>
					<!--      					沒有留言給您 -->
					<%-- 						</c:otherwise> --%>
					<%-- 					</c:choose> --%>



				</div>
				<div class="tab-pane fade" id="v-pills-userCollection"
					role="tabpanel" aria-labelledby="v-pills-userCollection-tab">
					<h2>userCollection</h2>

				</div>
			</div>
		</div>
	</div>
	<div></div>

	<footer>
		<!-- Footer Start-->
		<div class="footer-area">
			<div class="container">
				<div class="footer-top footer-padding">
					<div class="row justify-content-between">
						<div class="col-xl-3 col-lg-4 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="single-footer-caption mb-30">
									<!-- logo -->
									<div class="footer-logo">
										<a href="index.html"><img
											src="<c:url value='/images/logo/peppernoodle.png'/>" alt=""></a>
									</div>
								</div>
							</div>
						</div>
						<div class="col-xl-2 col-lg-2 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Quick Link</h4>
									<ul>
										<li><a href="#">Home</a></li>
										<li><a href="#">Listing</a></li>
										<li><a href="#">About</a></li>
										<li><a href="#">Contact</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xl-2 col-lg-3 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Categories</h4>
									<ul>
										<li><a href="#">台北美食</a></li>
										<li><a href="#">熱門餐廳</a></li>
										<li><a href="#">點券優惠</a></li>
										<li><a href="#">每周排行</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xl-3 col-lg-3 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Download App</h4>
									<ul>
										<li class="app-log"><a href="#"><img
												src="<c:url value='/images/gallery/app-logo.png'/>" alt=""></a></li>
										<li><a href="#"><img
												src="<c:url value='/images/gallery/app-logo2.png'/>" alt=""></a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="footer-bottom">
					<div class="row d-flex justify-content-between align-items-center">
						<div class="col-xl-9 col-lg-8">
							<div class="footer-copy-right">
								<p>
									Copyright &copy;
									<script>
													document
														.write(new Date().getFullYear());
												</script>
									All rights reserved | U copy <i class="fa fa-heart"
										aria-hidden="true"></i> <a href="https://colorlib.com"
										target="_blank">U died</a>
								</p>
							</div>
						</div>
						<div class="col-xl-3 col-lg-4">
							<!-- Footer Social -->
							<div class="footer-social f-right">
								<a href="#"><i class="fab fa-facebook-f"></i></a> <a href="#"><i
									class="fab fa-instagram"></i></a>
							</div>
						</div>
					</div>
=======
				<!-- Scroll Up -->
				<div id="back-top">
					<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git
				</div>
			</div>
		</div>
		<!-- Footer End-->
	</footer>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>



	<script>
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
							alert(this.value);
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
			        		alert(result.length);
			        		commentsLength =result.length;
//				        		alert(result[0].replyMessageBoxes);
			        		console.log(JSON.stringify(result));
			        		console.log(JSON.stringify(result[0].time));
			        		 var aDay = new Date();
			        		 aDay.setHours(9);
			        		 aDay.setMinutes(0);
			        		 aDay.setSeconds(0);
			        		 var sun = result[0].time;
			        		
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
				        	alert(result);
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
									alert(result);
									showAllComments();
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
						});
						
						//新增回覆留言(usermain頁面的 useraccount 之後會抓預設的值)
						$('body').on('click','button[name^="addreply"]',function(e){
							
							var messageIdValue = $(this).next().text();
							var urls           ="/PepperNoodles/user/addNewReplyCommentAjax/" + messageIdValue;
							var text           =$(this).prev().val();
							alert(messageIdValue);
							alert(text);
							
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
//	 						+ "&useraccount=chrislo5311@gmail.com"
							$.ajax({
								type:"POST",
								url:  urls,
								contentType:'application/json;charset=UTF-8',
								dataType: "text",
								data:JSON.stringify(data),
								
								success: function (result) {
									alert(result);
									showAllComments();
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
							alert(id);
							urls += id;
							$.ajax({
								type:"GET",
								url: urls ,
								dataType: "text",
								success: function (result) {
									alert(result);
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
					

					
	
					
					
				</script>

<<<<<<< HEAD
	<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>

	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>

	<script src="<c:url value='/scripts/popper.min.js' />"></script>

	<!-- 	<script type="text/javascript" -->
	src="
	<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js' />
	">
	</script>
	<!-- Jquery Mobile Menu -->
	<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script>

	<!-- Jquery Slick , Owl-Carousel Plugins -->
	<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script>
=======
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git


<<<<<<< HEAD
	<script src="<c:url value='/scripts/slick.min.js' />"></script>
=======
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD
	<!-- One Page, Animated-HeadLin -->
	<script src="<c:url value='/scripts/wow.min.js' />"></script>
	<script src="<c:url value='/scripts/animated.headline.js' />"></script>
	<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script>
	<!-- Nice-select, sticky -->
	<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script>
	<script src="<c:url value='/scripts/jquery.sticky.js' />"></script>
	<!-- contact js -->
	<script src="<c:url value='/scripts/contact.js' />"></script>
=======
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD
	<script src="<c:url value='/scripts/jquery.form.js' />"></script>
	<script src="<c:url value='/scripts/jquery.validate.min.js' />"></script>
	<script src="<c:url value='/scripts/mail-script.js' />"></script>
	<script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script>
=======
	<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD
	<!-- Jquery Plugins, main Jquery -->
	<script src="<c:url value='/scripts/plugins.js' />"></script>
	<script src="<c:url value='/scripts/main.js' />"></script>
</body>
=======
	<script>
 		$(window).on('load', function() {
			
// 			//讓bar固定在上面以及設定高度
			$(".header-sticky").addClass("sticky-bar");
 			$(".header-sticky").css("height", "90px");
			$(".header-sticky").css("position","static")
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git

<<<<<<< HEAD
=======
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
>>>>>>> branch 'master' of https://github.com/PepperNoodles/PepperNoodles.git
</html>