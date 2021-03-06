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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet"
          type="text/css">
    <link rel="stylesheet" href="<c:url value='/css/chatstyle.css'/>" >
</head>
<body>
<div class="container clearfix">
    <div class="people-list" id="people-list">
        <div class="search">
            <input id="userName" placeholder="search" type="text"/>
            <button id="registration">Enter the chat</button>
            <button id="fetchAll">Refresh</button>
        </div>
        <ul class="list" id="usersList">


        </ul>
    </div>

    <div class="chat">
        <div class="chat-header clearfix">
            <img alt="avatar" height="55px"
                 src="https://saiuniversity.edu.in/wp-content/uploads/2021/02/default-img.jpg"
                 width="55px"/>

            <div class="chat-about">
                <div class="chat-with" id="selectedUserId"></div>
                <div class="chat-num-messages"></div>
            </div>
            <i class="fa fa-star"></i>
        </div> <!-- end chat-header -->

        <div class="chat-history">
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
	const url = 'http://localhost:9090/PepperNoodles';
	let stompClient;
	let selectedUser;
	
	$("#registration").on("click",registration);
	$("#fetchAll").on("click",fetchAll);

	function connectToChat(userName) {
	    console.log("connecting to chat...")
	    let socket = new SockJS(url + '/chat');
	    stompClient = Stomp.over(socket);
	    stompClient.connect({}, function (frame) {
	        console.log("connected to: " + frame);
	        stompClient.subscribe("/topic/messages/" + userName, function (response) {
	            let data = JSON.parse(response.body);
	            console.log(data);
	            if (selectedUser === data.fromLogin) {
	                render(data.message, data.fromLogin);
	            } else {
	                newMessages.set(data.fromLogin, data.message);
	                $('#userNameAppender_' + data.fromLogin).append('<span id="newMessage_' + data.fromLogin + '" style="color: red">+1</span>');
	            }
	      
	        });
	    });
	}

	    function sendMsg(from,text){
	        //?????????message Model?????????
	        stompClient.send("/app/chat/"+selectedUser,{},JSON.stringify(
	            {
	                fromLogin:from,
	                message:text
	            }
	        ));
	    }

	    function registration(){
	        let userName=document.getElementById("userName").value;
	        console.log(userName);
	        //?????????Message Controller?????????
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
            console.log("selecting user"+userName);
            selectedUser=userName;
   
            $('#selectedUserId').html('');
            $('#selectedUserId').append('Chat with ' + userName);
        }

	    function fetchAll(){
	        $.get(url+"/fetchAllUser",function(response){	        	
	            let users = response;
	            console.log(users);
	            let userTemplateHTML="";
	            for(let i = 0;i<users.length;i++){
	                userTemplateHTML=userTemplateHTML+'<a href="#" id=\'user'+i+'\' name=\''+users[i]+'\'><li class="clearfix">'+
	                '<img alt="avatar" height="55px"'+
	                     'src="https://saiuniversity.edu.in/wp-content/uploads/2021/02/default-img.jpg"'+
	                     'width="55px"/>'+
	                '<div class="about">'+
	                    '<div class="name">'+users[i]+'</div>'+
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
        /////////////////////
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
            let username = $('#userName').val();
            console.log(username)
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

        init();


	}
       


)




</script>
</body>
</html>