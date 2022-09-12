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
				<html:form styleId="formID" action="/rptcl_2">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="opt_bank" styleId="opt_bank"/>
					<bean:define id="userData" name="login_form" property="userData" ></bean:define>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
<!-- 						e -->
						<table id = "search_tab">
							<tr>
								<td class="header necessary" style="width: 20%">營業日期</td>
								<td style="width: 30%">
									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required, maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<logic:equal name="userData" property="USER_TYPE" value="A">
								<td class="header" style="width: 100px">操作行代號</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value)">
										<html:option value="">全部</html:option>
										<html:optionsCollection name="rptcl_2_form" property="opt_bankList" label="label" value="value" />
									</html:select>
								</td>
								</logic:equal>
								<logic:greaterEqual name="userData" property="USER_TYPE" value="B">
									<td class="header" style="width: 100px">操作行代號</td>
									<td>
									<html:text name="rptcl_2_form" property="opt_bank" readonly="true" styleClass=" lock "></html:text>
									<html:hidden name="rptcl_2_form" styleId="OPBK_ID" property="OPBK_ID" ></html:hidden>
									</td>
								</logic:greaterEqual>
							</tr>
							<tr>
								<td class="header" style="width: 100px">清算階段</td>
								<td >
									<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="">全部</html:option>
										<html:option value="01">01</html:option>
										<html:option value="02">02</html:option>
									</html:select>
								</td>
								<td class="header">總行代號</td>
								<td >
									<html:select styleId="BGBK_ID" property="BGBK_ID">
										<html:option  value="">全部</html:option>
<%-- 										<logic:present name="rptcl_2_form" property="bgbkIdList"> --%>
										<logic:present name="rptcl_2_form" property="bg_bankList">
<%-- 											<html:optionsCollection name="rptcl_2_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection> --%>
											<html:optionsCollection name="rptcl_2_form" property="bg_bankList" label="label" value="value"></html:optionsCollection>
										</logic:present>
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
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePickerII($("#BIZDATE"),'<bean:write name="rptcl_2_form" property="BIZDATE"/>');
				setDateOnChange($("#BIZDATE") ,getBgbk_List);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
// 				$("#TXDT").val('<bean:write name="rptcl_2_form" property="TXDT"/>');
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rptcl_2_form" property="msg"/>';
				var result = '<bean:write name="rptcl_2_form" property="result"/>';
				if(fstop.isNotEmptyString(result) && result =="TRUE"){
// 					alert(msg);
					window.open(msg);
				}else if(fstop.isNotEmptyString(result) && result =="FALSE"){
					alert(msg);
				}
			}
			
			function onPut(str){
// 				blockUI();
				var text = $("#SENDERACQUIRE").find(":selected").text();
				text  = $.trim(text.substring(text.indexOf("-")+1,text.length)) ;
				if(window.console){console.log("text>>"+text);}
				$("#opt_bank").val(text);
				if($("#formID").validationEngine('validate')){
					blockUIForDownload();
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');//e
					$("#ac_key").val(str);
					$("#target").val('search');
					$("form").submit();
				}
			}
			function lockoth(obj , obj2){
				$(obj).attr('disabled' , false);
				$(obj2).attr('disabled' , true);
			}
			

			//取得該操作行所代理的總行清單
			function getBgbk_List(opbkId){
				var s_bizdate = $("#BIZDATE").val();
				opbkId = $("#OPBK_ID").val();
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate};
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
// 				$.removeCookie('fileDownloadToken' , null); //clears this cookie value 此方式chrome不適用
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>