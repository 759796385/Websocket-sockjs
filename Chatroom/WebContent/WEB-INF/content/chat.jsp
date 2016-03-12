<%@ page language="java" contentType="text/html;"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head lang="zh-CN">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<title>聊天室</title>
	<link rel="stylesheet" href="../css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<script type="text/javascript" src="../js/jquery-2.1.1.js"></script>
	<script type="text/javascript" src="../js/bootstrap.js"></script>
</head>
<body>
	<div class="container-fluid wrap">
		<!-- 导航栏 -->
		<div class="row-fluid ">
			<div class="head">
				<ul class="nav nav-pills">
					<li >
						<a href="#">首页</a>
					</li>
					<li class="active">
						<a href="#">聊天室</a>
					</li>
					<s:if test="#session.user.identity!='粉丝'">
						<li>
							<a href="speakList" target="_blank">禁言管理</a>
						</li>
					</s:if>
					<!-- <li class="disabled">
						<a href="#">关于</a>
					</li> -->
					<li class="dropdown pull-right">
						 <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
      						当前用户：<s:property value='%{#session.user.name}'/> <span class="caret"></span>
   						 </a>
   						 <ul class="dropdown-menu">
					     	<li>
					     		<a href="logout">注销用户</a>	
					     	</li>	
					    </ul>
					</li>
				</ul>
			</div>
		</div>
		<!-- 主体 -->
		<div class="row-fluid main">
			<!-- left-->
			<div class="col-md-3 left">
				<div class="online">
					<h4>
						<small>在线用户</small>
					</h4>
					<p>
						<small><em style="color:#a7aeaf;">当前在线人数 <span id="onlie_num"></span> 人</em></small>
					</p>
					<div class="scroll human">
					<ul class="list-unstyled" id="human">
						

					</ul>
					</div>
				</div>
				<!-- 退出聊天室按钮 -->
				<center>
					<button type="button" class="btn btn-primary outbtn" data-toggle="modal" data-target=".bs-example-modal-sm">退出聊天室</button>
				</center>
				<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
				  <div class="modal-dialog modal-sm">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title">确认框</h4>
				      </div>
				      <div class="modal-body">
				        <p>你确定要离开聊天室吗？</p>
				      </div>
				      <div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				        <button type="button" class="btn btn-primary exists">确定</button>
				      </div>
				    </div><!--modal-content -->
				  </div><!--modal-dialog -->
				</div><!--modal -->
			</div>

			<!--right-->
			<div class="col-md-8 col-md-offset-1 right">
				<div class="row-fluid messageare">
					<div class="chat" id="chat"> 
						<p class="text-danger" >
							[系统提示] 欢迎来到websocket聊天室，本站搭建中~
						</p>
					</div>
				</div>
				<div class="row-fluid sendmsg">
				<legend></legend>
					<form class="form-inline">
						<div class="form-group" >
							    <label class="sr-only" for="message">message</label>
							    <div class="input-group" >
							  		<div class="input-group-addon" id="private_chat" style="cursor:pointer;  ">@All</div>
							    	<input type="text" class="form-control"  id="message"  placeholder="开始聊天吧~"/>
							  	</div>
							
						</div>
						<button class="btn btn-default" type="button" id="text">提交</button>
					</form>
				</div>
			</div>
		</div>
	</div>


	<!-- 页脚 -->
	<div class="container-fluid footer">
		<div class="row-fluid ">
			<div class="col-md-12">
				<hr/>
				<p>
					聊天室初步UI草图
				</p>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="../js/sockjs-0.3.min.js"></script>
	<script type="text/javascript" src="../js/websocket.js"></script>
	<script>
		       websocket = null;
        domain= location.hostname;
        //判断当前浏览器是否支持WebSocket
        if(!window.WebSocket){
            console.log("浏览器等级低,使用SockJs连接");
            websocket = new SockJS("http://"+domain+"/Chatroom/sockjs/webSocketServer");
        }
        else{
        	console.log("使用websocket连接");
            setCookie('username',"<s:property value='%{#session.user.name}'/>",1);
            websocket = new WebSocket("ws://"+domain+"/Chatroom/ws/webSocketServer");
        }
        
       //  /* c_name:cookie名称 value:值 expiredays:过期时间*/
        function setCookie(c_name,value,expiredays)
        {
          var exdate=new Date();
          exdate.setDate(exdate.getDate()+expiredays);
          document.cookie=c_name+ "=" +escape(value)+((expiredays==null) ? "" : "; expires="+exdate.toGMTString())+";path=/";
        }

        //接收到消息的回调方法
        websocket.onmessage = function(event){
          console.log("收到消息");
          analyzeMessage(event.data);
        }
       //        //连接发生错误的回调方法
        websocket.onerror = function(){
            setMessageInnerHTML("<p class='text-info'>[提示] 连接出错...</p>");
            $("#text").attr("disabled",'disabled');
        };
         
       //  //连接成功建立的回调方法
        websocket.onopen = function(event){
          console.log("连接成功");
          onlineDOM();
          setMessageInnerHTML("<p class='text-info'>[提示] 成功连接聊天服务器...</p>");
        }
         
       //  //连接关闭的回调方法
        websocket.onclose = function(){
          console.log("连接关闭");
          setMessageInnerHTML("<p class='text-info'>[提示] 断开服务器...</p>");
          $("#text").attr("disabled",'disabled');
        }
       
	</script>
</body>
</html>