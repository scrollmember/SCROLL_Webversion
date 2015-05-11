<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="jp.ac.tokushima_u.is.ll.ws.service.ItemWebService" %>
<%@ page import="org.phprpc.*" %>
<%@page import = "jp.ac.tokushima_u.is.ll.ws.service.*" %>
<%
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
            ItemWebService service = context.getBean("itemWebService", ItemWebService.class);
            PHPRPC_Server server = new PHPRPC_Server();
            server.add(service);
            server.add("insertItem", service);
            server.add("findItemByLocation", service);
            server.start(request, response);
%>