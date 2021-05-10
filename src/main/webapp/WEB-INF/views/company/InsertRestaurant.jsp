<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>建立餐廳</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
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
	margin-left: 30px;
	float: left;
	width: 50%;
	margin: auto;
	padding: 15px;
}

.rest-picbox {
	float: left;
	margin: 10px;
	width: 450px;
	border: 1px solid pink;
	height: 450px;
	border-radius: 15px;
}

#restaurantPicturePreview {
      margin-left: 20px;
            margin-top: 20px;

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


<!-- 驗證地址不重複 -->
<script>
<!--餐廳地址Ajax驗證不可重複 -->
	$(function() {

		var checkboolean = "false";
		// 		blur驗證地址是否重複
		$("#RAdd").blur(
				function() {

					let value = $(this).val();
					let text = "";
					if (value == "") {
						$("#RAdd").css({
							"border" : "2px solid orange"
						});
						txt = "<span>地址不可為空白</span>";
						$("#RAddError").html(txt);
						checkboolean = false;
					} else {

						var xhr = new XMLHttpRequest();
						xhr.open("POST", "<c:url value='/checkRAddress' />",
								true);
						xhr.setRequestHeader("Content-type",
								"application/x-www-form-urlencoded");
						xhr.send("value=" + value);

						xhr.onreadystatechange = function() {
							// 伺服器請求完成
							if (xhr.readyState == 4 && xhr.status == 200) {
								var resultmap = JSON.parse(xhr.responseText);
								var resultmsg = resultmap.result;
								checkboolean = resultmap.checkboolean;

								if (checkboolean == "false") {

									$("#RAdd").css({
										"border" : "2px solid red"
									});
									txt = "此地址已經被註冊!";
									$("#RAddError").html(txt);
								} else {
									$("#RAdd").css({
										"border" : "2px solid green"
									});
									txt = "OK!";
									$("#RAddError").html(txt);

								}

							}
						}
					}
				});

		$("#checkBeforeSubmit").click(function() {
			var type = $(this).attr('type');
			if (checkboolean == "true") {
				$("#submitError").html("地址驗證成功!");
				$("#insertform").submit();
			} else {

				$("#submitError").html("地址驗證失敗!");
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
				style="background-image: url(<c:url value="/images/restaurantCRUD/background_2.jpg"/>) ;background-size:cover">

				<div class=" align-items-center justify-content-center">
					<form:form id="insertform" method="POST"
						modelAttribute="restaurant" enctype='multipart/form-data'>
						<!-- 餐廳管理頁面 -->
						<div class="rest-infobox ">


							<h1 style="color: red"><i class="fas fa-registered"></i> 建立餐廳</h1>
							<h5 style="color: #FF1493">餐廳名稱：</h5>
							<form:input path='restaurantName' id='RName' />
							<form:errors path="restaurantName" cssClass="error" />
							<br><br>
							<h5 style="color: #FF1493">餐廳地址：</h5>
							<form:input path='restaurantAddress' id="RAdd" />
							<span id="RAddError"></span><br>
							<form:errors path="restaurantAddress" cssClass="error" />
							<br> <div style="color: #FF1493">經度<form:input
									path='longitude' id="RLong" size="10" />
							</div><br> <div style="color: #FF1493">緯度<form:input
									path='latitude' id="RLati" size="10" /></div><br>


							<h5 style="color: #FF1493">聯絡方式：</h5>
							<form:input path='restaurantContact' />
							<form:errors path="restaurantContact" cssClass="error" />
							<br><br>
							<h5 style="color: #FF1493">餐廳網站：</h5>
							<form:input path='restaurantWebsite' placeholder="請輸入網址" size="35"/>
							<form:errors path="restaurantWebsite" cssClass="error" />
							<br><br>
							<div id="tagdiv">
								<h5 style="color: #FF1493">標籤：</h5>
								<form:input class="typeahead" type="text" placeholder="Tags e.g., 炸雞"
									path="foodTag" />
								<form:errors path="foodTag" cssClass="error" />
								
								<form:input path='productImage' type='file'
									id="restaurant-picture" accept="image/*" hidden="true" />
								<form:errors path="productImage" cssClass="error" />
								
								<br> 
	
								<div id="submitbox">
								<br>
								<input type='button' value="提交" id="checkBeforeSubmit" >
								<span id="submitError"></span>
								
								</div>
							</div>
								

						</div>
						<div class="rest-picbox">
							<!--秀圖區 -->
							
								<label for="restaurant-picture" style="cursor: pointer">
								<img width='400' height='400'
									src="<c:url value="/images/NoImage/restaurantdefault.png"/>"
									class="picture-src" id="restaurantPicturePreview" />
								</label>

							
						</div>
					</form:form>



				</div>
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





	<!-- JS here -->
	<!--預覽照片+Bloodhound -->
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

		});
	</script>

	<!--地址轉座標 -->
	<script>
		var map;

		function initMap() {
			map = new google.maps.Map(document.getElementById('map'), {
				center : {
					lat : 25.033710,
					lng : 121.564718
				},
				zoom : 15
			});
			var marker = new google.maps.Marker({
				position : {
					lat : 25.033710,
					lng : 121.564718
				},
				map : map
			});
		}

		function startTrans() {
			let addr = document.getElementById("RAdd").value;
			// 			console.log(addr);
			addressToLocation(addr);
			// 			console.log(document.getElementById("lat").value);      

		}

		function addressToLocation(addr) {
			let longitude;
			let latitude;
			let loca;
			var geocoder = new google.maps.Geocoder();
			geocoder.geocode({
				'address' : addr
			}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					longitude = results[0].geometry.location.lng();

					latitude = results[0].geometry.location.lat();

					let lat = document.getElementById("RLati");
					lat.value = latitude;
					let lng = document.getElementById("RLong");
					lng.value = longitude;
				} else {
				}
			})

		}

		document.getElementById("RAdd").addEventListener("change", startTrans);
	</script>

	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBTUCen4YixtEKjNBAL4CX5xkW1QQAembQ&callback=initMap"
		async defer>
		
	</script>
</body>
</html>