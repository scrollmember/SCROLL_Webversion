<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>

<html>

<c:import url="../include/head.jsp">
    <c:param name="title" value="Add a new object" />
    <c:param name="content">
        <style>
            .titleLangName{width:70px;}
            .titleMap{width:70%;}
            #titleTable button{width:20%;}
            .optional{display:none}
        </style>
            <script src="http://www.google.com/jsapi"></script>
            <script src="http://maps.google.com/maps/api/js?sensor=true"></script>
            <script src="<c:url value='/js/LLMap.js' />"></script>
            <script>
                function translateTitle(code){
                    var translateTitleUri = "${baseURL}/api/translate/itemTitle";
                    var titles = "{";
                    $.each($(".titleMap"),function(i, item){
                        titles+=$(item).attr("lang")+":\""+$(item).val()+"\"";
                        if(i<$(".titleMap").length-1)titles+=",";
                    });
                    titles+="}";
                    var inputdata={ 'target':code, 'titles': titles};
                    $.get(translateTitleUri,inputdata ,function(data){
                        $("#inputTitle_"+code).val(data);
                    });
                }

                function translateQuestion(){
                    var translateTitleUri = "${baseURL}/api/translate/text";
                    var inputdata={ 'src': $("#transFrom").val(), 'dest':$("#transTo").val(), 'text': $("#question").val()};
                    $.get(translateTitleUri,inputdata ,function(data){
                        $("#question").val($("#question").val()+"{"+data+"}");
                    });
                }
                
                var map;
                $(function(){
                	map = new LLMap("map", {
                		onchange:function(lat, lng, zoom){
                			$("#itemLat").val(lat);
                			$("#itemLng").val(lng);
                			$("#itemZoom").val(zoom);
                		}
                	});
                	
                	$(document).on("change", "#addLangSelect", function(){
                        var code = $("#addLangSelect").val();
                        if(code==null && code==-1)return;
                        var name = $("#addLangSelect").find("option:selected").text();
                        $("<tr><td class=\"titleLangName\">"+name+"</td><td><input name=\"titleMap['"+code+"']\" id=\"inputTitle_"+code+"\" class=\"titleMap\" lang=\""+code+"\" />&nbsp;<button onclick=\"translateTitle('"+code+"');return false;\">Translate</button></td></tr>").appendTo($("#titleTable"));
                        $("#addLangSelect option[value='"+code+"']").remove();
                        if($("#addLangSelect option").length<=1){
                            $("#addLangSelect").parent().hide();
                        }
                	}).on("click", "#showMoreButton",function(){
                		if($(".optional:first").is(":hidden")){
                			$(".optional").show();
                			$("#showMoreButton").text("Hide Details");
                		}else{
                			$(".optional").hide();
                			$("#showMoreButton").text("Show Details");
                		}
                	}).on("click", "#generateQrcode", function(){
                        if($("#qrcode").val()!=""){
                            if(!confirm("Generate a new QR code?")){
                                return;
                            }
                        }
                        $.get("<c:url value="/api/qrcode/generate" />", function(data){
                            $("#qrcode").val(data);
                            $("#qrcodeArea").html("<img src=\"http://chart.apis.google.com/chart?cht=qr&chs=120x120&chl="+data+"\"/>");
                        });
                        return false;
                    }).on("click", "#clearQrcode", function(){
                        $("#qrcode").val("");
                        $("#qrcodeArea").html("");
                    }).on("click", "#printQrcode", function(){
                        $.get("<c:url value="/api/qrcode/generate" />", function(data){
                            $("#qrcode").val(data);
                            $("#qrcodeArea").html("<img src=\"http://chart.apis.google.com/chart?cht=qr&chs=120x120&chl="+data+"\"/>");
                            window.open("<c:url value='/qrcodeprint?content=' />"+$("#qrcode").val(),"", "height=170, width=170" );
                        });
                    }).on("change", "#qrcode", function(){
                        if($.trim($("#qrcode").val())==""){
                            $("#qrcodeArea").html("");
                            return;
                        }
                        $("#qrcodeArea").html("<img src=\"http://chart.apis.google.com/chart?cht=qr&chs=120x120&chl="+$("#qrcode").val()+"\"/>");
                    });
                });
            </script>
            
    
	    <script type="text/javascript">
        function getUrlVars()
        {
            var vars = [], hash;
            var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
            for(var i = 0; i <hashes.length; i++)
            {
                hash = hashes[i].split('=');
                vars.push(hash[0]);
                vars[hash[0]] = hash[1];
            }
            return vars;
        }
	    
	    $(document).ready(function() {
	    	var vars = getUrlVars();
	    	if (vars["langlist"] === void 0) return ;
	   		var lang = vars["langlist"].split("|");
	    	$.each(lang, function(i, val){
		    	if (vars[val+"_title"] !== void 0) {
		    		$("#inputTitle_"+val).val(decodeURI(vars[val+"_title"]));
		    	}
	    	});
	    });
	    </script>

        </c:param>
    </c:import>
    <script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("alllog").className = "active";

	});
