<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.security.user.model.User"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp" %>
<%@include file="/common/extjs.jsp" %>
<style type="text/css">
 .add{
 	background-image: url("${ctx}/images/icons/add.gif");
 }
 .edit{
 	background-image: url("${ctx}/images/icons/cog.png");
 }
 .delete{
 	background-image: url("${ctx}/images/icons/remove.png");
 }
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收支类别管理</title>
<script type="text/javascript">
var tree;
function aa(){
	this.id;
	this.name;
	}
	function closeWindow(id,name){
	var a=new aa();
	a.id=id;
	a.name=name;

	window.returnValue = a;
	window.close();
	}
Ext.onReady(function(){
    // shorthand
    var Tree = Ext.tree;
    tree = new Tree.TreePanel({
        el:'tree-div',
    	useArrows:true,
        autoScroll:true,
        animate:true,
        containerScroll: true,
        loader: new Tree.TreeLoader({
            dataUrl:'${ctx}/finance/costSort/costSortTree.do?type=${type}'
        })
    	<s:if test="model.user.beginningInit == 1">
    	,
        listeners: {
        "dblclick":function(node, e){
        	editWin(node, 'add');
        },
        //单击时
        "click":function(node, e){
        	//alert(node.attributes.text);
        	closeWindow(node.attributes.id,node.attributes.text);
        	
        	//alert(node.attributes.name);
        	//alert(e.attributes);
        	
        	///if (node.expanded){
    			//node.collapse();
    		//}else{
    			//node.expand();
    		//}
        },
        "contextmenu": function(node, e) {   
    		//点击右键出现tree菜单   
    		node.select();//点击右键同时选中该项   
    		//e.preventDefault();   
    		var treeMenu = new Ext.menu.Menu   
    		([   
    		    { text: "添加", iconCls: "add", pressed: true, handler: function() { editWin(node, 'add'); } },
    			{ text: "修改", iconCls: "edit", pressed: true, handler: function() { editWin(node, 'edit'); } },   
    			{ text: "删除", iconCls: "delete", pressed: true, handler: function() { remove(node.id, node); } }
    		]);  
    		treeMenu.showAt(e.getPoint());   
    	} }
    	</s:if>
    });
	
    // set the root node
    var root = new Tree.AsyncTreeNode({
        text: '收支类别',
        id:'0'
    });
   
    tree.setRootNode(root);
    tree.render();
    root.expand();
});
  
var win; //定义win, 不重复创建
var typeNow; //防止win只创建一次从而type无法改变问题

function editWin(node, type){
	if(node.id == 0 && type != 'add'){
		alert("不允许修改或删除顶级收支类别【" + node.text + "】");
		return;
	}
	typeNow = type;
	if(!win){
		var form = new Ext.form.FormPanel({
			url:'${ctx}/finance/costSort/asynchronousSave.do',
			id:'myForm',
	        //baseCls: 'x-plain',
	        //labelWidth: 35,
	        //defaults: { width: 200 },
	        //defaultType: 'textfield',
	        //width: 200 ,
	    	region: 'east',
			frame: true,
			labelWidth: 35,
			width: 400,
			defaults: { width: 200 },
			//autoHeight: true,
	        items: [{
	        	xtype: 'hidden',
	        	fieldLabel: 'pid',
	            id:'pid',
	            name: 'parentId'
	        },{
	        	xtype: 'hidden',
	        	fieldLabel: ' id',
	            id:'rid',
	            name: 'model.id'
	        },{
	        	xtype: 'hidden',
	        	fieldLabel: '保存收支类型',
	            id:'type',
	            name: 'model.type'
	        },{
	        	xtype: 'textfield',
	            fieldLabel: '名称',
	            id:'name',
	            name: 'model.name'
	        },{   
	        	fieldLabel: '收支类型',   
	        	name: 'saveType',
	        	id: 'saveType',
	        	xtype: 'combo',   
	        	store: new Ext.data.SimpleStore({   
	        	fields: ['value','text'],   
	        	data: [['1','收入'],['2','支出']]
	        	}),   
	        	emptyText: '收入',   
	        	mode: 'local',   
	        	triggerAction: 'all',   
	        	valueField: 'text',   
	        	displayField: 'text',   
	        	readOnly: true  
	        	},{
	        	fieldLabel: '描述',   
	        	name: 'model.descn',   
	        	hiddenName: 'descn',   
	        	xtype: 'textarea',
	        	id: 'descn',
	        	height: 100
	        }],
	        buttons: [{
	            text: '保存',
	            handler:function(){
	            	var baseForm = form.getForm();
					var spsortName = baseForm.findField("name").getValue();
					var spsortType = baseForm.findField("saveType").getValue();
					if( spsortName == null || spsortName == ""){
            			alert("收支类别名称不允许为空！");
            			return;
            		}
					if(spsortType == null || spsortType == "" ){
            			alert("请选择收入类型,不允许为空！");
            			return;    			
            		}
					//设置status
					baseForm.findField("type").setValue(spsortType == "收入"?"1":"2");
	            	baseForm.submit({//提交表单
	            		waitMsg:'保存中,请稍后...', //表单提交等待过程中,出现的等待字符
                        success : function(submitForm, action){
                            var jsonData = Ext.util.JSON.decode(action.response.responseText);
                            if (typeNow == "add"){
                            	var info = jsonData.info;
                            	if(info){
                            		alert(info);
                            		return;
                            	}
	                            var pid = baseForm.findField("pid").getValue();
                            	var parent = tree.getNodeById(pid);
                            	var textName = jsonData.text + " ["+ spsortType + "]";
                            	var sub = new Ext.tree.TreeNode({
				  			        text  : textName,
				  			        id    : jsonData.id,
				  			        leaf  : true,
				  			        descn : Ext.get('descn').dom.value,
				  			        type  : baseForm.findField('type').getValue()
				  			      	//cls   : baseForm.findField('type').getValue() == 1 ? "isStatusYes" : "isStatusNo"
				  			     });
                            	parent.appendChild(sub);
                            	tree.expandPath(parent.getPath());
                            	
                            	sub.getUI().show();
                            	//parent.expand();
                           		//sub.enable();
                           		//tree.root.reload();
                            } else if (typeNow == "edit"){
                            	var info = jsonData.info;
                            	if(info){
                            		alert(info);
                            		return;
                            	}
                            	var rid = baseForm.findField("rid").getValue();
                            	var self = tree.getNodeById(rid);
                            	tree.getNodeById(rid).attributes.descn = jsonData.descn;
                            	tree.getNodeById(rid).attributes.type = jsonData.type;
                            	self.setText(jsonData.text+ " ["+ spsortType + "]");
                            	//self.getUI().addClass(tree.getNodeById(rid).attributes.status == 1 ? "isStatusYes" : "isStatusNo");
                            	self.getUI().show();
                            } else {
                            	//no sub
                            }
                            baseForm.reset(); //表单中所有数据置空
                            win.hide();                 //表单隐藏
                         },
                         failure : function(submitForm, action){
                                   //返回失败
                             //alert("failure");
                             alert("保存失败！请与管理员联系！\n" + action.response.responseText);
                         }
	            	});
	            }
	        },{
	            text: '关闭',
	            handler:function(){
	            	win.hide();
	            }
	        }]
	    });
	    win = new Ext.Window({
	        el:'editWin',
	        layout:'fit',
	        width:320,
	        height:270,
	        title:'收支类别管理',   
	        modal : true,
	        closeAction:'hide',
	        bodyStyle:'padding:10px;',
	        buttonAlign:'center',
	        items: form
	    });
  	}
  	win.show();
  	setValue(node, type);
}

