<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>

<html>
<head>
<title>期初数据设置</title>

<script type="text/javascript">
//期初数据锁定
function initSet(){

    Ext.MessageBox.confirm('提示', '您确定要锁定期初数据吗<font color="red">【注：锁定后数据将冻结，不能进行修改和删除】</font>？', function(btn){
        if (btn == 'yes') {
        	//加载进度条
        	loadProgress();
        	
		      $.ajax({
		    	    url: '${ctx}/init/initSet.do',
			  		type: 'post',
			  		dataType: 'json',
			  		success: function(rst, textStatus){
			  	  		if(rst.result == "success"){ 
			  	  		    Ext.MessageBox.hide(); //隐藏进度条
		    		        Ext.MessageBox.show({
		    		             title:'提示',
		    		             minWidth:220,
		    		             msg:'<div style=\'width:180\';><br/>已成功设置期初数据！</div>',
		    		             buttons:Ext.MessageBox.OK,
		    		             icon:Ext.MessageBox.INFO,
		    		             fn:function (){document.getElementById("edit").submit();}
		    		        });		    		        
			  	  	  	}
			  	  		if(rst.result == "error"){	 
			  	  		    Ext.MessageBox.hide(); //隐藏进度条
		    		        Ext.MessageBox.show({
		    		             title:'提示',
		    		             minWidth:220,
		    		             msg:'<div style=\'width:180\';><br/>请您重新登录系统！</div>',
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

//期初数据解除锁定
function initUnlock(){

    Ext.MessageBox.confirm('提示', '您确定要解锁期初数据吗<font color="red">【注：解锁后将不能进行进销存的相关业务操作】</font>？', function(btn){
        if (btn == 'yes') {
        	//加载进度条
        	loadProgress();
        	
		      $.ajax({
		    	    url: '${ctx}/init/initUnlock.do',
			  		type: 'post',
			  		dataType: 'json',
			  		success: function(rst, textStatus){
			  	  		if(rst.result == "success"){ 
			  	  		    Ext.MessageBox.hide(); //隐藏进度条
		    		        Ext.MessageBox.show({
		    		             title:'提示',
		    		             minWidth:220,
		    		             msg:'<div style=\'width:180\';><br/>已成功解锁期初数据！</div>',
		    		             buttons:Ext.MessageBox.OK,
		    		             icon:Ext.MessageBox.INFO,
		    		             fn:function (){document.getElementById("edit").submit();}
		    		        });		    		        
			  	  	  	}
			  	  		if(rst.result == "error"){	 
			  	  		    Ext.MessageBox.hide(); //隐藏进度条
		    		        Ext.MessageBox.show({
		    		             title:'提示',
		    		             minWidth:220,
		    		             msg:'<div style=\'width:180\';><br/>请您重新登录系统！</div>',
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
		msg : '期初数据设置中,请耐心等待···',
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
<div class="x-panel-header" >期初数据设置</div>
<div><%@ include file="/common/messages.jsp"%></div>

<div class="x-toolbar">
      <table width="99%">
        <tr>
          <td>
          	<form id="edit" action="edit.do"  method="post">    
          	    <c:if test="${beginingInit eq '0'}">                
		            <input type="button" value="期初数据锁定" class="button" onclick="initSet()"/>
		        </c:if>
		        <c:if test="${beginingInit eq '1'}">   
		            <input type="button" value="期初数据解锁" class="button" onclick="initUnlock()"/>
		        </c:if>
           </form>
         </td>        
       </tr>
     </table>
</div>   

</div>
</body>
</html>