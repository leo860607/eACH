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
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
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
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/txn_code" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:select styleId="selectedFeeAry" property="selectedFeeAry" style="display: none" multiple="true"></html:select>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 25%"><span>交易代號</span></td>
							<td style="width: 23%"><html:text styleId="TXN_ID" property="TXN_ID" readonly="true" size="3" styleClass="lock validate[required] text-input"></html:text></td>
							<td class="header necessary" style="width: 18%"><span>交易項目</span></td>
							<td><html:text styleId="TXN_NAME" property="TXN_NAME" size="10" maxlength="10" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交易類別</span></td>
							<td>
								<html:select styleId="TXN_TYPE" property="TXN_TYPE" onchange="setTXN_CHECK_TYPE(this.value)" styleClass="validate[required]">
									<html:option value="">===請選擇交易類別===</html:option>
									<html:option value="SC">SC - 入帳</html:option>
									<html:option value="SD">SD - 扣款</html:option>
								</html:select>
							</td>
							<td class="header necessary"><span>適用業務類別</span></td>
							<td>
								<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID" onchange="getAgent_txn_id(this.value)" styleClass="validate[required]">
									<html:option value="">====請選擇業務類別====</html:option>
									<html:optionsCollection name="txn_code_form" property="bsTypeIdList" label="label" value="value"></html:optionsCollection>
								</html:select>
							</td>
						</tr>
						
						<tr>
							<td class="header necessary"><span>金額上限</span></td>
							<td><html:text styleId="MAX_TXN_AMT" property="MAX_TXN_AMT" size="17" maxlength="17" styleClass="validate[required,decimal[16,2]]"></html:text></td>
							<logic:notEqual name="login_form" property="userData.USER_TYPE" value="B">
							<td class="header" ><span>代理業者交易類別</span></td>
							<td >
								<html:select styleId="AGENT_TXN_ID" property="AGENT_TXN_ID" >
									<html:option value="">====請選擇代理業者交易類別====</html:option>
								</html:select>
							</td>
							</logic:notEqual>
						</tr>
						<tr>
							<td class="header necessary"><span>啟用日期(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text></td>
							<td class="header necessary"><span>入帳檢核統一編號</span></td>
							<td>
								<html:radio styleId="TXN_CHECK_TYPE_1" property="TXN_CHECK_TYPE" value="1">檢核</html:radio>
								<html:radio styleId="TXN_CHECK_TYPE_2" property="TXN_CHECK_TYPE" value="2">不檢核</html:radio>
							</td>
						</tr>
						<tr>
							<td class="header"><span>交易說明</span></td>
							<td colspan="3"><html:text property="TXN_DESC" size="70" maxlength="66"></html:text></td>
						</tr>
					</table>
				</html:form>
			</div>
			<div style="width: 100%; text-align: center">
				<table style="width: 100%">
					<tr>
						<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
						
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
							</logic:equal>
							<label class="btn" id="back"  onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
						</td>
					</tr>
				</table>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		var isDelete = false;
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function back(str){
				$("#formID").validationEngine('detach');
				$("#ac_key").val(str);
				$("#target").val('search');
				$("#TXN_ID").attr('disabled', false);
				$("input[name=TXN_CHECK_TYPE]").attr('disabled', false);
				$("form").submit();
			}
			function onPut(str){
				if(str == "delete"){
					if(!confirm("是否確定刪除?")){return;}
					if(isDelete){
						
					}else{
						alert("啟用日期小於或等於營業日期，不可刪除");
						return;
					}
				}else if(str != "update"){
					$("#formID").validationEngine('detach');
				}else{
					if(jQuery('#formID').validationEngine('validate')){
						//將選中的手續費填入到隱藏的下拉選單selectedFeeAry中
						$("#selectedFeeAry option").remove();
						var feeId_startDate = "";
						$("#resultData tbody input:checkbox:checked").each(function(){
							feeId_startDate = $(this).prop("id");
							feeId_startDate = feeId_startDate.split("_")[2] + "-" + feeId_startDate.split("_")[3];
							$("#selectedFeeAry").append("<option value='" + feeId_startDate + "' selected></option>");
						});
						//$("[name=selectedFeeAry] option").prop("selected", true);
						
						//至少選擇一項手續費用
						if($("#selectedFeeAry option").size() == 0){
							//20150121 HUANGPU 不使用GRID選取手續費MAPPING
							//alert("至少選擇一項手續費用!");unblockUI();return;
						}
						
						/*20160408*/
// 						if(!checkBizDateII($("#ACTIVE_DATE").val())){
// 							return;
// 						}
						/*20160408*/
						
// 						if(isDelete){
							
// 						}else{
// 							alert("啟用日期小於或等於營業日期，不可儲存");
// 							return;
// 						}
					}else{return;}
				}
				//如果ACTIVE_DATE的屬性readonly 為 true , 不擋update
				//如果ACTIVE_DATE的屬性readonly 為 false , 判斷時間擋update
				if(!$("#ACTIVE_DATE").prop('readonly')){
					if(checkBizDateUpdate($("#ACTIVE_DATE").val())){
						alert("啟用日期小於或等於營業日期，不可儲存");
						return;
					}else{}
				}
				$("#ac_key").val(str);
				$("#target").val('search');
				$("#TXN_ID").attr('disabled', false);
				$("input[name=TXN_CHECK_TYPE]").attr('disabled', false);
				$("select").prop('disabled', false);  //20160411 disabled 的狀態在後台取不到值
				$("form").submit();
			}	
			function init(){
				$("#TXN_ID").attr('disabled', true);
				alterMsg();
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#ACTIVE_DATE").val('<bean:write name="txn_code_form" property="ACTIVE_DATE"/>');
				$("#MAX_TXN_AMT").val(Math.round($("#MAX_TXN_AMT").val()));
				getAgent_txn_id($("#BUSINESS_TYPE_ID").find(":selected").val());
				//setOptionTooltips();
				
				//不使用GRID設定手續費
				//setFeeTable();
				
				var txnCheckType = '<bean:write name="txn_code_form" property="TXN_CHECK_TYPE"/>';
				txnCheckType = txnCheckType.replace(/ /g,'');
				if(fstop.isEmptyString(txnCheckType)){
					setTXN_CHECK_TYPE('<bean:write name="txn_code_form" property="TXN_TYPE"/>');
					$("#TXN_CHECK_TYPE_1").attr("checked", true);
				}
//				20150624 add by hugo req by UAT-2015011-02
// 				判斷啟用日期是否小於等於營業日  
				checkBizDate($('#formID input')  , '<bean:write name="txn_code_form" property="ACTIVE_DATE"/>');
			}
			function checkBizDateUpdate(date){
				if(fstop.isEmptyString(date)){
					return;
				}
				var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate" ,activeDate:date};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
				if(fstop.isNotEmpty(vResult)){
					if(vResult.result == "TRUE"){
						return true;
					}else{
						return false;
					}
				}else{
					alert("系統異常");
				}
			}
			
// 			判斷啟用日期是否小於等於營業日  是:不可修改 ，否:可修改  
			function checkBizDate( obj ,date){
				if(fstop.isEmptyString(date)){
					return;
				}
				var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate" ,activeDate:date};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
				if(fstop.isNotEmpty(vResult)){
// 					啟用日期 小於營業日 要disabled 該欄位

					if(vResult.result == "TRUE"){
						$(obj).datepicker("destroy");
// 						$(obj).attr('readonly', true);
// 						$(obj).addClass('lock');
                        $("#ACTIVE_DATE").addClass('lock').prop('readonly',true);
// 						$("input[type=radio]").attr('disabled', true);
						$("select").prop('disabled', true);
						$("#AGENT_TXN_ID").prop('disabled', false);
						isDelete = false;
					}else{
						$(obj).attr('readonly', false);
						$(obj).removeClass('lock');
						$("input[type=radio]").attr('disabled', false);
						$("select").prop('disabled', false);
						isDelete = true;
					}

// 					if(vResult.result == "TRUE"){
// 						$(obj).datepicker("destroy");
// 						$(obj).attr('readonly', true);
// 						$(obj).addClass('lock');
// 						isDelete = false;
// 					}else{
// 						$(obj).attr('readonly', false);
// 						$(obj).removeClass('lock');
// 						isDelete = true;
// 					}
					if(window.console){console.log("msg>>"+vResult.msg);}
				}else{
					alert("系統異常");
				}
			}
			function checkBizDateII(date){
				var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate" ,activeDate:date};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
				if(fstop.isNotEmpty(vResult)){
					if(vResult.result == "TRUE"){
						alert("啟用日期不可小於或等於營業日:"+vResult.msg);
						return false;
					}
					if(window.console){console.log("msg>>"+vResult.msg);}
				}else{
					if(window.console){console.log("系統異常");}
					return false;
// 					alert("系統異常");
				}
				return true;
			}
			
			function alterMsg(){
				var msg = '<bean:write name="txn_code_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			function setOptionTooltips(){
				$("#feeAry option").each(function(){
					var data = $(this).html();
					data = JSON.parse(data);
					var pk = JSON.parse(data.id);
					$(this).html(pk["FEE_ID"]);
					$(this).attr("title", getOptionTitle(pk, data));
				});
				
				$("#selectedFeeAry option").each(function(){
					var data = $(this).html();
					data = JSON.parse(data);
					var pk = JSON.parse(data.id);
					$(this).html(pk["FEE_ID"]);
					$(this).attr("title", getOptionTitle(pk, data));
				});
			}
			
			function getOptionTitle(pk, data){
				/*
				data: 
				FEE_ID	手續費代號
				START_DATE	啟用日期
				OUT_BANK_FEE	扣款行手續費
				OUT_BANK_FEE_DISC	折扣後扣款行手續費
				IN_BANK_FEE	入帳行手續費
				IN_BANK_FEE_DISC	折扣後入帳行手續費
				TCH_FEE	交換所手續費
				TCH_FEE_DISC	折扣後交換所手續費
				SND_BANK_FEE	發動行手續費
				SND_BANK_FEE_DISC	折扣後發動行手續費
				DESC	說明
				*/
				var titleStr = "";
				titleStr += "扣款行手續費: " + data["OUT_BANK_FEE"] + "	折扣後扣款行手續費: " + data["OUT_BANK_FEE_DISC"] + "\n";
				titleStr += "入帳行手續費: " + data["IN_BANK_FEE"] + "	折扣後入帳行手續費: " + data["IN_BANK_FEE_DISC"] + "\n";
				titleStr += "交換所手續費: " + data["TCH_FEE"] + "	折扣後交換所手續費: " + data["TCH_FEE_DISC"] + "\n";
				titleStr += "發動行手續費: " + data["SND_BANK_FEE"] + "	折扣後發動行手續費: " + data["SND_BANK_FEE_DISC"] + "\n";
				titleStr += "啟用日期: " + pk["START_DATE"] + "	說明: " + data["FEE_DESC"];
				return titleStr;
			}
			
			function setFeeTable(){
				tableToGrid("#resultData",{
					//避免jqGrid自發性的submit
					datatype: 'local',
					autowidth:true,
	            	height: 120,
	            	shrinkToFit: true,
	            	sortable: true,
					sortname: 'FEE_ID',
					sorttype: 'text',
					gridview: true,
					loadonce: true,
	            	rowNum: 10000,
	            	hiddengrid: true,
	            	colNames:['手續費代號','啟用日期', '扣款行手續費', '折扣後扣款行手續費','入帳行手續費','折扣後入帳行手續費','交換所手續費','折扣後交換所手續費','發動行手續費','折扣後發動行手續費','說明'],
	            	colModel: [
						{name:'FEE_ID',index:'FEE_ID',align:'center',fixed:true,width: 100},
						{name:'START_DATE',index:'START_DATE',fixed:true,width: 90},
						{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,width: 95},
						{name:'OUT_BANK_FEE_DISC',index:'OUT_BANK_FEE_DISC',fixed:true,width: 150},
						{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,width: 95},
						{name:'IN_BANK_FEE_DISC',index:'IN_BANK_FEE_DISC',fixed:true,width: 150},
						{name:'TCH_FEE',index:'TCH_FEE',fixed:true,width: 95},
						{name:'TCH_FEE_DISC',index:'TCH_FEE_DISC',fixed:true,width: 150},
						{name:'SND_BANK_FEE',index:'SND_BANK_FEE',fixed:true,width: 95},
						{name:'SND_BANK_FEE_DISC',index:'SND_BANK_FEE_DISC',fixed:true,width: 150},
						{name:'FEE_DESC',index:'DESC',fixed:true,width: 150}
					],
					beforeSelectRow: function(rowid, e) {
					    return false;
					},
					loadtext: "處理中..."
				});
				//$("#resultData tr:odd").addClass('resultDataRowOdd');
				$("#resultData tr:odd").css("background", "rgb(230, 230, 238)");
				$("#resultData tr:even").css('background', "rgb(250, 250, 258)");
				$("#resultData tr").hover(function(){$(this).css("color","black");});
				
				unblockUI();
				
				var value = "";
				<logic:present name="txn_code_form" property="selectedFeeAry">
				<logic:iterate id="qry" name="txn_code_form" property="selectedFeeAry">
					value = '<bean:write name="qry"/>';
					$("#jqg_resultData_" + value.split("-")[0] + "_" + value.split("-")[1]).prop("checked", true);
					$("#selectedFeeAry").append("<option value='<bean:write name="qry"/>' selected></option>");
				</logic:iterate>
				</logic:present>
			}
			
			function addFee(){
				var options = $('option:selected', $("[name=feeAry]")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("[name=selectedFeeAry]").append($(options[i]));
				}
				
				sortListBox();
			}
			
			function removeFee(){
				var options = $('option:selected', $("[name=selectedFeeAry]")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("[name=feeAry]").append($(options[i]));
				}
				
				sortListBox();
			}
			
			function removeAllSelectedFee(){
				$("[name=selectedFeeAry] option").prop("selected", true);
				removeFee();
				$("#formID").validationEngine('hide');			
			}
			
			function sortListBox(){
				//sort items
				var ary = $("[name=feeAry] option").toArray();
				ary.sort(sortRule);
				$("[name=feeAry] option").remove();
				for(var i = 0; i < ary.length; i++){
					$("[name=feeAry]").append($(ary[i]));
				}
				
				ary = $("[name=selectedFeeAry] option").toArray();
				ary.sort(sortRule);
				$("[name=selectedFeeAry] option").remove();
				for(var i = 0; i < ary.length; i++){
					$("[name=selectedFeeAry]").append($(ary[i]));
				}
			}
			
			function sortRule(a, b) {
				if (a.value > b.value) {
					return 1;
				}
				if (a.value < b.value) {
					return -1;
				}
				// a must be equal to b
				return 0;
			}
			
			function setTXN_CHECK_TYPE(txnType){
				if(txnType == "SD" || txnType == ""){
// 					20150116 note by hugo 原本的寫法在user 點選不檢核 在換下拉選單為SD時 radio的checked 無法變為檢核
// 					$("#TXN_CHECK_TYPE_1").attr('checked', true);
// 					$('input[name="TXN_CHECK_TYPE"]')[0].checked = true; //此方法也可以
					$("#TXN_CHECK_TYPE_1").prop('checked', true);
					$("input[name=TXN_CHECK_TYPE]").attr("disabled", true);
				}else{
					//入帳檢核統一編號預設為[1=檢核]
// 					$("#TXN_CHECK_TYPE_1").attr('checked', true);
					$("#TXN_CHECK_TYPE_1").prop('checked', true);
					$("input[name=TXN_CHECK_TYPE]").attr("disabled", false);
				}
			}
			
			function getAgent_txn_id(value){
			<logic:notEqual name="login_form" property="userData.USER_TYPE" value="B">
				var business_type_id = value;
				var rdata = {component:"txn_code_bo", method:"getAgentTxnIdListByBSid" , BUSINESS_TYPE_ID:business_type_id  };
				fstop.getServerDataExII(uri, rdata, false,showAgent_txn_id);
			</logic:notEqual>
				
			}
			function showAgent_txn_id(obj){
				var select  = $("#AGENT_TXN_ID");
				$("#AGENT_TXN_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
					$("#AGENT_TXN_ID").val('<bean:write name="txn_code_form" property="AGENT_TXN_ID"/>');
				}else if(obj.result=="FALSE"){
// 					alert(obj.msg);
					if(window.console){console.log("obj.msg>>"+obj.msg);}
				}
			}
		</script>
	</body>
</html>
