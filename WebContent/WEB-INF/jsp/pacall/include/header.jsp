<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="Header">
    <div id="HeaderContainer">
        <h1><a href="http://ll.is.tokushima-u.ac.jp/${projectName}">Learning Log</a></h1>
        <div id="globalNav">
            <ul>
                <li class="<c:if test="${params.currentModule=='home'}">currentModule</c:if>"><a href="<c:url value="/" />">Home</a></li>
                <li><a href="<c:url value="/action/logout" />">Logout</a></li>
            </ul>
        </div><!-- globalNav -->

        <div id="topBanner">
        	<p style="font-size:60px;font-weight:bold;color:green;">P&nbsp;A&nbsp;C&nbsp;A&nbsp;L&nbsp;L</p>
        </div>
    </div><!-- HeaderContainer -->
</div><!-- Header -->
