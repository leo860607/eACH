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
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/json2.js"></script>
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
				<html:form styleId="formID" action="/txn_code" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="FEE_ID" styleId="FEE_ID"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					
					<table>
						<tr>
							<td class="header necessary" style="width: 25%"><span>交易代號</span></td>
							<td style="width: 23%"><html:text styleId="TXN_ID" property="TXN_ID" size="3" maxlength="3" styleClass="validate[required,custom[onlyLetterNumber],minSize[3]] text-input"></html:text></td>
							<td class="header necessary" style="width: 25%"><span>交易項目</span></td>
							<td><html:text styleId="TXN_NAME" property="TXN_NAME" size="10" maxlength="10" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交易類別</span></td>
							<td>
								<html:select property="TXN_TYPE" onchange="setTXN_CHECK_TYPE(this.value)" styleClass="validate[required]">
									<html:option value="">===請選擇交易類別===</html:option>
									<html:option value="SC">SC - 入帳</html:option>
									<html:option value="SD">SD - 扣款</html:option>
								</html:select>
							</td>
							<td class="header necessary"><span>適用業務類別</span></td>
							<td>
								<html:select property="BUSINESS_TYPE_ID" onchange="getAgent_txn_id(this.value)" styleClass="validate[required]">
									<html:option value="">====請選擇業務類別====</html:option>
									<html:optionsCollection name="txn_code_form" property="bsTypeIdList" label="label" value="value"></html:optionsCollection>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>金額上限</span></td>
							<td><html:text styleId="MAX_TXN_AMT" property="MAX_TXN_AMT" size="17" maxlength="17" styleClass="validate[required,decimal[16,2]]"></html:text></td>
							<td class="header" ><span>代理業者交易類別</span></td>
							<td >
<%-- 							<html:text styleId="AGENT_TXN_ID" property="AGENT_TXN_ID" size="4" maxlength="4" styleClass="validate[required,custom[onlyLetterNumber],minSize[4]] text-input"></html:text> --%>
								<html:select styleId="AGENT_TXN_ID" property="AGENT_TXN_ID" >
									<html:option value="">====請選擇代理業者交易類別====</html:option>
