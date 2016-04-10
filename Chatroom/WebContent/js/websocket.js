      //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = function(){
          websocket.close();
      }
       
      //将消息显示在网页上
      function setMessageInnerHTML(innerHTML){
         var $node =  $('#chat');
         $node.append(innerHTML);
         $node[0].scrollTop = $node[0].scrollHeight;
      }
       
      //关闭连接
      function closeWebSocket(){
          websocket.close();
      }
       
      //发送消息
      function send(){
          var message = document.getElementById('message').value;
          if(message==""){return}
          // var chatstyle = document.getElementById("private_chat").text();
          var chatstyle = $("#private_chat").text().substring(1);
          websocket.send(chatstyle+"@"+message);
      }
      /*解析服务器信息*/
      function analyzeMessage(msg){
          var status  = msg.substring(0,msg.indexOf("#"));
          if(status=="0"){
              onlineDOM();
              var message = msg.substring(msg.indexOf("#")+1);
              setMessageInnerHTML("<p class='text-muted'>[提示] "+message+"</p>");
          }if(status=="1"){
              var substring = msg.substring(msg.indexOf("#")+1);
              var name = substring.substring(0,substring.indexOf("@"));
              var message = substring.substring(substring.indexOf("@")+1);
              if(name!="All"){
                  setMessageInnerHTML("<p style='color:lawngreen'>[用户] "+message+"！</p>");
              }else{//所有人消息
                  setMessageInnerHTML("<p class='text-primary'>[用户] "+message+"</p>");
              }
          }
      }

      function onlineDOM(){
        $.getJSON("json/online",function(data){
                var $node = $("#human");
                var obj = eval(data).result;
                var num = obj.online_num;
                $("#onlie_num").text(num);
                var names= obj.names;
                $node.empty();
                for(var index in names){
                  $node.append("<li> <span class='glyphicon glyphicon-user'></span>"+names[index]+"</li>");
                }
              });
      } 

      $(function () {
        $("#text").click(function(){
          send();
          var message = $("#message").val();
          setMessageInnerHTML("<p class='text-right'>"+message+"</p>");
          $("#message").val("");
        });
        $('[data-toggle="popover"]').popover();
      });

	  $("#private_chat").click(function(){
	      $(this).text("@All");
	  });
	  /*退出，高级chrome直接白页*/
	  $(".exists").click(function(){
		  var userAgent = navigator.userAgent;
		  if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
		     window.location.href="about:blank";
		  } else {
		     window.opener = null;
		     window.open("", "_self");
		     window.close();
		  };
	  });
	 /*解析消息方式*/
    $(".chat").on('click','p',function(){
      var text = $(this).text();
      if(text.indexOf("[用户]")==-1){
          return;
      };
      var end = text.indexOf(":");
      var start = 5;
      var user = text.substring(start,end);
      $("#private_chat").text("@"+user);
    })

