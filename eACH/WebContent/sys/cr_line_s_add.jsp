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
				<html:form styleId="formID" action="/cr_line" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 40%"><span>銀行代號</span></td>
							<td>
								<html:select styleId="BANK_ID" property="BANK_ID" styleClass="validate[required] ">
									<html:option value="">==請選擇銀行代號==</html:option>
									<logic:present name="cr_line_form" property="bgbkIdList">
										<html:optionsCollection name="cr_line_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection> 
									</logic:present>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>基本額度</span></td>
							<td><html:text property="BASIC_CR_LINE"  onblur="chg_rest(this.value)" size="14" maxlength="14" styleClass="validate[required ,maxSize[11],custom[integer,min[0] ,max[99999999999]]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>剩餘額度</span></td>
							<td>
							<html:text  property="REST_CR_LINE"   size="14" maxlength="14" styleClass="lock validate[required ,maxSize[11],custom[integer,min[0] ,max[99999999999]]] text-input"></html:text>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
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
		
// 		var isAltF4 = false;
// 		window.onbeforeunload = bunload;

// 		function bunload()
// 		{
// 			onPut('back');
// 			alert('1');
// 			if (event.clientY < 0 || isAltF4 )
// 			{
// 				onPut('');
// 			    mess = "尚未儲存資料，確定離開此頁面?";
// 			    return mess;
// 			}
// 		};

// 		function document.onkeydown(){
// 			if ((window.event.altKey)&&(window.event.keyCode==115))
// 			{
// 				isAltF4 = true;
// 				bunload();
// 			}
// 		}
		
		
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function onPut(str,isValidate){
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
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="cr_line_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
// cleanFormII(this);
				onPut(str);
			}
			
			function chg_rest(value){
				if(window.console) console.log(value);
				$("input[name='REST_CR_LINE']").val(value);
			}
			
			
			function cleanFormII(){
				$.each($('#formID').serializeArray(), function(i, field) {
					if(field.name!="serchStrs"){
					$("#"+field.name).val("");
				}
// 				if(window.console){console.log("field.value>>"+field.value);}
				});
			}
			function cleanFormIII(){
				var serchs = {};
				$.each($('#formID').serializeArray(), function(i, field) {
// 					serchs[field.name] = field.value;
					field.value = "";
					$("#"+field.name).val("");
// 				if(window.console){console.log("field.value>>"+field.value);}
					
				});
// 				$("#serchStrs").val(JSON.stringify(serchs));
			}
		</script>
	</body>
</html>
