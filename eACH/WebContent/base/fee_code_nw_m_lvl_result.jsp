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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-新增</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
	</head>
	<body >
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">新增</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/fee_code_nw" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="FEE_TYPE" styleId="FEE_TYPE"/>
					<html:hidden property="FEE_UID" styleId="FEE_UID"/>
					<html:hidden property="FEE_DTNO" styleId="FEE_DTNO"/>
					<html:hidden property="FEE_LVL_TYPE" styleId="FEE_LVL_TYPE"/>
					<table>
						<tr >
							<td class="header necessary" style="width: 25%"><span>交易代號</span></td>
							<!-- 手續費代號應與交易代號相同，故僅限3碼(資料庫欄位為長度4) -->
							<td><html:text styleId="FEE_ID" property="FEE_ID" size="3" maxlength="3" styleClass="lock"></html:text></td>
								<td class="header necessary" style="width: 20%"><span>收費類型</span></td>
							<td>級距</td>
						</tr>
						<tr>
							<!-- 							TODO 未完成  要加入檢核啟用日期必須大於營業日期 -->
							<td class="header necessary" ><span>啟用日期</span></td>
							<td><html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="lock"></html:text>
							</td>
							<td class="header necessary"><span>收費別</span></td>
										<td>
											<logic:equal name="fee_code_nw_form" property="FEE_LVL_TYPE" value="1">
												固定
											</logic:equal>
											<logic:equal name="fee_code_nw_form" property="FEE_LVL_TYPE" value="2">
												百分比
											</logic:equal>
										</td>
						</tr>
						<tr>
<!-- 	   							<td class="header necessary" style="width: 20%"><span>級距序號</span></td> -->
<%-- 	   							<td><html:text styleId="FEE_DTNO" property="FEE_DTNO" readonly="true" size="3" maxlength="3" styleClass="lock"></html:text></td> --%>
	   							
						</tr>
						<tr>
	   							<td class="header necessary"><span>交易金額(起)</span></td>
							<td><html:text styleId="FEE_LVL_BEG_AMT" property="FEE_LVL_BEG_AMT" readonly="true"  styleClass="lock" size="13" maxlength="13" style="text-align: right"></html:text></td>
							<td class="header necessary"><span>交易金額(迄)</span></td>
							<td><html:text styleId="FEE_LVL_END_AMT" property="FEE_LVL_END_AMT" size="13" maxlength="13"  style="text-align: right" styleClass="lock"></html:text></td>
						</tr>
						</div>
						<tr>
							<td class="header necessary"><span>扣款行手續費</span></td>
							<td><html:text styleId="OUT_BANK_FEE" property="OUT_BANK_FEE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
							<td class="header necessary"><span>折扣後扣款行手續費</span></td>
							<td><html:text styleId="OUT_BANK_FEE_DISC" property="OUT_BANK_FEE_DISC" size="10" maxlength="10" styleClass="lock validate[required,decimal[7,2]]" style="text-align: right"></html:text><span  class="addpercent"></span></td>
						</tr>
						<tr>
							<td class="header necessary"><span>入帳行手續費</span></td>
							<td><html:text styleId="IN_BANK_FEE" property="IN_BANK_FEE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
							<td class="header necessary"><span>折扣後入帳行手續費</span></td>
							<td><html:text styleId="IN_BANK_FEE_DISC" property="IN_BANK_FEE_DISC" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span  class="addpercent"></span></td>
						</tr>
						<tr>
							<td class="header necessary"><span>交換所手續費</span></td>
							<td><html:text styleId="TCH_FEE" property="TCH_FEE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
							<td class="header necessary"><span>折扣後交換所手續費</span></td>
							<td><html:text styleId="TCH_FEE_DISC" property="TCH_FEE_DISC" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
						</tr>
						<tr>
							<td class="header necessary"><span>發動行手續費</span></td>
							<td><html:text styleId="SND_BANK_FEE" property="SND_BANK_FEE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
							<td class="header necessary"><span>折扣後發動行手續費</span></td>
							<td><html:text styleId="SND_BANK_FEE_DISC" property="SND_BANK_FEE_DISC" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
						</tr>
						<tr>
							<td class="header necessary"><span>銷帳行手續費</span></td>
							<td><html:text styleId="WO_BANK_FEE" property="WO_BANK_FEE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
							<td class="header necessary"><span>折扣後銷帳行手續費</span></td>
							<td><html:text styleId="WO_BANK_FEE_DISC" property="WO_BANK_FEE_DISC" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
						</tr>
						<tr>
							<td class="header"><span>客戶應支付手續費</span></td>
							<logic:notEqual name="fee_code_nw_form" property="HANDLECHARGE" value="">
								<td><html:text styleId="HANDLECHARGE" property="HANDLECHARGE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text><span class="addpercent"></span></td>
							</logic:notEqual>
							<logic:equal name="fee_code_nw_form" property="HANDLECHARGE" value="">
								<td><html:text styleId="HANDLECHARGE" property="HANDLECHARGE" size="10" maxlength="10" styleClass="lock" style="text-align: right"></html:text></span></td>
							</logic:equal>
							<td ><span></span></td>
							<td></td>
						</tr>
						<tr>
							<td class="header"><span>手續費說明</span></td>
							<td colspan="3">
								<html:text styleId="FEE_DESC" property="FEE_DESC" size="33" maxlength="33" styleClass="lock"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header"><span>啟用註解</span></td>
							<td colspan="3">
								<html:text property="ACTIVE_DESC" size="66" maxlength="66" styleClass="lock"></html:text>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
