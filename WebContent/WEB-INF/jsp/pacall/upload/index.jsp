<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:import url="../inc/define.jsp" />
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${baseURL}/css/pacall.css" />
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
				<h1>Upload</h1>
				<br />
				<c:url value='/pacall/upload' var="uploadUrl" />
				<form:form action="${uploadUrl}" method="post" commandName="form" enctype="multipart/form-data">
					<h2>
						<label for="file">Please select a folder to upload:</label>
					</h2>
					<br />
					<input type="file" name="files" id="files" webkitdirectory directory mozdirectory multiple style="width:100%;font-size:24px;border: 1px solid" />
					<br />
					<form:errors cssClass="error" path="files" />
					<h1>
						<input type="submit" value="Upload" style="width: 150px; height: 50px;" />&nbsp;
						<input type="reset" value="Return" style="width: 150px; height: 50px;" onclick="location.href='<c:url value='/pacall' />'"/>
					</h1>
				</form:form>
			</div>
		</div>
		</section>
		<footer>
			<a href="http://ll.is.tokushima-u.ac.jp" target="_blank">#Learning Log</a>
		</footer>
	</div>
</body>
</html>