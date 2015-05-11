<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7, IE=9" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/gallery.css" />" />
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<script>
	$(function(){
		$("#file").focus().select();
	});
</script>
<title>PACALL - Passive Capture for Learning Log</title>
</head>
<body>
	<div id="pagewrap">
		<header>
			<h1>PACALL - Passive Capture for Learning Log</h1>
		</header>
		<section id="content">
		<div id="Contents">
			<div id="ContentsContainer">
				<h1>Upload Success</h1>
				<br />
				<button style="width: 150px; height: 50px;" onclick="location.href='<c:url value='/pacall' />'">Return</button>
			</div>
		</div>
		</section>
		<footer>
			<a href="http://ll.is.tokushima-u.ac.jp" target="_blank">#Learning Log</a>
		</footer>
	</div>
</body>
</html>