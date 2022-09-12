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
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/jquery.cookie.js"></script>
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
				<html:form styleId="formID" action="/agent_send_profile">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab" style="width: 100%">
							<tr>
								<td class="header" style="width: 15%">代理業者統一編號</td>
								<td style="width: 15%">
								<html:select styleId="COMPANY_ID" property="COMPANY_ID">
									<html:option value="">全部</html:option>
									<html:optionsCollection name="agent_send_profile_form" property="companyIdList" label="label" value="value"/>
								</html:select>
<%-- 									<html:text styleId="COMPANY_ID" property="COMPANY_ID" size="12" maxlength="10" styleClass="validate[maxSize[10],notChinese] text-input"></html:text> --%>
								</td>
								
								<td class="header" >發動者統一編號</td>
								<td>
									<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="12" maxlength="10" styleClass="validate[maxSize[10],notChinese] text-input" ></html:text>
								</td>
							</tr>
							<tr>
								<td class="header">交易代號</td>
								<td>
									<html:select styleId="TXN_ID" property="TXN_ID">
										<html:option value="">全部</html:option>
										<html:optionsCollection name="agent_send_profile_form" property="txnIdList" label="label" value="value"/>
									</html:select>
								</td>
								<td class="header" >發動者名稱</td>
								<td>
									<html:text styleId="SND_COMPANY_NAME" property="SND_COMPANY_NAME" size="40" maxlength="65"></html:text>
								</td>
<!-- 								<td class="header">啟用日期</td> -->
<!-- 								<td> -->
<%-- 									<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text> --%>
<!-- 								</td> -->
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
<%-- 									<logic:equal name="login_form" property="userData.USER_TYPE" value="A"> --%>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" onclick="add(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
									<label class="btn" id="export" onclick ="exportDoc(this.id);"><img src="./images/export.png"/>&nbsp;匯出Excel</label>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="bs_table"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
		
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				
				
				initGridOption();
				initGrid();
				var acKey = $("#ac_key").val();
				if(fstop.isNotEmptyString(acKey)){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="agent_send_profile_form" property="msg"/>';
				var result = '<bean:write name="agent_send_profile_form" property="result"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 240,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
						colNames:[
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
						          '檢視明細',
								</logic:equal>
						          '代理業者統編','代理業者名稱','發動者統編','發動者名稱','交易代號','啟用日期', '停用日期' ],
						colModel: [
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
								</logic:equal>
									{name:'COMPANY_ID',index:'COMPANY_ID',fixed:true,width: 120},
									{name:'COMPANY_NAME',index:'COMPANY_NAME',fixed:true,width: 120},
									{name:'SND_COMPANY_ID',index:'SND_COMPANY_ID',fixed:true,width: 120},
									{name:'SND_COMPANY_NAME',index:'SND_COMPANY_NAME',fixed:true,width: 120},
									{name:'TXN_ID',index:'TXN_ID',align:'center',fixed:true,width: 100},
									{name:'ACTIVE_DATE',index:'ACTIVE_DATE',fixed:true,width: 90},
									{name:'STOP_DATE',index:'STOP_DATE',fixed:true,width: 90},
									
						],
		            	gridComplete:function (){
			            	$("#bs_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#bs_table .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
							var txn_id = "",snd_company_id = "", company_id = "";
							for(var rowCount in data){
								txn_id = data[rowCount].TXN_ID;
								snd_company_id = data[rowCount].SND_COMPANY_ID;
								company_id = data[rowCount].COMPANY_ID;
								data[rowCount].BTN = '<button type="button" id="edit_' + txn_id + '" onclick="edit_p(this.id , \''+company_id+'\', \'' +snd_company_id+ '\', \'' +txn_id+ '\')"><img src="./images/edit.png"/></button>';
							}
							</logic:equal>
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#bs_table") );
						},
	 					loadtext: "處理中..."
				};
			}
			
			function initGrid(){
				$("#bs_table").jqGrid(gridOption);
			}
			
			function onPut(str){
				var tmpSearchStrs = $("#serchStrs").val(); 
				if(str == "search" || str == "back" ){
					if($("#formID").validationEngine("validate")){
						blockUI();
						getSearch_condition('search_tab', 'input, select', 'serchStrs');
						searchData(str);
						unblockUI();
					}
				}else{
					if(str == "new"){
						cleanFormNE(document.getElementById(str));
					}
					$("#formID").validationEngine('detach');
					blockUI();
					$("#ac_key").val(str);
					$("#target").val('add_p');
					$("#serchStrs").val(tmpSearchStrs); 
					$("form").submit();
				}
			}
			
			function searchData(str){
				$("#bs_table").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=agent_send_profile_bo&method=search_toJson&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(window.console){console.log("qStr>>"+qStr);}
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'COMPANY_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'COMPANY_ID' , 'ASC' , true);
				}
				$("#bs_table").jqGrid(newOption);
			}
			
			function add(str){
				blockUI();
				cleanFormNE($("#search_tab"));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}
			
			function edit_p(str, company_id ,snd_company_id, txn_id ){
				blockUI();
				var tmp ={};
				tmp['SND_COMPANY_ID'] = snd_company_id;
				tmp['TXN_ID'] = txn_id;
				tmp['COMPANY_ID'] = company_id;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			//取得user查詢條件
			function getSearches(){
				var searches = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					searches[field.name] = field.value;
				});
				$("#serchStrs").val(JSON.stringify(searches));
				//if(window.console){console.log($("#serchStrs").val());}
			}
			
			function exportDoc(str){
				blockUIForDownload();
				$("#ac_key").val(str);
				$("#target").val('search');
				$("#formID").submit();
			}
			
			//==================此區塊為使用struts 輸出檔案會用到的API==================
			var fileDownloadCheckTimer;
			function blockUIForDownload() {
			    var token = new Date().getTime(); //use the current timestamp as the token value
			    $('#dow_token').val(token);
			    blockUI();
			    fileDownloadCheckTimer = window.setInterval(function () {
			      var cookieValue = $.cookie('fileDownloadToken');
			      if (cookieValue == token)
			       finishDownload();
			    }, 1000);
			}
			function finishDownload(){
				window.clearInterval(fileDownloadCheckTimer);
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>