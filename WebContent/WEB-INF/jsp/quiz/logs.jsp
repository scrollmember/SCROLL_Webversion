<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>

<html>
<c:import url="../include/head.jsp">
</c:import>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="${ctx}/learninglog/css/screen.css"
	type="text/css" media="screen">
<link rel="stylesheet" href="${ctx}/learninglog/css/bootstrap.css"
	type="text/css" media="screen">
</head>
<script type="text/javascript">
	$(document).ready(function() {
		

	
	});
</script>
 <script type="text/javascript">
 				var itemname;
 				var quiztype="yesnoquiz";
 				
 				var middle="";
 				var bottom;
	           function onQuery(){
	        	  
	        	   if($('#querylog').val() == "")
	        		   return;
	        	   <c:url value="/quiz/searchItem.json" var="searchItemUrl" />
	        	   $("#search_result_area").empty();
	        	   $.ajax({
	        	         type: "GET",
	        	         url: "${searchItemUrl}",
	        	         data: {taskId:'${task.id}', queryvalue:$('#querylog').val()},
	        	         dataType: "json",
	        	         success: function(data){
	        	        	 $.each(data.items,function(index, value){
	        	        		 var result = "<li><input type='radio' name='itemId' onclick=\"onAddItemId('"+value.itemId+"')\" value='"+value.itemId+"'> ";
	        	        		 var temp = "";
	        	        		 $.each(value.titles, function(i, title){
	        	        			 if(i == "${task.language.id}"){
	        	        			 	temp = title;
	        	        			 	return false;
	        	        			 }else
		        	        			 temp = title;
	        	        			 
	        	        		 });
	        	        	 	 <c:url value="/item/" var="itemUrl" />
        	        			 result = result+ "<a href='${itemUrl}"+value.itemId+"' target='_blank'>"+ temp+"</a>";
	        	        		 result +="</input>";
	        	        		 $("#search_result_area").append(result+"</li>");
	        	        	 })
	        	        	 
	        	          }
	        	      });
	        	   
	        	   
	           }
	           
	           function onSave(){
	        	  
	        	   <c:url value="/quiz/yesnocreate" var="searchItemUrl" />
	        	   $.ajax({
	        	         type: "POST",
	        	         url: "${searchItemUrl}",
	        	         data: {itemname:itemname},
	        	         success: function(data){
	        	        	
	        	        	document.location = "<c:url value="/quiz/menu"/>";
	        	          },
	        	         error: function()
	        	         {
	        	        	 alert("失敗");
	        	         }
	        	      });
	        	  
	           }
	          
	

 				function onAddItemId(itemId){
 					<c:url value="/quiz/createitem.json" var="searchItemUrl" />
 					$("#selectitem").empty();
 					  $.ajax({
 	        	         type: "GET",
 	        	         url: "${searchItemUrl}",
 	        	         data: {itemid:itemId},
 	        	         dataType: "json",
 	        	         success: function(data){
 	        	        	 $("#selectitem").append("<div class='container-fluid'><div class='row-fluid'><div class='span2'><img width='85px' height='85px' alt='' src='' id='testimage'/></div><div class='span10'><table class='table table-condensed'><tr class='success'><td>ULL Name</td><td>Author</td><td>Create Time</td></tr><tr><td id='lloname'></td><td id='lloauthor'></td><td id='llotime'></td></tr></table></div></div></div>");
 	        	        	 $.each(data.items,function(index, value){
 	        	
 	        	        		 if(value.image==null && index==0){
 	        	        			document.getElementById('testimage').src = "<c:url value="/images/no_image.gif" />";
 	        	        			document.getElementById('lloname').innerHTML=value.content;
 	        	        			document.getElementById('lloauthor').innerHTML=value.nickname;
 	        	        			document.getElementById('llotime').innerHTML=value.create_time;
 	        	        		 }
 	        	        		 else if(value.image=!null && index==0){
 	        	        			
 	        	        			document.getElementById('testimage').src = data.imageurl;
 	        	        			document.getElementById('lloname').innerHTML=value.content;
 	        	        			document.getElementById('lloauthor').innerHTML=value.nickname;
 	        	        			document.getElementById('llotime').innerHTML=value.create_time;
 	        	        		 }
 	    					
 	        	        		
 	        	        		
 	        	        	 })
 	        	        	 
 	        	  
 	        	          }
 	        	       
 	        	      });
 					
 					
 					
 					
	           }
 	           function refreshRelatedItem(){
	        	   <c:url value="/task/${task.id}/items.json" var="getItemUrl" />
	        	   $.ajax({
        			   type:"GET",
        			   url:"${getItemUrl}",
        			   success:function(data){
        				   $("#related_item_area").empty();
        				   $.each(data.items, function(index,value){
        					   var result = "<li><input type='checkbox' name='deleteitemId' onclick=\"onRemoveItemId('"+value.itemId+"')\" value='"+value.itemId+"'> ";
	        	        		 var temp = "";
	        	        		 $.each(value.titles, function(i, title){
	        	        			 if(i == "${task.language.id}"){
	        	        			 	temp = title;
	        	        			 	return false;
	        	        			 }else
		        	        			 temp = title;
	        	        		 });
	        	        		 result = result+ "<a href='${itemUrl}"+value.itemId+"' target='_blank'>"+ temp+"</a>";
	        	        		 result +="</input></li>";
	        	        		 $("#related_item_area").append(result);
        				   });        					   
        			   }
        		   });
	           }
 	           
 	          function Displayshow(num) {
 	     		if (num == 0) {
 	     			document.getElementById("yesandnoquiz").style.display = "block";
 	     			document.getElementById("textquiz").style.display = "none";
 	     			document.getElementById("imagequiz").style.display = "none";
 	     			document.getElementById("q1").className = "active";
 	     			document.getElementById("q2").className = "";
 	     			document.getElementById("q3").className = "";
 	     			
 	     		}
 	     		else if(num==1){
 	     			document.getElementById("yesandnoquiz").style.display = "none";
 	     			document.getElementById("textquiz").style.display = "block";
 	     			document.getElementById("imagequiz").style.display = "none";
 	     			document.getElementById("q1").className = "";
 	     			document.getElementById("q2").className = "active";
 	     			document.getElementById("q3").className = "";
 	     		
 	     		}
 	     		else if(num==2){
 	     			document.getElementById("yesandnoquiz").style.display = "none";
 	     			document.getElementById("textquiz").style.display = "none";
 	     			document.getElementById("imagequiz").style.display = "block";
 	     			document.getElementById("q1").className = "";
 	     			document.getElementById("q2").className = "";
 	     			document.getElementById("q3").className = "active";
 	     		}
 	     		
 	     	}
 	           
 	           
 </script>
