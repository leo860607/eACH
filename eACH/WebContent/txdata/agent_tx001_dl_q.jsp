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
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
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
				<html:form styleId="formID" action="/agent_dl_tx001">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 發動者端  -->
							<logic:notEqual name="userData" property="USER_TYPE" value="A">
							<tr>
								<td class="necessary header" style="width: 16%">營業日</td>
								<td style="width: 30%">
									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
									<html:hidden styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" ></html:hidden>
							</tr>	
							</logic:notEqual>
							<!-- 票交端 -->
							<logic:equal name="userData" property="USER_TYPE" value="A">
							<tr>
								<td class="necessary header" style="width: 16%">營業日</td>
								<td style="width: 30%">
									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								
								<td class="header">代理業者統一編號</td>
								<td>
								<html:select styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" onchange = "getSnd_Com_List()" styleClass="validate[required]">
									<html:option value="">====請選擇====</html:option>
									<html:optionsCollection name = "rpt_agent_form" property="company_IdList" label="label" value="value"/>
								</html:select>
								</td>
							</tr>
							<tr>
								<td class="header">清算階段</td>
							    <td>
							    <html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
							    <html:option value="01">01</html:option>
							    <html:option value="02">02</html:option>
							    </html:select>
							    </td>
								<td class="header">發動者統一編號</td>
								<td>
								<html:select styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" >
									<html:option value="">全部</html:option>
								</html:select>
								</td>
							</tr>
							</logic:equal>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="download" onclick="download(this.id)"><img src="./images/import.png"/>&nbsp;下載</label>
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
				<logic:notEqual name="userData" property="USER_TYPE" value="A">
				getSnd_Com_List();
				</logic:notEqual>
				$("#BIZDATE").val('<bean:write name="rpt_agent_form" property="BIZDATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="rpt_agent_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
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
			
			
			function download(ac_key){
				if($("#formID").validationEngine("validate")){
					$("#ac_key").val(ac_key);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					//判斷submit()是否完成
					blockUIForDownload();
					//這裡可以看出Struts的特色，在action層return null就可以停在原頁面不動，效果如同ajax不換頁
					$("form").submit();
				}
			}
			function blockUIForDownload(){
			    var token = new Date().getTime();
			    $('#dow_token').val(token);
			    blockUI();
			    //每一秒都確認目前的cookie是否與server端set的cookie一樣，如果一樣表示server端處理完成
			    fileDownloadCheckTimer = window.setInterval(function () {
			      var cookieValue = $.cookie('fileDownloadToken');
			      if (cookieValue == token){
			      	finishDownload();
			      }
			    }, 1000);
			}
			
			function finishDownload(){
				window.clearInterval(fileDownloadCheckTimer);
				$.cookie('fileDownloadToken',null);
				unblockUI();
			}
		</script>
	</body>
</html>