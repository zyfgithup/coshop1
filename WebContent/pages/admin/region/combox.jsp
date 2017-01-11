<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>
<title>属性列表选择器</title>
</head>
<body>
<script type="text/javascript">


Ext.onReady(function(){
    var comboxWithTree  = new Ext.form.ComboBox({
        store: new Ext.data.SimpleStore({
        	fields : [],
        	data : [[]]
        }),
        editable:false,
        width : 200,
        maxHeight : 600,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'选择地区',
        tpl : '<div id="div-tree"></div>',
		selectedClass : '',
		onSelect : Ext.emptyFn
        //applyTo: 'test'
    });
        
    tree = new Ext.tree.TreePanel({
        //el:'tree-div',
        border : false,
    	useArrows:true,
        autoScroll:true,
        animate:true,
        frame       : false,   
        width       : 190,   
        height      : 300, 
        containerScroll: true, 
        loader: new Ext.tree.TreeLoader({
            dataUrl:'${ctx}/admin/region/regionTree.do'
        }),
        listeners : {
        	click : function(node, e){
        		if (node.id != 0){
	        		comboxWithTree.setValue(node.text); 
	        		Ext.get("region_id").dom.value=node.id;
	        		comboxWithTree.collapse(); 
        		} else {
        			//Ext.Msg.alert("操作提示","请选择所属地区.");
        		}
        	}
        }
    });      
    var root = new Ext.tree.AsyncTreeNode({
        text: '地区',
        id:'0'
    });
    
    tree.setRootNode(root);
    
    comboxWithTree.on('expand',function(){   
    	tree.render('div-tree');   
    	tree.root.reload();     
    });   
    comboxWithTree.render('comboxWithTree');   
    comboxWithTree.setValue("编辑的默认值");
});
</script>
<br>
<table align="center">
	<tr>
		<td>
			选择地区:</td>
		<td>
			<div id='comboxWithTree' style=""></div></td>
	</tr>
	<tr>
		<td>地区ID:</td>
		<td>
			<input type="text" id="region_id"></td>
	</tr>
</table>

</body>
</html>