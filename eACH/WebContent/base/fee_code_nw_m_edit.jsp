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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-編輯</title>
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
			<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define>
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/fee_code_nw" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="FEE_UID" styleId="FEE_UID"/>
					<html:hidden property="FEE_TYPE" styleId="FEE_TYPE"/>
					<table>
						<!-- 票交端 -->
						<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
							<tr>
								<td class="header necessary" style="width: 25%"><span>交易代號</span></td>
								<td><html:text styleId="FEE_ID" property="FEE_ID" readonly="true" size="3" maxlength="3" styleClass="lock validate[required,notChinese]"></html:text></td>
								<!-- 手續費代號應與交易代號相同，故僅限3碼(資料庫欄位為長度4) -->
								<td class="header necessary" style="width: 20%"><span>收費類型</span></td>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="A">
									<td>固定</td>
								</logic:equal>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="B">
									<td>外加</td>
								</logic:equal>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="C">
									<td>百分比</td>
								</logic:equal>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="D">
									<td>級距</td>
								</logic:equal>
							</tr>
							<tr>
								
								<td class="header necessary" style="width: 20%"><span>啟用日期</span></td>
								<td><html:text styleId="START_DATE" property="START_DATE" readonly="true" size="8" maxlength="8" styleClass="lock validate[required,minSize[8],maxSize[8],twDate] text-input datepicker"></html:text></td>
								<td colspan="2">
								</td>
								
							</tr>
							<tr>
								<td class="header necessary"><span>扣款行手續費</span></td>
								<td><html:text styleId="OUT_BANK_FEE" property="OUT_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
								<td class="header necessary"><span>折扣後扣款行手續費</span></td>
								<td><html:text styleId="OUT_BANK_FEE_DISC" property="OUT_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>入帳行手續費</span></td>
								<td><html:text styleId="IN_BANK_FEE" property="IN_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
								<td class="header necessary"><span>折扣後入帳行手續費</span></td>
								<td><html:text styleId="IN_BANK_FEE_DISC" property="IN_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>交換所手續費</span></td>
								<td><html:text styleId="TCH_FEE" property="TCH_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
								<td class="header necessary"><span>折扣後交換所手續費</span></td>
								<td><html:text styleId="TCH_FEE_DISC" property="TCH_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>發動行手續費</span></td>
								<td><html:text styleId="SND_BANK_FEE" property="SND_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
								<td class="header necessary"><span>折扣後發動行手續費</span></td>
								<td><html:text styleId="SND_BANK_FEE_DISC" property="SND_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>銷帳行手續費</span></td>
								<td><html:text styleId="WO_BANK_FEE" property="WO_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
								<td class="header necessary"><span>折扣後銷帳行手續費</span></td>
								<td><html:text styleId="WO_BANK_FEE_DISC" property="WO_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td id="typeBnecessary" class="header necessary"><span>客戶應支付手續費</span></td>
								<td><html:text styleId="HANDLECHARGE" property="HANDLECHARGE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text><span class="addpercent"></span></td>
								<td ><span></span></td>
								<td></td>
							</tr>
							<tr>
								<td class="header"><span>手續費說明</span></td>
								<td colspan="3"><html:text property="FEE_DESC" size="33" maxlength="33" styleClass="lock"></html:text></td>
							</tr>
							<tr>
								<td class="header" style="width: 20%"><span>啟用註解</span></td>
								<td colspan="3">
									<html:text property="ACTIVE_DESC" size="66" maxlength="66"></html:text>
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
<%-- 								<logic:equal  name="userData" property="s_auth_type" value="A"> --%>
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<logic:notEqual name="fee_code_nw_form" property="FEE_UID" value="">
										<label class="btn" id="update_nw" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
										<label class="btn" id="delete_nw" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
									</logic:notEqual>	
									<logic:equal name="fee_code_nw_form" property="FEE_UID" value="">
										<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
										<label class="btn" id="delete" onclick="onPut(this.id)"><img src="./images/delete.png"/>&nbsp;刪除</label>
									</logic:equal>
								</logic:equal>	
									<label class="btn" id="back" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
								</td>
							</tr>
						</logic:equal>
						<!-- 銀行端 -->
						<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
							<tr>
								<td class="header necessary" style="width: 50%"><span>交易代號</span></td>
								<td><html:text styleId="FEE_ID" property="FEE_ID" readonly="true" size="4" maxlength="4" styleClass="lock validate[required,notChinese]"></html:text></td>
							</tr>
							<tr>
								<td class="header necessary"><span>啟用日期(民國年 ex.01030101)</span></td>
								<td><html:text styleId="START_DATE" property="START_DATE" readonly="true" size="8" maxlength="8" styleClass="lock validate[required,minSize[8],maxSize[8],twDate] text-input datepicker"></html:text></td>
							</tr>
							<tr>
								<td class="header necessary" style="width: 20%"><span>收費類型</span></td>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="A">
									<td>固定</td>
								</logic:equal>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="B">
									<td>外加</td>
								</logic:equal>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="C">
									<td>百分比</td>
								</logic:equal>
								<logic:equal name="fee_code_nw_form" property="FEE_TYPE" value="D">
									<td>級距</td>
								</logic:equal>
							</tr>
							<tr>
								<td class="header necessary"><span>扣款行手續費</span></td>
								<td><html:text styleId="OUT_BANK_FEE" property="OUT_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right" readonly="true"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>入帳行手續費</span></td>
								<td><html:text styleId="IN_BANK_FEE" property="IN_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right" readonly="true"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>銷帳行手續費</span></td>
								<td><html:text styleId="WO_BANK_FEE" property="WO_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right" readonly="true"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>交換所手續費</span></td>
								<td><html:text styleId="TCH_FEE" property="TCH_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right" readonly="true"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header necessary"><span>發動行手續費</span></td>
								<td><html:text styleId="SND_BANK_FEE" property="SND_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right" readonly="true"></html:text><span class="addpercent"></span></td>
							</tr>
							<tr>
								<td class="header"><span>手續費說明</span></td>
								<td><html:text property="FEE_DESC" size="33" maxlength="33" styleClass="lock"></html:text></td>
							</tr>
							<tr>
								<td class="header"><span>啟用註解</span></td>
								<td><html:text property="ACTIVE_DESC" size="66" maxlength="66" styleClass="lock"></html:text></td>
							</tr>
							<tr>
								<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
									<label class="btn" id="back" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
								</td>
							</tr>
						</logic:equal>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		var isDelete = false;
			$(document).ready(function(){
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				fee_type_change($("#FEE_TYPE").val());
			});
		
			function init(){
				$("#dataInputTable tr:odd").addClass("resultDataRowOdd");
				$("#dataInputTable tr:even").addClass("resultDataRowEven");
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#START_DATE").val('<bean:write name="fee_code_nw_form" property="START_DATE"/>');
				if($('#FEE_TYPE').val()=='C'){
					$('.addpercent').text("%");
				}else{
					$('.addpercent').text("");
				}
//				20150624 add by hugo req by UAT-2015011-02
// 				判斷啟用日期是否小於等於營業日  
				checkBizDate( $('#formID input') ,'<bean:write name="fee_code_nw_form" property="START_DATE"/>');
				//2015/02/02 add by hugo
				alterMsg();
			}
			
