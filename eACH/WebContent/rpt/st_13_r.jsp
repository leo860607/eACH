<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- 整批作業日報表 --%>
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
		<style type="text/css">
			#search_tab {width: 100%}
		</style>
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
				<html:form styleId="formID" action="/rptst_13">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<bean:define id="userData" name="login_form" property="userData" ></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<%-- 票交端 --%>
								<logic:equal name="userData" property="USER_TYPE" value="A">
									<td class="header necessary" style="width: 15%">上傳營業日期區間</td>
									<td style="width: 20%">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>
										~<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
								</logic:equal>
							
								<%-- 銀行端 --%>
								<logic:greaterEqual name="userData" property="USER_TYPE" value="B">
									<td class="header necessary" style="width: 15%">上傳營業日</td>
									<td style="width: 20%">
										<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
										<html:hidden styleId="END_DATE" property="END_DATE"/>
									</td>
								</logic:greaterEqual>
								<td class="header" style="width: 100px">清算階段</td>
								<td >
									<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="">全部</html:option>
										<html:option value="01">01</html:option>
										<html:option value="02">02</html:option>
									</html:select>
								</td>
							</tr>
							<tr>
								<%-- 票交端 --%>
								<logic:equal name="userData" property="USER_TYPE" value="A">
									<td class="header" style="width: 100px">操作行代號</td>
									<td colspan="3">
										<html:hidden name="rptst_13_form" property="OPBK" styleId="OPBK"/>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
											<html:option value="">全部</html:option>
											<html:optionsCollection name="rptst_13_form" property="opbkIdList" label="label" value="value" />
										</html:select>
									</td>
								</logic:equal>
								
								<%-- 銀行端 --%>
								<logic:greaterEqual name="userData" property="USER_TYPE" value="B">
									<td class="header" style="width: 100px">操作行代號</td>
									<td colspan="3">
										<html:text name="rptst_13_form" property="OPBK" styleId="OPBK" readonly="true" styleClass="lock"></html:text>
										<html:hidden name="rptst_13_form" property="OPBK_ID" styleId="OPBK_ID"></html:hidden>
									</td>
								</logic:greaterEqual>
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
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				
				setDatePickerII($("#START_DATE"),'<bean:write name="rptst_13_form" property="START_DATE"/>');
				<logic:equal name="userData" property="USER_TYPE" value="A">
					setDatePickerII($("#END_DATE"),'<bean:write name="rptst_13_form" property="END_DATE"/>');
				</logic:equal>
				
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rptst_13_form" property="msg"/>';
				var result = '<bean:write name="rptst_13_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
					window.open(msg);
				}else if(fstop.isNotEmptyString(result) && result =="FALSE"){
					alert(msg);
				}
			}
			
			function onPut(str){
				<logic:equal name="userData" property="USER_TYPE" value="A">
					var text = $("#OPBK_ID").find(":selected").text();
					text = text.replace(/ /g, '');
					$("#OPBK").val(text);
				</logic:equal>
				
				<logic:greaterEqual name="userData" property="USER_TYPE" value="B">
					$("#END_DATE").val($("#START_DATE").val());
				</logic:greaterEqual>
				
				if($("#formID").validationEngine('validate')){
					blockUIForDownload();
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("#ac_key").val(str);
					$("#target").val('search');
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