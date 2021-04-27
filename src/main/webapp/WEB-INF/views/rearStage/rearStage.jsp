<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Rear Stahe</title>
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
		$("#submitproduct").click(function() {
			event.preventDefault();
			var pname = $("#productname").val();
			var pprice = $("#productprice").val();
			var pdes = $("#productdescription").val();
			var pquantity = $("#productquantity").val();
			//補寫檢查
				data = new FormData();
		    	data.append('file', $('#wizard-picture')[0].files[0]);
				data.append('productinfo',new Blob([JSON.stringify( {"productName": pname,"productPrice": pprice,"description" : pdes,"quantity" : pquantity} )],{type: "application/json"}));
				$.ajax({
					method:"POST",
					url:"/PepperNoodles/insproduct",
					data:data,
					processData: false,
					contentType: false, 
					cache: false,  //不做快取
			        async : true,
			        success: function (result) {
			            $("#checkStatus").text(result.success);
			            secondPageError = true;			            
			        },
			        error: function (result) {
			            $("#checkStatus").text(result.fail);
			        }
				})
			
		})//end
		
		
		//$("#addP").onclick(function(){
			//pname="橘子20斤折價券";
			
		//});
		
		
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
	padding:20px;
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
#myBtn {
	  position: fixed;
	  bottom: 100px;
	  right: 30px;
	  z-index: 99;
	  font-size: 18px;
	  border: none;
	  outline: none;
	  background-color: #A52A2A;
	  color: black;
	  cursor: pointer;
	  padding: 15px;
	  border-radius: 4px;
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
                        	<!-- left bar write here -->
                        	</div>
                        </div>
                    </div>
                    <!-- Right content -->
                    <div class="col-xl-8 col-lg-8 col-md-6" style="border:1px solid red;padding:20px;">
                        <!-- listing Details Start-->
                        <div class="listing-details-area">
	                        <div class="row" id="Page1" >
	                            <div class=" col-lg-12">
	                                <h3 style="border-bottom: 1px solid #D3D3D3;float:left;">新增商品</h3> 
	                                <!-- product frame start -->
	                                <div class="row productFrame">
		                                <div class="col-sm-12 col-sm-offset-1">
											<div class="picture-container">
												<div class="picture">
													<img src="<c:url value="/images/NoImage/NoImage.png"/>"
															class="picture-src" id="wizardPicturePreview" style="display: block;width: 280x;height: 250px;border: 1px solid darkblue;" />
													<input type="file" id="wizard-picture" accept="image/*" style="margin-top: 20px;margin-bottom: 10px;">
												</div>
											</div>
										</div>
		                                <label>Name <small>(必填)</small></label>
										<input class="form-control" type="text" name="userName" id="productname" placeholder="">	
		                                <label>Price <small>(必填)</small></label>
										<input class="form-control" type="text" name="userName" id="productprice" placeholder="">	
		                                <label>Description <small>(必填)</small></label>
										<textarea class="form-control" id="productdescription" rows="2" cols="50" name="s1" style="display: block;"></textarea>	
		                                <label>Quantity<small>(估計販賣數量)</small></label>
										<input class="form-control" id="productquantity" type="number" placeholder="" step="1" min="0" max="100">	
	                                	<div style="margin-top: 20px;">
		                                	<input type="submit"  id="submitproduct"  value="提交" >
		                                	<span id="checkStatus" style="font-size: 18px;"></span>
	                                	</div>
	                                </div>
	                            </div>
	                            
	                        </div>
                        </div>
                        
                        <!-- listing Details End -->
                        <!--Pagination Start  -->
                        <div class="pagination-area pt-70 text-center" id="pagination">
                            <div class="container">
                                <div class="row">
                                    <div class="col-xl-12">
                                        <div class="single-wrap d-flex justify-content-center">
                                            <nav aria-label="Page navigation example">
                                                <ul class="pagination justify-content-start">
                                                    <li class="page-item active"><a class="page-link" href="#">01</a></li>
                                                    <li class="page-item"><a class="page-link" href="#">02</a></li>
                                                    <li class="page-item"><a class="page-link" href="#">03</a></li>
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
    
    <div  id="myBtn" title="Go to top">
			<button id="addP" style="color: black;">一鍵新增1</button>
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
		<!-- All JS Custom Plugins Link Here here -->
        <script src="<c:url value='/scripts/vendor/modernizr-3.5.0.min.js' />"></script>
		<!-- Jquery, Popper, Bootstrap -->
		<script src="<c:url value='/scripts/vendor/jquery-1.12.4.min.js' />"></script>
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