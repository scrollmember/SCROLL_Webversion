<%@page import="java.util.Date"%>
<%@page import="jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../../include/head.jsp">
        <c:param name="title" value="" />
        <c:param name="javascript">
        <script type="text/javascript">
        	$(function(){
        		window.print();
        	});
        </script>
        </c:param>
    </c:import>
 	<body style="text-align:center;">
 		<div id="main" style="width:800px;margin:auto;text-align:center">
 			<div><%=new Date()%></div>
 			<div>
		 		<c:forEach var="x" begin="1" end="18" step="1">
		 		<div style="float:left;text-align:center;margin:5px;border:2px;"> 
		 			<%String qrcode=KeyGenerateUtil.generateIdUUID(); %>
		 			<img src="http://chart.apis.google.com/chart?cht=qr&chs=150x150&chl=<%=qrcode%>" /><br />
		 			<%=qrcode%>
		 		</div>
		 		</c:forEach>
	 		</div>
 		</div>
 	</body>
</html>