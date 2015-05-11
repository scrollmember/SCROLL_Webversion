<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../../include/head.jsp">
    	<c:param name="css">
    		<style type="text/css">
    			.dualline{
    				background-color: #00FA9A;
    			}
    			.errorItem{
    				background-color: red;
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
                                        						<td>item_id</td>
                                        						<td>file_id</td>
                                        						<td>file_name</td>
                                        						<td>orig</td>
                                        						<td>80x60</td>
                                        						<td>160x120</td>
                                        						<td>320x240</td>
                                        						<td>800x600</td>
                                        						<td>mp4</td>
                                        						<td>mp3</td>
                                        						<td>ogg</td>
                                        					</tr>
                                        				</thead>
                                        				<tbody>
                                        					<c:forEach items="${list}" var="item" varStatus="sta">
                                        					
                                        					<tr <c:if test="${sta.index%2==1}">class="dualline"</c:if>>
                                        					<tr>
                                        						<td><a href="<c:url value='/item/${item.item_id}'/>">${item.item_id}</a></td>
                                        						<td>${item.file_id}</td>
                                        						<td>${item.file_name}</td>
                                        						<td <c:if test="${item.hasOrig!=null && !item.hasOrig}">class="errorItem"</c:if>>
                                        							${item.hasOrig}
                                        						</td >
                                        						<td <c:if test="${item.has80_60!=null && !item.has80_60}">class="errorItem"</c:if>>${item.has80_60}</td>
                                        						<td <c:if test="${item.has160_120!=null && !item.has160_120}">class="errorItem"</c:if>>${item.has160_120}</td>
                                        						<td <c:if test="${item.has320_240!=null && !item.has320_240}">class="errorItem"</c:if>>${item.has320_240}</td>
                                        						<td <c:if test="${item.has800_600!=null && !item.has800_600}">class="errorItem"</c:if>>${item.has800_600}</td>
                                        						<td <c:if test="${item.hasMp4!=null && !item.hasMp4}">class="errorItem"</c:if>>${item.hasMp4}</td>
                                        						<td <c:if test="${item.hasMp3!=null && !item.hasMp3}">class="errorItem"</c:if>>${item.hasMp3}</td>
                                        						<td <c:if test="${item.hasOgg!=null && !item.hasOgg}">class="errorItem"</c:if>>${item.hasOgg}</td>
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