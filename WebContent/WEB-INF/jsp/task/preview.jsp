<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="userId"><shiro:principal property="id" /></c:set>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="オブジェクト" />
        <c:param name="javascript">
            <script src="<c:url value="/js/jQuery.jPlayer.2.0.0/jquery.jplayer.min.js" />"></script>
            <script src="<c:url value="/js/jquery/jquery.linkify-1.0-min.js"/>"></script>
            <script src="<c:url value='/js/mediaelement/mediaelement-and-player.min.js' />"></script>
            <script>
                $(function(){
                	$("video, audio").mediaelementplayer();
                    $(".description").linkify();
                });
            </script>
            <script src="<c:url value="/js/jquery/stars/jquery.ui.stars.min.js" />"></script>
            <script src="${baseURL}/js/jquery/jquery.nyroModal.custom.min.js"></script>
			<!--[if IE 6]>
				<script type="text/javascript" src="${baseURL}/js/jquery/jquery.nyroModal-ie6.min.js"></script>
			<![endif]-->
            <script>
	            $(function() {
	            	  function preloadImg(image) {
	            	    var img = new Image();
	            	    img.src = image;
	            	  }
	
	            	  preloadImg('${baseURL}/images/ajaxLoader.gif');
	            	  preloadImg('${baseURL}/images/prev.gif');
	            	  preloadImg('${baseURL}/images/next.gif');
	            	  preloadImg('${baseURL}/images/close.gif');
	            	  $('.nyroModal').nyroModal();
            	});
            </script>
            <script src="http://maps.google.com/maps/api/js?sensor=true"></script>
            <script type="text/javascript">
                $(function(){
                	var map = new google.maps.Map(document.getElementById("taskMap"), {
                        disableDefaultUI: true,
                        scaleControl: true,
                        navigationControl: true,
                        mapTypeId: google.maps.MapTypeId.ROADMAP,
                    });
                    var bounds = new google.maps.LatLngBounds();
                    var marker;
                    marker = new google.maps.Marker({position:new google.maps.LatLng(${task.lat}, ${task.lng}), map:map});
                    bounds.extend(marker.getPosition());
	                <c:forEach items="${scripts}" var="script" varStatus="sta" >
	                    <c:if test="${(!empty script.lat) and (!empty script.lng)}">
                            marker = new google.maps.Marker({position:new google.maps.LatLng(${script.lat}, ${script.lng}), map:map});
                            bounds.extend(marker.getPosition());
	                    </c:if>
	                </c:forEach>
                   map.fitBounds(bounds);
                   
                });
            </script>
            
           <script type="text/javascript">
	           function onQuery(){
	        	   $.ajax({
	        	         type: "POST",
	        	         url: "http://localhost:8080/learninglog1/item/search.json",
	        	         data: "searchCond.title='cat'",
	        	         dataType: "json",
	        	         success: function(data){
	        	        	 $.each(data.items,function(index, value){
	        	        		 var result = "<input type='checkbox' name='itemId' value='"+value.itemId+"'> ";
	        	        		 var temp = "";
	        	        		 $.each(value.titles, function(i, title){
	        	        			 if(i == '16bb94322d930a55012d9785ad540033'){
	        	        			 	temp = title;
	        	        			 	return false;
	        	        			 }else
		        	        			 temp = title;
	        	        			 
	        	        		 });
        	        			 result += temp;
	        	        		 result +="</input>";
	        	        		 $("#search_result_area").append(result+"<br>");
	        	        	 })
	        	        	 $("#search_result_area").append("<input type='button' value='Add'/>");
	        	          }
	        	      });
	           }
           </script>
            
        </c:param>
        <c:param name="css">
            <link rel="stylesheet" type="text/css" media="screen" href="${baseURL}/js/jquery/stars/jquery.ui.stars.min.css" />
            <link rel="stylesheet" type="text/css" media="screen" href="${baseURL}/js/mediaelement/mediaelementplayer.min.css" />
            <link rel="stylesheet" type="text/css" media="screen" href="${baseURL}/css/nyroModal.css" />
        </c:param>
    </c:import>
    
    <body id="page_member_profile">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="localNav">
                        </div><!-- localNav -->
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                            </div><!-- Top -->
                            <div id="Center">
                                <div id="profile" >
                                    <div class="parts">
                                        <div class="partsHeading">
                                            <h3>Information</h3>
                                        </div>
                                        <table>
                                         	<tr>
                                                <th>Title</th>
                                                <td>
                                                	${task.title}
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Language</th>
                                                <td>
                                                	${task.language.name}
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Author</th>
                                                <td>
                                                	${task.author.nickname }
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Updated</th>
                                                <td>
                                                    <fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm" value="${task.updatetime}" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Scripts</th>
                                                <td>
                                                </td>
                                            </tr>
                                            <tr>
                                            	<td colspan="2">
                                            	<div id="script_area">
                                            		<ul>
	                                            		<c:forEach items="${scripts}" var="script" varStatus="s">
	                                            			<li>
	                                            				Step${s.index+1}: ${script.script} 
	                                            				  <c:if test="${script.image!=null}">
	                                            					<p class="photo">
								                                        <a id="itemImage" class="nyroModal" href="${staticserverUrl}/${projectName}/${script.image.id}_800x600.png">
								                                           <img alt="" src="${staticserverUrl}/${projectName}/${script.image.id}_320x240.png" width="240px" />
								                                        </a>
								                                    </p>
	                                            				</c:if>
	                                            			</li>
	                                            		</c:forEach>
                                            		</ul>
                                            	</div><!-- script area -->
                                            	</td>
                                            </tr>
                                            <c:if test="${!empty task.lat && !empty task.lng && !empty task.zoom}">
                                            <tr id="script_map">
                                                    <td colspan="2">
	                                                   <div id="taskMap" style="height:350px;">
	                                                   </div>
	                                                   <div id="directionsPanel" style="position:absolute; left: 410px; width:240px; height:400px; overflow: auto"></div>
                                                    </td>
                                            </tr>
                                            </c:if>  
                                        </table>
                                    </div>
                                </div><!-- dparts -->
                                <!--  
                                 <div class="new-share-info">
                                 	<span>Read (<strong>${readCount}</strong>)</span>
                                 </div>
                                 -->                      
                            </div><!-- Layout -->
                            <div class="block">
                            </div><!-- block -->
                            </div><!-- layoutC -->
                            <div id="sideBanner">
                                <div class="partsHeading">
                                    <h3>Related Learning Logs</h3>
                                </div>
                                <div id="search_area">
										<input name="querylog" />  <input type="button" value="Search" onClick="onQuery()"/>                              	
                                </div>
                                <div id="search_result_area">
                                </div>
                                <div id="relatedItem">
                                	<ul>
                                		<li>
                                			dddd
                                		</li>
                                		<li>
                                			cccc
                                		</li>
                                	</ul>
                                </div>
                            </div><!-- right -->
                        </div><!-- ContentsContainer -->
                    </div><!-- Contents -->
                    <c:import url="../include/footer.jsp" />
                </div><!-- Container -->
            </div><!-- Body -->
    </body>
</html>