// 			判斷啟用日期是否小於等於營業日  是:不可修改 ，否:可修改  
			function checkBizDate( obj ,date){
				if(fstop.isEmptyString(date)){
					return;
				}
				var rdata = {component:"eachsysstatustab_bo", method:"checkBizDate" ,activeDate:date};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
				if(fstop.isNotEmpty(vResult)){
// 					啟用日期 小於營業日 要disabled 該欄位
					if(vResult.result == "TRUE"){
						$(obj).attr('readonly', true);
						$(obj).addClass('lock');
						isDelete = false;
					}else{
						$(obj).attr('readonly', false);
						$(obj).removeClass('lock');
						$("#START_DATE").attr('readonly', true);
						$("#START_DATE").addClass('lock');
						$("#FEE_ID").attr('readonly', true);
						$("#FEE_ID").addClass('lock');
						
						isDelete = true;
					}
					if(window.console){console.log("msg>>"+vResult.msg);}
				}else{
					alert("系統異常");
				}
			}
			
			
			
			function onPut(str){
// 				alert(str)
				if(str != "update"){
					if(str == "delete"){
						if(!confirm("是否確定刪除?")){return false;}else{$("#formID").validationEngine('detach');}
						if(isDelete){
							
						}else{
							alert("啟用日期小於或等於營業日期，不可刪除");
							return;
						}
					}
					if(str == "back"){$("#formID").validationEngine('detach');}
				}else{
					if(!$("#formID").validationEngine("validate") || !checkFee()){
						return;
					}
					if(isDelete){
						
					}else{
						alert("啟用日期小於或等於營業日期，不可儲存");
						return;
					}
				}
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}
			
			//2015/02/02 add by hugo
			function alterMsg(){
				var msg = '<bean:write name="fee_code_nw_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function checkFee(){
				
				//扣款行手續費+入帳行手續費+交換所手續費+發動行手續費=0
				var total1 = (parseFloat($('#OUT_BANK_FEE').val()) +
				parseFloat($('#IN_BANK_FEE').val()) +
				parseFloat($('#TCH_FEE').val()) +
				parseFloat($('#SND_BANK_FEE').val())+
// 				parseFloat($('#HANDLECHARGE').val())+
				parseFloat($('#WO_BANK_FEE').val())).toFixed(3);
				if(window.console){console.log("total1="+total1);}
				if(total1 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n扣款行手續費+入帳行手續費+\n發動行手續費+銷帳行手續費+交換所手續費=0");
					return false;
				}
				//折扣後扣款行手續費+折扣後入帳行手續費+折扣後交換所手續費+折扣後發動行手續費=0
				var total2 = (parseFloat($('#OUT_BANK_FEE_DISC').val()) +
				parseFloat($('#IN_BANK_FEE_DISC').val()) +
				parseFloat($('#TCH_FEE_DISC').val()) +
				parseFloat($('#SND_BANK_FEE_DISC').val())+
				parseFloat($('#WO_BANK_FEE_DISC').val())).toFixed(3);
				if(window.console){console.log("total2="+total2);}
				if(total2 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n折扣後扣款行手續費+折扣後入帳行手續費+\n折扣後發動行手續費+折扣後銷帳行手續費+\n折扣後交換所手續費=0");
					return false;
				}
				return true;
			}
			
			function fee_type_change(value){
				//A固定 B外加 C百分比 D級距 1固定 2百分比
				if(value==='A'){
					$('.addpercent').text("");
					$('.LVLsection').hide();
					$("#sptr").attr("colspan","3");
					$('#typeBnecessary').removeClass("necessary");
					$('#HANDLECHARGE').removeClass("validate[required,decimal[7,2]]");
					$('#HANDLECHARGE').addClass("validate[decimal[7,2]]");
				}
				if(value==='B'){
					$('.addpercent').text("");
					$('.LVLsection').hide();
					$("#sptr").attr("colspan","3");
					$('#typeBnecessary').addClass("necessary");
					$('#HANDLECHARGE').addClass("validate[required,decimal[7,2]]");
				}
				if(value==='C'){
					$('.addpercent').text("%");
					$('.LVLsection').hide();
					$("#sptr").attr("colspan","3");
					$('#typeBnecessary').removeClass("necessary");
					$('#HANDLECHARGE').removeClass("validate[required,decimal[7,2]]");
					$('#HANDLECHARGE').addClass("validate[decimal[7,2]]");
				}
				if(value==='D'){
					$('.LVLsection').show();
					$('.addpercent').text("");
					$("#sptr").attr("colspan","1");
					$('#typeBnecessary').removeClass("necessary");
					$('#HANDLECHARGE').removeClass("validate[required,decimal[7,2]]");
					$('#HANDLECHARGE').addClass("validate[decimal[7,2]]");
				}
				if(value==='1'){
					$('.addpercent').text("");
				}
				if(value==='2'){
					$('.addpercent').text("%");
				}
			}
			
		</script>
	</body>
</html>
