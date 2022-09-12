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
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
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
			<div id="opPanel">
				<html:form styleId="formID" action="/typh_op">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<fieldset>
						<table id="search_tab">
							<tr>
								<td class="necessary header" style="width: 16%">請指定颱風天區間</td>
								<td style="width: 30%">
									<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[END_DATE]] text-input datepicker"></html:text>~
									<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width: 16%"></td>
								<td></td>
							</tr>
							<logic:equal name="login_form" property="userData.s_auth_type" value="A">
								<tr>
									<td colspan="2" style="padding-top: 5px">
										<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/start.png"/>&nbsp;執行</label>
									</td>
								</tr>
							</logic:equal>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>執行結果</legend>
				</fieldset>
			</div>
			<br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				var result = '<bean:write name="typh_op_form" property="result"/>';
				var type = result.split("-")[0];
				result = result.split("-")[1];
				var msg = '<bean:write name="typh_op_form" property="msg"/>';
				msg = msg.replace(/&quot;/g,'"');
				if(fstop.isNotEmptyString(msg)){
					if(result == "FALSE"){
						alert(msg); return;
					}else if(result == "TRUE"){
						//precheck
						if(type == "1"){
							showPrecheckResult(JSON.parse(msg));
						//execute
						}else if(type == "2"){
							showUpdateResult(JSON.parse(msg));
						}
					}
				}
			}
			
			function showPrecheckResult(precheckResult){
				if(precheckResult != null){
					var result = "";
					for(var i = 0; i < precheckResult.length; i++){
						if(precheckResult[i].NAME == "ONBLOCKTAB"){
							result += "圈存扣款檔-颱風天待處理筆數：" + precheckResult[i].NUM + "\n";
						}else if(precheckResult[i].NAME == "ONPENDINGTAB"){
							result += "Pending檔-颱風天待處理筆數：" + precheckResult[i].NUM + "\n";
						}else if(precheckResult[i].NAME == "ONCLEARINGTAB"){
// 							result += "連線結算檔-颱風天待處理筆數：" + precheckResult[i].NUM + "\n";
							
							//680 UAT-20161110-01 新增沖正筆數= (空值：筆)
							var zero = '';
							if(precheckResult[i].ZERO != "0"){
								zero = ' (空值：' + precheckResult[i].ZERO + '筆)';
							}
							result += "連線結算檔-颱風天待處理筆數：" + precheckResult[i].NUM + zero + "\n";
						}
					}
					result += '確定異動以上所有資料?';
					if(confirm(result)){
						onPut("update");
					}
				}
			}
			
			function showUpdateResult(updateResult){
				//Clear content
				$("#rsPanel > fieldset > legend").nextAll().remove();
				
				if(updateResult != null){
					var data, detail;
					for(var date in updateResult){
						detail = updateResult[date];
						data = '<div id="dataInputTable" style="width: 95%;margin:0 auto"><table>';
						data += '<tr><td rowspan="6" width="1%">'+date+'</td><td>圈存扣款檔-需處理筆數 '+detail.ONBLOCKTAB.B+'</td></tr>';
						data += '<tr><td>圈存扣款檔-處理後筆數 '+detail.ONBLOCKTAB.A+'</td></tr>';
						data += '<tr><td>Pending檔-需處理筆數 '+detail.ONPENDINGTAB.B+'</td></tr>';
						data += '<tr><td>Pending檔-處理後筆數 '+detail.ONPENDINGTAB.A+'</td></tr>';
						data += '<tr><td>連線結算檔-需處理筆數 '+detail.ONCLEARINGTAB.B+'</td></tr>';
						data += '<tr><td>連線結算檔-處理後筆數 '+detail.ONCLEARINGTAB.A+'</td></tr>';
						data += '</table></div>';
						$("#rsPanel > fieldset").append(data);
					}
					$("#rsPanel > fieldset").append("<br/>");
				}
			}
			
			function onPut(str){
				$("#ac_key").val(str);
				if($("#formID").validationEngine("validate")){
					blockUI();
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					$("#formID").submit();
				}
			}
		</script>
	</body>
</html>