<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-編輯</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
	</head>
	<body>
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/fee_adj" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 50%"><span>調整年月<br>(民國年月 ex:010401)</span></td>
							<td><html:text styleId="YYYYMM" property="YYYYMM" readonly="true" size="6" maxlength="6" styleClass="lock validate[required,notChinese,funcCall[checkYYYYMM]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>調帳分行</span></td>
							<td>
								<html:text styleId="BRBK_ID" property="BRBK_ID" readonly="true" size="7" styleClass="lock"/>-
								<html:text styleId="BRBK_NAME" property="BRBK_NAME" readonly="true" styleClass="lock"/>
							</td>
						</tr>
						<tr>
							<td class="header"><span>調帳金額</span></td>
							<td><html:text styleId="FEE" property="FEE" size="10" maxlength="10" styleClass="validate[notChinese,decimal[9,2]]"></html:text></td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
								<logic:equal name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="update" onclick="saveData(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
									<label class="btn" id="delete" onclick="delData(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
								</logic:equal>	
								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
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
				var msg = '<bean:write name="fee_adj_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function onPut(str){
				if(str == "save"){
					if(!$('#formID').validationEngine('validate')){
						unblockUI();return;
					}
				}
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function back(){
				$("#formID").validationEngine('detach');
				onPut('back');
			}
			
			function checkYYYYMM(){
				var str = $("#YYYYMM").val();
				if(str != ""){
					var patt = new RegExp("0[0-9][0-9][0-9](0[1-9]|1[0-2])$");
					var res = patt.test(str);
					if(!res){
						return "年月格式錯誤! ex:010403";
					}
				}
				return true;
			}
			
			function delData(str){
				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			function saveData(str){
				onPut(str);
			}
		</script>
	</body>
</html>
