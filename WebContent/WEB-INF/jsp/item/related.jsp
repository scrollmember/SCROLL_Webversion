<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="userId">
	<shiro:principal property="id" />
</c:set>
<!doctype html>
<html>


<c:import url="../include/head.jsp">
	<c:param name="title" value="Related Objects" />
	<c:param name="javascript">
		<script type="text/javascript"
			src="<c:url value='/js/mediaelement/mediaelement-and-player.min.js' />"></script>
		<script type="text/javascript">
			$(function() {
				$("video, audio").mediaelementplayer();
			});
		</script>

	</c:param>
	<c:param name="css">
		<link rel="stylesheet" type="text/css" media="screen"
			href="<c:url value="/js/jquery/stars/ui.stars.min.css"/>" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="<c:url value='/js/mediaelement/mediaelementplayer.min.css' />" />
	</c:param>
</c:import>
<script src="http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/sigma.min.js"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/sigma.parseGexf.js"></script>
<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script src="${baseURL}/js/timemap/lib/mxn/mxn.js?(googlev3)"></script>
<script src="${baseURL}/js/timemap/lib/timeline-1.2.js"></script>
<script src="${baseURL}/js/timemap/timemap_full.pack.js"></script>
<script src="${baseURL}/js/maconfirm.js"></script>
<script src="${baseURL}/js/highchart/highcharts.js"></script>
<script src="${baseURL}/js/highchart/modules/exporting.js"></script>
<style>
.sigma {
	border-style: solid;
	border-color: #000000;
	border-width: 5pt;
}
</style>
<style>
div,p {
	font-family: Verdana, Arial, sans-serif;
	
}

p.content {
	font-size: 12px;
	width: 30em;
}

div#help {
	font-size: 12px;
	width: 45em;
	padding: 1em;
}

div#timemap {
	border-style: solid;
	border-color: #000000;
	border-width: 2pt;
	
	height: 505px;
}

div#timelinecontainer {
	width: 100%;
	height: 0px;
}

div#timeline {
	overflow: scroll;
	width: 100%;
	height: 200px;
	font-size: 12px;
	background: #CCCCCC;
}

div#mapcontainer {
	width: 100%;
	height: 300px;
}

div#map {
	position: relative;
	width: 100%;
	height: 300px;
	background: #EEEEEE;
}

div.infotitle {
	font-size: 14px;
	font-weight: bold;
}

div.infodescription {
	font-size: 14px;
	font-style: italic;
}

