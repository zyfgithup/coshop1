<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>加油设置</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
        function remove(id){
            if (confirm("确认要删除规则吗?")){
                window.location.href="remove.do?model.id=" + id;
            }
        }
        Ext.onReady(function() {
            var pstree = new RegionTree({
                el : 'comboxWithTree',
                target : 'regionId',
                //emptyText : '选择地区',
                comboxWidth : 200,
                treeWidth : 195,
                url : '${ctx}/admin/region/regionTree.do?regionId=null',
                defValue : {id:'${regionId }',text:'${regionNameCun }'}
            });
            pstree.init();

        });
    </script>
    <!-- 地区 -->
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<body>
<div class="x-panel">
    <div class="x-panel-header">加油设置</div>
    <div class="x-toolbar">
        <s:form action="index" theme="simple">
            <table width="100%">
                <tr>
                    <td><table>
			        <s:form action="index" theme="simple">
                        <td>
                            地区：
                        </td>
                        <td>
                            <span id='comboxWithTree'></span>
                        </td>
			        	<td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
			        	</td>
			         </s:form>
		         </table>
                    </td>
                    <td style="padding-right:10px;" align="right">
                        <table>
                            <tr>
                                <td><a href="edit.do"><img src="${ctx}/images/icons/add.gif"/> 添加</a></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </s:form>
    </div>
    <div class="x-panel-body">
        <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
                  action="index.do"
                  useAjax="false"
                  doPreload="false"
                  xlsFileName="加油设置.xls"
                  pageSizeList="15,50,100"
                  editable="false"
                  sortable="false"
                  rowsDisplayed="15"
                  generateScript="true"
                  resizeColWidth="true"
                  classic="true"
                  width="100%"
                  height="350px"
                  minHeight="350"
                  toolbarContent="navigation|pagejump|pagesize|refresh|export|extend|status">
            <ec:row>
                <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
                <ec:column width="270" property="region.name" title="所属地区"  ellipsis="true">
                    ${item.region.parent.parent.parent.name }&nbsp;&nbsp;${item.region.parent.parent.name }&nbsp;&nbsp;${item.region.parent.name }&nbsp;&nbsp;${item.region.name }
                </ec:column>
                <ec:column width="100" property="kindName" title="柴油种类">
                </ec:column>
                <ec:column width="150" property="grPrice" title="油价（元/升）"/>
                <ec:column width="150" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                    <a href="edit.do?model.id=${item.id}" >编辑</a> |
                    <a href="remove.do?model.id=${item.id}" >删除</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>