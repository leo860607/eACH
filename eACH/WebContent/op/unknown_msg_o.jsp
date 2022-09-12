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
		<script type="text/javascript" src="./js/json2.js"></script>
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
				<html:form styleId="formID" action="/unknown_msg">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table>
							<!-- 票交端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								<tr>
									<td class="header necessary" style="width: 8%">日期</td>
									<td style="width: 30%">
										<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									<td style="width: 10%"></td><td></td>
								</tr>
								<tr>
									<td class="header">發動端</td>
									<td>
										<html:select styleId="SENDER_TYPE" property="SENDER_TYPE">
											<html:option value="">全部</html:option>
											<html:option value="TCH">交換所</html:option>
											<html:option value="BANK">銀行</html:option>
										</html:select>
										<html:select styleId="SENDERBANK" property="SENDERBANK">
										</html:select>
									</td>
									<td class="header">接收端</td>
									<td>
										<html:select styleId="RECEIVER_TYPE" property="RECEIVER_TYPE">
											<html:option value="">全部</html:option>
											<html:option value="TCH">交換所</html:option>
											<html:option value="BANK">銀行</html:option>
										</html:select>
										<html:select styleId="RECEIVERBANK" property="RECEIVERBANK">
										</html:select>
									</td>
								</tr>
							</logic:equal>
							<!-- 銀行端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
								<tr>
									<td class="header necessary" style="width:8%">日期</td>
									<td style="width: 20%">
										<html:hidden styleId="SENDER_TYPE" property="SENDER_TYPE"/>
										<html:hidden styleId="RECEIVER_TYPE" property="RECEIVER_TYPE"/>
										<html:hidden styleId="SENDERBANK" property="SENDERBANK"/>
										<html:hidden styleId="RECEIVERBANK" property="RECEIVERBANK"/>
										<html:text styleId="TXDATE" property="TXDATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width:10%">查詢角度</td>
									<td>
										<select id="TYPE_LIST">
											<option value="">全部</option>
											<option value="SENDER">發動端</option>
											<option value="RECEIVER">接收端</option>
										</select>
									</td>
								</tr>
							</logic:equal>
							<tr>
								<td colspan="4" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri="/eACH/baseInfo",gridOption;
			
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
				$("#formID").validationEngine({binded:false,promptPosition: "bottomRight"});
				
				$("#TXDATE").datepicker("setDate", new Date());
				$("#TXDATE").val('0' + $("#TXDATE").val());
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
					$("#SENDERBANK").hide();
					$("#RECEIVERBANK").hide();
					$("#SENDER_TYPE").change(function(){
						changeType("SENDER_TYPE", "SENDERBANK");
					});
					$("#RECEIVER_TYPE").change(function(){
						changeType("RECEIVER_TYPE", "RECEIVERBANK");
					});
				</logic:equal>
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					$("#TYPE_LIST").change(function(){
						$("input[id$='_TYPE']").val('');
						$("input[id$='BANK']").val('');
						
						var type = $(this).val();
						$("#"+type+"_TYPE").val('BANK');
						$("#"+type+"BANK").val('<bean:write name="login_form" property="userData.USER_COMPANY"/>');
					});
				</logic:equal>
			}
			
			function changeType(srcId, targetId){
				if($("#"+srcId).val() == "BANK"){
					$("#"+targetId).show();
					
					$("#"+targetId+" option:not(:first-child)").remove();
					var rdata = {component:"bank_group_bo", method:"getOpbkList"};
					fstop.getServerDataExII(uri, rdata, false, function(obj){
						var select = $("#"+targetId);
						if(obj.result=="TRUE"){
							var dataAry = obj.msg;
							for(var i = 0; i < dataAry.length; i++){
								select.append($("<option></option>").attr("value", dataAry[i].value).text(dataAry[i].label));
							}
						}else if(obj.result=="FALSE"){
							//alert(obj.msg);
						}
					});
				}else{
					$("#"+targetId).hide();
					$("#"+targetId).html("");
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="unknown_msg_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 260,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
						colNames:['交易日期','交易時間','發動端','接收端','交易代碼','交易名稱','原交易Header'],
		            	colModel: [
							{name:'PK_TXDATE',index:'PK_TXDATE',align:'center',fixed:true,width: 90,sortable:false}, 
							{name:'TXTIME',index:'TXTIME',fixed:true,width: 90},
							{name:'SENDERBANK',index:'SENDERBANK',fixed:true,width: 100},
							{name:'RECEIVERBANK',index:'RECEIVERBANK',fixed:true,width: 100},
							{name:'PCODE',index:'PCODE',fixed:true,width: 80},
							{name:'PNAME',index:'PNAME',fixed:true,width: 150},
							{name:'BASICDATA',index:'BASICDATA',fixed:true,width: 530}
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
							for(var rowCount in data){
								data[rowCount].PK_TXDATE = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd");
								data[rowCount].TXTIME = timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
							}
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
				if(!jQuery('#formID').validationEngine('validate')){
					return false;
				}
				//使用者操作軌跡用
				var serchs = {};
				serchs['DATE'] = $("#TXDATE").val();
				<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
					if($("#SENDER_TYPE").val() == ""){
						serchs['SENDER_TYPE'] = "all";
					}
					else if($("#SENDER_TYPE").val() == "BANK"){
						serchs['SENDER_TYPE'] = $("#SENDER_TYPE").val();
						serchs['SENDERBANK'] = $("#SENDERBANK").val();
					}
					else{
						serchs['SENDER_TYPE'] = $("#SENDER_TYPE").val();
					}
					if($("#RECEIVER_TYPE").val() == ""){
						serchs['RECEIVER_TYPE'] = "all";
					}
					else if($("#RECEIVER_TYPE").val() == "BANK"){
						serchs['RECEIVER_TYPE'] = $("#RECEIVER_TYPE").val();
						serchs['RECEIVERBANK'] = $("#RECEIVERBANK").val();
					}
					else{
						serchs['RECEIVER_TYPE'] = $("#RECEIVER_TYPE").val();
					}
				</logic:equal>
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					serchs['SENDER_TYPE'] = "BANK";
					serchs['SENDERBANK'] = $("#SENDERBANK").val();
					serchs['RECEIVER_TYPE'] = "BANK";
					serchs['RECEIVERBANK'] = $("#RECEIVERBANK").val();
					if($("#TYPE_LIST").val() == ""){
						serchs['QUERYVISION'] = "all";
					}
					else{
						serchs['QUERYVISION'] = $("#TYPE_LIST").val();
					}
				</logic:equal>
				serchs['action'] = $("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
				
				blockUI();
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
					if($("#SENDER_TYPE").val() == "" && $("#RECEIVER_TYPE").val() == ""){
						if($("#TXDATE").val() == ""){
							searchDataAll();
						}else{
							searchDataByDate();
						}
					}else{
						if($("#TXDATE").val() == ""){
							searchDataByStan();
						}else{
							searchDataByStanAndDate();
						}
					}
				</logic:equal>
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					searchDataByStanAndDate();
				</logic:equal>
				unblockUI();
			}
			
			function searchDataAll(){
				searchData("component=unknown_msg_bo&method=getDataAll&"+$("#formID").serialize());
			}
			
			function searchDataByStan(){
				//if(window.console){console.log("STAN=" + stan);}
				searchData("component=unknown_msg_bo&method=getDataByStan&"+$("#formID").serialize());
			}
			
			function searchDataByDate(){
				//if(window.console){console.log("DATE=" + date);}
				searchData("component=unknown_msg_bo&method=getDataByDate&"+$("#formID").serialize());
			}
			
			function searchDataByStanAndDate(){
				searchData("component=unknown_msg_bo&method=getDataByStanAndDate&"+$("#formID").serialize());
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
		</script>
	</body>
</html>