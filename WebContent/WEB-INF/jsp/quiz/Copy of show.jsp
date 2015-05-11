<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
	<script type="text/javascript">
	function fncPass() {
		$("#pass").val("1");
		$("#form").submit();
	}
	function fncMore() {
		var myform  = document.getElementById("form");
		
		myform.method="get";
		myform.submit();  
	}

	function resizeImgX(img,iwidth,iheight)
	{
	   var _img = new Image();
	   _img.src = img.src;
	   if(_img.width > _img.height)
	   {
	      img.width = (_img.width > iwidth) ? iwidth : _img.width;
	      img.height = (_img.height / _img.width) * img.width;
	   }
	   else if(_img.width < _img.height)
	   {
	      img.height = (_img.height > iheight) ? iheight : _img.height;
	      img.width = (_img.width / _img.height) * img.height ;
	   }
	   else
	   {
	      img.height = (_img.height > iheight) ? iheight : _img.height;
	      img.width = (_img.width > iwidth) ? iwidth : _img.width;
	   }
	}
	
</script>

    <c:import url="../include/head.jsp">
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
                            <div id="Left">
                      		 <c:if test="${!empty quiz}">
                      		      <c:if test="${quiz.questionType.id==2}">
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
	                                    </div><!-- itemImageZoom -->
	                                    <p class="text"></p>
	                                    <div class="moreInfo">
	                                        <ul class="moreInfo">
	                                            <li><a class="zoom" href="#itemImageZoom">もっと写真を見る</a></li>
	                                        </ul>
	                                    </div><!-- moreInfo -->
									</div><!-- memberImageBox_30 -->
									</c:if>
	                                <div id="mapbox" class="dparts nineTable">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Map</h3></div>
                                        <table>
                                            <tr>
                                                <td>
                                                    <div id="map" style="width:430px;height:350px;">
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr><td>Place：${quiz.item.place}</td></tr>
                                        </table>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                                </c:if>
                                <div id="mapbox" class="dparts nineTable">
                                    <div class="parts">
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div><!-- Left -->
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
                                                <c:if test="${quiz.questionType.id == 2}">
                                                23222
                                                   <table style="border-style:none;!important">
                                                   <c:forEach items="${quiz.quizChoices}" var="choice" varStatus="status">
                                                   		<c:choose>
	                                                        <c:when test= "${!answered}">
                                                        				<c:if test="${status.index%2==0}">
		                                                        			<tr>
		                                                        		</c:if>
		                                                        			<td width ="5%" style="text-align: center;">   
		                                                        					<input id="answeroption${status.index}" type="radio" name="answer"value="${choice.number}" <c:if test= "${status.index == 0}"> checked="checked"</c:if>/>
																			</td>		                                                        					
		                                                        		    <td style="text-align: center; padding:10px;">  
		                                                        				   <img alt="" src="${staticserverUrl}/${projectName}/${choice.content}_320x240.png" style="max-width:90%;height:auto;"  />
		                                                        		   </td>
		                                                        		<c:if test="${status.index%2==1}">	
		                                                        			</tr>
		                                                        		</c:if>
	                                                        </c:when>
	                                                        <c:otherwise>
	                                                        	  <c:if test="${status.index%2==0}">
		                                                        	 <tr>
		                                                       	  </c:if>
		                                                       	  <td style="padding:10px;">
		                                                              <c:if test="${rightanswer == status.index+1}">
		                                                              	<img src="<c:url value='/images/right_icon.png' />" alt="(Right)" />
		                                                              </c:if>
		                                                              <c:if test="${!result && youranswer == status.index+1}">
	                                                              	    <img src="<c:url value='/images/wrong_icon.png' />" alt="(Wrong)" />
	                                                                  </c:if>
	                                                              <c:url value="/item/${choice.item_id}" var="itemurl" />
	                                                               	 <a href="${itemurl}" target="_blank"><img alt="" src="${staticserverUrl}/${projectName}/${choice.content}_320x240.png"  style="max-width:85%;height:auto;"  /></a> 
	                                                              </td>
	                                                              <c:if test="${status.index%2==1}">	
			                                                        	</tr>
			                                                       </c:if>
                                                        </c:otherwise>
                                                        </c:choose>
                                                       </c:forEach>
                                                        </table>
                                                  </c:if>
                                                <c:if test="${quiz.questionType.id == 1}">
                                                11111
                                                <c:forEach items="${quiz.quizChoices}" var="choice" varStatus="status">
                                                       <ul style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 20px 20px; line-height: 40px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
	                                                	<li>
	                                                	<c:choose>
	                                                          <c:when test= "${!answered}">
		                                                            <input id="answeroption${status.index}" type="radio" name="answer"value="${choice.number}" <c:if test= "${status.index == 0}"> checked="checked"</c:if>/>
		                                                            	<label for="answeroption${status.index}">${choice.content}</label>
		                                                        </c:when>
		                                                        <c:otherwise>
		                                                              <c:if test="${rightanswer == status.index+1}">
		                                                              	<img src="<c:url value='/images/right_icon.png' />" alt="(Right)" />
		                                                              </c:if>
		                                                              <c:if test="${!result && youranswer == status.index+1}">
	                                                              	    <img src="<c:url value='/images/wrong_icon.png' />" alt="(Wrong)" />
	                                                                  </c:if>
	                                                              <c:url value="/item/${choice.item_id}" var="itemurl" />
	                                                              	 <a href="${itemurl}" target="_blank">${choice.content}</a> 
	                                                              ${choice.note}
	                                                        </c:otherwise>
	                                                      </c:choose>
	                                                    </li>
	                                                </ul>
	                                            </c:forEach>
                                                </c:if>	
                                                <c:choose>
	                                                <c:when test= "${!answered}">
	                                                	<br/>
														<input type="submit" class="buttonSubmit" value="Answer"/>
														<input type="button" class="buttonSubmit" value="Skip" onclick="return fncPass();"/>
	                                                </c:when>
	                                                <c:otherwise>
	                                                	<br/>
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
                                          <br/>
                                          <form:form commandName="form"  action="${url}" method="get">
	                                          <c:if test= "${answered}">
                                             <table>
                                                 <tr class="main-table" >
                                                     <td width="120" style="text-align:center;">
                                                     	 <input type="submit" value="More Quizzes" class="buttonSubmit"></input>
                                                         Learning Language:
                                                        <form:select path="language" itemLabel="name" itemValue="code" items="${languages}" ></form:select>
                                                     </td>
                                             </table>
                                             </c:if>
                                         </form:form>
                                        </c:if>
                                   	    <c:if test="${empty quiz}">
                                         <div class="moreInfo">
                                             <ul>
                                                    <li><b>You have finished all the quizzes. Please upload more!</b></li>
                                             </ul>
                                         </div>
                                      </c:if>
                                     </div> <!-- parts -->
                                </div><!-- dparts homeRecentList -->
                            </div><!-- Center -->
                      	</div><!-- LayoutA -->
                            <div style="text-align: center;">
                               <span style="font-family: arial,meiryo,simsun,sans-serif;font-size: 22px; font-weight:bold; ">
                               			<br/>
                                      	Today's Score:<br/>&nbsp;<font color="green" size="30px">${quizinfos.scores}</font><br/><br/>
	                                  	Your All Score:<br/>&nbsp;<font color="green" size="30px">${quizinfos.allscores}</font>
	                                  </span>
	                        </div>          
                    </div><!-- ContentsContainer -->
				 </div><!-- Contents -->
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
