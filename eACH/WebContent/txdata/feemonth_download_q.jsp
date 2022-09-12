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
				<html:form styleId="formID" action="/feemonth_download">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="downloadToken" styleId="downloadToken"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="necessary header" style="width: 16%">營業年月</td>
								<td style="width: 30%">
									<html:text styleId="BIZYM" property="BIZYM" size="7" maxlength="7" styleClass="validate[required,maxSize[7],minSize[7],funcCall[checkBIZYM]]"></html:text>
								</td>
								<!-- 票交端 -->
								<logic:equal name="userData" property="USER_TYPE" value="A">
									<td class="header">操作行</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID">
											<html:option value="all">全部</html:option>
											<logic:present name="feemonth_download_form" property="opbkIdList">
												<html:optionsCollection name="feemonth_download_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</logic:equal>
								<!-- 銀行端-->
								<logic:equal name="userData" property="USER_TYPE" value="B">
									<html:hidden styleId="OPBK_ID" property="OPBK_ID"></html:hidden>
								</logic:equal>
							</tr>
							<tr>
								<td  style="padding-top: 5px">
									<label class="btn" id="download" onclick="download(this.id)"><img src="./images/import.png"/>&nbsp;下載</label>
								</td>
								<td  style="padding-top: 5px">
									<label class="btn" id="downloadold" onclick="download(this.id)"><img src="./images/import.png"/>&nbsp;下載(舊版手續費)</label>
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
				$("#BIZYM").val('<bean:write name="feemonth_download_form" property="BIZYM"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="feemonth_download_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
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
			    $('#downloadToken').val(token);
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
			
			function checkBIZYM(){
				var str = $("#BIZYM").val();
				if(str != ""){
					var patt = new RegExp("0[0-9][0-9][0-9]/(0[1-9]|1[0-2])$");
					var res = patt.test(str);
					if(!res){
						return "年月格式錯誤! ex:0104/03";
					}
				}
				return true;
			}
		</script>
	</body>
</html>