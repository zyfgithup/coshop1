/**
 * 备份数据
 */
function dataBackup(){
	if(confirm('确认要进行数据备份吗？(如果数据量比较大可能备份时间会比较长)')){
		//加载进度条
		loadProgress();
		$.ajax({
			url : URL_PREFIX+"/databackup/all/saveAllDataBackup.do",
			type : "POST",
			data : {
				"model.remark" : $('#remark').val() 				
			},
			dataType : "json",
			success : function(result) {
				//如果执行成功，判断是否执行更新盈亏表操作
				if (result.success) {
					Ext.MessageBox.hide(); //隐藏进度条
					alert(result.msg);
					document.getElementById("index").submit();
				}else{
					Ext.MessageBox.hide(); //隐藏进度条
					alert(result.msg);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				Ext.MessageBox.hide(); //隐藏进度条
				alert(XMLHttpRequest + "@@" + textStatus + "@@@" + errorThrown);
				return false;
			}
		});
	}
}
/**
 * 备份文件下载
 */
function downloadFile(id){
	window.location.href=URL_PREFIX + "/databackup/all/downloadFile.do?model.id=" + id;
}
//加载进度条	
function loadProgress() {
	Ext.MessageBox.show({
		title : '提示',
		msg : '数据生成中,请耐心等待···',
		progress : true,
		width : 300,
		wait : true,
		waitConfig : {
			interval : 200,//0.6s进度条自动加载一定长度
			duration : 12000000000,
			fn : function() {
				Ext.MessageBox.hide();//让进度条消失
				Ext.MessageBox.show({
					title : '提示',
					minWidth : 220,
					minHeight : 220,
					msg : '系统超时！(可能由于数据量较大导致超时)',
					buttons : Ext.MessageBox.OK
				//icon : Ext.MessageBox.INFO,
				});
			}
		},
		closable : true
	});
}