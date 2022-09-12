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
		<html:form styleId="formID" action="/agent_profile" method="POST" >
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
						<td><html:text styleId="COMPANY_ID" property="COMPANY_ID" size="15" maxlength="10" onkeyup="checkCOMPANY_ID()" onchange="checkCOMPANY_ID()" styleClass="validate[required,maxSize[10],notChinese,onlyNumberSp] text-input"></html:text></td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>代理業者簡稱</span>
						</td>
						<td>
							<html:text styleId="COMPANY_ABBR_NAME" property="COMPANY_ABBR_NAME" size="15" maxlength="13" styleClass="validate[required,funcCall[checkAbbrName]]"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>代理業者名稱</span>
						</td>
						<td>
							<html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="60" maxlength="66" styleClass="validate[required]"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>啟用日期(民國年 ex.01030101)</span>
						</td>
						<td>
							<html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[ required, maxSize[8],minSize[8],twDate,twPast[STOP_DATE],notChinese] text-input datepicker"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header">
							<span>停用日期(民國年 ex.01030101)</span>
						</td>
						<td>
							<html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,twFuture[ACTIVE_DATE],notChinese] text-input datepicker"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header">
							<span>WebService URL</span>
						</td>
						<td>
							<html:text styleId="WS_URL" property="WS_URL" size="80" maxlength="200" styleClass="validate[custom[url],maxSize[200],notChinese] text-input"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header">
							<span>WebService NameSpace</span>
						</td>
						<td>
							<html:text styleId="WS_NAME_SPACE" property="WS_NAME_SPACE" size="80" maxlength="200" styleClass="validate[maxSize[200],notChinese] text-input"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>代理業者基碼</span>
						</td>
						<td>
							<html:text styleId="KEY_ID" property="KEY_ID" size="5" maxlength="3" styleClass="validate[required , maxSize[3],notChinese] text-input"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>代理業者基碼代號</span>
						</td>
						<td>
						<html:select styleId="KEY_FLAG"  property="KEY_FLAG" styleClass="validate[required]">
						<html:option value="">===請選擇===</html:option>
						<html:option value="01">01</html:option>
						<html:option value="02">02</html:option>
						<html:option value="03">03</html:option>
						<html:option value="04">04</html:option>
						</html:select>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span>代理業者代號</span>
						</td>
						<td>
							<html:text styleId="COMPANY_NO" property="COMPANY_NO" size="11" maxlength="9" styleClass="validate[required,maxSize[9]] text-input"></html:text>
							&nbsp;(會計科用)
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
							<label class="btn" id="save" onclick="onPut(this.id, true)"><img src="./images/save.png"/>&nbsp;儲存</label>
							<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
							<label class="btn" id="back"  onclick="onPut(this.id, false)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
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
			$("#ACTIVE_DATE").val('<bean:write name="agent_profile_form" property="ACTIVE_DATE"/>');
			$("#STOP_DATE").val('<bean:write name="agent_profile_form" property="STOP_DATE"/>');
		}
		
		function alterMsg(){
			var msg = '<bean:write name="agent_profile_form" property="msg"/>';
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
		
		function checkAbbrName(field, rules, i, options){
			var abbr_name = field.val();
			if(abbr_name.match(/.*[^\u0000-\u007F]+.*/)){
				if(abbr_name.match(/[^\u0000-\u007F]/g).length > 4){
					return '「代理業者簡稱」最多只能輸入4個中文';
				}
			}
		}
		
		function checkCOMPANY_ID(){
			var id = $("#COMPANY_ID").val();
			if($("#COMPANY_NAME").val() == '' && $("#COMPANY_ABBR_NAME").val() == ''){
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
							var COMPANY_ABBR_NAME = obj.detail[0].COMPANY_ABBR_NAME;
							
							$("#COMPANY_NAME").val(COMPANY_NAME);
							$("#COMPANY_ABBR_NAME").val(COMPANY_ABBR_NAME);
						}else{
							$("#COMPANY_NAME").val("");
							$("#COMPANY_ABBR_NAME").val("");
						}
					}
				});
			}
		}
	</script>
</body>
</html>