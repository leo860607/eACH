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
				<a href="#">結算通知結果</a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/each_batch">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="CUR_BIZDATE" styleId="CUR_BIZDATE" />
					<html:hidden property="CUR_CLEARINGPHASE" styleId="CUR_CLEARINGPHASE" />
					<html:hidden property="PRE_BIZDATE" styleId="PRE_BIZDATE" />
					<html:hidden property="PRE_CLEARINGPHASE" styleId="PRE_CLEARINGPHASE" />
					<html:hidden property="BIZDATE" styleId="BIZDATE" />
					<html:hidden property="CLEARINGPHASE" styleId="CLEARINGPHASE" />
					<html:hidden property="SYSDATE" styleId="SYSDATE" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
				</html:form>
			</div>
<!-- 			<div id="opPanel" style="padding-top:5px"> -->
<!-- 				<fieldset> -->
<!-- 					系統日期：<label id="LABEL_BIZDATE"></label>&nbsp; -->
<!-- 				</fieldset> -->
<!-- 			</div> -->
<!-- 					<div id="countdownBlock"> -->
<!-- 						<label class="btn" id="notify_data" onclick="getNotify_data(this.id)"><img src="./images/search.png"/>&nbsp;結算通知結果</label> -->
<!-- 						<label class="btn" id="exe_break" onclick="exe(this.id)"><img src="./images/ic_restore_black.png"/>&nbsp;中斷點開始執行</label> -->
<!-- 						<label class="btn" id="exe" onclick="exe(this.id)"><img src="./images/start.png"/>&nbsp;起始點開始執行</label> -->
<!-- 					</div> -->
<!-- 					<br> -->
			<div id="rsPanel" style="margin-top: 3px">
				<fieldset>
					<legend>結算通知結果 &nbsp;
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
				onPut('<bean:write name="each_batch_form" property="BIZDATE"/>','<bean:write name="each_batch_form" property="CLEARINGPHASE"/>');
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
// 		            	colNames:['交易代碼','收受通知銀行','清算階段','營業日','交易時間' ,'測試'],
		            	colNames:['交易代碼','收受通知銀行','清算階段','營業日','交易時間' ],
		            	colModel: [
// 		            	    {name:'BTN', fixed: true, align: 'center', width:70, sortable: false},
							{name:'PCODE', fixed: true, width:160 ,formatter:this_Fmatter},
							{name:'RECEIVERBANK', fixed: true, width:160},
							{name:'CLEARINGPHASE', fixed: true, width:60},
							{name:'BIZDATE', fixed: true, width:120 },
// 							{name:'BIZDATE', fixed: true, autowidth: true },
							{name:'TXTIME', fixed: true, width:160 },
// 							{name:'TX_TEST', fixed: true, width:120 },
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
				var qStr = "component=each_batch_bo&method=getNotifyData&BIZDATE="+bizdate+"&CLEARINGPHASE="+clearingphase+"";
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
			
			
			function reSearchData(newBizdate, newPhase){
				if(window.console){console.log("newBizdate>>"+newBizdate+",newPhase>>"+newPhase);}
				tmpDate = newBizdate ;
				tmpPhase = newPhase ;
				$("#BIZDATE").val(newBizdate);
				$("#CLEARINGPHASE").val(newPhase);
				$("#countdown").html(countdownSec);
				var qStr = "component=each_batch_bo&method=search_toJson&" + $("#formID").serialize();
				if(window.console){console.log("qStr>>"+qStr);}
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
// 				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}
			
			
			function createBTN(){
				var btn = '<label class="btn" id="btn_clearingPhase1" >';
// 				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#PRE_CLEARINGPHASE").val();
				btn += '<bean:write name="each_batch_form" property="BIZDATE"/>' + ' 清算階段-' + '<bean:write name="each_batch_form" property="CLEARINGPHASE"/>';
				btn += '</label>';
				if($("#btn_clearingPhase1")){
					$("#btn_clearingPhase1").replaceWith(btn);
				}
				
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
				var msg = '<bean:write name="each_batch_form" property="msg"/>';
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
