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
							<html:select styleId="PI_COMPANY_ID" property="PI_COMPANY_ID" onchange="getPI_List();">
								<html:option value="">全部</html:option>
								<html:optionsCollection name = "pi_snd_profile_form" property="pi_com_IdList" label="label" value="value"/>
							</html:select>
						</td>
						<td class="header necessary" style="width: 15%"><span>繳費類別代號</span></td>
						<td>
							<html:select styleId="BILL_TYPE_ID" property="BILL_TYPE_ID" styleClass="validate[required]">
								<html:option value="">==請選擇==</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="header necessary" ><span>發動業者統編</span></td>
						<td>
							<div style="width:100%">
								<div style="width:36%;color:red;display:inline;float:left;">
								<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="15" maxlength="10" styleClass="validate[required,maxSize[10],custom[onlyLetterNumber],notChinese] text-input" onblur="getCompanyData(this.value)"></html:text>
								</div>
								<div style="width:1%;color:red;display:inline;float:center;">
									※
								</div>
								<div style="width:59%;color:red;display:inline;float:right;">
									請確認發動者已於「代收發動者基本資料維護」建檔
								</div>
							</div>
						</td>
						<td class="header necessary"><span>發動業者簡稱</span></td>
						<td><html:text  styleClass="lock validate[required,funcCall[checkCompanyAbbr]]" styleId="COMPANY_ABBR_NAME" readonly="true" property="COMPANY_ABBR_NAME" size="10" maxlength="20"></html:text></td>
						
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
			$("#START_DATE").val('<bean:write name="pi_snd_profile_form" property="START_DATE"/>');
			$("#STOP_DATE").val('<bean:write name="pi_snd_profile_form" property="STOP_DATE"/>');
			$("#formID").validationEngine({binded: false, promptPosition: "bottomLeft"});
// 			getBrbk_List($("#BGBK_ID").val());

			getPI_List();
			$("#BILL_TYPE_ID").val('<bean:write name="pi_snd_profile_form" property="BILL_TYPE_ID"/>');
			
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
			var msg = '<bean:write name="pi_snd_profile_form" property="msg"/>';
			if(fstop.isNotEmptyString(msg)){
				alert(msg);
			}
		}
		
		
		
		function getCompanyData(companyId){
			fstop.getServerDataEx(uri + "?component=pi_snd_profile_bo&method=getCompanyDataByCompanyId&SND_COMPANY_ID=" + companyId, null, false, function(data){
				$("#COMPANY_ABBR_NAME").val(data.COMPANY_ABBR_NAME);
				$("#COMPANY_NAME").val(data.COMPANY_NAME);
			});
		}
		
		function checkCompanyAbbr(){
			var abbr_name = $("#COMPANY_ABBR_NAME").val();
			if(abbr_name.match(/.*[^\u0000-\u007F]+.*/)){
				if(abbr_name.match(/[^\u0000-\u007F]/g).length > 4){
					return '* 「業者簡稱」最多只能輸入4個中文';
				}
			}
		}
		
		function onPut(str, isValidate){
			if(isValidate){
				if(checkCompanyAbbr() == false){
					return;
				}
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				
				var COMPANY_NAME=$("#COMPANY_NAME").val();
				var COMPANY_ABBR_NAME=$("#COMPANY_ABBR_NAME").val();
				var id =$("#SND_COMPANY_ID").val();
				
				var count = checkAmount(id,COMPANY_NAME,COMPANY_ABBR_NAME);
				if(count >= 1){
					var res = checkName(COMPANY_NAME,COMPANY_ABBR_NAME);
					if(res == true){
// 						if(confirm("此「發動者統一編號」："+id+"  尚有 "+(count)+" 筆不同資料，是否同步更新其他"+(count)+"筆的「發動者簡稱」、「發動者名稱」?")){
// 							updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME);
// 						}else{
							alert("此「發動者統一編號」："+id+"的「發動者簡稱」、「發動者名稱」有誤")
							return;
// 						}
					}
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
		

		function getPI_List(){
			var pi_company_id = $("#PI_COMPANY_ID").val();
			if(pi_company_id == '' || pi_company_id == "all"){
				$("#BILL_TYPE_ID option:not(:first-child)").remove();
			}else{
				var rdata = {component:"pi_snd_profile_bo", method:"getBillIdListByPI", PI_COMPANY_ID:pi_company_id };
				fstop.getServerDataExII(uri, rdata, false, creatPI_List);
			}
		}
		
		function creatPI_List(obj){
			var select = $("#BILL_TYPE_ID");
			$("#BILL_TYPE_ID option:not(:first-child)").remove();
			if(window.console){console.log("url="+obj.result);}
			if(obj.result=="TRUE"){
				var dataAry =  obj.msg;
				for(var i = 0; i < dataAry.length; i++){
					select.append($("<option></option>").attr("value", dataAry[i].value).text(dataAry[i].label));
				}
			}else if(obj.result=="FALSE"){
				alert(obj.msg);
			}
		}
		
		function checkAmount(id,COMPANY_NAME,COMPANY_ABBR_NAME){
			var count;
			$.ajax({
				type:'POST',
				url:"/eACH/baseInfo?component=sc_com_bo&method=checkAmount&COMPANY_ID="+id,
				async:false,
				dataType:'text',
				success:function(result){
					count = result;
				}
			});
			return count;
		}
		
		function checkName(name,abbr_name){
			var id =$("#SND_COMPANY_ID").val();
			var res;
			$.ajax({
				type:'POST',
				url:"/eACH/baseInfo?component=sc_com_bo&method=checkCOMPANY_ID&COMPANY_ID="+id,
				async:false,
				dataType:'text',
				success:function(result){
					if(!result  == ''){
						var txt = '{"detail":' + result + '}';
						var obj = eval ("(" + txt + ")");
						
						var COMPANY_NAME = obj.detail[0].COMPANY_NAME;
						var COMPANY_ABBR_NAME = obj.detail[0].COMPANY_ABBR_NAME;
						
						if(name != COMPANY_NAME || abbr_name != COMPANY_ABBR_NAME){
							res = true;
						}else{
							res = false;
						}
					}
				}
			});
			return res;
		}

// 		function updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME){
// 			$.ajax({
// 				type:'POST',
// 				url:"/eACH/baseInfo?component=sc_com_bo&method=updateNameByIdOnly930&"+encodeURI($("#formID").serialize()),
// 				async:false,
// 				dataType:'text',
// 				success:function(result){
					
// 				}
// 			});
// 		}
	</script>
</body>
</html>