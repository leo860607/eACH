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
				<html:form styleId="formID" action="/fail_trans">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
						
							<!-- 票交端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								<tr>
									<td class="necessary header" style="width: 16%">營業日</td>
									<td colspan="3">
										<html:text styleId="TXDT" property="TXDT" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[ETXDT]] text-input datepicker"></html:text>
										~<html:text styleId="ETXDT" property="ETXDT" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
								</tr>
								<tr>
									<td class="header">操作行</td>
									<td style="width: 30%">
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)" >
											<html:option value="">全部</html:option>
											<logic:present name="fail_trans_form" property="opt_bankList">
												<html:optionsCollection name="fail_trans_form" property="opt_bankList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
									<td class="header" style="width: 16%">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="resetUserId(this)" >
											<html:option value="">全部</html:option>
											<logic:present name="fail_trans_form" property="bgbkIdList">
												<html:optionsCollection name="fail_trans_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								<tr>
							</logic:equal>
							
							<!-- 銀行端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
								<tr>
									<td class="necessary header" style="width: 16%">營業日</td>
									<td style="width: 30%">
										<html:text styleId="TXDT" property="TXDT" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[ETXDT]] text-input datepicker"></html:text>
										<html:hidden styleId="ETXDT" property="ETXDT"></html:hidden>
									</td>
									<td class="header" style="width: 16%">總行代號</td>
									<td>
										<html:hidden styleId="OPBK_ID" property="OPBK_ID"/>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="resetUserId(this)" >
											<html:option value="">全部</html:option>
											<logic:present name="fail_trans_form" property="bgbkIdList">
												<html:optionsCollection name="fail_trans_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								<tr>
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
 				$("#TXDT").val('<bean:write name="fail_trans_form" property="TXDT"/>');
 				$("#ETXDT").val('<bean:write name="fail_trans_form" property="ETXDT"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				initGridOptionII();
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					getBgbk_List($("#OPBK_ID").val());
				</logic:equal>
			}
			
			function alterMsg(){
				var msg = '<bean:write name="fail_trans_form" property="msg"/>';
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
		            	//sortable: true,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
// 		            	rownumbers:true,
		            	//loadonce: true,電腦系統原因,帳務原因,其他原因
						colNames:['操作行','總筆數', '成功總筆數', '自單位','非自單位', '自單位','非自單位','自單位','非自單位','失敗總筆數','百分比%(失敗總筆數/總筆數 )'],
		            	colModel: [
// 							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
							{name:'SENDERACQUIRE',index:'SENDERACQUIRE',fixed:true,width: 180},
							{name:'TOTALCOUNT',index:'TOTALCOUNT',fixed:true,width: 90,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SUCCESSCOUNT',index:'SUCCESSCOUNT',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SYSERRSELF',index:'SYSERRSELF',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SYSERROUT',index:'SYSERROUT',fixed:true,width: 150,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TXNERRSELF',index:'TXNERRSELF',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TXNERROUT',index:'TXNERROUT',fixed:true,width: 150,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'OTHERRSELF',index:'OTHERRSELF',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'OTHERROUT',index:'OTHERROUT',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FAILCOUNT',index:'FAILCOUNT',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'CALCULATE',index:'CALCULATE',fixed:true,width: 200,formatter:this_Fmatter,align:'right'}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
// 							var ser_no = "",userId = "" ,com_id="";
// 							var list = data.rows;
// 							for(var rowCount in list){
// 								if(window.console){console.log(rowCount);}
// 								ser_no = list[rowCount].id.SERNO;
// 								list[rowCount].BTN = '<button type="button" id="edit_' + ser_no + '" onclick="edit_p(this.id , \''+ser_no+'\' , \''+userId+'\' , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
// 								list[rowCount].BTN = rowCount;
// 								if(window.console){console.log("btn>>"+list[rowCount].BTN);}
// 							}
						},
	 					loadtext: "處理中...",
	 					pgtext: "{0} / {1}"
				};
			}
			function initGridOptionII(){
				jQuery("#resultData").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'SYSERRSELF', numberOfColumns: 2, titleText: '電腦系統原因(筆數/百分比)'},
						{startColumnName: 'TXNERRSELF', numberOfColumns: 2, titleText: '帳務原因(筆數/百分比)'},
						{startColumnName: 'OTHERRSELF', numberOfColumns: 2, titleText: '其他原因(筆數/百分比)'}
					  ]	
					});
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					blockUI();
					searchData();
					unblockUI();
				}
			}
			
			function searchData(){
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					$("#ETXDT").val($("#TXDT").val());
				</logic:equal>
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=fail_trans_bo&method=pageSearch&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				$("#resultData").jqGrid(newOption);
				initGridOptionII();
			}
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			function this_Fmatter (cellvalue, options, rowObject)
			{
				var tol = parseInt(rowObject.TOTALCOUNT);
				var failTol = parseInt(rowObject.FAILCOUNT);
				var size = Math.pow(10, 2);
// 				var p = Math.round((failTol/tol)) * 100 ;
				var p = Math.round((failTol/tol) *100 * size)/size  ;
				return p;
			   // do something here
			}
			
			function getBgbk_List(opbkId){
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId};
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
		</script>
	</body>
</html>