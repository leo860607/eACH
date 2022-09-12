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
			<div id="dataInputTable">
				<html:form styleId="formID" action="/sd_com" method="POST" >
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SND_BRBK_NAME" styleId="SND_BRBK_NAME" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="FEE_TYPE_ORG" styleId="FEE_TYPE_ORG" value=""/>
					<html:hidden property="FEE_TYPE_ACTIVE_DATE_ORG" styleId="FEE_TYPE_ACTIVE_DATE_ORG" value=""/>
					<html:hidden property="BIZDATE" styleId="BIZDATE" />
					<html:hidden property="FEE_ID" styleId="FEE_ID" />
					<table>
						<tr>
							<td class="header necessary"><span>交易代號</span></td>
							<td>
								<html:text styleId="TXN_ID" property="TXN_ID" readonly="true" styleClass="lock validate[required,maxSize[10]] text-input"></html:text>
							</td>
							<td class="header necessary"><span >收費類型</span></td>
							<td >												
									<html:select styleId="FEE_TYPE" property="FEE_TYPE" styleClass="validate[required]"  onchange="chgCondition();chgCondition2(this.id);">
										<html:option value="">=== 請選擇收費類型 ===</html:option>
									</html:select>					
							</td>
						</tr>
						
						<tr>
							<td class="header necessary" align="right"><span>發動分行代號</span></td>
							<td>
								<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID"  readonly="true" size="7" maxlength="7" styleClass="lock validate[required,maxSize[7]] text-input" ></html:text>
							</td>
							<td class="header" id="TD1"><span>手續費啟用日期<br>(民國年 ex.01030101)</span></td>
							<td ><html:text styleId="FEE_TYPE_ACTIVE_DATE" property="FEE_TYPE_ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker" onchange="chgCondition2(this.id)"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>發動者統一編號</span></td>
							<td><html:text styleId="COMPANY_ID" property="COMPANY_ID" readonly="true" size="12" maxlength="12" styleClass="lock validate[required,maxSize[10],notChinese] text-input" ></html:text></td>
							<td class="header"><span>發動分行名稱</span></td>
							<td><input id="SND_BRBK_NAME2" class="lock" type="text" readonly="true" disabled/></td>
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
								<%--<html:text property="CONTACT_INFO" size="40" maxlength="53"></html:text> --%>
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
							<logic:equal  name="sd_com_form" property="LOCK_ACTIVE_DATE" value="Y">
								<td class="header  necessary"><span>啟用日期<br>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="text-input lock"></html:text></td>
							</logic:equal>
							<logic:equal  name="sd_com_form" property="LOCK_ACTIVE_DATE" value="N">
								<td class="header  necessary"><span>啟用日期<br>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,required] text-input datepicker"></html:text></td>
							</logic:equal>
							<td class="header"><span>停用日期<br>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text></td>
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
				$("#FEE_TYPE").val('<bean:write name="sd_com_form" property="FEE_TYPE"/>');
				//將原本的FEE_TYPE傳到後端
				$('#FEE_TYPE_ORG').val($('#FEE_TYPE').val());
				
				//如果一進來頁面FEE_TYPE_ACTIVE_DATE 有值 必填
				if($('#FEE_TYPE_ACTIVE_DATE').val()!=''){
					$('#TD1').addClass('necessary');
					$('#FEE_TYPE_ACTIVE_DATE').addClass('validate[required]');
				}
				
				
				
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#SND_BRBK_NAME2").val(getSndBrbkName($("#SND_BRBK_ID").val()));
				$("#ACTIVE_DATE").val('<bean:write name="sd_com_form" property="ACTIVE_DATE"/>');
				$("#FEE_TYPE_ACTIVE_DATE").val('<bean:write name="sd_com_form" property="FEE_TYPE_ACTIVE_DATE"/>');
				$('#FEE_TYPE_ACTIVE_DATE_ORG').val($('#FEE_TYPE_ACTIVE_DATE').val());
				$("#STOP_DATE").val('<bean:write name="sd_com_form" property="STOP_DATE"/>');
				$("#DISPATCH_DATE").val('<bean:write name="sd_com_form" property="DISPATCH_DATE"/>');
				
