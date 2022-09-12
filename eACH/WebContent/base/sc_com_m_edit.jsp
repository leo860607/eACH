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
			<html:form styleId="formID" action="/sc_com" method="POST" >
			<html:hidden property="ac_key" styleId="ac_key" value=""/>
			<html:hidden property="target" styleId="target" value=""/>
			<html:hidden property="serchStrs" styleId="serchStrs" />
			<html:hidden property="sortname" styleId="sortname" />
			<html:hidden property="sortorder" styleId="sortorder" />
			<html:hidden property="edit_params" styleId="edit_params" />
			<html:hidden property="pageForSort" styleId="pageForSort"/>
			<html:hidden property="FEE_TYPE_ORG" styleId="FEE_TYPE_ORG" value=""/>
			<html:hidden property="FEE_TYPE_ACTIVE_DATE_ORG" styleId="FEE_TYPE_ACTIVE_DATE_ORG" value=""/>
			<html:hidden property="BIZDATE" styleId="BIZDATE" />
				<div id="dataInputTable">
					<table>
						<tr>
							<td class="header necessary" style="width: 50%">
								<span >交易代號</span>
							</td>
							<td >
								<html:text styleClass="lock" styleId="TXN_ID" property="TXN_ID" readonly="true"/>
							</td>
						</tr>
						<tr>						
						<td class="header necessary" style="width: 50%">
							<span >收費類型</span>
						</td>
						<td >					
							<html:select styleId="FEE_TYPE" property="FEE_TYPE" styleClass="validate[required]" onchange="chgCondition();chgCondition2(this.id);">
										<html:option value="">=== 請選擇收費類型 ===</html:option>
							</html:select>										
						</td>
		  			</tr>
		  			<tr >
		  				<td id="TD1" class="header"><span>手續費啟用日期<br>(民國年 ex.01030101)</span></td>
						<td><html:text styleId="FEE_TYPE_ACTIVE_DATE" property="FEE_TYPE_ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker" onchange="chgCondition2(this.id);"></html:text></td>
		  			</tr>
						<tr>
							<td class="header necessary" align="right">
								<span >發動分行代號</span>
							</td>
							<td >
<!-- 							20151112 edit by hugo req by UAT-20151027-02 改成如果查無分行也不檢核，單純查詢已存在的分行的中文名稱 -->
								<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID"  readonly="true" size="7" maxlength="7" styleClass="lock validate[required,maxSize[7]] text-input" ></html:text>
