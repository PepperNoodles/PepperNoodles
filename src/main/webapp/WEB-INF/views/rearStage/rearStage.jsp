<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="../includePage/includeRearTop.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Rear Stage</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- favicon的圖-每頁都要加 -->
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.js'/>"></script>
<script type="text/javascript" src="<c:url value='https://cdn.jsdelivr.net/npm/chart.js@3.2.0/dist/chart.min.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.css' />" />
<link rel="stylesheet" href="<c:url value='/css/fontawesome-all.min.css' />"/>
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.js'/>"></script>
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
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
<script type="text/javascript" src="<c:url value='https://cdn.jsdelivr.net/npm/chart.js@3.2.1/dist/chart.min.js'/>"></script>


<script>
$(document).ready(function() {
	
		

		$(function() {
		   $('.list-group-item').on('click', function() {
		     $('.glyphicon', this)
		       .toggleClass('glyphicon-chevron-right')
		       .toggleClass('glyphicon-chevron-down');
		   });

		 });
		
	    //提交產品
		var pname,pprice,pdes,pquantity,detailclassval;
		$("#submitproduct").click(function(e) {
		e.preventDefault();
		pname = $("#productname").val();
		pprice = $("#productprice").val();
		pdes = $("#productdescription").val();
		pquantity = $("#productquantity").val();
		detailclassval = $('#detailclass :selected').val();
		var editshelfval2 = $("input[name='editshelf']:checked").val();
		data = new FormData();
    	data.append('file', $('#wizard-picture')[0].files[0]);
		data.append('productinfo',new Blob([JSON.stringify( {"productName": pname,"productPrice": pprice,"description" : pdes,"quantity" : pquantity,"productDetailClass":detailclassval,"status":editshelfval2} )],{type: "application/json"}));
		$.ajax({
			method:"POST",
			url:"/PepperNoodles/insproduct",
			data:data,
			processData: false,
			contentType: false, 
			cache: false,  //不做快取
	        async : true,
	        success: function (result) {
	        	setTimeout(function(){  
	        		location.reload();//页面刷新
	        		},2000);
	             $("#checkStatus").text(result.success).css({"color":"blue"});
	        },
	        error: function (result) {
	            $("#checkStatus").text(result.fail);
	        }
		})
	
	});//end
		
	//商品表
	var Table = $("#productlist").DataTable({
		 language: {
		        "processing": "處理中...",
		        "loadingRecords": "載入中...",
		        "lengthMenu": "顯示 _MENU_ 項結果",
		        "zeroRecords": "沒有符合的結果",
		        "info": "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
		        "infoEmpty": "顯示第 0 至 0 項結果，共 0 項",
		        "infoFiltered": "(從 _MAX_ 項結果中過濾)",
		        "infoPostFix": "",
		        "search": "搜尋:",
		        "paginate": {
		            "first": "第一頁",
		            "previous": "上一頁",
		            "next": "下一頁",
		            "last": "最後一頁"
		        },
		        "aria": {
		            "sortAscending": ": 升冪排列",
		            "sortDescending": ": 降冪排列"
		        }
		    },
		    data:[],
		    columns: [
		    	{ "data": "productId" },
               { "data": "productName"  },
               { "data": "realeasedDate" ,
           	     "render": function (data, type, row, meta) {
           	     var time2 = new Date(data).format("yyyy-MM-dd hh:mm:ss");
                 return time2.substr(0,4)+'/'+time2.substr(5,2)+'/'+time2.substr(8,2)+' '+time2.substr(11,5)}},
               { "data": "productPrice" ,
                 "render": function(data,type,row,meta){
                	 return "$"+data;
                 }},
               { "data": "quantity" },
               { "data": "productDetailClassName" },
               { "data": "salesamount"},
               { "data": "",
            	 "render":function(data,type,row,meta){
            	 return "<button style='background-color:#00008B;border-radius:15px;' id='openProduct'><i class='fas fa-eye'></i></button>"; 
            	 }},
               { "data": "",
            	 "render": function(data,type,row,meta){
            		 return "<button style='background-color:#00008B;border-radius:15px;' id='deleteProduct'><i class='fas fa-trash-alt'></i></button>";
            	 }},
           	   { "data": "status",
            	 "render":function(data,type,row,meta){
            		 if (data=="上架中"){
            			return "<font color='green' size='1'>"+data+"</font>" ;
            		 }else{
            			 return "<font color='red' size='1'>"+data+"</font>";
            		 }
            	 }}
		    ],
		    filter: true,
		    bPaginate: true,
		    info: true,
		    ordering: true,
		    processing: true,
		    retrieve: true,
		    searching: true,
		    pageLength:10,
		    destroy: true,
		});
	
	
	Date.prototype.format = function (fmt) {
		  var o = {
		    "M+": this.getMonth() + 1, 
		    "d+": this.getDate(), 
		    "h+": this.getHours(), 
		    "m+": this.getMinutes(), 
		    "s+": this.getSeconds(), 
		    "q+": Math.floor((this.getMonth() + 3) / 3), 
		    "S": this.getMilliseconds() 
		  };
		  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		  for (var k in o)
		  if (new RegExp("(" +  k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" +  o[k]).substr(("" + o[k]).length)));
		  return fmt;
		}
	
		///////////////////////////////////////////////////////
		$(function getdatatable() {
			$.ajax({
				method:"GET",	
				url:"/PepperNoodles/getproductlist",
				contentType: 'application/json; charset=utf-8',
				dataType:'json',
		        async : true,
		        cache: false,
		        success:function(result){
		        	var orderlist = result.productlist;
		        	Table.clear().draw();
		            Table.rows.add(result.productlist).draw();
// 		            setInterval(getdatatable, 30000); 
		        },
		        error: function (result) {
		        	console.log("有問題");
		        }
			});
		});
		
		//檢視商品
		$("body").on("click","#openProduct",function(e){
			e.preventDefault();
			var productid = parseInt($(this).parent().prevAll("td:eq(6)").text(),10);
			localStorage.setItem("edit",productid);
			var pwindow = window.open("Editproduct","ProductWindow","toolbar=yes,scrollbars=yes,resizable=yes,top=200,left=400,width=800,height=500");
			
		});
		
		//刪除商品
		$("body").on("click","#deleteProduct",function(e){
			var productid = parseInt($(this).parent().prevAll("td:eq(7)").text(),10);
			var mymessage=confirm("確定修改此產品嗎？");
			if(mymessage==true){
				$.ajax({
					method:"GET",
					url:"/PepperNoodles/deleteProducts?deleteid="+productid+"",
					contentType: 'application/json; charset=utf-8',
					dataType:'text',
			        async : true,
			        cache: false,
			        success:function(result){
			        		alert("已成功刪除");
			        },
			        error: function (result) {
			        	console.log("有問題");
			        }
				});
			}
		});
		
		
		
		
		//圖表chart.js
		var myChart,ctx;
		$('body').on("click","#firesearch",function(e){
			var datelist = new Array();
			var mymonth = parseInt($('#mymonth :selected').val(),10);
			var myyear = parseInt($('#myyear :selected').val(),10);
			var myprouctid = parseInt($('#myproductname :selected').val(),10);
			var d = new Date(myyear,mymonth,0)　//取得所選年月份有幾天
		    var datenumber = d.getDate();
			for (var i = 1;i<=datenumber;i++){
				var datestr = (mymonth+"月"+i+"號");
				datelist.push(datestr);
			}
			
			$.ajax({
				method:"GET",	
				url:"/PepperNoodles/saleschart?year="+myyear+"&month="+mymonth+"&pid="+myprouctid+"",
				contentType: 'application/json; charset=utf-8',
				dataType:'json',
		        async : true,
		        cache: false,
		        success:function(result){
		        	console.log(result.salesamount);
		        	var salesamount = result.salesamount;
					if (myChart){
						myChart.destroy();
					}
		        	ctx = $('#myChart');
					myChart = new Chart(ctx, {
					  type: 'bar',
					  data: {
					    labels: datelist,//橫軸
					    datasets: [{
				    	  backgroundColor: getRandomColorEachEmployee(datelist.length),
					      label: '銷售業績(萬)',
					      data: salesamount//欄值
					    }]
					  },
					  options: {
						scaleShowValues: true,
						scales: {
						  yAxes: [{
							ticks: {
							  beginAtZero: true
							}
						  }],	
						  xAxes: [{
						    ticks: {
						      autoSkip: false
						    }
						  }]
						}
					  },
					  plugins:[{
						  beforeDraw: function(chart) {
						        var ctx = chart.ctx;
						        ctx.fillStyle = "white";
						        ctx.fillRect(0, 0, chart.width, chart.height);
						    }
					  }]
					});//end of chart
		        },
		        error: function (result) {
		        	console.log("有問題");
		        }
			});//ajax ends
			
			
		});//chart.js ends
		
			
		$("body").on("click","#mydownload",function(e){
			var url_base64jp = myChart.toBase64Image('image/jpeg', 1);
		    $("#mydownload").attr("href",url_base64jp).attr("download","PPNChart.jpg");
		})
			 
		//圓餅圖
		var myChart2,ctx2;
		$("body").on("click","#firePiesearch",function(e){
			var minusday =parseInt($("#pastday :selected").val().substring(2),10)
			var pieprouctid = parseInt($('#mypieproductname :selected').val(),10);
			$.ajax({
				method:"GET",
				url:"/PepperNoodles/piechart?dayrange="+minusday+"&pieproduct="+pieprouctid+"",
				contentType: 'application/json; charset=utf-8',
				dataType:'json',
		        async : true,
		        cache: false,
		        success:function(result){
		        	var labellist = result.pcatagory;
		        	var totallist = result.psubtotal;
		        	if (myChart){
						myChart.destroy();
					}
		        	ctx2 = $('#myPieChart');
					myChart2 = new Chart(ctx2, {
					  type: 'pie',
					  data: {
					    labels: labellist,//幾個餅
					    datasets: [{
				    	  backgroundColor: getRandomColorEachEmployee(labellist.length),
					      label: '銷售業績(萬)',
					      data: totallist//欄值
					    }]
					  },
					  options: {
						  responsive: true,
					  },
					  plugins: {
					      datalabels: {
					        color: 'blue',
					        labels: {
					          title: {
					            font: {
					              weight: 'bold'
					            }
					          },
					          value: {
					            color: 'green'
					          }
					        }
					      }
					    }
					});//end of chart
		        },
		        error:function(result){
		        	console.log("有問題")
		        }
			});
		});
		
		
		$("body").on("click","#piedownload",function(e){
			var url_base64jp = myChart2.toBase64Image('image/jpeg', 1);
		    $("#piedownload").attr("href",url_base64jp).attr("download","PPNPieChart.jpg");
		})
		
		
		
		function getRandomColor() {
		    var letters = '0123456789ABCDEF'.split('');
		    var color = '#';
		    for (var i = 0; i < 6; i++) {
		        color += letters[Math.floor(Math.random() * 16)];
		    }
		    return color;
		}
		
		
		function getRandomColorEachEmployee(count) {
		    var data =[];
		    for (var i = 0; i < count; i++) {
		        data.push(getRandomColor());
		    }
		    return data;
		}
		
		
	//////////////////////////斷//////////////////////////////////	
		$('body').on('click','#addP',function(){
			$("#productname").val('美式炸大雞');
			$("#productprice").val('50');
			$("#productdescription").val('DemoDemoDemoDemoDemo');
			$("#productquantity").val('20');
			$("input[id='upshelf']").attr("checked",true); 
		});
		
		$('body').on('click','#queryproduct',function(){
			$('#insertProduct').hide();
			$('#chartjs').hide();
			$('#chartheader').hide();
			$("#piechart").hide();
			$('#datatableforproducts').show();
		});
	
		$('body').on("click","#newproduct",function(){
			$('#datatableforproducts').hide();
			$('#chartjs').hide();
			$('#chartheader').hide();
			$("#piechart").hide();
			$('#insertProduct').show();
		});
		$('body').on("click","#chart",function(){
			$('#datatableforproducts').hide();
			$('#insertProduct').hide();
			$('#chartheader').show();
			$('#chartjs').show();
			$("#piechart").show();
// 			$.ajax({
// 				method:"GET",
// 				url:"/PepperNoodles/getoptionproductname",
// 				contentType: 'application/json; charset=utf-8',
// 				dataType:'json',
// 		        async : true,
// 		        cache: false,
// 		        success:function(result){
// 		        	$.each(result,function(index,element){
// 		        		var option = $("<option></option>")
// 		        		$("#myproduct").prop(option);
// 		        	});
		        	
// 		        },
// 		        error: function (result) {
// 		        	console.log("getoptionproductname有問題");
// 		        }
// 			});
			
			
		});
		
});

</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+TC&display=swap');
.left-column-div{
	width:100%;
	margin-bottom: 10px;
}

.productFrame{
 	padding:20px; 
	clear: both;
}
	
label{
	font-size: 18px;
}
.just-padding {
  padding: 15px;
}

.list-group.list-group-root {
  padding: 0;
  overflow: hidden;
}

.list-group.list-group-root .list-group {
  margin-bottom: 0;
}

.list-group.list-group-root .list-group-item {
  border-radius: 0;
  border-width: 1px 0 0 0;
}

.list-group.list-group-root > .list-group-item:first-child {
  border-top-width: 0;
}

.list-group.list-group-root > .list-group > .list-group-item {
  padding-left: 30px;
}

.list-group.list-group-root > .list-group > .list-group > .list-group-item {
  padding-left: 45px;
}

.list-group-item .glyphicon {
  margin-right: 5px;
}
a{
	color:black;
}
a:hover{
  text-decoration: none;
  background-color: #6d6d6d; 
}
.list-group-item{
	background-color:#DCDCDC
}
.display{
 	font-family: 'Noto Serif TC', serif;
 	font-size: 13px;
}
option{
/* 	padding: 5px 0; */
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
<!-- 	 <header> -->
<!-- 	<!-- Header Start --> 
<!--        <div class="header-area header-transparent"> -->
<!--             <div class="main-header"> -->
<!--                <div class="header-bottom  "> -->
<!--                     <div class="container-fluid"> -->
<!--                         <div class="row align-items-center"> -->
<!--                             Logo -->
<!--                             <div class="col-xl-2 col-lg-2 col-md-1"> -->
<!--                                 <div class="logo"> -->
<!--                                   <a href="/PepperNoodles"><img src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""></a> --%>
<!--                                 </div>                       -->
<!--                             </div> -->
<!--                             <div class="col-xl-10 col-lg-10 col-md-8"> -->
<!--                                 Main-menu -->
<!--                                 <div class="main-menu f-right d-none d-lg-block"> -->
<!--                                     <nav> -->
<!--                                         <ul id="navigation">                                                                                                                                      -->
<!--                                             <li><a href="#">城市</a> -->
<!--                                                 <ul class="submenu"> -->
<!--                                                     <li><a href="blog.html">台北</a></li> -->
<!--                                                     <li><a href="blog_details.html">新北</a></li> -->
<!--                                                     <li><a href="elements.html">基隆</a></li> -->
<!--                                                     <li><a href="listing_details.html">桃園</a></li> -->
<!--                                                 </ul> -->
<!--                                             </li> -->
<!--                                             <li><a href="#">美食</a> -->
<!--                                                 <ul class="submenu"> -->
<!--                                                     <li><a href="blog.html">美式</a></li> -->
<!--                                                     <li><a href="blog_details.html">日式燒烤</a></li> -->
<!--                                                     <li><a href="elements.html">韓式</a></li> -->
<!--                                                     <li><a href="listing_details.html">炸物</a></li> -->
<!--                                                 </ul> -->
<!--                                             </li> -->
<!--                                             <li><a href="#">排行榜</a> -->
<!--                                                 <ul class="submenu"> -->
<!--                                                     <li><a href="blog.html">免費排行</a></li> -->
<!--                                                     <li><a href="blog_details.html">付費排行</a></li> -->
<!--                                                     <li><a href="elements.html">周排行</a></li> -->
<!--                                                     <li><a href="listing_details.html">綜合排行</a></li> -->
<!--                                                 </ul> -->
<!--                                             </li> -->
<!--                                             <li><a href="about.html">論壇</a></li> -->
<!--                                             <li><a href="#">最新消息</a> -->
<!--                                                 <ul class="submenu"> -->
<!--                                                     <li><a href="blog.html">菜色新聞</a></li> -->
<!--                                                     <li><a href="blog_details.html">最新優惠</a></li> -->
<!--                                                     <li><a href="elements.html">新開幕</a></li>                                                    -->
<!--                                                 </ul> -->
<!--                                             </li> -->
<!--                                             <li><a href="about.html">發表食記</a></li> -->
<!--                                             <li><a href="contact.html">Contact</a></li> -->
<!--                                             <li class="add-list"><a href="listing_details.html"><i class="ti-plus"></i> add Listing</a></li> -->
<!--                                             <li><a href="/PepperNoodles/shoppingSystem/ShoppingMall" id="shoppingMall">商城</a></li> -->
<!--                                             
<!--                                         </ul> -->
<!--                                     </nav> -->
<!--                                 </div> -->
<!--                             </div> -->
<!--                             Mobile Menu -->
<!--                             <div class="col-12"> -->
<!--                                 <div class="mobile_menu d-block d-lg-none"></div> -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                </div> -->
<!--             </div> -->
<!--        </div> -->
<!--         Header End -->
<!-- 	  </header> -->
	  <main>

        <!-- Hero Start-->
<!--         <div class="hero-area3 hero-overly2 d-flex align-items-center " style="background-image:url(<c:url value="/images/hero/frechfries.jpg"/>);"> --%>
<!--             <div class="container"> -->
<!--                 <div class="row justify-content-center"> -->
<!-- 					<div class="col-xl-8 col-lg-9"> -->
<!-- 						<div class="hero-cap text-center pt-50 pb-20 "> -->
<!--                         </div> -->
<!-- 					</div> -->
<!--                 </div> -->
<!--             </div> -->
<!--         </div> -->
        <!--Hero End -->
        <!-- listing Area Start -->
        <div class="listing-area pt-120 pb-120" style="height:1500px;">
            <div class="container" style="padding: 0px;">
                <div class="row" >
                    <!-- Left content -->
                    <div class="col-xl-4 col-lg-4 col-md-6" style="color:black;background-color:#DCDCDC;">
                        <div class="row" style="padding:5px;">
                        	<div class="left-column-div" >
                        	<!-- left bar write here -->
                        	<div class="just-padding" style="">
							<div class="list-group list-group-root well">
<!-- 							  <a href="#item-1" class="list-group-item" data-toggle="collapse"> -->
<!-- 							    <i class="glyphicon glyphicon-chevron-right"></i>使用者 -->
<!-- 							  </a> -->
<!-- 							  <div class="list-group collapse" id="item-1"> -->
<!-- 								    <a href="#item-1-1" class="list-group-item" data-toggle="collapse"> -->
<!-- 								      <i class="glyphicon glyphicon-chevron-right"></i>Item 1.1 -->
<!-- 								    </a> -->
<!-- 								  <div class="list-group collapse" id="item-1-1"> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.1.1</a> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.1.2</a> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.1.3</a> -->
<!-- 								  </div> -->
<!-- 								    <a href="#item-1-2" class="list-group-item" data-toggle="collapse"> -->
<!-- 								    <i class="glyphicon glyphicon-chevron-right"></i>Item 1.2 -->
<!-- 								    </a> -->
<!-- 								  <div class="list-group collapse" id="item-1-2"> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.2.1</a> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.2.2</a> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.2.3</a> -->
<!-- 								  </div> -->
<!-- 								    <a href="#item-1-3" class="list-group-item" data-toggle="collapse"> -->
<!-- 								    <i class="glyphicon glyphicon-chevron-right"></i>Item 1.3 -->
<!-- 								    </a> -->
<!-- 								  <div class="list-group collapse" id="item-1-3"> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.3.1</a> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.3.2</a> -->
<!-- 								    <a href="#" class="list-group-item">Item 1.3.3</a> -->
<!-- 								  </div> -->
<!-- 							  </div> -->
							  
							  <a href="#item-2" class="list-group-item" data-toggle="collapse" style="border: 1px solid #A9A9A9;border-radius: 15px;">
							    <i class="glyphicon glyphicon-chevron-right"></i>產品
							  </a>
							  <div class="list-group collapse" id="item-2">
							    <a href="#item-2-1" class="list-group-item" data-toggle="collapse" id="queryproduct">
							      <i class="glyphicon glyphicon-chevron-right"></i>查詢/修改/刪除
							    </a>
<!-- 							  <div class="list-group collapse" id="item-2-1"> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.1.1</a> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.1.2</a> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.1.3</a> -->
<!-- 							  </div> -->
							    <a href="#item-2-2" class="list-group-item" data-toggle="collapse" id="newproduct">
							      <i class="glyphicon glyphicon-chevron-right" ></i>新增
							    </a>
<!-- 							  <div class="list-group collapse" id="item-2-2"> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.2.1</a> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.2.2</a> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.2.3</a> -->
<!-- 							  </div> -->
							    <a href="#item-2-3" class="list-group-item" data-toggle="collapse" id="chart">
							      <i class="glyphicon glyphicon-chevron-right"></i>報表
							    </a>
<!-- 							    <a href="#item-2-4" class="list-group-item" data-toggle="collapse" id="createEvent"> -->
<!-- 							      <i class="glyphicon glyphicon-chevron-right"></i>建立活動 -->
<!-- 							    </a> -->
<!-- 							  <div class="list-group collapse" id="item-2-3"> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.3.1</a> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.3.2</a> -->
<!-- 							    <a href="#" class="list-group-item">Item 2.3.3</a> -->
<!-- 							  </div> -->
							  </div>
<!-- 							  	<a href="#item-3" class="list-group-item" data-toggle="collapse"> -->
<!-- 							      <i class="glyphicon glyphicon-chevron-right"></i>Item 3 -->
<!-- 							    </a> -->
<!-- 							  <div class="list-group collapse" id="item-3"> -->
<!-- 							    <a href="#item-3-1" class="list-group-item"> -->
<!-- 							      <i class="glyphicon glyphicon-chevron-right"></i>Item 3.1 -->
<!-- 							    </a> -->
<!-- 							    <a href="#item-3-2" class="list-group-item"> -->
<!-- 							      <i class="glyphicon glyphicon-chevron-right"></i>Item 3.2 -->
<!-- 							    </a> -->
<!-- 							    <a href="#item-3-3" class="list-group-item" > -->
<!-- 							      <i class="glyphicon glyphicon-chevron-right"></i>Item 3.3 -->
<!-- 							    </a> -->
<!-- 							  </div> -->
<!-- 							   	<a href="#item-4" class="list-group-item" > -->
<!-- 							      <i class="glyphicon glyphicon-chevron-right"></i>Item 4 -->
<!-- 							  	</a> -->
							</div>
							</div>
                        	<!-- left bar ends here -->
                        	</div>
                        </div>
                    </div>
                    <!-- Right content -->
                    <div class="col-xl-8 col-lg-8 col-md-6" style="height:1250px;padding:20px;">
                        <!-- listing Details Start-->
                        <div class="listing-details-area" style=""> 
                        	<!-- 新增產品start -->
	                        <div class="row" id="insertProduct"  style="display: none">
	                            <div class=" col-lg-12">
	                                <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">新增商品</h3> 
	                                <!-- product frame start -->
	                                <div class="row productFrame">
		                                <div class="col-12">
											<div class="picture-container">
												<div class="picture">
													<img src="<c:url value="/images/NoImage/NoImage.png"/>"
															class="picture-src" id="wizardPicturePreview" style="display: block;width: 280x;height: 250px;border: 1px solid darkblue;" />
													<input type="file" id="wizard-picture" accept="image/*" style="margin-top: 20px;margin-bottom: 10px;">
												</div>
											</div>
										</div>
										<div class="col-12">
		                                <label>產品名 <small>(必填)</small></label>
										<input class="form-control" type="text" name="userName" id="productname" placeholder="">	
		                                <label>產品價格 <small>(必填)</small></label>
										<input class="form-control" type="text" name="userName" id="productprice" placeholder="">	
		                                <label>產品描述 <small>(必填)</small></label>
										<textarea class="form-control" id="productdescription" rows="2" cols="50" name="s1" style="display: block;"></textarea>	
		                                <label>庫存數量<small>(估計販賣數量)</small></label>
										<input class="form-control" id="productquantity" type="number" placeholder="" step="1" min="0" max="100">	
	                                	</div>
	                                	<div class="col-12 " style="text-align: left;font-size: 18px;margin-top: 5px;margin-bottom: 5px;">
	                                		商品類別:
	                                	</div>
		                                <div  class="col-12" style="">
											<select id="detailclass" style="font-size: 18px;"> 
												<option value="1">炸雞</option>
												<option value="2">冰淇淋</option>
												<option value="3">蔬菜水果</option>
												<option value="4">甜點</option>
												<option value="5">牛排</option>
												<option value="6">火鍋</option>
												<option value="7">羊肉爐</option>
											</select>
										</div>
										<div class="col-12" style="text-align: left; font-size: 18px; margin-top: 5px; margin-bottom: 5px;">
											是否上架?
										</div>
										<div  class="col-12">
											<span style="font-size:18px;">上架</span>
											<input type="radio"  id="upshelf" name="editshelf" value="上架中">
											<span style="font-size:18px;">下架</span>
											<input type="radio" id="offshelf" name="editshelf" value="下架中">
										</div>
	                                	<div  class="col-12" style="margin-top: 20px;text-align: center;">
		                                	<input type="submit"  id="submitproduct"  value="提交" >
		                                	<span id="checkStatus" style="font-size: 18px;"></span>
		                                	<button id="addP" style="color: black;">一鍵新增1</button>
	                                	</div>
	                                </div>
	                            </div>
	                            
	                        </div>
	                        <!-- 新增產品end -->
	                        
	                        <!-- 查詢產品 -->
	                        <div class="row" id="datatableforproducts" >
	                        	<!-- 商品表 -->
	                        	<div class="col-12" >
									<h2>商品表</h2>
									<table  id="productlist" class="display" >
										<thead>
											<tr>
												<th>#No.</th>
												<th>品名</th>
												<th>推出時間</th>
												<th>價格</th>
												<th>庫存</th>
												<th>子分類</th>
												<th>已售數量</th>
												<th>查看</th>
												<th>刪除</th>
												<th>上下架</th>
											</tr>
										</thead>
										<tbody>
											<tr>
											</tr>
										</tbody>
									</table>
								</div>
	                        </div>
	                        <!-- 查詢產品 -->
	                        
	                        <!-- chart.js -->
	                        <div class="row" id="chartheader" style="display:none">
	                        	<div class="col-12">
	                        		<h3 style="border-bottom: 1px solid #D3D3D3;float:left;">報表</h3>
	                        	</div>
	                        </div>
	                        <div class="row" id="chartjs" style="display: none">
	                        	<div style="float: left;display: flex;margin-left: 20px;align-items: center;font-size: 20px;">
	                        		選擇時間: 
	                        	</div>
	                        	<div  style="float: left;display: inline-block;margin-left:10px;">
	                        		<select id="myyear"> 
										<option value="2021">2021</option>
										<option value="2020">2020</option>
									</select>
	                        	</div>
	                        	<div  style="float: left;display: inline-block;margin-left:10px;">
	                        		<select id="mymonth"> 
										<option value="1">1月</option>
										<option value="2">2月</option>
										<option value="3">3月</option>
										<option value="4">4月</option>
										<option value="5">5月</option>
										<option value="6">6月</option>
										<option value="7">7月</option>
										<option value="8">8月</option>
										<option value="9">9月</option>
										<option value="10">10月</option>
										<option value="11">11月</option>
										<option value="12">12月</option>
									</select>
	                        	</div>
	                        	<div style="float: left;display: flex;margin-left: 20px;align-items: center;font-size: 20px;">
	                        		選擇品名: 
	                        	</div>
	                        	<div  style="float: left;display: inline-block;margin-left:10px;">
	                        		<select id="myproductname"> 
	                        		<!-- 動態品名 -->
	                        			<option value="0">無</option>
	                        			<option value="1">炸機折價券</option>
	                        			<option value="2">小美冰淇淋折價券</option>
	                        			<option value="3">蛋沙拉</option>
	                        			<option value="6">橘子</option>
	                        			<option value="7">大牛排</option>
	                        			<option value="8">馬卡龍</option>
	                        			<option value="9">好吃大冰球</option>
	                        			<option value="10">超硬大麵包</option>
	                        			<option value="11">火鍋料理包</option>
	                        			<option value="12">羊肉爐湯包</option>
									</select>
	                        	</div>
	                        	<div style="float: left;display: flex;margin-left: 20px;align-items: center;font-size: 20px;">
	                        		<button style='background-color:#00008B;border-radius:20px;' id='firesearch'><i class="fas fa-search"></i></button>
	                        	</div>
	                        	<div style="float: left;margin-left: 20px;display: flex;align-items: center;font-size: 10px;width: 30px;">
	                        		<a id='mydownload'><button  style='background-color:#A9A9A9;' ><i class="fas fa-download"></i></button></a>
	                        	</div>
	                        	<div class="col-12" id="chartframe">
									<canvas id="myChart" width="350" height="200"></canvas>	                        	
	                        	</div>
	                        </div>
	                        <!-- pie chart starts -->
	                        <div class="row" id="piechart" style="display:none;margin-top: 80px;">
	                        	<div style="float: left;display: flex;margin-left: 20px;align-items: center;font-size: 20px;">
	                        		圓餅圖: 
	                        	</div>
	                        	<div  style="float: left;display: inline-block;margin-left:10px;">
	                        		<select id="pastday"> 
										<option value="pd7">過去七天</option>
										<option value="pd3">過去三天</option>
									</select>
	                        	</div>
	                        	<div style="float: left;display: flex;margin-left: 20px;align-items: center;font-size: 20px;">
	                        		選擇品名: 
	                        	</div>
	                        	<select id="mypieproductname"> 
	                        		<!-- 動態品名 -->
	                        			<option value="0">無</option>
	                        			<option value="1">炸機折價券</option>
	                        			<option value="2">小美冰淇淋折價券</option>
	                        			<option value="3">蛋沙拉</option>
	                        			<option value="6">橘子</option>
	                        			<option value="7">大牛排</option>
	                        			<option value="8">馬卡龍</option>
	                        			<option value="9">好吃大冰球</option>
	                        			<option value="10">超硬大麵包</option>
	                        			<option value="11">火鍋料理包</option>
	                        			<option value="12">羊肉爐湯包</option>
									</select>
	                        	<div style="float: left;display: flex;margin-left: 20px;align-items: center;font-size: 20px;">
	                        		<button style='background-color:#00008B;border-radius:20px;' id='firePiesearch'><i class="fas fa-search"></i></button>
	                        	</div>
	                        	<div style="float: left;margin-left: 20px;display: flex;align-items: center;font-size: 10px;width: 30px;">
	                        		<a id='piedownload'><button  style='background-color:#A9A9A9;' ><i class="fas fa-download"></i></button></a>
	                        	</div>
	                        	<div class="col-12" id="chartframe">
									<canvas id="myPieChart" width="350" height="200"></canvas>	                        	
	                        	</div>
	                        
	                        </div>
	                        <!-- chart.js -->
	                        
	                        <!-- create event  -->
	                        <div class="row" id="eventCreate" style="display: none">
	                        	
	                        
	                        </div>
	                        <!-- create event ends -->
	                        
                        </div>
                        
                        <!-- listing Details End -->

                    </div>
                </div>
            </div>
        </div>
        <!-- listing-area Area End -->

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
    
    
    	<script>
		$(function(){
			$("#wizard-picture").change(function(){
					if (this.files && this.files[0]) {
						var reader = new FileReader();
						
						reader.onload = function (e) {
							$('#wizardPicturePreview').attr('src', e.target.result);
						}
						
						reader.readAsDataURL(this.files[0]);
					}
				});
			}) ;
		</script>
	
		<!-- JS here -->
        <script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
        <script src="<c:url value='/scripts/popper.min.js' />"></script>
        <script src="<c:url value='/scripts/jquery.slicknav.min.js' />"></script>
        <script src="<c:url value='/scripts/owl.carousel.min.js' />"></script>
        <script src="<c:url value='/scripts/slick.min.js' />"></script>
        <script src="<c:url value='/scripts/wow.min.js' />"></script>
         <script src="<c:url value='/scripts/price-range.js' />"></script>
		<script src="<c:url value='/scripts/animated.headline.js' />"></script>
        <script src="<c:url value='/scripts/jquery.magnific-popup.js' />"></script>
        <script src="<c:url value='/scripts/jquery.nice-select.min.js' />"></script>
		<script src="<c:url value='/scripts/jquery.sticky.js' />"></script>
        <script src="<c:url value='/scripts/contact.js' />"></script>
        <script src="<c:url value='/scripts/jquery.form.js' />"></script>
        <script src="<c:url value='/scripts/jquery.validate.min.js' />"></script>
        <script src="<c:url value='/scripts/mail-script.js' />"></script>
        <script src="<c:url value='/scripts/jquery.ajaxchimp.min.js' />"></script>
        <script src="<c:url value='/scripts/plugins.js' />"></script>
        <script src="<c:url value='/scripts/main.js' />"></script>
		<script  src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js" defer></script>
</body>
</html>