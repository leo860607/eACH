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
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/each_txn_code" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 40%"><span>交易類別</span></td>
							<td><html:text styleId="EACH_TXN_ID" property="EACH_TXN_ID" readonly="true" style="background-color: transparent;border: 0;" size="4" maxlength="4" styleClass="validate[required,notChinese] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交易類別項目</span></td>
							<td><html:text property="EACH_TXN_NAME"  size="66" maxlength="66" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>業務類別代號</span></td>
							<td><html:text property="BUSINESS_TYPE_ID" size="66" maxlength="66" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr>
							<td colspan="3" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
							</logic:equal>	
								<label class="btn" id="back" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
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
			
			function onPut(str){
				if(str == "update"){
					if(!$('#formID').validationEngine('validate')){
						unblockUI();return;
					}
				}
				if(str == "delete" && !confirm("是否確定刪除?")){
					return false;
				}
				if(str == "back"){
					$('#formID').validationEngine('detach');
				}
				blockUI();
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
				setDatePicker("#ACTIVE_DATE", 0);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="each_txn_code_form" property="msg"/>';
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
