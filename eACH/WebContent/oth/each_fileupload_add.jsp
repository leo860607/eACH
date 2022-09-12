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
				<a href="#">新增</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/each_fileupload" method="POST" enctype="multipart/form-data">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<table id="FILE_TABLE">
					<!-- 三個上傳檔案至少選一個-->
						<tr>
							<td class="header" style="width: 200px"><span>上傳檔案1</span></td>
							<td><html:file styleId="FILE1" property="FILE1" styleClass="validate[groupRequired[FILE_TABLE]]"/></td>
							<td class="header"><span>檔案描述1</span></td>
							<td><html:text styleId="FILE_DESC1" property="FILE_DESC1" value="" size="20" maxlength="20"></html:text></td>
						</tr>
						<tr>
							<td class="header" style="width: 200px"><span>上傳檔案2</span></td>
							<td><html:file styleId="FILE2" property="FILE2" styleClass="validate[groupRequired[FILE_TABLE]]"/></td>
							<td class="header"><span>檔案描述2</span></td>
							<td><html:text styleId="FILE_DESC2" property="FILE_DESC2" value="" size="20" maxlength="20"></html:text></td>
						</tr>
						<tr>
							<td class="header" style="width: 200px"><span>上傳檔案3</span></td>
							<td><html:file styleId="FILE3" property="FILE3" styleClass="validate[groupRequired[FILE_TABLE]]"/></td>
							<td class="header"><span>檔案描述3</span></td>
							<td><html:text styleId="FILE_DESC3" property="FILE_DESC3" value="" size="20" maxlength="20"></html:text></td>
						</tr>
						<tr align="center">
							<td colspan="4">
								<table style="border: 0; width: auto; text-align: center">
									<tr>
										<td style="border: 0;">
											<label>單位清單</label><br>
											<html:select property="banksArray" multiple="true" size="5" style="width: 230px;height:230px">
												<logic:present name="each_fileupload_form" property="banksList">
													<html:optionsCollection name="each_fileupload_form" property="banksList" label="label" value="value"></html:optionsCollection>
												</logic:present>
											</html:select >
										</td>
										<td style="border: 0;">
											<label class="btn" id="add" onclick="addAllBanks()"><img src="./images/right_all.png"/></label><br><br>
											<label class="btn" id="add" onclick="addBanks()">&nbsp;<img src="./images/right.png"/>&nbsp;</label><br><br>
											<label class="btn" id="add" onclick="removeBanks()">&nbsp;<img src="./images/left.png"/>&nbsp;</label><br><br>
											<label class="btn" id="add" onclick="removeAllBanks()"><img src="./images/left_all.png"/></label>
										</td>
										<td style="border: 0;">
											<label>可下載單位</label><br>
											<html:select property="selectedBanksArray" multiple="multiple" size="5" style="width: 230px;height:230px"></html:select >
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="add" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;上傳檔案</label>
								<label class="btn" id="clean" onclick="cleanForm(this);removeAllBanks()"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back"  onclick="back()"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
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
				unblockUI();
	        });
			
			function onPut(str){
				//將所有的選擇銀行反白，確保Server可以收到
				$("[name=selectedBanksArray] option").prop("selected", true);
				$("#ac_key").val(str);
				$("#target").val('search');
				var serchs = {};
				var selectedBanks = "";
				serchs['FILE1'] = $("#FILE1").val().substr($("#FILE1").val().lastIndexOf("\\")+1,$("#FILE1").val().length);
				serchs['FILE2'] = $("#FILE2").val().substr($("#FILE2").val().lastIndexOf("\\")+1,$("#FILE2").val().length);
				serchs['FILE3'] = $("#FILE3").val().substr($("#FILE3").val().lastIndexOf("\\")+1,$("#FILE3").val().length);
				serchs['FILE_DESC1'] = $("#FILE_DESC1").val();
				serchs['FILE_DESC2'] = $("#FILE_DESC2").val();
				serchs['FILE_DESC3'] = $("#FILE_DESC3").val();
				$.each($("[name=selectedBanksArray] option").toArray(),function(index,option){
					if(index == 0){
						selectedBanks = option.value;
					}
					else{
						selectedBanks += ","+option.value;
					}
				});
				serchs['SELECTEDBANKS'] = selectedBanks;
				serchs['action'] = $("#formID").attr('action');
				$("#serchStrs").val(JSON.stringify(serchs));
				$("form").submit();
			}
			
			function init(){
				alterMsg();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="each_fileupload_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			function addBanks(){
				var options = $('option:selected', $("[name=banksArray]")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("[name=selectedBanksArray]").append($(options[i]));
				}
				
				sortListBox();
			}
			function addAllBanks(){
				$("[name=banksArray] option").prop("selected", true);
				addBanks();
			}
			function removeBanks(){
				var options = $('option:selected', $("[name=selectedBanksArray]")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("[name=banksArray]").append($(options[i]));
				}
				
				sortListBox();
			}
			function removeAllBanks(){
				$("[name=selectedBanksArray] option").prop("selected", true);
				removeBanks();
			}
			function sortListBox(){
				//sort items
				var ary = $("[name=banksArray] option").toArray();
				ary.sort(sortRule);
				$("[name=banksArray] option").remove();
				for(var i = 0; i < ary.length; i++){
					$("[name=banksArray]").append($(ary[i]));
				}
				
				ary = $("[name=selectedBanksArray] option").toArray();
				ary.sort(sortRule);
				$("[name=selectedBanksArray] option").remove();
				for(var i = 0; i < ary.length; i++){
					$("[name=selectedBanksArray]").append($(ary[i]));
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
			function back(){
				$("#formID").validationEngine('detach');
				onPut('');
			}
		</script>
	</body>
</html>
