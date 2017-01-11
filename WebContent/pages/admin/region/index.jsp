<%@ page language="java" contentType="text/html; charset=UTF-8"
    import="com.systop.common.modules.region.RegionConstants" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp" %>
<%@include file="/common/extjs.jsp" %>
<title>地区列表</title>
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
<script type="text/javascript">
var tree;
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
            dataUrl:'${ctx}/admin/region/regionTree.do?regionId=null'
        }),
        listeners: {
        	"click" : function(node, e) {
        		if (node.expanded){
        			node.collapse();
        		}else{
        			node.expand();
        		}
        	},
        	"contextmenu": function(node, e) {   //点击右键出现tree菜单   
    			node.select();//点击右键同时选中该项   
    			e.preventDefault();   
    			var treeMenu = new Ext.menu.Menu([   
    		    	{ text: "添加", iconCls: "add", pressed: true, handler: function() { editWin(node, 'add'); } },
    				{ text: "修改", iconCls: "edit", pressed: true, handler: function() { editWin(node, 'edit'); } },   
    				{ text: "删除", iconCls: "delete", pressed: true, handler: function() { remove(node); } }
    				
    			]);  
    			treeMenu.showAt(e.getPoint());   
    		}
        }
    });

    // set the root node
      
    var root = new Tree.AsyncTreeNode({
        text: '地区',
        id:'0'
    });
    tree.setRootNode(root);
    tree.render();
    root.expand();
});

var win; //定义win, 不重复创建
var optionType; //操作类型[添加、修改]

/**
 * node:编辑的节点(如果是添加操作，node 为父节点)
 * type: add / edit
 */
function editWin(node, type){
	optionType = type;
	if(!win){
		var form = new Ext.form.FormPanel({
			url:'${ctx}/admin/region/save.do',
	        baseCls: 'x-plain',
	        labelWidth: 35,
	        defaults: { width: 200 },
	        defaultType: 'textfield',
			items: [{
				xtype: 'hidden',
				id:'pid',
				name: 'parentId'
			},{
				xtype: 'hidden',
				id:'rid',
				name: 'model.id'
			},{
				fieldLabel: '名称',
				id:'name',
				name: 'model.name'
			},{
				fieldLabel: '种类',
				id:'ckYname',
				name: 'model.ckYname'
			},{
				fieldLabel: '油价',
				id:'ckPrice',
				name: 'model.ckPrice'
			}],
	        buttons: [{
	            text: '保存',
	            handler:function(){
	            	var baseForm = form.getForm();
	            	var regionName =  baseForm.findField("name").getValue();
	            	if(!regionName){
	            		alert('地区名称不允许为空');	
	            		return;
	            	}	            		
	            	baseForm.submit({//提交表单
                        waitMsg:'保存中,请稍后...', //表单提交等待过程中,出现的等待字符
                        success : function(submitForm, action){
                            var jsonData = Ext.util.JSON.decode(action.response.responseText);
                            if (optionType == "add"){
	                            var pid = baseForm.findField("pid").getValue();
                            	var parent = tree.getNodeById(pid);
                            	var sub = new Ext.tree.TreeNode({
				  			        text : jsonData.text,
				  			        id   : jsonData.id,
				  			        leaf : true
				  			     });
                            	parent.appendChild(sub);
                            	tree.expandPath(parent.getPath());
                            	sub.getUI().show();
                            } else if (optionType == "edit"){
                            	var rid = baseForm.findField("rid").getValue();
                            	var self = tree.getNodeById(rid);
                            	self.setText(jsonData.text);
								reloadTree();
                            } else {//其他方式
                            	//no sub
                            }
                            baseForm.reset(); //表单中所有数据置空
                            win.hide();                 //表单隐藏
                         },
                         failure : function(submitForm, action){
                        	 var jsonData = Ext.util.JSON.decode(action.response.responseText);
                             //返回失败
                             Ext.Msg.alert('错误提示',jsonData.msg);
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
	        width:300,
	        height:160,
	        modal : true,
	        closeAction:'hide',
	        bodyStyle:'padding:5px;',
	        buttonAlign:'center',
	        items: form
	    });
  	}
  	win.show();
  	if (type == "add"){
	  	win.setTitle("地区添加");
  	}else{
  		win.setTitle("地区编辑");
  	}
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
	var ckYname = Ext.get("ckYname");
	var ckPrice = Ext.get("ckPrice");
	if (!pid || !rid || !name){
		return;
	} else {
	    if (type == 'add') {
			pid.dom.value = node.id;
		} else if (type == 'edit') {
		   rid.dom.value = node.id;
		   name.dom.value = node.text;
			ckYname.dom.value = node.attributes.ckName;
			ckPrice.dom.value = node.attributes.ckPrice;
		} else {
		   //no sub
		}
	}
}

/*
 * 删除节点，如果包含下级节点禁止删除
 */
function remove(node){
	if (node.isLeaf()){
		Ext.MessageBox.confirm("提示","您确定要删除该项吗?", function(button,text){   
			if(button=='yes'){
				Ext.Ajax.request({
					url: '${ctx}/admin/region/remove.do',
				  	method:'post',
				  	params:{
				  		'model.id' : node.id
				  	},
				  	success: function(response, options){
				  		var parnetNode = node.parentNode; 
				  		node.remove();
				  		if (parnetNode.childNodes.length == 0){
				  			parnetNode.leaf = true;
				  		}
				  	},
				  	failure: function(){
				  	 
				  	}
				});				
			}
		});
	}else{
		Ext.Msg.alert('操作提示','请先删除下级地区数据!');
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
<div class="x-panel">
<div class="x-panel-header">
  <table width="100%">
    <tr>
      <td width="80" align="center"><b>地区列表</b></td>
      <td>
        <!--  
      	<span onclick="openTree()" style="cursor: pointer;">
      	<img src="${ctx}/images/icons/col-move-top.gif">打开
      	</span>
      	-->
      	<span onclick="reloadTree()" style="cursor: pointer;">
      	<img src="${ctx}/images/icons/col-move-bottom.gif">刷新
      	</span>
      </td>
      <!--  
      <td>
      	&nbsp;<a href="comboxRefJs.jsp" target="_blank"><b>树形列表选择器</b></a>
      </td>
      -->
    </tr>
  </table>
</div>
<div id="tree-div" style="overflow:auto; height:470px;width:250px;"></div>
<div id="editWin"></div>
</div>
</body>
</html>