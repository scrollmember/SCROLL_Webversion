<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="My Logs" />
        <c:param name="javascript">
        	<script type="text/javascript">
        	$(function(){
        		$("#jumpTimemap").click(function(){
        			window.open("${baseURL}/timemap/mylogs");
        		});
        		$("#jumpLogstate").click(
        				function(){
        					window.open("${baseURL}/logstate");		
        				}
        		);
        	});
        	</script>
		</c:param>
    </c:import>
     <script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("mylog").className = "active";

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

    <body id="page_member_home">
        <div id="Body">
            <div id="Container">
            
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutA" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
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
                                                <img alt="LearningUser" src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />"  />
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
                                <div id="timemapBox" class="parts memberImageBox" style="text-align: center">
									<button id="jumpTimemap" class="btn btn-block btn-primary">Go to TimeMap</button>
                                </div>
                                <div id="timemapBox" class="parts memberImageBox" style="text-align: center">
									<button id="jumpLogstate" class="btn btn-block btn-info">My Learning Log States</button>
                                </div>
                            </div><!-- Left -->
                            <div id="Center">
                                <div id="homeRecentList_11" class="dparts homeRecentList"><div class="parts">
                                         <div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
                                            <h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">My Logs</h3>
                                            <a class="addlink" href="<c:url value="/item/add"/>" id="nextpage">Add new object</a>
                                        </div></div>
                                        <div class="block">
                                            <ul class="articleList">
                                                <c:forEach items="${myitems.result}" var="myitem">
                                                    <li><span class="date"><fmt:formatDate value="${myitem.createTime}"  type="date" pattern="yyyy/MM/dd" /></span><a href="<c:url value = "/item/${myitem.id}"/>">${myitem.defaultTitle}</a>
                                                    </li>
                                                    </c:forEach>
                                            </ul>
                                            <c:if test="${myitems.hasNext}">
                                            <div class="moreInfo">
                                                <ul class="moreInfo">
                                                    <li><a href="<c:url value="/item"><c:param name="username" value="${user.nickname}" /></c:url>">More</a></li>
                                                </ul>
                                            </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div id="homeRecentList_12" class="dparts homeRecentList"><div class="parts">
                                         <div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
                                            <h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Answered by Me</h3>
                                        </div></div>
                                        <div class="block">
                                            <ul class="articleList">
                                                <c:forEach items="${answeritems.result}" var="answeritem">
                                                    <li><span class="date"><fmt:formatDate value="${answeritem.createTime}"  type="date" pattern="yyyy/MM/dd" /></span>
                                                        <a href="<c:url value = "/item/${answeritem.id}"/>">${answeritem.defaultTitle}(${fn:length(answeritem.question.answerSet)})</a>(${answeritem.author.nickname})  </li>
                                                    </c:forEach>
                                            </ul>
                                            <c:if test="${answeritems.hasNext}">
                                            <div class="moreInfo">
                                                <ul class="moreInfo">
                                                    <li><a href="<c:url value="/item"><c:param name="answeruserId" value="${user.id}" /></c:url>">More</a></li>
                                                </ul>
                                            </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
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
