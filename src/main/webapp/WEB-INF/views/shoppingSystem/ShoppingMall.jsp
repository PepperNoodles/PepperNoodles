<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Shopping Mall</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />"/>
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/css/owl.carousel.min.css' />">
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
	
		var flag;
		var ifprice
		var len = 14;
		
		//興趣
		$.ajax({
			method:"GET",
			url:"/PepperNoodles/tagproducts",
			contentType: 'application/json; charset=utf-8', 
	        async : true,
	        cache: false,
	        success: function (result) {
	        	$("#Page1").show();
	        	$("#PageEverthing").hide();
	        	$('#pagination').hide();
	        	$('#rangeprice').hide();
	        	var $productCardDiv;
	        	var $singlelisting;
	        		$.each(result,
	       			function(index, element){
        			var text;
	       			if (element.description.length>len){
	       				text = element.description.substring(0,len-1)+"...";
	       			}else {
	       				text =  element.description;
	       			}
	       			$productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
	       			$singlelisting = $('<div></div>').addClass('single-listing mb-30')
	       			$('<div class="list-img"></div>').appendTo($singlelisting)
	       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
	       			$('<div class="list-caption"></div>').appendTo($singlelisting)
	       			.append("<input class='openproduct'  type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
	       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
	       			.append("<h3>"+element.productName+"</h3>")
	       			.append("<p class='JQellipsis'>"+text+"</p>")
	       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
	       			.append("<div class='list-footer'>"+
	       					"<ul>"+
	       						"<li>$"+element.productPrice+"</li>"+
	       						"<li style='display:none;'>"+element.quantity+"</li>"+
	       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
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
		//全部商品
		$.ajax({
			method:"GET",
			url:"/PepperNoodles/getallproducts",
			contentType: 'application/json; charset=utf-8', 
	        async : true,
	        cache: false,
	        success: function (allresult) {
	        	var $productCardDiv;
	        	var $singlelisting;
	        		$.each(allresult,
	       			function(index, element){
	       			var text;
	       			if (element.description.length>len){
	       				text = element.description.substring(0,len-1)+"...";
	       			}else {
	       				text =  element.description;
	       			}
	       			$productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
	       			$singlelisting = $('<div></div>').addClass('single-listing mb-30')
	       			$('<div class="list-img"></div>').appendTo($singlelisting)
	       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
	       			$('<div class="list-caption"></div>').appendTo($singlelisting)
	       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
	       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
	       			.append("<h3>"+element.productName+"</h3>")
	       			.append("<p>"+ text +"</p>")
	       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
	       			.append("<div class='list-footer'>"+
	       					"<ul>"+
	       						"<li>$"+element.productPrice+"</li>"+
	       						"<li style='display:none;'>"+element.quantity+"</li>"+
	       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
	       					"</ul>"+
	       					"</div>");
	       			$productCardDiv.append($singlelisting);
	       			$productCardDiv.appendTo($(".row.productFrame2"));
	       			});
	        },
	        error: function (result) {
	        	console.log(result);
	        }
		})

		//興趣/查看全部
		$("#seeMoreTagProducts").click(function(e){
			e.preventDefault();
			ifprice = 0;
			flag=1;
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/seemoretagproducts",
				contentType: 'application/json; charset=utf-8', 
		        async : true,
		        cache: false,
		        success: function (allresult) {
		        	console.log(allresult);
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
		        	var $productCardDiv;
		        	var $singlelisting;
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			$productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			$singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        		
		        
		        },
		        error: function (result) {
		        	console.log("問題是:"+result);
		        }
			})
		});
		
		//查看全部商品
		$("#seeMoreAllProducts").click(function(e){
			e.preventDefault();
			ifprice=0;
			flag = 2;
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/seemoreallproducts",
				contentType: 'application/json; charset=utf-8', 
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        },
		        error: function (result) {
		        	console.log("something wrong!");
		        }
			})
		});
		
		//主類別
		$("#mainclass").on("click","a",function(e){
			e.preventDefault();
			ifprice = 0;
			flag = $(this).index("a")-50;
			var mainname=$(this).attr("id");
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/mainclass?mainname="+mainname+"",
				contentType: 'application/json; charset=utf-8', 
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        },
		        error: function (result) {
		        	console.log("something wrong!");
		        }
			})
		});
		//主類別
		$("#ingredientmainclass").on("click","a",function(e){
			e.preventDefault();
			ifprice = 0;
			flag = $(this).index("a")-55;//59
			var mainname=$(this).attr("id");
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/mainclass?mainname="+mainname+"",
				contentType: 'application/json; charset=utf-8', 
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        },
		        error: function (result) {
		        	console.log("something wrong!");
		        }
			})
		});
		
			
		//子類別coupon
		$("#detailname").on("click","a",function(e){
			e.preventDefault();
			ifprice = 0;
			flag = $(this).index("a")-49;//54
			console.log(flag);
			var detailname=$(this).attr("id");
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/detailclass?detailname="+detailname+"",
				contentType: 'application/json; charset=utf-8', 
				dataType: "json",
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        	},
			        error: function (result) {
			        	console.log("something wrong!");
			        }
			 })
		});
		//子類別ingredients
		$("#ingredientname").on("click","a",function(e){
			e.preventDefault();
			ifprice = 0;
			flag = $(this).index("a")-50;//60
			console.log(flag);
			var detailname=$(this).attr("id");
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/detailclass?detailname="+detailname+"",
				contentType: 'application/json; charset=utf-8', 
				dataType: "json",
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        	},
			        error: function (result) {
			        	console.log("something wrong!");
			        }
			 })
		});
		//TAGS根據標籤搜索
		$("#puretag").on("click","a",function(e){
			e.preventDefault();
			ifprice = 0;
			flag = $(this).index("a")-55;//67
			console.log("flag num is: "+flag);
			var tagname=$(this).attr("id");
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/puretags?tagname="+tagname+"",
				contentType: 'application/json; charset=utf-8', 
				dataType: "json",
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li style='display:none;'>"+element.quantity+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        	},
			        error: function (result) {
			        	console.log("something wrong!");
			        }
			 })
		});
		//serach搜索
		var input;
		$("#google").on("click",function(e){
			e.preventDefault();
			ifprice = 0;
// 			尚未修改start here
			flag = 1001;
			input = $("#searchall").val();
			$("#pframeall > div").remove();
			$("#Page1").hide();
			$("#PageEverthing").show();
			$('#pagination').show();
			$('#rangeprice').show();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/search?input="+input+"",
				contentType: 'application/json; charset=utf-8', 
				dataType: "json",
		        async : true,cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        	},
			        error: function (result) {
			        	console.log("something wrong!");
			        }
			 })
		});
		
		
		//回上頁
		$('#goback').click(function(e){
			e.preventDefault();
			$("#Page1").show();
			$("#PageEverthing").hide();
			$('#pagination').hide();
			$('#rangeprice').hide();
		});
		
		//價格區段click事件
		var price;
		$('#pricerangeol').on("click","a",function(e){
			e.preventDefault();
			ifprice = 1;
			price = $(this).attr("id");
			input = $("#searchall").val();
			console.log(input);
			$("#pframeall > div").remove();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/getpricerange/"+price+"/"+flag+"?input="+input+"",
				contentType: 'application/json; charset=utf-8', 
		        async : true,
		        cache: false,
		        success: function (allresult) {
		        	var productlist = allresult.productlist;
		        	var totalpage = allresult.totalpage;//總頁數
						//print btns	        	
		        		$("#pagframe>li").remove();
						for (var i=1;i<=totalpage;i++){
							$("#pagframe").append('<li class="page-item active"><a class="page-link" href="#" id="'+i+'">'+i+'</a></li>');
						}
						//print products		        	
		        		$.each(productlist,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			var $productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			var $singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>")
		       			.append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        },
		        error: function (result) {
		        	console.log("問題是:"+result);
		        }
			})
		});//END
		
		//分頁click事件
		$('#pagframe').on("click",".page-link",function(e){
			//ifprice判斷是不是在價格區段之間
			//flag判斷在哪個click事件
			//page判斷哪一個分頁btn
			e.preventDefault();
			if (ifprice==1){
				ifprice = price;
			}
			var page = $(this).attr("id");//第一頁
			$("#pframeall > div").remove();
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/getpage/"+ifprice+"/"+flag+"/"+(page-1)+"?input="+input+"",
				contentType: 'application/json; charset=utf-8', 
		        async : true,
		        cache: false,
		        success: function (allresult) {
		        	var $productCardDiv;
		        	var $singlelisting;
						//print products		        	
		        		$.each(allresult,
		       			function(index, element){
		       			var text;
		       			if (element.description.length>len){
		       				text = element.description.substring(0,len-1)+"...";
		       			}else {
		       				text =  element.description;
		       			}
		       			$productCardDiv = $('<div id="productCard"></div>').addClass("col-lg-6");
		       			$singlelisting = $('<div></div>').addClass('single-listing mb-30')
		       			$('<div class="list-img"></div>').appendTo($singlelisting)
		       			.append("<img id='pimage' src='../../PepperNoodles/getProductImages?no="+ element.productId+"'/>" )
		       			$('<div class="list-caption"></div>').appendTo($singlelisting)
		       			.append("<input class='openproduct' type='submit' id='queryProduct' value='Open' data-toggle='modal' data-target='#myModal'>")
		       			.append("<input type='hidden' id='hiddenid' value='"+element.productId+"'/>")
		       			.append("<h3>"+element.productName+"</h3>")
		       			.append("<p>"+ text +"</p>").append("<p class='JQellipsis' style='display:none;'>"+element.description+"</p>")
		       			.append("<div class='list-footer'>"+
		       					"<ul>"+
		       						"<li>$"+element.productPrice+"</li>"+
		       						"<li class='addcart' id='addcart'>add to cart&nbsp<i class='fas fa-shopping-cart'></i></li>"+
		       					"</ul>"+
		       					"</div>");
		       			$productCardDiv.append($singlelisting);
		       			$productCardDiv.appendTo($(".row.productFrameall"));
		       			});
		        },
		        error: function (result) {
		        	console.log("問題是:"+result);
		        }
			})
		});//END
		
		
		$('body').on("click","#queryProduct",function(e){
			e.preventDefault();
			$(".modal-body>div").remove();
			var productid = $(this).next().val();
			var productname = $(this).nextAll("h3").text();
			var description = $(this).nextAll("p:eq(1)").text();
			var price = $(this).nextAll(".list-footer").find("ul li:eq(0)").text();
			var quantity = $(this).nextAll(".list-footer").find("ul li:eq(1)").text();
			//
			var heading = $("#pnameheading");
			var modalbody = $(".modal-body");
			var modalfooter = $(".modal-footer");
			var container = $('<div></div>').addClass("container");
			var row = $('<div></div>').addClass("row");
			var listimg = $("<div class='col-lg-4'></div>");
			var detail = $("<div class='col-lg-8'></div>");
			var content = $("<p></p>").text(description).css({"font-size":"20px","font-weight":"bolder"});
			//
			heading.text(productname);
			detail.append(content);
			container.append(row.append(listimg.append($("<img id='queryimage' src='../../PepperNoodles/getProductImages?no="+ productid+"'/>"))).append(detail))
			modalbody.append(container);
			
			
		});
		
		
				
});
</script>

