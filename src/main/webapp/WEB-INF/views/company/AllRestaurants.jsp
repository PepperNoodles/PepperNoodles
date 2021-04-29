<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<script type='text/javascript' src="<c:url value='/scripts/jquery-1.9.1.min.js' />"></script>
<meta charset="UTF-8">
<title>restaurantCRUD</title>
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
<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<!-- bloodHound ↓-->
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.11.1/typeahead.bundle.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.css">

<!-- bloodHound ↑-->
<style>
.header {
	background-color: #000000;
}

.rest-infobox {
	float: left;
	width: 30%;
}

.rest-picbox {
	float: right;
	margin: auto;
	width: 60%;
	border: 3px solid #73AD21;
	padding: 10px 10px 10px 10px;
	border: 3px solid #73AD21;
	padding: 10px 10px 10px 10px;
	height: 50%;
}

#restaurantPicturePreview {
	width: 100%;
	heght: 100%;
	object-fit: contain;
}

footer {
	clear: both;
	/* 	清除上面float影響 */
}

</style>

</head>
<body>



	<!-- 餐廳管理頁面 -->
	<!-- 背景圖片+自動填滿 -->
	<div class="image-container set-full-height"
		style="background-image: url(<c:url value="/images/restaurantCRUD/background_1.jpg"/>) ;background-size:cover">
		<div align='center'>
			<h3 style="color: red">所有餐廳</h3>
			<h3 style="color: red">目前身分${comDetail.userAccount.accountIndex}</h3>
			<a href="<c:url value='/Company/company'/> ">回企業首頁</a>
			<a href="<c:url value='/addrest'/> ">新增餐廳</a>
			<hr>
			<c:choose>
				<c:when test="${empty restaurants}">
					<h5 style="color: #FF1493">沒有任何會員資料</h5>
					<br>
				</c:when>
				<c:otherwise>
				<table border='1' cellpadding="3" cellspacing="1" >
							<tr>
			   <th width='80'>餐廳名稱</th>
			   <th width='120'>餐廳地址</th>
			   <th width='80'>聯絡方式</th>
			   <th width='80'>餐廳網站</th>
			   <th width='80'>標籤</th>
			   <th width='80'>環境照片</th>
			   <th colspan='1' width='36'>資料維護</th>
			</tr>
						<c:forEach var='restaurant' items='${restaurants}'>
							<tr>
								<td style="text-align: center;font-weight: bold">${restaurant.restaurantName}</td>
								<td style="text-align: center;font-weight: bold">${restaurant.restaurantAddress}</td>
								<td style="text-align: center;font-weight: bold">${restaurant.restaurantContact}</td>
								<td style="text-align: center;font-weight: bold">${restaurant.restaurantWebsite}</td>
								<td style="text-align: center;font-weight: bold">
<%-- 								<input id="${restaurant.restaurantId}" name="restid" class="typeahead"   disabled > --%>
								<div id="${restaurant.restaurantId}" name="restid"></div></td>
								<td><img width='120' height='120'
									src='${pageContext.request.contextPath}/restpicture/${restaurant.restaurantId}' id='restpicture' /></td>
<!-- 									<td style="font-weight: bold"> -->
<%-- 									<a class='manulink' href="${pageContext.request.contextPath}/rest/menu/${restaurant.restaurantId}">新增菜單</a><br> --%>
<%-- 									<a class='manulink' href="${pageContext.request.contextPath}/event/${restaurant.restaurantId}">新增活動</a> --%>
<!-- 									</td> -->
								<td style="font-weight: bold"><a class='updatelink' href="${pageContext.request.contextPath}/updateRest/${restaurant.restaurantId}">編輯</a><br>
								<a class='deletelink' href="${pageContext.request.contextPath}/deleteRest/${restaurant.restaurantId}">刪除</a></td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
			<hr>
		</div>

	</div>
	
<form id='deleteform' method='POST'>
	<input type='hidden' name='_method' value='DELETE'>
</form>



	<!-- JS here -->
<script>
    $(document).ready(function() {
        $('.deletelink').click(function() {
        	if (confirm('確定刪除此筆紀錄? ')) {
        		var href = $(this).attr('href');
                $("#deleteform").attr('action', href).submit();
        	} 
        	return false;
            
        });
		
		//抓餐廳tag
        let n = $("div[name='restid']");
//         console.log($("input[name='restid']"));
//         console.log(n.length);
//         console.log(n[0].id);
        
        for(let i=0;i<n.length;i++){
    	var urls="${pageContext.request.contextPath}/";
		urls+="<c:url value='restTag2/'/>"+n[i].id;										
// 		console.log(urls);
		
		$.ajax({
			type: "GET",
			url: urls,				
			dataType: "text",
			success: function (response) {
				var divFoodTag = document.getElementById(n[i].id);

				var jsontxt=JSON.parse(response);
// 				console.log(response);
// 				console.log(jsontxt);
// 				console.log(jsontxt.length);
				
				for(i=0;i<jsontxt.length;i++){
				$(divFoodTag).append(jsontxt[i].foodTagName+'<br>');
				giveValue(jsontxt[i].foodTagIid,jsontxt[i].foodTagName);
				}
			},
			error: function (thrownError) {
				console.log(thrownError);
			}
		
		
		});
        }
    	// bloodhound
		var foodTags = new Bloodhound({
			datumTokenizer : Bloodhound.tokenizers.obj.whitespace('text'),
			queryTokenizer : Bloodhound.tokenizers.whitespace,
			prefetch : 'http://localhost:433/PepperNoodles/data/FoodTag.json',
			cache : false
		});

		foodTags.initialize();
		
		var elt = $('.typeahead');
		elt.tagsinput({
			itemValue : 'value',
			itemText : 'text',
			
			typeaheadjs : {
				limit: 20,
				name : 'foodTags',
				displayKey : 'text',
				source : foodTags.ttAdapter()
			}
		});

		function giveValue(id,name){
			elt.tagsinput('add', {
				"value" : id,
				"text":name
			});
        }
        
        
        
    })
    
    
</script>
	<!--預覽照片 -->
	<script>
		$(function() {
			$("#restaurant-picture").change(
					function() {
						if (this.files && this.files[0]) {
							var reader = new FileReader();

							reader.onload = function(e) {
								$('#restaurantPicturePreview').attr('src',
										e.target.result);
							}

							reader.readAsDataURL(this.files[0]);
						}
					});
		});
	</script>


</body>
</html>