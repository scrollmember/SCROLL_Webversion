<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<c:import url="../include/head.jsp">
    <c:param name="title" value="Add a new Task" />
    <c:param name="content">
        <style>
            .optional{display:none}
        </style>
            <script src="http://www.google.com/jsapi"></script>
        </c:param>
    </c:import>
    <body>
        <div>
            <div>
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Top -->
                            <div id="Center">
                                <div id="diaryForm" class="dparts form">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Add new Task</h3></div>
                                        <c:url value="/task" var="taskUrl" />
                                        <form:form commandName="task" action="${taskUrl}" method="post" enctype="multipart/form-data">
                                        	<ul>
                                        		<li>
                                        			<form:errors path="title"></form:errors>
                                        		</li>
                                        		<li>
                                        			<form:errors path="languageId"></form:errors>
                                        		</li>
                                        	</ul>
                                            <table>
                                                <tr>
                                                    <th>
                                                        <label for="task_title">Title</label><strong>*</strong>
                                                    </th>
                                                    <td> 
                                                        <form:input path="title"  cssStyle="width:90%" />&nbsp;
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="task_level">Difficulty Level</label><strong>*</strong>
                                                    </th>
                                                    <td> 
                                                        <form:select path="level">
                                                             <form:option value="3" label="Difficult" />
                                                             <form:option value="2" label="Moderate" />
                                                             <form:option value="1" label="Easy" />
                                                        </form:select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="target_language">Target Language</label><strong>*</strong>
                                                    </th>
                                                    <td> 
                                                        <form:select path="languageId">
                                                                    <c:forEach items="${langs}" var="lan">
                                                                      <form:option value="${lan.id}" label="${lan.name}" />
                                                                    </c:forEach>
                                                        </form:select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="tag">Tag</label></th>
                                                    <td>
                                                        <form:input path="tag" id="tagInput" cssStyle="width:50%" />
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li>
                                                        <input type="submit" class="input_submit" value="Next" />
                                                     </li>
                                                     <li>   
                                                        <input type="submit" class="input_submit" value="Cancel" onclick=""/>
                                                    </li>
                                                </ul>
                                            </div>
                                        </form:form>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div><!-- Center -->
                        </div><!-- Layout -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
