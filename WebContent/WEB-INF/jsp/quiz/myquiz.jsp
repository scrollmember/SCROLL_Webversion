<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<c:import url="../include/head.jsp">
	<c:param name="javascript">

		<script type="text/javascript"
			src="<c:url value="/js/jquery/fancyzoom/js/fancyzoom.min.js" />"></script>
		<script type="text/javascript">
			$(function() {
				$('a.zoom').fancyZoom({
					directory : '<c:url value="/js/jquery/fancyzoom/images" />'
				});
			});
		</script>
		<script type="text/javascript">
			$(document).ready(function() {

				document.getElementById("quiz").className = "active";
				document.getElementById("correct").style.display = "none";
				document.getElementById("miss").style.display = "none";
				document.getElementById("correct2").style.display = "none";
				document.getElementById("miss2").style.display = "none";
				document.getElementById("Center").style.display = "block";
			});
		</script>

		<script src="http://maps.google.com/maps/api/js?sensor=true"></script>
		<script
			src="<c:url value="/js/jQuery.jPlayer.2.0.0/jquery.jplayer.min.js" />"></script>
		<script src="<c:url value="/js/jquery/jquery.linkify-1.0-min.js"/>"></script>
		<script
			src="<c:url value='/js/mediaelement/mediaelement-and-player.min.js' />"></script>
		<script type="text/javascript"
			src="http://connect.facebook.net/en_US/all.js"></script>
		<script type="text/javascript">
        $(function(){
        	var map = new google.maps.Map(document.getElementById("searchMap"), {
                disableDefaultUI: true,
                scaleControl: true,
                navigationControl: true,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
            });
            
            var bounds = new google.maps.LatLngBounds();
            var marker;
            <c:forEach items="${quizzes}" var="item" varStatus="sta">
                <c:if test="${(!empty item.item_lat) and (!empty item.item_lng)}">
                    marker = new google.maps.Marker({position:new google.maps.LatLng(${item.item_lat}, ${item.item_lng}), map:map});
                    bounds.extend(marker.getPosition());
                </c:if>
            </c:forEach>
           map.fitBounds(bounds);
           google.maps.event.addListener(map, "bounds_changed", function(){
               $('#x1').val(map.getBounds().getNorthEast().lat());
               $('#y1').val(map.getBounds().getNorthEast().lng());
               $('#x2').val(map.getBounds().getSouthWest().lat());
               $('#y2').val(map.getBounds().getSouthWest().lng());
               //$('#mapenabled').attr("checked", "checked");
           });
        });
            </script>

		<script>
function speak(title, lang){
	$("#ttsTitleArea").html("<div id=\"ttsTitle\"></div>");
	$('#ttsTitle').jPlayer({
		ready:function(){
        	$('#ttsTitle').jPlayer("setMedia", {
				mp3: "<c:url value="/api/translate/tts" />?ie=UTF-8&lang="+lang+"&text="+encodeURIComponent(title)
			}).jPlayer("play");
		},
		ended: function(){
			$("#ttsTitle").jPlayer("destroy");
			$("#ttsTitleArea").html("");
		},
		swfPath:"<c:url value="/js/jQuery.jPlayer.2.0.0" />",
		supplied: "mp3"
	});
}
</script>
	</c:param>
</c:import>
<script type="text/javascript">
	function ans() {
		val1 = $("input[name='answer']:checked").val();
		if (val1 == "1") {

			document.getElementById("q1").style.display = "none";
			document.getElementById("correct").style.display = "block";
			document.getElementById("Center").style.display = "block";
		} else {
			document.getElementById("q1").style.display = "none";
			document.getElementById("miss").style.display = "block";
			document.getElementById("Center").style.display = "block";
		}
	}
	function ans2() {
		val1 = $("input[name='answer']:checked").val();
		if (val1 == "1") {
			document.getElementById("q1").style.display = "none";
			document.getElementById("correct2").style.display = "block";
			document.getElementById("Center").style.display = "block";
		} else {
			document.getElementById("q1").style.display = "none";
			document.getElementById("miss2").style.display = "block";
			document.getElementById("Center").style.display = "block";
		}
	}
	
	function speak(title, lang){
		$("#ttsTitleArea").html("<div id=\"ttsTitle\"></div>");
		$('#ttsTitle').jPlayer({
			ready:function(){
	        	$('#ttsTitle').jPlayer("setMedia", {
					mp3: "<c:url value="/api/translate/tts" />?ie=UTF-8&lang="+lang+"&text="+encodeURIComponent(title)
				}).jPlayer("play");
			},
			ended: function(){
				$("#ttsTitle").jPlayer("destroy");
				$("#ttsTitleArea").html("");
			},
			swfPath:"<c:url value="/js/jQuery.jPlayer.2.0.0" />",
			supplied: "mp3"
		});
	}
