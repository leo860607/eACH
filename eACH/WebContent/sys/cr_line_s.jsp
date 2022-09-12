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
		<script type="text/javascript" src="./js/BigInteger_min.js"></script>
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
				<html:form styleId="formID" action="/cr_line">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="s_bank_id" styleId="s_bank_id" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 100px">銀行代號</td>
									<td>
										<html:select styleId="BANK_ID" property="BANK_ID">
<%-- 											<html:option value="none">==請選擇銀行代號==</html:option> --%>
											<logic:present name="cr_line_form" property="bgbkIdList">
												<html:option value="">全部</html:option> 
												<html:optionsCollection name="cr_line_form" property="bgbkIdList" label="label" value="value"></html:optionsCollection> 
											</logic:present>
										</html:select>
									</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
<!-- 									<label class="btn" id="clean" onclick ="cleanForm(this)"><img src="./images/clean.png"/>&nbsp;清除查詢條件</label> -->
								<logic:equal  name="userData" property="s_auth_type" value="A">
									<label class="btn" id="add" onclick ="add_p(this.id);"><img src="./images/add.png"/>&nbsp;新增</label>
								</logic:equal>
								
								<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
									<label class="btn" id="search_Over_CR" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;重新計算剩餘額度</label>
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="update_Over_CR" onclick="update_Over_CR(this.id)"><img src="./images/edit.png"/>&nbsp;更新剩餘額度</label>
								</logic:equal>
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
// 		var updata = new Array();
		var updata = {};
		var isClick_Over_CR = false;
// 		var bigInt = require("big-integer");
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
				initGrid_Over_CR();
				initSelect();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				
			}
			
			function alterMsg(){
				var msg = '<bean:write name="cr_line_form" property="msg"/>';
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
						sortname: 'BANK_ID',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','銀行代號','銀行名稱','基本額度','剩餘額度'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
							{name:'BANK_ID', fixed: true, width:120},
							{name:'BANK_NAME', fixed: true, width:120},
							{name:'BASIC_CR_LINE', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'REST_CR_LINE', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
// 							$("#resultData").setGridWidth($("#container").width()-15); 
// 							$("#resultData").setGridHeight($("#container").height()-($("#opPanel").height()*2)-$("#breadcrumb").height()-25);
							unblockUI();
							if(window.console){console.log("i="+i);}
							if(window.console){console.log("ac_key="+$("#ac_key").val());}
							if(i!=0 && ($("#ac_key").val() == 'save' || $("#ac_key").val() == 'update' || $("#ac_key").val() == 'delete')){
				 				$("#ac_key").val("");
				 				if(window.console){console.log("2i="+i);}
							}
							i++;
						},
						beforeProcessing: function(data, status, xhr){
// 							TODO 預計把建議額度與基本額度不同的資料 將BANK_ID 建議額度 基本額度 存成JSON字串 EX[{BANK_ID:"4210000",REC_CR_LINE:"1234" , BASIC_CR_LINE:"1000"},{}]
// 								var aray = [];aray[0]=data[rowCount]
							var bank_id ="";
							for(var rowCount in data){
								bank_id = data[rowCount].BANK_ID;
								data[rowCount].BTN = '<button type="button" id="edit_' + bank_id + '" onclick="edit_p(this.id , \''+bank_id+'\')"><img src="./images/edit.png"/></button>';
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
			
			function initGrid_Over_CR(){
				gridOption_Over_CR = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
		            	sortable: true,
						sortname: 'BANK_ID',
						sorttype: 'text',
						//gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','銀行代號','銀行名稱','基本額度','剩餘額度','計算剩餘額度'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
							{name:'BANK_ID', fixed: true, width:120},
							{name:'BANK_NAME', fixed: true, width:120},
							{name:'BASIC_CR_LINE', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'REST_CR_LINE', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'CNT_OVER_CR', fixed: true, width:100 ,sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
// 							$("#resultData").setGridWidth($("#container").width()-15); 
// 							$("#resultData").setGridHeight($("#container").height()-($("#opPanel").height()*2)-$("#breadcrumb").height()-25);
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
// 							把建議額度與基本額度不同的資料 將BANK_ID 建議額度 基本額度 存成JSON字串 EX[{BANK_ID:"4210000",REC_CR_LINE:"1234" , BASIC_CR_LINE:"1000"},{}]
// 								var aray = [];aray[0]=data[rowCount]
							var bank_id ="";
							for(var rowCount in data){
								bank_id = data[rowCount].BANK_ID;
								data[rowCount].BTN = '<button type="button" id="edit_' + bank_id + '" onclick="edit_p(this.id , \''+bank_id+'\')"><img src="./images/edit.png"/></button>';
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
						rowattr: function (rd) {
						    if (rd.REST_CR_LINE != rd.CNT_OVER_CR ) {
						    	var tmp = {};
						    	var zero = bigInt(11);
						    	if(window.console)console.log("zero>>"+zero );
// 						    	tmp[rd.BANK_ID] = rd.CNT_OVER_CR;
// 						    	updata[rd.BANK_ID] =  bigInt(rd.CNT_OVER_CR).value;
						    	updata[rd.BANK_ID] =  rd.CNT_OVER_CR+"";
// 						    	updata.push(JSON.stringify(tmp)) ;
// 						    	if(window.console)console.log("updata>>"+updata );
						    	if(window.console)console.log("updata>>"+JSON.stringify(updata) );
						        return {"class": "resultDataRowError"};
						    }
						},
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			
			function resetData(){
				updata = {};
// 				isClick_Over_CR = false;
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
				var qStr = "component=cr_line_bo&method=search_toJson&"+$("#formID").serialize();
				if(str == "search_Over_CR"){
					isClick_Over_CR = true;
					newOption = gridOption_Over_CR ;
					qStr = "component=cr_line_bo&method=search_toJson4Over_CR&"+$("#formID").serialize();
				}else{
					isClick_Over_CR = false;
				}
				if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.ajaxRowOptions={ async: false };
// 				 ajaxRowOptions: { async: true }
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'BANK_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'BANK_ID' , 'ASC' , true);
				}
				if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(newOption);
				resetData();
			}	
			
			function add_p(str){
				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2 ,id3){
				var tmp={};
				tmp['BANK_ID'] = id;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}	
			<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
			function update_Over_CR(str){
				if(isClick_Over_CR == true){
					var rdata = {component:"cr_line_bo", method:"update_Over_CR" ,updata:JSON.stringify(updata) };
					var vResult = fstop.getServerDataExIII(uri,rdata,false,null,null,'showupDataResult');
					alert(vResult.msg);
					onPut('search_Over_CR');
				}else{
					alert("請先點選重新計算剩餘額度");
				}
				
			}
			</logic:equal>
			
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
