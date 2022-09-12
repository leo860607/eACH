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
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/json2.js"></script>
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
			<html:form styleId="formID" action="/ap_status">
				<html:hidden property="ac_key" styleId="ac_key" />
				<html:hidden property="target" styleId="target" value=""/>
				<html:hidden property="BGBK_ID" styleId="BGBK_ID" value=""/>
				<html:hidden property="APID" styleId="APID" value=""/>
				<html:hidden property="serchStrs" styleId="serchStrs" />
				<html:hidden property="sortname" styleId="sortname" />
				<html:hidden property="sortorder" styleId="sortorder" />
				<html:hidden property="edit_params" styleId="edit_params" />
				<html:hidden property="pageForSort" styleId="pageForSort"/>
			</html:form>
			<div id="rsPanel" style="margin-top: -3px">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>	
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption;
			
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				initGridOption();
				initGrid();
				initSelect();
// 				searchData();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="ap_status_form" property="msg"/>';
				if(msg != ""){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 370,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
						colNames:['檢視明細','操作行代號','系統狀態','應用系統狀態'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false}, 
							{name:'BGBK_ID',index:'BGBK_ID',fixed:true,width: 85},
							{name:'MBSYSSTATUSNAME',index:'MBSYSSTATUSNAME',fixed:true,width: 120},
							{name:'MBAPSTATUSNAME',index:'MBAPSTATUSNAME',fixed:true,width: 120}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var id = "";
							for(var rowCount in data){
								id = "edit_" + data[rowCount].BGBK_ID + '@' + data[rowCount].APID;
								data[rowCount].BTN = '<button type="button" id="' + id + '" onclick="edit_p(this.id)"><img src="./images/edit.png"/></button>';
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
			
			function onPut(str){
				//使用者操作軌跡用
				var serchs = {};
				serchs['action'] = $("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
				
				$("#resultData").jqGrid('GridUnload');
				var qStr = "component=ap_status_bo&method=getData&"+$('#formID').serialize();
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'BGBK_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'BGBK_ID' , 'ASC' , true);
				}
				//if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(gridOption);
			}
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			function edit_p(id){
				id = id.split("_")[1];
				var bgbkId = id.split("@")[0];
				var apId = id.split("@")[1];
				$("#BGBK_ID").val(bgbkId);
				$("#APID").val(apId);
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
		</script>
	</body>
</html>