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
				<html:form styleId="formID" action="/chg_mac">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<fieldset>
						<table id="search_tab">
							<tr>
								<td class="header" style="width:13%">??????</td>
								<td style="width:10%">
									<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width:13%">????????????????????????</td>
								<td >
										<html:radio property="CHG_PCODE" value="1200" onclick="disabled_Idfield(this.value)">1200 - ??????????????????</html:radio>
										<html:radio property="CHG_PCODE" value="1210" onclick="disabled_Idfield(this.value)">1210 - ??????????????????</html:radio>
								</td>
							</tr>
							<tr>
							<td class="header" >?????????</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID">
										<html:option value="0000000">??????</html:option>
										<logic:present name="chg_mac_form" property="bgbkIdList">
											<html:optionsCollection name="chg_mac_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
								
								<td class="header">??????????????????</td>
								<td>
									<html:select styleId="IDFIELD" property="IDFIELD" disabled="true">
										<html:option value="">??????</html:option>
	<%-- 										<html:option value="01">01 - ??????????????????</html:option> --%>
	<%-- 										<html:option value="02">02 - ??????????????????</html:option> --%>
										<html:option value="01">01 - OPC????????????</html:option>
										<html:option value="02">02 - ????????????????????????</html:option>
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
										<label class="btn" id="add" name="btn_SEND" onclick="onPut(this.id)"><img src="./images/send.png"/>&nbsp;??????</label>
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
						<logic:equal name="login_form" property="userData.s_auth_type" value="A">
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
			var stans = "";
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
				var msg = '<bean:write name="chg_mac_form" property="msg"/>';
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
							'????????????',
							</logic:equal>
						    '??????????????????','???????????????','???????????????','????????????','??????????????????','??????????????????','FEP????????????','????????????'],
		            	colModel: [
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false},
							</logic:equal>
							//??????????????????????????????STAN??????
							//{name:'WEBTRACENO',index:'WEBTRACENO',fixed:true,width: 85},
							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 85},
							{name:'BANKID',index:'BANKID',fixed:true,width: 70},
							{name:'BANKNAME',index:'BANKNAME',fixed:true,width: 210},
							{name:'CHG_PCODE',index:'CHG_PCODE',fixed:true,width: 120 ,formatter:pcode_formatter},
							{name:'IDFIELD',index:'IDFIELD',fixed:true,width: 140},
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
							//????????????????????????????????????
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
							for(var rowCount in data){
// 								data[rowCount].BTN = '<input type="checkbox" class="chk_record" id="chk_'+data[rowCount].BANKID+'@'+data[rowCount].IDFIELD+'"/>';
								data[rowCount].BTN = '<input type="checkbox" class="chk_record" id="chk_'+data[rowCount].BANKID+'@'+data[rowCount].CHG_PCODE+'@'+data[rowCount].IDFIELD+'"/>';
								data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
// 								data[rowCount].CHG_PCODE = data[rowCount].CHG_PCODE=="1210"?"????????????????????????":(data[rowCount].CHG_PCODE=="1200"?"????????????????????????":data[rowCount].CHG_PCODE);
// 								data[rowCount].IDFIELD = data[rowCount].IDFIELD=="01"?"01-OPC????????????":data[rowCount].IDFIELD=="02"?"02-????????????????????????":data[rowCount].IDFIELD;
									if(window.console){console.log("data[rowCount].CHG_PCODE"+data[rowCount].CHG_PCODE);}
								if(data[rowCount].CHG_PCODE == "1210"){
									data[rowCount].IDFIELD = data[rowCount].IDFIELD == "01" ? "01-OPC????????????":"02-????????????????????????";
								}else if(data[rowCount].CHG_PCODE=="1200"){
									data[rowCount].IDFIELD ="01-????????????????????????";
								}
							}
							</logic:equal>
							<logic:notEqual name="login_form" property="userData.s_auth_type" value="A">
							for(var rowCount in data){
								data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
								data[rowCount].CHG_PCODE = data[rowCount].CHG_PCODE=="1210"?"????????????????????????":(data[rowCount].CHG_PCODE=="1200"?"????????????????????????":data[rowCount].CHG_PCODE);
								if(data[rowCount].CHG_PCODE=="1210"){
									data[rowCount].IDFIELD = data[rowCount].IDFIELD == "01"?"01-OPC????????????":"02-????????????????????????";
								}else if(data[rowCount].CHG_PCODE=="1200"){
									data[rowCount].IDFIELD = data[rowCount].IDFIELD=="01-????????????????????????";
								}
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
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
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
// 						searchDataByDate($("#TXDATE").val(),'','');
// 						startInterval(searchDataByDate, [$("#TXDATE").val(),'','']);
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
						blockUI();
						clearInterval(queryInterval);
// 						searchDataByDate($("#TXDATE").val(), $("#IDFIELD").val(), $("#OPBK_ID").val());
						searchDataByDate($("#TXDATE").val(), $('input[name=CHG_PCODE]:checked').val() ,$("#IDFIELD").val(), $("#OPBK_ID").val() , $("#RSPCODE").val());
					//??????	
					}else if(str == "add"){
						change_IDFIELD_validation(true);
						if(!jQuery('#formID').validationEngine('validate')){
							change_IDFIELD_validation(false);return false;
						}
						if(confirm("???????????????????")){
							blockUI();
// 							var stan = send($("#OPBK_ID").val(), $("#IDFIELD").val() );
							var stan = send($("#OPBK_ID").val(), $("#IDFIELD").val()  ,$('input[name=CHG_PCODE]:checked').val());
							change_IDFIELD_validation(false);
							if(stan != null){
								searchDataByStan(stan);
								startInterval(searchDataByStan, [stan]);
							}
						}
					}
					unblockUI();
				}
			}
			
			function send(bgbkId, idField ,pcode){
// 				var rdata = {component:"chg_mac_bo",method:"send",OPBK_ID:bgbkId,IDFIELD:idField};
// 				var rdata = {component:"chg_mac_bo",method:"send",OPBK_ID:bgbkId,IDFIELD:idField ,CHG_PCODE:pcode };
				var rdata = {component:"chg_mac_bo",method:"send",OPBK_ID:bgbkId,IDFIELD:idField ,CHG_PCODE:pcode ,action:$("#formID").attr('action')};
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
// 				var rdata = {component:"chg_mac_bo",method:"resend",DATA_LIST:chkList.toString()};
// 				var rdata = {component:"chg_mac_bo",method:"resend",DATA_LIST:chkList.toString() ,serchStrs:$("#serchStrs").val()};
				var rdata = {component:"chg_mac_bo",method:"resend",DATA_LIST:chkList.toString() ,action:$("#formID").attr('action')};
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					var failList = [];
					var successList = [];
					//???????????????????????????
					for(var stan in msg){
						if(msg[stan].result == "FALSE"){
							failList.push(msg[stan].STAN);
						}else if(msg[stan].result == "TRUE"){
							successList.push(msg[stan]);
//							20150421 add by hugo ?????? ??????????????????STAN ?????????
							stans=msg[stan].STAN;
						}
					}
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
				searchData("component=chg_mac_bo&method=getDataByStan&STAN="+stan);
			}
			
			function searchDataByDate(date, pcode,idField, bgbkId , rspcode){
				if(window.console){console.log("DATE=" + date);}
// 				searchData("component=chg_mac_bo&method=getDataByDate&TXDATE="+date+"&IDFIELD="+idField+"&BGBKID="+bgbkId);
// 				searchData("component=chg_mac_bo&method=getDataByDate&TXDATE="+date+"&IDFIELD="+idField+"&BGBKID="+bgbkId+"&RSPCODE="+rspcode+"&CHG_PCODE="+pcode);
				searchData("component=chg_mac_bo&method=getDataByDate&TXDATE="+date+"&IDFIELD="+idField+"&BGBKID="+bgbkId+"&RSPCODE="+rspcode+"&CHG_PCODE="+pcode+"&serchStrs="+$("#serchStrs").val());
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
			
			function change_IDFIELD_validation(toValidate){
				if(toValidate){
					$("#IDFIELD").addClass("validate[required]");
				}else{
					$("#IDFIELD").removeClass("validate[required]");
				}
			}
			
			function startInterval(func, values){
				clearInterval(queryInterval);
				queryInterval = setRepeater(func, values, 30);
			}
			
			
			function disabled_Idfield(str){
				if(str=="1200"){
					$("#IDFIELD").val("");
					$("#IDFIELD").attr("disabled" , true);
					change_IDFIELD_validation(false);
				}else{
					$("#IDFIELD").attr("disabled" , false);
					change_IDFIELD_validation(false);
				}
			}
			
			function pcode_formatter(cellvalue, options, rowObject){
				var status = rowObject.CHG_PCODE;
				var retstr = "";
				switch (status) {
				case "1200":
					retstr = "????????????????????????" ;
					break;
				case "1210":
					retstr = "????????????????????????" ;
					break;
				default:
					retstr = "???????????????"+rowObject.CHG_PCODE ;
					break;
				}
				return retstr;
			}
		</script>
	</body>
</html>