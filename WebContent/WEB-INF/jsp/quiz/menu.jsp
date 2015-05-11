<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>

<html>
<c:import url="../include/head.jsp">
</c:import>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="${ctx}/learninglog/css/screen.css"
	type="text/css" media="screen">
</head>
<body>
	<div id="Container">
		<c:import url="../include/header.jsp" />
		<div id="Contents">
			<div id="ContentsContainer">

				<div id="ItemLayout" class="Layout">

					<div id="Left">

						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Quiz
									Selections</h3>

							</div>
						</div>
						&nbsp;&nbsp;&nbsp;<span class="label label-inverse">Quiz
							Options</span><br> <br>
						<ul class="nav nav-list">
							<li style="font-size: 20px"><a
								href="<c:url value="/quiz" />"><img alt=""
									src="<c:url value='/images/quizicon/Focus.png'/>" width="35px"
									height="35px" />Daily Quiz</a></li>
							<li style="font-size: 20px"><a
								href="<c:url value="/quiz/myquiz" />"><img alt=""
									src="<c:url value='/images/quizicon/ColorStroke.png'/>"
									width="35px" height="35px" />My Quiz</a></li>
							<!-- <li><a href="<c:url value="/quiz/level1" />">Beginner Quiz</a></li>
							<li><a href="#">Intermediate Quiz</a></li>
							<li><a href="#">Senior Quiz</a></li>
							 -->
						</ul>

						<ul class="nav nav-list">
							<li style="font-size: 20px"><a
								href="<c:url value="/quiz/logs" />"><img alt=""
									src="<c:url value='/images/quizicon/Intensify.png'/>"
									width="35px" height="35px" />Quiz Logs</a></li>
							<li style="font-size: 20px"><a
								href="<c:url value="/quiz/create" />"><img alt=""
									src="<c:url value='/images/quizicon/Outline.png'/>"
									width="35px" height="35px" />Quiz Create</a></li>
							<li style="font-size: 20px"><a href="#"><img alt=""
									src="<c:url value='/images/quizicon/SolarWalk.png'/>"
									width="35px" height="35px" />Quiz Send & Recommendation</a></li>
							<li style="font-size: 20px"><a href="#"><img alt=""
									src="<c:url value='/images/quizicon/UnRarX.png'/>" width="35px"
									height="35px" />Quiz Delete</a></li>

						</ul>

					</div>
					<div id="Center">


						
							<div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">QuizRanking (Quiz Score)</h3>
								</div>
							</div>
						
								<table>
									<tr>
										<td>

										
												<c:forEach items="${quizRanking}" var="quizRanking" end="9"
													varStatus="status">
													<li style="font-size: 20px; margin-left: 10px"><c:if
															test="${status.count == 1}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai1.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 2}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai2.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 3}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai3.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 4}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai4.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 5}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai5.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 6}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai6.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 7}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai7.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 8}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai8.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 9}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai9.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <c:if test="${status.count == 10}">
															<strong><img alt=""
																src="<c:url value='/images/ranking/ranking-free-sozai10.png'/>"
																width="50px" height="50px" /></strong>
														</c:if> <a style="margin-left: 20px"
														href="<c:url value="/item"><c:param name="username" value="${quizRanking[0].nickname}" /></c:url>">${quizRanking[0].nickname}&nbsp;</a>(${quizRanking[1]})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>

													<hr>
												</c:forEach>
										
										</td>
										<td></td>
									</tr>
								</table>
						
				

						<!--  
						<span class="label label-inverse">Wrong
							quiz objects</span><br> <br>
						<center>
							<div class="quizwrongmenu_frame">
								<c:forEach begin="0" end="500" step="1" var="wronghistory"
									varStatus="status" items="${quizwrongitems}">


									<table style="line-height: 1.33em; border-collapse: collapse;"
										cellspacing="1″ cellpadding="5″>
										<tbody>
											<tr>
												<c:choose>
													<c:when test="${not empty wronghistory.image}">
														<td rowspan="3"><img alt=""
															src="${staticserverUrl}/${projectName}/${wronghistory.image}_320x240.png"
															width="85px" height="85px" />&nbsp;</td>
													</c:when>
													<c:otherwise>
														<td rowspan="3"><img width="85px" height="85px"
															alt="" src="<c:url value="/images/no_image.gif" />" />
															&nbsp;</td>
													</c:otherwise>
												</c:choose>


											</tr>
											<tr>
												<td>Learning Time</td>
												<td>&nbsp;&nbsp;${wronghistory.create_date}</td>

											</tr>
											<tr>

												<td>Log name</td>
												<td>&nbsp;&nbsp;<a
													href="<c:url value="/item/${wronghistory.item_id}"/>">${wronghistory.content}</a></td>

											</tr>



										</tbody>
									</table>
									<br>


								</c:forEach>
							</div>
						</center>-->
					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>