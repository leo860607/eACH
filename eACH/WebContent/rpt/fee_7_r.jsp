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
				<html:form styleId="formID" action="/rptfee_7">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								<tr>
									<td class="header necessary" style="width: 16%">營業年月</td>
									<td style="width: 30%">
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
									</td>
									<td class="header" style="width: 16%">操作行代號</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List_GE_MS(this.value);getBrbk_List($('#BGBK_ID').val())">
											<html:option value="">全部</html:option>
											<logic:present name="rptfee_7_form" property="opbkIdList">
												<html:optionsCollection name="rptfee_7_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<!-- 銀行端 -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
								<tr>
									<td class="header necessary" style="width: 16%">營業年月</td>
									<td style="width: 30%">
										民國&nbsp;<html:text styleId="TW_YEAR" property="TW_YEAR" size="4" maxlength="4" styleClass="validate[required,minSize[4],maxSize[4],twYear]"></html:text>&nbsp;年&nbsp;
										<html:select styleId="TW_MONTH" property="TW_MONTH" styleClass="validate[required]">
											<html:option value="01">一</html:option>
											<html:option value="02">二</html:option>
											<html:option value="03">三</html:option>
											<html:option value="04">四</html:option>
											<html:option value="05">五</html:option>
											<html:option value="06">六</html:option>
											<html:option value="07">七</html:option>
											<html:option value="08">八</html:option>
											<html:option value="09">九</html:option>
											<html:option value="10">十</html:option>
											<html:option value="11">十一</html:option>
											<html:option value="12">十二</html:option>
										</html:select>&nbsp;月
									</td>
									<td style="width: 16%">
										<bean:define id="userCompany" name="login_form" property="userData.USER_COMPANY"/>
										<html:hidden styleId="OPBK_ID" property="OPBK_ID" value="<%=(String)userCompany %>"/>
									</td>
									<td></td>
								</tr>
								<tr>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
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
				setDateOnChange($("#TW_YEAR") ,getBgbk_List_GE_MS);
				setDateOnChange($("#TW_MONTH") ,getBgbk_List_GE_MS);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
// 				getBgbk_List($("#OPBK_ID").val());
				getBgbk_List_GE_MS($("#OPBK_ID").val());
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rptfee_7_form" property="msg"/>';
				var result = '<bean:write name="rptfee_7_form" property="result"/>';
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
			
			function getBrbk_List(bgbkId){
				if(bgbkId == ''){
					$("#BRBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_branch_bo", method:"getBank_branch_List" , bgbkId:bgbkId  };
					fstop.getServerDataExII(uri, rdata, false,creatBrBkList);
				}
			}	
			
			function creatBrBkList(obj){
				var select = $("#BRBK_ID");
				$("#BRBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
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