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
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
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
				<html:form styleId="formID" action="/batdata">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="sourcePage" styleId="sourcePage" value="batdata"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端 -->
							<logic:equal name="userData" property="USER_TYPE" value="A">
								<tr>
									<td class="header" style="width: 20%">營業日</td>
									<td style="width: 25%">
										<html:text styleId="TXTIME1" property="TXTIME1" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,twPast[#TXTIME2]] text-input datepicker"></html:text>
										~<html:text styleId="TXTIME2" property="TXTIME2" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 15%">檔名</td>
									<td>
										<html:text styleId="FLBATCHSEQ" property="FLBATCHSEQ" size="20" maxlength="15" styleClass="validate[minSize[15],notChinese] text-input"></html:text>
										<small>檔名格式：操作行代號(3)-民國日期(8)序號(3)</small>
									</td>
								</tr>
								<tr>
									<td class="header">系統追蹤序號</td>
									<td>
										<html:text styleId="STAN" property="STAN" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									
									</td>
									<td class="header">操作行代號</td>
									<td>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value);getBrbk_List($('#BGBK_ID').val())">
											<html:option value="all">全部</html:option>
											<html:optionsCollection name="onblocktab_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</html:select>	
									</td>
								</tr>
								<tr>
									<td class="header">
										<html:radio property="CDNUMRAO" styleId="CDNUMRAO_1" value="SENDID"><label for="CDNUMRAO_1">發動</label></html:radio>
										<html:radio property="CDNUMRAO" styleId="CDNUMRAO_2" value="RECVID"><label for="CDNUMRAO_2">收受</label></html:radio>&nbsp;統編
									</td>
									<td><html:text styleId="CARDNUM_ID" property="CARDNUM_ID" size="10" maxlength="10" ></html:text></td>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">
										<html:radio styleId="radio_INACCTNO" property="opAction1" value="IN"></html:radio><label for="radio_INACCTNO">入帳</label>
										<html:radio styleId="radio_OUTACCTNO" property="opAction1" value="OUT"></html:radio><label for="radio_OUTACCTNO">扣款</label>&nbsp;帳號
									</td>
									<td><html:text styleId="ACCTNO" property="ACCTNO" size="20" maxlength="16"></html:text></td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option value="all">全部</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">金額</td>
									<td>
										<html:text styleId="TXAMT" property="TXAMT" size="13" maxlength="13"></html:text>
									</td>
									<td class="header">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="">全部</html:option>
										<html:option value="01">1</html:option>
										<html:option value="02">2</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">是否包含異常資料</td>
									<td>
										<html:checkbox styleId="GARBAGEDATA" property="GARBAGEDATA"></html:checkbox>
									</td>
									<td class="header">業務類別</td>
									<td>
										<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID" >
											<html:option value="">全部</html:option>
											<html:optionsCollection name="onblocktab_form" property="bsTypeList" label="label" value="value"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									</td>
									<td>&nbsp;</td>
									<td class="header">交易結果</td>
									<td>
										<html:select styleId="RESULTSTATUS" property="RESULTSTATUS" >
											<html:option value="">全部</html:option>
											<html:option value="A">成功</html:option>
											<html:option value="R">失敗</html:option>
											<html:option value="P">未完成</html:option>
											<html:option value="AP">成功及未完成</html:option>
											<html:option value="U">處理中</html:option>										
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<!-- 銀行端 -->
							<logic:equal name="userData" property="USER_TYPE" value="B">
								<tr>															
									<td class="header necessary" style="width: 20%">營業日</td>
									<td style="width: 20%">
										<html:text styleId="TXTIME1" property="TXTIME1" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[#TXTIME2]] text-input datepicker"></html:text>
										<html:hidden styleId="TXTIME2" property="TXTIME2" value=""/>
									</td>
									<td class="header" style="width: 15%">檔名</td>
									<td>
										<html:text styleId="FLBATCHSEQ" property="FLBATCHSEQ" size="20" maxlength="15" styleClass="validate[minSize[15],notChinese] text-input"></html:text>
										<small>檔名格式：操作行代號(3)-民國日期(8)序號(3)</small>
									</td>
								</tr>
								<tr>
									<td class="header">系統追蹤序號</td>
									<td>
										<html:text styleId="STAN" property="STAN" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									
									</td>
									<td class="header">總行代號</td>
									<td>
										<html:hidden styleId="OPBK_ID" property="OPBK_ID"></html:hidden>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">
										<html:radio property="CDNUMRAO" styleId="CDNUMRAO_1" value="SENDID"><label for="CDNUMRAO_1">發動</label></html:radio>
										<html:radio property="CDNUMRAO" styleId="CDNUMRAO_2" value="RECVID"><label for="CDNUMRAO_2">收受</label></html:radio>&nbsp;統編
									</td>
									<td><html:text styleId="CARDNUM_ID" property="CARDNUM_ID"  size="10" maxlength="10"></html:text></td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option value="all">全部</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">
										<html:radio styleId="radio_INACCTNO" property="opAction1" value="IN"></html:radio><label for="radio_INACCTNO">入帳</label>
										<html:radio styleId="radio_OUTACCTNO" property="opAction1" value="OUT"></html:radio><label for="radio_OUTACCTNO">扣款</label>&nbsp;帳號
									</td>
									<td><html:text styleId="ACCTNO" property="ACCTNO"  size="20" maxlength="16"></html:text></td>
									<td class="header">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="">全部</html:option>
											<html:option value="01">1</html:option>
											<html:option value="02">2</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">金額</td>
									<td>
										<html:text styleId="TXAMT" property="TXAMT"  size="13" maxlength="13"></html:text>
									</td>
									<td class="header">業務類別</td>
									<td>
										<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID" >
											<html:option value="">全部</html:option>
											<html:optionsCollection name="onblocktab_form" property="bsTypeList" label="label" value="value"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td style="left-padding: 10px">
										<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									</td>
									<td>&nbsp;</td>
									<td class="header">交易結果</td>
									<td>
										<html:select styleId="RESULTSTATUS" property="RESULTSTATUS" >
											<html:option value="">全部</html:option>
											<html:option value="A">成功</html:option>
											<html:option value="R">失敗</html:option>
											<html:option value="P">未完成</html:option>				
											<html:option value="U">處理中</html:option>						
										</html:select>
									</td>
								</tr>
							</logic:equal>
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
			<div id="rsPanel">
				<fieldset>
					<legend>總計</legend>
					<table id="dataSum"></table>
				</fieldset>
			</div>
			<br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var gridOption2;
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var formSerialize = "";
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
        	});
			
			function init(){
				alterMsg();
				setDatePicker();
				setDateOnChange($("#TXTIME1"), getBgbk_List);
				setDateOnChange($("#TXTIME2"), getBgbk_List);
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				
				if('<bean:write name="onblocktab_form" property="TXTIME1"/>' == "" &&
				   '<bean:write name="onblocktab_form" property="TXTIME2"/>' == "" &&
				   $("#ac_key").val() != "back"){
					$("[id^=TXTIME]").datepicker("setDate", new Date());
					$("[id^=TXTIME]").val('0' + $("[id^=TXTIME]").val());
				}else{
					var txtime1 = '<bean:write name="onblocktab_form" property="TXTIME1"/>';
					var txtime2 = '<bean:write name="onblocktab_form" property="TXTIME2"/>';
					$("#TXTIME1").val(txtime1);
					$("#TXTIME2").val(txtime2);
				}
				
				getBgbk_List($("#OPBK_ID").val());
				getBrbk_List($("#BGBK_ID").val());
				
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				
				if($("#ac_key").val() == "back"){
					$("#BGBK_ID").val('<bean:write name="onblocktab_form" property="BGBK_ID"/>');
					$("#BGBK_ID").change();
					$("#BRBK_ID").val('<bean:write name="onblocktab_form" property="BRBK_ID"/>');
					$("#BRBK_ID").change();
					onPut('back');
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="onblocktab_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function getBgbk_List(opbkId){
				var s_bizdate = $("#TXTIME1").val();
				var e_bizdate = $("#TXTIME2").val();
				<logic:equal name="userData" property="USER_TYPE" value="B">
					e_bizdate = "";
				</logic:equal>
				opbkId = $("#OPBK_ID").val();
				if(opbkId == '' || opbkId == "all"){
					$("#BGBK_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate , e_bizdate:e_bizdate};
					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
				}
			}
			
			function creatBgBkList(obj){
				var select = $("#BGBK_ID");
				$("#BGBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].BGBK_ID).text(dataAry[i].BGBK_ID + " - " + dataAry[i].BGBK_NAME));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
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
				$("#BRBK_ID option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var a in dataAry){
						select.append($("<option></option>").attr("value", dataAry[a].value).text(dataAry[a].label));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 360,
		            	sortname: 'TXDT, STAN',
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	//loadonce: true,
						colNames:['檢視明細','交易日期時間','整批處理序號(檔名)','系統追蹤序號', '發動行代號', '扣款行/扣款帳號', '入帳行/入帳帳號', '發動者統編/簡稱', '交易類別/交易代號','交易金額', '交易結果'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false}, 
							{name:'TXDT',index:'TXDT',align:'center',fixed:true,width: 150},
							{name:'FLBATCHSEQ',index:'FLBATCHSEQ',fixed:true,width: 120},
							{name:'STAN',index:'STAN',fixed:true,width: 100},
							{name:'SENDERBANKID',index:'SENDERBANKID',fixed:true,width: 200},
							{name:'OUTBANKID',index:'OUTBANKID',fixed:true,width: 200},
							{name:'INBANKID',index:'INBANKID',fixed:true,width: 200},
							{name:'SENDERID',index:'SENDERID',fixed:true,width: 120},
							{name:'PCODE',index:'PCODE',fixed:true,width: 150},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 120,align: 'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RESP',index:'RESP',fixed:true,width: 65}							
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
				        	//如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {RECORDS:data.records,TXAMT:data.dataSumList[0].TXAMT};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
				        	
							//查詢結果無資料
							noDataEvent(data ,$("#resultData") );
							
							get_curPage(this, data.page, null, null);
					    },
					    onSortCol: function(index,iCol,sortOrder){
						    $("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var txdate = "",stan="";							
							var list = data.rows;
 							for(var rowCount in list){ 								
 								txdate = list[rowCount].TXDATE;
 								stan = list[rowCount].STAN; 								
 								list[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+stan+'\')"><img src="./images/edit.png"/></button>';
 								
 								//資料處理
 								list[rowCount].OUTBANKID = list[rowCount].OUTBANKID + "<br/>" + list[rowCount].OUTACCTNO;
 								list[rowCount].INBANKID = list[rowCount].INBANKID + "<br/>" + list[rowCount].INACCTNO;
 								list[rowCount].SENDERID = list[rowCount].SENDERID.replace(/-/g, '<br/>');
 								list[rowCount].PCODE = list[rowCount].PCODE + "<br/>" + list[rowCount].TXID;
 							}
						},
	 					loadtext: "處理中...",
	 					//20150530 edit by hugo req ACH 初始化要頁數要 1/1
	 					pgtext: "1 / 1" 
				};
				gridOption2 = {
						datatype: "local",
						autowidth:true,
		            	height: 22,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	colNames:['查詢結果總筆數','交易金額'],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}}
						]
				};
			}
			
			function onPut(id){
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#TXTIME2").val($("#TXTIME1").val());
				</logic:equal>
				if($("#formID").validationEngine("validate")){
					blockUI();
					getSearch_condition('search_tab', 'input , select', 'serchStrs');
					searchData(id);
					unblockUI();
				}
			}
			
			function searchData(str){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=batdata_bo&method=search_toJson&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					newOption.page = parseInt('<bean:write name="onblocktab_form" property="pageForSort"/>');
					resetSortname(newOption , 'TXDT' , 'ASC' , false);
				}else{
					newOption.page = 1;
					resetSortname(newOption , 'TXDT' , 'ASC' , true);
				}
				
				$("#resultData").jqGrid(newOption);
			}
				
			function edit_p(str, id, id1){
				var tmp = {};
				tmp['TXDATE'] = id;
				tmp['STAN'] = id1;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
		</script>
	</body>
</html>