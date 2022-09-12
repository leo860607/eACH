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
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				var result = '<bean:write name="bank_group_form" property="result"/>';
				var message = '<bean:write name="bank_group_form" property="msg"/>';
				showMessage(result, message);
				setDatePicker();
				$("#ACTIVE_DATE").val('<bean:write name="bank_group_form" property="ACTIVE_DATE"/>');
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
			});
			
			function showMessage(result, message){
				if(fstop.isNotEmptyString(result)){
					var ac_key = result.split("_")[0];
					result = result.split("_")[1];
					if(ac_key == "add"){
						if(result == "true"){
							alert("儲存成功!");
							$("#BGBK_ID").val(message);
						}else{
							alert("儲存失敗：" + message);
						}
					}
					
					if(result == "true"){
						//Return to search page
						onPut("back", false);
					}
				}
			}
			
			function onPut(id, isValidate){
				if(isValidate){
					$("#formID").validationEngine({promptPosition: "bottomLeft"});
					if(!check()){
						return false;
					}
					if(!jQuery('#formID').validationEngine('validate')){
						$("#formID").validationEngine('detach');return false;
					}
					var uri = "${pageContext.request.contextPath}"+"/baseInfo?component=bank_group_bo&method=validate&" + $("#formID").serialize();
					var vResult = fstop.getServerDataExII(uri,null,false);
					if(!confirmValidateResult(vResult)){return false;}
				}else{
					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#ac_key").val(id);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function addBsType(){
				var options = $('option:selected', $("[name=bsTypeAry]")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("[name=selectedBsTypeAry]").append($(options[i]));
				}
				
				sortListBox();
			}
			
			function removeBsType(){
				var options = $('option:selected', $("[name=selectedBsTypeAry]")).detach();
				for(var i = 0 ; i < $(options).size(); i++){
					$("[name=bsTypeAry]").append($(options[i]));
				}
				
				sortListBox();
			}
			
			function sortListBox(){
				//sort items
				var ary = $("[name=bsTypeAry] option").toArray();
				ary.sort(sortRule);
				$("[name=bsTypeAry] option").remove();
				for(var i = 0; i < ary.length; i++){
					$("[name=bsTypeAry]").append($(ary[i]));
				}
				
				ary = $("[name=selectedBsTypeAry] option").toArray();
				ary.sort(sortRule);
				$("[name=selectedBsTypeAry] option").remove();
				for(var i = 0; i < ary.length; i++){
					$("[name=selectedBsTypeAry]").append($(ary[i]));
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
			
			function removeAllBsType(){
				$("[name=selectedBsTypeAry] option").prop("selected", true);
				removeBsType();
				$("#formID").validationEngine('hide');			
			}
			
			function addBankGroup(id){
				$("[name=selectedBsTypeAry] option").prop("selected", true);
				onPut(id, true);
			}
			
			function back(id){
				$("#BGBK_ID").val("none");
				onPut(id, false);
			}
			
			function confirmValidateResult(vResult){
				if(vResult == null){ alert("檢核失敗!"); return false;}
				else{
					if(vResult.result == "FALSE"){
						alert(vResult.msg);
						return false;
					}
				}
				return true;
			}
			
			function opbk_search(str){
				var url = getPath()+"bank_group.do?ac_key=opbk_search&target=opbk_search";
				$.showModalDialog({
					 url: url,
					 dialogArguments: str,
					 height: 200,
					 width: 500,
					 scrollable: false,
					 onClose: function(){ }
				});
			}
			
			function ctbk_search(str){
				var url = getPath()+"bank_group.do?ac_key=ctbk_search&target=ctbk_search";
				$.showModalDialog({
					 url: url,
					 dialogArguments: str,
					 height: 200,
					 width: 500,
					 scrollable: false,
					 onClose: function(){ }
				});
			}
			function chg_rest(value){
				$("input[name='REST_CR_LINE']").val(value);
			}
// 			使用ajx 來檢核該清算行是否有在額度檔內
			function checkCR_LINE(cleanbgbkId){
				var uri = "${pageContext.request.contextPath}"+"/baseInfo";
				if(window.console){console.log("do checkCR_LINE cleanbkid="+cleanbgbkId);}
				if(fstop.isNotEmpty(cleanbgbkId) && fstop.isNotEmptyString(cleanbgbkId)){
					var rdata = {component:"cr_line_bo", method:"getOne" , cleanbgbkId:cleanbgbkId  };
					if(window.console){console.log("uri>>"+uri);}
					fstop.getServerDataExII(uri, rdata, false,initCRfiled);
				}
			}
			
			function initCRfiled(obj){
// 				if(window.console){console.log("obj>>"+obj);}
				if(fstop.isNotEmpty(obj) && obj.result =="TRUE"){
// 					顯示該清算行額度 但不可修改;
					$("#BASIC_CR_LINE").removeClass();
// 					max先放100e 有需要在往上放大
					$("#BASIC_CR_LINE").addClass(" lock validate[required,maxSize[11],custom[integer,min[0] ,max[99999999999]]] text-input");
					$("#BASIC_CR_LINE").attr('readonly',true);
					$("#BASIC_CR_LINE").val(obj.data.BASIC_CR_LINE);
					$("#REST_CR_LINE").val(obj.data.REST_CR_LINE);
					$("#isEditCR").val("N");
				}else if(fstop.isNotEmpty(obj) && obj.result =="FALSE"){
// 					變更css 讓使用者修改額度 BASIC_CR_LINE  validate[required,maxSize[14],custom[integer,min[0]]] text-input
					$("#BASIC_CR_LINE").removeClass();
					$("#BASIC_CR_LINE").addClass("validate[required,maxSize[11],custom[integer,min[0] ,max[99999999999]]] text-input");
					$("#BASIC_CR_LINE").attr('readonly',false);
					$("#isEditCR").val("Y");
					
				}else{
					alert("取得額度檔異常");
				}
			}
			
			function chkEACH(){
				var obj = $("#selectedBsTypeAry");
				var ary = $("#selectedBsTypeAry option").toArray();
				if($(ary).length == 0){
					$("#IS_EACH").attr('checked' , false);
					$("#IS_EACH").val('N');
				}else{
					$("#IS_EACH").attr('checked' , true);
					$("#IS_EACH").val('Y');
				}
			}
			function check(){
				var ary = $("#selectedBsTypeAry option").toArray();
// 				alert(""+$("#IS_EACH").prop('checked'));
				if($("#IS_EACH").prop('checked')  &&  ary.length ==0 ){
// 					alert("總行所屬應用系統已勾選EACH ，承辦業務不可空白");
// 					return false;
// 20150827 note by hugo req by 李建利   只秀出提醒訊息
// 					alert("提醒:總行所屬應用系統已勾選EACH ，承辦業務未選擇");
					return  confirm("總行所屬應用系統已勾選EACH ，承辦業務未選擇") ;
				} 
				
				if($("#IS_EACH").prop('checked')==false &&  ary.length != 0 ){
// 					alert("已指定承辦業務， 總行所屬應用系統EACH為必選 ");
// 					或
// 					$("#IS_EACH").attr('checked' , true);
// 					return false;
// 					alert("提醒:已指定承辦業務， 總行所屬應用系統EACH尚未勾選 ");
					return  confirm("已指定承辦業務， 總行所屬應用系統EACH尚未勾選") ;
				} 
				return true;
			}
		</script>
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
				<html:form styleId="formID" action="/bank_group" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="isEditCR" styleId="isEditCR"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
<%-- 					<html:hidden property="SND_FEE_BRBK_ID"/> --%>
<%-- 					<html:hidden property="OUT_FEE_BRBK_ID"/> --%>
<%-- 					<html:hidden property="IN_FEE_BRBK_ID"/> --%>
					<table>
						<tr>
							<td class="header necessary" style="width: 25%"><span>總行代號</span></td>
							<td style="width: 25%"><html:text styleId="BGBK_ID" property="BGBK_ID" size="7" maxlength="7" styleClass="validate[required,maxSize[7],custom[onlyNumberSp],notChinese] text-input"></html:text></td>
							<td class="header necessary" style="width: 25%"><span>總行名稱</span></td>
							<td><html:text property="BGBK_NAME" size="15" maxlength="13" styleClass="validate[required,maxSize[13]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>銀行屬性</span></td>
							<td>
								<html:select property="BGBK_ATTR" styleClass="validate[required]">
									<html:option value="0">0 - 本國銀行</html:option>
									<html:option value="1">1 - 地區企銀</html:option>
									<html:option value="2">2 - 合作社</html:option>
									<html:option value="3">3 - 農漁會</html:option>
									<html:option value="4">4 - 外商銀行</html:option>
									<html:option value="5">5 - 共用中心</html:option>
									<html:option value="6">6 - 票交分所</html:option>
									<html:option value="7">7 - 票交所代理銀行</html:option>
								</html:select>
							</td>
							<td class="header necessary"><span>央行帳號</span></td>
							<td><html:text styleId="CTBK_ACCT" property="CTBK_ACCT" size="4" maxlength="4" styleClass="validate[required,maxSize[4],custom[onlyNumberSp],notChinese] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交換所代號</span></td>
							<td><html:text property="TCH_ID" size="2" maxlength="2" styleClass="validate[required,maxSize[2],notChinese] text-input"></html:text></td>
							<td class="header"></td>
							<td></td>
						</tr>
						<tr>
							<td class="header necessary"><span>操作行代號</span></td>
							<td>
								<html:text styleId="OPBK_ID" property="OPBK_ID" size="7" maxlength="7" 
								styleClass="validate[required,maxSize[7],custom[onlyNumberSp],notChinese] text-input"></html:text>
								<button type="button" value="OPBK" onclick="opbk_search(this.value)">...</button>
							</td>
							<td class="header necessary"><span>操作行名稱</span></td>
							<td>
								<html:text styleId="OPBK_NAME" property="OPBK_NAME" readonly="true" styleClass="lock validate[optional,maxSize[40]] text-input" size="40" maxlength="40"/>
							</td>
						</tr>
						<tr>
							<td class="header"><span>操作行異動備註</span></td>
							<td colspan="3"><html:text styleId = "OP_NOTE" property="OP_NOTE" size="100" maxlength="100" styleClass="validate[optional,maxSize[50]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>清算行代號</span></td>
							<td>
								<html:text styleId="CTBK_ID" property="CTBK_ID" size="7" maxlength="7" onblur="checkCR_LINE(this.value)"
								styleClass="validate[required,maxSize[7],custom[onlyNumberSp],notChinese] text-input"></html:text>
								<button type="button" value="CTBK" onclick="ctbk_search(this.value)">...</button>
							</td>
							<td class="header necessary"><span>清算行名稱</span></td>
							<td>
								<html:text styleId="CTBK_NAME" property="CTBK_NAME" readonly="true" styleClass="lock validate[optional,maxSize[40]] text-input" size="40" maxlength="40"/>
							</td>
						</tr>
						<tr>
							<td class="header"><span>清算行異動備註</span></td>
							<td colspan="3"><html:text styleId = "CT_NOTE" property="CT_NOTE" size="100" maxlength="100" styleClass="validate[optional,maxSize[50]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>基本額度</span></td>
							<td>
								<html:text styleId="BASIC_CR_LINE" property="BASIC_CR_LINE" size="14" maxlength="11" onblur="chg_rest(this.value)"
								readonly="true" styleClass="lock validate[required,maxSize[11],custom[integer,min[0]]] text-input"></html:text>
							</td>
							<td class="header necessary"><span>剩餘額度</span></td>
							<td>
								<html:text styleId="REST_CR_LINE" property="REST_CR_LINE" readonly="true" styleClass="lock validate[required,maxSize[14],custom[integer,min[0]]] text-input" size="14" maxlength="14"/>
							</td>
						</tr>
						<tr>
							<td class="header necessary"><span>啟用日期(民國年 ex.01030101)</span></td>
							<td><html:text styleId="ACTIVE_DATE" property="ACTIVE_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,notChinese,twPast[#STOP_DATE]] text-input datepicker"></html:text></td>
							<td class="header"></td>
							<td></td>
						</tr>
<!-- 						2016 0118 add by hugo req by UAT-20160112-06 -->
						<tr>
							<!-- TODO 新增時預設值，須從系統參數檔擷取 -->
							<td class="header"><span>每小時上傳最多檔案個數</span></td>
							<td><html:text styleId = "HR_UPLOAD_MAX_FILE" property="HR_UPLOAD_MAX_FILE" size="2" maxlength="2" styleClass="validate[required,maxSize[2],custom[integer,min[0] ,max[99]]] text-input"></html:text></td>
							<td class="header necessary"><span>整批檔案最大傳送筆數</span></td>
							<td><html:text styleId="FILE_MAX_CNT" property="FILE_MAX_CNT" size="6" maxlength="6" styleClass="validate[required,custom[integer,min[0] ,max[999999]]] text-input "></html:text></td>
						</tr>
<!-- 						20150226 edit by hugo  req by UAT-2015025-05 -->
<!-- 						20150210 HUANGPU 新增總行時可能無分行資料，故不提供輸入此項資料 -->
						<tr>
							<!--要去檢核分行欄位是否有同部總合的停用日期 -->
							<td class="header"><span>停用日期(民國年 ex.01030101)</span></td>
							<td><html:text styleId="STOP_DATE" property="STOP_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,notChinese,twFuture[#ACTIVE_DATE]] text-input datepicker"></html:text></td>
							<td class="header"><span>發動行手續費分行代號</span></td>
							<td><html:text property="SND_FEE_BRBK_ID" size="7" maxlength="7" styleClass="validate[optional,maxSize[7],notChinese,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName]] text-input"></html:text></td>
						</tr>
						<tr>
						<td class="header"><span>扣款行手續費分行代號</span></td>
							<td><html:text property="OUT_FEE_BRBK_ID" size="7" maxlength="7" styleClass="validate[optional,maxSize[7],notChinese,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName]] text-input"></html:text></td>
							<td class="header"><span>入帳行手續費分行代號</span></td>
							<td><html:text property="IN_FEE_BRBK_ID" size="7" maxlength="7" styleClass="validate[optional,maxSize[7],notChinese,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName]] text-input"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>銷帳行手續費分行代號</span></td>
							<td><html:text property="WO_FEE_BRBK_ID" size="7" maxlength="7" styleClass="validate[optional,maxSize[7],notChinese,branchId[/eACH/baseInfo,bank_branch_bo,getBranchName]] text-input"></html:text></td>
							<td class="header"><span>總行所屬應用系統</span></td>
							<td >
								<html:checkbox styleId="IS_EACH" property="IS_EACH" value = "Y"><span>EACH</span></html:checkbox>
							</td>
						</tr>
						<tr align="center">
							<td colspan="4">
								<table style="border: 0; width: auto; text-align: center">
									<tr>
										<td style="border: 0;">
											<label>業務清單</label><br>
											<html:select property="bsTypeAry" multiple="true" size="5" style="width: 200px">
												<logic:present name="bank_group_form" property="bsTypeList">
													<html:optionsCollection name="bank_group_form" property="bsTypeList" label="label" value="value"></html:optionsCollection>
												</logic:present>
											</html:select >
										</td>
										<td style="border: 0;">
<!-- 										20150827 note by hugo req by 李建利  讓使用者自行決定是否勾選 EACH EDDA  -->
<!-- 											<img src="./images/next.png" onclick="addBsType();onchange=chkEACH()" /><br><br> -->
<!-- 											<img src="./images/previous.png" onclick="removeBsType();onchange=chkEACH()"/> -->
											<img src="./images/next.png" onclick="addBsType();" /><br><br>
											<img src="./images/previous.png" onclick="removeBsType();"/>
										</td>
										<td style="border: 0;">
											<label>可承辦項目</label><br>
											<html:select styleId="selectedBsTypeAry" property="selectedBsTypeAry" multiple="multiple" size="5" style="width: 200px"></html:select >
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="add" onclick="addBankGroup(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this);removeAllBsType()"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	</body>
</html>
