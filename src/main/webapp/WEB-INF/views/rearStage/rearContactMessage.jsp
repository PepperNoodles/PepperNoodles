<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>聯絡我們</title>
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
<link rel="stylesheet"
	href="<c:url value='/css/owl.carousel.min.css' />">
<style>
.header {
	background-color: #000000;
	
}
#body{
	height:100vh;
	 background-image: url(
	 "https://images.unsplash.com/photo-1505935428862-770b6f24f629?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1047&q=80"
    );
    background-size: cover;
    background-position: center center;
    background-attachment: fixed;
}
#main{
	height:100vh;
}
</style>
</head>
<body id="body">
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

	<%@include file="../includePage/includeNav.jsp" %>
<div>
	<div id="main" class="container"> <!-- style="border: 1px solid red"> -->
		<div class="row justify-content-center align-items-center pt-100">
		<script src="https://www.google.com/recaptcha/api.js" async defer></script>
			<form id="messageForm" action="<c:url value=''/>" method="post"
				class="search-box align-items-center" >
				
				<div class="mt-10 col-36 ">
					<label for="nickname" >姓名:</label>
					<input id="nickname" type="text" name="nickname" placeholder="請輸入姓名" required
						class="single-input">
				</div>
				
				<div class="mt-10 col-36 ">
					<label for="username" >信箱:</label>
					<input id="username" type="text" name="username" placeholder="請輸入信箱" required
						class="single-input">
				</div>
<!-- 				<div class="mt-10 col-24"> -->
<!-- 					<input id="password" type="password" name="password" placeholder="Password:" required -->
<!-- 						class="single-input "> -->
<!-- 				</div> -->
				
				<div class="form-group">
		            <label for="message-text" class="col-form-label">Message:</label>
		            <textarea class="form-control" id="message-text"  cols="40" rows="6"></textarea>
	          	</div>

<!-- 				<div class="mt-10 col-24"> -->
<!-- 					RememberMe&nbsp;&nbsp;<input type="checkbox" name="remember-me"> -->
<%-- 					<br><a href="<c:url value='/forgotPassword'/>" ><span style="color:blue; hover:background-color: blue;">Forgot Password?</span></a> --%>
<!-- 				</div> -->
				<div class="mt-10 ">
				    
				    <div class="bs-example">
				    
<!-- 					<button type="submit" id="messageButton" value="submit" -->
<!-- 						class="genric-btn danger radius">送出</button> -->
							<div
						    class="g-recaptcha"
						    data-sitekey="6Lc1VccaAAAAAKKNWdKvTQoQcTDsaU8T8RgY2IjK"
						    data-theme="light" data-size="normal"
						    data-callback="validateAjax"
						    data-expired-callback="expired"
						    data-error-callback="error">
						</div>
						<br>
					  <input style="display:none" id="messageButton" type="button" value="送出" 
								class="genric-btn danger radius show-toast"/>
						
					   <div class="toast" id="myToast" style="position: fixed; bottom: 76%;right: -55; ">
					         <div class="toast-header">
					            <strong class="mr-auto"><i class="fa fa-grav"></i> 訊息通知!</strong>
					            <small>1 秒前</small>
					            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">
					                <span aria-hidden="true">&times;</span>
					            </button>
					        </div>
					        <div class="toast-body">
					            <div><p>感謝你寶貴的意見!! 我們會盡快回覆你。 
					                 <p><a href="/PepperNoodles" style="color: red">請按此返回首頁，謝謝</a></div>
					        </div>
					    </div>
					</div>	
						
					<br><br><br>
					<div class="pull-right">
<!-- 						<a href="#" id="user">會員一鍵登入</a>&emsp;&emsp;&emsp; -->
<!-- 						<a href="#"  id="company">企業一鍵登入</a> -->
							<a href="#"  id="message">訊息一鍵登入</a>
					</div>
                    
     
				</div>
			</form>
			
