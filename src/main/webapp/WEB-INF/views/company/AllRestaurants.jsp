<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<script type='text/javascript'
	src="<c:url value='/scripts/jquery-1.9.1.min.js' />"></script>
<meta charset="UTF-8">
<title>restaurantCRUD</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<!-- <!-- site.webmanifest run offline -->
<!-- <link rel="manifest" href="site.webmanifest"> -->

<!-- 	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>  -->
<!-- <script type="text/javascript" -->
<%-- <script src="<c:url value='/webjars/jquery/3.5.1/jquery.js'/>"></script> --%>
<!-- <link rel='stylesheet' -->
<%-- 	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" /> --%>
<!-- <script type="text/javascript" -->
<%-- 	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script> --%>

<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />
<link rel="stylesheet"
	href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/style.css' />">

<!-- bloodHound ↓-->

<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.11.1/typeahead.bundle.min.js"></script> -->
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.min.js"></script> -->
<!-- <link rel="stylesheet" -->
<!-- 	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css"> -->
<!-- <link rel="stylesheet" -->
<!-- 	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.css"> -->

<!-- bloodHound ↑-->
<style>
.nopadding {
	padding: 0 !important;
	margin: 0 !important;
}
</style>

</head>
<body>
	<%@include file="../includePage/includeNav.jsp"%>

	<div class="container-fluid">
		<div class="row">
			<!-- 左邊的Bar -->
			<div class="col-lg-2 nopadding" id=leftBar>
				<br>

				<div class="list-group">
					<%@include file="left.jsp"%>
				</div>
			</div>
			<!-- 右邊顯示的資料 背景圖片+自動填滿-->
			<div class="col-lg-10 nopadding image-container set-full-height" style="background-image: url(<c:url value="/images/restaurantCRUD/background_1.jpg"/>) ;background-size:cover">
				
					<div class="container">
						<div class="row d-flex align-items-center justify-content-center">
							<!-- 餐廳管理頁面 -->
							<div>
							<h3 style="color: red"><a href="<c:url value='/addrest'/> ">新增餐廳</a></h3>
							<c:choose>
								<c:when test="${empty restaurants}">
									<h5 style="color: #FF1493">沒有任何餐廳資料</h5>
									<br>
								</c:when>
								<c:otherwise>
									<table border='1' cellpadding="3" cellspacing="1">
										<tr>
											<th width='80'>餐廳名稱</th>
											<th width='120'>餐廳地址</th>
											<th width='80'>聯絡方式</th>
											<th width='80'>餐廳網站</th>
											<th width='80'>標籤</th>
											<th width='80'>環境照片</th>
											<th colspan='1' width='36'>資料維護</th>
										</tr>
										<c:forEach var='restaurant' items='${restaurants}'>
											<tr>
												<td style="text-align: center; font-weight: bold">${restaurant.restaurantName}</td>
												<td style="text-align: center; font-weight: bold">${restaurant.restaurantAddress}</td>
												<td style="text-align: center; font-weight: bold">${restaurant.restaurantContact}</td>
												<td style="text-align: center; font-weight: bold">${restaurant.restaurantWebsite}</td>
												<td style="text-align: center; font-weight: bold">
													<%-- 								<input id="${restaurant.restaurantId}" name="restid" class="typeahead"   disabled > --%>
													<div id="${restaurant.restaurantId}" name="restid"></div>
												</td>
												<td><img width='120' height='120'
													src='${pageContext.request.contextPath}/restpicture/${restaurant.restaurantId}'
													id='restpicture' /></td>
												<td style="font-weight: bold"><a class='updatelink'
													href="${pageContext.request.contextPath}/updateRest/${restaurant.restaurantId}">編輯</a><br>
													<a class='deletelink'
													href="${pageContext.request.contextPath}/deleteRest/${restaurant.restaurantId}">刪除</a></td>
											</tr>
										</c:forEach>
									</table>
								</c:otherwise>
							</c:choose>
							</div>
						</div>
					</div>
				
			</div>
		</div>
	</div>
	<form id='deleteform' method='POST'>
		<input type='hidden' name='_method' value='DELETE'>
	</form>
	<!-- footer -->
	<%@include file="../includePage/includeFooter.jsp"%>

	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>


	<!-- JS here -->

	<!-- Scroll Up -->
	<script>
		$(window).on('load', function() {

			// 			//讓bar固定在上面以及設定高度
			$(".header-sticky").addClass("sticky-bar");
			$(".header-sticky").css("height", "90px");
			$(".header-sticky").css("position", "static")

			//讓loading圖動起來
			$('#preloader-active').delay(450).fadeOut('slow');
			$('body').delay(450).css({
				'overflow' : 'visible'
			});

		});
	</script>
	<script>
	<!-- 刪除確認+tag抓取 -->
		$(document)
				.ready(
						function() {
							$('.deletelink').click(
									function() {
										if (confirm('確定刪除此筆紀錄? ')) {
											var href = $(this).attr('href');
											$("#deleteform").attr('action',
													href).submit();
										}
										return false;

									});

							//抓餐廳tag
							let n = $("div[name='restid']");
							//         console.log($("input[name='restid']"));
							//         console.log(n.length);
							//         console.log(n[0].id);

							for (let i = 0; i < n.length; i++) {
								var urls = "${pageContext.request.contextPath}/";
								urls += "<c:url value='restTag2/'/>" + n[i].id;
								// 		console.log(urls);

								$.ajax({
									type : "GET",
									url : urls,
									dataType : "text",
									success : function(response) {
										var divFoodTag = document
												.getElementById(n[i].id);

										var jsontxt = JSON.parse(response);
										// 				console.log(response);
										// 				console.log(jsontxt);
										// 				console.log(jsontxt.length);

										for (i = 0; i < jsontxt.length; i++) {
											$(divFoodTag).append(
													jsontxt[i].foodTagName
															+ '<br>');
											giveValue(jsontxt[i].foodTagIid,
													jsontxt[i].foodTagName);
										}
									},
									error : function(thrownError) {
										console.log(thrownError);
									}

								});
							}
							// bloodhound
							var foodTags = new Bloodhound(
									{
										datumTokenizer : Bloodhound.tokenizers.obj
												.whitespace('text'),
										queryTokenizer : Bloodhound.tokenizers.whitespace,
										prefetch : 'http://localhost:433/PepperNoodles/data/FoodTag.json',
										cache : false
									});

							foodTags.initialize();

							var elt = $('.typeahead');
							elt.tagsinput({
								itemValue : 'value',
								itemText : 'text',

								typeaheadjs : {
									limit : 20,
									name : 'foodTags',
									displayKey : 'text',
									source : foodTags.ttAdapter()
								}
							});

							function giveValue(id, name) {
								elt.tagsinput('add', {
									"value" : id,
									"text" : name
								});
							}

						})
	</script>

</body>
</html>