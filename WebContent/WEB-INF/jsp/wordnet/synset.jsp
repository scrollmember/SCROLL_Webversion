<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="Synset" />
        <c:param name="javascript">
        </c:param>
        <c:param name="css">
    
	    <script type="text/javascript">
	    $(document).ready(function() {
	    	var syno = $("span.synonym_list:first");
	    	var info = $("#synset_info");
	   		var text = syno.text().split("; ");
	   		syno.empty();
	   		$.each(text, function(i, val){
	   			if (val !== "") {
					$("<a/>").appendTo(syno).text(val).attr("href", "../"+val+"/").after(" ");
	   				info
	   				.append(val + ": <br />")
	   				.append(
	   					$("<a/>").text("View WordMap").attr("href", "../"+val+"/wordmap").after(" | ")
	   				).append(
	   					$("<a/>").text("Add Object").attr("href", "../../item/add/?langlist=en&en_title="+val)
	   				)
	   				.append("<hr />");
	   			}
	   			
	   		});
	    });
	    </script>
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
                        <div id="LayoutA" class="Layout">
                            <div id="Top">
                            </div><!-- Top -->
                            <div id="Left">
                            Synset: ${Synset}
                            <hr />
                            <div id="synset_info"></div>
                            </div>
                            <div id="Center">
                                <div id="profile" class="dparts searchResultList">
                                	<h2>Synonym</h2>
                      				<c:forEach var="synonym" items="${Synonym}">
                                   	<strong>${synonym.key}</strong>: <span class="synonym_list">${synonym.value}</span>
                         			<br />
                          			</c:forEach>
                                	<hr />
                                	
                                	<h2>Define</h2>
                      				<c:forEach var="synonymdef" items="${SynonymDef}">
                                	<strong>${synonymdef.key}</strong>: ${synonymdef.value}
                         			<br />
                          			</c:forEach>
                                	
                                	<hr />
                                	<h2>Link</h2>
                      				<c:forEach var="links" items="${Synlink}">
                                   	<strong>${links.key}</strong>: 
                                   		<c:forEach var="link" items="${links.value}">
                          				<a href="<c:url value="/wordnet/synset/${link.key}"></c:url>">${link.value}</a> 
                          				</c:forEach>
                         			<br />
                          			</c:forEach>
                                </div><!-- dparts -->
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
        </div>
    </body>
</html>