<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div id="localNav">
	<ul class="default">
	     <c:url value="/logstate/search" var="exper_url" >
	     	<c:param name="experienced" value="1"/> 
	     </c:url>
	      <c:url value="/logstate/search" var="not_exper_url" >
	     	<c:param name="experienced" value="0"/> 
	     </c:url>
	     <c:url value="/logstate/recommend" var="recommend_url" >
	     </c:url>
	     
		<li id="default__homepage"><a href="${exper_url}">Experienced</a></li>
		<li id="default__homepage"><a href="${not_exper_url}">Not Experienced</a></li>
		<li id="default__homepage"><a href="${recommend_url}">Recommended Learning Logs</a></li>
	</ul>
</div>