</script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Quiz</title>
</head>
<body>
	<div id="Body">

		<c:import url="../include/header.jsp" />

		<div id="MyQuizlayout">
			<div id="Left">
				<div id="q1">
					<c:if test="${mqtype==1}">
						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Question</h3>

							</div>
						</div>
						<div
							style="font-family: arial, meiryo, simsun, sans-serif; font-size: 22px; font-weight: bold; color: gray; margin-bottom: 10px;">
							<img src="<c:url value='/images/system-help.png' />" alt="●" />&nbsp;Do
							you remember ?
						</div>
						<div id="ttsTitleArea" style="height: 0; width: 0"></div>
						<div id="ttsTitle" style="height: 0; width: 0"></div>
						<c:forEach items="${quizzes}" var="choice" varStatus="status">
							<table
								style="line-height: 1.33em; border-collapse: collapse; font-size: 20px;"
								cellspacing="1″ cellpadding="5″>
								<tbody>
									<tr>
										<c:choose>
											<c:when test="${not empty choice.image}">
												<td rowspan="3"><img alt=""
													src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
													style="width: 200px; height: 200px; margin-left: 20px" />&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td rowspan="3"><img
													style="width: 200px; height: 200px; margin-left: 30px"
													alt="" src="<c:url value="/images/no_image.gif" />" />
													&nbsp;</td>
											</c:otherwise>
										</c:choose>


									</tr>
									<tr>
										<td>&nbsp;
											<button
												onclick="speak('${choice.content}','${choice.lan_code}');return false;">
												<img src="<c:url value='/images/speaker.png' />" />
											</button>&nbsp;My study language
										</td>
										<td>&nbsp;&nbsp;${choice.content}</td>

									</tr>
									<tr>

										<td>&nbsp;
											<button
												onclick="speak('${title.content}','${title.language.code}');return false;">
												<img src="<c:url value='/images/speaker.png' />" />
											</button>&nbsp;My language
										</td>
										<td>&nbsp;&nbsp;<a
											href="<c:url value="/item/${choice.itemid}"/>">${choice.mycontent}</a></td>

									</tr>

								</tbody>
							</table>
						</c:forEach>
						<br>
						<div
							style="font-family: arial, meiryo, simsun, sans-serif; font-size: 22px; font-weight: bold; color: gray; margin-bottom: 10px;">
							<img src="<c:url value='/images/system-help.png' />" alt="●" />&nbsp;Do
							you remember learning location?
						</div>
						<div id="searchMap"
							style="margin-left: 60px; width: 540px; height: 400px;"></div>
						<br>
						<div style="margin-top: 10px; text-align: center">
							<input type="submit" class="btn btn-primary" value="Yes"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" /> <input
								type="submit" class="btn btn-warning" value="No"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>
					</c:if>
					<c:if test="${mqtype==2}">
						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Question</h3>

							</div>
						</div>
						<div
							style="font-family: arial, meiryo, simsun, sans-serif; font-size: 22px; font-weight: bold; color: gray; margin-bottom: 10px;">
							<img src="<c:url value='/images/system-help.png' />" alt="●" />Select
							the right word
							<c:forEach items="${quizzes}" var="choice" varStatus="status">
								<c:if test="${choice.answer==1}">
									<font color="red"><u>${choice.mycontent}</u></font>
								</c:if>
							</c:forEach>
							?
						</div>

						<table style="border-style: none;">
							<c:forEach items="${quizzes}" var="choice" varStatus="status">
								<ul
									style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 3px 20px; line-height: 30px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
									<li><input type="radio" name="answer"
										value="${choice.answer}" />&nbsp;&nbsp;${choice.content}</li>
									<br>
								</ul>
							</c:forEach>
						</table>


						<div id="quizselect">
							<br /> <input type="button" class="btn btn-primary"
								value="Answer" onClick="ans()" /> <input type="button"
								class="btn btn-success" value="Too Easy" onclick="ans()" /> <input
								type="button" class="btn btn-warning" value="Too Difficult"
								onclick="ans()" /> <input type="button" class="btn btn-danger"
								value="No Good"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>
					</c:if>
					<c:if test="${mqtype==3}">
						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Question</h3>

							</div>
						</div>
						<div
							style="font-family: arial, meiryo, simsun, sans-serif; font-size: 22px; font-weight: bold; color: gray; margin-bottom: 10px;">
							<img src="<c:url value='/images/system-help.png' />" alt="●" />Which
							image can be linked to
							<c:forEach items="${quizzes}" var="choice" varStatus="status">
								<c:if test="${choice.answer==1}">
									<font color="red"><u>${choice.content}</u></font>
								</c:if>
							</c:forEach>
							?
						</div>

						<table style="border-style: none;">
							<c:forEach items="${quizzes}" var="choice" varStatus="status">
								<c:if test="${status.index==0}">
									<tr>
										<td>
											<ul
												style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 3px 20px; line-height: 30px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
												<li><input type="radio" name="answer"
													value="${choice.answer}" />&nbsp;&nbsp;<img alt=""
													src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
													style="width: 260px; height: 240px; margin-left: 60px" /></li>
											</ul>
										</td>
								</c:if>
								<c:if test="${status.index==1}">
									<td><ul
											style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 3px 20px; line-height: 30px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
											<li><input type="radio" name="answer"
												value="${choice.answer}" />&nbsp;&nbsp;<img alt=""
												src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
												style="width: 260px; height: 240px; margin-left: 60px" /></li>
										</ul></td>
									</tr>
								</c:if>
								<c:if test="${status.index==2}">
									<tr>
										<td><ul
												style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 3px 20px; line-height: 30px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
												<li><input type="radio" name="answer"
													value="${choice.answer}" />&nbsp;&nbsp;<img alt=""
													src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
													style="width: 260px; height: 240px; margin-left: 60px" /></li>
											</ul></td>
								</c:if>
								<c:if test="${status.index==3}">
									<td><ul
											style="list-style-image:url('<c:url value='/images/icon_arrow_2.gif' />');margin: 3px 20px; line-height: 30px;font-family: arial,meiryo,simsun,sans-serif; font-weight:bold; font-size: 18px; overflow: visible; color: green; vertical-align: baseline">
											<li><input type="radio" name="answer"
												value="${choice.answer}" />&nbsp;&nbsp;<img alt=""
												src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
												style="width: 260px; height: 240px; margin-left: 60px" /></li>
										</ul></td>
									</tr>
								</c:if>

							</c:forEach>
						</table>
						<div id="quizselect">
							<br /> <input type="button" class="btn btn-primary"
								value="Answer" onClick="ans2()" /> <input type="button"
								class="btn btn-success" value="Too Easy" onclick="ans()" /> <input
								type="button" class="btn btn-warning" value="Too Difficult"
								onclick="ans2()" /> <input type="button" class="btn btn-danger"
								value="No Good"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>
					</c:if>
					<c:if test="${mqtype==4}">
						<div class="moreInfo">
							<ul style="margin-top: 40px">
								<li><font size="4"><b>You have finished all the
											quizzes. Please upload more! or Let's play tommorow!</b></font></li>
							</ul>
						</div>
					</c:if>
				</div>
				<div id="center">
					<div id="correct">
						<br> <span
							style="word-break: normal; color: green; font-size: 17px">
							<img
								src="<c:url value='/images/quiz_rabit/rabit/blush.png' />" alt="" /><b>Very good!! Your answer is right! You can challenge more!
						</b>
						</span> <br> <br> <br>
						<c:forEach items="${quizzes}" var="choice" varStatus="status">
							<table style="line-height: 1.33em; border-collapse: collapse;"
								cellspacing="1″ cellpadding="5″>
								<tbody>
									<tr>
										<c:choose>
											<c:when test="${choice.answer==1}">
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/cans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/fans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:otherwise>
										</c:choose>


									</tr>
									<tr>
										<td>My Study Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;${choice.content}</td>

									</tr>
									<tr>

										<td>My Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;<a
											href="<c:url value="/item/${choice.itemid}"/>">${choice.mycontent}</a></td>

									</tr>



								</tbody>
							</table>
							<br>
						</c:forEach>
						<div style="margin-top: 10px;">
							<a href="<c:url value="/quiz/myquiz" />">
							</a>
						</div>
						<div style="text-align: center">
							<input type="button" class="btn-primary" value="More Quiz"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>
					</div>
					<div id=miss>
						<br> <span
							style="word-break: normal; color: green; font-size: 18px">
							<img
								src="<c:url value='/images/quiz_rabit/rabit/scared.png' />" alt="" /><b>Sorry, your answer is not right.The right answer is:<c:forEach
									items="${quizzes}" var="choice" varStatus="status">
									<c:choose>
										<c:when test="${choice.answer==1}">${status.index+1}</c:when>
									</c:choose>
								</c:forEach>
						</b>
						</span> <br> <br> <br>
						<c:forEach items="${quizzes}" var="choice" varStatus="status">
							<table style="line-height: 1.33em; border-collapse: collapse;"
								cellspacing="1″ cellpadding="5″>
								<tbody>
									<tr>
										<c:choose>
											<c:when test="${choice.answer==1}">
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/cans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/fans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:otherwise>
										</c:choose>


									</tr>
									<tr>
										<td>My Study Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;${choice.content}</td>

									</tr>
									<tr>

										<td>My Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;<a
											href="<c:url value="/item/${choice.itemid}"/>">${choice.mycontent}</a></td>

									</tr>



								</tbody>
							</table>
							<br>
						</c:forEach>
						<div style="margin-top: 10px;">
							<a href="<c:url value="/quiz/myquiz" />"> 
							</a>
						</div>
						<div style="text-align: center">
							<input type="button" class="btn-primary" value="More Quiz"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>

					</div>
				</div>

				<div id="center">
					<div id="correct2">
						<br> <span
							style="word-break: normal; color: green; font-size: 17px">
							<img
								src="<c:url value='/images/quiz_rabit/rabit/happy.png' />" alt="" /><b>Very good!! Your answer is right! You can challenge more!
						</b>
						</span> <br> <br> <br>
						<c:forEach items="${quizzes}" var="choice" varStatus="status">
							<table style="line-height: 1.33em; border-collapse: collapse;"
								cellspacing="1″ cellpadding="5″>
								<tbody>
									<tr>
										<c:choose>
											<c:when test="${choice.answer==1}">
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/cans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/fans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:otherwise>
										</c:choose>
										<td rowspan="3"><img alt=""
											src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
											style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>

									</tr>
									<tr>
										<td>My Study Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;${choice.content}</td>

									</tr>
									<tr>

										<td>My Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;<a
											href="<c:url value="/item/${choice.itemid}"/>">${choice.mycontent}</a></td>

									</tr>



								</tbody>
							</table>
							<br>
						</c:forEach>
						<div style="margin-top: 10px;">
							<a href="<c:url value="/quiz/myquiz" />">
							</a>
						</div>
						<div style="text-align: center">
							<input type="button" class="btn-primary" value="More Quiz"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>
					</div>
					<div id=miss2>
						<br> <span
							style="word-break: normal; color: green; font-size: 18px">
							<img
								src="<c:url value='/images/quiz_rabit/rabit/sad.png' />" alt="" /><b>Sorry, your answer is not right.The right answer is:<c:forEach
									items="${quizzes}" var="choice" varStatus="status">
									<c:choose>
										<c:when test="${choice.answer==1}">${status.index+1}</c:when>
									</c:choose>
								</c:forEach>
						</b>
						</span> <br> <br> <br>
						<c:forEach items="${quizzes}" var="choice" varStatus="status">
							<table style="line-height: 1.33em; border-collapse: collapse;"
								cellspacing="1″ cellpadding="5″>
								<tbody>
									<tr>
										<c:choose>
											<c:when test="${choice.answer==1}">
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/cans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:when>
											<c:otherwise>
												<td rowspan="3"><img alt=""
													src="<c:url value='/images/fans.png' />"
													style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
											</c:otherwise>
										</c:choose>

										<td rowspan="3"><img alt=""
											src="${staticserverUrl}/${projectName}/${choice.image}_320x240.png"
											style="width: 125px; height: 125px; margin-left: 10px" />&nbsp;</td>
									</tr>
									<tr>
										<td>My Study Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;${choice.content}</td>

									</tr>
									<tr>

										<td>My Language</td>
										<td>&nbsp;<button onclick="speak('${choice.content}','${choice.lan_code}');return false;"><img src="<c:url value='/images/speaker.png' />" /></button>&nbsp;<a
											href="<c:url value="/item/${choice.itemid}"/>">${choice.mycontent}</a></td>

									</tr>



								</tbody>
							</table>
							<br>
						</c:forEach>
						<div style="margin-top: 10px;">
							<a href="<c:url value="/quiz/myquiz" />"> 
							</a>
						</div>
						<div style="text-align: center">
							<input type="button" class="btn-primary" value="More Quiz"
								onclick="location.href='<c:url value="/quiz/myquiz" />'" />
						</div>

					</div>
				</div>

			</div>

			<div id="Center">
				<div class="navbar navbar-inner" style="position: static;">
					<div class="navbar-primary">
						<h3
							style="font-size: 14px; font-weight: bolder; line-height: 150%">Ancillary
							Material</h3>

					</div>
				</div>
				<c:forEach items="${materials}" var="sentence" varStatus="status">

					<li><font size="4">${sentence.def}</font></li>
				</c:forEach>

			</div>

		</div>

	</div>
	</div>
	</div>
	</div>
</body>
</html>