<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!doctype html>
<html>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Jan 2000 00:00:00 GMT">
<c:import url="../include/head.jsp">
</c:import>
<script type="text/javascript">
	window.onload = function() {
		$(function() {
			$("#loading").fadeOut();
			$("#NetworkContainer").fadeIn();
		});
	}
</script>
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
<link rel="stylesheet" href="${ctx}/learninglog/css/maconfirm.css" />
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
	padding: 1em;
	height: 1500px;
}

div#timelinecontainer {
	width: 100%;
	height: 30px;
}

div#timeline {
	overflow: scroll;
	width: 100%;
	height: 400px;
	font-size: 12px;
	background: #CCCCCC;
}

div#mapcontainer {
	width: 100%;
	height: 500px;
}

div#map {
	position: relative;
	width: 100%;
	height: 400px;
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
<style>
div#timelinecontainer {
	height: 220px;
}

div#mapcontainer {
	height: 400px;
}

.sigma {
	border-style: solid;
	border-color: #000000;
	border-width: 5pt;
}

table {
	border: 1px solid #000000;
}

a {
	color: #A50000;
	font-weight: bold;
}

thead th,thead td {
	color: #FFFFFF;
	text-align: center;
	padding: 2px 20px;
	background: #414141 url(../img/type02/thead_bg.jpg) repeat-x left top;
	font-weight: bold;
}

tbody th {
	text-align: left;
	color: #333333;
}

tbody th,tbody td {
	padding: 10px 10px;
	border-bottom: 1px dotted #000000;
}

tr.odd {
	background-color: #EEEFFF;
}
</style>

