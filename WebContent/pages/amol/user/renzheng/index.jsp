<%@ page language="java" import="com.systop.common.modules.region.RegionConstants" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>认证审核</title>
    <%@include file="/common/taglibs.jsp"%>
    <%@include file="/common/ec.jsp"%>
    <%@include file="/common/extjs.jsp"%>
    <%@include file="/common/meta.jsp"%>
    <script type="text/javascript">
        function refresh() {
            ECSideUtil.reload('ec');
        }
    </script>
    <!-- 地区 -->
    <script type="text/javascript" src="${ctx}/pages/admin/region/regionCombox.js"> </script>
    <script type="text/javascript" src="${ctx}/pages/amol/base/prosort/mersortCombox.js"></script>
    <link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
</head>
<script type="text/javascript">
    function removeObj(id){
        if(confirm("确定删除该认证？")){
            location.href="remove.do?model.id="+id;
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
<body>
<div class="x-panel">
    <div class="x-panel-header">认证审核</div>
    <div class="x-toolbar">
        <s:form action="index" theme="simple">
            <table width="100%">
                <tr>
                    <td>
                        <table>
			        <s:form action="index" theme="simple">
						<td align="right">认证名：</td>
							<td align="left" colspan="3">
						      <s:textfield id="name" name="model.user.name" cssClass="prosortCheck"/>
							</td>
			        	<td>
                        <td>
                            地区：
                        </td>
                        <td>
                            <span id='comboxWithTree'></span>
                        </td>
                        <td>
                            <s:hidden id="regionId" name="regionId"/>
                        </td>
                        <td align="right">认证类型：</td>
                        <td align="left" colspan="3">
                            <s:select list="#{'1':'保险认证','2':'加油车','3':'企业','4':'实名认证','5':'维修厂','6':'加油站认证'}" name="model.rzType" cssClass="required" cssStyle="width:200px;"></s:select>
                        </td>
                        <td align="right">认证状态：</td>
                        <td align="left" colspan="3">
                            <s:select list="#{'0':'未审核','1':'认证通过','-1':'未通过'}" name="model.iftg" cssClass="required" cssStyle="width:200px;"></s:select>
                        </td>
                        <td>
			        		<s:submit value="查询" cssClass="button"></s:submit>
			        	</td>
			         </s:form>
		         </table>
                    </td>
                    <td style="padding-right:10px;" align="right">
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
                  xlsFileName="认证审核.xls"
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
                <ec:column width="100" property="user.loginId" title="用户名"  ellipsis="true"/>
                <ec:column width="125" property="user.name" title="认证名" ellipsis="true"/>
                <ec:column width="70" property="_type" title="认证类型" style="text-align:center;">
                    <c:if test="${item.rzType=='1'}">
                        保险认证
                    </c:if>
                    <c:if test="${item.rzType=='2'}">
                        加油车认证
                    </c:if>
                    <c:if test="${item.rzType=='3'}">
                        企业认证
                    </c:if>
                    <c:if test="${item.rzType=='4'}">
                        实名认证
                    </c:if>
                    <c:if test="${item.rzType=='5'}">
                        维修厂认证
                    </c:if>
                    <c:if test="${item.rzType=='6'}">
                        加油站认证
                    </c:if>
                </ec:column>
                <ec:column width="70" property="_state" title="认证状态" style="text-align:center;" >
                    <c:if test="${item.iftg=='0'}">
                        未审核
                    </c:if>
                    <c:if test="${item.iftg=='1'}">
                        审核通过
                    </c:if>
                    <c:if test="${item.iftg=='-1'}">
                        审核未通过
                    </c:if>
                </ec:column>
                <ec:column width="350" property="_0" title="操作" viewsAllowed="html" style="text-align:center" sortable="false">
                    <a href="lookForFile.do?model.id=${item.id}" >查看认证资料</a> |
                    <a href="javascript:removeObj(${item.id})" >删除</a>
                </ec:column>
            </ec:row>
        </ec:table>
    </div>
</div>
</body>
</html>