<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Company</title>
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
	var hasErrorPwd = false;
	var hasErrorNextPwd = false;
	var hasErrorCheckNextPwd = false;
	
	$("#userPwd").blur(function(){
		let value=$(this).val();
	    let txt="";
	    if(value==""){
	    	$("#userPwdResult").css({"color":"red","font-size":"small"});
	    	$("#userPwd").css({"border":"2px solid red"});
	    	txt="<span>密碼不可為空白</span>";
	    	hasErrorComRealname = false;
	    }
	    else if(!isPWD(value)){
	    	$("#userPwdResult").css({"color":"red","font-size":"small"});
	    	$("#userPwd").css({"border":"2px solid red"});
	    	txt="<span>密碼格式錯誤</span>";
	    	hasErrorComRealname = false;
	    }
	    else{
	    	$("#userPwd").css("border","2px solid green");
	        txt="&emsp;";
	        hasErrorPwd = true;
	    }
	    $("#userPwdResult").html(txt);
	});
	
	$("#nextUserPwd").blur(function(){
		nextPwdValue=$(this).val();
	    let txt="";
	    if(nextPwdValue==""){
	    	$("#nextUserPwdResult").css({"color":"red","font-size":"small"});
	    	$("#nextUserPwd").css({"border":"2px solid red"});
	    	txt="<span>密碼不可為空白</span>";
	    	hasErrorComRealname = false;
	    }
	    else if(nextPwdValue.length<6){
	    	$("#nextUserPwdResult").css({"color":"red","font-size":"small"});
	    	$("#nextUserPwd").css({"border":"2px solid red"});
	    	txt="<span>密碼至少輸入6碼</span>";
	    	hasErrorComRealname = false;
	    }
	    else if(!isPWD(nextPwdValue)){
	    	$("#nextUserPwdResult").css({"color":"red","font-size":"small"});
	    	$("#nextUserPwd").css({"border":"2px solid red"});
	    	txt="<span>密碼格式錯誤</span>";
	    	hasErrorComRealname = false;
	    }
	    else{
	    	$("#nextUserPwd").css("border","2px solid green");
	        txt="&emsp;";
	        hasErrorNextPwd = true;
	    }
	    $("#nextUserPwdResult").html(txt);
	});
	
	$("#checkNextUserPwd").blur(function(){
		let value=$(this).val();
	    let txt="";
	    if(value == ""){
	    	$("#checkNextUserPwdResult").css({"color":"red","font-size":"small"});
	    	$("#checkNextUserPwd").css({"border":"2px solid red"});
	    	txt="<span>請輸入確認密碼</span>";
	    	hasErrorCheckNextPwd = false;
	    }
	    else if(value != nextPwdValue){
	    	$("#checkNextUserPwdResult").css({"color":"red","font-size":"small"});
	    	$("#checkNextUserPwd").css({"border":"2px solid red"});
	    	txt="<span>確認密碼需與密碼相同</span>";
	    	hasErrorCheckNextPwd = false;
	    }
	    else{
	    	$("#checkNextUserPwd").css("border","2px solid green");
	        txt="&emsp;";
	        hasErrorCheckNextPwd = true;
	    }
	    $("#checkNextUserPwdResult").html(txt);
	});
	
	//輸入完成傳值到Conrtoller
	$("#nextSlide").click(function(){
		txt="";
		if(!hasErrorPwd || !hasErrorNextPwd || !hasErrorCheckNextPwd){
			txt="<span>請輸入正確資訊</span>";
			$("#checkComStatus").css({"color":"red","font-size":"small"});
			$("#checkComStatus").html(txt);
		}
		else{
			txt="";
			$("#checkComStatus").html(txt);
			var id=${comDetail.companyDetailId};
			var userPwd = $("#userPwd").val();
			var nextUserPwd = $("#nextUserPwd").val();
// 			alert("id:"+id);
// 			alert("userPwd:"+userPwd);
// 			alert("nextUserPwd:"+nextUserPwd);
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "<c:url value='/updatePwd/' />"+id, true);
// 			var jsonAccount = {
// 								"userPwd": userPwd, 					
// 								"nextUserPwd": nextUserPwd, 	
// 			};
//  			alert("jsonMember:"+jsonMember.userPwd+":"+jsonMember.nextUserPwd);
// 			xhr.setRequestHeader("Content-Type", "application/json");
// 			xhr.send(JSON.stringify(jsonAccount));
			xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			xhr.send("userPwd=" + userPwd + "&nextUserPwd=" + nextUserPwd);
			
			xhr.onreadystatechange = function() {
				result = JSON.parse(xhr.responseText);
				if(result.success){
					txt="修改密碼成功!";
					$("#checkComStatus").css({"color":"red","font-size":"small"});
					$("#checkComStatus").html(txt);
				}
				else{
					txt="密碼錯誤，修改失敗!";
					$("#checkComStatus").css({"color":"red","font-size":"small"});
					$("#checkComStatus").html(txt);
				}
				var seconds = 3;
				$("#dvCountDown").show();
				$("#lblCount").html(seconds);
	  		    setInterval(function () {
					seconds--;
					$("#lblCount").html(seconds);
				    if (seconds == 0) {
				   	$("#dvCountDown").hide();
				       window.location = "<c:url value='/showCompany/${comDetail.companyDetailId}' />";
				    }
				}, 1000);	
			}			
		}
	});
	
	
});

