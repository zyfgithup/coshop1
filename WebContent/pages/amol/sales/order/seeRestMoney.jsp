<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <script src="${ctx}/scripts/plug/echarts-2.2.7/build/dist/echarts.js"></script>
    <script src="${ctx}/scripts/plug/echarts-2.2.7/build/dist/echarts-all.js"></script>
    <title>明细页</title>
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
        $(function(){
            initTb();
        });
        //初始化饼状图
        function initTb(){
            var myChart = echarts.init(document.getElementById('pictureId'));
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '柱状图'
                },
                tooltip: {},
                legend: {
                    data:['分布']
                },
                xAxis: {
                    data: ["收入","返现","充值"]
                },
                yAxis: {},
                series: [{
                    name: '金额',
                    type: 'bar',
                    data: [<%=request.getParameter("income")%>,<%=request.getParameter("fanxian")%>,<%=request.getParameter("charge")%>,]
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

        }



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

    <div id="pictureId" style="width: 600px;height:400px;">

    </div>
</div>
</body>
</html>