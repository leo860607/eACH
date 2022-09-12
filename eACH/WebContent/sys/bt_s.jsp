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
		
			.btnII {
			  background: #74b6e3;
			  background-image: -webkit-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: -moz-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: -ms-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: -o-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: linear-gradient(to bottom, #74b6e3, #3287b8);
			  -webkit-border-radius: 5;
			  -moz-border-radius: 5;
			  border-radius: 5px;
			  text-shadow: 1px 1px 3px #666666;
			  -webkit-box-shadow: 0px 1px 3px #666666;
			  -moz-box-shadow: 0px 1px 3px #666666;
			  box-shadow: 0px 1px 3px #666666;
			  font-family: 'Microsoft JhengHei';
			  color: #ffffff;
			  font-size: 14px;
			  padding: 4px 10px 4px 10px;
			  text-decoration: none;
			  cursor: pointer;
			}
			.btnII:hover {
			  background: #3cb0fd;
			  background-image: -webkit-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: -moz-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: -ms-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: -o-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: linear-gradient(to bottom, #3cb0fd, #3498db);
			  text-decoration: none;
			}
			
			
			.btnIII {
/* 			  background: #FFAF60; */
			  background: #FFBC55;
			  background-image: -webkit-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: -moz-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: -ms-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: -o-linear-gradient(top, #74b6e3, #3287b8);
			  background-image: linear-gradient(to bottom, #74b6e3, #3287b8);
			  -webkit-border-radius: 5;
			  -moz-border-radius: 5;
			  border-radius: 5px;
			  text-shadow: 1px 1px 3px #666666;
			  -webkit-box-shadow: 0px 1px 3px #666666;
			  -moz-box-shadow: 0px 1px 3px #666666;
			  box-shadow: 0px 1px 3px #666666;
			  font-family: 'Microsoft JhengHei';
			  color: #ffffff;
			  font-size: 14px;
			  padding: 4px 10px 4px 10px;
			  text-decoration: none;
			  cursor: pointer;
			}
			.btnIII:hover {
			  background: #EA7500;
			  background-image: -webkit-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: -moz-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: -ms-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: -o-linear-gradient(top, #3cb0fd, #3498db);
			  background-image: linear-gradient(to bottom, #3cb0fd, #3498db);
			  text-decoration: none;
			}
		
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
					<table  >
					<tr>
						<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
						<td>
							<table   border="1px"  style="border: black">
								<tr>
									<td class="header">
									作業編號1-20
									</td> 
								</tr>
								<tr>
									<td class="header">
											<label class="btnIII" id="gw_exe" onclick="gw_exe(this.id)"><img src="./images/start.png"/>&nbsp;起始點開始執行</label>
									</td> 
								</tr>
								<tr>
									<td class="header">
											<label class="btnIII" id="gw_exe_break" onclick="gw_exe(this.id)"><img src="./images/ic_restore_black.png"/>&nbsp;中斷點開始執行</label>
									</td> 
								</tr>
							</table>
						</td>
						</logic:equal>
						<td>
							<table   border="1px" style="border: black;">
								<tr>
									<td class="header">營業日</td> <td>清算階段</td><td>結算通知 </td><td>報表及檔案作業通知 </td>
								</tr>
								<tr>
									<td class="header" id="prebzdate"></td> <td> 01</td><td id="rpt_cl_01">尚未發佈</td><td id="rpt_01">尚未發佈</td>
								</tr>
								<tr>
									<td class="header" id="prebzdate"></td> <td> 02</td><td id="rpt_cl_02">尚未發佈</td><td id="rpt_02">尚未發佈</td>
								</tr>
							</table>
						</td>
						<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
						<td>
							<table  style="border: black;">
								<tr>
									<td class="header">作業編號21-70</td> 
								</tr>
								<tr>
									<td class="header">
											<label class="btn" id="exe" onclick="exe(this.id)"><img src="./images/start.png"/>&nbsp;起始點開始執行</label>
									</td> 
								</tr>
								<tr>
									<td class="header">
											<label class="btn" id="exe_break" onclick="exe(this.id)"><img src="./images/ic_restore_black.png"/>&nbsp;中斷點開始執行</label>
									</td> 
								</tr>
							</table>
						</td>
						</logic:equal>
					</tr>
					</table>
					
					
					
				</fieldset>
			</div >
<!-- 					<div id="countdownBlock"  > -->
<!-- 						<label class="btn" id="notify_data" onclick="getNotify_data(this.id)"><img src="./images/search.png"/>&nbsp;結算通知結果</label> -->
<!-- 						<label class="btn" id="exe_break" onclick="exe(this.id)"><img src="./images/ic_restore_black.png"/>&nbsp;中斷點開始執行</label> -->
<!-- 						<label class="btn" id="exe" onclick="exe(this.id)"><img src="./images/start.png"/>&nbsp;起始點開始執行</label> -->
<!-- 					</div> -->
					<br>
			<div id="rsPanel" style="margin-top: 3px">
				<fieldset>
					<legend>批次作業管理列表 &nbsp;
<!-- 					<label class="btn" id="cl1bzdate" onclick="searchsys(this.id)"><img src="./images/search.png"/>&nbsp;第一清算階段</label> -->
<!-- 					<label class="btn" id="cl2bzdate" onclick="searchsys(this.id)"><img src="./images/search.png"/>&nbsp;第二清算階段</label> -->
						<label class="btn" id="btn_clearingPhase1"></label>&nbsp;
						<label class="btn" id="btn_clearingPhase2"></label>&nbsp;
						<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
						<label class="btn" id="notify_cl" onclick="notify_cl(this.id)"><img src="./images/ic_notify.png"/>&nbsp;結算通知</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<label class="btn" id="notify" onclick="notify_all(this.id)"><img src="./images/ic_notify.png"/>&nbsp;報表及檔案作業通知</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<label class="btnII" id="notify_data" onclick="getNotify_data(this.id)"><img src="./images/search.png"/>&nbsp;結算通知結果</label>
						</logic:equal>
					</legend>
					<div id="countdownBlock">
<!-- 						剩餘 <font color="red" id="countdown"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reSearchData($('#CUR_BIZDATE').val(),$('#CUR_CLEARINGPHASE').val())"><img src="./images/refresh.png"/>&nbsp;重載</label> -->
						剩餘 <font color="red" id="countdown"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reLoad()"><img src="./images/refresh.png"/>&nbsp;重載</label>
					</div><br>
					<table id="resultData"></table>
				</fieldset>
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
					<fieldset>
						<legend>排程設定</legend>
						<table>
							<tr>
								<td class="header" style="width: 20%">清算階段 &nbsp;01&nbsp;啟動時間</td>
								<td style="width: 30%"> <html:text styleId="RP_CLEARPHASE1_TIME" property="RP_CLEARPHASE1_TIME" size="8" maxlength="8" ></html:text></td>
								<td class="necessary">主機 &nbsp;AP1 &nbsp;<html:text styleId="AP1" property="AP1" size="16" maxlength="16" styleClass="validate[required,custom[ipv4]]" ></html:text>
								是否啟動
								<html:radio styleId="AP1_ISRUN_Y" property="AP1_ISRUN" value="Y" onclick="rest_oth(this.id)">是</html:radio> 
								<html:radio styleId="AP1_ISRUN_N" property="AP1_ISRUN" value="N" >否</html:radio>
								</td>
							</tr>
							<tr>
								<td class="header">清算階段 &nbsp;02&nbsp;啟動時間</td>
								<td> <html:text styleId="RP_CLEARPHASE2_TIME" property="RP_CLEARPHASE2_TIME" size="8" maxlength="8"></html:text></td>
								<td class="necessary">主機 &nbsp;AP2 &nbsp;<html:text styleId="AP2" property="AP2" size="16" maxlength="16" styleClass="validate[required,custom[ipv4]]" ></html:text>
								是否啟動
								<html:radio styleId="AP2_ISRUN_Y" property="AP2_ISRUN" value="Y" onclick="rest_oth(this.id)">是</html:radio> 
								<html:radio styleId="AP2_ISRUN_N" property="AP2_ISRUN" value="N">否</html:radio> 
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="savesys" onclick ="savesys(this.id);"><img src="./images/save.png"/>&nbsp;儲存</label>
								</logic:equal>
								</td>
							</tr>
							
						</table>
					</fieldset>
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
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
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
				setTimePicker();
				searchsys('searchsys');
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
// 				tmpDate =tmpPhase = 
// 				tmpsysTime 
				var rdata = {component:"each_batch_bo", method:"chgSerarchCondition" ,tmpsysTime:tmpsysTime };
// 				var vResult = fstop.getServerDataExII(uri,rdata,false);
				var vResult = fstop.getServerDataExIII(uri,rdata,false,null,null,'chgSerarchCondition');
				if(fstop.isNotEmpty(vResult)){
					if(window.console){console.log("result>>"+vResult.result);}
					if(vResult.result =='TRUE'){
						tmpDate =""; tmpPhase = "";
						searchsys('searchsys');
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
			function setTimePicker(){
				if(window.console){console.log("setTimePicker1");}
				$("#RP_CLEARPHASE1_TIME").timepicker({ 'timeFormat': 'H:i:s' });
				$("#RP_CLEARPHASE2_TIME").timepicker({ 'timeFormat': 'H:i:s' });
				if(window.console){console.log("setTimePicker2");}
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
// 		            	colNames:['單一作業處理','作業編號','處理事項','處理狀態','處理狀態說明' ,'開始時間' ,'結束時間' , '清算階段' ,'營業日' , '批次作業類型'],
		            	colNames:['作業編號','處理事項','處理狀態','處理狀態說明' ,'開始時間' ,'結束時間' , '清算階段' ,'營業日' , '批次作業類型'],
		            	colModel: [
// 		            	    {name:'BTN', fixed: true, align: 'center', width:100, sortable: false},
							{name:'id.BATCH_PROC_SEQ' , index:'id.BATCH_PROC_SEQ' , fixed: true, width:60},
// 							{name:'BATCH_PROC_NAME', fixed: true, width:120},
							{name:'BATCH_PROC_DESC' , index:'BATCH_PROC_DESC', fixed: true, width:150},
							{name:'PROC_STATUS' , index:'PROC_STATUS', fixed: true, width:60 ,formatter:this_Fmatter },
// 							{name:'NOTE', fixed: true, width:150 ,cellattr:setCell},
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
// 							var rowData = jQuery(this).getRowData(rowid); 
							if(iCol == 3){
// 								var note = rowData["NOTE"];
								var note = tmpData[iRow-1].NOTE;
								if(window.console){console.log("a>>"+tmpData[iRow-1].NOTE);}
								if(window.console){console.log("b>>"+note);}
// 								note = note.replace(";","\n");
								note = note.replace(/;/g,"<br>");
// 								note = note.replace(",","\n");
// 								$("#msg_dialog").html(note);
// 								$("#MESSAGE").html("");
								$("#MESSAGE").html(note);
								$("#msg_dialog").dialog('open');
							}
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
							if(i!=0){
								getNotifyResult();
							}
							i=1;
						},
						beforeProcessing: function(data, status, xhr){
							var seq =""; cl =""; bizdate =""; batch_proc_desc="";
							tmpData = data;
							for(var rowCount in data){
								seq = data[rowCount].id.BATCH_PROC_SEQ;
								cl = data[rowCount].id.CLEARINGPHASE;
								bizdate = data[rowCount].id.BIZDATE;
								batch_proc_desc = data[rowCount].BATCH_PROC_DESC;
// 								data[rowCount].BTN = '<button type="button" disabled="disabled" id="edit_' + seq + '" onclick="notify_cl(this.id , \''+seq+'\' , \''+cl+'\' , \''+bizdate+'\'  )" ><img src="./images/ic_notify.png"/></button>';
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
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
				
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
// 				getNotifyResult();
			}	
			
			
			function getNotify_data(str){
// 				tmpDate =tmpPhase = 
// 				var url ="${pageContext.request.contextPath}"+"/sys/bt_notify_rs.jsp";
				var url ="${pageContext.request.contextPath}"+"/each_batch.do?ac_key=notify&BIZDATE="+tmpDate+"&CLEARINGPHASE="+tmpPhase+"";
				window.open(url);
			}
			
			
			function add_p(str){
				cleanFormII();
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				$("#BANK_ID").val(id) ;
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
// 			單獨執行批次
			function doBatBySeq(str , id , id2 ,id3 , id4){
// 				var tmpDate, tmpPhase ;
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
// 			結算通知
			function notify_cl(str , id , id2 ,id3){
// 				var tmpDate, tmpPhase ;
				var batch_proc_seq = id;
				if(confirm("確定執行結算通知?")){
					initNotifyResult();
					var rdata = {component:"each_batch_bo", method:"doCLNotify" ,BIZDATE:tmpDate ,CLEARINGPHASE:tmpPhase ,BATCH_PROC_SEQ:'9' ,action :action};
					var vResult = fstop.getServerDataExII(uri,rdata,false);
					if(fstop.isNotEmpty(vResult)){
						alert(vResult.msg);
// 						getNotifyResult();
						reLoad();
						
					}else{
						alert("系統異常");
					}
				}
			}
			function notify_all(str , id , id2 ,id3){
				if(confirm("報表及檔案作業通知?")){
					initNotifyResult();
					var rdata = {component:"each_batch_bo", method:"doNotify" ,BIZDATE:tmpDate ,CLEARINGPHASE:tmpPhase ,action :action };
					var vResult = fstop.getServerDataExII(uri,rdata,false);
					if(fstop.isNotEmpty(vResult)){
						alert(vResult.msg);
// 						getNotifyResult();
						reLoad();
					}else{
						alert("系統異常");
					}
				}
			}
			function getNotifyResult(){
				var rdata = {component:"each_batch_bo", method:"getNotifyResult" ,BIZDATE:tmpDate ,CLEARINGPHASE:tmpPhase  };
// 				var vResult = fstop.getServerDataExII(uri,rdata,false);
				var vResult = fstop.getServerDataExIII(uri,rdata,false , null ,null, 'getNotifyResult');
				if(fstop.isNotEmpty(vResult)){
					if(window.console){console.log("vResult"+vResult);}
					for(var index in vResult ){
						if(window.console){console.log("index>>"+index);}
						if(vResult[index].CLEAR_NOTIFY =="Y"){
							$("#rpt_cl_"+vResult[index].id.CLEARINGPHASE).html("成功");
						}
						else if(vResult[index].CLEAR_NOTIFY =="P")	{
							$("#rpt_cl_"+vResult[index].id.CLEARINGPHASE).html("結算通知執行中");
						}
						else if(vResult[index].CLEAR_NOTIFY =="N")	{
							$("#rpt_cl_"+vResult[index].id.CLEARINGPHASE).html("失敗");
						}				
						else{
							$("#rpt_cl_"+vResult[index].id.CLEARINGPHASE).html("尚未發佈");
						}
						if(vResult[index].RPT_NOTIFY =="Y"){
							$("#rpt_"+vResult[index].id.CLEARINGPHASE).html("成功");
						}
						else if(vResult[index].RPT_NOTIFY =="N")	{
							$("#rpt_"+vResult[index].id.CLEARINGPHASE).html("失敗");
						}	
						else{
							$("#rpt_"+vResult[index].id.CLEARINGPHASE).html("尚未發佈");
						}
					}
// 					立即更新右上角公告訊息
					getPublicData();
				}else{
					alert("系統異常");
				}
				$("td[id^=prebzdate]").html(tmpDate);
			}
			
			
// 			初始化頁面中的 結果
			function initNotifyResult(){
				$("#rpt_cl_"+tmpPhase).html("尚未發佈");
				$("#rpt_"+tmpPhase).html("尚未發佈");
			}
			
			function reLoad(){
				if(fstop.isEmptyString(tmpDate)){
					tmpDate = $('#PRE_BIZDATE').val();
					$("td[id^=prebzdate]").html(tmpDate);
				}
				if(fstop.isEmptyString(tmpPhase)){
// 					tmpPhase = $('#CUR_CLEARINGPHASE').val();
					tmpPhase = $('#PRE_CLEARINGPHASE').val();
				}
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
				chgSerarchCondition();
				var qStr = "component=each_batch_bo&method=search_toJson2&" + $("#formID").serialize();
				if(window.console){console.log("qStr>>"+qStr);}
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: true };
// 				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
				getNotifyResult();
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
			
// 			查詢排程時間
			function searchsys(str){
				var rdata = {component:"each_batch_bo", method:"searchSYS" };
// 				var vResult = fstop.getServerDataExII(uri,rdata,false);
				var vResult = fstop.getServerDataExIII(uri,rdata,false,null,null,'searchsys');
// 				if(window.console){console.log("vResult"+vResult);}
				if(fstop.isNotEmpty(vResult)){
					for(var i in vResult){
// 				if(window.console){console.log("vResult.RP_CLEARPHASE1_TIME>>"+vResult[i].RP_CLEARPHASE1_TIME);}
// 				if(window.console){console.log("vResult.AP1_ISRIN>>"+vResult[i].AP1_ISRUN);}
// 				if(window.console){console.log("vResult.AP2_ISRIN>>"+vResult[i].AP2_ISRUN);}
						
						$("#RP_CLEARPHASE1_TIME").val(vResult[i].RP_CLEARPHASE1_TIME); 
						$("#RP_CLEARPHASE2_TIME").val(vResult[i].RP_CLEARPHASE2_TIME); 
						$("#AP1").val(vResult[i].AP1); 
						$("#AP2").val(vResult[i].AP2);
						if(vResult[i].AP1_ISRUN == "Y" || vResult[i].AP1_ISRUN ==""){
							$("#AP1_ISRUN_Y").prop('checked', true);
// 							$("input[name=TXN_CHECK_TYPE]").attr("disabled", true);
						}else{
							$("#AP1_ISRUN_N").prop('checked', true);
						}
						if(vResult[i].AP2_ISRUN == "Y" || vResult[i].AP1_ISRUN==""){
							$("#AP2_ISRUN_Y").prop('checked', true);
// 							$("input[name=TXN_CHECK_TYPE]").attr("disabled", true);
						}else{
							$("#AP2_ISRUN_N").prop('checked', true);
						}
					}
				}else{
					alert("系統異常");
				}	
			}
			
// 			radio "是"只能單選，否可複選
			function rest_oth(str){
				switch (str) {
				case 'AP1_ISRUN_Y':
					$("#AP2_ISRUN_N").prop('checked', true);
					break;
				case 'AP2_ISRUN_Y':
					$("#AP1_ISRUN_N").prop('checked', true);
					break;
				default:
					break;
				}
			}
			
// 			異動排程時間 
			function savesys(str){
// 	if(window.console){console.log("ap1>>"+$("#AP1").val());}
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				var rdata = {component:"each_batch_bo", method:"saveSYS" , RP_CLEARPHASE1_TIME:$("#RP_CLEARPHASE1_TIME").val() , RP_CLEARPHASE2_TIME: $("#RP_CLEARPHASE2_TIME").val() ,AP1:$("#AP1").val(),AP2:$("#AP2").val() , AP1_ISRUN: $('input[name=AP1_ISRUN]:checked').val(),AP2_ISRUN:$('input[name=AP2_ISRUN]:checked').val(),action:action};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
// 				if(window.console){console.log("vResult"+vResult);}
// 				if(window.console){console.log("vResult.RP_CLEARPHASE1_TIME>>"+vResult.RP_CLEARPHASE1_TIME);}
				if(fstop.isNotEmpty(vResult)){
					alert(vResult.msg);
				}else{
					alert("系統異常");
				}
				searchsys('searchsys');
			}
			
// 			手動執行
			function exe(str){
				blockUI();
				var com = false;
				var rdata = {};
				if(str == 'exe'){
					if(confirm("確定要從起始點開始執行?")){
						rdata = {component:"each_batch_bo", method:"exeStartPoint" , BIZDATE: tmpDate , CLEARINGPHASE :tmpPhase , action:action};
						com=true;
					};
				}else{
					if(window.console){console.log("tmpDate>>"+tmpDate);}
					if(confirm("確定要從中斷點開始執行?")){
						rdata = {component:"each_batch_bo", method:"exeBreakPoint" , BIZDATE: tmpDate , CLEARINGPHASE :tmpPhase , action:action};
						com=true;
					};
				}
				if(com){
					setTimeout(function(){
						fstop.getServerDataExII(uri, rdata, false , showExeMsg );
					},1000);
				}else{
					unblockUI();
				}
				
			}
			function gw_exe(str){
				blockUI();
				var com = false;
				var rdata = {};
				if(str == 'gw_exe'){
					if(confirm("確定要從起始點開始執行?")){
						rdata = {component:"each_batch_bo", method:"exeGW_Bat" , BIZDATE: tmpDate , CLEARINGPHASE :tmpPhase , action:action , type:"00"};
						com=true;
					};
				}else{
					if(confirm("確定要從中斷點開始執行?")){
						rdata = {component:"each_batch_bo", method:"exeGW_Bat" , BIZDATE: tmpDate , CLEARINGPHASE :tmpPhase , action:action , type:"01"};
						com=true;
					};
				}
				if(com){
					setTimeout(function(){
						fstop.getServerDataExII(uri, rdata, false , showExeMsg );
					},1000);
				}else{
					unblockUI();
				}
				
			}
			
			function showExeMsg(vResult){
				if(fstop.isNotEmpty(vResult)){
					unblockUI();
					alert(vResult.msg);
				}else{
					unblockUI();
					alert("系統異常");
				}
				reLoad();
				getNotifyResult();
			}
			
			function refreshBizdateAndClrphase(){
// 				var uri = "/eACH/baseInfo?component=monitor_biz_bo&method=getBusinessDateAndClrphase";
				
				var rdata = {component:"each_batch_bo", method:"getBusinessDateAndClrphase"};
// 				var date = fstop.getServerDataExII(uri, rdata, false);
				var date = fstop.getServerDataExIII(uri, rdata, false , null ,null,'refreshBizdateAndClrphase' );
				$("#CUR_BIZDATE").val(date.bizdate);
				$("#CUR_CLEARINGPHASE").val(date.clearingphase);
				$("#PRE_BIZDATE").val(date.pre_bizdate);
				$("#PRE_CLEARINGPHASE").val(date.pre_clearingphase);
				$("#SYSDATE").val(date.sysdate);
				
				createLABEL();
				createBTN();
			}
			
			function createLABEL(){
// 				var label = '<label id="LABEL_BIZDATE">' + $("#CUR_BIZDATE").val() + '</label>';
				var label = '<label id="LABEL_BIZDATE">' + $("#SYSDATE").val() + '</label>';
				if($("#LABEL_BIZDATE")){
					$("#LABEL_BIZDATE").replaceWith(label);
				}
			}
			
			function createBTN(){
// 				var btn = '<label class="btn" id="btn_clearingPhase1" onclick="reSearchData(\'' + $("#CUR_BIZDATE").val() + '\',\'' + $("#CUR_CLEARINGPHASE").val() + '\')">';
// 				btn += $("#CUR_BIZDATE").val() + ' 清算階段-' + $("#CUR_CLEARINGPHASE").val();
// 因為是報表批次 所以抓PRE_BIZDATE即可
// 				var btn = '<label class="btn" id="btn_clearingPhase1" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#CUR_CLEARINGPHASE").val() + '\')">';
				var btn = '<label class="btn" id="btn_clearingPhase1" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#PRE_CLEARINGPHASE").val() + '\' ,\'' + 'btn_clearingPhase1'+ '\')">';
// 				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#CUR_CLEARINGPHASE").val();
				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#PRE_CLEARINGPHASE").val();
				btn += '</label>';
				if($("#btn_clearingPhase1")){
					$("#btn_clearingPhase1").replaceWith(btn);
					$("#btn_clearingPhase1").addClass("btn_selected");
				}
				
// 				btn = '<label class="btn" id="btn_clearingPhase2" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#PRE_CLEARINGPHASE").val() + '\')">';
				btn = '<label class="btn" id="btn_clearingPhase2" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#CUR_CLEARINGPHASE").val() + '\' ,\'' + 'btn_clearingPhase2'+ '\')">';
// 				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#PRE_CLEARINGPHASE").val();
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
