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
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/json2.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			.EACH_STATUS_CLASS{background-color: #74b6e3;}
			label[id^=LABEL] {color: white;}
			#countdownBlock{position:relative;float:right;top:-25px;background-color: #ECF1F6;padding:0px 5px}
			#sys_status table{  margin: 0 auto; border: 1px ;border-collapse: collapse;width: 60%;margin-top: 5px ;margin-left: 10px;margin-bottom: 5px;}
			#connstatus table{  margin: 0 auto; border: 1px ;border-collapse: collapse;margin-top: 5px ;margin-bottom: 5px;}
			#connstatus2 table{  margin: 0 auto; border: 1px ;border-collapse: collapse;}
 			td{ text-align: center; border: 1px solid white; font-size:14px; } 
			table th {border: 1px solid white;background-color:#7081B9; color:white; font-size:14px;}
			.header {text-align: right;padding-right: 20px;}
			.greenBar{
	                 background: #00C400; padding: 5px 5px; border-radius: 3px; color: rgb(255, 255, 255)  !important; text-decoration: none !important; display: inline-block; font-weight:bold;
                     }
            .redBar{
	                 background: Red; padding: 5px 5px; border-radius: 3px; color: rgb(255, 255, 255)  !important; text-decoration: none !important; display: inline-block; font-weight:bold;
                   }
            .div_right {
            	color:red; float: right;
			}
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
			<html:form styleId="formID" action="/monitor">
				<html:hidden property="ac_key" styleId="ac_key" value=""/>
				<html:hidden property="target" styleId="target" value=""/>
				<html:hidden property="BGBK_ID" styleId="BGBK_ID" value=""/>
				<html:hidden property="APID" styleId="APID" value=""/>
				<html:hidden property="serchStrs" styleId="serchStrs"/>
				<html:hidden property="PATH" styleId="PATH"/>
			</html:form>
			<div id="rsPanel" style="margin-top: 10px">
				<fieldset>
					<legend>查詢結果&nbsp;
						<span class="EACH_STATUS_CLASS">&nbsp;交換所系統狀態：<label id="LABEL_EACHSYSSTATUS"></label>&nbsp;</span>
						&nbsp;
						<span class="EACH_STATUS_CLASS">&nbsp;交換所應用系統狀態：<label id="LABEL_EACHAPSTATUS"></label>&nbsp;</span>
					</legend>
					<div id="countdownBlock">
						剩餘 <font color="red" id="countdown"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reSearchData()"><img src="./images/refresh.png"/>&nbsp;重載</label>
					</div><br>
					<span id="errorMsg" style="background-color: red; color: #ffffff; font-size: 20pt; "></span>
					<div id="sys_status">
					    <table  border="1px" style="border: black;">
					        <tr>
					            <th>eACH系統狀態</th>  <th>前營業日</th>  <th>本營業日</th>  <th>次營業日</th>  <th>清算階段</th>
					        </tr>
					        <tr>
					            <td><span id="SYSSTATUS"></span></td>  
					            <td><span id="PREVDATE"></span></td>  
					            <td><span id="THISDATE"></span></td>  
					            <td><span id="NEXTDATE"></span></td>  
					            <td><span id="CLEARINGPHASE"></span></td>
					        </tr>
					    </table>
					</div>
					
					<div id= "pending_msg" class= "div_right"  >
					<span id = "sp_pending_msg"></span>
					<label class="btn" id="search_pending" onclick="search_pending(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>&nbsp;
					</div>
					<br>
					
					<div id="connstatus" style="width:1249px;">
					    <table id="panel"  style="border: 1px solid white;">
					        <tr>
					            <th rowspan="4" style="width: 63px">銀行代號</th>  <th rowspan="4" style="width: 84px">系統狀態</th>  <th rowspan="4" style="width: 60px">應用系統狀態</th> 
					        </tr> 
					        <tr>    
					            <th colspan="20">連線狀態</th> 
					        </tr>
					        <tr> 
					            <th colspan="2">01</th>  <th colspan="2">02</th>  <th colspan="2">03</th>  <th colspan="2">04</th>  <th colspan="2">05</th>  <th colspan="2">06</th>  <th colspan="2">07</th>  <th colspan="2">08</th>  <th colspan="2">09</th>  <th colspan="2">10</th>
					        </tr>
					        <tr>    
<!-- 					        20170302 edit by hugo req 配合普鴻P-UAT-20150610-04 做修改 -->
<!-- 					            <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th>  <th>傳送端狀態</th><th>接收端狀態</th> -->
					            <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th>  <th>接收端狀態</th><th>傳送端狀態</th> <th>接收端狀態</th><th>傳送端狀態</th>  
					        </tr>
					    </table>
					</div>
					
					<div id="connstatus2" style="overflow-y:scroll; HEIGHT: 200px; width:1265px;">
					    <table id="tpanel">
					    </table>
					</div>
<!-- 					<table id="resultData"></table> -->
				</fieldset>
			</div>
			<br>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption;
			var countdownObj,countdownSec=30;
			var tmpDate ,tmpPhase;
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				initGridOption();
				initGrid();
				searchData();
				updateEachStatus();
				sysStatus();
				statusPanel('init');
				checkTxnDate();
				isOnPending();
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="monitor_form" property="msg"/>';
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
						colNames:['銀行代號','連線狀態','系統狀態','應用系統狀態','KeepAlive未回次數'],
		            	colModel: [
							{name:'BGBK_ID',index:'BGBK_ID',fixed:true,width: 85},
							{name:'CONN_STATE',index:'CONN_STATE',fixed:true,width: 120},
							{name:'MBSYSSTATUSNAME',index:'MBSYSSTATUSNAME',fixed:true,width: 120},
							{name:'MBAPSTATUSNAME',index:'MBAPSTATUSNAME',fixed:true,width: 120},
							{name:'KEEPALIVE',index:'KEEPALIVE',fixed:true,width: 150}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
						},
	 					loadtext: "處理中..."
				};
			}
