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
			.DATA_GROUP_1 {background-color: #FFD6C2;width:25% ; text-align: right;padding-right: 20px}
			.DATA_GROUP_11 {background-color: #FFD6C2;width:25% ;text-align: left ;padding-left: 20px}
			.DATA_GROUP_1A {background-color: #FFD6C2;width:25% ;text-align: center }
			.DATA_GROUP_2 {background-color: #74b6e3;width:25% ; text-align: right;padding-right: 20px}
			.DATA_GROUP_22 {background-color: #74b6e3;width:25% ;text-align: left ;padding-left: 20px}
			.DATA_GROUP_2A {background-color: #74b6e3;width:25% ;text-align: center; }
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
				<html:form styleId="formID" action="/onclearing_adj">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="serchStrs" styleId="serchStrs"/>	
					<bean:define id="map_BH" name="onclearing_adj_form" property="map_BH"/>
					<table>
						<tr>
							<td class="DATA_GROUP_2A" colspan="2" align="center" nowrap >連線結算檔</td>													
							<td class="DATA_GROUP_1A" colspan="2" align="center" nowrap>批次結算比對檔</td>													
						</tr>
<!-- 						<tr> -->
<!-- 							<td class="header DATA_GROUP_2" nowrap>營業日</td>													 -->
<%-- 							<td><bean:write name="map_BH"  property="BIZDATE"/></TD> --%>
<!-- 							<td class="header DATA_GROUP_1" nowrap>營業日</td>													 -->
<%-- 							<td width="25%"><bean:write name="map_BH"  property="BIZDATE"/></TD> --%>
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td class="header DATA_GROUP_2" nowrap>清算階段</td>													 -->
<%-- 							<td><bean:write name="map_BH"  property="CLEARINGPHASE"/></TD> --%>
<!-- 							<td class="header DATA_GROUP_1" nowrap>清算階段</td>													 -->
<%-- 							<td width="25%"><bean:write name="map_BH"  property="CLEARINGPHASE"/></TD> --%>
<!-- 						</tr> -->
						<tr>
							<td class="DATA_GROUP_2" nowrap>銀行代號</td>													
							<td class="DATA_GROUP_22" ><bean:write name="map_BH"  property="BANKNAME"/></TD>
							<td class="DATA_GROUP_1" nowrap>銀行代號</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BANKNAME"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應收筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RECVCNT" format="######"/></TD>
							<td class="DATA_GROUP_1" nowrap>應收筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RECVCNT" format="######"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應收金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RECVAMT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>應收金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RECVAMT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應付筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_PAYCNT" format="######"/></TD>
							<td class="DATA_GROUP_1" nowrap>應付筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_PAYCNT" format="######"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應付金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_PAYAMT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>應付金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_PAYAMT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正應收筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSRECVCNT" format="######"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正應收筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSRECVCNT" format="######"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正應收金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSRECVAMT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正應收金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSRECVAMT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正應付筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSPAYCNT" format="######"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正應付筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSPAYCNT" format="######"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正應付金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSPAYAMT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正應付金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSPAYAMT" format="###,###"/></TD>
						</tr>
						</table>
						
						<table>					
						<tr>
							<td class="DATA_GROUP_2" nowrap>應收同業手續費筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RECVMBFEECNT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>應收同業手續費筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RECVMBFEECNT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應收同業手續費金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RECVMBFEEAMT" format="#,#0.0#"/></TD>
							<td class="DATA_GROUP_1" nowrap>應收同業手續費金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RECVMBFEEAMT" format="#,#0.0#"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應付同業手續費筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_PAYMBFEECNT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>應付同業手續費筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_PAYMBFEECNT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應付同業手續費金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_PAYMBFEEAMT" format="#,#0.0#"/></TD>
							<td class="DATA_GROUP_1" nowrap>應付同業手續費金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_PAYMBFEEAMT" format="#,#0.0#"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應付交換所手續費筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_PAYEACHFEECNT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>應付交換所手續費筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_PAYEACHFEECNT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>應付交換所手續費金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_PAYEACHFEEAMT" format="#,#0.0#"/></TD>
							<td class="DATA_GROUP_1" nowrap>應付交換所手續費金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_PAYEACHFEEAMT" format="#,#0.0#"/></TD>
						</tr>
						
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正-應收同業手續費筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSRECVMBFEECNT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正-應收同業手續費筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSRECVMBFEECNT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正-應收同業手續費金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSRECVMBFEEAMT" format="#,#0.0#"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正-應收同業手續費金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSRECVMBFEEAMT" format="#,#0.0#"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正-應付同業手續費筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSPAYMBFEECNT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正-應付同業手續費筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSPAYMBFEECNT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正-應付同業手續費金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSPAYMBFEEAMT" format="#,#0.0#"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正-應付同業手續費金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSPAYMBFEEAMT" format="#,#0.0#"/></TD>
						</tr>
						
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正-應收交換所手續費筆數</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSRECVMBFEECNT" format="###,###"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正-應收交換所手續費筆數</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSRECVMBFEECNT" format="###,###"/></TD>
						</tr>
						<tr>
							<td class="DATA_GROUP_2" nowrap>沖正-應收交換所手續費金額</td>													
							<td class="DATA_GROUP_22"><bean:write name="map_BH"  property="CL_RVSRECVEACHFEEAMT" format="#,#0.0#"/></TD>
							<td class="DATA_GROUP_1" nowrap>沖正-應收交換所手續費金額</td>													
							<td class="DATA_GROUP_11"><bean:write name="map_BH"  property="BH_RVSRECVEACHFEEAMT" format="#,#0.0#"/></TD>
						</tr>				
						
						
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