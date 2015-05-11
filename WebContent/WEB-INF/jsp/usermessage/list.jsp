<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html>
<c:import url="../include/Slidermenu.jsp" />
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Messages" />
        <c:param name="javascript">
        	<c:url value="/usermessage/send" var="usermessagesenduri"/>
        	<script type="text/javascript">
        		$(function(){
        			$("#msgbox").dialog({
        				autoOpen:false,
        				modal:true
        			});
        		});
        		function showMsg(target, msgId){
        			$.post("<c:url value="/usermessage/readmessage" />",{'msgId':msgId} , function(data){
        				if(typeof(data.message)!="undefined"){
	        				refreshUnreadMsg();
	        				$(target).parent().removeClass("unreadMsg");
	        				$("#msgboxcontent").html(data.message.content.replace(/\\n/g,"<br />"));
	        				$("#msgboxreply").attr("onclick", "replyMsg('"+data.message.sendFromId+"','"+data.message.sendFromName+"')");
	        				$("#msgbox").dialog( "option", "title", "Message from "+data.message.sendFromName).dialog("open");
        				}
        			},"json");
        		}

        		function replyMsg(sendToUid, sendToUname){
        			$("#msgbox").dialog("close");
	            	$("<div id=\"usermsgdlg\">"
			                +"<div><form id=\"usermessageform\" action=\"#\" method=\"post\">"
			            	+"<input id=\"umsg_sendto\" name=\"umsg_sendto\" type=\"hidden\" value=\""+sendToUid+"\" />"
			            	+"<textarea id=\"umsg_content\" name=\"content\" style=\"width:100%;height:100%;\" rows=\"5\"></textarea>"
			            	+"<input type=\"submit\" value=\"Send\" onclick=\"sendUserMessage();return false;\"/>"
					        +"</form></div>"
					        +"</div>").dialog({
					        	title:"Send A Message To <span style=\"color:green\">"+sendToUname+"</span>",
					        	modal:true,
					        	resizable:false});
        		}
        	</script>
        </c:param>
    </c:import>
    <script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("message").className = "active";

	});
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
                                                <img alt="LearningUser" src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />" />
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <p class="text"><shiro:principal property="nickname" /></p>
                                    <p class="text">Level : ${user.userLevel}</p>
                                    <p class="text">EXP : ${nowExperiencePoint} / Next : ${nextExperiencePoint}</p>
                                </div><!-- parts -->
                            </div><!-- Left -->
                            <div id="Center">
                            <div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
                                            <h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Transmission Message</h3>
                                        </div></div>
                            <c:forEach items="${mysendlist}" var="mym">
                             <div class="block">
                            <ul class="articleList">
                            <li class="msgitem">
                             <fmt:formatDate value="${mym.create_time}"  type="both" dateStyle="default" /> to ${mym.nickname}　</li>
                          <br>${mym.content}
                            </ul>
                            </div>
                            </c:forEach>
                            
                            
                            
                                <div id="homeRecentList_11" class="dparts homeRecentList"><div class="parts">
                                         <div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
                                            <h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Received Message</h3>
                                        </div></div>
                                        <div class="block">
                                            <ul class="articleList">
                                                <c:forEach items="${messageList}" var="msg">
                                                    <li <c:if test="${!msg.readFlag}">class="unreadMsg"</c:if>><a class="msgitem" href="#" onclick="showMsg(this, '${msg.id}')" ><fmt:formatDate value="${msg.createTime}"  type="both" dateStyle="default" /> from ${msg.sendFrom.nickname}</a></li>
                                                </c:forEach>
                                            </ul>
                                            
                                        </div>
                                    </div>
                                </div>
                                <div id="msgbox">
        	<div id="msgboxcontent"></div>
        	<button id="msgboxreply">Reply</button>
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

    </body>
</html>
