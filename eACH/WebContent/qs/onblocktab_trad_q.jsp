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
		<script type="text/javascript" src="./js/jquery.cookie.js"></script>
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
				<html:form styleId="formID" action="/onblocktab">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SERNO" styleId="SERNO"/>
					<html:hidden property="TXDATE" styleId="TXDATE"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="colForSort" styleId="colForSort"/>
					<html:hidden property="ordForSort" styleId="ordForSort"/>
					<html:hidden property="searchCondition" styleId="searchCondition"/>
					<html:hidden property="sourcePage" styleId="sourcePage" value="onblocktab"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="dow_token" styleId="dow_token"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端 -->
							<logic:equal name="userData" property="USER_TYPE" value="A">
								<tr>															
									<td class="header">營業日</td>
									<td>
										<html:text styleId="TXTIME1" property="TXTIME1" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,twPast[#TXTIME2]] text-input datepicker"></html:text>
										~<html:text styleId="TXTIME2" property="TXTIME2" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate ,twDateRange[#TXTIME1,#TXTIME2,3]] text-input datepicker"></html:text>
									</td>
									<td class="header">查詢角度</td>
									<td>
										<html:select styleId="searchAspect" property="searchAspect">
											<html:option value="">全部</html:option>
											<html:option value="SENDER">發動行</html:option>
											<html:option value="OUT">扣帳行</html:option>
											<html:option value="IN">入帳行</html:option>
											<html:option value="WO">銷帳行</html:option>
										</html:select>
									</td>
									<td class="header">交易日期時間</td>
													<td>
										起<html:text styleId="TXTIME3" property="TXTIME3" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,twPast[#TXTIME3]] text-input datepicker"></html:text>
										<html:text styleId="HOUR1" property="HOUR1" size="2" maxlength="2" styleClass="validate[maxSize[2],minSize[2],twDate] text-input "></html:text>時
										<html:text styleId="MON1" property="MON1" size="2" maxlength="2" styleClass="validate[maxSize[2],minSize[2],twDate] text-input "></html:text>分
										</td>						
									</tr>
								<tr>
									<td class="header">系統追蹤序號</td>
									<td>
										<html:text styleId="STAN" property="STAN" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text> ~  
										<html:text styleId="STAN2" property="STAN2" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									
									</td>
									<td class="header">操作行代號</td>
									<td>
<%-- 										<html:select styleId="USER_COMPANY" property="USER_COMPANY" onchange="getBgbk_List(this.value);getBrbk_List($('#BGBK_ID').val())"> --%>
										<html:select styleId="OPBK_ID" property="OPBK_ID" onchange="getBgbk_List(this.value);getBrbk_List($('#BGBK_ID').val())">
											<html:option value="all">全部</html:option>
											<html:optionsCollection name="onblocktab_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</html:select>	
									</td>
									<td class="header"></td>
													<td>
										迄<html:text styleId="TXTIME4" property="TXTIME4" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text> 
										<html:text styleId="HOUR2" property="HOUR2" size="2" maxlength="2" styleClass="validate[maxSize[2],minSize[2],twDate] text-input "></html:text>時
										<html:text styleId="MON2" property="MON2" size="2" maxlength="2" styleClass="validate[maxSize[2],minSize[2],twDate] text-input "></html:text>分
										<input type="hidden" id="checkTXDT" />
										</td>
										
								</tr>
								<tr>
									<td class="header">
										<html:radio property="CDNUMRAO" styleId="CDNUMRAO_1" value="SENDID"><label for="CDNUMRAO_1">發動</label></html:radio>
										<html:radio property="CDNUMRAO" styleId="CDNUMRAO_2" value="RECVID"><label for="CDNUMRAO_2">收受</label></html:radio>&nbsp;統編
									</td>
									<td><html:text styleId="CARDNUM_ID" property="CARDNUM_ID" size="10" maxlength="10"></html:text></td>
									<td class="header">總行代號</td>
									<td>
										<html:select styleId="BGBK_ID" property="BGBK_ID" onchange="getBrbk_List(this.value)">
											<html:option value="">全部</html:option>
										</html:select>
									</td>
									<td class="header">交易代號</td>
									<td>
										<html:select styleId="TXID" property="TXID">
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "onblocktab_form" property="txnIdList" label="label" value="value"/>
									</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">收費業者統編</td>
									<td>
										<html:text styleId="TOLLID" property="TOLLID" size="10" maxlength="10"></html:text>
									</td>
									<td class="header">分行代號</td>
									<td>
										<html:select styleId="BRBK_ID" property="BRBK_ID">
											<html:option value="all">全部</html:option>
										</html:select>
									</td>
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
									<td class="header">
										<html:radio styleId="radio_INACCTNO" property="opAction1" value="IN"></html:radio><label for="radio_INACCTNO">入帳</label>
										<html:radio styleId="radio_OUTACCTNO" property="opAction1" value="OUT"></html:radio><label for="radio_OUTACCTNO">扣款</label>&nbsp;帳號
									</td>
									<td><html:text styleId="USERID" property="USERID" size="20" maxlength="16"></html:text></td>	
									<td class="header">清算階段</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="">全部</html:option>
										<html:option value="01">1</html:option>
										<html:option value="02">2</html:option>
										</html:select>
									</td>
									<td class="header">回應狀態</td>
									<td>
										<html:select styleId="RESSTATUS" property="RESSTATUS" onchange="showRCSERCTEXT()">
										<html:option value="all">全部</html:option>
										<html:option value="RC1">RC1</html:option>
										<html:option value="RC2">RC2</html:option>
										<html:option value="RC3">RC3</html:option>
										<html:option value="RC4">RC4</html:option>
										</html:select>
										<html:text styleId="RCSERCTEXT" property="RCSERCTEXT" size="20" maxlength="20"></html:text>
									</td>
								</tr>
								<tr>
									<td class="header">金額</td>
									<td>
										<html:text styleId="TXAMT" property="TXAMT" size="13" maxlength="13"></html:text>
									</td>
									<td class="header">業務類別</td>
									<td>
										<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID" >
											<html:option value="">全部</html:option>
											<html:optionsCollection name = "onblocktab_form" property="bsIdKist" label="label" value="value"/>
										</html:select>
									</td>
									<td class="RCSERCTEXTDESC">
									</td>
									<td class="RCSERCTEXTDESC">
										<span style="color: gray;">多條件查詢請以","為分隔。如：1234,2234</span>
									</td>
								</tr>
								<tr>
									<td class="header">是否包含異常資料</td>
									<td>
										<html:checkbox styleId="GARBAGEDATA" property="GARBAGEDATA"></html:checkbox>
									</td>
									<td class="header">銷帳單位</td>
									<td>
										<html:select styleId="BILLFLAG" property="BILLFLAG" >
											<html:option value="">全部</html:option>
											<html:option value="1">入帳行銷帳</html:option>
											<html:option value="2">發動行銷帳</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">是否過濾整批資料</td>
									<td>
										<html:checkbox styleId="FILTER_BAT" property="FILTER_BAT" ></html:checkbox>
									</td>
									<td class="header">繳費類別</td>
									<td>
										<html:select styleId="PFCLASS" property="PFCLASS">
											<html:option value="">全部</html:option>
											<html:optionsCollection name = "onblocktab_form" property="pfclassList" label="label" value="value"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td></td><td></td>
									<td class="header">繳費工具</td>
									<td>
										<html:select styleId="CHARGETYPE" property="CHARGETYPE" >
											<html:option value="">全部</html:option>
											<html:option value="1">約定授權</html:option>
											<html:option value="2">晶片金融卡</html:option>
											<html:option value="3">本人帳戶繳費</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
										<label class="btn" id="search_csv" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;匯出CSV檔</label>
									</td><td></td>
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
<!-- 							銀行端強制過濾整批資料(過濾有過濾的邏輯，非全部過濾，請看需求規格書) -->
							<html:hidden styleId="FILTER_BAT" property="FILTER_BAT" value="Y" ></html:hidden>
								<tr>															
									<td class="header">營業日</td>
									<td>
										<html:text styleId="TXTIME1" property="TXTIME1" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate,twPast[#TXTIME2]] text-input datepicker"></html:text>
										<html:hidden styleId="TXTIME2" property="TXTIME2" value=""/>
									</td>
									<td class="header">查詢角度</td>
									<td>
										<html:select styleId="searchAspect" property="searchAspect">
											<html:option value="">全部</html:option>
											<html:option value="SENDER">發動行</html:option>
											<html:option value="OUT">扣帳行</html:option>
											<html:option value="IN">入帳行</html:option>
											<html:option value="WO">銷帳行</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">系統追蹤序號</td>
									<td>
										<html:text styleId="STAN" property="STAN" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									
									</td>
									<td class="header">總行代號</td>
									<td>
										<!-- 操作行代號 -->
<%-- 										<html:hidden styleId="USER_COMPANY" property="USER_COMPANY"></html:hidden> --%>
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
									<td class="header">收費業者統編</td>
									<td>
										<html:text styleId="TOLLID" property="TOLLID" size="10" maxlength="10"></html:text>
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
									<td class="header">
										<html:radio styleId="radio_INACCTNO" property="opAction1" value="IN"></html:radio><label for="radio_INACCTNO">入帳</label>
										<html:radio styleId="radio_OUTACCTNO" property="opAction1" value="OUT"></html:radio><label for="radio_OUTACCTNO">扣款</label>&nbsp;帳號
									</td>
									<td><html:text styleId="USERID" property="USERID"  size="20" maxlength="16"></html:text></td>	
									<td class="header">業務類別</td>
									<td>
										<html:select styleId="BUSINESS_TYPE_ID" property="BUSINESS_TYPE_ID" >
											<html:option value="">全部</html:option>
											<html:optionsCollection name = "onblocktab_form" property="bsIdKist" label="label" value="value"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">金額</td>
									<td>
										<html:text styleId="TXAMT" property="TXAMT"  size="13" maxlength="13"></html:text>
									</td>
									<td class="header">銷帳單位</td>
									<td>
										<html:select styleId="BILLFLAG" property="BILLFLAG" >
											<html:option value="">全部</html:option>
											<html:option value="1">入帳行銷帳</html:option>
											<html:option value="2">發動行銷帳</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">繳費類別</td>
									<td>
										<html:select styleId="PFCLASS" property="PFCLASS">
											<html:option value="">全部</html:option>
											<html:optionsCollection name = "onblocktab_form" property="pfclassList" label="label" value="value"/>
										</html:select>
									</td>
									<td class="header">繳費工具</td>
									<td>
										<html:select styleId="CHARGETYPE" property="CHARGETYPE" >
											<html:option value="">全部</html:option>
											<html:option value="1">約定授權</html:option>
											<html:option value="2">晶片金融卡</html:option>
											<html:option value="3">本人帳戶繳費</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td style="left-padding: 10px">
										<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
										<label class="btn" id="search_csv" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;匯出CSV檔</label>
									</td><td></td>
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var gridOption2;
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			//處理欄位排序後停在原本的頁面的變數
			var pageForSort = 1;
			var defaultPage = '<bean:write name="onblocktab_form" property="pageForSort"/>';
			if(window.console){console.log("defaultPage>>"+defaultPage);}
			if(defaultPage.length != 0){
				pageForSort = parseInt(defaultPage);
			}
			var formSerialize = "";
			
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
        	});
			
			function init(){
				showRCSERCTEXT();
				alterMsg();
				setDatePicker();
				setDateOnChange($("#TXTIME1") ,getBgbk_List);
				setDateOnChange($("#TXTIME2") ,getBgbk_List);				
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				if('<bean:write name="onblocktab_form" property="TXTIME1"/>' == "" &&
				   '<bean:write name="onblocktab_form" property="TXTIME2"/>' == "" &&
				   $("#ac_key").val() != "back"){
					$("[id^=TXTIME]").datepicker("setDate", new Date());
					$("[id^=TXTIME]").val('0' + $("[id^=TXTIME]").val());
				}else{
					var txtime1 = '<bean:write name="onblocktab_form" property="TXTIME1"/>';
					//var txtime1_d = new Date(parseInt(txtime1.substring(0,4))+1911, parseInt(txtime1.substring(4,6))-1, parseInt(txtime1.substring(6,8)));
					var txtime2 = '<bean:write name="onblocktab_form" property="TXTIME2"/>';
					//var txtime2_d = new Date(parseInt(txtime2.substring(0,4))+1911, parseInt(txtime2.substring(4,6))-1, parseInt(txtime2.substring(6,8)));
					//$("#TXTIME1").datepicker("setDate", txtime1_d);
					//$("#TXTIME1").val('0' + $("#TXTIME1").val());
					var txtime3 = '<bean:write name="onblocktab_form" property="TXTIME3"/>';
					var txtime4 = '<bean:write name="onblocktab_form" property="TXTIME4"/>';
					$("#TXTIME1").val(txtime1);
					$("#TXTIME2").val(txtime2);
					$("#TXTIME3").val(txtime3);
					$("#TXTIME4").val(txtime4);
				}		
			
 				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				
// 				getBgbk_List($("#USER_COMPANY").val());
				getBgbk_List($("#OPBK_ID").val());
				$("#BGBK_ID").val('<bean:write name="onblocktab_form" property="BGBK_ID"/>');
				$("#BGBK_ID").change();
				getBrbk_List($("#BGBK_ID").val());
				$("#BRBK_ID").val('<bean:write name="onblocktab_form" property="BRBK_ID"/>'==''?"all":'<bean:write name="onblocktab_form" property="BRBK_ID"/>');
				
				formSerialize = $("#formID").serialize();
				
				if($("#ac_key").val() == "back"){
					if(window.console){console.log("ac>>"+$("#ac_key").val());}
					onPut('backSearch');
// 					$("#serchStrs").val('');
				}
			}
			
			function alterMsg(){
				var msg = '<bean:write name="onblocktab_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				initSortSettings(false);
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 270,
		            	sortname: $("#colForSort").val(),
		            	sortorder: $("#ordForSort").val(),
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	page: pageForSort,
		            	//loadonce: true,
						colNames:['檢視明細','交易日期時間','系統追蹤序號', '發動行代號', '扣款行/扣款帳號', '入帳行/入帳帳號', '發動者統編/簡稱', '交易類別/交易代號','繳費類別','收費業者','交易金額','交易結果','RC1','RC2','RC3','RC4','RC5','RC6'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 70,sortable:false}, 
							{name:'TXDT',index:'TXDT',align:'center',fixed:true,width: 150},
							{name:'STAN',index:'STAN',fixed:true,width: 100},
							{name:'SENDERBANKID',index:'SENDERBANKID',fixed:true,width: 200},
							{name:'OUTBANKID',index:'OUTBANKID',fixed:true,width: 200},
							{name:'INBANKID',index:'INBANKID',fixed:true,width: 200},
							{name:'SENDERID',index:'SENDERID',fixed:true,width: 120},
							{name:'PCODE',index:'PCODE',fixed:true,width: 150},
// 							{name:'TXAMT',index:'TXAMT',fixed:true,width: 120,align: 'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'PFCLASS',index:'PFCLASS',fixed:true,width: 150},
							{name:'TOLLID',index:'TOLLID',fixed:true,width: 150},
							{name:'TXAMT',index:'TXAMT',fixed:true,width: 120,align: 'right', formatter: valueAttr, formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RESP',index:'RESP',fixed:true,width: 65},
							{name:'RC1',index:'RC1',fixed:true,width: 65},
							{name:'RC2',index:'RC2',fixed:true,width: 65},
							{name:'RC3',index:'RC3',fixed:true,width: 65},
							{name:'RC4',index:'RC4',fixed:true,width: 65},
							{name:'RC5',index:'RC4',fixed:true,width: 65},	
							{name:'RC6',index:'RC4',fixed:true,width: 65}	
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
							noDataEvent(data);
					        //在gridComplete event後發動，將目前所在頁數存到pageForSort
				        	pageForSort = data.page;
				        	//如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {RECORDS:data.records,TXAMT:data.dataSumList[0].TXAMT};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
					    onSortCol: function(index,iCol,sortorder){
					    	//按下欄位的排序事件
					    	//$("#resultData").jqGrid("setGridParam",{page:pageForSort});
					    	if(window.console){console.log("onSortCol");}
// 					    	20150722 edit by hugo  req by kevin 先不紀錄排序
					    	$("#colForSort").val(index);
					    	$("#ordForSort").val(sortorder);
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
							var txdate = "",stan="",txid="",senderid="",senderbankid="",txamt="";							
							var list = data.rows;
 							for(var rowCount in list){ 								
 								txdate = list[rowCount].TXDATE;
 								stan = list[rowCount].STAN;
 								txid = list[rowCount].TXID;
 								senderid = list[rowCount].SENDERID;
 								senderbankid = list[rowCount].SENDERBANKID;
 								txamt = list[rowCount].TXAMT;
 								
 								list[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+stan+'\', \''+txid+'\', \''+senderid+'\', \''+senderbankid+'\', \''+txamt+'\')"><img src="./images/edit.png"/></button>';
 								//list[rowCount].TXDT = dateFormatter(list[rowCount].TXDT.substring(0,8), "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(list[rowCount].TXDT.substring(8,14), "hhmmss", "hh:mm:ss");
 								
 								//資料處理
 								list[rowCount].OUTBANKID = list[rowCount].OUTBANKID + "<br/>" + list[rowCount].OUTACCTNO;
 								list[rowCount].INBANKID = list[rowCount].INBANKID + "<br/>" + list[rowCount].INACCTNO;
 								list[rowCount].SENDERID = list[rowCount].SENDERID.replace(/-/g, '<br/>');
 								list[rowCount].PCODE = list[rowCount].PCODE + "<br/>" + list[rowCount].TXID;
 							}
						},
	 					loadtext: "處理中...",
// 	 					20150530 edit by hugo req ACH 初始化要頁數要 1/1
// 	 					pgtext: "{0} / {1}"
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
				function valueAttr(cellvalue, opts, rowObject) {
					if($.isNumeric(cellvalue)){
						return $.fn.fmatter.call(this, "currency", cellvalue, opts, rowObject);
					}else{
						return cellvalue;
					}
					
				};
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
						var validTitle=encodeHTML(dataAry[i].BGBK_ID);
						var validvalue=encodeHTML(dataAry[i].BGBK_NAME);
						var tmpOption=$("<option></option>");
						tmpOption.attr("value", validTitle).text(validTitle + " - " + validvalue);
						select.append(tmpOption);
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
						var validTitle=encodeHTML(dataAry[a].value);
						var validvalue=encodeHTML(dataAry[a].label);
						var tmpOption=$("<option></option>");
						tmpOption.attr("value", validTitle).text(validvalue);
						select.append(tmpOption);
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function onPut(id){
				var isNotBack = true;
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#TXTIME2").val($("#TXTIME1").val());
				</logic:equal>
				if($("#formID").validationEngine("validate")){
					if(id=='search_csv'){
						blockUIForDownload();
						$("#ac_key").val(id);
						$("#target").val('search');
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						$("form").submit();
					}else{
						blockUI();
						if(id == "backSearch"){
							initSortSettings(false);
							isNotBack = false;
						}else{
							initSortSettings(true);
						}
//	 					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						getSearch_condition('search_tab' , 'input , select , radio , checkbox' , 'serchStrs');
						searchData(isNotBack);
						unblockUI();
					}
					
				}
			}
			
			function searchData(isNotBack){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=onblocktab_bo&method=getResList&"+$("#formID").serialize();
				if(window.console){console.log("qStr>>"+qStr);}
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(window.console){console.log("IS DIFFERNET ? " + (formSerialize != $("#formID").serialize()));}
				if(window.console){console.log("IS DIFFERNET 1 " + formSerialize );}
				if(window.console){console.log("IS DIFFERNET 2 " + $("#formID").serialize());}
// 				if(formSerialize != $("#formID").serialize()){
				if(window.console){console.log("isNotBack>>"+isNotBack);}
				if(isNotBack){
					if(window.console){console.log("isNotBack");}
						newOption.page = 1;
				}else{
					if(window.console){console.log("page>>"+parseInt(defaultPage));}
					newOption.page = parseInt(defaultPage);
				}
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				if(window.console){console.log("a");}
				$("#resultData").jqGrid(newOption);
			}
			
			function initSortSettings(isSetToDefault){
				//預設排序方式
				if(window.console){console.log("isSetToDefault>>"+isSetToDefault);}
				if(window.console){console.log("colForSort.val>>"+$("#colForSort").val());}
				if(isSetToDefault || $("#colForSort").val() == ''){
// 					20150622 edit by hugo req by UAT-2015012-01
// 					$("#colForSort").val("TXDT");
					$("#colForSort").val("TXDT , STAN ");
					$("#ordForSort").val("ASC");
					if(gridOption != null){
// 						gridOption.sortname = "TXDT";
						gridOption.sortname = "TXDT , STAN";
						gridOption.sortorder = "ASC";
					}
				}
			}
			
			function edit_p(str,id,id1,id2,id3,id4,id5){
// 				$("#searchCondition").val($("#formID").serialize());
// 				$("#TXDATE").val(id);
// 				$("#STAN").val(id1);
// if(window.console){console.log("PageForSort>>"+$("#pageForSort").val());}
				$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
				var tmp = {};
				tmp['TXDATE']= id;
				tmp['STAN']= id1;
				tmp['TXID']=id2
				tmp['SENDERID']=id3
				tmp['SENDERBANKID']=id4
				tmp['TXAMT']=id5
				$("#edit_params").val(JSON.stringify(tmp));
				if(window.console){console.log($("#edit_params").val());}
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			function showRCSERCTEXT(){
				if($("#RESSTATUS").val()!='all'){
					$("#RCSERCTEXT").prop("disabled",false);
					$('#RCSERCTEXT').addClass("validate[required]");
					$('.RCSERCTEXTDESC').show();
				}else{
					$("#RCSERCTEXT").prop("disabled",true);
					$('#RCSERCTEXT').removeClass("validate[required]");
					$('#RCSERCTEXT').val("");
					$('.RCSERCTEXTDESC').hide();
				
				}
			}
			
			//==================此區塊為使用struts 輸出檔案會用到的API==================
			
			var fileDownloadCheckTimer;
			function blockUIForDownload() {
				var token = new Date().getTime(); //use the current timestamp as the token value
				$('#dow_token').val(token);
				blockUI();
				fileDownloadCheckTimer = window.setInterval(function() {
					var cookieValue = $.cookie('fileDownloadToken');
					if (cookieValue == token)
						finishDownload();
				}, 1000);
			}
			function finishDownload() {
				window.clearInterval(fileDownloadCheckTimer);
				//	 				$.removeCookie('fileDownloadToken' , null); //clears this cookie value 此方式chrome不適用
				$.cookie('fileDownloadToken', null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
			
			//=================ESAPI============================
			function encodeHTML(s) {
				return s.replace(/&/g, '').replace(/</g, '').replace(/\//g, '').replace(/\n/g,"").replace(/\r/g,"");
			}
		</script>
	</body>
</html>