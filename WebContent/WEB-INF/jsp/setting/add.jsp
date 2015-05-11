<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Settings" />
        <c:param name="css">
        	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/checkboxtree.css"/>" />
        </c:param>
        <c:param name="javascript">
            <script type="text/javascript" src="<c:url value="/js/jquery/jquery.checkboxtree.js" />"></script>
            <script type="text/javascript" src="<c:url value="/js/jquery/jquery.selectboxes.js" />"></script>
            <script type="text/javascript">
            	$(function(){
            		jQuery("#categorytree").checkboxTree({
            			collapsedarrow: "<c:url value='images/img-arrow-collapsed.gif' />",
            			expandedarrow: "<c:url value='images/img-arrow-expanded.gif' />",
            			blankarrow: "<c:url value='images/img-arrow-blank.gif' />",
            			checkchildren: false,
            			checkparents: false
            		});

            		//Init default category
            		var defaultCategoryId;
            		<c:if test="${user.defaultCategory!=null}">
            			defaultCategoryId = "${user.defaultCategory.id}";
            		</c:if>
            		$(".cbCategoryIdList:checked").each(function(index){
                        $(this).val()+":"+$("#name"+$(this).val()).val();
                        $("#defaultCategory").addOption($(this).val(), $("#name"+$(this).val()).val());
                	});
                	if(defaultCategoryId!=null){
                    	$("#defaultCategory").selectOptions(defaultCategoryId);
                	}

                	$(".cbCategoryIdList").click(function(){
                    	if($(this).attr("checked")==true){
                        	$("#defaultCategory").addOption($(this).val(), $("#name"+$(this).val()).val());
                    	}else{
                        	$("#defaultCategory").removeOption($(this).val());
                    	}
                    });
                });
            	
            	
            </script>
              
              
        </c:param>
    </c:import>
    <script type="text/javascript">
    $(document).ready(function() {
		document.getElementById("default__homepage").className = "";
		document.getElementById("default_settings").className = "active";
		
	});
    </script>
    <body id="page_diary_new">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                    	  <c:import url="../include/profile_var.jsp"></c:import>
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Top -->
                             <form:form commandName="settingform" action="${settingUrl}" method="post">
                            <div id="Center">
                                 <c:url value="/mysetting" var="settingUrl" />
                                <div id="diaryForm" class="dparts form">
                                    <div class="parts">
                                        <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Time to receive a quiz</h3></div></div>
                                            <table>
                                                <%--
                                                <tr>
                                                    <th><label for="monday">Monday</label></th>
                                                    <td>
                                                        <table>
                                                            <tr>
                                                                <td style="width:170px;">送信時間</td>
                                                                <td>
                                                                    <form:select path="monstime1">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>ほかの学習可能時間(午前)</td>
                                                                <td>
                                                                     <form:select path="monfperiod1">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${morning}"/>
                                                                    </form:select>
                                                                    &nbsp;To&nbsp;
                                                                     <form:select path="montperiod1">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${morning}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                           　<tr>
                                                                <td>ほかの学習可能時間(午後)</td>
                                                                <td>
                                                                      <form:select path="monfperiod2">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${afternoon}"/>
                                                                    </form:select>
                                                                    &nbsp;To&nbsp;
                                                                     <form:select path="montperiod2">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${afternoon}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>ほかの学習可能時間(夜)</td>
                                                                <td>
                                                                     <form:select path="monfperiod3">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${evening}"/>
                                                                    </form:select>
                                                                    &nbsp;To&nbsp;
                                                                     <form:select path="montperiod3">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${evening}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="Tuesday">Tuesday</label></th>
                                                    <td>
                                                        <table>
                                                            <tr>
                                                                <td style="width:170px;">送信時間</td> 
                                                                <td>
                                                                    <form:select path="tuestime1">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>ほかの学習可能時間(午前)</td>
                                                                <td>
                                                                     <form:select path="tuefperiod1">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${morning}"/>
                                                                    </form:select>
                                                                    &nbsp;To&nbsp;
                                                                     <form:select path="tuetperiod1">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${morning}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                           　<tr>
                                                                <td>ほかの学習可能時間(午後)</td>
                                                                <td>
                                                                      <form:select path="tuefperiod2">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${afternoon}"/>
                                                                    </form:select>
                                                                    &nbsp;To&nbsp;
                                                                     <form:select path="tuetperiod2">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${afternoon}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>ほかの学習可能時間(夜)</td>
                                                                <td>
                                                                     <form:select path="tuefperiod3">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${evening}"/>
                                                                    </form:select>
                                                                    &nbsp;To&nbsp;
                                                                     <form:select path="tuetperiod3">
                                                                        <form:option value="" label="Please Select"/>
                                                                        <form:options items="${evening}"/>
                                                                    </form:select>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                            --%>
                                                 <tr>
                                                    <th><label for="Monday">Monday</label></th>
                                                    <td>
                                                        <form:select path="monstime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                       ： <form:select path="monsmin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                                 <tr>
                                                    <th><label for="Tuesday">Tuesday</label></th>
                                                    <td>
                                                        <form:select path="tuestime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                         ： <form:select path="tuesmin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                                  <tr>
                                                    <th><label for="Monday">Wednesday</label></th>
                                                    <td>
                                                        <form:select path="wedstime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                         ： <form:select path="wedsmin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                                 <tr>
                                                    <th><label for="Tuesday">Thursday</label></th>
                                                    <td>
                                                        <form:select path="thurstime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                         ： <form:select path="thursmin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                                  <tr>
                                                    <th><label for="Friday">Friday</label></th>
                                                    <td>
                                                        <form:select path="fristime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                         ： <form:select path="frismin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                                 <tr>
                                                    <th><label for="Saturday">Saturday</label></th>
                                                    <td>
                                                        <form:select path="satstime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                         ： <form:select path="satsmin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                                 <tr>
                                                    <th><label for="Sunday">Sunday</label></th>
                                                    <td>
                                                        <form:select path="sunstime1">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${oneDay}"/>
                                                         </form:select>
                                                         ： <form:select path="sunsmin1">
                                                                        <form:options items="${minutes}"/>
                                                         </form:select>
                                                    </td>
                                                 </tr>
                                            </table>
                                    </div><!-- parts -->
                                    <%-- 2011/06/13 HOU Bin
                                      <br/>
                                     <div class="parts">
                                        <div class="partsHeading"><h3>The handset you use  </h3></div>
                                        <table>
                                                  <tr>
                                                    <th><label for="handset">Handset</label></th>
                                                    <td>
                                                        <form:select path="handsetcd">
                                                                        <form:option value="" label="Select"/>
                                                                        <form:options items="${handsets}"/>
                                                         </form:select>  
                                                    </td>
                                                 </tr>
                                        </table>
                                     </div>
                                      --%>
                                     <div class="parts">
                                         <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Mail Notification  </h3></div></div>
                                        <table>
                                                  <tr>
                                                    <th colspan="2"><label for="receiveMailNotification">Receive mail notificaion?</label></th>
                                                    <td>
                                                    	<form:checkbox path="receiveMailNotification" id="receiveMailNotification" />
                                                    	<%--
                                                    	<form:radiobutton path="receiveMailNotification" value="true" id="receiveMailNotificationYes"/>
                                                    	<label for = "receiveMailNotificationYes">Yes</label>
                                                    	<form:radiobutton path="receiveMailNotification" value="false" id="receiveMailNotificationNo"/>
                                                    	<label for = "receiveMailNotificationNo">No</label>
                                                    	--%>
                                                    </td>
                                                 </tr>
                                        </table>
                                     </div>
                                     <div class="parts">
                                        <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Select your categories</h3></div></div>
                                        <table>
                                                  <tr>
                                                    <th>Categories</th>
                                                    <td>
	                                                    <ul class="unorderedlisttree" id="categorytree">
	                                                    	<c:forEach items="${categoryRootList}" var="cat">
	                                                    	<li>
	                                                    		<c:set var="node" value="${cat}" scope="request" />
	                                                    		<jsp:include page="categoryNode.jsp" />
	                                                    	</li>
	                                                    	</c:forEach>
	                                                    </ul>
	                                                    <label for="defaultCategory">Default: </label>
	                                                    <select id="defaultCategory" name="defaultCategoryId" style="width: 200px;">
	                                                    </select>
                                                    </td>
                                                 </tr>
                                        </table>
                                     </div>
                                </div><!-- dparts -->
                                
                            </div><!-- Center -->
                                   <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li>
                                                        <input type="submit" class="btn btn btn-primary" value="Submit" />
                                                    </li>
                                                </ul>
                                            </div>
							</form:form>
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <%--
                            <form action="/member/changeLanguage" method="post"><label for="language_culture">言語</label>:
                                <select name="language[culture]" onchange="submit(this.form)" id="language_culture">
                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select><input value="diary/new" type="hidden" name="language[next_uri]" id="language_next_uri" /></form>
                            --%>
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
