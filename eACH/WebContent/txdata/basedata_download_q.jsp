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
				<html:form styleId="formID" action="/basedata_download">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="downloadToken" styleId="downloadToken"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<html:hidden property="actionType" styleId="actionType"/>
					<html:hidden property="encType" styleId="encType"/>
					<fieldset>
						<table id="search_tab">
						
						<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="C">
							<tr>
								<td>總行資料</td>
							</tr>
							<tr>
								<td>分行資料</td>
							</tr>
							<tr>
								<td>代收發動者基本資料</td>
							</tr>
							<tr>
								<td>代付發動者基本資料</td>
							</tr>
							<tr>
								<td>業務類別資料</td>
							</tr>
							<tr>
								<td>交易代號資料</td>
							</tr>
							<tr>
								<td>回應代碼資料</td>
							</tr>
							<tr>
								<td>手續費資料</td>
							</tr>
<!-- 							<tr> -->
<!-- 								<td>退回天數資料</td> -->
<!-- 							</tr> -->
<!-- 							<tr> -->
<!-- 								<td>繳費入帳業者基本資料</td> -->
<!-- 							</tr> -->
							<tr>
								<td>收費業者基本資料</td>
							</tr>
							<tr>
								<td>新版代收發動者基本資料</td>
							</tr>
							<tr>
								<td>新版代付發動者基本資料</td>
							</tr>
							<tr>
								<td>新版手續費資料</td>
							</tr>
							<tr>
								<td>代收代付業者手續費資料</td>
							</tr>
							</logic:notEqual>
							
							<logic:equal  name="login_form" property="userData.USER_TYPE" value="C">
							<tr>
								<td>總行資料</td>
							</tr>
							<tr>
								<td>分行資料</td>
							</tr>
							<tr>
								<td>交易代號資料</td>
							</tr>
							<tr>
								<td>回應代碼資料</td>
							</tr>
							
							</logic:equal>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="create" onclick="create(this.id,'1')"><img src="./images/add.png"/>&nbsp;產生資料</label>
									</logic:equal>
									
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
								BIG5編碼：
								<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
									
									<label class="btn" id="download" onclick="download(this.id,'B','1')"><img src="./images/import.png"/>&nbsp;下載(銀行端)</label>
									&nbsp;&nbsp;
									<label class="btn" id="download" onclick="download(this.id,'C','1')"><img src="./images/import.png"/>&nbsp;下載(業者端)</label>
								</logic:equal>
								<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="A">
									<label class="btn" id="download" onclick="download(this.id,'','1')"><img src="./images/import.png"/>&nbsp;下載</label>
								</logic:notEqual>
								</td>
								
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
								UTF8編碼：
									<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
									<label class="btn" id="download" onclick="download(this.id,'B','2')"><img src="./images/import.png"/>&nbsp;下載(銀行端)</label>
									&nbsp;
									<label class="btn" id="download" onclick="download(this.id,'C','2')"><img src="./images/import.png"/>&nbsp;下載(業者端)</label>
									</logic:equal>
									<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="A">
									<label class="btn" id="download" onclick="download(this.id,'','2')"><img src="./images/import.png"/>&nbsp;下載</label>
									</logic:notEqual>
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
			}
			
			function alterMsg(){
				var msg = '<bean:write name="basedata_download_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			function create(ac_key){
				if($("#formID").validationEngine("validate")){
					$("#ac_key").val(ac_key);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					blockUI();
					$("form").submit();
				}
			}
			function download(ac_key , str , enctype){
				<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
				if(str == "B"){
					$("#actionType").val(str);
					$("#encType").val(str);
				}
				if(str == "C"){
					$("#actionType").val(str);
					$("#encType").val(str);
				}
				</logic:equal>
				if($("#formID").validationEngine("validate")){
					$("#ac_key").val(ac_key);
					$("#target").val('search');
					$("#encType").val(enctype);
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
		</script>
	</body>
</html>