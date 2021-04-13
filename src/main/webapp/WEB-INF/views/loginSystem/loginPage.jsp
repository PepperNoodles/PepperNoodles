<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
<link rel="Shortcut icon" href="<c:url value='/images/icon/favicon-PepperNoodles.ico' />">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0'name='viewport' />
<!--     Fonts and icons     -->
<link href="http://netdna.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.css"	rel="stylesheet">
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js" integrity="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+" crossorigin="anonymous"></script>
<!-- CSS Files -->
<script type="text/javascript" src="<c:url value='/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/bootstrap.min.css' />" />
<link href="<c:url value='/css/gsdk-bootstrap-wizard.css' />" rel="stylesheet" />

<script>
window.onload = function() {
	//選擇是哪一種帳號的表單變數
// 	document.getElementsByName("accountType").addEventListener("change", checkAccountType);

	
	var hasError = false;
	var hasErrorpwd = false;
	var hasErrorCheckEmail = false;
	var checkState = false;
	var check = document.getElementById("UserEmail");
	var nextSlide = document.getElementById("nextSlide");//下一步
	var checkAccountStatus = document.getElementById("checkAccountStatus");
	var userValueinput = document.getElementById("UserEmail");
	var div1 = document.getElementById("result");
	
	
	
	//帳號驗證
	check.onblur = function() {
		var userValue = document.getElementById("UserEmail").value;
		if (!userValue) {
			userValueinput.style.border="2px solid red";
			div1.innerHTML = "<font color='red' size='-1'>Please Enter</font>";
			hasError = false;
			return;
		} else if (!isEmail(userValue)) {
			userValueinput.style.border="2px solid red";
			setErrorFor(div1,"<font color='red' size='-2'>Format Wrong</font>");
			hasError = false;
			return;
		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "<c:url value='/CheckMemberAccount' />", true);
		var jsonAccount = {
			"accountIndex" : userValue
		}
		xhr.setRequestHeader("Content-Type", "application/json");
//			alert(JSON.stringify(jsonAccount));//debug
		xhr.send(JSON.stringify(jsonAccount));
		var message = "";
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				accountResult = JSON.parse(xhr.responseText);
//					alert(accountResulst);
				if ( accountResult.username.length == 0  ) {
					message = "<font color='green' size='-2'>帳號可用</font>";
					userValueinput.style.border="2px solid green";
					div1.innerHTML = message;
					hasError = true;
				} else if (accountResult.username.startsWith("Error")) {
					message = "<font color='red' size='-2'>發生錯誤</font>";
					userValueinput.style.border="2px solid red";
					div1.innerHTML = message;
					hasError = false;
				} else {
					message = "<font color='red' size='-2'>帳號重複，請重新輸入帳號</font>";
					userValueinput.style.border="2px solid red";
					div1.innerHTML = message;
					hasError = false;
				}
			}
		}
	}//end
	
	//email驗證碼
	var checkmail = document.getElementById("checkMail");
	var verifyCodeServer;
	checkmail.onclick = function(){
		hasErrorCheckEmail = false;
		var userValue = document.getElementById("UserEmail").value;
		var verifycodeVal = document.getElementById("verifycode").value;
		var checkMailInput = document.getElementById("checkMailInput");
		checkMailInput.innerHTML = "";
		if (!userValue) {//再檢查一次email有沒有輸入
			cheqMailResult.innerHTML = "<font color='red' size='-1'>Please Enter</font>";
			hasErrorCheckEmail = false;
			return;
		} else if (!isEmail(userValue)) {
			setErrorFor(cheqMailResult,"<font color='red' size='-2'>Format Wrong</font>");
			hasErrorCheckEmail = false;
			return;
		}else {
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "<c:url value='/sendEmail' />", true);
			var jsonAccountEmailCheck = {
				"accountIndex" : userValue
			}
			xhr.setRequestHeader("Content-Type", "application/json");
			//alert(jsonAccountEmailCheck);
			xhr.send(JSON.stringify(jsonAccountEmailCheck));
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4 && xhr.status == 200) {
					checkMailInput.innerHTML = "<font color='blue'>驗證碼已寄</font>";
					accountCode = JSON.parse(xhr.responseText);
					verifyCodeServer = accountCode.emailCode;
					//alert(accountCode.emailCode);
				}
			}
		}
			
	}//end
	
	//驗證碼驗證
	var verifycodeInput = document.getElementById("verifycode");
	var cheqMailResult = document.getElementById("cheqMailResult");
	verifycodeInput.onblur = function(){
		var verifycodeVal = document.getElementById("verifycode").value;
		if (verifycodeVal.length==""){
			verifycodeInput.style.border="2px solid red";
			cheqMailResult.innerHTML = "<font color='red' >Please Enter</font>";
			hasErrorCheckEmail = false;
		}else if (verifyCodeServer==verifycodeVal){
// 			alert(verifycodeVal);
			verifycodeInput.style.border="2px solid green";
			cheqMailResult.innerHTML = "<font color='green' >Correct</font>";
			hasErrorCheckEmail = true;
		}else{
			verifycodeInput.style.border="2px solid red";
			cheqMailResult.innerHTML = "<font color='red' >Verify Code is Wrong</font>";
			hasErrorCheckEmail = false;
		}
	}
	

	//密碼驗證
	var sendPwd = document.getElementById("userPwd");
	var div2 = document.getElementById("result2");
	sendPwd.onblur = function() {
		hasErrorpwd = false;
		var nextSlide = document.getElementById("nextSlide");//下一步
		//讀取欄位資料
		var userValue = document.getElementById("UserEmail").value;
		var pwdValue = document.getElementById("userPwd").value;
		var div1 = document.getElementById("result");
		if (!pwdValue) {
			sendPwd.style.border="2px solid red";
			setErrorFor(div2,"<font color='red' >Please Enter</font>");
			hasErrorpwd = false;
		} else if (!isPWD(pwdValue)) {
			sendPwd.style.border="2px solid red";
			setErrorFor(div2,
			"<font color='red'>Please Enter more than 6 words，including letters,numbers,special symbols</font>");
			hasErrorpwd = false;
		} else {
			sendPwd.style.border="2px solid green";
			div2.innerHTML = "<font color='green'>密碼可用</font>";
			hasErrorpwd = true;
		}
	}//end
	
		//下一頁-使用者
		var nextSlide = document.getElementById("nextSlide");//下一步
		var accoutPage1 = document.getElementById("accoutPage1");
		var accoutDetailPage2 = document.getElementById("accoutDetailPage2");
		nextSlide.onclick = function() {
			var accountType = document.getElementsByName("accountType");
			
			console.log(accountType[0]);
			for (var i = 0; i< accountType.length; i++) {
		  		if (accountType[i].checked) {
		  			accountTypeVal = accountType[i].value;
			    	break;
		  		}
			}
			var privacycheck = privacyornot();
			if (hasError && hasErrorpwd && hasErrorCheckEmail && privacycheck){
				console.log(accountTypeVal);
				if(accountTypeVal =="user"){
				accoutPage1.classList.add("tohide");
				accoutDetailPage2.classList.remove("tohide");
				accoutDetailPage2.classList.add("toshow");
				}
				if(accountTypeVal == "company"){
				accoutPage1.classList.add("tohide");
				comDetailPage2.classList.remove("tohide");
				comDetailPage2.classList.add("toshow");
				}
				
			}else {
				checkAccountStatus.innerHTML="<font color='red' >請輸入正確資訊</font>";
			}
		}
