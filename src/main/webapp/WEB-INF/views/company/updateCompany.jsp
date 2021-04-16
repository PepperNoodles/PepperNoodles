<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改店家基本資料</title>
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

<script>
	$(function() {
		$("#wizard-picture").change(function() {
			if (this.files && this.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#wizardPicturePreview').attr('src', e.target.result);
				}

				reader.readAsDataURL(this.files[0]);
			}
		});
		
		var hasErrorComRealname = true;
		var hasErrorComPhone = true;
		var hasErrorComLocation = true;
		//前端判斷是否輸入正確
		$("#comRealname").blur(function(){
			let value=$(this).val();
		    let txt="";
		    if(value==""){
		    	$("#comRealnameResult").css({"color":"red","font-size":"small"});
		    	$("#comRealname").css({"border":"2px solid red"});
		    	txt="<span>企業名稱不可為空白</span>";
		    	hasErrorComRealname = false;
		    }
		    if(value.length<2){
		    	$("#comRealnameResult").css({"color":"red","font-size":"small"});
		    	$("#comRealname").css({"border":"2px solid red"});
		    	txt="<span>名稱需至少2個字</span>";
		    	hasErrorComRealname = false;
		    }
		    else{
		    	$("#comRealname").css("border","2px solid green");
		        txt="&emsp;";
		        hasErrorComRealname = true;
		    }
		    $("#comRealnameResult").html(txt);
		});
		
		$("#comPhonenumber").blur(function(){
			let value=$(this).val();
		    let txt="";
		    if(value==""){
		    	$("#comPhotoResult").css({"color":"red","font-size":"small"});
		    	$("#comPhonenumber").css({"border":"2px solid red"});
		    	txt="<span>請輸入連絡電話</span>";
		    	hasErrorComPhone = false;
		    }
		    else{
		    	for (let i = 0; i < value.length; i++) {
		            let ch = value.charAt(i);
		            if(ch>=0&&ch<=9){
		                txt="&emsp;";
		            $("#comPhonenumber").css("border","2px solid green");
		            hasErrorComPhone = true;
		            }
		            else{
		            	$("#comPhotoResult").css({"color":"red","font-size":"small"});
		    			$("#comPhonenumber").css({"border":"2px solid red"});
		                txt="<span>只能輸入數字</span>";
		                hasErrorComPhone = false;
		            }
		        }
		    }
		    $("#comPhotoResult").html(txt);
		});

		$("#comLocation").blur(function(){
			let value=$(this).val();
		    let txt="";
		    if(value==""){
		    	$("#comLocationResult").css({"color":"red","font-size":"small"});
		    	$("#comLocation").css({"border":"2px solid red"});
		    	txt="<span>地址不可為空白</span>";
		    	hasErrorComLocation = false;
		    }
		    else{
		    	$("#comLocation").css("border","2px solid green");
		        txt="&emsp;";
		        hasErrorComLocation = true;
		    }
		    $("#comLocationResult").html(txt);
		});
		
		//輸入完成傳值到Conrtoller
		$("#nextSlide").click(function(){
			if(!hasErrorComRealname || !hasErrorComPhone || !hasErrorComLocation){
				txt="<span>請輸入正確資訊</span>";
				$("#comStatus").css({"color":"red","font-size":"small"});
			}
			else{
				txt="&emsp;";
				document.form1.submit();
			}
				$("#comStatus").html(txt);
		});

	});
</script>
</head>
<body>
	<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/login/noodles.jpg"/>)">
		<div class="logo-container">
			<div class="logo">
				<a href="/PepperNoodles">
				<img src="<c:url value="/images/logo/peppernoodle.png"/>" width="100px"	height="100px" style="margin-left: 93%;">
				</a>
			</div>
		</div>
		<!-- container   -->
		<div class="container">
			<div class="row">
				<div class="col-sm-8 col-sm-offset-2">
					<!--      Wizard container        -->
					<div class="wizard-container">
						<div class="card wizard-card" data-color="blue"
							id="wizardProfile">
							<div class="wizard-header">
								<h3>
									<b>修改 店家資料</b><br>
								</h3>
							</div>
							<br>
							<form:form method="POST" modelAttribute="comDetail"	enctype='multipart/form-data' name="form1">
								<div class="">
									<!-- 整大包的div -->
									<div class="" id="accoutPage1">
										<div>
											<c:if test='${comDetail.companyDetailId == null}'>
												<br>帳號：<br>&nbsp;
											<form:input path='realname' />
												<br>&nbsp;
											<form:errors path="realname" cssClass="error" />
											</c:if>
										</div>
										<div class="row">
											<c:if test='${comDetail.companyDetailId != null}'>
												<div class="col-sm-4 col-sm-offset-1"></div>
												<div class="col-sm-6">
													<div class="form-group">
														<label><h5>會員帳號：${comDetail.userAccount.accountIndex}</h5></label>
													</div>
												</div>
											</c:if>
												<div class="col-sm-4 col-sm-offset-1">
													<div class="picture-container">
														<div class="picture">
															<img src="<c:url value="/getComPicture/${comDetail.companyDetailId}"/>" class="picture-src" id="wizardPicturePreview" />
															<form:input type="file" id="wizard-picture" accept="image/*" path="userphoto" />
															<form:errors path="userphoto" cssClass="error" />
														</div>
														<h6>更換圖片</h6>
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<label>企業名稱:<small>(1.不可空白，2.至少兩個字以上)</small></label>
														<form:input class="form-control" type="text" path="realname" id="comRealname" />
														<span id="comRealnameResult">&emsp;</span>
														<form:errors path='realname' cssClass="error" />
													</div>
													<div class="form-group">
														<label>連絡電話: <small>(請輸入數字。例:09xxxxxxxx)</small></label>
														<form:input class="form-control" type="text" path="phonenumber" id="comPhonenumber" />
														<span id="comPhotoResult">&emsp;</span>
														<form:errors path='phonenumber' cssClass="error" />
													</div>
												</div>
												<div class="col-sm-1 col-sm-offset-1"></div>
												<div class="col-sm-10 col-sm-offset-1">
													<div class="form-group">
														<label>地址:<small>(請輸入公司地址)</small></label>
														<form:input class="form-control" type="text" path="location" id="comLocation" />
														<span id="comLocationResult">&emsp;</span>
														<form:errors path='location' cssClass="error" />
														<br>	<br>
														<div class="pull-right">
															<input type="button" class='btn btn-next btn-fill btn-warning btn-wd btn-sm' 
																  value='確認修改' id="nextSlide" style="margin-bottom: 20px; margin-top: 10px" />
														</div>
														<div class="pull-right" style="margin-right: 20%;" id="comStatus">
															<div style="width: 150px;height: 30px;"></div>
														</div> 
														<div class="pull-left">
															<a class='btn btn-previous btn-fill btn-default btn-wd btn-sm'
												  			   style="margin-bottom: 20px; margin-top: 10px"
												 			   href="<c:url value='/' />showCompany/${comDetail.companyDetailId}">回基本資料</a>
														</div>
													</div>
												</div>
										</div>
									</div>
								</div>
							</form:form>
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
</body>
<!--   Core JS Files   -->
<script src="<c:url value='/scripts/jquery.bootstrap.wizard.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value="/scripts/bootstrap2.min.js"/>"></script>
<!--  Plugin for the Wizard -->
<script src="<c:url value='/scripts/gsdk-bootstrap-wizard.js'/>"></script>

</html>