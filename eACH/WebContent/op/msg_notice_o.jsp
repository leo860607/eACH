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
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			#opPanel_custom{background-color:#fff;border-collapse:collapse;margin:0 auto}
			#opPanel_custom td{padding:10px;border:1px solid #799bc7}
			#opPanel_custom td:first-child{width:100px}
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
			<div id="opPanel">
				<html:form styleId="formID" action="/msg_notice">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
<%-- 					<html:hidden property="NOTICEID" styleId="NOTICEID" /> --%>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<table style="text-align:left">
							<tr>
								<td class="header" style="width:8%">日期</td>
								<td style="width:10%">
									<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width:10%">操作行</td>
								<td>
									<html:select styleId="BGBK_ID" property="BGBK_ID">
										<html:option value="0000000">全部</html:option>
										<logic:present name="msg_notice_form" property="bgbkIdList">
											<html:optionsCollection name="msg_notice_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="header" style="width: 15%">
									回覆結果
								</td>
								<td>
									<select id="RSPCODE" name="RSPCODE">
										<option value="">全部</option>
										<option value="S">成功</option>
										<option value="F">失敗</option>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="padding-top: 5px">
									<label class="btn" id="search" name="btn_SEARCH" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="btn_open_dialog" onclick="showMsgDialog()"><img src="./images/send.png"/>&nbsp;傳送訊息</label>
									</logic:equal>
								</td>
							</tr>
						</table>
						<!-- 訊息內容對話框 -->
						<div id="msg_dialog" title="訊息內容" style="font-size: 16px">
							操作行：<select onchange="$('#BGBK_ID').val(this.value)">
								<option value="0000000">全部</option>
								<logic:present name="msg_notice_form" property="bgbkIdList">
									<logic:iterate id="oBean" name="msg_notice_form" property="bgbkIdList">
										<option value="<bean:write name="oBean" property="value"/>"><bean:write name="oBean" property="label"/></option>
									</logic:iterate>
								</logic:present>
							</select>
							<br>
							<br>
