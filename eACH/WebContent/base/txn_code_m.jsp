<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/></title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
	</head>
	<body onload="unblockUI()">
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/txn_code">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 100px">交易代號</td>
								<td style="width: 300px">
									<html:select styleId="TXN_ID" property="TXN_ID">
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "txn_code_form" property="idList" label="label" value="value"/>
									</html:select>
								</td>
								<td class="header" style="width: 260px">啟用日期(民國年 ex.01030101)</td>
								<td>
									<html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
<%-- 									<logic:equal name="login_form" property="userData.USER_TYPE" value="A"> --%>
										<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="bs_table"></table>
				</fieldset>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>手續費歷程</legend>
					<table id="fee_table"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var gridOption, gridOption2;
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#ACTIVE_DATE").val('<bean:write name="txn_code_form" property="ACTIVE_DATE"/>');
				initGridOption();
				initGridII();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="txn_code_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth: true,
		            	height: 120,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum:10000,
		            	loadonce: true,
// 		            	sortable: true,
						colNames:['檢視明細','查詢手續費歷程','交易代號','交易項目', '交易類別', '入帳檢核統一編號', '交易說明','啟用日期','金額上限'
						<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
						          ,'適用業務類別','代理業者交易類別'],
						</logic:equal>          
						<logic:equal  name="login_form" property="userData.USER_TYPE" value="B">
						          ,'適用業務類別'],
						</logic:equal>          
						<logic:equal  name="login_form" property="userData.USER_TYPE" value="C">
						          ,'代理業者交易類別'],
						</logic:equal>          
		            	colModel: [
							{name:'btn',index:'btn', sortable: false, fixed: true, align: 'center', width:60},
							{name:'btn_2',index:'btn_2', sortable: false, fixed: true, align: 'center', width:100},
							{name:'TXN_ID',index:'TXN_ID', fixed: true, width:60},
							{name:'TXN_NAME',index:'TXN_NAME' ,fixed: true, width:100},
							{name:'TXN_TYPE',index:'TXN_TYPE', fixed: true, width:60},
							{name:'TXN_CHECK_TYPE',index:'TXN_CHECK_TYPE', fixed: true, width:120, formatter: tctFmatter},
							{name:'TXN_DESC',index:'TXN_DESC', fixed: true, width:100},
							{name:'ACTIVE_DATE',index:'ACTIVE_DATE', fixed: true, width:90},
							{name:'MAX_TXN_AMT',index:'MAX_TXN_AMT', fixed: true, width:150},
							<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
							{name:'BUSINESS_TYPE_ID',index:'BUSINESS_TYPE_ID', fixed: true, width:150},
							{name:'AGENT_TXN_ID',index:'AGENT_TXN_ID', fixed: true, width:150}
							</logic:equal> 
							<logic:equal  name="login_form" property="userData.USER_TYPE" value="B">
							{name:'BUSINESS_TYPE_ID',index:'BUSINESS_TYPE_ID', fixed: true, width:150},
							</logic:equal> 
							<logic:equal  name="login_form" property="userData.USER_TYPE" value="C">
							{name:'AGENT_TXN_ID',index:'AGENT_TXN_ID', fixed: true, width:150}
							</logic:equal> 
						],
		            	gridComplete:function (){
			            	$("#bs_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#bs_table .jqgrow:even").addClass('resultDataRowEven');
						},
						beforeProcessing: function(data, status, xhr){
							for(var rowCount in data){
								rowCount = parseInt(rowCount);
								data[rowCount].btn ='<button type="button" id="edit_'+rowCount+'" value="edit" onclick="edit_p(this.value,\''+data[rowCount].TXN_ID+'\')" ><img src="./images/edit.png"/></button>';
								data[rowCount].btn_2 = '<button type="button" id="txnRow_'+rowCount+'" value="feeSearch" onclick="reloadFeeTable(\'' + (rowCount+1) + '\')" ><img src="./images/search.png"/></button>';
							}
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#bs_table") );
						},
	 					onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadtext: "處理中..."
				};
				
				gridOption2 = {
						datatype: "local",
						autowidth: true,
		            	height: 80,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum:10000,
		            	loadonce: true,
		            	
		            	<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
							colNames:['交易代號','啟用日期', '扣款行手續費', '折扣後扣款行手續費','入帳行手續費','折扣後入帳行手續費','交換所手續費','折扣後交換所手續費','發動行手續費','折扣後發動行手續費','銷帳行手續費','折扣後銷帳行手續費','客戶支付手續費','說明','啟用註解'],
							colModel: [
									{name:'FEE_ID',index:'FEE_ID',fixed:true,width: 100,align:'center'},
									{name:'START_DATE',index:'START_DATE',fixed:true},
									{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,align: 'right',width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'OUT_BANK_FEE_DISC',index:'OUT_BANK_FEE_DISC',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,align: 'right',width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'IN_BANK_FEE_DISC',index:'IN_BANK_FEE_DISC',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'TCH_FEE',index:'TCH_FEE',align: 'right',fixed:true,width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'TCH_FEE_DISC',index:'TCH_FEE_DISC',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'SND_BANK_FEE',index:'SND_BANK_FEE',align: 'right',fixed:true,width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'SND_BANK_FEE_DISC',index:'SND_BANK_FEE_DISC',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'WO_BANK_FEE',index:'WO_BANK_FEE',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'WO_BANK_FEE_DISC',index:'WO_BANK_FEE_DISC',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'HANDLECHARGE',index:'HANDLECHARGE',align: 'right',fixed:true,width: 150,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'FEE_DESC',index:'DESC',fixed:true,width: 150},
									{name:'ACTIVE_DESC',index:'ACTIVE_DESC',fixed:true,width: 150}
							],
						</logic:equal>
						<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
							colNames:['交易代號','啟用日期', '扣款行手續費','入帳行手續費','交換所手續費','發動行手續費','客戶支付手續費','說明','啟用註解'],
							colModel: [
									{name:'FEE_ID',index:'FEE_ID',fixed:true,width: 100,align:'center'},
									{name:'START_DATE',index:'START_DATE',fixed:true},
									{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,align: 'right',width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,align: 'right',width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'TCH_FEE',index:'TCH_FEE',align: 'right',fixed:true,width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'SND_BANK_FEE',index:'SND_BANK_FEE',align: 'right',fixed:true,width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'HANDLECHARGE',index:'HANDLECHARGE',align: 'right',fixed:true,width: 95,formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
									{name:'FEE_DESC',index:'DESC',fixed:true,width: 150},
									{name:'ACTIVE_DESC',index:'ACTIVE_DESC',fixed:true,width: 150}
							],
						</logic:equal>
		            	
		            	gridComplete:function (){
			            	$("#fee_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#fee_table .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#fee_table") );
						},
	 					loadtext: "處理中..."
				};
			}
			
			function initGridII(){
				var acKey = $("#ac_key").val();
				if( fstop.isNotEmptyString(acKey) ){
// 					initSearches();
					onPut('back');
				}else{
					onPut('search');
					
// 					$("#bs_table").jqGrid(gridOption);
				}
				
				$("#fee_table").jqGrid(gridOption2);
				
			}
			
			function reloadFeeTable(rowId){
				blockUI();
				if(window.console){console.log("rowId>>"+rowId);}
				var txnId = $("#bs_table").jqGrid('getCell', rowId, 'TXN_ID');
				
				if(window.console){console.log("txnId>>"+txnId);}
// 				var qStr = "component=txn_code_bo&method=getFeeCodeByTxnId_toJson&txnId="+txnId;
				var qStr = "component=txn_code_bo&method=getFeeCodeByTxnId_toJson&txnId="+txnId+"&action="+$("#formID").attr('action');
				$("#fee_table").jqGrid('GridUnload');
				var newOption = gridOption2;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
				$("#fee_table").jqGrid(newOption);
				unblockUI();
			}
			
			function onPut(str){
				$("#formID").validationEngine({promptPosition: "topLeft"});
				if(!$("#formID").validationEngine('validate')){
					$("#formID").validationEngine('detach');return;
				}
				blockUI();
// 				getSearches();
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				$("#ac_key").val(str);
				$("#target").val('search');
				
				$("#bs_table").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=txn_code_bo&method=search_toJson&"+$("#formID").serialize();
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.pgtext= "{0} / {1}";
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'TXN_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'TXN_ID' , 'ASC' , true);
				}
				//if(window.console)console.log("url>>"+newOption.url);
				$("#bs_table").jqGrid(newOption);
				
				//清除手續費歷程
				$("#fee_table").jqGrid('GridUnload');
				gridOption2.url = "/eACH/baseInfo?component=txn_code_bo&method=getFeeCodeByTxnId_toJson&txnId=";
				$("#fee_table").jqGrid(gridOption2);
				unblockUI();
				//$("form").submit();
			}	
			
			function add_p(str){
				blockUI();
				$("#formID").validationEngine('detach');
				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id ){
				$("#formID").validationEngine('detach');
				$("#ac_key").val(str);
				$("#target").val('edit_p');
// 				$("#TXN_ID").val(id);
				var tmp ={};
				tmp['TXN_ID'] = id;
				$("#edit_params").val(JSON.stringify(tmp));
				$("form").submit();
			}	
			
			function tctFmatter(cellvalue, options, rowObject){
				if(cellvalue == "1"){
					return "檢核";
				}else if(cellvalue == "2"){
					return "不檢核";
				}else{
					return "未定義";
				}
			}
			
			//取得user查詢條件
			function getSearches(){
				var searches = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					searches[field.name] = field.value;
				});
				$("#serchStrs").val(JSON.stringify(searches));
				//if(window.console){console.log($("#serchStrs").val());}
			}
			
			//初始化查詢條件
			function initSearches(){
				var searches = {};
				//if(window.console){console.log($("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					searches = $.parseJSON($("#serchStrs").val()) ;
				}
				for(var key in searches){
					//if(window.console){console.log(key+"="+searches[key]);}
					//模式二 塞入user當初查詢條件
					if(key != "ac_key"){
						$("#"+key).val(searches[key]);
					}
				}
			}
			
			//Clean form except <serchStrs>
			function cleanFormII(obj){
				var serchStrs = "";
				if($("[name=serchStrs]")){
					serchStrs = $("[name=serchStrs]").val();
					cleanForm(obj);
					$("[name=serchStrs]").val(serchStrs);
				}else{
					cleanForm(obj);
				}
			}
			
			function cleanForm(obj){
				var formObj = $(obj).parents("form")[0];
				formObj.reset();
				$("input", formObj).val("");
				$("select", formObj).val("");
				if($(formObj).validationEngine != null){
					$(formObj).validationEngine('hide');
				}
			}
		</script>
	</body>
</html>
