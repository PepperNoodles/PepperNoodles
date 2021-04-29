<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel='stylesheet' -->
<%-- 	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" /> --%>
<!-- <link rel="stylesheet" -->
<%-- 	href="<c:url value='/css/fontawesome-all.min.css' />" /> --%>
<!-- <script type="text/javascript" -->
<%-- 	src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script> --%>
<!-- <script type="text/javascript" -->
<%-- 	src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script> --%>


<title>backstage</title>
<style>
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
.well a:hover{
 	text-decoration: none;
 	background-color: #6d6d6d;	
}

.well a{
	color:#000000;
}

</style>

</head>
<body>
<div class="just-padding">

<div class="list-group list-group-root well">
  
  <a href="#item-1" class="list-group-item" data-toggle="collapse">
    <i class="glyphicon glyphicon-chevron-right"></i>會員管理
  </a>
  <div class="list-group collapse" id="item-1">
    
    <a href="<c:url value='/rearStage/rearNormalUser'/>" class="list-group-item">
      <i class="glyphicon glyphicon-chevron-right"></i>一般會員
    </a>
   
    
    <a href="#item-1-2" class="list-group-item" data-toggle="collapse">
      <i class="glyphicon glyphicon-chevron-right"></i>企業會員
    </a>
<!--     <div class="list-group collapse" id="item-1-2"> -->
<!--       <a href="#" class="list-group-item">Item 1.2.1</a> -->
<!--       <a href="#" class="list-group-item">Item 1.2.2</a> -->
<!--       <a href="#" class="list-group-item">Item 1.2.3</a> -->
<!--     </div> -->
    
    <a href="#item-1-3" class="list-group-item" data-toggle="collapse">
      <i class="glyphicon glyphicon-chevron-right"></i>後台管理
    </a>
    <div class="list-group collapse" id="item-1-3">
      <a href="#" class="list-group-item">Item 1.3.1</a>
      <a href="#" class="list-group-item">Item 1.3.2</a>
      <a href="#" class="list-group-item">Item 1.3.3</a>
    </div>
    
  </div>
  
  <a href="#item-2" class="list-group-item" data-toggle="collapse">
    <i class="glyphicon glyphicon-chevron-right"></i>餐廳
  </a>
  <div class="list-group collapse" id="item-2">
    
    <a href="#item-2-1" class="list-group-item" data-toggle="collapse">
      <i class="glyphicon glyphicon-chevron-right"></i>Item 2.1
    </a>
    <div class="list-group collapse" id="item-2-1">
      <a href="#" class="list-group-item">Item 2.1.1</a>
      <a href="#" class="list-group-item">Item 2.1.2</a>
      <a href="#" class="list-group-item">Item 2.1.3</a>
    </div>
    
    <a href="#item-2-2" class="list-group-item" data-toggle="collapse">
      <i class="glyphicon glyphicon-chevron-right"></i>Item 2.2
    </a>
    <div class="list-group collapse" id="item-2-2">
      <a href="#" class="list-group-item">Item 2.2.1</a>
      <a href="#" class="list-group-item">Item 2.2.2</a>
      <a href="#" class="list-group-item">Item 2.2.3</a>
    </div>
    
    <a href="#item-2-3" class="list-group-item" data-toggle="collapse">
      <i class="glyphicon glyphicon-chevron-right"></i>Item 2.3
    </a>
    <div class="list-group collapse" id="item-2-3">
      <a href="#" class="list-group-item">Item 2.3.1</a>
      <a href="#" class="list-group-item">Item 2.3.2</a>
      <a href="#" class="list-group-item">Item 2.3.3</a>
    </div>
    
  </div>
  
  
  <a href="#item-3" class="list-group-item" data-toggle="collapse">
    <i class="glyphicon glyphicon-chevron-right"></i>產品
  </a>
  <div class="list-group collapse" id="item-3">
    
    <a href="#item-3-1" class="list-group-item">
      <i class="glyphicon glyphicon-chevron-right"></i>Item 3.1
    </a>

    
    <a href="#item-3-2" class="list-group-item">
      <i class="glyphicon glyphicon-chevron-right"></i>Item 3.2
    </a>

    <a href="#item-3-3" class="list-group-item" >
      <i class="glyphicon glyphicon-chevron-right"></i>Item 3.3
    </a>
  </div>
 
   <a href="#item-4" class="list-group-item" >
    <i class="glyphicon glyphicon-chevron-right"></i>Item 4
  </a>
    

  
</div>
  
</div>	
	
	
	
	
	
<script type="text/javascript">
var $j = jQuery.noConflict();


	$j(function() {    
		  $('.list-group-item').on('click', function() {
		    $('.glyphicon', this)
		      .toggleClass('glyphicon-chevron-right')
		      .toggleClass('glyphicon-chevron-down');
		  });

		});



	


</script>


</body>
</html>