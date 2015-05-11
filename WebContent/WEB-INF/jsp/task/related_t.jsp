<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Related Objects" />
        <c:param name="javascript">
            <script type="text/javascript" src="<c:url value="/js/jquery/fancyzoom/js/fancyzoom.min.js" />"></script>
            <script type="text/javascript">
                $(function(){
                    $('a.zoom').fancyZoom({
                        directory:'<c:url value="/js/jquery/fancyzoom/images" />'
                    });
                });
            </script>
            <c:choose>
            <c:when test="${!empty item.itemLat && !empty item.itemLng}">
                <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=true&amp;key=${googleMapApi}" type="text/javascript"></script>
                <script type="text/javascript">
                    var map,marker;
                    function initMap(){
                        var lat = ${item.itemLat};
                        var lng = ${item.itemLng};
                        var zoom = ${item.itemZoom};
                        map = new GMap2($("#map")[0],{draggable:false});
                        var center = new GLatLng(lat, lng);
                        map.setCenter(center, zoom);
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
            </c:when>
            <c:otherwise>
				<script type="text/javascript">
	            	$(function(){
	            		$("#mapbox").hide();
	            	});
            	</script>
            </c:otherwise>
            </c:choose>
        </c:param>
        <c:param name="css">
            <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/js/jquery/stars/ui.stars.min.css"/>" />
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
                        <div id="LayoutA" class="Layout">
                            <div id="Top">
                            </div><!-- Top -->
                            <div id="Left">
                            	<div id="opbox" class="dparts nineTable">
                                    <div class="parts">
		                            	<c:choose>
		                                <c:when test="${item.author.id eq userId && fromcreated && item.relogItem==null}">
		                                   <button onclick="location.href='<c:url value="/item/${item.id}/edit" />'">Return to edit</button>
		                                   <button onclick="location.href='<c:url value="/item/${item.id}" />'">Continue</button>
		                                </c:when>
		                                <c:otherwise>
		                                	<button style="width:100%" onclick="location.href='<c:url value="/item/${item.id}" />'">Return</button>
		                                </c:otherwise>
		                                </c:choose>
	                                </div>
                            	</div>
                                <div id="memberImageBox_30" class="parts memberImageBox">
                                <c:choose>
                                	<c:when test="${fileType eq 'image'}">
	                                    <p class="photo">
	                                        <a id="itemImage" class="zoom" href="#itemImageZoom">
	                                           <img alt="" src="${staticserverUrl}/${projectName}/${item.image.id}_320x240.png" width="240px" />
	                                        </a>
	                                    </p>
	                                    <div id="itemImageZoom">
	                                        <img alt="" src="${staticserverUrl}/${projectName}/${item.image.id}_800x600.png" />
	                                    </div>
	                                    <p class="text"></p>
	                                    <div class="moreInfo">
	                                        <ul class="moreInfo">
	                                            <li><a class="zoom" href="#itemImageZoom">Zoom in</a></li>
	                                        </ul>
	                                    </div>
                                    </c:when>
                                    <c:when test="${fileType eq 'video'}">
                                    	<p class="photo">
	                                    	<video id="itemvideo" class="video"
	                                    		width="240px" controls preload
	                                    		poster="${staticserverUrl}/${projectName}/${item.image.id}_320x240.png"
	                                    		style="background-color:black"/>
	                                    		<source src="${staticserverUrl}/${projectName}/${item.image.id}_320x240.mp4"></source>
	                                    	</video>
                                    	</p>
                                    </c:when>
                                    <c:when test="${fileType eq 'audio'}">
                                    		<audio controls style="width:240px">
                                    			<source src="${staticserverUrl}/${projectName}/${item.image.id}.mp3" />
                                    			<source src="${staticserverUrl}/${projectName}/${item.image.id}.ogg" />
                                    		</audio>
                                    </c:when>
                                    <c:when test="${fileType eq 'pdf'}">
                                    	<p class="photo">
                                    		<a id="itemImage" href="${staticserverUrl}/${projectName}/${item.image.id}.pdf">
	                                           <img alt="" src="${staticserverUrl}/${projectName}/${item.image.id}_320x240.png" width="240px" />
	                                        </a>
                                    	</p>
                                    </c:when>
                                    <c:otherwise>
                                    	<p class="photo">
                                    		<img width="240px" alt="" src="<c:url value="/images/no_image.gif" />" />
                                    	</p>
                                    </c:otherwise>
                                </c:choose>
                                </div><!-- parts -->
                                <div id="infobox" class="dparts nineTable">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Information</h3></div>
                                        <table>
                                            <tr>
                                                <th style="width: 80px">Title</th>
                                                <td>
                                                	<c:choose>
                                                    <c:when test="${empty item.titles}">
                                                        NO NAME
                                                    </c:when>
                                                    <c:otherwise>
                                                    <table>
                                                    	<c:forEach items="${item.titles}" var="title">
                                                            <tr>
                                                                <td style="width:70px;">${title.language.name}</td>
                                                                <td>${title.content}</td>
                                                            </tr>
                                                    	</c:forEach>
                                                    </table>
                                                    </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th><label for="tag1">ID</label></th>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td style="width:70px;">Barcode</td>
                                                            <td>${item.barcode}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>QR Code</td>
                                                            <td>${item.qrcode}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>RFID</td>
                                                            <td>${item.rfid}</td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Author</th>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${empty item.relogItem}">
                                                            <a href="<c:url value="/item"><c:param name="username" value="${item.author.nickname}" /></c:url>"><c:out value="${item.author.nickname}" /></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="<c:url value="/item/${item.relogItem.id}" />">ReLog</a> from <a href="<c:url value="/item"><c:param name="username" value="${item.relogItem.author.nickname}" /></c:url>"><c:out value="${item.relogItem.author.nickname}" /></a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Created at</th>
                                                <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${item.createTime}" /></td>
                                            </tr>
                                            <tr>
                                                <th>Updated at</th>
                                                <td>
                                                    <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${item.updateTime}" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Date of<br/> photo</th>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <th>Comment</th>
                                                <% pageContext.setAttribute("newLineChar", "\n");%>
                                                <td>
                                                    ${fn:replace(item.note,newLineChar,'<br />')}
                                                </td>
                                            </tr>
                                            <tr>
                                            	<th>Tags</th>
                                            	<td>
                                            		<c:forEach items="${item.itemTags}" var="tag">
                                            			<a href="<c:url value="/item"><c:param name="tag" value="${tag.tag}" /></c:url>">${tag.tag}</a> &nbsp;
                                                    </c:forEach>
                                            	</td>
                                            </tr>
                                        </table>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                                <div id="mapbox" class="dparts TagsParts">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Map</h3></div>
                                        <table>
                                            <tr>
                                                <td>
                                                    <div id="map" style="height: 200px;">
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr><td>Place：${item.place}</td></tr>
                                        </table>
                                    </div>
                                </div>
                            </div><!-- Left -->
                            <c:url value="/item/${item.id}/delete" var="itemresourcedelete" />
                            <form:form id="itemDeleteForm" method="post" action="${itemresourcedelete}" />
                            <c:url value="/item/${item.id}/relog" var="relogUrl" />
                            <form:form id="itemRelogForm" method="post" action="${relogUrl}" />
                            <c:url value="/item/${item.id}/teacherconfirm" var="teacherConfirmUrl" />
                            <form:form id="teacherConfirmForm" action="${teacherConfirmUrl}" method="post"/>
                            <c:url value="/item/${item.id}/teacherreject" var="teacherRejectUrl" />
                            <form:form id="teacherRejectForm" action="${teacherRejectUrl}" method="post"/>
                            <c:url value="/item/${item.id}/teacherdelcfmstatus" var="teacherDelCfmUrl" />
                            <form:form id="teacherDelCfmStatusForm" action="${teacherDelCfmUrl}" method="post"/>
                            <div id="Center">
                                <div id="profile" class="dparts searchResultList">
                                
                                	
									<c:if test="${!empty relatedWordList}">
										Recommended：
										<c:forEach items="${relatedWordList}" var="wordnet" varStatus="status">
											<a href="<c:url value="/wordnet/synset/${wordnet.synset}" />">${wordnet.name}</a>
											<c:if test="${!status.last}">
											 / 
											</c:if>
										</c:forEach>
									</c:if>
                                
                                    
									<c:if test="${!empty similarItemList.Item}">
										<c:set var="itemList" value="${similarItemList.Item}" scope="request" />
										<c:set var="infoList" value="${similarItemList.Info}" scope="request" />
										<c:import url="./itemlist.jsp">
											<c:param name="ItemList" value="itemList" />
											<c:param name="InfoList" value="infoList" /> 
											<c:param name="title" value="Related Objects" />
											<c:param name="more" value="word" />
										</c:import>
                                    </c:if>
									<c:if test="${!empty relatedItemList.result}">
										<c:set var="itemList" value="${relatedItemList.result}" scope="request" />
										<c:import url="./itemlist.jsp">
											<c:param name="ItemList" value="itemList" /> 
											<c:param name="title" value="Related Objects" />
											<c:param name="more" value="word" />
										</c:import>
                                    </c:if>
                                    
									<c:if test="${!empty similarImageList}">
										<c:set var="itemList" value="${similarImageList.Item}" scope="request" />
										<c:set var="infoList" value="${similarImageList.Info}" scope="request" />
										<c:import url="./itemlist.jsp">
											<c:param name="ItemList" value="itemList" /> 
											<c:param name="InfoList" value="infoList" /> 
											<c:param name="title" value="Similar Images" />
											<c:param name="more" value="image" />
										</c:import>
                                    </c:if>
                                    
									<c:if test="${!empty nearestItems.result}">
										<c:set var="itemList" value="${nearestItems.result}" scope="request" />
										<c:import url="./itemlist.jsp">
											<c:param name="ItemList" value="itemList" /> 
											<c:param name="title" value="Nearest Objects" />
											<c:param name="more" value="near" />
										</c:import>
                                    </c:if>
                                    
									<c:if test="${!empty similarTimeList}">
										<c:set var="itemList" value="${similarTimeList.Item}" scope="request" />
										<c:set var="infoList" value="${similarTimeList.Info}" scope="request" />
										<c:import url="./itemlist.jsp">
											<c:param name="ItemList" value="itemList" /> 
											<c:param name="InfoList" value="infoList" /> 
											<c:param name="title" value="Similar TimeZone" />
											<c:param name="more" value="time" />
										</c:import>
                                    </c:if>
                                    
                                </div><!-- dparts -->
                            </div><!-- Layout -->
                            <div class="block">
                            </div>
                            <div id="sideBanner">
                            </div><!-- sideBanner -->
                        </div><!-- ContentsContainer -->
                    </div><!-- Contents -->
                    <c:import url="../include/footer.jsp" />
                </div><!-- Container -->
            </div><!-- Body -->
        </div>
    </body>
</html>