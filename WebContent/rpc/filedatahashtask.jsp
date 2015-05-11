<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="jp.ac.tokushima_u.is.ll.ws.service.ItemWebService" %>
<%@page import = "jp.ac.tokushima_u.is.ll.service.task.*" %>
<%
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
			FiledataHashTaskService service = context.getBean("filedataHashTaskService", FiledataHashTaskService.class);
			service.run();
%>