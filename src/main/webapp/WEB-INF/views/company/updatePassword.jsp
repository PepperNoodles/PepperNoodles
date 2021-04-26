<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改密碼</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<!-- 有她左邊的BAR會變小 -->
<%-- <link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" /> --%>
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
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "<c:url value='/updatePwd/' />"+id, true);
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
				       window.location = "<c:url value='/Company/company' />";
				    }
				}, 1000);	
			}			
		}
	});
	
	$("#instant").click(function(){
		$("#userPwd").val('123!Q123');
		$("#nextUserPwd").val('123!Q123');
		$("#checkNextUserPwd").val('123!Q123');
		hasErrorPwd = true;
		hasErrorNextPwd = true;
        hasErrorCheckNextPwd = true;
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
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
#instant{
	color:olive;
	cursor: pointer;
}
</style>
</head>
<body>
<%@include file="../includePage/includeNav.jsp" %>
	
<div class="container-fluid">
	<div class="row">
    <!-- 左邊的Bar -->
    	<div class="col-lg-2 nopadding">
      		<br>
        	<div class="list-group">
        		<%@include file="left.jsp" %>
        	</div>
        </div>
        
		<div class="col-lg-10 nopadding " >
      		<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/company/conpany.jpg"/>)">
				<!-- container   -->
				
				<div class="container">
				<!-- 置中用↓   -->
					<div class="row d-flex align-items-center justify-content-center">
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
									<!-- 整大包的div -->
									<div id="accoutPage1">
										<div>
											<c:if test='${comDetail.companyDetailId == null}'>
												<br>帳號：<br>&nbsp;
												<br>&nbsp;
											</c:if>
										</div>
										<div class="row">
												<c:if test='${comDetail.companyDetailId != null}'>
													<div class="col-sm-5 col-sm-offset-1"></div>
													<div class="col-sm-7">
														<div class="form-group">
															<label><h5>會員帳號：${comDetail.userAccount.accountIndex}</h5></label>
														</div>
													</div>
													<div class="col-sm-4 ml-5">
														<br><br><br>
														<div>
															<img src="<c:url value="/getComPicture/${comDetail.companyDetailId}"/>" width="100%" />
														</div>
													</div>
													<div class="col-sm-6 ml-1">
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
													<div class="col-sm-12"></div>
													<div class="col-sm-1 col-sm-offset-2"></div>
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
													 	  		   href="<c:url value='/' />updateCom/${comDetail.companyDetailId}">回修改資料</a>
															</div>
														</div>
													</div>
												</c:if>
										</div>
										<div><a class="pull-right" id="instant">一鍵修改</a></div>
									</div>
									<!-- ↑　整大包的div 結束 -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- ↑ 背景結束 -->
		</div>
		<!-- ↑ 右大半邊結束 -->
	</div>		
	<!-- ↑ row 結束 -->
</div>				
			
			
			
			
<%@include file="../includePage/includeFooter.jsp" %>
<!-- Scroll Up -->
<div id="back-top">
	<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
</div>
<script>
	$(window).on('load', function() {
		
		//讓bar固定在上面以及設定高度
		$(".header-sticky").addClass("sticky-bar");
 		$(".header-sticky").css("height", "90px");
		$(".header-sticky").css("position","static")

 		//讓loading圖動起來
 		$('#preloader-active').delay(450).fadeOut('slow');
 		$('body').delay(450).css({'overflow' : 'visible'});			
			
 	});
 </script>
</body>
</html>