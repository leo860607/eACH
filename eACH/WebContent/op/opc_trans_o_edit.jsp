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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-檢視明細</title>
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
			.DATA_GROUP_1 {background-color: transparent;width:25%}
			.DATA_GROUP_2 {background-color: transparent;width:25%}
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
				<html:form styleId="formID" action="/opc_trans">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<html:hidden property="pageForSort" styleId="pageForSort"/>	
					<bean:define id="detailData" name="opc_trans_form" property="detailData"/>
					<!-- OPCTRANSACTIONLOGTAB -->
					<logic:equal name="opc_trans_form" property="pageType" value="A">
						<logic:iterate id="detail" name="opc_trans_form" property="detailData">
							<table>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>系統追蹤序號</td>													
									<td width="25%"><bean:write name="detail"  property="STAN"/></TD>
									<td class="header DATA_GROUP_1" nowrap>交易日期/時間</td>													
									<td><bean:write name="detail" property="TXDATE"/>&nbsp;<bean:write name="detail" property="TXTIME"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>WEB交易追蹤序號</td>													
									<td><bean:write name="detail" property="WEBTRACENO"/></TD>
									<td class="header DATA_GROUP_1" nowrap>銀行</td>													
									<td><bean:write name="detail" property="BANKID"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>交易代號</td>													
									<td><bean:write name="detail" property="PCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>回應代碼</td>													
									<td><bean:write name="detail" property="RSPCODE"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>ID欄</td>													
									<td><bean:write name="detail" property="IDFIELD"/></TD>
									<td class="header DATA_GROUP_1" nowrap>資料欄</td>													
									<td><bean:write name="detail" property="DATAFIELD"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>FEP交易追蹤序號</td>													
									<td><bean:write name="detail" property="FEPTRACENO"/></TD>
									<td class="header DATA_GROUP_1" nowrap>FEP處理結果</td>													
									<td><bean:write name="detail" property="FEPPROCESSRESULT"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>確認訊息回應代碼</td>													
									<td><bean:write name="detail" property="CONCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>確認訊息回應時間</td>													
									<td><bean:write name="detail" property="CONTIME"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>應用系統狀態查詢結果</td>													
									<td><bean:write name="detail" property="INQSTATUS"/></TD>
									<td></td><td></td>
								</tr>
							</table>
						</logic:iterate>
					</logic:equal>
					<!-- PENDINGLOGTAB -->
					<logic:equal name="opc_trans_form" property="pageType" value="B">
						<logic:iterate id="detail" name="opc_trans_form" property="detailData">
							<table>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>系統追蹤序號</td>													
									<td width="25%"><bean:write name="detail" property="STAN"/></TD>
									<td class="header DATA_GROUP_1" nowrap>交易日期/時間</td>													
									<td><bean:write name="detail" property="TXDT"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>交易代號</td>													
									<td><bean:write name="detail" property="PCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>回應代碼</td>													
									<td><bean:write name="detail" property="RSPCODE"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>狀態</td>													
									<td><bean:write name="detail" property="STATUS"/></TD>
									<td class="header DATA_GROUP_1" nowrap>處理狀況</td>													
									<td><bean:write name="detail" property="PENDING"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>逾時註記</td>													
									<td><bean:write name="detail" property="TIMEOUTCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>檢核結果</td>													
									<td><bean:write name="detail" property="RSPRESULTCODE"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>處理結果</td>													
									<td><bean:write name="detail" property="RESULTCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap></td>													
									<td><bean:write name="detail" property=""/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>發動單位代號</td>													
									<td><bean:write name="detail" property="SENDERBANK"/></TD>
									<td class="header DATA_GROUP_1" nowrap>接收單位代號</td>													
									<td><bean:write name="detail" property="RECEIVERBANK"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>原交易日期</td>													
									<td><bean:write name="detail" property="OTXDATE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>原交易序號</td>													
									<td><bean:write name="detail" property="OSTAN"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>原交易金額</td>													
									<td><bean:write name="detail" property="TXAMT"/></TD>
									<td class="header DATA_GROUP_1" nowrap>端末機代號</td>													
									<td><bean:write name="detail" property="TRMLID"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>原發動行代號</td>													
									<td><bean:write name="detail" property="SENDERBANKID"/></TD>
									<td class="header DATA_GROUP_1" nowrap></td>													
									<td><bean:write name="detail" property=""/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>原扣款行代號</td>													
									<td><bean:write name="detail" property="OUTBANKID"/></TD>
									<td class="header DATA_GROUP_1" nowrap>原扣款帳號</td>													
									<td><bean:write name="detail" property="OUTACCTNO"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>原入帳行代號</td>													
									<td><bean:write name="detail" property="INBANKID"/></TD>
									<td class="header DATA_GROUP_1" nowrap>原入帳帳號</td>													
									<td><bean:write name="detail" property="INACCTNO"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>DTREQ</td>													
									<td><bean:write name="detail" property="DTREQ"/></TD>
									<td class="header DATA_GROUP_1" nowrap>DTRSP</td>													
									<td><bean:write name="detail" property="DTRSP"/></TD>
								</tr>
							</table>
						</logic:iterate>
					</logic:equal>	
					<!-- SETTLEMENLOGTAB -->
					<logic:equal name="opc_trans_form" property="pageType" value="C">
						<logic:iterate id="detail" name="opc_trans_form" property="detailData">
							<table>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>系統追蹤序號</td>													
									<td width="25%"><bean:write name="detail" property="STAN"/></TD>
									<td class="header DATA_GROUP_1" nowrap>交易日期/時間</td>													
									<td><bean:write name="detail" property="TXDT"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>交易代號</td>													
									<td><bean:write name="detail" property="PCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>回應代碼</td>													
									<td><bean:write name="detail" property="RSPCODE"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>狀態</td>													
									<td><bean:write name="detail" property="STATUS"/></TD>
									<td class="header DATA_GROUP_1" nowrap>處理狀況</td>													
									<td><bean:write name="detail" property="PENDING"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>逾時註記</td>													
									<td><bean:write name="detail" property="TIMEOUTCODE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>檢核結果</td>													
									<td><bean:write name="detail" property="RSPRESULTCODE"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>營業日</td>													
									<td><bean:write name="detail" property="BIZDATE"/></TD>
									<td class="header DATA_GROUP_1" nowrap>清算階段</td>													
									<td><bean:write name="detail" property="CLEARINGPHASE"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>發動單位代號</td>													
									<td><bean:write name="detail" property="SENDERBANK"/></TD>
									<td class="header DATA_GROUP_1" nowrap>接收單位代號</td>													
									<td><bean:write name="detail" property="RECEIVERBANK"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>被查詢行庫代號</td>													
									<td><bean:write name="detail" property="BANKID"/></TD>
									<td class="header DATA_GROUP_1" nowrap>營業旗標</td>													
									<td><bean:write name="detail" property="BIZFLAG"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>業務類別</td>													
									<td><bean:write name="detail" property="BIZGROUP"/></TD>
									<td class="header DATA_GROUP_1" nowrap></td>													
									<td><bean:write name="detail" property=""/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>DTREQ</td>													
									<td><bean:write name="detail" property="DTREQ"/></TD>
									<td class="header DATA_GROUP_1" nowrap>DTRSP</td>													
									<td><bean:write name="detail" property="DTRSP"/></TD>
								</tr>
							</table>
						</logic:iterate>
					</logic:equal>
					<%-- 強迫使用者關閉分頁--%>
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
				$("form").submit();
			}
		</script>		
	</body>
</html>