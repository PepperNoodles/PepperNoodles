<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>最新活動</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />
<script>
	
$(function(){
	$("#add").click(function(){
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "<c:url value='/CheckMemberAccount' />", true);
	});
});
	
function()
</script>

<style>
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
#picture{
 	border-style:dashed;
/*  	cursor:hand; */
/* 	object-fit: */
}
/* div{ */
/* 	text-align: center; */
/* } */
.top{
	margin-top:30px; 
}
.toshow{
	display: block;
}
.tohide{
	display: none;
}
.infobox {
	float: left;
/* 	width: 10%; */
 	margin: 30px 0% 0% 20%; 
}
.picbox {
	float: right;
	margin: 0% 40% 0% 0%; 
/* 	margin: auto; */
/* 	width: 50%; */
/* 	padding: 0px 10px 10px 10px; */
/* 	height: 50%; */
}
.top{
height: 500px;
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

	<div class="container-fluid">
    <div class="row">
    <!-- 左邊的Bar -->
      <div class="col-lg-2 nopadding" id=leftBar>
      	<br>
        <h3 class="container">${comDetail.realname}</h3>
        <div class="list-group">
        	<%@include file="left.jsp" %>
        </div>
      </div>
   	  <!-- 右邊顯示的資料 -->  
      <div class="col-lg-10 nopadding " >
		<!-- 上傳圖案的部分 -->
      	<div class="top">
			<div class="infobox">
				<label for="wizardPicturePreview">
					<img src="<c:url value="/images/company/++.png"/>" width="100px" id="picture"/>
				</label>
				<input hidden type="file" id="wizardPicturePreview" accept="image/*" name="photo">
			</div>
				
			<div class="picbox">
				<img class="picture-src" id="PicturePreview" width="300px" /><br>
				<div>
					<input class='picbox tohide' type='button'  value='新增菜單' id="add" style="margin-bottom: 20px;margin-top: 10px"/>
				</div>						
			</div>
      	</div>
      	<!-- 顯示資料庫的菜單 -->
      	<hr>
      	
      	
	  <!-- 右邊顯示的資料結束 -->  
      </div>
    </div>
    <!-- /.row -->
</div>

	<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

<script>
	$(window).on('load', function() {
// 		//讓bar固定在上面以及設定高度
		$(".header-sticky").addClass("sticky-bar");
		$(".header-sticky").css("height", "90px");
		$(".header-sticky").css("position","static")
		//讓loading圖動起來
		$('#preloader-active').delay(450).fadeOut('slow');
		$('body').delay(450).css({'overflow' : 'visible'});			
			
	});
</script>
<!--預覽照片 -->
<script>
$(function() {
	$("#wizardPicturePreview").change(function() {
		if (this.files && this.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#PicturePreview').attr('src',e.target.result);
			}
			reader.readAsDataURL(this.files[0]);
			$("#add").removeClass("tohide");
			$("#add").addClass("toshow");
		}
	});
});
</script>

</body>
</html>