//第二頁
		var nicknameinput = document.getElementById("nickname");
		var nickNameresult = document.getElementById("nickNameresult");
		var realName = document.getElementById("realName").value;
		var realNameinput = document.getElementById("realName");
		var realNameresult = document.getElementById("realNameresult");
		var phoneNumber = document.getElementById("phoneNumber").value;
		var phoneNumberinput = document.getElementById("phoneNumber");
		var phoneNumberresult = document.getElementById("phoneNumberresult");
		var birthday = document.getElementById("birthday").value;
		var birthdayinput = document.getElementById("birthday");
		var dateresult = document.getElementById("dateresult");
		var location = document.getElementById("location").value;
		var nextSlide2 = document.getElementById("nextSlide2");//下一步2
		var lastSlide2 = document.getElementById("lastSlide2");//上一步2
		var checkAccountStatus2 = document.getElementById("checkAccountStatus2");
		var secondPageError = false;
		
		nicknameinput.onblur = function (){
			var nickname = document.getElementById("nickname").value;
            nicknamelen = nickname.length;
            flaga =false;
            rech = /^[\u4E00-\u9fff]{3,}$/;
            if (nicknamelen==""){
            	nickNameresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>Please enter</em></font>";
            	secondPageError = false;
            } else if (nicknamelen>2){             
                if (rech.test(nickname)){
                    flaga=true;
                }
                if (flaga){
                	nickNameresult.innerHTML="<i class="+'"fas fa-check-circle"'+"style="+"color:green"+"></i> correct";
                	secondPageError = true;
                }
                else{
                	nickNameresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>incorrect</em></font>";
                	secondPageError = false;
                }
            }
            else{
            	nickNameresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>please enter 3 words!</em></font>";
            	secondPageError = false;
            }
        }
		
		realNameinput.onblur = function (){
			var realName = document.getElementById("realName").value;
            realnamelen = realName.length;
            flaga =false;
            rech = /^[\u4E00-\u9fff]{3,}$/;
            if (realnamelen==""){
            	realNameresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>Please enter</em></font>";
            	secondPageError = false;
            } else if (realnamelen>2){             
                if (rech.test(realName)){
                    flaga=true;
                }
                if (flaga){
                	realNameresult.innerHTML="<i class="+'"fas fa-check-circle"'+"style="+"color:green"+"></i> correct";
                	secondPageError = true;
                }
                else{
                	realNameresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>incorrect</em></font>";
                	secondPageError = false;
                }
            }
            else{
            	realNameresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>Please enter 3 words!</em></font>";
            	secondPageError = false;
            }
        }
		
		phoneNumberinput.onblur=function(){
			var phoneNumber = document.getElementById("phoneNumber").value;
			phonelength = phoneNumber.length;
			flaga =false;
			if (phonelength==""){
				phoneNumberresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>must enter</em></font>";
				secondPageError = false;
			}else if (phonelength==10){
				if (isPhoneNum(phoneNumber)){
					 flaga=true;
				}
				if (flaga){
					phoneNumberresult.innerHTML="<i class="+'"fas fa-check-circle"'+"style="+"color:green"+"></i> correct";
				}else {
					phoneNumberresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>incorrect</em></font>";
				}
			}else {
				phoneNumberresult.innerHTML="<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>Please Enter 10 words</em></font>";
			}
		}
		
		birthdayinput.onblur =function(){
            var birthday = document.getElementById("birthday").value;
            let birthdayLength = birthday.length;
            let flag2 = false ;
            // re = /^((19|20)?[0-9]{2}[- /.](0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01]))*$/;
            re = /^(?:\d{4}\/(?:(?:(?:(?:0[13578]|1[02])\/(?:0[1-9]|[1-2][0-9]|3[01]))|(?:(?:0[469]|11)\/(?:0[1-9]|[1-2][0-9]|30))|(?:02\/(?:0[1-9]|1[0-9]|2[0-8]))))|(?:(?:\d{2}(?:0[48]|[2468][048]|[13579][26]))|(?:(?:[02468][048])|[13579][26])00)\/02\/29)$/;
            if (birthdayLength==""){
            	dateresult.innerHTML = "<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>must enter</em></font>";
            }
            else if (birthdayLength>=10){
                if (re.test(birthday)){
                    flag2 = true;
                }
                if (flag2){
                	dateresult.innerHTML = "<i class="+'"fas fa-check-circle"'+"style="+"color:green"+"></i> correct";
                	secondPageError = true;
                }
                else {
                	dateresult.innerHTML = "<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>incorrect</em></font>";
                	secondPageError = false;
                }
            }
            else {
            	dateresult.innerHTML = "<i class="+'"fas fa-times-circle"'+"style="+"color:red"+"></i> <font color=red><em>格式輸入不正確(月份、日期請加 0 ) e.g 2020/02/07</em></font>";
            	secondPageError = false;
            }
        }
	 	
		//傳值
		var nextSlide2 = document.getElementById("nextSlide2");//下一步2
		var tagPage3 = document.getElementById("tagPage3");
		
		nextSlide2.onclick = function(event) {
			event.preventDefault();
			var radios = document.getElementsByName('gender');
			var sexValue;
			for (var i = 0, length = radios.length; i < length; i++) {
		  		if (radios[i].checked) {
			   	 	sexValue = radios[i].value;
			    	break;
		  		}
			}
			var userValue = $("#UserEmail").val();
			var pwdValue = $("#userPwd").val();
			var nickname = $("#nickname").val();
			var realName = $("#realName").val();
			var phoneNumber = $("#phoneNumber").val();
			var birthday = $("#birthday").val();
			var location = $("#location").val();
			var checkAccountStatus2 = document.getElementById("checkAccountStatus2");
			//
			//
			if (secondPageError){
// 				var xhr1 = new XMLHttpRequest();
// 				xhr1.open("POST", "<c:url value='/members' />", true);
				data = new FormData();
		    	data.append('file', $('#wizard-picture')[0].files[0]);
				data.append('userinfo',new Blob([JSON.stringify( {"accountIndex": userValue,"password": pwdValue,"realName" : realName,"nickname" : nickname,"phoneNumber" :phoneNumber,"birthDay" : birthday,"gender" :sexValue,"location": location} )],{type: "application/json"}));
// 				var jsonMember = {
// 					"account" : userValue,
// 					"password" : pwdValue,
// 					"realName" : realName,
// 					"nickname" : nickname,
// 					"phoneNumber" :phoneNumber,
// 					"birthDay" : birthday,
// 					"sex" :sexValue,
// 					"Location": location
// 				}
				$.ajax({
					method:"POST",
					url:"/PepperNoodles/members",
					data:data,
					processData: false,
					contentType: false, 
					cache: false,  //不做快取
			        async : true,
			        success: function (result) {
			            $("#checkAccountStatus2").text(result.success);//填入提示訊息到result標籤內
			            secondPageError = true;
			            accoutDetailPage2.classList.remove("toshow");
						accoutDetailPage2.classList.add("tohide");
						tagPage3.classList.remove("tohide");
						tagPage3.classList.add("toshow");
						alert("123");
			        },
			        error: function (result) {
			            $("#checkAccountStatus2").text(result.fail); //填入提示訊息到result標籤內
			        }
				})

// 				xhr1.setRequestHeader("Content-Type", "multipart/form-data");
// 				alert(JSON.stringify(jsonMember));//debug
// 				xhr1.send(JSON.stringify(jsonMember));
// 				xhr1.send(data);
// 				xhr1.onreadystatechange = function() {
// 					if (xhr1.readyState == 4 && (xhr1.status == 200 || xhr1.status == 201)) {
// 						result = JSON.parse(xhr1.responseText);
// 						if (result.fail) {
// 							checkAccountStatus2.innerHTML = "<font color='red' >"+ result.fail + "</font>";
// 						} else if (result.success) {
// 							checkAccountStatus2.innerHTML = "<font color='green' >"	+ result.success + "</font>";
// 							secondPageError = true;
							
// 						}
// 					}
// 				}
			}
		}//end

