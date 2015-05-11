<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!doctype html>
<html>
<c:import url="../include/Slidermenu.jsp" />
<c:import url="../include/head.jsp">
	<c:param name="title" value="Dashboard" />

</c:import>
<c:import var="pageLinks" url="itempage.jsp">
	<c:param name="searchCond" value="${searchCond}" />
	<c:param name="page" value="${page}" />
	<c:param name="type" value="1" />
</c:import>
<script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("dashboard").className = "active";

	});
</script>
<body id="page_member_home">
	<div id="Body">
		<div id="Container">
			<c:import url="../include/header.jsp" />
			<div id="Contents">
				<div id="ContentsContainer">
					<div id="LayoutA" class="Layout">
						<div id="Top">
							<div id="information_21" class="parts informationBox">
								<div class="body"></div>
							</div>
							<!-- parts -->
						</div>
						<!-- Top -->
						<div class="block container-fluid">
							<div class="row">
							 <div class="pagerRelative">
												${pageLinks}
	                                        </div>
	                                        <br>
							</div>
							<div class="row">
								<c:forEach items="${itemPage.result}" var="item">
									<c:set var="privateFlg" value="false" />
									<c:if
										test="${item.shareLevel=='PRIVATE' && userId!=item.author.id}">
										<c:set var="privateFlg" value="true" />
									</c:if>							
										<div class="col-xs-4" style="padding-bottom: 10px;height: 105px;"  data-url="<c:url value="/item/${item.id}" />">
											<div class="col-xs-5" style="overflow: hidden;">
												<c:choose>
													<c:when test="${!privateFlg}">
														<c:choose>
															<c:when test="${empty item.image}">
																<img height="70px" alt=""
																	src="<c:url value="/images/no_image.gif" />" />
															</c:when>
															<c:otherwise>
																<img class="staticimage" height="70px" alt=""
																	src="${staticserverUrl}/${projectName}/${item.image.id}_160x120.png" />
															</c:otherwise>
														</c:choose>
														<br />
													</c:when>
													<c:otherwise>
														<img height="70px" alt=""
															src="<c:url value="/images/locked.png" />"
															title="Private" />
                                                                        		Details<img
															alt="photo"
															src="<c:url value="/images/icon_camera.gif" />" />
													</c:otherwise>
												</c:choose>
											</div>
											<div class="col-xs-7">
												<c:choose>
													<c:when test="${empty item.titles}">
                                                                            NO NAME
                                                                        </c:when>
													<c:otherwise>
														<div class="titleTable">
															<table>
																<c:forEach items="${item.titles}" var="title">
																	<tr>
																		<td style="width: 70px;">${title.language.name}&nbsp;</td>
																		<td>&nbsp;${title.content}</td>
																	</tr>
																</c:forEach>
															</table>
														</div>
													</c:otherwise>
												</c:choose>
											</div>
											<div class="logs-icon ${color}"></div>
										</div>																
								</c:forEach>
							</div>
							<div class="row">
							   <div class="pagerRelative">
									${pageLinks}<p class="number"></p>
                               </div>
                                 <br>
							</div>
						</div>
						<!-- Layout -->
						<div id="sideBanner"></div>
						<!-- ContentsContainer -->
					</div>


				</div>

			</div>
			<!-- Contents -->
			<c:import url="../include/footer.jsp" />

		</div>
		<!-- Container -->

	</div>
	<!-- Body -->
</body>
</html>
