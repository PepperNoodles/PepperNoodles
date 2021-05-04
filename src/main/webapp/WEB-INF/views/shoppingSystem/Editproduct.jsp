<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Product</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<script src="https://code.jquery.com/jquery-3.6.0.js" ></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.min.js"></script>
<script>
$(document).ready(function() {
	
	

	var productid = localStorage.getItem("edit");
	$.ajax({
		method : "GET",
		url : "openProductToEdit?id=" + productid + "",
		async : true,
		cache : false,
		success : function(response) {
			var product = response.productToEdit
			$('#productname').val(product.productName);
			$("#productprice").val(product.productPrice);
			$("#productdescription").val(product.description);
			$("#productquantity").val(product.quantity);
			$('#detailclass option[value='+product.fkProductDetailClassId+']').attr('selected','selected');
			if (product.status=="上架中"){
				$("#upshelf").prop("checked",true);
			}else{
				$("#offshelf").prop("checked",true);
			}
			var div1 = $('<div></div>');
			div1.append("<img class='picture-src' id='wizardPicturePreview' style='margin-left:12%;object-fit:cover;display:block;width:500px;height:300px;border:1px solid darkblue' src='../../getProductImages?no="+productid+"' />");
			div1.append("<img class='picture-src' id='wizardPicturePreview2' style='margin-left:12%;object-fit:cover;display:none;width:500px;height:300px;border:1px solid darkblue' src='<c:url value='/images/NoImage/NoImage.png'/>' />");
			div1.append("<input type='file' id='wizard-picture' accept='image/*' style='margin-left:40%;margin-top: 20px; margin-bottom: 10px;'>");
			div1.appendTo(".picture-container");
		},
		error : function(response) {
			console.log("查詢失敗")
		}
	});
	
	$('body').on("change","#wizard-picture",function(){
		if (this.files) {
			var reader = new FileReader();
			reader.onload = function (e) {
				$('#wizardPicturePreview2').attr('src', e.target.result);
				$("#wizardPicturePreview").hide();
				$("#wizardPicturePreview2").show();
			}
			reader.readAsDataURL(this.files[0]);
		}
	});
	
	$('body').on("click","#editproduct",function(e){
		e.preventDefault();
		var mymessage=confirm("確定修改此產品嗎？");
		if(mymessage==true){
			var pname = $("#productname").val();
			var pprice = $("#productprice").val();
			var pdescription = $("#productdescription").val();
			var pquantity = $("#productquantity").val();
			var detailclass = $('#detailclass :selected').val();
			var editshelfval2 = $("input[name='editshelf']:checked").val();
			alert(editshelf);
			data = new FormData();
			data.append('file', $('#wizard-picture')[0].files[0]);
			data.append('productinfo', new Blob([ JSON.stringify({
				"productid" : productid,
				"productName" : pname,
				"productPrice" : pprice,
				"description" : pdescription,
				"quantity" : pquantity,
				"productDetailClass" : detailclass,
				"status":editshelf
			}) ], {type : "application/json"}));
			$.ajax({
				method : "POST",
				url : "/PepperNoodles/editproduct",
				data:data,
				processData : false,
				contentType : false,
				cache : false,
				async : true,
				dataType:"text",
				success : function(result) {
					alert("修改成功");
					location.reload();
				},error : function(result) {
					$("#checkAccountStatus2").text(result.fail); //填入提示訊息到result標籤內
				}
			});
		}
		else if(mymessage==false){
		}
	});
	
	
});
</script>

<style type="text/css">
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+TC&display=swap');
body {
	background-color: #FAEBD7;
	font-size: 20px;
}
.productFrame{
 	padding:20px; 
	clear: both;
}
</style>
</head>
<body>
	<div class="container">
		<div class="row productFrame">
			<div class="col-12">
				<div class="picture-container">
				</div>
			</div>
			<div class="col-12" >
				<label>產品名 <small>(必填)</small></label> <input class="form-control"
					type="text" name="userName" id="productname" placeholder="">
				<label>產品價格 <small>(必填)</small></label> <input class="form-control"
					type="text" name="userName" id="productprice" placeholder="">
				<label>產品描述 <small>(必填)</small></label>
				<textarea class="form-control" id="productdescription" rows="2"
					cols="50" name="s1" style="display: block;"></textarea>
				<label>庫存數量<small>(估計販賣數量)</small></label> <input
					class="form-control" id="productquantity" type="number"
					placeholder="" step="1" min="0" max="100">
			</div>
			<div class="col-12 " style="text-align: left; font-size: 18px; margin-top: 5px; margin-bottom: 5px;">
				商品類別:
			</div>
			<div class="col-12" style="">
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
				<input type="radio" id="upshelf" name="editshelf" value="上架中">
				<span style="font-size:18px;">下架</span>
				<input type="radio" id="offshelf" name="editshelf" value="下架中">
			</div>
			<div class="col-12" style="margin-top: 20px; text-align: center;">
				<input type="submit" id="editproduct" value="修改"> <span
					id="checkStatus" style="font-size: 18px;"></span>
			</div>
		</div>
	</div>
	
	
	
	
	
</body>
</html>