<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:if test="${node!=null}">
<form:checkbox path="categoryIdList" value="${node.id}" id="cb${node.id}" cssClass="cbCategoryIdList"/>
<input type="hidden" id="name${node.id}" name="name${node.id}" value="${node.name}">
<label>${node.name}</label>
	<c:if test="${!empty node.children}">
	<ul>
	<c:forEach var="node" items="${node.children}">
		<li>
		<c:set var="node" value="${node}" scope="request" />
		<jsp:include page="categoryNode.jsp" />
		</li>
	</c:forEach>
	</ul>
	</c:if>
</c:if>