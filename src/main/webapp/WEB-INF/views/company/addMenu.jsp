<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>最新活動</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />
<script>
	
$(document).ready(function(){
	//預覽圖片
	$("#menuPicture").change(function() {
		if (this.files && this.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#PicturePreview').attr('src',e.target.result);
			}
			reader.readAsDataURL(this.files[0]);
			$("#addMenu").removeClass("tohide");
			$("#addMenu").addClass("toshow");
		}
	});
	
	$("#addMenu").click(function(){
		var restaurantId = 1;
		data = new FormData();
    	data.append('file', $('#menuPicture')[0].files[0]);
		data.append('restInfo',new Blob([JSON.stringify( {"restaurantId": restaurantId} )],{type: "application/json"}));
		data.append('restInfo', restaurantId);

    	$.ajax({
			method:"POST",
			url:"/PepperNoodles/rest/addMenu",
			data:data,
			processData: false,
			contentType: false, 
			cache: false,  //不做快取
	        async : true,
	        success: function (result) {
				alert("新增成功");
	            $("#menuList").text(result);//填入提示訊息到result標籤內
	            console.log(result);
	            location.href="http://localhost:9090/PepperNoodles"+result;
	        },
	        error: function (result) {
	            $("#menuList").text(result.fail); //填入提示訊息到result標籤內
	        }
		})
	});
	
	//刪除時出現確認訊息
    $('.delete').click(function() {
    	if (confirm('確定刪除此筆紀錄? ')) {
    		var href = $(this).attr('href');
//     		alert(href);
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
#picture{
 	border-style:dashed;
	cursor: pointer;
}
.top{
/* 	margin-top:30px;  */
	height: 500px;
}
.toshow{
	display: block;
}
.tohide{
	display: none;
}
.infobox {
	float: left;
/* 	width: 10%; */
 	margin: 30px 0% 0% 20%; 
}
.picbox {
	float: right;
	margin: 0% 40% 0% 0%; 
}

.menuList{
	text-align: center;
}
.picbox{
 	margin-top:30px;  
}
.delete{
	color: #842B00;
}
td a:hover{
	color:	#F75000;
}
</style>
</head>
<body>
 	<%@include file="../includePage/includeNav.jsp" %>
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

	<div class="container-fluid">
    <div class="row">
    <!-- 左邊的Bar -->
      <div class="col-lg-2 nopadding" id=leftBar>
      	<br>
        <div class="list-group">
        	<%@include file="left.jsp" %>
        </div>
      </div>
   	  <!-- 右邊顯示的資料 -->  
      <div class="col-lg-10 nopadding " >
      	<div class="image-container set-full-height " style="background-image: url(<c:url value="/images/menu/menu.jpg"/>)">
      
		<!-- 上傳圖案的部分 -->
      	<div class="top">
			<div class="infobox">
				<label for="menuPicture">
					<img src="<c:url value="/images/company/++.png"/>" width="100px" id="picture"/>
				</label>
				<input hidden type="file" id="menuPicture" accept="image/*" name="photo">
			</div>
				
			<div class="picbox">
				<img class="picture-src" id="PicturePreview" width="300px" /><br>
				<div>
					<input class='picbox tohide' type='button'  value='新增菜單' id="addMenu" style="margin-bottom: 20px;margin-top: 10px"/>
				</div>						
			</div>
      	</div>
      	<!-- 顯示資料庫的菜單 -->
      	<hr>
      	<!-- ajax回傳 -->
      	<div id="menuList">
      		<form id='deleteform' method='POST' name='form1'>
					<input type='hidden' name='_method' value='DELETE'>
				</form>
				
				<c:choose>
					<c:when test="${empty menus}">
	    				沒有任何菜單<br> 
					</c:when>
					<c:otherwise>
						<table style="text-align:center">
							<tr><th height="100px"><h1>我的菜單</h1></th></tr>
							<c:forEach var='menus' items='${menus}'>
								<tr>
<%-- 									<td>${menus.menuDetailId}</td> --%>
									<td>
										<a href="<c:url value="/rest/getMenuPicture/${menus.menuDetailId}"/>">
											<img width='20%' src="<c:url value="/rest/getMenuPicture/${menus.menuDetailId}"/>" />
										</a>
										<br>
									</td>
								</tr>
								<tr>
									<td height="80px">
										<a class="delete" href="<c:url value='/rest/deleteMenuPicture/${menus.menuDetailId}' />" >刪除菜單</a>
									</td>
								</tr>
							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose>
      	</div>
	  <!-- 右邊顯示的資料結束 -->  
      </div>
    </div>
    <!-- /.row -->
</div>
</div>

	<%@include file="../includePage/includeFooter.jsp" %>
	<!-- Scroll Up -->
	<div id="back-top">
		<a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
	</div>

<script>
	$(window).on('load', function() {
// 		//讓bar固定在上面以及設定高度
		$(".header-sticky").addClass("sticky-bar");
		$(".header-sticky").css("height", "90px");
		$(".header-sticky").css("position","static")
		//讓loading圖動起來
		$('#preloader-active').delay(450).fadeOut('slow');
		$('body').delay(450).css({'overflow' : 'visible'});			
			
	});

</script>

</body>
</html>