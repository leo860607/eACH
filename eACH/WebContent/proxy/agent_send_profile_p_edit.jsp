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
	<title><bean:write name="login_form" property="userData.s_func_name"/>-修改</title>
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
			<a href="#">修改</a>
		</div>	
		<html:form styleId="formID" action="/agent_send_profile" method="POST" >
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
						<td class="header necessary" style="width: 45%">
							<span>代理業者統一編號</span>
						</td>
						<td>
<%-- 						<html:select styleId="COMPANY_ID" property="COMPANY_ID"> --%>
<%-- 							<html:option value="">全部</html:option> --%>
<%-- 							<html:optionsCollection name="agent_send_profile_form" property="companyIdList" label="label" value="value"/> --%>
<%-- 						</html:select> --%>
						<html:text styleId="COMPANY_ID" property="COMPANY_ID" size="15" maxlength="10" readonly="true"  styleClass="lock validate[required,maxSize[10],notChinese,onlyNumberSp] text-input"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary" >
							<span>發動者統一編號</span>
						</td>
						<td>
							<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="15" maxlength="10" readonly="true" styleClass="lock validate[required,maxSize[10],notChinese,onlyNumberSp] text-input"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary" ><span>發動者名稱</span></td>
						<td>
							<html:text styleId="SND_COMPANY_NAME" property="SND_COMPANY_NAME" size="40" maxlength="40" readonly="true" styleClass="lock text-input" ></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>交易代號</span>
						</td>
						<td>
						<html:text styleId="TXN_ID" property="TXN_ID" size="3" maxlength="3" readonly="true" styleClass="lock validate[required,maxSize[3]] text-input"></html:text>
<%-- 						<html:select styleId="TXN_ID" property="TXN_ID"> --%>
<%-- 							<html:option value="">全部</html:option> --%>
<%-- 							<html:optionsCollection name="agent_send_profile_form" property="txnIdList" label="label" value="value"/> --%>
<%-- 						</html:select> --%>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>啟用日期(民國年 ex.01030101)</span>
						</td>
						<td>
							<html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header">
							<span>停用日期(民國年 ex.01030101)</span>
						</td>
						<td>
							<html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
						</td>
					</tr>
					
<!-- 					<tr> -->
<!-- 						<td colspan="2" align="center" style="padding: 10px; vertical-align: middle"> -->
<!-- 							<label class="btn" id="save" onclick="onPut(this.id, true)"><img src="./images/save.png"/>&nbsp;儲存</label> -->
<!-- 							<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label> -->
<!-- 							<label class="btn" id="back"  onclick="onPut(this.id, false)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label> -->
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr>
						
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id,true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
							</logic:equal>	
								<label class="btn" id="back" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
				</table>
			</div>	
		</html:form>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	<script type="text/javascript">
		
		$(document).ready(function () {
			blockUI();
			init();
			disabledForm('<bean:write name="login_form" property="userData.s_auth_type"/>');
		});
		
		function init(){
			alterMsg();
			setDatePicker();
			$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			$("#ACTIVE_DATE").val('<bean:write name="agent_send_profile_form" property="ACTIVE_DATE"/>');
			$("#STOP_DATE").val('<bean:write name="agent_send_profile_form" property="STOP_DATE"/>');
		}
		
		function alterMsg(){
			var msg = '<bean:write name="agent_send_profile_form" property="msg"/>';
			if(fstop.isNotEmptyString(msg)){
				alert(msg);
			}
		}
		
		function onPut(str , isValidate){
			if(isValidate && !jQuery('#formID').validationEngine('validate')){
				return false;
			}else{
				$("#formID").validationEngine('detach');
			}
			$("#ac_key").val(str);
			$("#target").val('search');
			$("form").submit();
		}
		

		function checkCOMPANY_ID(){
			var id = $("#SND_COMPANY_ID").val();
//				if($("#COMPANY_NAME").val() == '' && $("#COMPANY_ABBR_NAME").val() == ''){
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sc_com_bo&method=checkCOMPANY_ID&COMPANY_ID="+id,
					async:false,
					dataType:'text',
					success: function(result){
						if(!result == ''){
							var txt = '{"detail":' + result + '}';
							var obj = eval ("(" + txt + ")");
							
							var COMPANY_NAME = obj.detail[0].COMPANY_NAME;
							
							$("#SND_COMPANY_NAME").val(COMPANY_NAME);
						}else{
							$("#SND_COMPANY_NAME").val("");
						}
					}
				});
//				}
		}
		
	</script>
</body>
</html>