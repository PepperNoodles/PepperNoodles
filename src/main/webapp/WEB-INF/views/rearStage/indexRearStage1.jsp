<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
   <%@include file="../includePage/includeRearTop.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>後台首頁</title>

<script type="text/javascript"
	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
<style type="text/css">
	a{
		color:#FFFFFF;
	}

</style>
</head>


<body>
	
	
	<div class="row" style ="">
		<div class="col-2 " >
			<%@include file="../includePage/includeRearNav.jsp"%>
		</div>
		<div class="col-10">
		<!-- content -->

			<div class="card" style="width: 18rem;">		
				<img src="..." class="card-img-top" alt="...">			 
				<div class="card-body">
					<h5 class="card-title">一般會員資料表</h5>
					<p class="card-text">查看會員資料</p>
					<a href="#" class="btn btn-primary">Go somewhere</a>
				</div>			  	
			 </div>
			
			
			<div class="card" style="width: 18rem;">
				<img src="..." class="card-img-top" alt="...">
				<div class="card-body">
					<h5 class="card-title">企業會員資料表</h5>
					<p class="card-text">查看企業資料</p>
					<a href="#" class="btn btn-primary">Go somewhere</a>
				</div>
			</div>
			
			<div class="card" style="width: 18rem;">
				<img src="..." class="card-img-top" alt="...">
				<div class="card-body">
					<h5 class="card-title">餐廳</h5>
					<p class="card-text">Some quick example text to build on the
						card title and make up the bulk of the card's content.</p>
					<a href="#" class="btn btn-primary">Go somewhere</a>
				</div>
			</div>
			
			<div class="card" style="width: 18rem;">
				<img src="..." class="card-img-top" alt="...">
				<div class="card-body">
					<h5 class="card-title">產品</h5>
					<p class="card-text">Some quick example text to build on the
						card title and make up the bulk of the card's content.</p>
					<a href="#" class="btn btn-primary">Go somewhere</a>
				</div>
			</div>
			
<!-- 			<div class="row" id="Page1" > -->
<!-- 	                            <div class=" col-lg-12"> -->
<!-- 	                                <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">您可能有興趣的商品</h3>  -->
<!-- 	                                <span class="seeMore" id="seeMoreTagProducts" ><a>查看更多</a></span> -->
<!-- 	                                product frame start -->
<!-- 	                                <div class="row productFrame"></div> -->
<!-- 	                            </div> -->
<!-- 								全部商品start -->
<!-- 	                            <div class="col-lg-12"> -->
<!-- 	                                <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">全部商品</h3> -->
<!-- 	                                <span class="seeMore" id="seeMoreAllProducts" ><a>查看更多</a></span> -->
<!-- 	                                <div class="row productFrame2"></div> -->
	                            
<!-- 	                            全部商品end -->
<!--                        			</div> -->
<!--              </div> -->



		</div>
	</div>
	
	<script> 
	  $(document).ready(function () {
			
			var Table = $("#userlist").DataTable({
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
		                { "data": "accountIndex"  },		               
		                { "data": "enabled" }
		                
				    ],
				    filter: true,
				    bPaginate: true,
				    info: true,
				    ordering: true,
				    processing: true,
				    retrieve: true,
				    searching: true, //關閉filter功能
	              columnDefs: [{
	                  targets: [3],
	                  orderable: true,
	              }]
				});
			
				
				$.ajax({
					method:"GET",	
					url:"/PepperNoodles/user/getAccountList",
					contentType: 'application/json; charset=utf-8',
					dataType:'json',
			        async : true,
			        cache: false,
			        success:function(result){
			        	console.log("yes123");
			        	console.log(JSON.stringify(result));
			        	console.log(result.AccountList);
			        	Table.clear().draw();
			            Table.rows.add(result.AccountList).draw();
			        },
			        error: function (result) {
			        	console.log("有問題");
			        }
				});
	  });
  </script>
	  

</body>
</html>