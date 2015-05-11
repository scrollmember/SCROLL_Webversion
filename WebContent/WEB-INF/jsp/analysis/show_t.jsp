
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
	src="<c:url value='js/networkanalysis/sigma.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='js/networkanalysis/sigma.parseGexf.js'/>"></script>
<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
  
        <script src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script src="${baseURL}/js/timemap/lib/mxn/mxn.js?(googlev3)"></script>
        <script src="${baseURL}/js/timemap/lib/timeline-1.2.js"></script>
        <script src="${baseURL}/js/timemap/timemap_full.pack.js"></script>
        <script src="${baseURL}/js/maconfirm.js"></script>
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
position : relative;
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
</style>

<script type="text/javascript">

	function init() {
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

		// Parse a GEXF encoded file to fill the graph
		// (requires "sigma.parseGexf.js" to be included)
		//sigInst.parseGexf('file:///Users/mouri/Documents/workspace/miraikan/WebContent/js/networkanalysis/graph.gexf');
		//Local folder
		//sigInst.paseGexf('/C://Users/mouri/Desktop/graph.gexf');
		
		//sigInst.parseGexf('<c:url value="/js/networkanalysis/graph.gexf" />');
		
		//Global folder
		//sigInst.parseGexf('/home/learninglog/Desktop/NetworkGexf/graph.gexf');
		//'${ctx}/images/ajaxLoader.gif'
		
		//local
		sigInst.parseGexf("<c:url value="/js/networkanalysis/graph.gexf"/>");
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
			if(nodedata[0]=="0"){
				var tm;
				
				var nodeurl="${baseURL}/analysis/show?format=json&username="+nodedata[1];
				
					tm = TimeMap.init({
						mapId : "map", // Id of map div element (required)
						timelineId : "timeline", // Id of timeline div element (required)
						options : {
							eventIconPath : "${baseURL}/js/timemap/images/",
							 centerOnItems: false,
					            mapCenter: theMapCenter,
					            mapZoom: theMapZoom
							
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
						bandIntervals : [ Timeline.DateTime.WEEK, Timeline.DateTime.MONTH ]
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

				
			}
			else if (nodedata[0]=="1"){
				var timedata = nodedata[3].split(".");
				
				var tm;
				var nodeurl="${baseURL}/analysis/show?format=json&username="+nodedata[2];
				
					tm = TimeMap.init({
						mapId : "map", // Id of map div element (required)
						timelineId : "timeline", // Id of timeline div element (required)
						options : {
							eventIconPath : "${baseURL}/js/timemap/images/",
							 centerOnItems: false,
					            mapCenter: theMapCenter,
					            mapZoom: theMapZoom
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
						bandIntervals : [ Timeline.DateTime.WEEK, Timeline.DateTime.MONTH ]
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
					var separatedata="command data";
					
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

	
	//var tm;
	
		//tm = TimeMap.init({
			//mapId : "map", // Id of map div element (required)
			//timelineId : "timeline", // Id of timeline div element (required)
			//options : {
			//	eventIconPath : "${baseURL}/js/timemap/images/"
			//},
			//datasets : [ {
			//	id : "llog",
			//	title : "Llog",
			//	theme : "orange",
			//	type : "json_string",
			//	options : {
			//		url : "${baseURL}/analysis/show?format=json"
			//	}
			//} ],
			//bandIntervals : [ Timeline.DateTime.WEEK, Timeline.DateTime.MONTH ]
	//	});
		// add our new function to the map and timeline filters
		//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
		//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
		//obj = new Date();

		//obj.setFullYear(2013);

		//obj.setMonth(4);

		//obj.setDate(2);

	//	obj.setHours(3);
		//tm.scrollToDate(Date(), false, true);
		//document.getElementById("").style.display="none";
		
		
function mapdisplay(num){
	
	if(num==0){
		document.getElementById("disp").style.display="none";
		
	}
	if(num==1){
		
		document.getElementById("disp").style.display="block";
	}
	
}
	
		
</script>


<body>

<!-- <div id="Body">


		<div id="Contents"> -->	
	<c:import url="../include/header.jsp">
						</c:import>
			<div id="NetworkContainer">
				<div id="LayoutTask" class="Layout">
					
					
						<!-- /navbar -->
						<!-- parts -->
				
					<div id="Center">
						<div class="span12 sigma-parent" id="sigma-example-parent">
							<div class="sigma" id="sigma-example"
								style="background-color: #3d3d3d; width: 800px; height: 600px;"></div>
						</div>
					</div>
					<div id="Right">
						<div class="navbar navbar-inner" style="position: static;">

							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Setting  <button style="float:right" id="showMoreButton" onclick="mapdisplay(1)" class="btn btn-primary"> SHOW </button><button style="float:right" id="showMoreButton" onclick="mapdisplay(0)" class="btn btn-primary">None</button>　</h3>
							</div>
						</div>
					<table>
					<tr><td height="35px"><B>Layout Algorithm</B></td></tr>
					<tr>	<td><c:url value="/analysis" var="itemUrl" />
						<form:form commandName="analysisCond" action="${itemUrl}"
							method="post" enctype="multipart/form-data">
							<form:select path="analysisalgo" cssStyle="width:80%" >
								<form:option value="nothing" label="nothing" />
								<form:option value="Yifan" label="Yifan " />
							</form:select>
						</td>
						</tr>
						<tr><td>	<div class="operation">
								<ul class="moreInfo button">
									<li><input type="submit" class="btn btn-info"
										value="Analysis" /></li>

								</ul>
							</div></td></tr>
							

		</table>				
					</form:form>
					
					<table>
					<tr><td height="35px"><B>Filtering Function</B></td></tr>
					<tr>	<td><c:url value="/analysis" var="itemUrl" />
						<form:form commandName="analysisCond" action="${itemUrl}"
							method="post" enctype="multipart/form-data">
							Degree Filter　　<form:select path="degree" cssStyle="width:50%">
							<form:option value="" label="" />
							<form:option value="1" label="1" />
								<form:option value="2" label="2" />
								<form:option value="3" label="3 " />
								<form:option value="4" label="4" />
								<form:option value="5" label="5 " />
								<form:option value="6" label="6" />
								<form:option value="7" label="7 " />
								<form:option value="8" label="8" />
								<form:option value="9" label="9 " />
							</form:select>
						</td>
						</tr>
						<tr>	<td>
							Target_name　    <form:input path="egofiltername" cssStyle="width:50%;height:100%"></form:input>
								
						</td></tr>
						<tr>
						<td>Distance　　　　<form:select path="egofilterdistance" cssStyle="width:50%">
						<form:option value="" label="" />
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
						<tr><td>	<div class="operation">
								<ul class="moreInfo button">
									<li><input type="submit" class="btn btn-info"
										value="Analysis" /></li>

								</ul>
							</div></td></tr>
							

		</table>				
					</form:form>
					
				
					</div>
					
				</div>
		<br>
<div id="disp">
	<div id="timemap">
				<div id="timelinecontainer">
					</div>
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
		



	
</body>
</html>