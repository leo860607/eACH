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
				<html:form styleId="formID" action="/fee_search_aftb">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<html:hidden property="CLEARINGPHASE_PRE" styleId="CLEARINGPHASE_PRE"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端查詢條件 -->
							<logic:equal name="userData" property="USER_TYPE" value="A">
								<tr>
									<td class="necessary header" style="width: 16%">營業日</td>
									<td style="width: 30%">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>~
										<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twDateRange[START_DATE,END_DATE,3]] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 16%">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="all">全部</html:option>
											<logic:present name="fee_search_form" property="pcodeList">
												<html:optionsCollection name="fee_search_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">操作行</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value);getBrbkIdList()">
											<html:option value="all">全部</html:option>
											<logic:present name="fee_search_form" property="opbkIdList">
												<html:optionsCollection name="fee_search_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
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
								<tr>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbkIdList()">
											<html:option  value="all">全部</html:option>
											<%-- 
												<logic:present name="fee_search_form" property="bgbkIdList">
													<html:optionsCollection name="fee_search_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection>
												</logic:present>
											--%>
										</html:select>
									</td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option  value="all">全部</html:option>
											<logic:present name="fee_search_form" property="brbkIdList">
												<html:optionsCollection name="fee_search_form" property="brbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<!-- 銀行端查詢條件 -->
							<logic:equal name="userData" property="USER_TYPE" value="B">
								<tr>
									<td class="header" style="width: 16%">營業日</td>
									<td colspan="3">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
								</tr>
								<tr>
									<td class="header">清算階段</td>
									<td style="width: 30%">
										<!-- 銀行端隱藏欄位 -->
										<div style="display: none">
											<html:hidden styleId="END_DATE" property="END_DATE" value="" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"/>
											<html:hidden styleId="OPBK_ID" property="OPBK_ID"/>
										</div>
										
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="all">全部</html:option>
											<html:option value="01">1</html:option>
											<html:option value="02">2</html:option>
										</html:select>
									</td>
									<td class="header" style="width: 100px">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="all">全部</html:option>
											<logic:present name="fee_search_form" property="pcodeList">
												<html:optionsCollection name="fee_search_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbkIdList()">
											<html:option  value="all">全部</html:option>
											<logic:present name="fee_search_form" property="bgbkIdList">
												<html:optionsCollection name="fee_search_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option  value="all">全部</html:option>
											<logic:present name="fee_search_form" property="brbkIdList">
												<html:optionsCollection name="fee_search_form" property="brbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
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
				$("#START_DATE").val('<bean:write name="fee_search_form" property="START_DATE"/>');
				//$("#START_DATE").val('0' + $("#START_DATE").val());
				$("#END_DATE").val('<bean:write name="fee_search_form" property="END_DATE"/>');
				//$("#END_DATE").val('0' + $("#END_DATE").val());
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				$("#dataSum").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'FIRECOUNT', numberOfColumns: 2, titleText: '發動單位'},
						{startColumnName: 'DEBITCOUNT', numberOfColumns: 2, titleText: '扣款單位'},
						{startColumnName: 'SAVECOUNT', numberOfColumns: 2, titleText: '入帳單位'},
						{startColumnName: 'CANCELCOUNT', numberOfColumns: 2, titleText: '銷帳單位'}
					  ]
				});
				groupGridHeaders();
				
				<logic:equal name="userData" property="USER_TYPE" value="B">
					getBgbk_List($("#OPBK_ID").val());
				</logic:equal>
			}
			
			function alterMsg(){
				var msg = '<bean:write name="fee_search_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				<logic:equal name="userData" property="USER_TYPE" value="A">
					var gridHeight = 170;
				</logic:equal>
				<logic:equal name="userData" property="USER_TYPE" value="B">
					var gridHeight = 190;
				</logic:equal>
				
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: gridHeight,
		            	sorttype:"text",
		            	sortname: "BANKID, PCODE, ACCTCODE",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['總行代號','分行代號','交易類別',"類別型態",'筆數','金額','筆數','金額','筆數','金額','筆數','金額','應收應付差額'],
		            	colModel: [
							{name:'BGBK_ID_NAME',index:'BGBK_ID_NAME',fixed:true,width: 200},
							{name:'BANKID',index:'BANKID',fixed:true,width: 200},       	
							{name:'PCODE',index:'PCODE',fixed:true,width: 65,sortable:true}, 
							{name:'ACCTCODE',index:'ACCTCODE',fixed:true,width: 65},
							{name:'FIRECOUNT',index:'FIRECOUNT',fixed:true,width: 65,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FIREAMT',index:'FIREAMT',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'DEBITCOUNT',index:'DEBITCOUNT',fixed:true,width: 65,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'DEBITAMT',index:'DEBITAMT',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'SAVECOUNT',index:'SAVECOUNT',fixed:true,width: 65,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SAVEAMT',index:'SAVEAMT',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'CANCELCOUNT',index:'CANCELCOUNT',fixed:true,width: 65,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'CANCELAMT',index:'CANCELAMT',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'DIF',index:'DIF',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}, sortable: false}
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
					        			RECORDS:data.records,
					        			FIRECOUNT:data.dataSumList[0].FIRECOUNT,
					        			FIREAMT:data.dataSumList[0].FIREAMT,
					        			DEBITCOUNT:data.dataSumList[0].DEBITCOUNT,
					        			DEBITAMT:data.dataSumList[0].DEBITAMT,
					        			SAVECOUNT:data.dataSumList[0].SAVECOUNT,
					        			SAVEAMT:data.dataSumList[0].SAVEAMT,
					        			CANCELCOUNT:data.dataSumList[0].CANCELCOUNT,
					        			CANCELAMT:data.dataSumList[0].CANCELAMT,
					        			DIF:parseFloat(data.dataSumList[0].FIREAMT) + parseFloat(data.dataSumList[0].DEBITAMT) + parseFloat(data.dataSumList[0].SAVEAMT) + parseFloat(data.dataSumList[0].CANCELAMT)
					        	};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var list = data.rows;
							for(var rowCount in list){
								//if(window.console){console.log(rowCount);}
								data.rows[rowCount].DIF = parseFloat(data.rows[rowCount].FIREAMT) + parseFloat(data.rows[rowCount].DEBITAMT) + parseFloat(data.rows[rowCount].SAVEAMT) + parseFloat(data.rows[rowCount].CANCELAMT);
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
		            	colNames:['查詢結果總筆數','筆數','金額','筆數','金額','筆數','金額','筆數','金額','應收應付差額'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FIRECOUNT',index:'FIRECOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FIREAMT',index:'FIREAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'DEBITCOUNT',index:'DEBITCOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'DEBITAMT',index:'DEBITAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'SAVECOUNT',index:'SAVECOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SAVEAMT',index:'SAVEAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'CANCELCOUNT',index:'CANCELCOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'CANCELAMT',index:'CANCELAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'DIF',index:'DIF',fixed:true,width: 95,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}}
						]
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "search"){
						blockUI();
						searchData();
						groupGridHeaders();
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
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#END_DATE").val($("#START_DATE").val());
				</logic:equal>
				
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
				
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=fee_search_bo&method=pageSearch_aftb&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				$("#resultData").jqGrid(newOption);
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
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId ,s_bizdate:s_bizdate,e_bizdate:e_bizdate};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
				}
			}
			
			function creatBgBkList(obj){
				var select = $("#BGBK_ID");
				$("#BGBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						var validTitle=encodeHTML(dataAry[i].BGBK_ID);
						var validvalue=encodeHTML(dataAry[i].BGBK_NAME);
						var tmpOption=$("<option></option>");
						tmpOption.attr("value", validTitle).text(validTitle + " - " + validvalue)
						select.append(tmpOption);
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function getBrbkIdList(){
				var rdata = {component:"fee_search_bo", method:"getBank_branch_List", bgbkId:$("#BGBK_ID").val()};
				fstop.getServerDataExII(uri, rdata, false, setBrbkIdList);
			}
			
			function setBrbkIdList(obj){
				var select = $("#BRBK_ID");
				select.children().remove();
				var tmpAll=$("<option></option>");
				tmpAll.attr("value", "all").text("全部");
				select.append(tmpAll);
				for( var key in obj ){
					var validTitle=encodeHTML(obj[key].value);
					var validvalue=encodeHTML(obj[key].label);
					var tmpOption=$("<option></option>");
					tmpOption.attr("value", validTitle).text(validvalue);
					select.append(tmpOption);
				}
			}
			
			function groupGridHeaders(){
				$("#resultData").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'FIRECOUNT', numberOfColumns: 2, titleText: '發動單位'},
						{startColumnName: 'DEBITCOUNT', numberOfColumns: 2, titleText: '扣款單位'},
						{startColumnName: 'SAVECOUNT', numberOfColumns: 2, titleText: '入帳單位'},
						{startColumnName: 'CANCELCOUNT', numberOfColumns: 2, titleText: '銷帳單位'}
					  ]
				});
			}
			
// 			==================此區塊為使用struts 輸出檔案會用到的API==================
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
// 				$.removeCookie('fileDownloadToken' , null); //clears this cookie value 此方式chrome不適用
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
			//=================ESAPI============================
			function encodeHTML(s) {
				return s.replace(/&/g, '').replace(/</g, '').replace(/\//g, '').replace(/\n/g,"").replace(/\r/g,"");
			}
			
// 			function checkExeSatus(s_date ,e_date,cl_pre){
// // 				alert("call checkBizDate")
// 				if(fstop.isEmptyString(date)){
// 					return;
// 				}
// 				var rdata = {component:"eachsysstatustab_bo", method:"checkExeSatus" ,activeDate:date,compareWay:compareWay};
// 				var vResult = fstop.getServerDataExII(uri,rdata,false);
// // 				alert(vResult.result);
// 				if(fstop.isNotEmpty(vResult)){
// 					//不可存,跳alert
// 					if(vResult.result == "TRUE"){
// 						startDateCheck = true;
// 					//可存
// 					}else{
// 						startDateCheck = false;
// 					}
// 					if(window.console){console.log("msg>>"+vResult.msg);}
// 				}else{
// 					alert("系統異常");
// 				}
// 			}
			
		</script>
	</body>
</html>