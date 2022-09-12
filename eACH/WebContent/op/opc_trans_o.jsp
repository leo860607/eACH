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
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/jquery.timepicker.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.timepicker.min.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/json2.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			#countdownBlock{position:relative;float:right;top:-20px;background-color: #ECF1F6;padding:0px 5px}
		</style>
	</head>
	<body>
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
				<html:form styleId="formID" action="/opc_trans">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="STAN" styleId="STAN" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="OPCCUR_TXDATE" styleId="OPCCUR_TXDATE"/>
					<html:hidden property="OPCTXDATE" styleId="OPCTXDATE"/>
					<html:hidden property="OPCTXDATE_PRE" styleId="OPCTXDATE_PRE"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id = "search_tab">
							<tr>
								<td class="header" style="width: 10%">交易日期</td>
								<td style="width: 20%">
									<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width: 15%">交易起訖時間</td>
								<td>
									<html:text styleId="TXTIME_START" property="TXTIME_START" size="10"></html:text>~
									<html:text styleId="TXTIME_END" property="TXTIME_END" size="10"></html:text>
								</td>
							</tr>
							<tr>
								<td class="header">發動端</td>
								<td>
									<html:select styleId="SENDER_TYPE" property="SENDER_TYPE">
										<html:option value="">全部</html:option>
										<html:option value="TCH">交換所</html:option>
										<html:option value="BANK">銀行</html:option>
									</html:select>
									<html:select styleId="SENDERBANK" property="SENDERBANK"></html:select>
								</td>
								<td class="header">接收端</td>
								<td>
									<html:select styleId="RECEIVER_TYPE" property="RECEIVER_TYPE">
										<html:option value="">全部</html:option>
										<html:option value="TCH">交換所</html:option>
										<html:option value="BANK">銀行</html:option>
									</html:select>
									<html:select styleId="RECEIVERBANK" property="RECEIVERBANK"></html:select>
								</td>
							</tr>
							<tr>
								<td class="header">交易代號</td>
								<td>
									<html:select styleId="PCODE" property="PCODE">
										<html:option value="all">全部</html:option>
										<logic:present name="opc_trans_form" property="pcodeList">
											<html:optionsCollection name="opc_trans_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
								<td class="header">回覆結果</td>
								<td>
									<html:select styleId="RSPCODE" property="RSPCODE">
										<html:option value="">全部</html:option>
										<html:option value="fail">非成功</html:option>
										<logic:present name="opc_trans_form" property="txnErrorCodeList">
											<html:optionsCollection name="opc_trans_form" property="txnErrorCodeList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData"></table>
				</fieldset>
			</div>
			<br/>
			<div id="opPanel" style="padding-top:5px">
					<fieldset>
						系統日期：<label id="LABEL_OPCTXDATE"></label>
					</fieldset>
				</div>
				
				<div id="rsPanel" style="margin-top: 5px">
					<fieldset>
						<legend>
							OPC交易監控&nbsp;
							<%--<label class="btn" id="btn_clearingPhaseAll" onclick="reSearchData('')">全部</label> --%>
							<label class="btn" id="BTN_OPCTXDATE"></label>&nbsp;
							<label class="btn" id="BTN_OPCTXDATE_PRE"></label>
						</legend>
						<div id="countdownBlock">
							剩餘 <font color="red" id="COUNTDOWN"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reSearchData($('#OPCCUR_TXDATE').val())"><img src="./images/refresh.png"/>&nbsp;重載</label>
						</div><br>
						<table id="resultData2"></table>
					</fieldset>
				</div>
			<br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption,gridOption2;;
			var countdownObj, countdownSec = 30 ;
			var pageForSort = 1;
			var defaultPage = '<bean:write name="opc_trans_form" property="pageForSort"/>';
//				20151112 add by hugo req by UAT-2015904-01
			var receiverbank = '<bean:write name="opc_trans_form" property="RECEIVERBANK"/>';
			var senderbank = '<bean:write name="opc_trans_form" property="SENDERBANK"/>';
			var tmpbtnid ;
			var tmpTxDate ;
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				initGridOption();
				initGrid();
