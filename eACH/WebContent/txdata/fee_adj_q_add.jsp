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
							<td><html:text styleId="YYYYMM" property="YYYYMM" size="6" maxlength="6" styleClass="validate[required,notChinese,funcCall[checkYYYYMM]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>調帳分行</span></td>
							<td>
								<html:hidden styleId="BRBK_ID" property="BRBK_ID"/>
								<!-- 選擇調帳角色 -->
								<select id="role_list" class="validate[required]" onchange="changeRole(this.value)">
									<option value="">請選擇</option>
									<option value="TCH">票交所</option>
									<option value="BANK">銀行分行</option>
								</select>
								<html:select styleId="BGBK_ID" property="BGBK_ID" styleClass="validate[required]" onchange="getBrbk_List(this.value)">
									<html:option value="">==請選擇總行代號==</html:option>
								</html:select>
								<select id="BRBK_LIST" name="BRBK_LIST" class="validate[required]" onchange="$('#BRBK_ID').val(this.value)">
									<option value="">==請選擇分行代號==</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="header"><span>調帳金額</span></td>
							<td><html:text styleId="FEE" property="FEE" size="10" maxlength="10" styleClass="validate[notChinese,decimal[9,2]]"></html:text></td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="save" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this);changeRole('')"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back"  onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
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
				$("#BGBK_ID").hide();
				$("#BRBK_LIST").hide();
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
				$("#BGBK_ID").attr("disabled", false);
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function back(str){
				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			function changeRole(role){
				if(role != ""){
					$("#BGBK_ID").show();
					$("#BGBK_ID").attr("disabled", false);
					
					var rdata = {component:"fee_adj_bo", method:"getBgbkIdList", ROLE: role};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
					
					if(role == "TCH"){
						//固定為總所
						$("#BGBK_ID").val('0188888');
						$("#BGBK_ID").attr("disabled", true);
						$("#BRBK_ID").val("0188888");
						$("#BRBK_LIST").hide();
					}else{
						$("#BRBK_LIST").show();
					}
				}else{
					$("#BGBK_ID").hide();
					$("#BGBK_ID").attr("disabled", false);
					$("#BGBK_ID option:not(:first-child)").remove();
					$("#BRBK_LIST option:not(:first-child)").remove();
					$("#BRBK_LIST").hide();
				}
				
			}
			
			function getBrbk_List(bgbkId){
				if(bgbkId == ''){
					$("#BRBK_LIST option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_branch_bo", method:"getBank_branch_List" , bgbkId:bgbkId  };
					fstop.getServerDataExII(uri, rdata, false,creatBrBkList);
				}
			}	
			
			function creatBrBkList(obj){
				if(obj.feeBranch){
					alert('該行「總行管理」已設定手續費分行代號');
				}
				var select = $("#BRBK_LIST");
				$("#BRBK_LIST option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
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
			
			function creatBgBkList(obj){
				var select = $("#BGBK_ID");
				$("#BGBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].value).text(dataAry[i].label));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
		</script>
	</body>
</html>
