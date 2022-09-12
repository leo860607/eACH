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
				<html:form styleId="formID" action="/sd_com" method="POST" >
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary"><span>交易代號</span></td>
							<td>
								<html:select styleId="TXN_ID" property="TXN_ID" styleClass="validate[required]" onchange="getFeetype_List(this.value)">
									<html:option value="">==請選擇交易代號==</html:option>
									<html:optionsCollection name = "sd_com_form" property="txnIdList" label="label" value="value"/>
								</html:select>
							</td>
							<td class="header necessary"><span >收費類型</span></td>
							<td >												
									<html:select styleId="FEE_TYPE" property="FEE_TYPE" styleClass="validate[required]">
										<html:option value="">=== 請選擇收費類型 ===</html:option>
									</html:select>					
							</td>
						</tr>
						<tr>
							<td class="header necessary" align="right"><span>發動分行代號</span></td>
							<td>
								<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID" size="7" maxlength="7" styleClass="validate[required,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName],maxSize[7],notChinese] text-input"></html:text>
<%-- 								<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID" size="7" maxlength="7" styleClass="validate[required,branchId_II[/eACH/baseInfo,bank_branch_bo,getBranchName],maxSize[7],notChinese] text-input"></html:text> --%>
								<button type="button" value="SND_BRBK" onclick="branch_search(this.value)">...</button>
							</td>
							<td class="header  necessary"><span>手續費啟用日期<br>(民國年 ex.01030101)</span></td>
							<td ><html:text styleId="FEE_TYPE_ACTIVE_DATE" property="FEE_TYPE_ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,required] text-input datepicker"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>發動者統一編號</span></td>
							<td><html:text onkeyup="checkCOMPANY_ID(),checkCOMPANY_NAME()" onchange="checkCOMPANY_ID(),checkCOMPANY_NAME()" styleId="COMPANY_ID" property="COMPANY_ID" size="12" maxlength="10" styleClass="validate[required,maxSize[10],notChinese] text-input"></html:text></td>
							<td class="header necessary"><span>發動分行名稱</span></td>
							<td>
								<html:text styleClass="lock" styleId="SND_BRBK_NAME" property="SND_BRBK_NAME" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>發動者簡稱</span></td>
							<td><html:text styleId="COMPANY_ABBR_NAME" property="COMPANY_ABBR_NAME" size="15" maxlength="13" styleClass="validate[required,maxSize[13]]"></html:text></td>
							<td class="header necessary"><span>發動者名稱</span></td>
							<td><html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="40" maxlength="66" styleClass="validate[required,maxSize[66]]"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>聯絡人資訊</span></td>
							<td>
								<%-- <html:text property="CONTACT_INFO" size="40" maxlength="53"></html:text> --%>
								<html:textarea styleId="CONTACT_INFO" property="CONTACT_INFO" cols="30" rows="5" styleClass="validate[maxSize[50]]" />
							</td>
							<td class="header necessary"><span>用戶號碼<br/>(交易代號為930時用戶號碼為非必填欄位)</span></td>
							<td><html:text property="USER_NO" size="40" maxlength="66" styleClass="validate[user_no_required[]]"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>文號</span></td>
							<td><html:text property="CASE_NO" size="40" maxlength="66"></html:text></td>
							<td></td><td></td>
						</tr>
						<tr>
							<td class="header"><span>發文日期<br>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="DISPATCH_DATE" property="DISPATCH_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text></td>
							<td class="header"><span>開辦日期</span></td>
							<td><html:text styleId="START_DATE" property="START_DATE" size="20" maxlength="20"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>啟用日期<br>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,required] text-input datepicker"></html:text></td>
							<td class="header"><span>停用日期<br>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text></td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id, true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back" onclick="onPut(this.id,false)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		var startDateCheck = false;
		var feeDateCheck = false;
		var feeNearDate="";
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	       	});
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#DISPATCH_DATE").val('<bean:write name="sd_com_form" property="DISPATCH_DATE"/>');
				$("#ACTIVE_DATE").val('<bean:write name="sd_com_form" property="ACTIVE_DATE"/>');
				$("#STOP_DATE").val('<bean:write name="sd_com_form" property="STOP_DATE"/>');
				$("#TXN_ID").val('');
				
			}
			
			function onPut(str , isValidate){
				if(isValidate){
					if(checkAbbrName() == false){
						return;
					}
					
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
					
					
					//判斷1 業者啟用日期需等於手續費啟用日期
					if(!($("#ACTIVE_DATE").val()==$("#FEE_TYPE_ACTIVE_DATE").val())){
						alert("手續費啟用日期需等於業者啟用日期");
						return ; 
					}
					
					if( $('#FEE_TYPE_ACTIVE_DATE').val()!=''){
						//判斷2  手續費啟用日期 需大於 BIZDATE
						checkBizDate( $('#formID input') , $('#FEE_TYPE_ACTIVE_DATE').val(),'>=');
						if(!startDateCheck){
							alert("手續費啟用日期需大於今天日期");
							return;
						}
						//判斷3 手續費啟用日期 所選之交易代號的收費類型的生效日
						checkActive();
						if(!feeDateCheck){
							alert("手續費啟用日期需大於收費類型最小啟用日 ( " + feeNearDate + " )" );
							return;
						}
					}
					
					$("#ac_key").val('save');
					$("#target").val('search');
					$("form").submit();
					
				}else{
					$("#formID").validationEngine('detach');
					$("#ac_key").val(str);
					$("#target").val('search');
					$("form").submit();
				}
				
			}	
				
			function alterMsg(){
				var msg = '<bean:write name="sd_com_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function checkAbbrName(){
				var abbr_name = $("#COMPANY_ABBR_NAME").val();
				if(abbr_name.match(/.*[^\u0000-\u007F]+.*/)){
					if(abbr_name.match(/[^\u0000-\u007F]/g).length > 4){
						alert('「發動者簡稱」最多只能輸入4個中文');
						return false;
					 }
				}
			}
			
			function checkCOMPANY_ID(){
				var id =$("#COMPANY_ID").val();
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sd_com_bo&method=checkCOMPANY_ID&COMPANY_ID="+id,
					async:false,
					dataType:'text',
					success:function(result){
						if(!result  == ''){
							var txt = '{"detail":' + result + '}';
							var obj = eval ("(" + txt + ")");
							
							var COMPANY_NAME = obj.detail[0].COMPANY_NAME;
							var COMPANY_ABBR_NAME = obj.detail[0].COMPANY_ABBR_NAME;
							
							$("#COMPANY_NAME").val(COMPANY_NAME);
							$("#COMPANY_ABBR_NAME").val(COMPANY_ABBR_NAME);
						}
					}
				});
			}
			
			function checkCOMPANY_NAME(){
				var id =$("#COMPANY_ID").val();
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sd_com_bo&method=checkCOMPANY_NAME&COMPANY_ID="+id,
					async:false,
					dataType:'text',
					success:function(result){
						var count = result;
						if(count  > 1){
							alert('「發動者統一編號」：'+id+'， 有另外 '+(count-1)+' 筆不同的「發動者名稱」');
						}
					}
				});
			}
			
			function checkAmount(id,COMPANY_NAME,COMPANY_ABBR_NAME){
				var count;
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sd_com_bo&method=checkAmount&COMPANY_ID="+id,
					async:false,
					dataType:'text',
					success:function(result){
						count = result;
					}
				});
				return count;
			}
			
			function updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME){
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sd_com_bo&method=updateNameById&"+encodeURI($("#formID").serialize()),
					async:false,
					dataType:'text',
					success:function(result){
						
					}
				});
			}
			
			function getFeetype_List(txnid){
				txnid = $("#TXN_ID").val();
				if(txnid == '' || txnid == "all"){
					$("#FEE_TYPE option:not(:first-child)").remove();
				}else{
					var rdata = {component:"sd_com_bo", method:"getFeeTypeList", TXN_ID:txnid};
					fstop.getServerDataExII(uri, rdata, false, creatFeeTypeList);
				}
			}
			
			function creatFeeTypeList(obj){
				var select = $("#FEE_TYPE");
				$("#FEE_TYPE option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					$("#FEE_TYPE option:first-child").attr("value",'').text("=== 請選擇收費類型 ===");
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].FEE_TYPE).text(dataAry[i].FEE_TYPE_CHT));
					}
				}else if(obj.result=="FALSE"){
					if(confirm("此交易代號尚無任何手續費資料，是否前往新增 ?")){
						$("#formID").validationEngine('detach');
						$('#formID').attr("action","/eACH/fee_code_nw.do")
						$("#target").val('add_p');
						$('#FEE_ID').val($('#TXN_ID').val());
						$("form").submit();
					}else{
						$('#TXN_ID').val("");
						return;
					}
				}
			}
			
			function checkActive(){
				txnid = $("#TXN_ID").val();
				fee_type = $("#FEE_TYPE").val();
				start_date = $("#FEE_TYPE_ACTIVE_DATE").val();
				var rdata = {component:"sd_com_bo", method:"checkActive", TXN_ID:txnid , FEE_TYPE:fee_type , START_DATE:start_date};
				fstop.getServerDataExII(uri, rdata, false, aftercheckActiveAction);
			}
			
			function aftercheckActiveAction(obj){
// 				alert("call aftercheckActiveAction")
// 				alert(obj.result);
				if(obj.result =="FALSE"){
					feeDateCheck=false;
					feeNearDate = obj.msg[0].START_DATE;
				}else{
					feeDateCheck=true;
				}
			}
			
			function checkBizDate( obj ,date,compareWay){
// 				alert("call checkBizDate")
				if(fstop.isEmptyString(date)){
					return;
				}
				var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate3" ,activeDate:date,compareWay:compareWay};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
// 				alert(vResult.result);
				if(fstop.isNotEmpty(vResult)){
					//不可存,跳alert
					if(vResult.result == "TRUE"){
						startDateCheck = true;
					//可存
					}else{
						startDateCheck = false;
					}
					if(window.console){console.log("msg>>"+vResult.msg);}
				}else{
					alert("系統異常");
				}
			}
			
			
		</script>
	</body>
</html>