<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!doctype html>
<html>
<c:import url="../include/head.jsp">
	<c:param name="title" value="Profile" />
</c:import>
<link rel="stylesheet" href="${ctx}/learninglog/css/setting.css" />

<script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("setting").className = "active";
		document.getElementById("prof_setting").className = "active";
		document.getElementById("default__homepage").className = "";
	});
</script>
<body>

	<div id="Container">
		<c:import url="../include/header.jsp" />
		<div id="Contents">
			<div id="ContentsContainer">
				<c:import url="../include/profile_var.jsp"></c:import>

				<div id="LayoutF" class="Layout">
					<div id="Top">
						<div id="information_21" class="parts informationBox">
							<div class="body"></div>
						</div>
						<!-- parts -->
					</div>
					<!-- Top -->

					<div id="Center">


						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Expansion
									of the field of view</h3>
							</div>
						</div>
						<c:url value="/mysetting/setting" var="set" />
						<form:form commandName="setting" action="${set}" method="post"
							enctype="multipart/form-data">

							<div class="knowledgaelayout">Layout Setting</div>

							<div class="radioka" style="height: 20px;">
								Random
								<form:radiobutton name="q1" value="random"
									style="height: 20px; margin-top: -1px;" path="yifan" />
							</div>
							<div class="radioka" style="height: 20px;">
								Yifan-Hu
								<form:radiobutton name="q1" value="Yifan"
									style="height: 20px; margin-top: -1px;" path="yifan" />
							</div>
							<hr size="1" color="#ffc8c8" width="100%">
							<div class="knowledgaelayout" style="margin-top: 25px;">Filtering
								Setting</div>
							<div class="radioka" style="height: 20px;">Distance of the
								field of view</div>
							<form:select path="viewdistance" style="width:100px;margin-left:20px;margin-top:20px">
								<form:option value="1" label="1" />
								<form:option value="2" label="2" />
								<form:option value="3" label="3" />
								<form:option value="4" label="4" />
								<form:option value="5" label="5" />
							</form:select>
							<div class="radioka" style="height: 20px;">Enhancing the
								quality of your knowledge</div>
							<form:select path="kaquality" style="width:100px;margin-left:20px;margin-top:20px">
								<form:option value="1" label="1" />
								<form:option value="2" label="2" />
								<form:option value="3" label="3" />
								<form:option value="4" label="4" />
								<form:option value="5" label="5" />
							</form:select>
							</div>
							 <input type="submit" value="Setting" class="btn btn-info" style="width:100px;margin-left:20px;margin-top:20px"/>
						</form:form><br><br>
						<div id="Center">
						
					<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Group Setting</h3>
							</div>
						</div>
						<c:url value="/mysetting/group" var="set2" />
						<form:form commandName="setting" action="${set2}" method="post"
							enctype="multipart/form-data">
						<div class="radioka" style="height: 20px">Group Name<form:input path="groupname" style="height:30px;margin-left:10px;margin-top:5px"/></div>
						
						<hr size="1" color="#ffc8c8" style="width:100%;margin-top:40px">
						<table>
						<tr><th rowspan="4">Privacy</th><td width="200"><center><form:radiobutton name="q1" value="public"
									style="height: 20px; margin-top: -1px;" path="privacy" />Public</center></td></tr><tr><td width="200"><center>
									<form:radiobutton name="q1" value="private"
									style="height: 20px; margin-top: -1px;" path="privacy" />Private</center></td></tr>
						</table>
						
							</div>
							 <input type="submit" value="Add" class="btn btn-info" style="width:100px;margin-left:20px;margin-top:20px"/>
						</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>