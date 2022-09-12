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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-新增</title>
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
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">新增</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/each_user" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden styleId="USER_TYPE" property="USER_TYPE"  />
<%-- 					<html:hidden styleId="TMPUSER_TYPE" property="TMPUSER_TYPE"  /> --%>
					<table>
						<tr>
							<td class="header necessary" style="width: 250px">用戶代號</td>
							<td>
<%-- 								<html:text property="USER_ID" value="" size="10" maxlength="10"  styleClass="validate[required,minSize[10],maxSize[10]] text-input"></html:text> --%>
								<html:text property="USER_ID" value="" size="10" maxlength="10"  styleClass="validate[required,minSize[10],maxSize[10],userId[-]] text-input"></html:text>
							</td>
							<td class="header necessary">用戶類型</td>
							<td >
									<logic:equal name="each_user_form" property="USER_TYPE" value="A">
									票交所
									</logic:equal>
<%-- 									<logic:equal name="each_user_form" property="USER_TYPE" value="B">銀行</logic:equal> --%>
<%-- 								<html:select styleId="USER_TYPE" property="USER_TYPE" onchange="resetRoleId(this);initUserCompany()"> --%>
								<logic:equal name="each_user_form" property="USER_TYPE" value="B">
									<html:select styleId="TMPUSER_TYPE" property="TMPUSER_TYPE" onchange="resetRoleId(this);resetUserCom(this)">
									<html:option value="B">銀行</html:option>
									<html:option value="C">發動者</html:option>
									</html:select>
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header necessary" style="width: 250px">用戶所屬單位代號</td>
							<td id="user_t">
							<logic:equal name="each_user_form" property="USER_TYPE" value="A">
								0188888-臺灣票據交換所
								<html:hidden styleId="USER_COMPANY" property="USER_COMPANY" value="0188888"></html:hidden>
							</logic:equal>
<%-- 							<logic:equal name="each_user_form" property="USER_TYPE" value="B"> --%>
							<logic:notEqual name="each_user_form" property="USER_TYPE" value="A">
								<select id="USER_COMPANY" name="USER_COMPANY" class="validate[required]">
									<option value="">==請選擇所屬單位代號==</option>
								</select>
							</logic:notEqual>
<%-- 							</logic:equal> --%>
							</td>
							<td class="header necessary" >所屬群組</td>
							<td>
								<html:select styleId="ROLE_ID" property="ROLE_ID"  styleClass="validate[required]">
									<html:option value="">==請選擇所屬群組==</html:option>
									<html:optionsCollection name="each_user_form" property="role_list" label="label" value="value"></html:optionsCollection>
								</html:select>
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
								是<html:radio property="USE_IKEY"  value="Y"></html:radio>
								否<html:radio property="USE_IKEY"  value="N"></html:radio>
							</td>
						</tr>
						<tr>
							<td class="header">閒置逾時效期 (天)</td>
							<td>
								<html:text property="NOLOGIN_EXPIRE_DAY" value="" size="3" maxlength="3" styleClass="validate[custom[integer]]" ></html:text>
							</td>
							<td class="header">用戶閒置timeout時間(分)</td>
							<td>
								<html:text property="IDLE_TIMEOUT"  size="3" maxlength="3" styleClass="validate[custom[integer]]" ></html:text>
							</td>
						</tr>
						<tr>
							<td class="header">限制IP登入</td>
							<td colspan="3">
								<html:text styleId="IP" property="IP" value="" size="40" maxlength="200" styleClass="validate[mutilIpv4[;]]"></html:text>
								<font class="header" color="red">單一IP或多個IP(各IP間以分隔符號分開)</font>
							</td>
							
						</tr>
						<tr>
							<td class="header">用戶說明</td>
							<td colspan="3">
								<html:textarea property="USER_DESC" cols="80" rows="2" value="" styleClass="validate[maxSize[66]]"></html:textarea>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id , true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back"  onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function onPut(str , isValidate){
				$("#ac_key").val(str);
				$("#target").val('search');
				if(isValidate){
					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker("#ACTIVE_DATE", 0);
				initUserCompany();
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="each_user_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
				onPut(str,false);
			}
			
			function chg_rest(value){
				if(window.console) console.log(value);
				$("input[name='REST_each_user']").val(value);
			}
			function resetRoleId(obj){
				$(obj).find(":selected").val();
				var rdata = {component:"each_user_bo", method:"getRole_ListByRole_Type" , role_type:$(obj).find(":selected").val() } ;
				fstop.getServerDataExII(uri, rdata, false, showRoles);
			}
			function showRoles(obj){
				var select = $("#ROLE_ID");
				select.children().remove();
				select.append($("<option></option>").attr("value", "").text("==請選擇所屬群組=="));
				for( var key in obj ){
					select.append($("<option></option>").attr("value", obj[key].value).text(obj[key].label));
				}
			}
			
			function resetUserCom(obj){
				var user_type = $(obj).find(":selected").val();
				if(window.console){console.log("user_type>>>"+user_type);}
				if(window.console){console.log("user_type>>>"+obj.id);}
				$("#user_t").children().remove();
				if(user_type!="A"){
					jQuery('<select/>', {
						'class':'validate[required]',
					    id: 'USER_COMPANY',
					    name:'USER_COMPANY'
					}).appendTo('#user_t');
					initUserCompany(user_type);
// 					initUserCompany();
				}
			}
			
			function initUserCompany(user_type){
				user_type =  fstop.isEmpty(user_type)?$("#USER_TYPE").val():user_type;
				if(window.console){console.log("USER_TYPE>>"+user_type);}
// 				var rdata = {component:"each_user_bo", method:"getBgbkListByUser_Type" , user_type:select.find(":selected").val() } ;
				var rdata = {component:"each_user_bo", method:"getBgbkListByUser_Type" , user_type:user_type } ;
				if(user_type != "A"){
					fstop.getServerDataExII(uri, rdata, false, showUserCompany);
				}
			}
			function showUserCompany(obj){
				if(window.console){console.log("obj>>"+obj);}
				var select = $("#USER_COMPANY");
				select.children().remove();
				select.append($("<option></option>").attr("value", "").text("==請選擇所屬單位代號=="));
				for( var key in obj ){
					select.append($("<option></option>").attr("value", obj[key].value).text(obj[key].label));
				}
			}
		</script>
	</body>
</html>
