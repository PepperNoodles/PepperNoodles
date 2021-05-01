<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Updaterestaurant</title>
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">

<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />

<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>


<style>
.nopadding {
	padding: 0 !important;
	margin: 0 !important;
}

.rest-infobox {
	float: left;
	width: 50%;
	margin: auto;
	padding: 15px;
}

.rest-picbox {
	float: right;
	margin: 10px;
	width: 400px;
	border: 3px solid red;
	border: 3px solid red;
	height: auto;
}

#restaurantPicturePreview {
	width: 100%;
	height: auto;
}

#tagdiv {
	clear: both;
	/* 	清除上面float影響  */
}
</style>
<style>
/* 3.35 twitter-bootstrap */
label {
	display: inline-block;
	max-width: 100%;
	margin-bottom: 5px;
	font-weight: bold;
}

.label {
	display: inline;
	padding: .2em .6em .3em;
	font-size: 75%;
	font-weight: bold;
	line-height: 1;
	color: #fff;
	text-align: center;
	white-space: nowrap;
	vertical-align: baseline;
	border-radius: .25em;
}

.label:empty {
	display: none;
}

.label-info {
	background-color: #5bc0de;
}

.label-info[href]:hover, .label-info[href]:focus {
	background-color: #31b0d5;
}
/* ↑3.35 twitter-bootstrap */
</style>
<style>
/* ↓tag */
@font-face {
	font-family: "Prociono";
	src: url("../font/Prociono-Regular-webfont.ttf");
}

.container {
	margin: 0 auto;
	max-width: 750px;
	text-align: center;
}

.tt-dropdown-menu, .gist {
	text-align: left;
}

html {
	color: #333333;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 18px;
	line-height: 1.2;
}

#scrollable-dropdown-menu .tt-dropdown-menu {
	max-height: 150px;
	overflow-y: auto;
}

.title, .example-name {
	font-family: Prociono;
}

p {
	margin: 0 0 10px;
}

.title {
	font-size: 64px;
	margin: 20px 0 0;
}

.example {
	padding: 30px 0;
}

.example-name {
	font-size: 32px;
	margin: 20px 0;
}

.demo {
	margin: 50px 0;
	position: relative;
}

.typeahead, .tt-query, .tt-hint {
	border: 2px solid #CCCCCC;
	border-radius: 8px 8px 8px 8px;
	font-size: 24px;
	height: 30px;
	line-height: 30px;
	outline: medium none;
	padding: 8px 12px;
	width: 396px;
}

.typeahead {
	background-color: #FFFFFF;
}

.typeahead:focus {
	border: 2px solid #0097CF;
}

.tt-query {
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
}

.tt-hint {
	color: #999999;
}

.tt-dropdown-menu {
	background-color: #FFFFFF;
	border: 1px solid rgba(0, 0, 0, 0.2);
	border-radius: 8px 8px 8px 8px;
	box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
	margin-top: 12px;
	padding: 8px 0;
	width: 422px;
}

.tt-suggestion {
	font-size: 18px;
	line-height: 24px;
	padding: 3px 20px;
}

.tt-suggestion.tt-cursor {
	background-color: #0097CF;
	color: #FFFFFF;
}

.tt-suggestion p {
	margin: 0;
}

.gist {
	font-size: 14px;
}

.example-twitter-oss .tt-suggestion {
	padding: 8px 20px;
}

.example-twitter-oss .tt-suggestion+.tt-suggestion {
	border-top: 1px solid #CCCCCC;
}

.example-twitter-oss .repo-language {
	float: right;
	font-style: italic;
}

.example-twitter-oss .repo-name {
	font-weight: bold;
}

.example-twitter-oss .repo-description {
	font-size: 14px;
}

.example-sports .league-name {
	border-bottom: 1px solid #CCCCCC;
	margin: 0 20px 5px;
	padding: 3px 0;
}

.example-arabic .tt-dropdown-menu {
	text-align: right;
}
/* ↑tag */
</style>
<!--餐廳地址Ajax驗證不可重複 -->
<script>
	$(function() {

		var checkboolean = "true";

		$("#checkBeforeSubmit").click(function() {
			var type = $(this).attr('type');
			if (checkboolean == "true") {
				$("#submitError").html("OK!");
				$("#updatefrom").submit();
			} else {

				$("#submitError").html("失敗!");
				return false;
			}

		});

	});
