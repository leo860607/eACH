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
			<div id="opPanel" style="margin-top: 5px">
				<html:form styleId="formID" action="/async_mac">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<fieldset>
						<table>
							<tr>
								<td class="header" style="width:8%">日期</td>
								<td style="width:10%">
									<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],notChinese,twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width:13%">操作行</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID">
										<html:option value="0000000">全部</html:option>
										<logic:present name="async_mac_form" property="opbkIdList">
											<html:optionsCollection name="async_mac_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="padding-top: 5px">
									<label class="btn" id="search" name="btn_OP_DATE" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" name="btn_SEND" onclick="onPut(this.id)"><img src="./images/send.png"/>&nbsp;送出</label>
									</logic:equal>
								</td>
							</tr>
						</table>
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
			
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				initGridOption();
				initGrid();
				setDatePicker();
				$("#formID").validationEngine({binded:false, promptPosition: "bottomRight"});
				
				$("#TXDATE").datepicker("setDate", new Date());
				$("#TXDATE").val('0' + $("#TXDATE").val());
			}
			
			function alterMsg(){
				var msg = '<bean:write name="async_mac_form" property="msg"/>';
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
						    '交易追蹤序號','操作行代號','操作行名稱','交易類別','傳送通知時間','FEP回覆結果','回覆結果'],
		            	colModel: [
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},
							</logic:equal>
							//交易追蹤序號，改顯示STAN欄位
							//{name:'WEBTRACENO',index:'WEBTRACENO',fixed:true,width: 85},
							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 85},
							{name:'BANKID',index:'BANKID',fixed:true,width: 70},
							{name:'BANKNAME',index:'BANKNAME',fixed:true,width: 210},
							{name:'PCODE',index:'PCODE',fixed:true,width: 120},
							{name:'DATETIME',index:'DATETIME',fixed:true,width: 150},
							{name:'FEP_ERR_DESC',index:'FEP_ERR_DESC',align:'center',fixed:true,width: 200},
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
								data[rowCount].BTN = '<input type="checkbox" class="chk_record" id="chk_'+data[rowCount].BANKID+'"/>';
								data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
								data[rowCount].PCODE = data[rowCount].PCODE=="1200"?"押碼基碼同步通知":data[rowCount].PCODE;
							}
							</logic:equal>
							<logic:notEqual name="login_form" property="userData.s_auth_type" value="A">
							for(var rowCount in data){
								data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
								data[rowCount].PCODE = data[rowCount].PCODE=="1200"?"押碼基碼同步通知":data[rowCount].PCODE;
							}
							</logic:notEqual>
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
						chkList.push($(this).attr("id").split("_")[1]);
						count++;
					});
					if(count == 0){alert("請先選取重送的項目!");return false;}
					if(confirm("確定執行重送?")){
						if(reSend(chkList)){
							blockUI();
							if($("#TXDATE").val() == ""){
								$("#TXDATE").datepicker("setDate", new Date());
								$("#TXDATE").val('0' + $("#TXDATE").val());
							}
							searchDataByDate($("#TXDATE").val(), $("#OPBK_ID").val());
							startInterval(searchDataByDate, [$("#TXDATE").val(), $("#OPBK_ID").val()]);
							unblockUI();
						}
					}
				}else{
					//查詢
					if(str == "search"){
						if(!jQuery('#formID').validationEngine('validate')){
							return false;
						}
						blockUI();
						clearInterval(queryInterval);
						searchDataByDate($("#TXDATE").val(), $("#OPBK_ID").val());
					//送出	
					}else if(str == "add"){
						if(!jQuery('#formID').validationEngine('validate')){
							return false;
						}
						if(confirm("確定送出通知?")){
							blockUI();
							var stan = send($("#OPBK_ID").val());
							if(stan != null){
								searchDataByStan(stan);
								startInterval(searchDataByStan, [stan]);
							}
						}
					}
					unblockUI();
				}
			}
			
			function send(opbkId){
				var rdata = {component:"async_mac_bo",method:"send",OPBK_ID:opbkId};
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
				var rdata = {component:"async_mac_bo",method:"resend",DATA_LIST:chkList.toString()};
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					var failList = [];
					var successList = [];
					//有失敗的資料則顯示
					for(var stan in msg){
						if(msg[stan].result == "FALSE"){
							failList.push(msg[stan].STAN);
						}else if(msg[stan].result == "TRUE"){
							successList.push(msg[stan]);
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
			
			function searchDataByStan(stan){
				//if(window.console){console.log("STAN=" + stan);}
				searchData("component=async_mac_bo&method=getDataByStan&STAN="+stan);
			}
			
			function searchDataByDate(date, opbkId){
				//if(window.console){console.log("DATE=" + date);}
				searchData("component=async_mac_bo&method=getDataByDate&TXDATE="+date+"&OPBKID="+opbkId);
			}
			
			function searchData(qStr){
				//if(window.console){console.log("qStr=" + qStr);}
				
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