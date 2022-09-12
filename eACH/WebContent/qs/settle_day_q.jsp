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
				<html:form styleId="formID" action="/settle_day">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端查詢條件 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								<tr>
									<td class="necessary header" style="width: 16%">營業日</td>
									<td style="width: 25%">
										<html:text styleId="DATE" property="DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 16%">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="all">全部</html:option>
											<logic:present name="settle_day_form" property="pcodeList">
												<html:optionsCollection name="settle_day_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">操作行</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
											<html:option value="all">全部</html:option>
											<logic:present name="settle_day_form" property="opbkIdList">
												<html:optionsCollection name="settle_day_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
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
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="all">全部</html:option>
											<html:option value="01">1</html:option>
											<html:option value="02">2</html:option>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<!-- 銀行端查詢條件 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
								<tr>
									<td class="necessary header" style="width: 16%">營業日</td>
									<td style="width: 30%">
										<html:text styleId="DATE" property="DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 16%">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="all">全部</html:option>
											<logic:present name="settle_day_form" property="pcodeList">
												<html:optionsCollection name="settle_day_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">總行代號</td>
									<td>
										<html:hidden styleId="OPBK_ID" property="OPBK_ID"></html:hidden>
										<html:select styleId="BGBK_ID" property="BGBK_ID">
											<html:option value="all">全部</html:option>
										</html:select>
									</td>
									<td class="header">清算階段</td>
									<td>
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
			<br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var gridOption2;
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			//處理欄位排序後停在原本的頁面的變數
			//var pageForSort = 1;
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				setDateOnChange($("#DATE") ,getBgbk_List); 
				$("#DATE").val('<bean:write name="settle_day_form" property="DATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					getBgbk_List($("#OPBK_ID").val());
				</logic:equal>
			}
			
			function alterMsg(){
 				var msg = '<bean:write name="settle_day_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				var gridHeight = 250;
				
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: gridHeight,
		            	sorttype:"text",
		            	sortname:'BANKIDANDNAME',
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['總行代號','清算階段','交易類別','應收筆數','應收金額','應付筆數','應付金額','沖正-應收筆數','沖正-應收金額','沖正-應付筆數','沖正-應付金額','應收應付差額'],
		            	colModel: [
							{name:'BANKIDANDNAME',index:'BANKIDANDNAME',fixed:true,width: 200}, 
							{name:'CLEARINGPHASE',index:'CLEARINGPHASE',fixed:true,width: 65}, 
							{name:'PCODEANDNAME',index:'PCODEANDNAME',fixed:true,width: 65},
							{name:'RECVCNT',index:'RECVCNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RECVAMT',index:'RECVAMT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PAYCNT',index:'PAYCNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PAYAMT',index:'PAYAMT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSRECVCNT',index:'RVSRECVCNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSRECVAMT',index:'RVSRECVAMT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSPAYCNT',index:'RVSPAYCNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSPAYAMT',index:'RVSPAYAMT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'DIF',index:'DIF',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
					        //在gridComplete event後發動，將目前所在頁數存到pageForSort
				        	//pageForSort = data.page;
					        
					        //如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {
					        			RECORDS:data.records,
					        			RECVCNT:data.dataSumList[0].RECVCNT, 
					        			RECVAMT:data.dataSumList[0].RECVAMT, 
					        			PAYCNT:data.dataSumList[0].PAYCNT, 
					        			PAYAMT:data.dataSumList[0].PAYAMT, 
					        			RVSRECVCNT:data.dataSumList[0].RVSRECVCNT, 
					        			RVSRECVAMT:data.dataSumList[0].RVSRECVAMT, 
					        			RVSPAYCNT:data.dataSumList[0].RVSPAYCNT, 
					        			RVSPAYAMT:data.dataSumList[0].RVSPAYAMT,
					        			DIF: (parseFloat(data.dataSumList[0].RECVAMT) + parseFloat(data.dataSumList[0].RVSRECVAMT)) -
					        				 (parseFloat(data.dataSumList[0].PAYAMT) + parseFloat(data.dataSumList[0].RVSPAYAMT))
					        	};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
					    onSortCol: function(index,iCol,sortorder){
					    	//按下欄位的排序事件
// 					    	$("#resultData").jqGrid("setGridParam",{page:pageForSort});
					    	$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
					    beforeProcessing: function(data, status, xhr){
					    	//應收應付差額
 							for(var rowCount in data.rows){
 								data.rows[rowCount].DIF = 
 									(parseFloat(data.rows[rowCount].RECVAMT) + parseFloat(data.rows[rowCount].RVSRECVAMT)) -
 									(parseFloat(data.rows[rowCount].PAYAMT) + parseFloat(data.rows[rowCount].RVSPAYAMT));
 							}
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					loadtext: "處理中...",
// 	 					20150530 edit by hugo req ACH 初始化要頁數要 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
						
				};
				
				gridOption2 = {
						datatype: "local",
						autowidth:true,
		            	height: 39,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	colNames:['查詢結果總筆數','應收筆數','應收金額','應付筆數','應付金額','沖正-應收筆數','沖正-應收金額','沖正-應付筆數','沖正-應付金額','應收應付差額'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RECVCNT',index:'RECVCNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RECVAMT',index:'RECVAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PAYCNT',index:'PAYCNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PAYAMT',index:'PAYAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSRECVCNT',index:'RVSRECVCNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSRECVAMT',index:'RVSRECVAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSPAYCNT',index:'RVSPAYCNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RVSPAYAMT',index:'RVSPAYAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'DIF',index:'DIF',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
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
						getSearch_condition('search_tab', 'input , select', 'serchStrs');
						$("#formID").submit();
					}
				}
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=settle_day_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				$("#resultData").jqGrid(newOption);
			}
			
			function getBgbk_List(opbkId){
				var s_bizdate = $("#DATE").val();
				opbkId = $("#OPBK_ID").val();
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId_Single_Date", OPBK_ID:opbkId , s_bizdate:s_bizdate};
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