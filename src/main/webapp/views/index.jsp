<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>estate result show page</title>

<style>
.submit {
	height: 30px;
	color: blue;
	size: 10px;
	margin-left: 100px;
}
.input {
height: 25px;
color: blue;
}
</style>

</head>
<body>
	<br />
	<br>
	<font color=blue size=4></font>
	<br />
	<br />
	<form action="/index/demopro/" method="post" enctype="multipart/form-data">
	</br>
      you choose: <select name="choose">
<option value="volvo">Volvo</option>
<option value="saab">Saab</option>
<option value="fiat" selected="selected">Fiat</option>
<option value="audi">Audi</option>
</select>
</br>
		<br /> <font color=black size=3>权重：  &nbsp&nbsp&nbsp</font><input type="file" name="file" /><br />
		<br /> <font color=black size=3>param2:  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</font><input class="input" type="text" name="path" value="/" /><br />
		<br /> <input class="submit" type="submit" value="提交">
	</form>
</body>
</html>
