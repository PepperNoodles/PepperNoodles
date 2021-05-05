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
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script> -->
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.min.js"></script> -->
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-datetimepicker/2.7.1/js/bootstrap-material-datetimepicker.min.js"></script> -->
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/locale/ja.js"></script> -->
<!-- <script type="text/javascript" -->
<!-- 	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/locale/zh-tw.min.js"></script> -->
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
	<%@include file="../includePage/includeNav.jsp"%>
	<div class="container-fluid" >
		<div class="row">
			<!-- 左邊的Bar -->
			<div class="col-lg-2 nopadding" id=leftBar>
				<br>

				<div class="list-group">
					<%@include file="left.jsp"%>
				</div>
			</div>
			<!-- 右邊顯示的資料 背景圖片+自動填滿-->
			<div class="col-lg-10 nopadding "style="background-image: url(<c:url value="/images/restaurantCRUD/restaurant-opening-hours.png"/>);background-size:cover;">

			
				
						<br><br>
						<h1 style="color:black">${restaurantBusinHour.restaurant.restaurantName}</h1>
						<div id="Monday" class="col-sm-7" style="float:left">
							<br><br> <span> <select id="MondaySelect">
									<option value="X" selected>請選擇日期</option>
									<option value="0">星期一</option>
									<option value="1">星期二</option>
									<option value="2">星期三</option>
									<option value="3">星期四</option>
									<option value="4">星期五</option>
									<option value="5">星期六</option>
									<option value="6">星期日</option>
							</select><br><br><h2 style="color:black"><input type="radio" name="Day1" value="1" id="MondayOpenRadio" checked="true" />營業 <input type="radio" name="Day1" value="1"
								id="MondayCloseRadio" />不營業 <br> <br></h2>
							</span>
							<form:form id="MondayCloseform" method="POST"
								modelAttribute="restaurantBusinHour"
								enctype='multipart/form-data'>
								<form:input path="day" type="text" value="" id="day1close" 
									style="display:none" />
							</form:form>
							<div id="MondayOpen" style="">
								<form:form id="MondayOpenform" method="POST"
									modelAttribute="restaurantBusinHour"
									enctype='multipart/form-data'>
									<form:input path="day" type="text" value="" id="day1open"
										style="display:none" />
				<h2 style="color:black"> 時段一:<form:input path="openTime" type="text" id="open1" size="5" class="checkinput"/> to 
				<form:input path="closeTime" type="text" id="close1" class="checkinput" size="5" />
										<br> <br> <br> 時段二:<form:input path="openTime2nd" type="text" id="open2" size="5" class="checkinput"/>
										to
										<form:input path="closeTime2nd" type="text" id="close2"
											size="5" class="checkinput"/>
										<br> <br> <br> 時段三:<form:input path="openTime3rd" type="text" id="open3" size="5" class="checkinput"/>
										to
										<form:input path="closeTime3rd" type="text" id="close3"
											size="5" class="checkinput"/>
									</h2>
</form:form>
<br> <br> 
<input type="button" id="checktime"value="鎖定時間" /> 
<input type="button" id="flashinput"value="一鍵新增" /> 
<br> <br>


<script>
									

