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
			<div id="opPanel">
				<html:form styleId="formID" action="/agent_turnon">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<table>
							<tr>
								<td class="header" style="width: 15%">??????</td>
								<td style="width: 35%">
									<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width: 15%">????????????</td>
								<td style="width: 35%" >
									<html:select styleId="ABBR_ID" property="ABBR_ID">
										<html:option value="0000000">??????</html:option>
										<logic:present name="agent_turnon_form" property="abbrIdList">
											<html:optionsCollection name="agent_turnon_form" property="abbrIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="header" style="width: 15%">
									????????????
								</td>
								<td>
									<select id="RSPCODE" name="RSPCODE">
										<option value="">??????</option>
										<option value="S">??????</option>
										<option value="F">??????</option>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="padding-top: 5px">
									<label class="btn" id="search" name="btn_OP_DATE" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;??????</label>
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" name="btn_ABBR_ID" onclick="onPut(this.id)"><img src="./images/send.png"/>&nbsp;??????</label>
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
						????????????
						<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
							&nbsp;<label class="btn" id="edit" onclick="onPut(this.id)"><img src="./images/resend.png"/>&nbsp;??????</label>&nbsp;
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
			var stans ="" ;
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
				$("#formID").validationEngine({promptPosition: "bottomRight"});
				
				$("#TXDATE").datepicker("setDate", new Date());
				$("#TXDATE").val('0' + $("#TXDATE").val());
			}
			
			function alterMsg(){
				var msg = '<bean:write name="agent_turnon_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
						autowidth:true,
		            	height: 300,
		            	sorttype:"text",
		            	shrinkToFit: false,
		            	rowNum: 10000,
		            	loadonce: true,
						colNames:[
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
						    	'????????????',
						    </logic:equal>
						          '??????????????????','???????????????','???????????????','????????????','??????????????????','FEP????????????','????????????'
						],
		            	colModel: [
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
								{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},
							</logic:equal>
							//??????????????????????????????STAN??????
							//{name:'WEBTRACENO',index:'WEBTRACENO',fixed:true,width: 85},
							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 85},
							{name:'BANKID',index:'BANKID',fixed:true,width: 70},
							{name:'BANKNAME',index:'BANKNAME',fixed:true,width: 210},
							{name:'PCODE',index:'PCODE',fixed:true,width: 70},
							{name:'DATETIME',index:'DATETIME',fixed:true,width: 150},
							{name:'FEP_ERR_DESC',index:'FEP_ERR_DESC',fixed:true,width: 200},
							{name:'RSP_ERR_DESC',index:'RSP_ERR_DESC',fixed:true,width: 150}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							//????????????????????????????????????
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
								for(var rowCount in data){
									data[rowCount].BTN = '<input type="checkbox" class="chk_record" id="chk_'+data[rowCount].BANKID+'"/>';
									data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
									data[rowCount].PCODE = data[rowCount].PCODE=="1100"?"????????????":data[rowCount].PCODE;
								}
							</logic:equal>
							<logic:notEqual name="login_form" property="userData.s_auth_type" value="A">
								for(var rowCount in data){
									data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
									data[rowCount].PCODE = data[rowCount].PCODE=="1100"?"????????????":data[rowCount].PCODE;
								}
							</logic:notEqual>
						},
						loadComplete: function(data){
// 							?????????????????????
							noDataEvent(data ,$("#resultData") );
						},
	 					loadtext: "?????????..."
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
					if(count == 0){alert("???????????????????????????!");return false;}
					if(reSend(chkList)){
						blockUI();
						if($("#TXDATE").val() == ""){
							$("#TXDATE").datepicker("setDate", new Date());
							$("#TXDATE").val('0' + $("#TXDATE").val());
						}
// 						20150420 edit by hugo req by ????????? ???????????????????????????????????????(?????????????????????)
// 						searchDataByDate($("#TXDATE").val());
// 						startInterval(searchDataByDate, [$("#TXDATE").val()]);
						searchDataByStan(stans);
						startInterval(searchDataByStan, [stans]);
						unblockUI();
					}
				}else{
					//??????
					if(str == "search"){
						if(!jQuery('#formID').validationEngine('validate')){
							return false;
						}
						//??????????????????
						var serchs = {};
						serchs['DATE'] = $("#TXDATE").val();
						if($("#ABBR_ID").val() == "0000000"){
							serchs['OPBK_ID'] = "all";
						}
						else{
							serchs['OPBK_ID'] = $("#ABBR_ID").val();
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
						searchDataByDate($("#TXDATE").val(), $("#ABBR_ID").val() , $("#RSPCODE").val(),$("#serchStrs").val());
					//??????	
					}else if(str == "add"){
						if(confirm("???????????????????")){
							blockUI();
							var stan = send($("#ABBR_ID").val());
							if(stan != null){
								searchDataByStan(stan);
								startInterval(searchDataByStan, [stan]);
							}
						}
					}
					unblockUI();
				}
			}
			
			function send(abbrId){
				if(preCheck()){
					var rdata = {component:"agent_turnon_bo",method:"send",ABBR_ID:abbrId ,action:$("#formID").attr('action')};
					var msg = fstop.getServerDataExII(uri, rdata, false);
					if(msg != null){
						if(fstop.isNotEmpty(msg.msg)){
							alert(msg.msg);
						}
						return msg.result=="TRUE"?msg.STAN:null;
					}
				}
				return null;
			}
			
			function preCheck(){
				var rdata = {component:"agent_turnon_bo",method:"preCheck"};
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					if(fstop.isNotEmpty(msg.msg)){
						alert(msg.msg);
					}
					return msg.result=="FALSE"?false:true;
				}else{
					return false;
				}
			}
			
			function reSend(chkList){
				var rdata = {component:"agent_turnon_bo",method:"resend",ABBR_ID_LIST:chkList.toString(),action:$("#formID").attr('action')};
				if(window.console){console.log("chkList>>"+JSON.stringify(chkList));}
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					var failList = [];
					var successList = [];
					//???????????????????????????
					for(var abbrId in msg){
						if(msg[abbrId].result == "FALSE"){
							failList.push(msg[abbrId].STAN);
						}else if(msg[abbrId].result == "TRUE"){
							successList.push(msg[abbrId]);
// 							20150421 add by hugo ?????? ??????????????????STAN ?????????
							stans=msg[abbrId].STAN;
						}
					}
					if(window.console){console.log("stans>>"+JSON.stringify(stans));}
					if(failList.length > 0){
						alert("???????????????????????????\n" + failList);
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
				if(window.console){console.log("STAN=" + stan);}
				searchData("component=agent_turnon_bo&method=getDataByStan&STAN="+stan);
			}
			
			function searchDataByDate(date, abbrId , rspcode,serchStrs){
				//if(window.console){console.log("DATE=" + date);}
				searchData("component=agent_turnon_bo&method=getDataByDate&TXDATE="+date+"&ABBRID="+abbrId+"&RSPCODE="+rspcode+"&SERCHSTRS="+serchStrs);
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
				$("#resultData").jqGrid(newOption);
			}
			
			function startInterval(func, values){
				clearInterval(queryInterval);
				queryInterval = setRepeater(func, values, 30);
			}
		</script>
	</body>
</html>