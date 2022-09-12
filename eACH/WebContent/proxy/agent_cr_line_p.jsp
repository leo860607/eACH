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
<%-- 				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a> --%>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/agent_cr_line">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 15%">發動者統一編號</td>
								<td style="width: 15%">
									<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="12" maxlength="10" styleClass="validate[maxSize[10],notChinese] text-input"></html:text>
								</td>
								<td class="header" style="width: 15%">發動者名稱</td>
								<td>
									<html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="40" maxlength="40"></html:text>
								</td>
							</tr>
							<tr>
								<td class="header" >低於剩餘額度</td>
								<td >
									<html:text styleId="REST_CR_LINE" property="REST_CR_LINE" size="12" maxlength="10" styleClass="validate[maxSize[10],notChinese] text-input"></html:text>
								</td>
								<td class="header" >低於基本額度%</td>
								<td>
									<html:text styleId="BASIC_CR_LINE" property="BASIC_CR_LINE" size="12" maxlength="10" ></html:text>
								</td>
							</tr>
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
				var msg = '<bean:write name="agent_cr_line_form" property="msg"/>';
				if(window.console){console.log("msg>>"+msg);}
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
						sortname: 'SND_COMPANY_ID',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','發動者統一編號','發動者名稱' ,'基本額度','剩餘額度' , '代理業者統編' ,'代理業者名稱'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
							{name:'SND_COMPANY_ID', fixed: true, width:120},
							{name:'COMPANY_NAME', fixed: true, width:120},
							{name:'BASIC_CR_LINE', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'REST_CR_LINE', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'COMPANY_ID',index:'COMPANY_ID',fixed:true,width: 90},
							{name:'TMP_COMPANY_NAME',index:'TMP_COMPANY_NAME',fixed:true,width: 90},
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
							if(window.console){console.log("i="+i);}
							if(window.console){console.log("ac_key="+$("#ac_key").val());}
							if(i!=0 && ($("#ac_key").val() == 'save' || $("#ac_key").val() == 'update' || $("#ac_key").val() == 'delete')){
				 				alterMsg();
				 				$("#ac_key").val("");
				 				if(window.console){console.log("2i="+i);}
							}
							i++;
						},
						beforeProcessing: function(data, status, xhr){
							var snd_company_id ="";
							for(var rowCount in data){
								snd_company_id = data[rowCount].SND_COMPANY_ID;
								data[rowCount].BTN = '<button type="button" id="edit_' + snd_company_id + '" onclick="edit_p(this.id , \''+snd_company_id+'\')"><img src="./images/edit.png"/></button>';
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
// 					$("#formID").validationEngine('detach');
				}
				
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				var qStr = "component=agent_cr_line_bo&method=search_toJson&"+$("#formID").serialize();
				if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
// 				 ajaxRowOptions: { async: true }
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'SND_COMPANY_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'SND_COMPANY_ID' , 'ASC' , true);
				}
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
			}	
			
			function add_p(str){
				cleanFormNE(document.getElementById(str));
// 				$("#REST_CR_LINE").val("0");
// 				$("#BASIC_CR_LINE").val("0");
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				var tmp={};
				tmp['SND_COMPANY_ID'] = id;
				$("#edit_params").val(JSON.stringify(tmp));
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
					if(field.name!="serchStrs"){
						$("#"+field.name).val("");
					}
					
				});
			}
		</script>
	</body>
</html>
