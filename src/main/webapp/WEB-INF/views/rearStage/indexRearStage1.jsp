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

<!-- Bootstrap 4 Admin右上方訊息通知-->
<script src="<c:url value='/plugins/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<!-- 右上方訊息通知 -->
<%-- <script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.js'/>"></script> --%>

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

        <div class="row productFrame" style="background-color: #FAFAFA"> 
            <div id="" class="col-lg-6">
                <div class="single-listing mb-30">
					<div class="card" style="width: 40rem;">
					    <div class="list-img">		
							<img src="<c:url value="/images/rear/normalUserTable.jpg"/>" class="card-img-top" alt="...">
						</div>			 
						<div class="card-body">
							<h4 class="card-title">一般會員資料表</h4>
							<p class="card-text">查看會員資料</p>
							<a href="<c:url value='/rearStage/rearNormalUser' />" class="btn btn-primary">Go somewhere</a>
						</div>			  	
					 </div>
				 </div>
			 </div>
			
			<div id="" class="col-lg-6">
                <div class="single-listing mb-30">
					<div class="card" style="width: 40rem;">
						<img src="<c:url value="/images/rear/companyTable.jpg"/>" class="card-img-top" alt="...">
						<div class="card-body">
							<h4 class="card-title">企業會員資料表</h4>
							<p class="card-text">查看企業資料</p>
							<a href="<c:url value='/rearStage/userAccountRearCompanyUser' />" class="btn btn-primary">Go somewhere</a>
						</div>
					</div>
			 	</div>
			 </div>
			
			<div id="" class="col-lg-6">
                <div class="single-listing mb-30">
					<div class="card" style="width: 40rem;">
						<img src="<c:url value="/images/rear/restaurantTable.jpg"/>" class="card-img-top" alt="...">
						<div class="card-body">
							<h4 class="card-title">餐廳資料</h4>
							<p class="card-text">查看餐廳資料</p>
							<a href="<c:url value='/rearStage/rest' />" class="btn btn-primary">Go somewhere</a>
						</div>
					</div>
			 	</div>
			 </div>
			
			<div id="" class="col-lg-6">
                <div class="single-listing mb-30">
					<div class="card" style="width: 40rem;">
						<img src="<c:url value="/images/rear/restaurantTable.jpg"/>" class="card-img-top" alt="...">
						<div class="card-body">
							<h4 class="card-title">產品資料</h4>
							<p class="card-text">產品後台</p>
							<a href="http://localhost:433/PepperNoodles/user/rearStage/rearStage" class="btn btn-primary">前往企業後台</a>
						</div>
					</div>
			 	</div>
			 </div>
		</div>	
			




		</div>
	</div>
	
	<script> 
// 	  $(document).ready(function () {
			
// 			var Table = $("#userlist").DataTable({
// 				 language: {
// 				        "processing": "處理中...",
// 				        "loadingRecords": "載入中...",
// 				        "lengthMenu": "顯示 _MENU_ 項結果",
// 				        "zeroRecords": "沒有符合的結果",
// 				        "info": "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
// 				        "infoEmpty": "顯示第 0 至 0 項結果，共 0 項",
// 				        "infoFiltered": "(從 _MAX_ 項結果中過濾)",
// 				        "infoPostFix": "",
// 				        "search": "搜尋:",
// 				        "paginate": {
// 				            "first": "第一頁",
// 				            "previous": "上一頁",
// 				            "next": "下一頁",
// 				            "last": "最後一頁"
// 				        },
// 				        "aria": {
// 				            "sortAscending": ": 升冪排列",
// 				            "sortDescending": ": 降冪排列"
// 				        }
// 				    },
// 				    data:[],
// 				    columns: [				    	
// 		                { "data": "accountIndex"  },		               
// 		                { "data": "enabled" }
		                
// 				    ],
// 				    filter: true,
// 				    bPaginate: true,
// 				    info: true,
// 				    ordering: true,
// 				    processing: true,
// 				    retrieve: true,
// 				    searching: true, //關閉filter功能
// 	              columnDefs: [{
// 	                  targets: [3],
// 	                  orderable: true,
// 	              }]
// 				});
			
				
// 				$.ajax({
// 					method:"GET",	
// 					url:"/PepperNoodles/user/getAccountList",
// 					contentType: 'application/json; charset=utf-8',
// 					dataType:'json',
// 			        async : true,
// 			        cache: false,
// 			        success:function(result){
// 			        	console.log("yes123");
// 			        	console.log(JSON.stringify(result));
// 			        	console.log(result.AccountList);
// 			        	Table.clear().draw();
// 			            Table.rows.add(result.AccountList).draw();
// 			        },
// 			        error: function (result) {
// 			        	console.log("有問題");
// 			        }
// 				});
// 	  });
  </script>
  
  
	
</body>
</html>