</script>
</div><!-- monday Open -->
			<input type="button" id="checkBeforeSubmit" value="設定進資料庫" />
		<script>
								// 		hide or show Open div
				$(document).ready(function () {	
					var day1checktime = false;
									
									$(function() {

										//open1
										var hr1_open1 = null;
										var min1_open1 = null;
										var hr2_open1 = null;
										var min2_open1 = null;

										var date1_open1 = null;
										var date2_open1 = null;
										var date3_open1 = null;
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
													date1_open1 = moment("20210501", "YYYYMMDD").add(hr1_open1,'hours').add(min1_open1, 'minutes');
													date2_open1 = moment("20210501", "YYYYMMDD").add(hr1_open1,'hours').add(min1_open1, 'minutes').add(1,'minutes');
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

															var date1_open2 = moment("20210501", "YYYYMMDD").add(hr2_open1, 'hours').add(min2_open1,'minutes');
															var date2_open2 = moment("20210501", "YYYYMMDD").add(hr2_open1, 'hours').add(min2_open1,'minutes').add(1, 'minutes');
															var date3_open2 = moment("202105012359","YYYYMMDDHHmm");

															$('#open2').bootstrapMaterialDatePicker({
																lang : 'zh-tw',
																format : 'HH:mm',
																date : false,
																// currentDate : date1_open2,
																minDate : date2_open2,
																maxDate : date3_open2,
																cancelText : '回上一步',
																switchOnClick : true,
																shortTime : true
															});

											$('#open2').change(function() {

													// console.log($('#lang1')[0].value);

													var timestr1_open2 = $('#open2')[0].value.split(":", 2);
													hr1_open2 = timestr1_open2[0];
													min1_open2 = timestr1_open2[1];

													var date1 = moment("20210501","YYYYMMDD").add(hr1_open2, 'hours').add(min1_open2,'minutes');
													var date2 = moment("20210501","YYYYMMDD").add(hr1_open2, 'hours').add(min1_open2,'minutes').add(1, 'minutes');
													var date3 = moment("202105012359","YYYYMMDDHHmm");

													$('#close2').bootstrapMaterialDatePicker(
																								{
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
														var timestr2_open2 = $('#close2')[0].value.split(":", 2);
														hr2_open2 = timestr2_open2[0];
														min2_open2 = timestr2_open2[1];
														var date1_open3 = moment("20210501", "YYYYMMDD").add(hr2_open2, 'hours').add(min2_open2,'minutes');
														var date2_open3 = moment("20210501", "YYYYMMDD").add(hr2_open2, 'hours').add(min2_open2,'minutes').add(1, 'minutes');
														var date3_open3 = moment("202105012359","YYYYMMDDHHmm");

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

														$('#open3').change(function() {	var timestr1_open3 = $('#open3')[0].value.split(":",2);
																	hr1_open3 = timestr1_open3[0];
																	min1_open3 = timestr1_open3[1];
																	var date1 = moment("20210501","YYYYMMDD").add(hr1_open3,'hours').add(min1_open3,'minutes');
																	var date2 = moment("20210501","YYYYMMDD").add(hr1_open3,'hours').add(min1_open3,'minutes').add(1,'minutes');
																	var date3 = moment("202105012359","YYYYMMDDHHmm");
									
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
																	var timestr2_open3 = $('#close3')[0].value.split(":",2);
																	hr2_open3 = timestr2_open3[0];
																	min2_open3 = timestr2_open3[1];
																	
														});

												});

										});

										//確認日期正確

										$('#checktime').click(function() {

															//都沒填時間
															if (hr1_open1 == null & hr2_open1 == null) {

																$(".checkinput").css({
																	"border" : "2px solid red"
																});
																day1checktime = "false";

																alert('至少填一段時間');
															}
															//只填一段時間
															else if (hr1_open1 != null & hr2_open1 != null& hr1_open2 == undefined& hr2_open2 == null) {
																console.log('檢查1段時間');
																var time1 = (parseInt(hr1_open1) * 60 + parseInt(min1_open1));
																var time2 = (parseInt(hr2_open1) * 60 + parseInt(min2_open1));

																if (time2 > time1) {
																	day1checktime = "true";
																	console.log('檢查結果:時間正確');
																	$(".checkinput").css({"border" : "2px solid green","background-color" : "#BBFFBB"});

																} else {
																	day1checktime = "false";
																	console.log('檢查結果:時間錯誤');
																	$(".checkinput").css({"border" : "2px solid red"});
																}

															}

															//填兩段時間
															else if (hr1_open1 != null & hr2_open1 != null
																	& hr1_open2 != null & hr2_open2 != null
																	& hr1_open3 == undefined
																	& hr2_open3 == null) {
																console.log('檢查2段時間');
																var time1 = (parseInt(hr1_open1) * 60 + parseInt(min1_open1));
																var time2 = (parseInt(hr2_open1) * 60 + parseInt(min2_open1));
																var time3 = (parseInt(hr1_open2) * 60 + parseInt(min1_open2));
																var time4 = (parseInt(hr2_open2) * 60 + parseInt(min2_open2));

																if (time4 > time3 & time3 > time2
																		& time2 > time1) {
																	day1checktime = "true";
																	console.log('檢查結果:時間正確');
																	$(".checkinput").css({"border" : "2px solid green","background-color" : "#BBFFBB"});

																} else {
																	day1checktime = "false";
																	console.log('檢查結果:時間錯誤');
																	$(".checkinput").css({"border" : "2px solid red"});
																}
															}

															//填三段時間
															else if (hr1_open1 != null & hr2_open1 != null
																	& hr1_open2 != null & hr2_open2 != null
																	& hr1_open3 != null & hr2_open3 != null) {

																console.log('檢查3段時間');
																var time1 = (parseInt(hr1_open1) * 60 + parseInt(min1_open1));
																var time2 = (parseInt(hr2_open1) * 60 + parseInt(min2_open1));
																var time3 = (parseInt(hr1_open2) * 60 + parseInt(min1_open2));
																var time4 = (parseInt(hr2_open2) * 60 + parseInt(min2_open2));
																var time5 = (parseInt(hr1_open3) * 60 + parseInt(min1_open3));
																var time6 = (parseInt(hr2_open3) * 60 + parseInt(min2_open3));
																if (time6 > time5 & time5 > time4
																		& time4 > time3 & time3 > time2
																		& time2 > time1) {
																	day1checktime = "true";
																	console.log('檢查結果:時間正確');
																	$(".checkinput").css({
																		"border" : "2px solid green",
																		"background-color" : "#BBFFBB"
																	});

																} else {
																	day1checktime = "false";
																	console.log('檢查結果:時間錯誤');
																	$(".checkinput").css({
																		"border" : "2px solid red"
																	});
																}
															} else {

																day1checktime = "false";
																$(".checkinput").css({
																	"border" : "2px solid red"
																});
																alert('請檢查時間');

															}

														});

										//一鍵新增
										$('#flashinput').click(function() {
											
											$('#open1').val('07:30');
											$('#close1').val('11:30');
											
											$('#open2').val('13:30');
											$('#close2').val('17:30');

											
											$(".checkinput").css({
												"border" : "2px solid green",
												"background-color" : "#BBFFBB"
											});
											day1checktime='true';

										});
									})	
									
									
									
									
								//以下	
										var MondayOpen = $('#MondayOpen');
										$("#Monday").click(function() {
												if ($('#MondayOpenRadio').prop("checked") == true) {
													MondayOpen.removeClass("tohide");
												} else if ($('#MondayOpenRadio').prop("checked") == false) {
													MondayOpen.addClass("tohide");
													}
												});
										$("#MondaySelect").change(function() {
											var day = this.value;
											// 				  console.log("選擇了星期"+day);
											$('#day1open').val(day);
											$('#day1close').val(day);
										});
										
									
									
									
									
									$("#checkBeforeSubmit").click(function() {
														//判斷是否選擇哪一天
														if ($(":selected")[0].value != "X") {
															console.log('有選擇日期');
															console.log('checkBeforeSubmit day1checktime '+day1checktime);
															//有無營業+時間OK 可送出
															if ($('#MondayOpenRadio').prop("checked") == true& day1checktime == "true") {

																var data = $("#MondayOpenform").serializeArray();

																var urls = "${pageContext.request.contextPath}/";
																urls += "<c:url value='Hours/'/>"+ ${restaurantBusinHour.restaurant.restaurantId};

																$.ajax({
																			type : "POST",
																			url : urls,
																			//contentType:'application/json; charset=UTF-8', //不可以唷! controller會接不到
																			data : data,
																			dataType : "text",
																			success : function(response) {
																				var ShowMondayTimeResult = document.getElementById("ShowMondayTimeResult");
																				var result = JSON.parse(response);

																		var weekday = ['一','二','三','四','五','六','日'];

																		txt = '<table >';
																		for (i = 0; i < result.length; i++) {
																			txt += '<tr><td><h2 style="color:red">星期'
																					+ weekday[parseInt(result[i].day)]
																					+ '</h2></td>';
																			if (result[i].openTime == null) {
																				txt += '<td><h2 style="color:red">不營業</h2></td></tr>';
																			} else {
																				txt += '<td><h2 style="color:red">'
																						+ result[i].openTime
																						+ '~'
																						+ result[i].closeTime
																						+ '</h2></td></tr>';

																			}

																			if (result[i].openTime2nd != null
																					&& result[i].openTime2nd.length != 0) {
																				txt += '<td></td><td><h2 style="color:red">'
																						+ result[i].openTime2nd
																						+ '~'
																						+ result[i].closeTime2nd
																						+ '</h2></td></tr>';
																			}
																			if (result[i].openTime3rd != null
																					&& result[i].openTime3rd.length != 0) {
																				txt += '<td></td><td><h2 style="color:red">'+ result[i].openTime3rd
																						+ '~'+ result[i].closeTime3rd+ '</h2></td></tr>';
																			}

																		}

																		txt += "</table>";
																		ShowMondayTimeResult.innerHTML = txt;},
																		error : function(thrownError) {
																				console.log(thrownError);
																			}

																		});

																//不營業
															} else if ($('#MondayCloseRadio').prop("checked") == true) {

																var data = $("#MondayCloseform").serializeArray();

																var urls = "${pageContext.request.contextPath}/";
																urls += "<c:url value='Hours/'/>"+ ${restaurantBusinHour.restaurant.restaurantId};

																$.ajax({
																			type : "POST",
																			url : urls,
																			//contentType:'application/json; charset=UTF-8', //不可以唷! controller會接不到
																			data : data,
																			dataType : "text",
																			success : function(response) {
																				var ShowMondayTimeResult = document.getElementById("ShowMondayTimeResult");

																				var result = JSON.parse(response);

																				var weekday = ['一','二','三','四','五','六','日'];

																				txt = '<table >';
																				for (i = 0; i < result.length; i++) {
																					txt += '<tr><td><h2 style="color:red">星期'
																							+ weekday[parseInt(result[i].day)]
																							+ '</h2></td>';
																					if (result[i].openTime == null) {
																						txt += '<td><h2 style="color:red">不營業</h2></td></tr>';
																					} else {
																						txt += '<td><h2 style="color:red">'
																								+ result[i].openTime
																								+ '~'
																								+ result[i].closeTime
																								+ '</h2></td></tr>';

																					}

																					if (result[i].openTime2nd != null
																							&& result[i].openTime2nd.length != 0) {
																						txt += '<td></td><td><h2 style="color:red">'
																								+ result[i].openTime2nd
																								+ '~'
																								+ result[i].closeTime2nd
																								+ '</h2></td></tr>';
																					}
																					if (result[i].openTime3rd != null
																							&& result[i].openTime3rd.length != 0) {
																						txt += '<td></td><td><h2 style="color:red">'+ result[i].openTime3rd
																								+ '~'+ result[i].closeTime3rd+ '</h2></td></tr>';
																					}

																				}

																				txt += "</table>";
																				ShowMondayTimeResult.innerHTML = txt;
																			},
																			error : function(thrownError) {
																				console.log(thrownError);
																			}

																		});
															} else {
																alert('設定失敗')
																console.log('day1checktime='+ day1checktime);
																console.log('inputValue='+ $('#open1')[0].value);
																return false;
															}

														} else {

															alert('請選擇日子');
														}

													});
									
									
								});
								
								
								//			送給controller 要改成ajax
								
							</script>
								
						
						</div>
						
						
					<div id="showresult" class="col-md-5 col-md-offset-1" style="float:right">
							<div id="ShowMondayTimeResult"><h2 style="color:black">設定結果</h2></div>
						</div>







			
			

			</div>
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
		</div>
	</div>
	
			
	<!-- footer -->
	<%@include file="../includePage/includeFooter.jsp"%>

	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>


	<!-- JS here -->

	<!-- Scroll Up -->
	<script>
		$(window).on('load', function() {

			// 			//讓bar固定在上面以及設定高度
			$(".header-sticky").addClass("sticky-bar");
			$(".header-sticky").css("height", "90px");
			$(".header-sticky").css("position", "static")

			//讓loading圖動起來
			$('#preloader-active').delay(450).fadeOut('slow');
			$('body').delay(450).css({
				'overflow' : 'visible'
			});

		});
		

		
		
	</script>
</body>
</html>