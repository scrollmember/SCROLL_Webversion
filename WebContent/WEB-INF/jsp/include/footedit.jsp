<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="Footer">
    <div id="FooterContainer" style="margin-top:10px">
            <%--<a target="_blank" href="<c:url value="/privacyPolicy" />">プライバシーポリシー</a>
            <a target="_blank" href="<c:url value="/userAgreement" />">利用規約</a>
            --%>
            <div style="float:left">
            	<a target="_blank" href="<c:url value="/help" />" style="font-size:22px;font-weight:bold;font-family: arial"><img src="<c:url value="/images/help.png" />"/>Help</a>
            	<a target="_blank" href="http://sites.google.com/site/learning64u/video" style="font-size:22px;font-weight:bold;font-family: arial"><img src="<c:url value="/images/videos.png" />"/>Videos</a>
            </div>
            <div style="float:right">
	            <c:url value='/download/apk' var="downloadapkUrl" />
	            <a href="javascript:void(window.open('${downloadapkUrl}'))">Download LearningLog for Android</a>&nbsp;&nbsp;
	            <a href="javascript:void(window.open('http://sites.google.com/site/learning64u/'))">Jump to Learning Log Project-&gt;</a>
            </div>
    </div><!-- FooterContainer -->
</div><!-- Footer -->