<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.phprpc.*" %>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import = "jp.ac.tokushima_u.is.ll.ws.service.*" %>
<%
    PHPRPC_Server phprpc_server = new PHPRPC_Server();
    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
    LLQuizRemoteService llquizRemoteService = context.getBean("llquizRemoteService",LLQuizRemoteService.class);
    ItemWebService itemservice = context.getBean("itemWebService", ItemWebService.class);
    UserRemoteService userservice = context.getBean("userRemoteService", UserRemoteService.class);
    phprpc_server.add("composeQuizByUsername", llquizRemoteService);
    phprpc_server.add("checkQuizAnswerByUsernaeme", llquizRemoteService);
    phprpc_server.add("composeQuizForMail", llquizRemoteService);
    phprpc_server.add(userservice);
    phprpc_server.add("searchMyItems", itemservice);
    phprpc_server.add("getItemByid", itemservice);
    phprpc_server.add("checkItemByid", itemservice);
    phprpc_server.add("searchAllItems", itemservice);
    phprpc_server.add("insertItem", itemservice);
    phprpc_server.add("insertItemByMmail", itemservice);
    phprpc_server.add("findItemByLocation", itemservice);
    phprpc_server.start(request, response);
%>
