<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../inc/define.jsp" />
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7, IE=9" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${baseURL}/css/pacall.css" />
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<title>Upload</title>
<script   type="text/javascript">  
function closeWindow(){  
	window.open('','_parent','');  
	window.close();  
}  
</script>  
</head>
<body>
    <header> 
        <h1>PACALL - Passive Capture for Learning Log</h1> 
    </header>
<div id="pagewrap"> 

	<section id="content">
        <div id="error" style="color:red">
        	<ul>
        		<c:forEach items="${errors}" var="error">
        		<li>${error}</li>
        		</c:forEach>
        	</ul>
        </div>
        <div style="float:left">
			<img src="<c:url value="/action/image/${folder}/${filename}.JPG?width=640&height=480" />"  />
		</div>
		<div style="float:left;margin: 50px 50px;">
			<form action="<c:url value='/action/upload' />" method="post">
				<input type="hidden" name="folder" value="${folder}"/>
				<input type="hidden" name="filename" value="${filename}"/>
				<input type="submit" value="Upload it" style="width: 150px; height: 50px;"/><br /><br /><br />
			</form>
			<button style="width: 150px; height: 50px;" onclick="closeWindow()">Close</button>
		</div>
	</section>
</div>
        <footer>
            <h2>Passive Capture for Learning Log</h2>
            <a class="learninglog" target="_blank" href="http://ll.is.tokushima-u.ac.jp/">&rarr;Visit SCRO<span style="color:#da431c;">LL</span></a>
        </footer>
</body>
</html>