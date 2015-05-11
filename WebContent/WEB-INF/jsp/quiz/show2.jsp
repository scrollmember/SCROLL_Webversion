<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>


    <c:import url="../include/head.jsp">
        <c:param name="currentModule" value="item" />
        <c:param name="javascript">
	<script type="text/javascript">
	function fncPass() {
		$("#pass").val("1");
		$("#form").submit();
	}
</script>
            <script type="text/javascript" src="<c:url value="/js/jquery/fancyzoom/js/fancyzoom.min.js" />"></script>
            <script type="text/javascript">
                $(function(){
                    $('a.zoom').fancyZoom({
                        directory:'<c:url value="/js/jquery/fancyzoom/images" />'
                    });
                });
            </script>
        </c:param>
    </c:import>
    <script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("quiz").className = "active";

	});
</script>
    <body id="page_member_profile">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                   <div id="Contents">
                    <div id="ContentsContainer">
                    <div id="LayoutA" class="Layout">
                     <div id="Left">
                          <c:if test="${!empty quiz.item.itemLat && !empty quiz.item.itemLng && !empty quiz.item.itemZoom}">
                          <div id="mapbox" class="dparts nineTable">
                             <div class="parts">
                                 <div class="partsHeading"><h3>Map</h3></div>
                                 <table>
                                     <tr>
                                         <td>
                                             <div id="map" style="width:430px;height:350px;">
                                                  <img src="http://maps.google.com/maps/api/staticmap?size=260x200&sensor=false&center=${quiz.item.itemLat},${quiz.item.itemLng}&zoom=${quiz.item.itemZoom}&mobile=true&markers=${quiz.item.itemLat},${quiz.item.itemLng}" />
                                             </div>
                                         </td>
                                     </tr>
                                     <c:if test="${!empty quiz.item.place}"><tr><td>Place：${quiz.item.place}</td></tr></c:if>
                                 </table>
                             </div><!-- partsHeading -->
                         </div><!-- dparts -->
                         </c:if>
                     </div><!-- mapbox -->
                           		</div><!-- left --> 
                                <div id="Center">
                                <div class="dparts homeRecentList">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Quiz</h3></div>
                                          <c:url value="/quiz" var="url" />
                                          <c:if test="${!empty quiz}">
                                          <form:form commandName="form" action="${url}" method="post" id="form">
                                          	 <form:hidden path="language"/>
                                          	 <form:hidden path="pass" id="pass"/>
                                          	 <input name="alarmtype" type="hidden" value="8"/>
                                             <input type="hidden" name="quizid" value="${quiz.id}"></input>
                                                <span style="font-family: arial,meiryo,simsun,sans-serif;font-size: 22px; font-weight:bold; color: gray"><img src="<c:url value='/images/system-help.png' />" alt="●"/>${quiz.content}</span></td>
                                                <ul style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 20px 20px; line-height: 40px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
                                                <c:forEach items="${quiz.choices}" var="choice" varStatus="status">
                                                	<li>
                                                	<c:choose>
                                                        <c:when test= "${!answered}">
                                                            <input id="answeroption${status.index}" type="radio" name="answer"value="${choice.number}" <c:if test= "${status.index == 0}"> checked="checked"</c:if>/>
                                                            <c:if test="${quiz.quiztypeid == 2}">
                                                            	 <img alt="" src="${staticserverUrl}/${projectName}/${choice.content}_160x120.png" />
                                                            </c:if>
                                                            <c:if test="${quiz.quiztypeid != 2}">
                                                            	<label for="answeroption${status.index}">${choice.content}</label>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                        	<c:choose>
	                                                              <c:when test="${rightanswer == status.index+1}">
	                                                              	<img src="<c:url value='/images/right_icon.png' />" alt="(Right)" />
	                                                              </c:when>
	                                                              <c:when test="${!result && youranswer == status.index+1}">
                                                              	    <img src="<c:url value='/images/wrong_icon.png' />" alt="(Wrong)" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                  	&nbsp;&nbsp;
                                                                  </c:otherwise>
                                                        	</c:choose>
                                                              <c:url value="/item/${choice.item_id}" var="itemurl" />
                                                               <c:if test="${quiz.quiztypeid == 2}">
                                                               	 <a href="${itemurl}" target="_blank"><img alt="" src="${staticserverUrl}/${projectName}/${choice.content}_160x120.png" /></a> 
                                                               </c:if>
                                                               <c:if test="${quiz.quiztypeid != 2}">
                                                              	 <a href="${itemurl}" target="_blank">${choice.content}</a> 
                                                               </c:if>	
                                                              ${choice.note}
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </li>
                                                </c:forEach>
                                                </ul>
                                                <c:choose>
	                                                <c:when test= "${!answered}">
														<input type="submit" class="buttonSubmit" value="Answer"/>
														<input type="button" class="buttonSubmit" value="Skip" onclick="return fncPass();"/>
	                                                </c:when>
	                                                <c:otherwise>
	                                                	<div style="display:block; border: 8px ridge orange; width: 250px; height: 80px;margin-left: 110px; font-family: arial; font-size: 22px; font-weight: bold">
	                                                		<span style="word-break: normal;color:<c:if test='${result}'>green</c:if><c:if test='${!result}'>red</c:if>">
	                                                		${comment}
	                                                		</span>
	                                                	</div>
	                                                	<div style="margin-top: -30px;">
	                                                		<img src="<c:url value='/images/${faceicon}.png' />" alt="" />
	                                                	</div>
	                                                </c:otherwise>
                                                </c:choose>
                                          </form:form>
                                    </c:if>
                                    <c:if test="${empty quiz}">
                                         <div class="moreInfo">
                                             <ul class="moreInfo">
                                                    <li>You have finished all the quizzes. Please try the other kind!</li>
                                             </ul>
                                         </div>
                                      </c:if>
                                             <br/>
                                           <div class="partsHeading"><h3>Setting</h3></div>
                                             <form:form commandName="form"  action="${url}" method="get">
                                             <table>
                                                 <tr class="main-table">
                                                     <td width="120">
                                                         Learning Language:
                                                     </td>
                                                     <td>
                                                        <form:select path="language" itemLabel="name" itemValue="code" items="${languages}" ></form:select>
                                                     </td>
                                                 </tr>
                                                 <tr class="main-table">
                                                     <td colspan="2" >
                                                         <input type="submit" value="More Quizzes" class="buttonSubmit"></input>
                                                     </td>
                                                 </tr>
                                             </table>
                                         </form:form>
                                    </div>
                                    <br/>
                                    <div class="partsHeading"><h3>Correct Rate</h3></div>
                                    	 <table>
	                                    	 <tr>
	                                    	 	<td>
	                                    	 		The correct rate for today:
	                                         		<img alt="" src="http://chart.apis.google.com/chart?cht=bvs&chd=t:${quizinfos.correcttimes},${quizinfos.wrongtimes},${quizinfos.passtimes}&chds=0,${quizinfos.daytimes}&chs=500x200&chdl=Correct(${quizinfos.correcttimes})|Wrong(${quizinfos.wrongtimes})|Skip(${quizinfos.passtimes})&chco=00FFFF|0000FF|FF00FF&chbh=40">
	                                         		<br/>
	                                         	</td>
	                                         </tr>
	                                         <tr>
	                                         	<td>
	                                         		The correct rate of all quizzes:
	                                         		<img alt="" src="http://chart.apis.google.com/chart?cht=bvs&chd=t:${quizinfos.allcorrecttimes},${quizinfos.allwrongtimes},${quizinfos.allpasstimes}&chds=0,${quizinfos.alltimes}&chs=500x200&chdl=Correct(${quizinfos.allcorrecttimes})|Wrong(${quizinfos.allwrongtimes})|Skip(${quizinfos.allpasstimes})&chco=00FFFF|0000FF|FF00FF&chbh=40">   
	                                         		<br/>
	                                         	</td>
	                                         </tr>
                                         </table> 
                                       
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Layout -->
                     </div>
                    </div>
                   </div>
            </div><!-- Container -->
            </div><!-- Body -->
    </body>
</html>
