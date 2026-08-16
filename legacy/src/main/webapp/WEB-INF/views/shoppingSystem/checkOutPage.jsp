<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Checkout Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon"	href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<script type="text/javascript"	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel='stylesheet'	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet"	href="<c:url value='/css/fontawesome-all.min.css' />" />
<script type="text/javascript"	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<link rel="stylesheet"	href="<c:url value='/css/owl.carousel.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/slicknav.css' />">
<link rel="stylesheet" href="<c:url value='/css/flaticon.css' />">
<link rel="stylesheet" href="<c:url value='/css/animate.min.css' />">
<link rel="stylesheet" href="<c:url value='/css/magnific-popup.css' />">
<link rel="stylesheet" href="<c:url value='/css/themify-icons.css' />">
<link rel="stylesheet" href="<c:url value='/css/slick.css' />">
<link rel="stylesheet" href="<c:url value='/css/nice-select.css' />">
<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<link rel="stylesheet" href="<c:url value='/css/price_rangs.css' />">
<script>
	$(document).ready(function() {
		
			//讀取商品
			myStorage = window.localStorage;
			var len   = myStorage.length;
			var pricetag = $('#pricetag');
			var id, name, amount, price,totalprice;
			var tbody = $('tbody');
			$('#pricetag>h2').remove();
			if (len==0){
				totalprice = 0;
				$('tbody').children("tr").remove();
				$('#pricetag>h2').remove();
				var tr 	 = $('<tr></tr>');
				var col1 = $('<td colspan="5">目前訂單沒有商品</td>').css({"text-align":"center"});
				tr.append(col1);
				tbody.append(tr);
				pricetag.append('<h2><strong>總價格: '+totalprice+' 元</strong></h2>');
			} else{
				for (var i = 0; i < len; i++) {//每個商品
				var item = localStorage.key(i);
				var object = JSON.parse(localStorage.getItem(item));
				for ( var key in object) {
					if (key == "id") {
						id = object[key];
					} else if (key == "name") {
						name = object[key];
					} else if (key == "amount") {
						amount = parseInt(object[key], 10);
					} else if (key == "price") {
						price = parseInt(object[key].substring(1),10);
					}
				}
				var tr = $('<tr></tr>');
				var col1 = $('<td></td>').text(i + 1).attr("id","cart" + parseInt(id, 10));
				var col2 = $('<td></td>').text(name);
				var col3 = $('<td></td>')
						.html('<input type="number" value="'+amount+'" min="1" style="width:40px;height:20px;">');
				var col4 = $('<td id="pr' + (i + 1) + '"></td>')
						.text(price);
				var col5 = $('<td></td>')
						.append('<i class="fas fa-trash-alt" id="garbage"></i>');
				tr.append(col1).append(col2).append(col3).append(col4).append(col5);
				tbody.append(tr);
				totalprice=0;
				$('#pricetag>h2').remove();
				for (var j = 0; j < len; j ++){
					amount = $('#pr'+(j+1)+'').prev().children("input").val();
					totalprice +=  parseInt($('#pr'+(j+1)+'').text(),10)*amount;
				}
				pricetag.append('<h2><strong>總價格: '+totalprice+' 元</strong></h2>');
			}
			
			};
			
			
			//點數字
			$('body').on('change','table tbody tr td:nth-child(3) input[type=number]',function(e){
				e.preventDefault();
				totalprice=0;
				var pricetag = $('#pricetag');
 					for (var i = 0; i < myStorage.length; i ++){
					amount = $('#pr'+(i+1)+'').prev().children("input").val();
					totalprice +=  parseInt($('#pr'+(i+1)+'').text(),10)*amount;
				}
				$('#pricetag>h2').remove();
				pricetag.append('<h2><strong>總價格: '+totalprice+' 元</strong></h2>');
			});
			
			//點垃圾桶
			$('body').on('click','#garbage',function(e){
				e.preventDefault();
				var idd;
				var tbody 	 = $('tbody');
				var pricetag = $('#pricetag');
				var pid = parseInt($(this).parent().prevAll("tr td:first-child").attr("id").substring(4),10);
				var row = parseInt($(this).parent().prevAll("tr td:first-child").text(),10)-1;
				for (var k = 1; k <= (len+1); k++){
					var object = JSON.parse(localStorage.getItem('item'+k+''));
					for (var key in object){
						if (key=="id")idd = parseInt(object[key],10);
					}
					if (pid==idd){
						localStorage.removeItem('item'+k+'');
						$('tbody').find('tr:eq('+row+')').remove();
						$('#pricetag>h2').remove();
					}
				}
				totalprice = 0;
				if (localStorage.length!=0){
					for (var i = 0; i < localStorage.length; i ++){
						amount = $('#pr'+(i+1)+'').prev().children("input").val();
						totalprice +=  parseInt($('#pr'+(i+1)+'').text(),10)*amount;
					}
				}else if (localStorage.length==0){
					$('tbody').children("tr").remove();
					$('#pricetag>h4').remove();
					var tr 	 = $('<tr></tr>');
					var col1 = $('<td colspan="5">目前購物車沒有商品</td>').css({"text-align":"center"});
					tr.append(col1);
					tbody.append(tr);
				}
				pricetag.append('<h2><strong>總價格: '+totalprice+' 元</strong></h2>');
			});
			
			
			
			//打開地址事件
// 			$('#address').hide();
// 			$('#inputReciever').hide();
// 			$('#inputPhone').hide();
// 			$('body').on('change','#inlineRadio1,#inlineRadio2',function(e){
// 				e.preventDefault();
// 				 var watid = $(this).attr('id');
// 				 if (watid=='inlineRadio1'){
// 					 $('#address').show();
// 					 $('#inputReciever').show();
// 					 $('#inputPhone').show();
// 				 }else {
// 					 $('#address').hide();
// 					 $('#inputReciever').hide();
// 					 $('#inputPhone').hide();
// 				 }
// 			});
			
			//請求興趣商品事件
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/tagproducts",
				contentType: 'application/json; charset=utf-8', 
		        async : true,
		        cache: false,
		        success: function (result) {
		        	var lens = 14;
		        	$("#Page1").show();
		        	var $productCardDiv;
		        	var $singlelisting;
		        		$.each(result,
		       			function(index, element){
	        			var text;
		       			if (element.description.length>lens){
		       				text = element.description.substring(0,lens-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			$productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			$singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input style='display:none;' class='openproduct'  type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p class='JQellipsis'>"+text+"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer' id='pro"+element.productId+"'>"+
		       					"<ul>"+
		       						"<li style='font-size:20px;'>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart' value='1' style='display:none;'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       						"<li class='addcart' id='removecart' value='2' style='display:none'>remove from cart&nbsp<i class='fas fa-trash-alt'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrame"));
		       			});
		        },
		        error: function (result) {
		        	console.log(result);
		        }
			});
			
			//查看更多商品
			$('body').on('click','#seeMoreTagProducts',function(e){
				e.preventDefault();
				window.open("http://localhost:9090/PepperNoodles/shoppingSystem/ShoppingMall", '_blank');
			});
			
			
			
			//結帳
			$('body').on('click','#checkout',function(e){
				e.preventDefault();
				var idlist = new Array();
				var amountlist = new Array();
				for (var i=0 ; i < len ; i++){
					var pid    = parseInt($('#pr'+(i+1)+'').prevAll("tr td:first-child").attr('id').substring(4),10);
					var amount = $('#pr'+(i+1)+'').prev().children("input").val();
					idlist.push(pid);
					amountlist.push(amount);
				}
				
				var address  = $('#inputAddress2').val();
				var reciever = $('#inputReciever4').val();
				var rphone   = $('#inputPhone4').val();
				var robject = new Object();
				robject = {"address":address,"reciever":reciever,"rphone":rphone}
				data = new FormData();
				data.append('idlist',JSON.stringify(idlist));
				data.append('amountlist',JSON.stringify(amountlist));
				data.append('robject',JSON.stringify(robject));
				if (idlist.length != 0){
					$.ajax({
						method:"POST",
						url:"/PepperNoodles/cheqInvoicecheckOutController",
						data:data,
						contentType: false, 
						processData: false,
						cache: false,  //不做快取
				        async : true,
				        success: function (response) {
				        	myStorage.clear();
							localStorage.setItem("ecpayform",response.ecpayform);
//	 						$("#ecpayform").append(response.ecpayform);
				        	window.open("http://localhost:433/PepperNoodles/shoppingSystem/OrderFormECpay", '_blank');
				        },
				        error: function (url) {
				        	console.log("Problems everywhere");
				        }	
					});
				}
				    
				
			});
			
			
			$('body').on('click','#justsavetoDB',function(e){
				e.preventDefault();
				localStorage.clear();
				var idlist = new Array();
				var amountlist = new Array();
				for (var i=0 ; i < len ; i++){
					var pid    = parseInt($('#pr'+(i+1)+'').prevAll("tr td:first-child").attr('id').substring(4),10);
					var amount = $('#pr'+(i+1)+'').prev().children("input").val();
					idlist.push(pid);
					amountlist.push(amount);
				}
				var address  = $('#inputAddress2').val();
				var reciever = $('#inputReciever4').val();
				var rphone   = $('#inputPhone4').val();
				var robject = new Object();
				robject = {"address":address,"reciever":reciever,"rphone":rphone}
				
				data = new FormData();
				data.append('idlist',JSON.stringify(idlist));
				data.append('amountlist',JSON.stringify(amountlist));
				data.append('robject',JSON.stringify(robject));
				$.ajax({
					method:"POST",
					url:"/PepperNoodles/buyNextTime",
					data:data,
					contentType: false, 
					processData: false,
					cache: false,  //不做快取
			        async : true,
			        dataType: "text",
			        success: function (response) {
			        	myStorage.clear();
			        	console.log(response);
			        	window.location.href = ("http://localhost:433/PepperNoodles/shoppingSystem/ShoppingMall");
			        },
			        error: function (response) {
			        	console.log("沒開新分頁");
			        }	
				});    
				
				
			});			
			
			
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/getUserInfo",
				contentType: 'application/json; charset=utf-8',
				dataType:'json',
		        async : true,
		        cache: false,
		        success:function(result){
		        	$('#inputAddress2').val(result.uaddress) ;
					$('#inputReciever4').val(result.uname);
					$('#inputPhone4').val(result.uphone);
		        },
		        error: function (result) {
		        	console.log("有問題");
		        }
			})
			
			
			
		
			//一件新增
			$('body').on('click','#oneclick',function(e){
				e.preventDefault();
				$('#inputAddress2').val("106台北市大安區復興南路一段390號2樓") ;
				$('#inputReciever4').val("Demo");
				$('#inputPhone4').val("0512789456");
			});
			
	
});	
</script>

