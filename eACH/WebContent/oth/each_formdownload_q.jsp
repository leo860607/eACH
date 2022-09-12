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
				<html:form styleId="formID" action="/each_formdownload">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="filename" styleId="filename" value=""/>
					<html:hidden property="downloadToken" styleId="downloadToken"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
				</html:form>
			</div>
			<div id="dataInputTable">
				<table>
				<logic:iterate id="dataListMap" name="each_formdownload_form" property="dataListMap">
				<bean:define id="Header" name="dataListMap" property="Header"></bean:define>
						<tr>
							<td style="font-weight:bold" colspan="2">
								<img src="./images/icon-down.png"/><bean:write name="Header"/>
							</td>
						</tr>
					<logic:iterate id="FileList" name="dataListMap" property="FileList">
						<tr>
							<logic:iterate id="file" name="FileList" indexId="num">
								<logic:equal name="num" value="1">
									<bean:define id="filename" name="file"></bean:define>
								</logic:equal>
							</logic:iterate>
							<td style="text-align:center;width:6%"><button type="button" onclick="download('<bean:write name="filename"/>')"><img src="./images/import.png"/></button></td>
							<logic:iterate id="file" name="FileList" indexId="num">
								<logic:equal name="num" value="0">
									<td><bean:write name="file"/></td>
								</logic:equal>
							</logic:iterate>
						</tr>
					</logic:iterate>
				</logic:iterate>
				</table>
				<br>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		
			//判斷檔案是否出現下載頁籤的變數
			var fileDownloadCheckTimer;
		
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
			}
			
			function alterMsg(){
 				var msg = '<bean:write name="each_formdownload_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			function download(filename){
				//因為中文要傳到後端比對，所以這裡要用encode，到後端再decode，否則中文會變亂碼
				$("#filename").val(encodeURI(filename));
				$("#ac_key").val('download');
				$("#target").val('search');
				var serchs = {};
				serchs['FILENAMES'] = filename;
				serchs['action'] = $("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
				//判斷submit()是否完成
				blockUIForDownload();
				//這裡可以看出Struts的特色，在action層return null就可以停在原頁面不動，效果如同ajax不換頁
				$("form").submit();
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
		</script>
	</body>
</html>