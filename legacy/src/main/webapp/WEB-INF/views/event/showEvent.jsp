<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>餐廳活動</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<!-- 有她左邊的BAR會變小 -->
<link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" />
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />

<script>
$(document).ready(function(){
	showEvent();
	
});

function showEvent(){
	let urls="Http://localhost:433";
    urls+="<c:url value='/notOverEvent' />";
$.ajax({
		type: "GET",
		url: urls,				
		dataType: "text",
		success: function (response) {
			console.log(response);
			notOverEvent = JSON.parse(response);
			console.log("未結束的活動有幾筆:"+notOverEvent.length);
			
			var text="";
// 				text +="<div class='container'>";
// 				text +="	<div class='row ml-2'>";
			if(notOverEvent.length == 0){
				text="<h2 class='noEvent'>目前沒有活動...</h2>";
			}
			else{
				
			for(i=0;i<notOverEvent.length;i++){
				
				text +="		<div class='page col-md-4' style='width: 30rem;'>";
				text +="			<div class='item'>";
				text +="				<p></p>";
				text +="				<figure>";
				text +="					<div class='eventImg'>";
				text +="						<a href='<c:url value='/getEventPicture/"+notOverEvent[i].eventId+"'/>' >";
				text +="							<img class='img-thumbnail' src='<c:url value='/getEventPicture/"+notOverEvent[i].eventId+"'/>' />";
				text +="						</a>";
				text +="					</div>";
				text +="					<figcaption class='mt-3 ml-5'>活動店家 : ";
				text +="						<a href='<c:url value='/userPage/"+notOverEvent[i].restaurant.restaurantId+"'/>'>"+notOverEvent[i].restaurant.restaurantName+"</a>";
				text +="					</figcaption>";
				text +="				</figure>";
				text +="				<p />";
// 				text +=""+ notOverEvent[i].restaurant.restaurantName +"";
				text +="				<h5>"+notOverEvent[i].eventName+"</h5>";
				text +="				<p class='time'>"+notOverEvent[i].eventStartDate+"-"+notOverEvent[i].eventEndDate+"</p>";
				text +="				<P>"+notOverEvent[i].content+"</P>";
				text +="			</div>";
				text +="		</div>";
			}
// 				text +="	</div>";
// 				text +="</div>";
			}
			
			$("#showNotOver").html(text);
		},
		error: function (thrownError) {
			console.log(thrownError);
		}
	});
}
</script>
<style>
.noEvent{
	margin:auto;
	text-align: center;
}
.page{
	height:450px;
	background-color:#FFFAF4;
	border:5px	#842B00 double;
	margin:0px 20px 20px 0px; 
	border-radius: 10px;
}
.time{
	color: red;
}
.item div{
	text-align:center;
	height:250px;
	line-height : 250px;
	}
.eventImg img{

		vertical-align:middle;
	    max-height: 100%;  
	    max-width: 100%; 
	    width: auto;
	    height: auto;
	    text-align: center;
/*  	    position: absolute;    */
 	    top: 0;   
 	    bottom: 0;   
 	    left: 0;   
 	    right: 0;   
	    margin: auto;    
/*    		display: block; 有他在圖片無法垂直置中 */ 
}
.EventIngDiv{
	text-align:center;
}
div .EventIngH2{
 	color: 	#F00078;
 	font-weight:1000;
 	font-size:70px;
 	 
}
</style>
</head>
<body>
<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/company/event.png"/>)" >
<p>&emsp;</p>

<div class="section-tittle text-center mb-80 EventIngDiv">
	<p class="EventIngH2">活 動 進 行 中 !!</p>
</div>
<div class="container " id="showNotOver">

<!-- 	<div class="card"> -->
<!-- 		<div class="row rowOdd"> -->
<!-- 			<div class="col-md-3"> -->
<!-- 				<img width="500px" src='https://bootdey.com/img/Content/user_2.jpg' > -->
<!-- 			</div> -->
<!-- 			<div class="col-md-9"> -->
<!-- 				<div class="card-body"> -->
<!-- 					<h5 class="card-title">Card title</h5> -->
<!-- 				</div> -->
<!-- 			</div>		 -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- 	<div class="card"> -->
<!-- 		<div class="row rowEven"> -->
<!-- 			<div class="col-md-3 "> -->
<!-- 				<img src='https://bootdey.com/img/Content/user_2.jpg' > -->
<!-- 			</div> -->
<!-- 			<div class="col-md-9"> -->
<!-- 				<div class="card-body"> -->
<!-- 					<h5 class="card-title">Card title</h5> -->
<!-- 				</div> -->
<!-- 			</div>		 -->
<!-- 		</div> -->
<!-- 	</div> -->
	
	
</div>
</div>
</body>

</html>