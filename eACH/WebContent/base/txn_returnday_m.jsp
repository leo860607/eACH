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
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<!-- NECESSARY END -->
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				alterMsg();
				initGrid();
				initSelect();
				unblockUI();
	        });
			
			
			function searchData(str){
				if(str == "search"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=txn_returnday_bo&method=search_toJson&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'TXN_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'TXN_ID' , 'ASC' , true);
				}
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}
			
			function onPut(id){
				searchData(id);
			}
			
			function add_p(id){
				cleanFormNE(document.getElementById(id));
				blockUI();
				$("#ac_key").val(id);
				$("#target").val('search');
				$("form").submit();
			}
			function edit_p(id , id1  , id2){
				var splice = id.split("_");
				var tmp ={};
				tmp['TXN_ID'] = id1;
				tmp['ACTIVE_DATE'] = id2;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val(splice[0]);
				$("#target").val('search');
				$("form").submit();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="txn_returnday_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			function initGrid(){
				gridOption = {
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
						sortname: 'TXN_ID',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','交易代號','退回天數','啟用日期'],
		            	colModel: [
	   	            	    {name:'BTN', sortable: false, fixed: true, align: 'center', width:60},
	   						{name:'id.TXN_ID', sortable: true, fixed: true, width:200},
	   						{name:'RETURN_DAY', sortable: true,sorttype: 'int', fixed: true, width:200},
	   						{name:'id.ACTIVE_DATE', sortable: true, fixed: true, width:200}
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
							var txn_id =""; active_date = "";
							for(var rowCount in data){
								txn_id = data[rowCount].id.TXN_ID;
								active_date = data[rowCount].id.ACTIVE_DATE;
								data[rowCount].BTN = '<button type="button" id="edit_' + txn_id + '" onclick="edit_p(this.id , \''+txn_id+'\' , \''+active_date+'\')"><img src="./images/edit.png"/></button>';
								//if(window.console)console.log("edit3>>"+data[rowCount].BTN);
							}
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
				
				$("#resultData").jqGrid(gridOption);
			}
			
		</script>
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
<%-- 				<html:form action="/txn_returnday"> --%>
				<html:form styleId="formID" action="/txn_returnday">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target"/>
					<html:hidden property="ACTIVE_DATE" styleId="ACTIVE_DATE" value=""/>
					<html:hidden property="STXN_ID" styleId="STXN_ID" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 15%">交易代號</td>
								<td>
									<html:select styleId="TXN_ID" property="TXN_ID">
										<logic:present name="txn_returnday_form" property="txnIdList">
											<html:option value="">全部</html:option> 
											<html:optionsCollection name="txn_returnday_form" property="txnIdList" label="label" value="value"></html:optionsCollection> 
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<!-- 20141127 HUANGPU Kevin說歷史紀錄不是這個意思 
									<label class="btn" id="history" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;歷史紀錄</label>
									-->
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="new" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData">
					</table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
	</body>
</html>