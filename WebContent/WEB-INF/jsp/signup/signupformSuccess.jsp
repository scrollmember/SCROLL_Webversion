<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Signup Success" />
        <c:param name="javascript">
            <script type="text/javascript">
                var i = 3;
                function onload(){
                    i--;
                    setTimeout("onload()", 1100);
                    if(i==0){
                        window.location = "<c:url value='/signin' />";
                    }else{
                        $("countNum").text(i);
                    }
                }
                $(function(){
                    onload();
                });
            </script>
        </c:param>
    </c:import>
    <body id="page_member_login">
        <div id="Body">
            <div id="Container">

                <div id="Header">
                    <div id="HeaderContainer">
                        <h1><a href="http://ll.is.tokushima-u.ac.jp">Signup Success</a></h1>
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
                        <div id="LayoutA" class="Layout">
                            <div id="Top">
                                <p>Signup Success</p>
                                <p>Redirect to login page after <span id="countNum"></span></p>
                            </div><!-- Top -->
                            <div id="Center">
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <%--
                        <div id="sideBanner">
                            <form action="/learning64u/web/member/changeLanguage" method="post"><label for="language_culture">言語</label>:
                                <select name="language[culture]" onchange="submit(this.form)" id="language_culture">
                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select><input value="member/home" type="hidden" name="language[next_uri]" id="language_next_uri" /></form>
                        </div><!-- sideBanner -->
                        --%>
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
