<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-編輯</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
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
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define>
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/user_bank" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="USER_COMPANY"  />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 250px">用戶代號</td>
							<td>
								<html:text property="USER_ID" readonly="true" size="10" maxlength="10"  styleClass="validate[required] text-input lock"></html:text>
							</td>
							<td class="header necessary">用戶類型</td>
							<td>
								<logic:equal name = "each_user_form" property="USER_TYPE" value="B">銀行</logic:equal>
								<logic:equal name = "each_user_form" property="USER_TYPE" value="A">票交</logic:equal>
								<html:hidden property="USER_TYPE" ></html:hidden>
							</td>
						</tr>
						<tr>
							<td class="header necessary">狀態(是否啟用)</td>
							<td>
<%-- 								<html:checkbox property="USER_STATUS"  value="Y">啟用</html:checkbox> --%>
								是<html:radio property="USER_STATUS"  value="Y"></html:radio>
								否<html:radio property="USER_STATUS"  value="N"></html:radio>
							</td>
							<td class="header necessary">是否使用Ikey</td>
							<td>
<%-- 								<html:checkbox property="USE_IKEY" value="Y">使用Ikey</html:checkbox> --%>
								是<html:radio disabled="true" property="USE_IKEY"  value="Y"></html:radio>
								否<html:radio disabled="true" property="USE_IKEY"  value="N"></html:radio>
							</td>
						</tr>
						<tr>
							<td class="header">閒置逾時效期 (天)</td>
							<td>
								<html:text property="NOLOGIN_EXPIRE_DAY" readonly="true" size="3" maxlength="3" styleClass="validate[custom[integer]] lock"></html:text>
							</td>
							<td class="header">用戶閒置timeout時間(分)</td>
							<td>
								<html:text property="IDLE_TIMEOUT"  size="3" maxlength="3" styleClass="validate[custom[integer]] " ></html:text>
							</td>
						</tr>
						<tr>
							<td class="header">限制IP登入</td>
							<td>
								<html:text styleId="IP" property="IP" readonly="true" size="40" maxlength="200" styleClass="validate[mutilIpv4[;]] lock" ></html:text>
							</td>
							<td class="header">所屬群組</td>
							<td>
								<html:select property="ROLE_ID" >
								<html:option value="">==請選擇==</html:option>
								<html:optionsCollection name="each_user_form" property="role_list" label="label" value="value"></html:optionsCollection>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header">用戶說明</td>
							<td colspan="3">
								<html:text property="USER_DESC" readonly="true" size="40" maxlength="66" styleClass="validate[maxSize[66]] lock"></html:text>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<logic:equal  name="userData" property="s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
<!-- 								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label> -->
								</logic:equal>
								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function onPut(str,isValidate){
				$("#ac_key").val(str);
				$("#target").val('search');
				if(str == "delete"){
					if(!confirm("是否確定刪除")){
						return false;
					}
				}
				if(isValidate){
					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				$("input[name='USE_IKEY']").attr('disabled',false);
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker("#ACTIVE_DATE", 0);
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="each_user_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			
		</script>
	</body>
</html>
