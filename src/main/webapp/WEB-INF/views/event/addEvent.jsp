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
	});
	
	//選擇開始時間
	$j("#eventStartDate").datepicker({
		showOn : "button",
		buttonImage : "images/company/1.png",
		buttonImageOnly : true,
// 		minDate: 0,
		onClose: function(selectedDate) {
			$j("#eventEndDate").datepicker("option", "minDate", selectedDate);
		}	
	});
	
	//選擇結束時間-不能比開始時間小
	$j("#eventEndDate").datepicker({
		showOn : "button",
		buttonImage : "images/company/1.png",
		buttonImageOnly : true,
		changeMonth: true,
		changeYear: true,
		minDate: 0,
		onClose: function(selectedDate) {
			$j("#eventStartDate").datepicker("option", "maxDate", selectedDate);
		}
	});
		
	//一鍵新增
	$("#butAdd").click(function(){
		$j("#eventName").val('開幕慶!!!');
		$j("#eventStartDate").val('2021/05/18');
		$j("#eventEndDate").val('2021/06/18');
		$j("#content").val('提前預訂，兩人同行即可享第二人半價優惠!');
	});
	
	//傳值
	$("#addEvent").click(function(){
		restaurantId = 1;
		eventName = $("#eventName").val();
		eventStartDate = $("#eventStartDate").val();
		eventEndDate = $("#eventEndDate").val();
		content = $("#content").val();
		
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
			},
	        error: function (result) {
// 				alert("新增失敗");
			}
		})
		
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
        		<%@include file="../company/left.jsp" %>
        	</div>
        </div>
        
		<div class="col-lg-10 nopadding " >
			<div class="image-container set-full-height" style="background-image: url(<c:url value="/images/company/event.jpeg"/>)">
				<br><br><br>
				<table class="container" border='1' cellpadding="3" cellspacing="1">
					<tr>
						<th colspan="4">
							<br>
							<div class="infobox">
								<label for="eventPicture">
									<img src="<c:url value="/images/company/++.png"/>" height="200px" id="picture"/>
								</label>
								<input hidden type="file" id="eventPicture" accept="image/*" name="photo">
								<br>
								上傳活動圖片
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
							<input id="eventName" type="text" >
						</td>
						<td>
							<input type="text" id="eventStartDate" name="eventStartDate" />
							
						</td>
						<td>
							<input type="text" id="eventEndDate" name="eventEndDate" />
						</td>
	 	 				<td>
	 	 					<textarea id="content" name="content" rows="5" cols="40" placeholder="活動內容..." required></textarea>
	 	 				</td>
					</tr>
					<tr>
						<td colspan="4" id="addEvent">
							<input type='button' class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
							       name='next' value='新增活動' id="nextSlide" style="margin-bottom: 20px;margin-top: 10px"/>
						</td>
						
						
					</tr>
					<tr>
						<td colspan="4" style="text-align:right;" id="butAdd"><br>一鍵新增</td>
					</tr>
				</table>
				
				
			</div>
		<!--  big container -->
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