<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
<c:if test="${areas!=null}">
 <style type="text/css">
      #map {
        width: 800px;
        height: 500px;
      }
  </style>

    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript">
      // Global variables
      var map;
      
      /**
       * Called on the initial page load.
       */
      
      <c:forEach items="${areas}" var="area" varStatus="vs">
      var marker1${vs.index};
      var marker2${vs.index};
      var rectangle${vs.index};
      </c:forEach>
		
      function init() {
          map = new google.maps.Map(document.getElementById('map'), {
            'zoom': 17,
            'center': new google.maps.LatLng("${centerpoint.lat}", "${centerpoint.lng}"),
            'mapTypeId': google.maps.MapTypeId.ROADMAP
          });

        // Plot two markers to represent the Rectangle's bounds.
 		<c:forEach items="${areas}" var="area" varStatus="vs">
        marker1${vs.index} = new google.maps.Marker({
          map: map,
		   position: new google.maps.LatLng("${area.minlat}",	"${area.minlng}"),
          draggable: true,
          title: 'Drag me!'
        });
        marker2${vs.index} = new google.maps.Marker({
          map: map,
         position: new google.maps.LatLng("${area.maxlat}",	"${area.maxlng}"),
          draggable: true,
          title: 'Drag me!'
        });
        // Create a new Rectangle overlay and place it on the map.  Size
        // will be determined by the LatLngBounds based on the two Marker
        // positions.
        rectangle${vs.index} = new google.maps.Rectangle({
          map: map
        });
        </c:forEach>
        
    	<c:forEach items="${areas}" var="area" varStatus="vs">
        // Allow user to drag each marker to resize the size of the Rectangle.
        google.maps.event.addListener(marker1${vs.index}, 'drag', redraw);
        google.maps.event.addListener(marker2${vs.index}, 'drag', redraw);
        </c:forEach>

        redraw();
      }

 	
 	   /**
      * Updates the Rectangle's bounds to resize its dimensions.
      */
     function redraw() {
 		  var latLngBounds;
 		  <c:forEach items="${areas}" var="area" varStatus="vs">
	       latLngBounds = new google.maps.LatLngBounds(
	         marker1${vs.index}.getPosition(),
	         marker2${vs.index}.getPosition()
	       );
      	  rectangle${vs.index}.setBounds(latLngBounds);
      </c:forEach>
     }
     // Register an event listener to fire when the page finishes loading.
     google.maps.event.addDomListener(window, 'load', init);
    </script>
    </c:if>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="オブジェクト" />
        <c:param name="css">
            <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/js/jquery/stars/jquery.ui.stars.min.css"/>" />
            <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='/js/mediaelement/mediaelementplayer.min.css' />" />
        </c:param>
    </c:import>
    <script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("homework").className = "active";

	});
</script>
    <body id="page_member_profile">
        <div id="Body">
            <div id="Container"><br><br><br>
                <c:import url="../include/header.jsp" />
                 <div id="Contents">
                 <div id="LayoutD" class="Layout">
                  <div id="Left"></div>
                  <div id="Center">
                	<br/>
					<div id="map" style="margin-left:200px;"></div>
					<div id="questionaire" style="margin-left:300px;">
					<br>
					<b>あなたの学習習慣</b><br>
					<table style="width:700px;">
						<c:if test="${areas!=null}">
						 <c:forEach items="${areas}" var="area" varStatus="vs">
							<tr>
								<td width="42%">よく勉強している場所</td><td><b>(${area.minlat},${area.minlng}),(${area.maxlat},${area.maxlng})</b></td> 
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${areas==null}">
							<tr>
								<td width="42%">よく勉強している場所</td><td><b>無（履歴データが少ないので、システムをもっと使ってください！）</b></td> 
							</tr>
						</c:if>
						<c:forEach items="${times}" var="stime" varStatus="vt">
							<tr>
								<td>よく勉強している時間帯</td>
								<td>
								<b>
								${stime.starttime}～${stime.endtime}
								</b>
								</td>
							</tr>
						</c:forEach>
					</table>
					<br/>
					<b>評価</b><br/>
					  	<c:url value="/myhabit" var="evaluateUrl" />
						<form:form  commandName="form" action="${evaluateUrl}" method="post">
							<table style="width:700px;">
							<c:if test="${areas!=null}">
								<tr>
									<td width="42%">自分がよく勉強している場所は</td>
									<td>
										<form:select path="geoscore">
											<form:option value="5">非常に正しい</form:option>
											<form:option value="4">正しい</form:option>
											<form:option value="3">どちらも言えない</form:option>
											<form:option value="2">正しくない</form:option>
											<form:option value="1">非常に正しくない</form:option>
										</form:select>
									</td>
								</tr>
								<tr>
									<td>よく勉強している場所で推薦することについては</td>
									<td>
										<form:select path="georecommend">
											<form:option value="5">とても良い</form:option>
											<form:option value="4">良い</form:option>
											<form:option value="3">どちらも言えない</form:option>
											<form:option value="2">良くない</form:option>
											<form:option value="1">とても良くない</form:option>
										</form:select>
									</td>
								</tr>
								</c:if>
								<tr>
									<td>自分がよく勉強している時間帯は</td>
									<td>
										<form:select path="timescore">
											<form:option value="5">非常に正しい</form:option>
											<form:option value="4">正しい</form:option>
											<form:option value="3">どちらも言えない</form:option>
											<form:option value="2">正しくない</form:option>
											<form:option value="1">非常に正しくない</form:option>
										</form:select>
									</td>
								</tr>
								<tr>
									<td>よく勉強している時間帯に推薦することについては</td>
									<td>
										<form:select path="timerecommend">
											<form:option value="5">とても良い</form:option>
											<form:option value="4">良い</form:option>
											<form:option value="3">どちらも言えない</form:option>
											<form:option value="2">良くない</form:option>
											<form:option value="1">とても良くない</form:option>
										</form:select>
									</td>
								</tr>
								<tr>
									<td colspan="2" align="center">
										<input class="btn btn-primary" type="submit" value="Submit"/>
									</td>
								</tr>
							</table>
						</form:form>
					</div>
					<br/>
					</div>
            </div><!-- Body -->
                <c:import url="../include/footer.jsp" />
            </div>
            </div>
        </div>
    </body>
</html>
