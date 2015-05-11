<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="inc/define.jsp" />
<!doctype html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>PACALL: Passive Capture for Learning Log</title>
        <link rel="stylesheet" href="${baseURL}/css/pacall.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="${baseURL}/js/folderupload.js"></script>
        <!-- Our CSS stylesheet file -->
        <!--[if lt IE 9]>
          <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <script>
        $(function(){
            $(document).on("click", ".uploadready", function(){
                startupload("${baseURL}/pacall/upload", {
                    progressUpdated: function(file){
                        if($.data(file) && $.data(file).find){
                            $.data(file).find('progress').attr("value", file.percentage);
                        }
                    },
                    uploadFinished:function(file){
                        if($.data(file) && $.data(file).addClass){
                            $.data(file).addClass('done');
                        }
                        var finished = true;
                        var numOfFinished = 0;
                        for(var i in files){
                            if(!files[i].percentage || files[i].percentage < 100){
                            	finished = false;
                            	break;
                            }else{
                            	numOfFinished++;
                            }
                        };
                        $("#uploadProgress").text(numOfFinished+"/"+files.length);
                        if(finished){
                            toggleStatus(3);
                        }
                    }
                });
            }).on("click", ".uploadfinish", function(){
            	location.href="${baseURL}/pacall";
            });
        });
        </script>
    </head>
    
    <body>
        <header>
            <h1>PACALL: Passive Capture for Learning Log</h1>
        </header>
        <div class="navigator">
            <ul id="filter">
                <li onclick="location.href='${baseURL}/pacall'" class="return">Return</li>
            </ul>
            <span class="pageList">
                <span onclick="changePage(parseInt(currentPage)-1)" class="pageNav">&lt;&lt;</span>
                <input class="jumpPage" type="range" min="1" />
                <span onclick="changePage(parseInt(currentPage)+1)" class="pageNav">&gt;&gt;</span>
                <span class="pageInfo"></span>
            </span>
            <div class="uploadStatus"><span id="uploadProgress" style="display: inline">0/0</span>&nbsp;<span class="uploadready">&rarr;Upload</span><span class="uploading">Uploading</span><span class="uploadfinish">&rarr;Finish</span></div>
        </div>
        <div id="dropbox">
            <span class="message">Drop SenseCam photo folder here to upload. <br /><i>Or click here to select a folder</i></span>
        </div>
        <footer>
            <h2>Passive Capture for Learning Log</h2>
            <a class="learninglog" target="_blank" href="http://ll.is.tokushima-u.ac.jp/">&rarr;Visit SCRO<span style="color:#da431c;">LL</span></a>
        </footer>
    </body>
</html>