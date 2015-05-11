<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	</head>
	<body>
--------------------------------------------------------------------<br />
<b>Learning Log Application Updates                 ${sendDate}</b><br />
--------------------------------------------------------------------<br />
Please input your learning log:<br />
<ul>
	<li><a href="${newObjectPageLink}">What did you learn this week?</a> </li>
	<li><a href="${newObjectPageLink}">What questions did you have this week?</a> </li>
	<li><a href="${quizPageLink}">Do you want to learn from Quiz?</a> </li>
</ul>
<br />
<#if oneMonthList?size!=0>
From your past learning log:<br />
One month ago: <br />
<ul>
	<#list oneMonthList as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>
<#if twoMonthsList?size!=0>
Two month ago: <br />
<ul>
	<#list twoMonthsList as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>
<#if threeMonthsList?size!=0>
Three month ago: <br />
<ul>
	<#list threeMonthsList as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>
<#if fourMonthsList?size!=0>
Your past Learning Log: <br />
<ul>
	<#list fourMonthsList as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>
<#if thisWeek?size!=0>
New entries in this week: <br />
<ul>
	<#list thisWeek as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>
<#if toAnswerItems?size!=0>
Entries awaiting your answers:<br />
<ul>
	<#list toAnswerItems as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>
<#if answeredItems?size!=0>
Latest answered questions for you:<br />
<ul>
	<#list answeredItems as item>
	<li><a href="${item.url}">${item.title}</a>&nbsp;(${item.author})</li>
	</#list>
</ul>
<br />
</#if>

<br />
<a href="${directLogin}">Login your account in Learning Log System &gt;&gt;</a>
<br /><br />
--------------------------------------------------------------------<br />
If you don’t want to receive this email notification, please adjust your
<a href="${settingLink}">setting</a>.<br /><br />
<b>Learning Log Developer Team</b>
	</body>
</html>