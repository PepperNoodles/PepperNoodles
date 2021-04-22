<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登入</title>

<%-- <link rel="stylesheet" href="<c:url value='/css/style.css' />"> --%>
<!-- Bootstrap core CSS -->
<%-- <link href="<c:url value='/vendor/bootstrap/css/bootstrap.min.css' />" rel="stylesheet"> --%>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet">
<!-- <script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script> -->
<!-- CSS Files -->
<%-- <script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script> --%>
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />



<style>
.header {
	background-color: green;
}
#page2{
	background-color: blue;
	padding:0;
	margin: 0;
}
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
.updatePwd {
	color: 	#00AEAE;
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
      	<!-- 背景圖 -->
      	<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/company/conpany.jpg"/>)">
      		<div class="container">
			<div class="row d-flex align-items-center justify-content-center">
				<div class="col-sm-8 col-sm-offset-2">
					<!--      Wizard container        -->
					<div class="wizard-container">
						<div class="card wizard-card " data-color="blue" id="wizardProfile">
							<div class="wizard-header ">
								<h3>
									<b>店家資料</b><br>
								</h3>
							</div>
							<br>
							<form method="GET" name="form1" enctype='multipart/form-data'>
								<div class="">
									<!-- 整大包的div -->
									<div class="" id="accoutPage1">
										<div>
											<c:if test='${comDetail.companyDetailId == null}'>
												<br>帳號：<br>&nbsp;
												<input path='realname' />
											</c:if>
										</div>
										<div class="row ml-5">
											<c:if test='${comDetail.companyDetailId != null}'>
											
											<div class="col-sm-4 col-sm-offset-1">
												<br>
												<img src="<c:url value="/getComPicture/${comDetail.companyDetailId}"/>" width="100%"  />
											</div>
											<div class="col-sm-7">
												<div class="form-group">
													<label><h5>會員帳號 :&nbsp;${comDetail.userAccount.accountIndex}</h5></label>
<!-- 													 <br> -->
<%-- 													<a class="updatePwd" href="<c:url value='/' />updateComPwd/${comDetail.companyDetailId}">修改密碼</a> --%>
												</div>
												
												<div class="form-group">
													<label><h5>企業名稱 :&nbsp;${comDetail.realname}</h5></label>
												</div>
												<div class="form-group">
													<label><h5>連絡電話 :&nbsp;${comDetail.phonenumber}</h5></label>
												</div>
												
												<div class="form-group">
													<label><h5>地址 :&nbsp;${comDetail.location}</h5></label>
												</div>
												
												<div class="pull-right">
													<a class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
													   style="margin-bottom: 20px; margin-top: 10px"
													   href="<c:url value='/' />updateCom/${comDetail.companyDetailId}">修改資料</a>
												</div>
												
											</div>
											</c:if>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- wizard container -->
      	<!-- 背景圖結束 -->	
      	</div>
      </div>
    </div>
    <!-- /.row -->
</div>
</div>
</div>


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