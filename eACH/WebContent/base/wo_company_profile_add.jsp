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
			<a href="#">新增</a>
		</div>	
		<html:form styleId="formID" action="/wo_company_profile" method="POST" >
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
						<td class="header necessary" style="width: 30%"><span>收費業者統一編號</span></td>
						<td style="width: 22%"><html:text styleId="WO_COMPANY_ID" property="WO_COMPANY_ID" size="15" maxlength="10" styleClass="validate[required,maxSize[10],custom[onlyLetterNumber],notChinese] text-input" onblur="getCompanyData(this.value)"></html:text></td>
						<td class="header necessary" style="width: 15%"><span>交易代號</span></td>
						<td>
							<logic:present name="wo_company_profile_form" property="txnIdList">
								<html:select styleId="TXN_ID" property="TXN_ID" styleClass="validate[required]">
									<html:option value="">==請選擇==</html:option>
									<html:optionsCollection name="wo_company_profile_form" property="txnIdList" label="label" value="value"/>
								</html:select>
							</logic:present>
						</td>
					</tr>
					<tr>
						<td class="header necessary"><span>收費業者簡稱(存摺列印用)</span></td>
						<td><html:text   styleClass=" validate[required,funcCall[checkCompanyAbbr]]" styleId="WO_COMPANY_ABBR_NAME" property="WO_COMPANY_ABBR_NAME" size="10" maxlength="20"></html:text></td>
						<td class="header necessary"><span>收費業者名稱</span></td>
						<td colspan="3"><html:text styleId="WO_COMPANY_NAME" property="WO_COMPANY_NAME" size="60" maxlength="66"  styleClass="validate[required] text-input"></html:text></td>
					</tr>
					<tr>
						<td class="header necessary"><span>繳費類別代號</span></td>
						<td>
						<logic:present name="wo_company_profile_form" property="bill_type_IdList">
							<html:select styleId="BILL_TYPE_ID" property="BILL_TYPE_ID" styleClass="validate[required]">
								<html:option value="">==請選擇==</html:option>
								<html:optionsCollection name = "wo_company_profile_form" property="bill_type_IdList" label="label" value="value"/>
							</html:select>
						</logic:present>	
						</td>
						<td class="header necessary"><span>銷帳行所屬總行</span></td>
						<td>
						<logic:present name="wo_company_profile_form" property="bgbkIdList" >
							<html:select styleId="INBANK_ID" property="INBANK_ID" styleClass="validate[required]">
								<html:option value="">全部</html:option>
								<html:optionsCollection name = "wo_company_profile_form" property="bgbkIdList" label="label" value="value"/>
							</html:select>
						</logic:present>	
						</td>
						
					</tr>
					<tr>
						<td class="header necessary"><span>支援銷帳類型</span></td>
						<td id="BILL_TOOL">
								<html:checkbox styleId="TYPE_ACCT" property="TYPE_ACCT" value="Y" styleClass="validate[groupCheckbox[BILL_TOOL]]"><label for="TCH_FUNC">虛擬帳號</label></html:checkbox>
								<html:checkbox styleId="TYPE_WRITE_OFF_NO" property="TYPE_WRITE_OFF_NO" value="Y" styleClass="validate[groupCheckbox[BILL_TOOL]]"><label for="BANK_FUNC">銷帳編號</label></html:checkbox>
								<html:checkbox styleId="TYPE_BARCODE" property="TYPE_BARCODE" value="Y" styleClass="validate[groupCheckbox[BILL_TOOL]]"><label for="COMPANY_FUNC">三段式條碼</label></html:checkbox>
						</td>
						<td class="header necessary"><span>銷帳行帳號</span></td>
						<td>
							<html:text styleId="INBANK_ACCT_NO" property="INBANK_ACCT_NO" size="20" maxlength="16" styleClass="validate[required,maxSize[16],minSize[16],custom[onlyLetterNumber],notChinese] text-input" ></html:text>
						</td>
					</tr>
					<tr>
					
						<td class="header"><span>條碼-代收項目</span></td>
						<td>
							<html:text styleId="SD_ITEM_NO" property="SD_ITEM_NO" size="3" maxlength="3" styleClass="validate[maxSize[3],custom[onlyLetterNumber],notChinese] text-input" ></html:text>
						</td>
						<td class="header"><span>條碼-解析格式代號</span></td>
						<td>
							<html:text styleId="FMT_ID" property="FMT_ID" size="3" maxlength="3" styleClass="validate[maxSize[3],custom[onlyLetterNumber],notChinese] text-input" ></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary"><span>啟用日期</span></td>
						<td><html:text styleId="START_DATE" property="START_DATE" size="10" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,notChinese,twPast[#STOP_DATE]] text-input datepicker"></html:text></td>
						<td class="header"><span>停用日期</span></td>
						<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="10" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,twFuture[#START_DATE]] text-input datepicker"></html:text></td>
					</tr>




					<tr>
						<td class="header"><span>整合性業者</span></td>
						<td>
								<html:radio styleId="IS_INTEGRATED_1" property="IS_INTEGRATED" value="1">是</html:radio>
								<html:radio styleId="IS_INTEGRATED_2" property="IS_INTEGRATED" value="2">否</html:radio>
						</td>
						<td class="header"><span>代收次項目</span></td>
						<td><html:text styleId="SD_ITEM" property="SD_ITEM" size="10" maxlength="8" styleClass="validate[maxSize[8] text-input"></html:text></td>
					</tr>
					<tr>
						<td class="header"><span>次項目起位</span></td>
						<td><html:text styleId="ITEM_START" property="ITEM_START" size="2" maxlength="2" styleClass="validate[maxSize[2],custom[onlyLetterNumber],notChinese] text-input" ></html:text></td>
						<td class="header"><span>次項目末位</span></td>
						<td><html:text styleId="ITEM_END" property="ITEM_END" size="2" maxlength="2" styleClass="validate[maxSize[2],custom[onlyLetterNumber],notChinese] text-input" ></html:text></td>
					</tr>
					<tr>
						<td class="header"><span>次代收項代號</span></td>
						<td><html:text styleId="SD_ITEM_ID" property="SD_ITEM_ID" size="10" maxlength="8" styleClass="validate[maxSize[8],custom[onlyLetterNumber],notChinese] text-input" ></html:text></td>
					</tr>
					<tr>
						<td class="header"><span>虛擬帳號欄位說明</span></td>
						<td colspan="3">
							<html:textarea styleId="VIRTUAL_ACC_NOTE" property="VIRTUAL_ACC_NOTE" style="width:100%" rows="3" styleClass="validate[maxSize[50]]"></html:textarea>
						</td>
					</tr>
					<tr>
						<td class="header"><span>銷帳編號欄位說明</span></td>
						<td colspan="3">
							<html:textarea styleId="WO_NO_NOTE" property="WO_NO_NOTE" style="width:100%" rows="3" styleClass="validate[maxSize[50]]"></html:textarea>
						</td>
					</tr>
					
					


					<tr>
						<td class="header"><span>備註</span></td>
						<td colspan="3">
							<html:textarea styleId="NOTE" property="NOTE" style="width:60%" rows="5" styleClass="validate[maxSize[500]]"></html:textarea>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
							<label class="btn" id="save" onclick="onPut(this.id, true)"><img src="./images/save.png"/>&nbsp;儲存</label>
							<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
							<label class="btn" id="back" onclick="onPut(this.id, false)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
						</td>
					</tr>
				</table>
			</div>	
		</html:form>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	<script type="text/javascript">
		var lastClickId = "";
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
	
		$(document).ready(function () {
			blockUI();
			init();
			disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
		});
		
		function init(){
			alterMsg();
			setDatePicker();
			$("#START_DATE").val('<bean:write name="wo_company_profile_form" property="START_DATE"/>');
			$("#STOP_DATE").val('<bean:write name="wo_company_profile_form" property="STOP_DATE"/>');
			$("#formID").validationEngine({binded: false, promptPosition: "bottomLeft"});
			
			//整合性業者預設為[2=否]
			$("#IS_INTEGRATED_2").prop('checked', true);
			
			getBrbk_List($("#BGBK_ID").val());
			
			//SET checkbox EVENT
			$("#BILL_TOOL input:checkbox").click(function(){
				var id = $(this).attr("id");
				lastClickId = id;
				setTimeout(function(){clearPrompt();}, 20);
			});
			
		}
		
		function clearPrompt(){
			$("#BILL_TOOL input:checkbox").each(function(){
				if($(this).attr("id") != lastClickId){
					$(this).validationEngine('hide');
				}
			});
		}
		
		function alterMsg(){
			var msg = '<bean:write name="wo_company_profile_form" property="msg"/>';
			if(fstop.isNotEmptyString(msg)){
				alert(msg);
			}
		}
		
		
		
		function getCompanyData(companyId){
			fstop.getServerDataEx(uri + "?component=wo_company_profile_bo&method=getCompanyDataByCompanyId&WO_COMPANY_ID=" + companyId, null, false, function(data){
// 				$("#COMPANY_ABBR_NAME").val(data.COMPANY_ABBR_NAME);
				$("#WO_COMPANY_NAME").val(data.COMPANY_NAME);
			});
		}
		
		function checkCompanyAbbr(){
			var abbr_name = $("#WO_COMPANY_ABBR_NAME").val();
			if(abbr_name.match(/.*[^\u0000-\u007F]+.*/)){
				if(abbr_name.match(/[^\u0000-\u007F]/g).length > 4){
					return '* 「業者簡稱」最多只能輸入4個中文';
				}
			}
		}
		
		function onPut(str, isValidate){
			if(isValidate){
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
			}else{
				$("#formID").validationEngine('detach');
			}
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
	</script>
</body>
</html>