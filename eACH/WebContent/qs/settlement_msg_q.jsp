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
				<html:form styleId="formID" action="/settlement_msg">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="STAN" styleId="STAN" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 15%">交易日期</td>
								<td style="width: 35%">
									<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width: 15%">交易起訖時間</td>
								<td style="width: 35%">
									<html:text styleId="TXTIME_START" property="TXTIME_START" size="10"></html:text>~
									<html:text styleId="TXTIME_END" property="TXTIME_END" size="10"></html:text>
								</td>
							</tr>
							<tr>
								<td class="header">發動端</td>
								<td>
									<html:select styleId="SENDER_TYPE" property="SENDER_TYPE">
										<html:option value="all">全部</html:option>
										<html:option value="TCH">交換所</html:option>
										<html:option value="BANK">銀行</html:option>
									</html:select>
									<html:select styleId="SENDERBANK" property="SENDERBANK"></html:select>
								</td>
								<td class="header">接收端</td>
								<td>
									<html:select styleId="RECEIVER_TYPE" property="RECEIVER_TYPE">
										<html:option value="all">全部</html:option>
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
								<td class="header" style="width: 15%">
									回覆結果
								</td>
								<td>
									<select id="RSPCODE" name="RSPCODE">
										<option value="all">全部</option>
										<option value="S">成功</option>
										<option value="F">失敗</option>
									</select>
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption;
			var pageForSort = 1;
			var defaultPage = '<bean:write name="opc_trans_form" property="pageForSort"/>';
			var i =0 ;
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				initGridOption();
				initGrid();
				setDatePicker();
				$("#TXTIME_START").timepicker({ 'timeFormat': 'H:i:s' });
				$("#TXTIME_END").timepicker({ 'timeFormat': 'H:i:s' });
				$("#formID").validationEngine({binded:false,promptPosition: "bottomRight"});
				
				$("#TXDATE").datepicker("setDate", new Date());
				$("#TXDATE").val('0' + $("#TXDATE").val());
				
				$("#SENDERBANK").hide();
				$("#RECEIVERBANK").hide();
				$("#SENDER_TYPE").change(function(){
					changeType("SENDER_TYPE", "SENDERBANK");
				});
				$("#RECEIVER_TYPE").change(function(){
					changeType("RECEIVER_TYPE", "RECEIVERBANK");
				});
				
				initSearchs();
				
				if($("#ac_key").val() == 'back'){
					reOnPut();
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="opc_trans_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				var tmp = i == 0 ? "1 / 1": "{0} / {1}";
				i=1;
				gridOption = {
						toppager: true,
						datatype: "local",
						pagerpos: "left",
						autowidth:true,
		            	height: 250,
		            	sorttype:"text",
// 		            	sortname:'PK_TXDATE, TXTIME',
		            	sortname:'TXDATE, TXDT , SENDERBANK ,RECEIVERBANK',
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	//loadonce: true,
		            	/*
						colNames:['檢視明細','交易日期','交易時間','銀行','交易代號','交易訊息','回應代碼'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},       
							{name:'PK_TXDATE',index:'PK_TXDATE',align:'center',fixed:true,width: 90,sortable:false}, 
							{name:'TXTIME',index:'TXTIME',fixed:true,width: 90},
							{name:'BANKNAME',index:'BANKNAME',fixed:true,width: 90},
							{name:'PCODE',index:'PCODE',fixed:true,width: 200},
							{name:'DATAFIELD',index:'DATAFIELD',fixed:true,width: 200},
							{name:'RSP_ERR_DESC',index:'RSP_ERR_DESC',fixed:true,width: 250}
						],
						*/
						colNames:['檢視明細','交易日期','交易時間','交易追蹤序號','交易代號','銀行代號','被查詢參加單位','FEP回覆結果','回覆結果'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},       
							{name:'TXDATE',index:'TXDATE',align:'center',fixed:true,width: 90}, 
							{name:'TXDT',index:'TXDT',fixed:true,width: 90},
// 							{name:'PK_TXDATE',index:'PK_TXDATE',align:'center',fixed:true,width: 90}, 
// 							{name:'TXTIME',index:'TXTIME',fixed:true,width: 90},
// 							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 90},
							{name:'PK_STAN',index:'STAN',fixed:true,width: 90},
// 							{name:'PNAME',index:'PNAME',fixed:true,width: 220},
							{name:'PCODE',index:'PCODE',fixed:true,width: 220},
// 							{name:'BANK',index:'BANK',fixed:true,width: 300},
							{name:'BANK',index:'SENDERBANK ,RECEIVERBANK',fixed:true,width: 300},
							{name:'BANKID',index:'BANKID',fixed:true,width: 300},
							{name:'RSPRESULTCODE',index:'RSPRESULTCODE',fixed:true,width: 220},
							{name:'RSPCODE',index:'RSPCODE',fixed:true,width: 220}
// 							{name:'FEP_ERR_DESC',index:'FEP_ERR_DESC',fixed:true,width: 220},
// 							{name:'RSP_ERR_DESC',index:'RSP_ERR_DESC',fixed:true,width: 220}
						],
						loadComplete: function(data){
							//在gridComplete event後發動，將目前所在頁數存到pageForSort
				        	pageForSort = data.page;
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
// 								txdate = data[rowCount].TXDATE;
								txdate = data[rowCount].PK_TXDATE;
 								stan = data[rowCount].PK_STAN;
//  								pcode = data[rowCount].PCODE;
 								pcode = data[rowCount].PNAME;
								data[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+stan+'\', \'' + pcode + '\')"><img src="./images/edit.png"/></button>';
								
							}
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
// 						    $("#sortname").val(index);
// 						    $("#sortorder").val(sortOrder);
						    $(this).setGridParam({ postData: { isSearch: 'N' } });
						},
// 						   e
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
	 					loadtext: "處理中...",
// 	 					20150427 edit by hugo req ACH 初始化要頁數要 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
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
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
				searchData();
				unblockUI();
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
// 				var qStr = "component=settlement_msg_bo&method=getDataByStan&"+$("#formID").serialize();
				var qStr = "component=settlement_msg_bo&method=pageSearch&"+$("#formID").serialize();
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = 1;
// 				20150427 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				//if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(gridOption);
			}
			
			function edit_p(str,id,id1,id2){
				$("#formID").validationEngine('detach');
				
				var oTXDATE = $("#TXDATE").val();
				$("#TXDATE").val(id);
				$("#STAN").val(id1);
				$("#PCODE").val(id2);
				$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				
				var url = "${pageContext.request.contextPath}".replace("/eACH" , "")+$("#formID").attr("action")+"?ac_key=edit&target=edit_p&PCODE="+id2+"&TXDATE="+id+"&STAN="+id1;
				$("#TXDATE").val(oTXDATE);
				window.open(url, '_blank');
				
				//$("form").submit();
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
				var qStr = "component=settlement_msg_bo&method=getDataByStan&"+$("#formID").serialize();
				var newOption = gridOption;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(defaultPage.length != 0){
					pageForSort = parseInt(defaultPage);
				}
				newOption.page = pageForSort;
// 				20150427 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
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
						}else if(obj.result=="FALSE"){
							//alert(obj.msg);
						}
					});
				}else{
					$("#"+targetId).hide();
					$("#"+targetId).html("");
				}
			}
		</script>
	</body>
</html>