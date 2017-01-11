<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
  <title>经销商管理</title>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp"%>
<script type="text/javascript">
  function agents(){
	  this.id;
	  this.name;
  }
  function closeWindow(id,name){
	  var agent = new agents();
	  agent.id = id;
	  agent.name = name;
	  if (navigator.userAgent.indexOf("MSIE")>0){//IE判断
			window.returnValue = agent;
			window.close();
	  }else{
			window.parent.close();
			window.parent.opener.returnAction(agent);
	  }
  }
</script>
</head>
<body>
  <div class="x-panel">
    <div class="x-panel-header">分销商管理</div>
    <div class="x-toolbar">
      <table width="99%">
        <tr>
        <s:form action="selectAgent.do" theme="simple">
		  <td>
		          分销商名称：
		    <s:textfield id="name"  name="model.name" size="30" cssStyle="height:19px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
            <s:submit value="查询" cssClass="button"></s:submit>
          </td>
        </s:form>
       </tr>
     </table>
   </div>   
   <div class="x-panel-body">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit" 
	   action="index.do" 
	   useAjax="false"
	   doPreload="false" 
	   pageSizeList="15,20,30" 
	   editable="false"
	   sortable="false" 
	   rowsDisplayed="15"
	   generateScript="true" 
	   resizeColWidth="true" 
	   classic="true" 
	   width="100%"
	   height="380px"
	   minHeight="380"
	   toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	   <ec:row ondblclick="closeWindow('${item.id}','${item.name}')">
	     <ec:column width="30" property="_s" title="No." value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		 <ec:column width="90" property="name" title="分销商名称" style="text-align:center"/>
		 <ec:column width="90" property="region.name" title="所属地区" style="text-align:center" ellipsis="true"/>
		 <ec:column width="110" property="idCard" title="身份证号" style="text-align:center"/>
		 <ec:column width="40" property="sex" title="性别" style="text-align:center"/>
		 <ec:column width="80" property="mobile" title="电话" style="text-align:center"/>
		 <ec:column width="100" property="phone" title="手机" style="text-align:center"/>
	   </ec:row>
      </ec:table>
    </div>
  </div>
</body>
</html>