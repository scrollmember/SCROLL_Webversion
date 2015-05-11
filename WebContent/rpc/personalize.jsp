<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import = "jp.ac.tokushima_u.is.ll.service.*" %>
<%@page import = "jp.ac.tokushima_u.is.ll.entity.*" %>
<%@page import = "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-31j">
<title>Insert title here</title>
</head>
<body>
<%
	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
	PersonalizeSerivce service = context.getBean("personalizeService",PersonalizeSerivce.class);
	ItemQueueService is = context.getBean("itemqueueService", ItemQueueService.class);
	service.personlize();
%> 

Hello!
</body>
</html>