<style>
.left-column-div{
	width:100%;
	margin-bottom: 10px;
}
.totextcolor{
	color:black;
	
}
.mainclass{
	margin-left: 10px;
}
.mainclass:hover h4{
	color:#00008B;
	cursor: pointer;
}
.detailclass{
	margin-left: 15px;
}
.detailclass:hover a{
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
.productFrame2{
	padding:5px;
	clear: both;
}
.productFrameall{
	padding:5px;
	clear: both;
}
.addcart{
	cursor: pointer;
	text-decoration: underline;
}
.addcart:hover {
	text-decoration: none;
}
.openproduct:hover {
	box-shadow: 0 0px 3px 0 rgba(0,0,0,0.24), 0 8px 15px 0 rgba(0,0,0,0.19);
}
.modal-img {
	border-radius: 2em;
	border:2px solid #DCDCDC;
	overflow: hidden;
	max-width: 220px;
	max-height: 130px;
}
.modal-img img{
	width: 100%;
	height:100%;
	 object-fit: cover;
}
#queryimage{
	border-radius: 2em;
	border:2px solid #DCDCDC;
	overflow: hidden;
	width: 100%;
	max-height: 150px;
	 object-fit: cover;
}
.contentfont{
	font-size: 50px;
	font-weight: bolder;
}
</style>
</head>
<body>
	<!-- Preloader Start -->
    <div id="preloader-active">
        <div class="preloader d-flex align-items-center justify-content-center">
            <div class="preloader-inner position-relative" >
                <div class="preloader-circle"  style="background-color: rgb(102, 102, 102);"></div>
                <div class="preloader-img pere-text" >
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
                                  <a href="/PepperNoodles"><img src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""></a>
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
                                                </ul>
                                            </li>
                                            <li><a href="#">美食</a>
                                                <ul class="submenu">
                                                    <li><a href="blog.html">美式</a></li>
                                                    <li><a href="blog_details.html">日式燒烤</a></li>
                                                    <li><a href="elements.html">韓式</a></li>
                                                    <li><a href="listing_details.html">炸物</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="#">排行榜</a>
                                                <ul class="submenu">
                                                    <li><a href="blog.html">免費排行</a></li>
                                                    <li><a href="blog_details.html">付費排行</a></li>
                                                    <li><a href="elements.html">周排行</a></li>
                                                    <li><a href="listing_details.html">綜合排行</a></li>
                                                </ul>
                                            </li>
                                            <li><a href="about.html">論壇</a></li>
                                            <li><a href="#">最新消息</a>
                                                <ul class="submenu">
                                                    <li><a href="blog.html">菜色新聞</a></li>
                                                    <li><a href="blog_details.html">最新優惠</a></li>
                                                    <li><a href="elements.html">新開幕</a></li>                                                   
                                                </ul>
                                            </li>
                                            <li><a href="about.html">發表食記</a></li>
                                            <!-- <li><a href="contact.html">Contact</a></li> -->
                                            <!-- <li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->
                                            <li><a href="/PepperNoodles/shoppingSystem/ShoppingMall" id="shoppingMall">商城</a></li>
                                            <li class="login"><a href="loginSystem/loginPage">
                                                <i class="ti-user"></i> Sign in or Register</a>
                                            </li>
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
        <div class="hero-area3 hero-overly2 d-flex align-items-center " style="background-image:url(<c:url value="/images/hero/frechfries.jpg"/>);">
            <div class="container">
                <div class="row justify-content-center">
