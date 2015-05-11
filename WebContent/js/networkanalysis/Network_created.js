var kavalue;

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
					defaultEdgeType : 'curve',
					defaultEdgeArrow: 'target',
					edgeLabels : true
				}).graphProperties({
					minNodeSize : 0.5,
					maxNodeSize : 20,
					minEdgeSize : 1,
					maxEdgeSize : 1
				}).mouseProperties({
					maxRatio : 8
				});
		
		
		sigInst.parseGexf("${baseURL}/js/networkanalysis/kpt.gexf");
		//sigInst.parseGexf("http://ll.artsci.kyushu-u.ac.jp/NetworkGexf/ka.gexf");
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
					edgeLabels : 'true',
					defaultEdgeArrow: 'target',
					defaultEdgeType : 'curve'
				}).graphProperties({
					minNodeSize : 3,
					maxNodeSize : 10,
					minEdgeSize : 1,
					maxEdgeSize : 3
				}).mouseProperties({
					maxRatio : 4
				});
			//KAL.parseGexf("${baseURL}/js/networkanalysis/graph.gexf");
			KAL.parseGexf("${baseURL}/js/networkanalysis/ka5.gexf");
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