<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../../include/head.jsp">
    	<c:param name="title" value="Edit Language" />
    </c:import>
    <body id="page_diary_list">
    <div id="Body">
        <div id="Container">
            <c:import url="../../include/header.jsp" />
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
                                <div id="groupSearchFormLine" class="parts searchFormLine">
                                </div>
                                <div class="dparts searchResultList">
                                	<div class="parts">
                                        <div class="pagerRelative">
                                        </div>
                                        <div class="block">
                                        			<a href="<c:url value="/admin/language/${language.id}" />">Edit</a>
                                        			&nbsp;<a href="<c:url value="/admin/language" />">Return</a>
											<c:url value="/admin/${user.id}/edit" var="targetUrl" />
		                                    <form:form commandName="language" action="${targetUrl}" method="post">
			                                    <table>
			                                        <tr>
			                                            <th style="width: 25%"><label for="code">Code</label></th>
			                                            <td>
			                                            	<form:input path="code" />
			                                            	<form:errors path="code" />
			                                            </td>
			                                        </tr>
			                                        <tr>
			                                            <th><label for="name">Name</label></th>
			                                            <td><form:input path="name" />
			                                            <form:errors path="name" /></td>
			                                        </tr>
			                                    </table>
		                                    	<input type="submit" value="Submit" />
		                                    </form:form>
                                        </div>
                                        <div class="pagerRelative">
                                            <p class="number"><!--7件中 1～7件目を表示--></p>
                                        </div>
                                    </div>
                                </div>
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <%--
                            <form action="/member/changeLanguage" method="post"><label for="language_culture">言語</label>:
                                <select name="language[culture]" onchange="submit(this.form)" id="language_culture">

                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select><input value="diary/index" type="hidden" name="language[next_uri]" id="language_next_uri" /></form>
                            --%>
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->

                <c:import url="../../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>