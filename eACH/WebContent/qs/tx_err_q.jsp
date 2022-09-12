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
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
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
				<html:form styleId="formID" action="/tx_err">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="sourcePage" styleId="sourcePage" value="tx_err"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab" width="99%">
							<tr>
								<td class="header" style="width: 10%">營業日期</td>
								<td style="width: 12%">
									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td rowspan="2" style="border: 1px dashed navy; padding: 0.5em">
									 <b>※&nbsp;錯誤類別說明</b>：<br/>
									「01」：票交所超過3分鐘未回應發動行;&nbsp;「02」：接收單位超過3分鐘未回應票交所;&nbsp;「03」：票交所檢核發動行確認訊息有誤;<br/>
									「04」：整批資料有未完成;&nbsp;「05」：票交未送確認訊息至接收行
								</td>
							</tr>
							<tr>
								<td class="header">清算階段</td>
								<td>
									<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="all">全部</html:option>
										<html:option value="01">1</html:option>
										<html:option value="02">2</html:option>
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
			<div id="rsPanel">
				<fieldset>
					<legend>總計</legend>
					<table id="dataSum"></table>
				</fieldset>
			</div><br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption, gridOption2;
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomRight"});
				$("#BIZDATE").val('<bean:write name="onblocktab_form" property="BIZDATE"/>');
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				
				initSelect();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="onblocktab_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth: true,
		            	height: 370,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	//loadonce: true,
						colNames:['檢視明細','交易日期時間','系統追蹤序號','發動行代號','扣款行/扣款帳號','入帳行/入帳帳號','發動者統編/簡稱','交易類別/交易代號','交易金額','錯誤類別'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 60,sortable:false},       	
							{name:'TXDT',index:'TXDT',fixed:true,width: 140}, 
							{name:'STAN',index:'STAN',fixed:true,width: 100},
							{name:'SENDERBANKID',index:'SENDERBANKID',fixed:true,width: 160},
							{name:'OUTBANKID',index:'OUTBANKID',fixed:true,width: 160},
							{name:'INBANKID',index:'INBANKID',fixed:true,width: 160},
							{name:'SENDERID',index:'SENDERID',fixed:true,width: 120},
							{name:'PCODE',index:'PCODE',fixed:true,width: 120},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ERR_TYPE',index:'ERR_TYPE',align:'center',fixed:true,width: 60}
						],
						loadComplete: function(data){
							get_curPage(this ,data.page , null , null);
							//查詢結果無資料
							noDataEvent(data ,$("#resultData"));
							
							//如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {RECORDS:data.records, TXAMT:data.dataSumList[0].TXAMT};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
						    get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
					    onPaging: function(data) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var list = data.rows;
							var txdate, stan;
 							for(var rowCount in list){
 								txdate = list[rowCount].TXDATE;
 								stan = list[rowCount].STAN;
 								list[rowCount].BTN = '<button type="button" id="edit_' + rowCount+ '" onclick="edit_p(this.id, \''+txdate+'\',\''+stan+'\')"><img src="./images/edit.png"/></button>';
 							}
						},
	 					loadtext: "處理中...",
// 	 					20150530 edit by hugo req ACH 初始化要頁數要 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
						
				};
				gridOption2 = {
						datatype: "local",
						autowidth:true,
		            	height: 22,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	colNames:['查詢結果總筆數','交易金額'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						]
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					blockUI();
					getSearch_condition('search_tab', 'input , select', 'serchStrs');
					searchData(str);
					unblockUI();
				}
			}
			
			function searchData(str){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=tx_err_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.pgtext= "{0} / {1}";
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					newOption.page = parseInt('<bean:write name="onblocktab_form" property="pageForSort"/>');
					resetSortname(newOption,'TXDT','ASC',false);
				}else{
					resetSortname(newOption,'TXDT','ASC',true);
					newOption.page = 1;
					$("#pageForSort").val(1);
				}
				$("#resultData").jqGrid(newOption);
			}
			
			function edit_p(str, txdate, stan){
				var tmp = {TXDATE: txdate, STAN: stan};
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function initSelect(){
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					//onPut('search');
				}
			}
			
		</script>
	</body>
</html>