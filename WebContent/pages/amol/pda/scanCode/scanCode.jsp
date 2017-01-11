<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator,com.systop.amol.sales.model.SalesDetail"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<title>扫码</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<script type="text/javascript">
//条形码
function scanCodes(){
	this.str;
}

/**
 * 判断条形码是否有重复的，有则提示
 * 没有则将   条形码集合（条形码之间用逗号隔开）返回，并关闭浏览器窗口
 */
function closeWindow(){
	//条形码集合（条形码之间用逗号隔开）
	var s = new scanCodes();
	s.str = "";
	
	//条形码数组集合
	var arr = new Array();
	
	var barcodeList = document.getElementsByName("barcodes");
	for(var t=0;t <barcodeList.length;t++){
		var barcode = barcodeList[t].value;
		if(barcode.length != 0){
			arr[t] = barcode;
			if(barcode != null && barcode.length > 0){
				s.str += barcode+",";
			}
	    }else{
	    	alert("商品条形码不能为空！");
	    	return false;
	    }
	}
	if(IsArrayElementRepeat(arr)){
		s.str="";
		window.returnValue = s;
		alert("有重复的商品条形码，请检查！");
	}else{
		window.returnValue = s;
		window.close();
	}
}

/**
 * 一个使用Javascript编写的判断数组中是否有重复元素的程序。在进行判断时，可以进行文本比较，也可以进行二进制比较。
 * function IsArrayElementRepeat(arr) //判断一个数组中的元素是否存在重复的情况，如果存在重复的元素，返回true，否则返回false。
 */
function IsArrayElementRepeat(arr){
	  var bRepeat=false;
	  if(arr!=null && arr!=undefined && typeof(arr)=="object")
	  {
		   var i;
		   for(i=0;i<arr.length;i++)
		   {
			   var y;
			   for(y=0;y<arr.length;y++)
		       {
				   if(i != y){
					   bEqual=(arr[i]==arr[y]);//文本比较
					   if(bEqual){
						   bRepeat=true;
						   return bRepeat;
					   }
				   }
		       }
		   }
	  }
	  return bRepeat;
}

/**
 * 初始化
 */
function csh(){
	var count = <%=request.getParameter("count") %>;
	for(i = 0;i < count; i++){
		var barcodeTabObject = document.getElementById("barcodeTab");
		var tr = barcodeTabObject.insertRow(barcodeTabObject.rows.length);
		var td1 = tr.insertCell(0);
		var td2 = tr.insertCell(1);
		td1.align = "right";
		td1.innerHTML = "商品条形码：";
		td2.innerHTML = "<input name='barcodes' type='text' size='38'/>";
	}
}
</script>
</head>
<body onload="csh();">
<div class="x-panel" style="width: 100%">
<div class="x-panel-header">扫描商品条形码</div>
<div align="center" style="width: 100%">
<form>
	<fieldset style="width: 98%; padding:10px 10px 10px 10px;">
	<legend>扫描商品条形码</legend>
		<center>
		<table width="50%" border="1" id="barcodeTab">
			<tr bgcolor="#E6F4F7"  align="center"><td colspan="2">条形码</td></tr>
			   <%
			      Object object = request.getAttribute("sd");
			      if(null != object && !("".equals(object.toString()))){
				      for( Iterator   it = (Iterator)object;  it.hasNext(); )
				      {
				    	  SalesDetail s = (SalesDetail)it.next();
			   %>
				
						   <tr> 
							 <td>
							 	<input name="barcodes" type="text"/>
							 </td>
						     <td>
						     	<input type='button' value='删除' onclick='removeRow(this.parentNode.parentNode);'/> | 
			                    <input type='button' value='扫码' onclick='scanCode(this.parentNode.parentNode);'/>
						     	<input type='text' name='codes' id='codes'/>
						     </td>
						   </tr>
				    
				  
			   <%}} %>
		</table>
	</center>
	<table width="600px" style="margin-bottom: 10px;">
		<tr>
			<td style="text-align: center;">
			  <input type='button' value='确定' onclick="closeWindow();" class="button"/>
			  &nbsp;&nbsp;
			  <input type="reset" value='重置' class="button"/>
			</td>
		</tr>
	</table> 
	</fieldset>
</form>
</div>
</div>
</body>
</html>