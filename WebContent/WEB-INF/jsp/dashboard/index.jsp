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
<script
	src="http://ll.artsci.kyushu-u.ac.jp/learninglog/js/highchart/highcharts.js"></script>
<script
	src="http://ll.artsci.kyushu-u.ac.jp/learninglog/js/highchart/modules/exporting.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		//document.getElementById("dashboard").className = "active";

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
						<div class="block">
							<table class="table">
								<tr>
									<td colspan="2">Uploaded learning logs</td>
									<td>
									<span class="badge">
									<c:forEach items="${uploadItemRanking}" var="uploadRanking"	end="9">
										${uploadRanking[1]}
										</c:forEach>
										</span>
										</td>
									<td></td>
								</tr>
								<tr>
									<td colspan="2">Completed quizzes</td>
									<td><span class="badge">${numberCompletedQuizzes}</span></td>
									<td></td>
								</tr>
								<tr>
									<td colspan="2">Learning log views</td>
									<td>
									<span class="badge">${numberLogsViews}</span></td>
									<td></td>
								</tr>
								<tr>
									<td>Memorized learning logs</td>
									<td><span class="badge">${correct_items.correct}</span></td>
									<td><a href="<c:url value='/dashboard/4/1' />">
											<button class="btn btn-info">View the logs</button>
									</a></td>
									<td></td>
								</tr>
								<tr>
									<td>Number of incorrect answers (once) </td>
									<td><span class="badge">${items.wrong1}</span></td>
									<td><a href="<c:url value='/dashboard/1/1' />"><button
												class="btn btn-success">View the logs</button></a></td>
									<td><a href="<c:url value='/quiz/1' />"><button class="btn btn-success">Enjoy the quiz!</button></td>
								</tr>
								<tr>
									<td>Number of incorrect answers (twice)</td>
									<td><span class="badge">${items.wrong2}</span></td>
									<td><a href="<c:url value='/dashboard/2/1' />"><button
												class="btn btn-warning">View the logs</button></a></td>
									<td><a href="<c:url value='/quiz/2' />"><button class="btn btn-warning">Enjoy the
											quiz!</button></a></td>
								</tr>
								<tr>
									<td>Number of incorrect answers (3 or more times) </td>
									<td><span class="badge">${items.wrong3}</span></td>
									<td><a href="<c:url value='/dashboard/3/1' />"><button
												class="btn btn-danger">View the logs</button></a></td>
									<td><a href="<c:url value='/quiz/3' />"><button class="btn btn-danger">Enjoy the
											quiz!</button></a></td>
								</tr>
								<tr>
									<td>Recommended learning logs</td>
									<td><span class="badge"></span></td>
									<td><a href="<c:url value='/dashboard/5/1' />"><button
												class="btn btn-default">View the logs</button></a></td>
									<td><a href="<c:url value='/quiz' />"><button class="btn btn-default">Enjoy the
											quiz!</button></a></td>
								</tr>								
							</table>
							<div id="chart"></div>
							<script>
								$(function() {
									var chart;

									$(document)
											.ready(
													function() {

														// Build the chart
														$('#chart')
																.highcharts(
																		{
																			chart : {
																				plotBackgroundColor : null,
																				plotBorderWidth : null,
																				plotShadow : false
																			},
																			title : {
																				text : ' '
																			},
																			tooltip : {
																				pointFormat : '{series.name}: <b>{point.percentage:.1f}%</b>'
																			},
																			plotOptions : {
																				pie : {
																					allowPointSelect : true,
																					cursor : 'pointer',
																					dataLabels : {
																						enabled : true,
																						format: '<b>{point.name}</b>: {point.percentage:.1f} %',
																		                style: {
																		                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
																		                    }
																					},
																					showInLegend : true
																				}
																			},
																			series : [ {
																				type : 'pie',
																				name : 'Browser share',
																				data : [																						
																						{
																							name : 'The learning logs of incorrect answer(once)',
																							color: '#5bb75b',
																							y : ${items.wrong1}																						
																						}	,
																						{
																							name : 'The learning logs of incorrect answer(twice)',
																							color: '#faa732',
																							y : ${items.wrong2}																						
																						}	,	
																						{
																							name : 'The learning logs of incorrect answer(3 or more times)',
																							color: '#da4f49',
																							y : ${items.wrong3}																						
																						}	,	
																						{
																							name : 'Memorized learning logs',
																							color: '#49afcd',
																							y : ${correct_items.correct},
																							sliced : true,
																							selected : true
																						}
																						 ],	
																			} ]
																		});
													});

								});
								/**
								 * Grid-light theme for Highcharts JS
								 * @author Torstein Honsi
								 */

								// Load the fonts
								Highcharts
										.createElement(
												'link',
												{
													href : 'http://fonts.googleapis.com/css?family=Dosis:400,600',
													rel : 'stylesheet',
													type : 'text/css'
												},
												null,
												document
														.getElementsByTagName('head')[0]);

								Highcharts.theme = {
									colors : [ "#7cb5ec", "#f7a35c", "#90ee7e",
											"#7798BF", "#aaeeee", "#ff0066",
											"#eeaaee", "#55BF3B", "#DF5353",
											"#7798BF", "#aaeeee" ],
									chart : {
										backgroundColor : null,
										style : {
											fontFamily : "Dosis, sans-serif"
										}
									},
									title : {
										style : {
											fontSize : '16px',
											fontWeight : 'bold',
											textTransform : 'uppercase'
										}
									},
									tooltip : {
										borderWidth : 0,
										backgroundColor : 'rgba(219,219,216,0.8)',
										shadow : false
									},
									legend : {
										itemStyle : {
											fontWeight : 'bold',
											fontSize : '13px'
										}
									},
									xAxis : {
										gridLineWidth : 1,
										labels : {
											style : {
												fontSize : '12px'
											}
										}
									},
									yAxis : {
										minorTickInterval : 'auto',
										title : {
											style : {
												textTransform : 'uppercase'
											}
										},
										labels : {
											style : {
												fontSize : '12px'
											}
										}
									},
									plotOptions : {
										candlestick : {
											lineColor : '#404048'
										}
									},

									// General
									background2 : '#F0F0EA'

								};

								// Apply the theme
								Highcharts.setOptions(Highcharts.theme);
							</script>
						</div>
					</div>
					<!-- Layout -->
					<div id="sideBanner"></div>
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
