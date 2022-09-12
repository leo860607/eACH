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
				<html:form styleId="formID" action="/role_list_bank">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden styleId="ROLE_ID" property="ROLE_ID"  />
					<html:hidden styleId="ROLE_TYPE" property="ROLE_TYPE"  />
					<html:hidden styleId="serchStrs" property="serchStrs"  />
					
					<fieldset>
						<table id="search_tab">
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
				onPut('search');
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
						sorttype: 'text',
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
							var role_id ="";
							for(var rowCount in data){
								role_id = data[rowCount].ROLE_ID;
								data[rowCount].BTN = '<button type="button" id="edit_' + role_id + '" onclick="edit_p(this.id , \''+role_id+'\')"><img src="./images/edit.png"/></button>';
							if(window.console){console.log("BTN>>>"+data[rowCount].BTN);}
							}
						},
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if($("#ac_key").val() == 'save' || $("#ac_key").val() == 'update' ||$("#ac_key").val() == 'back' ){
					onPut('');
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
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs' );
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=role_list_bo&method=search_toJson4Bank&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				
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
				$("#ROLE_ID").val(id) ;
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
			
			function e_USER_Fmatter (cellvalue, options, rowObject)
			{
				
				if(window.console){console.log("cellvalue>>"+cellvalue);}
				if(window.console){console.log("options>>"+options.va);}
				if(window.console){console.log("cellvalue>>"+rowObject);}
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
