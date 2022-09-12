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
<%-- 				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a> --%>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">新增</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/agent_cr_line" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="OLD_BASIC_CR_LINE" styleId="OLD_BASIC_CR_LINE" />
					<html:hidden property="OLD_REST_CR_LINE" styleId="OLD_REST_CR_LINE" />
					<table>
						<tr>
							<td class="header necessary" style="width: 40%"><span>發動者統一編號</span></td>
							<td>
								<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="12" maxlength="10"  readonly="true" styleClass="lock validate[required ,maxSize[10],notChinese ,onlyNumberSp] text-input"></html:text>
							</td>
							<td ><span>發動者名稱</span></td>
							<td>
								<html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="40" maxlength="40" readonly="true" styleClass="lock text-input" ></html:text>
							</td>
						</tr>
						<tr valign="middle">
							<td class="header"><span>基本額度</span></td>
							<td><html:text styleId="BASIC_CR_LINE" property="BASIC_CR_LINE"   readonly="true"  size="14" maxlength="14" styleClass="lock validate[required,,maxSize[11],custom[integer,min[0],max[99999999999]]] text-input"></html:text></td>
							<td ><span>基本額度調整</span></td>
							<td >
								<table border="0">
									<tr >
										<td rowspan="2">
										<input type="radio" name="BASIC_CR" value="add" checked="checked">+項<br> 
										<input type="radio" name="BASIC_CR" value="minus">-&nbsp;項 
										</td>
									</tr>
									<tr >
										<td >
										<input id="BASIC_CR_ADJ" type="text" size="14" maxlength="14" class="validate[custom[integer,min[0]]]" value = "0" > 
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="header"><span>剩餘額度</span></td>
							<td><html:text  styleId="REST_CR_LINE" property="REST_CR_LINE"   readonly="true" size="14" maxlength="14" styleClass=" lock validate[custom[integer,min[0],,maxSize[11],max[99999999999]]] text-input"></html:text></td>
							<td ><span>剩餘額度調整</span></td>
							<td >
								<table border="0">
								<tr >
									<td rowspan="2">
									<input type="radio" name="REST_CR" value="add" checked="checked" >+項<br> 
									<input type="radio" name="REST_CR" value="minus">-&nbsp;項
									</td>
								</tr>
								<tr >
									<td >
									<input id="REST_CR_ADJ" type="text" value = "0" size="13" maxlength="13" >
									</td>
								</tr>
								</table>
							 </td>
						</tr>
						<tr>
						
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
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
		
		
		
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
// 			function onPut(str,isValidate){
// 				$("#ac_key").val(str);
// 				$("#target").val('search');
// 				if(isValidate){
// 					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
// 						return false;
// 					}
// 				}else{
// 					$("#formID").validationEngine('detach');
// 				}
// 				$("form").submit();
// 			}	
			
			function onPut(str,isValidate){
				if(str=='update'){
					countCR();
				}
				$("#ac_key").val(str);
				$("#target").val('search');
				if(isValidate){
					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
						return false;
					}
// 					if(str=='update'){
// 						countCR();
// 					}
// 					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
// 						return false;
// 					}
				}else{
					$("#formID").validationEngine('detach');
				}
				$("form").submit();
			}	
			
			
			function init(){
				alterMsg();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setCRval();
// 				setDatePicker("#ACTIVE_DATE", 0);
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="agent_cr_line_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
// cleanFormII(this);
				onPut(str);
			}
			
			function setCRval(){
				$("#OLD_REST_CR_ADJ").val($("#REST_CR_LINE").val());
				$("#OLD_BASIC_CR_ADJ").val($("#BASIC_CR_LINE").val());
			}
			
			function chg_rest(value){
				if(window.console) console.log(value);
				$("input[name='REST_CR_LINE']").val(value);
			}
			

			function checkCOMPANY_ID(){
				var id = $("#SND_COMPANY_ID").val();
// 				if($("#COMPANY_NAME").val() == '' && $("#COMPANY_ABBR_NAME").val() == ''){
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
								
								$("#COMPANY_NAME").val(COMPANY_NAME);
							}else{
								$("#COMPANY_NAME").val("");
							}
						}
					});
// 				}
			}
			
			

			function setCRval(){
				$("#OLD_REST_CR_ADJ").val($("#REST_CR_LINE").val());
				$("#OLD_BASIC_CR_ADJ").val($("#BASIC_CR_LINE").val());
			}
			
			function countCR(){
				var ras = $("input[type=radio]:checked");
				var basic_line = 0;
				var reset_line = 0;
				var val = 0;
				var cr_adj = 0;
				var id = "";
				var tmpadj = 0;
// 				if(window.console){console.log("length>>"+ras.length);}
				for(var i =0 ; i<ras.length ; i++){
					id = $(ras[i]).attr("name");
// 					if(window.console){console.log("id>>"+id);}
					if(id =='BASIC_CR'){
// 						cr_adj = Math.abs( parseInt($("#"+id+"_ADJ").val()));
						cr_adj = parseInt($("#"+id+"_ADJ").val());
						if(cr_adj<0){cr_adj=0;}
						basic_line = parseInt($("#BASIC_CR_LINE").val());
						if($(ras[i]).val() == 'add'){
							val = basic_line+cr_adj;
							tmpadj = cr_adj;
						}
						if($(ras[i]).val() == 'minus'){
							val = basic_line+(-cr_adj);
							tmpadj = -cr_adj;
						}
						
						$("#"+id+"_LINE").val(val);
					}
					
					if(id =='REST_CR'){
// 						cr_adj = Math.abs( parseInt($("#"+id+"_ADJ").val()));
						cr_adj =  parseInt($("#"+id+"_ADJ").val());
						if(cr_adj<0){cr_adj=0;}
						reset_line = parseInt($("#"+id+"_LINE").val());
						if($(ras[i]).val() == 'add'){
							val = reset_line+cr_adj+tmpadj;
						}
						if($(ras[i]).val() == 'minus'){
							val = reset_line+(-cr_adj)+tmpadj;
						}
						if(window.console){console.log("RESET.val>>"+val);}
						$("#"+id+"_LINE").val(val);
					}
				}
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
