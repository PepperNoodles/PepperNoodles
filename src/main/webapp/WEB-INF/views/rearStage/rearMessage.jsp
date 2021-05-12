<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
   <%@include file="../includePage/includeRearTop.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>訊息資料</title>

<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">

<!-- Bootstrap 4 Admin右上方訊息通知-->
<%-- <script src="<c:url value='/plugins/bootstrap/js/bootstrap.bundle.min.js' />"></script> --%>
<!-- 右上方訊息通知 -->
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script>

<!-- Jquery, Popper, Bootstrap -->
        <script src="<c:url value='/scripts/popper.min.js' />"></script>

<style type="text/css">
	
	a{
		color:#FFFFFF;
	}
	thead tr:hover{
		background-color:#6d6d6d;
	}
	table {
		border-collapse: separate;
		border: solid black 1px;
		border-radius: 6px;
		-moz-border-radius: 6px;
	}
	.navBar{
	padding-left: 0px;
	}
	
	.mainBody{
		height:800px;
		overflow:auto;
	}

</style>
</head>


<body>
	
	<div class="container-fluid " >
	<div class="row mainBody" >
		<div class="col-2 navBar" >
			<%@include file="../includePage/includeRearNav.jsp"%>
		</div>
		<div class="col-10" style="margin-top: 20px;">
		<!-- content -->
		
<!-- 			<div class="tab-pane fade" id="v-pills-userList" role="tabpanel" aria-labelledby="v-pills-userList-tab"> -->
							<h2>訊息資料</h2>
							<table  id="messagelist" class="display">
								<thead>
									<tr>
										<th>編號</th>
										<th>帳號</th>
										<th>訊息</th>
										<th>發布時間</th>
										<th>更新時間</th>
										<th>處理狀態</th>
										<th>處理完畢</th>
										<th>刪除訊息</th>
									</tr>
								</thead>
							</table>