<body>
	<div id="Container">
		<c:import url="../include/header.jsp" />
		<div id="Contents">
			<div id="ContentsContainer">

				<div id="QuizCreateLayout" class="Layout">

					<div id="Center">

						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Quiz Logs</h3>

							</div>
						</div>
						<table class="table table-condensed">
						<tr class="success">
   						 <td>Answerstate = 0</td>
   						 <td>Wrong quiz</td>
  						</tr>
  						<tr class="info">
   						 <td>Answerstate = 1</td>
   						 <td>Correct quiz</td>
  						</tr>
  						<tr class="warning">
   						 <td>Answerstate = 2</td>
   						 <td>"No Good" button</td>
  						</tr>
  						<tr class="info">
   						 <td>Answerstate = 3</td>
   						 <td>"Too easy" button and the wrong quiz</td>
  						</tr>
  						<tr>
   						 <td>Answerstate = 4</td>
   						 <td>"Too difficult" button and the wrong quiz</td>
  						</tr>
  						</table>
  						<br>
						
						<table class="table table-condensed">
						<tr class="success">
   						 <td>Nickname</td>
   						 <td>Answerstate</td>
   						 <td>The number of quiz logs</td>
   						
  						</tr>
						<c:forEach begin="0" end="10000" step="1" var="quizhistory"
									varStatus="status" items="${quizdata}">


									
						<tr>
   						 <td>${quizhistory.nickname}</td>
   						 <td>${quizhistory.answerstate}</td>
   						 <td>${quizhistory.total}</td>
   						 
  						</tr>
						


								</c:forEach>
						
						</table>
						</div>
						

			

					</div>

				</div>
			</div>
		</div>

</body>
</html>