
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
				<html:form styleId="formID" action="/undone_txdata">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="sourcePage" styleId="sourcePage" value="undone_txdata"/>
					<html:hidden property="TXDATE" styleId="TXDATE"/>
					<html:hidden property="STAN" styleId="STAN"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端 -->
							<logic:equal name="userData" property="USER_TYPE" value="A">
								<tr>
									<td class="necessary header" style="width: 15%">營業日</td>
									<td style="width: 300px">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>~
										<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twDateRange[START_DATE,END_DATE,3]] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 15%">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="all">全部</html:option>
											<html:option value="01">1</html:option>
											<html:option value="02">2</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">操作行</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
											<html:option value="all">全部</html:option>
											<logic:present name="undone_txdata_form" property="opbkIdList">
												<html:optionsCollection name="undone_txdata_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
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
									<td class="header">業務類別</td>
									<td>
										<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID">
											<html:option value="all">全部</html:option>
											<logic:present name="undone_txdata_form" property="bsTypeList">
												<html:optionsCollection name="undone_txdata_form" property="bsTypeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
									<td class="header">系統追蹤序號</td>
									<td>
										<html:text styleId="OSTAN" property="OSTAN" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									
									</td>
								</tr>
								<tr>
									<td class="header">處理情形</td>
									<td>
										<html:select styleId="RESULTCODE" property="RESULTCODE">
											<html:option value="all">全部</html:option>
											<html:option value="A">已處理</html:option>
											<html:option value="P">未處理</html:option>
										</html:select>
									</td>
									<td class="header">是否過濾整批資料</td>
									<td>
										<html:checkbox styleId="FILTER_BAT" property="FILTER_BAT" ></html:checkbox>
									</td>
								</tr>
									
							</logic:equal>
							
							<!-- 銀行端 -->
							<logic:equal name="userData" property="USER_TYPE" value="B">
							<html:hidden styleId="FILTER_BAT" property="FILTER_BAT" value="Y" ></html:hidden>
								<tr>
									<td class="necessary header" style="width: 15%">營業日</td>
									<td style="width: 300px">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>
										<%-- ~<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text> --%>
										<html:hidden styleId="END_DATE" property="END_DATE"/>
									</td>
									<td class="header" style="width: 15%">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="">全部</html:option>
											<html:option value="01">1</html:option>
											<html:option value="02">2</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<%--
									<td class="header">操作行</td>
									<td>
										 
										<html:select styleId="OPBK_ID" property="OPBK_ID" disabled="true">
											<html:option value="all">全部</html:option>
											<logic:present name="undone_txdata_form" property="opbkIdList">
												<html:optionsCollection name="undone_txdata_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
									--%>
									<td class="header">總行代號</td>
									<td>
										<html:hidden styleId="OPBK_ID" property="OPBK_ID"></html:hidden>
										<html:select styleId="BGBK_ID" property="BGBK_ID">
											<html:option value="all">全部</html:option>
										</html:select>
									</td>
									<td class="header">業務類別</td>
									<td>
										<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID">
											<html:option value="all">全部</html:option>
											<logic:present name="undone_txdata_form" property="bsTypeList">
												<html:optionsCollection name="undone_txdata_form" property="bsTypeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
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
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var gridOption2;
			var pageForSort = 1;
			var defaultPage = '<bean:write name="undone_txdata_form" property="pageForSort"/>';
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				setDateOnChange($("#START_DATE") ,getBgbk_List);
				setDateOnChange($("#END_DATE") ,getBgbk_List);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				groupGridHeaders();
				
				/* 已由Action帶入營業日
				$("#START_DATE").datepicker("setDate", new Date());
				$("#START_DATE").val('0' + $("#START_DATE").val());
				$("#END_DATE").datepicker("setDate", new Date());
				$("#END_DATE").val('0' + $("#END_DATE").val());
				*/
				$("#START_DATE").val("<bean:write name='undone_txdata_form' property='START_DATE'/>");
				$("#END_DATE").val("<bean:write name='undone_txdata_form' property='END_DATE'/>");
				
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#OPBK_ID").val("<bean:write name='undone_txdata_form' property='OPBK_ID'/>");
				</logic:equal>
				initSearchs();
				getBgbk_List($("#OPBK_ID").val());
				initSearchs();
				
				if($("#ac_key").val() == "back"){
					if(defaultPage.length != 0){
						pageForSort = parseInt(defaultPage);
					}
					reOnPut();
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="undone_txdata_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				<logic:equal name="userData" property="USER_TYPE" value="A">
					var gridHeight = 270;
				</logic:equal>
				<logic:equal name="userData" property="USER_TYPE" value="B">
					var gridHeight = 200;
				</logic:equal>
				
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: gridHeight,
		            	sortname:'TXDT',
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['檢視明細','交易日期','交易類別','交易代號','發動者統編','系統追蹤序號','發動行代號','代號','帳號','代號','帳號','交易金額'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},
							{name:'TXDT',index:'TXDT',fixed:true,width: 150},
							{name:'PCODE',index:'PCODE',fixed:true,width: 130 },
							{name:'TXID',index:'TXID',fixed:true,width: 90 },
							{name:'SENDERID',index:'SENDERID',fixed:true,width: 90},
							{name:'STAN',index:'STAN',fixed:true,width: 90},
							{name:'SENDERBANKID',index:'SENDERBANKID',fixed:true,width: 180 },
							{name:'OUTBANKID',index:'OUTBANKID',fixed:true,width: 180 },
							{name:'OUTACCTNO',index:'OUTACCTNO',fixed:true,width: 150 },
							{name:'INBANKID',index:'INBANKID',fixed:true,width: 180 },
							{name:'INACCTNO',index:'INACCTNO',fixed:true,width: 150},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 95, align:"right", formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						],
