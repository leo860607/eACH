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
				<html:form styleId="formID" action="/clear_data">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="colForSort" styleId="colForSort"/>
					<html:hidden property="ordForSort" styleId="ordForSort"/>
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="srch_BIZDATE" styleId="srch_BIZDATE" value=""/>
					<html:hidden property="srch_CLEARINGPHASE" styleId="srch_CLEARINGPHASE" value=""/>
					<html:hidden property="srch_PCODE" styleId="srch_PCODE" value=""/>
					<html:hidden property="srch_BANKID" styleId="srch_BANKID" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="necessary header" style="width: 16%">營業日</td>
								
								<!-- 票交端查詢條件 -->
							    <logic:equal name="userData" property="USER_TYPE" value="A">
								<td style="width: 30%">
<!-- 								setDateOnChange onchange="getBgbk_List('')" -->
<%-- 									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text> --%>
									<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8"  styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>~
									<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8"  styleClass="validate[required,maxSize[8],minSize[8],twDate,twDateRange[START_DATE,END_DATE,3]] text-input datepicker"></html:text>
								</td>
								</logic:equal>
								
								<!-- 銀行端查詢條件 -->
							    <logic:equal name="userData" property="USER_TYPE" value="B">
							    <td style="width: 30%">
<%-- 									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text> --%>
									<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>
									<div style="display: none">
									    <html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" value="" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</div>
								</td>
								</logic:equal>
								
								<td class="header" style="width: 16%">
									<input type="radio" id="op_type_OPBK" name="op_type" value="OPBK"/>
									<label for="op_type_OPBK">操作行代號</label>
								</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value )">
										<html:option value="all">全部</html:option>
										<logic:present name="clear_data_form" property="opbkIdList">
											<html:optionsCollection name="clear_data_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
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
								<td class="header">
									<input type="radio" id="op_type_CTBK" name="op_type" value="CTBK"/>
									<label for="op_type_CTBK">清算行代號</label>
								</td>
								<td>
									<html:select styleId="CTBK_ID" property="CTBK_ID" onchange="getBgbk_List(this.value )">
										<html:option value="all">全部</html:option>
										<logic:present name="clear_data_form" property="ctbkIdList">
											<html:optionsCollection name="clear_data_form" property="ctbkIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="header" >交易類別</td>
								<td>
									<html:select styleId="PCODE" property="PCODE">
										<html:option value="all">全部</html:option>
										<logic:present name="clear_data_form" property="pcodeList">
											<html:optionsCollection name="clear_data_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
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
			var gridOption, gridOption2;
			var type = "";
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
				setDateOnChange($("#END_DATE") ,getBgbk_List );
				//起訖日期預設代相同值
