<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Change Password" />
    </c:import>
        <style>
table {
  width: 600px;
  border-spacing: 0;
  font-size:14px;
}
table th {
  color: #000;

  padding: 8px 15px;

  background: #eee;

  background:-moz-linear-gradient(#eee, #ddd 50%);

  background:-webkit-gradient(linear, 100% 100%, 100% 50%, from(#eee), to(#ddd));

  font-weight: bold;

  border-top:1px solid #aaa;

  border-bottom:1px solid #aaa;

  line-height: 120%;

  text-align: center;

  text-shadow:0 -1px 0 rgba(255,255,255,0.9);

  box-shadow: 0px 1px 1px rgba(255,255,255,0.3) inset;

}

table th:first-child {

  border-left:1px solid #aaa;

  border-radius: 5px 0 0 0;

}

table th:last-child {

  border-radius:0 5px 0 0;

  border-right:1px solid #aaa;

  box-shadow: 2px 2px 1px rgba(0,0,0,0.1);

}

table tr td {
  padding: 8px 15px;

  text-align: center;

}

table tr td:first-child {

  border-left: 1px solid #aaa;
}

table tr td:last-child {

  border-right: 1px solid #aaa;

  box-shadow: 2px 2px 1px rgba(0,0,0,0.1);

}

table tr {

  background: #fff;

}

table tr:nth-child(2n+1) {

  background: #f5f5f5;

}

table tr:last-child td {

  border-bottom:1px solid #aaa;

  box-shadow: 2px 2px 1px rgba(0,0,0,0.1);

}

table tr:last-child td:first-child {

  border-radius: 0 0 0 5px;

}

table tr:last-child td:last-child {

  border-radius: 0 0 5px 0;

}

table tr:hover {

  background: #eee;

  cursor:pointer;

}

</style>
         <script type="text/javascript">
    $(document).ready(function() {

            		document.getElementById("default_settings").className = "";
            		document.getElementById("default__homepage").className = "";
            		document.getElementById("default_category").className = "";
            		document.getElementById("default_password").className = "active";
            		
            		
            	});
    </script>
    <body id="profile_edit">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                    	<c:import url="../include/profile_var.jsp"></c:import>
                        <div id="localNav">
                        </div><!-- localNav -->
                        <div id="LayoutA" class="Layout">
                            <div id="Top">
                            </div><!-- Top -->
                            <div id="Left">
                                <div id="memberImageBox_22" class="parts memberImageBox">
                                    <p class="photo">
                                        <img alt="LearningUser" src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />" width="180" /></p>
                                    <p class="text"><shiro:principal property="nickname" /></p>
                                    <p class="text">Level : ${user.userLevel}</p>
                                    <p class="text">EXP : ${nowExperiencePoint} / Next : ${nextExperiencePoint}</p>

                                    <div class="moreInfo">
                                        <ul class="moreInfo">
                                            <li><a href=" <c:url value="/profile/avataredit"/>">Edit photo</a></li>
                                        </ul>
                                    </div>
                                </div><!-- parts -->
                            </div><!-- Left -->
                             <c:url value="/profile/changepassword" var="profileEditFormUrl" />
                             <form:form modelAttribute="form" action="${profileEditFormUrl}" method="post">
                                 <div id="Center">
                                       <div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary"><h3 style="font-size: 14px; font-weight: bolder; line-height: 150%">Change password</h3></div></div>
                                 <input type="hidden" name="userid" value="${user.id}"/>
                                            <table>
                                                <tr>
                                                    <th><label for="password">Current password</label></th>
                                                    <td>
                                                        <form:password path="oldpassword" style="height:30px"/>
                                                        <form:errors path="oldpassword" cssClass="error" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="password">New password</label></th>
                                                    <td>
                                                        <form:password path="password" style="height:30px"/>
                                                        <form:errors path="password" cssClass="error" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th><label for="passwordConfirm">New password(Again)</label></th>
                                                    <td>
                                                        <form:password path="passwordConfirm" style="height:30px"/>
                                                        <form:errors path="passwordConfirm" cssClass="error" />
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                    <li>
                                                        <input type="submit" class="btn btn btn-primary" value="Update" />
                                                    </li>
                                                </ul>
                                            </div>
                                  </div>
                                        </form:form>
                        </div><!-- Layout -->
                        <div class="block">
                        </div>
                        <div id="sideBanner">
                        </div><!-- sideBanner -->

                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
