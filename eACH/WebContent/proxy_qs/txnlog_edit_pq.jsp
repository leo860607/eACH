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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-明細</title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			.DATA_GROUP_1 {background-color: #FFD6C2;width:25%}
			.DATA_GROUP_2 {background-color: #74b6e3;width:25%}
			.DATA_GROUP_V_2 {word-break: break-all}
		</style>
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
				<a href="#">檢視明細</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/txnlog">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>	
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="date_type_checked" styleId="date_type_checked" />
					<bean:define id="detailData" name="txnlog_form" property="detailData"/>	
					<table style="table-layout: fixed;">
						<tr>
							<td class="header DATA_GROUP_1" nowrap>營業日</td>													
							<td width="25%"><bean:write name="detailData"  property="BIZDATE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>交易清算階段</td>													
							<td><bean:write name="detailData"  property="CLEARINGPHASE"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>業者交易日期時間</td>													
							<td><bean:write name="detailData" property="TRANSACTIONDATETIME"/></TD>
							<td class="header DATA_GROUP_1" nowrap>票交所處理日期時間</td>													
							<td><bean:write name="detailData"  property="HANDLEDATETIME"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>業者交易序號</td>													
							<td><bean:write name="detailData"  property="SEQ"/></TD>
							<td class="header DATA_GROUP_1" nowrap>系統追蹤序號</td>													
							<td><bean:write name="detailData"  property="STAN"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>業者統一編號</td>													
							<td><bean:write name="detailData" property="AGENT_COMPANY_ID"/></TD>
							<td class="header DATA_GROUP_1" nowrap>接收單位編號</td>													
							<td><bean:write name="detailData"  property="RECEIVERID"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>業者交易類別</td>													
							<td><bean:write name="detailData" property="PROCESSCODE"/>
							-<bean:write name="detailData" property="PROCESSCODE_NAME"/>
							</TD>
							<td class="header DATA_GROUP_1" nowrap>交易類別</td>													
							<td><bean:write name="detailData"  property="TG_PCODE"/>
							-<bean:write name="detailData"  property="TG_PCODE_NAME"/>
							</TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>交易回應代碼</td>													
							<td><bean:write name="detailData" property="RESPONSECODE"/>
							-<bean:write name="detailData" property="RESPONSECODE_NAME"/>
							</TD>
							<td class="header DATA_GROUP_1" nowrap>交易代號</td>													
							<td><bean:write name="detailData"  property="TXID"/>
							-<bean:write name="detailData"  property="TXN_NAME"/>
							</TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>交易金額</td>													
							<td><bean:write name="detailData"  property="TRANSACTIONAMOUNT" format="###.##"/></TD>
							<td class="header DATA_GROUP_1" nowrap></td>													
							<td></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>業者交易結果</td>													
							<td><bean:write name="detailData"  property="TG_RESULT"/></TD>	
							<td class="header DATA_GROUP_1" nowrap>圈存交易結果</td>													
							<td><bean:write name="detailData"  property="OB_RESULT"/></TD>	
<!-- 							<td class="header DATA_GROUP_1" nowrap></td>													 -->
<!-- 							<td></TD> -->
<!-- 							<td class="header DATA_GROUP_1" nowrap>業者交易結果</td>													 -->
<%-- 							<td><bean:write name="detailData"  property="TG_RESULT"/></TD> --%>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>客戶支付手續費</td>													
							<td><bean:write name="detailData"  property="CUST_FEE" format="#,###.##"/></TD>
							<td class="header DATA_GROUP_1" nowrap>發動者專區</td>													
							<td><bean:write name="detailData"  property="ISSUERREMARK"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>入帳單位代號</td>													
							<td><bean:write name="detailData"  property="INBANK_NAME"/></TD>
							<td class="header DATA_GROUP_1" nowrap>扣款單位代號</td>													
							<td><bean:write name="detailData"  property="OUTBANK_NAME"/>
							</TD>	
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>入帳者帳號</td>													
							<td><bean:write name="detailData"  property="INACCOUNTNUM"/></TD>		
							<td class="header DATA_GROUP_1" nowrap>入帳者統一編號</td>													
							<td><bean:write name="detailData"  property="INNATIONALID"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>扣款者帳號</td>													
							<td><bean:write name="detailData"  property="OUTACCOUNTNUM"/></TD>																		
							<td class="header DATA_GROUP_1" nowrap>扣款者統一編號</td>													
							<td><bean:write name="detailData"  property="OUTNATIONALID"/></TD>
						</tr>

						<tr>		
							<td class="header DATA_GROUP_1" nowrap>檢核類型</td>													
							<td><bean:write name="detailData"  property="CHECKTYPE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>存摺摘要</td>													
							<td><bean:write name="detailData"  property="NOTE" /></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>用戶號碼</td>													
							<td><bean:write name="detailData"  property="USERNUM"/></TD>		
							<td class="header DATA_GROUP_1" nowrap>原特店代號</td>													
							<td class = "DATA_GROUP_V_2"><bean:write name="detailData"  property="ORGMERCHANTID"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>特店代號</td>
							<td class = "DATA_GROUP_V_2"><bean:write name="detailData" property="MERCHANTID"/></TD>
						</tr>
					</table>
						
					<table style="table-layout: fixed;">	
						<tr>
							<td class="header DATA_GROUP_2" nowrap>訂單編號</td>
							<td class = "DATA_GROUP_V_2"><bean:write name="detailData" property="ORDERNUM"/></td>
							<td class="header DATA_GROUP_2" nowrap>原訂單編號</td>													
							<td class = "DATA_GROUP_V_2"><bean:write name="detailData"  property="ORGORDERNUM"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>端末機代號</td>													
							<td><bean:write name="detailData"  property="TERMINERID"/></TD>
							<td class="header DATA_GROUP_2" nowrap>原端末機代號</td>													
							<td><bean:write name="detailData"  property="ORGTERMINERID"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>驗證回覆資料區</td>													
							<td><bean:write name="detailData"  property="VERIFIEDDATA"/></td>
							<td class="header DATA_GROUP_2" nowrap>原交易追蹤序號</td>													
							<td><bean:write name="detailData"  property="ORGTRANSACTIONNO"/></td>
						</tr>	
						<tr>
							<td class="header DATA_GROUP_2" nowrap>原交易日期</td>													
							<td><bean:write name="detailData"  property="ORGTRANSACTIONDATE"/></td>
							<td class="header DATA_GROUP_2" nowrap>原交易金額</td>													
							<td><bean:write name="detailData"  property="ORGTRANSACTIONAMOUNT"/></td>
						</tr>	
						<tr>
							<td class="header DATA_GROUP_2" nowrap>銷帳單位</td>
							<td>
								<bean:write name="detailData" property="BILLFLAG"/>
								<logic:equal  name="detailData" property="BILLFLAG" value="1">
									-入帳行銷帳
								</logic:equal>
								<logic:equal  name="detailData" property="BILLFLAG" value="2">
									-發動行銷帳
								</logic:equal>
							</td>
							<td class="header DATA_GROUP_2" nowrap>銷帳資料類型</td>
							<td>
								<bean:write name="detailData"  property="BILLTYPE"/>
								<logic:equal  name="detailData" property="BILLTYPE" value="A">
									-虛擬帳號
								</logic:equal>
								<logic:equal  name="detailData" property="BILLTYPE" value="B">
									-銷帳編號
								</logic:equal>
								<logic:equal  name="detailData" property="BILLTYPE" value="C">
									-三段式條碼
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>銷帳資料區</td>													
							<td><bean:write name="detailData"  property="BILLDATA"/></td>
							<td class="header DATA_GROUP_2" nowrap>原銷帳資料區</td>													
							<td width="25%" style="word-break:break-all;"><bean:write name="detailData"  property="ORGBILLDATA"/></td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>收費業者統一編號</td>
							<td><bean:write name="detailData"  property="TOLLID"/></td>
							<td class="header DATA_GROUP_2" nowrap>繳費類別</td>
							<td><bean:write name="detailData" property="PFCLASS"/></td>
							
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>繳費工具類型</td>
							<td>
								<bean:write name="detailData"  property="CHARGETYPE"/>
								<logic:equal  name="detailData" property="CHARGETYPE" value="1">
									-約定授權
								</logic:equal>
								<logic:equal  name="detailData" property="CHARGETYPE" value="2">
									-晶片金融卡
								</logic:equal>
								<logic:equal  name="detailData" property="CHARGETYPE" value="3">
									-本人帳戶繳費
								</logic:equal>
							</td>
							<td class="header DATA_GROUP_2" nowrap></td>
							<td></td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>繳費工具驗證資料區</td>
							<td width="25%" style="word-break:break-all;" colspan="3"><bean:write name="detailData" property="CHECKDATA" /></td>
						</tr>
					</table>
						
					<table>	
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
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
			function back(str){
				$("#ac_key").val(str);
				$("#target").val('search');
				$("#date_type_checked").val('<bean:write name="txnlog_form" property="date_type_checked"/>');
				$("form").submit();
			}
		</script>		
	</body>
</html>