<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Admin" />
    </c:import>

    <body id="page_member_home">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
						<c:import url="../include/admin_submenu.jsp"></c:import>
						<h1>Welcome!</h1>
						<%--
                  		 <div>
          				 	<form method="post" action="<c:url value="/admin/usestat/day" />">
          				 		<input type="submit" value="Download Stat CSV (daily)" />
          				 	</form>
          				 </div>
                  		 <div>
          				 	<form method="post" action="<c:url value="/admin/usestat/week" />">
          				 		<input type="submit" value="Download Stat CSV (weekly)" />
          				 	</form>
          				 </div>
          				 --%>
                    </div><!-- Contents -->
                    <c:import url="../include/footer.jsp" />
                </div>
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
