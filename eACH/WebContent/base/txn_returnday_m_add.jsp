<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-新增</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				var result = '<bean:write name="txn_returnday_form" property="result"/>';
				var message = '<bean:write name="txn_returnday_form" property="msg"/>';
				showMessage(result, message);
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomRight"});
				$("#ACTIVE_DATE").val('<bean:write name="txn_returnday_form" property="ACTIVE_DATE"/>');
	        });
			
			function showMessage(result, message){
				if(fstop.isNotEmptyString(result)){
					var ac_key = result.split("_")[0];
					result = result.split("_")[1];
					if(ac_key == "add"){
						if(result == "true"){
							alert("儲存成功!");
							$("#TXN_ID").append("<option value='" + message + "' selected>" + message + "</option>");
						}else{
							alert("儲存失敗：" + message);
						}
					}
					
					if(result == "true"){
						//Return to search page
						onPut("back", false);
					}
				}
			}
			
			function onPut(id, isValidate){
				if(isValidate){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#ac_key").val(id);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function back(id){
				$("#TXN_ID").append("<option value='none' selected>NONE</option>");
				onPut(id, false);
			}
			
			function greaterEqualSysDate(field, rules, i, options){
				var sysDate = getTwSysDate('');
				if(parseFloat(field.val()) < parseFloat(sysDate) ){
					return "* 必須大於等於系統日 " + sysDate;
				}
			}
		</script>
	</head>
	<body onload="unblockUI()">
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">新增</a>
			</div>
			<br>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/txn_returnday" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 45%"><span>交易代號</span></td>
							<td>
								<html:select styleId="TXN_ID" property="TXN_ID" styleClass="validate[required] select">
									<html:option value="">==請選擇交易代號==</html:option>
									<logic:present name="txn_returnday_form" property="txnIdList">
										<html:optionsCollection name="txn_returnday_form" property="txnIdList" label="label" value="value"></html:optionsCollection> 
									</logic:present>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>退回天數</span></td>
							<td><html:text property="RETURN_DAY" size="3" maxlength="3" styleClass="validate[required,notChinese,custom[onlyNumberSp],maxSize[3]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>啟用日期(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,funcCall[greaterEqualSysDate]] text-input datepicker"></html:text></td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="add" onclick="onPut(this.id, true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	</body>
</html>
