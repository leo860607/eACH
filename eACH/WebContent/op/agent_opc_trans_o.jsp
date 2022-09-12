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
				<html:form styleId="formID" action="/agent_opc_trans">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="STAN" styleId="STAN" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					
					<fieldset>
						<legend>查詢條件</legend>
						<table id = "search_tab">
							<tr>
								<td class="header" style="width: 10%">交易日期</td>
								<td style="width: 25%">
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
										<html:option value="ACH">交換所</html:option>
										<html:option value="AGENT">代理業者</html:option>
									</html:select>
<%-- 									<html:select styleId="ISSUERID" property="AGENT_COMPANY_ID"></html:select> --%>
									<html:select styleId="ISSUERID" property="ISSUERID"></html:select>
								</td>
								<td class="header">接收端</td>
								<td>
									<html:select styleId="RECEIVER_TYPE" property="RECEIVER_TYPE">
										<html:option value="">全部</html:option>
										<html:option value="ACH">交換所</html:option>
										<html:option value="AGENT">代理業者</html:option>
									</html:select>
<%-- 									<html:select styleId="RECEIVERID" property="AGENT_COMPANY_ID"></html:select> --%>
									<html:select styleId="RECEIVERID" property="RECEIVERID"></html:select>
								</td>
							</tr>
							<tr>
								<td class="header">交易類別</td>
								<td>
									<html:select styleId="PROCESSCODE" property="PROCESSCODE">
										<html:option value="">全部</html:option>
										<html:option value="1000">1000</html:option>
										<html:option value="2000">2000</html:option>
									</html:select>
								</td>
								<td class="header">回覆結果</td>
								<td>
									<html:select styleId="RESPONSECODE" property="RESPONSECODE">
										<html:option value="">全部</html:option>
										<logic:present name="rpt_agent_form" property="txnErrorCodeList">
											<html:optionsCollection name="rpt_agent_form" property="txnErrorCodeList" label="label" value="value"></html:optionsCollection>
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption;
			var pageForSort = 1;
			var defaultPage = '<bean:write name="rpt_agent_form" property="pageForSort"/>';
			var receiverbank = '<bean:write name="rpt_agent_form" property="RECEIVERID"/>';
			var senderbank = '<bean:write name="rpt_agent_form" property="ISSUERID"/>';
			if(window.console){console.log("receiverbank>>"+receiverbank);}
			if(window.console){console.log("senderbank>>"+senderbank);}
			var sign_obj_ary = {TXDATE:' ' , AGENT_COMPANY_ID:'<BR>' ,RESPONSECODE:'<BR>',RSPCODE:'<BR>'};
			var obj_ary = {TXDATE:'TXTIME' , AGENT_COMPANY_ID:'COMPANY_ABBR_NAME' ,RESPONSECODE:'RESPONSECODE_NAME',RSPCODE:'RSPCODE_NAME'};;
			
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
				var txdate = '<bean:write name="rpt_agent_form" property="TXDATE"/>';
				setDatePickerII($("#TXDATE") ,txdate);
				$("#TXTIME_START").timepicker({ 'timeFormat': 'H:i:s' });
				$("#TXTIME_END").timepicker({ 'timeFormat': 'H:i:s' });
				$("#formID").validationEngine({binded:false,promptPosition: "bottomRight"});
				if(window.console){console.log("txdate>>"+txdate);}
				if(!fstop.isNotEmptyString(txdate)){
					$("#TXDATE").datepicker("setDate", new Date());
					$("#TXDATE").val('0' + $("#TXDATE").val());
				}
				
// 				$("#SENDERBANK").hide();
// 				$("#RECEIVERBANK").hide();
				$("#ISSUERID").hide();
				$("#RECEIVERID").hide();
				$("#SENDER_TYPE").change(function(){
					changeType("SENDER_TYPE", "ISSUERID");
				});
				$("#RECEIVER_TYPE").change(function(){
					changeType("RECEIVER_TYPE", "RECEIVERID");
				});
				
