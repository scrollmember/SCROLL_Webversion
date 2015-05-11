<%@page contentType="text/html" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!doctype html>
<html>
<c:import url="include/head.jsp">
	<c:param name="title" value="Learning Log" />
</c:import>
<script src="${baseURL}/js/highchart/highcharts.js"></script>
<script src="${baseURL}/js/highchart/modules/exporting.js"></script>
<script src="${baseURL}/js/notify/notify.min.js"></script>
<script src="${baseURL}/js/notify/styles/bootstrap/notify-bootstrap.js"></script>


<script type="text/javascript">
	$(document).ready(function() {
		

		document.getElementById("homework").className = "active";

	});
</script>
<script type="text/javascript">
var kavalue;
	$(function() {
		$('#chartcontain')
				.highcharts(
						{
							title : {
								text : 'Learning Logs chat 2014',
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
	
	
	$(function () {
        $('#chartcontain2').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'Learning Log chart'
            },
            subtitle: {
                text: 'Updated learning logs'
            },
            xAxis: {
                categories: [
                    'Jan',
                    'Feb',
                    'Mar',
                    'Apr',
                    'May',
                    'Jun',
                    'Jul',
                    'Aug',
                    'Sep',
                    'Oct',
                    'Nov',
                    'Dec'
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'The number of learning logs'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f}　個</b></td></tr>'
                    ,
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
                name: 'Your Learning logs',
                data: [${learningnumbers[0]}, ${learningnumbers[1]}, ${learningnumbers[2]}, ${learningnumbers[3]}, ${learningnumbers[4]},
				         ${learningnumbers[5]}, ${learningnumbers[6]},${learningnumbers[7]}, ${learningnumbers[8]}, ${learningnumbers[9]},
				         ${learningnumbers[10]}, ${learningnumbers[11]}]
    
            }, {
                name: 'All Learning logs',
                data: [${learningnumbers2[0]}, ${learningnumbers2[1]}, ${learningnumbers2[2]}, ${learningnumbers2[3]}, ${learningnumbers2[4]},
				         ${learningnumbers2[5]}, ${learningnumbers2[6]},${learningnumbers2[7]}, ${learningnumbers2[8]}, ${learningnumbers2[9]},
				         ${learningnumbers2[10]}, ${learningnumbers2[11]}]
    
            }]
        });
    });
	
	$(function () {
        $('#chartcontain3').highcharts({
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: 'Your Learning Time'
            },
            subtitle: {
                text: 'You are <font size="5" color="#ff0000">${learningType}</font>'
            },
            xAxis: [{
                categories: ['0:00', '1:00', '2:00', '3:00', '4:00', '5:00',
                    '6:00', '7:00', '8:00', '9:00', '10:00', '11:00','12:00', '13:00', '14:00', '15:00', '16:00',
                    '17:00', '18:00', '19:00', '20:00', '21:00', '22:00',"23:00"]
            }],
            yAxis: [{ // Primary yAxis
                labels: {
                    format: '{value}個',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                title: {
                    text: 'Your updated logs',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                }
            }, { // Secondary yAxis
                title: {
                    text: 'All updated learning logs',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                labels: {
                    format: '{value} 個',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                opposite: true
            }],
            tooltip: {
                shared: true
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                x: 120,
                verticalAlign: 'top',
                y: 100,
                floating: true,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            series: [{
                name: 'All One-day flow',
                type: 'column',
                yAxis: 1,
                data: [${learningnumbers4[0]}, ${learningnumbers4[1]}, ${learningnumbers4[2]}, ${learningnumbers4[3]}, ${learningnumbers4[4]},
				         ${learningnumbers4[5]}, ${learningnumbers4[6]},${learningnumbers4[7]}, ${learningnumbers4[8]}, ${learningnumbers4[9]},
				         ${learningnumbers4[10]}, ${learningnumbers4[11]},${learningnumbers4[12]}, ${learningnumbers4[13]}, ${learningnumbers4[14]}, ${learningnumbers4[15]},
				         ${learningnumbers4[16]},${learningnumbers4[17]}, ${learningnumbers4[18]},${learningnumbers4[19]}, ${learningnumbers4[20]}, ${learningnumbers4[21]},
				         ${learningnumbers4[22]}, ${learningnumbers4[23]}],
                tooltip: {
                    valueSuffix: ' 個'
                }
    
            }, {
                name: 'Your One-day flow',
                type: 'spline',
                yAxis: 1,
                data: [${learningnumbers3[0]}, ${learningnumbers3[1]}, ${learningnumbers3[2]}, ${learningnumbers3[3]}, ${learningnumbers3[4]},
				         ${learningnumbers3[5]}, ${learningnumbers3[6]},${learningnumbers3[7]}, ${learningnumbers3[8]}, ${learningnumbers3[9]},
				         ${learningnumbers3[10]}, ${learningnumbers3[11]},${learningnumbers3[12]}, ${learningnumbers3[13]}, ${learningnumbers3[14]}, ${learningnumbers3[15]},
				         ${learningnumbers3[16]},${learningnumbers3[17]}, ${learningnumbers3[18]},${learningnumbers3[19]}, ${learningnumbers3[20]}, ${learningnumbers3[21]},
				         ${learningnumbers3[22]}, ${learningnumbers3[23]}],
                tooltip: {
                    valueSuffix: '個'
                }
            }]
        });
    });
	
	$(function () {
        $('#chartcontain4').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'Learning Place Top 20'
            },
            subtitle: {
                text: 'Place Information'
            },
            xAxis: {
                type: 'category',
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'The number of place'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '',
            },
            series: [{
                name: 'Population',
                data: [
                    ['${placename[0]}', ${placecount[0]}],
                    ['${placename[1]}', ${placecount[1]}],
                    ['${placename[2]}', ${placecount[2]}],
                    ['${placename[3]}', ${placecount[3]}],
                    ['${placename[4]}', ${placecount[4]}],
                    ['${placename[5]}', ${placecount[5]}],
                    ['${placename[6]}', ${placecount[6]}],
                    ['${placename[7]}', ${placecount[7]}],
                    ['${placename[8]}', ${placecount[8]}],
                    ['${placename[9]}', ${placecount[9]}],
                    ['${placename[10]}', ${placecount[10]}],
                    ['${placename[11]}', ${placecount[11]}],
                    ['${placename[12]}', ${placecount[12]}],
                    ['${placename[13]}', ${placecount[13]}],
                    ['${placename[14]}', ${placecount[14]}],
                    ['${placename[15]}', ${placecount[15]}],
                    ['${placename[16]}', ${placecount[16]}],
                    ['${placename[17]}', ${placecount[17]}],
                    ['${placename[18]}', ${placecount[18]}],
                    ['${placename[19]}', ${placecount[19]}]
                ],
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif',
                        textShadow: '0 0 3px black'
                    }
                }
            }]
        });
    });
	
	function Displayshow(num) {
		if (num == 0) {
			document.getElementById("chartcontain3").style.display = "block";
			document.getElementById("chartcontain").style.display = "none";
			document.getElementById("chartcontain2").style.display = "none";
			document.getElementById("chartcontain4").style.display = "none";
			document.getElementById("rightplace").style.display = "none";
			document.getElementById("placereview").style.display = "none";
			document.getElementById("rightseason").style.display = "none";
			document.getElementById("seasonreview").style.display = "none";
			document.getElementById("rightday").style.display = "block";
			
		}
		else if(num==1){
			document.getElementById("chartcontain").style.display = "block";
			document.getElementById("chartcontain2").style.display = "block";
			document.getElementById("chartcontain3").style.display = "none";
			document.getElementById("chartcontain4").style.display = "none";
			document.getElementById("rightplace").style.display = "none";
			document.getElementById("placereview").style.display = "none";
			document.getElementById("rightseason").style.display = "block";
			document.getElementById("seasonreview").style.display = "block";
			document.getElementById("rightday").style.display = "none";
		}
		else if(num==2){
			document.getElementById("chartcontain").style.display = "none";
			document.getElementById("chartcontain2").style.display = "none";
			document.getElementById("chartcontain3").style.display = "none";
			document.getElementById("chartcontain4").style.display = "block";
			document.getElementById("rightplace").style.display = "block";
			document.getElementById("placereview").style.display = "block";
			document.getElementById("rightseason").style.display = "none";
			document.getElementById("seasonreview").style.display = "none";
			document.getElementById("rightday").style.display = "none";
		}
		
	}

	
	</script>
