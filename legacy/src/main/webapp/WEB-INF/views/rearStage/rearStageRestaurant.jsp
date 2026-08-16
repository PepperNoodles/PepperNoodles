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
<script type="text/javascript" src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.js'/>"></script>

<style type="text/css">
	a{
		color:#FFFFFF;
	}
	
	.frame {  
    height: 120px; /*can be anything*/
    width: 120px; /*can be anything*/
    position: relative;
	}
	
	.frame2 {  
    height: 120px; /*can be anything*/
    width: 180px; /*can be anything*/
    position: relative;
	}
	
	td img{  
		object-fit: cover; 
	    max-height: 100%;  
	    max-width: 100%; 
	    width: auto;
	    height: auto;
	    position: absolute;  
	    top: 0;  
	    bottom: 0;  
	    left: 0;  
	    right: 0;  
	    margin: auto;   
  		display: block;
	}
	
	input{
	width:250px;
	}
		
	.tagButton{
	 background-color:#D26900;
	}
	
	.tagButton:hover{
	 background-color:#FFDCB9;
	 color:black;
	}
	
	.navBar{
	padding-left: 0px;
	}
	
	.mainBody{
		height:900px;
		overflow:auto;
	}
	
</style>
</head>


<body>
	
<div class="container-fluid">
	<div class="row mainBody" style ="">
		<div class="col-2 navBar" >
			<%@include file="../includePage/includeRearNav.jsp"%>
		</div>
		<div class="col-10">
		<!-- content -->

			<h2>餐廳資料</h2>
							<table  id="restlist" class="display">
								<thead>
									<tr>
										<th>編號</th>
										<th>餐廳名稱</th>
										<th>地址</th>
										<th>經度</th>
										<th>緯度</th>
										<th>電話</th>
										<th>擁有者</th>
										<th>參考照片</th>
										<th>標籤</th>
										<th>修改</th>
										<th>位置資訊<th>
<!-- 										<th>編輯</th> -->
<!-- 										<th>刪除</th> -->
<!-- 										<th>accountdetail</th> -->
<!-- 										<th>roles</th> -->
<!-- 										<th>code</th> -->
										
									</tr>
								</thead>
							
							</table>

		</div>
	</div>
	
<!-- 	<div> -->
<!-- 		<h2>EndOfPage</h2> -->
<!-- 	</div> -->
	<!-- 彈出修改視窗 -->
	<div class="modal" id="updateModal" tabindex="-1">
	  <div class="modal-dialog modal-lg">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">更新餐廳</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        	<table class="table">				 
				  <tbody>
				    <tr>
				      <td>restaurantId</td>
				      <td><input id="restaurantId" readonly="readonly"></td>
				    </tr>
				    <tr>
				  
				      <td>restaurantName</td>
				      <td><input id="restaurantName"></td>
				    </tr>
				    <tr>				  
				      <td>restaurantAddress</td>
				      <td><input id="restaurantAddress"></td>
				    </tr>
				    <tr>				  
				      <td>longitude</td>
				      <td><input id="longitude"></td>
				    </tr>
				    <tr>				  
				      <td>latitude</td>
				      <td><input id="latitude"></td>
				    </tr>
				    <tr>				  
				      <td>restaurantContact</td>
				      <td><input id="restaurantContact"></td>
				    </tr>
				    <tr>				  
				      <td>restaurantWebsite</td>
				      <td><input id="restaurantWebsite"></td>
				    </tr>
				    <tr>				  
				      <td>restaurantOwner</td>
				      <td><input id="restaurantOwner"><span id="checkUSerResult"></span></td>
				    </tr>
				     <tr>				  
				      <td>restaurantPicture
				      	
						
				      </td>
				      <td>
				      <input type="file" id="file_input" accept="image/jpeg, image/png"/>
				      <div class="frame2" id="restaurantImage" ></div></td>
				    </tr>
			
				  </tbody>
				</table>
					        
	        
	        
	        <div class="modal-footer">
	        <button type="button" class="btn-secondary" data-dismiss="modal">Close</button>
	        <button type="button" id="singleRestUpdate" class="btn-primary">Save changes</button>
	      </div>
	      </div>
