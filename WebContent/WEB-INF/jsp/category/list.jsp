<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Category" />
    </c:import>
      <script type="text/javascript">
    $(document).ready(function() {

            		document.getElementById("default_settings").className = "";
            		document.getElementById("default__homepage").className = "";
            		document.getElementById("default_category").className = "active";
            		
            	});
    </script>
<body id="page_diary_list">
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
                            <div id="Center">
                                <div id="groupSearchFormLine" class="parts searchFormLine">
                                </div>
                                <div class="dparts searchResultList"><div class="parts">
                                         <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
                                            <h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Categories</h3>
                                        </div></div>
                                        <div class="pagerRelative">
                                            <a href="<c:url value="/category/add" />">Create a category</a>
                                            <p class="number"><!--7件中 1～7件目を表示--></p>
                                        </div>
                                        <div class="block" style="font-size:large">
                                        <c:forEach items="${categoryList}" var="category">
											<a href="<c:url value='/category/${category.id}' />">${category.name}</a><br />
										</c:forEach>
                                        </div>
                                        <div class="pagerRelative">
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