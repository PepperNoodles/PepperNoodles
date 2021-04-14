<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

			<!DOCTYPE html>
			<html>

			<head>
				<!--抓取原本路徑用-->
				<base localhref= />
		
				<meta charset="UTF-8">
				<title>userMain</title>
				<meta name="viewport" content="width=device-width, initial-scale=1">
				<!-- site.webmanifest run offline -->
				<link rel="manifest" href="site.webmanifest">
				<!-- favicon的圖-每頁都要加 -->
				<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
				<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
				<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />
				<script type="text/javascript"
					src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
				<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
				<link rel="stylesheet" href="<c:url value='/css/owl.carousel.min.css' />">
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
					a{
						color:black;
					}
					a:hover{
						color:blue;
					}
					tr:hover{
						background-color:#BEBEBE;
					}
				</style>
			</head>

			<body>
				<!-- 讀取圖案 -->
				<div id="preloader-active">
					<div class="preloader d-flex align-items-center justify-content-center">
						<div class="preloader-inner position-relative">
							<div class="preloader-circle" style="background-color: rgb(102, 102, 102);"></div>
							<div class="preloader-img pere-text">
								<img src="<c:url value=" /images/logo/peppernoodle.png" />" alt="">
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
												<a href="/PepperNoodles"><img style="height: 80px" src="<c:url value="/images/logo/peppernoodle.png" />" alt=""></a>
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
															</ul>
														</li>
														<li><a href="#">美食</a>
															<ul class="submenu">
																<li><a href="blog.html">美式</a></li>
																<li><a href="blog_details.html">日式燒烤</a></li>
																<li><a href="elements.html">韓式</a></li>
																<li><a href="listing_details.html">炸物</a></li>
															</ul>
														</li>
														<li><a href="#">排行榜</a>
															<ul class="submenu">
																<li><a href="blog.html">免費排行</a></li>
																<li><a href="blog_details.html">付費排行</a></li>
																<li><a href="elements.html">周排行</a></li>
																<li><a href="listing_details.html">綜合排行</a></li>
															</ul>
														</li>
														<li><a href="about.html">論壇</a></li>
														<li><a href="#">最新消息</a>
															<ul class="submenu">
																<li><a href="blog.html">菜色新聞</a></li>
																<li><a href="blog_details.html">最新優惠</a></li>
																<li><a href="elements.html">新開幕</a></li>
															</ul>
														</li>
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
							</div>
						</div>
					</div>
					<!-- Header End -->
				</header>


				<div class="container mt-10" style="width:50%">
					<!--有照片的那個bar  -->
					<div class="d-flex">
						<div class="p-2 bg-info">
							<img style="height: 100px"
								src="<c:url value='/userProtrait/${userAccount.userAccountDetail.useretailId}'/>">
						</div>

						<div class="p-2 flex-fill align-self-end justify-content-start">
							<h1>${userAccount.userAccountDetail.nickName}</h1>
						</div>


					</div>

					<div class="d-flex">
						<div class="p-2  flex-fill bg-secondary">
							<a href="#"><i class="fas fa-users"></i>好友</a>
						</div>
						<div class="p-2  flex-fill bg-secondary">
							<a href="#"><i class="fas fa-file-alt"></i>關於我</a>
						</div>
						<div class="p-2  flex-fill bg-secondary">
							<a href="#"><i class="fas fa-comments"></i>留言區</a>
						</div>
						<div class="p-2  flex-fill bg-secondary">
							<a href="#"><i class="fas fa-heart"></i>收藏區</a>
						</div>


					</div>





					<div class="mt-5" id="basicInfo">
						<h2>基本資料</h2>
						<p id="accountIndex">email: ${userAccount.accountIndex} </p>
						<p>性別：${userAccount.userAccountDetail.gender}</p>
						<p>地區：${userAccount.userAccountDetail.location}</p>
					</div>

					<div class="" id="friend">
						<h2>好友區</h2>
						<h3>搜尋好友</h3>
							<input class="form-control mr-sm-2" id="nameSearch" type="search" placeholder="Search" aria-label="Search">
							<button class="btn btn-primary my-2 my-sm-0 " id="btn-search" >Search</button>
						<!-- 展示結果-->
						<div id="searchResult">



						</div>

						<h3>搜尋好友</h3>
						<button id="checkRequestList" style="color:black">查看邀請</button>
						<div id="friendRequest">
							
						


						</div>

					</div>


				</div>




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
															src="<c:url value='/images/logo/peppernoodle.png'/>"
															alt=""></a>
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
																src="<c:url value='/images/gallery/app-logo.png'/>"
																alt=""></a></li>
													<li><a href="#"><img
																src="<c:url value='/images/gallery/app-logo2.png'/>"
																alt=""></a></li>
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
					$(window).on('load', function () {
						$(".header-sticky").addClass("sticky-bar");
						$(".header-sticky").css("height", "90px");
						//$(".header-sticky").css("position","static ")

						$('#preloader-active').delay(450).fadeOut('slow');
						$('body').delay(450).css({
							'overflow': 'visible'
						});


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
									showSearchList(response);
								},
								error: function (thrownError) {
									console.log(thrownError);
								}
							});
						})

					
						function showSearchList(response){
							$("#searchResult").html("");
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
							$("#searchResult").append(table);
							
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

				<!-- JS here -->
				<!-- All JS Custom Plugins Link Here here -->
				<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>

				<!-- Jquery, Popper, Bootstrap -->
				<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>

				<script src="<c:url value='/scripts/popper.min.js' />"></script>

				<!-- 	<script type="text/javascript" -->
				src="
				<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js' />"></script>
				<!-- Jquery Mobile Menu -->
				<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script>

				<!-- Jquery Slick , Owl-Carousel Plugins -->
				<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script>


				<script src="<c:url value='/scripts/slick.min.js' />"></script>

				<!-- One Page, Animated-HeadLin -->
				<script src="<c:url value='/scripts/wow.min.js' />"></script>
				<script src="<c:url value='/scripts/animated.headline.js' />"></script>
				<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script>
				<!-- Nice-select, sticky -->
				<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script>
				<script src="<c:url value='/scripts/jquery.sticky.js' />"></script>
				<!-- contact js -->
				<script src="<c:url value='/scripts/contact.js' />"></script>

				<script src="<c:url value='/scripts/jquery.form.js' />"></script>
				<script src="<c:url value='/scripts/jquery.validate.min.js' />"></script>
				<script src="<c:url value='/scripts/mail-script.js' />"></script>
				<script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script>

				<!-- Jquery Plugins, main Jquery -->
				<script src="<c:url value='/scripts/plugins.js' />"></script>
				<script src="<c:url value='/scripts/main.js' />"></script>
			</body>

			</html>