</script>

<script type="text/javascript">


$(function() {
	

	});
	
function myLocation(locationReturned) {
   
    console.log(locationReturned);
}
</script>
    <body>
        <div>
            <div>
           
                <c:import url="../include/header.jsp" />
                <c:import url="../include/Slidermenu.jsp" /> 
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Top -->
                            <div id="Center">
                                <div id="diaryForm" class="dparts form">
                                    <div class="parts">
                                        <div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Add new object</h3></div></div>
                                        <c:url value="/item" var="itemUrl" />
                                        <form:form commandName="item" action="${itemUrl}" method="post" enctype="multipart/form-data">
                                            <strong>*</strong>&nbsp;Required.
                                            <button style="float:right" id="showMoreButton" onclick="return false;" class="btn btn-primary">Show Details</button>　
                                            <table>
                                                <tr class="optional">
                                                    <th><label for="setting">Setting</label></th>
                                                    <td>
                                                        <table>
                                                            <tr>
                                                                <td style="width:90px;">Share Level</td>
                                                                <td>
                                                                    <form:select path="shareLevel">
                                                                        <form:option value="PUBLIC" label="Public" />
                                                                        <form:option value="PRIVATE" label="Private" />
                                                                    </form:select>
                                                                </td>
                                                                  <td style="width:90px;">Category</td>
                                                                <td>
                                                                    <c:choose>
                                                            <c:when test="${empty user.myCategoryList}">
                                                                <a href="<c:url value="/mysetting" />" target="_blank">Set categories</a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <form:select path="categoryId">
                                                                    <form:option value="" label="Select category" />
                                                                    <c:forEach items="${user.myCategoryList}" var="cat">
                                                                      <form:option value="${cat.id}" label="${cat.name}" />
                                                                    </c:forEach>
                                                                </form:select>
                                                            </c:otherwise>
                                                        </c:choose>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr class="optional">
                                                    <th><label for="quiztype">Quiz Options</label></th>
                                                    <td>
                                                        <ul>
                                                        <c:forEach items="${questionTypes}" var="qestype">
                                                            <li><form:checkbox path="questionTypeIds" value="${qestype.id}" checked="true"/><label>&nbsp;${qestype.title}</label></li>
                                                        </c:forEach>
                                                            <li><form:checkbox path="locationBased" /><label>&nbsp;Location Based</label></li>
                                                        </ul>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="diary_title">Title</label><strong>*</strong>
                                                    </th>
                                                    <td> 
                                                        <table id="titleTable">
                                                            <c:forEach items="${langs}" var="lang">
                                                                <tr>
                                                                    <td class="titleLangName" style="width:90px">${lang.name}</td>
                                                                    <td>
                                                                        <form:input path="titleMap['${lang.code}']" id="inputTitle_${lang.code}" cssClass="titleMap" lang="${lang.code}" placeholder="Language input" style="height:30px"/>&nbsp;<button onclick="translateTitle('${lang.code}');return false;" class="btn btn-success">Translate</button>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </table>
                                                        <div>
                                                            <select id="addLangSelect">
                                                                <option value="-1">--Add a new language--</option>
                                                                <c:forEach items="${langList}" var="lang">
                                                                <c:if test="${!langs.contains(lang)}"><option id="addLang_${lang.code}" value="${lang.code}">${lang.name}</option></c:if>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="photo">Photo|Video<br />Audio|PDF</label></th>
                                                    <td>
                                                        <input type="file" name="image" id="image" class="input_file" /><form:errors cssClass="error" path="image" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="note">Description</label></th>
                                                    <td>
                                                        <form:textarea path="note" cols="20" rows="4" cssStyle="width:98%" placeholder="please enter up your description"/>
                                                    </td>
                                                </tr>
                                                <tr class="optional">
                                                    <th><label for="tag1">ID</label></th>
                                                    <td>
                                                        <table>
                                                            <tr>
                                                                <td style="width:70px;">Barcode</td>
                                                                <td><form:input path="barcode" id="barcode" /></td>
                                                            </tr>
                                                            <tr>
                                                                <td>QR code</td>
                                                                <td>
                                                                    <form:input path="qrcode" id="qrcode" />
                                                                    <button id="printQrcode" onclick="return false;">Print</button>
                                                                    <button id="clearQrcode" onclick="return false;">Clear</button>
                                                                    <div id="qrcodeArea"></div>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>RFID</td>
                                                                <td><form:input path="rfid" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th rowspan="2"><label for="place">Location</label></th>
                                                    <td>
                                                        <form:input path="place" cssClass="input_text" id="place"/>
                                                        (e.g.) The University of Tokushima
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <form:hidden path="itemLat" id="itemLat" />
                                                        <form:hidden path="itemLng" id="itemLng"/>
                                                        <form:hidden path="itemZoom" id="itemZoom"/>
                                                        <div id="map" style="width: 98%; height: 350px"></div>
                                                    </td>
                                                </tr>
                                                <tr> <th rowspan="2"><label for="place">Place</label></th>
                                                <td>
                                                
                                              
                                           		<c:forEach items="${placeinfo}" var="obj">
                                        
                                           		 <li>
                                           
                                           		 <form:radiobutton name="q1" path="map['${obj.key}']" value="${obj.value}"/><label>&nbsp;<c:out value="${obj.key}"/></label></li>
                                            
                                                        </c:forEach>
                                                </td>
                                                </tr>
                                                <tr class="optional">
                                                    <th><label for="question">Question</label></th>
                                                    <td>
                                                        <form:textarea path="question" cols="30" rows="10" id="question" cssStyle="width:98%" ></form:textarea>
                                                        <select id="transFrom" name="transFrom">
                                                            <c:forEach items="${langList}" var="lang">
                                                            <option value="${lang.code}">${lang.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                        -&gt;
                                                        <select id="transTo" name="transTo">
                                                            <c:forEach items="${langList}" var="lang">
                                                            <option value="${lang.code}">${lang.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                        <button id="transButton" onclick="translateQuestion();return false;">Translate Question</button>
                                                    </td>
                                                </tr>
                                                <tr class="optional">
                                                    <th><label for="tag1">Tag</label></th>
                                                    <td>
                                                        <input type="text" name="tag" id="tagInput" />
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li>
                                                        <input type="submit" class="btn btn-info" value="Save" />
                                                    </li>
                                                    <li>
                                                        <a href="<c:url value="/item" />">Return to Object List</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </form:form>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div><!-- Center -->
                        </div><!-- Layout -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
  
    </body>
</html>
