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
			.DATA_GROUP_1 {background-color: #FFD6C2;width:25%}
			.DATA_GROUP_2 {background-color: #74b6e3;width:25%}
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
				<html:form styleId="formID" action="/clear_data">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="colForSort" styleId="colForSort"/>
					<html:hidden property="ordForSort" styleId="ordForSort"/>
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>	
					<bean:define id="detailData" name="clear_data_form" property="detailData"/>
					<table>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>營業日</td>													
							<td width="25%"><bean:write name="detailData"  property="BIZDATE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>清算階段代號</td>													
							<td><bean:write name="detailData"  property="CLEARINGPHASE"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>總行代號</td>													
							<td><bean:write name="detailData" property="BANKID"/></TD>
							<td class="header DATA_GROUP_1" nowrap>交易類別</td>													
							<td><bean:write name="detailData"  property="PCODE"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>應收筆數</td>													
							<td><bean:write name="detailData"  property="RECVCNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_1" nowrap>應收金額</td>													
							<td><bean:write name="detailData"  property="RECVAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>應付筆數</td>													
							<td><bean:write name="detailData"  property="PAYCNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_1" nowrap>應付金額</td>													
							<td><bean:write name="detailData"  property="PAYAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>應收同業手續費筆數</td>													
							<td><bean:write name="detailData"  property="RECVMBFEECNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_1" nowrap>應收同業手續費金額</td>													
							<td><bean:write name="detailData"  property="RECVMBFEEAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>應付同業手續費筆數</td>													
							<td><bean:write name="detailData"  property="PAYMBFEECNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_1" nowrap>應付同業手續費金額</td>													
							<td><bean:write name="detailData"  property="PAYMBFEEAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>應付交換所手續費筆數</td>													
							<td><bean:write name="detailData"  property="PAYEACHFEECNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_1" nowrap>應付交換所手續費金額</td>													
							<td><bean:write name="detailData"  property="PAYEACHFEEAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_2" nowrap>沖正-應收筆數</td>													
							<td><bean:write name="detailData"  property="RVSRECVCNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_2" nowrap>沖正-應收金額</td>													
							<td><bean:write name="detailData"  property="RVSRECVAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_2" nowrap>沖正-應付筆數</td>													
							<td><bean:write name="detailData"  property="RVSPAYCNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_2" nowrap>沖正-應付金額</td>													
							<td><bean:write name="detailData"  property="RVSPAYAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_2" nowrap>沖正-應收同業手續費筆數</td>													
							<td><bean:write name="detailData"  property="RVSRECVMBFEECNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_2" nowrap>沖正-應收同業手續費金額</td>													
							<td><bean:write name="detailData"  property="RVSRECVMBFEEAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_2" nowrap>沖正-應付同業手續費筆數</td>													
							<td><bean:write name="detailData"  property="RVSPAYMBFEECNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_2" nowrap>沖正-應付同業手續費金額</td>													
							<td><bean:write name="detailData"  property="RVSPAYMBFEEAMT" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_2" nowrap>沖正-應收交換所手續費筆數</td>													
							<td><bean:write name="detailData"  property="RVSRECVEACHFEECNT" format="#,###"/></TD>
							<td class="header DATA_GROUP_2" nowrap>沖正-應收交換所手續費金額</td>													
							<td><bean:write name="detailData"  property="RVSRECVEACHFEEAMT" format="#,##0.00"/></TD>
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