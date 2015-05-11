<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="localNav">
	<ul class="default">
		<li id="default__homepage"><a href="<c:url value="/profile" />">Profile</a></li>
		<li id="default_settings"><a href="<c:url value="/mysetting" />">Settings</a></li>
		<li id="default_settings"><a href="<c:url value="/category" />">Category</a></li>
		<li id="default_password"><a href="<c:url value="/profile/changepassword" />">Change Password</a></li>
	</ul>
</div>