</script>
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
			<!-- 右邊顯示的資料 背景圖片+自動填滿 -->
			<div class="col-lg-10 nopadding image-container set-full-height"
				style="background-image: url(<c:url value="/images/restaurantCRUD/background_1.jpg"/>) ;background-size:cover">

				<div class=" align-items-center justify-content-center">
					<form:form method="POST" modelAttribute="updateRestaurant"
						enctype='multipart/form-data' id="updatefrom">
						<!-- 餐廳管理頁面 -->
						<div class="rest-infobox ">
							<h1 style="color: red">更新餐廳</h1>
							<h5 style="color: #FF1493">餐廳名稱：</h5>
							<form:input path='restaurantName' />
							<form:errors path="restaurantName" cssClass="error" />
							<br>
							<h5 style="color: #FF1493">餐廳地址：</h5>
							<form:input path='restaurantAddress' id="RAdd" disabled="true" />
							<span id="RAddError"></span>
							<form:errors path="restaurantAddress" cssClass="error" />
							<br> <span style="color: #FF1493">經度<form:input
									path='longitude' id="RLong" size="10" disabled="true" />
							</span><br> <span style="color: #FF1493">緯度<form:input
									path='latitude' id="RLati" size="10" disabled="true" /></span>
							<h5 style="color: #FF1493">聯絡方式：</h5>
							<form:input path='restaurantContact' />
							<form:errors path="restaurantContact" cssClass="error" />
							<br>
							<h5 style="color: #FF1493">餐廳網站：</h5>
							<form:input path='restaurantWebsite' />
							<form:errors path="restaurantWebsite" cssClass="error" />
							<div id="tagdiv">
								<h5 style="color: #FF1493">標籤：</h5>
								<form:input class="typeahead" type="text" placeholder="add Tag"
									path="foodTag" />
								<form:errors path="foodTag" cssClass="error" />
								
								<form:input path='productImage' type='file'
									id="restaurant-picture" accept="image/*" />
								<form:errors path="productImage" cssClass="error" />

								<div id="submitbox">
								<br> <input type='button' value="提交" id="checkBeforeSubmit" >
								<span id="submitError"></span>
								</div>
							</div>
						</div>
						<div class="rest-picbox">
							<!--秀圖區 -->
							<div>

								<img
									src=<c:url value='/restpicture/${updateRestaurant.restaurantId}'/>
									class="picture-src" id="restaurantPicturePreview" />
							</div>

						</div>
					</form:form>

				</div>

				<div style="display: none" id="${updateRestaurant.restaurantId}"
					name="restid"></div>
			</div>
		</div>
	</div>
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
	<!-- 抓餐廳tag -->
	<script>
		$(document)
				.ready(
						function() {
							let n = $("div[name='restid']");
							// 							console.log($("div[name='restid']"));
							// 							console.log(n.length);
							var jsontxt;
							for (let i = 0; i < n.length; i++) {
								var urls = "${pageContext.request.contextPath}/";
								urls += "<c:url value='restTag2/'/>" + n[i].id;
								// 								console.log(urls);
								$.ajax({
									type : "GET",
									url : urls,
									dataType : "text",
									success : function(response) {
										var divFoodTag = document
												.getElementById(n[i].id);

										jsontxt = JSON.parse(response);
										// 				console.log(response);
										// 				console.log("====="+jsontxt[0].foodTagName);
										// 				console.log(jsontxt.length);

										for (i = 0; i < jsontxt.length; i++) {
											$(divFoodTag).append(
													jsontxt[i].foodTagName
															+ '&nbsp;');
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
	<!--預覽照片-->
	<script>
		$(function() {
			$("#restaurant-picture").change(
					function() {
						if (this.files && this.files[0]) {
							var reader = new FileReader();

							reader.onload = function(e) {
								$('#restaurantPicturePreview').attr('src',
										e.target.result);

							}
							reader.readAsDataURL(this.files[0]);
						}

					});
		});
	</script>



</body>
</html>