<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
   <%@include file="../includePage/includeRearTop.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>後台測試用</title>

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
		
<!-- 			<div class="tab-pane fade" id="v-pills-userList" role="tabpanel" aria-labelledby="v-pills-userList-tab"> -->
<!-- 							<h2>一般會員資料</h2> -->
<!-- 							<table  id="userlist" class="display"> -->
<!-- 								<thead> -->
<!-- 									<tr> -->
<!-- 										<th>帳號</th> -->
<!-- 										<th>enabled</th> -->
										
<!-- 									</tr> -->
<!-- 								</thead> -->
<!-- 								<tbody> -->
<!-- 									<tr> -->
<!-- 									</tr> -->
<!-- 								</tbody> -->
<!-- 							</table> -->
<!-- 			</div> -->
		
		
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