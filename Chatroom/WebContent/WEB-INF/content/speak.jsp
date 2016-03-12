<%@ page language="java" contentType="text/html;"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head lang="zh-CN">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1">
	<title>禁言管理</title>
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
					<li >
						<a href="login">聊天室</a>
					</li>
					<s:if test="#session.user.identity!='粉丝'">
						<li class="active">
							<a href="#">禁言管理</a>
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
		<div class="row-fluid main">
			<div>
				<form class="form-inline text-center" action="addSpeak" method="POST">
					<div class="form-group">
					<label>禁言：</label><input type="text" required name="silence.user.name" class="form-control"  placeholder="输入名字"/>
					</div>
					<button type="submit" class="btn btn-default">提交</button>
				</form>
			</div>
			<div class="col-md-4 col-md-offset-4 content">
				<table class="table">
					<thead>
						<tr>
							<td>id</td>
							<td>用户</td>
							<td>操作</td>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="%{silences}" var="silence">
						<tr>
							<td><s:property value='#silence.sid'/></td>
							<td><s:property value="#silence.user.name"/></a></td>
							<td><a href="removeSpeak?id=<s:property value='#silence.sid'/>">解禁</a></td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
		<div class="container-fluid footer">
	</div>
	</div>
	</body>
	</html>