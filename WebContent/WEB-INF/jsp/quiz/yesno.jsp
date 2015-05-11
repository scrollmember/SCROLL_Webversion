<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<script type="text/javascript">
		function fncAnswer(v) {
			$("#answer").val(v);
			$("#form").submit();
		}
	</script>
<div style="font-family: arial,meiryo,simsun,sans-serif;font-size: 22px; font-weight:bold; color: gray; margin-bottom:10px;">
	<img src="<c:url value='/images/system-help.png' />" alt="●"/> 
	<c:if test ="${quiz.item.author.id == quiz.author.id }">
		Do you remember?
	</c:if>
	<c:if test ="${quiz.item.author.id != quiz.author.id }">
		Do you know?
	</c:if>
</div>
 <form:hidden path="answer"/>
<table style=" border: 2px ridge; font-family: arial; font-size: 22px; font-weight: bold">
   <c:if test="${!empty quiz.item.titles}"> 
   <c:url value="/item/${quiz.item.id}" var="itemurl" ></c:url>
       	<c:forEach items="${quiz.item.titles}" var="title">
          <tr>
           	<td style="width:120px;">${title.language.name}</td>
            	<td><a href="${itemurl}" target="_blank"><c:out value="${title.content}" /></a></td>
          </tr>
         </c:forEach>
     </c:if>
     <c:if test="${!empty quiz.item.barcode || !empty quiz.item.qrcode || !empty quiz.item.rfid}">
      <tr>
          <th><label for="tag1">ID</label></th>
          <td>
              <table>
              	<c:if test="${!empty quiz.item.barcode}">
                  <tr>
                      <td style="width:70px;">Barcode</td>
                      <td>${quiz.item.barcode}</td>
                  </tr>
                  </c:if>
                  <c:if test="${!empty quiz.item.qrcode}">
                  <tr>
                      <td>QR Code</td>
                      <td>
                      	<c:if test="${!empty quiz.item.qrcode}">
                      		<img src="http://chart.apis.google.com/chart?cht=qr&chs=120x120&chl=${systemUrl}/instant/item?qrcode=${quiz.item.qrcode}" />
                      	</c:if>
                      </td>
                  </tr>
                  </c:if>
                  <c:if test="${!empty quiz.item.rfid}">
                  <tr>
                      <td>RFID</td>
                      <td>${quiz.item.rfid}</td>
                  </tr>
                  </c:if>
              </table>
          </td>
      </tr>
      </c:if>
     <c:if test="${!empty quiz.item.itemTags}"> 
     <tr>
     	<th>Tags</th>
     	<td>
     		<c:forEach items="${quiz.item.itemTags}" var="tag">
     			<a href="<c:url value="/item"><c:param name="tag" value="${tag.tag}" /></c:url>">${tag.tag}</a> &nbsp;
             </c:forEach>
     	</td>
     </tr>
     </c:if>
</table>
<div style="margin-top:10px; text-align:center">
	<input type="submit" class="btn btn-primary" value="Yes" onclick="fncAnswer(1);return false;"/>
	<input type="submit" class="btn btn-warning" value="No" onclick="fncAnswer(0);return false;"/>
	<input type="button" class="btn btn-danger" value="No Good" onclick="return fncPass();"/>
</div>
