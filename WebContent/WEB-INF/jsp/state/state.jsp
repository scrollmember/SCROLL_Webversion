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
                                        						<td width="50%"><b>Remembered Learning Log &nbsp;(${exper_correct.size()})</b></td>
                                        						<td ><b>Not Remembered Learning Log &nbsp;(${exper_wrong.size()})</b></td>
                                        					</tr>
                                        					
                                        				</thead>
                                        				<tbody>
                                        					<c:if test="${exper_correct.size()>=exper_wrong.size()}">
	                                        					<c:forEach items="${exper_correct}" var="state" varStatus="sta">
		                                        					<tr <c:if test="${sta.index%2==1}">class="dualline"</c:if>>
		                                        						<td>
			                                        						<a href="<c:url value="/item/${state.item.id}" />"  target="_blank" style="TEXT-DECORATION:none">
			                                        						<c:forEach items="${state.item.titles}" var="title" varStatus="t">
			                                        							${title.content}
			                                        						</c:forEach>
			                                        						</a>
		                                        						</td>
		                                        						<td>
		                                        							<c:if test="${sta.index < exper_wrong.size()}">
		                                        								<a href="<c:url value="/item/${exper_wrong.get(sta.index).item.id}" />"  target="_blank" style="TEXT-DECORATION:none">
			                                        								<c:forEach items="${exper_wrong.get(sta.index).item.titles}" var="title" varStatus="t">
				                                        							${title.content}
				                                        							</c:forEach>
				                                        						</a>
		                                        							</c:if>
		                                        						</td>
		                                        					</tr>
	                                        					</c:forEach>
                                        					</c:if>
                                        					<c:if test="${exper_correct.size()<exper_wrong.size()}">
	                                        					<c:forEach items="${exper_wrong}" var="state" varStatus="sta">
		                                        					<tr <c:if test="${sta.index%2==1}">class="dualline"</c:if>>
		                                        						<td>
			                                        						<c:if test="${sta.index < exper_correct.size() && exper_correct.size()>0}">
			                                        								<a href="<c:url value="/item/${exper_correct.get(sta.index).item.id}" />"  target="_blank" style="TEXT-DECORATION:none">
				                                        								<c:forEach items="${exper_correct.get(sta.index).item.titles}" var="title" varStatus="t">
					                                        							${title.content}
					                                        							</c:forEach>
					                                        						</a>
			                                        						</c:if>
		                                        						</td>
		                                        						<td>
		                                        							<a href="<c:url value="/item/${state.item.id}" />"  target="_blank" style="TEXT-DECORATION:none">
			                                        						<c:forEach items="${state.item.titles}" var="title" varStatus="t">
			                                        							${title.content}
			                                        						</c:forEach>
			                                        						</a>
		                                        						</td>
		                                        					</tr>
	                                        					</c:forEach>
                                        					</c:if>
                                        				</tbody>
                                        			</table>
                                        		</li>
                                        	</ul>
                                        </div>