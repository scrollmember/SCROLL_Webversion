<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../../include/head.jsp">
    	<c:param name="title" value="Lanuage List" />
    	<c:param name="css">
    		<style type="text/css">
    			.dualline{
    				background-color: #00FA9A;
    			}
    		</style>
    	</c:param>
    </c:import>
    <body id="page_diary_list">
    <div id="Body">
        <div id="Container">
            <c:import url="../../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
						<c:import url="../../include/admin_submenu.jsp"></c:import>
                        <div id="Layout" class="Layout">
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
                                        <div class="pagerRelative">
                                        </div>
                                        <div class="block">
                                        	<ul>
                                        		<li>
                                        			<a href="<c:url value="/admin" />">Return</a><br />
                                        			<a href="<c:url value="/admin/language/add" />">Add a new language</a>
                                        		</li>
                                        		<li>
                                        			<table>
                                        				<thead>
                                        					<tr>
                                        						<td width="70px"></td>
                                        						<td width="70px"></td>
                                        						<td width="230px">Code</td>
                                        						<td width="230px">Name</td>
                                        					</tr>
                                        				</thead>
                                        				<tbody>
                                        					<c:forEach items="${languageList}" var="language" varStatus="sta">
                                        					
                                        					<tr <c:if test="${sta.index%2==1}">class="dualline"</c:if>>
                                        						<td><button onclick="location.href='<c:url value="/admin/language/${language.id}" />'">Show</button></td>
                                        						<td><button onclick="location.href='<c:url value="/admin/language/${language.id}/edit" />'">Edit</button></td>
                                        						<td>${language.code}</td>
                                        						<td>${language.name}</td>
                                        					</tr>
                                        					</c:forEach>
                                        				</tbody>
                                        			</table>
                                        		</li>
                                        	</ul>
                                        </div>
                                        <div class="pagerRelative">
                                            <p class="number"><!--7件中 1～7件目を表示--></p>
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