//回上一頁-會員
		var lastSlide2 = document.getElementById("lastSlide2");
		lastSlide2.onclick = function() {
			var accoutPage1 = document.getElementById("accoutPage1");
			var accoutDetailPage2 = document.getElementById("accoutDetailPage2");
			accoutDetailPage2.classList.remove("toshow");
			accoutDetailPage2.classList.add("tohide");
			accoutPage1.classList.remove("tohide");
			accoutPage1.classList.add("toshow");
		}
//回上一頁-企業
		var comlastSlide2 = document.getElementById("comlastSlide2");
		comlastSlide2.onclick = function() {
			var accoutPage1 = document.getElementById("accoutPage1");
			var comDetailPage2 = document.getElementById("comDetailPage2");
			comDetailPage2.classList.remove("toshow");
			comDetailPage2.classList.add("tohide");
			accoutPage1.classList.remove("tohide");
			accoutPage1.classList.add("toshow");
		}
		
		//一鍵新增-UserPage
		var addmember = document.getElementById("addMember");
		addmember.onclick = function(){
			var userValue = document.getElementById("UserEmail");
			var pwdValue = document.getElementById("userPwd");
			userValue.value="chrislo5311@gmail.com";
			pwdValue.value="a123456@";
			hasError = true;
			hasErrorpwd = true;
		}
		
		var addMemberDetail = document.getElementById("addMemberDetail");			
		addMemberDetail.onclick = function(){
			var nickname = document.getElementById("nickname");
			var realName = document.getElementById("realName");
			var phoneNumber = document.getElementById("phoneNumber");
			var birthday = document.getElementById("birthday");
			nickname.value = "史帝夫大哥";
			realName.value = "史帝夫哥好強阿";
			phoneNumber.value = "0916175248";
			birthday.value ="1977/01/01";
		}
		
		//第3頁
		var nextSlide3 = document.getElementById("sendData");//下一步
		nextSlide3.onclick = function() {
			var hobby = document.getElementsByName("hobby");
			
			var hobbyVal = [];
			for (var i = 0; i< hobby.length; i++) {
		  		if (hobby[i].checked) {
		  			hobbyVal.push(hobby[i].value);
		  		}
			}
	  		console.log(hobbyVal);
	  		
	  	//interest 傳值
	  	var divResult = document.getElementById("divResult");
	  		var xhr = new XMLHttpRequest();
			xhr.open("POST", "<c:url value='/addAccountInterest' />", true);

			xhr.setRequestHeader("Content-Type", "application/json");
			// 			alert(JSON.stringify(jsonAccount));//debug
			xhr.send(JSON.stringify(hobbyVal));
			var message = "";
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4 && xhr.status == 200) {
// 					interestResult = JSON.parse(xhr.responseText);
// 					alert(interestResult);
// 					alert(interestResult.success);
// 					alert(interestResult.success.value);
					if (interestResult.success == 1) {
						message = "<span><font color='green' size='-2'>興趣新增成功</font></span>";
						console.log("興趣新增成功");
						
						} else {
						message = "<span><font color='green' size='-2'>興趣之後還可以繼續修改與填寫唷:))</font></span>";
						
					}
				}
				divResult.innerHTML = message;
			}

		}
		
		//下一頁-企業端
		$(document).ready(function(){
			//確認表格皆填完整
			var hasErrorComRealname = false;
			var hasErrorComPhone = false;
			var hasErrorComLocation = false;
			//前端判斷是否輸入正確
			$("#comRealname").blur(function(){
				let value=$(this).val();
			    let txt="";
			    if(value==""){
			    	$("#comRealnameResult").css({"color":"red","font-size":"small"});
			    	$("#comRealname").css({"border":"2px solid red"});
			    	txt="<span>企業名稱不可為空白</span>";
			    	hasErrorComRealname = false;
			    }
			    if(value.length<2){
			    	$("#comRealnameResult").css({"color":"red","font-size":"small"});
			    	$("#comRealname").css({"border":"2px solid red"});
			    	txt="<span>名稱需至少2個字</span>";
			    	hasErrorComRealname = false;
			    }
			    else{
			    	$("#comRealname").css("border","2px solid green");
			        txt="&emsp;";
			        hasErrorComRealname = true;
			    }
			    $("#comRealnameResult").html(txt);
			});

			$("#comPhonenumber").blur(function(){
				let value=$(this).val();
			    let txt="";
			    if(value==""){
			    	$("#comPhotoResult").css({"color":"red","font-size":"small"});
			    	$("#comPhonenumber").css({"border":"2px solid red"});
			    	txt="<span>請輸入連絡電話</span>";
			    	hasErrorComPhone = false;
			    }
			    else{
			    	for (let i = 0; i < value.length; i++) {
			            let ch = value.charAt(i);
			            if(ch>=0&&ch<=9){
			                txt="&emsp;";
			            $("#comPhonenumber").css("border","2px solid green");
			            hasErrorComPhone = true;
			            }
			            else{
			            	$("#comPhotoResult").css({"color":"red","font-size":"small"});
			    			$("#comPhonenumber").css({"border":"2px solid red"});
			                txt="<span>只能輸入數字</span>";
			                hasErrorComPhone = false;
			            }
			        }
			    }
			    $("#comPhotoResult").html(txt);
			});

			$("#comLocation").blur(function(){
				let value=$(this).val();
			    let txt="";
			    if(value==""){
			    	$("#comLocationResult").css({"color":"red","font-size":"small"});
			    	$("#comLocation").css({"border":"2px solid red"});
			    	txt="<span>地址不可為空白</span>";
			    	hasErrorComLocation = false;
			    }
			    else{
			    	$("#comLocation").css("border","2px solid green");
			        txt="&emsp;";
			        hasErrorComLocation = true;
			    }
			    $("#comLocationResult").html(txt);
			});

			//一鍵新增
			$("#addcompany").click(function(){
				$("#UserEmail").val('ting0420a@gmail.com');
				$("#userPwd").val('123!Q123');
				$("#privacycheck").prop("checked", true);
				$("#company").prop("checked", true);
				hasError = true;
				hasErrorpwd = true;
				privacycheck = true;
			});
			
			$("#signinCompany").click(function(){
				$("#comRealname").val('黯然消魂麵館');
				$("#comPhonenumber").val('09123456789');
				$("#comLocation").val('台北市中正路二段158號1樓');
				hasErrorComRealname = true;
				hasErrorComPhone = true;
				hasErrorComLocation = true;
			});
			
			//輸入完成傳值到Conrtoller
			$("#comNextSlide2").click(function(){
				if(!hasErrorComRealname || !hasErrorComPhone || !hasErrorComLocation){
					txt="<span>請輸入正確資訊</span>";
					$("#checkComStatus2").css({"color":"red","font-size":"small"});
					$("#checkComStatus2").html(txt);
				}
				else{
					txt="&emsp;";
					$("#checkComStatus2").html(txt);
					document.form1.method= "post"; 
					document.form1.action= "/PepperNoodles/addCom";
					document.form1.enctype="multipart/form-data";
					document.form1.submit();
				}
			});
			
		});
	
}//end

