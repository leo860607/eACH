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
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
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
				<html:form styleId="formID" action="/each_user">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden styleId="USER_COMPANY" property="USER_COMPANY"  />
					<html:hidden styleId="USER_TYPE" property="USER_TYPE"  />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<logic:equal name="each_user_form" property="USER_TYPE" value="A">
						<html:hidden styleId="USER_ID" property="USER_ID"  />
					</logic:equal>
					<fieldset>
					<logic:equal name="each_user_form" property="USER_TYPE" value="B">
						<legend>查詢條件</legend>
					</logic:equal>
						<table id = "search_tab"><!--  e -->
						<logic:equal name="each_user_form" property="USER_TYPE" value="B">
							<tr>
								<td class="header" style="width: 100px">用戶代號</td>
									<td>
										<html:text styleId="USER_ID" property="USER_ID"  ></html:text>
									</td>
							</tr>
						</logic:equal>	
<!-- 							<tr> -->
<!-- 								<td class="header" style="width: 100px">用戶類型</td> -->
<!-- 									<td> -->
<!-- 									</td> -->
<!-- 							</tr> -->
							<tr>
								<td colspan="2" style="padding-top: 5px">
								<logic:equal name="each_user_form" property="USER_TYPE" value="B">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
								</logic:equal>
<!-- 									<label class="btn" id="clean" onclick ="cleanForm(this)"><img src="./images/clean.png"/>&nbsp;清除查詢條件</label> -->
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
				var msg = '<bean:write name="each_user_form" property="msg"/>';
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
						sortname: 'USER_ID',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','用戶代號','所屬單位代號','用戶類型','狀態','用戶說明','是否使用Ikey','所屬群組','閒置逾時效期 (天)','限制IP登入','用戶閒置timeout時間(分)','上次登入日期','上次登入IP'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
// 							{name:'id.USER_ID', fixed: true, width:80},
// 							{name:'id.USER_COMPANY', fixed: true, width:200},
							{name:'USER_ID', fixed: true, align: 'center', width:80},
							{name:'COM_NAME', fixed: true, align: 'center', width:150},
							{name:'USER_TYPE', fixed: true, align: 'center', width:80,formatter:e_USER_Fmatter},
							{name:'USER_STATUS', fixed: true, align: 'center', width:80,formatter:e_USER_Fmatter},
							{name:'USER_DESC', fixed: true, align: 'center', width:100},
							{name:'USE_IKEY', fixed: true, align: 'center', width:120 ,formatter:e_USER_Fmatter},
							{name:'ROLE_ID', fixed: true, align: 'center', width:150},
							{name:'NOLOGIN_EXPIRE_DAY', fixed: true, align: 'center', width:150},
							{name:'IP', fixed: true, align: 'center', width:100},
							{name:'IDLE_TIMEOUT', fixed: true, align: 'center', width:180},
							{name:'LAST_LOGIN_DATE', fixed: true, align: 'center', width:100},
							{name:'LAST_LOGIN_IP', fixed: true, align: 'center', width:100},
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
							var user_id ="" ;user_comId="" ; user_type="" ;
							for(var rowCount in data){
								user_id = data[rowCount].USER_ID;
								user_comId = data[rowCount].USER_COMPANY;
								user_type = data[rowCount].USER_TYPE;
// 								user_id = data[rowCount].id.USER_ID;
// 								user_comId = data[rowCount].id.USER_COMPANY;
// 								user_type = data[rowCount].USER_TYPE;
								data[rowCount].BTN = '<button type="button" id="edit_' + user_id + '" onclick="edit_p(this.id , \''+user_id+'\' , \''+user_comId+'\', \''+user_comId+'\')"><img src="./images/edit.png"/></button>';
							if(window.console){console.log("BTN>>>"+data[rowCount].BTN);}
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
// 				if(window.console)console.log("ac_key"+$("#ac_key").val());
// 				var user_type = $("#USER_TYPE").val();
// 				if($("#ac_key").val() == 'save' || $("#ac_key").val() == 'update' ||$("#ac_key").val() == 'back' ){
// 					var str = $("#serchStrs").val();
// 					if(user_type=="A"){
// 						$("#USER_ID").val('') ;
// 					}else{
// 						$("#USER_ID").val(str[USER_ID]) ;
// 					}
// 				}
			}
			
			function onPut(str){
				if(str == "search"){
// 					if(!jQuery('#formID').validationEngine('validate')){
// 						return false;
// 					}
					$("#USER_COMPANY").val("");
					if(window.console){console.log("USER_COMPANY>>"+$("#USER_COMPANY").val());}
				}else{
					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var user_type = $("#USER_TYPE").val();
				if(window.console)console.log("user_type>>"+user_type);
// 				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');//e
				
				
				var qStr ="";
				if(user_type=="A"){
					getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?USER_TYPE=A&");
// 					qStr = "component=each_user_bo&method=search_toJson&"+$("#formID").serialize();
				}else{
					$("#serchStrs").val('');
					getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?USER_TYPE=B&");
// 					qStr = "component=each_user_bo&method=search_toJson&"+$("#formID").serialize()+"";
				}
				qStr = "component=each_user_bo&method=search_toJson&"+$("#formID").serialize()+"";
				if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'USER_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'USER_ID' , 'ASC' , true);
				}
				if(window.console)console.log("A.url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}	
			
				
				
			function add_p(str){
// 				cleanFormNE(document.getElementById(str));
				$("#formID").validationEngine('detach');
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				var tmp={};
				tmp['USER_ID'] = id;
				tmp['USER_COMPANY'] = id2;
				tmp['USER_TYPE'] = id3;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
// 			function edit_p(str , id , id2 ,id3){
// 				$("#formID").validationEngine('detach');
// 				getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?USER_TYPE=B&");
// 				$("#USER_ID").val(id) ;
// 				$("#USER_COMPANY").val(id2) ;
// 				$("#USER_TYPE").val(id3) ;
// 				$("#ac_key").val('edit');
// 				$("#target").val('edit_p');
// 				$("form").submit();
// 			}	
			
			function e_USER_Fmatter (cellvalue, options, rowObject)
			{
				
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
					rtnStr = "啟用";
					return rtnStr;
				}else {
					rtnStr = "未啟用";
					return rtnStr;
				}
				
			   // do something here
			}
			
			
		</script>
	</body>
</html>
