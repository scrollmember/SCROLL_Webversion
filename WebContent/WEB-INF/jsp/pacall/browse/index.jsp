<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:import url="../inc/define.jsp" />
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7, IE=9" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/flick/jquery-ui-1.8.13.custom.css" />" />
    <link rel="stylesheet" href="${baseURL}/css/pacall.css" />
<title>Browse</title>
<script>
	$(function() {
		$(".typeButton").click(function(e){
			location.href="<c:url value="/pacall/browse" />?id=${folder.id}&type="+$(this).attr("data-type");
		});
	  $(".jumpPage").on({
	        mouseup: function(e){
	            changePage($(".jumpPage").val());
	        },
	        change: function(e){
	            var totalPage = $(".jumpPage").attr("max");
	            $(".pageInfo").text($(".jumpPage").val()+"/"+totalPage);
	        }
	    });
	});
	function changePage(page){
		page = parseInt(page);
		var current = ${sensePicPage.thisPageNumber};
		var min = parseInt($(".jumpPage").attr("min"));
		var max = parseInt($(".jumpPage").attr("max"));
		if(page<min) page = min;
		if(page>max) page = max;
		if(page == current) return;
		location.href="${baseURL}/pacall/browse?id=${folder.id}&type=${type}&page="+page;
	}
</script>
</head>
<body>
    <header>
        <h1>PACALL - Passive Capture for Learning Log</h1>
    </header>
        <div class="navigator">
            <ul id="filter">
                <li onclick="location.href='<c:url value="/pacall/folder" />'" class="return">Return</li>
                <li class="typeButton <c:if test='${type==null}'>active</c:if>" data-type="all">All(${typeCount['all']})</li>
                <li class="typeButton <c:if test='${type=="manual"}'>active</c:if>" data-type="manual">Manual(${typeCount['manual']})</li>
                <li class="typeButton <c:if test='${type=="normal"}'>active</c:if>" data-type="normal">Normal(${typeCount['normal']})</li>
                <li class="typeButton <c:if test='${type=="duplicate"}'>active</c:if>" data-type="duplicate">Duplicate(${typeCount['duplicate']})</li>
                <li class="typeButton <c:if test='${type=="shake"}'>active</c:if>" data-type="shake">Shake(${typeCount['shake']})</li>
                <li class="typeButton <c:if test='${type=="dark"}'>active</c:if>" data-type="dark">Dark(${typeCount['dark']})</li>
            </ul>
            </div>
            <div>
                
            </div>
            <div class="clear"><br/></div>
            <div class="pageList" style="display: block">
                <span onclick="changePage(parseInt(${sensePicPage.thisPageNumber})-1)" class="pageNav">&lt;&lt;</span>
                <input class="jumpPage" type="range" min="1" max="${sensePicPage.lastPageNumber}" value="${sensePicPage.thisPageNumber}" />
                <span onclick="changePage(parseInt(${sensePicPage.thisPageNumber})+1)" class="pageNav">&gt;&gt;</span>
                <span class="pageInfo">${sensePicPage.thisPageNumber}/${sensePicPage.lastPageNumber}</span>
                <form style="display:inline" action="<c:url value="/pacall/browse"/>" method="get">
                <input type="hidden" name="id" value="${folder.id}" />
                <input type="hidden" name="type" value="${type}" />
                <input type="hidden" name="page" value="${page}" />
                &nbsp;&nbsp;Lines per page: <input type="text" name="lines" size="3" maxlength="3" value="${lines}" /><input type="submit" value="Change" />
                </form>
            </div>

        <div id="dropbox">
            <c:forEach items="${sensePicPage.result}" var="sensePic">
            <div class="preview">
                <span class="imageHolder">
                    <a rel="gal" target="_blank" class="gallery" href="<c:url value="/pacall/viewpic"><c:param name="id" value="${sensePic.id}"/></c:url>">
                            <img src="${baseURL}/pacall/showpic/${sensePic.id}_240_180.jpg" width="240px" height="180px" />
                    </a>
                </span>
                <div class="fileinfo">
                    <span class="help">${sensePic.name}
                        <c:forEach begin="1" end="${similarityMap[sensePic.id]}">★</c:forEach>
                        <c:if test="${sensePic.samePicId!=null && sensePic.samePicId!=''}">(${sensePic.samePicId})
                        </c:if><br/><fmt:formatDate value="${sensePic.date}" type="both" pattern="yyyy/MM/dd HH:mm:ss" /></span><div class="mask"></div>
                </div>
            </div>
            </c:forEach>
		</div>
        <footer>
            <h2>Passive Capture for Learning Log</h2>
            <a class="learninglog" target="_blank" href="http://ll.is.tokushima-u.ac.jp/">&rarr;Visit SCRO<span style="color:#da431c;">LL</span></a>
        </footer>
</body>
</html>