<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <div id="Layout" class="Layout">
                            <div id="Center">
                                <div class="dparts searchResultList">
                                        <div class="pagerRelative">
                                        </div>
                                        <div class="block">
                                        	<ul>
                                        		<li>
                                        			<table>
                                        				<thead>
                                        					<tr>
                                        						<td><b>Recommended Learning Logs &nbsp;(${recommend_items.size()})</b></td>
                                        					</tr>
                                        					
                                        				</thead>
                                        				<tbody>
                                        					<c:forEach items="${recommend_items}" var="state" varStatus="sta">
	                                        					<tr <c:if test="${sta.index%2==1}">class="dualline"</c:if>>
	                                        						<td>
		                                        						<a href="<c:url value="/item/${state.id}" />"  target="_blank" style="TEXT-DECORATION:none">
		                                        						<c:forEach items="${state.titles}" var="title" varStatus="t">
		                                        							${title.content}
		                                        						</c:forEach>
		                                        						</a>
	                                        						</td>
	                                        					</tr>
                                        					</c:forEach>
                                        				</tbody>
                                        			</table>
                                        		</li>
                                        	</ul>
                                        </div>