</div>	      
	      
	    </div>
	  </div>
	</div>
	
	<script> 
	  $(document).ready(function () {	
		  
			var Table = $("#restlist").DataTable({
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
		                { "data": "rest.restaurantId"  },		               
		                { "data": "rest.restaurantName" },
		                { "data": "rest.restaurantAddress"  },		               
		                { "data": "rest.longitude" },
		                { "data": "rest.latitude"  },		               
		                { "data": "rest.restaurantContact" },
		                { "data": 'userIndex' },
		                { "render": function (data, type, JsonResultRow, meta) {
                            return '<img class="frame" src="<c:url value='/restSearch/restPicByid'/>'+"/"+JsonResultRow.rest.restaurantId+'">';
                        }},
                        {"render": function (data, type, JsonResultRow, meta){
                        	let buttons="";
                        	for (let i=0; i<JsonResultRow.rest.foodTag.length;i++ ){                        	
                        		buttons += '<button class="tagButton" style="border-radius:5px;" id=tag'+JsonResultRow.rest.foodTag[i].foodTagIid+'>'+JsonResultRow.rest.foodTag[i].foodTagName+'</button>'
                        	}         
                        	return buttons;
                        }},
                        { "render": function (data, type, JsonResultRow, meta) {
                            return '<button class="updateButton" style="background-color:#00008B;border-radius:15px;" id='+JsonResultRow.rest.restaurantId+'><i class="fas fa-edit"></i></button>';
                        }},
                        { "render": function (data, type, JsonResultRow, meta) {
                            return '<button class="getPositionButton" style="background-color:#00008B;border-radius:15px;" id=rest'+JsonResultRow.rest.restaurantId+'><i class="fas fa-map-marker-alt"></i></button>';
                        }}
		                  
				    ],
				    filter: true,
				    bPaginate: true,
				    info: true,
				    ordering: true,
				    processing: true,
				    retrieve: true,
				    order: [[0, 'asc']],
				    searching: true //關閉filter功能
//  	              	columnDefs: [{
//  	                  targets: [6],
//  	                  render: function(restid) {
//  	                    return '<img  src="'+"<c:url value='/restSearch/restPicByid'/>"+"/"+loca[i].restid+'">'
//  	                  }
//  	             	}]
				   
				});
			
				
				$.ajax({
					method:"GET",	
					url:"/PepperNoodles/rearStage/rearRestAll",
					contentType: 'application/json; charset=utf-8',
					dataType:'json',
			        async : true,
			        cache: false,
			        success:function(result){
// 			        	console.log("yes123");
// 			        	console.log(JSON.stringify(result));
 			        	//console.log(result[6].userIndex);
			        	Table.clear().draw();
			            Table.rows.add(result).draw();
			            
			          //  $(".updateButton").click(updateRest);
			          //  $(".getPositionButton").click(positionShow);
			        },
			        error: function (result) {
			        	console.log("有問題");
			        }
				});
				var jq = jQuery.noConflict();
				
				//檢查公司使用者
				function checkCompanyUser(){
					$("#checkUSerResult").html();
					let userAccount = this.value;
					if (userAccount!=null){
						//console.log("check user Account"+userAccount);
						let urls="/PepperNoodles/rearStage/checkCompanyUser"+"?userAccount="+userAccount;
						
						$.ajax({
							method:"GET",	
							url:urls,
							contentType: 'application/json; charset=utf-8',
							dataType:'text',
					        async : true,
					        cache: false,
					        success:function(result){
					      //  	console.log("result");
					        	switch(result) {
					        	case "ok":
					        		//$("#checkUSerResult").html("此使用者非企業會員");
					        		 alert("確認OK");
					        			break;
					        	case "ng":
					        			//$("#checkUSerResult").html("此使用者非企業會員");
					        		   alert("此使用者非企業會員");
					        			break;
					        	case "no user":
					        			//$("#checkUSerResult").html("沒有此使用者");
					        		  alert("沒有此使用者");				        	
					        			break;				        	
					        	}
					        	
					        },
					        error: function (result) {
					        	console.log("有問題");
					        }
						});
					}
					
					
				}
				
				function updateRest(){
					let id = this.id
					console.log(id);
					jq('#updateModal').modal('toggle');
					
					$.ajax({
						method:"GET",	
						url:"/PepperNoodles/rearStage/rearRest/"+id,
						contentType: 'application/json; charset=utf-8',
						dataType:'json',
				        async : true,
				        cache: false,
				        success:function(result){
				        	console.log("yes123");
				        	console.log(JSON.stringify(result));
				        	console.log(result.restaurantId);
				        	$("#restaurantId:text").val(result.rest.restaurantId);
				        	$("#restaurantName:text").val(result.rest.restaurantName);
				        	let url = "<c:url value='/restSearch/restPicByid'/>"+"/"+result.rest.restaurantId;
				        	$("#restaurantImage").html("");
				        	$("#restaurantImage").append('<img id="jsImage" src='+url+'>');
				        	
				        	$("#restaurantAddress:text").val(result.rest.restaurantAddress);
				        	$("#longitude:text").val(result.rest.longitude);
				        	$("#latitude:text").val(result.rest.latitude);
				        	$("#restaurantContact:text").val(result.rest.restaurantContact);
				        	$("#restaurantWebsite:text").val(result.rest.restaurantWebsite);
				        	$("#restaurantOwner:text").val(result.userIndex);
				        	
				        	
				        },
				        error: function (result) {
				        	console.log("有問題");
				        }
					});
				}
				
				//綁定方法
				
				$("body").on("click",".getPositionButton",positionShow);
				$("body").on("click",".updateButton",updateRest);
				$("body").on("change","#restaurantOwner",checkCompanyUser);
				
				//上傳並顯示圖片
				function readURL(input) {
					  if (input.files && input.files[0]) {
					    var reader = new FileReader();					    
					    reader.onload = function(e) {
					      $('#jsImage').attr('src', e.target.result);
					    }					    
					    reader.readAsDataURL(input.files[0]); // convert to base64 string
					  }
					}

				$("#file_input").change(function() {
					 readURL(this);
				});
				
				//顯示位址
				function positionShow(){
					console.log(this.id);
					let restId = this.id.slice(4,);
					
					let url = "<c:url value='/restSearch/userSingleRestPage/' />" + restId;
					features = "width="+1200+",height="+600+",top="+50+",left="+50; 
					window.open(url,"toolbar=no,location=no,directories=no",features);
				}
				
				
				//更新
				$("#singleRestUpdate").click(function(){
					//let fileUploader = document.querySelector('#file_input');
					let selectedFile = $('#file_input').get(0).files[0];
					 console.log(typeof(selectedFile));
					 console.log(selectedFile);
				// console.log(fileUploader.target.files); 
					
					
					let formdata = new FormData();
					
					let blob = new Blob([ JSON.stringify({
						"restaurantId":$("#restaurantId:text").val(),
						"restaurantName": $("#restaurantName:text").val(),
						"restaurantAddress": $("#restaurantAddress:text").val(),
						"longitude": $("#longitude:text").val(),
						"latitude": $("#latitude:text").val(),
						"restaurantContact": $("#restaurantContact:text").val(),
						"restaurantWebsite": $("#restaurantWebsite:text").val(),
						"restaurantOwner": $("#restaurantOwner:text").val()
						
					})]);
				
					formdata.append('restinfo',blob);
					formdata.append('restImg',selectedFile);	
					
					
					$.ajax({
						method:"PUT",	
						url:"/PepperNoodles/rearStage/updateRest",
						//contentType: 'application/x-www-form-urlencoded',
						//contentType:'multipart/form-data',
						contentType:false,
						//dataType:'application/json; charset=utf-8',
						dataType:'text',
						data:formdata,
						processData: false, 
				        async : true,
				        cache: false,
				        success:function(result){
				        	console.log("====="+result)
				        	let refreshUrl = "<c:url value='/rearStage/rest' />"
				        	window.location.href = refreshUrl;
				        },
				        error: function (result) {
				        	console.log("有問題");
				        }
					});
					
				})
				
				
	  });
  </script>
	  

</body>
</html>