<style>
.ghostwhite {
	background-color: #F8F8FF;
}

.row {
	margin: 40px;
}

.cartmenu {
	width: 100%;
	height: auto;
	box-shadow: 2px 3px 5px #888888;
	border-radius: 10px;
}

#garbage:hover {
	cursor: pointer;
}
#linepay{
	cursor: pointer;
}
.seeMore{
	float:right;
	color:#00008B;
	cursor: pointer;
	text-decoration: underline;
}
.seeMore:hover {
	text-decoration: none;
}
.productFrame{
	padding:5px;
	clear: both;
}
.openproduct:hover {
	box-shadow: 0 0px 3px 0 rgba(0,0,0,0.24), 0 8px 15px 0 rgba(0,0,0,0.19);
}
.blue{
	color: darkblue;
}

</style>
</head>
<body>
	<!-- Preloader Start -->
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
	<!-- Preloader Start -->
	<header>
		<!-- Header Start -->
		<div class="header-area header-transparent">
			<div class="main-header">
				<div class="header-bottom  ">
					<div class="container-fluid">
						<div class="row align-items-center">
							<!-- Logo -->
							<div class="col-xl-2 col-lg-2 col-md-1">
								<div class="logo">
									<a href="/PepperNoodles"><img
										src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""></a>
								</div>
							</div>
							<div class="col-xl-10 col-lg-10 col-md-8">
								<!-- Main-menu -->
								<div class="main-menu f-right d-none d-lg-block">
									<nav>
										<ul id="navigation">
											<li><a href="#">城市</a>
												<ul class="submenu">
													<li><a href="blog.html">台北</a></li>
													<li><a href="blog_details.html">新北</a></li>
													<li><a href="elements.html">基隆</a></li>
													<li><a href="listing_details.html">桃園</a></li>
												</ul></li>
											<li><a href="#">美食</a>
												<ul class="submenu">
													<li><a href="blog.html">美式</a></li>
													<li><a href="blog_details.html">日式燒烤</a></li>
													<li><a href="elements.html">韓式</a></li>
													<li><a href="listing_details.html">炸物</a></li>
												</ul></li>
											<li><a href="#">排行榜</a>
												<ul class="submenu">
													<li><a href="blog.html">免費排行</a></li>
													<li><a href="blog_details.html">付費排行</a></li>
													<li><a href="elements.html">周排行</a></li>
													<li><a href="listing_details.html">綜合排行</a></li>
												</ul></li>
											<li><a href="about.html">論壇</a></li>
											<li><a href="#">最新消息</a>
												<ul class="submenu">
													<li><a href="blog.html">菜色新聞</a></li>
													<li><a href="blog_details.html">最新優惠</a></li>
													<li><a href="elements.html">新開幕</a></li>
												</ul></li>
											<li><a href="about.html">發表食記</a></li>
											<!-- <li><a href="contact.html">Contact</a></li> -->
											<!-- <li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->
											<li><a href="/PepperNoodles/shoppingSystem/ShoppingMall"
												id="shoppingMall">商城</a></li>
											<c:choose>
												<c:when test="${pageContext['request'].userPrincipal == null}"><li class="login">
												<a href="loginSystem/loginPage"> <i class="ti-user">												
												</i> Sign in or Register</a>
												<ul class="submenu">
													<li><a href="<c:url value='/loginSystem/loginPage'/>">註冊</a></li>
													<li><a href="<c:url value='/loginSystem/normaluser'/>">使用者登入</a></li>
													<li><a href="<c:url value='/loginSystem/companyuser'/>">企業登入</a></li>
												</ul>	
												</li></c:when>
												<c:otherwise><li class="login"><sec:authorize access="isAuthenticated()">
												<a href="personalPage/edit"><i class="ti-user"></i><sec:authentication   property="principal.username" /> </a></sec:authorize>
												<ul class="submenu">
													<li><a href="<c:url value='/user/login'/>">個人頁面</a></li>
													<li><a href="<c:url value='/logout/page'/>">登出</a></li>
												</ul>	
												</li></c:otherwise>
												
											</c:choose>
										</ul>
									</nav>
								</div>
							</div>
							<!-- Mobile Menu -->
							<div class="col-12">
								<div class="mobile_menu d-block d-lg-none"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Header End -->
	</header>
	<main>
		<!-- Hero Start-->
		<div class="hero-area3 hero-overly2 d-flex align-items-center "
			style="background-image:url(<c:url value="/images/hero/frechfries.jpg"/>);">
			<div class="container">
				<div class="row justify-content-center">
					<!--new section here -->
					<div class="col-xl-8 col-lg-9">
						<div class="hero-cap text-center pt-50 pb-20 ">
							<h3>Hope You Enjoy</h3>
							<h2>Checkout products</h2>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--Hero End -->
		<div class="listing-area pt-120 pb-120">
			<div class="container">
