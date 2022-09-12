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
		<title><bean:write name="login_form" property="userData.s_func_name"/></title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
	</head>
	<body>
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<html:form styleId="formID" action="/alert_monitor">
				<html:hidden property="ac_key" styleId="ac_key" value=""/>
				<html:hidden property="target" styleId="target" value=""/>
				
			
				<div id="opPanel" style="padding-top:10px;padding-bottom: 10px">
					<fieldset>
					<legend>監控設定</legend>
						大額監控設定：
						<table>
						<tr>
							<td class="header necessary" ><span>監控間隔</span></td>
							<td><html:text styleId="MONITOR_AMOUNT_PERIOD" property="MONITOR_AMOUNT_PERIOD" size="3" maxlength="3" styleClass="validate[required,custom[number]]"></html:text> 分鐘</td>
						</tr>
						<tr>
							<td class="header necessary"><span>監控金額</span></td>
							<td><html:text styleId="MONITOR_AMOUNT" property="MONITOR_AMOUNT" size="10" maxlength="10" styleClass="validate[required,custom[number]]"></html:text> 元</td>
						</tr>
						</table>
						<br>
						逾時監控設定：
						<table>
						<tr>
							<td class="header necessary" ><span>監控間隔</span></td>
							<td><html:text styleId="MONITOR_PENDING_PERIOD" property="MONITOR_PENDING_PERIOD" size="3" maxlength="3" styleClass="validate[required,custom[number]]"> </html:text> 分鐘</td>
						</tr>
						<tr>
							<td class="header necessary"><span>監控筆數</span></td>
							<td><html:text styleId="MONITOR_PENDING" property="MONITOR_PENDING" size="2" maxlength="2" styleClass="validate[required,custom[number]]"></html:text> 筆</td>
						</tr>
						</table>
						<br>
						<table>
						<tr>
						告警郵件收件者：
						<td class="header necessary"><span>郵件地址</span></td>
							<td colspan="8"><html:text styleId="MONITOR_MAILRECEIVER" property="MONITOR_MAILRECEIVER" size="100" maxlength="2000" styleClass="validate[required,notChinese]"></html:text></td>
							<td>(多收件者請用","分隔)</td>
						</tr>
						<tr>
							<td	 style="padding: 10px; vertical-align: middle">
								<label class="btn" id="monitor_set" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
							</td>
							<td	 style="padding: 10px; vertical-align: middle">
								<label class="btn" id="refill" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;回復</label>
							</td>
						</tr>
						</table>
					</fieldset>
				</div>
			</html:form>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var amount = '<bean:write name="alert_monitor_form" property="MONITOR_AMOUNT"/>';
		var amount_p = '<bean:write name="alert_monitor_form" property="MONITOR_AMOUNT_PERIOD"/>';
		var pending = '<bean:write name="alert_monitor_form" property="MONITOR_PENDING"/>';
		var pending_p = '<bean:write name="alert_monitor_form" property="MONITOR_PENDING_PERIOD"/>';
		var mailr = '<bean:write name="alert_monitor_form" property="MONITOR_MAILRECEIVER"/>';
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				alterMsg();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="alert_monitor_form" property="msg"/>';
				if(msg != ""){
					alert(msg);
				}
			}
			
			function onPut(str){
				if(str == "monitor_set"){
					if(!$("#formID").validationEngine("validate")){
						return
					}
					$('#MONITOR_AMOUNT').val($('#MONITOR_AMOUNT').val().replace(/\,/g,""));
					$("#ac_key").val(str);
					$("#target").val('search');
					$("form").submit();
				}else if (str =="refill"){
					$('#MONITOR_AMOUNT').val(amount);
					$('#MONITOR_AMOUNT_PERIOD').val(amount_p);
					$('#MONITOR_PENDING').val(pending);
					$('#MONITOR_PENDING_PERIOD').val(pending_p);
					$('#MONITOR_MAILRECEIVER').val(mailr);
				}
			}
			$("#MONITOR_AMOUNT").on("keyup",function(){
				this.value =this.value.replace(/,/g, '').replace(/\d+?(?=(?:\d{3})+$)/g, function(s){
	                return s +',';
	            });
			$("#MONITOR_AMOUNT").val(this.value);
			});
			$("#MONITOR_AMOUNT").on("focus",function(){
				$("#MONITOR_AMOUNT").val("");
			});
		</script>
	</body>
</html>