<%-- 								<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID"  size="7" maxlength="7" styleClass="lock validate[required,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName],maxSize[7],] text-input" readonly="true"></html:text> --%>
								<!-- <button type="button" value="SND_BRBK" onclick="branch_search(this.value)">...</button> -->
							</td>
						</tr>
						<tr>
							<td class="header">
								<span>發動分行名稱</span>
							</td>
							<td >
								<input class="lock" id="SND_BRBK_NAME" type="text" value=""  readonly="true">
							</td>
						</tr>
						<tr>
							<td  class="header necessary" >
								<span >發動者統一編號</span>
							</td>
							<td >
								<html:text styleId="COMPANY_ID" property="COMPANY_ID"  size="12" maxlength="10" styleClass="validate[required,maxSize[10]] text-input lock" readonly="true"></html:text>
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
								<span >上市上櫃公司代號</span>
							</td>
							<td >
								<html:text styleId="IPO_COMPANY_ID" property="IPO_COMPANY_ID" size="6" maxlength="6" styleClass="validate[notChinese]"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header">
								<span >現金股息發放日期</span>
							</td>
							<td >
								<html:text styleId="PROFIT_ISSUE_DATE" property="PROFIT_ISSUE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
							</td>
						</tr>
						<tr>
						<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
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
	var startDateCheck = false;
	var feeDateCheck = false;
	var feeNearDate="";
	var CHG_FEE_TYPE_ACTIVE_DATE=false;
	var CHG_FEE_TYPE=false;
		$(document).ready(function () {
			blockUI();
			init();
			disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
		});
		
		function init(){
			alterMsg();
			setDatePicker();
			//先產生收費類型下拉選單  再把前頁的值代入
			getFeetype_List();
			$("#FEE_TYPE").val('<bean:write name="sc_com_form" property="FEE_TYPE"/>');
			
			//將原本的FEE_TYPE傳到後端
			$('#FEE_TYPE_ORG').val($('#FEE_TYPE').val());
			
			
			//如果一進來頁面FEE_TYPE_ACTIVE_DATE 有值 必填
			if($('#FEE_TYPE_ACTIVE_DATE').val()!=''){
				$('#TD1').addClass('necessary');
				$('#FEE_TYPE_ACTIVE_DATE').addClass('validate[required]');
			}
			
			$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			$('#PROFIT_ISSUE_DATE').val('<bean:write name="sc_com_form" property="PROFIT_ISSUE_DATE"/>');
			$("#FEE_TYPE_ACTIVE_DATE").val('<bean:write name="sc_com_form" property="FEE_TYPE_ACTIVE_DATE"/>');
			$('#FEE_TYPE_ACTIVE_DATE_ORG').val($('#FEE_TYPE_ACTIVE_DATE').val());
			
			if($("#SND_BRBK_ID").val() != ''){
				var rdata = {component:'bank_branch_bo', method:'getBranchName', brbkId: $("#SND_BRBK_ID").val()};
				var branch = fstop.getServerDataExII('/eACH/baseInfo', rdata, false);
				$("#SND_BRBK_NAME").val(branch.BRBK_NAME);
			}
			
			validateTXN_ID();
		}	
			
		function onPut(str, isValidate){
			if(isValidate){
				if(checkAbbrName() == false){
					return;
				}
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				
				var name ='<bean:write name="sc_com_form" property="COMPANY_NAME"/>';
				var abbr_name ='<bean:write name="sc_com_form" property="COMPANY_ABBR_NAME"/>';
				var COMPANY_NAME=$("#COMPANY_NAME").val();
				var COMPANY_ABBR_NAME=$("#COMPANY_ABBR_NAME").val();
				var id =$("#COMPANY_ID").val();
				var txnid =$("#TXN_ID").val();
				var fee_type =$("#FEE_TYPE").val();
				var fee_type_active_date =$("#FEE_TYPE_ACTIVE_DATE").val();
				var snd_brbk_id =$("#SND_BRBK_ID").val();				
				
				if(name != COMPANY_NAME || abbr_name != COMPANY_ABBR_NAME){
					var count = checkAmount(id,COMPANY_NAME,COMPANY_ABBR_NAME);
					if(count > 1){
						if(confirm("此「發動者統一編號」："+id+"  尚有 "+(count-1)+" 筆不同資料，是否同步更新其他"+(count-1)+"筆的「發動者簡稱」、「發動者名稱」?")){
							updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME);
						}else{
							return;
						}
					}
				}
				
				if(CHG_FEE_TYPE==true||CHG_FEE_TYPE_ACTIVE_DATE==true){
					
					if( $('#FEE_TYPE_ACTIVE_DATE').val()!=''){
						//判斷2  手續費啟用日期 需大於 BIZDATE
						checkBizDate( $('#formID input') , $('#FEE_TYPE_ACTIVE_DATE').val(),'>');
						if(!startDateCheck){
							alert("手續費啟用日期不可小於次一營業日期");
							return;
						}
						//判斷3 手續費啟用日期 所選之交易代號的收費類型的生效日
						checkActive();
						if(!feeDateCheck){
							alert("手續費啟用日期不可小於此收費類型最小啟用日 ( " + feeNearDate + " )" );
							return;
						}
					}
					if(!checkDoubleDate(id , txnid , snd_brbk_id , fee_type_active_date)){
						alert("手續費啟用日期重複，請先刪除歷程設定");
						return;
					}
				}
				
				//若數字大於0  代表需要跳提示
				//狀況1 設定要檢核 卻無不同資料 COUNT = 0 可繼續儲存
				//狀況2 設定要檢核  COUNT > 0 跳提示
				//狀況3 設定不檢核  直接回傳0 可繼續儲存
				var count = checkSD_SC_TYPE(id,txnid,fee_type,snd_brbk_id);
				if(count > 0){
					if(confirm("此「發動者統一編號」："+id+" 對應之「交易代號」："+ txnid +"  尚有 "+ count +" 筆不同收費類型，是否同步修改其他"+ count +"筆的「收費類型」、「手續費啟用日期」? ")){
						updateFee_typeByCIDnTXID(id,fee_type_active_date);
					}else{
						return;
					}
				}
				
			}else if(str == "back"){
				$("#formID").validationEngine('detach');
			}
			if(str == "delete" ){
//					20150623 add by hugo 刪除時不檢核欄位
				if(confirm("是否確定刪除?")){
					$("#formID").validationEngine('detach');
				}else{
					return;
				}
			}
			blockUI();
			$("#ac_key").val(str);
			$("#target").val('search');
			$("form").submit();
		}	
		function removeAllBsType(){
			$("#formID").validationEngine('hide');			
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
		
		
		function getFeetype_List(txnid) {
			txnid = $("#TXN_ID").val();
			var rdata = {
				component : "sc_com_bo",
				method : "getFeeTypeList",
				TXN_ID : txnid
			};
			fstop.getServerDataExII(uri, rdata, false, creatFeeTypeList);
		}

		function creatFeeTypeList(obj) {
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
		
		function chgCondition(){
			if($('#FEE_TYPE').val() != $('#FEE_TYPE_ORG').val() && $('#FEE_TYPE').val() !=''){
				$('#TD1').addClass('necessary');
				$('#FEE_TYPE_ACTIVE_DATE').addClass('validate[required]');
			}else{
				$('#TD1').removeClass('necessary');
				$('#FEE_TYPE_ACTIVE_DATE').removeClass('validate[required]');
			}
		}
		
		function checkActive(){
			txnid = $("#TXN_ID").val();
			fee_type = $("#FEE_TYPE").val();
			start_date = $("#FEE_TYPE_ACTIVE_DATE").val();
			var rdata = {component:"sd_com_bo", method:"checkActive", TXN_ID:txnid , FEE_TYPE:fee_type , START_DATE:start_date};
			fstop.getServerDataExII(uri, rdata, false, aftercheckActiveAction);
		}
		
		function chgCondition2(id){
			if(id=='FEE_TYPE'){
				if($('#FEE_TYPE').val()!=$('#FEE_TYPE_ORG').val()){
					CHG_FEE_TYPE=true
				}else{
					CHG_FEE_TYPE=false
				}
			}else if(id=='FEE_TYPE_ACTIVE_DATE'){
				if($('#FEE_TYPE_ACTIVE_DATE').val()!=$('#FEE_TYPE_ACTIVE_DATE_ORG').val()){
					CHG_FEE_TYPE_ACTIVE_DATE=true
				}else{
					CHG_FEE_TYPE_ACTIVE_DATE=false;
				}
			}
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
		
		function checkSD_SC_TYPE(COMPANY_ID,TXN_ID,FEE_TYPE,SND_BRBK_ID){
			var count;
			$.ajax({
				type:'POST',
				url:"/eACH/baseInfo?component=sc_com_bo&method=checkSD_SC_TYPE&COMPANY_ID="+COMPANY_ID+"&TXN_ID="+TXN_ID+"&FEE_TYPE="+FEE_TYPE+"&SND_BRBK_ID="+SND_BRBK_ID,
				async:false,
				dataType:'text',
				success:function(result){
					count=result;
				}
			});
			return count;
		}
		
		function updateFee_typeByCIDnTXID(COMPANY_ID,TXN_ID,FEE_TYPE){
			$.ajax({
				type:'POST',
				url:"/eACH/baseInfo?component=sc_com_bo&method=updateFee_typeByCIDnTXID&"+encodeURI($("#formID").serialize()),
				async:false,
				dataType:'text',
				success:function(result){
					
				}
			});
		}
		
		function checkDoubleDate(COMPANY_ID,TXN_ID,SND_BRBK_ID,FEE_TYPE_ACTIVE_DATE){
			var rdata = {component:"sc_com_bo", method:"checkDoubleDate", COMPANY_ID:COMPANY_ID , TXN_ID:TXN_ID , SND_BRBK_ID:SND_BRBK_ID , FEE_TYPE_ACTIVE_DATE:FEE_TYPE_ACTIVE_DATE};
			var vResult = fstop.getServerDataExII(uri, rdata, false);
			if(fstop.isNotEmpty(vResult)){
				if(vResult.result=='TRUE'){
					return true;
				}else{
					return false;
				}
			}else{
				alert("系統異常");
			}
		}
	</script>
</html>
