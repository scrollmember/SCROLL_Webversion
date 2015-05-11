<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html>

    <c:import url="../include/head.jsp">
		<c:param name="title" value="My Status" />
		<c:param name="javascript">
		    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
			<script type="text/javascript">
				google.load("visualization", "1", {packages:["corechart"]});
				google.setOnLoadCallback(drawChart);
				function drawChart() {
					var data = new google.visualization.DataTable();
					var test1 = ${nextExperiencePoint};
					data.addColumn('string', 'Task');
					data.addColumn('number', 'EXP');
					data.addRows([
						['UploadObjects', ${user.experiencePoint}],
						['Quiz', ${quizInfos.allscores}],
						['AnserQuestion', ${answerCount}],
					]);

		        var options = {
					width: 430, height: 230,
					//title: 'My EXP',
					backgroundColor: 'transparent', // 背景色を透過
					colors: ['#ff7f50', '#ffa500', '#f5deb3'],
					legend: {textStyle: {fontSize: 11}}
				};

				var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
				chart.draw(data, options);
				}
			</script>
		</c:param>
    </c:import>
<script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("mystatus").className = "active";

	});
</script>


    <body id="page_status_home">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutA" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    <p></p>
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Top -->

                            <div id="Left">
                                <div id="memberImageBox_22" class="parts memberImageBox">
                                    <p class="photo">
                                        <c:choose>
                                            <c:when test="${empty user.avatar}">
                                                <img alt="LearningUser" src="<c:url value="/images/no_image.gif" />" height="180" width="180" />
                                            </c:when>
                                            <c:otherwise>
                                                <img alt="LearningUser" src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />" />
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <p class="text"><shiro:principal property="nickname" /></p>
                                    <p class="text">Level : ${user.userLevel}</p>
                                    <p class="text">EXP : ${nowExperiencePoint} / Next : ${nextExperiencePoint}</p>

                                    <div class="moreInfo">
                                        <ul class="moreInfo">
                                            <li><a href=" <c:url value="/profile/avataredit"/>">Edit Photo</a></li>
                                            <li><a href="<c:url value="/profile"/>">My Profile</a></li>
                                        </ul>
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Left -->


                            <div id="Center">
                            	<div id="expRate" class="dparts homeRecentList">
									<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">My EXP Rate</h3></div></div>
									<div id="chart_div"></div>
								</div>

								<div id="levelRanking" class="parts panking">
									<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">LevelRanking</h3></div></div>
									<div class="partsInfo" style="color: navy">
									<table>
									<tr>
									<td>
									TopRank
									<ol>
										<c:forEach items="${levelRanking}" var="levelRanking" end="4" varStatus="status">
                                            <li><strong><c:out value="${status.count} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${levelRanking.nickname}" /></c:url>">${levelRanking.nickname}&nbsp;(${levelRanking.userLevel})</a></li>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    <td>
                                    YourRank
                                    <ol>
										<c:forEach items="${levelRanking}" var="levelRanking"  begin="${myLevelRankStart}" end="${myLevelRankEnd}"  varStatus="status">
										<c:if test="${status.count == 3}">
                                            <li><strong><c:out value="${status.index + 1} "/><a href="<c:url value="/item"><c:param name="username" value="${levelRanking.nickname}" /></c:url>">${levelRanking.nickname}&nbsp;(${levelRanking.userLevel})</a></strong></li>
                                        </c:if>
                                        <c:if test="${status.count != 3}">
                                            <li><strong><c:out value="${status.index + 1} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${levelRanking.nickname}" /></c:url>">${levelRanking.nickname}&nbsp;(${levelRanking.userLevel})</a></li>
                                        </c:if>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    </tr>
                                    </table>
                                    </div>
								</div>

								<div id="uploadRanking" class="parts panking">
									<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">UploadRanking</h3></div></div>
									<div class="partsInfo" style="color: navy">
									<table>
									<tr>
									<td>
									TopRank
									<ol>
										<c:forEach items="${uploadItemRanking}" var="uploadRanking" end="4" varStatus="status">
                                            <li><strong><c:out value="${status.count} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${uploadRanking[0].nickname}" /></c:url>">${uploadRanking[0].nickname}&nbsp;(${uploadRanking[1]})</a></li>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    <td>
                                    YourRank
                                    <ol>
										<c:forEach items="${uploadItemRanking}" var="uploadRanking"  begin="${myUploadRankStart}" end="${myUploadRankEnd}"  varStatus="status">
										<c:if test="${status.count == 3}">
                                            <li><strong><c:out value="${status.index + 1} "/><a href="<c:url value="/item"><c:param name="username" value="${uploadRanking[0].nickname}" /></c:url>">${uploadRanking[0].nickname}&nbsp;(${uploadRanking[1]})</a></strong></li>
                                        </c:if>
                                        <c:if test="${status.count != 3}">
                                        	<li><strong><c:out value="${status.index + 1} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${uploadRanking[0].nickname}" /></c:url>">${uploadRanking[0].nickname}&nbsp;(${uploadRanking[1]})</a></li>
                                        </c:if>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    </tr>
                                    </table>
                                    </div>
								</div>

								<div id="uploadRanking" class="parts panking">
									<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">AnswerRanking</h3></div></div>
									<div class="partsInfo" style="color: navy">
									<table>
									<tr>
									<td>
									TopRank
									<ol>
										<c:forEach items="${answerRanking}" var="answerRanking" end="4" varStatus="status">
                                            <li><strong><c:out value="${status.count} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${answerRanking[0].nickname}" /></c:url>">${answerRanking[0].nickname}&nbsp;(${answerRanking[1]})</a></li>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    <td>
                                    YourRank
                                    <ol>
										<c:forEach items="${answerRanking}" var="answerRanking" begin="${myAnswerRankStart}" end="${myAnswerRankEnd}" varStatus="status">
										<c:if test="${status.count == 3}">
                                            <li><strong><c:out value="${status.index + 1} "/><a href="<c:url value="/item"><c:param name="username" value="${answerRanking[0].nickname}" /></c:url>">${answerRanking[0].nickname}&nbsp;(${answerRanking[1]})</a></strong></li>
                                        </c:if>
                                        <c:if test="${status.count != 3}">
                                        	<li><strong><c:out value="${status.index + 1} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${answerRanking[0].nickname}" /></c:url>">${answerRanking[0].nickname}&nbsp;(${answerRanking[1]})</a></li>
                                        </c:if>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    </tr>
                                    </table>
                                    </div>
								</div>


								<div id="quizRanking" class="parts panking">
									<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">QuizRanking</h3></div></div>
									<div class="partsInfo" style="color: navy">
									<table>
									<tr>
									<td>
									TopRank
									<ol>
										<c:forEach items="${quizRanking}" var="quizRanking" end="4" varStatus="status">
                                            <li><strong><c:out value="${status.count} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${quizRanking[0].nickname}" /></c:url>">${quizRanking[0].nickname}&nbsp;(${quizRanking[1]})</a></li>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    <td>
                                    YourRank
                                    <ol>
										<c:forEach items="${quizRanking}" var="quizRanking" begin="${myQuizRankStart}" end="${myQuizRankEnd}" varStatus="status">
										<c:if test="${status.count == 3}">
                                            <li><strong><c:out value="${status.index + 1} "/><a href="<c:url value="/item"><c:param name="username" value="${quizRanking[0].nickname}" /></c:url>">${quizRanking[0].nickname}&nbsp;(${quizRanking[1]})</a></strong></li>
                                        </c:if>
                                        <c:if test="${status.count != 3}">
                                        	<li><strong><c:out value="${status.index + 1} "/></strong><a href="<c:url value="/item"><c:param name="username" value="${quizRanking[0].nickname}" /></c:url>">${quizRanking[0].nickname}&nbsp;(${quizRanking[1]})</a></li>
                                        </c:if>
                                        </c:forEach>
                                    </ol>
                                    </td>
                                    </tr>
                                    </table>
                                    </div>
								</div>


								<!--
								<div id="achievement" class="dparts homeRecentList">
									<div class="partsHeading"><h3>Achievements</h3></div>
								</div>
								-->


                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                        </div><!-- ContentsContainer -->
                    </div><!-- Contents -->
                    <c:import url="../include/footer.jsp" />
                </div><!-- Container -->
            </div><!-- Body -->
        </div>
        <c:import url="../include/Slidermenu.jsp" />
    </body>
</html>
