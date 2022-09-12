<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>分行查詢</title>
		<link rel="stylesheet" type="text/css" href="./css/newStyle.css">
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		</script>
	</head>
	<body onload="unblockUI()">
		<div id="container">
			<div id="opPanel">
				<html:form action="/bank_branch">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<fieldset>
						<legend>查詢條件</legend>
						<table width="100%">
							<tr>
								<td width="100px">總行代號</td>
								<td>
									<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)" >
										<html:option value="none">==請選擇總行代號==</html:option>
										<html:optionsCollection name = "bank_branch_form" property="bgIdList" label="label" value="value"/>
									</html:select>
								</td>
								<td id="td_branchId"  class="header" style="display: none;"  width="100px">分行代號</td>
								<td id="td_branchId" style="display: none;">
									<html:select styleId="BRBK_ID" property="BRBK_ID" style="display:none"></html:select>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="text-align: center">
									<label class="btn" id="search" onclick="onPut(this.id)">&nbsp;確定</label>
									<label class="btn" id="cancel" onclick ="$dlg.dialogWindow.dialog('close');">&nbsp;取消</label>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
		</div>
	
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
	        });
			
			function init(){
				initSelect();
				alterMsg();
			}
			function alterMsg(){
				var msg = '<bean:write name="bank_branch_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			function onPut(str){
				var text = $("#BRBK_ID").find(":selected").text()+'';
				if($("#BRBK_ID").find(":selected").val() != "none"){
					var strAry = text.split("-");
					var p = getParentWindowWithDialog();
					var arg = window.dialogArguments+'';
					p.$("#"+arg+"_ID").val(strAry[0].substring(0, 7));
		// 			p.$("#"+arg+"_NAME").html(strAry[1]);
					p.$("#"+arg+"_NAME").val(strAry[1]);
				}
				
		// 		window.close();//ie 會壞掉
				$dlg.dialogWindow.dialog('close');
			}	
		
			function getBrbk_List(bgbkId){
				if(bgbkId == 'none'){
					$("#BRBK_ID").children().remove();
					$("td[id^='td_branch']").hide();
				}else{
					var rdata = {component:"bank_branch_bo", method:"getBank_branch_List" , bgbkId:bgbkId  };
					fstop.getServerDataExII(uri, rdata, false,creatBrBkList);
				}
			}
			function creatBrBkList(obj){
				var select = $("#BRBK_ID");
				select.show();
				$("td[id^='td_branch']").show();
				select.children().remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
		// 			$("#BRBK_ID").children().remove();
		//			select.append($("<option></option>").attr("value", "none").text("===請選擇分行代號==="));
		// 			select.append($("<option></option>").attr("value", "").text("全部"));
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			function initSelect(){
				var select = $("#BGBK_ID");
				var val = select.find(":selected").val();
				if(fstop.isNotEmpty(val) && val!="none"){
					getBrbk_List(val);
					$("#BRBK_ID").show();
		
					$("#BRBK_ID").children().each(function() {
		// 			    alert(this.text);    //  文字
		// 			    alert(this.value);   //  值
					    if(this.value =='<bean:write name="bank_branch_form" property="BRBK_ID"/>' ){
					    	this.selected = true;
		// 			    	$(this).attr("selected", "true");
					    }
					});
		
				}
			}
		</script>
	</body>
</html>
