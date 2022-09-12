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
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			#countdownBlock{position:relative;float:right;top:-20px;background-color: #ECF1F6;padding:0px 5px}
		</style>
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
			<html:form styleId="formID" action="/monitor_biz">
				<html:hidden property="ac_key" styleId="ac_key" value=""/>
				<html:hidden property="target" styleId="target" value=""/>
				<html:hidden property="serchStrs" styleId="serchStrs" />
				<html:hidden property="USER_COMPANY2" styleId="USER_COMPANY2" value=""/>
				<html:hidden property="BIZDATE" styleId="BIZDATE"/>
				<html:hidden property="CLEARINGPHASE" styleId="CLEARINGPHASE"/>
				<html:hidden property="CUR_BIZDATE" styleId="CUR_BIZDATE"/>
				<html:hidden property="CUR_CLEARINGPHASE" styleId="CUR_CLEARINGPHASE"/>
				<html:hidden property="PRE_BIZDATE" styleId="PRE_BIZDATE"/>
				<html:hidden property="PRE_CLEARINGPHASE" styleId="PRE_CLEARINGPHASE"/>
				<div id="opPanel" style="padding-top:5px">
					<fieldset>
						營業日期：<label id="LABEL_BIZDATE"></label>
					</fieldset>
				</div>
				
				<div id="alertArea" style="padding-top:5px" hidden>
					<fieldset>
						<legend style="color: red">警示訊息</legend>
						<strong>
						<font id="pendingText" color=red></font>
						<br>
						<font id="timeoutText" color=red></font><br>
						<font color=red>煩請連絡相關人員!</font>
						</strong>
					</fieldset>
				</div>
				
				<div id="rsPanel" style="margin-top: 5px">
					<fieldset>
						<legend>
							查詢結果&nbsp;
							<%--<label class="btn" id="btn_clearingPhaseAll" onclick="reSearchData('')">全部</label> --%>
							<label class="btn" id="btn_clearingPhase1"></label>&nbsp;
							<label class="btn" id="btn_clearingPhase2"></label>
						</legend>
						<div id="countdownBlock">
							剩餘 <font color="red" id="countdown"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reSearchData($('#CUR_BIZDATE').val(),$('#CUR_CLEARINGPHASE').val())"><img src="./images/refresh.png"/>&nbsp;重載</label>
						</div><br>
						<table id="resultData"></table>
					</fieldset>
				</div>
				
				<div id="settingPanel" style="margin-top: 5px">
					<fieldset>
						<legend>
							參數設定
						</legend>
						未完成警示設定：<input type="text" id="pending" maxlength="5" size="5" style="margin-top: 5px" >筆
						<br>
						逾時警示設定：<input type="text" id="timeout" maxlength="5" size="5" style="margin-top: 5px">筆
					</fieldset>
				</div>
			</html:form>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption;
			var countdownObj, countdownSec = 60;
			var alertMsgPending="";
			var alertMsgTimeOut="";
			$(document).ready(function(){
				
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				$('#pending').val("100");
				$('#timeout').val("20")
				alterMsg();
				initGridOption();
				initGrid();
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					$("#USER_COMPANY2").val('<bean:write name="login_form" property="userCompany"/>');
				</logic:equal>
				//初始化查詢條件的營業日、清算階段
				$("#BIZDATE").val($("#CUR_BIZDATE").val());
				$("#CLEARINGPHASE").val($("#CUR_CLEARINGPHASE").val());
				
				createLABEL();
				createBTN();
				
				searchData();
				
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="monitor_biz_form" property="msg"/>';
				if(msg != ""){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						//toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 400,
		            	sorttype:"text",
		            	sortname: 'BANKID',
		            	shrinkToFit: true,
		            	gridview: true,
		            	rowNum: -1,
						colNames:['銀行代號','系統狀態','成功交易筆數','失敗交易筆數','成功交易筆數','失敗交易筆數','成功交易筆數','失敗交易筆數','逾時交易筆數','未完成交易筆數','沖正交易筆數','待處理筆數','總筆數','剩餘額度','應用系統狀態'],
		            	colModel: [
							{name:'BANKID',index:'BANKID',fixed:true,width: 200},
							{name:'MEMBANKSSYSTS',index:'MEMBANKSSYSTS',fixed:true,width: 80},
							{name:'FIRE_SUCCESS_COUNT',index:'FIRE_SUCCESS_COUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FIRE_FAIL_COUNT',index:'FIRE_FAIL_COUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'OUT_SUCCESS_COUNT',index:'OUT_SUCCESS_COUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'OUT_FAIL_COUNT',index:'OUT_FAIL_COUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'IN_SUCCESS_COUNT',index:'IN_SUCCESS_COUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'IN_FAIL_COUNT',index:'IN_FAIL_COUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TIMEOUTCOUNT',index:'TIMEOUTCOUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PENDTCOUNT_1',index:'PENDTCOUNT_1',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ACCTCOUNT',index:'ACCTCOUNT',fixed:true,width: 100,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PENDTCOUNT_2',index:'PENDTCOUNT_2',fixed:true,width: 80,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TOTALCOUNT',index:'TOTALCOUNT',fixed:true,width: 80,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'REST_CR_LINE',index:'REST_CR_LINE',fixed:true,width: 120,align:'right',formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'MEMBANKAPPSTS',index:'MEMBANKAPPSTS',fixed:true,width: 100}
						],
		            	gridComplete:function (){
		            		$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
		            		$("#resultData .jqgrow:even").addClass('resultDataRowEven');
		            		
						},
						rowattr: function (rd) {
// 							20150530 edit by hugo req by UAT-20150519-05
// 						    if (rd.MEMBANKAPPSTS != '簽到') {
// 						        return {"class": "resultDataRowError"};
// 						    }
							var pendingSet = $("#pending").val();
							var timeoutSet = $("#timeout").val();
							var pendingCount = rd.PENDTCOUNT_1;
							var timeoutCount = rd.TIMEOUTCOUNT;
// 							rd.MEMBANKAPPSTS = '簽到';
							//先把提示區的條件濾掉
						    if ((parseInt(pendingCount) < parseInt(pendingSet) && parseInt(timeoutCount) < parseInt(timeoutSet)) &&
						    		rd.MEMBANKAPPSTS != '簽到' || rd.MEMBANKSSYSTS == '關機' || rd.MEMBANKSSYSTS == '簽退') {
						        return {"class": "resultDataRowError"};
						    }
// 							alert("pendingSet:"+pendingSet);
// 							alert("timeoutSet:"+timeoutSet);
// 							alert("pendingCount:"+pendingCount);
// 							alert("timeoutCount:"+timeoutCount);
						    if (parseInt(pendingCount) >= parseInt(pendingSet) || parseInt(timeoutCount) >= parseInt(timeoutSet)) {
						    	
						    	if(parseInt(pendingCount) >= parseInt(pendingSet) ){
						    		alertMsgPending = alertMsgPending + rd.BANKID + " 未完成交易已超過"+pendingSet+"筆！("+pendingCount+")<br>";
						    	}
						    	
						    	if ( parseInt(timeoutCount) >= parseInt(timeoutSet) ){
						    		alertMsgTimeOut = alertMsgTimeOut + rd.BANKID + " 逾時交易筆數易已超過"+timeoutSet+"筆！("+timeoutCount+")<br>";
						    	}
						        return {"class": "resultDataRowError2"};
						    }
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var list = data.rows;
							for(var rowCount in list){
								list[rowCount].FIRE_DEBIT_SAVE_COUNT = list[rowCount].FIRECOUNT + '/' + list[rowCount].SAVECOUNT + '/' + list[rowCount].DEBITCOUNT;
							}
						},
						loadComplete: function(data){ 
							if(alertMsgPending!=''||alertMsgTimeOut!=''){
								$("#alertArea").show();
								if(alertMsgPending!=''){
									$("#pendingText").html(alertMsgPending);
								}else{
									$("#pendingText").html("");
								}
								if(alertMsgTimeOut!=''){
									$("#timeoutText").html(alertMsgTimeOut);
								}else{
									$("#timeoutText").html("");
								}
							}else{
								$('#alertArea').hide();
							}
						},
	 					loadtext: "處理中..."
	 					//,pageText: "{0} / {1}"
				};
			}
			
			
			function initGrid(){
				$("#resultData").jqGrid(gridOption);
			}
			
			function searchData(){
				getSearchs();
				$("#resultData").jqGrid('GridUnload');
				var qStr = "component=monitor_biz_bo&method=pageSearch&" + $("#formID").serialize();
				var newOption = gridOption;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				$("#resultData").jqGrid(newOption);
				groupGridHeaders();
				
			}
			
			function groupGridHeaders(){
				$("#resultData").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'FIRE_SUCCESS_COUNT', numberOfColumns: 2, titleText: '發動行'},
						{startColumnName: 'OUT_SUCCESS_COUNT', numberOfColumns: 2, titleText: '扣款行'},
						{startColumnName: 'IN_SUCCESS_COUNT', numberOfColumns: 2, titleText: '入帳行'}
					  ]
				});
			}
			
			function reSearchData(newBizdate, newPhase , id){
				alertMsgPending="";
				alertMsgTimeOut="";
				$("pendingText").html("");
				$("timoutText").html("");
// 				alert("id>>"+id);
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
				$("#BIZDATE").val(newBizdate);
				$("#CLEARINGPHASE").val(newPhase);
				
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
				
				var qStr = "component=monitor_biz_bo&method=pageSearch2&" + $("#formID").serialize();
				$("#resultData").jqGrid('setGridParam',{url: "/eACH/baseInfo?"+qStr});
				$("#resultData").trigger('reloadGrid');
			}
			
			function refreshCountdown(){
				var nextSec = parseInt($("#countdown").html()) - 1;
				if(nextSec <= 0){nextSec = countdownSec;refreshBizdateAndClrphase();reSearchData($("#BIZDATE").val(), $("#CLEARINGPHASE").val());}
				$("#countdown").html(nextSec);
			}
			
			function startInterval(interval, func, values){
				clearInterval(countdownObj);
				countdownObj = setRepeater(func, values, interval);
			}
			
			function refreshBizdateAndClrphase(){
				var uri = "/eACH/baseInfo?component=monitor_biz_bo&method=getBusinessDateAndClrphase";
				var date = fstop.getServerDataExII(uri, null, false);
				$("#CUR_BIZDATE").val(date.bizdate);
				$("#CUR_CLEARINGPHASE").val(date.clearingphase);
				$("#PRE_BIZDATE").val(date.pre_bizdate);
				$("#PRE_CLEARINGPHASE").val(date.pre_clearingphase);
				
				createLABEL();
				createBTN();
			}
			
			function createLABEL(){
				var label = '<label id="LABEL_BIZDATE">' + $("#CUR_BIZDATE").val() + '</label>';
				if($("#LABEL_BIZDATE")){
					$("#LABEL_BIZDATE").replaceWith(label);
				}
			}
			
			function createBTN(){
				var btn = '<label class="btn" id="btn_clearingPhase1" onclick="reSearchData(\'' + $("#CUR_BIZDATE").val() + '\',\'' + $("#CUR_CLEARINGPHASE").val() + '\' ,\'' + 'btn_clearingPhase1'+ '\')">';
				btn += $("#CUR_BIZDATE").val() + ' 清算階段-' + $("#CUR_CLEARINGPHASE").val();
				btn += '</label>';
				if($("#btn_clearingPhase1")){
					$("#btn_clearingPhase1").replaceWith(btn);
					$("#btn_clearingPhase1").addClass("btn_selected");
				}
				
				btn = '<label class="btn" id="btn_clearingPhase2" onclick="reSearchData(\'' + $("#PRE_BIZDATE").val() + '\',\'' + $("#PRE_CLEARINGPHASE").val() + '\' ,\'' + 'btn_clearingPhase2'+ '\')">';
				btn += $("#PRE_BIZDATE").val() + ' 清算階段-' + $("#PRE_CLEARINGPHASE").val();
				btn += '</label>';
				if($("#btn_clearingPhase2")){
					$("#btn_clearingPhase2").replaceWith(btn);
					
				}
			}
			
			//取得user查詢條件
			function getSearchs(){
				var serchs = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					serchs[field.name] = field.value;
				});
				serchs['action']=$("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
			}
		</script>
	</body>
</html>