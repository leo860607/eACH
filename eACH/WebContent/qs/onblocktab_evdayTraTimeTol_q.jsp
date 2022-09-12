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
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
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
				<html:form styleId="formID" action="/onblocktabevdayTraTimeTol">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="SERNO" styleId="SERNO"/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="necessary header" style="width: 18%">交易日期</td>
								<td style="width: 18%"><html:text styleId="TXTIME" property="TXTIME" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text></td>
								<td class="header" style="width: 28%">業務類別</td>
								<td>
									<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name = "onblocktab_evdayTraTimeTol_form" property="bsIdKist" label="label" value="value"/>
									</html:select>
								</td>								
							</tr>
							<tr>
								<td class="header" >操作行</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
										<html:option value="all">全部</html:option>
										<logic:present name="onblocktab_evdayTraTimeTol_form" property="opbkIdList">
											<html:optionsCollection name="onblocktab_evdayTraTimeTol_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
								<td class="header" >總行代號</td>
								<td>
									<html:select styleId="BGBK_ID" property="BGBK_ID">
										<html:option value="all">全部</html:option>
									</html:select>
								</td>
							</tr>
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
				setDateOnChange($("#TXTIME") ,getBgbk_List);
				$("[id^=TXTIME]").val('<bean:write name="onblocktab_evdayTraTimeTol_form" property="TXTIME"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				groupGridHeaders("resultData");
				$("#dataSum").jqGrid(gridOption2);
				groupGridHeaders("dataSum");
			}
			
			function alterMsg(){
				var msg = '<bean:write name="onblocktab_evdayTraTimeTol_form" property="msg"/>';
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
		            	height: 240,
		            	sorttype:"text",
		            	//sortname:"SENDERACQUIRE asc",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['操作行代號','交易筆數','逾時筆數','筆數','平均值(秒)','筆數','平均值(秒)','筆數','平均值(秒)','筆數','平均值(秒)'],
		            	colModel: [
							//{name:'id.SERNO',index:'id.SERNO',align:'center',fixed:true,width: 100}, 
							{name:'BANKIDANDNAME',index:'BANKIDANDNAME',fixed:true,width: 200},
							{name:'TOTALCOUNT',index:'TOTALCOUNT',fixed:true,width: 90,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PENDCOUNT',index:'PENDCOUNT',fixed:true,width: 90,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PRCCOUNT',index:'PRCCOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PRCTIME',index:'PRCTIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'SAVECOUNT',index:'SAVECOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SAVETIME',index:'SAVETIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'DEBITCOUNT',index:'DEBITCOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'DEBITTIME',index:'DEBITTIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'ACHPRCCOUNT',index:'ACHPRCCOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ACHPRCTIME',index:'ACHPRCTIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
					        //如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {
					        			NUM: data.records,
					        			TOTALCOUNT: data.dataSumList[0].TOTALCOUNT,
					        			PENDCOUNT: data.dataSumList[0].PENDCOUNT,
					        			PRCCOUNT: data.dataSumList[0].PRCCOUNT,
					        			PRCTIME: data.dataSumList[0].PRCTIME,
					        			SAVECOUNT: data.dataSumList[0].SAVECOUNT,
					        			SAVETIME: data.dataSumList[0].SAVETIME,
					        			DEBITCOUNT: data.dataSumList[0].DEBITCOUNT,
					        			DEBITTIME: data.dataSumList[0].DEBITTIME,
					        			ACHPRCCOUNT: data.dataSumList[0].ACHPRCCOUNT,
					        			ACHPRCTIME: data.dataSumList[0].ACHPRCTIME
					        	};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					        
							//查詢結果無資料
							noDataEvent(data ,$("#resultData") );
					    },
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					beforeProcessing: function(data, status, xhr){							
	 						//var avttime = "",userId = "" ,com_id="" ; 
	 						/*
	 						var list = data.rows; 
							for(var rowCount in list){								
								list[rowCount].avgTime = list[rowCount].avgTime+"("+list[rowCount].TXN_STD_PROC_TIME+")";
								list[rowCount].achAvgTime = list[rowCount].achAvgTime+"("+list[rowCount].TCH_STD_ECHO_TIME+")";
								list[rowCount].achSaveTime = list[rowCount].achSaveTime+"("+list[rowCount].TCH_STD_ECHO_TIME+")";
								list[rowCount].achDebitTime = list[rowCount].achDebitTime+"("+list[rowCount].TCH_STD_ECHO_TIME+")";
								list[rowCount].inSaveTime = list[rowCount].inSaveTime+"("+list[rowCount].PARTY_STD_ECHO_TIME+")";
								list[rowCount].outDebitTime = list[rowCount].outDebitTime+"("+list[rowCount].PARTY_STD_ECHO_TIME+")";
								//alert('333  '+avttime);
								//data[rowCount].BTN = '<button type="button" id="edit_' + ser_no + '" onclick="edit_p(this.id , \''+ser_no+'\' , \''+userId+'\' , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
							//if(window.console){console.log("btn>>"+data[rowCount].BTN );}
							}
							*/
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
		            	height: 42,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	colNames:['查詢結果總筆數','交易筆數','逾時筆數','筆數','平均值(秒)','筆數','平均值(秒)','筆數','平均值(秒)','筆數','平均值(秒)'],
		            	colModel: [
							//{name:'id.SERNO',index:'id.SERNO',align:'center',fixed:true,width: 100}, 
							{name:'NUM',index:'NUM',fixed:true,width: 200},
							{name:'TOTALCOUNT',index:'TOTALCOUNT',fixed:true,width: 90,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PENDCOUNT',index:'PENDCOUNT',fixed:true,width: 90,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PRCCOUNT',index:'PRCCOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PRCTIME',index:'PRCTIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'SAVECOUNT',index:'SAVECOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SAVETIME',index:'SAVETIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'DEBITCOUNT',index:'DEBITCOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'DEBITTIME',index:'DEBITTIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}},
							{name:'ACHPRCCOUNT',index:'ACHPRCCOUNT',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ACHPRCTIME',index:'ACHPRCTIME',fixed:true,width: 100,align:'right',formatter:'number',formatoptions:{decimalPlaces:2}}
						]
				};
			}
			
			function groupGridHeaders(id){
				$("#" + id).jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'PRCCOUNT', numberOfColumns: 2, titleText: '單筆處理時間高於標準'},
						{startColumnName: 'SAVECOUNT', numberOfColumns: 2, titleText: '銀行每筆入帳處理時間高於標準'},
						{startColumnName: 'DEBITCOUNT', numberOfColumns: 2, titleText: '銀行每筆扣款處理時間高於標準'},
						{startColumnName: 'ACHPRCCOUNT', numberOfColumns: 2, titleText: '票交所處理時間高於標準'}
					  ]
				});
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "search"){
						$("#USER_COMPANY").prop("disabled", false);
						blockUI();
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						searchData();
						groupGridHeaders("resultData");
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
				var qStr = "component=onblocktab_bo&method=getEvdayTimeTolList&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
//	 	 		20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
	 			newOption.pgtext= "{0} / {1}";
				$("#resultData").jqGrid(newOption);
			}
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
//				$("#SERNO").val(id) ;
//				if(window.console){console.log("id2>>"+id2);}
//				$("#USERID").val(id2) ;
//				$("#USER_COMPANY").val(id3) ;
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function getBgbk_List(opbkId){
				var s_bizdate = $("#TXTIME").val();
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