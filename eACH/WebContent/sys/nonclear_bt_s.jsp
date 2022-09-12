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
		<link rel="stylesheet" type="text/css" href="./css/jquery.timepicker.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.timepicker.min.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<style type="text/css">
			#countdownBlock{position:relative;float:right;top:-27px;background-color: #ECF1F6;padding:0px 5px}
			#dataInputTable_b table{  margin: 0 auto; border: 1px ;border-collapse: collapse;width: 60%;margin-top: 5px ;margin-left: 10px;margin-bottom: 5px;}
			#dataInputTable_b table td, #dataInputTable_b table th {border: 1px solid white;}
			#dataInputTable_b .header {text-align: right;padding-right: 20px;}
			
			#dataInputTable_c table{  margin: 0 auto; border: 1px ;border-collapse: collapse;width: 95%;margin-top: 5px ;margin-left: 10px;margin-bottom: 5px;}
			#dataInputTable_c table td, #dataInputTable_c table th {border: 1px solid white; padding: 5px}
			#dataInputTable_c .header {text-align: right;padding-right: 20px;}

			.ui-jqgrid tr.jqgrow td { white-space: normal !important; height:auto; vertical-align:text-top; padding-top:2px;}
		
		</style>
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
			
			<div  id="dataInputTable_c" style="padding-top:5px " >
				<fieldset>
					<legend>系統日期：<label id="LABEL_BIZDATE"></label>&nbsp;</legend>
				</fieldset>
			</div >
					<br>
			<div id="rsPanel" style="margin-top: 3px">
				<fieldset>
					<legend>批次作業管理列表 &nbsp;
						<label class="btn" id="btn_clearingPhase1"></label>&nbsp;
						<label class="btn" id="btn_clearingPhase2"></label>&nbsp;
					</legend>
					<div id="countdownBlock">
						剩餘 <font color="red" id="countdown"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reLoad()"><img src="./images/refresh.png"/>&nbsp;重載</label>
					</div><br>
					<table id="resultData"></table>
				</fieldset>
			</div>
			
			
			<div id="opPanel">
				<html:form styleId="formID" action="/nonclear_batch">
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
			<!-- 訊息內容對話框 -->
			<div id="msg_dialog" title="訊息內容" style="font-size: 16px ;border: 1px solid black;"  >
