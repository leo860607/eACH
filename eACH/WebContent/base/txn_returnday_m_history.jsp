<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>退回天數設定</title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/newStyle.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<!-- NECESSARY END -->
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				prepareGrid();
				unblockUI();
	        });
			
			function prepareGrid(){
				tableToGrid("#resultData",{
	            	width: 910,
	            	height: 360,
	            	//shrinkToFit: true,
	            	colModel: [
						{name:'交易代號', sortable: false, width:70},
						{name:'交易項目', sortable: false, width:90},
						{name:'啟用日期', sortable: false, width:80},
						{name:'退回天數', sortable: false, width:70},
						{name:'建立日期', sortable: false, width:90}
					],
					beforeSelectRow: function(rowid, e) {
					    return false;
					}
	            });
				$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
				$("#resultData .jqgrow:even").addClass('resultDataRowEven');
			}
		</script>
	</head>
	<body>
		<div id="container">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#" class="has-next">基本資料管理</a>
				<a href="#" class="has-next">退回天數設定</a>
				<a href="#">歷史紀錄</a>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData">
						<thead>
							<tr>
								<th>交易代號</th>
								<th>交易項目</th>
								<th>啟用日期</th>
								<th>退回天數</th>
								<th>建立日期</th>
							</tr>
						</thead>
						<tbody>
							<logic:notPresent name="txn_returnday_form" property="scaseary" >
								<tr>
									<td></td><td></td><td></td><td></td><td></td>
								</tr>
							</logic:notPresent>
							<logic:present name="txn_returnday_form" property="scaseary" >
								<logic:iterate id="qry" name="txn_returnday_form" property="scaseary" indexId="iNum">
									<tr>
										<td><bean:write name="qry" property="TXN_ID"/></td>
										<td><bean:write name="qry" property="TXN_NAME"/></td>
										<logic:equal name="qry" property="IS_ACTIVE" value="0">
											<td><font color=red>未啟用</font></td>
										</logic:equal>
										<logic:equal name="qry" property="IS_ACTIVE" value="1">
											<td><bean:write name="qry" property="ACTIVE_DATE"/></td>
										</logic:equal>
										<logic:equal name="qry" property="RETURN_DAY" value="0">
											<td><font color=red>未設定</font></td>
										</logic:equal>
										<logic:notEqual name="qry" property="RETURN_DAY" value="0">
											<td><bean:write name="qry" property="RETURN_DAY" format="#"/></td>
										</logic:notEqual>
										<td><bean:write name="qry" property="CDATE"/></td>
									</tr>
								</logic:iterate>
							</logic:present>
						</tbody>
					</table>
					<div style="width: 100%; text-align: center; margin-top: 10px">
						<label class="btn" id="search" onclick="blockUI();window.location.href='txn_returnday.do'"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
					</div>
				</fieldset>
			</div>
		</div>
	</body>
</html>