<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyRestaurants</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">


<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />
<link rel="stylesheet"
	href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/style.css' />">

<style>
.nopadding {
	padding: 0px ;
	margin: 0px ;
}

td{
 padding:5px ;
}


table{
cellpadding:8px;
}

#showtimelist{

width:120px;
float:left;
}

#settingtime{
width:auto;
/* border:2px green solid; */
float:left;
padding:8px 16px;
}

#resthead{

padding:16px 16px;

}
 .figure1 {
          
            width: 350px;
            height: 400px;
            border: solid 1px blue;
            border-radius: 15px;
            margin-top: 10px;
            margin-left: 10px;
            float: left;
        }
 img {
            margin-left: 20px;
            margin-top: 20px;

        }
  .restcard{
      background-color: white(0, 255, 64);
            border: solid 5px white;
 
 }     
 .restinfo{
    width: 480px;
            height: 400px;
            border: solid 1px blue;
            border-radius: 15px;
            margin-top: 10px;
            margin-left: 10px;
            float: left;
 }  
 
.modifydiv1{

margin-left: 30px;
 }  
.modifydiv2{
float: right;
padding-right: 40px;
 }  
 
div a:hover { color: blue; }

</style>

</head>
<body>
	<%@include file="../includePage/includeNavforAllrest.jsp"%>

	<div class="container-fluid">
		<div class="row">
			<!-- 左邊的Bar -->
			<div class="col-lg-2 nopadding" id=leftBar>
				<br>

				<div class="list-group">
					<%@include file="left.jsp"%>
				</div>
			</div>
			<!-- 右邊顯示的資料 背景圖片+自動填滿-->
			<div class="col-lg-10 nopadding image-container set-full-height" style="background-image: url(<c:url value="/images/restaurantCRUD/restaurant-opening-hours.png"/>);background-size:cover;">
				<div class="container">
					<div class="row d-flex align-items-center justify-content-center">
			
					
<!-- 餐廳管理頁面 -->
<div class="container">
	<br>
		<h3 style="color: red">
			 <a href="<c:url value='/addrest'/>"><i class="fas fa-registered"></i> 建立餐廳</a>
		</h3>
		<div class="row ml-3">
		<c:choose>
			<c:when test="${empty restaurants}">
				<h5 style="color: #FF1493">沒有任何餐廳資料</h5>
				<br>
			</c:when>
			<c:otherwise>
		<c:forEach var='restaurant' items='${restaurants}'>
			<div  class="page  pl-3 ml-3 col-10 restcard">
					<br>
				
		<figure class="figure1">
		<div id="resthead"><h2> ${restaurant.restaurantName}</h2></div>	
           <span  style="padding:16px 16px;color:green;" id="${restaurant.restaurantId}" name="restid"></span><br>
            <img width='200' height='200'
						src='${pageContext.request.contextPath}/restpicture/${restaurant.restaurantId}'
						id='restpicture' />
            <figcaption style="text-align:left;margin: 20px;clear:both;">
          <div id ="timezone">


	<div id="showtimelist">
		<ul class="nav nav-tabs " >
		        <li class="nav-item dropdown" >
		            <span  class="nav-link dropdown-toggle .nopadding" data-bs-toggle="dropdown" href="#" role="button"aria-expanded="false">營業時間</span>	
		            <ul class="dropdown-menu">
					<li><div id="getrestHour${restaurant.restaurantId}" name="restHour" >待新增</div></li>
		            </ul>
		        </li>
		</ul>
	</div>
	<div id="settingtime"><a class='updatelink' style="color: black" onMouseOver="this.style.color='blue'"
   onMouseOut="this.style.color='black'" href="${pageContext.request.contextPath}/Hours/${restaurant.restaurantId}"><i class="fas fa-wrench"></i> 設定營業時間</a></div>
</div>
            </figcaption>
        </figure>


			
						
														
<!-- 		營業時間測試 -->


    <script>
  
        
    var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'))
    var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
            return new bootstrap.Dropdown(dropdownToggleEl)
        })

											//抓餐廳營業時間

												var urls = "${pageContext.request.contextPath}/";
												urls += "<c:url value='getHours/'/>" +${restaurant.restaurantId};