<!-- 								<label class="btn" id="add" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;儲存</label> -->
<!-- 								<label class="btn" id="clean" onclick="cleanFormNE(this)"><img src="./images/clean.png"/>&nbsp;清除</label> -->
								<label class="btn" id="back" onclick="onPut(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
								
								<logic:notEqual name="fee_code_nw_form" property="FEE_LVL_END_AMT" value="9999999999999">
 										<label class="btn" id="addmore" onclick="onPut(this.id)" disable><img src="./images/add.png"/>&nbsp;繼續新增級距</label>
 								</logic:notEqual>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>級距詳情</legend>
					<table id="bs_table"></table>
				</fieldset>
				</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function(){
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
			});
		
			function init(){
				$("#dataInputTable tr:odd").addClass("resultDataRowOdd");
				$("#dataInputTable tr:even").addClass("resultDataRowEven");
				initGridOption();
				initGrid();
				value = $("#FEE_LVL_TYPE").val();
				fee_type_change(value);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				$("#START_DATE").val('<bean:write name="fee_code_nw_form" property="START_DATE"/>');
				var fee_uid = $("#FEE_UID").val();
// 				alert('FEE_UID >> '+ fee_uid);
 				getLVLData(fee_uid);
 				checkDataEnd(fee_uid);
 				alterMsg();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="fee_code_nw_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
		
			function onPut(str){
				var fee_uid = $("#FEE_UID").val();
				if(str != "addmore"){
					$("#formID").validationEngine('detach');
					if(!checkDataEnd(fee_uid)){
						alert("級距資料尚未新增完畢 , 需有一筆交易金額(迄)為9999999999999之資料 \n 請點選繼續新增級距按鈕 !!!")
						return;
					}
				}else{
// 					var dtno = Number($("#FEE_DTNO").val())+1;
// 					alert(dtno);
// 					var dtnoStr = ""+dtno;
// 					alert(dtnoStr);
// 					if(dtnoStr.length<2){
// 						dtnoStr="0"+dtno;
// 					}
// 					alert(dtnoStr);
// 					$("#FEE_DTNO").val(dtnoStr);
// 					alert("submit DTNO >>>>> " +$("#FEE_DTNO").val())
					$("#ac_key").val('add_more');
					$("form").submit();
					return;
				}
				$("#target").val('search');
				$("#ac_key").val(str);
				$("form").submit();
			}
			
			function checkFee(){
				//扣款行手續費+入帳行手續費+交換所手續費+發動行手續費=0		
				var total1 = (parseFloat($('#OUT_BANK_FEE').val()) +
				parseFloat($('#IN_BANK_FEE').val()) +
				parseFloat($('#TCH_FEE').val()) +
				parseFloat($('#SND_BANK_FEE').val())+
// 				parseFloat($('#HANDLECHARGE').val())+
				parseFloat($('#WO_BANK_FEE').val())).toFixed(3);
				if(window.console){console.log("total1>>"+total1);}
				if(total1 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n扣款行手續費+入帳行手續費+\n發動行手續費+銷帳行手續費+交換所手續費=0");
					return false;
				}
				//折扣後扣款行手續費+折扣後入帳行手續費+折扣後交換所手續費+折扣後發動行手續費=0
				var total2 = (parseFloat($('#OUT_BANK_FEE_DISC').val()) +
				parseFloat($('#IN_BANK_FEE_DISC').val()) +
				parseFloat($('#TCH_FEE_DISC').val()) +
				parseFloat($('#SND_BANK_FEE_DISC').val())+
				parseFloat($('#WO_BANK_FEE_DISC').val())).toFixed(3);
				if(window.console){console.log("total2>>"+total2);}
				if(total2 != 0){
					alert("手續費金額錯誤!請確認是否符合以下規範：\n折扣後扣款行手續費+折扣後入帳行手續費+\n折扣後發動行手續費+折扣後銷帳行手續費+\n折扣後交換所手續費=0");
					return false;
				}
				return true;
			}
			
			function fee_type_change(value){
				//A固定 B外加 C百分比 D級距 1固定 2百分比
				if(value==='A'||value==='B'){
					$('.addpercent').text("");
					$('.LVLsection').hide();
				}
				if(value==='C'){
					$('.addpercent').text("%");
					$('.LVLsection').hide();
				}
				if(value==='D'){
					$('.LVLsection').show();
					$('.addpercent').text("");
				}
				if(value==='1'){
					$('.addpercent').text("");
				}
				if(value==='2'){
					$('.addpercent').text("%");
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
							colNames:['檢視明細','交易代號','交易項目','級距類型','級距序號','級距金額(起)','級距金額(迄)','啟用日期', '扣款行手續費', '折扣後扣款行手續費','入帳行手續費','折扣後入帳行手續費','銷帳行手續費','折扣後銷帳行手續費','交換所手續費','折扣後交換所手續費','發動行手續費','折扣後發動行手續費','客戶支付手續費','說明','啟用註解'],
							colModel: [
								{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
								{name:'FEE_ID',index:'FEE_ID',align:'center',fixed:true,width: 100},
								{name:'TXN_NAME',index:'TXN_NAME',fixed:true,width: 100},
								{name:'FEE_LVL_TYPE_CHT',index:'FEE_LVL_TYPE_CHT',align:'center',fixed:true,width: 90},
								{name:'FEE_DTNO',index:'FEE_DTNO',align:'center',fixed:true,width: 90},
								{name:'FEE_LVL_BEG_AMT',index:'FEE_LVL_BEG_AMT',fixed:true,width: 120,align:'right'},
								{name:'FEE_LVL_END_AMT',index:'FEE_LVL_END_AMT',fixed:true,width: 120,align:'right'},
								{name:'START_DATE',index:'START_DATE',fixed:true,width: 90},
//									{name:'OUT_BANK_FEE',index:'OUT_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'OUT_BANK_FEE_DISC',index:'OUT_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'IN_BANK_FEE',index:'IN_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'IN_BANK_FEE_DISC',index:'IN_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'WO_BANK_FEE',index:'WO_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'WO_BANK_FEE_DISC',index:'WO_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'TCH_FEE',index:'TCH_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'TCH_FEE_DISC',index:'TCH_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'SND_BANK_FEE',index:'SND_BANK_FEE',fixed:true,width: 95,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'SND_BANK_FEE_DISC',index:'SND_BANK_FEE_DISC',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
//									{name:'HANDLECHARGE',index:'HANDLECHARGE',fixed:true,width: 150,align:'right',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
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
		            	gridComplete:function (){
			            	$("#bs_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#bs_table .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					beforeProcessing: function(data, status, xhr){
							for(var rowCount in data){
								var feeId = "",startDate = "",feeType = "",feeUid = "",feeDtno = "" ;
								var in_bank_fee_disc = "",out_bank_fee = "",out_bank_fee_disc = "",snd_bank_fee = "",snd_bank_fee_disc = "";
								var tch_fee = "",tch_fee_disc = "",wo_bank_fee = "",wo_bank_fee_disc = "",handlecharge = "";
								feeType = data[rowCount].FEE_TYPE;
								feeUid = data[rowCount].FEE_UID;
								feeId = data[rowCount].FEE_ID;
								feeDtno = data[rowCount].FEE_DTNO;
								fee_lvl_type = data[rowCount].FEE_LVL_TYPE;
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
								data[rowCount].BTN = '<button type="button" id="edit_' + feeUid + '" onclick="edit_p( \''+feeUid+'\',\''+feeType+'\', \'' +feeDtno+ '\')"><img src="./images/edit.png"/></button>';
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
			}
			function initGrid(){
				$("#bs_table").jqGrid(gridOption);
			}
			function getLVLData(str){
				$("#bs_table").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=fee_code_nwlvl_bo&method=search_toJson_LVL&feeUId="+str;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str)){
					resetSortname(newOption , 'FEE_UID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'FEE_UID' , 'ASC' , true);
				}
				$("#bs_table").jqGrid(newOption);
			}
			function checkDataEnd(str){
				var rdata = {component:"fee_code_nwlvl_bo", method:"checkDataEnd" ,feeUid:str};
				var vResult = fstop.getServerDataExII(uri,rdata,false);
// 				console.log("vResult >>> "+vResult);
				if(fstop.isNotEmpty(vResult)){
// 					TRUE為已有END為9999999999999 隱藏addmore按鈕
					if(vResult.result == "TRUE"){
						$("#addmore").hide();
						return true;
					}else{
						return false;
					}
				}
			}
			
			function edit_p(feeUid,feeType,feeDtno){
				blockUI();
				$("#formID").validationEngine('detach');
				var tmp ={};
				tmp['FEE_UID'] = feeUid;
				tmp['FEE_TYPE'] = feeType;
				tmp['FEE_DTNO'] = feeDtno;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit_lvl');
// 				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function fee_type_change(value){
				// A固定 B外加 C百分比 D級距 1固定 2百分比
				if(value==='A'){
					$('.addpercent').text("");
					$('.LVLsection').hide();
					$("#sptr").attr("colspan","3");
					$('#typeBnecessary').removeClass("necessary");
					$('#HANDLECHARGE').removeClass("validate[required,decimal[7,2]]");
				}
				if(value==='B'){
					$('.addpercent').text("");
					$('.LVLsection').hide();
					$("#sptr").attr("colspan","3");
					$('#typeBnecessary').addClass("necessary");
					$('#HANDLECHARGE').addClass("validate[required,decimal[7,2]]");
				}
				if(value==='C'){
					$('.addpercent').text("%");
					$('.LVLsection').hide();
					$("#sptr").attr("colspan","3");
					$('#typeBnecessary').removeClass("necessary");
					$('#HANDLECHARGE').removeClass("validate[required,decimal[7,2]]");
				}
				if(value==='D'){
					$('.LVLsection').show();
					$('.addpercent').text("");
					$("#sptr").attr("colspan","1");
					$('#typeBnecessary').removeClass("necessary");
					$('#HANDLECHARGE').removeClass("validate[required,decimal[7,2]]");
				}
				if(value==='1'){
					$('.addpercent').text("");
				}
				if(value==='2'){
					$('.addpercent').text("%");
				}
			}
			
		</script>
	</body>
</html>
