<%@ page language="java" contentType="text/html;"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>注册</title>
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
		  	<h2 class="text-center">设置密码</h2>
			<form id="wyccn" name="wyccn" action="regist.action" method="post" class="form-horizontal">
				<div class="form-group has-success">
				    <label for="nick_name" class="col-sm-2 col-md-2 control-label">用户名</label>
				    <div class="col-sm-10 col-md-10">
				    	<input type="text" class="form-control" name="user.name" maxlength="7" minlength="2" placeholder="用户名" value="<s:property value='%{user.name}'/>" required="required" readonly=""/>
				    </div>
				</div>
				<div class="form-group has-success">
				    <label for="user_password" class="col-sm-2 col-md-2 control-label">密码</label>
				    <div class="col-sm-10 col-md-10">
				    	<input type="password" class="form-control" id="pw_1" name="user.password" maxlength="12" minlength="4" placeholder="输入密码" required="required" autofocus="autofocus"/>
				    </div>
				</div>
				<div class="form-group has-success">
				    <label for="user_password" class="col-sm-2 col-md-2 control-label">密码</label>
				    <div class="col-sm-10 col-md-10">
				    	<input type="password" class="form-control" id="pw_2" name="" maxlength="12" minlength="4" placeholder="请再次确认密码" required="required"/>
				    </div>
				</div>
				<div class="form-group ">
				    <div class="container-fluid ">
				    	<p class="text-center text-danger" id="error"></p>
				    	<input type="hidden" name="user.email" value="<s:property value='%{user.email}'/>"/>
				    	<input type="hidden" name="user.uid" value="<s:property value='%{user.uid}'/>"/>
				      	<button class="btn btn-info btn-block" data-loading-text="正在登录..." type="submit">注册</button>
				    </div>
				</div>
		  	</form>
		</div>
	</div>
	<!--.content-->
	<script src="../js/jquery-2.1.1.js"></script>
	<script src="../js/bootstrap.js"></script>
	<script type="text/javascript">

		$("#wyccn").on("submit",function(){
			var pass1 = $("#pw_1").val();
			var pass2 =$("#pw_2").val();
			if(pass1==pass2){
				return true;
			}else{
				$("#error").text("两次密码不一致");
				$("#pw_1").select();
				return false;
			}
		});
	</script>
</body>
</html>