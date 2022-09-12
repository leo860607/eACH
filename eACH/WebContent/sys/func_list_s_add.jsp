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
				<html:form styleId="formID" action="/func_list" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 30%"><span>功能代號</span></td>
							<td style="width: 20%"><html:text styleId="FUNC_ID" property="FUNC_ID"  size="10" maxlength="10" styleClass="validate[required,maxSize[10]]"></html:text></td>
							<td class="header necessary" style="width: 35%"><span>功能名稱</span></td>
							<td style="width: 15%"><html:text styleId="FUNC_NAME" property="FUNC_NAME"  size="30" maxlength="66" styleClass="validate[required,maxSize[66]]"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>功能說明</span></td>
							<td><html:text styleId="FUNC_DESC" property="FUNC_DESC"  size="30" maxlength="66" styleClass="validate[required,maxSize[66]]"></html:text></td>
							<td class="header necessary"><span>功能類別</span></td>
							<td>
								<html:select styleId="FUNC_TYPE" property="FUNC_TYPE" styleClass="validate[required]" >
									<html:option value="">===請選擇功能種類===</html:option>
									<html:option value="1">作業模組</html:option>
									<html:option value="2">功能項目</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>功能所屬單位</span></td>
							<td id="FUNC_OWNER">
								<html:checkbox styleId="TCH_FUNC" property="TCH_FUNC" value="Y" styleClass="validate[groupCheckbox[FUNC_OWNER]]"><label for="TCH_FUNC">交換所</label></html:checkbox>
								<html:checkbox styleId="BANK_FUNC" property="BANK_FUNC" value="Y" styleClass="validate[groupCheckbox[FUNC_OWNER]]"><label for="BANK_FUNC">銀行</label></html:checkbox>
								<!-- 20141211 HUANGPU David說不需要 -->
								<!-- 20150119 eidt by hugo 票交李建利說需要 依據UAT-20150112-17 -->
								<html:checkbox styleId="COMPANY_FUNC" property="COMPANY_FUNC" value="Y" styleClass="validate[groupCheckbox[FUNC_OWNER]]"><label for="COMPANY_FUNC">發動者</label></html:checkbox>
							</td>
							<td id="TD_UP_FUNC_ID" class="header"><span>上層所屬作業</span></td>
							<td>
								<html:select styleId="UP_FUNC_ID" property="UP_FUNC_ID" style="width: 200px">
									<html:option value="">無</html:option>
									<html:optionsCollection name="func_list_form" property="funcIdList" label="label" value="value"></html:optionsCollection>
								</html:select>
							</td>
						</tr>
						
						<tr>
							<td id="TD_FUNC_URL" class="header necessary"><span>連結網址</span></td>
							<td colspan="3"><html:text styleId="FUNC_URL" property="FUNC_URL" size="100" maxlength="200" styleClass="validate[required,maxSize[200],notChinese] text-input "  ></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>是否啟用</span></td>
<%-- 							<td><html:checkbox styleId="IS_USED" property="IS_USED" value="Y"></html:checkbox></td> --%>
							<td>
							是<html:radio property="IS_USED" value="Y"></html:radio>
							否<html:radio property="IS_USED" value="N"></html:radio>
							</td>
							<td  id="TD_START_DATE" class="header"><span>啟用日期</span></td>
							<td><html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,minSize[8],maxSize[8],twDate] text-input datepicker"></html:text></td>
<%-- 							<td><html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[minSize[8],maxSize[8],custom[twdate]] text-input datepicker"></html:text></td> --%>
						</tr>
						<tr>
<!-- 							<td class="header necessary"><span>功能顯示順序</span></td> -->
<%-- 							<td><html:text styleId="FUNC_SEQ" property="FUNC_SEQ" size="4" maxlength="4" styleClass="validate[required,custom[integer],min[0]]" ></html:text></td> --%>
							<td class="header necessary"><span>是否使用&nbsp;IKey</span></td>
							<td>
