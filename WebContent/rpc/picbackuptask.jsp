<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import = "jp.ac.tokushima_u.is.ll.service.task.*" %>
<%
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
            PicBackupService picBackupService = context.getBean("picBackupService", PicBackupService.class);
            try {
                boolean result = picBackupService.execute();
                if (result) {
                    out.println("Success");
                } else {
                    out.println("Fail");
                }
            } catch (Exception e) {
                out.println(e);
            }
%>