// 				initSearchs();
				
				if($("#ac_key").val() == 'back'){
//						20151112 add by hugo req by UAT-2015904-01
					$("#RECEIVER_TYPE").trigger("change");
					$("#SENDER_TYPE").trigger("change");
					
					reOnPut();
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rpt_agent_form" property="msg"/>';
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
		            	sortname:'TXDATE, TXTIME',
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	hoverrows: true,
		            	pagerpos : 'left',
		            	emptyrecords:'',
						colNames:['檢視明細','交易日期時間','代理業者交易序號','代理業者統一編號/簡稱','交易類別','交易回覆結果','原交易回覆結果'  ],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},       
							{name:'TXDATE',index:'TXDATE',align:'center',fixed:true,width: 200 
								, formatter:function (cellvalue, options, rowObject){
											return each_formatter_txt(cellvalue, options, rowObject,sign_obj_ary,obj_ary);
											}
							}, 
							{name:'SEQ',index:'SEQ',fixed:true,width: 120},
							{name:'AGENT_COMPANY_ID',index:'AGENT_COMPANY_ID',fixed:true,width: 200, formatter:function (cellvalue, options, rowObject){
								return each_formatter_txt(cellvalue, options, rowObject,sign_obj_ary,obj_ary);
							}},
							{name:'PROCESSCODE',index:'PROCESSCODE',fixed:true,width: 150 },
							{name:'RESPONSECODE',index:'RESPONSECODE',fixed:true,width: 200, formatter:function (cellvalue, options, rowObject){
								return each_formatter_txt(cellvalue, options, rowObject,sign_obj_ary,obj_ary);
							}},
							{name:'RSPCODE',index:'RSPCODE',fixed:true,width: 200, formatter:function (cellvalue, options, rowObject){
								return each_formatter_txt(cellvalue, options, rowObject,sign_obj_ary,obj_ary);
							}},
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
							var txdate = "", seq = "";
							data = data.rows;
							//轉換「傳送通知時間」格式
							for(var rowCount in data){
								txdate = data[rowCount].TXDATE;
 								seq = data[rowCount].SEQ;
//  								if(window.console){console.log("txdate>>"+txdate+",SEQ>>"+seq);}
								data[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+seq+'\')"><img src="./images/edit.png"/></button>';
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
				var qStr = "component=agent_opc_trans_bo&method=getDataByStan&"+$("#formID").serialize();
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = 1;
				newOption.pgtext =  "{0} / {1}";
				resetSortname(newOption , "TXDATE, TXTIME","DESC" , true);
				$("#resultData").jqGrid(gridOption);
			}
			
			function edit_p(str,id,id1,id2){
				$("#formID").validationEngine('detach');
				var tmp={};
				tmp['TXDATE'] = id;
				tmp['SEQ'] = id1;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				if(window.console){console.log("sortname=>>"+$("#sortname").val());}
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
				var qStr = "component=agent_opc_trans_bo&method=getDataByStan&"+$("#formID").serialize();
				var newOption = gridOption;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(defaultPage.length != 0){
					pageForSort = parseInt(defaultPage);
				}
				newOption.page = pageForSort;
//			    	20150722 edit by hugo  req by kevin 先不紀錄排序
				resetSortname(newOption , "TXDATE, TXTIME","DESC" , false);
				newOption.pgtext =  "{0} / {1}";
				$("#resultData").jqGrid(gridOption);
				unblockUI();
			}
			<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
			function changeType(srcId, targetId){
				if($("#"+srcId).val() == "AGENT"){
					$("#"+targetId).show();
					$("#"+targetId+" option:not(:first-child)").remove();
					var rdata = {component:"agent_profile_bo", method:"getCompany_Id_ABBR_List"};
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
			</logic:equal>
			
			<logic:equal name="login_form" property="userData.USER_TYPE" value="C">
			function changeType(srcId, targetId){
				var val = '<bean:write name="login_form" property="userData.USER_COMPANY"/>';
				if($("#"+srcId).val() == "AGENT"){
					$("#"+targetId).show();
					$("#"+targetId+" option:not(:first-child)").remove();
						var select = $("#"+targetId);
							select.append($("<option></option>").attr("value", val).text(val));
				}else{
					$("#"+targetId).hide();
					$("#"+targetId).html("");
				}
			}
			</logic:equal>
		</script>
	</body>
</html>