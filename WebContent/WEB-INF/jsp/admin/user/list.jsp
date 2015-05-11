<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../../include/head.jsp">
    	<c:param name="title" value="User List" />
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
                                        		<li style="font-family: arial,sans-serif; font-size: 20px; font-weight:900">${category.name}
                                        		</li>
                                        		<li>
                                        			<a href="<c:url value="/admin" />">Return</a><br />
                                        			<a href="<c:url value="/admin/user/add" />">Add new user</a>
                                        		</li>
                                        		<li>
                                        			<br />
                                        			<form action="<c:url value="/admin/user" />" method="get">
                                        				Email: <input type="text" name="email" />
                                        				<input id="searchButton" type="submit" value="Search" />
                                        			</form>
                                        			<br />
                                        		</li>
                                        		<li>
                                        			<table>
                                        				<thead>
                                        					<tr>
                                        						<td width="70px"></td>
                                        						<td width="70px"></td>
                                        						<td width="230px">Pc Email</td>
                                        						<td width="70px;">Name</td>
                                        						<td width="70px;">Nickname</td>
                                        					</tr>
                                        				</thead>
                                        				<tbody>
                                        					<c:forEach items="${userList}" var="user" varStatus="sta">
                                        					
                                        					<tr <c:if test="${sta.index%2==1}">class="dualline"</c:if>>
                                        						<td><button onclick="location.href='<c:url value="/admin/user/${user.id}" />'">Show</button></td>
                                        						<td><button onclick="location.href='<c:url value="/admin/user/${user.id}/edit" />'">Edit</button></td>
                                        						<td>${user.pcEmail}</td>
                                        						<td>${user.firstName}&nbsp;${user.lastName}</td>
                                        						<td>${user.nickname}</td>
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