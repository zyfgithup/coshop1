<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>

<html>
<head>
<title>初始化数据</title>

<script type="text/javascript">
//提取经销商
function showAgent() {
	if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
		var cus=window.showModalDialog("${ctx}/pages/amol/user/agent/selector.jsp",null,"dialogWidth=800px;resizable: Yes;");
	    if(cus!=null){
	    	var agentName = document.getElementById("agentName") ;
	    	agentName.value=cus.name;
			var agentId = document.getElementById("agentId") ;
			agentId.value=cus.id;
		}
 	}else{
		window.open("${ctx}/pages/amol/user/agent/selector.jsp","","width=800px,height=520,menubar=no,toolbar=no,location=no,scrollbars=no,status=no,modal=yes");
	    this.returnAction=function(cus){
	    	if(cus!=null){
	    		var agentName = document.getElementById("agentName") ;
	    		agentName.value=cus.name;
	    		var agentId = document.getElementById("agentId") ;
	    		agentId.value=cus.id;
	    	}
		};
 	}
}

//查询该经销商下各个表的记录数
function queryData() {
	var agentName = document.getElementById('agentName').value;
    if (agentName == null || agentName == '') {
        Ext.MessageBox.show({
             title:'提示',
             minWidth:220,
             msg:'<div style=\'width:180\';><br/>请选择经销商！</div>',
             buttons:Ext.MessageBox.OK,
             icon:Ext.MessageBox.INFO
        });
        return false;
    }
}

//初始化该经销商下的各个表的数据
function initData(){
	var agentId = document.getElementById('agentId').value;
	var agentName = document.getElementById('agentName').value;
    if (agentName == null || agentName == '') {
        Ext.MessageBox.show({
             title:'提示',
             minWidth:220,
             msg:'<div style=\'width:180\';><br/>请选择经销商！</div>',
             buttons:Ext.MessageBox.OK,
             icon:Ext.MessageBox.INFO
        });
        return false;
    }

    Ext.MessageBox.confirm('提示', '您确定初始化【' + agentName + '】经销商的数据吗<font color="red">【注：初始化后数据将不能恢复】</font>？', function(btn){
        if (btn == 'yes') {
        	//加载进度条
        	loadProgress();
        	
		      $.ajax({
		    	    url: '${ctx}/init/initData.do',
			  		type: 'post',
			  		dataType: 'json',
			  		data: {agentId : agentId},
			  		success: function(rst, textStatus){
			  	  		if(rst.result == "success"){ 
			  	  		    Ext.MessageBox.hide(); //隐藏进度条
		    		        Ext.MessageBox.show({
		    		             title:'提示',
		    		             minWidth:250,
		    		             msg:'<div style=\'width:220\';><br/>已成功初始化数据！</div>',
		    		             buttons:Ext.MessageBox.OK,
		    		             icon:Ext.MessageBox.INFO,
		    		             fn:function (){document.getElementById("index").submit();}
		    		        });		    		        
			  	  	  	}
			  	  		if(rst.result == "error"){	 
			  	  		    Ext.MessageBox.hide(); //隐藏进度条
		    		        Ext.MessageBox.show({
		    		             title:'提示',
		    		             minWidth:250,
		    		             msg:'<div style=\'width:220\';><br/>初始化数据失败,请您重新初始化数据！</div>',
		    		             buttons:Ext.MessageBox.OK,
		    		             icon:Ext.MessageBox.INFO
		    		        });
                            return false;
			  	  	  	}			  	  		
			  		}
			     });
       }
    });
}

//加载进度条	
function loadProgress() {
	Ext.MessageBox.show({
		title : '提示',
		msg : '数据初始化中,请耐心等待···',
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
					msg : '系统超时,请您重新初始化数据！',
					buttons : Ext.MessageBox.OK
				});
			}
		},
		closable : true
	});
}
</script>
</head>
<body>
<div class="x-panel">
<div class="x-panel-header" >初始化数据</div>
<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form id="index" action="index.do" method="post">
		        <input type="button" id="btn1" class="button" value="选择经销商" onclick="showAgent()"/>
		        <s:textfield id="agentName" name="agentName" maxlength="18" class="required" style="width:250px" readOnly="true"/>				
			    <s:hidden id="agentId" name="agentId" />                
			    <input type="submit" value="查询" class="button" onclick="return queryData()"/>&nbsp;&nbsp;                      
		        <input type="button" value="初始化数据" class="button" onclick="initData()"/>
           </form>
         </td>
       </tr>
     </table>
</div>   
<div class="x-panel-body">
<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="index.do" 
		useAjax="false"
		doPreload="false" 
		pageSizeList="15,20,40" 
		editable="false"
		sortable="false" 
		rowsDisplayed="15"
		generateScript="true" 
		resizeColWidth="true" 
		classic="true" 
		width="100%"
		height="400px"
		minHeight="400"
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    <ec:column width="40" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="220" property="tableName" title="表名"  style="text-align:left"/>
		<ec:column width="150" property="counts" title="记录数"  style="text-align:right"/>

	</ec:row>
</ec:table>
</div>
</div>
</body>
</html>