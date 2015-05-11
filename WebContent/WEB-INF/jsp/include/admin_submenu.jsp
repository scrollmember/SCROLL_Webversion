<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="localNav">
	<ul class="default">
		<li id="default__homepage"><a href="<c:url value="/admin" />">Home</a></li>
		<li id="default_user"><a href="<c:url value="/admin/user" />">User</a></li>
		<li id="default_language"><a href="<c:url value="/admin/language" />">Language</a></li>
		<li id="default_quizstat"><a href="<c:url value="/admin/quizadmin" />">Quiz Stat</a></li>
		<li id="default_wordcsv"><a href="<c:url value="/admin/wordlistfile" />">Word Stat</a></li>
	</ul>
</div>