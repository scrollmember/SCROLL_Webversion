<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<c:import url="../include/head.jsp">
    <c:param name="title" value="Add a new object" />
    <c:param name="content">
        <style>
            .optional{display:none}
        </style>
            <script src="http://www.google.com/jsapi"></script>
            <script src="http://maps.google.com/maps/api/js?sensor=true"></script>
            <script src="<c:url value='/js/LLMap.js' />"></script>
        <script>
                
                var map;
                $(function(){
                	map = new LLMap("map", {
                		onchange:function(lat, lng, zoom){
                			$("#lat").val(lat);
                			$("#lng").val(lng);
                			$("#zoom").val(zoom);
                		}
                	});
                });
                
                
                $(
       	            function(){
       	            		if(!$('#location_based_cb').is(':checked'))
       	                		$('#script_map').hide();
       	                	else
       	            			$('#script_map').show();
       	            	}	
       	         );
                
            	$(document).on("change", "#location_based_cb", function(){
            		if($('#location_based_cb').is(':checked'))
            			$('#script_map').show();
                	else
                		$('#script_map').hide();
            	});
            	
              	$(document).on("change", "#time_based_cb", function(){
            		if($('#time_based_cb').is(':checked'))
            			$('#script_time').show();
                	else
                		$('#script_time').hide();
            	});
            </script>
            
            
            
            <script type="text/javascript">
	           function onQuery(){
	        	   if($('#querylog').val() == "")
	        		   return;
	        	   <c:url value="/task/searchItem.json" var="searchItemUrl" />
	        	   $("#search_result_area").empty();
	        	   $.ajax({
	        	         type: "GET",
	        	         url: "${searchItemUrl}",
	        	         data: {taskId:'${task.id}', queryvalue:$('#querylog').val()},
	        	         dataType: "json",
	        	         success: function(data){
	        	        	 $.each(data.items,function(index, value){
	        	        		 var result = "<li><input type='checkbox' name='itemId' onclick=\"onAddItemId('"+value.itemId+"')\" value='"+value.itemId+"'> ";
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
	           
	           
	           function onAddItemId(itemId){
	        	   <c:url value="/task/${task.id}/additem.json" var="addItemUrl" />
        		   $.ajax({
        			   type:"POST",
        			   url:"${addItemUrl}",
        			   data: "itemIds="+itemId,
        			   success:function(data){
        				   refreshRelatedItem();
        			   }
        		   });
	           }
	           
	           function onRemoveItemId(itemId){
	        	   <c:url value="/task/${task.id}/removeitem.json" var="removeItemUrl" />
        		   $.ajax({
        			   type:"POST",
        			   url:"${removeItemUrl}",
        			   data: "itemIds="+itemId,
        			   success:function(data){
        				   refreshRelatedItem();
        			   }
        		   });
	           }
	           
	           $(refreshRelatedItem());
	           
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
           </script>
            
            
        </c:param>
    </c:import>
    <body>
        <div>
            <div>
                <c:import url="../include/header.jsp" />
                <div id="Contents">
                    <div id="ContentsContainer">
                        <div id="LayoutC" class="Layout">
                            <div id="Top">
                                <div id="information_21" class="parts informationBox">
                                    <div class="body">
                                    </div>
                                </div><!-- parts informationBox -->
                            </div><!-- Top -->
                            <div id="Center">
                                <div id="diaryForm" class="dparts form">
                                    <div class="parts">
                                        <div class="partsHeading"><h3>Step ${task.taskScripts.size()+1} of the task</h3></div>
                                        <c:url value="/task/${task.id}/addscript" var="taskUrl" />
                                        <form:form commandName="script" action="${taskUrl}" method="post" enctype="multipart/form-data">
                                            <table>
                                                <tr>
                                                    <th>
                                                        <label for="task_title">Title</label>
                                                    </th>
                                                    <td> 
                                                        	${task.title}
                                                        	<form:hidden path="taskId" value="${task.id}"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="script">Script</label><strong>*</strong>
                                                    </th>
                                                    <td> 
	                                                 <form:textarea path="script" cols="20" rows="15" cssStyle="width:98%" /><form:errors cssClass="error" path="script"/>
                                                    </td>
                                                </tr>
                                                  <tr>
                                                    <th><label for="photo">Photo|Video<br />Audio|PDF</label></th>
                                                    <td>
                                                        <input type="file" name="image" id="image" class="input_file" /><form:errors cssClass="error" path="image" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="place">Place</label>
                                                    </th>
                                                    <td> 
                                                    	<form:input path="place"/>&nbsp;&nbsp;&nbsp;<label>(e.g. School, Supermarket, Bank)</label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="time_based">Time Based</label>
                                                    </th>
                                                    <td> 
                                                        <form:checkbox path="timeBased" id="time_based_cb" />
                                                    </td>
                                                </tr>
                                                <tr id="script_time">
                                                  <th>
                                                        <label for="Time_Period">Time Period</label>
                                                    </th>
                                                    <td> 
                                                  	<form:select path="starthour">
                                                                        <form:options items="${oneDay}"/>
                                                    </form:select>
                                                	： <form:select path="startmin">
                                                                        <form:options items="${minutes}"/>
                                                    </form:select>~
                                                    <form:select path="endhour">
                                                                        <form:options items="${oneDay}"/>
                                                    </form:select>
                                                	： <form:select path="endmin">
                                                                        <form:options items="${minutes}"/>
                                                    </form:select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        <label for="location_based">Location Based</label>
                                                    </th>
                                                    <td> 
                                                        <form:checkbox path="locationBased" id="location_based_cb" />
                                                    </td>
                                                </tr>
                                         		<tr id="script_map">
                                                    <td colspan="2">
                                                        <form:hidden path="lat" id="lat" />
                                                        <form:hidden path="lng" id="lng"/>
                                                        <form:hidden path="zoom" id="zoom"/>
                                                        <div id="map" style="width: 98%; height: 350px"></div>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="operation">
                                                <ul class="moreInfo button">
                                                 	<li>
                                                        <input type="submit" class="back_submit" value="Back" />
                                                     </li>
                                                    <li>
                                                        <input type="submit" class="next_submit" value="Next" />
                                                     </li>
                                                     <li>   
                                                        <input type="submit" class="publish_submit" value="Publish" />
                                                    </li>
                                                </ul>
                                            </div>
                                        </form:form>
                                    </div><!-- parts -->
                                </div><!-- dparts -->
                            </div><!-- Center -->
                        </div><!-- Layout -->
                        <div id="sideBanner">
                          <div class="partsHeading">
                                    <h3>Related Learning Logs</h3>
                          </div>
                          <div>
                          	<ul id="related_item_area"></ul>
                          </div> 
                          <br>
                   		  <div id="search_area">
								<input name="querylog" id="querylog"/>  <input type="button" value="Search" onClick="onQuery()"/>                              	
                           </div>
                           <br>
                           <div>
                           <ul id="search_result_area"></ul>
                           </div>
                        </div>
                    </div><!-- ContentsContainer -->
                </div><!-- Contents -->
                <c:import url="../include/footer.jsp" />
            </div><!-- Container -->
        </div><!-- Body -->
    </body>
</html>