<%-- 				<html:textarea styleClass="text ui-widget-content ui-corner-all" styleId="MESSAGE" property="MESSAGE" style="width: 100%" rows="5"/> --%>
			<textarea id = "MESSAGE" style="width: 100%" rows="10" class="text ui-widget-content ui-corner-all" ></textarea>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	
		
		<script type="text/javascript">
		var i = 0;
		var tmpData ;
		var tmpbtnid ;
		var action = $("#formID").attr('action');
		if(window.console){console.log("window.width>>"+$(window).width()+"window.height()"+$(window).height());}
		var uri = "${pageContext.request.contextPath}"+"/secondInfo";
		var reloadUrl = "${pageContext.request.contextPath}"+"/settlement_msg.do?b=&fcid="+'<bean:write name="login_form" property="userData.s_fcid"/>';
		var gridOption;
		var countdownObj, countdownSec = 50;
		var tmpsysTime ;
		var tmpDate, tmpPhase ;
			$(document).ready(function () {
				$(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function init(){
				$("#msg_dialog").dialog({
					autoOpen: false,
					height: 300,
					width: 700,
					modal: false,
					close: function() {
// 						$("#MESSAGE").html("");
					},
					open: function() {
					}
				});
				initGrid();
// 				setTimePicker();
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
				refreshBizdateAndClrphase();
				reLoad();
				getServerTime();
			}
			
// 			伺服器時間
			function getServerTime(){
				var rdata = {component:"each_batch_bo", method:"getServerTime"  };
// 				var vResult = fstop.getServerDataExII(uri,rdata,false);
				var vResult = fstop.getServerDataExIII(uri,rdata,false,null,null,'getServerTime');
				if(fstop.isNotEmpty(vResult)){
					tmpsysTime = vResult.sysTime;
					if(window.console){console.log("vResult.sysTime>>"+vResult.sysTime);}
// 					alert(vResult.msg);
				}else{
					if(window.console){console.log("getServerTime ..異常");}
// 					alert("系統異常");
				}
			}
			
			function chgSerarchCondition(){
				var rdata = {component:"each_batch_bo", method:"chgSerarchCondition" ,tmpsysTime:tmpsysTime };
// 				var vResult = fstop.getServerDataExII(uri,rdata,false);
				var vResult = fstop.getServerDataExIII(uri,rdata,false,null,null,'chgSerarchCondition');
				if(fstop.isNotEmpty(vResult)){
					if(window.console){console.log("result>>"+vResult.result);}
					if(vResult.result =='TRUE'){
						tmpDate =""; tmpPhase = "";
						reSetTmpTime();
						if(window.console){console.log("result.msg>>"+vResult.msg);}
					}else{
						if(window.console){console.log("result.msg2>>"+vResult.msg);}
					}
					tmpsysTime = vResult.stsTime;
// 					alert(vResult.msg);
				}else{
					if(window.console){console.log("chgSerarchCondition ..異常");}
// 					alert("系統異常");
				}
	
			}
			
			
			function initGrid(){
				gridOption = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
// 		            	height: 200,
		            	height: "320px",
		            	shrinkToFit: true,
// 		            	sortable: true, req by 票交 李建利 統一改成欄位不可拖拉互換
						sortname: 'id.BATCH_PROC_SEQ',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['重新執行','作業編號','處理事項','處理狀態','處理狀態說明' ,'開始時間' ,'結束時間' , '清算階段' ,'營業日' , '批次作業類型'],
		            	colModel: [
							{name:'BTN', fixed: true, align: 'center', width:100, sortable: false},
							{name:'id.BATCH_PROC_SEQ' , index:'id.BATCH_PROC_SEQ' , fixed: true, width:60},
							{name:'BATCH_PROC_DESC' , index:'BATCH_PROC_DESC', fixed: true, width:200},
							{name:'PROC_STATUS' , index:'PROC_STATUS', fixed: true, width:60 ,formatter:this_Fmatter },
							{name:'NOTE' , index:'NOTE', fixed: true, width:250 },
							{name:'BEGIN_TIME', index:'BEGIN_TIME', fixed: true, width:170 },
							{name:'END_TIME', index:'END_TIME', fixed: true, width:170 },
							{name:'id.CLEARINGPHASE', index:'id.CLEARINGPHASE', fixed: true, width:80 },
							{name:'id.BIZDATE', index:'id.BIZDATE', fixed: true, width:100 },
							{name:'PROC_TYPE', index:'PROC_TYPE', fixed: true, width:100 ,formatter:type_Fmatter },
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						ondblClickRow: function(rowid,iRow,iCol, e){
							if(iCol == 3){
								var note = tmpData[iRow-1].NOTE;
								if(window.console){console.log("a>>"+tmpData[iRow-1].NOTE);}
								if(window.console){console.log("b>>"+note);}
								note = note.replace(/;/g,"<br>");
								$("#MESSAGE").html(note);
								$("#msg_dialog").dialog('open');
							}
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeProcessing: function(data, status, xhr){
							var seq =""; cl =""; bizdate =""; batch_proc_desc="";
							tmpData = data;
							for(var rowCount in data){
								seq = data[rowCount].id.BATCH_PROC_SEQ;
								cl = data[rowCount].id.CLEARINGPHASE;
								bizdate = data[rowCount].id.BIZDATE;
								batch_proc_desc = data[rowCount].BATCH_PROC_DESC;
								data[rowCount].BTN = '<button type="button"  id="edit_' + seq + '" onclick="doBatBySeq(this.id , \''+seq+'\' , \''+cl+'\' , \''+bizdate+'\' ,\''+batch_proc_desc+'\' )" ><img src="./images/start.png"/></button>';
// 								if(data[rowCount].id.BATCH_PROC_SEQ <= 20){
// 									data[rowCount].BTN = '<button type="button" id="bat_' + seq + '" onclick="doBatBySeq(this.id , \''+seq+'\' , \''+cl+'\' , \''+bizdate+'\' ,\''+batch_proc_desc+'\' )"><img src="./images/ic_play_circle_outline_black_18dp.png"/></button> ';
// 								}
							}
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent_Bat(data ,$("#resultData") );
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
			
			function onPut(str){
				var qStr = "";
				if(str == "searchsys"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
// 					getSearchs();
				qStr = "component=cr_line_bo&method=search_toJson&"+$("#formID").serialize();
				}else {
// 					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/secondInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
				
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}	
			
			
			
			
			
			function edit_p(str , id , id2 ,id3){
				$("#BANK_ID").val(id) ;
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
// 			單獨執行批次
			function doBatBySeq(str , id , id2 ,id3 , id4){
				var batch_proc_seq = id;
				if(confirm("確定要重新執行 "+id4+"的排程")){
					var rdata = {component:"each_batch_bo", method:"doBatBySeq" ,BIZDATE:tmpDate ,CLEARINGPHASE:tmpPhase ,BATCH_PROC_SEQ:batch_proc_seq ,action:action};
					var vResult = fstop.getServerDataExII(uri,rdata,false);
					if(fstop.isNotEmpty(vResult)){
						alert(vResult.msg);
					}else{
						alert("系統異常");
					}
					reLoad();
				}
				
			}
			
			
// 			初始化頁面中的 結果
			function initNotifyResult(){
				$("#rpt_cl_"+tmpPhase).html("尚未發佈");
				$("#rpt_"+tmpPhase).html("尚未發佈");
			}
			
			function reLoad(){
				reSearchData(tmpDate,tmpPhase , tmpbtnid);
				
			}
			function reSetTmpTime(){
				tmpDate = $('#PRE_BIZDATE').val();
				tmpPhase = $('#PRE_CLEARINGPHASE').val();
			}
			
			function reSearchData(newBizdate, newPhase , id){
				if(window.console){console.log("newBizdate>>"+newBizdate+",newPhase>>"+newPhase);}
				
				if(id=='btn_clearingPhase1'){
					$("#"+id).addClass("btn_selected");
					$("#btn_clearingPhase2").removeClass("btn_selected");
				}else if(id=='btn_clearingPhase2'){
					$("#"+id).addClass("btn_selected");
					$("#btn_clearingPhase1").removeClass("btn_selected");
				}
				if($("#"+id).attr('class')==='btn'){
					$("#"+id).addClass("btn_selected");
				}else{
					$("#"+id).remove("btn_selected");
				}
				tmpbtnid = id;
				
				tmpDate = newBizdate ;
				tmpPhase = newPhase ;
				$("#BIZDATE").val(newBizdate);
				$("#CLEARINGPHASE").val(newPhase);
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
				var qStr = "component=nonclear_batch_bo&method=search_toJson&" + $("#formID").serialize();
				if(window.console){console.log("qStr>>"+qStr);}
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				newOption.url = "/eACH/secondInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: true };
// 				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}
			
			function refreshCountdown(){
				var nextSec = parseInt($("#countdown").html()) - 1;
				if(nextSec <= 0){
					nextSec = countdownSec;
					refreshBizdateAndClrphase();
					reSearchData($("#BIZDATE").val(), $("#CLEARINGPHASE").val() , tmpbtnid);
				}
				$("#countdown").html(nextSec);
			}
			
			function startInterval(interval, func, values){
				clearInterval(countdownObj);
				countdownObj = setRepeater(func, values, interval);
			}
			
			
			
			function refreshBizdateAndClrphase(){
// 				var rdata = {component:"each_batch_bo", method:"getBusinessDateAndClrphase"};
				var rdata = {component:"nonclear_batch_bo", method:"getBusinessDateAndClrphase"};
				var date = fstop.getServerDataExII(uri, rdata, false);
// 				var date = fstop.getServerDataExIII(uri, rdata, false , null ,null,'refreshBizdateAndClrphase' );
				$("#CUR_BIZDATE").val(date.bizdate);
				$("#CUR_CLEARINGPHASE").val(date.clearingphase);
				$("#PRE_BIZDATE").val(date.pre_bizdate);
				$("#PRE_CLEARINGPHASE").val(date.pre_clearingphase);
				$("#SYSDATE").val(date.sysdate);
if(window.console){console.log("date.bizdate>>"+date.bizdate);}
				tmpDate=date.bizdate;
// 				tmpDate=date.pre_bizdate;
if(window.console){console.log("date.clearingphase>>"+date.clearingphase);}
				tmpPhase=date.pre_clearingphase;
				createLABEL();
				createBTN();
// 				reLoad();
			}
			
// 			系統日期
			function createLABEL(){
				var label = '<label id="LABEL_BIZDATE">' + $("#SYSDATE").val() + '</label>';
				if($("#LABEL_BIZDATE")){
					$("#LABEL_BIZDATE").replaceWith(label);
				}
			}
			
			function createBTN(){
				var btn = '<label class="btn" id="btn_clearingPhase1" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#PRE_CLEARINGPHASE").val() + '\' ,\'' + 'btn_clearingPhase1'+ '\')">';
// 				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#PRE_CLEARINGPHASE").val();
				btn += $("#CUR_BIZDATE").val() + ' 清算階段-' + $("#PRE_CLEARINGPHASE").val();
				btn += '</label>';
				if($("#btn_clearingPhase1")){
					$("#btn_clearingPhase1").replaceWith(btn);
					$("#btn_clearingPhase1").addClass("btn_selected");
				}
				
				btn = '<label class="btn" id="btn_clearingPhase2" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#CUR_CLEARINGPHASE").val() + '\' ,\'' + 'btn_clearingPhase2'+ '\')">';
				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#CUR_CLEARINGPHASE").val();
				btn += '</label>';
				if($("#btn_clearingPhase2")){
					$("#btn_clearingPhase2").replaceWith(btn);
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
			
// 			單獨執行批次
			function doBatBySeq(str , id , id2 ,id3 , id4){
// 				var tmpDate, tmpPhase ;
				var batch_proc_seq = id;
				if(confirm("確定要重新執行 "+id4+"的排程")){
					var rdata = {component:"nonclear_batch_bo", method:"doBatBySeq" ,BIZDATE:tmpDate ,CLEARINGPHASE:tmpPhase ,BATCH_PROC_SEQ:batch_proc_seq ,action:action};
					var vResult = fstop.getServerDataExII(uri,rdata,false);
					if(fstop.isNotEmpty(vResult)){
						alert(vResult.msg);
					}else{
						alert("系統異常");
					}
					reLoad();
				}
				
			}
			function alterMsg(){
				var msg = '<bean:write name="each_batch_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function this_Fmatter (cellvalue, options, rowObject)
			{
				var status = rowObject.PROC_STATUS;
				var retstr = "";
				switch (status) {
				case "S":
					retstr = "成功" ;
					break;
				case "F":
					retstr = "失敗" ;
					break;
				case "P":
					retstr = "處理中" ;
					break;
				default:
					retstr = "未執行" ;
					break;
				}
				return retstr;
			}
			function type_Fmatter (cellvalue, options, rowObject)
			{
				var status = rowObject.PROC_TYPE;
				var retstr = "";
				switch (status) {
				case "D":
					retstr = "日批次作業" ;
					break;
				case "M":
					retstr = "月批次作業" ;
					break;
				default:
					retstr = "未知的類型" ;
					break;
				}
				return retstr;
			}
		</script>
	</body>
</html>
