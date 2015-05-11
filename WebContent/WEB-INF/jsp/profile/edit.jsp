<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!doctype html>
<html>
<c:import url="../include/head.jsp">
	<c:param name="title" value="Profile" />
</c:import>

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
	$(document).ready(function() {

		document.getElementById("setting").className = "active";

	});
</script>
<body id="profile_edit">
	<div id="Body">
		<div id="Container">
			<c:import url="../include/header.jsp" />
			<div id="Contents">
				<div id="ContentsContainer">
					<c:import url="../include/profile_var.jsp"></c:import>
					<div id="localNav"></div>
					<!-- localNav -->
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
							</div>
							<!-- parts -->

						</div>
						<!-- Left -->

						<div id="Center">
							<div class="navbar navbar-inner" style="position: static;">
								<div class="navbar-primary">
									<h3
										style="font-size: 14px; font-weight: bolder; line-height: 150%">Edit
										profile</h3>
								</div>
							</div>
							<c:url value="/profile/edit" var="profileEditFormUrl" />
							<form:form modelAttribute="form" action="${profileEditFormUrl}"
								method="post">
								<input type="hidden" name="userid" value="${user.id}" />
								<strong>*</strong>&nbsp;Required。
                                            <table>
									<tr>
										<th><label for="pcEmail">PC E-mail:</label></th>
										<td>${user.pcEmail}</td>
									</tr>
									<tr>
										<th><label for="nickname">Nickname</label> <strong>*</strong></th>
										<td><form:input path="nickname" cssClass="input_text"
												id="nickname" style="height:30px" /> <form:errors
												path="nickname" cssClass="error" /></td>
									</tr>
									<tr>
										<th><label for="firstName">First name</label></th>
										<td><form:input path="firstName" style="height:30px" />
											<form:errors path="firstName" cssClass="error" /></td>
									</tr>
									<tr>
										<th><label for="lastName">Last name</label></th>
										<td><form:input path="lastName" style="height:30px" /> <form:errors
												path="lastName" cssClass="error" /></td>
									</tr>
									<tr>
										<th><label for="interesting">Interests</label></th>
										<td><form:textarea rows="2" cols="30" path="interesting"
												id="interesting" />
											<div>
												<form:errors path="interesting" cssClass="error" />
											</div></td>
									</tr>

									<c:forEach begin="0" end="1" var="myLangsIndex"
										varStatus="status">
										<tr>
											<th><label for="myLangs[${status.index}]">Native
													language&nbsp;${status.count}</label> <c:if
													test="${status.index eq 0}">
													<strong>*</strong>
												</c:if></th>
											<td><form:select path="myLangs[${myLangsIndex}]">
													<option value="">-Please select-</option>
													<form:options items="${langList}" itemValue="code"
														itemLabel="name" />
												</form:select> <form:errors path="myLangs[${myLangsIndex}]"
													cssClass="error" /></td>
										</tr>
									</c:forEach>


									<c:forEach begin="0" end="1" var="studyLangsIndex"
										varStatus="st">
										<tr>
											<th><label for="studyLangs[${st.index}]">Language
													of study&nbsp;${st.count}</label> <c:if test="${0 eq st.index}">
													<strong>*</strong>
												</c:if></th>
											<td><form:select path="studyLangs[${st.index}]">
													<option value="">-Please select-</option>
													<form:options items="${langList}" itemValue="code"
														itemLabel="name" />
												</form:select> <form:errors path="studyLangs[${st.index}]"
													cssClass="error" /></td>
										</tr>
									</c:forEach>

									<tr>
										<th><label for="age">Age</label></th>
										<td><form:select path="age">
												<option value="">-Please select-</option>
												<form:option value="10~20" label="10~20" />
												<form:option value="21~30" label="21~30" />
												<form:option value="31~40" label="31~40" />
												<form:option value="41~50" label="41~50" />
												<form:option value="51~60" label="51~60" />
											</form:select></td>
									</tr>
									<tr>
										<th><label for="gender">Gender</label></th>
										<td><form:select path="gender">
												<option value="">-Please select-</option>
												<form:option value="man" label="man" />
												<form:option value="woman" label="woman" />

											</form:select></td>
									</tr>

									<tr>
										<th><label for="nationality">Nationality</label></th>
										<td><form:select path="nationality">
												<option value="">-Please select-</option>
												<form:option value="Afrikaans" label="Afrikaans" />
												<form:option value="Amharic" label="Amharic" />
												<form:option value="Armenian" label="Armenian" />
												<form:option value="Basque" label="Basque" />
												<form:option value="Bengali" label="Bengali" />
												<form:option value="Bulgarian" label="Bulgarian" />
												<form:option value="Catalan" label="Catalan" />
												<form:option value="Cherokee" label="Cherokee" />
												<form:option value="Czech" label="Czech" />
												<form:option value="Dhivehi" label="Dhivehi" />
												<form:option value="estonian" label="estonian" />
												<form:option value="Finnish" label="Finnish" />
												<form:option value="Galician" label="Galician" />
												<form:option value="German" label="German" />
												<form:option value="Greek" label="Greek" />
												<form:option value="Gujarati" label="Gujarati" />

												<form:option value="Hindi" label="Hindi" />
												<form:option value="Indonesian" label="Indonesian" />
												<form:option value="Kazakh" label="Kazakh" />
												<form:option value="Korean" label="Korean" />
												<form:option value="Kurdish" label="Kurdish" />
												<form:option value="Laothian" label="Laothian" />
												<form:option value="Latvian" label="Latvian" />
												<form:option value="Lithuanian" label="Lithuanian" />
												<form:option value="Malayalam" label="Malayalam" />
												<form:option value="Marathi" label="Marathi" />
												<form:option value="Mongolian" label="Mongolian" />
												<form:option value="Norwegian" label="Norwegian" />
												<form:option value="Persian" label="Persian" />
												<form:option value="Portuguese" label="Portuguese" />
												<form:option value="Romanian" label="Romanian" />
												<form:option value="Sanskrit" label="Sanskrit" />

												<form:option value="Sindhi" label="Sindhi" />
												<form:option value="Slovenian" label="Slovenian" />
												<form:option value="Swahili" label="Swahili" />
												<form:option value="Swedish" label="Swedish" />
												<form:option value="Tamil" label="Tamil" />
												<form:option value="Thai" label="Thai" />
												<form:option value="Turkish" label="Turkish" />
												<form:option value="Urdu" label="Urdu" />
												<form:option value="Uighua" label="Uighua" />
												<form:option value="Welsh" label="Welsh" />
												<form:option value="Albanian" label="Albanian" />
												<form:option value="Arabic" label="Arabic" />
												<form:option value="Azerbaijani" label="Azerbaijani" />
												<form:option value="Belarusian" label="Belarusian" />
												<form:option value="Bihari" label="Bihari" />
												<form:option value="Burmese" label="Burmese" />

												<form:option value="Croatian" label="Croatian" />
												<form:option value="Danish" label="Danish" />
												<form:option value="Dutch" label="Dutch" />
												<form:option value="Esperanto" label="Esperanto" />
												<form:option value="Tagalog" label="Tagalog" />
												<form:option value="French" label="French" />
												<form:option value="Georgian" label="Georgian" />
												<form:option value="Guarani" label="Guarani" />
												<form:option value="Hebrew" label="Hebrew" />
												<form:option value="Hungarian" label="Hungarian" />
												<form:option value="Icelandic" label="Icelandic" />
												<form:option value="Inuktitut" label="Inuktitut" />
												<form:option value="Irish" label="Irish" />
												<form:option value="Italian" label="Italian" />
												<form:option value="Kannada" label="Kannada" />
												<form:option value="Khmer" label="Khmer" />

												<form:option value="Kyrgyz" label="Kyrgyz" />
												<form:option value="Macedonian" label="Macedonian" />
												<form:option value="Malay" label="Malay" />
												<form:option value="Maltese" label="Maltese" />
												<form:option value="Nepali" label="Nepali" />
												<form:option value="Oriya" label="Oriya" />
												<form:option value="Pashto" label="Pashto" />
												<form:option value="Polish" label="Polish" />
												<form:option value="Punjabi" label="Punjabi" />
												<form:option value="Russian" label="Russian" />
												<form:option value="Serbian" label="Serbian" />
												<form:option value="Sinhalese" label="Sinhalese" />
												<form:option value="Slovak" label="Slovak" />
												<form:option value="Spanish" label="Spanish" />
												<form:option value="Tajik" label="Tajik" />
												<form:option value="Telugu" label="Telugu" />


												<form:option value="Tibetan" label="Tibetan" />
												<form:option value="Ukranian" label="Ukranian" />
												<form:option value="Uzbek" label="Uzbek" />
												<form:option value="Vietnamese" label="Vietnamese" />
												<form:option value="Yiddish" label="Yiddish" />
												<form:option value="English" label="English" />
												<form:option value="Japanese" label="Japanese" />
												<form:option value="Chinese" label="Chinese" />


											</form:select></td>
									</tr>
										<tr>
										<th><label for="stay">Length of stay</label></th>
										<td><form:select path="stay">
												<option value="">-Please select-</option>
												<form:option value="1" label="1 years" />
												<form:option value="2" label="2 years" />
												<form:option value="3" label="3 years" />
												<form:option value="4" label="4 years" />
												<form:option value="5" label="5 years" />
												<form:option value="6" label="6 years" />
												<form:option value="7" label="7 years" />
												<form:option value="8" label="8 years" />
												<form:option value="9" label="9 years" />
												<form:option value="10" label="10 years" />
											</form:select></td>
									</tr>
									<tr>
										<th><label for="j_level">Japanese Language Proficiency Test</label></th>
										<td><form:select path="j_level">
												<option value="">-Please select-</option>
												<form:option value="N1" label="N1" />
												<form:option value="N2" label="N2" />
												<form:option value="N3" label="N3" />
												<form:option value="N4" label="N4" />
												<form:option value="N5" label="N5" />
												<form:option value="none" label="None" />
											</form:select></td>
									</tr>
								</table>
								<div class="operation">
									<ul class="moreInfo button">
										<li><center>
												<input type="submit" class="btn btn-block btn-primary"
													value="Update" style="width: 75px" />
											</center></li>
										<br>
										<li><a class="btn" href="<c:url value="/profile" />">Return</a>
										</li>
									</ul>
								</div>
							</form:form>
						</div>

					</div>
					<!-- Layout -->
					<div class="block"></div>
					<div id="sideBanner"></div>
					<!-- sideBanner -->

				</div>
				<!-- ContentsContainer -->
			</div>
			<!-- Contents -->
			<c:import url="../include/footer.jsp" />
		</div>
		<!-- Container -->
	</div>
	<!-- Body -->
</body>
</html>