<!-- 				<div class="row"> -->
					<h2 style="border-bottom: 1px solid #D3D3D3;">
						<strong>訂單明細</strong>
					</h2>
<!-- 				</div> -->
<!-- 				<div class="row"> -->
					<div class="col-xl-12 col-sm-offset-2" style="margin-top: 5%;">
						<div class="cartmenu">
							<div class="table-responsive-sm">
								<table class="table table-hover text-info text-justify " style="font-size: 18px;">
									<thead>
										<tr class=" Active">
											<th>編號</th>
											<th>產品名</th>
											<th>數量</th>
											<th>價格</th>
											<th>取消</th>
										</tr>
									</thead>
									<tbody>
										<!-- product here -->
									</tbody>
								</table>
							</div>
						</div>
					</div>
<!-- 				</div> -->
			
				<!-- table row ends -->
				<!-- 配送方式 -->
				<h4 style="text-decoration: underline;font-weight: bold;margin-top: 20%;">配送方式：</h4>
				<!-- 大空白 -->
				<div class="row g-3" style="height: 300px;">
				
				 	<div class="col-6" id="inputReciever">
				    	<label for="inputEmail4" class="form-label" style="font-size: 18px;">收件人:</label>
				    	<input type="email" class="form-control" id="inputReciever4">
				  	</div>
				  	<div class="col-6" id="inputPhone">
				    	<label for="inputPassword4" class="form-label" style="font-size: 18px;">收件人電話:</label>
				    	<input type="text" class="form-control" id="inputPhone4">
				  	</div>
					 <div class="col-12" id="address">
					    <label for="inputAddress2" class="form-label" style="font-size: 18px;">地址:</label>
					    <input type="text" class="form-control" id="inputAddress2" placeholder="XX市, XX區, XX路...">
					  </div>
				</div>
				<!-- 付款方式 -->
				<h4 style="text-decoration: underline;font-weight: bold">付款方式：</h4>
					<div class="row">
						<div class="col-3" id="linepay">
							<img src="<c:url value='../images/Product/ecpay.png'/>" style="border:1px solid #DCDCDC;border-radius:20px;width: 130px;height: 50px;object-fit:cover;display: block;">
							<a style="margin-left: 30px;">ECPay</a>
						</div>
						<div class="col-3">
