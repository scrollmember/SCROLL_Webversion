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
		

		document.getElementById("yesandnoquiz").style.display = "block";
			document.getElementById("textquiz").style.display = "none";
			document.getElementById("imagequiz").style.display = "none";

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

					<div id="Left">

						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Quiz
									Create Types</h3>

							</div>
						</div>
						<ul class="nav nav-list">
							<li class="active" id="q1"><a
									href="#" onClick="Displayshow(0)">Yes & No Quiz</a></li>
								<li id="q2"><a href="#"
									onClick="Displayshow(1)">Text multiple-choice Quiz</a></li>
								<li id="q3"><a href="#"
									onClick="Displayshow(2)">Image multiple-choice Quiz</a></li>

						</ul>
						<br>
				
				

					</div>
					<div id="Center">

						<div class="navbar navbar-inner" style="position: static;">
							<div class="navbar-primary">
								<h3
									style="font-size: 14px; font-weight: bolder; line-height: 150%">Quiz Layout</h3>

							</div>
						</div>
						<c:url value="/quiz/create" var="QuizcreateURL" /> 
						
						<div id="yesandnoquiz">
						<p class="text-info">&nbsp; Question : Do you remember?</p>
							
						<div id="selectitem"></div>
					<br>
								<div id="search_area">
							<input name="querylog" id="querylog" /> <input type="button"
								value="Search" onClick="onQuery()" />
						</div>
						<br>
						<div>
							<ul id="search_result_area"></ul>
						</div>
						<div style="width: 85px">
							<ul class="moreInfo button" style="width: 85px">
							<li><input type="submit"
							class="btn btn-block btn-primary" value="Create"
							style="width: 85px" onClick="onSave()"/></li>

							</ul>
							</div>
						</div>
						
						<div id="textquiz">
						Select the right word * ?
						<table class="table table-condensed">
						<tr class="success">
   						 <td>1</td>
   						 <td>TB - Monthly</td>
   						 <td>01/04/2012</td>
   						 <td>Approved</td>
  						</tr>
						</table>
						Answer Too Easy Too Difficult No good
						</div>
						<div id="imagequiz">
						Which image can be linked * ?
						<div class="container-fluid">
  <div class="row-fluid">
    <div class="span2">
      test
    </div>
    <div class="span10">
      test2
    </div>
  </div>
</div>
						</div>
						

					<div>
                          	<ul id="related_item_area"></ul>
                          </div> 

					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>