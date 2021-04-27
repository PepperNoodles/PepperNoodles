<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新增活動</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css" rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<%-- <script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script> --%>
<!-- 有她左邊的BAR會變小 -->
<%-- <link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" /> --%>
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />
<!-- 套用datepicker需加下面3個套件 -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>



<script>
var $j = jQuery.noConflict();
	
$j(document).ready(function(){
	
	var eventNameError = false;
	var eventStartDateError = false;
	var eventEndDateError = false;
	var contentError = false;
// 	var eventPictureError = false;
	
	//預覽圖片
	$("#eventPicture").change(function() {
		if (this.files && this.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#picture').attr('src',e.target.result);
				$('#picture').css("border-style","none");
			}
			reader.readAsDataURL(this.files[0]);
		}
// 		eventPictureError = true;
	});
	
	//選擇開始時間
	$j("#eventStartDate").datepicker({
		dateFormat: 'yy-mm-dd',
		showOn : "button",
		buttonImage : "${pageContext.request.contextPath}/images/company/date.png",
		buttonImageOnly : true,
// 		minDate: 0,
		onClose: function(selectedDate) {
			$j("#eventEndDate").datepicker("option", "minDate", selectedDate);
		}	
	});
	
	//選擇結束時間-不能比開始時間小
	$j("#eventEndDate").datepicker({
		dateFormat: 'yy-mm-dd',
		showOn : "button",
		buttonImage : "${pageContext.request.contextPath}/images/company/date.png",
		buttonImageOnly : true,
		changeMonth: true,
		changeYear: true,
		minDate: 0,
		onClose: function(selectedDate) {
			$j("#eventStartDate").datepicker("option", "maxDate", selectedDate);
		}
	});
		
	
	$("#eventName").blur(function(){
		let value=$(this).val();
	    let txt="";
	    if(value==""){
	    	$("#eventNameResult").css({"color":"red","font-size":"small"});
	    	$("#eventName").css({"border":"2px solid red"});
	    	txt="<span>活動名稱不可為空白</span>";
	    	eventNameError = false;
	    }
	    else{
	    	$("#eventName").css("border","2px solid green");
	        txt="&emsp;";
	        eventNameError = true;
	    }
	    $("#eventNameResult").html(txt);
	});
	
	$("#eventStartDate").blur(function(){
		let value=$(this).val();
	    let txt="";
	    if(value==""){
	    	$("#eventStartDateResult").css({"color":"red","font-size":"small"});
	    	$("#eventStartDate").css({"border":"2px solid red"});
	    	txt="<span>活動開始日期不可為空白</span>";
	    	eventStartDateError = false;
	    }
	    else{
	    	$("#eventStartDate").css("border","2px solid green");
	        txt="&emsp;";
	        eventStartDateError = true;
	    }
	    $("#eventStartDateResult").html(txt);
	});
	
	$("#eventEndDate").blur(function(){
		let value=$(this).val();
	    let txt="";
	    if(value==""){
	    	$("#eventEndDateResult").css({"color":"red","font-size":"small"});
	    	$("#eventEndDate").css({"border":"2px solid red"});
	    	txt="<span>活動結束日期不可為空白</span>";
	    	eventEndDateError = false;
	    }
	    else{
	    	$("#eventEndDate").css("border","2px solid green");
	        txt="&emsp;";
	        eventEndDateError = true;
	    }
	    $("#eventEndDateResult").html(txt);
	});
	
	$("#content").blur(function(){
		let value=$(this).val();
	    let txt="";
	    if(value==""){
	    	$("#contentResult").css({"color":"red","font-size":"small"});
	    	$("#content").css({"border":"2px solid red"});
	    	txt="<span>活動內容不可為空白</span>";
	    	contentError = false;
	    }
	    else{
	    	$("#content").css("border","2px solid green");
	        txt="&emsp;";
	        contentError = true;
	    }
	    $("#contentResult").html(txt);
	});
	
	//一鍵新增
	$("#butAdd").click(function(){
		$j("#eventName").val('開幕慶!!!');
		$j("#eventStartDate").val('2021-05-18');
		$j("#eventEndDate").val('2021-06-18');
		$j("#content").val('提前預訂，兩人同行即可享第二人半價優惠!');
		eventNameError = true;
		eventStartDateError = true;
		eventEndDateError = true;
		contentError = true;
	});

	
	//傳值
	$("#addEvent").click(function(){
		restaurantId = ${restId};
		eventName = $("#eventName").val();
		eventStartDate = $("#eventStartDate").val();
		eventEndDate = $("#eventEndDate").val();
		content = $("#content").val();
		
		if(!eventNameError || !eventStartDateError || !eventEndDateError || !contentError){
			alert("請輸入完整訊息");		
		}
// 		else if(!eventPictureError){
// 			alert("請上傳圖片");	
// 		}
		else{
		data = new FormData();
    	data.append('file', $('#eventPicture')[0].files[0]);
		data.append('eventInfo',new Blob([JSON.stringify( {"restaurantId": restaurantId,
														   "eventName": eventName,
														   "eventStartDate": eventStartDate,
														   "eventEndDate" : eventEndDate,
														   "content" : content} )],{type: "application/json"}));
		$.ajax({
			method:"POST",
			url:"/PepperNoodles/addEvent",
			data:data,
			processData: false,
			contentType: false, 
			cache: false,  //不做快取
			async : true,
			success: function (result) {
				alert("新增成功");
			location.href="http://localhost:433/PepperNoodles/event/"+restaurantId;
			},
	        error: function (result) {
// 				alert("新增失敗");
			}
		})
		}
	});
	
	//刪除
	$(".deleteEvent").click(function(){
		if (confirm('確定刪除此筆紀錄? ')) {
    		var href = $(this).attr('href');
            $('#deleteform').attr('action', href).submit();
    	} 
    	return false;
	});
	
		
});



	
</script>
<style>
.nopadding{
	padding:0 !important;
	margin: 0 !important;
}
table {
	text-align: center;
}
#picture{
 	border-style:dashed;
	cursor: pointer;
}
buttonImage{
	height: 20px
}
.page{
	background-color:#FFFAF4;
	border:5px	#842B00 double;
	margin:0px 20px 20px 0px; 
	border-radius: 10px;
}
.time{
	color: red;
}
.img{
	 opacity: 0.5;
}
.pull-right a{
	color:#8E8E8E;
}
.pull-right a:hover{
	color: red;
}
body {
　background-image: url(<c:url value="/images/company/event.jpeg"/>);
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
        		<%@include file="../company/left2.jsp" %>
        	</div>
        </div>
        
		<div class="col-lg-10 nopadding" >
<%-- 			<div class="image-container set-full-height img" style="background-image: url(<c:url value="/images/company/event.jpeg"/>)" > --%>
				<br><br><br>
				<table class="container" >
					<tr>
						<th colspan="4">
							<br>
							<div class="infobox">
								<label for="eventPicture">
									<img class="rounded" src="<c:url value="/images/company/++.png"/>" height="200px" id="picture"/>
								</label>
								<input hidden type="file" id="eventPicture" accept="image/*" name="photo">
								<br>
								上傳活動圖片
								<br>&nbsp;<br>&nbsp;<br>&nbsp;<br>&nbsp;
							</div>
						</th>
					</tr>
					<tr>
						<th>活動名稱</th>
						<th>開始時間</th>
						<th>結束時間</th>
						<th>活動內容</th>
					</tr>
					<tr>
						<td >
							<input id="eventName" type="text">
							<br>
							<span id="eventNameResult">&nbsp;</span>
						</td>
						<td>
							<input type="text" id="eventStartDate" name="eventStartDate"/>
							<br>
							<span id="eventStartDateResult">&nbsp;</span>
						</td>
						<td>
							<input type="text" id="eventEndDate" name="eventEndDate" />
							<br>
							<span id="eventEndDateResult">&nbsp;</span>
						</td>
	 	 				<td>
	 	 					<textarea id="content" name="content" rows="5" cols="40" placeholder="活動內容..." required></textarea>
	 	 					<br>
							<span id="contentResult">&nbsp;</span>
	 	 				</td>
					</tr>
					<tr>
						<td colspan="4" id="addEvent">
							<input type='button' class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
							       name='next' value='新增活動' id="nextSlide" style="margin-bottom: 20px;margin-top: 10px"/>
							<p style="text-align:right;" id="butAdd">一鍵新增</p>
						</td>
					</tr>
				</table>
				<br>
				<hr>
				<div id="menuList">
      				<form id='deleteform' method='POST'>
						<input type='hidden' name='_method' value='DELETE'>
					</form>
				
				<c:choose>
					<c:when test="${empty events}">
	    				<h4 style="text-align:center">沒有任何活動</h4><br> 
					</c:when>
					<c:otherwise>
					<div class="container">
						<div class="row ml-2">
							<c:forEach var='events' items='${events}'>
								<div class="page col-3 pl-3 ml-3">
									<div class="item">
										<P></P>
										<a href="<c:url value='/getEventPicture/${events.eventId}'/>" >
											<img width="100%" src="<c:url value="/getEventPicture/${events.eventId}"/>" />
										</a>
										<p />
										<h4>${events.eventName}</h4>
										<p class="time">${events.eventStartDate} - ${events.eventEndDate}</p>
										<P>${events.content}</P>
									</div>
									<div class="pull-right">
										<a id="updateEvent" href="<c:url value='/findUpdateEvent/${events.eventId}' />">修改</a>
										<a class="deleteEvent" id="deleteEvent" href="<c:url value='/deleteEvent/${events.eventId}' />">刪除</a>
									</div>
								</div>
								<br><br><br><br><br>
						
						
						
						
						
						
						
						</c:forEach>
      					</div>
      					<!-- row 結束 -->
      				</div>
      				<!-- container 結束 -->
					</c:otherwise>
				</c:choose>
				</div>
			
			
			
			
			
			
			
				
			</div>
		<!--  big container -->
		</div>
<!-- 	</div> -->
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