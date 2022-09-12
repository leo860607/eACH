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
			<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define>
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/sys_para" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SEQ_ID" styleId="SEQ_ID" />
					<html:hidden property="SD_SC_TYPE_CHK" styleId="SD_SC_TYPE_CHK" />
					<table>
						<tr>
							<td class="header necessary" style="width: 25%"><span>timeout時間 (分鐘)</span></td>
							<td><html:text styleId="TIMEOUT_TIME" property="TIMEOUT_TIME"  size="10" maxlength="10" styleClass="validate[required , custom[integer] , min[0] ] text-input"></html:text></td>
							<td class="header necessary" style="width: 30%"><span>檔案上傳大小 (MB)</span></td>
							<td><html:text property="MAX_FILE_SIZE"  size="10" maxlength="10" styleClass="validate[required ,custom[integer] , min[0]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>票交所標準回應時間（秒）</span></td>
							<td><html:text property="TCH_STD_ECHO_TIME"  size="10" maxlength="10" styleClass="validate[required ,decimal[5,2], min[0]] text-input"></html:text></td>
							<td class="header necessary"><span>單筆標準完成時間（秒）</span></td>
							<td><html:text property="TXN_STD_PROC_TIME"  size="10" maxlength="10" styleClass="validate[required ,decimal[5,2], min[0]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>銀行每筆入帳標準處理時間(秒)</span></td>
							<td><html:text property="BANK_SC_STD_PROC_TIME"  size="10" maxlength="10" styleClass="validate[required ,decimal[5,2], min[0]] text-input"></html:text></td>
							<td class="header necessary"><span>銀行每筆扣款標準處理時間(秒)</span></td>
							<td><html:text property="BANK_SD_STD_PROC_TIME"  size="10" maxlength="10" styleClass="validate[required ,decimal[5,2], min[0]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>緩衝上傳時間（分鐘）</span></td>
							<td><html:text property="PEND_TIME_BUF"  size="2" maxlength="2" styleClass="validate[required ,custom[integer], min[0]] text-input"></html:text></td>
							<td class="header necessary"><span>每小時上傳最多檔案個數預設值（整批）</span></td>
							<td><html:text property="HR_UP_MAX_FILE_DFT"  size="2" maxlength="2" styleClass="validate[required ,custom[integer], min[0] ,max[99]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>整批檔案最大傳送筆數</span></td>
							<td><html:text styleId="FILE_MAX_CNT" property="FILE_MAX_CNT" size="6" maxlength="6" styleClass="validate[required,maxSize[6],custom[integer,min[0] ,max[999999]]] text-input "></html:text></td>
							<td class="header necessary"><span>交易差異時間（秒）</span></td>
							<td><html:text property="TRAN_DIFF_TIME"  size="10" maxlength="10" styleClass="validate[required ,decimal[5,2], min[0]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>約定非上傳時間起(1)</span></td>
							<td><html:text styleId="APT_PEND_START_TIME1" property="APT_PEND_START_TIME1"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
							<td class="header"><span>約定非上傳時間迄(1)</span></td>
							<td><html:text property="APT_PEND_END_TIME1"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>約定非上傳時間起(2)</span></td>
							<td><html:text property="APT_PEND_START_TIME2"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
							<td class="header"><span>約定非上傳時間迄(2)</span></td>
							<td><html:text property="APT_PEND_END_TIME2"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>約定非上傳時間起(3)</span></td>
							<td><html:text property="APT_PEND_START_TIME3"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
							<td class="header"><span>約定非上傳時間迄(3)</span></td>
							<td><html:text property="APT_PEND_END_TIME3"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>約定非上傳時間起(4)</span></td>
							<td><html:text property="APT_PEND_START_TIME4"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
							<td class="header"><span>約定非上傳時間迄(4)</span></td>
							<td><html:text property="APT_PEND_END_TIME4"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>約定非上傳時間起(5)</span></td>
							<td><html:text property="APT_PEND_START_TIME5"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
							<td class="header"><span>約定非上傳時間迄(5)</span></td>
							<td><html:text property="APT_PEND_END_TIME5"  size="8" maxlength="8" styleClass="validate[option ] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>ID+ACCT每筆限額</span></td>
							<td><html:text property="ACCT_LIMIT_SAMT"  size="10" maxlength="10" styleClass="validate[option ,custom[integer,min[0] ,max[9999999999]]] text-input"></html:text></td>
							<td class="header"><span>晶片金融卡每筆限額</span></td>
							<td><html:text property="ATM_LIMIT_SAMT"  size="10" maxlength="10" styleClass="validate[option ,custom[integer,min[0] ,max[9999999999]]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>授權帳戶每筆限額</span></td>
							<td colspan><html:text property="CERT_LIMIT_SAMT"  size="10" maxlength="10" styleClass="validate[option ,custom[integer,min[0] ,max[9999999999]]] text-input"></html:text></td>
							<td class="header necessary"><span>是否檢核業者檔收費類型</span></td>
							<td>
							<html:checkbox styleId="SD_SC_TYPE_CHK_Y" property="SD_SC_TYPE_CHK_Y" onclick="changeCheckBox(this.id)">是</html:checkbox>
							<html:checkbox styleId="SD_SC_TYPE_CHK_N" property="SD_SC_TYPE_CHK_N" onclick="changeCheckBox(this.id)">否</html:checkbox>
							<td>
						</tr>
						
						<tr>
						<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<logic:equal  name="userData" property="s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								</logic:equal>
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
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function onPut(str,isValidate){
				$("#ac_key").val(str);
				$("#target").val('edit_p');
				if(str == "delete"){
					if(!confirm("是否確定刪除")){
						return false;
					}
				}
				if(isValidate){
					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
						return false;
					}
					if($('#SD_SC_TYPE_CHK_Y').prop("checked")){
						$('#SD_SC_TYPE_CHK').val("0");
					}else{
						$('#SD_SC_TYPE_CHK').val("1");
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				// 0 檢核 1不檢核
				if('<bean:write name="sys_para_form" property="SD_SC_TYPE_CHK"/>'=='0'){
					$('#SD_SC_TYPE_CHK_Y').prop("checked",true);
				}else{
					$('#SD_SC_TYPE_CHK_N').prop("checked",true);
				}
				setDatePicker("#ACTIVE_DATE", 0);
				setTimePicker();
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="sys_para_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			function setTimePicker(){
				if(window.console){console.log("setTimePicker1");}
				$("input[name^='APT_PEND']").timepicker({ 'timeFormat': 'H:i:s' });
				if(window.console){console.log("setTimePicker2");}
			}
			
			function changeCheckBox(id){
				if(id=='SD_SC_TYPE_CHK_Y'){
					$('#SD_SC_TYPE_CHK_N').prop("checked",false);
				}else{
					$('#SD_SC_TYPE_CHK_Y').prop("checked",false);
				}
			}
		</script>
	</body>
</html>
