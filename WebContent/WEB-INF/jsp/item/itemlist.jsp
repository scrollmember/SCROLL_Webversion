<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
// paramでセットした名前からリストを取得
String listName = request.getParameter("ItemList");
// リストを使えるようにする
pageContext.setAttribute("ItemList", (java.util.List)pageContext.findAttribute(listName));

// 情報(Synset、距離とか)
String infoName = request.getParameter("InfoList");
if (infoName != null && !infoName.equals("")) {
	pageContext.setAttribute("InfoList", (java.util.List)pageContext.findAttribute(infoName));
}
%>
<div class="parts">
	<div class="partsHeading">
		<h3>${param.title}</h3>
	</div>
	<div class="block">
	<c:forEach items="${ItemList}" var="item" varStatus="status">
		<c:set var="privateFlg" value="false" />
		<c:if test="${item.shareLevel=='PRIVATE' && userId!=item.author.id}">
			<c:set var="privateFlg" value="true" />
		</c:if>
		<div class="item_block">
			<div class="item_image">
			<c:choose>
				<c:when test="${!privateFlg}">
					<a href="<c:url value="/item/${item.id}" />">
					<c:choose>
						<c:when test="${(empty item.image)}">
							<img height="70px" alt="Details" src="<c:url value="/images/no_image.gif" />" />
						</c:when>
						<c:otherwise>
							<img height="70px" alt="Details" src="${staticserverUrl}/${projectName}/${item.image.id}_160x120.png" />
						</c:otherwise>
					</c:choose>
					</a>
				</c:when>
				<c:otherwise>
					<img height="70px" alt="" src="<c:url value="/images/locked.png" />" title="Private" />
				</c:otherwise>
			</c:choose>
			</div>
			<div class="item_info">
				<c:choose>
					<c:when test="${empty item.titles}">
						NO NAME<br />
					</c:when>
					<c:otherwise>
						<c:forEach items="${item.titles}" var="title">
							<div class="item_hide">${title.language.name}: </div>${title.content}<br />
						</c:forEach>
					</c:otherwise>
				</c:choose>
				<div class="item_hide">
					<hr />
					<c:if test="${!empty InfoList[status.index]}">
						<c:forEach items="${InfoList[status.index]}" var="info">
							<c:choose>
								<c:when test="${info.key == 'synset'}">Synset: <a href="<c:url value="/wordnet/synset/${info.value}"></c:url>">${info.value}</a></c:when>
								<c:when test="${info.key == 'synset0'}">FirstSynset: <a href="<c:url value="/wordnet/synset/${info.value}"></c:url>">${info.value}</a></c:when>
								<c:when test="${info.key == 'dist'}">Distance: ${info.value}</c:when>
								<c:when test="${info.key == 'sim'}">Similarity: ${info.value}</c:when>
							</c:choose>
							<br />
						</c:forEach>
						<hr />
					</c:if>
					Author: <a href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>" target="_blank">
					<c:out value="${item.author.nickname}" /></a>
					<c:if test="${!empty item.relogItem}">
						&nbsp;(<a href="<c:url value="/item/${item.relogItem.id}" />" target="_blank">ReLog</a> from <a href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>" target="_blank"><c:out value="${item.relogItem.author.nickname}" /></a>)
					</c:if>
					<br />
					Created time: <fmt:formatDate type="both" dateStyle="full" timeStyle="short" value="${item.createTime}" /><br />
					Updated time: <fmt:formatDate type="both" dateStyle="full" timeStyle="short" value="${item.updateTime}" />
					<hr />
					
				</div>
			</div>
		</div>
	</c:forEach>
	</div>
	<a href="<c:url value="/item/${item.id}/related/${param.more}" />">more</a>
</div>