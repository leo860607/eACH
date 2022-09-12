<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>使用者管理-新增</title>
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
				<html:form styleId="formID" action="/user_bank" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<input id="USER_COMPANY" name="USER_COMPANY" type="hidden" value="<bean:write name="userData" property="USER_COMPANY" />">
					<table>
						<tr>
							<td class="header necessary" style="width: 250px">用戶代號</td>
							<td>
								<html:text property="USER_ID" value="" size="10" maxlength="10"  styleClass="validate[required] text-input"></html:text>
							</td>
							<td class="header">用戶類型</td>
							<td>
							<bean:define id="user_type" type="String" name="userData" property="USER_TYPE"></bean:define>
							<logic:equal name="userData" property="USER_TYPE" value="B">銀行</logic:equal>
							<logic:equal name="userData" property="USER_TYPE" value="A">票交</logic:equal>
							<html:hidden property="USER_TYPE"  value="<%=user_type %>"></html:hidden>
							</td>
						</tr>
						<tr>
							<td class="header">狀態</td>
							<td>
								<html:checkbox property="USER_STATUS"  value="Y">啟用</html:checkbox>
							</td>
							<td class="header">是否使用Ikey</td>
							<td>
								<html:checkbox property="USE_IKEY" value="Y">使用Ikey</html:checkbox>
							</td>
						</tr>
						<tr>
							<td class="header">閒置逾時效期 (天)</td>
							<td>
								<html:text property="NOLOGIN_EXPIRE_DAY" value="" size="3" maxlength="3"></html:text>
							</td>
							<td class="header">用戶閒置timeout時間(分)</td>
							<td>
								<html:text property="IDLE_TIMEOUT" value="" size="3" maxlength="3"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header">限制IP登入</td>
							<td>
								<html:text property="IP" value="" size="20" maxlength="200"></html:text>
							</td>
							<td class="header">所屬群組</td>
							<td>
								<html:select property="ROLE_ID">
								<html:option value="">==請選擇==</html:option>
								<html:optionsCollection name="each_user_form" property="role_list" label="label" value="value"></html:optionsCollection>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header">用戶說明</td>
							<td colspan="3">
								<html:textarea property="USER_DESC" cols="80" rows="5" value="" ></html:textarea>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
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
				var msg = '<bean:write name="each_user_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(){
				$("#formID").validationEngine('detach');
				onPut('');
			}
			
			function chg_rest(value){
				if(window.console) console.log(value);
				$("input[name='REST_each_user']").val(value);
			}
		</script>
	</body>
</html>
