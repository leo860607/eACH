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
				<html:form styleId="formID" action="/wk_date" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="IS_TXN_DATE_VAL" styleId="IS_TXN_DATE_VAL" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<table>
						<tr>
							<td class="header necessary" style="width: 50%"><span>營業日</span></td>
							<td>
								<html:text styleClass="lock" styleId="TXN_DATE" property="TXN_DATE" readonly="true" size="8" maxlength="8"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header"><span>星期</span></td>
							<td>
								<html:select styleId="WEEKDAY" property="WEEKDAY" disabled="true">
									<html:option value="1">一</html:option>
									<html:option value="2">二</html:option>
									<html:option value="3">三</html:option>
									<html:option value="4">四</html:option>
									<html:option value="5">五</html:option>
									<html:option value="6">六</html:option>
									<html:option value="7">日</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header"><span>是否為營業日</span></td>
							<td>
								<html:checkbox styleId="IS_TXN_DATE" property="IS_TXN_DATE"></html:checkbox>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
							<logic:equal  name="userData" property="s_auth_type" value="A">
								<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
							</logic:equal>	
								<label class="btn" id="back"  onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				checkDate();
				unblockUI();
	        });
			
			function init(){
				setDefaultIsTxnDate();
				$("#formID").validationEngine({promptPosition: "bottomRight"});
			}
			
			function setDefaultIsTxnDate(){
				if('<bean:write name="wk_date_form" property="IS_TXN_DATE"/>' == 'Y'){
					$("#IS_TXN_DATE").prop("checked", true);
				}else{
					$("#IS_TXN_DATE").prop("checked", false);
				}
			}
			
			function onPut(str){
				blockUI();
				if($("#IS_TXN_DATE").prop("checked") == true){
					$("#IS_TXN_DATE_VAL").val("Y");
				}else{
					$("#IS_TXN_DATE_VAL").val("N");
				}
				$("#WEEKDAY").prop("disabled", false);
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}
			
			//判斷日期是否為當天以前，或時間為當天中午12點之後
			function checkDate(){
				var year = '0'+parseInt(new Date().getFullYear()-1911);
				var month = parseInt(new Date().getMonth()+1);
				var day = new Date().getDate();
				
				if(String(day).length<2){
					day = '0'+String(day);
				}
				var hour = new Date().getHours();
// 				20170221 add by hugo <10 要加0否則會跟
				month = month<10 ? '0'+month:month;
				var today = year+''+month+''+day;
				var date = $("#TXN_DATE").val();
				var check1= date < today;
				var check2= date == today;
				var check3= (hour>11);
				
				console.log('date:'+date);
				console.log('today:'+today);
				console.log('hour:'+hour);
				
				console.log('check1:'+check1);
				console.log('check2:'+check2);
				console.log('check3:'+check3);
// 				if( (date < today) || (date == today)&&(hour>11) ){
				if( check1 || check2 && check3 ){	
					$("#IS_TXN_DATE").attr('disabled','true');
					$("#update").attr('disabled','true');
				}
				
			}
		</script>
	</body>
</html>
