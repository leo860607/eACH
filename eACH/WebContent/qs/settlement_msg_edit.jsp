<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-檢視明細</title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/newStyle.css">
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
			.DATA_GROUP_2 {background-color: #FFD6C2;width:25%}
			.DATA_GROUP_3 {background-color: #74b6e3;width:25%}
		</style>
	</head>
	<body>
		<div id="container">
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
					<html:hidden property="pageForSort" styleId="pageForSort"/>	
					<bean:define id="detailData" name="opc_trans_form" property="detailData"/>
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
								<tr>
									<td class="header DATA_GROUP_2" nowrap>ACH應付筆數</td>
									<td><bean:write name="detail" property="ACHPAYCNT" /></td>
									<td class="header DATA_GROUP_2" nowrap>ACH應收筆數</td>
									<td><bean:write name="detail" property="ACHRECVCNT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>ACH應付金額</td>
									<td><bean:write name="detail" property="ACHPAYAMT" /></td>
									<td class="header DATA_GROUP_2" nowrap>ACH應收金額</td>
									<td><bean:write name="detail" property="ACHRECVAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH應付筆數</td>
									<td><bean:write name="detail" property="CCACHPAYCNT" /></td>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH應收筆數</td>
									<td><bean:write name="detail" property="CCACHRECVCNT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH應付金額</td>
									<td><bean:write name="detail" property="CCACHPAYAMT" /></td>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH應收金額</td>
									<td><bean:write name="detail" property="CCACHRECVAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>ACH沖正應付筆數</td>
									<td><bean:write name="detail" property="ACHRVSPAYCNT" /></td>
									<td class="header DATA_GROUP_2" nowrap>ACH沖正應收筆數</td>
									<td><bean:write name="detail" property="ACHRVSRECVCNT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>ACH沖正應付金額</td>
									<td><bean:write name="detail" property="ACHRVSPAYAMT" /></td>
									<td class="header DATA_GROUP_2" nowrap>ACH沖正應收金額</td>
									<td><bean:write name="detail" property="ACHRVSRECVAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH沖正應付筆數</td>
									<td><bean:write name="detail" property="CCACHRVSPAYCNT" /></td>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH沖正應收筆數</td>
									<td><bean:write name="detail" property="CCACHRVSRECVCNT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH沖正應付金額</td>
									<td><bean:write name="detail" property="CCACHRVSPAYAMT" /></td>
									<td class="header DATA_GROUP_2" nowrap>代理清算ACH沖正應收金額</td>
									<td><bean:write name="detail" property="CCACHRVSRECVAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_3" nowrap>應付筆數</td>
									<td><bean:write name="detail" property="PAYCNT" /></td>
									<td class="header DATA_GROUP_3" nowrap>應收筆數</td>
									<td><bean:write name="detail" property="RECVCNT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_3" nowrap>應付金額</td>
									<td><bean:write name="detail" property="PAYAMT" /></td>
									<td class="header DATA_GROUP_3" nowrap>應收金額</td>
									<td><bean:write name="detail" property="RECVAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_3" nowrap>應收付淨額</td>
									<td colspan="2" ><bean:write name="detail" property="NETAMT" /></td>
								</tr>	
								<tr>
									<td class="header DATA_GROUP_3" nowrap>應付手續費(含角分)</td>
									<td><bean:write name="detail" property="PAYFEEAMT" /></td>
									<td class="header DATA_GROUP_3" nowrap>應收手續費(含角分)</td>
									<td><bean:write name="detail" property="RECVFEEAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_3" nowrap>沖正應付手續費(含角分)</td>
									<td><bean:write name="detail" property="RVSPAYFEEAMT" /></td>
									<td class="header DATA_GROUP_3" nowrap>沖正應收手續費(含角分)</td>
									<td><bean:write name="detail" property="RVSRECVFEEAMT" /></td>
								</tr>
								<tr>
									<td class="header DATA_GROUP_3" nowrap>應收付手續費淨額(含角分)</td>
									<td colspan="2"><bean:write name="detail" property="NETFEEAMT" /></td>
								</tr>
								
						</table>
						</logic:iterate>
					</logic:equal>
					<%-- 強迫使用者關閉分頁
					<table>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="back"  onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
					--%>
				</html:form>
			</div>			
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