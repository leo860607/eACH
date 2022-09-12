<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>清算行查詢</title>
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
	<body onload="">
		<div id="container">
			<div id="opPanel">
				<html:form action="/bank_group">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<fieldset>
						<legend>查詢條件</legend>
						<table width="100%">
							<tr>
								<td width="100px">清算行代號</td>
								<td>
									<html:select styleId="CTBK_ID" property="CTBK_ID" >
										<html:option value="none">==請選擇清算行代號==</html:option>
										<html:optionsCollection name="bank_group_form" property="ctbkIdList" label="label" value="value"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: center">
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
				init();
	        });
			
			function init(){
				initSelect();
			}
			
			function onPut(str){
				var text = $("#CTBK_ID").find(":selected").text()+'';
				if($("#CTBK_ID").find(":selected").val() != "none"){
					var strAry = text.split("-");
					var p = getParentWindowWithDialog();
					var arg = window.dialogArguments+'';
					p.$("#"+arg+"_ID").val(strAry[0].substring(0, 7));
					p.$("#"+arg+"_NAME").val(strAry[1].replace(/ /g, ""));
// 					20150122 add by hugo
					p.checkCR_LINE(strAry[0].substring(0, 7));
				}
				
		// 		window.close();//ie 會壞掉
				$dlg.dialogWindow.dialog('close');
			}	
		
			function initSelect(){
				var select = $("#CTBK_ID");
				var val = select.find(":selected").val();
			}
		</script>
	</body>
</html>
