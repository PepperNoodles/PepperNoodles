<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="org.hibernate.Hibernate,java.util.Set,java.util.HashSet,com.infotran.springboot.commonmodel.FoodTagUser,com.infotran.springboot.commonmodel.UserAccount"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="includePage/includeNav.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>contact us</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
<script src="<c:url value='/scripts/popper.min.js' />"></script>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">

<style>
#body {
	height: 100vh;
	background-image:
		url(
    "https://images.unsplash.com/photo-1523294587484-bae6cc870010?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1464&q=80"
    );
	background-size: cover;
	background-position: center center;
	background-attachment: fixed;
}

#main {
	background-color: #FCFCFC;
}

@import
	url('https://fonts.googleapis.com/css2?family=Noto+Serif+TC&display=swap')
	;

.header {
	background-color: #000000;
}

td a {
	color: #0000C6;
}

.searchButton {
	height: 30px;
	border-radius: 5px;
}

.friendsysImg {
	height: 120px; /*can be anything*/
	width: 160px; /*can be anything*/
	position: relative;
}

.friendsysImg img {
	object-fit: cover;
	max-height: 100%;
	max-width: 100%;
	width: auto;
	height: auto;
	position: absolute;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	margin: auto;
	display: block;
}

.nav a {
	color: black;
}

a:hover {
	color: blue;
}

tr:hover {
	background-color: #BEBEBE;
}

td>img {
	height: 100px;
}

table {
	padding: 5px;
	border-collapse: separate;
	border: solid black 1px;
	border-radius: 6px;
	-moz-border-radius: 6px;
}

.display {
	font-family: 'Noto Serif TC', serif;
	font-size: 15px;
	button
	{
	color
	:
	black;
}

.collumntogreen {
	color: green;
}

.collumntored {red;
	
}

table a {
	color: #0000C6;
}

.collumntogreen {
	color: green;
}

.collumntored {red;
	
}

table a {
	color: #0000C6;
}

.frame2 {
	height: 60px; /*can be anything*/
	width: 90px; /*can be anything*/
	position: relative;
}

.memoBoard {
	overflow-x: hidden;
	overflow-y: auto;
}

/* border radius example is drawn from this pen: https://codepen.io/shshaw/pen/MqMZGR
I've added a few comments on why we're using certain properties
*/
/*  td, th {  */
/*      border-left:solid black 1px;  */
/*      border-top:solid black 1px;  */
/*  }  */

/*  th {  */
/*      background-color: blue;  */
/*     border-top: none;  */
/* }  */

/*  td:first-child, th:first-child {  */
/*       border-left: none;  */
/*  }  */
</style>
<script>
		
		$(window).on('load', function() {
			
			let urlss="${pageContext.request.contextPath}/";
			urlss+="<c:url value='userLoggin/getName'/>";
			console.log(urlss);
			
			$.ajax({
				type: "GET",
				url: urlss,	
				async:false,
				dataType: "text",
				success: function (response) {
					console.log(response);	
				},
				error: function (thrownError) {
					console.log(thrownError);
				}
				
			});
			
		});
</script>
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
	<div id="main" class="container mt-5" style="width: 80%; height: 100vh">
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>

		<div class="listing-area pt-30 pb-30"></div>

		<div class="container-fluid">

			<div class="container mt-10" style="width: 80%; height: 100%">
				<h2>聯絡藕門:))</h2>
				<form onsubmit="return validateForm()" action="sendContact" method="post">
					<div class="form-group">
						<label for="exampleFormControlInput1">您的Email：</label> <input
							type="text" class="form-control" id="exampleFormControlInput1"
							placeholder="name@example.com">
					</div>

					<div class="form-group">
						<label for="exampleFormControlTextarea1">問題敘述</label>
						<textarea class="form-control" id="exampleFormControlTextarea1"
							rows="3"></textarea>
					</div>
					
<!-- 					<div class=" g-recaptcha m-3 row justify-content-center" -->
<!-- 						data-sitekey="6Lc1VccaAAAAAKKNWdKvTQoQcTDsaU8T8RgY2IjK"></div> -->
						
						<div
						    class="g-recaptcha  m-3 row justify-content-center"
						    data-sitekey="6Lc1VccaAAAAAKKNWdKvTQoQcTDsaU8T8RgY2IjK"
						    data-theme="light" data-size="normal"
						    data-callback="validateAjax"
						    data-expired-callback="expired"
						    data-error-callback="error">
						</div>
						
					<div class="wizard-footer height-wizard col-sm-10 col-sm-offset-1">
						<div class="m-3 row justify-content-center">
							<input type="submit" id="submitMessage" disabled="disabled" class='genric-btn default-border circle' style='color: black' value="送出訊息" >
							<input type="reset"  class='genric-btn default-border circle' style='color: black' value="重填" >
							

						</div>
					</div>
				</form>

			</div>
		</div>
	</div>

	<div class="listing-area pt-120 pb-120"></div>

	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

<script type="text/javascript"> 

// function validateForm(){
//  	if(grecaptcha.getResponse()){
//  		return true;
//  	}else{
//  		alert("Please prove that you're not robot!!!");
//  		return false;
//  	}
// }

 function  validateAjax (reponse){
	 alert("1")
	 console.log(reponse)
	 urls= "/PepperNoodles/recaptchaajaxcheck/?token=";
	 urls += reponse;
	 $.ajax({
		 method:"get",
		 url: urls,
		 dataType: "text",
		 success:function(result){
			 alert(result)
			 $("#submitMessage").prop("disabled",false);
		 },
		 error:function(result){
			 alert(result)
			 $("#submitMessage").prop("disabled",true);
		 }
	 
	 });
	 
		 
	 
	
}

</script>
	<script>

			$(document).ready(function () {			
				
			});			

 	</script>



</body>
</html>




