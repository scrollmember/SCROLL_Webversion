<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
        <c:param name="title" value="オブジェクト" />
        <c:param name="javascript">
            <script type="text/javascript" src="<c:url value="/js/wordmap/raphael-min.js"/>"></script>
            <script type="text/javascript" src="<c:url value="/js/wordmap/js-mindmap.js"/>"></script>
            <script type="text/javascript" src="<c:url value="/js/wordmap/wordnet.js"/>"></script>
        </c:param>
        <c:param name="css">
            <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/js/wordmap/wm_style.css"/>" />
        </c:param>
    </c:import>
    
    <body id="page_member_profile">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div class="Layout" id="WORDMAP">
                    	<div id="wm_root_word">${WordEntitle}</div>
                    	<div id="wm_system_url"><c:url value="/" /></div>
                    	<div id="wm_image_path">${staticserverUrl}/${projectName}/</div>
                    	<div id="wm_loading">loading...</div>
                    	<div id="wm_topbar">
                    		<span id="wm_topbar_wordnet">
                    		<a class="node wm_selected">all</a>
							<a class="node wm_unselected wm_hype">hype</a>
							<a class="node wm_unselected wm_syno">syno</a>
							<a class="node wm_unselected wm_hypo">hypo</a>
							</span>
						</div>
                    	<div id="wm_info">
                    	
                    	
                    	
                    	</div>
                    	<div id="wordarea"></div>
                   	</div>
                    <c:import url="../include/footer.jsp" />
                </div><!-- Container -->
            </div><!-- Body -->
        </div>
    </body>
</html>
