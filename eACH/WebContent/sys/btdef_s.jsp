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
				<html:form styleId="formID" action="/each_batch_def">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="BATCH_PROC_SEQ" styleId="BATCH_PROC_SEQ" />
					
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
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
		var i = 0;
		if(window.console){console.log("window.width>>"+$(window).width()+"window.height()"+$(window).height());}
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var gridOption;
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				initGrid();
				initSelect();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});

			}
			
			function alterMsg(){
				var msg = '<bean:write name="each_batch_def_form" property="msg"/>';
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
						sortname: 'BATCH_PROC_SEQ',
						sorttype: 'int',
						gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','批次執行序','批次作業','批次作業說明','錯誤中斷是否執行' ,'批次作業類型'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
							{name:'BATCH_PROC_SEQ',index :'BATCH_PROC_SEQ',sortname: 'BATCH_PROC_SEQ',  fixed: true, width:100  ,sorttype: 'int'},
							{name:'BATCH_PROC_NAME', fixed: true, width:150},
							{name:'BATCH_PROC_DESC', fixed: true, width:180 },
							{name:'ERR_BREAK', fixed: true, width:100 },
							{name:'PROC_TYPE', fixed: true, width:100 },
						],
// 						emptyrecords: '查無資料...',//沒有作用

						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');

						},
						beforeProcessing: function(data, status, xhr){
							var seq ="";
							for(var rowCount in data){
								seq = data[rowCount].BATCH_PROC_SEQ;
								data[rowCount].BTN = '<button type="button" id="edit_' + seq + '" onclick="edit_p(this.id , \''+seq+'\')"><img src="./images/edit.png"/></button>';
// 				if(window.console)console.log("BTN>>"+data[rowCount].BTN);
							}
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
						    $("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
						    if(window.console){console.log("sortname="+$("#sortname").val());}
						    if(window.console){console.log("sortorder="+$("#sortorder").val());}
						},
// 						   e
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if($("#ac_key").val() == 'save' || $("#ac_key").val() == 'update' || $("#ac_key").val() == 'delete' ){
// 					initSearchs();
					onPut('',true);
					alterMsg();
				}else if($("#ac_key").val() == 'back'){
// 					initSearchs();
					onPut('search' , true);
// 					$("#ac_key").val("");
				}else{
					onPut('search');
				}
			}
			
			function onPut(str , isBack){
				if(str == "search"){
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
// 					getSearchs();
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');//e
				}else{
// 					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=each_batch_def_bo&method=search_toJson&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
// 				 ajaxRowOptions: { async: true }
//		    	20150722 edit by hugo  req by kevin 先不紀錄排序
				resetSortname(newOption , 'BATCH_PROC_SEQ' , 'ASC');
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
				unblockUI();
			}	
			
			function add_p(str){
				var array = ["serchStrs" , "sortname" , "sortorder"];
				cleanFormExceptArray( $('#formID'), array);
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				$("#BATCH_PROC_SEQ").val(id) ;
				
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
			
// 			取得user查詢條件
			function getSearchs(){
				var serchs = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					serchs[field.name] = field.value;
				});
					if(window.console){console.log("JSON.stringify(serchs)"+JSON.stringify(serchs));}
				$("#serchStrs").val(JSON.stringify(serchs));
			}
			
// 			初始化查詢條件
			function initSearchs(){
				var serchs = {};
				if(window.console){console.log("initSearchs.serchStrs>>"+$("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					serchs = $.parseJSON($("#serchStrs").val()) ;
				}
// 				if(window.console){console.log("serchs>>"+serchs);}
				for(var key in serchs){
// 					if(window.console){console.log(key+"="+serchs[key]);}
// 					模式一 所有查詢條件清空
// 					$("#"+key).val("");
					if(window.console){console.log(key+".val"+$("#"+key).val());}
// 					模式二 塞入user當初查詢條件
					if(key!="ac_key"){
						$("#"+key).val(serchs[key]);
					}
					
// 					$("#"+key).val("0040000");
				}
			}
// 			清空所有查詢條件
			function cleanSearchs(){
				var serchs = {};
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					serchs = $.parseJSON($("#serchStrs").val()) ;
				}
				for(var key in serchs){
// 					所有查詢條件清空
					$("#"+key).val("");
				}
			}
			
// 			表單清空
			function cleanFormII(){
				$.each($('#formID').serializeArray(), function(i, field) {
					if(field.name!="serchStrs" || field.name!="sortname" || field.name!="sortorder"){
						$("#"+field.name).val("");
					}
					
				});
			}
			
		</script>
	</body>
</html>
