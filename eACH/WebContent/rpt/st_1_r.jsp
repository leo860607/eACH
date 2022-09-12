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
				<html:form styleId="formID" action="/rptst_1">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								<tr>
									<td class="header necessary" style="width: 16%">營業日區間</td>
									<td style="width: 30%">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
										~<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 16%">處理結果</td>
									<td>
										<html:select styleId="RESULTSTATUS" property="RESULTSTATUS">
											<html:option value="">全部</html:option>
											<html:option value="A">成功</html:option>
											<html:option value="R">失敗</html:option>
											<html:option value="P">未完成</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="">全部</html:option>
											<html:option value="01">01</html:option>
											<html:option value="02">02</html:option>
										</html:select>
									</td>
									<td class="header">操作行代號</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
											<html:option value="">全部</html:option>
											<logic:present name="rptst_1_form" property="opbkIdList">
												<html:optionsCollection name="rptst_1_form" property="opbkIdList" label="label" value="value" />
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="">全部</html:option>
											<logic:present name="rptst_1_form" property="pcodeList">
												<html:optionsCollection name="rptst_1_form" property="pcodeList" label="label" value="value" />
											</logic:present>
										</html:select>
									</td>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<!-- 銀行端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
								<tr>
									<td class="header necessary" style="width: 16%">營業日</td>
									<td style="width: 30%">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
										<html:hidden styleId="END_DATE" property="END_DATE"></html:hidden>
									</td>
									<td class="header" style="width: 16%">處理結果</td>
									<td>
										<html:select styleId="RESULTSTATUS" property="RESULTSTATUS">
											<html:option value="">全部</html:option>
											<html:option value="A">成功</html:option>
											<html:option value="R">失敗</html:option>
											<html:option value="P">未完成</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="">全部</html:option>
											<html:option value="01">01</html:option>
											<html:option value="02">02</html:option>
										</html:select>
									</td>
									<td class="header">總行代號</td>
									<td>
										<!-- 操作行代號 -->
										<html:hidden styleId="OPBK_ID" property="OPBK_ID"></html:hidden>
										<html:select styleId="BGBK_ID" property="BGBK_ID">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">交易類別</td>
									<td>
										<html:select styleId="PCODE" property="PCODE">
											<html:option value="">全部</html:option>
											<logic:present name="rptst_1_form" property="pcodeList">
												<html:optionsCollection name="rptst_1_form" property="pcodeList" label="label" value="value" />
											</logic:present>
										</html:select>
									</td>
									<td></td><td></td>
								</tr>
							</logic:equal>
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
				setDateOnChange($("#START_DATE") ,getBgbk_List);
				setDateOnChange($("#END_DATE") ,getBgbk_List);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#START_DATE").val('<bean:write name="rptst_1_form" property="START_DATE"/>');
				$("#END_DATE").val('<bean:write name="rptst_1_form" property="END_DATE"/>');
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
					getBgbk_List($("#OPBK_ID").val());
				</logic:equal>
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rptst_1_form" property="msg"/>';
				var result = '<bean:write name="rptst_1_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
					window.open(msg);
				}
			}
			
			function onPut(str){
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
				$("#END_DATE").val($("#START_DATE").val());
				</logic:equal>
				if($("#formID").validationEngine("validate")){
					blockUIForDownload();
					$("#ac_key").val(str);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("form").submit();
				}
			}
			
			function getBgbk_List(opbkId){
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
				}
			}
			
			function getBgbk_List(opbkId){
				var s_bizdate = $("#START_DATE").val();
				var e_bizdate = $("#END_DATE").val();
				opbkId = $("#OPBK_ID").val();
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
				e_bizdate = "";
				</logic:equal>
				
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate ,e_bizdate:e_bizdate};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
				}
			}
			
			function creatBgBkList(obj){
				var select = $("#BGBK_ID");
				$("#BGBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].BGBK_ID).text(dataAry[i].BGBK_ID + " - " + dataAry[i].BGBK_NAME));
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
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>