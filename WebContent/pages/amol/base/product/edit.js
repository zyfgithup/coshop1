Ext.onReady(function() {
	var comboxWithTree = new Ext.form.ComboBox({
		store : new Ext.data.SimpleStore({
			fields : [],
			data : [ [] ]
		}),
		editable : false,
		width : 150,
		maxHeight : 600,
		mode : 'local',
		triggerAction : 'all',
		emptyText : '选择商品类型',
		tpl : '<div id="div-tree"></div>',
		selectedClass : '',
		onSelect : Ext.emptyFn
	// applyTo: 'test'
	});
	tree = new Ext.tree.TreePanel({
		// el:'tree-div',
		border : false,
		useArrows : true,
		autoScroll : true,
		animate : true,
		frame : false,
		width : 130,
		height : 300,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
			dataUrl : '/base/prosort/prosortTree.do?status=1'// 只查看状态为可用
		}),
		listeners : {
			click : function(node, e) {
				if (node.id != 0) {
					comboxWithTree.setValue(node.text);
					Ext.get("prosortId").dom.value = node.id;
					comboxWithTree.collapse();
				} else {
					// Ext.Msg.alert("操作提示","请选择所属地区.");
					Ext.get("prosortId").dom.value = "";
				}
			}
		}
	});
	var root = new Ext.tree.AsyncTreeNode({
		text : '所有商品类型',
		id : '0'
	});
	tree.setRootNode(root);
	comboxWithTree.on('expand', function() {
		tree.render('div-tree');
		tree.root.reload();
	});
	comboxWithTree.render('comboxWithTree');
	//var psname = '${model.prosort.name}';
	//if(psname != ''){
	//	comboxWithTree.setValue(psname);
	//}else{
	//	comboxWithTree.setValue("编辑的默认值");
	//}
});
