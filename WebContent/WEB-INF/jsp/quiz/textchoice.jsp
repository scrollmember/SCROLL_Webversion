<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div
	style="font-family: arial, meiryo, simsun, sans-serif; font-size: 22px; font-weight: bold; color: gray; margin-bottom: 10px;">
	<img src="<c:url value='/images/system-help.png' />" alt="●" />Select
	the right word <br />
	<span style="color: black">${quiz.content}</span>?
</div>
<c:choose>
	<c:when test="${answered}">
		<div
			style="display: block; border: 8px ridge orange; width: 250px; height: 80px; margin-left: 110px; font-family: arial; font-size: 22px; font-weight: bold">
			<a href="<c:url value="/quiz" />" style="TEXT-DECORATION: none">
				<span
				style="word-break: normal;color:<c:if test='${result}'>green</c:if><c:if test='${!result}'>red</c:if>">
					${comment} </span>
			</a>
		</div>
		<div style="margin-top: -30px;">
			<a href="<c:url value="/quiz" />"> <img
				src="<c:url value='/images/${faceicon}.png' />" alt="" />
			</a>
		</div>
		<div style="text-align: center">
			<input type="button" class="btn-primary" value="More Quiz"
				onclick="return fncMore();" />
		</div>
	</c:when>
</c:choose>
<table style="border-style: none;">
	<c:forEach items="${quiz.quizChoices}" var="choice" varStatus="status">
		<ul
			style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 3px 20px; line-height: 30px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
			<li><c:choose>
					<c:when test="${!answered}">
						<input id="answeroption${status.index}" type="radio" name="answer"
							value="${choice.number}"
							<c:if test= "${status.index == 0}"> checked="checked"</c:if> />
						<label for="answeroption${status.index}">${choice.content}</label>
					</c:when>
					<c:otherwise>
						<c:if test="${rightanswer == status.index+1}">
							<img src="<c:url value='/images/right_icon.png' />" alt="(Right)" />
						</c:if>
						<c:if test="${!result && youranswer == status.index+1}">
							<img src="<c:url value='/images/wrong_icon.png' />" alt="(Wrong)" />
						</c:if>
						<c:url value="/item/${choice.item.id}" var="itemurl" />
						<a href="${itemurl}" target="_blank">${choice.content}</a> 
	                         ${choice.note}
	              </c:otherwise>
				</c:choose></li>
		</ul>
	</c:forEach>
</table>
<c:choose>
	<c:when test="${!answered}">
		<br />
		<input type="submit" class="btn btn-primary" value="Answer" />
		<input type="submit" class="btn btn-success" value="Too Easy"
			onclick="return fncEasy();" />
		<input type="submit" class="btn btn-warning" value="Too Difficult"
			onclick="return fncDifficult();" />
		<input type="button" class="btn btn-danger" value="No Good"
			onclick="return fncPass();" />
	</c:when>
</c:choose>