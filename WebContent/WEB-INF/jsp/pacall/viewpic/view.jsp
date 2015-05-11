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
<div id="pagewrap"> 
	<header> 
		<h1>PACALL - Passive Capture for Learning Log</h1> 
	</header>
    <div class="navigator">
       <ul id="filter">
           <li onclick="location.href='<c:url value="/pacall/folder" />'" class="return">Return</li>
       </ul>
    </div>
    <div class="clear"><br/></div>
	<div id="dropbox">
        <div id="error" style="color:red">
        	<ul>
        		<c:forEach items="${errors}" var="error">
        		<li>${error}</li>
        		</c:forEach>
        	</ul>
        </div>
        <div style="float:left">
            <img src="${baseURL}/pacall/showpic/${sensePic.id}_640_480.jpg" width="640px" height="480px" />
		</div>
		<div style="float:left;margin: 50px 50px;">
			<form action="<c:url value='/pacall/uploaditem' />" method="post">
				<input type="hidden" value="${sensePic.id}" name="id" />
				<input type="submit" value="Upload it" style="width: 150px; height: 50px;"/><br /><br /><br />
			</form>
			<button style="width: 150px; height: 50px;" onclick="closeWindow()">Close</button>
		</div>
	<c:if test="${!empty sameItems}">
	<div style="clear:both">
	<h2>Same objects</h2>
	<section id="sameItems">
		 <c:forEach items="${sameItems}" var="item">
		  	<c:set var="privateFlg" value="false" />
		  	<c:if test="${item.shareLevel=='PRIVATE' && userId!=item.author.id}">
		  		<c:set var="privateFlg" value="true" />
		  	</c:if>
		      <div class="ditem">
		          <div class="item">
		          <a href="<c:url value="/item/${item.id}" />" target="_blank">
		              <table>
		                  <tbody>
		                      <tr>
		                          <td rowspan="5" class="photo">
		                          	<c:choose>
		                          		<c:when test="${!privateFlg}">
		                                  <c:choose>
		                                      <c:when test="${empty item.image}">
		                                          <img height="70px" alt="" src="<c:url value="/images/no_image.gif" />" />
		                                      </c:when>
		                                      <c:otherwise>
		                                          <img height="70px" alt="" src="${staticserverUrl}/${projectName}/${item.image.id}_160x120.png" />
		                                      </c:otherwise>
		                                  </c:choose>
		                              <br />
		                              	</c:when>
		                              	<c:otherwise>
		                              		<img height="70px" alt="" src="<c:url value="/images/locked.png" />" title="Private" />
		                              		Details<img alt="photo" src="<c:url value="/images/icon_camera.gif" />" />
		                              	</c:otherwise>
		                              </c:choose>
		                          </td>
		                          <td colspan="2">
		                          	<c:choose>
		                              <c:when test="${empty item.titles}">
		                                  NO NAME
		                              </c:when>
		                              <c:otherwise>
		                              <div class="titleTable">
		                              <table>
		                              	<c:forEach items="${item.titles}" var="title">
		                                      <tr>
		                                          <td style="width:70px;">${title.language.name}</td>
		                                          <td>${title.content}</td>
		                                      </tr>
		                              	</c:forEach>
		                              </table>
		                              </div>
		                              </c:otherwise>
		                              </c:choose>
		                          </td>
		                      </tr>
		                      <tr>
		                          <td style="text-align:right;vertical-align:bottom; height:10px; middle;display: table-cell" colspan="2">
		                              <a class="userlink" data-uid="${item.author.id}" data-uname="${item.author.nickname}" href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>">
		                                  <c:out value="${item.author.nickname}" />
		                              </a>
		                              <c:if test="${!empty item.relogItem}">
		                                  &nbsp;(<a href="$/item/${item.relogItem.id}" />">ReLog</a> from <a class="userlink" data-uid="${item.relogItem.author.id}" data-uname="${item.relogItem.author.nickname}" href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>"><c:out value="${item.relogItem.author.nickname}" /></a>)
		                              </c:if>
		                              &nbsp;<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm"  value="${item.createTime}" />
		                          </td>
		                      </tr>
		                  </tbody>
		              </table>
		              </a>
		          </div>
		      </div>
		      <hr />
		  </c:forEach>
	</section>
	</div>
	</c:if>
	<c:if test="${!empty similarItems}">
	<div style="clear:both">
	<h2>Similar objects</h2>
	<section id="similarItems">
		 <c:forEach items="${similarItems}" var="item">
		  	<c:set var="privateFlg" value="false" />
		  	<c:if test="${item.shareLevel=='PRIVATE' && userId!=item.author.id}">
		  		<c:set var="privateFlg" value="true" />
		  	</c:if>
		      <div class="ditem">
		          <div class="item">
		          <a href="<c:url value="/item/${item.id}" />" target="_blank">
		              <table>
		                  <tbody>
		                      <tr>
		                          <td rowspan="5" class="photo">
		                          	<c:choose>
		                          		<c:when test="${!privateFlg}">
		                                  <c:choose>
		                                      <c:when test="${empty item.image}">
		                                          <img height="70px" alt="" src="<c:url value="/images/no_image.gif" />" />
		                                      </c:when>
		                                      <c:otherwise>
		                                          <img height="70px" alt="" src="${staticserverUrl}/${projectName}/${item.image.id}_160x120.png" />
		                                      </c:otherwise>
		                                  </c:choose>
		                              <br />
		                              	</c:when>
		                              	<c:otherwise>
		                              		<img height="70px" alt="" src="<c:url value="/images/locked.png" />" title="Private" />
		                              		Details<img alt="photo" src="<c:url value="/images/icon_camera.gif" />" />
		                              	</c:otherwise>
		                              </c:choose>
		                          </td>
		                          <td colspan="2">
		                          	<c:choose>
		                              <c:when test="${empty item.titles}">
		                                  NO NAME
		                              </c:when>
		                              <c:otherwise>
		                              <div class="titleTable">
		                              <table>
		                              	<c:forEach items="${item.titles}" var="title">
		                                      <tr>
		                                          <td style="width:70px;">${title.language.name}</td>
		                                          <td>${title.content}</td>
		                                      </tr>
		                              	</c:forEach>
		                              </table>
		                              </div>
		                              </c:otherwise>
		                              </c:choose>
		                          </td>
		                      </tr>
		                      <tr>
		                          <td style="text-align:right;vertical-align:bottom; height:10px; middle;display: table-cell" colspan="2">
		                              <a class="userlink" data-uid="${item.author.id}" data-uname="${item.author.nickname}" href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>">
		                                  <c:out value="${item.author.nickname}" />
		                              </a>
		                              <c:if test="${!empty item.relogItem}">
		                                  &nbsp;(<a href="<c:url value="/item/${item.relogItem.id}" />">ReLog</a> from <a class="userlink" data-uid="${item.relogItem.author.id}" data-uname="${item.relogItem.author.nickname}" href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>"><c:out value="${item.relogItem.author.nickname}" /></a>)
		                              </c:if>
		                              &nbsp;<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm"  value="${item.createTime}" />
		                          </td>
		                      </tr>
		                  </tbody>
		              </table>
		              </a>
		          </div>
		      </div>
		      <hr />
		  </c:forEach>
	</section>
	</div>
	</c:if>
	</div> 
</div>
        <footer>
            <h2>Passive Capture for Learning Log</h2>
            <a class="learninglog" target="_blank" href="http://ll.is.tokushima-u.ac.jp/">&rarr;Visit SCRO<span style="color:#da431c;">LL</span></a>
        </footer>
</body>
</html>