<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
    	<c:param name="title" value="Lanuage List" />
    	<c:param name="css">
    		<style type="text/css">
    			.dualline{
    				background-color: #00FA9A;
    			}
    		</style>
    	</c:param>
    </c:import>
    <body id="page_diary_list">
    <div id="Body">
        <div id="Container">
            <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
						<c:import url="../include/state_submenu.jsp"/>
							<c:if test="${isRecommended == 1}">
								<c:import url="recommend.jsp"/>
							</c:if>
							<c:if test="${isRecommended != 1}">
								<c:import url="state.jsp"/>
							</c:if>
                                        <div class="pagerRelative">
                                            <p class="number"><!--7件中 1～7件目を表示--></p>
                                        </div>
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