const url = 'http://localhost:8080';
let stompClient;
let selectedUser;

function connectToChat(userName){
	console.log("connecting to chat...");
    //對應到Controller的MessageMapping
    let socket = new SockJS(url+'/chat'+userName);
    stompClient=Stomp.over(socket);
    //連線到server
    stompClient.connect({},function(frame){
        console.log("connect to "+ frame)
        stompClient.subscribe("/topic/messages/"+userName,function(response){
            let data = JSON.parse(response.body);
            console.log(data);

        });
    });
	}

    function sendMsg(from,text){
        //對應到message Model的屬性
        stompClient.send("/app/chat/"+selectedUser,{},JSON.stringify(
            {
                fromLogin:from,
                message,text
            }
        ));
    }

    function registration(){
        let userName=document.getElementById("userName");
        //對應到Message Controller的方法
        $.get(url+"/registration/"+userName,function(response){
            connectToChat(userName);
        }).fail(function(error){
            if(error.status===400){
                alert("Login is already busy");
            }
        });
    }

    function fetchAll(){
        $.get(url+"/fetchAllUser",function(response){
            let users = response;
            let userTemplateHTML="";
            for(let i = 0;i<users.length;i++){
                userTemplateHTML=userTemplateHTML+' <li class="clearfix">'+
                '<img alt="avatar" height="55px"'+
                     'src="https://saiuniversity.edu.in/wp-content/uploads/2021/02/default-img.jpg"'+
                     'width="55px"/>'+
                '<div class="about">'+
                    '<div class="name">'+users[i]+'</div>'+
                    '<div class="status">'+
                        '<i class="fa fa-circle online"></i></div></div></li>';
            }
        $("#usersList").html(userTemplateHTML);
        });
  }
