<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>测试List集合</h1>
	<table style="height:500px;width:600px;size:2px;"border="0">
		<tr>
			<td>编号:</td>
			<td>姓名:</td>
			<td>年龄:</td>
			<td>性别:</td>
			<td>地址:</td>
		</tr>
		<#list list as p>
		<tr>
			<td>${p_index}</td>
			<td>${p.username!}</td>
			<td>${p.age!}</td>
			<td>${p.sex!}</td>
			<td>${p.address!}</td>
		</tr>
		</#list>
	</table>
</body>
</html>