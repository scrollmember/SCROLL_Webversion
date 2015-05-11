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
            <script type="text/javascript" src="<c:url value="/js/wordmap/wm_item.js"/>"></script>
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
                    	<div id="wm_root_id">${ItemID}</div>
                    	<div id="wm_root_word">${ItemEntitle}</div>
                    	<div id="wm_system_url"><c:url value="/" /></div>
                    	<div id="wm_image_path">${staticserverUrl}/${projectName}/</div>
                    	<div id="wm_loading">loading...</div>
                    	<div id="wm_topbar">
                    		<span id="wm_topbar_item">
                    		<a class="node wm_selected">all</a>
							<a class="node wm_unselected wm_word">word</a>
							<a class="node wm_unselected wm_img">img</a>
							<a class="node wm_unselected wm_pos">pos</a>
							<a class="node wm_unselected wm_time">time</a>
							</span>
						</div>
                    	<div id="wm_info">
                    		<strong></strong><br />
                    		<img height="80px" alt="Image" src="<c:url value="/images/no_image.gif" />" /><br />
                    		<a href="" title="details">Details</a>
                    		<hr />
                    		Synset:
                    		<div id="wm_info_synset"></div>
                    	</div>
                    	<div id="wordarea"></div>
                   	</div>
                    <c:import url="../include/footer.jsp" />
                </div><!-- Container -->
            </div><!-- Body -->
        </div>
    </body>
</html>
