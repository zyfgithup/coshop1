<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<title>选择商品</title>
<style type="text/css">
 .isStatusNo{
 	font-weight: bold;
 	color: blue;
 }
 .isStatusYes{
 	color: black;
 	font-weight: normal;
 }
 .x-tree-node-icon{
	display: none;	 
 }
</style>
<script type="text/javascript">
var tree;

Ext.onReady(function(){
    var Tree = Ext.tree;
    tree = new Tree.TreePanel({
        el:'tree-div',
    	useArrows:true,
        autoScroll:true,
        animate:true,
        containerScroll: true,
        loader: new Tree.TreeLoader({
            dataUrl:'${ctx}/base/prosort/prosortTree.do?status=1'
        }),
        listeners: {
        //单击时
        "click":function(node, e){
        	document.getElementById('iframe_product').src="${ctx}/base/product/showIndex.do?model.prosort.id=" + node.attributes.id ;        	
        	//alert(node.attributes.id);
          }
        }

    });
	
    // set the root node
    var root = new Tree.AsyncTreeNode({
        text: '商品类型',
        id:'0'
    });
   
    tree.setRootNode(root);
    tree.render();
    root.expand();
    //tree.expandAll();
});
  

</script>
</head>
<body>
<div class="x-panel" style="width: 795px; margin-top: 0px; margin-right: auto; margin-bottom: 0px; margin-left: auto;">

<div class="x-panel-header" style="border: 0px;">
 <table width="100%">
    <tr>
      <td width="80" align="center"><b>选择商品</b></td>
      <td style="margin-left: 20px;padding-left: 20px"></td>
    </tr>
  </table>
</div>

<div>
 <table width="100%" >
  <tr>
	  <td>
		<div id="tree-div" 
			style="overflow: auto; height: 485px; width: 205px; border: 0px solid #c3daf9; float: left;">
		</div>
	  </td>
	  <td>	
		<div >
		<iframe name="iframe_product" id="iframe_product" src="${ctx}/base/product/showIndex.do?status=1" style="HEIGHT:485px; WIDTH: 586px;" frameborder="0" scrolling="no"></iframe>
		</div>
	   </td>
   </tr>
</table>
</div>

</div>
</body>
</html>