<!--                 new section here -->
					<div class="col-xl-8 col-lg-9">
						<div class="hero-cap text-center pt-50 pb-20 ">
                            <h3>Welcome to PepperNoodle</h3>
                            <h2>new upcoming products</h2>
                        </div>
					</div>
                </div>
            </div>
        </div>
        <!--Hero End -->
        <!-- listing Area Start -->
        <div class="listing-area pt-120 pb-120">
            <div class="container">
                <div class="row">
                
                    <!-- Left content -->
                    <div class="col-xl-4 col-lg-4 col-md-6" style="border:1px solid red;color:black;">
                        <div class="row" style="border:1px solid red;padding:5px;">
                        	<div class="left-column-div" >
                        		<input type="search" id="searchall" style="width:85%;" placeholder="炸雞、咖哩、冰淇淋...">
                        		<span ><button id="google" style="color: black"><i class="fas fa-search"></i></button></span>
                        	</div>
                        	<div class="left-column-div" style="margin-top: 10px;">
                        		<h4><a>商品類別</a></h4>
                        	</div>
                        	<div class="left-column-div" >
                        		<div class="mainclass" style="" id ="mainclass">
                        			<h4><a id="coupon">票券</a></h4>
                        		</div>
                        		<div class="detailclass">
                        			<ol class="ordered-list" id="detailname">
                        				<li><a id="friedchicken">炸雞</a></li>
                        				<li><a id="icecream">冰淇淋</a></li>
                        				<li><a id="vegfruit">蔬菜水果</a></li>
                        				<li><a id="desert">甜點</a></li>
                        				<li><a id="steak">牛排</a></li>
                        			</ol>
                        		</div>
                        	</div>
                        	<div class="left-column-div" >
                        		<div class="mainclass" style="" id ="ingredientmainclass">
                        			<h4><a id="ingredient">食材</a></h4>
                        		</div>
                        		<div class="detailclass">
                        			<ol class="ordered-list" id="ingredientname">
                        				<li><a id="hotpot">火鍋</a></li>
                        				<li><a id="lambstove">羊肉爐</a></li>
                        			</ol>
                        		</div>
                        	</div>
                        	<div class="left-column-div" id="rangeprice">
                        		<div class="mainclass" style="">
                        			<h4><a>價格區間</a></h4>
                        		</div>
                        		<div class="detailclass">
                        			<ol class="ordered-list" id="pricerangeol">
                        				<li><a id="0">$0 - $500</a></li>
                        				<li><a id="500">$500 - $1000</a></li>
                        				<li><a id="1000">$1000 - $1500</a></li>
                        			</ol>
                        		</div>
                        	</div>
                        	<div class="left-column-div" >
                        		<div class="mainclass" style="">
                        			<h4><a>TAGS</a></h4>
                        		</div>
                        		<div class="detailclass button-group-area mt-10" id="puretag">
                        			<a class="genric-btn primary-border small" id="friedchicken">炸雞</a>
                        			<a class="genric-btn primary-border small" id="icecream">冰淇淋</a>
                        			<a class="genric-btn primary-border small" id="salad">沙拉</a>
                        			<a class="genric-btn primary-border small" id="desert">甜點</a>
                        			<a class="genric-btn primary-border small" id="hotpot">火鍋</a>
                        		</div>
                        	</div>
                        </div>
                    </div>
                    <!-- Right content -->
                    <div class="col-xl-8 col-lg-8 col-md-6" style="border:1px solid red;padding:20px;">
                        <!-- listing Details Start-->
                        <div class="listing-details-area">
                        	<!-- test for bootstrap modal -->
	                        <!-- Modal Start -->
							<div class="modal fade" id="myModal" >
							    <div class="modal-dialog modal-dialog-centered modal-lg">
							      <div class="modal-content">
							      
							        <!-- Modal Header -->
							        <div class="modal-header">
								        <div class="container">
								          <div class="row justify-content-center">
	    									<h1 style="font-weight: bolder;" id="pnameheading"></h1>
	  									  </div>
	  									</div>
							          <button type="button" class="close" data-dismiss="modal">&times;</button>
							        </div>
							        
							        <!-- Modal body -->
							        <div class="modal-body">
							        </div>
							        
							        <!-- Modal footer -->
							        <div class="modal-footer">
							          <button type="button" class="genric-btn danger-border circle arrow small" data-dismiss="modal">Close</button>
							          <button type="button" class="genric-btn danger-border circle arrow small" >購買</button>
							        </div>
							        
							      </div>
							    </div>
							 </div>	
	                        <!-- Modal End -->
	                        <div class="row" id="Page1" >
	                            <div class=" col-lg-12">
	                                <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">您可能有興趣的商品</h3> 
	                                <span class="seeMore" id="seeMoreTagProducts" ><a>查看更多</a></span>
	                                <!-- product frame start -->
	                                <div class="row productFrame"></div>
	                            </div>
								<!--全部商品start -->
	                            <div class="col-lg-12">
	                                <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">全部商品</h3>
	                                <span class="seeMore" id="seeMoreAllProducts" ><a>查看更多</a></span>
	                                <div class="row productFrame2"></div>
	                            
	                            <!--全部商品end -->
	                        	</div>
                        	</div>
                        	
                        	<div class="row" id="PageEverthing">
                        		<div class=" col-lg-12">
                        		<span class="seeMore" id="goback" style="margin-bottom: 20px;"><a>回首頁</a></span>
	                        		<div class="row productFrameall" id="pframeall">
	                        		
	                        		</div>
                        		</div>
                        	</div>
                        
                        <!-- listing Details End -->
                        <!--Pagination Start  -->
                        <div class="pagination-area pt-70 text-center" id="pagination" style= "border: 1px solid red" >
                            <div class="container">
                                <div class="row">
                                    <div class="col-xl-12">
                                        <div class="single-wrap d-flex justify-content-center">
                                            <nav aria-label="Page navigation example">
                                                <ul class="pagination justify-content-start" id="pagframe">
