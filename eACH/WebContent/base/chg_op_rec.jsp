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
		<logic:equal name="bank_group_form" property="ac_key" value="OP">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-操作行變更記錄</title>
		</logic:equal>
		<logic:equal name="bank_group_form" property="ac_key" value="CT">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-清算行變更記錄</title>
		</logic:equal>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/jquery.timepicker.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.timepicker.min.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<style type="text/css">
			#countdownBlock{position:relative;float:right;top:-27px;background-color: #ECF1F6;padding:0px 5px}
		</style>
	</head>
	<body>
	<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define>
		<div id="container">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<logic:equal name="bank_group_form" property="ac_key" value="OP">
					<a href="#">操作行變更記錄</a>
				</logic:equal>
				<logic:equal name="bank_group_form" property="ac_key" value="CT">
					<a href="#">清算行變更記錄</a>
				</logic:equal>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/bank_group">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
				</html:form>
			</div>
			<div id="rsPanel" style="margin-top: 3px">
				<fieldset>
				<logic:equal name="bank_group_form" property="ac_key" value="OP">
					<legend>操作行變更記錄 &nbsp;
<!-- 						<label class="btn" id="btn_clearingPhase1"></label>&nbsp; -->
					</legend>
				</logic:equal>
				<logic:equal name="bank_group_form" property="ac_key" value="CT">
					<legend>清算行變更記錄 &nbsp;
<!-- 						<label class="btn" id="btn_clearingPhase1"></label>&nbsp; -->
					</legend>
				</logic:equal>
					
					<table id="resultData"></table>
				</fieldset>
			</div>
		</div>
		
		<script type="text/javascript">
		var i = 0;
		if(window.console){console.log("window.width>>"+$(window).width()+"window.height()"+$(window).height());}
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		var gridOption;
		var countdownObj, countdownSec = 50;
		var tmpsysTime ;
		var tmpDate, tmpPhase ;
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				initGrid();
				onPut('<bean:write name="bank_group_form" property="BGBK_ID"/>','<bean:write name="bank_group_form" property="ac_key"/>');
			}
			
			
			
			function initGrid(){
				gridOption = {
						//避免jqGrid自發性的submit
						datatype: 'local',
// 						width: 910,
		            	height: "490px",
						autowidth: true,
						autoheight: true,
		            	shrinkToFit: true,
// 		            	sortable: true, req by 票交 李建利 統一改成欄位不可拖拉互換
						sortname: 'START_DATE',
						sortorder: 'DESC',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	<logic:equal name="bank_group_form" property="ac_key" value="OP">
							colNames:['總行代號','總行名稱','操作行代號','操作行名稱','啟用日期' , '操作行異動備註'],
						</logic:equal>
		            	<logic:equal name="bank_group_form" property="ac_key" value="CT">
							colNames:['總行代號','總行名稱','清算行代號','清算行名稱','啟用日期' , '清算行異動備註'],
						</logic:equal>
		            	
		            	colModel: [
							{name:'BGBK_ID', fixed: true, width:100 },
							{name:'BGBK_NAME', fixed: true, width:100 },
							<logic:equal name="bank_group_form" property="ac_key" value="OP">
								{name:'OPBK_ID', fixed: true, width:100},
								{name:'OPBK_NAME', fixed: true, width:100},
								{name:'START_DATE', fixed: true, width:100},
								{name:'OP_NOTE', fixed: true, width:600}
							</logic:equal>
			            	<logic:equal name="bank_group_form" property="ac_key" value="CT">
			            		{name:'CTBK_ID', fixed: true, width:100},
			            		{name:'CTBK_NAME', fixed: true, width:100},
			            		{name:'START_DATE', fixed: true, width:100},
			            		{name:'CT_NOTE', fixed: true, width:600}
							</logic:equal>
							
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
							var seq =""; cl =""; bizdate ="";
						},
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			
			function onPut(bgbk_id , type){
				var qStr = "component=bank_opbk_bo&method=getOpbkRec&BGBK_ID="+bgbk_id+"";
				if(type == 'CT'){
					qStr = "component=bank_ctbk_bo&method=getCtbkRec&BGBK_ID="+bgbk_id+"";
				}
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}	
			
			
			
// 			取得user查詢條件
			function getSearchs(){
				var serchs = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					serchs[field.name] = field.value;
				});
					if(window.console){console.log("JSON.stringify(serchs)"+JSON.stringify(serchs));}
				$("#serchStrs").val(JSON.stringify(serchs));
			}
			
// 			初始化查詢條件
			function initSearchs(){
				var serchs = {};
				if(window.console){console.log("initSearchs.serchStrs>>"+$("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					serchs = $.parseJSON($("#serchStrs").val()) ;
				}
// 				if(window.console){console.log("serchs>>"+serchs);}
				for(var key in serchs){
// 					if(window.console){console.log(key+"="+serchs[key]);}
// 					模式一 所有查詢條件清空
// 					$("#"+key).val("");
					if(window.console){console.log(key+".val"+$("#"+key).val());}
// 					模式二 塞入user當初查詢條件
					if(key!="ac_key"){
						$("#"+key).val(serchs[key]);
					}
					
// 					$("#"+key).val("0040000");
				}
			}
			
// 			表單清空
			function cleanFormII(){
				$.each($('#formID').serializeArray(), function(i, field) {
					if(field.name!="serchStrs"){
						$("#"+field.name).val("");
					}
					
				});
			}
			
			
			function alterMsg(){
				var msg = '<bean:write name="bank_group_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function this_Fmatter (cellvalue, options, rowObject)
			{
				var status = rowObject.PCODE;
				var retstr = "";
				switch (status) {
				case "5200":
					retstr = status+"-結算通知" ;
					break;
				case "5201":
					retstr = status+"-結算通知代理清算行" ;
					break;
				default:
					retstr = status ;
					break;
				}
				return retstr;
			}
		</script>
	</body>
</html>