<script type="text/javascript">
var kavalue;
	$(function() {
		$('#chartcontain')
				.highcharts(
						{
							title : {
								text : 'Learning Logs chat',
								x : -20
							//center
							},
							subtitle : {
								text : 'Updated learning logs',
								x : -20
							},
							xAxis : {
								categories : [ 'Jan', 'Feb', 'Mar', 'Apr',
										'May', 'Jun', 'Jul', 'Aug', 'Sep',
										'Oct', 'Nov', 'Dec' ]
							},
							yAxis : {
								title : {
									text : 'The number of learning logs'
								},
								plotLines : [ {
									value : 0,
									width : 1,
									color : '#808080'
								} ]
							},
							tooltip : {
								valueSuffix : '個'
							},
							legend : {
								layout : 'vertical',
								align : 'right',
								verticalAlign : 'middle',
								borderWidth : 0
							},
							series : [ {
								name : 'Your line',
								data : [ ${learningnumbers[0]}, ${learningnumbers[1]}, ${learningnumbers[2]}, ${learningnumbers[3]}, ${learningnumbers[4]},
								         ${learningnumbers[5]}, ${learningnumbers[6]},${learningnumbers[7]}, ${learningnumbers[8]}, ${learningnumbers[9]},
								         ${learningnumbers[10]}, ${learningnumbers[11]} ]
							} ]
						});
	});
	function init() {
		 console.log(${learningnumbers[0]});
		document.getElementById("chartcontain").style.display = "none";
		document.getElementById("Rightstatistics").style.display = "none";
		document.getElementById("knowledgeawareness").style.display = "none";
		document.getElementById("kalselection").style.display = "none";
		document.getElementById("Rightkal").style.display = "none";
		// Instanciate sigma.js and customize rendering :
		var sigInst = sigma.init(document.getElementById('sigma-example'))
				.drawingProperties({
					defaultLabelColor : '#ccc',
					defaultLabelSize : 14,
					defaultLabelBGColor : '#fff',
					defaultLabelHoverColor : '#000',
					labelThreshold : 6,
					defaultEdgeType : 'curve'
				}).graphProperties({
					minNodeSize : 0.5,
					maxNodeSize : 5,
					minEdgeSize : 1,
					maxEdgeSize : 1
				}).mouseProperties({
					maxRatio : 4
				});
		
		
		sigInst.parseGexf("${baseURL}/js/networkanalysis/graph.gexf");
		
		// Bind events :
		var greyColor = '#666';
		var theMapZoom = 3;
		var lon = 134.198822;
		var lat = 79.767522;
		var theMapCenter = new mxn.LatLonPoint()
		theMapCenter.lon = lon;
		theMapCenter.lat = lat;
		theMapCenter.lng = lon;
		
		sigInst
				.bind(
						'overnodes',
						function(event) {
							var nodes = event.content;
							var neighbors = {};
							sigInst
									.iterEdges(
											function(e) {
												if (nodes.indexOf(e.source) < 0
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
				.bind(
						'outnodes',
						function() {
							sigInst
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
		
		
		
		sigInst.bind('downnodes', function(event) {
			var nodestate = event.content[0];
			var nodedata = nodestate.split(",");
			console.log(nodedata);
			if (nodedata[0] == "0") {
				var tm;

				var nodeurl = "${baseURL}/analysis/show?format=json&username="
						+ nodedata[1];

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
				tm = TimeMap.
				// add our new function to the map and timeline filters
				//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
				//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
				console.log(nodedata[2]);
				console.log(nodedata[3]);
				obj = new Date();

				obj.setFullYear(2013);

				obj.setMonth(4);

				obj.setDate(2);

				obj.setHours(3);
				tm.scrollToDate(Date(), false, true);

			} else if (nodedata[0] == "1") {
				var timedata = nodedata[3].split(".");

				var tm;
				var nodeurl = "${baseURL}/analysis/show?format=json&username="
						+ nodedata[2];

				tm = TimeMap.init({
					mapId : "map", // Id of map div element (required)
					timelineId : "timeline", // Id of timeline div element (required)
					options : {
						eventIconPath : "${baseURL}/js/timemap/images/",
						centerOnItems : false,
						mapCenter : theMapCenter,
						mapZoom : theMapZoom
					//mapCenter : new LatLonPoint(134,35)
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

				obj = new Date();

				obj.setFullYear(timedata[0]);

				obj.setMonth(timedata[1]);
				obj.setDate(2);

				obj.setHours(3);

				tm.scrollToDate(obj, false, true);

				var timedata = nodedata[5].split("#");
				var separatedata = "command data";

				//for(var i=0;i<timedata.length;i++){
				//	separatedata=timedata[i].split("$");
				//}

			}

			//nodedata = "http://ll.artsci.kyushu-u.ac.jp/learninglog/item/"
			//		+ nodedata;
			//location.href = nodedata;

			// add our new function to the map and timeline filters
			//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
			//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail

		});

		// Draw the graph :
		sigInst.draw();
		
	}

	if (document.addEventListener) {
		document.addEventListener("DOMContentLoaded", init, false);

	} else {
		window.onload = init;
	}

	//Layout 表示非表示設定		
	function mapdisplay(num) {

		if (num == 0) {
			document.getElementById("disp").style.display = "none";

		}
		if (num == 1) {

			document.getElementById("disp").style.display = "block";
		}

	}
	function Displayshow(num) {
		if (num == 0) {
			document.getElementById("sigma-example-parent").style.display = "block";
			document.getElementById("Rightlayout").style.display = "block";
			document.getElementById("disp").style.display = "block";
			document.getElementById("chartcontain").style.display = "none";
			document.getElementById("Rightstatistics").style.display = "none";
			document.getElementById("knowledgeawareness").style.display = "none";
			document.getElementById("kalselection").style.display = "none";
			document.getElementById("Rightkal").style.display = "none";
			
		}
		if (num == 1) {
			document.getElementById("sigma-example-parent").style.display = "none";
			document.getElementById("Rightlayout").style.display = "none";
			document.getElementById("disp").style.display = "none";
			document.getElementById("chartcontain").style.display = "none";
			document.getElementById("Rightstatistics").style.display = "none";
			document.getElementById("knowledgeawareness").style.display = "block";
			document.getElementById("kalselection").style.display = "block";
			document.getElementById("Rightkal").style.display = "block";
			
			if(kavalue==10){
				
			}
			else{
			var KAL = sigma.init(document.getElementById('knowledgeawarenesslens'))
			.drawingProperties({
					defaultLabelColor : '#ccc',
					defaultLabelSize : 14,
					defaultLabelBGColor : '#fff',
					defaultLabelHoverColor : '#000',
					labelThreshold : 6,
					defaultEdgeType : 'curve'
				}).graphProperties({
					minNodeSize : 0.5,
					maxNodeSize : 3,
					minEdgeSize : 1,
					maxEdgeSize : 2
				}).mouseProperties({
					maxRatio : 4
				});
			KAL.parseGexf("${baseURL}/js/networkanalysis/ka.gexf");
			// Bind events :
			var greyColor = '#666';
			var theMapZoom = 3;
			var lon = 134.198822;
			var lat = 79.767522;
			var theMapCenter = new mxn.LatLonPoint()
			theMapCenter.lon = lon;
			theMapCenter.lat = lat;
			theMapCenter.lng = lon;
			KAL.bind(
					'overnodes',
					function(event) {
						var nodes = event.content;
						var neighbors = {};
						KAL
								.iterEdges(
										function(e) {
											if (nodes.indexOf(e.source) < 0
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

					}).bind(
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
			kavalue=10;
			KAL.draw();}
		}
		if (num == 2) {
			document.getElementById("sigma-example-parent").style.display = "none";
			document.getElementById("Rightlayout").style.display = "none";
			document.getElementById("disp").style.display = "none";
			document.getElementById("knowledgeawareness").style.display = "none";
			document.getElementById("kalselection").style.display = "none";
			document.getElementById("chartcontain").style.display = "block";
			document.getElementById("Rightstatistics").style.display = "block";
			document.getElementById("Rightkal").style.display = "none";	
		}
	}
	
	$(document).ready(function() {

		document.getElementById("Analysis").className = "active";

	});
</script>


<body>

	<!-- <div id="Body">


		<div id="Contents"> -->
	<c:import url="../include/header.jsp">
	</c:import>


	<div id="NetworkContainer">
		<ul class="nav nav-tabs">
			<li class="active"><a
				href="/learninglog/analysis#sigma-example-parent" data-toggle="tab"
				onClick="Displayshow(0)">Network Graph and Time-Map</a></li>
			<li><a href="/learninglog/analysis#test" data-toggle="tab"
				onClick="Displayshow(1)">Knowledge Awareness Lens</a></li>
			<li><a href="/learninglog/analysis#test" data-toggle="tab"
				onClick="Displayshow(2)">Statistics</a></li>
			<li><a href="/learninglog/analysis#test" data-toggle="modal"
				data-target="#myModal">Description</a></li>

		</ul>
		<div id="LayoutTask" class="Layout">



			<div id="Center">
				<div class="span12 sigma-parent" id="knowledgeawareness">
					<div class="sigma" id="knowledgeawarenesslens"
						style="background-color: #3d3d3d; width: 800px; height: 600px; margin-bottom: 20px;"></div>
				</div>

				<div class="span12 sigma-parent" id="sigma-example-parent">
					<div class="sigma" id="sigma-example"
						style="background-color: #3d3d3d; width: 800px; height: 600px; margin-bottom: 20px;"></div>
				</div>
				<div id="chartcontain"
					style="width: 800px; height: 600px; margin-bottom: 20px;"></div>
			</div>
			<div id="Right">
				<div id="Rightlayout">
					<div class="navbar navbar-inner" style="position: static;">

						<div class="navbar-primary">
							<h3
								style="font-size: 14px; font-weight: bolder; line-height: 150%">
								Setting <br>
								<!--  <div class="btn-group">
									<button type="button" id="showMoreButton"
										onclick="mapdisplay(1)" class="btn btn-default">Show</button>
									<button type="button" id="showMoreButton"
										onclick="mapdisplay(0)" class="btn btn-default">None</button>
								</div>-->
							</h3>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapseOne">Layout Setting</a>
							</h4>
						</div>
						<div id="collapseOne" class="panel-collapse collapse in">
							<div class="panel-body">
								<table style="width:100%">
									<thead>
										<tr class="odd">
											<th>Layout Algorithm</th>

										</tr>
									</thead>

									<tbody>
										<c:url value="/analysis" var="itemUrl" />
										<tr class="even">
											<td><form:form commandName="analysisCond"
													action="${itemUrl}" method="post"
													enctype="multipart/form-data" style="margin:0px;">
													<form:select path="analysisalgo" cssStyle="width:100%"
														style="margin:0px;">
														<form:option value="nothing" label="nothing" />
														<form:option value="Yifan" label="Yifan" />
													</form:select></td>


											<tr class="even" style="margin: 0px;">
											<td style="margin: 0px;"><div class="operation"
													style="margin: 0px;">
													<ul class="moreInfo button" style="margin: 0px;">
														<li><input type="submit"
															class="btn btn-block btn-primary" value="Analysis"
															style="margin: 0px;" /></li>

													</ul>
												</div></td>
										</tr>
										</tr>
									</tbody>
								</table>
								</form:form>


							</div>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapseTwo"> Filtering Setting </a>
							</h4>
						</div>
						<div id="collapseTwo" class="panel-collapse collapse">
							<div class="panel-body">
								<table style="width:100%">
									<thead>
										<tr class="odd">
											<th>Filtering Function</th>

										</tr>
									</thead>

									<tbody>
										<c:url value="/analysis" var="itemUrl" />
										<tr class="even">
											<td style="margin: 0px;"><c:url value="/analysis"
													var="itemUrl" /> <form:form commandName="analysisCond"
													action="${itemUrl}" method="post"
													enctype="multipart/form-data" style="margin:0px;">
													<form:select path="degree" cssStyle="width:100%"
														style="margin:0px;">
														<form:option value="Degree Input"/>
														<form:option value="1" label="1" />
														<form:option value="2" label="2" />
														<form:option value="3" label="3 " />
														<form:option value="4" label="4" />
														<form:option value="5" label="5 " />
														<form:option value="6" label="6" />
														<form:option value="7" label="7 " />
														<form:option value="8" label="8" />
														<form:option value="9" label="9 " />
													</form:select></td>
										
										<tr class="odd">
											<td><form:input path="egofiltername"
													cssStyle="width:100%;height:100%" placeholder="Target LLO Input"></form:input></td>
										</tr>

										<tr class="odd">
											<td><form:select path="egofilterdistance"
													cssStyle="width:100%">
													<form:option value="Distance Input"/>
													<form:option value="1" label="1" />
													<form:option value="2" label="2" />
													<form:option value="3" label="3 " />
													<form:option value="4" label="4" />
													<form:option value="5" label="5 " />
													<form:option value="6" label="6" />
													<form:option value="7" label="7 " />
													<form:option value="8" label="8" />
													<form:option value="9" label="9 " />
												</form:select></td>
										</tr>

										<tr class="even" style="margin: 0px;">
											<td style="margin: 0px;"><div class="operation"
													style="margin: 0px;">
													<ul class="moreInfo button" style="margin: 0px;">
														<li><input type="submit"
															class="btn btn-block btn-primary" value="Analysis"
															style="margin: 0px;" /></li>

													</ul>
												</div></td>
										</tr>
										</tr>
									</tbody>
								</table>
								</form:form>
							</div>
						</div>
					</div>

				</div>
				<div id="Rightkal">
				<div class="navbar navbar-inner" style="position: static;">

						<div class="navbar-primary">
							<h3
								style="font-size: 14px; font-weight: bolder; line-height: 150%">
								Setting <br>

							</h3>
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#kalchange1"> Target Learning Log </a>
							</h4>
						</div>
						<div id="kalchange1" class="panel-collapse collapse">
							<div class="panel-body">
								<table style="width:100%">
									<thead>
										<tr class="odd">
											<th>Target LLO</th>

										</tr>
									</thead>

									<tbody>
										<c:url value="/analysis/kal" var="itemUrl" />
										<tr class="even">
											<td style="margin: 0px;"><c:url value="/analysis/kal"
													var="itemUrl" /> <form:form commandName="analysisCond"
													action="${itemUrl}" method="post"
													enctype="multipart/form-data" style="margin:0px;">
													<form:select path="degree" cssStyle="width:100%"
														style="margin:0px;">
														<form:option value="Target Degree"/>
														<form:option value="1" label="1" />
														<form:option value="2" label="2" />
														<form:option value="3" label="3 " />
														<form:option value="4" label="4" />
														<form:option value="5" label="5 " />
														<form:option value="6" label="6" />
														<form:option value="7" label="7 " />
														<form:option value="8" label="8" />
														<form:option value="9" label="9 " />
													</form:select></td>
										
										<tr class="odd">
											<td> <form:input path="egofiltername"
													cssStyle="width:100%;height:100%" placeholder="Target LLO Name (e.g. natto)"></form:input></td>
										</tr>

										<tr class="odd">
											<td><form:select path="egofilterdistance"
													cssStyle="width:100%">
													<form:option value="Target Distance"/>
													<form:option value="1" label="1" />
													<form:option value="2" label="2" />
													<form:option value="3" label="3 " />
													<form:option value="4" label="4" />
													<form:option value="5" label="5 " />
													<form:option value="6" label="6" />
													<form:option value="7" label="7 " />
													<form:option value="8" label="8" />
													<form:option value="9" label="9 " />
												</form:select></td>
										</tr>

										<tr class="even" style="margin: 0px;">
											<td style="margin: 0px;"><div class="operation"
													style="margin: 0px;">
													<ul class="moreInfo button" style="margin: 0px;">
														<li><input type="submit"
															class="btn btn-block btn-primary" value="Analysis"
															style="margin: 0px;" /></li>

													</ul>
												</div></td>
										</tr>
										</tr>
									</tbody>
								</table>
								</form:form>
							</div>
						</div>
					</div>
					
				</div>
				
				<div id="Rightstatistics">
					<div class="navbar navbar-inner" style="position: static;">

						<div class="navbar-primary">
							<h3
								style="font-size: 14px; font-weight: bolder; line-height: 150%">
								Setting <br>

							</h3>
						</div>
					</div>

					<div class="panel-group" id="accordion">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapseOne"> Graphing the learning logs </a>
								</h4>
							</div>
							<div id="collapseOne" class="panel-collapse collapse">
								<div class="panel-body">
									<c:url value="/analysis/stastics" var="stastics" />
									<form:form commandName="analysisCond" action="${stastics}"
										method="post" enctype="multipart/form-data"
										style="margin:0px;">
							Date　　　　<form:select path="degree" cssStyle="width:50%"
											style="margin:0px;">
											<form:option value="" label="" />
											<form:option value="2008" label="2008" />
											<form:option value="2009" label="2009" />
											<form:option value="2010" label="2010" />
											<form:option value="2011" label="2011" />
											<form:option value="2012" label="2012 " />
											<form:option value="2013" label="2013" />
											<form:option value="2014" label="2014 " />
											<form:option value="2015" label="2015" />
											<form:option value="2016" label="2016" />
											<form:option value="2017" label="2017" />
											<form:option value="2018" label="2018" />
										</form:select>
										<ul class="moreInfo button" style="margin: 0px;">
											<li><input type="submit"
												class="btn btn-block btn-primary" value="Analysis"
												style="margin: 0px;" /></li>

										</ul>
									</form:form>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapseTwo"> Graphing the learning logs </a>
								</h4>
							</div>
							<div id="collapseTwo" class="panel-collapse collapse">
								<div class="panel-body">Anim pariatur cliche
									reprehenderit, enim eiusmod high life accusamus terry
									richardson ad squid. 3 wolf moon officia aute, non cupidatat
									skateboard dolor brunch. Food truck quinoa nesciunt laborum
									eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on
									it squid single-origin coffee nulla assumenda shoreditch et.
									Nihil anim keffiyeh helvetica, craft beer labore wes anderson
									cred nesciunt sapiente ea proident. Ad vegan excepteur butcher
									vice lomo. Leggings occaecat craft beer farm-to-table, raw
									denim aesthetic synth nesciunt you probably haven't heard of
									them accusamus labore sustainable VHS.</div>
							</div>
						</div>
					</div>

				</div>
			</div>

		</div>
		<div id="LayoutTask" class="Layout">
			<div id="kalselection"></div>
		</div>

		<br>
		<div id="disp">
			<div id="timemap">
				<div id="timelinecontainer"></div>
				<div id="timeline"></div>

				<div id="mapcontainer">
					<div id="map"></div>
				</div>
			</div>
			<div id="ContentsContainer" style="font-size: 24px;" class="optional">
				<%--
                                          <form>
                            <select onchange="setSelectedTag(this);">
                                <option value="">All</option>
                                <option value="kitchen">kitchen</option>
                                <option value="communication">communication</option>
                             </select>
                        </form>
                --%>
			</div>
		</div>

	</div>

	<!-- /.modal -->
<div class="modal fade" id="myModal" tabindex="-1"
												role="dialog" aria-labelledby="myModalLabel"
												aria-hidden="true">
												<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
																aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">How to use network
						graph and Time-Map</h4>
				</div>
				<div class="modal-body">
					<ul>
						The interface of the network graph on web browser is shown as next
						Figure. The learners can recognize relationships between
						own/others author and knowledge by using the network graph
						interface. The learners' node (red or blue node) on the network
						graph is connected to many knowledge (yellow node) in accordance
						with node color. The network layout consists of using Yifan Hu
						multilevel layout (Y.F Hu, 2001, 2005). It is a very fast
						algorithm with a good quality on large graphs. It combines a
						force-directed model with a multilevel algorithm to reduce the
						complexity. The repulsive forces on one node from a cluster of
						distant nodes are approximated by a Barnes-Hut calculation (Barnes
						and P. Hut., 1986), which treats them as one super-node.
						<br>
						<br>
						<center>
							<img src="${baseURL}/images/networkgraphsample.png" width=400
																		height=400>
						</center>
						<br> The interface of Time-map on web browser is shown as
						next Figure. Figure shows the sample of learners' timeline. It
						represents the shift of learning history in accordance with lapse
						of time. The learners might forget the learning logs when and
						where they have learned before. Therefore, the system can remind
						the learners of them by combining timeline with map as shown in
						next Figure.
						<br>
						<br>
						<center>
							<img src="${baseURL}/images/timeline.png" width=400 height=400>
						</center>
						<br>
						<br>
						<center>
							<img src="${baseURL}/images/timemap.png" width=400 height=400>
						</center>
						<br>
					</ul>
				</div>

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
																aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">What is Color of
						nodes?</h4>
				</div>
				<div class="modal-body">
					<ul>
						Pink color node shows the learner’s own name on upper layer. If
						connecting the pink node to yellow node on intermediate layer,
						edge color will be decided as pink so that they can be easily
						recognized as the learner’s own logs.
						<br> Blue color nodes show the names of other learners on
						upper layer. If connecting the blue node to yellow color node on
						intermediate layer, edge color is decided blue color.
						<br> Yellow nodes represent both the learner own knowledge
						and the knowledge of other learners. For example, the learner can
						recognize his own knowledge because edge between the learner own
						name on upper layer and the knowledge on intermediate layer is
						pink color. In addition, the learner might discover knowledge of
						other learners related to own knowledge.
						<br>Red color node shows the location of learners on lowest
						layer. The node includes latitude, longitude and created time.
						<br>
						<br>
						<li>●Learner’s own name</li>Pink
						<li>●Names of other learners</li>Blue
						<li>●Knowledge of learners</li>Yellow
						<li>●Location of learners</li>Red
					</ul>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

				</div>
			</div>
		</div>
	</div>



										</body>

</html>
