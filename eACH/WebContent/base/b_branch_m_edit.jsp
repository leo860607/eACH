<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-編輯</title>
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript">
			function initGrid(){
				tableToGrid("#bs_table",{
		        	width: $("body").width()
		        });
			}
		
		</script>
	</head>
	<body onload="unblockUI()">
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
				<html:form styleId="formID" action="/bank_branch" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 50%"><span>總行代號</span></td>
							<td>
								<html:select styleId="BGBK_ID" property="BGBK_ID" styleClass="lock validate[required]" disabled="true">
									<html:option value="">==請選擇總行代號==</html:option>
									<html:optionsCollection name = "bank_branch_form" property="bgIdList" label="label" value="value"/>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>分行代號</span></td>
							<td><html:text styleId="BRBK_ID" property="BRBK_ID" size="7" maxlength="7" styleClass="lock validate[required,maxSize[7],custom[onlyNumberSp],notChinese] text-input" disabled="true"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>分行名稱</span></td>
							<td><html:text property="BRBK_NAME" size="13" maxlength="13" styleClass="validate[required,maxSize[13]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交換所代號</span></td>
							<td><html:text property="TCH_ID" size="2" maxlength="2" styleClass="validate[required,maxSize[2],custom[onlyNumberSp],notChinese] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>啟用日期(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[required , maxSize[8],minSize[8],twDate ,twPast[#STOP_DATE]] text-input datepicker"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>停用日期(民國年 ex.01030101)</span></td>
<%-- 							<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],funcCall[checkDates]] ,minSize[8],twDate] text-input datepicker"></html:text></td> --%>
							<td>
<%-- 							<html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate ,twPast[#ACTIVE_DATE]] text-input datepicker"></html:text> --%>
							<html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate ,twFuture[#ACTIVE_DATE]] text-input datepicker"></html:text>
							<html:radio styleId="SYNCSPDATE_Y" property="SYNCSPDATE" value="Y" onclick="opStopDate(this.value)"></html:radio>與總行同步
							&nbsp; <html:radio styleId="SYNCSPDATE_N" property="SYNCSPDATE" value="N" onclick="opStopDate(this.value)"></html:radio>自行設定
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="userData" property="s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
							</logic:equal>	
								<label class="btn" id="back"  onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
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
	        });
			function onPut(str){
				if(str != "update"){
					$("#formID").validationEngine('detach');
				}else{
					$("#formID").validationEngine({promptPosition: "bottomLeft"});
					if(!$("#formID").validationEngine('validate')){
						$("#formID").validationEngine('detach');return;
					}
				}
				if(str == "delete"){
					if(!confirm("是否確定刪除?")){
						return false;
					}
				}
				blockUI();
				$("#ac_key").val(str);
				$("#target").val('search');
				$("#BGBK_ID").attr('disabled', false);
				$("#BRBK_ID").attr('disabled', false);
				$("form").submit();
			}	
			function init(){
				//$("#bank_branch_ID").attr('disabled', true);
				alterMsg();
				setDatePicker();
				$("#ACTIVE_DATE").val('<bean:write name="bank_branch_form" property="ACTIVE_DATE"/>');
				$("#STOP_DATE").val('<bean:write name="bank_branch_form" property="STOP_DATE"/>');
				initStopDate();
			}
			function alterMsg(){
				var msg = '<bean:write name="bank_branch_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			function initStopDate(){
// 				var isSync = $("input[name='SYNCSPDATE']").val();
				var isSync = '<bean:write name="bank_branch_form" property="SYNCSPDATE"/>';
				if(window.console){console.log("isSync>>"+isSync);}
				opStopDate(isSync);
			}
			
			function opStopDate(str){
				if(str=="Y"){
					removeDatePicker($("#STOP_DATE"));
					$("#STOP_DATE").removeClass("datepicker");
					$("#STOP_DATE").attr("disabled" , true);
				}else{
					$("#STOP_DATE").attr("disabled" , false);
					$("#STOP_DATE").addClass("datepicker");
					setDatePickerII($("#STOP_DATE"),$("#STOP_DATE").val());
				}
					
			}
// 			function checkDates(){
// 				alert("hi");
// 			}


		</script>
	</body>
</html>
