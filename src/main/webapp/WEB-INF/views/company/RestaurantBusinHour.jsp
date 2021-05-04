<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>餐廳營業時間</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-datetimepicker/2.7.1/css/bootstrap-material-datetimepicker.min.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-datetimepicker/2.7.1/js/bootstrap-material-datetimepicker.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/locale/ja.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/locale/zh-tw.min.js"></script>
<style>
.toshow {
	display: block;
}

.tohide {
	display: none;
}
</style>
<script>

			
</script>
</head>
<body>


	
	<div id="Monday" >
	<span id="MondaySelect">	
	星期一
	<input type="radio" name="Day1" value="1" id="MondayOpenRadio" checked="true"/>營業
	<input type="radio" name="Day1" value="1" id="MondayCloseRadio"/>不營業
			</span>
	

	<div id="MondayOpen" style="display" >
<form:form id="sendform" method="POST" modelAttribute="restaurantBusinHour" enctype='multipart/form-data'>
			<form:input path="day" type="text" value="1" id="day1" style="display:none" />
			from <form:input path="openTime" type="text" id="open1" /> to <form:input path="closeTime" type="text" id="close1" /><br>
			from <form:input path="openTime2nd" type="text" id="open2" /> to <form:input path="closeTime2nd" type="text" id="close2" /><br>
			from <form:input path="openTime3rd" type="text" id="open3" /> to <form:input path="closeTime3rd" type="text" id="close3" />
	</form:form>
	
	
		<input type="button" id="checktime" value="確定時間" />
		
		
		
		<script>
			$(function() {
				
				//open1
				var hr1_open1 = null;
				var min1_open1 = null;
				var hr2_open1 = null;
				var min2_open1 = null;
				// 				var m = moment();
				$('#open1').bootstrapMaterialDatePicker({
					lang : 'zh-tw',
					format : 'HH:mm',
					date : false,
					cancelText : '回上一步',
					switchOnClick : true,
					shortTime : true

				});

				$('#open1').change(
						function() {

							// 					console.log($('#lang1')[0].value);

							var timestr1_open1 = $('#open1')[0].value.split(":", 2);
							hr1_open1 = timestr1_open1[0];
							min1_open1 = timestr1_open1[1];

							var date1 = moment("20210501", "YYYYMMDD").add(hr1_open1,
									'hours').add(min1_open1, 'minutes');
							var date2 = moment("20210501", "YYYYMMDD").add(hr1_open1,
									'hours').add(min1_open1, 'minutes').add(1,
									'minutes');
							var date3 = moment("202105012359", "YYYYMMDDHHmm");

							$('#close1').bootstrapMaterialDatePicker({
								lang : 'zh-tw',
								currentDate : date1,
								minDate : date2,
								maxDate : date3,
								switchOnClick : true,
								format : 'HH:mm',
								date : false,
								cancelText : '回上一步',
								shortTime : true

							});

						});

				$('#close1').change(function() {

					// 					console.log($('#lang2')[0].value);

					var timestr2_open1 = $('#close1')[0].value.split(":", 2);
					hr2_open1 = timestr2_open1[0];
					min2_open1 = timestr2_open1[1];

				});
				
				//確認日期正確
				$('#checktime').click(function() {

					if (hr1_open1 != null & hr2_open1 != null) {

						if (parseInt(hr1_open1) > parseInt(hr2_open1)) {
							console.log('NG');
							checkboolean = "false";
						} 
						else if(parseInt(hr1_open1) == parseInt(hr2_open1)){
							if(parseInt(min1_open1) > parseInt(min2_open1)){
								console.log('NG');
								checkboolean = "false";
							}
							else{
								console.log('OK');
								checkboolean = "true";
							}
						}
						else {
							console.log('OK');
							checkboolean = "true";
						}

					} else {
						alert('至少填一段時間');
					}

				});
				
				//open2
				var hr1_open2 = null;
				var min1_open2 = null;
				var hr2_open2 = null;
				var min2_open2 = null;
				// 				var m = moment();
				$('#open2').bootstrapMaterialDatePicker({
					lang : 'zh-tw',
					format : 'HH:mm',
					date : false,
					cancelText : '回上一步',
					switchOnClick : true,
					shortTime : true

				});

				$('#open2').change(
						function() {

							// 					console.log($('#lang1')[0].value);

							var timestr1_open2 = $('#open2')[0].value.split(":", 2);
							hr1_open2 = timestr1_open2[0];
							min1_open2 = timestr1_open2[1];

							var date1 = moment("20210501", "YYYYMMDD").add(hr1_open2,
									'hours').add(min1_open2, 'minutes');
							var date2 = moment("20210501", "YYYYMMDD").add(hr1_open2,
									'hours').add(min1_open2, 'minutes').add(1,
									'minutes');
							var date3 = moment("202105012359", "YYYYMMDDHHmm");

							$('#close2').bootstrapMaterialDatePicker({
								lang : 'zh-tw',
								currentDate : date1,
								minDate : date2,
								maxDate : date3,
								switchOnClick : true,
								format : 'HH:mm',
								date : false,
								cancelText : '回上一步',
								shortTime : true

							});

						});

				$('#close2').change(function() {

					// 					console.log($('#lang2')[0].value);

					var timestr2_open2 = $('#close1')[0].value.split(":", 2);
					hr2_open2 = timestr2_open2[0];
					min2_open2 = timestr2_open2[1];

				});
				
			

			})
		</script>
	</div><!-- monday Open -->
		<input type="button" id="checkBeforeSubmit" value="送出" />
		<script>
//			送給controller 要改成ajax
		var checkboolean=false;
		$("#checkBeforeSubmit").click(function() {
			var type = $(this).attr('type');
			if (checkboolean == "true") {
				console.log('送出成功'); 
				console.log('checkboolean='+checkboolean); 
				console.log('inputValue='+$('#open1')[0].value); 
				$("#sendform").submit();
			} else {
				console.log('送出失敗')
				console.log('checkboolean='+checkboolean);
				console.log('inputValue='+$('#open1')[0].value); 
				return false;
			}

		});
// 		hide or show Open div
		$(function() {
			var MondayOpen = $('#MondayOpen');		
			  $("#MondaySelect").click(function() {
				  
				  if($('#MondayOpenRadio').prop("checked")==true){
					  
					  MondayOpen.removeClass("tohide");
				  }
				  else if($('#MondayOpenRadio').prop("checked")==false){
					  MondayOpen.addClass("tohide");
				  }
				  
	          });
			
		})
		
		
		</script>	

	</div> <!-- monday -->




</body>
</html>