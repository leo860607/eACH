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
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
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
			<div id="opPanel" style="margin-top: 5px">
				<html:form styleId="formID" action="/fee_adj">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="BRBK_ID" styleId="BRBK_ID"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<table id="search_tab">
							<tr>
								<td class="header necessary" style="width: 16%">調整年月</td>
								<td>
									<html:text styleId="YYYYMM" property="YYYYMM" size="6" maxlength="6" styleClass="validate[required, funcCall[checkYYYYMM]]"/>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" onclick="add_p()"><img src="./images/add.png"/>&nbsp;新增</label>
										<label class="btn" id="publish"  onclick="publish(this.id)"><img src="./images/resend.png"/>&nbsp;發布</label>
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
			var uri="/eACH/baseInfo",gridOption,queryInterval;
			var pageForSort = 1;
			var defaultPage = '<bean:write name="fee_adj_form" property="pageForSort"/>';
			
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				initGrid();
				initSearchs();
				
				if($("#ac_key").val() != "" && $("#ac_key").val() != "search"){
					if(defaultPage.length != 0){
						pageForSort = parseInt(defaultPage);
					}
					reOnPut('search');
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="fee_adj_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 270,
		            	sorttype:"text",
		            	sortname:'BRBK_ID',
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	//loadonce: true,
		            	page:pageForSort,
						colNames:['檢視明細','序號','分行代號','分行名稱','調帳金額','生效日期'],
		            	colModel: [
							{name:'BTN',index:'BTN',fixed:true,width: 70,align:'center',sortable:false},
							{name:'SNO',index:'SNO',fixed:true,width: 65},
							{name:'id.BRBK_ID',index:'BRBK_ID',fixed:true,width: 90},
							{name:'BRBK_NAME',index:'BRBK_NAME',fixed:true,width: 200},
							{name:'FEE',index:'FEE',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
							{name:'ACTIVE_DATE',index:'ACTIVE_DATE',fixed:true,width: 90}
						],
						loadComplete: function(data){
							//在gridComplete event後發動，將目前所在頁數存到pageForSort
				        	pageForSort = data.page;
						},
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							//轉換「傳送通知時間」格式
							var list = data.rows;
							for(var i = 0; i < list.length; i++){
								list[i].BTN = '<button type="button" id="edit_' + list[i].SNO + '" onclick="edit_p(\'' + list[i].id.YYYYMM + '\',\'' + list[i].id.BRBK_ID + '\')"><img src="./images/edit.png"/></button>';
								//if(console.log){console.log(list[i].BTN);}
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
	 					loadtext: "處理中...",
	 					pgtext: "{0} / {1}"
				};
			}
			
			function initGrid(){
				$("#resultData").jqGrid(gridOption);
			}
			
			function onPut(str){
				if(str == "search"){
					blockUI();
					if(!$('#formID').validationEngine('validate')){
						unblockUI();return;
					}
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					searchData(str);
					unblockUI();
				}
			}
			
			function searchData(str){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
// 				newOption.url = "/eACH/baseInfo?component=fee_adj_bo&method=search_toJson&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?component=fee_adj_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = 1;
				$("#resultData").jqGrid(newOption);
			}
			
			function add_p(){
				getSearch_condition('search_tab' , 'input , select', 'serchStrs');
// 				cleanFormII(document.getElementById('add'));
				cleanFormNE(document.getElementById('add'));
				$('#formID').validationEngine('detach');
				$("#ac_key").val('add');
				$("#target").val('add_p');
				$("form").submit();
			}
			
			function edit_p(yyyymm, brbkId){
				$('#formID').validationEngine('detach');
				$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
				getSearch_condition('search_tab' , 'input , select', 'serchStrs');
				$("#YYYYMM").val(yyyymm);
				$("#BRBK_ID").val(brbkId);
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			//初始化查詢條件
			function initSearchs(){
				var serchs = {};
				//if(window.console){console.log("initSearchs.serchStrs>>"+$("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					serchs = $.parseJSON($("#serchStrs").val());
				}
				//if(window.console){console.log("serchs>>"+serchs);}
				for(var key in serchs){
					//if(window.console){console.log(key+".val("+$("#"+key).val() + ")");}
					//模式二 塞入user當初查詢條件
					if(key != "ac_key" && key != "serchStrs" && key != "pageForSort"){
						$("#"+key).val(serchs[key]);
					}
				}
			}
			
			function checkYYYYMM(){
				var str = $("#YYYYMM").val();
				if(str != ""){
					var patt = new RegExp("0[0-9][0-9][0-9](0[1-9]|1[0-2])$");
					var res = patt.test(str);
					if(!res){
						return "年月格式錯誤! ex:010403";
					}
				}
				return true;
			}
			
			function reOnPut(){
				blockUI();
				if(!$('#formID').validationEngine('validate')){
					unblockUI();return;
				}
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
// 				newOption.url = "/eACH/baseInfo?component=fee_adj_bo&method=search_toJson&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?component=fee_adj_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				newOption.page = pageForSort;
				resetSortname(newOption , 'SNO' , 'ASC' , false);
				$("#resultData").jqGrid(newOption);
				unblockUI();
			}
			
			function publish(str){
				if(!$('#formID').validationEngine('validate')){
					unblockUI();return;
				}
				if(confirm('是否確定發布調帳資料?')){
					$("#ac_key").val(str);
					$("#target").val('search');
					getSearch_condition('search_tab' , 'input , select', 'serchStrs');
					$("form").submit();
				}
			}
		</script>
	</body>
</html>