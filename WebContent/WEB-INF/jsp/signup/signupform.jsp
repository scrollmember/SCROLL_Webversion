<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Enter Profile" />
    </c:import>
    <body id="page_member_registerInput">
        <div id="Body">
            <div id="Container">


                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="localNav">
                        </div><!-- localNav -->
                        <div id="LayoutC" class="Layout">
                            <div id="Center">
                                <div id="RegisterForm" class="dparts form">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Enter Profile</h3></div>
                                        <c:url value="/signup/${user.activecode}" var="signupFormUrl" />
                                        <form:form commandName="signupForm" action="${signupFormUrl}" method="post">
                                            <p><strong>*</strong>are required</p>
                                            <table>
                                                <tr>
                                                    <th><label for="pcEmail">E-mail address</label></th>
                                                    <td>${user.pcEmail}</td>
                                                </tr>
                                                <tr>
                                                    <th><label for="nickname">Nickname</label> <strong>*</strong></th>
                                                    <td>
                                                        <form:input path="nickname" cssClass="input_text" id="nickname" />
                                                        <form:errors path="nickname" cssClass="error" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="firstName">Family name</label></th>
                                                    <td>
                                                        <form:input path="firstName" />
                                                        <form:errors path="firstName" cssClass="error" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="lastName">Given name</label></th>
                                                    <td>
                                                        <form:input path="lastName" />
                                                        <form:errors path="lastName" cssClass="error" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="interesting">Interests</label></th>
                                                    <td>
                                                        <form:textarea rows="4" cols="30" path="interesting" id="interesting" />
                                                        <div><form:errors path="interesting" cssClass="error" /></div>
                                                    </td>
                                                </tr>
                                                <c:forEach begin="0" end="1" var="myLangsIndex">
                                                    <tr>
                                                        <th>
                                                            <label for="myLangs[${myLangsIndex}]">Native language(${myLangsIndex})</label>
                                                            <c:if test="${0 eq myLangsIndex}"><strong>*</strong></c:if>
                                                        </th>
                                                        <td>
                                                            <form:select path="myLangs[${myLangsIndex}]">
                                                                <option value="">-Select-</option>
                                                                <form:options  items="${langList}" itemValue="code" itemLabel="name" />
                                                            </form:select>
                                                            <form:errors path="myLangs[${myLangsIndex}]" cssClass="error" />
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                <c:forEach begin="0" end="1" var="studyLangsIndex">
                                                    <tr>
                                                        <th>
                                                            <label for="studyLangs[${studyLangsIndex}]">Language of study(${studyLangsIndex})</label>
                                                            <c:if test="${0 eq studyLangsIndex}"><strong>*</strong></c:if>
                                                        </th>
                                                        <td>
                                                            <form:select path="studyLangs[${studyLangsIndex}]">
                                                                <option value="">-Select-</option>
                                                                <form:options  items="${langList}" itemValue="code" itemLabel="name" />
                                                            </form:select>
                                                            <form:errors path="studyLangs[${studyLangsIndex}]" cssClass="error" />
                                                        </td>
                                                    </tr>
                                                </c:forEach>
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