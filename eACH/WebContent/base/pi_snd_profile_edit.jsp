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
			<html:form styleId="formID" action="/pi_snd_profile" method="POST" >
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
						<td class="header necessary" style="width: 30%"><span>收費業者統編</span></td>
						<td style="width: 20%">
							<html:select styleId="PI_COMPANY_ID" property="PI_COMPANY_ID" onchange="getPI_List();" disabled="true">
								<html:option value="">全部</html:option>
								<html:optionsCollection name = "pi_snd_profile_form" property="pi_com_IdList" label="label" value="value"/>
							</html:select>
						</td>
						<td class="header necessary" style="width: 15%"><span>繳費類別代號</span></td>
						<td>
							<html:select styleId="BILL_TYPE_ID" property="BILL_TYPE_ID" styleClass="validate[required]" disabled="true">
								<html:option value="">==請選擇==</html:option>
								<html:optionsCollection name = "pi_snd_profile_form" property="bill_type_IdList" label="label" value="value"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="header necessary" ><span>發動業者統編</span></td>
						<td>
						<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="15" maxlength="10" readonly="true" styleClass="lock validate[required,maxSize[10],custom[onlyLetterNumber],notChinese] text-input" onblur="getCompanyData(this.value)"></html:text>
						</td>
						<td class="header necessary"><span>發動業者簡稱</span></td>
						<td><html:text readonly="true" styleClass="lock validate[required,funcCall[checkCompanyAbbr]]" styleId="COMPANY_ABBR_NAME" property="COMPANY_ABBR_NAME" size="10" maxlength="20"></html:text></td>
						
					</tr>
					<tr>
						<td class="header necessary" ><span>發動業者名稱</span></td>
						<td colspan="3"><html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="60" maxlength="66" readonly="true" styleClass="lock validate[required] text-input"></html:text></td>
					</tr>
					<tr>
						<td class="header necessary"><span>啟用日期</span></td>
						<td><html:text styleId="START_DATE" property="START_DATE" size="10" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,notChinese,twPast[#STOP_DATE]] text-input datepicker"></html:text></td>
						<td class="header"><span>停用日期</span></td>
						<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="10" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,twFuture[#START_DATE]] text-input datepicker"></html:text></td>
					</tr>
					<tr>
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
			fstop.getServerDataEx(uri + "?component=pi_snd_profile_bo&method=getCompanyDataByCompanyId&SND_COMPANY_ID=" + companyId, null, false, function(data){
				$("#COMPANY_ABBR_NAME").val(data.COMPANY_ABBR_NAME);
				$("#COMPANY_NAME").val(data.COMPANY_NAME);
			});
		}
		
		
		function init(){
			alterMsg();
			setDatePicker();
			$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			$('#START_DATE').val('<bean:write name="pi_snd_profile_form" property="START_DATE"/>');
			$('#STOP_DATE').val('<bean:write name="pi_snd_profile_form" property="STOP_DATE"/>');
			snd_company_id_Event();
		}
		
		
		function snd_company_id_Event(){
			var snd_company_id =  '<bean:write name="pi_snd_profile_form" property="SND_COMPANY_ID"/>';
			if(fstop.isNotEmptyString(snd_company_id)){
				$("#SND_COMPANY_ID").trigger("onblur");
			}
		}
		
		function alterMsg(){
			var msg = '<bean:write name="pi_snd_profile_form" property="msg"/>';
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
			$( "#BILL_TYPE_ID" ).prop( "disabled", false );
			$( "#PI_COMPANY_ID" ).prop( "disabled", false );
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
