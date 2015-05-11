<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import = "jp.ac.tokushima_u.is.ll.service.*" %>
<%
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
			ItemFileStatusService service = context.getBean("itemFileStatusService", ItemFileStatusService.class);
            try {
                boolean result = service.repairItemFile();
                if (result) {
                    out.println("Success");
                } else {
                    out.println("Fail");
                }
            } catch (Exception e) {
                out.println(e);
            }
%>