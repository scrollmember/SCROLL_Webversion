<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7, IE=9" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${baseURL}/css/pacall.css" />
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<script>
$(function(){
	$("#folder_table tbody tr").mouseenter(function(e){
		$(this).addClass("selectedItem");
	}).mouseleave(function(e){
		$(this).removeClass("selectedItem");
	}).click(function(e){
		location.href=$(this).attr("data-url");
	});
});
</script>
<title>Select Folder</title>
</head>
<body>
        <header>
            <h1>PACALL: Passive Capture for Learning Log</h1>
        </header>
        <div class="navigator">
            <a href="<c:url value='/pacall/upload' />" style="font-size: 20px;font-weight:bold">Upload</a>
        </div>
       <div id="dropbox">
			<table id="folder_table">
				<thead>
					<tr>
						<td>Period</td>
						<td>Picture Number</td>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${folderList}" var="folder" varStatus="status">
					<tr data-url="<c:url value="/pacall/browse?id=${folder.id}" />" <c:choose><c:when test="${status.count%2==0}">class="row-2"</c:when><c:otherwise>class="row-1"</c:otherwise></c:choose>>
						<td>
						  <fmt:formatDate value="${folder.startDate}" type="both" pattern="yyyy/MM/dd HH:mm:ss" />
						  &nbsp;~&nbsp;
						  <fmt:formatDate value="${folder.endDate}" type="both" pattern="yyyy/MM/dd HH:mm:ss" />
				        </td>
						<td>${folder.picnum}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
       <footer>
           <h2>Passive Capture for Learning Log</h2>
           <a class="learninglog" target="_blank" href="http://ll.is.tokushima-u.ac.jp/">&rarr;Visit SCRO<span style="color:#da431c;">LL</span></a>
       </footer>
</body>
</html>