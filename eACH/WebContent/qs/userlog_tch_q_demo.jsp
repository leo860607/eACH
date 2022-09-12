<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>交換所端操作紀錄查詢</title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/newStyle.css">
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
		<div id="container">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#" class="has-next">查詢統計</a>
				<a href="#">操作紀錄查詢_demo</a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/userlog_demo">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SERNO" styleId="SERNO"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table>
							<tr>
								<td class="necessary" style="width: 100px">操作日期</td>
								<td style="width: 450px"><html:text styleId="TXTIME" property="TXTIME" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text></td>
								<td style="width: 100px">操作行代號</td>
								<td>
								<logic:equal name="userData" property="USER_TYPE" value="A">
									<html:select styleId="USER_COMPANY" property="USER_COMPANY" onchange="resetUserId(this)" >
										<html:option value="all">全部</html:option>
										<html:optionsCollection name="userlog_form" property="userCompanyList" label="label" value="value"></html:optionsCollection>
									</html:select>
								</logic:equal>	
								<logic:equal name="userData" property="USER_TYPE" value="B">
									<html:text  readonly="true"  styleClass="lock" styleId="USER_COMPANY" property="USER_COMPANY" ></html:text>
								</logic:equal>	
								</td>
							</tr>
							<tr>
								<td class="necessary">操作者代號</td>
								<td>
									<html:select styleId="USERID" property="USERID" styleClass="validate[required]">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name="userlog_form" property="userIdList" label="label" value="value"></html:optionsCollection>
									</html:select>
								</td>
								<td>功能名稱</td>
								<td>
									<html:select styleId="FUNC_ID" property="FUNC_ID">
									<html:option  value="all">全部</html:option>
<%-- 									<logic:equal name="userData" property="USER_TYPE" value="A"> --%>
<%-- 										<logic:present name="userlog_form" property="funcList"> --%>
<%-- 										<logic:iterate id="upFunc" name="userlog_form" property="funcList"> --%>
<!-- 											作業模組 -->
<%-- 											<option class="upFuncOpt" value="<bean:write name="upFunc" property="FUNC_ID"/>"><bean:write name="upFunc" property="FUNC_NAME"/></option> --%>
<!-- 											功能項目 -->
<%-- 											<logic:present name="upFunc" property="SUB_LIST"> --%>
<%-- 											<logic:iterate id="subFunc" name="upFunc" property="SUB_LIST"> --%>
<%-- 												<option class="subFuncOpt" value="<bean:write name="subFunc" property="FUNC_ID"/>">&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="subFunc" property="FUNC_NAME"/></option> --%>
<%-- 											</logic:iterate> --%>
<%-- 											</logic:present> --%>
<%-- 										</logic:iterate> --%>
<%-- 										</logic:present> --%>
<%-- 									</logic:equal> --%>
<%-- 									<logic:equal name="userData" property="USER_TYPE" value="B"> --%>
										<logic:present name="userlog_form" property="funcList">
										<logic:iterate id="map" name="userlog_form" property="funcList">
											<logic:iterate id="map1" name="map">
												<!-- 作業模組 -->
												<optgroup label="<bean:write name="map1" property="key"/>" >
												<!-- 功能項目 -->
												<logic:present name="map1" property="value">
												<logic:iterate id="map2" name="map1" property="value">
													<option class="subFuncOpt" value="<bean:write name="map2" property="value"/>">&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="map2" property="label"/></option>
												</logic:iterate>
												</logic:present>
												</optgroup>
											</logic:iterate>	
										</logic:iterate>
										</logic:present>
<%-- 									</logic:equal> --%>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData"></table>
				</fieldset>
			</div>
		</div>
		<script type="text/javascript">
			var gridOption;
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#formID").validationEngine({promptPosition: "bottomRight"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="userlog_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	width: 910,
		            	height: 220,
		            	sortable: true,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 8,
		            	//loadonce: true,
						colNames:['檢視明細','序號','交易日期', '所屬單位代號', '操作者代號', '管理功能項目','交易代號','操作項目','操作者IP'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
							{name:'id.SERNO',index:'id.SERNO',align:'center',fixed:true,width: 100},
							{name:'TXTIME',index:'TXTIME',fixed:true,width: 90},
							{name:'id.USER_COMPANY',index:'id.USER_COMPANY',fixed:true,width: 95},
							{name:'id.USERID',index:'id.USERID',fixed:true,width: 95},
							{name:'UP_FUNC_ID',index:'UP_FUNC_ID',fixed:true,width: 150},
							{name:'FUNC_ID',index:'FUNC_ID',fixed:true,width: 95},
							{name:'OPITEM',index:'OPITEM',fixed:true,width: 150},
							{name:'USERIP',index:'USERIP',fixed:true,width: 95}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var ser_no = "",userId = "" ,com_id="";
							var list = data.rows;
							for(var rowCount in list){
								if(window.console){console.log(rowCount);}
								ser_no = list[rowCount].id.SERNO;
								userId = list[rowCount].id.USERID;
								com_id = list[rowCount].id.USER_COMPANY;
								list[rowCount].BTN = '<button type="button" id="edit_' + ser_no + '" onclick="edit_p(this.id , \''+ser_no+'\' , \''+userId+'\' , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
								//if(window.console){console.log("btn>>"+data[rowCount].BTN );}
							}
						},
	 					loadtext: "處理中...",
	 					pgtext: "{0} / {1}"
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					$("#USER_COMPANY").prop("disabled", false);
					blockUI();
					searchData();
					unblockUI();
				}
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=userlog_bo&method=pageSearch&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				$("#resultData").jqGrid(newOption);
			}
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				$("#SERNO").val(id) ;
				if(window.console){console.log("id2>>"+id2);}
				$("#USERID").val(id2) ;
				$("#USER_COMPANY").val(id3) ;
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			function resetUserId(obj){
				$(obj).find(":selected").val();
				var rdata = {component:"userlog_bo", method:"getUserIdListByComId" , com_id:$(obj).find(":selected").val() } ;
				fstop.getServerDataExII(uri, rdata, false, showUsers);
			}
			function showUsers(obj){
				if(window.console){console.log("obj>>"+obj);}
				var select = $("#USERID");
				select.children().remove();
				select.append($("<option></option>").attr("value", "all").text("==全部=="));
				for( var key in obj ){
					select.append($("<option></option>").attr("value", obj[key].value).text(obj[key].label));
				}
			}
		</script>
	</body>
</html>