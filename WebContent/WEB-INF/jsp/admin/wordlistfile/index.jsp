<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
    <c:import url="../../include/head.jsp">
        <c:param name="title" value="Admin" />
		<c:param name="javascript">
            <script type="text/javascript">
                $(function(){
                    $('#dateFrom').datepicker({
                        dateFormat: 'yy-mm-dd'
                    });
                    $('#dateTo').datepicker({
                        dateFormat: 'yy-mm-dd'
                    });
                });
            </script>
		</c:param>
    </c:import>

    <body id="page_member_home">
        <div id="Body">
            <div id="Container">
                <c:import url="../../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
						<c:import url="../../include/admin_submenu.jsp"></c:import>
                  		 <div>
                  		 	<ul style="color:red">
                  		 	<c:forEach items="${errors}" var="error">
                  		 		<li>${error}</li>
                  		 	</c:forEach>
                  		 	</ul>
                  		    <form method="post" action="<c:url value="/admin/wordlistfile" />">
                  		 		<label for="dateFrom">Date:</label>
                  		 		<input id=dateFrom type="date" name="dateFrom" size="12" maxlength="10" value="${dateFrom}"/>00:00:00-<input id="dateTo" type="date" name="dateTo" size="12" maxlength="10" value="${dateTo}"/>00:00:00
          				 		<input type="submit" value="Download Word CSV" />
          				 	</form>
          				 </div> 
                    </div><!-- Contents -->
                    <c:import url="../../include/footer.jsp" />
                </div>
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
