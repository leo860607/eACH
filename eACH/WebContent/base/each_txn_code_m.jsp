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
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
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
				<html:form styleId="formID" action="/each_txn_code">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 15%">交易類別</td>
								<td colspan="3">
									<html:select styleId="EACH_TXN_ID" property="EACH_TXN_ID" styleClass="validate[groupRequired[query]]">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name = "each_txn_code_form" property="idList" label="label" value="value"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<!-- <label class="btn" id="clean" onclick ="cleanForm(this)"><img src="./images/clean.png"/>&nbsp;清除查詢條件</label> -->
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="add" onclick ="add_p(this.id);"><img src="./images/add.png"/>&nbsp;新增</label>
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
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var gridOption;
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				initGrid();
				initSelect();
				$("#formID").validationEngine({promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="each_txn_code_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGrid(){
				gridOption = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
						sortname: 'EACH_TXN_ID',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','交易類別','交易類別項目','業務類別代號'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:60, sortable: false},
							{name:'EACH_TXN_ID', fixed: true, width:100},
							{name:'EACH_TXN_NAME', fixed: true, width:620},
							{name:'BUSINESS_TYPE_ID', fixed: true, width:100},
							
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
							var each_txn_id ="";
							for(var rowCount in data){
								each_txn_id = data[rowCount].EACH_TXN_ID;
								//if(window.console)console.log("edit>>"+data[rowCount].id.SND_BRBK_ID);
								//if(window.console)console.log("edit2>>"+brbk_id);
								data[rowCount].BTN = '<button type="button" id="edit_' + each_txn_id + '" onclick="edit_p(this.id , \''+each_txn_id+'\')"><img src="./images/edit.png"/></button>';
// 								if(window.console)console.log("edit3>>"+data[rowCount].BTN);
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
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				var acKey = $("#ac_key").val();
				if(fstop.isNotEmptyString(acKey)){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			function onPut(str){
				if(str == "search"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=each_txn_code_bo&method=search_toJson2&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'EACH_TXN_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'EACH_TXN_ID' , 'ASC' , true);
				}
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}	
			
			function add_p(str){
				$("#formID").validationEngine('detach');
				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				var tmp ={};
				tmp['EACH_TXN_ID'] = id;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
			
			
		</script>
	</body>
</html>
