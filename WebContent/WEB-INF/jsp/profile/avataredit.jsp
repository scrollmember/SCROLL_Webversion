<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="写真編集" />
    </c:import>
      <script type="text/javascript">

	$(document).ready(function() {

		document.getElementById("setting").className = "active";
	});
</script>
    <body id="page_member_registerInput">
        <div id="Body">
            <div id="Container">

              
                     <c:import url="../include/header.jsp" />
                

                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="localNav">
                        </div><!-- localNav -->
                        <div id="LayoutC" class="Layout">
                            <div id="Center">
                                <div id="RegisterForm" class="dparts form">
                                    <div class="parts">
                                    <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
                                        <h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">写真を編集する</h3></div></div>
                                        <c:url value="/profile/avataredit" var="url" />
                                        <form:form modelAttribute="form" action="${url}" method="post" enctype="multipart/form-data">
                                            <div id="memberImageBox_30" class="parts memberImageBox">
                                                <p class="photo">
                                                    <a id="avatarImage" class="zoom" href="#avatarImageZoom">
                                                        <img alt="" src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />" width="240px" />
                                                    </a>
                                                </p>
                                                <p class="text"></p>
                                            </div><!-- parts -->
                                            <table>
                                                <tr>
                                                    <td>
                                                        <input type="file" name="photo" id="photo" /><form:errors path="photo" />
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li><center>
                                                        <input type="submit" class="btn btn-block btn-primary" value="Save" style="width:150px"/>
                                                    </center></li>
                                                </ul>
                                            </div>
                                        </form:form>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <%--
                            <form action="/member/changeLanguage" method="post"><label for="language_culture">言語</label>:
                                <select name="language[culture]" onchange="submit(this.form)" id="language_culture">
                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select><input value="member/registerInput" type="hidden" name="language[next_uri]" id="language_next_uri" /></form>
                            --%>
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>