function isPWD(pwd) {
	return /^(?=.*[A-Za-z])(?=.*\d)(?=.*[#^@$!%*?&])[A-Za-z\d#^@$!%*?&]{6,}$/
			.test(pwd);
}

</script>
<style>
.center{
	text-align: center;
	color: red;
}
</style>
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
						<div class="card wizard-card" data-color="blue" id="wizardProfile">
							<div class="wizard-header">
								<h3>
									<b>修改密碼</b><br>
								</h3>
							<div class="center" id="dvCountDown" style="display: none">
								&nbsp;&nbsp;你將在<span id='lblCount'></span>秒後跳轉。
							</div>								
							</div>
							<br>
								<div class="">
									<!-- 整大包的div -->
									<div class="" id="accoutPage1">
										<div>
											<c:if test='${comDetail.companyDetailId == null}'>
												<br>帳號：<br>&nbsp;
												<br>&nbsp;
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
												<div class="col-sm-4 col-sm-offset-1">
														<br><br>
														<img src="<c:url value="/getComPicture/${comDetail.companyDetailId}"/>" class="picture-src"  />
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<label>舊密碼:</label>
														<input class="form-control" type="password" name="userPwd" id="userPwd" />
														<span id="userPwdResult">&nbsp;</span>
													</div>
													
													<div class="form-group">
														<label>新密碼:</label>
														<input class="form-control" type="password" name="nextUserPwd" id="nextUserPwd" />
														<span id="nextUserPwdResult">&nbsp;</span>
													</div>
													
													<div class="form-group">
														<label>確認密碼:</label>
															<input class="form-control" type="password" name="checkNextUserPwd" id="checkNextUserPwd" />
														<span id="checkNextUserPwdResult">&nbsp;</span>
													</div>
												</div>
												<div class="col-sm-1 col-sm-offset-1"></div>
												<div class="col-sm-10 col-sm-offset-1">
													<div class="form-group">
															
														<div class="pull-right">
															<input type='submit' class='btn btn-next btn-fill btn-warning btn-wd btn-sm' 
																  value='確認修改' id="nextSlide" style="margin-bottom: 20px; margin-top: 10px" />
														</div>
														<div class="pull-right" style="margin-right: 20%;" id="checkComStatus">
															<div style="width: 150px;height: 30px;"></div>
														</div> 
														<div class="pull-left">
															<a class='btn btn-previous btn-fill btn-default btn-wd btn-sm'
												  			   style="margin-bottom: 20px; margin-top: 10px"
												 			   href="<c:url value='/' />showCompany/${comDetail.companyDetailId}">回基本資料</a>
														</div>
													</div>
												</div>
											</c:if>
										</div>
									</div>
								</div>
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