<%-- 									<html:optionsCollection name="txn_code_form" property="agentTxnIdList" label="label" value="value"></html:optionsCollection> --%>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>交易代號啟用日期<br/>(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text></td>
							<td class="header necessary"><span>入帳檢核統一編號</span></td>
							<td>
								<html:radio styleId="TXN_CHECK_TYPE_1" property="TXN_CHECK_TYPE" value="1">檢核</html:radio>
								<html:radio styleId="TXN_CHECK_TYPE_2" property="TXN_CHECK_TYPE" value="2">不檢核</html:radio>
							</td>
						</tr>
						
<!-- 						<tr> -->
							
<!-- 							<td></td><td></td> -->
<!-- 						</tr> -->
						
						<tr>
							<td class="header"><span>交易說明</span></td>
							<td colspan="3"><html:text property="TXN_DESC" size="70" maxlength="66"></html:text></td>
						</tr>
					</table>
					<table>
						<tr>
							<td class="header necessary" style="width: 25%">手續費啟用日期<br/>(民國年 ex.01030101)</td>
							<td style="width: 23%">
								<html:text name="txn_code_form" styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,minSize[8],maxSize[8],twDate] text-input datepicker"/>
							</td>
							<td class="header" style="width: 25%">手續費說明</td>
							<td><html:text property="FEE_DESC" size="33" maxlength="33"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>扣款行手續費</span></td>
							<td><html:text styleId="OUT_BANK_FEE" property="OUT_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後扣款行手續費</span></td>
							<td><html:text styleId="OUT_BANK_FEE_DISC" property="OUT_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>入帳行手續費</span></td>
							<td><html:text styleId="IN_BANK_FEE" property="IN_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後入帳行手續費</span></td>
							<td><html:text styleId="IN_BANK_FEE_DISC" property="IN_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交換所手續費</span></td>
							<td><html:text styleId="TCH_FEE" property="TCH_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後交換所手續費</span></td>
							<td><html:text styleId="TCH_FEE_DISC" property="TCH_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>發動行手續費</span></td>
							<td><html:text styleId="SND_BANK_FEE" property="SND_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後發動行手續費</span></td>
							<td><html:text styleId="SND_BANK_FEE_DISC" property="SND_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>銷帳行手續費</span></td>
							<td><html:text styleId="WO_BANK_FEE" property="WO_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後銷帳行手續費</span></td>
							<td><html:text styleId="WO_BANK_FEE_DISC" property="WO_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>客戶支付手續費</span></td>
							<td><html:text styleId="HANDLECHARGE" property="HANDLECHARGE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td class="header"><span>啟用註解</span></td>
							<td colspan="3">
								<html:text property="ACTIVE_DESC" size="66" maxlength="66"></html:text>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
			<div style="width: 100%; text-align: center">
				<table style="width: 100%">
					<tr>
						<td colspan="2" style="padding: 10px; vertical-align: middle; text-align: center">
							<label class="btn" id="save" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
							<label class="btn" id="clean" onclick="cleanFormNE(document.getElementById('ac_key'));$('#TXN_CHECK_TYPE_1').prop('checked', true);"><img src="./images/clean.png"/>&nbsp;清除</label>
							<label class="btn" id="back"  onclick="back()"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
						</td>
					</tr>
				</table>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				//20150120 HUANGPU 不使用GRID設定手續費MAPPING
				//setFeeTable();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				//入帳檢核統一編號預設為[1=檢核]
				if('<bean:write name="txn_code_form" property="ACTIVE_DATE"/>' == ""){
					$("#TXN_CHECK_TYPE_1").prop('checked', true);
				}
				
				//交易代號等同於手續費代號
				$("#TXN_ID").change(function(){
					$("#FEE_ID").val($(this).val());
				});
				
				$("#ACTIVE_DATE").val('<bean:write name="txn_code_form" property="ACTIVE_DATE"/>');
				$("#START_DATE").val('<bean:write name="txn_code_form" property="START_DATE"/>');
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="txn_code_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function setFeeTable(){
				tableToGrid("#resultData",{
					//避免jqGrid自發性的submit
					datatype: 'local',
					autowidth:true,
	            	height: 190,
	            	shrinkToFit: true,
	            	sortable: true,
					sortname: 'FEE_ID',
					sorttype: 'text',
					gridview: true,
					loadonce: true,
	            	rowNum: 10000,
	            	hiddengrid: true,
	            	colNames:['手續費代號','啟用日期', '扣款行手續費', '折扣後扣款行手續費','入帳行手續費','折扣後入帳行手續費','交換所手續費','折扣後交換所手續費','發動行手續費','折扣後發動行手續費','說明','銷帳行手續費','折扣後銷帳行手續費'],
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
						{name:'FEE_DESC',index:'DESC',fixed:true,width: 150},
						{name:'WO_BANK_FEE',index:'WO_BANK_FEE',fixed:true,width: 95},
						{name:'SND_BANK_FEE_DISC',index:'SND_BANK_FEE_DISC',fixed:true,width: 150}
					],
					beforeSelectRow: function(rowid, e) {
					    return false;
					},
					gridComplete: function(){
						$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
						$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						unblockUI();
					},
					loadtext: "處理中..."
				});
			}
			
			function onPut(str){
				if(jQuery('#formID').validationEngine('validate') && checkFee()){
					blockUI();
					
					if($("#MAX_TXN_AMT").val() == ""){
						$("#MAX_TXN_AMT").val("0");
					}
					
					$("#ac_key").val(str);
					$("#target").val('search');
					$("input[name=TXN_CHECK_TYPE]").attr('disabled', false);
					$("form").submit();
				}else{
					return;
				}
			}	
			
			function back(){
				$("#formID").validationEngine('detach');
				$("#ac_key").val('back');
				$("#target").val('search');
				$("form").submit();
			}
			
			function setTXN_CHECK_TYPE(txnType){
				if(window.console){console.log("txnType>>"+txnType);}
				if(txnType == "SD" || txnType == ""){
// 					20150116 edit by hugo 扣款預設為檢核且不可更改 
					$("#TXN_CHECK_TYPE_1").prop('checked', true);
					$("input[name=TXN_CHECK_TYPE]").attr("disabled", true);
				}else{
					//入帳檢核統一編號預設為[1=檢核]
					$("#TXN_CHECK_TYPE_1").prop('checked', true);
					$("input[name=TXN_CHECK_TYPE]").attr("disabled", false);
				}
// 				if(txnType == "SD" || txnType == ""){
// 					$("input[name=TXN_CHECK_TYPE]").attr("disabled", false);
// 				}else{
// 					//入帳檢核統一編號預設為[1=檢核]
// 					$("#TXN_CHECK_TYPE_1").prop('checked', true);
// 					$("input[name=TXN_CHECK_TYPE]").attr("disabled", true);
// 				}
			}
			
			function checkFee(){
				//扣款行手續費+入帳行手續費+交換所手續費+發動行手續費=0
				var total1 = (parseFloat($('#OUT_BANK_FEE').val()) +
				parseFloat($('#IN_BANK_FEE').val()) +
				parseFloat($('#TCH_FEE').val()) +
				parseFloat($('#SND_BANK_FEE').val())+
				parseFloat($('#WO_BANK_FEE').val())).toFixed(3);
				if(total1 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n扣款行手續費+入帳行手續費+\n交換所手續費+發動行手續費+\n銷帳行手續費=0");
					return false;
				}
				//折扣後扣款行手續費+折扣後入帳行手續費+折扣後交換所手續費+折扣後發動行手續費=0
				var total2 = (parseFloat($('#OUT_BANK_FEE_DISC').val()) +
				parseFloat($('#IN_BANK_FEE_DISC').val()) +
				parseFloat($('#TCH_FEE_DISC').val()) +
				parseFloat($('#SND_BANK_FEE_DISC').val())+
				parseFloat($('#WO_BANK_FEE_DISC').val())).toFixed(3);
				if(total2 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n折扣後扣款行手續費+折扣後入帳行手續費+\n折扣後交換所手續費+折扣後發動行手續費+\n折扣後銷帳行手續費=0");
					return false;
				}
				return true;
			}
			
			
			function getAgent_txn_id(value){
				var business_type_id = value;
				var rdata = {component:"txn_code_bo", method:"getAgentTxnIdListByBSid" , BUSINESS_TYPE_ID:business_type_id  };
				fstop.getServerDataExII(uri, rdata, false,showAgent_txn_id);
				
			}
			function showAgent_txn_id(obj){
				var select  = $("#AGENT_TXN_ID");
				$("#AGENT_TXN_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
				}else if(obj.result=="FALSE"){
// 					alert(obj.msg);
					if(window.console){console.log("obj.msg>>"+obj.msg);}
				}
			}
		</script>
	</body>
</html>
