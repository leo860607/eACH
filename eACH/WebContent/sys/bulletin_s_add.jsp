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
				<html:form styleId="formID" action="/bulletin" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="SEND_STATUS" styleId="SEND_STATUS" />
					<table>
						<tr>
							<td class="header necessary" style="width: 20%"><span>公告內容</span></td>
							<td>
							<html:text styleId="CHCON" property="CHCON" size="100" maxlength="100" styleClass="validate[required ,maxSize[100]]" ></html:text>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
								<label class="btn" id="save" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
							</logic:equal>
							
								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">	
								<label class="btn" id="saveNsend" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存及發佈公告</label>
							</logic:equal>	
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
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function onPut(str,isValidate){
				if(str == 'saveNsend'){
					str = 'save';
					$("#SEND_STATUS").val("Y");
				}
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
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="bulletin_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
// 				cleanFormII(this);
				onPut(str);
			}
			
			function cleanFormII(){
				
				$.each($('#formID').serializeArray(), function(i, field) {
					if(field.name!="serchStrs" || field.name!="sortname" || field.name!="sortorder"){
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