// 				setDatePicker();
				var txdate = '<bean:write name="opc_trans_form" property="TXDATE"/>';
				setDatePickerII($("#TXDATE") ,txdate);
				$("#TXTIME_START").timepicker({ 'timeFormat': 'H:i:s' });
				$("#TXTIME_END").timepicker({ 'timeFormat': 'H:i:s' });
				$("#formID").validationEngine({binded:false,promptPosition: "bottomRight"});
				if(window.console){console.log("txdate>>"+txdate);}
				if(!fstop.isNotEmptyString(txdate)){
					$("#TXDATE").datepicker("setDate", new Date());
					$("#TXDATE").val('0' + $("#TXDATE").val());
				}
				
				$("#SENDERBANK").hide();
				$("#RECEIVERBANK").hide();
				$("#SENDER_TYPE").change(function(){
					changeType("SENDER_TYPE", "SENDERBANK");
				});
				$("#RECEIVER_TYPE").change(function(){
					changeType("RECEIVER_TYPE", "RECEIVERBANK");
				});
				
// 				initSearchs();
				
				if($("#ac_key").val() == 'back'){
//						20151112 add by hugo req by UAT-2015904-01
					$("#RECEIVER_TYPE").trigger("change");
					$("#SENDER_TYPE").trigger("change");
					
					reOnPut();
				}
				
				
				//OPC監控
				initGridOption2();
				initGrid2();
				//初始化查詢條件的營業日、清算階段、系統日期
				$("#OPCTXDATE").val($("#OPCCUR_TXDATE").val());
				createLABEL();
				createBTN();
				searchOpcData();
				$("#COUNTDOWN").html(countdownSec);
				startInterval(1, refreshCountdown, []);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="opc_trans_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						datatype: "local",
						pagerpos: "left",
						autowidth:true,
// 						autoheight:true,
		            	height: 250,
		            	sorttype:"text",
		            	sortname:'PK_TXDATE, TXTIME',
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	hoverrows: true,
		            	pagerpos : 'left',
		            	emptyrecords:'',
