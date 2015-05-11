<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Help" />
        <c:param name="css">
       	<style type="text/css">
        	.langTitle{
        		color: gray;
        		font-size: 24px;
        		font-family: arial,MS Gothic,simhei,sans-serif;
        		font-weight: bold;
        	}
        	#Top table{
        		font-size:20px;
        		margin: auto 50px;
        		color: green;
        	}
       	</style>
        </c:param>
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
                            	<div style="font-size:30px;font-family: arial;font-weight:bold;color:orange">
                            		<img src="<c:url value="/images/help_large.png" />" style="height:50px; weight:50px;" />
                            		<span style="vertical-align:0.5em">Learning Log</span>
                            	</div>
                            	<div>
                            		<table>
                            			<tr>
                            				<td colspan="3" class="langTitle">English</td>
                            			</tr>
                            			<tr>
                            				<th style="width:200px;">Manual</th>
                            				<td style="width:200px;">
                            					<a href="${baseURL}/resources/docs/manual_en.pdf" target="_blank"/><img src="${baseURL}/images/download_icon_48x48.png" /></a>
                            					<a href="${baseURL}/resources/docs/manual_en.pdf" target="_blank"/>Download</a>
                            				</td>
                            				<td style="width:200px;">
                            					<a href="http://docs.google.com/viewer?url=${baseURL}/resources/docs/manual_en.pdf" target="_blank"><img src="${baseURL}/images/online_icon_48x48.png" /></a>
                            					<a href="http://docs.google.com/viewer?url=${baseURL}/resources/docs/manual_en.pdf" target="_blank">Online</a>
                            				</td>
                            			</tr>
                            			<tr>
                            				<td colspan="3"><hr /></td>
                            			</tr>
                            			<tr>
                            				<td colspan="3" class="langTitle">日本語</td>
                            			</tr>
                            			<tr>
                            				<th>マニュアル</th>
                            				<td>
                            					<a href="${baseURL}/resources/docs/manual_jp.pdf" target="_blank"/><img src="${baseURL}/images/download_icon_48x48.png" /></a>
                            					<a href="${baseURL}/resources/docs/manual_jp.pdf" target="_blank"/>ダウンロード</a>
                            				</td>
                            				<td>
                            					<a href="http://docs.google.com/viewer?url=${baseURL}/resources/docs/manual_jp.pdf" target="_blank"><img src="${baseURL}/images/online_icon_48x48.png" /></a>
                            					<a href="http://docs.google.com/viewer?url=${baseURL}/resources/docs/manual_jp.pdf" target="_blank">オンライン</a>
                            				</td>
                            			</tr>
                            			<tr>
                            				<td colspan="3"><hr /></td>
                            			</tr>
                            			<tr>
                            				<td colspan="3" class="langTitle">简体中文</td>
                            			</tr>
                            			<tr>
                            				<th>使用手册</th>
                            				<td>
                            					<a href="${baseURL}/resources/docs/manual_zh.pdf" target="_blank"/><img src="${baseURL}/images/download_icon_48x48.png" /></a>
                            					<a href="${baseURL}/resources/docs/manual_zh.pdf" target="_blank"/>下载</a>
                            				</td>
                            				<td>
                            					<a href="http://docs.google.com/viewer?url=${baseURL}/resources/docs/manual_zh.pdf" target="_blank"><img src="${baseURL}/images/online_icon_48x48.png" /></a>
                            					<a href="http://docs.google.com/viewer?url=${baseURL}/resources/docs/manual_zh.pdf" target="_blank">在线阅读</a>
                            				</td>
                            			</tr>
                            		</table>
                            	</div>
                            </div><!-- Top -->
                            <div id="Center">
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                            <%--
                            <form action="#" method="get"><label for="language_culture">言語</label>:
                                <select name="siteLanguage" onchange="submit(this.form)" id="language_culture">
                                    <option value="en">English</option>
                                    <option value="ja_JP" selected="selected">日本語 (日本)</option>
                                </select>
                            </form>
                            --%>
                        </div><!-- sideBanner -->
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
