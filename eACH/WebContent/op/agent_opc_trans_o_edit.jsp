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
				<html:form styleId="formID" action="/agent_opc_trans">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<html:hidden property="pageForSort" styleId="pageForSort"/>	
					<bean:define id="detailData" name="rpt_agent_form" property="detailData"/>
					<!-- OPCTRANSACTIONLOGTAB -->
						<logic:iterate id="detail" name="rpt_agent_form" property="detailData">
							<table>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>業者交易日期時間</td>													
									<td width="25%"><bean:write name="detail"  property="TMP_TXDATE" ignore="true" />&nbsp;<bean:write name="detail"  property="TXTIME" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>業者交易序號</td>													
									<td><bean:write name="detail" property="SEQ" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>業者交易類別</td>													
									<td><bean:write name="detail" property="PROCESSCODE" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>訊息類別</td>													
									<td><bean:write name="detail" property="MSGTYPE" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>發動單位編號</td>													
									<td><bean:write name="detail" property="ISSUERID" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>接收單位編號</td>													
									<td><bean:write name="detail" property="RECEIVERID" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>被查詢交易日期</td>													
									<td><bean:write name="detail" property="QRYTRANSACTIONDATE" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>被查詢交易序號</td>													
									<td><bean:write name="detail" property="QRYSEQUENCENUM" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>發動者專區</td>													
									<td colspan="3"><bean:write name="detail" property="ISSUERREMARK" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>回應代碼</td>													
									<td colspan="3"><bean:write name="detail" property="RESPONSECODE" ignore="true"/>&nbsp;<bean:write name="detail" property="RESPONSECODE_NAME" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>交易追蹤序號</td>													
									<td><bean:write name="detail" property="STAN" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>原交易回應代碼</td>													
									<td><bean:write name="detail" property="RSPCODE" ignore="true"/>&nbsp;<bean:write name="detail" property="RSPCODE_NAME" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>交易營業日</td>													
									<td><bean:write name="detail" property="BIZDATE" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>票交所處理日期時間</td>													
									<td><bean:write name="detail" property="ACH_TXDATE" ignore="true"/>&nbsp;<bean:write name="detail" property="ACH_TXTIME" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>交易清算階段</td>													
									<td><bean:write name="detail" property="CLEARINGPHASE" ignore="true"/></TD>
									<td class="header DATA_GROUP_1" nowrap>客戶支付手續費</td>													
									<td><bean:write name="detail" property="CUST_FEE" ignore="true" format="#,##0.00"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>金額</td>													
									<td><bean:write name="detail" property="TRANSACTIONAMOUNT" ignore="true" format="###.##"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>訊息代碼</td>													
									<td colspan="3"><bean:write name="detail" property="MSGCODE" ignore="true"/></TD>
								</tr>
								<tr>
									<td class="header DATA_GROUP_1" nowrap>訊息</td>													
									<td colspan="3"><bean:write name="detail" property="MESSAGE" ignore="true" /></TD>
								</tr>
							</table>
						</logic:iterate>
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