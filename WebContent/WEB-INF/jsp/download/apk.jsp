<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Download LearningLog for Android" />
    </c:import>
    <body id="page_member_login">
        <div id="Body">
            <div id="Container">
                <div id="Header">
                    <div id="HeaderContainer">
                        <h1><a href="http://ll.is.tokushima-u.ac.jp">Learning Log</a></h1>
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
                                <h2 style="font-weight: bold; font-size: 30px;">Download LearningLog for Android</h2>
                            	<p style="font-size: 20px;word-wrap: normal;word-break: normal">
                            	Those applications are free and available at Google Play.
                            	</p>
                            </div><!-- Top -->
                            <div id="Center" style="text-align:center; margin: 25px 0px; border: 1px dotted #FF8F00;border-radius: 10px;padding:10px;">
                            	<h3 style="font-family: cursive,serif; font-size: 24px;">LearningLog Client for Android</h3>
                            	
                                <c:set var="apkUrl" value="http://chart.apis.google.com/chart?cht=qr&chs=320x320&chl=${urlApkLearningLog}"></c:set>
                            	<img alt="" src="${apkUrl}" /><br />
                            	<!--                             	
                            	<a href="http://play.google.com/store/apps/details?id=jp.ac.tokushima_u.is.ll"><img 
                                alt="Get it on Google Play" src="http://ll.is.tokushima-u.ac.jp/learninglog/images/get_it_on_play_logo.png" /></a><br />
								-->                            	
                            	<h3 style="font-family: cursive,serif; font-size: 24px;"><a href="${urlApkLearningLog}" target="_blank">Download Apk from Server</a></h3>
				
                                
                            	<a href="${urlApkLearningLog}" target="_blank"><img alt="" src="http://chart.apis.google.com/chart?cht=qr&chs=320x320&chl=http://ll.is.tokushima-u.ac.jp/learninglog/apk/ALearnLog.apk}" /></a><br />
					
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <!--
                            <form action="#" method="get"><label for="language_culture">言語</label>:
                                <select name="siteLanguage" onchange="submit(this.form)" id="language_culture">
                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select>
                            </form>
                            -->
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
