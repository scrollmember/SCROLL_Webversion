<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!doctype html>
<html>
<c:import url="../include/head.jsp">
	<c:param name="title" value="Profile" />
</c:import>
<script type="text/javascript">
	$(document).ready(function() {

		document.getElementById("setting").className = "active";

	});
</script>
<style>
table {
	width: 600px;
	border-spacing: 0;
	font-size: 14px;
}

table th {
	color: #000;
	padding: 8px 15px;
	background: #eee;
	background: -moz-linear-gradient(#eee, #ddd 50%);
	background: -webkit-gradient(linear, 100% 100%, 100% 50%, from(#eee),
		to(#ddd) );
	font-weight: bold;
	border-top: 1px solid #aaa;
	border-bottom: 1px solid #aaa;
	line-height: 120%;
	text-align: center;
	text-shadow: 0 -1px 0 rgba(255, 255, 255, 0.9);
	box-shadow: 0px 1px 1px rgba(255, 255, 255, 0.3) inset;
}

table th:first-child {
	border-left: 1px solid #aaa;
	border-radius: 5px 0 0 0;
}

table th:last-child {
	border-radius: 0 5px 0 0;
	border-right: 1px solid #aaa;
	box-shadow: 2px 2px 1px rgba(0, 0, 0, 0.1);
}

table tr td {
	padding: 8px 15px;
	text-align: center;
}

table tr td:first-child {
	border-left: 1px solid #aaa;
}

table tr td:last-child {
	border-right: 1px solid #aaa;
	box-shadow: 2px 2px 1px rgba(0, 0, 0, 0.1);
}

table tr {
	background: #fff;
}

table tr:nth-child(2n+1) {
	background: #f5f5f5;
}

table tr:last-child td {
	border-bottom: 1px solid #aaa;
	box-shadow: 2px 2px 1px rgba(0, 0, 0, 0.1);
}

table tr:last-child td:first-child {
	border-radius: 0 0 0 5px;
}

table tr:last-child td:last-child {
	border-radius: 0 0 5px 0;
}

table tr:hover {
	background: #eee;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	//var gname;
	//var gauthor;
	var globalauthor;
	var globalgroupname;
	var groupid;
	function onJoin(id, n, group) {
		globalauthor = "";
		globalauthor = n;
		groupid = group;
		globalgroupname = "";
		globalgroupname = id;
		//gname=id;
		//gauthor=n;
		//alert(id);
		//var text=document.createTextNode(id);
		//gname.document.value="test"
		//gname.appendChild(text);
		document.getElementById("gname").innerHTML = id;
		//getauthor
		<c:url value="/mysetting/getauthor" var="group" />
		$.ajax({
			type : "POST",
			url : "${group}",
			data : {
				groupid : groupid
			},
			success : function(msg) {

			},
			error : function() {
				alert("failure");
			}
		});
	}

	$(function() {
		<c:url value="/mysetting/groupadd" var="group" />
		$("button#submit").click(function() {
			$.ajax({
				type : "POST",
				url : "${group}",
				data : {
					authorid : globalauthor,
					groupname : globalgroupname,
					groupid : groupid
				},
				success : function(msg) {

				},
				error : function() {
					alert("failure");
				}
			});

		});
		<c:url value="/mysetting/groupdelete" var="groupdelete" />
		$("button#delete").click(function() {
			$.ajax({
				type : "POST",
				url : "${groupdelete}",
				data : {
					authorid : globalauthor,
					groupname : globalgroupname,
					groupid : groupid
				},
				success : function(msg) {

				},
				error : function() {
					alert("failure");
				}
			});

		});

	});
</script>
<body id="page_member_home">
	<div id="Body">
		<div id="Container">
			<c:import url="../include/header.jsp" />
			<div id="Contents">
				<div id="ContentsContainer">
					<c:import url="../include/profile_var.jsp">
					</c:import>
					<div id="LayoutA" class="Layout">
						<div id="Top">
							<div id="information_21" class="parts informationBox">
								<div class="body"></div>
							</div>
							<!-- parts -->
						</div>
						<!-- Top -->
						<div id="Left">
							<div id="memberImageBox_22" class="parts memberImageBox">

								<p class="photo">
									<c:choose>
										<c:when test="${empty user.avatar}">
											<img alt="LearningUser"
												src="<c:url value="/images/no_image.gif" />" height="180"
												width="180" />
										</c:when>
										<c:otherwise>
											<img alt="LearningUser"
												src="<c:url value="${staticserverUrl}/${projectName}/${user.avatar.id}_320x240.png" />"
												height="180" />
										</c:otherwise>
									</c:choose>
								</p>
								<p class="text">
									<shiro:principal property="nickname" />
								</p>
								<p class="text">Level : ${user.userLevel}</p>
								<p class="text">EXP : ${nowExperiencePoint} / Next :
									${nextExperiencePoint}</p>

								<div class="moreInfo">
									<ul class="moreInfo">
										<li><a href=" <c:url value="/profile/avataredit"/>">Edit
												Photo</a></li>
										</ul>
										
								</div>
								<ul style="margin-top:10px;margin-right:20px"><b>Your group :</b> <c:forEach items="${groupname}" var="group"
												varStatus="status">
													<li>${group.groupname}</li>
											</c:forEach>
									</ul>
							</div>
							<div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Group</h3>
								</div>
							</div>

							<c:forEach items="${groupdata}" var="group" varStatus="status">

								<a href=""
									onClick="onJoin('${group.groupname}','${group.author}','${group.id}')"
									style="margin-left: 30px" data-toggle="modal"
									data-target="#myModal">${group.groupname}</a>

								<br>



							</c:forEach>
							<br>
							<br>
							<br>
							<!-- parts -->
						</div>
						<!-- Left -->
						<div id="Center">
							<div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Profile</h3>
									&nbsp;<a href="<c:url value="/profile/edit" />">Edit</a>
								</div>
							</div>

							<c:url value="/signup/${user.activecode}" var="signupFormUrl" />

							<table>
								<tr valign="middle">
									<th><label for="pcEmail">Email</label></th>
									<td>${user.pcEmail}</td>
								</tr>
								<tr>
									<th><label for="nickname">Nickname</label></th>
									<td>${user.nickname}</td>
								</tr>
								<tr>
									<th><label for="firstName">Family Name</label></th>
									<td>${user.firstName}</td>
								</tr>
								<tr>
									<th><label for="lastName">Given Name</label></th>
									<td>${user.lastName}</td>
								</tr>
								<tr>
									<th><label for="interesting">Interests</label></th>
									<td>${form.interesting}</td>
								</tr>
								<c:forEach items="${user.myLangs}" var="myLan" varStatus="lan">
									<tr>
										<th>Native language(${lan.count})</th>
										<td>${myLan.name}</td>
									</tr>
								</c:forEach>
								<c:forEach items="${user.studyLangs}" var="slan"
									varStatus="status">
									<tr>
										<th><label>Language of study(${status.count})</label></th>
										<td>${slan.name}</td>
									</tr>
								</c:forEach>
								<tr>
									<th><label>Categories</label></th>
									<td>
										<ul>
											<c:forEach items="${user.myCategoryList}" var="cat">
												<li
													<c:if test="${user.defaultCategory!=null && user.defaultCategory.id==cat.id}">style="font-weight:bold"</c:if>><center>${cat.name}</center></li>
											</c:forEach>
										</ul>
									</td>
								</tr>
								<tr>
									<th><label for="age">Age</label></th>
									<td>${ability.age}</td>
								</tr>
								<tr>
									<th><label for="gender">Gender</label></th>
									<td>${ability.gender}</td>
								</tr>
								<tr>
									<th><label for="nationality">Nationality</label></th>
									<td>${ability.nationality}</td>
								</tr>
								<tr>
									<th><label for="stay">Length of stay</label></th>
									<td>${ability.stay} years</td>
								</tr>
								
								<tr>
									<th><label for="j_level">Japanese Language Proficiecy Test</label></th>
									<td>${ability.j_level}</td>
								</tr>
								
							</table>
							<br>
							<c:if test="${!empty kadata.authorid}">
								<div class="navbar navbar-inner" style="position: static;">
									<div class="navbar-primary">
										<h3
											style="font-size: 14px; font-weight: bolder; line-height: 150%">Your
											vision</h3>
									</div>
								</div>

								<table>
									<tr valign="middle">
										<th><label for="pcEmail">Layout</label></th>
										<td>${kadata.layout}</td>
									</tr>
									<tr>
										<th><label for="nickname">Distance</label></th>
										<td>${kadata.viewdistance}</td>
									</tr>
									<tr>
										<th><label for="firstName">Quality</label></th>
										<td>${kadata.kaquality}</td>
									</tr>

								</table>

								<br>
								<br>
								<br>
								<br>
								<br>
								<br>
							</c:if>
						</div>
						<!-- parts -->




					</div>
					<!-- Center -->
				</div>


				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel" aria-hidden="true"
					style="height: 200px; width: 610px">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">
									<div id="gname"></div>
								</h4>
							</div>
							<div>
							<c:forEach items="${authorgroup}" var="group" varStatus="status">

								${group.nickname}

								<br>
							</c:forEach>
							</div>
							<div class="modal-body">
								<form class="contact">
									<button value="Join" class="btn btn-info" data-dismiss="modal"
										id="submit">Join</button>
									<button type="submit" class="btn btn-primary" value="Not Join"
										data-dismiss="modal" id="delete">Delete Join</button>
										<button type="submit" class="btn btn-primary" value="Not Join"
										data-dismiss="modal">Not Join</button>
								</form>

							</div>

						</div>
					</div>
				</div>

				<!-- Layout -->
				<div id="sideBanner"></div>
				<!-- ContentsContainer -->
			</div>
			<!-- Contents -->
			<c:import url="../include/footer.jsp" />
		</div>
	</div>
	<!-- Container -->
	</div>
	<!-- Body -->
</body>
</html>
