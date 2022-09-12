<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>new body</title>
		<link rel="stylesheet" type="text/css" href="./css/newStyle.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#login_info tr:odd").addClass("resultDataRowOdd");
				$("#login_info tr:even").addClass("resultDataRowEven");
			});
		</script>
	</head>
	<body>
		<div style="height: 10px"></div>
		<table id="login_info">
			<thead>
				<tr>
					<th colspan="4" style="height:350px;background-image:url('./images/page_background.jpg')">
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="header" width="150px">本次簽入時間</td>
					<td width="300px">
						<bean:write name="login_form" property="THIS_LOGIN_DATE"/>
					</td>
					<td class="header" width="200px">本次簽入網路位址</td>
					<td width="300px">
						<bean:write name="login_form" property="THIS_LOGIN_IP"/>
					</td>
				</tr>
				<tr>
					<td class="header">上次簽入時間</td>
					<td>
						<bean:write name="login_form" property="userData.LAST_LOGIN_DATE"/>
					</td>
					<td class="header">上次簽入網路位址</td>
					<td>
						<bean:write name="login_form" property="userData.LAST_LOGIN_IP"/>
					</td>
				</tr>
				<logic:equal name="login_form" property="result" value="FALSE">
					<tr>
						<td class="header">憑證效期</td>
						<td colspan="3">憑證即將在「<bean:write name="login_form" property="ikeyValidateDate"/>」到期，請<a href="<bean:write name="login_form" property="ikeyExtendURL"/>">按此</a>更新</td>
					</tr>
				</logic:equal>
			</tbody>
		</table>
	</body>
</html>