// 			20151022 add by hugo 因應整批功能，查詢整批交易是否出現未完成交易
			function isOnPending(){
				var uri = "${pageContext.request.contextPath}"+"/baseInfo";
				var action = $("#formID").attr('action');
				var rdata = {component:"monitor_bo", method:"isOnPending"  ,action :action};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
				if(fstop.isNotEmpty(vResult)){
// 					alert(vResult.msg);
					if(window.console){console.log("msg>>"+vResult.msg);}
					if(vResult.result == "TRUE"){
						$("#sp_pending_msg").html(vResult.msg);
						$("#pending_msg").show();
						tmpDate = vResult.bizdate;
						tmpPhase = vResult.clearingphase;
					}else{
						$("#pending_msg").hide();
					}
				}else{
					if(window.console){console.log("系統異常...");}
					$("#sp_pending_msg").html("系統異常...");
					$("#pending_msg").show();
// 					alert("系統異常");
				}
			}
			function search_pending(){
				var url ="${pageContext.request.contextPath}"+"/monitor.do?ac_key=search_pending&BIZDATE="+tmpDate+"&CLEARINGPHASE="+tmpPhase+"";
				window.open(url);
			}
			
			
			function initGrid(){
				$("#resultData").jqGrid(gridOption);
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var qStr = "component=monitor_bo&method=getData";
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(gridOption);
			}
			
			function updateEachStatus(){
				var rdata = {component:"monitor_bo", method:"getEachStatus"};
				var status = fstop.getServerDataExIII(uri, rdata, false);
				if(status != null){
					$("#LABEL_EACHAPSTATUS").html(status.EACHAPSTATUS);
					$("#LABEL_EACHSYSSTATUS").html(status.EACHSYSSTATUS);
				}
			}
			
			function startInterval(interval, func, values){
				clearInterval(countdownObj);
				countdownObj = setRepeater(func, values, interval);
			}
			
			function refreshCountdown(){
				var nextSec = parseInt($("#countdown").html()) - 1;
				if(nextSec <= 0){
					nextSec = countdownSec;
					updateEachStatus();
					searchData();
					sysStatus();
					statusPanel('');
					checkTxnDate();
					isOnPending();
				}
				$("#countdown").html(nextSec);
			}
			
			function reSearchData(){
				updateEachStatus();
				searchData();
				sysStatus();
				statusPanel('');
				checkTxnDate();
				isOnPending();
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
			}
			
			function sysStatus(){
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=monitor_bo&method=getSYSSTATUSTAB",
					async:false,
					dataType:'text',
					success:function(result){
						var txt = '{"table":' + result + '}';						
						var obj = eval ("(" + txt + ")");
						
						var datemode = obj.table[0].DATEMODE;						
						var sysstatus = obj.table[0].SYSSTATUS;
						
						if(sysstatus == '1'){
							sysstatus = '開機';
							
						}else{
							sysstatus = '關機';
							document.getElementById("errorMsg").innerHTML ="系統關機";
						}
						document.getElementById("SYSSTATUS").innerHTML = sysstatus;
						
						if(datemode == '0'){
							document.getElementById("PREVDATE").innerHTML = obj.table[0].PREVDATE;
							document.getElementById("THISDATE").innerHTML = obj.table[0].THISDATE;
							document.getElementById("NEXTDATE").innerHTML = obj.table[0].NEXTDATE;
						}else{
							document.getElementById("PREVDATE").innerHTML = obj.table[0].THISDATE;
							document.getElementById("THISDATE").innerHTML = obj.table[0].NEXTDATE;
							document.getElementById("NEXTDATE").innerHTML = "";
						}
												
						document.getElementById("CLEARINGPHASE").innerHTML = obj.table[0].CLEARINGPHASE;
					}
				});
			}
			
			function statusPanel(path){
				//使用者操作軌跡用
				var serchs = {};
				$("#PATH").val("");
				$("#PATH").val(path);
				serchs['BIZDATE'] = $("#THISDATE").html();
				serchs['action'] = $("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=monitor_bo&method=getStatusPanel&"+$('#formID').serialize(),
					async:false,
					dataType:'text',
					success:function(result){
						var txt = '{"panel":' + result + '}';						
						var obj = eval ("(" + txt + ")");
						
// 						var datemode = obj.table[0].DATEMODE;						
						
						var key, count = 0;
						for(key in obj.panel) {
							if(obj.panel.hasOwnProperty(key)) {
								count++;
							}
						}
						var table = document.getElementById("tpanel");
						var rowCount = table.rows.length;
						if(rowCount > 0) {
							for(var i=0;i<rowCount;i++){
								table.deleteRow(0);
							}
						}
						
						for(var i=0;i<count;i++){
							var BGBK_ID=obj.panel[i].BGBK_ID;
							var MBSYSSTATUS=obj.panel[i].MBSYSSTATUS;
							var MBAPSTATUS=obj.panel[i].MBAPSTATUS;
							var APID=obj.panel[i].APID;
							
							//如果換key失敗則bgcolor設為紅色
							var bgcolor='';
							if(BGBK_ID==APID){
								bgcolor='bgcolor=\"#cdc9c9\"';
							}
							
							var sysstatus; if(MBSYSSTATUS=='1'){sysstatus='開機'}else if(MBSYSSTATUS=='2'){sysstatus='押碼基碼同步'}else if(MBSYSSTATUS=='3'){sysstatus='訊息通知'}else{sysstatus='關機'}
							var apstatus; if(MBAPSTATUS=='1'){apstatus='簽到'}else if(MBAPSTATUS=='2'){apstatus='暫時簽退'}else if(MBAPSTATUS=='9'){apstatus='簽退'}
							var s01=obj.panel[i].s01, s01a; if(s01=='3'){s01a = '<div class="greenBar"></div>'}else if(s01=='0'||s01=='1'||s01=='2'){s01a = '<div class="redBar"></div>'}else{s01a =''}
							var r01=obj.panel[i].r01, r01a; if(r01=='3'){r01a = '<div class="greenBar"></div>'}else if(r01=='0'||r01=='1'||r01=='2'){r01a = '<div class="redBar"></div>'}else{r01a =''}
							var s02=obj.panel[i].s02, s02a; if(s02=='3'){s02a = '<div class="greenBar"></div>'}else if(s02=='0'||s02=='1'||s02=='2'){s02a = '<div class="redBar"></div>'}else{s02a =''}
							var r02=obj.panel[i].r02, r02a; if(r02=='3'){r02a = '<div class="greenBar"></div>'}else if(r02=='0'||r02=='1'||r02=='2'){r02a = '<div class="redBar"></div>'}else{r02a =''}
							var s03=obj.panel[i].s03, s03a; if(s03=='3'){s03a = '<div class="greenBar"></div>'}else if(s03=='0'||s03=='1'||s03=='2'){s03a = '<div class="redBar"></div>'}else{s03a =''}
							var r03=obj.panel[i].r03, r03a; if(r03=='3'){r03a = '<div class="greenBar"></div>'}else if(r03=='0'||r03=='1'||r03=='2'){r03a = '<div class="redBar"></div>'}else{r03a =''}
							var s04=obj.panel[i].s04, s04a; if(s04=='3'){s04a = '<div class="greenBar"></div>'}else if(s04=='0'||s04=='1'||s04=='2'){s04a = '<div class="redBar"></div>'}else{s04a =''}
							var r04=obj.panel[i].r04, r04a; if(r04=='3'){r04a = '<div class="greenBar"></div>'}else if(r04=='0'||r04=='1'||r04=='2'){r04a = '<div class="redBar"></div>'}else{r04a =''}
							var s05=obj.panel[i].s05, s05a; if(s05=='3'){s05a = '<div class="greenBar"></div>'}else if(s05=='0'||s05=='1'||s05=='2'){s05a = '<div class="redBar"></div>'}else{s05a =''}
							var r05=obj.panel[i].r05, r05a; if(r05=='3'){r05a = '<div class="greenBar"></div>'}else if(r05=='0'||r05=='1'||r05=='2'){r05a = '<div class="redBar"></div>'}else{r05a =''}
							var s06=obj.panel[i].s06, s06a; if(s06=='3'){s06a = '<div class="greenBar"></div>'}else if(s06=='0'||s06=='1'||s06=='2'){s06a = '<div class="redBar"></div>'}else{s06a =''}
							var r06=obj.panel[i].r06, r06a; if(r06=='3'){r06a = '<div class="greenBar"></div>'}else if(r06=='0'||r06=='1'||r06=='2'){r06a = '<div class="redBar"></div>'}else{r06a =''}
							var s07=obj.panel[i].s07, s07a; if(s07=='3'){s07a = '<div class="greenBar"></div>'}else if(s07=='0'||s07=='1'||s07=='2'){s07a = '<div class="redBar"></div>'}else{s07a =''}
							var r07=obj.panel[i].r07, r07a; if(r07=='3'){r07a = '<div class="greenBar"></div>'}else if(r07=='0'||r07=='1'||r07=='2'){r07a = '<div class="redBar"></div>'}else{r07a =''}
							var s08=obj.panel[i].s08, s08a; if(s08=='3'){s08a = '<div class="greenBar"></div>'}else if(s08=='0'||s08=='1'||s08=='2'){s08a = '<div class="redBar"></div>'}else{s08a =''}
							var r08=obj.panel[i].r08, r08a; if(r08=='3'){r08a = '<div class="greenBar"></div>'}else if(r08=='0'||r08=='1'||r08=='2'){r08a = '<div class="redBar"></div>'}else{r08a =''}
							var s09=obj.panel[i].s09, s09a; if(s09=='3'){s09a = '<div class="greenBar"></div>'}else if(s09=='0'||s09=='1'||s09=='2'){s09a = '<div class="redBar"></div>'}else{s09a =''}
							var r09=obj.panel[i].r09, r09a; if(r09=='3'){r09a = '<div class="greenBar"></div>'}else if(r09=='0'||r09=='1'||r09=='2'){r09a = '<div class="redBar"></div>'}else{r09a =''}
							var s10=obj.panel[i].s10, s10a; if(s10=='3'){s10a = '<div class="greenBar"></div>'}else if(s10=='0'||s10=='1'||s10=='2'){s10a = '<div class="redBar"></div>'}else{s10a =''}
							var r10=obj.panel[i].r10, r10a; if(r10=='3'){r10a = '<div class="greenBar"></div>'}else if(r10=='0'||r10=='1'||r10=='2'){r10a = '<div class="redBar"></div>'}else{r10a =''}
							$("#tpanel").append("<tr "+bgcolor+"><td style='width: 64px'>"+BGBK_ID+"</td>"+"<td style='width: 87px'>"+sysstatus+"</td>"+"<td style='width: 60px'>"+apstatus+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s01+",'01','s')\">"+s01a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r01+",'01','r')\">"+r01a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s02+",'02','s')\">"+s02a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r02+",'02','r')\">"+r02a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s03+",'03','s')\">"+s03a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r03+",'03','r')\">"+r03a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s04+",'04','s')\">"+s04a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r04+",'04','r')\">"+r04a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s05+",'05','s')\">"+s05a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r05+",'05','r')\">"+r05a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s06+",'06','s')\">"+s06a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r06+",'06','r')\">"+r06a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s07+",'07','s')\">"+s07a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r07+",'07','r')\">"+r07a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s08+",'08','s')\">"+s08a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r08+",'08','r')\">"+r08a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s09+",'09','s')\">"+s09a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r09+",'09','r')\">"+r09a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+s10+",'10','s')\">"+s10a+"</td>"+
									"<td  style='width: 50px' onclick=\"panelDetail(\'"+BGBK_ID+"\',"+r10+",'10','r')\">"+r10a+"</td>"+
									"</tr>");
						}
						
						
					}
				});
			}

			function panelDetail(BGBK_ID,status,CHANNEL_ID,SR){
				if(status !== ''){
					var bgbkid = BGBK_ID.substring(0,3);
					$.ajax({
						type:'POST',
						url:"/eACH/baseInfo?component=monitor_bo&method=getPanelDetail&BGBK_ID="+bgbkid+"&CHANNEL_ID="+CHANNEL_ID,
						async:false,
						dataType:'text',
						success:function(result){
							var txt = '{"detail":' + result + '}';
							var obj = eval ("(" + txt + ")");
							var SEND_CONNECT_TIME = obj.detail[0].SEND_CONNECT_TIME;
							var RECV_CONNECT_TIME = obj.detail[0].RECV_CONNECT_TIME;
							var SEND_LASTTX_TIME = obj.detail[0].SEND_LASTTX_TIME;
							var RECV_LASTTX_TIME = obj.detail[0].RECV_LASTTX_TIME;
							var SEND_DISCONNECT_TIME = obj.detail[0].SEND_DISCONNECT_TIME;
							var RECV_DISCONNECT_TIME = obj.detail[0].RECV_DISCONNECT_TIME;
							
							var send_connect='';var recv_connect='';var send_lasttx='';var recv_lasttx='';var send_disconnect='';var recv_disconnect = '';
								 if(SEND_CONNECT_TIME!=''){send_connect = SEND_CONNECT_TIME.substring(0,4)+'-'+SEND_CONNECT_TIME.substring(4,6)+'-'+SEND_CONNECT_TIME.substring(6,8)+' '+SEND_CONNECT_TIME.substring(8,10)+':'+SEND_CONNECT_TIME.substring(10,12)+':'+SEND_CONNECT_TIME.substring(12,14)}
								 if(RECV_CONNECT_TIME!=''){recv_connect = RECV_CONNECT_TIME.substring(0,4)+'-'+RECV_CONNECT_TIME.substring(4,6)+'-'+RECV_CONNECT_TIME.substring(6,8)+' '+RECV_CONNECT_TIME.substring(8,10)+':'+RECV_CONNECT_TIME.substring(10,12)+':'+RECV_CONNECT_TIME.substring(12,14)}
								 if(SEND_LASTTX_TIME!=''){send_lasttx = SEND_LASTTX_TIME.substring(0,4)+'-'+SEND_LASTTX_TIME.substring(4,6)+'-'+SEND_LASTTX_TIME.substring(6,8)+' '+SEND_LASTTX_TIME.substring(8,10)+':'+SEND_LASTTX_TIME.substring(10,12)+':'+SEND_LASTTX_TIME.substring(12,14)}
								 if(RECV_LASTTX_TIME!=''){recv_lasttx = RECV_LASTTX_TIME.substring(0,4)+'-'+RECV_LASTTX_TIME.substring(4,6)+'-'+RECV_LASTTX_TIME.substring(6,8)+' '+RECV_LASTTX_TIME.substring(8,10)+':'+RECV_LASTTX_TIME.substring(10,12)+':'+RECV_LASTTX_TIME.substring(12,14)}
								 if(SEND_DISCONNECT_TIME!=''){send_disconnect = SEND_DISCONNECT_TIME.substring(0,4)+'-'+SEND_DISCONNECT_TIME.substring(4,6)+'-'+SEND_DISCONNECT_TIME.substring(6,8)+' '+SEND_DISCONNECT_TIME.substring(8,10)+':'+SEND_DISCONNECT_TIME.substring(10,12)+':'+SEND_DISCONNECT_TIME.substring(12,14)}
								 if(RECV_DISCONNECT_TIME!=''){recv_disconnect = RECV_DISCONNECT_TIME.substring(0,4)+'-'+RECV_DISCONNECT_TIME.substring(4,6)+'-'+RECV_DISCONNECT_TIME.substring(6,8)+' '+RECV_DISCONNECT_TIME.substring(8,10)+':'+RECV_DISCONNECT_TIME.substring(10,12)+':'+RECV_DISCONNECT_TIME.substring(12,14)}
							
														
							if(SR == 's'){
								alert("傳送端連線時間："+send_connect+"\r"+"傳送端最後一次傳送交易時間："+send_lasttx+"\r"+"傳送端斷線時間："+send_disconnect);
							}else{
								alert("接收端連線時間："+recv_connect+"\r"+"接收端最後一次傳送交易時間："+recv_lasttx+"\r"+"接收端斷線時間："+recv_disconnect);
							}
							
						}
					})
				}
			}
			
			function checkTxnDate(){
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=monitor_bo&method=checksysStatus",
					async:false,
					dataType:'text',
					success:function(result){
						if(!result  == ''){
							document.getElementById("errorMsg").innerHTML = result;
						}
					}
				})
			}

		</script>
	</body>
</html>