<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-明細</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
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
			<div id="dataInputTable" >
				<html:form styleId="formID" action="/userlog" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="fc_type" styleId="fc_type"/>
					<table >
						<tr>
<!-- 							<td class="header" style="width: 150px">用戶代號</td> -->
							<td class="header" style="width: 30%">用戶代號</td>
							<td style="width: 280px">
								<bean:write name="userlog_form" property="USERID"/>
							</td>
							<td class="header" style="width: 150px">所屬單位代號</td>
							<td>
								<bean:write name="userlog_form" property="USER_COMPANY"/>
							</td>
						</tr>
						<tr>
							<td class="header">登入IP</td>
							<td>
								<bean:write name="userlog_form" property="USERIP"/>
							</td>
							<td class="header">操作時間</td>
							<td>
								<bean:write name="userlog_form" property="TXTIME"/>
							</td>
						</tr>
						<tr>
							<td class="header">操作功能類型</td>
							<td>
<%-- 								<bean:write name="userlog_form" property="FUNC_TYPE"/> --%>
								<logic:equal name="userlog_form" property="FUNC_TYPE" value="1">作業模組</logic:equal>
								<logic:equal name="userlog_form" property="FUNC_TYPE" value="2">功能項目</logic:equal>
							</td>
							<td class="header">操作項目</td>
							<td>
<%-- 								<bean:write name="userlog_form" property="OPITEM"/> --%>
									<span id= "OPITEM"></span>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="A">新增</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="B">修改</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="C">查詢</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="D">刪除</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="E">報表列印</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="F">檔案下載</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="G">送出</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="H">重送</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="I">登入</logic:equal> --%>
<%-- 								<logic:equal name="userlog_form" property="OPITEM" value="J">登出</logic:equal> --%>
							</td>
						</tr>
						<tr>
							<td class="header">主功能代號</td>
							<td>
								<bean:write name="userlog_form" property="UP_FUNC_ID"/>
							</td>
							<td class="header">功能代號</td>
							<td>
								<bean:write name="userlog_form" property="FUNC_ID"/>
							</td>
						</tr>
						<tr>
							<td class="header">結果</td>
							<td colspan="3">
								<bean:write name="userlog_form" property="ADEXCODE"/>
							</td>
						</tr>
						<tr>
							<td class="header">變更前內容</td>
							<td colspan="3">
							<html:textarea name="userlog_form" readonly="true" property="BFCHCON" rows="5" cols="80"> </html:textarea>
<%-- 								<bean:write name="userlog_form" property="BFCHCON"/> --%>
							</td>
						</tr>
						<tr>
							<td class="header">變更後內容</td>
							<td colspan="3">
							<html:textarea name="userlog_form" readonly="true"  property="AFCHCON" rows="5" cols="80"> </html:textarea>
<%-- 								<bean:write name="userlog_form" property="AFCHCON"/> --%>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				$("div.show_on_success").toggle(document.URL.indexOf("?") !== -1);
				blockUI();
				init();
				unblockUI();
	        });
			
			function onPut(str){
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}	
			
			function init(){
				alterMsg();
				getOpt_Items();
				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker("#ACTIVE_DATE", 0);
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="userlog_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			function getOpt_Items(){
				var rdata = {component:"bank_group_bo", method:"getOpt_Items"};
				var vResult =fstop.getServerDataExII(uri, rdata, false);
				if(window.console){console.log("vResult>>"+vResult);}
				if(fstop.isNotEmpty(vResult)){
					opt_Items = vResult;
				}
				var tmp = opt_Items['<bean:write name="userlog_form" property="OPITEM"/>'];
				$("#OPITEM").html(tmp);
			}
			
			function back(str){
				$("#formID").validationEngine('detach');
				onPut(str);
			}
			
			
		</script>
	</body>
</html>
