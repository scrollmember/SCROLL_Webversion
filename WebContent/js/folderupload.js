window.URL = window.URL ? window.URL :window.webkitURL ? window.webkitURL : window;
var files = [], currentPage = 0, pageSize=15;
$(function(){
	resetFolder();
	toggleStatus(0);
	$('#dropbox').click(function(){
		$("#folder").click();
	});
	
	$('#dropbox').on("click", ".preview", function(){
		return false;
	});
	
	$(document).on("change", "#folder", function(e){
		toggleStatus(0);
		$("#dropbox .preview").remove();
		
		files = [];
		currentPage = 0;
		
		var fileList = e.target.files;
		resetFolder();
		if(!validateFiles(fileList)){
			alert("Please select a right SenseCam photo folder!");
			return;
		}
		var size = fileList.length;
		for(var i=0;i<size;i++){
			if(fileList[i].type.indexOf("image")!=-1 || fileList[i].name=="SENSOR.CSV"){
				files.push(fileList[i]);
			}
		}
		$("#uploadProgress").text("0/"+files.length);
		changePage();
		toggleStatus(1);
	});
	
	$(".jumpPage").on({
		mouseup: function(e){
			changePage($(".jumpPage").val());
		},
		change: function(e){
			var totalPage = Math.ceil(files.length/pageSize);
			$(".pageInfo").text($(".jumpPage").val()+"/"+totalPage);
		}
	});
	
	$("body").on({
		dragenter:function(e){
			$("#folder").css("width", $(document).width());
			$("#folder").css("height", $(document).height());
		},
		mouseout: function(e){
			resetFolder();
		}
	});
});
//
var template = '<div class="preview">'+
'<span class="imageHolder">'+
	'<canvas />'+
	'<span class="uploaded"></span>'+
'</span>'+
'<progress value="0" max="100" style="width:100%"></progress>'+
'<div class="fileinfo"></div>'+
'</div>';

function createImage(file){
	var preview = $(template), canvas = $('canvas', preview), ctx = canvas[0].getContext("2d");
	if(file.type.indexOf("image")!=-1){
		var img = new Image();
		var imageURL = window.URL.createObjectURL(file);
		img.onload = function(e){
			var ratio = 1;
			var maxWidth = 240;
			var maxHeight = 180;
			if(this.width > maxWidth)
	            ratio = maxWidth / this.width;
	        else if(this.height > maxHeight)
	        	ratio = maxHeight / this.height;
			canvas[0].width = this.width*ratio;
			canvas[0].height = this.height*ratio;
			ctx.drawImage(img, 0,0, this.width*ratio, this.height*ratio);
		};
		img.src = imageURL;
		
		$('.fileinfo', preview).html(file.name);
		window.URL.revokeObjectURL(imageURL);
	}else if(file.name=="SENSOR.CSV"){
		var img = new Image();
		var imageURL = "../images/pacall/fileicon.png";
		img.onload = function(e){
			var ratio = 1;
			var maxHeight = 180;
	        ratio = maxHeight / this.height;
			canvas[0].width = this.width*ratio;
			canvas[0].height = this.height*ratio;
			ctx.drawImage(img, 0,0, this.width*ratio, this.height*ratio);
		};
		img.src = imageURL;
		
		$('.fileinfo', preview).html(file.name);
	}
	$('#dropbox .message').hide();
	preview.attr("id", file.name+"_box");
	preview.appendTo($('#dropbox'));
	
	$.data(file,preview);
	
	var percentage = file.percentage?file.percentage:0;
	$.data(file).find('.progress').width(percentage+"%");
	if(percentage == 100){
		$.data(file).addClass('done');
	}
}

function resetFolder(){
	$("#folder").remove();
	$("<input type=\"file\" id=\"folder\" webkitdirectory directory multiple/>").prependTo($("body"));
}

function changePage(page){
	$(".jumpPage").attr("disabled", true);
	$("#dropbox .preview").remove();
	if(!page || page<1) page = 1;
	var totalPage = Math.ceil(files.length/pageSize);
	if(page>totalPage) page = totalPage;
	if(totalPage>1){
		$(".pageList").show();
	}else{
		$(".pageList").hide();
	}
	var filesLength = files.length;
	var start = pageSize*(page-1);
	if(start>filesLength) start = 0;
	
	var end=start+pageSize;
	if(end > filesLength){
		end = filesLength;
	}
	for(var i=start;i<end;i++){
		createImage(files[i]);
	}
	currentPage = page;
	$(".pageInfo").text(page+"/"+totalPage);
	$(".jumpPage").attr("max",totalPage).attr("disabled", false);
	if($(".jumpPage").val()!=currentPage){
		$(".jumpPage").val(currentPage);
	}
}

function startupload(url, opts){
	toggleStatus(2);
	
	if(!opts)opts = {};
	if(!opts.progressUpdated)opts.progressUpdated = function(){};
	if(!opts.uploadFinished) opts.uploadFinished = function(){};
	
	//Upload Folder Info and Get Folder Id
	opts.uploadSensorCsvFinished = function(responseText){
		//Upload Photos
		var result = JSON.parse(responseText);
		console.log(result);
    	for(var i in files){
    		if(files[i].name.toUpperCase()!="SENSOR.CSV"){
    			upload(url, result.folderId, files[i], true, opts);
    		}
    	}
	};
	
	//Upload Sensor Data
	for(var i in files){
		if(files[i].name.toUpperCase()=="SENSOR.CSV"){
			upload(url, null, files[i], true, opts);
		}
	}
}

function upload(url, folderId, file, async, opts){
	if(!file)return;
	var xhr = new XMLHttpRequest();
	xhr.upload.file = file;
	xhr.upload.addEventListener("progress", function(e){
		if (e.lengthComputable) {
		    var percentage = Math.round((e.loaded * 100) / e.total);
		    this.file.percentage = percentage;
		    if(opts.progressUpdated){
		    	opts.progressUpdated(this.file);
		    }
		}
	}, false);
	xhr.onload = function() { 
	    if (xhr.responseText) {
		    result = opts.uploadFinished(file, xhr.responseText);
		    if(file.name=="SENSOR.CSV"){
		    	opts.uploadSensorCsvFinished(xhr.responseText);
		    }
	    }
	};
	xhr.open("post", url, async);
	var formData = new FormData();
	formData.append('folderId', folderId);
	formData.append('file', file);
	xhr.send(formData);
}

function validateFiles(fileList){
	var size = fileList.length;
	if(!fileList || fileList.length == 0) return false;
	var sensorcsvCount = 0, hasPhoto = false;
	for(var i=0;i<size;i++){
		if(!hasPhoto && fileList[i].type.indexOf("image")!=-1){
			hasPhoto = true;
		}
		if(fileList[i].name.toUpperCase()=="SENSOR.CSV"){
			sensorcsvCount++;
		}
	}
	if(hasPhoto && sensorcsvCount==1){
		return true;
	}
	return false;
}

function toggleStatus(status){
	$(".uploadStatus").children().hide();
	if(status == 1){
		$(".uploadStatus .uploadready").show();
	}else if(status==2){
		$(".uploadStatus .uploading").show();
	}else if(status==3){
		$(".uploadStatus .uploadfinish").show();
	}
	$("#uploadProgress").show();
}