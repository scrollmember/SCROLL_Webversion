<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Learning Log" />
    </c:import>
    <body id="page_member_registerInput">
        <div id="Body">
            <div id="Container">

                <div id="Header">
                    <div id="HeaderContainer">
                        <h1><a href="/">Learning Log</a></h1>
                        <div id="globalNav">
                            <ul>
                            </ul>
                        </div><!-- globalNav -->
                        <div id="topBanner">
                        </div>
                    </div><!-- HeaderContainer -->
                </div><!-- Header -->

                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="localNav">
                        </div><!-- localNav -->
                        <div id="LayoutC" class="Layout">
                            <div id="Center">
                                <div id="RegisterForm" class="dparts form">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Reset your password</h3></div>
                                        <c:url value="/signup/resetpassword/${user.activecode}" var="signupFormUrl" />
                                        <form:form commandName="signupForm" action="${signupFormUrl}" method="post">
                                            <p><strong>*</strong>are required</p>
                                            <table>
                                                <tr>
                                                    <th><label for="pcEmail">E-mail address</label></th>
                                                    <td>${user.pcEmail}</td>
                                                </tr>
                                                <tr>
                                                    <th><label for="password">Password</label><strong>*</strong></th>
                                                    <td>
                                                        <form:password path="password" />
                                                        <form:errors path="password" cssClass="error" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="passwordConfirm">Password(Confirm)</label><strong>*</strong></th>
                                                    <td>
                                                        <form:password path="passwordConfirm" />
                                                        <form:errors path="passwordConfirm" cssClass="error" />
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li>
                                                        <input type="submit" class="input_submit" value="Submit" />
                                                    </li>
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