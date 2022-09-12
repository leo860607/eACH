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
				<bean:define id="userData" name="login_form" property="userData"></bean:define>
				<html:form styleId="formID" action="/rptst_14">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab" style="width: 100%">
							<tr>
								<%-- 票交端 --%>
								<logic:equal name="userData" property="USER_TYPE" value="A">
									<td class="header necessary" style="width: 13%">營業月份區間</td>
									<td style="width: 25%">
										民國&nbsp;
										<html:text name="rptst_14_form" styleId="TW_YEAR" property="TW_YEAR" size="4" maxlength="4" styleClass="validate[required]"></html:text>
										&nbsp;年&nbsp;
										<html:select name="rptst_14_form" styleId="START_TW_MONTH" property="START_TW_MONTH">
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
										~
										<html:select name="rptst_14_form" styleId="END_TW_MONTH" property="END_TW_MONTH">
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
									<td class="header" style="width: 100px">操作行代號</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID">
											<html:option value="all">全部</html:option>
											<logic:present name="rptst_14_form" property="opbkIdList">
												<html:optionsCollection name="rptst_14_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</logic:equal>
								
								<%-- 銀行端 --%>
								<logic:equal name="userData" property="USER_TYPE" value="B">
									<td class="header necessary" style="width: 13%">營業月份</td>
									<td style="width: 20%">
										民國&nbsp;
										<html:text name="rptst_14_form" styleId="TW_YEAR" property="TW_YEAR" size="4" maxlength="4" styleClass="validate[required]"></html:text>
										&nbsp;年&nbsp;
										<html:select name="rptst_14_form" styleId="START_TW_MONTH" property="START_TW_MONTH">
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
										</html:select>
										<html:hidden property="END_TW_MONTH" styleId="END_TW_MONTH"/>
										&nbsp;月
									</td>
									<td class="header" style="width: 100px">操作行代號</td>
									<td>
										<html:text name="rptst_14_form" property="OPBK" styleId="OPBK" readonly="true" styleClass="lock"></html:text>
										<html:hidden name="rptst_14_form" property="OPBK_ID" styleId="OPBK_ID"></html:hidden>
									</td>
								</logic:equal>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search_pdf" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;產生報表(PDF)</label>
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
				var msg = '<bean:write name="rptst_14_form" property="msg"/>';
				var result = '<bean:write name="rptst_14_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
					window.open(msg);
				}
			}
			
			function onPut(str){
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#END_TW_MONTH").val($("#START_TW_MONTH").val());
				</logic:equal>
				
				if($("#formID").validationEngine("validate")){
					blockUIForDownload();
					$("#ac_key").val(str);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("form").submit();
				}
			}
			
			//==================此區塊為使用struts 輸出檔案會用到的API==================
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