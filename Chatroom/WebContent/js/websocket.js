
      //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = function(){
          websocket.close();
      }
       
      //将消息显示在网页上
      function setMessageInnerHTML(innerHTML){
          document.getElementById('chat').innerHTML += innerHTML ;
      }
       
      //关闭连接
      function closeWebSocket(){
          websocket.close();
      }
       
      //发送消息
      function send(){
          var message = document.getElementById('message').value;
          // var chatstyle = document.getElementById("private_chat").text();
          var chatstyle = $("#private_chat").text().substring(1);
          websocket.send(chatstyle+"@"+message);
      }

      function analyzeMessage(msg){
          var status  = msg.substring(0,msg.indexOf("#")-1);
          if(status=="0"){
              var message = msg.substring(msg.indexOf("#")+1);
              setMessageInnerHTML("<p class='text-muted'>[提示] "+message+"</p>");
          }if(status=="1"){
              var substring = msg.substring(msg.indexOf("#")+1);
              var name = substring.substring(0,substring.indexOf("@")-1);
              var message = substring.substring(substring.indexOf("@")+1);
              if(name!="All"){
                  setMessageInnerHTML("<p style='color:lawngreen'>[用户] "+message+"！</p>");
              }else{//所有人消息
                  setMessageInnerHTML("<p class='text-primary'>[用户] "+message+"</p>");
              }
          }
      }

      $(function () {

      $("#text").click(function(){send()});
      $('[data-toggle="popover"]').popover();

      $(".chat > p").click(function(){
          var text = $(this).text();
          if(text.indexOf("[用户]")==-1){
            return;
          }
          var end = text.indexOf(":");
          var start = 13;
          var user = text.substring(start,end);
          $("#private_chat").text("@"+user);
      });
    });
    $("#private_chat").click(function(){
      $(this).text("@All");
    })