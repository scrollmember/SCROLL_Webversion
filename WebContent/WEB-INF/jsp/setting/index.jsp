<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="setting"/>
        <c:param name="javascript">

        </c:param>
    </c:import>
    <body>
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp"/>
                <div id="LayoutA" class="Layout">
                    <div id="Top">

                    </div>
                    <div id="Left">

                    </div>
                    <div id="Center">
                        <div id="settingForm"  class="table">
                        <div class="partsHeading"><h3>クイズメール送信設定</h3></div>
                        <c:url value="/setting" var="settingcontroller"/>
                        <form:form action="settingcontroller" method="post">
                            <strong>*</strong>は必須項目です。
                            <table>
                                <tr>
                                    <th colspan="2">Monday</th>
                                </tr>
                                <tr>
                                    <th width="40%">送信時間</th>
                                    <td><input name="stime1" size="5" id="stime1"/><form:errors cssClass="error" path="stime1"></form:errors></td>
                                </tr>
                                <tr>
                                    <th>都合いい時間</th>
                                    <td>
                                        <input name="speriodfrom1" size="5" id="speriodfrom1"/>&nbsp; &nbsp;<input name="speriodto1" size="5" id="speriodto1"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Tuesday</th>
                                    <td><input name="stime2" id="stime2"/><form:errors cssClass="error" path="stime2"/></td>
                                </tr>
                            </table>
                        </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
    </body>
</html>
