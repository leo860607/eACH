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
		<style type="text/css">
		.ser1{}
		.ser2{}
		</style>
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
				<html:form styleId="formID" action="/proxy_cl_rpt">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<bean:define id="userData" name="login_form" property="userData" ></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header necessary" style="width: 16%">營業日期</td>
								<td style="width: 30%">
									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[ maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>								
								</td>
								<td class="header necessary" >代理清算行代號</td>
								<logic:equal name="userData" property="USER_TYPE" value="A">
								<td>
									<html:select styleId="CTBK_ID" property="CTBK_ID" styleClass="ser1 ,validate[ required]">
										<html:option value="">===請選擇代理清算行代號===</html:option>
<%-- 										<html:optionsCollection name="proxy_cl_form" property="ct_bankList" label="label" value="value" /> --%>
										<html:optionsCollection name="proxy_cl_form" property="proxy_cl_bankList" label="label" value="value" />
									</html:select>
								</td>	
								</logic:equal>	
								<logic:equal name="userData" property="USER_TYPE" value="B">
								<td>
									<html:text name="proxy_cl_form" property="CTBK_NAME" readonly="true" styleClass=" lock "></html:text>
									<html:hidden name="proxy_cl_form" property="CTBK_ID" ></html:hidden>
								</td>
								</logic:equal>	
								
							</tr>
							<tr>
								<td class="header" >清算階段</td>
								<td>
									<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="">全部</html:option>
										<html:option value="01">01</html:option>
										<html:option value="02">02</html:option>
									</html:select>
								</td>					
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;產生報表</label>
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
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#BIZDATE").val('<bean:write name="proxy_cl_form" property="BIZDATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="proxy_cl_form" property="msg"/>';
				var result = '<bean:write name="proxy_cl_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
// 					alert(msg);
					window.open(msg);
				}
				if(result == "FALSE"){
 					alert(msg);
				}
			}
			
			function onPut(str){
// 				blockUI();
				if($("#formID").validationEngine('validate')){
					blockUIForDownload();
					$("#ac_key").val(str);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("form").submit();
				}
			}
			function lockoth(obj , obj2){
				$(obj).attr('disabled' , false);
				$(obj2).attr('disabled' , true);
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