<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>DataGrid Selection - jQuery EasyUI Demo</title>
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="easyui/demo.css">
    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
</head>
<body>
<h2>积分商品管理</h2>
<p>选择一个积分商品放到购物车.</p>
<div style="margin:20px 0;">
    <a href="#" class="easyui-linkbutton" onclick="getSelected()">单选</a>
    <a href="#" class="easyui-linkbutton" onclick="getSelections()">多选</a>
</div>
<table id="dg" class="easyui-datagrid" title="DataGrid Selection" style="width:700px;height:250px"
       data-options="singleSelect:true,url:'${ctx}/base/yyy/index.do',method:'post'">
    <thead>
    <tr>
        <th data-options="field:'stuName',width:80">学生姓名</th>
    </tr>
    </thead>
</table>
<div style="margin:10px 0;">
    <span>Selection Mode: </span>
    <select onchange="$('#dg').datagrid({singleSelect:(this.value==0)})">
        <option value="0">Single Row</option>
        <option value="1">Multiple Rows</option>
    </select>
</div>
<script type="text/javascript">
    function getSelected(){
        var row = $('#dg').datagrid('getSelected');
        if (row){
            $.messager.alert('Info', row.itemid+":"+row.productid+":"+row.attr1);
        }
    }
    function getSelections(){
        var ss = [];
        var rows = $('#dg').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
            var row = rows[i];
            ss.push('<span>'+row.itemid+":"+row.productid+":"+row.attr1+'</span>');
        }
        $.messager.alert('Info', ss.join('<br/>'));
    }
</script>
</body>
</html>