<%-- 							<html:checkbox styleId="USE_IKEY" property="USE_IKEY" value="Y"></html:checkbox> --%>
								是<html:radio property="USE_IKEY" value="Y"></html:radio>
								否<html:radio property="USE_IKEY" value="N"></html:radio>
							</td>
							<td class="header">代理清算行專屬功能</td>
							<td>
								是<html:radio property="PROXY_FUNC" value="Y"></html:radio>
								否<html:radio property="PROXY_FUNC" value="N"></html:radio>
							</td>
						</tr>
						<tr>
							<td class="header" ><span>功能名稱(銀行端)</span></td>
							<td ><html:text styleId="FUNC_NAME_BK" property="FUNC_NAME_BK" size="30" maxlength="66" styleClass="validate[maxSize[66]]"></html:text></td>
							<td class="header">查詢是否紀錄</td>
							<td>
								是<html:radio property="IS_RECORD" value="Y"></html:radio>
								否<html:radio property="IS_RECORD" value="N"></html:radio>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id , true)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
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
			var lastClickFuncOwnerId = "";
		
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function init(){
				alterMsg();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker();
				//20141208 HUANGPU 開會討論預設打勾 
// 				20140119 edit by hugo 改radiio 且預設為N
// 				$("#USE_IKEY").prop("checked", true);
				
				//SET FUNC_OWNER EVENT
				$("#FUNC_OWNER input:checkbox").click(function(){
					var id = $(this).attr("id");
					lastClickFuncOwnerId = id;
					//20141210 HUANGPU David說可複選
					/*
					$("#FUNC_OWNER input:checkbox").each(function(){
						if($(this).attr("id") != id){
							$(this).prop("checked", false);
						}
					});
					*/
					setTimeout(function(){clearPrompt();}, 20);
				});
				
				
				//SET IS_USED EVENT
// 				$("#IS_USED").click(function(){
// 					if($(this).prop("checked") == true){
// 						setSTART_DATE();
// 					}else{
// 						$("#START_DATE").datepicker("setDate", "");
// 					}
// 				});
				//SET IS_USED EVENT
				$("input[name=IS_USED]").click(function(){
					if($(this).val() == 'Y'){
						setSTART_DATE();
// 						20150119 add by hugo
						$("#START_DATE").addClass("validate[required,minSize[8],maxSize[8],twDate] text-input datepicker");
						$("#START_DATE").removeClass("validate[minSize[8],maxSize[8],twDate] text-input datepicker");
						$("#TD_START_DATE").addClass("opPanel .header necessary");
						$("#TD_START_DATE").removeClass("opPanel .header");
					}else{
						$("#START_DATE").datepicker("setDate", "");
// 						20150119 add by hugo
						$("#START_DATE").addClass("validate[minSize[8],maxSize[8],twDate] text-input datepicker");
						$("#START_DATE").removeClass("validate[required,minSize[8],maxSize[8],twDate] text-input datepicker");
						$("#TD_START_DATE").addClass("opPanel .header");
						$("#TD_START_DATE").removeClass("opPanel .header necessary");
					}
				});
				
				//SET FUNC_TYPE EVENT
				$("#FUNC_TYPE").change(function(){
					if($(this).val() == "1"){
						$("#UP_FUNC_ID option[value='']").attr("selected", "selected");
						$("#UP_FUNC_ID").prop("disabled", true);
// 						20150119 add by hugo
						$("#UP_FUNC_ID").removeClass("validate[required]");
						$("#TD_UP_FUNC_ID").removeClass("opPanel .header necessary");
						$("#TD_UP_FUNC_ID").addClass("opPanel .header");
						if(window.console){console.log($(this).val());}
						$("#FUNC_URL").removeClass("validate[required,maxSize[200],notChinese] text-input");
						$("#TD_FUNC_URL").removeClass("opPanel .header necessary");
						$("#TD_FUNC_URL").addClass("opPanel .header");
						
					}else{
						$("#UP_FUNC_ID").prop("disabled", false);
// 						20150119 add by hugo
						$("#UP_FUNC_ID").addClass("validate[required]");
						$("#TD_UP_FUNC_ID").removeClass("opPanel .header");
						$("#TD_UP_FUNC_ID").addClass("opPanel .header necessary");
						
						$("#FUNC_URL").addClass("validate[required,maxSize[200],notChinese] text-input");
						$("#TD_FUNC_URL").removeClass("opPanel .header");
						$("#TD_FUNC_URL").addClass("opPanel .header necessary");
					}
				});
				
				//SET FUNC_OWNER CHECKBOX EVENT
				$("#FUNC_OWNER input:checkbox").click(function(){
					changeUpFuncIdList();
				});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="func_list_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function onPut(str , isValidate){
				if($("#IS_USED").prop("checked") == true && $("#START_DATE").val() == ""){
					setSTART_DATE();
				}
				$("#UP_FUNC_ID").prop("disabled", false);
				$("#ac_key").val(str);
				$("#target").val('search');
				if(isValidate){
					if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				$("form").submit();
			}
			
			function back(str){
// 				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			function setSTART_DATE(){
				$("#START_DATE").datepicker("setDate", new Date());
				$("#START_DATE").val("0" + $("#START_DATE").val());
			}
			
			function clearPrompt(){
				$("#FUNC_OWNER input:checkbox").each(function(){
					if($(this).attr("id") != lastClickFuncOwnerId){
						$(this).validationEngine('hide');
					}
				});
			}
			
			//依照功能所屬單位切換上層所屬作業清單
			function changeUpFuncIdList(){
				//清空原本未歸類群組的上層所屬作業清單(保留第一項)
				$("#UP_FUNC_ID > option:not(:first-child)").remove();
				
				var uri = "/eACH/baseInfo";
				var rdata = {component:"func_list_bo",method:"getFuncIdList_toJson"};
				rdata.TCH_FUNC = ( $("#TCH_FUNC").prop("checked")==true? "Y":"N" );
				rdata.BANK_FUNC = ( $("#BANK_FUNC").prop("checked")==true? "Y":"N" );
				//rdata.COMPANY_FUNC = ( $("#COMPANY_FUNC").prop("checked")==true? "Y":"N" );
				
				var upFuncIdList = fstop.getServerDataExII(uri, rdata, false);
				if(upFuncIdList.length != null && upFuncIdList.length > 0){
					for(var i = 0; i < upFuncIdList.length; i++){
						$("#UP_FUNC_ID").append('<option value="'+upFuncIdList[i].value+'">'+upFuncIdList[i].label+'</option>');
					}
				}
			}
		</script>
	</body>
</html>