<!--                                                     <li class="page-item active"><a class="page-link" href="#">01</a></li> -->
<!--                                                     <li class="page-item"><a class="page-link" href="#">02</a></li> -->
<!--                                                     <li class="page-item"><a class="page-link" href="#">03</a></li> -->
                                                <li class="page-item"><a class="page-link" href="#"><span class="ti-angle-right"></span></a></li>
                                                </ul>
                                            </nav>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--Pagination End  -->
                    </div>
                </div>
            </div>
        </div>
        <!-- listing-area Area End -->
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
                                        <a href="index.html"><img src="<c:url value='/images/logo/peppernoodle.png'/>" alt=""></a>
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
                                        <li class="app-log"><a href="#"><img src="<c:url value='/images/gallery/app-logo.png'/>" alt=""></a></li>
                                        <li><a href="#"><img src="<c:url value='/images/gallery/app-logo2.png'/>" alt=""></a></li>
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
  								Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | U copy <i class="fa fa-heart" aria-hidden="true"></i>    <a href="https://colorlib.com" target="_blank">U died</a>
  								</p>
                            </div>
                        </div>
                        <div class="col-xl-3 col-lg-4">
                            <!-- Footer Social -->
                            <div class="footer-social f-right">
                                <a href="#"><i class="fab fa-facebook-f"></i></a>
                                <a href="#"><i class="fab fa-instagram"></i></a>
                            </div>
                        </div>
                    </div>
               </div>
            </div>
        </div>
        <!-- Footer End-->
    </footer>
    <!-- Scroll Up -->
    <div id="back-top" >
        <a title="Go to Top" href="#"> <i class="fas fa-level-up-alt"></i></a>
    </div>
    
    	
	
		<!-- JS here -->
		<!-- All JS Custom Plugins Link Here here -->
        <script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
		<!-- Jquery, Popper, Bootstrap -->
<%-- 		<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script> --%>
        <script src="<c:url value='/scripts/popper.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>
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