/**
 * isAdd:true, 如果是添加，id代表parentId
 * isAdd:false,代表修改，id代表自身
 */
function setValue(node, type){
	var pid = Ext.get("pid");
	var rid = Ext.get("rid");
	var name = Ext.get("name");
	var descn = Ext.get("descn");
	//得到表单中的下拉列表
	var cboType = win.items.get("myForm").findById("saveType");//Ext.get("status");
	if (!pid || !rid || !name || !descn){
		return;
	} else {
	    if (type == 'add') {
	    	name.dom.value = '';
	    	//添加初始化表单中状态选项内容,将继承上级选项
	    	cboType.setValue(node.attributes.type=="1"?"收入":"支出");
			pid.dom.value = node.id;
		} else if (type == 'edit') {
		   rid.dom.value = node.id;
		   //去掉空格以及后缀
		   name.dom.value = $.trim(node.text.replace("[收入]","").replace("[支出]",""));
		   descn.dom.value = node.attributes.descn==null?"":node.attributes.descn;
		   cboType.setValue(node.attributes.type=="1"?"收入":"支出");
		} else {
		   //no sub
		}
	}
}
/**
 * 删除节点，不允许包含子节点
 */
function remove(id, node){
	if(node.isLeaf()){
		//Ext.MessageBox.confirm("提示","您确定要删除该项吗?", function(button,text){   
		//if(button=='yes'){
		if (confirm("您确定要删除该项吗？")){
			Ext.Ajax.request({
				url:'${ctx}/finance/costSort/remove.do',
				params:
		        {
					'model.id':id
		        },
		        success : function(response,options){
		        	var jsonData = Ext.util.JSON.decode(response.responseText);
		        	var msg = jsonData.msg;
		        	alert(msg);
		        	if(jsonData.success){
			        	node.remove();
			        	node.getUI().show();			        		
		        	}
		        },
		        failure : function(response,options){
		            //返回失败
			      //alert("failure");
			      alert("删除失败！\n" + response.responseText);
			  }
			});
		}
		//});
	}else{
		alert('请先删除下级商品类型数据!');
	}
}

function openTree(){
	if(!tree){
		alert('创建Tree失败');
	}else{
		tree.expandAll();
	}
}

function reloadTree(){
	if(!tree){
		alert('创建Tree失败');
	}else{
		tree.root.reload();
	}
}

</script>
</head>
<body>
<div class="x-panel" style="width: 100%">
<%User user = (User)request.getAttribute("user");%>
<div><%@ include file="/common/messages.jsp"%></div>
<div class="x-panel-header">
 <table width="100%">
    <tr>
      <td width="80" align="center"><b>收支类别管理</b></td>
      <td style="margin-left: 20px;padding-left: 20px">
      	<span onclick="openTree()" style="cursor: pointer;">
      	<img src="${ctx}/images/icons/col-move-top.gif">打开
      	</span>
      	<span onclick="reloadTree()" style="cursor: pointer;">
      	<img src="${ctx}/images/icons/col-move-bottom.gif">刷新
      	</span>
      </td>
      <c:if test="${user.type=='agent'}">
      <td align="right">
      	<a href="${ctx}/finance/costSort/edit.do"><img
			src="${ctx}/images/icons/add.gif" />&nbsp;新建收支类别&nbsp;</a>
      </td>
      </c:if>
    </tr>
  </table>
</div>
<div id="tree-div" style="overflow:auto; height:475px;width:250px;"></div>
<div id="editWin"></div>
</div>
</body>
</html>