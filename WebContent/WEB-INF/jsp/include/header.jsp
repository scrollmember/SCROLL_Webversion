<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<head>


<link rel="stylesheet"
	href="${ctx}/learninglog/js/bootstrap/css/bootstrap.css" />
<link rel="stylesheet"
	href="${ctx}/learninglog/js/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${ctx}/learninglog/js/bootstrap/css/bootstrap-responsive.css" />


<link href="${ctx}/learninglog/js/bootstrap/css/navbar.css"
	rel="stylesheet">
<!--
   <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery/jquery.contextMenu.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery/jquery.form.js"/>"></script>
  -->

<script src="<c:url value="/js/bootstrap/js/bootstrap.min.js"/>"></script>
<!--   <script src="<c:url value="/js/sidrmenu/jquery.sidr.min.js"/>"></script>-->
<script>
	$(document).ready(function() {
		//$('#simple-menu').sidr();
	});
</script>

</head>


<div class="container" style="margin-left: 70px; width: 80%">

	<!-- Static navbar -->
	<div class="navbar navbar-inverse" role="navigation">
		<div class="navbar-collapse collapse" style="witdh: 80%">
			<ul class="nav navbar-nav">
				<li id="simple-menu"
					class="<c:if test="${params.currentModule=='home'}">currentModule</c:if>"><a
					href="#sidr"><i class="icon-align-justify"></i></a></li>
				<li id="homework"
					class="<c:if test="${params.currentModule=='home'}">currentModule</c:if>"><a
					href="<c:url value="/" />"><i class="icon-home"></i>Home</a></li>
				<li id="mylog"
					class="<c:if test="${params.currentModule=='myhome'}">currentModule</c:if>"><a
					href="<c:url value="/member" />"><i class="icon-file"></i>My
						Logs</a></li>
				<li id="alllog"
					class="<c:if test="${params.currentModule=='item'}">currentModule</c:if>"><a
					href="<c:url value="/item" />"><i class="icon-folder-open"></i>All
						Logs</a></li>
				<li id="quiz"
					class="<c:if test="${params.currentModule=='forum'}">currentModule</c:if>"><a
					href="<c:url value="/quiz/menu" />"><i class="icon-pencil"></i>Quiz</a></li>
				<li id="Analysis"><a href="<c:url value="/analysis" />"><i
						class="icon-signal"></i>Analysis</a></li>
				<!-- <li id="task"
									class="<c:if test="${params.currentModule=='task'}">currentModule</c:if>"><a
									href="./window.html"
									onclick="window.open('http://ll.is.tokushima-u.ac.jp/learninglog2/task/taskitem', '', 'width=1300,height=1500'); return false;"><i
										class="icon-globe"></i>Task</a></li> -->
				<li id="message"
					class="<c:if test="${params.currentModule=='usermessage'}">currentModule</c:if>"><a
					href="<c:url value="/usermessage" />"><i class="icon-gift"></i>Message<span
						id="msgUnreadCount"></span></a></li>
				<!-- <li id="dashboard"	
									class="<c:if test="${params.currentModule=='dashboard'}">currentModule</c:if>"><a
									href="<c:url value="/dashboard" />"><i class="icon-gift"></i>Dashboard</a></li> -->

				<li id="mystatus"
					class="<c:if test="${params.currentModule=='mystatus'}">currentModule</c:if>"><a
					href="<c:url value="/status" />"><i class="icon-bell"></i>My
						Status</a></li>
				<shiro:hasRole name="admin">

					<li id="admin"
						class="<c:if test="${params.currentModule=='admin'}">currentModule</c:if>"><a
						href="<c:url value="/admin" />"><i class=""></i>Admin</a></li>
				</shiro:hasRole>
				<li id="setting"
					class="<c:if test="${params.currentModule=='profile'}">currentModule</c:if>"><a
					href="<c:url value="/profile" />"><i class="icon-user"></i>Setting</a></li>
				<li><a href="<c:url value="/logout" />"><i class="icon-off"></i>Logout</a></li>

			</ul>
			<!-- 
          <ul class="nav navbar-nav navbar-right">
            <li class="active"><a href="./">Default</a></li>
            <li id="system"><a href="../navbar-static-top/">Static top</a></li>
            <li><a href="../navbar-fixed-top/">Fixed top</a></li>
          </ul> -->
		</div>
		<!--/.nav-collapse -->
	</div>
</div>

<!-- /container -->




<!--
<nav class="navbar navbar-default" role="navigation">
		<div class="navbar navbar-inverse">
			<div class="navbar-inner">
				
					<button type="button" class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse">
						<span class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<div class="nav-collapse collapse" style="height: 80px">
						
						<ul class="nav">
						
                         <li id="simple-menu"
								class="<c:if test="${params.currentModule=='home'}">currentModule</c:if>"><a
								href="#sidr"><i class="icon-align-justify"></i>Menu</a></li>

							<li id="homework"
								class="<c:if test="${params.currentModule=='home'}">currentModule</c:if>"><a
								href="<c:url value="/" />"><i class="icon-home"></i>Home</a></li>
							<li id="mylog"
								class="<c:if test="${params.currentModule=='myhome'}">currentModule</c:if>"><a
								href="<c:url value="/member" />"><i class="icon-file"></i>My Logs</a></li>
							<li id="alllog"
								class="<c:if test="${params.currentModule=='item'}">currentModule</c:if>"><a
								href="<c:url value="/item" />"><i class="icon-folder-open"></i>All Logs</a></li>
							<li id="quiz"
								class="<c:if test="${params.currentModule=='forum'}">currentModule</c:if>"><a
								href="<c:url value="/quiz" />"><i class="icon-pencil"></i>Quiz</a></li>
							<!--<li id="task"
								class="<c:if test="${params.currentModule=='task'}">currentModule</c:if>"><a
								href="./window.html"
								onclick="window.open('http://ll.is.tokushima-u.ac.jp/learninglog2/task/taskitem', '', 'width=1300,height=1500'); return false;"><i class="icon-globe"></i>Task</a></li>
							 <li id="message"
								class="<c:if test="${params.currentModule=='usermessage'}">currentModule</c:if>"><a
								href="<c:url value="/usermessage" />"><i class="icon-gift"></i>Message<span
									id="msgUnreadCount"></span></a></li>-->
<!--  
							<li id="mystatus"
								class="<c:if test="${params.currentModule=='mystatus'}">currentModule</c:if>"><a
								href="<c:url value="/status" />"><i class="icon-bell"></i>My Status</a></li>
							<shiro:hasRole name="admin">
								<li id="admin"
									class="<c:if test="${params.currentModule=='admin'}">currentModule</c:if>"><a
									href="<c:url value="/admin" />"><i class=""></i>Admin</a></li>
							</shiro:hasRole>
							<li id="setting"
								class="<c:if test="${params.currentModule=='profile'}">currentModule</c:if>"><a
								href="<c:url value="/profile" />"><i class="icon-user"></i>Setting</a></li>
							<li><a href="<c:url value="/logout" />"><i class="icon-off"></i>Logout</a></li>-->
<!-- 	</ul>
					</div>-->
<!-- globalNav -->




<!-- Header -->

<!--  <div id="topBanner"></div>
		-->

