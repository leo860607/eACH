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
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
	</head>
	<body>
	<div id="wrapper">
	<jsp:include page="/header.jsp"></jsp:include>
	<div id="block_body">
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/bulletin">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="SNO" styleId="SNO" />
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="necessary" >日期區間</td>
								<td class="header" align="left" >
									<html:text styleId="SDATE" property="SDATE" size="8" maxlength="8" styleClass="validate[ required,maxSize[8],minSize[8],twDate,twPast[#EDATE]] text-input datepicker"></html:text>
									~<html:text styleId="EDATE" property="EDATE" size="8" maxlength="8" styleClass="validate[required ,maxSize[8],minSize[8],twDate , ] text-input datepicker"></html:text>
								</td>
								<td class="header" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
								<td class="header" align="left" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
<!-- 								<td  style="padding-top: 5px" > -->
<!-- 								</td> -->
								<td style="padding-top: 5px" colspan="5">
									<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢儲存公告</label>
									</logic:equal>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="add" onclick ="add_p();"><img src="./images/add.png"/>&nbsp;新增公告</label>
									</logic:equal>
									<label class="btn" id="rec" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢公告紀錄</label>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="cancel_status" onclick="cancel_status()"><img src="./images/delete.png"/>&nbsp;取消公告</label>
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
		</div>
		<!-- 訊息內容對話框 -->
			<div id="msg_dialog" title="公告內容" style="font-size: 16px ;border: 1px solid black;"  >
<%-- 				<html:textarea styleClass="text ui-widget-content ui-corner-all" styleId="MESSAGE" property="MESSAGE" style="width: 100%" rows="5"/> --%>
			<textarea id = "MESSAGE" style="width: 100%" rows="10" class="text ui-widget-content ui-corner-all" ></textarea>
			</div>
		</div>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var gridOption;
			var gridOption_Rec;
			var action = $("#formID").attr('action');
			var userType = '<bean:write name="login_form" property="userData.USER_TYPE"/>';
			if(window.console){console.log("userType>>"+userType);}
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				$("#msg_dialog").dialog({
					autoOpen: false,
					height: 300,
					width: 700,
					modal: false,
					close: function() {
// 						$("#MESSAGE").html("");
					},
					open: function() {
					}
				});
				
				alterMsg();
				setDatePickerII($("#SDATE") , '<bean:write name="bulletin_form" property="SDATE"/>');
				setDatePickerII($("#EDATE") , '<bean:write name="bulletin_form" property="EDATE"/>');
				<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="A">
				initGrid_Rec();
// 				onPut('rec');
				initSelect_Rec();
				</logic:notEqual>	
				<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
				initGrid();
// 				onPut('search');
				initSelect();
				</logic:equal>	
				
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="bulletin_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGrid(){
				gridOption = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
		            	sortable: true,
						sortname: 'SNO',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','重新發佈','公告儲存時間','公告發佈時間','公告內容'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
							{name:'SEND_STATUS', fixed: true , align: 'center', width:80 , },
							{name:'SAVE_DATE', fixed: true, width:160},
							{name:'SEND_DATE', fixed: true, width:160},
							{name:'CHCON', fixed: true, width:400},
						], 
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						ondblClickRow: function(rowid,iRow,iCol, e){
							var colModel = $(this).jqGrid("getGridParam", "colModel");
    						var colName = colModel[iCol].name;
							var colVal = $(this).jqGrid("getCell", rowid, iCol);
							if(colName == 'CHCON'){
								$("#MESSAGE").html(colVal);
								$("#msg_dialog").dialog('open');
							}
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeProcessing: function(data, status, xhr){
							var seq_id ="";
							for(var rowCount in data){
								seq_id = data[rowCount].SNO;
								data[rowCount].BTN = '<button type="button" id="edit_' + seq_id + '" onclick="edit_p(this.id , \''+seq_id+'\')"><img src="./images/edit.png"/></button>';
								data[rowCount].SEND_STATUS = '<button type="button" id="send_status" onclick="send_status(\''+seq_id+'\')"><img src="./images/ic_notify.png"/></button>';
							}
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data);
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
						    if(window.console){console.log("sortname="+$("#sortname").val());}
						    if(window.console){console.log("sortorder="+$("#sortorder").val());}
						    $("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
						    get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
// 						   e
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			//公告紀錄的GRID
			function initGrid_Rec(){
				gridOption_Rec = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
		            	sortable: true,
						sortname: 'SEND_DATE',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="A">
		            	colNames:['公告發佈時間','公告內容' ],
		            	colModel: [
							{name:'SEND_DATE', fixed: true, width:160},
							{name:'CHCON', fixed: true, width:600},
						], 
						</logic:notEqual>
		            	<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
		            	colNames:['公告發佈時間','公告內容' , '發佈公告的用戶代號' ,'發佈公告的操作者IP'],
		            	colModel: [
							{name:'SEND_DATE', fixed: true, width:160},
							{name:'CHCON', fixed: true, width:400},
							{name:'USERID', fixed: true, width:160},
							{name:'USERIP', fixed: true, width:160},
						], 
						</logic:equal>
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						ondblClickRow: function(rowid,iRow,iCol, e){
							var colModel = $(this).jqGrid("getGridParam", "colModel");
    						var colName = colModel[iCol].name;
							var colVal = $(this).jqGrid("getCell", rowid, iCol);
							if(colName == 'CHCON'){
								$("#MESSAGE").html(colVal);
								$("#msg_dialog").dialog('open');
							}
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeProcessing: function(data, status, xhr){
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data);
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
						    $("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
						},
// 						   e
						loadtext: "處理中..."
				};
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
				$("#resultData").jqGrid(gridOption_Rec);
				</logic:equal>
			}
			
			//發佈
			function send_status(id){
				var sno = id;
				var rdata = {component:"bulletin_bo", method:"send_status" , SNO: sno  , action:action};
				if(confirm("確定要發佈公告?")){
					var vResult =fstop.getServerDataExII(uri, rdata, false  );
					if(fstop.isNotEmpty(vResult)){
						alert(vResult.msg);
						getPublicData();
						onPut('search');
//	 					onPut('rec');
					}else{
						alert("系統異常");
					}
				}
				
			}
			//發佈
			function cancel_status(id){
				var rdata = {component:"bulletin_bo", method:"cancel_status" , action:action};
				if(confirm("確定要取消公告?")){
					var vResult =fstop.getServerDataExII(uri, rdata, false  );
					if(fstop.isNotEmpty(vResult)){
						alert(vResult.msg);
						getPublicData();
						onPut('search');
//	 					onPut('rec');
					}else{
						alert("系統異常");
					}
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
			function initSelect_Rec(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('rec');
				}
			}
			
			function onPut(str){
				
				
				if(str == "search" || str=="rec"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');//e
				}else{
					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=bulletin_bo&method=search_toJson&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				if(str=="rec"){
					if(userType == 'A'){
						initGrid_Rec();
					}
					newOption = gridOption_Rec;
					qStr = "component=bulletin_bo&method=search_Rec&"+$("#formID").serialize();
					resetSortname(newOption , 'SEND_DATE' , 'DESC',true);
				}else if(str=="back"){
					resetSortname(newOption , 'SEND_DATE' , 'DESC',false);
				}else{
					resetSortname(newOption , 'SEND_DATE' , 'DESC',true);
				}
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}	
			
			function add_p(str){
				$("#formID").validationEngine('detach');
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				var tmp={};
				tmp['SNO'] = id;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
			
			
		</script>
	</body>
</html>
