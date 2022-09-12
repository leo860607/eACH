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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-結算通知結果</title>
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
				<a href="#">整批未完成交易</a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/monitor">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="BIZDATE" styleId="BIZDATE" />
					<html:hidden property="CLEARINGPHASE" styleId="CLEARINGPHASE" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
				</html:form>
			</div>
			<div id="rsPanel" style="margin-top: 3px">
				<fieldset>
					<legend>整批未完成交易&nbsp;
						<label class="btn" id="btn_clearingPhase1"></label>&nbsp;
					</legend>
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
// 				initSelect();
				createBTN();
				onPut('<bean:write name="monitor_form" property="BIZDATE"/>','<bean:write name="monitor_form" property="CLEARINGPHASE"/>');
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
						sortname: 'BATCH_PROC_SEQ',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['營業日期','操作行代號','未完成筆數' ],
		            	colModel: [
// 		            	    {name:'BTN', fixed: true, align: 'center', width:70, sortable: false},
							{name:'FLBIZDATE', fixed: true, width:160 },
							{name:'SENDERACQUIRE', fixed: true, width:160},
							{name:'NUM', fixed: true, width:60},
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
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if($("#ac_key").val() == 'save' || $("#ac_key").val() == 'update'  ){
					initSearchs();
					onPut();
// 					alterMsg();
				}else if($("#ac_key").val() == 'back'){
					initSearchs();
					onPut('search');
// 					$("#ac_key").val("");
				}else{
					onPut('search');
				}
			}
			
			function onPut(bizdate , clearingphase){
				var qStr = "component=monitor_bo&method=getPendingData&BIZDATE="+bizdate+"&CLEARINGPHASE="+clearingphase+"";
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
			
			
			function createBTN(){
				var btn = '<label class="btn" id="btn_clearingPhase1" >';
				btn += '<bean:write name="monitor_form" property="BIZDATE"/>' + ' 清算階段-' + '<bean:write name="monitor_form" property="CLEARINGPHASE"/>';
				btn += '</label>';
				if($("#btn_clearingPhase1")){
					$("#btn_clearingPhase1").replaceWith(btn);
				}
				
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
			
			
			
			function alterMsg(){
				var msg = '<bean:write name="monitor_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
		</script>
	</body>
</html>
