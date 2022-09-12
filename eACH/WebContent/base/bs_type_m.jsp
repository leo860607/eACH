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
		<title><bean:write name="login_form" property="userData.s_func_name"/></title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<!-- NECESSARY END -->
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}";
			var gridOption,gridOption2;
			function initGrid(){
				tableToGrid("#resultData", gridOption);
				$("#pcode_table").jqGrid(gridOption2);
			}
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
				<html:form action="/business_type" styleId="formID">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="BUSINESS_TYPE_NAME" styleId="BUSINESS_TYPE_NAME" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 18%">業務類別代號</td>
								<td>
									<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID" >
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "business_type_form" property="bsIdKist" label="label" value="value"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
								<logic:equal  name="userData" property="s_auth_type" value="A">	
									<label class="btn" id="add" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
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
					<table id="resultData"></table>
				</fieldset>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>交易類別</legend>
					<table id="pcode_table"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
				
			});
			
			function init(){
				var ac_key = '<bean:write name="business_type_form" property="ac_key"/>';
				initGridOption();
				if(fstop.isNotEmptyString(ac_key) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}	
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
						autowidth:true,
			            height: 250,
			            shrinkToFit: true,
			            //sortable: true,
						sortname: 'BUSINESS_TYPE_ID',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
			            rowNum: 10000,
			            hiddengrid: true,
			            colNames:['檢視明細','查詢交易類別','業務類別代號','業務類別名稱'],
			            colModel: [
			         		{name:'BTN', fixed: true, align: 'center', width:65, sortable: false},
			         		{name:'PCODE_BTN', fixed: true, align: 'center', width:90, sortable: false},
							{name:'BUSINESS_TYPE_ID', fixed: true, width:120},
							{name:'BUSINESS_TYPE_NAME', fixed: true, width:690}
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
								var business_type_id = data[rowCount].BUSINESS_TYPE_ID;
								var business_name = data[rowCount].BUSINESS_TYPE_NAME;
// 								if(window.console){console.log("business_type_id>>"+business_type_id);}
								data[rowCount].BTN = '<button type="button" id="edit_' + data[rowCount].BUSINESS_TYPE_ID + '" onclick="edit_p(this.id , \'' + business_type_id +  '\' , \''+business_name+'\')"><img src="./images/edit.png"/></button>';
								data[rowCount].PCODE_BTN = '<button type="button" id="' + data[rowCount].BUSINESS_TYPE_ID + '" onclick="getPcodeList(this.id)"><img src="./images/edit.png"/></button>';
							}
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#resultData") );
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadtext: "處理中..."
				};
				
				gridOption2 = {
						datatype: "local",
						autowidth:true,
			            height: 90,
			            shrinkToFit: true,
			            //sortable: true,
						sortname: 'EACH_TXN_ID',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
			            rowNum: 10000,
			            //hiddengrid: true,
			            colNames:['交易類別代號','交易類別名稱'],
			            colModel: [
							{name:'EACH_TXN_ID', fixed: true, width:120},
							{name:'EACH_TXN_NAME', fixed: true, width:760}
						],
						beforeSelectRow: function(rowid, e) {
							return false;
						},
						gridComplete: function(){
							$("#pcode_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#pcode_table .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#pcode_table") );
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							if(window.console){console.log("index>>"+index);}
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadtext: "處理中..."
				};
				
				initGrid();
				
				alterMsg();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="business_type_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function onPut(str){
				searchData(str);
			}	
			
			function searchData(str){
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');//e
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=business_type_bo&method=search_toJson&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'BUSINESS_TYPE_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'BUSINESS_TYPE_ID' , 'ASC' , true);
				}
				$("#resultData").jqGrid(newOption);
			}
			
			function add_p(str){
				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('business_type_add');
				$("form").submit();
			}	
			
			function edit_p(str , id , name){
				str = str.split("_")[0];
				var tmp = {};
				tmp['BUSINESS_TYPE_ID'] = id;
				tmp['BUSINESS_TYPE_NAME'] = name;
				$("#ac_key").val(str);
				$("#target").val('business_type_edit');
				$("#edit_params").val(JSON.stringify(tmp));
				$("form").submit();
			}	
			
			
			function getPcodeList(bsId){
				blockUI();
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
// 				var qStr = "component=each_txn_code_bo&method=search_toJson&bsTypeId="+bsId+"&serchStrs="+$("#serchStrs").val();
				var qStr = "component=each_txn_code_bo&method=search_toJson&bsTypeId="+bsId+'&serchStrs={"BUSINESS_TYPE_ID":'+'"'+bsId+'"'+',"action":"/eACH/business_type.do"}';
// 				var qStr = "component=each_txn_code_bo&method=search_toJson&bsTypeId="+bsId;
				$("#pcode_table").jqGrid('GridUnload');
				var newOption = gridOption2;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
				$("#pcode_table").jqGrid(newOption);
				unblockUI();
			}
		</script>
	</body>
</html>
