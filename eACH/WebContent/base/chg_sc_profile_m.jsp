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
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.cookie.js"></script>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		</script>
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
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/chg_sc_profile" method="POST" enctype="multipart/form-data">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="error_list" styleId="error_list" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 18%">代收項目代號</td>
								<td style="width: 30%">
									<html:text styleId="SD_ITEM_NO" property="SD_ITEM_NO" size="5" maxlength="3"></html:text>
								</td>
								<td class="header" style="width: 20%"><span>入帳行所屬總行代號</span></td>
								<td>
									<html:select styleId="BGBK_ID" property="BGBK_ID">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name = "chg_sc_profile_form" property="bgbkIdList" label="label" value="value"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="header">業者統一編號</td>
								<td><html:text styleId="COMPANY_ID" property="COMPANY_ID" size="15" maxlength="10"></html:text></td>
								<td class="header">交易代號</td>
								<td>
									<html:select styleId="TXN_ID" property="TXN_ID">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name = "chg_sc_profile_form" property="txnIdList" label="label" value="value"/>
									</html:select>
								</td>
							</tr>
							<tr>
							    <td class="header">業者名稱</td>							     
		                        <td colspan="3"><html:text styleId="COMPANY_NAME" property="COMPANY_NAME" maxlength="65" style="width: 100%"></html:text></td>
							</tr>
							<tr>
							    <td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
									<label class="btn" id="export" onclick ="onPut(this.id);"><img src="./images/export.png"/>&nbsp;匯出Excel</label>
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
				initGrid();
				initSelect();
				$("#formID").validationEngine({binded:false,promptPosition:"bottomLeft"});
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="chg_sc_profile_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGrid(){
				gridOption = {
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
						//sortname: '',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','代收項目代號','業者統一編號','業者簡稱','業者名稱','交易代號','入帳行代號','入帳行名稱','入帳行帳號','格式代號','可收逾期繳費天數','啟用日期','停用日期','備註'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:60, sortable: false},
		            	    {name:'SD_ITEM_NO', fixed: true, width:100},
		            	    {name:'COMPANY_ID', fixed: true, width:100},
		            	    {name:'COMPANY_ABBR_NAME', fixed: true, width:80},
		            	    {name:'COMPANY_NAME', fixed: true, width:200},
		            	    {name:'TXN_ID', fixed: true, width:80},
		            	    {name:'INBANKID', fixed: true, width:80},
		            	    {name:'INBANKNAME', fixed: true, width:150},
		            	    {name:'INBANKACCTNO', fixed: true, width:130},
		            	    {name:'LAYOUTID', fixed: true, width:70},
		            	    {name:'DEALY_CHARGE_DAY', fixed: true, width:120},
		            	    {name:'START_DATE', fixed: true, width:80},
		            	    {name:'STOP_DATE', fixed: true, width:80},
		            	    {name:'NOTE', fixed: true, width:200}
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeProcessing: function(data, status, xhr){
							for(var rowCount in data){
								rowCount = parseInt(rowCount);
								data[rowCount].BTN ='<button type="button" id="edit_'+rowCount+'" value="edit" onclick="edit_p(this.value,\''+data[rowCount].SD_ITEM_NO+'\')" ><img src="./images/edit.png"/></button>';
							}
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadComplete: function(data){
							//查詢結果無資料
							noDataEvent(data ,$("#resultData") );
						},
						loadtext: "處理中..."
				};
				
				$("#resultData").jqGrid(gridOption);
			}
			
			function add_p(str){
				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}
			
			function initSelect(){
				//if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "export"){
						blockUIForDownload();
						$("#ac_key").val(str);
						$("#target").val('search');
						$("#formID").submit();
					}else{
						blockUI();
						getSearch_condition('search_tab', 'input, select', 'serchStrs');
						$("#resultData").jqGrid('GridUnload');
						var newOption = gridOption;
						var qStr = "component=chg_sc_profile_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+"&"+encodeURI($("#formID").find("[name!='serchStrs']").serialize());
						//if(window.console)console.log($("#formID").serialize());
						newOption.url = "/eACH/baseInfo?"+qStr;
						newOption.datatype = "json";
						newOption.mtype = 'POST';
						//if(window.console)console.log("url>>"+newOption.url);
						if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
							resetSortname(newOption , 'SD_ITEM_NO' , 'ASC' , false);
						}else{
							resetSortname(newOption , 'SD_ITEM_NO' , 'ASC' , true);
						}
						$("#resultData").jqGrid(newOption);
					}
				}
			}
			
			function edit_p(str, sdItemNo){
				var tmp={};
				tmp['SD_ITEM_NO'] = sdItemNo;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val(str);
				$("#target").val('edit_p');
				$("form").submit();
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
