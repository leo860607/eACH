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
				<html:form styleId="formID" action="/rptst_10">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header necessary" style="width: 16%">營業年月</td>
								<td style="width: 40%">
									民國&nbsp;
									<html:text name="rptst_10_form" styleId="TW_YEAR" property="TW_YEAR" size="4" maxlength="4" styleClass="validate[required]"></html:text>
									&nbsp;年&nbsp;
									<html:select name="rptst_10_form" styleId="TW_MONTH" property="TW_MONTH">
										<html:option value="">全部</html:option>
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
									</html:select>
									&nbsp;月
								</td>
								<td class="header" style="width: 100px">業務類別</td>
								<td>
									<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID">
										<html:option value="all">全部</html:option>
										<logic:present name="rptst_10_form" property="pcodeList">
											<html:optionsCollection name="rptst_10_form" property="pcodeList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
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
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rptst_10_form" property="msg"/>';
				var result = '<bean:write name="rptst_10_form" property="result"/>';
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