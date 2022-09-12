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
		<script type="text/javascript" src="./js/json2.js"></script>
	</head>
	<body onload="unblockUI()">
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/ap_status" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<table id="search_tab">
						<tr>
							<td class="header necessary" style="width: 50%"><span>總行代號</span></td>
							<td><html:text styleId="BGBK_ID" property="BGBK_ID" readonly="true" styleClass="lock"></html:text></td>
						</tr>
						<tr>
							<td class="header necessary"><span>應用系統代號</span></td>
							<td><html:text styleId="APID" property="APID" readonly="true" styleClass="lock"></html:text></td>
						</tr>
						<tr>
							<td class="header"><span>系統狀態</span></td>
							<td>
								<html:select styleId="MBSYSSTATUS" property="MBSYSSTATUS">
									<html:option value="1">開機</html:option>
									<html:option value="2">押碼基碼同步</html:option>
									<html:option value="3">訊息通知</html:option>
									<html:option value="9">關機</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="header"><span>應用系統狀態</span></td>
							<td>
								<html:select styleId="MBAPSTATUS" property="MBAPSTATUS">
									<html:option value="1">簽到</html:option>
									<html:option value="2">暫時簽退</html:option>
									<html:option value="9">簽退</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center" style="padding: 10px; vertical-align: middle">
								<logic:equal name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="update" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label>
								</logic:equal>
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
			function back(id){
				$("#ac_key").val("back");
				$("#target").val('search');
				$("form").submit();
			}
			
			function onPut(str){
				//若要關機，必須原本為「簽退」狀態
				if('<bean:write name="ap_status_form" property="MBAPSTATUS"/>' != '9' &&
				   '<bean:write name="ap_status_form" property="MBSYSSTATUS"/>' != '9' &&
				   $("#MBAPSTATUS").val() != "9" && $("#MBSYSSTATUS").val() == "9"){
					alert("非「簽退」狀態不可關機!");return;
				}
				
				//若要簽到，必須原本為「訊息通知」狀態
				if('<bean:write name="ap_status_form" property="MBSYSSTATUS"/>' != '3' &&
				   '<bean:write name="ap_status_form" property="MBAPSTATUS"/>' != '1' &&
				   $("#MBSYSSTATUS").val() != "3" && $("#MBAPSTATUS").val() == "1"){
					alert("非「訊息通知」狀態不可簽到!");return;
				}
				
				$("#ac_key").val(str);
				$("#target").val('search');
				//使用者操作軌跡用
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
				
				$("form").submit();
			}
		</script>
	</body>
</html>
