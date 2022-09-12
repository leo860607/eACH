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
		<title>><bean:write name="login_form" property="userData.s_func_name"/></title>
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
				<html:form styleId="formID" action="/rptst_2">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header necessary" style="width: 16%">營業月份區間</td>
<!-- 								<td style="width: 30%"> -->
<%-- 									<html:text styleId="START_MONTH" property="START_MONTH" size="6" maxlength="6" styleClass="validate[required, maxSize[6],minSize[6],twDate] text-input datepicker"></html:text>~ --%>
<%-- 									<html:text styleId="END_MONTH" property="END_MONTH" size="6" maxlength="6" styleClass="validate[required, maxSize[6],minSize[6],twDate] text-input datepicker"></html:text> --%>
<!-- 								</td> -->
                                    <td style="width: 40%">
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
									</td>
								<td class="header">操作行代號</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List_GE_M(this.value)">
										<html:option value="">全部</html:option>
										<logic:present name="rptst_2_form" property="opbkIdList">
											<html:optionsCollection name="rptst_2_form" property="opbkIdList" label="label" value="value" />
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="header">總行代號</td>
								<td>
									<html:select styleId="BGBK_ID" property="BGBK_ID">
										<html:option value="">全部</html:option>
									</html:select>
								</td>
								<td class="header">發動者統編</td>
								<td>
									<html:text styleId="SENDERID" property="SENDERID" size="11" maxlength="10" styleClass="validate[maxSize[10]] text-input"></html:text>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search_pdf" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;產生報表(PDF)</label>
									<label class="btn" id="search_xls" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;產生報表(EXCEL)</label>
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
				setDatePicker();
				setDateOnChange($("#START_YEAR") ,getBgbk_List_GE_M);
				setDateOnChange($("#END_YEAR") ,getBgbk_List_GE_M);
				setDateOnChange($("#START_MONTH") ,getBgbk_List_GE_M);
				setDateOnChange($("#END_MONTH") ,getBgbk_List_GE_M);
				
				
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#START_MONTH").val('<bean:write name="rptst_2_form" property="START_YEAR"/>');
				$("#START_MONTH").val('<bean:write name="rptst_2_form" property="START_MONTH"/>');
				$("#END_MONTH").val('<bean:write name="rptst_2_form" property="END_YEAR"/>');
				$("#END_MONTH").val('<bean:write name="rptst_2_form" property="END_MONTH"/>');
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rptst_2_form" property="msg"/>';
				var result = '<bean:write name="rptst_2_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
					window.open(msg);
				}
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					blockUIForDownload();
					$("#ac_key").val(str);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("form").submit();
				}
			}
			
// 			function getBgbk_List(opbkId){
// 				if(opbkId == '' || opbkId == "all"){
// 					$("#BGBK_ID option:not(:first-child)").remove();
// 				}else{
// 					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId};
// 					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
// 				}
// 			}
			
// 			function creatBgBkList(obj){
// 				var select = $("#BGBK_ID");
// 				$("#BGBK_ID option:not(:first-child)").remove();
// 				if(obj.result=="TRUE"){
// 					var dataAry =  obj.msg;
// 					for(var i = 0; i < dataAry.length; i++){
// 						select.append($("<option></option>").attr("value", dataAry[i].BGBK_ID).text(dataAry[i].BGBK_ID + " - " + dataAry[i].BGBK_NAME));
// 					}
// 				}else if(obj.result=="FALSE"){
// 					alert(obj.msg);
// 				}
// 			}
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
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>