// 						loadComplete: function(data){
// 							//在gridComplete event後發動，將目前所在頁數存到pageForSort
// 				        	pageForSort = data.page;
// 				        	noDataEvent(data);
// 						},
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
							//在gridComplete event後發動，將目前所在頁數存到pageForSort
				        	pageForSort = data.page;
				        	noDataEvent(data);
					        //如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {RECORDS:data.records,TXAMT:data.dataSumList[0].TXAMT};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var txdate = "", stan = "";							
							var list = data.rows;
 							for(var rowCount in list){ 								
 								txdate = list[rowCount].TXDATE;
 								stan = list[rowCount].STAN; 								
 								list[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+stan+'\')"><img src="./images/edit.png"/></button>';
 							}
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
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
		            	colNames:['查詢結果總筆數','交易金額'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						]
				};
			}
			
			function setDate(id,date){
				$("#" + id).val(date);
			}
			
			function onPut(str){
				<logic:equal name="userData" property="USER_TYPE" value="B">
					setDate('END_DATE', $("#START_DATE").val());
				</logic:equal>
				
				if($("#formID").validationEngine("validate")){
					$("#OPBK_ID").prop("disabled", false);
					blockUI();
					getSearch_condition('search_tab' , 'input , select , checkbox' , 'serchStrs');
					if(window.console){console.log("serchStrs>>"+$("#serchStrs").val());}
					searchData();
					groupGridHeaders();
					unblockUI();
					<logic:equal name="userData" property="USER_TYPE" value="B">
						$("#OPBK_ID").prop("disabled", true);
					</logic:equal>
				}
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=undone_txdata_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = 1;
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				resetSortname(newOption , 'TXDT' , 'ASC' , true);
				$("#pageForSort").val(1);
				$("#resultData").jqGrid(newOption);
			}
			
			function groupGridHeaders(){
				$("#resultData").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'OUTBANKID', numberOfColumns: 2, titleText: '扣款行'},
						{startColumnName: 'INBANKID', numberOfColumns: 2, titleText: '入帳行'}
					  ]
				});
			}
			
			function getBgbk_List(opbkId){
				var s_bizdate = $("#START_DATE").val();
				var e_bizdate = $("#END_DATE").val();
				opbkId = $("#OPBK_ID").val();
				<logic:equal name="userData" property="USER_TYPE" value="B">
					e_bizdate = "";
				</logic:equal>
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate , e_bizdate:e_bizdate};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
				}
			}
			
			function creatBgBkList(obj){
				var select = $("#BGBK_ID");
				$("#BGBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						var validTitle=makeValidStr(dataAry[i].BGBK_ID);
						var validvalue=makeValidStr(dataAry[i].BGBK_NAME);
						var option=$("<option></option>");
						option.attr("value", validTitle).text(validTitle + " - " + validvalue);
						select.append(option);
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			//初始化查詢條件
			function initSearchs(){
				var serchs = {};
				//if(window.console){console.log("initSearchs.serchStrs>>"+$("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					var validStr=$("#serchStrs").val();
					validStr = makeValidStr(validStr);
					serchs = $.parseJSON(validStr);
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
			
			function edit_p(str,id,id1){
				$("#TXDATE").val(id);
				$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
				$("#STAN").val(id1);
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function reOnPut(){
				<logic:equal name="userData" property="USER_TYPE" value="B">
					setDate('END_DATE', $("#START_DATE").val());
				</logic:equal>
				
				if($("#formID").validationEngine("validate")){
					$("#OPBK_ID").prop("disabled", false);
					blockUI();
					$("#resultData").jqGrid('GridUnload');
					var newOption = gridOption;
					var qStr = "component=undone_txdata_bo&method=pageSearch&"+$("#formID").serialize();
					newOption.url = "/eACH/baseInfo?"+qStr;
					newOption.datatype = "json";
					newOption.mtype = 'POST';
					newOption.page = pageForSort;
//	 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
					newOption.pgtext= "{0} / {1}";
					newOption.page = parseInt('<bean:write name="undone_txdata_form" property="pageForSort"/>');
					resetSortname(newOption , 'TXDT' , 'ASC' , false);
					$("#resultData").jqGrid(newOption);
					groupGridHeaders();
					unblockUI();
					<logic:equal name="userData" property="USER_TYPE" value="B">
						$("#OPBK_ID").prop("disabled", true);
					</logic:equal>
				}
			}
			//=================ESAPI============================
			function makeValidStr(s) {
    			return s.replace(/&/g, '').replace(/</g, '').replace(/\//g, '').replace(/\n/g,"").replace(/\r/g,"");
			}
		</script>
	</body>
</html>