<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="" />
        <c:param name="javascript">
        <script type="text/javascript">
        	$(function(){
        		window.print();
        	});
        </script>
        </c:param>
    </c:import>
 	<body>
 		<img src="http://chart.apis.google.com/chart?cht=qr&chs=150x150&chl=${content}" />
 	</body>
</html>