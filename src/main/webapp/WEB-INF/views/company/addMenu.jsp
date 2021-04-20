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
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />

<style>
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
.picture{
	border-style:dashed;
/* 	object-fit: */
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
      	<div class="image-container set-full-height">
      
      		<h1>新增菜單</h1>
      		
      		<div class="col-sm-4 col-sm-offset-1">
				<div class="picture-container">
					<div>
					<label for="wizardPicturePreview">
						<img src="<c:url value="/images/company/+.png"/>"
							 width="100px" class="picture" />
					</label>
						<input hidden type="file" id="wizardPicturePreview" accept="image/*" name="photo">
					</div>
				</div>
			</div>
			
      
      	
      	</div>
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



</body>
</html>