// 														console.log(urls);

												$.ajax({
													type : "GET",
													url : urls,
													dataType : "text",
													success : function(response) {
														var divrestHour = document
																.getElementById("getrestHour"+${restaurant.restaurantId});

														var result = JSON.parse(response);
												
														var weekday = [
															'一',
															'二',
															'三',
															'四',
															'五',
															'六',
															'日' ];

													txt = '<table id="tablemenu" align="center" ><h6>';
													for (j = 0; j < result.length; j++) {
														txt += '<tr><td>星期'
																+ weekday[parseInt(result[j].day)]
																+ '</td>';
														if (result[j].openTime == null) {
															txt += '<td>不營業</td></tr>';
														} else {
															txt += '<td>'
																	+ result[j].openTime
																	+ '~'
																	+ result[j].closeTime
																	+ '</td></tr>';

														}

														if (result[j].openTime2nd != null
																&& result[j].openTime2nd.length != 0) {
															txt += '<td></td><td>'
																	+ result[j].openTime2nd
																	+ '~'
																	+ result[j].closeTime2nd
																	+ '</td></tr>';
														}
														if (result[j].openTime3rd != null
																&& result[j].openTime3rd.length != 0) {
															txt += '<td></td><td>'+ result[j].openTime3rd
																	+ '~'+ result[j].closeTime3rd+ '</td></tr>';
														}

													}

													txt += "</h6></table>";
													divrestHour.innerHTML = txt;
													},
													error : function(thrownError) {
														console.log(thrownError);
													}

												});
											
											
											</script>



	<!-- 		營業時間測試 -->	
						<div class="restinfo">
						
								<br>
								<div class="modifydiv1">
								<h2>  餐廳地址: </h2><h5>${restaurant.restaurantAddress}</h5><br>
								<h2>  聯絡方式: </h2><h5>${restaurant.restaurantContact}</h5><br>
								<h2>  餐廳網站: </h2><h5><a href="<c:url value='${restaurant.restaurantWebsite}' />"><i class="fas fa-link"></i> ${restaurant.restaurantWebsite}</a></h5><br>
								</div>
								<div class="modifydiv2">
								<h3>
								
								<a class='updatelink'href="${pageContext.request.contextPath}/updateRest/${restaurant.restaurantId}"><i class="fas fa-edit"></i></a>
								<a class='deletelink' href="${pageContext.request.contextPath}/deleteRest/${restaurant.restaurantId}"><i class="fas fa-trash-alt"></i></a></h3>
								</div>
								
						
						</div>
								

					<br>				
			</div>
									
										
									
									
									
									
										</c:forEach>
								</c:otherwise>
							</c:choose>
							</div>
						</div>
						<!-- ↑餐廳管理頁面 -->
					</div>
				</div>

			</div>
		</div>
	</div>
	<form id='deleteform' method='POST'>
		<input type='hidden' name='_method' value='DELETE'>
	</form>
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
		
		$("#restHourMenu").click(function(){
			$("#tablemenu").slideUp("slow");
			$("#tablemenu").slideToggle("slow");
		});
	</script>
	<script>
	<!-- 刪除確認+tag抓取 -->
		$(document)
				.ready(
						function() {
							$('.deletelink').click(
									function() {
										if (confirm('確定刪除此筆紀錄? ')) {
											var href = $(this).attr('href');
											$("#deleteform").attr('action',
													href).submit();
										}
										return false;

									});

							//抓餐廳tag
							let n = $("span[name='restid']");
							//         console.log($("input[name='restid']"));
							//         console.log(n.length);
							//         console.log(n[0].id);

							for (let i = 0; i < n.length; i++) {
								var urls = "${pageContext.request.contextPath}/";
								urls += "<c:url value='restTag2/'/>" + n[i].id;
								// 		console.log(urls);

								$.ajax({
									type : "GET",
									url : urls,
									dataType : "text",
									success : function(response) {
										var divFoodTag = document
												.getElementById(n[i].id);

										var jsontxt = JSON.parse(response);
										// 				console.log(response);
										// 				console.log(jsontxt);
										
									
											
											$(divFoodTag).append('Tags: ')
											for (i = 0; i < jsontxt.length; i++) {
											
													$(divFoodTag).append("<i class='fas fa-tag'></i>"+jsontxt[i].foodTagName+" ");
											
								
							
											}
						
									},
									error : function(thrownError) {
										console.log(thrownError);
									}

								});
							}
				
							
						
							

						})
	</script>
	
</body>
</html>