<body id="page_member_home">

	<div id="Body">


		<div id="Container">
			<c:import url="include/header.jsp" />
			<div id="Contents">

				<div id="IndexContainer">

					<div id="IndexLayout" class="Layout">

						<div id="Top">
							<div id="information_21" class="parts informationBox">
								<div class="body">
									<p></p>
								</div>
							</div>
							<!-- parts -->
						</div>
						<!-- Top -->
						<div id="Left">

							<div id="memberImageBox_22" class="parts memberImageBox">
								<p class="photo">
									<c:choose>
										<c:when test="${empty user.avatar}">
											<img alt="LearningUser"
												src="<c:url value="/images/no_image.gif" />" height="180"
												width="180" />
										</c:when>
										<c:otherwise>
											<img alt="LearningUser"
												src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />"
												/>
										</c:otherwise>
									</c:choose>
								</p>
								<p class="text">
									<shiro:principal property="nickname" />
								</p>
								<p class="text">Level : ${user.userLevel}</p>
								<p class="text">EXP : ${nowExperiencePoint} / Next :
									${nextExperiencePoint}</p>

								<div class="moreInfo">
									<%--
                                        <ul class="moreInfo">
                                            <li><a href=" <c:url value="/profile/avataredit"/>">Edit Photo</a></li>
                                            <li><a href="<c:url value='/profile' />">My Profile</a></li>
                                        </ul>
									--%>
								</div>
							</div>
							<!-- parts -->
							<!-- <div id="rankingbox1" class="parts ranking">
                                <div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-inverse">
                                    <div class="partsHeading"><h3>Upload Heroes</h3></div>
                                    <div class="partsInfo" style="color: navy">
                                    </div></div>
                                        <ul>
                                            <c:forEach items="${uploadItemRanking}" var="uploadRanking" end="9">
                                                <li><a href="<c:url value="/item"><c:param name="username" value="${uploadRanking[0].nickname}" /></c:url>">${uploadRanking[0].nickname}&nbsp;(${uploadRanking[1]})</a></li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>-->
							<!-- parts -->
							<!--  <div id="rankingbox1" class="parts ranking"
								style="font-size: 14px; font-weight: bolder; line-height: 150%">
								<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
										<h3
											style="font-size: 14px; font-weight: bolder; line-height: 150%">Upload
											ALLs</h3>
									</div>
								</div>

								<div class="partsInfo" style="color: navy">

									<ul>
										<c:forEach items="${uploadItemRanking}" var="uploadRanking"
											end="9">
											<li><a
												href="<c:url value="/item"><c:param name="username" value="${uploadRanking[0].nickname}" /></c:url>">${uploadRanking[0].nickname}&nbsp;(${uploadRanking[1]})</a></li>
										</c:forEach>

									</ul>
								</div>
							</div>-->



							<div id="rankingbox2" class="parts ranking">


								<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
										<h3
											style="font-size: 14px; font-weight: bolder; line-height: 150%">Answer
											Heroes</h3>
									</div>
								</div>
								<div class="partsInfo" style="color: navy">
									<ul>
										<c:forEach items="${answerRanking}" var="aRanking" end="9">
											<li><a
												href="<c:url value="/item"><c:param name="answeruser" value="${aRanking[0].nickname}" /></c:url>">${aRanking[0].nickname}&nbsp;(${aRanking[1]})</a></li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<!-- parts -->
							<div id="rankingbox2" class="parts ranking">
								<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
										<h3
											style="font-size: 14px; font-weight: bolder; line-height: 150%">Statistics</h3>
									</div>
								</div>
								<div class="partsInfo" style="color: navy">
									<ul>
										<c:forEach items="${stat}" var="stat">
											<li>${stat.key}&nbsp;(${stat.value})</li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<!-- parts -->
							<div id="homeRecentList_11" class="dparts homeRecentList">
								<div class="parts">
									<div class="navbar navbar-inner" style="position: static;">
										<div class="navbar-primary">
											<h3
												style="font-size: 14px; font-weight: bolder; line-height: 150%">Entries
												awaiting your answers</h3>
										</div>
									</div>
									<div class="block">
										<ul class="articleList">
											<c:forEach items="${toAnswerItems.result}" var="item">
												<li><span class="date"><fmt:formatDate
															value="${item.createTime}" type="date"
															pattern="yyyy/MM/dd" /></span><a
													href="<c:url value = "/item/${item.id}"/>">${item.question.content}(${fn:length(item.question.answerSet)})</a>
													(${item.author.nickname})</li>
											</c:forEach>
										</ul>
										<c:if test="${toAnswerItems.hasNext}">
											<div class="moreInfo">
												<ul class="moreInfo">
													<c:url value="/item" var="toAnswerQuesItemUrl">
														<c:forEach items="${user.myLangs}" var="lang">
															<c:param name="toAnswerQuesLangsCode"
																value="${lang.code}" />
														</c:forEach>
													</c:url>
													<li><a href="${toAnswerQuesItemUrl}">More...</a></li>
												</ul>
											</div>
										</c:if>
									</div>
								</div>
							</div>
							<div id="homeRecentList_12" class="dparts homeRecentList">
								<div class="parts">
									<div class="navbar navbar-inner" style="position: static;">
										<div class="navbar-primary">
											<h3
												style="font-size: 14px; font-weight: bolder; line-height: 150%">New
												entries written in the language you are learning</h3>
										</div>
									</div>
									<div class="block">
										<ul class="articleList">
											<c:forEach items="${toStudyItems.result}" var="item">
												<li><span class="date"><fmt:formatDate
															value="${item.updateTime}" type="date"
															pattern="yyyy/MM/dd" /></span><a
													href="<c:url value="/item/${item.id}"/>">${item.question.content}(${fn:length(item.question.answerSet)})</a>
													(${item.author.nickname})</li>
											</c:forEach>
										</ul>
										<c:if test="${toStudyItems.hasNext}">
											<div class="moreInfo">
												<c:url value="/item" var="toStudyQuesItemUrl">
													<c:forEach items="${user.studyLangs}" var="lang">
														<c:param name="toStudyQuesLangsCode" value="${lang.code}" />
													</c:forEach>
												</c:url>
												<ul class="moreInfo">
													<li><a href="${toStudyQuesItemUrl}">More...</a></li>
												</ul>
											</div>
										</c:if>
									</div>
								</div>
							</div>
							<div id="homeRecentList_13" class="dparts homeRecentList">
								<div class="parts">
									<div class="navbar navbar-inner" style="position: static;">
										<div class="navbar-primary">
											<h3
												style="font-size: 14px; font-weight: bolder; line-height: 150%">Latest
												answered questions for you</h3>
										</div>
									</div>
									<div class="block">
										<ul class="articleList">
											<c:forEach items="${answeredItems.result}" var="item">
												<li><span class="date"> <fmt:formatDate
															value="${item.updateTime}" type="date"
															pattern="yyyy/MM/dd" />
												</span> <a href="<c:url value="/item/${item.id}"/>">${item.question.content}(${fn:length(item.question.answerSet)})</a>
													(${item.author.nickname})</li>
											</c:forEach>
										</ul>
										<c:if test="${answeredItems.hasNext}">
											<div class="moreInfo">
												<c:url value="/item" var="answeredQuesItemUrl">
													<c:param name="userId" value="${user.id}" />
													<c:param name="hasAnswers" value="true" />
												</c:url>
												<ul class="moreInfo">
													<li><a href="${answeredQuesItemUrl}">More...</a></li>
												</ul>
											</div>
										</c:if>
									</div>
								</div>
							</div>

						</div>
						<!-- Left -->
						<div id="dashboard">

							<div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Dash
										Board</h3>
								</div>
							</div>

							<ul class="nav nav-tabs">
								<li class="active"><a
									href="/learninglog/index#sigma-example-parent"
									data-toggle="tab" onClick="Displayshow(0)">One-day Activity</a></li>
								<li><a href="/learninglog/index#test" data-toggle="tab"
									onClick="Displayshow(1)">Monthly Activity</a></li>
								<li><a href="/learninglog/index#test" data-toggle="tab"
									onClick="Displayshow(2)">Place Activity</a></li>


							</ul>

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
									<td class="box1"><a href="<c:url value='/quiz/1' />"><button class="btn btn-success">Enjoy the quiz!</button></td>
								</tr>
								<tr>
									<td>Number of incorrect answers (twice)</td>
									<td><span class="badge">${items.wrong2}</span></td>
									<td><a href="<c:url value='/dashboard/2/1' />"><button
												class="btn btn-warning">View the logs</button></a></td>
									<td class="box1"><a href="<c:url value='/quiz/2' />"><button class="btn btn-warning">Enjoy the
											quiz!</button></a></td>
								</tr>
								<tr>
									<td>Number of incorrect answers (3 or more times) </td>
									<td><span class="badge">${items.wrong3}</span></td>
									<td><a href="<c:url value='/dashboard/3/1' />"><button
												class="btn btn-danger">View the logs</button></a></td>
									<td class="box1"><a href="<c:url value='/quiz/3' />"><button class="btn btn-danger">Enjoy the
											quiz!</button></a></td>
								</tr>
								<tr>
									<td>Recommended learning logs</td>
									<td><span class="badge"></span></td>
									<td><a href="<c:url value='/dashboard/5/1' />"><button
												class="btn btn-default">View the logs</button></a></td>
									<td class="box1"><a href="<c:url value='/quiz' />"><button class="btn btn-default">Enjoy the
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
								
								$(".box1").notify("Enjoy the quizes!", {position: "right"})
							</script>
						</div>

						</div>


						<div id="Center">

							<div id="chartcontain4"
								style="width: 600px; height: 600px; margin-bottom: 20px;"></div>
							<div id="chartcontain3"
								style="width: 600px; height: 600px; margin-bottom: 20px;"></div>
							<div id="chartcontain"
								style="width: 600px; height: 500px; margin-bottom: 20px;"></div>
							<div id="chartcontain2"
								style="width: 600px; height: 500px; margin-bottom: 20px;"></div>

							<!--  
                                <div class="btn-group">
        <button class="btn dropdown-toggle" data-toggle="dropdown">Button <span class="caret"></span></button>
        <ul class="dropdown-menu">
            <li><a href="#">編集</a></li>
            <li><a href="#">削除</a></li>
            <li><a href="#">共有</a></li>
            <li class="divider"></li>
            <li><a href="#">Log out</a></li>
        </ul>
    </div>
    -->

						</div>
						<!-- Center -->

						<div id="right">
							<div class="navbar navbar-inner" style="position: static;"
								id="rightseason">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Learning
										Season</h3>
								</div>
							</div>

							<div id="seasonreview">
								<h5>The Spring Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Spring}" var="spring" begin="1" end="10"
									step="1">

									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>

								</c:forEach>
								<h5>The Summer Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Summer}" var="spring" begin="1" end="10"
									step="1">
									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>
								</c:forEach>
								<h5>The Fall Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Fall}" var="spring" begin="1" end="10"
									step="1">
									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>
								</c:forEach>

								<h5>The Winter Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Winter}" var="spring" begin="1" end="10"
									step="1">
									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>
								</c:forEach>
							</div>

							<div id="rightday">
								<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
										<h3
											style="font-size: 14px; font-weight: bolder; line-height: 150%">Learning
											Time Zone</h3>
									</div>
								</div>
								<h5>The Morning Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Morning}" var="spring" begin="1" end="10"
									step="1">
									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>
								</c:forEach>

								<h5>The Day Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Day}" var="spring" begin="1" end="10"
									step="1">
									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>
								</c:forEach>

								<h5>The Night Learning Logs (Latest top 10)</h5>
								<c:forEach items="${Night}" var="spring" begin="1" end="10"
									step="1">
									<li><a href="<c:url value="/item/${spring.id}"></c:url>">${spring.dashtime}(${spring.nickname})<br>
										<ul class="moreInfo">${spring.content}
											</ul></a></li>
								</c:forEach>

							</div>
							<div class="navbar navbar-inner" style="position: static;"
								id="rightplace">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Learning
										Place</h3>

								</div>
							</div>
							<div id="placereview">
								<h5>City Hall(市役所)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'city_hall'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Hospital(病院)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'hospital'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Accounting(会計事務所)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'accounting'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Transit Station(駅、停留所)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'transit_station'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Florist(花屋)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'florist'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Park(公園)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'park'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Doctor(医者)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'doctor'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Pet Store(ペット ショップ)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'doctor'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Dentist(歯医者)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'dentist'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Library(図書館)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'library'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Place of Worship(礼拝所)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'place_of_worship'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Bank(銀行)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'bank'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Embassy(大使館)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'embassy'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Bus Station(バスターミナル)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'bus_station'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Fire Station(消防署)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'fire_station'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>University(大学)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'university'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Laundry(クリーニング店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'laundry'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Mosque(モスク)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'mosque'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Gas Station(ガソリンスタンド)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'gas_station'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Bakery(ベーカリー、パン屋)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'bakery'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Store(店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'store'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Liquor Store(酒店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'liquor_store'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Clothing Store(衣料品店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'clothing_store'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Food(食料品店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'food'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Car Reqair(車の修理)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'car_repair'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Shopping Mall(ショッピングモール)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'shopping_mall'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Health(健康)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'health'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Hardware Store(金物店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'hardware_store'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Movie Theater(映画館)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'movie_theater'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Hair Care(散髪屋、ヘアケア)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'hair_care'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Layer(弁護士)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'lawyer'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Bar(居酒屋)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'bar'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Home Goods Store(インテリアショップ)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'home_goods_store'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Post Office(郵便局)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'post_office'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Meal Takeaway(テイクアウト)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'meal_takeaway'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Meal Takeaway(テイクアウト)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'meal_takeaway'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>amusement_park(遊園地)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'amusement_park'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Book Store(書店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'book_store'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Pharmacy(薬局)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'pharmacy'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Electronics Store(電器店)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'electronics_store'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Cafe(カフェ)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'cafe'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Restaurant(レストラン)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'restaurant'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Moving Company(引越会社)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'moving_company'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Convenience Store(コンビニエンスストア)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'convenience_store'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>School(学校)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'school'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Stadium(スタジアム)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'stadium'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>spa(温泉)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'spa'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Museum(博物館)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'museum'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>
								<h5>Church(教会)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'church'}" var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

								<h5>Grocery or Supermarket(スーパー)</h5>
								<c:forEach items="${PlaceAttribute}" var="place">
									<c:if test="${place.attribute == 'grocery_or_supermarket'}"
										var="establish" />
									<c:if test="${establish}">
										<li><a
											href="<c:url value="/item"><c:param name="place" value="${place.learningplace}" /></c:url>">${place.learningplace}(${place.cc})</a></li>
									</c:if>
								</c:forEach>

							</div>
						</div>
					</div>
					<!-- Layout -->
				</div>
				<!-- ContentsContainer -->
			</div>
			<!-- Contents -->
			<c:import url="include/footer.jsp" />
		</div>
		<!-- Container -->
	</div>
	<!-- Body -->


	<script type="text/javascript">
	document.getElementById("chartcontain").style.display = "none";
	document.getElementById("chartcontain2").style.display = "none";
	document.getElementById("chartcontain4").style.display = "none";
	document.getElementById("rightplace").style.display = "none";
	document.getElementById("placereview").style.display = "none";
	document.getElementById("rightseason").style.display = "none";
	document.getElementById("seasonreview").style.display = "none";
	document.getElementById("rightday").style.display = "block";
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-16851731-2']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript';
ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' :
'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0];
s.parentNode.insertBefore(ga, s);
})();

</script>


</body>

</html>
<c:import url="include/Slidermenu.jsp" />