<!-- 							20150414 add by hugo req by 票交李建利  UAT-20150411-07 -->
							訊息代碼:
							<html:text styleId="NOTICEID" property="NOTICEID" value="1000" size="4" maxlength="4" styleClass="validate[maxSize[4],minSize[4]] text-input"></html:text>
							訊息代碼清單:
							<select id = "NOTICEIDS"onchange="$('#NOTICEID').val(this.value)"> 
							<option value="1000">1000</option>
							<option value="1001">1001</option>
							<option value="1999">1999</option>
							<option value="5200">5200</option>
							<option value="5201">5201</option>
							</select>
							<html:textarea styleClass="text ui-widget-content ui-corner-all" styleId="MESSAGE" property="MESSAGE" style="width: 100%" rows="5"/>
							<div style="text-align:center">
								<label class="btn" id="add" name="btn_SEND" onclick="onPut(this.id)">
									<img src="./images/send.png"/>&nbsp;送出
								</label>
							</div>
						</div>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>
						查詢結果
						<logic:equal name="login_form" property="userData.s_auth_type" value="A">
							&nbsp;<label class="btn" id="edit" onclick="onPut(this.id)"><img src="./images/resend.png"/>&nbsp;重送</label>&nbsp;
						</logic:equal>
					</legend>
					<table id="resultData"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>	
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption,queryInterval;
			var stans = "";
			var tmpDate, tmpPhase ;
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				$("#msg_dialog").dialog({
					autoOpen: false,
					height: 300,
					width: 500,
					modal: true,
					close: function() {
						$("#MESSAGE").val("");
// 						$("#NOTICEID").val("1000");
// 						$("#NOTICEIDS").val("1000");
						$("#BGBK_ID").val($("#BGBK_ID").val());
						
// 						$("select", this).val("0000000");
					},
// 					20150421 edit by hugo  req by 李建利 開啟視窗時順便把使用者在父視窗的所選銀行帶至新視窗
					open: function() {
// 						$("#MESSAGE").val("123");
						$("select", this).val($("#BGBK_ID").val());
						$("#NOTICEID").val("1000");
						$("#NOTICEIDS").val("1000");
					}
				});
				alterMsg();
				initGridOption();
				initGrid();
				setDatePicker();
				$("#formID").validationEngine({promptPosition: "bottomRight"});
				
				$("#TXDATE").datepicker("setDate", new Date());
				$("#TXDATE").val('0' + $("#TXDATE").val());
			}
			
			function alterMsg(){
				var msg = '<bean:write name="msg_notice_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 270,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
						colNames:[
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
						    '選取重送',
						    </logic:equal>
						    '交易追蹤序號','操作行代號','操作行名稱','交易類別','傳送通知時間','訊息內容','FEP回覆結果','回覆結果'],
		            	colModel: [
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},
							</logic:equal>
							//交易追蹤序號，改顯示STAN欄位
							//{name:'WEBTRACENO',index:'WEBTRACENO',fixed:true,width: 85},
							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 85},
							{name:'BANKID',index:'BANKID',fixed:true,width: 70},
							{name:'BANKNAME',index:'BANKNAME',fixed:true,width: 210},
							{name:'PCODE',index:'PCODE',fixed:true,width: 70},
							{name:'DATETIME',index:'DATETIME',fixed:true,width: 150},
							{name:'DATAFIELD',index:'DATAFIELD',fixed:true,width: 150},
							{name:'FEP_ERR_DESC',index:'FEP_ERR_DESC',align:'center',fixed:true,width: 90},
							{name:'RSP_ERR_DESC',index:'RSP_ERR_DESC',align:'center',fixed:true,width: 120}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							//轉換「傳送通知時間」格式
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
								for(var rowCount in data){
									data[rowCount].BTN = '<input type="checkbox" class="chk_record" id="chk_'+data[rowCount].PK_STAN+'_'+data[rowCount].PK_TXDATE+'"/>';
									data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
									data[rowCount].PCODE = data[rowCount].PCODE=="1300"?"訊息通知":data[rowCount].PCODE;
								}
							</logic:equal>
							<logic:notEqual name="login_form" property="userData.s_auth_type" value="A">
								for(var rowCount in data){
									data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
									data[rowCount].PCODE = data[rowCount].PCODE=="1300"?"訊息通知":data[rowCount].PCODE;
								}
							</logic:notEqual>
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
				if(str == "edit"){
					var count = 0, chkList = [];
					$(".chk_record:checked").each(function(){
						chkList.push($(this).attr("id").split("_")[1] + "@" + $(this).attr("id").split("_")[2]);
						count++;
					});
					if(count == 0){alert("請先選取重送的項目!");return false;}
					if(reSend(chkList)){
						blockUI();
						if($("#TXDATE").val() == ""){
							$("#TXDATE").datepicker("setDate", new Date());
							$("#TXDATE").val('0' + $("#TXDATE").val());
						}
// 						20150420 edit by hugo req by 李建利 重送後只要查詢出重送的銀行(最新的重送記錄)
// 						searchDataByDate($("#TXDATE").val());
// 						startInterval(searchDataByDate, [$("#TXDATE").val()]);
						searchDataByStan(stans);
						startInterval(searchDataByStan, [stans]);
						unblockUI();
					}
				}else{
					//查詢
					if(str == "search"){
						if(!jQuery('#formID').validationEngine('validate')){
							return false;
						}
						//操作軌跡記錄
						var serchs = {};
						serchs['DATE'] = $("#TXDATE").val();
						if($("#BGBK_ID").val() == "0000000"){
							serchs['OPBK_ID'] = "all";
						}
						else{
							serchs['OPBK_ID'] = $("#BGBK_ID").val();
						}
						if($("#RSPCODE").val() == ""){
							serchs['RSPCODE'] = "all";
						}
						else{
							serchs['RSPCODE'] = $("#RSPCODE").val();
						}
						serchs['action'] = $("#formID").attr('action');
						$("#serchStrs").val(JSON.stringify(serchs));
						blockUI();
						clearInterval(queryInterval);
// 						20150420 add by hugo req by 李建利  增加回覆結果查詢條件
// 						searchDataByDate($("#TXDATE").val(), $("#BGBK_ID").val());
						searchDataByDate($("#TXDATE").val(), $("#BGBK_ID").val() , $("#RSPCODE").val(),$("#serchStrs").val());
					//送出	
					}else if(str == "add"){
						if($("#NOTICEID").val() == "1000" || $("#NOTICEID").val() == "1001" || $("#NOTICEID").val() == "1999"){
							if(confirm("確定送出通知?")){
								blockUI();
								var stan = send($("#BGBK_ID").val(), $("#MESSAGE").val() , $("#NOTICEID").val());
								if(stan != null){
									searchDataByStan(stan);
									startInterval(searchDataByStan, [stan]);
								}
								$("#msg_dialog").dialog('close');
							}
						}else if($("#NOTICEID").val() == "5200" || $("#NOTICEID").val() == "5201"){
							if(confirm("確定執行結算通知?")){
								refreshBizdateAndClrphase();
								var uri = "${pageContext.request.contextPath}"+"/baseInfo";
								var bgbkId = $("#BGBK_ID").val();
								var rdata = {component:"msg_notice_bo", method:"doCLNotifySole" ,BIZDATE:tmpDate ,CLEARINGPHASE:tmpPhase ,BGBK_ID:bgbkId ,BATCH_PROC_SEQ:'9' ,action :"msg_notice"};
								var vResult = fstop.getServerDataExII(uri,rdata,false);
								if(fstop.isNotEmpty(vResult)){
									alert(vResult.msg);
								}else{
									alert("系統異常");
								}
								$("#msg_dialog").dialog('close');
							}
						}else{
							alert("訊息代碼輸入有誤");
						}
					}
					unblockUI();
				}
			}
			
			function refreshBizdateAndClrphase(){
				var rdata = {component:"msg_notice_bo", method:"getBusinessDateAndClrphase"};
				var date = fstop.getServerDataExIII(uri, rdata, false , null ,null,'refreshBizdateAndClrphase' );
				tmpDate=date.pre_bizdate;
				tmpPhase=date.pre_clearingphase;
			}
			
			function send(bgbkId, message , noticeid){
				//判斷是否有中文，有則轉成UTF-8傳送
				if(message.match(/.*[^\u0000-\u007F]+.*/)){
					message = encodeURI(message);
				}
				
				var rdata = {component:"msg_notice_bo",method:"send",BGBK_ID:bgbkId,MESSAGE:message , NOTICEID:noticeid,action:$("#formID").attr('action')};
				if(window.console){console.log("rdata>>"+rdata);}
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					if(fstop.isNotEmpty(msg.msg)){
						alert(msg.msg);
					}
					return msg.result=="TRUE"?msg.STAN:null;
				}else{
					return null;
				}
			}
			
			function reSend(chkList){
				var rdata = {component:"msg_notice_bo",method:"resend",ID_LIST:chkList.toString(),action:$("#formID").attr('action')};
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					var failList = [];
					var successList = [];
					//有失敗的資料則顯示
					for(var bgbkId in msg){
						if(msg[bgbkId].result == "FALSE"){
							failList.push(msg[bgbkId].STAN);
						}else if(msg[bgbkId].result == "TRUE"){
							successList.push(msg[bgbkId]);
// 							20150421 add by hugo 重送 不管幾家銀行STAN 都一樣
							stans=msg[bgbkId].STAN;
						}
					}
					if(failList.length > 0){
						alert("以下資料重送失敗：\n" + failList);
						return false;
					}else{
						alert(successList[0].msg);
						return true;
					}
				}else{
					return false;
				}
			}
			
			function showMsgDialog(){
				$('#msg_dialog').dialog('open');
			}
			
			function searchDataByStan(stan){
				if(window.console){console.log("STAN=" + stan);}
				searchData("component=msg_notice_bo&method=getDataByStan&STAN="+stan);
			}
			
			function searchDataByDate(date, bgbkId ,rspcode,serchStrs){
				//if(window.console){console.log("DATE=" + date);}
// 				searchData("component=msg_notice_bo&method=getDataByDate&TXDATE="+date+"&BGBKID="+bgbkId);
				searchData("component=msg_notice_bo&method=getDataByDate&TXDATE="+date+"&BGBKID="+bgbkId+"&RSPCODE="+rspcode+"&SERCHSTRS="+serchStrs);
			}
			
			function searchData(qStr){
				if(window.console){console.log("qStr=" + qStr);}
				
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(gridOption);
			}
			
			function startInterval(func, values){
				clearInterval(queryInterval);
				queryInterval = setRepeater(func, values, 30);
			}
		</script>
	</body>
</html>