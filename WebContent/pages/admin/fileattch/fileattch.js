var Systop_FileAttch_IDS = "";

/**
 * 渲染上传组件，具体属性和方法看官方文档
 */
function renderUploader () {
	$('#systop_upload').uploadify({
		'uploader'  : URL_PREFIX + '/scripts/uploadify/uploadify.swf',
		'cancelImg' : URL_PREFIX + '/scripts/uploadify/cancel.png',
		'script'    : URL_PREFIX + '/fileattch/upLoadFile.do',
		'buttonImg' : URL_PREFIX + '/scripts/uploadify/upload_b.jpg',
		'fileDataName' : 'attch',
		//'removeCompleted' : false,
		'multi'          : true,
		'queueID'        : 'systop-file-queue',
		'queueSizeLimit' : 5,
		'width'     : '80',
		'height'    : '25',
		'sizeLimit' : 102400000,
		'auto'      : true,
		'onComplete': function(event, ID, fileObj, response, data) {
			var htmlStr = $("#systop-uploaded-files").html();
			//icon 图标前缀
			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(fileObj.type) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;&nbsp;&nbsp;";
			//文件信息
			baseInfo = "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(fileObj.size/(1024*1024)) + "MB&nbsp;&nbsp;&nbsp;&nbsp;" + "速度:" + fixNum(data.speed/1024) + "MB/s&nbsp;&nbsp;&nbsp;&nbsp;";
			//删除按钮
			removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + response + ", true)'>";
			htmlStr += "<div id='" + response + "'>" + imgStr +  fileObj.name + baseInfo + removeImg + "</div>";
			$("#systop-uploaded-files").html(htmlStr);
			
			Systop_FileAttch_IDS = $("#fileattchids").val();
			Systop_FileAttch_IDS += (response + ",");
			$("#fileattchids").val(Systop_FileAttch_IDS);
		 }
  });
}
/**
 * 将一个文件从文件列表中删除，同时删除数据库及物理文件
 * @param fileId
 */
function deleteFile(fileId, isDelFileId){
	$.ajax({
	    url: URL_PREFIX + '/fileattch/ajaxRemove.do?model.id=' + fileId,
	    type: 'GET',
	    dataType: 'json',
	    error: function(){
	    	alert('Error delete fileattch...');
	    },
	    success: function(data, textStatus){
	    	//do nothing...
	    	//alert('Success delete fileAttch...' + data.id);
	    }
	});
	if (isDelFileId){
		document.getElementById(fileId).style.display="none";
		pattenId = fileId + ",";
		Systop_FileAttch_IDS = $("#fileattchids").val();
		if (Systop_FileAttch_IDS.indexOf(pattenId) != -1){
			Systop_FileAttch_IDS = Systop_FileAttch_IDS.replace(pattenId, "");
			$("#fileattchids").val(Systop_FileAttch_IDS);
		}
	}
}

var supportFileIcon = ".txt|.doc|.docx|.xls|.xlsx|.ppt|.pptx|.jpg|.gif|.png|.rar|.zip";
var imgExtNames = ".jpg|.gif|.png|";
/**
 * @param fileType [demo.txt]:[.txt]
 */
function getFileIcon(fileType){
	if(fileType != null && fileType.indexOf(".") == 0){
		ext = fileType.substring(1);
		if (supportFileIcon.indexOf(fileType) != -1){
			if (imgExtNames.indexOf(fileType) != -1){
				return "img.png";
			}else{
				return ext + ".png";
			}
		}else{
			return "other.png";
		}
	}
}

//加载图片
function loadOtherIcon(img){
	img.src = URL_PREFIX + "/scripts/uploadify/imgs/other.png";
}

//取整数
function fixNum(num){
	newNum = new Number(num);
	return newNum.toFixed(2);

}

/**
 * 显示附件列表
 * @param fileIds
 */
function viewFileAttchList(fileIds, viewDel){
	if (fileIds == null || fileIds.length ==0){
		return;
	}
	$.ajax({
	    url: URL_PREFIX + '/fileattch/findFiles.do?fileIds=' + fileIds,
	    type: 'get',
	    dataType: 'json',
	    error: function(){
	    	alert('get file list error');
	    },
	    success: function(data, textStatus){
	    	var htmlStr = $("#systop_file_list").html();
	    	if (data != null && data.files != null){
	    		for(i = 0; i < data.files.length; i++){
	    			//icon 图标前缀
	    			imgStr = "<img src='" +URL_PREFIX + "/scripts/uploadify/imgs/" + getFileIcon(data.files[i].ext) + "' onError='loadOtherIcon(this)'>&nbsp;&nbsp;";
	    			//文件信息
	    			baseInfo = "<a href='" + URL_PREFIX + data.files[i].path + "' target='_blank'>" + data.files[i].name + "</a>";
	    			baseInfo += "&nbsp;&nbsp;&nbsp;&nbsp;大小:" + fixNum(data.files[i].totalBytes/(1024*1024)) + "MB";
	    			//删除按钮
	    			removeImg = "";
	    			if (viewDel){
	    				removeImg = "<img src='" + URL_PREFIX + "/images/icons/remove.png' onclick='deleteFile(" + data.files[i].id + ", true)'>";
	    			}
	    			htmlStr += "<div id='" + data.files[i].id + "'>" + imgStr + baseInfo + removeImg + "</div>";
	    			$("#systop_file_list").html(htmlStr);
	    		}
	    	}
	    }
	});
}
