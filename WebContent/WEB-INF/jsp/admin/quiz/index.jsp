<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
     <c:import url="../../include/head.jsp">
        <c:param name="title" value="setting" />
        <c:param name="javascript">
            
        </c:param>
    </c:import>
    <body>
                    <div id="Container">
                <c:import url="../../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutC" >
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div>
                            </div><!-- Top -->
                                <div id="Center">
                <div class="CenterD">
                				<a href="<c:url value="/admin" />">Return</a>
		                           <table border="1">
		                            	<tr>
		                            		<th width="25%">Email</th>
		                            		<th>Name</th>
		                            		<th>Nickname</th>
		                            		<th>Righttimes</th>
		                            		<th>Wrongtimes</th>
		                            		<th>Referencetimes</th>
		                            		<th>Reviewtimes</th>
		                            		<th>Upload</th>
		                            		<th>Relog</th>
		                            	</tr>
		                               <c:forEach items="${quizinfos}" var="quizinfo">
		                               	<tr>
		                               		<td><c:out value="${quizinfo.user.pcEmail}" /></td>
		                               		<td><c:out value="${quizinfo.user.firstName}" /> <c:out value="${quizinfo.user.lastName}" /></td>
		                             		<td><c:out value="${quizinfo.user.nickname}" /></td>
		                             		<td><c:out value="${quizinfo.righttimes}" /> </td>
		                             		<td><c:out value="${quizinfo.wrongtimes}" /></td>
		                             		<td><c:out value="${quizinfo.referencenumber}" /></td>
		                             		<td><c:out value="${quizinfo.myreferencenumber}" /></td>
		                             		<td><c:out value="${quizinfo.uploadnumber}" /></td>
		                             		<td><c:out value="${quizinfo.relognumber}" /></td>
		                             	</tr>
		                               </c:forEach>
	                               </table>
	           				 </div> 
	           				 <br/>
                        </div><!-- Layout -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
