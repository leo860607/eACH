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
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/json2.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
	</head>
	<body onload="unblockUI()">
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
			<html:form styleId="formID" action="/chg_sc_profile" method="POST" >
				<html:hidden property="ac_key" styleId="ac_key" value=""/>
				<html:hidden property="target" styleId="target" value=""/>
				<html:hidden property="serchStrs" styleId="serchStrs" />
				<html:hidden property="sortname" styleId="sortname" />
				<html:hidden property="sortorder" styleId="sortorder" />
				<html:hidden property="edit_params" styleId="edit_params" />
				<html:hidden property="pageForSort" styleId="pageForSort"/>
				<div id="dataInputTable">
					<table>
						<tr>
							<td class="header necessary" style="width: 30%"><span>代收項目</span></td>
							<td style="width: 20%"><html:text styleId="SD_ITEM_NO" property="SD_ITEM_NO" size="5" maxlength="3" styleClass="lock validate[required,maxSize[3],custom[onlyNumberSp],notChinese] text-input" readonly="true"></html:text></td>
							<td class="header necessary" style="width: 15%"><span>交易代號</span></td>
							<td>
								<logic:present name="chg_sc_profile_form" property="txnIdList">
									<html:select styleId="TXN_ID" property="TXN_ID" styleClass="validate[required]">
										<html:optionsCollection name="chg_sc_profile_form" property="txnIdList" label="label" value="value"/>
									</html:select>
								</logic:present>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>業者統一編號</span></td>
							<td><html:text styleId="COMPANY_ID" property="COMPANY_ID" size="15" maxlength="10" styleClass="validate[required,maxSize[10],custom[onlyLetterNumber],notChinese] text-input" onblur="getCompanyData(this.value)"></html:text></td>
							<td class="header"><span>業者簡稱</span></td>
							<td><html:text styleClass="validate[required, funcCall[checkCompanyAbbr]]" styleId="COMPANY_ABBR_NAME" property="COMPANY_ABBR_NAME" size="10" maxlength="20"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>業者名稱</span></td>
							<td colspan="3"><html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="100" maxlength="65" styleClass="validate[required] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>入帳行所屬總行代號</span></td>
							<td>
								<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)" styleClass="validate[required]">
									<html:optionsCollection name="chg_sc_profile_form" property="bgbkIdList" label="label" value="value"/>
								</html:select>
							</td>
							<td class="header necessary"><span>入帳行代號</span></td>
							<td>
								<html:select styleId="INBANKID" property="INBANKID" styleClass="validate[required]"></html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>入帳行帳號</span></td>
							<td><html:text styleId="INBANKACCTNO" property="INBANKACCTNO" size="20" maxlength="16" styleClass="validate[required,maxSize[16],custom[onlyNumberSp],notChinese] text-input"></html:text></td>
							<td class="header"><span>格式代號</span></td>
							<td><html:text styleId="LAYOUTID" property="LAYOUTID" size="5" maxlength="3" styleClass="validate[maxSize[3],notChinese] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>可收逾期繳費天數</span></td>
							<td><html:text styleId="DEALY_CHARGE_DAY" property="DEALY_CHARGE_DAY" size="5" maxlength="3" styleClass="validate[maxSize[3],custom[onlyNumberSp],notChinese] text-input" style="text-align: right" onblur="trans_DEALY_CHARGE_DAY(this.value)"></html:text></td>
							<td class="header"><span>&nbsp;</span></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td class="header"><span>啟用日期</span></td>
							<td><html:text styleId="START_DATE" property="START_DATE" size="10" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,twPast[#STOP_DATE]] text-input datepicker"></html:text></td>
							<td class="header"><span>停用日期</span></td>
							<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="10" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,twFuture[#START_DATE]] text-input datepicker"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>備註</span></td>
							<td colspan="3">
								<html:textarea styleId="NOTE" property="NOTE" style="width:60%" rows="5" styleClass="validate[maxSize[500]]"></html:textarea>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="update" onclick="onPut(this.id, true)"><img src="./images/save.png"/>&nbsp;儲存</label>
									<label class="btn" id="delete" onclick="onPut(this.id, false)"><img src="./images/delete.png"/>&nbsp;刪除</label>
								</logic:equal>
								<label class="btn" id="back" onclick="onPut(this.id, false)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</div>
			</html:form>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	</body>
	
	<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
	
		$(document).ready(function () {
			blockUI();
			init();
			disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
		});
		
		function getCompanyData(companyId){
			fstop.getServerDataEx(uri + "?component=chg_sc_profile_bo&method=getCompanyDataByCompanyId&COMPANY_ID=" + companyId, null, false, function(data){
				$("#COMPANY_ABBR_NAME").val(data.COMPANY_ABBR_NAME);
				$("#COMPANY_NAME").val(data.COMPANY_NAME);
			});
		}
		
		function getBrbk_List(bgbkId){
			if(bgbkId == ''){
				//$("#INBANKID option:not(:first-child)").remove();
				$("#INBANKID option").remove();
			}else{
				var rdata = {component:"bank_branch_bo", method:"getBank_branch_List" , bgbkId:bgbkId  };
				fstop.getServerDataExII(uri, rdata, false,creatBrBkList);
			}
		}
		
		function creatBrBkList(obj){
			var select = $("#INBANKID");
			//$("#INBANKID option:not(:first-child)").remove();
			$("#INBANKID option").remove();
			if(obj.result=="TRUE"){
				var dataAry =  obj.msg;
				for(var a in dataAry){
					if(dataAry[a].value == '<bean:write name="chg_sc_profile_form" property="INBANKID"/>'){
						select.append($("<option selected></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}else{
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
				}
			}else if(obj.result=="FALSE"){
				alert(obj.msg);
			}
		}
		
		function init(){
			alterMsg();
			setDatePicker();
			$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			$('#START_DATE').val('<bean:write name="chg_sc_profile_form" property="START_DATE"/>');
			$('#STOP_DATE').val('<bean:write name="chg_sc_profile_form" property="STOP_DATE"/>');
			getBrbk_List($("#BGBK_ID").val());
		}
		
		function alterMsg(){
			var msg = '<bean:write name="chg_sc_profile_form" property="msg"/>';
			if(fstop.isNotEmptyString(msg)){
				alert(msg);
			}
		}
		
		function onPut(str, isValidate){
			if(isValidate){
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
			}else if(str == "back"){
				$("#formID").validationEngine('detach');
			}
			if(str == "delete" && !confirm("是否確定刪除?")){
				//20150623 add by hugo 刪除時不檢核欄位
				$("#formID").validationEngine('detach');
				return false;
			}
			blockUI();
			$("#ac_key").val(str);
			$("#target").val('search');
			$("form").submit();
		}
		
		function trans_DEALY_CHARGE_DAY(val){
			if(val.match(/^\d+$/g)){
				$("#DEALY_CHARGE_DAY").val(parseFloat(val));
			}else if(val == ''){
				$("#DEALY_CHARGE_DAY").val("0");
			}
		}
		
		function checkCompanyAbbr(){
			var abbr_name = $("#COMPANY_ABBR_NAME").val();
			if(abbr_name.match(/.*[^\u0000-\u007F]+.*/)){
				if(abbr_name.match(/[^\u0000-\u007F]/g).length > 4){
					return '* 「業者簡稱」最多只能輸入4個中文';
				}
			}
		}
	</script>
</html>
