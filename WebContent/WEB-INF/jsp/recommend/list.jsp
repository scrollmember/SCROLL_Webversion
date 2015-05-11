<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Recommend user" />
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


    <body id="page_diary_new">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Top -->

                            <!-- 追加 -->
                            <c:url value="/recommend/test" var="settingUrl" />
                             <form:form commandName="settingform" action="${settingUrl}" method="post">
                            <div id="Center">
<!-- ここから表示 -->
									<!-- クイズテスト？ -->
 <!-- aaaaaaaaa -->
                                        <div class="block" style="font-size:large">
                                        <c:forEach items="${categoryList}" var="category">
                                        	<%=new java.util.GregorianCalendar().getTime() %>
                                        	<input type="checkbox" checked="checked" name="${category.name}" }>
                                        	${category.id}：${category.name}
											<a href="<c:url value='/category/${category.id}' />">${category.name}</a><br />
										</c:forEach>

                                                <c:forEach items="${quiz.choices}" var="choice" varStatus="status">
                                                <tr>
                                                    <td>
                                                        <c:if test= "${!answered}">
                                                            <c:if test= "${status.index == 0}">
                                                               <input type="radio" name="answer"value="${choice.number}"  checked="checked"/>${choice.content} &nbsp;
                                                            </c:if>
                                                            <c:if test= "${status.index != 0}">
                                                                <input type="radio" name="answer"value="${choice.number}" />${choice.content} &nbsp;
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test= "${answered}">
                                                              <c:url value="/item/${choice.item_id}" var="itemurl" />
                                                            <c:if test= "${status.index == 0}">
                                                                <input type="radio" name="answer"value="${choice.number}"  checked="checked"/><a href="${itemurl}" target="_blank">${choice.content}</a> &nbsp;${choice.note}
                                                            </c:if>
                                                            <c:if test= "${status.index != 0}">
                                                                <input type="radio" name="answer"value="${choice.number}" /><a href="${itemurl}" target="_blank">${choice.content}</a> &nbsp;${choice.note}
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                                </c:forEach>


                                        </div>

<!-- カテゴリ設定ここから -->
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Select your favorite categories</h3></div>
                                        <table>
                                                  <tr>
                                                    <th>Categories</th>
                                                    <td>
	                                                    <ul class="unorderedlisttree" id="categorytree">
	                                                    	<c:forEach items="${categoryRootList}" var="cat">
	                                                    	<li>
	                                                    		<c:set var="node" value="${cat}" scope="request" />
	                                                    		<jsp:include page="../setting/categoryNode.jsp" />
	                                                    	</li>
	                                                    	</c:forEach>
	                                                    </ul>
	                                                    <label for="defaultCategory">aaa: </label>
	                                                    <select id="defaultCategory" name="defaultCategoryId" style="width: 200px;">
	                                                    </select>
                                                    </td>
                                                 </tr>
                                        </table>
                                     </div>
                                     <div class="operation">
                                     	<div class="moreInfo button">
                                     		<li>
                                     			<input type="submit" class="input_submit" value="Test"/>
                                     		</li>
                                     	</div>
                                     </div>

<!-- ここまで表示 -->

<a href="${categoryUrl}">Return</a>
<a href="<c:url value='/category' />">Top</a>&gt;
                            </div><!-- Center -->
                            <!-- 追加 -->
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