<!-- 			<div class="bs-example"> -->
<!-- 			     <input  id="messageButton" type="button" value="送出"  -->
<!-- 						class="genric-btn danger radius show-toast"/> -->
<!-- 			    <button type="button" class="genric-btn danger radius show-toast">Show Toast</button> -->
<!-- 			    <div class="toast" id="myToast" style="position: absolute; top: 20; right: -55;"> -->
<!-- 			        <div class="toast-header"> -->
<!-- 			            <strong class="mr-auto"><i class="fa fa-grav"></i> We miss you!</strong> -->
<!-- 			            <small>11 mins ago</small> -->
<!-- 			            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast"> -->
<!-- 			                <span aria-hidden="true">&times;</span> -->
<!-- 			            </button> -->
<!-- 			        </div> -->
<!-- 			        <div class="toast-body"> -->
<!-- 			            <div>It's been a long time since you visited us. We've something special for you. <a href="#">Click here!</a></div> -->
<!-- 			        </div> -->
<!-- 			    </div> -->
<!-- 			</div> -->

		</div>
	</div>
	<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>
</div>
	<script type="text/javascript">
// 		$(window).on('load', function() {

// 			$(".header-sticky").css("height", "90px");

// 			$('#preloader-active').delay(450).fadeOut('slow');
// 			$('body').delay(450).css({
// 				'overflow' : 'visible'
// 			});
			
// 			//一鍵新增-企業
// 			$("#company").click(function(){
// 				$("#password").val('123!Q123');
// 				$("#username").val('ting0420a@gmail.com');
// 			});
// 			//一鍵新增-會員
// 			$("#user").click(function(){
// 				$("#password").val('a123456@');
// 				$("#username").val('chrislo5311@gmail.com');
// 			});
			
// 			$('form').submit(function(ev){
// 				 ev.preventDefault();
// 				 let username = $("#username").val();
// 				// console.log(username);
// 				 let urls = "<c:url value='/userPreLoggin/getName'/>";
// 				 urls +="?username="+username;
				 
// 				 $.ajax({
// 						type: "GET",
// 						url: urls,				
// 						dataType: "text",
// 						success: function (response) {
// 							console.log(response);	
// 							if (response == 'true'){
// 								//console.log('ok');	
// 								$('form').unbind('submit').submit()
// 							}
// 							$('form').unbind('submit').submit()
// 						},
// 						error: function (thrownError) {
// 							console.log(thrownError);
// 						}
// 					});
				 
// 		         //later you decide you want to submit
// 		         //			
// 			})
			
			
// 		});
		 
		 //傳送form表單和留言訊息
		$(document).ready(function(){
			$("#messageButton").click(function(){
// 				alert("here");
				let formdata = new FormData();
				let blob = new Blob([ JSON.stringify({
					"userRealName":$("#nickname:text").val(),
					"userAccountIndex": $("#username:text").val(),
					"messageText": $("#message-text").val()
				
				})]);
				
				
				formdata.append('rearMessageInfo',blob);
				
				let url="<c:url value='/addRearMessage'/>"; //要傳送的controller方法路徑
				$.ajax({
					type:"POST",
					url:url,
					dataType:"json",
					data:formdata,
					contentType:false,
					processData: false, 
// 					data:{
// 						nickname:$("#nickname").val(),
// 						username:$("#username").val(),
// 						message-text:$("#message-text").val()
// 					},
					success:function(){	
						    $("#myToast").toast({ delay: 5000 }); //訊息通知停留5秒
                            //$("#myToast").toast({ autohide: false }); //訊息通知需要手動關閉
						    $("#myToast").toast('show');
						    //alert("成功");
						    setTimeout(function(){
				        		window.location.href = "http://localhost:433/PepperNoodles/"; //页面重導
//	 							location.reload(); //成功重整頁面
				        		},5000); //5秒後跳轉頁面
					},
					
					error:function(){
						alert("失敗!");
					}
				});
			});
			
			//一鍵新增-訊息
			$("#message").click(function(){
				$("#nickname").val('路人甲');
				$("#username").val('1qaz2ws0502@gmail.com');
				$("#message-text").val('請問，我為什麼被停權了!!');
			});
		});
		 
		//機器人認證 reCAPTCHA
		 function  validateAjax (reponse){
// 			 alert("1")
			 console.log(reponse)
			 urls= "/PepperNoodles/recaptchaajaxcheck/?token=";
			 urls += reponse;
			 $.ajax({
				 method:"get",
				 url: urls,
				 dataType: "text",
				 success:function(result){
					 $("#messageButton").toggle();
					
				 },
				 error:function(result){
					 alert(result)
				 }
			 
			 });
		 }
	</script>
	
	<script>
// 		$(document).ready(function(){
// 		    $(".show-toast").click(function(){
// 		        $("#myToast").toast({ delay: 8000 });
// 		        $("#myToast").toast('show');
// 		    }); 
// 		});
	</script>

</body>
</html>