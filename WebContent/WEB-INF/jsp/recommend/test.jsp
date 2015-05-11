<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="currentModule" value="item" />
        <c:param name="javascript">
            <script type="text/javascript" src="<c:url value="/js/jquery/fancyzoom/js/fancyzoom.min.js" />"></script>
            <script type="text/javascript">
                $(function(){
                    $('a.zoom').fancyZoom({
                        directory:'<c:url value="/js/jquery/fancyzoom/images" />'
                    });
                });
            </script>
            <c:if test="${!empty quiz && !empty quiz.item.itemLat && !empty quiz.item.itemLng}">
                <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=true&amp;key=${googleMapApi}" type="text/javascript"></script>
                <script type="text/javascript">
                    var map,marker;
                    function initMap(){
                        var lat = ${quiz.item.itemLat};
                        var lng = ${quiz.item.itemLng};
                        var zoom = ${quiz.item.itemZoom};
                        map = new GMap2($("#map")[0],{draggable:false});
                        var center = new GLatLng(lat, lng);
                        map.setCenter(center, zoom);
                    <%--map.addControl(new GScaleControl());--%>
                    <%--map.addControl(new GLargeMapControl());--%>
                    <%--map.addControl(new GOverviewMapControl());--%>
                    <%--map.enableDoubleClickZoom();--%>
                    <%--map.enableScrollWheelZoom();--%>
                    <%--map.enableContinuousZoom();--%>
                            marker=new GMarker(center,{draggable:false});
                            map.addOverlay(marker);
                        }

                        $(function(){
                            if (GBrowserIsCompatible()) {
                                initMap();
                            }
                        });

                        $(window).unload(function(){
                            GUnload();
                        });
                </script>
            </c:if>
        </c:param>
    </c:import>
    <body id="page_member_profile">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                   <div id="Contents">
                    <div id="ContentsContainer">
                      <div id="LayoutA" class="Layout">
                           <c:if test="${!empty quiz}">
                            <div id="Left">
                                <div id="memberImageBox_30" class="parts memberImageBox">
                                    <p class="photo">
                                        <a id="itemImage" class="zoom" href="#itemImageZoom">
                                            <c:choose>
                                                <c:when test="${empty quiz.item.image}">
                                                    <img width="240px" alt="" src="<c:url value="/images/no_image.gif" />" />
                                                </c:when>
                                                <c:otherwise>
                                                    <img alt="" src="${staticserverUrl}/${projectName}/${quiz.item.image.id}_320x240.png" width="240px" />
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </p>
                                    <div id="itemImageZoom">
                                        <c:choose>
                                            <c:when test="${empty quiz.item.image}">
                                                <img width="240px" alt="" src="<c:url value="/images/no_image.gif" />" />
                                            </c:when>
                                            <c:otherwise>
                                                <img alt="" src="${staticserverUrl}/${projectName}/${quiz.item.image.id}_800x600.png" />
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <p class="text"></p>
                                    <div class="moreInfo">
                                        <ul class="moreInfo">
                                            <li><a class="zoom" href="#itemImageZoom">もっと写真を見る</a></li>
                                        </ul>
                                    </div>
                                </div><!-- parts -->
                                 <div id="mapbox" class="dparts nineTable">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Map</h3></div>
                                        <table>
                                            <tr>
                                                <td>
                                                    <div id="map" style="height: 200px;">
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr><td>場所：${quiz.item.place}</td></tr>
                                        </table>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div>
                                <div id="Center">
                                <div class="dparts homeRecentList">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Quiz</h3></div>
                                          <c:url value="/quiz" var="url" />
                                          <form:form commandName="form" action="${url}" method="post">
                                          	 <form:hidden path="language"/>
                                          	 <%for(int i=1;i<10;i++){%>
                                             <input type="hidden" name="quizid" value="${quiz.id}"></input>
                                                <span style="font-family: arial,meiryo,simsun,sans-serif;font-size: 22px; font-weight:bold; color: gray"><img src="<c:url value='/images/system-help.png' />" alt="●"/>${quiz.content}</span></td>
                                                <ul style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 20px 20px; line-height: 40px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
                                                <c:forEach items="${quiz.choices}" var="choice" varStatus="status">
                                                	<li>
                                                	<c:choose>
                                                        <c:when test= "${!answered}">
                                                            <input id="answeroption${status.index}" type="radio" name="answer"value="${choice.number}" <c:if test= "${status.index == 0}"> checked="checked"</c:if>/>
                                                            	<label for="answeroption${status.index}">${choice.content}</label>
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
                                                              <a href="${itemurl}" target="_blank">${choice.content}</a>
                                                              ${choice.note}
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </li>
                                                </c:forEach>
                                                </ul>
                                                <c:choose>
	                                                <c:when test= "${!answered}">
														<input type="submit" value="Answer"/>
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
                                                <% }%>
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
                                                 <tr>
                                                     <td width="120">
                                                         Learning Language:
                                                     </td>
                                                     <td>
                                                        <form:select path="language" itemLabel="name" itemValue="code" items="${languages}" ></form:select>
                                                     </td>
                                                 </tr>
                                                 <tr>
                                                     <td colspan="2">
                                                         <input type="submit" value="More Quizzes" ></input>
                                                     </td>
                                                 </tr>
                                             </table>
                                         </form:form>
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
