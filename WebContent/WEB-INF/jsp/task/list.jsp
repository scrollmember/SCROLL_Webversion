<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="userId"><shiro:principal property="id" /></c:set>
<!doctype html>
<c:import var="pageLinks" url="itempage.jsp">
	<c:param name="searchCond" value="${searchCond}" />
	<c:param name="page" value="${page}" />
</c:import>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="All Logs" />
        <c:param name="css">
            <style type="text/css">
                #itemSearchFormLine th{border:none;}
                #itemSearchFormLine td{border:none;}
            </style>
        </c:param>
        <c:param name="javascript">
        </c:param>
    </c:import>
    <body id="page_diary_list">
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
                            <div id="Center">
                                <div class="dparts searchResultList"><div class="parts">
                                        <div class="partsHeading">
                                            <h3>Sorted by date</h3>
                                            <a class="addlink" href="<c:url value="/item/add" />">Add new object</a>
                                        </div>
                                        <div class="pagerRelative">
											${pageLinks}
                                        </div>
                                        <div class="block">
                                      	Task List
                                        </div>
                                        <div class="pagerRelative">
											${pageLinks}
											<p class="number"><!--7件中 1～7件目を表示--></p>
                                        </div>
                                    </div></div>
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <%--
                            <form action="/member/changeLanguage" method="post"><label for="language_culture">言語</label>:
                                <select name="language[culture]" onchange="submit(this.form)" id="language_culture">

                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select><input value="diary/index" type="hidden" name="language[next_uri]" id="language_next_uri" /></form>
                            --%>
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
