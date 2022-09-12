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
				<html:form styleId="formID" action="/fee_code" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 25%"><span>交易代號</span></td>
							<!-- 手續費代號應與交易代號相同，故僅限3碼(資料庫欄位為長度4) -->
							<td><html:text styleId="FEE_ID" property="FEE_ID" size="3" maxlength="3" styleClass="validate[required,notChinese,minSize[3]]"></html:text></td>
<!-- 							TODO 未完成  要加入檢核啟用日期必須大於營業日期 -->
							<td class="header necessary" style="width: 20%"><span>啟用日期</span></td>
							<td><html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,minSize[8],maxSize[8],twDate] text-input datepicker"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>扣款行手續費</span></td>
							<td><html:text styleId="OUT_BANK_FEE" property="OUT_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後扣款行手續費</span></td>
							<td><html:text styleId="OUT_BANK_FEE_DISC" property="OUT_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>入帳行手續費</span></td>
							<td><html:text styleId="IN_BANK_FEE" property="IN_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後入帳行手續費</span></td>
							<td><html:text styleId="IN_BANK_FEE_DISC" property="IN_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交換所手續費</span></td>
							<td><html:text styleId="TCH_FEE" property="TCH_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後交換所手續費</span></td>
							<td><html:text styleId="TCH_FEE_DISC" property="TCH_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>發動行手續費</span></td>
							<td><html:text styleId="SND_BANK_FEE" property="SND_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後發動行手續費</span></td>
							<td><html:text styleId="SND_BANK_FEE_DISC" property="SND_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>銷帳行手續費</span></td>
							<td><html:text styleId="WO_BANK_FEE" property="WO_BANK_FEE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>折扣後銷帳行手續費</span></td>
							<td><html:text styleId="WO_BANK_FEE_DISC" property="WO_BANK_FEE_DISC" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>客戶應支付手續費</span></td>
							<td><html:text styleId="HANDLECHARGE" property="HANDLECHARGE" size="9" maxlength="9" styleClass="validate[required,decimal[7,2]]" style="text-align: right"></html:text></td>
							<td ><span></span></td>
							<td></td>
						</tr>
						<tr>
							<td class="header"><span>手續費說明</span></td>
							<td colspan="3">
								<html:text property="FEE_DESC" size="33" maxlength="33"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header"><span>啟用註解</span></td>
							<td colspan="3">
								<html:text property="ACTIVE_DESC" size="66" maxlength="66"></html:text>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="add" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label>
								<label class="btn" id="back" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			$(document).ready(function(){
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
			});
		
			function init(){
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#START_DATE").val('<bean:write name="fee_code_form" property="START_DATE"/>');
				alterMsg();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="fee_code_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
		
			function onPut(str){
				if(str != "add"){
					$("#formID").validationEngine('detach');
				}else{
					if(!$("#formID").validationEngine("validate") || !checkFee()){
						return;
					}
					
					//20150127 HUANGPU 檢查是否有對應的交易代號
					var qStr = "/eACH/baseInfo?component=txn_code_bo&method=search_toJson&TXN_ID="+$("#FEE_ID").val();
					var data = fstop.getServerDataExII(qStr, null, false);
// 					20150416 edit by hugo req by UAT-20150331-02 ，須為「交易代號維護」項下存在之「交易代號」才允許新增成功
// 					if( (data == null || (data != null && data.length == 0)) && !confirm("無對應的交易代號，是否繼續新增?")){
					if( (data == null || (data != null && data.length == 0)) ){
						alert("無對應的交易代號，無法新增");
						$("#formID").validationEngine('detach');return;
					}
				}
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}
			
			function checkFee(){
				//扣款行手續費+入帳行手續費+交換所手續費+發動行手續費=0		
				var total1 = (parseFloat($('#OUT_BANK_FEE').val()) +
				parseFloat($('#IN_BANK_FEE').val()) +
				parseFloat($('#TCH_FEE').val()) +
				parseFloat($('#SND_BANK_FEE').val())+
// 				parseFloat($('#HANDLECHARGE').val())+
				parseFloat($('#WO_BANK_FEE').val())).toFixed(3);
				if(window.console){console.log("total1>>"+total1);}
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
				if(window.console){console.log("total2>>"+total2);}
				if(total2 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n折扣後扣款行手續費+折扣後入帳行手續費+\n折扣後發動行手續費+折扣後銷帳行手續費+\n折扣後交換所手續費=0");
					return false;
				}
				return true;
			}
			

		</script>
	</body>
</html>