<%-- 							<img src="<c:url value='../images/Product/linepay.png'/>" style="border:1px solid #DCDCDC;border-radius:20px;width: 130px;height: 50px;object-fit:cover;display: block;"> --%>
<!-- 							<a style="margin-left: 30px;">LinePay</a> -->
						</div>
						<div class="col-3"></div>
						<div class="col-3"></div>
					</div>
				<div class="row">
					<div class="col-12">
						<div style="text-align: right;" id="pricetag"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-12">
						<div style="text-align: right;">
							<a href="#" class="genric-btn primary medium" id="checkout">結帳</a>
						</div>
						<br>
						<div style="text-align: right;">
							<a href="#" class="genric-btn primary medium" id="justsavetoDB">下次再結</a>
						</div>
					</div>
				</div>
				
				
				<div class="row justify-content-xl-center" style="margin-top: 10%;"> 
<!-- 					<div class="col-xl-12 "> -->
						<div class="listing-details-area">
							<div class="row" id="Page1" >
								<div class=" col-lg-12">
			                        <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">猜您可能有興趣的商品</h3> 
			                        <span class="seeMore" id="seeMoreTagProducts" >
			                        <a>查看更多</a>
			                        </span>
			                        <!-- product frame start -->
			                        <div class="row productFrame"  ></div>
			                    </div>
							
							</div>
						</div>
					</div>
				<div id="">
				
				</div>
