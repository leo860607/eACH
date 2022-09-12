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
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		</script>
	</head>
	<body onload="unblockUI()">
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
				<html:form styleId="formID" action="/agent_profile" method="POST">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab" style="width: 98%">
							<tr>
								<td class="header" style="width: 15%">代理業者統一編號</td>
								<td style="width: 15%">
									<html:text styleId="COMPANY_ID" property="COMPANY_ID" size="12" maxlength="10" styleClass="validate[maxSize[10],notChinese] text-input"></html:text>
								</td>
								<td class="header" style="width: 20%"><span>代理業者名稱</span></td>
								<td>
									<html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="40" maxlength="65"></html:text>
								</td>
							</tr>
							<tr>
							    <td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
								</td>
							</tr>
						</table>
						<span id="countRows"></span>
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
				$(document).ajaxStart(blockUI).ajaxStop(unblockUI);
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				initGrid();
				initSelect();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="agent_profile_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGrid(){
				gridOption = {
						datatype: 'local',
						autowidth:true,
		            	height: 260,
		            	shrinkToFit: true,
						sortname: 'COMPANY_ID',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','代理業者統一編號','代理業者簡稱','代理業者名稱','啟用日期','停用日期','WebService URL','WS_NAME_SPACE','代理業者基碼','代理業者基碼代號','代理業者代號(會計科用)'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:60, sortable: false},
							{name:'COMPANY_ID', fixed: true, width:120},
							{name:'COMPANY_ABBR_NAME', fixed: true, width:100},
							{name:'COMPANY_NAME', fixed: true, width:250},
							{name:'ACTIVE_DATE', fixed: true, width:80},
							{name:'STOP_DATE', fixed: true, width:80},
							{name:'WS_URL', fixed: true, width:150},
							{name:'WS_NAME_SPACE', fixed: true, width:130},
							{name:'KEY_ID', fixed: true, width:90},
							{name:'KEY_FLAG', fixed: true, width:90},
							{name:'COMPANY_NO', fixed: true, width:150}
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeProcessing: function(data, status, xhr){
							var com_id = "";
							for(var rowCount in data){
								com_id = data[rowCount].COMPANY_ID;
								data[rowCount].BTN = '<button type="button" id="edit_' + com_id + '" onclick="edit_p(this.id , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
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
				
				$("#resultData").jqGrid(gridOption);
			}
			
			function initSelect(){
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					//因資料量過 故預設一開始不查詢
					//onPut('search');
				}
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str != "back" && $("#COMPANY_ID").val() == "" && $("#COMPANY_NAME").val() == "" && !confirm("確定查詢全部資料?(載入時間較長)")){
						return;
					}
					blockUI();
					getSearch_condition('search_tab', 'input, select', 'serchStrs');
					$("#resultData").jqGrid('GridUnload');
					var newOption = gridOption;
					var qStr = "component=agent_profile_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+"&"+encodeURI($("#formID").find("[name!='serchStrs']").serialize());
					newOption.url = "/eACH/baseInfo?"+qStr;
					newOption.datatype = "json";
					newOption.mtype = 'POST';
					if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
						resetSortname(newOption , 'COMPANY_ID' , 'ASC' , false);
					}else{
						resetSortname(newOption , 'COMPANY_ID' , 'ASC' , true);
					}
					$("#resultData").jqGrid(newOption);
				}
			}
			
			function add_p(str){
				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}
			
			function edit_p(str, companyId){
				var tmp = {COMPANY_ID: companyId};
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
		</script>
	</body>
</html>