<!-- 			</div> -->
		</div>
			<div aria-live="polite" data-autohide="true" aria-atomic="true" style="position: relative; min-height: 200px;">
		  <div class="toast" style="position: fixed; bottom: 15%;right: 15px;">
		    <div class="toast-header">
		      <!-- 上方框框的內容 -->
		      <strong class="mr-auto">貼心提醒</strong>
		      <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
		        <span aria-hidden="true">&times;</span>
		      </button>
		    </div>
		    <!-- 文字內容 P -->
		    <div class="toast-body"> 
		    	<p></p>
		    </div>
		  </div>
	</div>
	
	</div>
	</div>
	<!-- 通知訊息 Bootstrap的Toast功能 -->

	<script> 
	  $(document).ready(function () {
			
			var Table = $("#messagelist").DataTable({
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
				    	{ "data": "rearMessageId"  },
				    	{ "data": "userAccount.accountIndex"  },
		                { "data": "messageText"  },
		                { "data": "time" ,
		                  "render":function(data,type,row,meta){ //用來轉換成本地時間
		                  var time2 = new Date(data).format("yyyy-MM-dd hh:mm:ss");
		                  return time2.substr(0,4)+'/'+time2.substr(5,2)+'/'+time2.substr(8,2)+' '+time2.substr(11,5)
		                  }},
		                { "data": "updateTime",
			              "render":function(data,type,row,meta){ //用來轉換成本地時間如果更新時間不是null才執行
			            	if(data!=null){
			            		var time2 = new Date(data).format("yyyy-MM-dd hh:mm:ss");
			              		return time2.substr(0,4)+'/'+time2.substr(5,2)+'/'+time2.substr(8,2)+' '+time2.substr(11,5)
			            	}else{
			            		return '';
			            	}
			              }},
		                { "data": "condition" },
		                { "render":function(data,type,row,meta){
			                return "<button style='background-color:#00008B;border-radius:15px;' id='update'><i class='far fa-credit-card'></i></button>";
			              }},
		                { "render":function(data,type,row,meta){
			                return "<button style='background-color:#00008B;border-radius:15px;' id='delete'><i class='far fa-credit-card'></i></button>";
			              }}
		               
		                
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
					url:"/PepperNoodles/rearStage/getMessageList",
					contentType: 'application/json; charset=utf-8',
					dataType:'json',
			        async : true,
			        cache: false,
			        success:function(result){
			        	console.log("yes123");
			        	console.log(JSON.stringify(result)); //Map的List物件	
			        	console.log(result.MessageList);
			        	Table.clear().draw();
			            Table.rows.add(result.MessageList).draw();
			           
			        },
			        error: function (result) {
			        	console.log("有問題");
			        }
				});
				
				//用來轉換本地日期時間
				Date.prototype.format = function (fmt) {
					  var o = {
					    "M+": this.getMonth() + 1, //月份
					    "d+": this.getDate(), //日
					    "h+": this.getHours(), //小時
					    "m+": this.getMinutes(), //分
					    "s+": this.getSeconds(), //秒
					    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
					    "S": this.getMilliseconds() //毫秒
					  };
					  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
					  for (var k in o)
					  if (new RegExp("(" +  k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" +  o[k]).substr(("" + o[k]).length)));
					  return fmt;
					}
			  	  
				
	  });
	  
	  //呼應button更新訊息狀態
	  $('body').on('click','#update',function(e){
	       e.preventDefault();
	       
	        var status = $(this).parent().prevAll("tr td:eq(5)").text(); //編號id
	        $.ajax({
	         method:"GET",
	         url:"/PepperNoodles/rearMessageFindById?rearMessage_id="+status,
	         contentType:"application/json",
	         processData: false,
	         cache: false,  //不做快取
             async : true,
             success: function (response) {
             alert(response.userAccount.accountIndex)
             location.reload(); //成功重整頁面

           	},
            error: function (response) {
             console.log("訊息沒出去");
            } 
	        });
	        
	     
	        
	      });
	  
	  //刪除留言
	  $('body').on('click','#delete',function(e){
	       e.preventDefault();
	       if(confirm('確定刪除此筆訊息?')){ //confirm確認功能
	    	   	      
	        var status = parseInt($(this).parent().prevAll("tr td:eq(6)").text(),10); //編號id
	        //parseInt( , 10)
	        console.log(status)
	        $.ajax({
		         method:"GET",
		         url:"/PepperNoodles/rearMessageDeleteById?rearMessage_id="+status,
		         contentType:"application/json",
		         processData: false,
		         cache: false,  //不做快取
	             async : true,
	             dataType:"text",
	             success: function (response) {
	            	 if (response == "OK"){
	            		 alert(status+"已被刪除");
	            		 location.reload();
	            	 }
// 		             setTimeout(function(){
		             	//成功重整頁面
// 		             },3000);
		
	          	 },
		         error: function (response) {
		         console.log("訊息沒出去");
		         } 
	        });
	        
	       }
	        return false;
	    });
	  
	  var j = jQuery.noConflict(); //因為$吃不到才使用j = jQuery.noConflict();
	  //後台訊息通知
  	  $.ajax({
  			method:"GET",
  			url:"/PepperNoodles/informMessageCondition",
  			async : true,
  			cache : false,
  			success : function(response) {
  				var messageReplyList = response.replyList;
  				var messageNewList = response.newList;
  				var informMenu1 = $("#informMenu1");
  				var count = 0;
  				count += (messageReplyList.length + messageNewList.length);
  				j('.toast-body p').text('您有 '+count+' 則新通知');
  				j('.toast').toast({delay: 3000});
  				j('.toast').toast('show');
  				$.each(messageReplyList,function(index,element){
  					var li1 = $("<li><a href ='javascript:void(0)'>貼心提醒! 編號"+"<font color='blue'>"+element.rearMessageId+"</font>"+"已經回覆訊息囉!</a></li>");
  					informMenu1.append(li1);
  				});
  				$.each(messageNewList,function(index,element){
  					var li2 = $("<li><a href='javascript:void(0)'>新訊息! 編號"+"<font color='blue'>"+element.rearMessageId+"</font>"+" 已發布!</a></li>");
  					informMenu1.append(li2);
  				});
  			},error:function(response){
  				
  			}
  		});
	  
	
</script>


</body>
</html>