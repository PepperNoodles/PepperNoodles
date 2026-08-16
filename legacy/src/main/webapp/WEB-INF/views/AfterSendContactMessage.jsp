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
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>

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

		<div class="listing-area pt-30 pb-30"></div>

		<div class="container-fluid">

			<div class="container mt-10" style="width: 80%; height: 100%">
				<h1>Nice!!訊息已收到 我們應該會找時間處理唷XDD</h1>

			</div>
		</div>
	</div>

	<div class="listing-area pt-120 pb-120"></div>

	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

<script type="text/javascript"> 
function validateForm(){
 	if(grecaptcha.getResponse()){
 		return true;
 	}else{
 		alert("Please prove that you're not robot!!!");
 		return false;
 	}
}
</script>
	<script>
	

			$(document).ready(function () {			
				
			}
				
			
			

 	</script>



</body>
</html>




