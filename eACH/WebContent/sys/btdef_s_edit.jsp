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
				<html:form styleId="formID" action="/each_batch_def" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<table>
						<tr>
							<td class="header necessary" style="width: 40%"><span>批次執行序</span></td>
							<td>
							<html:text styleId="BATCH_PROC_SEQ" property="BATCH_PROC_SEQ" size="3" maxlength="3" styleClass="validate[required ,maxSize[14],custom[integer,max[100]]" ></html:text>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>批次作業</span></td>
							<td><html:text styleId="BATCH_PROC_NAME" property="BATCH_PROC_NAME"   size="30" maxlength="30" styleClass="validate[required ,maxSize[30]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>批次作業說明</span></td>
							<td>
							<html:text styleId="BATCH_PROC_DESC" property="BATCH_PROC_DESC"   size="33" maxlength="33" styleClass="validate[required ,maxSize[33]] text-input"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>錯誤中斷是否執行</span></td>
							<td>
							<html:radio property="ERR_BREAK" value="Y">是</html:radio>
							<html:radio property="ERR_BREAK" value="N">否</html:radio>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>批次作業類型</span></td>
							<td>
							<html:select styleId="PROC_TYPE"  property="PROC_TYPE" >
								<html:option value="D">日批次作業</html:option>
								<html:option value="M">月批次作業</html:option>
							</html:select>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="userData" property="s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
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
		
// 		var isAltF4 = false;
// 		window.onbeforeunload = bunload;

// 		function bunload()
// 		{
// 			if (event.clientY < 0 || isAltF4 )
// 			{
// 				onPut('');
// 			    mess = "尚未儲存資料，確定離開此頁面?";
// 			    return mess;
// 			}
// 		};
		
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
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
				var msg = '<bean:write name="each_batch_def_form" property="msg"/>';
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
