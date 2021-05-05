<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Template For inClude</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- site.webmanifest run offline -->
<link rel="manifest" href="site.webmanifest">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"
	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<!-- <link rel='stylesheet' -->
<%-- 	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" /> --%>
<!-- <script type="text/javascript" -->
<%-- 	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>  --%>
<link rel="stylesheet"
	href="<c:url value='/css/fontawesome-all.min.css' />" />
<!-- <script type="text/javascript" -->
<%-- 	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script> --%>
	


<style>
#aaa a{
color:#000000;
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

	<div class="row">
		<div class="col-3" id="aaa">
			<%@include file="../template/backstageTemp.jsp" %>
		
		
		</div>
		<div class="col-8">
			<h1>testqqqssss</h1>
			<h1>test</h1>
			<h1>test</h1>
			<h1>test</h1>
		
		
		</div>
	</div>
	
	<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="請輸入信箱">Open modal for @mdo</button>
<!-- 	<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="@fat">Open modal for @fat</button> -->
<!-- 	<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="@getbootstrap">Open modal for @getbootstrap</button> -->
	
	<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">New message</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form>
	          <div class="form-group">
	            <label for="recipient-name" class="col-form-label" >姓名:</label>
	            <input type="text" class="form-control" id="recipient-name" placeholder="請輸入名字">
	          </div>
	          <div class="form-group">
	            <label for="recipient-name" class="col-form-label" >使用者信箱:</label>
	            <input type="text" class="form-control" id="recipient-name" placeholder="請輸入信箱">
	          </div>
	          <div class="form-group">
	            <label for="message-text" class="col-form-label">Message:</label>
	            <textarea class="form-control" id="message-text"></textarea>
	          </div>
	        </form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary">Send message</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	

<%-- 	<%@include file="../includePage/includeFooter.jsp" %> --%>
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
 		
 		
 		$("body").on("click",'#exampleModal', function(){
//  			$(this).modal('toggle')
			//
 			$(this).attr('href')
 		});
 		
 		$('#exampleModal').on('show.bs.modal', function (event) {
 			  
 			  var button = $(event.relatedTarget) // Button that triggered the modal
//  			  var recipient = button.data('whatever') // Extract info from data-* attributes
 			  // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
 			  // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
 			  var modal = $(this)
 			  modal.find('.modal-title').text('New message to ' + recipient)
 			  modal.find('.modal-body input').val(recipient)
 			  
 			})
 		
 		
 	</script>
	<!-- JS here -->



</body>
</html>