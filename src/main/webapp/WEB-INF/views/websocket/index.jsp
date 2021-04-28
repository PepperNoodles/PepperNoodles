<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Custom messanger</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.0/handlebars.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/list.js/1.1.1/list.min.js"></script>
    <!--    libs for stomp and sockjs-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <!--    end libs for stomp and sockjs-->
    
    
    <link rel='stylesheet'
	href="<c:url value='/webjars/bootstrap/4.6.0/css/bootstrap.min.css' />" />
	<script type="text/javascript"
		src="<c:url value='/webjars/bootstrap/4.6.0/js/bootstrap.min.js'/>"></script> 
    <link rel="stylesheet" href="<c:url value='/css/chatstyle.css'/>" >
</head>
<style>
body{
	height: 100%;
    background-image: url("https://images.unsplash.com/photo-1532210317995-cc56d90177d9?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mjh8fGJhY2tncm91bmR8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60");
    background-size: cover;
    background-position: center center;
    background-attachment: fixed;	
	
	}
.userInfo a{
	text-decoration: none;
	color:#C4E1FF;
	font-size:1.3em
}
</style>


<body>
<div class="container clearfix row" >
<div class="col-3 pl-0" style="height:700px">

									
						
	<div class="m-3">
	<a href="/PepperNoodles"><img style="height:50px" src="<c:url value="/images/logo/peppernoodle.png"/>" alt=""></a>
	Home   <span id="userName" style="display:none;">${userAccount.accountIndex}</span></div>

	 <div class="userInfo">
     
     	<img alt="avatar" height="55px" id="avatar" 
         src="http://localhost:433/PepperNoodles/userProtrait/${userAccount.userAccountDetail.useretailId}"
         width="70px"/>
        
         <a href="<c:url value='/user/login'/>" class="m-2">${userAccount.userAccountDetail.nickName}</a> 
          
     </div>
		
	
    <div class="people-list bg-secondary mt-2" style="width:150px" id="people-list">
    	<div class="d-flex justify-content-center">
    	<h6 class="text-light mt-2">好友區</h6>	
    	</div>
<!--         <div class="search"> -->
<!--             <input id="userNameOld" placeholder="search" type="text"/> -->
<!--             <button id="registration">Enter the chat</button> -->
<!--             <button id="fetchAll">Refresh</button> -->
<!--       </div>  -->
        
    
  
     <div class="friends ">
        <ul class="list" style="height:80vh" id="usersList">


        </ul>
     </div>
    </div>
</div>
    <div class="chat my-3" style="height:700px">
        <div class="chat-header clearfix" >
            <img alt="avatar" height="55px" id="avatar" 
                 src="http://localhost:433/PepperNoodles/userProtrait/${userAccount.userAccountDetail.useretailId}"
                 width="70px"/>

            <div class="chat-about">
                <div class="chat-with" id="selectedUserId"></div>
                <div class="chat-num-messages"></div>
            </div>
            <i class="fa fa-star"></i>
        </div> <!-- end chat-header -->

        <div class="chat-history " style="height:400px">
            <ul>

            </ul>

        </div> <!-- end chat-history -->

        <div class="chat-message clearfix">
            <textarea id="message-to-send" name="message-to-send" placeholder="Type your message" rows="3"></textarea>

            <i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp;
            <i class="fa fa-file-image-o"></i>

            <button id="sendBtn">Send</button>

        </div> <!-- end chat-message -->

    </div> <!-- end chat -->

</div> <!-- end container -->

<script id="message-template" type="text/x-handlebars-template">
    <li class="clearfix">
        <div class="message-data align-right">
            <span class="message-data-time">{{time}}, Today</span> &nbsp; &nbsp;
            <span class="message-data-name">You</span> <i class="fa fa-circle me"></i>
        </div>
        <div class="message other-message float-right">
            {{messageOutput}}
        </div>
    </li>
</script>

<script id="message-response-template" type="text/x-handlebars-template">
    <li>
        <div class="message-data">
            <span class="message-data-name"><i class="fa fa-circle online"></i> {{userName}}</span>
            <span class="message-data-time">{{time}}, Today</span>
        </div>
        <div class="message my-message">
            {{response}}
        </div>
    </li>
