<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="jp.ac.tokushima_u.is.ll.ws.service.ItemWebService" %>
<%@page import = "jp.ac.tokushima_u.is.ll.quiz.*" %>
<%
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
			ConvertWorkerService service = context.getBean(ConvertWorkerService.class);
            service.convert();
%>