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
			<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define>
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/func_list">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 100px">功能代號</td>
								<td><html:text styleId="FUNC_ID" property="FUNC_ID" size="10" maxlength="10"></html:text></td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal  name="userData" property="s_auth_type" value="A">
									<label class="btn" id="add" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
		
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				initGridOption();
				initGrid();
				alterMsg();
				initSelect();
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 270,
		            	sortable: true,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
						//colNames:['檢視明細','功能顯示順序','功能代號','功能名稱','功能說明','功能種類','交換所端使用','銀行端使用','發動者端使用','連結網址','上層所屬作業','是否使用Ikey','使用狀態','啟用日期'],
						colNames:['檢視明細','功能代號','功能名稱','功能說明','功能種類','交換所端使用','銀行端使用','連結網址','上層所屬作業','是否使用Ikey','代理清算行功能','功能顯示順序','使用狀態','啟用日期'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false}, 
							{name:'FUNC_ID',index:'FUNC_ID',fixed:true,width: 70},
							{name:'FUNC_NAME',index:'FUNC_NAME',fixed:true,width: 180},
							{name:'FUNC_DESC',index:'FUNC_DESC',fixed:true,width: 250},
							{name:'FUNC_TYPE',index:'FUNC_TYPE',fixed:true,width: 70},
							{name:'TCH_FUNC',index:'TCH_FUNC',align:'center',fixed:true,width: 90},
							{name:'BANK_FUNC',index:'BANK_FUNC',align:'center',fixed:true,width: 90},
							//{name:'COMPANY_FUNC',index:'COMPANY_FUNC',align:'center',fixed:true,width: 90},
							{name:'FUNC_URL',index:'FUNC_URL',fixed:true,width: 150},
							{name:'UP_FUNC_ID',index:'UP_FUNC_ID',fixed:true,width: 90},
							{name:'USE_IKEY',index:'USE_IKEY',align:'center',fixed:true,width: 90},
							{name:'PROXY_FUNC',index:'PROXY_FUNC',align:'center',fixed:true,width: 100},
							{name:'FUNC_SEQ',index:'FUNC_SEQ',fixed:true,width: 85},
							{name:'IS_USED',index:'IS_USED',fixed:true,width: 70},
							{name:'START_DATE',index:'START_DATE',fixed:true,width: 90}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var funcId;
							for(var rowCount in data){
								funcId = data[rowCount].FUNC_ID;
								data[rowCount].BTN = '<button type="button" id="edit" onclick="edit_p(this.id , \''+funcId+'\')"><img src="./images/edit.png"/></button>';
								data[rowCount].FUNC_TYPE = (data[rowCount].FUNC_TYPE == "1"? "作業模組" : "功能項目");
								data[rowCount].TCH_FUNC = '<input type="checkbox" disabled '+(data[rowCount].TCH_FUNC == "Y"?"checked":"")+'>';
								data[rowCount].BANK_FUNC = '<input type="checkbox" disabled '+(data[rowCount].BANK_FUNC == "Y"?"checked":"")+'>';
								data[rowCount].COMPANY_FUNC = '<input type="checkbox" disabled '+(data[rowCount].COMPANY_FUNC == "Y"?"checked":"")+'>';
								data[rowCount].USE_IKEY = '<input type="checkbox" disabled '+(data[rowCount].USE_IKEY == "Y"?"checked":"")+'>';
								data[rowCount].PROXY_FUNC = '<input type="checkbox" disabled '+(data[rowCount].PROXY_FUNC == "Y"?"checked":"")+'>';
								data[rowCount].IS_USED = (data[rowCount].IS_USED == "Y"?"啟用":"停用");
							}
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#resultData") );
						},
	 					loadtext: "處理中..."
				};
			}
			
			function initGrid(){
				$("#resultData").jqGrid(gridOption);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="func_list_form" property="msg"/>';
				var result = '<bean:write name="func_list_form" property="result"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function onPut(str){
				if(str == "search"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
				}
				blockUI();
// 				searchData($("#FUNC_ID").val());
				searchData(str);
				unblockUI();
			}
			
			function add_p(str){
				cleanFormNE(document.getElementById(str));
				$("#formID").validationEngine('detach');
				$("#ac_key").val('add');
				$("#target").val('add_p');
				$("form").submit();
			}
			
			
			
			function searchData(str){
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
// 				var qStr = "component=func_list_bo&method=search_toJson&FUNC_ID="+funcId+"&serchStrs="+$("#serchStrs").val();
				var qStr = "component=func_list_bo&method=search_toJson&"+$("#formID").serialize();
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'FUNC_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'FUNC_ID' , 'ASC' , true);
				}
				//if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(gridOption);
			}
			
			
			function edit_p(str, funcId){
				blockUI();
// 				$("#FUNC_ID").val(funcId);
				var tmp={};
				tmp['FUNC_ID'] = funcId;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}
		</script>
	</body>
</html>