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
				<html:form styleId="formID" action="/txnlog">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="date_type_checked" styleId="date_type_checked" />
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<!-- 票交端 -->
								<tr>	
									<td class="header">
										<input type="radio" id="date_type_txdate" name="date_type" value="TXDATE" checked/>交易日期
									</td>
									<td>
										<html:text styleId="STXDATE" property="STXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate ,twPast[#TXTIME2]] text-input datepicker"></html:text>
										~<html:text styleId="ETXDATE" property="ETXDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>														
									<td class="header">代理業者統一編號</td>
									<td>
									<logic:equal name="userData" property="USER_TYPE" value="A">
									<html:select styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" onchange = "getSnd_Com_List()" >
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "txnlog_form" property="company_IdList" label="label" value="value"/>
									</html:select>
									</logic:equal>
									<!-- 發動者端  -->
									<logic:notEqual name="userData" property="USER_TYPE" value="A">
										<bean:write name="txnlog_form" property="AGENT_COMPANY_NAME"/>
										<html:hidden styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" ></html:hidden>
									</logic:notEqual>
<%-- 										<html:text styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									 --%>
									</td>
								</tr>
								<tr>
									<td class="header">
										<input type="radio" id="date_type_bizdate" name="date_type" value="BIZDATE"/>營業日
									</td>
									<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
									<td>
										<html:text styleId="SBIZDATE" property="SBIZDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate ,twPast[#TXTIME2]] text-input datepicker"></html:text>
										~<html:text styleId="EBIZDATE" property="EBIZDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
									</td>
									</logic:equal>
									<logic:notEqual name="login_form" property="userData.USER_TYPE" value="A">
									<td>
										<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[maxSize[8],minSize[8],twDate ,twPast[#TXTIME2]] text-input datepicker"></html:text>
									</td>
									</logic:notEqual>
									<td class="header">發動者統一編號</td>
									<td>
									<html:select styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" >
										<html:option value="">全部</html:option>
									</html:select>
<%-- 										<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									 --%>
									</td>
								</tr>
								<tr>
									<td class="header">代理業者交易序號</td>
									<td>
										<html:text styleId="SEQ" property="SEQ" size="7" maxlength="7" styleClass="validate[notChinese]"></html:text>									
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
									<td class="header">系統追蹤序號</td>
									<td>
										<html:text styleId="STAN" property="STAN" size="14" maxlength="10" styleClass="validate[notChinese]"></html:text>									
									</td>
									<td class="header">交易代號</td>
									<td>
										<html:select styleId="TXN_ID" property="TXN_ID" >
											<html:option value="">全部</html:option>
											<html:optionsCollection name = "txnlog_form" property="txn_IdList" label="label" value="value"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">
										<html:radio styleId="radio_INACCTNO" property="opAction1" value="IN"></html:radio><label for="radio_INACCTNO">入帳</label>
										<html:radio styleId="radio_OUTACCTNO" property="opAction1" value="OUT"></html:radio><label for="radio_OUTACCTNO">扣款</label>&nbsp;帳號
									</td>
									<td><html:text styleId="ACCOUNTNUM" property="ACCOUNTNUM"  size="20" maxlength="16"></html:text></td>
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
									<td class="header">收費業者統編</td>
									<td>
										<html:text styleId="TOLLID" property="TOLLID" size="10" maxlength="10"></html:text>
									</td>
									<td class="header">繳費類別</td>
									<td>
										<html:select styleId="PFCLASS" property="PFCLASS">
											<html:option value="">全部</html:option>
											<html:optionsCollection name = "txnlog_form" property="pfclassList" label="label" value="value"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">金額</td>
									<td>
<%-- 										<html:text styleId="TXAMT" property="TXAMT" size="13" maxlength="13"></html:text> --%>
										<html:text styleId="TRANSACTIONAMOUNT" property="TRANSACTIONAMOUNT" size="13" maxlength="13"></html:text>
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
									<td class="header">是否包含異常資料</td>
									<td>
										<html:checkbox styleId="GARBAGEDATA" property="GARBAGEDATA"></html:checkbox>
									</td>
									<td class="header">交易結果</td>
									<td>
									<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
										<html:select styleId="RESULTSTATUS" property="RESULTSTATUS" >
											<html:option value="">全部</html:option>
											<html:option value="A">成功</html:option>
											<html:option value="R">失敗</html:option>
											<html:option value="W">處理中</html:option>
											<html:option value="P">未完成</html:option>
											<html:option value="U">與中心不一致</html:option>										
										</html:select>
									</logic:equal>
									<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="A">
										<html:select styleId="RESULTSTATUS" property="RESULTSTATUS" >
											<html:option value="">全部</html:option>
											<html:option value="A">成功</html:option>
											<html:option value="R">失敗</html:option>
											<html:option value="W">處理中</html:option>
										</html:select>
									</logic:notEqual>
									</td>
								</tr>
								<tr>
									<td><label class="btn" id="search" onclick="onPut(this.id)"><img
											src="./images/search.png" />&nbsp;查詢</label></td>
									<td></td>
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
$(document).ready(function () {
	blockUI();
	init();
	unblockUI();
});

function init(){
	var ac_key = '<bean:write name="txnlog_form" property="ac_key"/>';
	alterMsg();
	setDatePicker();
	initDateType();
// 	$("#TXDATE").val('<bean:write name="txnlog_form" property="TXDATE"/>');
// 	<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
// 	setDatePickerII($("#SBIZDATE") , '<bean:write name="txnlog_form" property="SBIZDATE"/>');
// 	setDatePickerII($("#EBIZDATE") , '<bean:write name="txnlog_form" property="EBIZDATE"/>');
// 	</logic:equal>
// 	<logic:notEqual name="userData" property="USER_TYPE" value="A">
// 		setDatePickerII($("#BIZDATE") , '<bean:write name="txnlog_form" property="BIZDATE"/>');
// 		getSnd_Com_List();
// 	</logic:notEqual>
	$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
	initGridOption();
	$("#resultData").jqGrid(gridOption);
	$("#dataSum").jqGrid(gridOption2);
	if(fstop.isNotEmptyString(ac_key) ){
		onPut('back');
	}
}

function initDateType(){
	var date_type_checked = '<bean:write name="txnlog_form" property="date_type_checked"/>';
	if(date_type_checked == "BIZDATE"){
		$("#date_type_bizdate").prop("checked", true);
	}
	if($("input[name=date_type]:checked").val() == "BIZDATE"){
		<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
			setDatePickerII($("#SBIZDATE") , '<bean:write name="txnlog_form" property="SBIZDATE"/>');
			setDatePickerII($("#EBIZDATE") , '<bean:write name="txnlog_form" property="EBIZDATE"/>');
		</logic:equal>
		<logic:notEqual name="userData" property="USER_TYPE" value="A">
			setDatePickerII($("#BIZDATE") , '<bean:write name="txnlog_form" property="BIZDATE"/>');
			getSnd_Com_List();
		</logic:notEqual>
		$("#STXDATE").val('');
		$("#ETXDATE").val('');
	}else if($("input[name=date_type]:checked").val() == "TXDATE"){
		$("#STXDATE").val('<bean:write name="txnlog_form" property="STXDATE"/>');
		$("#ETXDATE").val('<bean:write name="txnlog_form" property="ETXDATE"/>');
		$("#SBIZDATE").val('');
		$("#EBIZDATE").val('');
	}
}

$("input[name=date_type]").click(function(){
	if($("input[name=date_type]:checked").val() == "BIZDATE"){
		<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
		setDatePickerII($("#SBIZDATE") , '<bean:write name="txnlog_form" property="SBIZDATE"/>');
		setDatePickerII($("#EBIZDATE") , '<bean:write name="txnlog_form" property="EBIZDATE"/>');
		</logic:equal>
		$("#STXDATE").val('');
		$("#ETXDATE").val('');
	}else if($("input[name=date_type]:checked").val() == "TXDATE"){
		$("#STXDATE").val('<bean:write name="txnlog_form" property="STXDATE"/>');
		$("#ETXDATE").val('<bean:write name="txnlog_form" property="ETXDATE"/>');
		$("#SBIZDATE").val('');
		$("#EBIZDATE").val('');
	}
	$("#date_type_checked").val($("input[name=date_type]:checked").val());
});

function alterMsg(){
	var msg = '<bean:write name="txnlog_form" property="msg"/>';
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
        	sortname: 'TRANSACTIONDATETIME',
        	sortorder: 'asc',
        	sorttype:"text",
        	shrinkToFit: true,
        	rowNum: 10,
        	page: 1,
        	//loadonce: true,
			colNames:['檢視明細','交易日期時間','代理業者交易序號','系統追蹤序號', '代理業者統編/簡稱' ,'發動者統編/簡稱', '扣款行/扣款帳號', '入帳行/入帳帳號', '交易類別/交易代號','繳費類別','收費業者','交易金額', '交易結果'],
        	colModel: [
// '發動者統編/簡稱'
//    COMPANY_ID,TXID,PCODE,,TXN_NAME,PCODE_NAME,INBANKID,OUTBANKID,OUTACCOUNTNUM,OUTACCOUNTNUM,INBANK_NAME,OUTBANK_NAME,COMPANY_ABBR_NAME,SND_COMPANY_ID,SND_COMPANY_ABBR_NAME    
				{name:'BTN',index:'BTN',align:'',fixed:true,width: 70,sortable:false}, 
				{name:'TMP_TXDATE',index:'TRANSACTIONDATETIME',align:'center',fixed:true,width: 150},
				{name:'SEQ',index:'STAN',fixed:true,width: 100},
				{name:'STAN',index:'STAN',fixed:true,width: 100},
				{name:'COMPANY_ID',index:'COMPANY_ID',fixed:true,width: 200 , formatter :jq_brLine},
				{name:'SND_COMPANY_ID',index:'SND_COMPANY_ID',fixed:true,width: 200 , formatter :jq_brLine},
				{name:'OUTBANK_NAME',index:'OUTBANKID',fixed:true,width: 200 , formatter :jq_brLine},
				{name:'INBANK_NAME',index:'INBANKID',fixed:true,width: 200 , formatter :jq_brLine},
				{name:'PCODE',index:'PCODE',fixed:true,width: 150 ,formatter :jq_brLine},
				{name:'PFCLASS',index:'PFCLASS',fixed:true,width: 150},
				{name:'TOLLID',index:'TOLLID',fixed:true,width: 150},
				{name:'TRANSACTIONAMOUNT',index:'TRANSACTIONAMOUNT',fixed:true,width: 120,align: 'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
				{name:'RESULTSTATUS',index:'RESULTSTATUS',fixed:true,width: 65}							
			],
        	gridComplete:function (){
            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
				$("#resultData .jqgrow:even").addClass('resultDataRowEven');
			},
			loadComplete: function(data){
		        //在gridComplete event後發動，將目前所在頁數存到pageForSort
				get_curPage(this ,data.page , null , null );
				noDataEvent(data);
	        	//如果有加總資料的話，就要代到下面的GRID
		        if(data.dataSumList != undefined){
		        	$("#dataSum").jqGrid('delRowData',1);
		        	var sumData = {RECORDS:data.records,TXAMT:data.dataSumList[0].TXAMT};
					$("#dataSum").jqGrid('addRowData',1,sumData);
		        }
		    },
		    onSortCol: function(index,iCol,sortorder){
		    	//按下欄位的排序事件
		    	$("#sortname").val(index);
			    $("#sortorder").val(sortorder);
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
				var txdate = "",stan="" , seq , company_id;							
				var list = data.rows;
					for(var rowCount in list){ 								
						txdate = list[rowCount].TXDATE;
// 						stan = list[rowCount].STAN; 	
						company_id = list[rowCount].COMPANY_ID; 	
						seq = list[rowCount].SEQ; 
						list[rowCount].BTN = '<button type="button" id="edit_' + txdate+ '" onclick="edit_p(this.id, \''+txdate+'\' , \''+company_id+'\' , \''+seq+'\')"><img src="./images/edit.png"/></button>';
						//list[rowCount].TXDT = dateFormatter(list[rowCount].TXDT.substring(0,8), "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(list[rowCount].TXDT.substring(8,14), "hhmmss", "hh:mm:ss");
					}
			},
				loadtext: "處理中...",
//				20150530 edit by hugo req ACH 初始化要頁數要 1/1
//				pgtext: "{0} / {1}"
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




function onPut(str){
	getSearch_condition('search_tab' , 'input , select , radio , checkbox' , 'serchStrs');
	 	searchData(str);
}	

function edit_p(str,txdate,company_id , seq){
	$("#pageForSort").val($("#resultData").jqGrid('getGridParam', 'page'));
	var tmp = {};
	tmp['TXDATE']= txdate;
	tmp['ISSUERID']= company_id;
	tmp['SEQ']= seq;
	$("#edit_params").val(JSON.stringify(tmp));
	if(window.console){console.log($("#edit_params").val());}
	$("#ac_key").val('edit');
	$("#target").val('edit_p');
	$("form").submit();
}

function searchData(str){
	$("#resultData").jqGrid('GridUnload');
	var newOption = gridOption;
	var qStr = "component=txnlog_bo&method=pageSearch&"+$("#formID").serialize();;
	newOption.url = "/eACH/baseInfo?"+qStr;
	newOption.datatype = "json";
	newOption.mtype = 'POST';
//		20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
	newOption.pgtext= "{0} / {1}";
	if(window.console){console.log("pageForSort>>"+'<bean:write name="txnlog_form" property="pageForSort"/>');}
	if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
		newOption.page = parseInt('<bean:write name="txnlog_form" property="pageForSort"/>');
		resetSortname(newOption , 'TRANSACTIONDATETIME' , 'ASC' , false);
	}else{
		resetSortname(newOption , 'TRANSACTIONDATETIME' , 'ASC' , true);
		newOption.page = 1;
		$("#pageForSort").val(1);
	}
	$("#resultData").jqGrid(newOption);
}


function getSnd_Com_List(){
	var com_id = "";
	com_id = $("#AGENT_COMPANY_ID").val();
	if(com_id == '' || com_id == "all"){
		$("#SND_COMPANY_ID option:not(:first-child)").remove();
	}else{
		var rdata = {component:"txnlog_bo", method:"getSnd_Com_List", COMPANY_ID:com_id };
		fstop.getServerDataExII(uri, rdata, false, creatSnd_Com_List);
	}
}

function creatSnd_Com_List(obj){
	var select = $("#SND_COMPANY_ID");
	$("#SND_COMPANY_ID option:not(:first-child)").remove();
	if(obj.result=="TRUE"){
		var dataAry =  obj.msg;
		for(var i = 0; i < dataAry.length; i++){
			select.append($("<option></option>").attr("value", dataAry[i].SND_COMPANY_ID).text(dataAry[i].SND_COMPANY_ID + " - " + dataAry[i].SND_COMPANY_ABBR_NAME));
		}
	}else if(obj.result=="FALSE"){
		alert(obj.msg);
	}
}


function jq_brLine(cellvalue, options, rowObject){
	if(window.console){console.log("cellvalue>>"+cellvalue);}
	if(window.console){console.log("rowObject>>"+rowObject);}
	if(window.console){console.log("rowObject>>"+rowObject.name);}
	if(window.console){console.log("options>>"+options);}
	if(window.console){console.log("options>>"+options.colModel);}
	if(window.console){console.log("options>>"+options.colModel.name);}
// 	var ary = options.colModel;
// 	for(var i in ary ){
		
// 	if(window.console){console.log("i>>"+i);}
// 	if(window.console){console.log("ary>>"+ary[i]);}
// 	}
	var rtnStr = "";
	rtnStr = cellvalue.replace("-","<br>");
	if(options.colModel.name == "SND_COMPANY_ID"){
		rtnStr = rowObject.SND_COMPANY_ID+"<br>"+rowObject.SND_COMPANY_ABBR_NAME;
		return rtnStr;
	};
	if(options.colModel.name == "COMPANY_ID"){
		rtnStr = rowObject.COMPANY_ID+"<br>"+rowObject.COMPANY_ABBR_NAME;
		return rtnStr;
	};
	if(options.colModel.name == "PCODE"){
		rtnStr = rowObject.PCODE+"-"+rowObject.PCODE_NAME+"<br>"+rowObject.TXID+"-"+rowObject.TXN_NAME;
		return rtnStr;
	};
	if(options.colModel.name == "OUTBANK_NAME"){
		rtnStr = rowObject.OUTBANK_NAME+"<br>"+rowObject.OUTACCOUNTNUM;
		return rtnStr;
	};
	if(options.colModel.name == "INBANK_NAME"){
		rtnStr = rowObject.INBANK_NAME+"<br>"+rowObject.INACCOUNTNUM;
		return rtnStr;
	};
	//cellvalue - 当前cell的值  
	//options - 该cell的options设置，包括{rowId, colModel,pos,gid}  
	//rowObject - 当前cell所在row的值，如{ id=1, name="name1", price=123.1, ...} 
	return rtnStr;
} 
function jq_linkLine(cellvalue, options, rowObject){
	var rtnStr = "";
	rtnStr = cellvalue;
	if(options.colModel.name == "SND_COMPANY_ID"){
		rtnStr = rowObject.SND_COMPANY_ID+"<br>"+rowObject.SND_COMPANY_ABBR_NAME;
		return rtnStr;
	};
	return rtnStr;
} 
</script>


</body>
</html>