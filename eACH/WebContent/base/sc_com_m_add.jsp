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
		<html:form styleId="formID" action="/sc_com" method="POST" >
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
						<td class="header necessary" style="width: 50%">
							<span >交易代號</span>
						</td>
						<td >
							<html:select styleId="TXN_ID" property="TXN_ID" styleClass="validate[required]" onchange="validateTXN_ID();getFeetype_List(this.value)">
								<html:option value="">==請選擇交易代號==</html:option>
								<html:optionsCollection name = "sc_com_form" property="txnIdList" label="label" value="value"/>
							</html:select>
						</td>
					</tr>
					<tr>						
						<td class="header necessary" style="width: 50%">
							<span >收費類型</span>
						</td>
						<td >
							<html:select styleId="FEE_TYPE" property="FEE_TYPE" styleClass="validate[required]" >
										<html:option value="">=== 請選擇收費類型 ===</html:option>
							</html:select>										
						</td>
		  			</tr>
		  			<tr>
		  				<td class="header necessary"><span>手續費啟用日期<br>(民國年 ex.01030101)</span></td>
						<td><html:text styleId="FEE_TYPE_ACTIVE_DATE" property="FEE_TYPE_ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,required] text-input lock" readonly="true" ></html:text></td>
		  			</tr>
					<tr>
						<td class="header necessary" align="right">
							<span >發動分行代號</span>
						</td>
						<td >
							<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID" size="7" maxlength="7" styleClass="validate[required,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName],maxSize[7],notChinese] text-input"></html:text>
<%-- 							<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID" size="7" maxlength="7" styleClass="validate[required,branchId_II[/eACH/baseInfo,bank_branch_bo,getBranchName],maxSize[7],notChinese] text-input"></html:text> --%>
							<button type="button" value="SND_BRBK" onclick="branch_search(this.value)">...</button>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span >發動分行名稱</span>
						</td>
						<td>
							<html:text styleId="SND_BRBK_NAME" property="SND_BRBK_NAME" readonly="true" styleClass="lock"/>
						</td>
					</tr>
					<tr>
						<td  class="header necessary" >
							<span >發動者統一編號</span>
						</td>
						<td >
							<html:text onkeyup="checkCOMPANY_ID(),checkCOMPANY_NAME()" onchange="checkCOMPANY_ID(),checkCOMPANY_NAME()" styleId="COMPANY_ID" property="COMPANY_ID" size="12" maxlength="10" styleClass="validate[required,maxSize[10],notChinese] text-input" ></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span >發動者簡稱</span>
						</td>
						<td >
							<html:text styleId="COMPANY_ABBR_NAME" property="COMPANY_ABBR_NAME" size="15" maxlength="13" styleClass="validate[required]"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header necessary">
							<span >發動者名稱</span>
						</td>
						<td >
							<html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="60" maxlength="66" styleClass="validate[required]"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header">
							<span>上市上櫃公司代號</span>
						</td>
						<td>
							<html:text styleId="IPO_COMPANY_ID" property="IPO_COMPANY_ID" size="6" maxlength="6" styleClass="validate[notChinese]"></html:text>
						</td>
					</tr>
					<tr>
						<td class="header">
							<span>現金股息發放日期</span>
						</td>
						<td>
							<html:text styleId="PROFIT_ISSUE_DATE" property="PROFIT_ISSUE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
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
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		var startDateCheck = false;
		var feeDateCheck = false;
		var feeNearDate="";
		$(document).ready(function () {
			blockUI();
			init();
			disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
		});
		
		function onPut(str , isValidate){
			if(isValidate){
				if(checkAbbrName() == false){
					return;
				}
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				
				var COMPANY_NAME=$("#COMPANY_NAME").val();
				var COMPANY_ABBR_NAME=$("#COMPANY_ABBR_NAME").val();
				var id =$("#COMPANY_ID").val();
				
				var count = checkAmount(id,COMPANY_NAME,COMPANY_ABBR_NAME);
				if(count > 1){
					var res = checkName(COMPANY_NAME,COMPANY_ABBR_NAME);
					if(res == true){
						if(confirm("此「發動者統一編號」："+id+"  尚有 "+(count)+" 筆不同資料，是否同步更新其他"+(count)+"筆的「發動者簡稱」、「發動者名稱」?")){
							updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME);
						}else{
							return;
						}
					}
				}
				
				if( $('#FEE_TYPE_ACTIVE_DATE').val()!=''){
					//判斷2  手續費啟用日期 需大於 BIZDATE
					checkBizDate( $('#formID input') , $('#FEE_TYPE_ACTIVE_DATE').val(),'=');
					if(!startDateCheck){
						alert("手續費啟用日期需等於營業日期");
						return;
					}
					//判斷3 手續費啟用日期 所選之交易代號的收費類型的生效日
					checkActive();
					if(!feeDateCheck){
						alert("手續費啟用日期不可小於此收費類型最小啟用日 ( " + feeNearDate + " )" );
						return;
					}
				}
			}else{
				$("#formID").validationEngine('detach');
			}
			$("#ac_key").val(str);
			$("#target").val('search');
			$("form").submit();
		}
		
		function removeAllBsType(){
			$("#formID").validationEngine('hide');			
		}
		function init(){
			$("#SND_BRBK_ID").val('');
			alterMsg();
			setDatePicker();
			$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			$("#PROFIT_ISSUE_DATE").val('<bean:write name="sc_com_form" property="PROFIT_ISSUE_DATE"/>');
			$("#FEE_TYPE_ACTIVE_DATE").val('<bean:write name="sc_com_form" property="BIZDATE"/>');
			getFeetype_List('<bean:write name="sc_com_form" property="TXN_ID"/>')
		}	
		function alterMsg(){
			var msg = '<bean:write name="sc_com_form" property="msg"/>';
			if(fstop.isNotEmptyString(msg)){
				alert(msg);
			}
		}	
