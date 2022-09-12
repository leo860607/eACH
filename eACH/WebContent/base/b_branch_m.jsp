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
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
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
				<html:form styleId="formID" action="/bank_branch">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 15%">總行代號</td>
								<td style="width: 35%">
									<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)">
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "bank_branch_form" property="bgIdList" label="label" value="value"/>
									</html:select>
								</td>
								<td class="header" style="width: 15%">分行代號</td>
								<td>
									<html:select styleId="BRBK_ID" property="BRBK_ID">
										<html:option value="">全部</html:option>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="4" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
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
		var tmpPage = 1;
		var isSort = false;
		var gridOption;
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				var ac_key = '<bean:write name="bank_branch_form" property="ac_key"/>';
				initSelect();
				alterMsg();
// 				initGrid();
				initGridOption();
				if(fstop.isNotEmptyString(ac_key) ){
					onPut('');
				}else{
					onPut('search');
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 250,
		            	sortname:'BGBK_ID',
// 		            	sortable: true,
// 		            	sorttype:"text",
// 		            	multiSort:true,
// 		            	sortname: 'BGBK_ID,BRBK_NAME',
		            	sortorder: 'asc',
		            	shrinkToFit: true,
		            	rowNum: 10,
// 		            	rownumbers:true,
						colNames :['檢視明細','總行代號','分行代號','分行名稱','交換所代號','啟用日期','停用日期'],
						colModel: [
				            	    {name:'BTN', index:'BTN' , fixed: true, align: 'center', width:60},
									{name:'id.BGBK_ID',index:'BGBK_ID' , fixed: true, width:80},
									{name:'id.BRBK_ID',index:'BRBK_ID', fixed: true, width:80},
									{name:'BRBK_NAME',index:'BRBK_NAME', fixed: true, width:420},
									{name:'TCH_ID',index:'TCH_ID', fixed: true, width:75},
									{name:'ACTIVE_DATE', index:'ACTIVE_DATE', fixed: true, width:70},
									{name:'STOP_DATE', index:'STOP_DATE', fixed: true, width:70}
								],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							
						},
						loadComplete:function (data){
// 							if(window.console){console.log("data>>"+data.page);}
							get_curPage(this ,data.page , null , null );
							noDataEvent(data);
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							if(window.console){console.log("index>>"+index);}
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
// 					        return 'start';
// 					        return 'stop';
					    },
					    onPaging: function(data) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var bgbkId = "";
							var brbkId = "";
// 							if(console.log){console.log("data.length>>"+data);}
							for(var i = 0; i < data.rows.length ; i++){
								bgbkId = data.rows[i].id.BGBK_ID;
								brbkId = data.rows[i].id.BRBK_ID;
								data.rows[i].BTN = '<button type="button" id="edit_' + i + '" onclick="edit_p(\'edit\', \'' + bgbkId + '\',\''+ brbkId + '\')"><img src="./images/edit.png"/></button>';
// 								if(console.log){console.log(data.rows[i].BTN);}
								$("#bs_table").jqGrid('addRowData', i + 1, data.rows[i]);
							}
						},
	 					loadtext: "處理中...",
// 	 					20150530 edit by hugo req ACH 初始化要頁數要 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
						
				};
			}
			
			
			function alterMsg(){
				var msg = '<bean:write name="bank_branch_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function onPut(str){
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
				if($("#BGBK_ID").val() != "" && $("#BRBK_ID").val() == null){
// 					alert("無分行資料");
					$("#bs_table").clearGridData(true).trigger("reloadGrid");
				}else{
					if(fstop.isEmpty(str) || fstop.isEmptyString(str)){
					 	searchData('back');
					}else{
						searchData();
					}
				}
			}	
			
			function searchData(str){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=bank_branch_bo&method=pageSearch&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				if(window.console){console.log("pageForSort>>"+'<bean:write name="bank_branch_form" property="pageForSort"/>');}
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					newOption.page = parseInt('<bean:write name="bank_branch_form" property="pageForSort"/>');
					resetSortname(newOption , 'BGBK_ID,BRBK_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'BGBK_ID,BRBK_ID' , 'ASC' , true);
					newOption.page = 1;
					$("#pageForSort").val(1);
				}
				$("#resultData").jqGrid(newOption);
			}
			
			
			function add_p(str){
// 				$("#formID").validationEngine('detach');
// 				cleanFormNE(document.getElementById(str));
				cleanFormNE($("#search_tab"));
// 				alert("serchStrs>>"+$("#serchStrs").val());
				if(window.console){console.log("serchStrs>>"+$("#serchStrs").val());}
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}	
			
			function edit_p(str , id , id2){
				$("#ac_key").val(str);
				$("#target").val('edit_p');
				var tmp = {};
				tmp['BGBK_ID']= id;
				tmp['BRBK_ID']= id2;
				$("#edit_params").val(JSON.stringify(tmp));
				$("form").submit();
			}	
		
			function getBrbk_List(bgbkId){
				if(bgbkId == ''){
					$("#BRBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_branch_bo", method:"getBank_branch_List" , bgbkId:bgbkId  };
					fstop.getServerDataExII(uri, rdata, false,creatBrBkList);
				}
			}
			
			function creatBrBkList(obj){
				var select = $("#BRBK_ID");
				getBrbk_List("");
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function initSelect(){
				var select = $("#BGBK_ID");
				var val = select.find(":selected").val();
				getBrbk_List(val);
	
				$("#BRBK_ID").children().each(function() {
					//alert(this.text);    //  文字
					//alert(this.value);   //  值
				    if(this.value =='<bean:write name="bank_branch_form" property="BRBK_ID"/>' ){
				    	this.selected = true;
						//$(this).attr("selected", "true");
				    }
				});
			}
		</script>
	</body>
</html>
