<%@ page language="java" contentType="text/html;"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>登陆</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<link rel="stylesheet" type="text/css" href="../css/bootstrap-theme.css">
	<link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="../css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="../css/main.css">
</head>
<body>
	<div class="container">
		<div class="loginBox row">
		  	<h2 class="text-center">登录</h2>
			<form id="wyccn" name="wyccn" action="login" method="post" class="form-horizontal">
				<div class="form-group has-success">
				    <label for="nick_name" class="col-sm-2 col-md-2 control-label">用户名</label>
				    <div class="col-sm-10 col-md-10">
				    	<input type="text" class="form-control" name="user.name" maxlength="7" minlength="2" placeholder="用户名" value="" required="required">
				    </div>
				</div>
				<div class="form-group has-success">
				    <label for="user_password" class="col-sm-2 col-md-2 control-label">密码</label>
				    <div class="col-sm-10 col-md-10">
				    	<input type="password" class="form-control" name="user.password" placeholder="密码" required="required">
				    </div>
				</div>
			  	<div class="form-group">
			  	  	<div class="text-center" style="color: #990033;"><s:actionerror/><s:actionmessage/></div>
				</div>
				<div class="form-group">
				    <div class="col-sm-offset-4 col-sm-10 col-md-10">
				    	<input type="hidden" name="" value=""/>
				      	<button class="btn btn-info" data-loading-text="正在登录..." type="submit">登 录</button>
				      	<button class="btn btn-info" type="reset">清 空</button>
				    </div>
				</div>
				<div class="form-group text-center ">
			  	  	<a href="regist.jsp">没有账号?点击注册</a>
				</div>
		  	</form>
		</div>
	</div>
	<!--.content-->
	<script src="../js/jquery-2.1.1.js"></script>
	<script src="../js/bootstrap.js"></script>
</body>
</html>