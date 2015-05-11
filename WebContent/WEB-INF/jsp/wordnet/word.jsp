<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Word" />
        <c:param name="javascript">
        </c:param>
        <c:param name="css">
        </c:param>
    </c:import>
    <body id="page_member_profile">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="localNav">
                        </div><!-- localNav -->
                        <div class="Layout">
                            <div id="Top">
                            ${Word}
                            </div><!-- Top -->
                           	<table>
                               	<tr>
                                   	<th style="width:70px;">Synset</th>
                                   	<th>Define(eng)</th>
                                  	<th>Define(jpn)</th>
                               	</tr>
                      				<c:forEach var="syns" items="${Synset}">
                               	<tr>
                                   	<td style="width:70px;"><a href="<c:url value="/wordnet/synset/${syns.key}"></c:url>">${syns.key}</a></td>
                                   	<c:forEach var="syn" items="${syns.value}">
                          				<td>${syn.value}</td>
                          				</c:forEach>
                            		</tr>
                          			</c:forEach>
                            </table>
                            <div class="block">
                            </div>
                            <div id="sideBanner">
                            </div><!-- sideBanner -->
                        </div><!-- ContentsContainer -->
                    </div><!-- Contents -->
                    <c:import url="../include/footer.jsp" />
                </div><!-- Container -->
            </div><!-- Body -->
        </div>
    </body>
</html>