<!-- 				</div>	 -->
			</div>
			<!-- container ends -->
		</div>
		<!--  ends -->

	<div>
		<button id="oneclick" style="position: fixed;bottom: 10%;right:5%;background-color: black;color:white">一鍵新增</button>
	</div>
	
	
	</main>
	<footer>
		<!-- Footer Start-->
		<div class="footer-area">
			<div class="container">
				<div class="footer-top footer-padding">
					<div class="row justify-content-between">
						<div class="col-xl-3 col-lg-4 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="single-footer-caption mb-30">
									<!-- logo -->
									<div class="footer-logo">
										<a href="index.html"><img
											src="<c:url value='/images/logo/peppernoodle.png'/>" alt=""></a>
									</div>
								</div>
							</div>
						</div>
						<div class="col-xl-2 col-lg-2 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Quick Link</h4>
									<ul>
										<li><a href="#">Home</a></li>
										<li><a href="#">Listing</a></li>
										<li><a href="#">About</a></li>
										<li><a href="#">Contact</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xl-2 col-lg-3 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Categories</h4>
									<ul>
										<li><a href="#">台北美食</a></li>
										<li><a href="#">熱門餐廳</a></li>
										<li><a href="#">點券優惠</a></li>
										<li><a href="#">每周排行</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-xl-3 col-lg-3 col-md-4 col-sm-6">
							<div class="single-footer-caption mb-50">
								<div class="footer-tittle">
									<h4>Download App</h4>
									<ul>
										<li class="app-log"><a href="#"><img
												src="<c:url value='/images/gallery/app-logo.png'/>" alt=""></a></li>
										<li><a href="#"><img
												src="<c:url value='/images/gallery/app-logo2.png'/>" alt=""></a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="footer-bottom">
					<div class="row d-flex justify-content-between align-items-center">
						<div class="col-xl-9 col-lg-8">
							<div class="footer-copy-right">
								<p>
									<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
									Copyright &copy;
									<script>
										document
												.write(new Date().getFullYear());
									</script>
									All rights reserved | This template is made with <i
										class="fa fa-heart" aria-hidden="true"></i> by <a
										href="https://colorlib.com" target="_blank">Colorlib</a>
									<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
								</p>
							</div>
						</div>
						<div class="col-xl-3 col-lg-4">
							<!-- Footer Social -->
							<div class="footer-social f-right">
								<a href="#"><i class="fab fa-facebook-f"></i></a> <a href="#"><i
									class="fab fa-instagram"></i></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Footer End-->
	</footer>
	<!-- JS here -->
	<!-- All JS Custom Plugins Link Here here -->
	<script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
	<!-- Jquery, Popper, Bootstrap -->
	<script src="<c:url value='/scripts/popper.min.js' />"></script>
	<script type="text/javascript"
		src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
	<!-- Jquery Mobile Menu -->
	<script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script>
	<!-- Jquery Slick , Owl-Carousel Plugins -->
	<script src="<c:url value='/scripts/owl.carousel.min.js' />"></script>
	<script src="<c:url value='/scripts/slick.min.js' />"></script>
	<!-- One Page, Animated-HeadLin -->
	<script src="<c:url value='/scripts/wow.min.js' />"></script>
	<script src="<c:url value='/scripts/price-range.js' />"></script>
	<script src="<c:url value='/scripts/animated.headline.js' />"></script>
	<script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script>
	<!-- Nice-select, sticky -->
	<script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script>
	<script src="<c:url value='/scripts/jquery.sticky.js' />"></script>
	<!-- contact js -->
	<script src="<c:url value='/scripts/contact.js' />"></script>
	<script src="<c:url value='/scripts/jquery.form.js' />"></script>
	<script src="<c:url value='/scripts/jquery.validate.min.js' />"></script>
	<script src="<c:url value='/scripts/mail-script.js' />"></script>
	<script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script>
	<!-- Jquery Plugins, main Jquery -->
	<script src="<c:url value='/scripts/plugins.js' />"></script>
	<script src="<c:url value='/scripts/main.js' />"></script>
</body>
</html>