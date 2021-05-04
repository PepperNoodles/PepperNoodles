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
	<input type="radio" name="Day1" value="1" id="MondayOpenRadio" checked="true" />營業
	<input type="radio" name="Day1" value="1" id="MondayCloseRadio"/>不營業
			</span>
	<form:form id="MondayCloseform" method="POST" modelAttribute="restaurantBusinHour" enctype='multipart/form-data'>
	<form:input path="day" type="text" value="1" id="day1close" style="display:none" />
	</form:form>
	<div id="MondayOpen" style="display" >
<form:form id="MondayOpenform" method="POST" modelAttribute="restaurantBusinHour" enctype='multipart/form-data'>
			<form:input path="day" type="text" value="1" id="day1open" style="display:none" />
			from <form:input path="openTime" type="text" id="open1" /> to <form:input path="closeTime" type="text" id="close1" /><br>
			from <form:input path="openTime2nd" type="text" id="open2" /> to <form:input path="closeTime2nd" type="text" id="close2" /><br>
			from <form:input path="openTime3rd" type="text" id="open3" /> to <form:input path="closeTime3rd" type="text" id="close3" />
	</form:form>
	
	
		<input type="button" id="checktime" value="檢查時間" />
		
		
		
		<script>
			$(function() {
				alert(${restaurantBusinHour.restaurant.restaurantId});
				//open1
				var hr1_open1 = null;
				var min1_open1 = null;
				var hr2_open1 = null;
				var min2_open1 = null;
				
				var date1_open1=null;
				var date2_open1=null;
				var date3_open1=null;
				//open2
				var hr1_open2 = null;
				var min1_open2 = null;
				var hr2_open2 = null;
				var min2_open2 = null;
				//open3
				var hr1_open3 = null;
				var min1_open3 = null;
				var hr2_open3 = null;
				var min2_open3 = null;
				

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

							var timestr1_open1 = $('#open1')[0].value.split(":", 2);
							hr1_open1 = timestr1_open1[0];
							min1_open1 = timestr1_open1[1];

							date1_open1 = moment("20210501", "YYYYMMDD").add(hr1_open1,
									'hours').add(min1_open1, 'minutes');
							date2_open1 = moment("20210501", "YYYYMMDD").add(hr1_open1,
									'hours').add(min1_open1, 'minutes').add(1,
									'minutes');
							date3_open1 = moment("202105012359", "YYYYMMDDHHmm");

							$('#close1').bootstrapMaterialDatePicker({
								lang : 'zh-tw',
								currentDate : date1_open1,
								minDate : date2_open1,
								maxDate : date3_open1,
								switchOnClick : true,
								format : 'HH:mm',
								date : false,
								cancelText : '回上一步',
								shortTime : true

							});

						});

				$('#close1').change(function() {

					var timestr2_open1 = $('#close1')[0].value.split(":", 2);
					hr2_open1 = timestr2_open1[0];
					min2_open1 = timestr2_open1[1];
						
					
					//open2
				
					
					var date1_open2=moment("20210501", "YYYYMMDD").add(hr2_open1,'hours').add(min2_open1, 'minutes');
					var date2_open2=moment("20210501", "YYYYMMDD").add(hr2_open1,'hours').add(min2_open1, 'minutes').add(1,'minutes');
					var date3_open2=moment("202105012359", "YYYYMMDDHHmm");
					
					$('#open2').bootstrapMaterialDatePicker({
						lang : 'zh-tw',
						format : 'HH:mm',
						date : false,
// 						currentDate : date1_open2,
						minDate : date2_open2,
						maxDate : date3_open2,
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

						var timestr2_open2 = $('#close2')[0].value.split(":", 2);
						hr2_open2 = timestr2_open2[0];
						min2_open2 = timestr2_open2[1];

					
					
						//open3
					
						
						var date1_open3=moment("20210501", "YYYYMMDD").add(hr2_open2,'hours').add(min2_open2, 'minutes');
						var date2_open3=moment("20210501", "YYYYMMDD").add(hr2_open2,'hours').add(min2_open2, 'minutes').add(1,'minutes');
						var date3_open3=moment("202105012359", "YYYYMMDDHHmm");
						
						$('#open3').bootstrapMaterialDatePicker({
							lang : 'zh-tw',
							format : 'HH:mm',
							date : false,
// 							currentDate : date1_open3,
							minDate : date2_open3,
							maxDate : date3_open3,
							cancelText : '回上一步',
							switchOnClick : true,
							shortTime : true

						});
					
						$('#open3').change(
								function() {

									// 					console.log($('#lang1')[0].value);

									var timestr1_open3 = $('#open3')[0].value.split(":", 2);
									hr1_open3 = timestr1_open3[0];
									min1_open3 = timestr1_open3[1];

									var date1 = moment("20210501", "YYYYMMDD").add(hr1_open3,
											'hours').add(min1_open3, 'minutes');
									var date2 = moment("20210501", "YYYYMMDD").add(hr1_open3,
											'hours').add(min1_open3, 'minutes').add(1,
											'minutes');
									var date3 = moment("202105012359", "YYYYMMDDHHmm");

									$('#close3').bootstrapMaterialDatePicker({
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

						$('#close3').change(function() {

							// 					console.log($('#lang2')[0].value);

							var timestr2_open3 = $('#close3')[0].value.split(":", 2);
							hr2_open3 = timestr2_open3[0];
							min2_open3 = timestr2_open3[1];

						});
						
					});
					

					
				});
				
				//確認日期正確
			
				
			
			$('#checktime').click(function() {

					//都沒填時間
					if (hr1_open1 == null & hr2_open1 == null) {
								
						alert('至少填一段時間');
						day1checktime = "false";
				

					} 
					//只填一段時間
					else if (hr1_open1 != null & hr2_open1 != null & hr1_open2==undefined & hr2_open2==null){
						console.log('檢查1段時間');
						var time1=(parseInt(hr1_open1)*60 +parseInt(min1_open1));
						var time2=(parseInt(hr2_open1)*60 +parseInt(min2_open1));

			
						if(time2>time1){
							day1checktime = "true";
							console.log('檢查結果:時間正確');
							
						}
						else{
							day1checktime = "false";
							console.log('檢查結果:時間錯誤');
						}
						
					}
					
					//填兩段時間
					else if (hr1_open1 != null & hr2_open1 != null & hr1_open2!=null & hr2_open2!=null & hr1_open3==undefined & hr2_open3==null){
						console.log('檢查2段時間');
						var time1=(parseInt(hr1_open1)*60 +parseInt(min1_open1));
						var time2=(parseInt(hr2_open1)*60 +parseInt(min2_open1));
						var time3=(parseInt(hr1_open2)*60 +parseInt(min1_open2));
						var time4=(parseInt(hr2_open2)*60 +parseInt(min2_open2));
						
						if(time4>time3 & time3>time2 & time2>time1){
							day1checktime = "true";
							console.log('檢查結果:時間正確');
							
						}
						else{
							day1checktime = "false";
							console.log('檢查結果:時間錯誤');
						}
					}
					
				
					//填三段時間
					else if (hr1_open1 != null & hr2_open1 != null & hr1_open2!=null & hr2_open2!=null & hr1_open3!=null & hr2_open3!=null){
						
						console.log('檢查3段時間');
						var time1=(parseInt(hr1_open1)*60 +parseInt(min1_open1));
						var time2=(parseInt(hr2_open1)*60 +parseInt(min2_open1));
						var time3=(parseInt(hr1_open2)*60 +parseInt(min1_open2));
						var time4=(parseInt(hr2_open2)*60 +parseInt(min2_open2));
						var time5=(parseInt(hr1_open3)*60 +parseInt(min1_open3));
						var time6=(parseInt(hr2_open3)*60 +parseInt(min2_open3));
						if(time6>time5 & time5>time4 & time4>time3 & time3>time2 & time2>time1){
							day1checktime = "true";
							console.log('檢查結果:時間正確');
							
						}
						else{
							day1checktime = "false";
							console.log('檢查結果:時間錯誤');
						}
					}
					else {
						
						
						alert('請檢查時間');
						day1checktime = "false";
						
						
					}

				});
				
			})
		</script>
	</div><!-- monday Open -->
		<input type="button" id="checkBeforeSubmit" value="設定" />
		<script>
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
		
		
		
		
//			送給controller 要改成ajax
		var day1checktime=false;
		$("#checkBeforeSubmit").click(function() {
			//有營業+時間OK 可送出
			if ($('#MondayOpenRadio').prop("checked")==true & day1checktime == "true"  ) {
// 				console.log('設定成功'); 
// 				console.log('day1checktime='+day1checktime); 
// 				console.log('inputValue='+$('#open1')[0].value); 

				var data = $("#MondayOpenform").serializeArray();
				console.log(data);
				var urls = "${pageContext.request.contextPath}/";
				urls += "<c:url value='Hours/'/>" +${restaurantBusinHour.restaurant.restaurantId};
// 				urls +="/"+data[0][0].value;
					
				
			
// 				console.log(data[0][0].value);
				$.ajax({
					type : "POST",
					url : urls,
					contentType:'application/json; charset=UTF-8',
					data:data,
					dataType : "text",
					success : function(response) {
						var ShowMondayTimeResult = document
								.getElementById("ShowMondayTimeResult");
// 						console.log(ShowMondayTimeResult);
// 						console.log(${restaurantBusinHour.restaurant.restaurantId});
// 						var jsontxt = JSON.parse(response);
						console.log(response);

						
						ShowMondayTimeResult.innerHTML=response;
				
					
					},
					error : function(thrownError) {
						console.log(thrownError);
					}

				});
				
			//不營業
			} else if($('#MondayCloseRadio').prop("checked")==true){
				console.log('星期一不營業')
				$("#MondayCloseform").submit();
			}else{
				alert('設定失敗')
				console.log('day1checktime='+day1checktime);
				console.log('inputValue='+$('#open1')[0].value); 
				return false;
			}
				
			

		});

		
		
		</script>	

	</div> <!-- monday -->
	<div id="ShowMondayTimeResult">
	
	設定結果
	</div>



</body>
</html>