div.custominfostyle {
	font-family: Georgia, Garamond, serif;
	font-size: 1.5em;
	font-style: italic;
	width: 20em;
}
</style>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						Sleep(3);

						var tm;
						var now;
						var nodeurl = "${baseURL}/analysis/show?format=json&username="+"${userid}";

						tm = TimeMap.init({
							mapId : "map", // Id of map div element (required)
							timelineId : "timeline", // Id of timeline div element (required)
							options : {
								eventIconPath : "${baseURL}/js/timemap/images/",
								centerOnItems : false,
								mapCenter : theMapCenter,
								mapZoom : theMapZoom

							},
							datasets : [ {
								id : "llog",
								title : "Llog",
								theme : "red",
								type : "json_string",
								options : {
									url : nodeurl
								}
							} ],
							bandIntervals : [ Timeline.DateTime.WEEK,
									Timeline.DateTime.MONTH ]
						});
						
						
						// add our new function to the map and timeline filters
						//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
						//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
						
						now  = new Date();
						obj  = new Date();
						
		
						
					

						
						tm.scrollToDate(obj, false, true);

						
						
						document.getElementById("mylog").className = "active";
						var KAL = sigma
								.init(
										document
												.getElementById('knowledgeawarenesslens'))
								.drawingProperties({
									defaultLabelColor : '#ccc',
									defaultLabelSize : 14,
									defaultLabelBGColor : '#fff',
									defaultLabelHoverColor : '#000',
									labelThreshold : 6,
									defaultEdgeType : 'curve'
								}).graphProperties({
									minNodeSize : 3,
									maxNodeSize : 11,
									minEdgeSize : 1,
									maxEdgeSize : 3
								}).mouseProperties({
									maxRatio : 4
								});
						//KAL.parseGexf("${baseURL}/js/networkanalysis//ka.gexf");
						KAL.parseGexf("http://ll.artsci.kyushu-u.ac.jp/Gexf/ka.gexf");
						// Bind events :
						var greyColor = '#666';
						var theMapZoom = 3;
						var lon = 134.198822;
						var lat = 79.767522;
						var theMapCenter = new mxn.LatLonPoint()
						theMapCenter.lon = lon;
						theMapCenter.lat = lat;
						theMapCenter.lng = lon;
						KAL
								.bind(
										'overnodes',
										function(event) {
											var nodes = event.content;
											var neighbors = {};
											KAL
													.iterEdges(
															function(e) {
																if (nodes
																		.indexOf(e.source) < 0
																		&& nodes
																				.indexOf(e.target) < 0) {
																	if (!e.attr['grey']) {
																		e.attr['true_color'] = e.color;
																		e.color = greyColor;
																		e.attr['grey'] = 1;
																	}
																} else {
																	e.color = e.attr['grey'] ? e.attr['true_color']
																			: e.color;
																	e.attr['grey'] = 0;

																	neighbors[e.source] = 1;
																	neighbors[e.target] = 1;
																}
															})
													.iterNodes(
															function(n) {
																
																if (!neighbors[n.id]) {
																	if (!n.attr['grey']) {
																		n.attr['true_color'] = n.color;
																		n.color = greyColor;
																		n.attr['grey'] = 1;
																	}
																} else {
																	n.color = n.attr['grey'] ? n.attr['true_color']
																			: n.color;
																	n.attr['grey'] = 0;
																}
															}).draw(2, 2, 2);

										})
							.bind('downnodes', function(event) {
								KAL.iterNodes(
										function(n) {
											
											if(n.id==event.content[0]){
											//alert(n.attr.attributes[4].val);
											
											for(var i=0;i<n.attr.attributes.length;i++){
												if(n.attr.attributes[i].attr=="checknumber"){
													var tm;

													var nodeurl = "${baseURL}/analysis/show?format=json&username="
															+ n.attr.attributes[0].val;

													tm = TimeMap.init({
														mapId : "map", // Id of map div element (required)
														timelineId : "timeline", // Id of timeline div element (required)
														options : {
															eventIconPath : "${baseURL}/js/timemap/images/",
															centerOnItems : false,
															mapCenter : theMapCenter,
															mapZoom : theMapZoom

														},
														datasets : [ {
															id : "llog",
															title : "Llog",
															theme : "red",
															type : "json_string",
															options : {
																url : nodeurl
															}
														} ],
														bandIntervals : [ Timeline.DateTime.WEEK,
																Timeline.DateTime.MONTH ]
													});
													
													// add our new function to the map and timeline filters
													//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
													//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
													for(var i=0;i<n.attr.attributes.length;i++){
														if(n.attr.attributes[i].attr=="createtime"){
													var nodestate = n.attr.attributes[i].val;
													var timedata = nodestate.split("/");
													obj = new Date();

													obj.setFullYear(timedata[0]);

													obj.setMonth(timedata[1]);

													obj.setDate(timedata[2]);

												
													tm.scrollToDate(obj, false, true);
														}
													}
												}
												}
											}
											
										}
										
								);
							
					
							
								})
						
										
										
								.bind(
										'outnodes',
										function() {
											KAL
													.iterEdges(
															function(e) {
																e.color = e.attr['grey'] ? e.attr['true_color']
																		: e.color;
																e.attr['grey'] = 0;
															})
													.iterNodes(
															function(n) {
																n.color = n.attr['grey'] ? n.attr['true_color']
																		: n.color;
																n.attr['grey'] = 0;
															}).draw(2, 2, 2);
										});
						
					
						
						kavalue = 10;
						KAL.draw();
					});
	

	function Sleep(T) {
		var d1 = new Date().getTime();
		var d2 = new Date().getTime();
		while (d2 < d1 + 1000 * T) { //T秒待つ 
			d2 = new Date().getTime();
		}
		return;
	}
