<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>一般錯誤代號維護-編輯</title>
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
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/gl_err_code" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<table>
						<tr>
							<td class="header necessary" style="width: 50%"><span>一般錯誤代號</span></td>
							<td><html:text styleId="ERROR_ID" property="ERROR_ID" readonly="true" style="background-color: transparent;border: 0;" size="4" maxlength="4" styleClass="validate[required,notChinese] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>錯誤說明</span></td>
							<td><html:text property="ERR_DESC"  size="16" maxlength="16" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr>
							<td colspan="3" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
								<label class="btn" id="back" onclick="back()"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
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
				if(str == "update"){
					$("#formID").validationEngine({promptPosition: "bottomLeft"});
					if(!$('#formID').validationEngine('validate')){
						$("#formID").validationEngine('detach');unblockUI();return;
					}
				}
				if(str == "delete" && !confirm("是否確定刪除?")){
					return false;
				}
				blockUI();
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
				setDatePicker("#ACTIVE_DATE", 0);
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="gl_err_code_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(){
				$("#formID").validationEngine('detach');
				onPut('back');
			}
		</script>
	</body>
</html>