</script>


<script>
$(window).load(function(){
    console.log("this is ok");
	const url = 'http://localhost:433/PepperNoodles';
 	let currentUser =document.getElementById("userName").innerHTML;
	let stompClient;
	let selectedUser;
	
	
	$("#registration").on("click",registration);
	$("#fetchAll").on("click",fetchAll);
	fetchAll();
	$.ajax({
		type: "GET",
		url: url+"/userMessageLoggin/getName",				
		dataType: "text",
		success: function (response) {
			console.log(response);
			registrationByUserName(response);
			//$('#userName').html(response);
		},
		error: function (thrownError) {
			console.log(thrownError);
		}
	});
	
	
	function connectToChat(userName) {
	    console.log("connecting to chat...")
	    let socket = new SockJS(url + '/chat');
	    stompClient = Stomp.over(socket);
	    stompClient.connect({}, function (frame) {
	        console.log("connected to: " + frame);
	        stompClient.subscribe("/topic/messages/" + userName, function (response) {
	            let data = JSON.parse(response.body);
	            console.log("data:"+data);
	            if (selectedUser === data.fromLogin) {
	            	console.log("收到訊息嗎?1")
	                render(data.message, data.fromLogin);
	            } else {
	            	console.log("收到訊息嗎?2");
	            	
	                newMessages.set(data.fromLogin, data.message);
	                $('#userNameAppender_' + data.fromLogin).append('<span id="newMessage_' + data.fromLogin + '" style="color: red">+1</span>');
	            }
	      
	        });
	    });
	}

	    function sendMsg(from,text){
	        //對應到message Model的屬性
	        console.log("from:"+from);
	        console.log("selectedUser: "+selectedUser);
	        //增加時間
	        var today =  new Date().getTime();
			console.log(today);

	    
			//新增時間	        
	        stompClient.send("/app/chat/"+selectedUser,{},JSON.stringify(
	            {
	                fromLogin:from,
	                message:text,
	                to:selectedUser,
	                date:today
	            }
	        ));
	    }

	    function registration(){
	        let userName=currentUser;
	        //對應到Message Controller的方法
	        $.get(url+"/registration/"+userName,function(response){
	            connectToChat(userName);
	        }).fail(function(error){
	            if(error.status===400){
	                alert("Login is already busy");
	            }
	        });
	    }
       
	    
	    function registrationByUserName(userName){
	        //let userName=document.getElementById("userName").value;
	        console.log(userName);
	        //對應到Message Controller的方法
	        $.get(url+"/registration/"+userName,function(response){
	            connectToChat(userName);
	        }).fail(function(error){
	            if(error.status===400){
	                alert("Login is already busy");
	            }
	        });
	    }

        function selectUser(){
        	let userName = this.name;
        	let userNick=$(this).find('.name').html()
            console.log("selecting user"+userName);
            selectedUser=userName;
            $chatHistoryList.html('');
            
            $('#selectedUserId').html('');
            $('#selectedUserId').append('Chat with ' + userNick);
            createChatHistory(currentUser,selectedUser);
            
        }

	    function fetchAll(){	    	
	        $.get(url+"/fetchAllUser/"+currentUser,function(response){	    
	        	console.log(response);
	            //let users = JSON.parse(response);
	            let users=response;
	            console.log(users);
	            let userTemplateHTML="";
	            for(let i = 0;i<users.length;i++){
	            	let imgUrl=url+"/userProtrait/"+users[i].mUserDetailId;
	                userTemplateHTML=userTemplateHTML+'<a class="text-dark" href="#" id=\'user'+i+'\' name=\''+users[i].mUserAccountIndex+'\'><li class="clearfix">'+
	                '<img alt="avatar" height="55px"'+
	                     'src='+imgUrl+' '+
	                     'width="70px"/>'+
	                '<div class="about">'+
	                    '<div class="name">'+users[i].mUserAccountNickName+'</div>'+
	                    '<div class="status">'+
	                        '<i class="fa fa-circle online"></i></div></div></li></a>';
	            
	            }
	        $("#usersList").html(userTemplateHTML);
                //-- Only add events when innerHTML overwrites are done.
                let userlist = document.getElementById("usersList");
                var targetSpans = userlist.querySelectorAll ("a[name]");
                for (let j = 0 ;j<targetSpans.length;j++) {
                    targetSpans[j].addEventListener ("click", selectUser);
                }
	        });
	    }
	    
        let $chatHistory;
        let $button;
        let $textarea;
        let $chatHistoryList;

        function init() {
            cacheDOM();
            bindEvents();
        }

        function bindEvents() {
            $button.on('click', addMessage.bind(this));
            $textarea.on('keyup', addMessageEnter.bind(this));
        }

        function cacheDOM() {
            $chatHistory = $('.chat-history');
            $button = $('#sendBtn');
            $textarea = $('#message-to-send');
            $chatHistoryList = $chatHistory.find('ul');
        }

        function render(message, userName) {
            scrollToBottom();
            // responses
            var templateResponse = Handlebars.compile($("#message-response-template").html());
            var contextResponse = {
                response: message,
                time: getCurrentTime(),
                userName: userName
            };

            setTimeout(function () {
                $chatHistoryList.append(templateResponse(contextResponse));
                scrollToBottom();
            }.bind(this), 1500);
        }

        function sendMessage(message) {
            let username =document.getElementById("userName").innerHTML;
            	
            	//$('#userName').val();
           // let username = ${userAccount.accountIndex};
            console.log("username:"+username);
            console.log("message:"+message);
            sendMsg(username, message);
            scrollToBottom();
            if (message.trim() !== '') {
                var template = Handlebars.compile($("#message-template").html());
                var context = {
                    messageOutput: message,
                    time: getCurrentTime(),
                    toUserName: selectedUser
                };

                $chatHistoryList.append(template(context));
                scrollToBottom();
                $textarea.val('');
            }
        }

        function scrollToBottom() {
            $chatHistory.scrollTop($chatHistory[0].scrollHeight);
        }

        function getCurrentTime() {
            return new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
        }

        function addMessage() {
            sendMessage($textarea.val());
        }

        function addMessageEnter(event) {
            // enter was pressed
            if (event.keyCode === 13) {
                addMessage();
            }
        }

        
		///////////////////show聊天紀錄

	    function createChatHistory(currentUser,toUser){
	    	cacheDOM();
	    	
	    	$.ajax({
	    		type: "GET",
	    		url: url+"/getUserMessage/"+currentUser+"/"+toUser,				
	    		dataType: "text",
	    		success: function (response) {
	    			//console.log(response);
	    			messagg=JSON.parse(response);
	    		  	for (let i = 0;i<messagg.length;i++){
	    	    		if(currentUser ==messagg[i].fromLogin){
	    	    			console.log(messagg[i].fromLogin+":"+messagg[i].message);
	    	    			//送出
	    	    			  var template = Handlebars.compile($("#message-template").html());
	    	                  var context = {
	    	                      messageOutput: messagg[i].message,
	    	                      time: messagg[i].date,
	    	                      toUserName: messagg[i].to
	    	                  };
	    	                  $chatHistoryList.append(template(context));	    	    			
	    	    		}else{
	    	    			console.log(messagg[i].fromLogin+":"+messagg[i].message);
	    	    			//收到
	    	    			var templateResponse = Handlebars.compile($("#message-response-template").html());
	    	                var contextResponse = {
	    	                    response: messagg[i].message,
	    	                    time: messagg[i].date,
	    	                    userName: messagg[i].fromLogin
	    	                };
	    	                $chatHistoryList.append(templateResponse(contextResponse));
	    	                scrollToBottom();	    	    			
	    	    		}	    	    		
	    	    	}
	    		},
	    		error: function (thrownError) {
	    			console.log(thrownError);
	    		}
	    	});
	    }       
        init();
	}
)




</script>
</body>
</html>