// 		            	emptyrecords:'查無資料...',
// 		            	viewrecords: true,
		            	//loadonce: true,
						colNames:['檢視明細','交易日期','交易時間','交易追蹤序號','交易代號','銀行','FEP回覆結果','回覆結果' ,'COMFIRM訊息' ],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},       
							{name:'PK_TXDATE',index:'PK_TXDATE',align:'center',fixed:true,width: 90}, 
							{name:'TXTIME',index:'TXTIME',fixed:true,width: 90},
							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 90},
							{name:'PNAME',index:'PNAME',fixed:true,width: 220},
							{name:'BANKID',index:'BANKID',fixed:true,width: 300},
							{name:'FEP_ERR_DESC',index:'FEP_ERR_DESC',fixed:true,width: 220},
							{name:'RSP_ERR_DESC',index:'RSP_ERR_DESC',fixed:true,width: 220},
							{name:'CONCODE',index:'CONCODE',fixed:true,width: 220}
						],
						loadComplete: function(data){
							//在gridComplete event後發動，將目前所在頁數存到pageForSort
							pageForSort = data.page;
// 							查詢結果無資料
							noDataEvent(data);
						},
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var txdate = "", stan = "", pcode = "";
							data = data.rows;
							//轉換「傳送通知時間」格式
							for(var rowCount in data){
								txdate = data[rowCount].TXDATE;
 								stan = data[rowCount].PK_STAN;
 								pcode = data[rowCount].PCODE;
								data[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+stan+'\', \'' + pcode + '\')"><img src="./images/edit.png"/></button>';
							}
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
						    $("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
// 						    if(window.console){console.log("sortname="+$("#sortname").val());}
// 						    if(window.console){console.log("sortorder="+$("#sortorder").val());}
							get_curPage(this ,null , null);
						    $(this).setGridParam({ postData: { isSearch: 'N' } });
						},
// 						   e
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
	 					loadtext: "處理中...",
// 	 					pgtext: "{0} / {1}",
	 					pgtext: "1 / 1",
				};
				
			}
			
			function initGrid(){
				$("#resultData").jqGrid(gridOption);
			}
			
			function onPut(str){
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				blockUI();
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				searchData();
				unblockUI();
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var qStr = "component=opc_trans_bo&method=getDataByStan&"+$("#formID").serialize();
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = 1;
				newOption.pgtext =  "{0} / {1}";
				resetSortname(newOption , "PK_TXDATE, TXTIME","ASC" , true);
				$("#resultData").jqGrid(gridOption);
			}
			
			function edit_p(str,id,id1,id2){
				$("#formID").validationEngine('detach');
				var tmp={};
				tmp['TXDATE'] = id;
				tmp['STAN'] = id1;
				tmp['PCODE'] = id2;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				if(window.console){console.log("sortname=>>"+$("#sortname").val());}
// 				if(window.console){console.log("serchStrs>>"+$("#serchStrs").val());}
				$("form").submit();
			}
			
			//初始化查詢條件
			function initSearchs(){
				var serchs = {};
				//if(window.console){console.log("initSearchs.serchStrs>>"+$("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					serchs = $.parseJSON($("#serchStrs").val());
				}
				//if(window.console){console.log("serchs>>"+serchs);}
				for(var key in serchs){
					//if(window.console){console.log(key+".val("+$("#"+key).val() + ")");}
					//模式二 塞入user當初查詢條件
					if(key != "ac_key" && key != "serchStrs" && key != "pageForSort"){
						$("#"+key).val(serchs[key]);
					}
				}
			}
			
			function reOnPut(){
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var qStr = "component=opc_trans_bo&method=getDataByStan&"+$("#formID").serialize();
				var newOption = gridOption;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(defaultPage.length != 0){
					pageForSort = parseInt(defaultPage);
				}
				newOption.page = pageForSort;
//			    	20150722 edit by hugo  req by kevin 先不紀錄排序
				resetSortname(newOption , "PK_TXDATE, TXTIME","ASC" , false);
				newOption.pgtext =  "{0} / {1}";
				$("#resultData").jqGrid(gridOption);
				unblockUI();
			}
			
			function changeType(srcId, targetId){
				if($("#"+srcId).val() == "BANK"){
					$("#"+targetId).show();
					$("#"+targetId+" option:not(:first-child)").remove();
					var rdata = {component:"bank_group_bo", method:"getOpbkList"};
					fstop.getServerDataExII(uri, rdata, false, function(obj){
						var select = $("#"+targetId);
						if(obj.result=="TRUE"){
							var dataAry = obj.msg;
							for(var i = 0; i < dataAry.length; i++){
								select.append($("<option></option>").attr("value", dataAry[i].value).text(dataAry[i].label));
							}
// 							20151112 add by hugo req by UAT-2015904-01
							if( srcId == "RECEIVER_TYPE" && fstop.isNotEmpty(receiverbank) && fstop.isNotEmptyString( receiverbank)){
								select.val(receiverbank);
							}
							if( srcId == "SENDER_TYPE" && fstop.isNotEmpty(senderbank) && fstop.isNotEmptyString( senderbank)){
								select.val(senderbank);
							}
						}else if(obj.result=="FALSE"){
							//alert(obj.msg);
						}
					});
				}else{
					$("#"+targetId).hide();
					$("#"+targetId).html("");
				}
			}
			
			function createLABEL(){
				var label = '<label id="LABEL_OPCTXDATE">' + $("#OPCCUR_TXDATE").val() + '</label>';
				if($("#LABEL_OPCTXDATE")){
					$("#LABEL_OPCTXDATE").replaceWith(label);
				}
			}
			
			function createBTN(){
				var btn = btn = '<label class="btn" id="BTN_OPCTXDATE" onclick="reSearchData(\'' + $("#OPCCUR_TXDATE").val() + '\',\'' + 'BTN_OPCTXDATE'+ '\')">';
				btn += $("#OPCCUR_TXDATE").val()
				btn += '</label>';
				if($("#BTN_OPCTXDATE")){
					$("#BTN_OPCTXDATE").replaceWith(btn);
					$("#BTN_OPCTXDATE").addClass("btn_selected");
					
				}
				btn = btn = '<label class="btn" id="BTN_OPCTXDATE_PRE" onclick="reSearchData(\'' + $("#OPCTXDATE_PRE").val() + '\',\'' + 'BTN_OPCTXDATE_PRE'+ '\')">';
				btn += $("#OPCTXDATE_PRE").val()
				btn += '</label>';
				if($("#BTN_OPCTXDATE_PRE")){
					$("#BTN_OPCTXDATE_PRE").replaceWith(btn);
					
				}
			}
			
			function reSearchData( newTxDate , id){
// 				alert("id>>"+id);
				if(id=='BTN_OPCTXDATE'){
					$("#"+id).addClass("btn_selected");
					$("#BTN_OPCTXDATE_PRE").removeClass("btn_selected");
				}else if(id=='BTN_OPCTXDATE_PRE'){
					$("#"+id).addClass("btn_selected");
					$("#BTN_OPCTXDATE").removeClass("btn_selected");
				}else{
					$("#BTN_OPCTXDATE").addClass("btn_selected");
					$("#BTN_OPCTXDATE_PRE").removeClass("btn_selected");
				}
				if($("#"+id).attr('class')==='btn'){
					$("#"+id).addClass("btn_selected");
				}else{
					$("#"+id).remove("btn_selected");
				}
				$("#OPCTXDATE").val(newTxDate);
				
				$("#COUNTDOWN").html(countdownSec);
				startInterval(1, refreshCountdown, []);
				
				var qStr = "component=opc_trans_bo&method=opcSearch&" + $("#formID").serialize();
// 				$("#resultData2").jqGrid('setGridParam',{url: "/eACH/baseInfo?"+qStr});
				$("#resultData2").jqGrid('GridUnload');
				var newOption = gridOption2;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: true };