</script>

<body id="page_member_profile">
	<div id="Body">
		<div id="Container">

			<c:import url="../include/header.jsp" />
			<c:import url="../include/Slidermenu.jsp" />
			<div id="Contents">
				<div id="ContentsContainer">
					<div id="localNav"></div>
					<!-- localNav -->
					<div id="LayoutE" class="Layout">
						<div id="LayoutE" class="Layout">
						<div id="Karecommend">
							
							<div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Recommendation
										objects</h3>
								</div>
							</div>

							<div class="moreInfo">
								<p>You might study these objects in the next place</p>
								<c:forEach begin="0" end="15" step="1" var="obj" varStatus="status" items="${recommenditem}">						
　								<ul class="moreInfo">
									<li>${status.index+1}.<a href="<c:url value="/item/${obj.target_itemid}"/>">${obj.label} (importance:${obj.degreeout})</a></li>
								</ul>
								</c:forEach>
								
							</div>
							<div class="moreInfo">
								<p>These users are famous or representative learners in SCROLL system</p>
							<c:forEach begin="0" end="15" step="1" var="obj" varStatus="status" items="${recommenditem2}">						
　								<ul class="moreInfo">
								
									<li>${obj}</li>
								</ul>
								</c:forEach>
						</div>

					</div>
					</div>
						<div id="Ka">
							<div class="span12 sigma-parent" id="knowledgeawareness">
								<div class="sigma" id="knowledgeawarenesslens"
									style="background-color: #3d3d3d; width: 1160px; height: 540px; margin-bottom: 15px;"></div>
							</div>
						
						</div>
					
					<div id="LayoutE" class="Layout">
					
					<div id="disp">
			<div id="timemap">
				<div id="timelinecontainer"></div>
				<div id="timeline"></div>

				<div id="mapcontainer">
					<div id="map"></div>
				</div>
			</div>
			<div id="ContentsContainer" style="font-size: 24px;" class="optional">
		
			</div>
		</div>
					
					
					</div>
					
					<div id="LayoutE" class="Layout">

						<div id="Top"></div>
						<!-- Top -->
						<div id="Left" style="margin-top:20px">
							<div id="opbox" class="dparts nineTable">
								<div class="parts">
									<c:choose>
										<c:when
											test="${item.author.id eq userId && fromcreated && item.relogItem==null}">
											<button class="btn btn-primary"
												onclick="location.href='<c:url value="/item/${item.id}/edit"/>'">Return
												to edit</button>
											<button class="btn btn-info"
												onclick="location.href='<c:url value="/item/${item.id}" />'">Continue</button>
										</c:when>
										<c:otherwise>
											<button style="width: 100%"
												onclick="location.href='<c:url value="/item/${item.id}" />'">Return</button>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div id="memberImageBox_30" class="parts memberImageBox">
								<c:choose>
									<c:when test="${fileType eq 'image'}">
										<p class="photo">
											<a id="itemImage" class="zoom" href="#itemImageZoom"> <img
												alt=""
												src="${staticserverUrl}/${projectName}/${item.image.id}_320x240.png"
												width="240px" />
											</a>
										</p>
										<div id="itemImageZoom">
											<img alt=""
												src="${staticserverUrl}/${projectName}/${item.image.id}_800x600.png" />
										</div>
										<p class="text"></p>
										<div class="moreInfo">
											<ul class="moreInfo">
												<li><a class="zoom" href="#itemImageZoom">Zoom in</a></li>
											</ul>
										</div>
									</c:when>
									<c:when test="${fileType eq 'video'}">
										<p class="photo">
											<video id="itemvideo" class="video" width="240px" controls=""
												preload="auto"
												poster="${staticserverUrl}/${projectName}/${item.image.id}_320x240.png"
												style="background-color: black">
												<source
													src="${staticserverUrl}/${projectName}/${item.image.id}_320x240.mp4"></source>
											</video>
										</p>
									</c:when>
									<c:when test="${fileType eq 'audio'}">
										<audio controls style="width: 240px">
											<source
												src="${staticserverUrl}/${projectName}/${item.image.id}.mp3" />
											<source
												src="${staticserverUrl}/${projectName}/${item.image.id}.ogg" />
										</audio>
									</c:when>
									<c:when test="${fileType eq 'pdf'}">
										<p class="photo">
											<a id="itemImage"
												href="${staticserverUrl}/${projectName}/${item.image.id}.pdf">
												<img alt=""
												src="${staticserverUrl}/${projectName}/${item.image.id}_320x240.png"
												width="240px" />
											</a>
										</p>
									</c:when>
									<c:otherwise>
										<p class="photo">
											<img width="240px" alt=""
												src="<c:url value="/images/no_image.gif" />" />
										</p>
									</c:otherwise>
								</c:choose>
							</div>
							<!-- parts -->
							<div id="infobox" class="dparts nineTable">
								<div class="parts">
									<div class="navbar navbar-inner" style="position: static;">
										<div class="navbar-primary">
											<h3
												style="font-size: 14px; font-weight: bolder; line-height: 150%">Information</h3>
										</div>
									</div>
									<table>
										<tr>
											<th style="width: 80px">Title</th>
											<td><c:choose>
													<c:when test="${empty item.titles}">
                                                        NO NAME
                                                    </c:when>
													<c:otherwise>
														<table>
															<c:forEach items="${item.titles}" var="title">
																<tr>
																	<td style="width: 70px;">${title.language.name}</td>
																	<td>${title.content}</td>
																</tr>
															</c:forEach>
														</table>
													</c:otherwise>
												</c:choose></td>
										</tr>
										<tr>
											<th><label for="tag1">ID</label></th>
											<td>
												<table>
													<tr>
														<td style="width: 70px;">Barcode</td>
														<td>${item.barcode}</td>
													</tr>
													<tr>
														<td>QR Code</td>
														<td>${item.qrcode}</td>
													</tr>
													<tr>
														<td>RFID</td>
														<td>${item.rfid}</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<th>Author</th>
											<td><c:choose>
													<c:when test="${empty item.relogItem}">
														<a
															href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>"><c:out
																value="${item.author.nickname}" /></a>
													</c:when>
													<c:otherwise>
														<a href="<c:url value="/item/${item.relogItem.id}" />">ReLog</a> from <a
															href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>"><c:out
																value="${item.relogItem.author.nickname}" /></a>
													</c:otherwise>
												</c:choose></td>
										</tr>
										<tr>
											<th>Created at</th>
											<td><fmt:formatDate type="both" dateStyle="short"
													timeStyle="short" value="${item.createTime}" /></td>
										</tr>
										<tr>
											<th>Updated at</th>
											<td><fmt:formatDate type="both" dateStyle="short"
													timeStyle="short" value="${item.updateTime}" /></td>
										</tr>
										<tr>
											<th>Date of<br /> photo
											</th>
											<td></td>
										</tr>
										<tr>
											<th>Comment</th>
											<%
												pageContext.setAttribute("newLineChar", "\n");
											%>
											<td>${fn:replace(item.note,newLineChar,'<br />')}</td>
										</tr>
										<tr>
											<th>Tags</th>
											<td><c:forEach items="${item.itemTags}" var="tag">
													<a
														href="<c:url value="/item"><c:param name="tag" value="${tag.tag}" /></c:url>">${tag.tag}</a> &nbsp;
                                                    </c:forEach></td>
										</tr>
									</table>
								</div>
								<!-- parts -->
							</div>
							<!-- dparts -->
							<c:if
								test="${!empty item.itemLat && !empty item.itemLng && !empty item.itemZoom}">
								<div id="mapbox" class="dparts TagsParts">
									<div class="parts">
										<div class="navbar navbar-inner" style="position: static;">
											<div class="navbar-primary">
												<h3
													style="font-size: 14px; font-weight: bolder; line-height: 150%">Map</h3>
											</div>
										</div>
										<table>
											<tr>
												<td>
													<div id="map" style="height: 200px;">
														<img
															src="http://maps.google.com/maps/api/staticmap?size=260x200&sensor=false&center=${item.itemLat},${item.itemLng}zoom=${item.itemZoom}&mobile=true&markers=${item.itemLat},${item.itemLng}" />
													</div>
												</td>
											</tr>
											<tr>
												<td>Place：${item.place}</td>
											</tr>
										</table>
									</div>
								</div>
							</c:if>
						</div>
						<!-- Left -->
						<c:url value="/item/${item.id}/delete" var="itemresourcedelete" />
						<form:form id="itemDeleteForm" method="post"
							action="${itemresourcedelete}" />
						<c:url value="/item/${item.id}/relog" var="relogUrl" />
						<form:form id="itemRelogForm" method="post" action="${relogUrl}" />
						<c:url value="/item/${item.id}/teacherconfirm"
							var="teacherConfirmUrl" />
						<form:form id="teacherConfirmForm" action="${teacherConfirmUrl}"
							method="post" />
						<c:url value="/item/${item.id}/teacherreject"
							var="teacherRejectUrl" />
						<form:form id="teacherRejectForm" action="${teacherRejectUrl}"
							method="post" />
						<c:url value="/item/${item.id}/teacherdelcfmstatus"
							var="teacherDelCfmUrl" />
						<form:form id="teacherDelCfmStatusForm"
							action="${teacherDelCfmUrl}" method="post" />
						<div id="Center">
							<div id="profile" class="dparts searchResultList">
								<c:if test="${!empty relatedItemList.result}">
									<div class="parts">
										<div class="navbar navbar-inner" style="position: static;">
											<div class="navbar-primary">
												<h3
													style="font-size: 14px; font-weight: bolder; line-height: 150%">Related
													Objects</h3>
											</div>
										</div>
										<div class="block">
											<c:forEach items="${relatedItemList.result}" var="item">
												<c:set var="privateFlg" value="false" />
												<c:if
													test="${item.shareLevel=='PRIVATE' && userId!=item.author.id}">
													<c:set var="privateFlg" value="true" />
												</c:if>
												<div class="ditem">
													<div class="item">
														<table>
															<tbody>
																<tr>
																	<td rowspan="5" class="photo"><c:choose>
																			<c:when test="${!privateFlg}">
																				<a href="<c:url value="/item/${item.id}" />"
																					target="_blank"> <c:choose>
																						<c:when test="${(empty item.image)}">
																							<img height="70px" alt=""
																								src="<c:url value="/images/no_image.gif" />" />
																						</c:when>
																						<c:otherwise>
																							<img height="70px" alt=""
																								src="${staticserverUrl}/${projectName}/${item.image.id}_160x120.png" />
																						</c:otherwise>
																					</c:choose>
																				</a>
																				<br />
																				<a href="<c:url value="/item/${item.id}" />"
																					target="_blank">Details<img alt="photo"
																					src="<c:url value="/images/icon_camera.gif" />" /></a>
																			</c:when>
																			<c:otherwise>
																				<img height="70px" alt=""
																					src="<c:url value="/images/locked.png" />"
																					title="Private" />
                                                                        		Details<img
																					alt="photo"
																					src="<c:url value="/images/icon_camera.gif" />" />
																			</c:otherwise>
																		</c:choose></td>
																	<th>Title</th>
																	<td><c:choose>
																			<c:when test="${empty item.titles}">
                                                                            NO NAME
                                                                        </c:when>
																			<c:otherwise>
																				<table>
																					<c:forEach items="${item.titles}" var="title">
																						<tr>
																							<td style="width: 70px;">${title.language.name}</td>
																							<td>${title.content}</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</c:otherwise>
																		</c:choose></td>
																</tr>
																<tr>
																	<th>Author</th>
																	<td><a
																		href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>"
																		target="_blank"> <c:out
																				value="${item.author.nickname}" />
																	</a> <c:if test="${!empty item.relogItem}">
                                                                            &nbsp;(<a
																				href="<c:url value="/item/${item.relogItem.id}" />"
																				target="_blank">ReLog</a> from <a
																				href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>"
																				target="_blank"><c:out
																					value="${item.relogItem.author.nickname}" /></a>)
                                                                        </c:if>
																	</td>
																</tr>
																<tr>
																	<th>Create</th>
																	<td><fmt:formatDate type="both" dateStyle="full"
																			timeStyle="short" value="${item.createTime}" /></td>
																</tr>
																<tr>
																	<th>Update</th>
																	<td><fmt:formatDate type="both" dateStyle="full"
																			timeStyle="short" value="${item.updateTime}" /></td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
								</c:if>

							</div>
							<!-- dparts -->
						</div>

						<div id="Right">
							<div id="profile" class="dparts searchResultList">
								<c:if test="${!empty nearestItems.result}">
									<div class="parts">
										<div class="navbar navbar-inner" style="position: static;">
											<div class="navbar-primary">
												<h3
													style="font-size: 14px; font-weight: bolder; line-height: 150%">Nearest
													Objects</h3>
											</div>
										</div>
										<div class="block">
											<c:forEach items="${nearestItems.result}" var="item">
												<c:set var="privateFlg" value="false" />
												<c:if
													test="${item.shareLevel=='PRIVATE' && userId!=item.author.id}">
													<c:set var="privateFlg" value="true" />
												</c:if>
												<div class="ditem">
													<div class="item">
														<table>
															<tbody>
																<tr>
																	<td rowspan="5" class="photo"><c:choose>
																			<c:when test="${!privateFlg}">
																				<a href="<c:url value="/item/${item.id}" />"
																					target="_blank"> <c:choose>
																						<c:when test="${(empty item.image)}">
																							<img height="70px" alt=""
																								src="<c:url value="/images/no_image.gif" />" />
																						</c:when>
																						<c:otherwise>
																							<img height="70px" alt=""
																								src="${staticserverUrl}/${projectName}/${item.image.id}_160x120.png" />
																						</c:otherwise>
																					</c:choose>
																				</a>
																				<br />
																				<a href="<c:url value="/item/${item.id}" />"
																					target="_blank">Details<img alt="photo"
																					src="<c:url value="/images/icon_camera.gif" />" /></a>
																			</c:when>
																			<c:otherwise>
																				<img height="70px" alt=""
																					src="<c:url value="/images/locked.png" />"
																					title="Private" />
                                                                        		Details<img
																					alt="photo"
																					src="<c:url value="/images/icon_camera.gif" />" />
																			</c:otherwise>
																		</c:choose></td>
																	<th>Name</th>
																	<td><c:choose>
																			<c:when test="${empty item.titles}">
                                                                            NO NAME
                                                                        </c:when>
																			<c:otherwise>
																				<table>
																					<c:forEach items="${item.titles}" var="title">
																						<tr>
																							<td style="width: 70px;">${title.language.name}</td>
																							<td>${title.content}</td>
																						</tr>
																					</c:forEach>
																				</table>
																			</c:otherwise>
																		</c:choose></td>
																</tr>
																<tr>
																	<th>Author</th>
																	<td><a
																		href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>"
																		target="_blank"> <c:out
																				value="${item.author.nickname}" />
																	</a> <c:if test="${!empty item.relogItem}">
                                                                            &nbsp;(<a
																				href="<c:url value="/item/${item.relogItem.id}" />"
																				target="_blank">ReLog</a> from <a
																				href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>"
																				target="_blank"><c:out
																					value="${item.relogItem.author.nickname}" /></a>)
                                                                        </c:if>
																	</td>
																</tr>
																<tr>
																	<th>Created time</th>
																	<td><fmt:formatDate type="both" dateStyle="full"
																			timeStyle="short" value="${item.createTime}" /></td>
																</tr>
																<tr>
																	<th>Updated time</th>
																	<td><fmt:formatDate type="both" dateStyle="full"
																			timeStyle="short" value="${item.updateTime}" /></td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
								</c:if>
							</div>
						</div>
						<!-- Layout -->
						<div class="block"></div>
						<div id="sideBanner"></div>
						<!-- sideBanner -->
					</div>
					<!-- ContentsContainer -->
				</div>
				<!-- Contents -->
				<c:import url="../include/footer.jsp" />
			</div>
			<!-- Container -->
		</div>
		<!-- Body -->
	</div>
</body>
</html>