// 		20150709 edit by hugo  依據UAT-20150701-05 改成不檢核
		//交易代號為201、202時，「上市上櫃公司代號」、「現金股息發放日期」為必要欄位
		function validateTXN_ID(){
// 			var txnId = $("#TXN_ID").val();
// 			if(txnId == "201" || txnId == "202"){
// 				$("#IPO_COMPANY_ID").parent().prev().addClass("necessary");
// 				$("#PROFIT_ISSUE_DATE").parent().prev().addClass("necessary");
// 				$("#IPO_COMPANY_ID").addClass("validate[required]");
// 				change_PROFIT_ISSUE_DATE_Class(true);
// 			}else{
// 				$("#IPO_COMPANY_ID").parent().prev().removeClass("necessary");
// 				$("#PROFIT_ISSUE_DATE").parent().prev().removeClass("necessary");
// 				$("#IPO_COMPANY_ID").removeClass("validate[required]");
// 				change_PROFIT_ISSUE_DATE_Class(false);
// 			}
		}
		
		function change_PROFIT_ISSUE_DATE_Class(isNeccessary){
			var newClasses = [];
			var classes = $("#PROFIT_ISSUE_DATE").attr("class").split(" ");
			var vdClass;
			for(var i = 0; i < classes.length; i++){
				if(classes[i].indexOf("validate[") != -1){
					vdClass = classes[i];
				}else{
					newClasses.push(classes[i]);
				}
			}
			var vdSubClasses = vdClass.substring(vdClass.indexOf("[") +  1, vdClass.lastIndexOf("]")).split(",");
			var index = -1;
			for(var i = 0; i < vdSubClasses.length; i++){
				if(vdSubClasses[i] == "required"){
					index = i;
				}
			}
			$("#PROFIT_ISSUE_DATE").removeClass();
			if(isNeccessary){
				if(index < 0) {vdSubClasses.push("required");}
			}else{
				if(index >= 0) {vdSubClasses.splice( index, 1 );}
			}
			var newVdClass = "validate[" + vdSubClasses.join() + "]";
			newClasses.push(newVdClass);
			for(var i = 0; i < newClasses.length; i++){
				$("#PROFIT_ISSUE_DATE").addClass(newClasses[i]);
			}
			if(window.console){console.log('$("#PROFIT_ISSUE_DATE").attr("class") >> ' + $("#PROFIT_ISSUE_DATE").attr("class"));}
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
				url:"/eACH/baseInfo?component=sc_com_bo&method=checkCOMPANY_ID&COMPANY_ID="+id,
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
		
		function checkName(name,abbr_name){
			var id =$("#COMPANY_ID").val();
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

		function checkCOMPANY_NAME(){
			var id =$("#COMPANY_ID").val();
			$.ajax({
				type:'POST',
				url:"/eACH/baseInfo?component=sc_com_bo&method=checkCOMPANY_NAME&COMPANY_ID="+id,
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
				url:"/eACH/baseInfo?component=sc_com_bo&method=checkAmount&COMPANY_ID="+id,
				async:false,
				dataType:'text',
				success:function(result){
					count = result;
// 					if(count > 1){
// 						if(confirm("此「發動者統一編號」："+id+"  尚有 "+(count-1)+" 筆不同資料，是否同步修改其他"+(count-1)+"筆的「發動者簡稱」、「發動者名稱」?")){
// 							updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME);
// 						}
// 					}
				}
			});
			return count;
		}

		function updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME){
			$.ajax({
				type:'POST',
				url:"/eACH/baseInfo?component=sc_com_bo&method=updateNameById&"+encodeURI($("#formID").serialize()),
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
				var rdata = {component:"sc_com_bo", method:"getFeeTypeList", TXN_ID:txnid};
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
		
		function checkBizDate( obj ,date,compareWay){
//				alert("call checkBizDate")
			if(fstop.isEmptyString(date)){
				return;
			}
			var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate2" ,activeDate:date,compareWay:compareWay};
			var vResult = fstop.getServerDataExII(uri,rdata,false);
//				alert(vResult.result);
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
		function checkActive(){
			txnid = $("#TXN_ID").val();
			fee_type = $("#FEE_TYPE").val();
			start_date = $("#FEE_TYPE_ACTIVE_DATE").val();
			var rdata = {component:"sc_com_bo", method:"checkActive", TXN_ID:txnid , FEE_TYPE:fee_type , START_DATE:start_date};
			fstop.getServerDataExII(uri, rdata, false, aftercheckActiveAction);
		}
		function aftercheckActiveAction(obj){
//				alert("call aftercheckActiveAction")
//				alert(obj.result);
			if(obj.result =="FALSE"){
				feeDateCheck=false;
				feeNearDate = obj.msg[0].START_DATE;
			}else{
				feeDateCheck=true;
			}
		}
	</script>
</body>
</html>
