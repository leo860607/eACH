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
		<script type="text/javascript" src="./js/jquery.cookie.js"></script>
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
				<html:form styleId="formID" action="/hr_txp_time">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端查詢條件 -->
							<logic:equal name="userData" property="USER_TYPE" value="A">
								<tr>
									<td class="necessary header" style="width: 18%">交易日期</td>
									<td style="width: 18%">
										<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 18%">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="all">全部</html:option>
											<logic:present name="hr_txp_time_form" property="pcodeList">
												<html:optionsCollection name="hr_txp_time_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">操作行</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
											<html:option value="all">全部</html:option>
											<logic:present name="hr_txp_time_form" property="opbkIdList">
												<html:optionsCollection name="hr_txp_time_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID">
											<html:option value="all">全部</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">清算階段</td>
									<td colspan="3">
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="all">全部</html:option>
											<html:option value="01">1</html:option>
											<html:option value="02">2</html:option>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="export" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;列印匯出</label>
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
					<table id="resultData"></table>
				</fieldset>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>總計</legend>
					<table id="dataSum"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var gridOption2;
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				setDateOnChange($("#TXDATE") ,getBgbk_List);
				$("[id^=TXDATE]").val('<bean:write name="hr_txp_time_form" property="TXDATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="hr_txp_time_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 220,
		            	//sortable: false,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['交易時間(每小時)','交易筆數','成功筆數','失敗筆數','逾時未回覆筆數','單筆平均處理時間(秒)',
						          '銀行每筆入帳平均處理時間(秒)','銀行每筆扣款平均處理時間(秒)','交換所平均處理時間(秒)'],
		            	colModel: [
							{name:'HOURLAPNAME',index:'HOURLAPNAME',fixed:true,width: 130}, 
							{name:'TOTALCOUNT',index:'TOTALCOUNT',fixed:true,width: 70,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'OKCOUNT',index:'OKCOUNT',fixed:true,width: 70,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FAILCOUNT',index:'FAILCOUNT',fixed:true,width: 70,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PENDCOUNT',index:'PENDCOUNT',fixed:true,width: 110,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PRCTIME',index:'PRCTIME',fixed:true,width: 150,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'SAVETIME',index:'SAVETIME',fixed:true,width: 200,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'DEBITTIME',index:'DEBITTIME',fixed:true,width: 200,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'ACHPRCTIME',index:'ACHPRCTIME',fixed:true,width: 200,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
					        //如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {RECORDS:data.records,TOTALCOUNT:data.dataSumList[0].TOTALCOUNT,OKCOUNT:data.dataSumList[0].OKCOUNT,FAILCOUNT:data.dataSumList[0].FAILCOUNT,PENDCOUNT:data.dataSumList[0].PENDCOUNT};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
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
		            	colNames:['查詢結果總筆數','交易筆數','成功筆數','失敗筆數','逾時未回覆筆數'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TOTALCOUNT',index:'TOTALCOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'OKCOUNT',index:'OKCOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FAILCOUNT',index:'FAILCOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PENDCOUNT',index:'PENDCOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						]
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "search"){
						blockUI();
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						searchData();
						unblockUI();
					}else if(str == "export"){
						blockUIForDownload();
						$("#ac_key").val(str);
						$("#target").val('search');
						$("#sortname").val( $("#resultData").getGridParam('sortname') );
						$("#sortorder").val( $("#resultData").getGridParam('sortorder') );
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						$("#formID").submit();
					}
				}
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=hr_txp_time_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				$("#resultData").jqGrid(newOption);
			}
			
			function getBgbk_List(opbkId){
				var s_bizdate = $("#TXDATE").val();
				opbkId = $("#OPBK_ID").val();
				
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo",  method:"getByOpbkId_Single_Date", OPBK_ID:opbkId , s_bizdate:s_bizdate};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
				}
			}
			
			function creatBgBkList(obj){
				var select = $("#BGBK_ID");
				$("#BGBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].BGBK_ID).text(dataAry[i].BGBK_ID + " - " + dataAry[i].BGBK_NAME));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			//==================此區塊為使用struts 輸出檔案會用到的API==================
			var fileDownloadCheckTimer;
			function blockUIForDownload() {
			    var token = new Date().getTime(); //use the current timestamp as the token value
			    $('#dow_token').val(token);
			    blockUI();
			    fileDownloadCheckTimer = window.setInterval(function () {
			      var cookieValue = $.cookie('fileDownloadToken');
			      if (cookieValue == token)
			       finishDownload();
			    }, 1000);
			}
			function finishDownload(){
				window.clearInterval(fileDownloadCheckTimer);
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>