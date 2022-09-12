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
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
	</head>
	<body>
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
				<html:form styleId="formID" action="/role_list">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden styleId="ROLE_ID" property="ROLE_ID"  />
					<html:hidden styleId="ROLE_TYPE" property="ROLE_TYPE"  />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab" >
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<logic:equal  name="userData" property="s_auth_type" value="A">
									<label class="btn" id="add" onclick ="add_p(this.id);"><img src="./images/add.png"/>&nbsp;新增</label>
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
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
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
				$("#formID").validationEngine({promptPosition: "bottomLeft"});
// 				onPut('search');
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="role_list_form" property="msg"/>';
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
						sortname: 'ROLE_ID',
// 						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','群組代號','群組名稱','群組說明','是否使用iKEY','角色類型'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
							{name:'ROLE_ID', fixed: true, align: 'center', width:120},
							{name:'ROLE_NAME', fixed: true, align: 'center', width:100},
							{name:'DESC', fixed: true, align: 'center', width:100},
							{name:'USE_IKEY', fixed: true, align: 'center', width:100 ,formatter:e_USER_Fmatter},
							{name:'ROLE_TYPE', fixed: true, align: 'center', width:100,formatter:e_USER_Fmatter},
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
							var role_id =""; role_type = "";
							for(var rowCount in data){
								role_id = data[rowCount].ROLE_ID;
								role_type = data[rowCount].ROLE_TYPE;
								data[rowCount].BTN = '<button type="button" id="edit_' + role_id + '" onclick="edit_p(this.id , \''+role_id+'\' , \''+role_type+'\')"><img src="./images/edit.png"/></button>';
// 							if(window.console){console.log("BTN>>>"+data[rowCount].BTN);}
							}
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							
// 							if(window.console){console.log("index>>>"+index);}
// 							if(window.console){console.log("sortOrder>>>"+sortOrder);}
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#resultData") );
						},
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			function onPut(str){
				if(str == "search"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
				}else{
					$("#formID").validationEngine('detach');
				}
				var role_type = $("#ROLE_TYPE").val();
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				if(role_type=='A'){
					getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?ROLE_TYPE=A&");
				}else{
					getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?ROLE_TYPE=B&");
				}
				var qStr = "component=role_list_bo&method=search_toJson&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'ROLE_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'ROLE_ID' , 'ASC' , true);
				}
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
				tmp['ROLE_ID'] = id;
				tmp['ROLE_TYPE'] = id2;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
			
			function e_USER_Fmatter (cellvalue, options, rowObject)
			{
				
// 				if(window.console){console.log("cellvalue>>"+cellvalue);}
// 				if(window.console){console.log("options>>"+options.va);}
// 				if(window.console){console.log("cellvalue>>"+rowObject);}
				var rtnStr = "";
				
				if(cellvalue == "A"){
					rtnStr = "票交所";
					return rtnStr;
				}
				if(cellvalue == "B"){
					rtnStr = "銀行";
					return rtnStr;
				}
				if(cellvalue == "C"){
					rtnStr = "發動者";
					return rtnStr;
				}
				if(cellvalue=="Y"){
					rtnStr = "使用";
					return rtnStr;
				}else {
					rtnStr = "不使用";
					return rtnStr;
				}
				
			   // do something here
			}
			
		</script>
	</body>
</html>
