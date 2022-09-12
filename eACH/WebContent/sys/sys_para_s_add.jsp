<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系統參數維護-新增</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/newStyle.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
	</head>
	<body>
		<div id="container">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">新增</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/sys_para" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
<%-- 					<html:hidden property="SEQ_ID" styleId="SEQ_ID" value=""/> --%>
					<table>
						<tr>
							<td class="header necessary" style="width: 40%"><span>timeout時間 (分鐘)</span></td>
							<td><html:text styleId="TIMEOUT_TIME" property="TIMEOUT_TIME" value = "" size="10" maxlength="10" styleClass="validate[required] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>檔案上傳大小 (MB)</span></td>
							<td><html:text property="MAX_FILE_SIZE" value = "" size="10" maxlength="10" styleClass="validate[required] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>票交所標準回應時間（秒）</span></td>
							<td><html:text property="TCH_STD_ECHO_TIME" value = "" size="10" maxlength="10" styleClass="validate[required] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>參加單位標準回應時間（秒）</span></td>
							<td><html:text property="PARTY_STD_ECHO_TIME" value = "" size="10" maxlength="10" styleClass="validate[required] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>單筆標準完成時間（秒）</span></td>
							<td><html:text property="TXN_STD_PROC_TIME" value = "" size="10" maxlength="10" styleClass="validate[required] text-input"></html:text></td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanForm(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back"  onclick="back()"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
			<br>
		</div>
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function onPut(str){
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker("#ACTIVE_DATE", 0);
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="sys_para_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(){
				$("#formID").validationEngine('detach');
				onPut('');
			}
		</script>
	</body>
</html>