// 				$("#BIZDATE").val('<bean:write name="clear_data_form" property="BIZDATE"/>');
				$("#START_DATE").val('<bean:write name="clear_data_form" property="START_DATE"/>');
				$("#END_DATE").val('<bean:write name="clear_data_form" property="END_DATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				initOpType();
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					getBgbk_List($("#OPBK_ID").val());
				</logic:equal>
				
				initSearchs();
				if($("#ac_key").val() != ''){
					reOnPut('');
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="clear_data_form" property="msg"/>';
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
		            	height: 250,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['檢視明細','營業日','清算階段','總行代號/名稱','交易類別','應收應付總筆數','應收應付淨額','應收應付手續費總筆數','應收應付手續費淨額'],
		            	colModel: [
							{name:'BTN',index:'BTN',fixed:true,width: 60,sortable:false,align:'center'},
							{name:'format_BIZDATE',index:'format_BIZDATE',fixed:true,width: 90},
							{name:'format_CLEARINGPHASE',index:'format_CLEARINGPHASE',fixed:true,width: 65},
							{name:'BANKIDANDNAME',index:'BANKIDANDNAME',fixed:true,width: 180},
							{name:'PCODEANDNAME',index:'PCODEANDNAME',fixed:true,width: 230},							
							{name:'TOTAL_RECV_PAY_CNT',index:'TOTAL_RECV_PAY_CNT',fixed:true,width: 100},
							{name:'TOTAL_RECV_PAY_AMT',index:'TOTAL_RECV_PAY_AMT',fixed:true,width: 100,align:'right',formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TOTAL_RECV_PAY_FEE_CNT',index:'TOTAL_RECV_PAY_FEE_CNT',fixed:true,width: 150},
							{name:'TOTAL_RECV_PAY_FEE_AMT',index:'TOTAL_RECV_PAY_FEE_AMT',fixed:true,width: 150,align:'right',formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:2}}
						],
						onSortCol: function(index,iCol,sortorder){
					    	//按下欄位的排序事件
					    	$("#colForSort").val(index);
					    	$("#ordForSort").val(sortorder);
						    $(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
							$("#pageForSort").val(data.page);
					        //如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
// if(window.console){console.log("dataSumList>>"+data.dataSumList);}
// if(window.console){console.log("dataSumList>>"+data.dataSumList.length);}
								var sumData ; 
								if(data.dataSumList.length != 0){
									sumData = {RECORDS:data.records,ALL_TOTAL_RECV_PAY_CNT:data.dataSumList[0].ALL_TOTAL_RECV_PAY_CNT,ALL_TOTAL_RECV_PAY_AMT:data.dataSumList[0].ALL_TOTAL_RECV_PAY_AMT,ALL_TOTAL_RECV_PAY_FEE_CNT:data.dataSumList[0].ALL_TOTAL_RECV_PAY_FEE_CNT,ALL_TOTAL_RECV_PAY_FEE_AMT:data.dataSumList[0].ALL_TOTAL_RECV_PAY_FEE_AMT};
								}else{
					        		sumData = {RECORDS:0,ALL_TOTAL_RECV_PAY_CNT:0,ALL_TOTAL_RECV_PAY_AMT:0,ALL_TOTAL_RECV_PAY_FEE_CNT:0,ALL_TOTAL_RECV_PAY_FEE_AMT:0};
								}
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							data = data.rows;
							for(var rowCount in data){
								data[rowCount].BTN = '<button type="button" id="edit_' + rowCount + '" onclick="edit_p(\''+data[rowCount].BIZDATE+'\',\''+data[rowCount].CLEARINGPHASE+'\',\''+data[rowCount].BANKID+'\',\''+data[rowCount].PCODE+'\')"><img src="./images/edit.png"/></button>';
								//if(console.log){console.log(data[rowCount].BTN);}
							}
						},
	 					loadtext: "處理中...",
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1/ 1"
				};
				gridOption2 = {
						datatype: "local",
						autowidth:true,
		            	height: 22,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	colNames:['查詢結果總筆數' , '總應收應付總筆數' ,'總應收應付淨額' ,'總應收應付手續費總筆數' ,'總應收應付手續費淨額'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ALL_TOTAL_RECV_PAY_CNT',index:'ALL_TOTAL_RECV_PAY_CNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ALL_TOTAL_RECV_PAY_AMT',index:'ALL_TOTAL_RECV_PAY_AMT',fixed:true,width: 120,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ALL_TOTAL_RECV_PAY_FEE_CNT',index:'ALL_TOTAL_RECV_PAY_FEE_CNT',fixed:true,width: 180,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ALL_TOTAL_RECV_PAY_FEE_AMT',index:'ALL_TOTAL_RECV_PAY_FEE_AMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:2}}
						]
				};
			}
			
			function initOpType(){
				$("input[name=op_type]").click(function(){
					blockUI();
					
					if($(this).val() == "OPBK"){
						$("#CTBK_ID option:not(:first-child)").remove();
						$("#CTBK_ID").attr("disabled", true);
						$("#OPBK_ID").attr("disabled", false);
						getOpbkList();
						type = "OP";
					}else{
						$("#OPBK_ID option:not(:first-child)").remove();
						$("#OPBK_ID").attr("disabled", true);
						$("#CTBK_ID").attr("disabled", false);
						getCtbkList();
						type = "CT";
					}
					$("#BGBK_ID option:not(:first-child)").remove();
					unblockUI();
				});
				$("#op_type_OPBK").click();
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "search"){
						blockUI();
						
						<logic:equal name="userData" property="USER_TYPE" value="B">
						    $("#END_DATE").val($("#START_DATE").val());
					    </logic:equal>
					    
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						searchData();
						
						<logic:equal name="userData" property="USER_TYPE" value="B">
					        $("#END_DATE").val('');
				        </logic:equal>
						
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
			
			function edit_p(bizdate, clearingphase, bankid, pcode){
				$("#srch_BIZDATE").val(bizdate);
				$("#srch_CLEARINGPHASE").val(clearingphase);
				$("#srch_PCODE").val(pcode);
				$("#srch_BANKID").val(bankid);
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=clear_data_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = 1;
				newOption.sortname = "BANKID, CLEARINGPHASE";
				newOption.pgtext="{0} / {1}";
				$("#resultData").jqGrid(newOption);
			}
			
			function getOpbkList(){
				$("#OPBK_ID option:not(:first-child)").remove();
				var rdata = {component:"bank_group_bo", method:"getOpbkList"};
				fstop.getServerDataExII(uri, rdata, false, createOpbkList);
			}
			
			function createOpbkList(obj){
				var select = $("#OPBK_ID");
				$("#OPBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry = obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						var validTitle=encodeHTML(dataAry[i].value);
						var validvalue=encodeHTML(dataAry[i].label);
						var tmpOption=$("<option></option>");
						tmpOption.attr("value", validTitle).text(validTitle + " - " + validvalue);
						select.append(tmpOption);
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function getCtbkList(){
				$("#CTBK_ID option:not(:first-child)").remove();
				var rdata = {component:"bank_group_bo", method:"getCtbkList"};
				fstop.getServerDataExII(uri, rdata, false, createCtbkList);
			}
			
			function createCtbkList(obj){
				var select = $("#CTBK_ID");
				$("#CTBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry = obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						var validTitle=encodeHTML(dataAry[i].value);
						var validvalue=encodeHTML(dataAry[i].label);
						var tmpOption=$("<option></option>");
						tmpOption.attr("value", validTitle).text(validTitle + " - " + validvalue);
						select.append(tmpOption);
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function getBgbk_List(opbkId ){
// 				alert("type>>"+type);
				var ctbkId = $("#CTBK_ID").val();
					opbkId = $("#OPBK_ID").val() ;
				var s_bizdate = $("#START_DATE").val();
				var e_bizdate = $("#END_DATE").val();
				if((opbkId == '' || opbkId == "all") && (ctbkId == '' || ctbkId == "all")){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbk_Ctbk", OPBK_ID:opbkId  , CTBK_ID:ctbkId, s_bizdate:s_bizdate ,e_bizdate:e_bizdate , type:type };
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
						tmpOption.attr("value", validTitle).text(validTitle + " - " + validvalue);
						select.append(tmpOption);
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
					var validStr=encodeHTML($("#serchStrs").val());
					serchs = $.parseJSON(validStr);
				}
				//if(window.console){console.log("serchs>>"+serchs);}
				for(var key in serchs){
					//if(window.console){console.log(key+".val("+$("#"+key).val() + ")");}
					if(key != "ac_key" && key != "serchStrs" && key != "pageForSort"){
						$("#"+key).val(serchs[key]);
					}
				}
				if(!$.isEmptyObject(serchs)){
					if($("[name=op_type]").val() != serchs['op_type']){
						$("#op_type_"+serchs['op_type']).click();
					}
					$("#"+serchs['op_type']+"_ID").val(serchs[serchs['op_type']+"_ID"]);
					getBgbk_List($("#"+serchs['op_type']+"_ID").val());
					$("#BGBK_ID").val(serchs["BGBK_ID"]);
				}
			}
			
			function reOnPut(str){
				if($("#formID").validationEngine("validate")){
					blockUI();
					$("#resultData").jqGrid('GridUnload');
					var newOption = gridOption;
					var qStr = "component=clear_data_bo&method=pageSearch&"+$("#formID").serialize();
					newOption.url = "/eACH/baseInfo?"+qStr;
					newOption.datatype = "json";
					newOption.mtype = 'POST';
					newOption.page = '<bean:write name="clear_data_form" property="pageForSort"/>';
					newOption.sortname = '<bean:write name="clear_data_form" property="colForSort"/>';
					newOption.sortorder = '<bean:write name="clear_data_form" property="ordForSort"/>';
					newOption.pgtext="{0} / {1}";
					$("#resultData").jqGrid(newOption);
					unblockUI();
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
			
			//=================ESAPI============================
			function encodeHTML(s) {
				return s.replace(/&/g, '').replace(/</g, '').replace(/\//g, '').replace(/\n/g,"").replace(/\r/g,"");
			}
		</script>
	</body>
</html>