// 				if($('#BIZDATE').val()>$('#ACTIVE_DATE').val()){
// 					$('#ACTIVE_DATE').addClass('lock').attr('readonly', true);
// 					removeDatePicker($('#ACTIVE_DATE'));
// 					$("#ACTIVE_DATE").val('<bean:write name="sd_com_form" property="ACTIVE_DATE"/>');
// 					$('#STOP_DATE').addClass('lock').attr('readonly', true);
// 					removeDatePicker($('#STOP_DATE'));
// 					$('#START_DATE').addClass('lock').attr('readonly', true);
// 					removeDatePicker($('#START_DATE'));
// 					$("#STOP_DATE").val('<bean:write name="sd_com_form" property="STOP_DATE"/>');
// 					$('#DISPATCH_DATE').addClass('lock').attr('readonly', true);
// 					removeDatePicker($('#DISPATCH_DATE'));
// 					$("#DISPATCH_DATE").val('<bean:write name="sd_com_form" property="DISPATCH_DATE"/>');
// 				}
				
				var value = $("#CONTACT_INFO").val();
				$("#CONTACT_INFO").val(value.replace(/(\\n)/g,'\n'));
			}
			
			function getSndBrbkName(brbkId){
				var uri = "${pageContext.request.contextPath}"+"/baseInfo";
				var rdata = {component:"bank_branch_bo", method:"getBranchName", brbkId: brbkId};
				var sndBrbkName = fstop.getServerDataExII(uri, rdata, false);
				if(sndBrbkName != null){
					sndBrbkName = sndBrbkName.BRBK_NAME;
				}
				return sndBrbkName;
			}
		
			function onPut(str , isValidate){
				$("#SND_BRBK_NAME").val($("#SND_BRBK_NAME2").val());
				if(isValidate){
					if(checkAbbrName() == false){
						return;
					}
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
					
					var name ='<bean:write name="sd_com_form" property="COMPANY_NAME"/>';
					var abbr_name ='<bean:write name="sd_com_form" property="COMPANY_ABBR_NAME"/>';
					var COMPANY_NAME=$("#COMPANY_NAME").val();
					var COMPANY_ABBR_NAME=$("#COMPANY_ABBR_NAME").val();
					var id =$("#COMPANY_ID").val();
					var txnid =$("#TXN_ID").val();
					var fee_type =$("#FEE_TYPE").val();
					var fee_type_active_date =$("#FEE_TYPE_ACTIVE_DATE").val();
					var snd_brbk_id =$("#SND_BRBK_ID").val();
					var active_date =$("#ACTIVE_DATE").val();
					
					if(name != COMPANY_NAME || abbr_name != COMPANY_ABBR_NAME){
						var count = checkAmount(id,COMPANY_NAME,COMPANY_ABBR_NAME);
						if(count > 1){
							if(confirm("此「發動者統一編號」："+id+"  尚有 "+(count-1)+" 筆不同資料，是否同步修改其他"+(count-1)+"筆的「發動者簡稱」、「發動者名稱」?")){
								updateNameById(id,COMPANY_NAME,COMPANY_ABBR_NAME);
								
							}else{
								return;
							}
						}
					}
					//檢核：啟用日期需大於歷程內最小那一筆手續費啟用日
					if(!check_min_fee_type_active_date(id,txnid,snd_brbk_id,active_date)){
						alert("啟用日期不可小於歷程內最小手續費啟用日");
						return;
					}
					if(CHG_FEE_TYPE==true||CHG_FEE_TYPE_ACTIVE_DATE==true){
						
						if(!checkDoubleDate(id , txnid , snd_brbk_id , fee_type_active_date)){
							alert("手續費啟用日期重複，請先刪除歷程設定");
							return;
						}
						
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
					
				}else if(str == 'back'){
					$("#formID").validationEngine('detach');
					blockUI();
					$("#ac_key").val(str);
					$("#target").val('search');
					$("form").submit();
				}
				if(str == "delete" && !confirm("是否確定刪除?")){
// 					20150623 add by hugo 刪除時不檢核欄位
					$("#formID").validationEngine('detach');
					return false;
				}else{
					blockUI();
					$("#ac_key").val(str);
					$("#target").val('search');
					$("form").submit();
				}
			}	
			function removeAllBsType(){
				$("#formID").validationEngine('hide');			
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
			
			function checkSD_SC_TYPE(COMPANY_ID,TXN_ID,FEE_TYPE,SND_BRBK_ID){
				var count;
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sd_com_bo&method=checkSD_SC_TYPE&COMPANY_ID="+COMPANY_ID+"&TXN_ID="+TXN_ID+"&FEE_TYPE="+FEE_TYPE+"&SND_BRBK_ID="+SND_BRBK_ID,
					async:false,
					dataType:'text',
					success:function(result){
						count=result;
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
			
			function updateFee_typeByCIDnTXID(COMPANY_ID,TXN_ID,FEE_TYPE){
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=sd_com_bo&method=updateFee_typeByCIDnTXID&"+encodeURI($("#formID").serialize()),
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
						txnid= $("#TXN_ID").val();
						cleanFormNE(document.getElementById("back"));
						$("#formID").validationEngine('detach');
						$('#formID').attr("action","/eACH/fee_code_nw.do")
						$("#target").val('add_p');
						$("#FEE_ID").val(txnid)
						$("form").submit();
					}else{
// 						cleanFormNE(document.getElementById("back"));
						$("#formID").validationEngine('detach');
						$("#formID").attr("action","/eACH/sd_com.do")
						$("form").submit();
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
				var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate2" ,activeDate:date,compareWay:compareWay};
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
			
			function checkDoubleDate(COMPANY_ID,TXN_ID,SND_BRBK_ID,FEE_TYPE_ACTIVE_DATE){
				var rdata = {component:"sd_com_bo", method:"checkDoubleDate", COMPANY_ID:COMPANY_ID , TXN_ID:TXN_ID , SND_BRBK_ID:SND_BRBK_ID , FEE_TYPE_ACTIVE_DATE:FEE_TYPE_ACTIVE_DATE};
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
			
			function chgCondition(){
				if($('#FEE_TYPE').val() != $('#FEE_TYPE_ORG').val() && $('#FEE_TYPE').val() !=''){
					$('#TD1').addClass('necessary');
					$('#FEE_TYPE_ACTIVE_DATE').addClass('validate[required]');
				}else{
					$('#TD1').removeClass('necessary');
					$('#FEE_TYPE_ACTIVE_DATE').removeClass('validate[required]');
				}
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
			function check_min_fee_type_active_date(COMPANY_ID,TXN_ID,SND_BRBK_ID,ACTIVE_DATE){
				var rdata = {component:"sd_com_bo", method:"check_min_fee_type_active_date", COMPANY_ID:COMPANY_ID , TXN_ID:TXN_ID , SND_BRBK_ID:SND_BRBK_ID , ACTIVE_DATE:ACTIVE_DATE};
				var vResult = fstop.getServerDataExIII(uri, rdata, false);
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
	</body>
</html>