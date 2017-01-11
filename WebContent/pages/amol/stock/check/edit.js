/**
 * 选择仓库 
 */
function selectStorage(){
	$("#productSortId").val('');
	$("#productName").val('');
	$("#productName").attr("title", $("#productName").val());
}
/**
 * 选择员工
 */
function emp() {
	if (navigator.userAgent.indexOf("MSIE") > 0) {//IE判断
		var cus = window.showModalDialog(
				URL_PREFIX+"/pages/amol/base/employee/selector.jsp", null,
				"dialogWidth=800px;resizable: Yes;");
		if (cus != null) {
			var tab = document.getElementById("empname");
			tab.value = cus.name;
			var tab1 = document.getElementById("empid");
			tab1.value = cus.id;
		}
	} else {
		window.open(URL_PREFIX+"/pages/amol/base/employee/selector.jsp",
					"",
					"width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		this.returnAction = function(cus) {
			if (cus != null) {
				var tab = document.getElementById("empname");
				tab.value = cus.name;
				var tab1 = document.getElementById("empid");
				tab1.value = cus.id;
			}
		};
	}
}
/**
 * 选择商品
 */
function selectPro(){
	var storageId = $("#storageId").val(); // 获得仓库ID
	if(!storageId){
		alert('请先选择仓库');
		return;
	}
	var pSortID = document.getElementById("productSortId").value;  // 商品类型ID
	var checkNo = $("#checkNo").val();
	if (navigator.userAgent.indexOf("MSIE") > 0) {//IE判断
		var cus = window.showModalDialog(
				URL_PREFIX+"/pages/amol/stock/check/selectorPro.jsp?storageId="+storageId
							+"&productSortId="+pSortID
							+"&proCode="+$("#productCode").val()
							+"&checkNo="+checkNo
							, null,"dialogWidth=800px;resizable: Yes;");
		if (cus != null) {
			var proCode = "";
			var proName = "";
			$.each(cus, function(i, item){
				if(item){
					proCode += item.code + ",";
					proName += item.name + ",";
				}
			});
			$("#productCode").val($("#productCode").val()+ proCode);
			$("#productName").val($("#productName").val()+ proName);
			$("#productName").attr("title", $("#productName").val());
		}
	} else {
		window.open(URL_PREFIX+"/pages/amol/stock/check/selectorPro.jsp?storageId="+storageId+"&productSortId="+pSortID+"&checkNo="+checkNo,
					"",
					"width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
		this.returnAction = function(cus) {
			if (cus != null) {
				var arr = "";
				$.each(cus, function(i, item){
					if(item){
						arr += item.id + ",";
					}
				});    	
				$("#productCode").val($("#productCode").val()+arr);
			}
		};
	}
}

/**
 * 生成库存盘点提示
 */
function generateStockCheckInfo() {
	var storageId = $("#storageId").val(); // 获得仓库ID

	var checkDate = $("#checkDate").val();  //获得选择日期
	var d = new Date();   // 获得当前日期
	var vYear = d.getFullYear();
	var vMon = d.getMonth() + 1;
	var vDay = d.getDate();
	var dd = vYear+"-"+vMon+"-"+vDay;
	var stdt=new Date(checkDate.replace("-","/"));
	var etdt=new Date(dd.replace("-","/"));
	if(stdt > etdt){
		alert('请选择日期,日期大于当前日期！');
		return;	
	}
	if (storageId == null || storageId == '') {
		alert('请您选择仓库！');
		return;
	}
	var storageName = "";
	var objs = document.getElementById("storageId");
	//获得选中仓库名称
	for (i = 0; i < objs.length; i++) {
		if (objs[i].selected == true) {
			storageName = objs[i].innerText;
		}
	}
	var pCode = document.getElementById("productCode").value;
	var pSortName = document.getElementById("productSortName").value;
	var msg = "确定要生成 【" + storageName + "】仓库";
	
	if(pSortName != '' && pSortName != '所有商品类型' && pSortName != '选择商品类型'){
		msg += "【"+ pSortName +"】类型";
	}
	if(pCode != '' ){
		msg += " 编码【"+pCode+"】商品";
	}
	var stockCheckId = $('#stockCheckId').val();
	if(!stockCheckId){
		msg += "的盘点数据吗?";
		if (confirm(msg)){
			//第一次生成盘点数据
			generateStockCheck();
		}
	}else{
		msg += "的盘点数据吗?(如果出现重复商品将会被覆盖)";
		if(confirm(msg)){
			//追加盘点详单数据
			appGenerateSCD();			
		}
	}
}

/**
 * 生成库存盘点
 */
function generateStockCheck() {
	var checkDate = $("#checkDate").val(); //盘点日期
	var checkNo = $("#checkNo").val(); //库存盘点编号
	var userId = $("#userId").val();//录入员ID（用户）
	var empId = $("#empid").val();//清查人员ID（员工）
	var storageId = $("#storageId").val(); // 获得仓库ID
	
	//var pName = document.getElementById("productName").value;  // 商品名称
	var pCode = document.getElementById("productCode").value;  // 商品编码
	var pSortID = document.getElementById("productSortId").value;  // 商品类型ID
	
	//加载进度条
	loadProgress();

	$.ajax({
		url : URL_PREFIX+"/stock/check/detail/generateStockCheckDetail.do",
		type : "POST",
		data : {
			"model.createTime" : checkDate,
			"model.stockCheck.checkNo" : checkNo,
			"model.stockCheck.storage.id" : storageId,
			"model.employee.id" : empId,
			"productCode":pCode,
			"productSort.id":pSortID			
		},
		dataType : "json",
		success : function(result) {
			//如果执行成功，判断是否执行更新盈亏表操作
			if (result.success) {
				Ext.MessageBox.hide(); //隐藏进度条
				//返回库存盘点ID
				$("#stockCheckId").attr("value", result.stockCheckId);
				//清空商品名称或编码
				$('#productName').val('');
				$('#productCode').val('');
				//清空商品类型
				$('#productSortId').val('0');
				$('#productSortName').val('选择商品类型');
				document.getElementById("index").submit();
				//刷新EC
				//ecRefresh();
			}else{
				Ext.MessageBox.hide(); //隐藏进度条
				alert(result.msg);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.hide(); //隐藏进度条
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : XMLHttpRequest + "@@" + textStatus + "@@@"
						+ errorThrown,//"发生异常，请与管理员联系！",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			return false;
		}
	});
}

/**
 * 追加库存盘点明细
 */
function appGenerateSCD() {
	var stockCheckId = $('#stockCheckId').val();  //盘点单ID
	var storageId = $("#storageId").val(); // 获得仓库ID
	var pCode = document.getElementById("productCode").value;  // 商品编码
	var pSortID = document.getElementById("productSortId").value;  // 商品类型ID
	
	//加载进度条
	loadProgress();

	$.ajax({
		url : URL_PREFIX+"/stock/check/detail/appGenerateSCD.do",
		type : "POST",
		data : {
			"model.stockCheck.id" : stockCheckId,
			"model.stockCheck.storage.id" : storageId,
			"productCode":pCode,
			"productSort.id":pSortID			
		},
		dataType : "json",
		success : function(result) {
			//如果执行成功，判断是否执行更新盈亏表操作
			if (result.success) {
				Ext.MessageBox.hide(); //隐藏进度条
				//清空商品名称或编码
				$('#productName').val('');
				$('#productCode').val('');
				//清空商品类型
				$('#productSortId').val('0');
				$('#productSortName').val('选择商品类型');
				document.getElementById("index").submit();
			}else{
				Ext.MessageBox.hide(); //隐藏进度条
				alert(result.msg);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.hide(); //隐藏进度条
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : XMLHttpRequest + "@@" + textStatus + "@@@"
						+ errorThrown,//"发生异常，请与管理员联系！",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			return false;
		}
	});
}

/**
 * 修改库存盘点明细数据
 */
function updateSCD(count) {
	// 获得该行TR
	var tr = count.parentNode.parentNode.parentNode;
	//或得库存数量
	var stockCount = tr.cells[7].innerText;
	if (count.value == "") {
		//tr.cells[5].innerHTML = parseInt(stockCount);
		count.value = "0";
	} 
	
	//验证实际数量
	if (isNaN(count.value)) {
		alert('请输入数字！');
		count.focus();
		count.value = "";
		tr.cells[7].innerHTML = "";
		return;
	}
	
	//如果是小数改成整数后录入
	count.value =parseInt(count.value);
	tr.cells[8].innerHTML = parseInt(count.value) - parseInt(stockCount);	
	updateStockCheckDetail(count);
}

/**
 * 修改库存盘点明细数据(异步)
 */
function updateStockCheckDetail(count) {
	var tr = count.parentNode.parentNode.parentNode; //得到行对象
	//var checkDate = $("#checkDate").val(); //盘点日期
	//var checkNo = $("#checkNo").val(); //库存盘点编号
	var stockId = tr.id; //即时库存ID
	var faceCount = count.value; //实际数量
	//var userId = $("#userId").val();//录入员ID（用户）
	//var empId = $("#empid").val();//清查人员ID（员工）
	var checkCount = tr.cells[8].innerText; //清算数量
	var scId = tr.id;//库存盘点记录ID（可能为空，表示未保存）
	//var scId = document.getElementsByName("stockCheckDetailId")[tr.rowIndex].value;
	if(!scId){
		Ext.MessageBox.show({
			title : '提示',
			minWidth : 220,
			msg : '发生异常信息，请重新操作或与管理员联系！',
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.INFO
		});
		return;
	}
	//var storageId = $("#storageId").val(); // 获得仓库ID
	//var stockCheckId = $("#stockCheckId").val();
	$.ajax({
		url : URL_PREFIX+"/stock/check/detail/updateStockCheckDetail.do",
		type : "POST",
		data : {
			"model.count" : faceCount,
			"model.id" : scId,
			"model.checkCount" : checkCount
		},
		dataType : "json",
		success : function(result) {
			//如果执行成功，判断是否执行更新盈亏表操作
			if (result.success) {
				//返回库存盘点ID
				//$("#stockCheckId").attr("value",result.stockCheckId);
				//返回库存盘点详单ID
				//document.getElementsByName("stockCheckDetailId")[tr.rowIndex].value = result.id;
				//$("#stockCheckDetailId").attr("value", result.id);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : XMLHttpRequest.responseText,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
	});
}

/**
 * 库存清查所有
 * （保存亏损单，盈利单，并且平仓）
 */
function stockCheckAll() {
	//var storageId = $("#storageId").val(); //所盘点的库ID
	//var empId = $("#empid").val();//清查人员ID（员工）
	//var checkNo = $("#checkNo").val(); //库存盘点编号
	//var checkDate = $("#checkDate").val(); //盘点日期
	var stockCheckId = $("#stockCheckId").val(); //库存盘点ID

	if (!stockCheckId) {
		Ext.MessageBox.show({
			title : '提示',
			minWidth : 220,
			msg : '清查库存错误请与管理员联系!',
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.INFO
		});
		return;
	}
	$.ajax({
		url : URL_PREFIX+"/stock/check/detail/getStockLiquidateInfo.do",
		type : "POST",
		data : {
			//"model.stockCheck.checkNo" : checkNo,
			//"model.stock.storage.id" : storageId,
			//"model.createTime" : checkDate
			"model.stockCheck.id" : stockCheckId
		},
		dataType : "json",
		success : function(result) {
			//如果执行成功，进行库存清算消息提示操作
			stockLiquidateMessage(result);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : XMLHttpRequest.responseText,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
	});
}
/**
 * 库存清算消息 
 */
function stockLiquidateMessage(result) {
	var msg = "";//stockCheckSurplus//stockCheckDeficit

	//盈利信息
	if (result.stockCheckProfit && result.stockCheckProfit.length > 0) {
		msg += "有" + result.stockCheckProfit.length
				+ "项商品<font color='red'>盈利</font></br>";
		$.each(result.stockCheckProfit, function(i, value) {
			msg += "[" + value.name + "] ";
		});
		msg += "</br>";
	}
	//else {
	//	msg += "没有盈利信息!</br></br>";
	//}
	//亏损信息
	if (result.stockCheckDeficit && result.stockCheckDeficit.length > 0) {
		msg += "有<font color='blue'>【" + result.stockCheckDeficit.length
				+ "】</font>项商品<font color='red'>亏损</font></br>";
		$.each(result.stockCheckDeficit, function(i, value) {
			msg += "【" + value.name + "】";
		});
		msg += "</br></br>";
	}
	//else {
	//	msg += "没有亏损信息</br></br>";
	//}
	//未录入信息（将视为亏损）		
	if (result.stockIsNull && result.stockIsNull.length > 0) {
		msg += "未盘点商品" + result.stockIsNull.length
				+ "条,如果清算实际库存将保存为0！</br>";
		$.each(result.stockIsNull, function(i, value) {
			msg += "[" + value.name + "] ";
		});
		msg += "</br></br>";
	}
	msg += "<font color='red'>注意：</font>清算完成后将不允许修改实际库存，并将进行平仓操作！";
	Ext.MessageBox.show({
		title : '提示',
		minWidth : 280,
		minHeight : 250,
		msg : msg,
		buttons : Ext.MessageBox.YESNO,
		//icon : Ext.MessageBox.INFO,
		fn : function(button, text) {
			if (button == "yes") {
				//当点的是，测进行库存清算（生成报损报盈单）
				saveStockCheckKY();
			}
		}
	});
}
/**
 * 保存数据(异步)库存盘点盈亏表
 */
function saveStockCheckKY() {
	//var checkDate = $("#checkDate").val(); //盘点日期
	//var checkNo = $("#checkNo").val(); //库存盘点编号
	//var userId = $("#userId").val();//录入员ID（用户）
	//var empId = $("#empid").val();//清查人员ID（员工）
	//var storageId = $("#storageId").val(); //仓库ID

	var stockCheckId = $("#stockCheckId").val(); //库存盘点ID

	loadProgress();

	$.ajax({
		url : URL_PREFIX+"/stock/check/detail/stockLiquidate.do",
		type : "POST",
		data : {
			//"model.createTime" : checkDate,
			//"model.stockCheck.checkNo" : checkNo,
			//"model.stock.storage.id" : storageId,
			//"model.user.id" : userId,
			"model.stockCheck.id" : stockCheckId
		//"model.employee.id" : empId
		},
		//async : false, //默认为true异步，false同步
		dataType : "json",
		success : function(result) {
			Ext.MessageBox.hide(); //隐藏进度条
			var msg = "【清算失败】</br>";
			if (result.success) {
				//如果执行成功
				msg = "【清算成功】</br>";
				if (result.lossNo) {
					//document.getElementsByName("model.id")[tr.rowIndex].value = result.id;
					msg += "生成亏损单编号：<font color='red'>" + result.lossNo
							+ "</font>";
					msg += "</br></br>";
				}
				if (result.profitNo) {
					msg += "生成盈利单编号：<font color='red'>" + result.profitNo
							+ "</font>";
				}
				if (!result.lossNo && !result.profitNo) {
					msg += "未有损益单生成";
				}
			} else {
				msg = result.msg;
			}
			Ext.MessageBox.show({
				title : '提示',
				minWidth : 220,
				msg : msg,
				buttons : Ext.MessageBox.OK,
				fn : function(button, text) {
					if (button == "ok") {
						document.getElementById("index").submit();
					}
				}
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			Ext.MessageBox.hide(); //隐藏进度条
			Ext.MessageBox.show({
				title : '提示',
				//minWidth : 220,
				msg : XMLHttpRequest.responseText,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
	});
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
			interval : 300,//0.6s进度条自动加载一定长度
			duration : 120000,
			fn : function() {
				Ext.MessageBox.hide();//让进度条消失
				Ext.MessageBox.show({
					title : '提示',
					minWidth : 220,
					minHeight : 220,
					msg : '系统超时！',
					buttons : Ext.MessageBox.OK
				//icon : Ext.MessageBox.INFO,
				});
			}
		},
		closable : true
	});
}

/**
 * 改变录入状态为2(冻结录入按钮)
 */
function updateStockCheckStatus(){
	var stockCheckId = $('#stockCheckId').val();
	if(!stockCheckId){
		alert('请先录入实际库存后，再确定盘点！');
		return;
	}
	if(confirm('确认要提交本次库存盘点吗？(提交后将不允许追加或删除库存盘点商品)')){
    	//加载进度条
    	loadProgress();
    	$.ajax({
    		url : URL_PREFIX+"/stock/check/updateStockCheckStatus.do",
    		type : "POST",
    		data : {
    			"model.id" : stockCheckId,
    			"model.status" : '2'
    		},
    		dataType : "json",
    		success : function(result) {
    			if(result.success){
    				//清空商品名称或编码
    				$('#productName').val('');
    				$('#productCode').val('');
    				Ext.MessageBox.hide(); //隐藏进度条
    				document.getElementById("index").submit();
    			}
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    			Ext.MessageBox.show({
    				title : '提示',
    				minWidth : 220,
    				msg : XMLHttpRequest.responseText,
    				buttons : Ext.MessageBox.OK,
    				icon : Ext.MessageBox.INFO
    			});
    		}
    	});
    }	
}

/**
* 删除库存盘点详单一条记录
*/
function remove(id){
    if(confirm('确认要删除本条记录吗？')){
    	//加载进度条
    	loadProgress();
    	$.ajax({
    		url : URL_PREFIX+"/stock/check/detail/removeStockCheckDetail.do",
    		type : "POST",
    		data : {
    			"model.id" : id
    		},
    		dataType : "json",
    		success : function(result) {
    			if(result.success){
    				Ext.MessageBox.hide(); //隐藏进度条
    				alert('删除成功');
    				document.getElementById("index").submit();
    			}
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    			Ext.MessageBox.show({
    				title : '提示',
    				minWidth : 220,
    				msg : XMLHttpRequest.responseText,
    				buttons : Ext.MessageBox.OK,
    				icon : Ext.MessageBox.INFO
    			});
    		}
    	});
    }	
}


















