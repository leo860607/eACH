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
		<link rel="stylesheet" type="text/css" href="./css/jquery.timepicker.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.timepicker.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
	</head>
	<body>
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
<%-- 			<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define> --%>
			<!-- BREADCRUMBS -->
<!-- 			<div id="breadcrumb"> -->
<%-- 				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a> --%>
<%-- 				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a> --%>
<%-- 				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a> --%>
<!-- 				<a href="#">編輯</a> -->
<!-- 			</div> -->
			<div id="dataInputTable">
				<html:form styleId="formID" action="/isec_version" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SEQ_ID" styleId="SEQ_ID" />
					<table>
						<tr>
							<td class="header necessary" style="width: 25%" align = "left"><span>安控元件版本更新：</span></td>
<!-- 							<td><input type = "text" size="10" maxlength="10" value="1.410.510"></td> -->
							<td><html:text styleId="ISEC_VERSION" property="ISEC_VERSION"  size="15" maxlength="15" styleClass="validate[min[0]] text-input"></html:text></td>
						</tr>						
						<tr>
						<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
<%-- 								<logic:equal  name="userData" property="s_auth_type" value="A"> --%>
								<label class="btn" id="update" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
<%-- 								</logic:equal> --%>
<!-- 								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label> -->
<!-- 								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label> -->
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
				init();
<%--
				blockUI();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
--%>				
	        });
			
			function onPut(str,isValidate){
				$("#ac_key").val(str);
				$("#target").val('edit');
<%--				
				if(str == "delete"){
					if(!confirm("是否確定刪除")){
						return false;
					}
				}
--%>				
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
				
 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
<%--
 				setDatePicker("#ACTIVE_DATE", 0);
 				setTimePicker();
--%>
			}	
<%--	
			function alterMsg(){
				var msg = '<bean:write name="sys_para_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
 				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			function setTimePicker(){
				if(window.console){console.log("setTimePicker1");}
				$("input[name^='APT_PEND']").timepicker({ 'timeFormat': 'H:i:s' });
				if(window.console){console.log("setTimePicker2");}
			}
--%>
		</script>
	</body>
</html>
