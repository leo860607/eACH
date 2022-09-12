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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-新增</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
	</head>
	<body onload="unblockUI()">
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">新增</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/business_type" method="POST" >
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="json_selectedBank" styleId="json_selectedBank"/>
					<html:hidden property="json_unselectedBank" styleId="json_unselectedBank"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 48%"><span>業務類別代號</span></td>
							<td><html:text property="BUSINESS_TYPE_ID" size="4" maxlength="4" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>業務類別名稱</span></td>
							<td><html:text property="BUSINESS_TYPE_NAME" size="10" maxlength="6" styleClass="validate[required]"></html:text></td>
						</tr>
						<tr align="center">
							<td colspan="4">
								<table style="border: 0; width: auto; text-align: center">
									<tr>
										<td style="border: 0;">
											<label>總行清單</label><br>
											<html:select styleId="unselectedBankArray" property="unselectedBankArray" multiple="true" size="5" style="width: 230px;height:230px">
												<logic:present name="business_type_form" property="unselectedBankList">
													<html:optionsCollection name="business_type_form" property="unselectedBankList" label="label" value="value"></html:optionsCollection>
												</logic:present>
											</html:select >
										</td>
										<td style="border: 0;">
											<img src="./images/next.png" onclick="addBanks()"/><br><br>
											<img src="./images/previous.png" onclick="removeBanks()"/>
										</td>
										<td style="border: 0;">
											<label>可承辦銀行</label><br>
											<html:select styleId="selectedBankArray" property="selectedBankArray" multiple="multiple" size="5" style="width: 230px;height:230px">
												<logic:present name="business_type_form" property="selectedBankList">
													<html:optionsCollection name="business_type_form" property="selectedBankList" label="label" value="value"></html:optionsCollection>
												</logic:present>
											</html:select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this);removeAllSelectedBankArray()"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back"  onclick="onPut('back')"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
	        });
			
			function onPut(str){
				if(str != "save"){
					$("#formID").validationEngine('detach');
				}else{
					$("#formID").validationEngine({promptPosition: "bottomLeft"});
					if(!$("#formID").validationEngine('validate')){
						$("#formID").validationEngine('detach');return;
					}
				}
				blockUI();
				$("#selectedBankArray option").prop("selected", true);
				
				//保留送交的清單，以免儲存失敗返回此頁面時，上次設定的值消失
				setOptionsToJson("selectedBank");
				setOptionsToJson("unselectedBank");
				
				$("#ac_key").val(str);
				$("#target").val('business_type');
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="business_type_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function setOptionsToJson(id){
				var ary = [];
				$("#" + id + "Array option").each(function(){
					ary.push({value: $(this).val(), label: $(this).text()});
				});
				$("#json_" + id).val(JSON.stringify(ary));
				//if(console.log){console.log($("#json_" + id).val());}
			}
			
			function addBanks(){
				var options = $('option:selected', $("#unselectedBankArray")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("#selectedBankArray").append($(options[i]));
				}
				
				sortListBox();
			}
			
			function removeBanks(){
				var options = $('option:selected', $("#selectedBankArray")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("#unselectedBankArray").append($(options[i]));
				}
				
				sortListBox();
			}
			
			function sortListBox(){
				//sort items
				var ary = $("#unselectedBankArray option").toArray();
				ary.sort(sortRule);
				$("#unselectedBankArray option").remove();
				for(var i = 0; i < ary.length; i++){
					$("#unselectedBankArray").append($(ary[i]));
				}
				
				ary = $("#selectedBankArray option").toArray();
				ary.sort(sortRule);
				$("#selectedBankArray option").remove();
				for(var i = 0; i < ary.length; i++){
					$("#selectedBankArray").append($(ary[i]));
				}
			}
			
			function sortRule(a, b) {
				if (a.value > b.value) {
					return 1;
				}
				if (a.value < b.value) {
					return -1;
				}
				// a must be equal to b
				return 0;
			}
			
			function removeAllSelectedBankArray(){
				$("#selectedBankArray option").prop("selected", true);
				removeBanks();
			}
		</script>
	</body>
</html>
