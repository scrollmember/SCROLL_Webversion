<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
    <c:import url="../include/head.jsp">
    <c:param name="title" value="Timemap - My Logs" />
    <c:param name="content">
        <script src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script src="${baseURL}/js/timemap/lib/mxn/mxn.js?(googlev3)"></script>
        <script src="${baseURL}/js/timemap/lib/timeline-1.2.js"></script>
        <script src="${baseURL}/js/timemap/timemap_full.pack.js"></script>
        <style>
        div, p {
font-family: Verdana, Arial, sans-serif;
}

p.content {
font-size: 12px;
width: 30em;
}

div#help {
font-size: 12px;
width: 45em;
padding: 1em;
}

div#timemap {
padding: 1em;
}

div#timelinecontainer{
width: 100%;
height: 200px;
}

div#timeline{
 width: 100%;
 height: 100%;
 font-size: 12px;
 background: #CCCCCC;
}

div#mapcontainer {
 width: 100%;
 height: 400px;
}

div#map {
 width: 100%;
 height: 100%;
 background: #EEEEEE;
}

div.infotitle {
    font-size: 14px;
    font-weight: bold;
}
div.infodescription {
    font-size: 14px;
    font-style: italic;
}

div.custominfostyle {
    font-family: Georgia, Garamond, serif;
    font-size: 1.5em;
    font-style: italic;
    width: 20em;
}
        
        </style>
        <script>
            var tm;
            $(function(){
                tm = TimeMap.init({
                    mapId: "map",               // Id of map div element (required)
                    timelineId: "timeline",     // Id of timeline div element (required)
                    options: {
                        eventIconPath: "${baseURL}/js/timemap/images/"
                    }, 
                    datasets: [
                        {
                            id: "llog",
                            title: "Llog",
                            theme: "orange",
                            type: "json_string",
                            options: {
                                url: "${baseURL}/timemap/mylogs?format=json"
                            }
                        }
                    ],
                    bandIntervals:[
                        Timeline.DateTime.WEEK,
                        Timeline.DateTime.MONTH
                    ]
                });
                // add our new function to the map and timeline filters
                //tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
                //tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
                tm.scrollToDate(Date(), false, true);
            });
            
            // filter function for tags
            /*
            TimeMap.filters.hasSelectedTag = function(item) {
                // if no tag was selected, every item should pass the filter
                if (!window.selectedTag) {
                    return true;
                }
                if (item.opts.tags) {
                    // look for selected tag
                    if (item.opts.tags.indexOf(window.selectedTag) >= 0) {
                        // tag found, item passes
                        return true;
                    } 
                    else {
                        // indexOf() returned -1, so the tag wasn't found
                        return false;
                    }
                }
                else {
                    // item didn't have any tags
                    return false;
                }
            };
            // onChange handler for pulldown menu
            function setSelectedTag(select) {
                var idx = select.selectedIndex;
                window.selectedTag = select.options[idx].value;
                // run filters
                tm.filter('map');
                tm.filter('timeline');
            }
            */
        </script>
        <style>
            div#timelinecontainer{ height: 200px;}
            div#mapcontainer{ height: 400px;}
        </style>
        </c:param>
    </c:import>

    <body id="page_member_home">
        <div id="Body">
            <div id="Container">
                <c:import url="../include/header.jsp" />

                <div id="Contents">
                            <div id="timemap">
                                <div id="timelinecontainer">
                                  <div id="timeline"></div>
                                </div>
                                <div id="mapcontainer">
                                  <div id="map"></div>
                                </div>
                            </div>
                    <div id="ContentsContainer" style="font-size: 24px;">
                          <%--
                                          <form>
                            <select onchange="setSelectedTag(this);">
                                <option value="">All</option>
                                <option value="kitchen">kitchen</option>
                                <option value="communication">communication</option>
                             </select>
                        </form>
                --%>
                    </div><!-- Contents -->
                    <c:import url="../include/footer.jsp" />
                </div>
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
