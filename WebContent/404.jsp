<%@ page language="java" contentType="text/html; charset=UTF-8"
	isErrorPage="true" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>404 Error Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
<!--
body {
	margin: 20px;
	font-family: Tahoma, Verdana;
	font-size: 14px;
	color: #333333;
	background-color: #FFFFFF;
}

a {
	color: #1F4881;
	text-decoration: none;
}
-->
</style>
</head>
<body>
<div
	style="border: #cccccc solid 1px; padding: 20px; width: 500px; margin: auto"
	align="center">Sorry, the page can't be found!<br />
<br />
<a href='javascript:history.back();'>【Go to previous page】</a>
<a href='<c:url value="/" />'>【Go to homepage】</a></div>
<br />
<br />
<br />
<div style="border: 0px; padding: 0px; width: 500px; margin: auto">
Learning Log
</div>
</body>
</html>