// 				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData2").jqGrid(newOption);
// 				$("#resultData2").trigger('reloadGrid');
			}
			
			function startInterval(interval, func, values){
				clearInterval(countdownObj);
				countdownObj = setRepeater(func, values, interval);
			}
			
			function refreshCountdown(){
				var nextSec = parseInt($("#COUNTDOWN").html()) - 1;
				if(nextSec <= 0){nextSec = countdownSec;refreshTxdate();reSearchData($("#OPCCUR_TXDATE").val());}
				$("#COUNTDOWN").html(nextSec);
			}
			
			function refreshTxdate(){
// 				var uri = "/eACH/baseInfo?component=opc_trans_bo&method=getTxdate";
				var rdata = {component:"opc_trans_bo", method:"getTxdate"};
				var date = fstop.getServerDataExIII(uri, rdata, false , null ,null,'refreshTxdate' );
				$("#OPCCUR_TXDATE").val(date.txdate);
				$("#OPCTXDATE_PRE").val(date.txdate_pre);
				
				createLABEL();
				createBTN();
			}
			function searchOpcData(){
				getSearchs();
				$("#resultData2").jqGrid('GridUnload');
				var qStr = "component=opc_trans_bo&method=opcSearch&" + $("#formID").serialize();
				var newOption2 = gridOption2;
				newOption2.url = "/eACH/baseInfo?"+qStr;
				newOption2.datatype = "json";
				newOption2.mtype = 'POST';
				$("#resultData2").jqGrid(newOption2);
				groupGridHeaders();
			}
			function groupGridHeaders(){
				$("#resultData2").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'ERROR1200', numberOfColumns: 3, titleText: '交易失敗次數'}
					  ]
				});
			}
			function initGridOption2(){
				gridOption2 = {
						//toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 280,
		            	sorttype:"text",
		            	sortname: 'BANKID',
		            	shrinkToFit: true,
		            	gridview: true,
		            	rowNum: -1,
						colNames:['銀行代號','eACH押碼基碼同步通知','eACH變更押碼基碼通知','eACH換日通知'],
		            	colModel: [
							{name:'BANKID',index:'BANKID',fixed:true,width: 200},
							{name:'ERROR1200',index:'ERROR1200',fixed:true,width: 170,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ERROR1210',index:'ERROR1210',fixed:true,width: 170,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ERROR1310',index:'ERROR1310',fixed:true,width: 180,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						],
		            	gridComplete:function (){
		            		$("#resultData2 .jqgrow:odd").addClass('resultDataRowOdd');
		            		$("#resultData2 .jqgrow:even").addClass('resultDataRowEven');
		            		
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					loadtext: "處理中..."
	 					//,pageText: "{0} / {1}"
				};
			}
			function initGrid2(){
				$("#resultData2").jqGrid(gridOption2);
			}
			//取得user查詢條件
			function getSearchs(){
				var serchs = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					serchs[field.name] = field.value;
				});
				serchs['action']=$("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
			}
		</script>
	</body>
</html>