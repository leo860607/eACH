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
		<script type="text/javascript" src="./js/jquery.cookie.js"></script>
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
				<html:form styleId="formID" action="/rptst_17">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<bean:define id="userData" name="login_form" property="userData" ></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id = "search_tab">
							<tr>
							<td class="header necessary" style="width: 16%">營業月份區間</td>
							<td style="width: 40%">
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								民國&nbsp;<html:text styleId="START_YEAR" property="START_YEAR" size="4" maxlength="4" styleClass="validate[required,minSize[4],maxSize[4],twYear]"></html:text>&nbsp;年&nbsp;
								<html:select styleId="START_MONTH" property="START_MONTH" styleClass="validate[required]">
									<html:option value="01">01</html:option>
									<html:option value="02">02</html:option>
									<html:option value="03">03</html:option>
									<html:option value="04">04</html:option>
									<html:option value="05">05</html:option>
									<html:option value="06">06</html:option>
									<html:option value="07">07</html:option>
									<html:option value="08">08</html:option>
									<html:option value="09">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
								</html:select>&nbsp;月 - 
								民國&nbsp;<html:text styleId="END_YEAR" property="END_YEAR" size="4" maxlength="4" styleClass="validate[required,minSize[4],maxSize[4],twYear]"></html:text>&nbsp;年&nbsp;
								<html:select styleId="END_MONTH" property="END_MONTH" styleClass="validate[required]">
									<html:option value="01">01</html:option>
									<html:option value="02">02</html:option>
									<html:option value="03">03</html:option>
									<html:option value="04">04</html:option>
									<html:option value="05">05</html:option>
									<html:option value="06">06</html:option>
									<html:option value="07">07</html:option>
									<html:option value="08">08</html:option>
									<html:option value="09">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
								</html:select>&nbsp;月
								</logic:equal>
								<logic:notEqual name="login_form" property="userData.USER_TYPE" value="A">
								民國&nbsp;<html:text styleId="TW_YEAR" property="TW_YEAR" size="4" maxlength="4" styleClass="validate[required,minSize[4],maxSize[4],twYear]"></html:text>&nbsp;年&nbsp;
								<html:select styleId="TW_MONTH" property="TW_MONTH" styleClass="validate[required]">
									<html:option value="01">01</html:option>
									<html:option value="02">02</html:option>
									<html:option value="03">03</html:option>
									<html:option value="04">04</html:option>
									<html:option value="05">05</html:option>
									<html:option value="06">06</html:option>
									<html:option value="07">07</html:option>
									<html:option value="08">08</html:option>
									<html:option value="09">09</html:option>
									<html:option value="10">10</html:option>
									<html:option value="11">11</html:option>
									<html:option value="12">12</html:option>
								</html:select>&nbsp;月 
								</logic:notEqual>
							</td>
								<td class="header" style="width: 16%">交易代號</td>
								<td >
								<html:select styleId="TXID" property="TXID" >
									<html:option value="">全部</html:option>
									<html:optionsCollection name = "rpt_agent_form" property="txnIdList" label="label" value="value"/>
								</html:select>
								</td>
								
							</tr>
							<tr>
								<td class="header">代理業者統一編號</td>
								<td>
								<logic:equal name="userData" property="USER_TYPE" value="A">
								<html:select styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" onchange = "getSnd_Com_List()" >
									<html:option value="">全部</html:option>
									<html:optionsCollection name = "rpt_agent_form" property="company_IdList" label="label" value="value"/>
								</html:select>
								</logic:equal>
								<!-- 發動者端  -->
								<logic:notEqual name="userData" property="USER_TYPE" value="A">
									<bean:write name="rpt_agent_form" property="AGENT_COMPANY_ID"/>
									<html:hidden styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" ></html:hidden>
								</logic:notEqual>
								</td>
								<td class="header">發動者統一編號</td>
								<td>
								<html:select styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" >
									<html:option value="">全部</html:option>
								</html:select>
								</td>
							</tr>
							<tr>
								<td class="header" style="width: 100px">交易結果</td>
								<td >
									<html:select styleId="TG_RESULT" property="TG_RESULT">
										<html:option value="">全部</html:option>
										<html:option value="A">成功</html:option>
										<html:option value="R">失敗</html:option>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;產生報表</label>
									<label class="btn" id="xls" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;產生報表(EXCEL)</label>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				<logic:equal name="userData" property="USER_TYPE" value="A">
					setDatePickerII($("#SBIZDATE"),'<bean:write name="rpt_agent_form" property="SBIZDATE"/>');
					setDatePickerII($("#EBIZDATE"),'<bean:write name="rpt_agent_form" property="EBIZDATE"/>');
				</logic:equal>
				<logic:notEqual name="userData" property="USER_TYPE" value="A">
					setDatePickerII($("#BIZDATE"),'<bean:write name="rpt_agent_form" property="BIZDATE"/>');
				getSnd_Com_List();
				</logic:notEqual>
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rpt_agent_form" property="msg"/>';
				var result = '<bean:write name="rpt_agent_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
// 					alert(msg);
					window.open(msg);
				}else if(fstop.isNotEmptyString(result) && result =="FALSE"){
					alert(msg);
				}
			}
			
			function onPut(str){
				if($("#formID").validationEngine('validate')){
					blockUIForDownload();
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("#ac_key").val(str);
					$("#target").val('search');
					$("form").submit();
				}
			}
			

			function getSnd_Com_List(){
				var com_id = "";
				com_id = $("#AGENT_COMPANY_ID").val();
				if(com_id == '' || com_id == "all"){
					$("#SND_COMPANY_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"txnlog_bo", method:"getSnd_Com_List", COMPANY_ID:com_id };
					fstop.getServerDataExII(uri, rdata, false, creatSnd_Com_List);
				}
			}
			

			function creatSnd_Com_List(obj){
				var select = $("#SND_COMPANY_ID");
				$("#SND_COMPANY_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].SND_COMPANY_ID).text(dataAry[i].SND_COMPANY_ID + " - " + dataAry[i].SND_COMPANY_ABBR_NAME));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
// 			==================此區塊為使用struts 輸出檔案會用到的API==================
			var fileDownloadCheckTimer;
			function blockUIForDownload() {
			    var token = new Date().getTime(); //use the current timestamp as the token value
			    $('#dow_token').val(token);
			    blockUI();
			    fileDownloadCheckTimer = window.setInterval(function () {
			      var cookieValue = $.cookie('fileDownloadToken');
			      if (cookieValue == token)
			       finishDownload();
			    }, 1000);
			}
			function finishDownload(){
				window.clearInterval(fileDownloadCheckTimer);
// 				$.removeCookie('fileDownloadToken' , null); //clears this cookie value 此方式chrome不適用
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>