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
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
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
			<div id="opPanel">
				<html:form styleId="formID" action="/fee_code_nw">
					<html:hidden property="ac_key" styleId="ac_key" />
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
								<td class="header" style="width: 20%">交易代號</td>
								<td>
									<html:select styleId="FEE_ID" property="FEE_ID">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name="fee_code_nw_form" property="idList" label="label" value="value"/>
									</html:select>
								</td>
								<td class="header" style="width: 20%">啟用日期</td>
								<td>
									<html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
							</tr>
							<tr>
								<td class="header">收費類型</td>
									<td>
										<html:select styleId="FEE_TYPE" property="FEE_TYPE">
										<html:option value="">全部</html:option>
										<html:option value="A">固定</html:option>
										<html:option value="B">外加</html:option>
										<html:option value="C">百分比</html:option>
										<html:option value="D">級距</html:option>
										</html:select>
									</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
<%-- 									<logic:equal name="login_form" property="userData.USER_TYPE" value="A"> --%>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="new" onclick="onPut(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
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
					<table id="bs_table"></table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
		
			$(document).ready(function () {
				blockUI();
				init();
// 				alert('<bean:write name = "fee_code_nw_form"/>')
				unblockUI();
	        });
			
			function init(){
				initGridOption();
				initGrid();
				alterMsg();
				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker();
				var acKey = $("#ac_key").val();
				if(fstop.isNotEmptyString(acKey)){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="fee_code_nw_form" property="msg"/>';
				var result = '<bean:write name="fee_code_nw_form" property="result"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 240,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
		            	<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
							colNames:['檢視明細','交易代號','交易項目','收費類型','級距類型','級距序號','級距金額(起)','級距金額(迄)','啟用日期', '扣款行手續費', '折扣後扣款行手續費','入帳行手續費','折扣後入帳行手續費','銷帳行手續費','折扣後銷帳行手續費','交換所手續費','折扣後交換所手續費','發動行手續費','折扣後發動行手續費','客戶支付手續費','說明','啟用註解'],
							colModel: [
// 										{name:'FEE_UID',index:'FEE_UID',align:'center',fixed:true,width: 100,sortable:false}, 
										{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
										{name:'FEE_ID',index:'FEE_ID',align:'center',fixed:true,width: 100},
										{name:'TXN_NAME',index:'TXN_NAME',fixed:true,width: 100},
										{name:'FEE_TYPE_CHT',index:'FEE_TYPE_CHT',align:'center',fixed:true,width: 90},
										{name:'FEE_LVL_TYPE_CHT',index:'FEE_LVL_TYPE_CHT',align:'center',fixed:true,width: 90},
										{name:'FEE_DTNO',index:'FEE_DTNO',align:'center',fixed:true,width: 90},
										{name:'FEE_LVL_BEG_AMT',index:'FEE_LVL_BEG_AMT',fixed:true,width: 120,align:'right'},
										{name:'FEE_LVL_END_AMT',index:'FEE_LVL_END_AMT',fixed:true,width: 120,align:'right'},
										{name:'START_DATE',index:'START_DATE',fixed:true,width: 90},
// 										{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'OUT_BANK_FEE_DISC',index:'OUT_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'IN_BANK_FEE_DISC',index:'IN_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'WO_BANK_FEE',index:'WO_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'WO_BANK_FEE_DISC',index:'WO_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'TCH_FEE',index:'TCH_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'TCH_FEE_DISC',index:'TCH_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'SND_BANK_FEE',index:'SND_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'SND_BANK_FEE_DISC',index:'SND_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'HANDLECHARGE',index:'HANDLECHARGE',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
										{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'OUT_BANK_FEE_DISC',index:'OUT_BANK_FEE_DISC',fixed:true,width: 150,align:'right'},
										{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'IN_BANK_FEE_DISC',index:'IN_BANK_FEE_DISC',fixed:true,width: 150,align:'right'},
										{name:'WO_BANK_FEE',index:'WO_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'WO_BANK_FEE_DISC',index:'WO_BANK_FEE_DISC',fixed:true,width: 150,align:'right'},
										{name:'TCH_FEE',index:'TCH_FEE',fixed:true,width: 95,align:'right'},
										{name:'TCH_FEE_DISC',index:'TCH_FEE_DISC',fixed:true,width: 150,align:'right'},
										{name:'SND_BANK_FEE',index:'SND_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'SND_BANK_FEE_DISC',index:'SND_BANK_FEE_DISC',fixed:true,width: 150,align:'right'},
										{name:'HANDLECHARGE',index:'HANDLECHARGE',fixed:true,width: 150,align:'right'},
										{name:'FEE_DESC',index:'DESC',fixed:true,width: 150},
										{name:'ACTIVE_DESC',index:'ACTIVE_DESC',fixed:true,width: 150}
							],
						</logic:equal>
						<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
							colNames:['檢視明細','交易代號','交易項目','收費類別','級距類型','級距序號','級距金額(起)','級距金額(迄)','啟用日期', '扣款行手續費','入帳行手續費','交換所手續費','發動行手續費','客戶支付手續費','說明','啟用註解'],
							colModel: [
										{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
										{name:'FEE_ID',index:'FEE_ID',align:'center',fixed:true,width: 100},
										{name:'TXN_NAME',index:'TXN_NAME',fixed:true,width: 120},
										{name:'START_DATE',index:'START_DATE',fixed:true,width: 90},
										{name:'FEE_TYPE_CHT',index:'FEE_TYPE_CHT',align:'center',fixed:true,width: 90},
										{name:'FEE_LVL_TYPE_CHT',index:'FEE_LVL_TYPE_CHT',align:'center',fixed:true,width: 90},
										{name:'FEE_DTNO',index:'FEE_DTNO',align:'center',fixed:true,width: 90},
										{name:'FEE_LVL_BEG_AMT',index:'FEE_LVL_BEG_AMT',fixed:true,width: 120,align:'right'},
										{name:'FEE_LVL_END_AMT',index:'FEE_LVL_END_AMT',fixed:true,width: 120,align:'right'},
// 										{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'TCH_FEE',index:'TCH_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'SND_BANK_FEE',index:'SND_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
// 										{name:'HANDLECHARGE',index:'HANDLECHARGE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
										{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'TCH_FEE',index:'TCH_FEE',fixed:true,width: 95,align:'right'},
										{name:'SND_BANK_FEE',index:'SND_BANK_FEE',fixed:true,width: 95,align:'right'},
										{name:'HANDLECHARGE',index:'HANDLECHARGE',fixed:true,width: 150,align:'right'},
										{name:'FEE_DESC',index:'DESC',fixed:true,width: 150},
										{name:'ACTIVE_DESC',index:'ACTIVE_DESC',fixed:true,width: 150}
							],
						</logic:equal>
		            	gridComplete:function (){
			            	$("#bs_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#bs_table .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							for(var rowCount in data){
								var feeId = "",startDate = "",feeType = "",feeUid = "",feeDtno = "",fee_lvl_type="",in_bank_fee="";
								var in_bank_fee_disc = "",out_bank_fee = "",out_bank_fee_disc = "",snd_bank_fee = "",snd_bank_fee_disc = "";
								var tch_fee = "",tch_fee_disc = "",wo_bank_fee = "",wo_bank_fee_disc = "",handlecharge = "",start_date="";
								feeType = data[rowCount].FEE_TYPE;
								feeUid = data[rowCount].FEE_UID;
								feeId = data[rowCount].FEE_ID;
								feeDtno = data[rowCount].FEE_DTNO;
								fee_lvl_type = data[rowCount].FEE_LVL_TYPE;
								start_date = data[rowCount].START_DATE;
								
								if(!(feeType==='D')){
									if(feeType==='C'){
										data[rowCount].IN_BANK_FEE =  data[rowCount].IN_BANK_FEE+"%";
										data[rowCount].IN_BANK_FEE_DISC = data[rowCount].IN_BANK_FEE_DISC +'%';
										data[rowCount].OUT_BANK_FEE = data[rowCount].OUT_BANK_FEE +'%';
										data[rowCount].OUT_BANK_FEE_DISC = data[rowCount].OUT_BANK_FEE_DISC +'%';
										data[rowCount].SND_BANK_FEE = data[rowCount].SND_BANK_FEE +'%';
										data[rowCount].SND_BANK_FEE_DISC = data[rowCount].SND_BANK_FEE_DISC +'%';
										data[rowCount].TCH_FEE = data[rowCount].TCH_FEE +'%';
										data[rowCount].TCH_FEE_DISC = data[rowCount].TCH_FEE_DISC +'%';
										data[rowCount].WO_BANK_FEE = data[rowCount].WO_BANK_FEE +'%';
										data[rowCount].WO_BANK_FEE_DISC = data[rowCount].WO_BANK_FEE_DISC +'%';
										data[rowCount].HANDLECHARGE = data[rowCount].HANDLECHARGE +'%';
									}
									if(feeUid===''){
										data[rowCount].BTN = '<button type="button" id="edit_' + feeUid + '" onclick="edit_p(\''+feeId+'\',\''+start_date+'\',\''+feeType+'\')"><img src="./images/edit.png"/></button>';
									}else{
										data[rowCount].BTN = '<button type="button" id="edit_' + feeUid + '" onclick="edit_p_nw(\''+feeUid+'\',\''+feeType+'\',\'00\')"><img src="./images/edit.png"/></button>';
									}
								}else{
									if(fee_lvl_type==='2'){
										data[rowCount].IN_BANK_FEE =  data[rowCount].IN_BANK_FEE+"%";
										data[rowCount].IN_BANK_FEE_DISC = data[rowCount].IN_BANK_FEE_DISC +'%';
										data[rowCount].OUT_BANK_FEE = data[rowCount].OUT_BANK_FEE +'%';
										data[rowCount].OUT_BANK_FEE_DISC = data[rowCount].OUT_BANK_FEE_DISC +'%';
										data[rowCount].SND_BANK_FEE = data[rowCount].SND_BANK_FEE +'%';
										data[rowCount].SND_BANK_FEE_DISC = data[rowCount].SND_BANK_FEE_DISC +'%';
										data[rowCount].TCH_FEE = data[rowCount].TCH_FEE +'%';
										data[rowCount].TCH_FEE_DISC = data[rowCount].TCH_FEE_DISC +'%';
										data[rowCount].WO_BANK_FEE = data[rowCount].WO_BANK_FEE +'%';
										data[rowCount].WO_BANK_FEE_DISC = data[rowCount].WO_BANK_FEE_DISC +'%';
										data[rowCount].HANDLECHARGE = data[rowCount].HANDLECHARGE +'%';
									}
									data[rowCount].BTN = '<button type="button" id="edit_' + feeUid + '" onclick="edit_p_nw( \''+feeUid+'\',\''+feeType+'\', \'' +feeDtno+ '\')"><img src="./images/edit.png"/></button>';
								}
								
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
							noDataEvent(data , $("#bs_table") );
						},
	 					loadtext: "處理中..."
				};
			}
			
			function initGrid(){
				$("#bs_table").jqGrid(gridOption);
			}
			
			function onPut(str){
				var tmpSearchStrs = $("#serchStrs").val(); 
				if(str == "search" || str == "back" ){
					if($("#formID").validationEngine("validate")){
						blockUI();
						getSearch_condition('search_tab', 'input, select', 'serchStrs');
						searchData(str);
						unblockUI();
					}
				}else{
					if(str == "new"){
						cleanFormNE(document.getElementById(str));
					}
					$("#formID").validationEngine('detach');
					blockUI();
					$("#ac_key").val(str);
					$("#target").val('add_p');
					$("#serchStrs").val(tmpSearchStrs); 
					$("form").submit();
				}
			}
			
			function searchData(str){
				var feeId = $("#FEE_ID").val();
				var startDate = $("#START_DATE").val();
				var feeType = $("#FEE_TYPE").val();
				$("#bs_table").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=fee_code_nw_bo&method=search_toJsonII&feeId="+feeId+"&startDate="+startDate+"&feeType="+feeType+"&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'FEE_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'FEE_ID' , 'ASC' , true);
				}
				$("#bs_table").jqGrid(newOption);
			}
			
			
			
			function edit_p(feeId,start_date,feeType){
				blockUI();
				var tmp ={};
				tmp['FEE_ID'] = feeId;
				tmp['START_DATE'] = start_date;
				tmp['FEE_TYPE'] = feeType;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			
			function edit_p_nw(feeUid,feeType,feeDtno){
				blockUI();
				var tmp ={};
				tmp['FEE_UID'] = feeUid;
				tmp['FEE_TYPE'] = feeType;
				tmp['FEE_DTNO'] = feeDtno;
				$("#edit_params").val(JSON.stringify(tmp));
// 				alert($("#edit_params").val());
				$("#ac_key").val('edit_nw');
				if("D"==feeType){
					$("#ac_key").val('edit_lvl');
				}
// 				alert($("#edit_params").val());
// 				alert($("ac_key").val());
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			//取得user查詢條件
			function getSearches(){
				var searches = {};
				$("#serchStrs").val("");
				$.each($('#formID').serializeArray(), function(i, field) {
					searches[field.name] = field.value;
				});
				$("#serchStrs").val(JSON.stringify(searches));
				//if(window.console){console.log($("#serchStrs").val());}
			}
			
		</script>
	</body>
</html>