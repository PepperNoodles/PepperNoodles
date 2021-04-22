<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店家基本資料</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" />
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />

</head>
<body>
	<div class="image-container set-full-height"
		style="background-image: url(<c:url value="/images/login/noodles.jpg"/>)">
<!-- 		<div class="logo-container"> -->
<!-- 			<div class="logo"> -->
<!-- 				<a href="/PepperNoodles"> <img -->
<%-- 					src="<c:url value="/images/logo/peppernoodle.png"/>" width="100px" --%>
<!-- 					height="100px" style="margin-left: 93%;"> -->
<!-- 				</a> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<!-- container   -->
		<div class="container">
			<div class="row">
				<div class="col-sm-8 col-sm-offset-2">
					<!--      Wizard container        -->
					<div class="wizard-container">
						<div class="card wizard-card" data-color="blue" id="wizardProfile">
							<div class="wizard-header">
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
										<div class="row">
											<c:if test='${comDetail.companyDetailId != null}'>
											
											<div class="col-sm-4 col-sm-offset-1">
												<br>
												<img src="<c:url value="/getComPicture/${comDetail.companyDetailId}"/>" class="picture-src"  />
											</div>
											<div class="col-sm-6">
												<div class="form-group">
													<label><h5>會員帳號 :&nbsp;${comDetail.userAccount.accountIndex}</h5></label>
													&nbsp;&nbsp; 
													<a href="<c:url value='/' />updateComPwd/${comDetail.companyDetailId}">修改密碼</a>
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
				</div>
			</div>
			<!-- end row -->
		</div>
		<!--  big container -->
		<div class="footer">
			<div class="container">
				<div class="footer-copy-right">
					<p>
						Copyright
						<script>
							document.write(new Date().getFullYear());
						</script>
						All rights reserved | U copy <i class="fa fa-heart"
							aria-hidden="true"></i>U died
					</p>
				</div>
			</div>
		</div>

	</div>
	<script>
		$(function() {
			$("#wizard-picture").change(
					function() {
						if (this.files && this.files[0]) {
							var reader = new FileReader();

							reader.onload = function(e) {
								$('#wizardPicturePreview').attr('src',
										e.target.result);
							}

							reader.readAsDataURL(this.files[0]);
						}
					});
		});
	</script>

</body>
<!--   Core JS Files   -->
<script src="<c:url value='/scripts/jquery.bootstrap.wizard.js'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<c:url value="/scripts/bootstrap2.min.js"/>"></script>
<!--  Plugin for the Wizard -->
<script src="<c:url value='/scripts/gsdk-bootstrap-wizard.js'/>"></script>

</html>