// function checkAccountType(){
// 	if
// }

function setErrorFor(input, message) {
	input.innerHTML = "<font color='red' size='-2'>" + message + "</font>";
	hasError = true;
}

function isEmail(email) {
	return /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
			.test(email);
}

function isPWD(pwd) {
	return /^(?=.*[A-Za-z])(?=.*\d)(?=.*[#^@$!%*?&])[A-Za-z\d#^@$!%*?&]{6,}$/
			.test(pwd);
}

function isPhoneNum(phonenum){
	return /^09\d{2}(\d{6}|-\d{3}-\d{3})$/.test(phonenum);
}

function privacyornot() {
	  return document.getElementById("privacycheck").checked;
	}



</script>

<style>
	#myBtn {
	  position: fixed;
	  bottom: 20px;
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
	.todisable {
		cursor: not-allowed;
		pointer-events: none;
	}
	.totextcenter{
		text-align:center;
	}
	.toshow{
		display: block;
	}
	.tohide{
		display: none;
	}
	
</style>
</head>
<body>
<form name="form1">
	<div class="image-container set-full-height"style="background-image: url(<c:url value="/images/login/noodles.jpg"/>)">
		<div class="logo-container">
			<div class="logo">
				<a href="/PepperNoodles"><img src="<c:url value="/images/logo/peppernoodle.png"/>" width="100px"
					height="100px" style="margin-left: 93%;"></a>
			</div>
		</div>
		<!-- container   -->
		<div class="container">
			<div class="row">
				<div class="col-sm-8 col-sm-offset-2">
					<!--      Wizard container        -->
					<div class="wizard-container">
						<div class="card wizard-card" data-color="blue"	id="wizardProfile">
							<!--use ajax -->
								<div class="wizard-header">
									<h3>
										<b>註冊</b> <b>胡椒麵會員</b> <br> <small>This
											information will let us know more about you.</small>
									</h3>
								</div>
								
								<div class=""><!-- 整大包的div -->
									<div class="" id="accoutPage1" >
										<div class="row">
<!-- 											<h4 class="info-text">請您輸入基本資訊</h4> -->
											<div  style="text-align: center;font-size:20px;margin-top: 0px;margin-bottom: 15px;" >
											<label style="margin-top:10px;font-weight: bold;">選擇身份: </label>
											<label style="font-weight: bold;"><input type="radio" name="accountType" value="user" id="user" >&nbsp;使用者註冊</label>
											<label style="font-weight: bold;"><input type="radio" name="accountType" value="company" id="company">&nbsp;企業註冊</label>
											</div>
											<!-- First -->
											<div class="col-sm-4 col-sm-offset-1">
												<div class="picture-container">
													<div class="picture">
														<img src="<c:url value="/images/NoImage/NoImage_Male.png"/>"
															class="picture-src" id="wizardPicturePreview"  />
														<input type="file" id="wizard-picture" accept="image/*" name="photo">
													</div>
													<h6>Choose Picture</h6>
												</div>
											</div>
											<div class="col-sm-6">
												<div class="form-group">
													<label>Email <small>(必填)</small></label>
													<input class="form-control" type="text" name="userName" id="UserEmail" placeholder="pepper@Noodle.com">
													<span id="result"></span><br>
												</div>
												<div class="form-group">
													<label>Password <small>(必填-至少6字且包含英文、數字、特殊字元)</small></label> 
													<input class="form-control" type="password" name="userPwd" id="userPwd">
													<span id="result2"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<button type="button" id="checkMail" style="margin-top: 10px;margin-bottom: 10px">驗證信箱</button>
													<label><small id="checkMailInput"></small></label>
													<input class="form-control" type="text" name="verifycode" id="verifycode" placeholder="請輸入驗證碼...">
													<span id="cheqMailResult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group" id="" style="border:1px inset #F0FFFF;height:100px;overflow: scroll;"><c:import var="data" url="/data/PersonalPrivacy.txt" charEncoding="UTF-8"/><pre><c:out value="${data}"/></pre></div>
												<label><input type="checkbox" id="privacycheck">我已經閱讀個人資料保護法並且同意註冊為會員</label>
											</div>
										
										<div class="wizard-footer height-wizard col-sm-10 col-sm-offset-1">
											<div class="pull-right">
											<input type='button' class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
												name='next' value='Next' id="nextSlide" style="margin-bottom: 20px;margin-top: 10px"/>
											</div>	
											<div class="pull-right" style="margin-right: 20%;" id="checkAccountStatus">
												<div style="width: 150px;height: 30px;"></div>
											</div> 
											<div class="pull-left">
											<input type='button' class='btn btn-previous btn-fill btn-default btn-wd btn-sm'
												name='previous' value='Previous' id='lastSlide' style="margin-bottom: 20px;margin-top: 10px"/>
											</div>
											
										</div>
										</div>
									</div>
									<!-- second -->
									<div class="tab-pane tohide" id="accoutDetailPage2">
										<div class="row">
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>Nick Name:<small>(1.不可空白，2.至少兩個字以上，3.必須全部為中文字)</small></label>
													<input class="form-control" type="text" name="nickName" id="nickname">
													<span id="nickNameresult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>Real Name:<small>(1.不可空白，2.至少兩個字以上，3.必須全部為中文字)</small></label>
													<input class="form-control" type="text" name="realName" id="realName">
													<span id="realNameresult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>Sex:<small></small></label><br>
													<input type="radio" name="gende	r" value="male" id="male">男
													<input type="radio" name="gender" value="female" id="female">女
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>手機:<small>(請輸入10手機號碼。例:09xxxxxxxx)</small></label>
													<input class="form-control" type="text" name="phoneNumber" id="phoneNumber">
													<span id="phoneNumberresult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>BirthDay:<small>(格式:西元年/月/日 yyyy/MM/dd)</small></label>
													<input class="form-control" type="text" id="birthday" name="datt" autocomplete="off">
													<span id="dateresult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>Location:<small>(請選擇所在區域)</small></label><br>
													<select name="location" id="location" >
														<option value="大安">大安區</option>
														<option value="信義">信義區</option>
														<option value="中山">中山區</option>
														<option value="士林">士林區</option>
														<option value="中正">中正區</option>
													</select>
												</div>
											</div>
										<div class="wizard-footer height-wizard col-sm-10 col-sm-offset-1">
											<div class="pull-right">
											<input type='button' class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
												name='next' value='Next' id="nextSlide2"  style="margin-bottom: 20px;margin-top: 10px"/>
											</div>	
											<div class="pull-right" style="margin-right: 20%;" id="checkAccountStatus2">
												<div style="width: 150px;height: 30px;"></div>
											</div> 
											<div class="pull-left">
											<input type='button' class='btn btn-previous btn-fill btn-default btn-wd btn-sm'
												name='previous' value='Previous' id='lastSlide2'  style="margin-bottom: 20px;margin-top: 10px"/>
											</div>
										</div>
										</div>
									</div>
					<!-- 企業的Detail -->
									
									<div class="tab-pane tohide" id="comDetailPage2">
										<div class="row">
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>企業名稱:<small>(1.不可空白，2.至少兩個字以上)</small></label>
													<input class="form-control" type="text" name="comRealname" id="comRealname">
													<span id="comRealnameResult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>連絡電話:<small>(請輸入數字。例:09xxxxxxxx)</small></label>
													<input class="form-control" type="text" name="comPhonenumber" id="comPhonenumber">
													<span id="comPhotoResult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1">
												<div class="form-group">
													<label>地址:<small>(請輸入公司地址)</small></label>
													<input class="form-control" type="text" name="comLocation" id="comLocation">
													<span id="comLocationResult"></span><br>
												</div>
											</div>
											<div class="col-sm-10 col-sm-offset-1" style="display: none;">
												<div class="form-group">
													<label>企業等級:<small>一般會員</small></label>
													<input class="form-control" type="text" name="comlevel" id="comlevel" value="一般會員">
													<span id="comlevelResult"></span><br>
												</div>
											</div>
										<div class="wizard-footer height-wizard col-sm-10 col-sm-offset-1">
											<div class="pull-right">
											<input type='button' class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
												name='next' value='Next' id="comNextSlide2"  style="margin-bottom: 20px;margin-top: 10px"/>
											</div>	
											<div class="pull-right" style="margin-right: 20%;" id="checkComStatus2">
												<div style="width: 150px;height: 30px;"></div>
											</div> 
											<div class="pull-left">
											<input type='button' class='btn btn-previous btn-fill btn-default btn-wd btn-sm'
												name='previous' value='Previous' id='comlastSlide2'  style="margin-bottom: 20px;margin-top: 10px"/>
											</div>
										</div>
										</div>
									</div>
									
									<!-- third -->
									<div class="tab-pane tohide" id="tagPage3" >
										<div class="row">
										
											<table border="1px solid black"  style="border-collapse: collapse;font-size: 20px;" class="totextcenter col-sm-10 col-sm-offset-1">
												<tr>
													<td width="50px">興趣:</td>
													<td><input type="checkbox" name="hobby" value="curry"
														id="hobby">咖哩</td>
													<td><input type="checkbox" name="hobby" value="BBQ"
														id="hobby">烤肉</td>
													<td><input type="checkbox" name="hobby" value="pizza"
														id="hobby">披薩</td>
													<td><input type="checkbox" name="hobby" value="fried"
														id="hobby">炸物</td>
													<td><input type="checkbox" name="hobby" value="hamburger"
														id="hobby">漢堡</td>
													<td><input type="checkbox" name="hobby" value="springRoll"
														id="hobby">春捲</td>
												</tr>
											</table>
											<br>
											<span id="divResult" ></span>
											
										<div class="wizard-footer height-wizard col-sm-10 col-sm-offset-1">
											<div class="pull-right">
												<input type='button'class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
													name='finish' value='Finish' id='sendData' style="margin-bottom: 20px;margin-top: 300px"/>
											</div>
											
											<div class="pull-right toshow"  id="">
												<input type='button'class='btn btn-next btn-fill btn-warning btn-wd btn-sm'
													name='' value='前往登入' id='toBasic' style="margin-bottom: 20px;margin-top: 300px"/>
											</div> 
											
											<div class="pull-left">
												<input type='button' class='btn btn-previous btn-fill btn-default btn-wd btn-sm'
													name='previous' value='Previous' id='lastSlide' style="margin-bottom: 20px;margin-top: 300px" />
											</div>
										</div>
										
										</div>
									</div>
								</div>
								
						  </div>
					</div>
					<!-- wizard container -->
				</div>
			</div>
			<!-- end row -->
			<div  id="myBtn" title="Go to top">
				<button type="button" id="addMember">一鍵新增1</button>
				<button type="button" id="addMemberDetail">一鍵新增2</button>
				<br>
				<button type="button" id="addcompany">一鍵企業1</button>
				<button type="button" id="signinCompany">一鍵企業2</button>
				
			</div>
		</div>
		<!--  big container -->

		<div class="footer">
			<div class="container">
				<div class="footer-copy-right">
					<p>
						Copyright
						<script>
							document.write(new Date().getFullYear());
						</script>
						All rights reserved | U copy <i class="fa fa-heart"	aria-hidden="true"></i>U died
					</p>
				</div>
			</div>
		</div>

	</div>
</form>	
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
	
</body>
<!--   Core JS Files   -->
<script src="<c:url value='/scripts/jquery.bootstrap.wizard.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value="/scripts/bootstrap2.min.js"/>"></script>

<!--  Plugin for the Wizard -->
<script src="<c:url value='/scripts/gsdk-bootstrap-wizard.js'/>"></script>

</html>