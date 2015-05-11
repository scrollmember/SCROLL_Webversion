<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
    
<html>

    <c:import url="../include/head.jsp">
        <c:param name="title" value="Create a new category" />
    </c:import>
     <script type="text/javascript">
    $(document).ready(function() {

            		document.getElementById("default_settings").className = "";
            		document.getElementById("default__homepage").className = "";
            		document.getElementById("default_category").className = "active";
            		
            	});
    </script>
    <body id="page_diary_new">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                     <c:import url="../include/profile_var.jsp"></c:import>
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Top -->
                            <div id="Center">
                                <div id="groupSearchFormLine" class="parts searchFormLine">
									<a href="<c:url value='/category' />">Top</a>&gt;
									<c:forEach items="${categoryPath}" var="node">
										<a href="<c:url value='/category/${node.id}' />">${node.name}</a>&gt;
									</c:forEach>
                                </div>
                                <div id="diaryForm" class="dparts form">
                                    <div class="parts">
                                        <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Create a new category</h3></div></div>
                                        <c:choose>
										<c:when test="${category == null}">
										<c:url value="/category" var="categoryUrl" />
										</c:when>
										<c:otherwise>
										<c:url value="/category/${category.id}" var="categoryUrl"></c:url>
										</c:otherwise>
										</c:choose>
										<form:form commandName="categoryForm" action="${categoryUrl}" method="post">
                                            <p><strong>*</strong>&nbsp;Required.</p>
                                            <table>
                                                <tr>
                                                    <th><label for="name">Category name<strong>*</strong></label></th>
                                                    <td>
                                                        <form:input path="name" id="name" /><form:errors path="name" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="note">Description</label></th>
                                                    <td>
                                                        <form:textarea path="note" cols="50" rows="4" />
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li>
                                                        <input type="submit" class="input_submit" value="Create" />
                                                    </li>
                                                    <li>
                                                        <a href="${categoryUrl}">Return</a>
                                                    </li>
                                                </ul>
                                            </div>
										</form:form>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <%--
                            <form action="/member/changeLanguage" method="post"><label for="language_culture">言語</label>:
                                <select name="language[culture]" onchange="submit(this.form)" id="language_culture">
                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select><input value="diary/new" type="hidden" name="language[next_uri]" id="language_next_uri" /></form>
                            --%>
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>