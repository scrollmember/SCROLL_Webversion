<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>

<html>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Jan 2000 00:00:00 GMT">
<c:import url="../include/head.jsp">
</c:import>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/colorbox.js"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/timeliner.min.js"></script>

<script src="http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/sigma.min.js"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/sigma.parseGexf.js"></script>
<script type="text/javascript"
	src="${baseURL}/js/networkanalysis/sigma2.js"></script>


<script src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script src="${baseURL}/js/timemap/lib/mxn/mxn.js?(googlev3)"></script>
<script src="${baseURL}/js/timemap/lib/timeline-1.2.js"></script>
<script src="${baseURL}/js/timemap/timemap_full.pack.js"></script>
<script src="${baseURL}/js/maconfirm.js"></script>
<script src="${baseURL}/js/highchart/highcharts.js"></script>
<script src="${baseURL}/js/highchart/modules/exporting.js"></script>

<script src="${baseURL}/js/networkanalysis/osdc2012.reddit.js"></script>
<script src="${baseURL}/js/networkanalysis/osdc2012.misc.js"></script>
<script src="${baseURL}/js/networkanalysis/sigma.forceatlas2.js"></script>
<script src="http://maps.google.com/maps/api/js?sensor=true"></script>
<script>
		$(document).ready(function() {
			$.timeliner({
				startOpen:['#19540516EX', '#19630828EX']
			});
			$.timeliner({
				timelineContainer: '#timelineContainer_2'
			});
			// Colorbox Modal
			$(".CBmodal").colorbox({inline:true, initialWidth:100, maxWidth:682, initialHeight:100, transition:"elastic",speed:750});
			
			document.getElementById("Analysis").className = "active";
			
			
			 var local = false;
			 
				var greyColor = '#666';
				var theMapZoom = 4;
				var lon = 134.198822;
				var lat = 34.767522;
				var theMapCenter = new mxn.LatLonPoint()
				theMapCenter.lon = lon;
				theMapCenter.lat = lat;
				theMapCenter.lng = lon;
				
				//Time Map Searching
				
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
									
			 
			   // Circular plugin:
		    sigma.publicPrototype.circularize = function() {
		      var R = 100, i = 0, L = this.getNodesCount();
		   
		      this.iterNodes(function(n){
		        n.x = Math.cos(Math.PI*(i++)/L)*R;
		        n.y = Math.sin(Math.PI*(i++)/L)*R;
		      });
		   
		      return this.position(0,0,1).draw();
		    };

		    // Instanciate sigma.js:

		    var s1 = sigma.init($('.sigma-container')[0]).drawingProperties({
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
		    var s2 = sigma.init($('.sigma-container2')[0]).drawingProperties({
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
				maxNodeSize : 4,
				minEdgeSize : 1,
				maxEdgeSize : 1
			}).mouseProperties({
				maxRatio : 8
			});
		    
		    
		    //s1.parseGexf("http://ll.artsci.kyushu-u.ac.jp/Gexf/kpt.gexf");
		    s2.parseGexf("http://localhost:8080/learninglog/js/networkanalysis/tdadata.gexf");
			s1.parseGexf("http://localhost:8080/learninglog/js/networkanalysis/tdadata.gexf");
		    // Tweak:
		    // Give focus to sigma-container when sigma is clicked:
		    $('#sigma_mouse_1').click(function(){
		      $('.sigma-container').focus();
		      $('.sigma-container2').focus();
		     
		    });

	

		    $('form[name="post-url-form"]').submit(function(e){
		      if(local)
		    	  s1.parseGexf("http://localhost:8080/learninglog/js/networkanalysis/kpt.gexf");
		      else
		        reddit.pageComments($(this).find('input[type="text"]').attr('value'));

		      e.stopPropagation();
		      e.preventDefault();
		      return false;
		    });

		    $('.contains-icon').mouseover(function() {
		      $(this).find('.icon-button').addClass('icon-white');
		    }).mouseout(function() {
		      $(this).find('.icon-button').removeClass('icon-white');
		    });

		    // Init first loading:
		    if(local)
		      reddit.localPageComments('data_sample.json');
		    else
		      reddit.pageComments($(this).find('input[type="text"]').attr('value'));

		    /**
		     * NAVIGATION:
		     */
		    var moveDelay = 80,
		        zoomDelay = 2;

		    $('.move-icon').bind('click keypress',function(event) {
		      var newPos = s1.position();
		      var newPos2 = s2.position();
		      switch ($(this).attr('action')) {
		        case 'up':
		          newPos.stageY += moveDelay;
		          newPos2.stageY += moveDelay;
		          break;
		        case 'down':
		          newPos.stageY -= moveDelay;
		          break;
		        case 'left':
		          newPos.stageX += moveDelay;
		          break;
		        case 'right':
		          newPos.stageX -= moveDelay;
		          break;
		      }

		      s1.goTo(newPos.stageX, newPos.stageY);

		      event.stopPropagation();
		      return false;
		    });

		    $('.zoom-icon').bind('click keypress',function(event) {
		      var ratio = s1.position().ratio;
		      switch ($(this).attr('action')) {
		        case 'in':
		          ratio *= zoomDelay;
		          break;
		        case 'out':
		          ratio /= zoomDelay;
		          break;
		      }

		      s1.goTo(
		        $('.sigma-container').width() / 2,
		        $('.sigma-container').height() / 2,
		        ratio
		      );
		      s2.goTo(
				        $('.sigma-container2').width() / 2,
				        $('.sigma-container2').height() / 2,
				        ratio
				      );

		      event.stopPropagation();
		      return false;
		    });

		    $('.refresh-icon').bind('click keypress',function(event) {
		      s1.position(0, 0, 1).draw();
		      s2.position(0, 0, 1).draw();
		      event.stopPropagation();
		      return false;
		    });

		    $('.sigma-container').keydown(function(e) {
		      var newPos = s1.position(),
		          change = false;
		      newPos.ratio = undefined;

		      switch (e.keyCode) {
		        case 32:
		          s1.position(0, 0, 1).draw();
		          e.stopPropagation();
		          return false;
		        case 38:
		        case 75:
		          newPos.stageY += moveDelay;
		          change = true;
		          break;
		        case 40:
		        case 74:
		          newPos.stageY -= moveDelay;
		          change = true;
		          break;
		        case 37:
		        case 72:
		          newPos.stageX += moveDelay;
		          change = true;
		          break;
		        case 39:
		        case 76:
		          newPos.stageX -= moveDelay;
		          change = true;
		          break;
		        case 107:
		          newPos.ratio = s1.position().ratio * zoomDelay;
		          newPos.stageX = $('.sigma-container').width() / 2;
		          newPos.stageY = $('.sigma-container').height() / 2;
		          change = true;
		          break;
		        case 109:
		          newPos.ratio = s1.position().ratio / zoomDelay;
		          newPos.stageX = $('.sigma-container').width() / 2;
		          newPos.stageY = $('.sigma-container').height() / 2;
		          change = true;
		          break;
		      }

		      if(change) {
		        s1.goTo(newPos.stageX, newPos.stageY, newPos.ratio);
		        e.stopPropagation();
		        return false;
		      }
		    }).focus(function(){
		      s1.stopForceAtlas2();
		    }).blur(function(){
		      s1.startForceAtlas2();
		    });
		    


		    /**
		     * OTHER
		     */
		    function onAction() {
		      // Make all nodes unactive:
		      s1.iterNodes(function(n) {
		        n.active = false;
		      });
		    
		    }

		    // Autocompleted search field:
		    $('form.search-nodes-form').submit(function(e) {
		      onAction();
		      e.preventDefault();
		    });

		    // Node information:
		    function loadRedditUser(node) {
		      hideTwitterUser();

		      if(node['label'] !== '[deleted]')
		        reddit.user(node['label']);
		      else
		        showRedditUser();
		    }

		    function showRedditUser(obj) {
		      hideTwitterUser();

		      if(obj){
		        // Name :
		        $('div.node-info-container .node-name').append(
		          '<h3>' +
		            '<a target="_blank" href="' +
		              'http://www.reddit.com/user/' + obj['name'] +
		            '">' +
		            obj['name'] +
		            '</a>' +
		          '</h3>'
		        );

		        // Link Karma :
		        $('div.node-info-container .node-link-karma').append(
		          'Link karma: ' + obj['link_karma']
		        );

		        // Comments Karma :
		        $('div.node-info-container .node-comments-karma').append(
		          'Comments karma: ' + obj['comment_karma']
		        );
		      }else{
		        $('div.node-info-container .node-name').append(
		          '<h3>' +
		            'Oops, the requested user has been deleted.' +
		          '</h3>'
		        );
		      }
		    }

		    function hideTwitterUser() {
		      $('div.node-info-container .node-info').empty();
		    }

		    s1.bind(
					'overnodes',
					function(event) {
						var nodes = event.content;
						var neighbors = {};
						s1
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
						s1
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



		s1.bind('downnodes', function(event) {
		s1.iterNodes(
				function(n) {
					var theMapZoom = 6;
					
					if(n.id==event.content[0]){
					//alert(n.attr.attributes[4].val);
					
					for(var i=0;i<n.attr.attributes.length;i++){
						if(n.attr.attributes[i].attr=="Authorid"){
							var tm;

							var nodeurl = "${baseURL}/analysis/show?format=json&username="
									+ n.attr.attributes[i].val;

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

							//obj.setDate(timedata[2]);

						
							tm.scrollToDate(obj, false, true);
								}
							}
						}
						}
					}
					
				}
				
		);

		});
		 
			
		
	    s2.bind(
				'overnodes',
				function(event) {
					var nodes = event.content;
					var neighbors = {};
					s2
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
					s2
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



	s2.bind('downnodes', function(event) {
	s2.iterNodes(
			function(n) {
				var theMapZoom = 6;
				
				if(n.id==event.content[0]){
				//alert(n.attr.attributes[4].val);
				
				for(var i=0;i<n.attr.attributes.length;i++){
					if(n.attr.attributes[i].attr=="Authorid"){
						var tm;

						var nodeurl = "${baseURL}/analysis/show?format=json&username="
								+ n.attr.attributes[i].val;

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

						//obj.setDate(timedata[2]);

					
						tm.scrollToDate(obj, false, true);
							}
						}
					}
					}
				}
				
			}
			
	);

	});
		
		

	
		
		 });
	
		
		var place_lat=-1;
		var place_log=-1;
		navigator.geolocation.watchPosition(successCallback, errorCallback);
		
		function successCallback(position) {
			   place_lat = position.coords.latitude;
			 
			   place_log= position.coords.longitude;
			   var clickElem = document.getElementById('nextpage');
			   clickElem.href = "<c:url value="/item/add"/>"+"?lat="+place_lat+"&lng="+place_log;
			  
			 
		}
		function errorCallback(error){
			alert("位置情報取得できない");
		}
	</script>
<link rel="stylesheet" href="${ctx}/learninglog/css/maconfirm.css" />
<link rel="stylesheet" href="${ctx}/learninglog/css/screen.css"
	type="text/css" media="screen">
<link rel="stylesheet" href="${ctx}/learninglog/css/responsive.css"
	type="text/css" media="screen">
<link rel="stylesheet" href="${ctx}/learninglog/css/colorbox.css"
	type="text/css" media="screen">
<link rel="stylesheet" href="${ctx}/learninglog/css/osdc2012.css"
	type="text/css" media="screen">

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
var kavalue;
$(function() {
	$('#chartcontain')
			.highcharts(
					{
						title : {
							text : 'Learning Logs chat 2013年度',
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
	document.getElementById("Right").style.display = "none";
	document.getElementById("chartcontain").style.display = "none";
	document.getElementById("Rightstatistics").style.display = "none";
	document.getElementById("knowledgeawareness").style.display = "none";
	document.getElementById("kalselection").style.display = "none";
	document.getElementById("Rightkal").style.display = "none";
	document.getElementById("kousin").style.display = "none";
	document.getElementById("sigma-example-parent").style.display = "none";
	// Instanciate sigma.js and customize rendering :
	//var sigInst = sigma.init(document.getElementById('sigma-example'))
	//		.drawingProperties({
	//			defaultLabelColor : '#ccc',
	//			defaultLabelSize : 14,
	//			defaultLabelBGColor : '#fff',
	//			defaultLabelHoverColor : '#000',
	//			labelThreshold : 6,
	//			defaultEdgeType : 'curve',
	//			defaultEdgeArrow: 'target',
		//		edgeLabels : true
			//}).graphProperties({
				//minNodeSize : 0.5,
				//maxNodeSize : 20,
				//minEdgeSize : 1,
				//maxEdgeSize : 1
			//}).mouseProperties({
				//maxRatio : 8
		//	});
	
	//sigInst.parseGexf("http://ll.artsci.kyushu-u.ac.jp/Gexf/kpt.gexf");
	// Bind events :

						
						// add our new function to the map and timeline filters queuetype
						//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
						//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
						
						//now  = new Date();
						//obj  = new Date();

						//tm.scrollToDate(obj, false, true);

	
	
	

	// Draw the graph :
	//s1.draw();
	
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
		document.getElementById("sigma-example-parent2").style.display = "none";
		document.getElementById("Rightlayout").style.display = "block";
		document.getElementById("disp").style.display = "block";
		document.getElementById("chartcontain").style.display = "none";
		document.getElementById("Rightstatistics").style.display = "none";
		document.getElementById("knowledgeawareness").style.display = "none";
		document.getElementById("kalselection").style.display = "none";
		document.getElementById("Rightkal").style.display = "none";
		document.getElementById("Rightkal2").style.display = "block";
		
	}
	if (num == 5) {
		document.getElementById("sigma-example-parent").style.display = "none";
		document.getElementById("sigma-example-parent2").style.display = "block";
		document.getElementById("Rightlayout").style.display = "none";
		document.getElementById("disp").style.display = "none";
		document.getElementById("chartcontain").style.display = "none";
		document.getElementById("Rightstatistics").style.display = "none";
		document.getElementById("knowledgeawareness").style.display = "none";
		document.getElementById("kalselection").style.display = "none";
		document.getElementById("Rightkal").style.display = "none";
		document.getElementById("Rightkal2").style.display = "none";
		
	}
	if (num == 100) {
		
		document.getElementById("Rightkal2").style.display = "block";
		
		
	}
	if (num == 101) {
	
		document.getElementById("Rightkal2").style.display = "none";
		document.getElementById("Rightkal2_recommend2").style.display = "none";
		
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
		document.getElementById("Rightkal2").style.display = "none";
		
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
		KAL.parseGexf("http://ll.artsci.kyushu-u.ac.jp/Gexf/kpt.gexf");
		//KAL.parseGexf("${baseURL}/js/networkanalysis/ka5.gexf");
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
		document.getElementById("Rightkal2").style.display = "none";	
	}
}

</script>



<body>
	<c:import url="../include/header.jsp">
	</c:import>
	<div id="NetworkContainer" >

		<ul class="nav nav-tabs">
			<li class="active"><a
				href="/learninglog/analysis#sigma-example-parent" data-toggle="tab"
				onClick="Displayshow(5)">KAL based on TDA</a></li>
				<li><a
				href="/learninglog/analysis#sigma-example-parent2" data-toggle="tab"
				onClick="Displayshow(0)">KAL based on BUA</a></li>
			<!-- <li><a href="/learninglog/analysis#test" data-toggle="tab"
				onClick="Displayshow(1)">Knowledge Awareness Lens</a></li>
			<li><a href="/learninglog/analysis#test" data-toggle="tab"
				onClick="Displayshow(2)">Statistics</a></li>-->
			<li><a href="/learninglog/analysis#test" data-toggle="modal"
				data-target="#myModal">Description</a></li>
			<li><c:url value="/analysis" var="itemUrl" /> <form:form
					commandName="analysisCond" action="${itemUrl}" method="post"
					enctype="multipart/form-data" style="margin:0px;">
					<form:input path="egofiltername" cssStyle="width:100%;height:150%"
						placeholder="Target LLO Input"></form:input></li>
						<li style="margin-left:10px;">
						<form:select path="analysisalgo" cssStyle="width:100%"
												style="margin-left:30px;">
												<form:option value="Original" label="Original" />
												<form:option value="Yifan" />
												<form:option value="Random" label="Random" />
												<form:option value="ForceAtlas" label="ForceAtlas" />
									
											</form:select>
						
						</li>
			<li><input type="submit" class="btn btn-block btn-primary"
				value="Search" style="margin-left: 10px; width: 85px" /></li>

		</ul>

		</form:form>
		<div id="LayoutTask" class="Layout" >
			<div id="Center">

				<div class="span12 sigma-parent" id="knowledgeawareness">
					<div class="sigma" id="knowledgeawarenesslens"
						style="background-color: #3d3d3d; width: 700px; height: 600px;"></div>
				</div>
				<div class="span12 sigma-parent" id="sigma-example-parent" style="width:900px">
					<div class="sigma-container" id="sigma-example"
						style="background-color: #3d3d3d; width: 900px; height: 750px;">

					</div>
	
	<div class="control-panel" style="width:900px;height:1px">
    <!-- Here are the info about user -->
      <div class="move" style="margin-top:-80px;position: absolute">
              <div class="contains-icon move-icon" tabindex="0" action="up" title="Move up in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-up" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon move-icon" tabindex="0" action="left" title="Move left in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-left" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon move-icon" tabindex="0" action="right" title="Move right in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-right" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon move-icon" tabindex="0" action="down" title="Move down in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-down" style="background-color:#D9E5FF"></div>
              </div>
            </div>
  <div class="zoom" style="margin-top:-80px">
              <div class="contains-icon zoom-icon" tabindex="0" action="out" title="Zoom out the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-zoom-out" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon" tabindex="0" action="refresh" title="Reset graph position" style="background-color:#D9E5FF">
                <div class="icon-button refresh-icon icon-resize-full" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon zoom-icon" tabindex="0" action="in" title="Zoom in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-zoom-in" style="background-color:#D9E5FF"></div>
              </div>
            </div>

       

            <!-- And here go all the button to navigate in the graph -->
           
  </div>
				
					<div id="disp" style="width: 900px">
						<div id="timemap">
							<div id="timelinecontainer"></div>
							<div id="timeline"></div>

							<div id="mapcontainer">
								<div id="map"></div>
							</div>
						</div>
						<div id="ContentsContainer" style="font-size: 24px; width: 500px"
							class="optional"></div>
					</div>

				</div>

<div class="span12 sigma-parent2" id="sigma-example-parent2" style="width:900px">
					<div class="sigma-container2" id="sigma-example"
						style="background-color: #3d3d3d; width: 900px; height: 750px;">

					</div>
	
	<div class="control-panel" style="width:900px;height:1px">
    <!-- Here are the info about user -->
      <div class="move" style="margin-top:-80px;position: absolute">
              <div class="contains-icon move-icon" tabindex="0" action="up" title="Move up in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-up" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon move-icon" tabindex="0" action="left" title="Move left in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-left" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon move-icon" tabindex="0" action="right" title="Move right in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-right" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon move-icon" tabindex="0" action="down" title="Move down in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-arrow-down" style="background-color:#D9E5FF"></div>
              </div>
            </div>
  <div class="zoom" style="margin-top:-80px">
              <div class="contains-icon zoom-icon" tabindex="0" action="out" title="Zoom out the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-zoom-out" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon" tabindex="0" action="refresh" title="Reset graph position" style="background-color:#D9E5FF">
                <div class="icon-button refresh-icon icon-resize-full" style="background-color:#D9E5FF"></div>
              </div>
              <div class="contains-icon zoom-icon" tabindex="0" action="in" title="Zoom in the graph" style="background-color:#D9E5FF">
                <div class="icon-button icon-zoom-in" style="background-color:#D9E5FF"></div>
              </div>
            </div>

       

            <!-- And here go all the button to navigate in the graph -->
           
  </div>
				


				</div>




				<div id="chartcontain" style="width: 800px; height: 600px;"></div>

			</div>




			<div id="Right">
				<ul class="nav nav-tabs" style="width: 440px">
					<li class="active"><a href="/learninglog/analysis#test"
						data-toggle="modal" data-target="#myModal">Recommendation</a></li>
					<li><a class="addlink" href="<c:url value="/item/add"/>"
						id="nextpage">Add new object</a></li>

				</ul>
				<div class="recommendation_frame">
					<div class="moreInfo">
						<p>
							<font size="3">You might study these objects in the next
								place</font>
						</p>
						${analysisCond.egofiltername}
						<c:forEach begin="0" end="15" step="1" var="obj"
							varStatus="status" items="${Word_Recommend}">
							<ul class="moreInfo">
								<li>${status.index+1}.<a
									href="<c:url value="/item/${obj.target_itemid}"/>">${obj.label}
										(importance:${obj.degreeout})</a></li>
							</ul>
						</c:forEach>
						<p>
							<font size="3"> Moving from a region to another regions</font>
						</p>
						<div id="timelineContainer" class="timelineContainer">

							<div class="timelineToggle">
								<p>
									<a class="expandAll">+ expand all</a>
								</p>
							</div>

							<br class="clear">
							<c:forEach begin="0" end="15" step="1" var="obj"
								varStatus="status" items="${Place_Recommend}">
								<div class="timelineMajor">
									<h2 class="timelineMajorMarker">
										<span>${status.index+1}.${obj.label}(importance:${obj.degreeout})</span>
									</h2>
									<dl class="timelineMinor">
										<dt id="19540516">
											<a>${capturingplace.target_place}</a>
										</dt>
										<dd class="timelineEvent" id="19540516EX"
											style="display: none;">

											<c:forEach begin="0" end="15" step="1" var="ppattern"
												varStatus="status" items="${Place_Collocation}">
												<c:forEach begin="0" end="20" step="1" var="pattern"
													varStatus="status" items="${ppattern}">
													<c:if test="${pattern.place == obj.label}">

														<table
															style="line-height: 1.33em; border-collapse: collapse;"
															cellspacing="1″ cellpadding="5″>
															<tbody>
																<tr>
																	<c:choose>
																		<c:when test="${not empty pattern.image}">
																			<td rowspan="3"><img alt=""
																				src="${staticserverUrl}/${projectName}/${pattern.image}_320x240.png"
																				width="40px" height="40px" />&nbsp;</td>
																		</c:when>
																		<c:otherwise>
																			<td rowspan="3"><img width="40px" height="40px"
																				alt="" src="<c:url value="/images/no_image.gif" />" />
																				&nbsp;</td>
																		</c:otherwise>
																	</c:choose>
																	<td>Author</td>
																	<td>&nbsp;&nbsp;${pattern.nickname}</td>

																</tr>
																<tr>
																	<td>Log name</td>
																	<td>&nbsp;&nbsp;<a
																		href="<c:url value="/item/${pattern.itemid}"/>">${pattern.content}</a></td>

																</tr>
																<tr>
																	<td>Create time</td>
																	<td>&nbsp;&nbsp;${pattern.create_time}</td>

																</tr>

															</tbody>
														</table>
														<br>
													</c:if>

												</c:forEach>

											</c:forEach>

											<br class="clear">
										</dd>
										<!-- /.timelineEvent -->
									</dl>
									<!-- /.timelineMinor -->
								</div>
								<!-- /.timelineMajor -->
							</c:forEach>
							<br class="clear">
						</div>
						<!--<c:forEach begin="0" end="15" step="1" var="obj"
							varStatus="status" items="${Place_Recommend}">
							<ul class="moreInfo">
								<li>${status.index+1}.<a
									href="<c:url value="/item/${obj.target_itemid}"/>">${obj.label}
										(importance:${obj.degreeout})</a></li>
							</ul>
						</c:forEach>-->
					</div>
				</div>
				<div class="kousin" id="kousin">
					<div id="timelineContainer" class="timelineContainer">

						<div class="timelineToggle">
							<p>
								<a class="expandAll">+ expand all</a>
							</p>
						</div>

						<br class="clear">

						<c:forEach items="${CapturingPlace}" var="capturingplace">
							<div class="timelineMajor">
								<h2 class="timelineMajorMarker">
									<span>${capturingplace.itemtime}</span>
								</h2>
								<dl class="timelineMinor">
									<dt id="19540516">
										<a>${capturingplace.target_place}</a>
									</dt>
									<dd class="timelineEvent" id="19540516EX"
										style="display: none;">
										<p>test</p>
										<br class="clear">
									</dd>
									<!-- /.timelineEvent -->
								</dl>
								<!-- /.timelineMinor -->
							</div>
							<!-- /.timelineMajor -->
						</c:forEach>
						<br class="clear">
					</div>
					<!-- /#timelineContainer -->
				</div>


				<!--
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
  -->
				<!-- 
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapseTwo"> Filtering Setting </a>
							</h4>
						</div>
						<div id="collapseTwo" class="panel-collapse collapse in">
							<div class="panel-body">
								<table style="width: 100%">
									<thead>
										<tr class="odd">
											<th>Search and Filtering</th>

										</tr>
									</thead>

									<tbody>
										<c:url value="/analysis" var="itemUrl" />
										<form:form commandName="analysisCond" action="${itemUrl}"
											method="post" enctype="multipart/form-data"
											style="margin:0px;">
											<!--<tr class="even">
									<td style="margin: 0px;"><c:url value="/analysis"
													var="itemUrl" /> 
													<form:select path="degree" cssStyle="width:100%"
														style="margin:0px;">
														<form:option value="Degree Input" />
														<form:option value="1" label="1" />
														<form:option value="2" label="2" />
														<form:option value="3" label="3 " />
														<form:option value="4" label="4" />
														<form:option value="5" label="5 " />
														<form:option value="6" label="6" />
														<form:option value="7" label="7 " />
														<form:option value="8" label="8" />
														<form:option value="9" label="9 " />
													</form:select></td> </tr>-->
				<!-- 		<tr class="odd">

												<td><form:input path="egofiltername"
														cssStyle="width:100%;height:100%"
														placeholder="Target LLO Input"></form:input></td>
											</tr>
											<!-- 
										<tr class="odd">
											<td><form:select path="egofilterdistance"
													cssStyle="width:100%">
													<form:option value="Distance Input" />
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
 -->
				<!--  
											<tr class="even" style="margin: 0px;">
												<td style="margin: 0px;"><div class="operation"
														style="margin: 0px;">
														<ul class="moreInfo button" style="margin: 0px;">
															<li><input type="submit"
																class="btn btn-block btn-primary" value="Search"
																style="margin: 0px;" /></li>

														</ul>
													</div></td>
											</tr>
										</form:form>
									</tbody>
								</table>

							</div>
						</div>
					</div>

				</div>-->
				<br> <br>
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
						<div id="kalchange1" class="panel-collapse collapse in">
							<div class="panel-body">
								<table style="width: 100%">
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
														<form:option value="Target Degree" />
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
													cssStyle="width:100%;height:100%"
													placeholder="Target LLO Name (e.g. natto)"></form:input></td>
										</tr>

										<tr class="odd">
											<td><form:select path="egofilterdistance"
													cssStyle="width:100%">
													<form:option value="Target Distance" />
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


<div id="Right2">
				<ul class="nav nav-tabs" style="width: 440px">
				<li class="active"><a href="/learninglog/analysis#filter"
						data-toggle="tab" onClick="Displayshow(100)">Mining work</a></li>
					<li><a href="/learninglog/analysis#test"
						data-toggle="tab" onClick="Displayshow(101)">Recommendation</a></li>
					<li><a class="addlink" href="<c:url value="/item/add"/>"
						id="nextpage">Add new object</a></li>

				</ul>
				
				<div id="Rightkal2">

					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#kalchange1">Filttering items</a>
							</h4>
						</div>
						<div id="kalchange1" class="panel-collapse collapse in">
							<div class="panel-body">
								<table style="width: 100%">

									<tbody>
										<c:url value="/analysis/kal" var="itemUrl" />
										<tr><b><u>Nationality</u></b></tr>
										<tr class="even">
											<td style="margin-right: 20px;width:140px "><c:url value="/analysis/topdown"
													var="itemUrl" /> <form:form commandName="analysisCond"
													action="${itemUrl}" method="post"
													enctype="multipart/form-data" style="margin:0px;">
													<c:forEach items="${nationalitycounter}" var="obj" varStatus="status" >
													<c:if test="${status.index == '11'}">
													</td><td style="margin: 0px;width:140px">
													</c:if>
													<c:if test="${status.index == '22'}">
													</td><td style="margin: 0px;width:140px">
													</c:if>
													<form:checkbox path="nationality" value="${obj.name}" checked="checked"/>${obj.name}
													(${obj.total})<br>
													</c:forEach>
										<!--  			
											<form:checkbox path="nationality" value="Arabic" checked="checked"/>Arabic<br>
											<form:checkbox path="nationality" value="Armenian" checked="checked"/>Armenian<br>
											<form:checkbox path="nationality" value="Catalan" checked="checked"/>Catalan<br>
											<form:checkbox path="nationality" value="Chinese" checked="checked"/>Chinese<br>
											<form:checkbox path="nationality" value="Croatian" checked="checked"/>Croatian<br>
											<form:checkbox path="nationality" value="Danish" checked="checked"/>Danish<br>
											<form:checkbox path="nationality" value="Dutch" checked="checked"/>Dutch<br>
											<form:checkbox path="nationality" value="English" checked="checked"/>English<br>
											<form:checkbox path="nationality" value="French" checked="checked"/>French<br>
											
										</td><td style="margin: 0px;width:140px">
										<form:checkbox path="nationality" value="German" checked="checked"/>German<br>
											<form:checkbox path="nationality" value="Greek" checked="checked"/>Greek<br>
											<form:checkbox path="nationality" value="Hindi" checked="checked"/>Hindi<br>
											<form:checkbox path="nationality" value="Hungarian" checked="checked"/>Hungarian<br>
											<form:checkbox path="nationality" value="Icelandic" checked="checked"/>Icelandic<br>
											<form:checkbox path="nationality" value="Japanese" checked="checked"/>Japanese<br>
											<form:checkbox path="nationality" value="Khmer" checked="checked"/>Khmer<br>
											<form:checkbox path="nationality" value="Korean" checked="checked"/>Korean<br>
											<form:checkbox path="nationality" value="Malay" checked="checked"/>Malay<br>
											
											
										</td><td  align="left">
										<form:checkbox path="nationality" value="Mongolian" checked="checked"/>Mongolian<br>
											<form:checkbox path="nationality" value="Portuguese" checked="checked"/>Portuguese<br>
											<form:checkbox path="nationality" value="Russian" checked="checked"/>Russian<br>
											<form:checkbox path="nationality" value="Spanish" checked="checked"/>Spanish<br>
											<form:checkbox path="nationality" value="Swedish" checked="checked"/>Swedish<br>
											<form:checkbox path="nationality" value="Tagalog" checked="checked"/>Tagalog<br>
											<form:checkbox path="nationality" value="Thai" checked="checked"/>Thai<br>
											<form:checkbox path="nationality" value="Turkish" checked="checked"/>Turkish<br>
											<form:checkbox path="nationality" value="Vietname" checked="checked"/>Vietname<br>-->
											
											</td>
										<tr><td><br></td></tr>	
										<tr class="odd">
										<td><b><u>Gender</u></b></td>
											<td>
											<form:checkbox path="gender" value="man" />Man<br>
											<form:checkbox path="gender" value="woman" />Woman<br>
						
											</td>
										</tr>
										<tr class="odd">
										<td><b><u>Age</u></b></td>
											<td><br>
											<form:checkbox path="age" value="10" />10~20<br>
											<form:checkbox path="age" value="20" />20~30<br>
											<form:checkbox path="age" value="30" />30~40<br>
											<form:checkbox path="age" value="40" />40~50<br>
											<form:checkbox path="age" value="50" />50~60<br>
											</td>
										</tr>

									<tr class="odd">
										<td><b><u>Knowledge</u></b></td>
											<td><br>
											<c:forEach items="${poscounter}" var="posobj" varStatus="status" >
											<c:if test="${posobj.name == 'n'}">
											<form:checkbox path="knowledge" value="n" />Noun (${posobj.total})<br>
											</c:if>
											<c:if test="${posobj.name == 'v'}">
											<form:checkbox path="knowledge" value="v"/>Verb (${posobj.total})<br>
											</c:if>
											<c:if test="${posobj.name == 'a'}">
											<form:checkbox path="knowledge" value="a" />Adjective (${posobj.total})<br>
											</c:if>
											<c:if test="${posobj.name == 'r'}">
											<form:checkbox path="knowledge" value="r" />Adverb (${posobj.total})<br>
											</c:if>
											</c:forEach>
											

										</td>
										</tr>
										
										<tr class="odd">
										<td><b><u>Place</u></b></td>
											<td><br>
											<form:checkbox path="place" value="amusement_park" />amusement park<br>
											<form:checkbox path="place" value="bakery" />bakery<br>
											<form:checkbox path="place" value="bank" />bank<br>
											<form:checkbox path="place" value="bar" />bar<br>
												<form:checkbox path="place" value="beauty_salon" />beauty_salon<br>
											<form:checkbox path="place" value="book_store" />book_store<br>
											<form:checkbox path="place" value="bus_station" />bus_station<br>
											<form:checkbox path="place" value="cafe" />cafe<br>
												
												<form:checkbox path="place" value="car_dealer" />car_dealer<br>
											<form:checkbox path="place" value="car_repair" />car_repair<br>
											<form:checkbox path="place" value="church"/>church<br>
											<form:checkbox path="place" value="convenience_store" />convenience_store<br>
												
												<form:checkbox path="place" value="dentist" />dentist<br>
											<form:checkbox path="place" value="department_store" />department_store<br>
											<form:checkbox path="place" value="doctor" />doctor<br>
											
											<form:checkbox path="place" value="embassy" />embassy<br>
												<form:checkbox path="place" value="finance" />finance<br>
											<form:checkbox path="place" value="fire_station" />fire_station<br>
											<form:checkbox path="place" value="food"/>food<br>
											<form:checkbox path="place" value="gas_station" />gas_station<br>
												<form:checkbox path="place" value="grocery_or_supermarket" />grocery_or_supermarket<br>
											<form:checkbox path="place" value="hair_care" />hair_care<br>
											<form:checkbox path="place" value="hardware_store" />hardware_store<br>
											</td><td>
											<form:checkbox path="place" value="health" />health<br>
											
												<form:checkbox path="place" value="home_goods_store" />home_goods_store<br>
											<form:checkbox path="place" value="hospital" />hospital<br>
											<form:checkbox path="place" value="laundry" />laundry<br>
											<form:checkbox path="place" value="lawyer" />lawyer<br>
												<form:checkbox path="place" value="library"/>library<br>
											<form:checkbox path="place" value="liquor_store"/>liquor_store<br>
											<form:checkbox path="place" value="lodging" />lodging<br>
											<form:checkbox path="place" value="meal_delivery" />meal_delivery<br>
												<form:checkbox path="place" value="mosque" />mosque<br>
											<form:checkbox path="place" value="movie_theater" />movie_theater<br>
											<form:checkbox path="place" value="museum" />museum<br>
												<form:checkbox path="place" value="park" />park<br>
											<form:checkbox path="place" value="pet_store" />pet_store<br>
											<form:checkbox path="place" value="pharmacy" />pharmacy<br>
												<form:checkbox path="place" value="post_office" />post_office<br>
											<form:checkbox path="place" value="restaurant" />restaurant<br>
											<form:checkbox path="place" value="school" />school<br>
												<form:checkbox path="place" value="shopping_mall" />shopping_mall<br>
											<form:checkbox path="place" value="store" />store<br>
											<form:checkbox path="place" value="train_station" />train_station<br>
											<form:checkbox path="place" value="university" />university<br>
											

											</td>
										</tr>

										<tr class="odd">
										<td><b><u>Layout Type</u></b></td>
											<td><br>
											<form:radiobutton path="layouttype" value="Random" checked="checked"/>Random<br>
											<form:radiobutton path="layouttype" value="Yifan" />Yifan<br>
						
											</td>
										</tr>
										
										<tr class="even" style="margin: 0px;">
											<td style="margin: 0px;"><br><div class="operation"
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

	
				<div id="Rightkal2_Recommend2">
				<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#kalchange1">Recommendation Logs</a>
							</h4>
						</div>
						<div id="kalchange1" class="panel-collapse collapse in">
							<div class="panel-body">
							
							<span class="label label-info">Common logs among learners</span><br>
							<c:forEach begin="0" end="29" step="1" var="betweenness"
												varStatus="status" items="${betweenness_recommendation}">
												
							<a href="<c:url value="/item/${betweenness.id}"/>">${status.index+1}. ${betweenness.content}
										(importance:${betweenness.r_betweenness})</a><br>	
											
												</c:forEach><br>
							<span class="label label-info">Logs of Shortest path</span><br>
						<c:forEach begin="0" end="29" step="1" var="betweenness"
												varStatus="status" items="${closeness_recommendation}">
												
							<a href="<c:url value="/item/${betweenness.id}"/>">${status.index+1}. ${betweenness.content}
										(importance:${betweenness.r_clossness})</a><br>	
											
												</c:forEach><br>
							<span class="label label-info">Important logs among learners</span><br>
							<c:forEach begin="0" end="29" step="1" var="betweenness"
												varStatus="status" items="${degree_recommendation}">
												
							<a href="<c:url value="/item/${betweenness.id}"/>">${status.index+1}. ${betweenness.content}
										(importance:${betweenness.r_degree})</a><br>	
											
												</c:forEach><br>
							<span class="label label-info">Important and common logs among learners</span><br>
						<c:forEach begin="0" end="29" step="1" var="betweenness"
												varStatus="status" items="${original_tecommendation}">
												
							<a href="<c:url value="/item/${betweenness.id}"/>">${status.index+1}. ${betweenness.content}
										(importance:${betweenness.new_algorithm})</a><br>	
											
												</c:forEach><br>
							
						
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



		<!-- 
			<div id="disp">
				<div id="timemap">
					<div id="timelinecontainer"></div>
					<div id="timeline"></div>

					<div id="mapcontainer">
						<div id="map"></div>
					</div>
				</div>
				<div id="ContentsContainer" style="font-size: 24px;"
					class="optional">
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
 			-->
	</div>

	<!-- /.modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		style="height: 900px; width: 1250px; position: absolute; top: 300px; left: 430px">
		<div class="modal-dialog" style="height: 900px; width: 1140px">
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
