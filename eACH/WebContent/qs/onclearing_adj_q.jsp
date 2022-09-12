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
	<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
	<script type="text/javascript" src="./js/script.js"></script>
	<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
	<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
	<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
	<script type="text/javascript" src="./js/fstop.js"></script>
	<style type="text/css">
	#countdownBlock{position:relative;float:right;top:-27px;background-color: #ECF1F6;padding:0px 5px}
	</style>
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
			<html:form styleId="formID" action="/onclearing_adj">
				<html:hidden property="ac_key" styleId="ac_key" />
				<html:hidden property="target" styleId="target" />
				<html:hidden property="serchStrs" styleId="serchStrs" />
				<html:hidden property="edit_params" styleId="edit_params" />
			<fieldset>
					<legend>查詢條件</legend>
					<table id="search_tab">
						<tr>
							<td class="header necessary" style="width: 100px">營業日期</td>
							<td>
								<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,notChinese] text-input datepicker"></html:text>
							</td>
							<td class="header" style="width: 100px">清算階段</td>
							<td>
								<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
									<html:option value="">全部</html:option> 
									<html:option value="01">01</html:option> 
									<html:option value="02">02</html:option> 
								</html:select>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="padding-top: 5px">
								<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
							</td>
						</tr>
					</table>
			</fieldset>
			</html:form>
		</div>
				
		<div id="rsPanel" style="margin-top: 10px">
			<fieldset>
				<legend>查詢結果</legend>
				<div id = "countdownBlock">
						<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
							<label class="btn" id="exeupdate" onclick="exeupdate(this.id)"><img src="./images/edit.png"/>&nbsp;更新連線結算檔</label>
						</logic:equal>
				</div>
				<br>
				<table id="resultData">
				<tr><td colspan="5"><div id="emptyMsgDiv">No URLs have been loaded for evaluation.</div><td></tr>
				</table>
			</fieldset>
		</div>
	</div>
		
	</div>
</body>
<script type="text/javascript">
var grid = "";
var uri = "${pageContext.request.contextPath}"+"/baseInfo";
$(document).ready(function () {
	blockUI();
	init();
	unblockUI();
});


function init(){
	setDatePickerII($("#BIZDATE"),'<bean:write name="onclearing_adj_form" property="BIZDATE"/>');
	initGrid();
	groupGridHeaders();
	reload();
}

function groupGridHeaders(){
	$("#resultData").jqGrid('setGroupHeaders', {
		  useColSpanStyle: true, 
		  groupHeaders:[
			{startColumnName: 'CLCNT', numberOfColumns: 4, titleText: '連線結算檔'},
			{startColumnName: 'BHCNT', numberOfColumns: 4, titleText: '批次結算比對檔'}
		  ]
	});
}
function initGrid(){
	gridOption = {
			//避免jqGrid自發性的submit
			datatype: 'local',
// 			toppager: true,
// 			pagerpos: "left",
			autowidth:true,
        	height: 240,
        	shrinkToFit: true,
        	sortable: true,
			sortname: 'BANKID',
			sorttype: 'text',
			//gridview: true,
			loadonce: true,
        	rowNum: 10000,
        	hiddengrid: true,
        	emptyrecords:'查無資料',
        	viewrecords:  true,
        	colNames:['檢視明細','銀行代號','清算階段','應收付總筆數','應收付差額','手續費應收付總筆數','手續費應收付差額','應收付總筆數','應收付差額','手續費應收付總筆數','手續費應收付差額'],
        	colModel: [
        	    {name:'BTN', fixed: true, align: 'center', width:80, sortable: false},
				{name:'BANKNAME', fixed: true, width:180},
				{name:'CLEARINGPHASE', fixed: true, width:80},
				{name:'CLCNT', fixed: true, width:120 ,align : 'right'},
				{name:'CLDIFAMT', fixed: true, width:100 ,align : 'right',sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
				{name:'CLFEECNT', fixed: true, width:140 ,align : 'right'},
				{name:'CLFEEDIFAMT', fixed: true, width:120 ,align : 'right',sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}},
				{name:'BHCNT', fixed: true, width:120 ,align : 'right'},
				{name:'BALANCEAMT', fixed: true, width:100 ,align : 'right',sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
				{name:'BHFEECNT', fixed: true, width:140 ,align : 'right'},
				{name:'BALANCEFFAMT', fixed: true, width:120 ,align : 'right',sorttype: 'int',formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2}}
			],
			beforeSelectRow: function(rowid, e) {
			    return false;
			},
			rowattr: function (rd) {
			    if (rd.CLCNT != rd.BHCNT || rd.CLDIFAMT != rd.BALANCEAMT || rd.CLFEECNT != rd.BHFEECNT || rd.CLFEEDIFAMT != rd.BALANCEFFAMT ) {
			        return {"class": "resultDataRowError"};
			    }
			},
			gridComplete: function(data){
				$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
				$("#resultData .jqgrow:even").addClass('resultDataRowEven');
			},
			beforeProcessing: function(data, status, xhr){
				var bankid ="";bizdate="";clearingphase =""; 
				for(var rowCount in data){
					bankid = data[rowCount].BANKID;
					bizdate = data[rowCount].BIZDATE;
					clearingphase = data[rowCount].CLEARINGPHASE;
					data[rowCount].BTN = '<button type="button" id="edit_' + bankid + '" onclick="edit_p(this.id , \''+bizdate+'\' , \''+clearingphase+'\' , \''+bankid+'\' )"><img src="./images/edit.png"/></button>';
				};
			},

			loadtext: "處理中..."
	};
	$("#resultData").jqGrid(gridOption);
}

function edit_p(str , id , id2 ,id3){
	var tmp={};
	tmp['BIZDATE'] = id;
	tmp['CLEARINGPHASE'] = id2;
	tmp['BANKID'] = id3;
	$("#edit_params").val(JSON.stringify(tmp));
	$("#ac_key").val('edit');
	$("#target").val('edit_p');
	$("form").submit();
}

function onPut(str){
	if(!jQuery('#formID').validationEngine('validate')){
		return false;
	}
	searchData();
}	


function searchData(){
	getSearch_condition('search_tab', 'input, select', 'serchStrs');
	blockUI();
	$("#resultData").jqGrid('GridUnload');
	var newOption = gridOption;
	var qStr = "component=onclearing_adj_bo&method=search_toJson&"+$("#formID").serialize();
	newOption.url = "/eACH/baseInfo?"+qStr;
	newOption.datatype = "json";
	newOption.mtype = 'POST';
	newOption.ajaxRowOptions={ async: false };
//		 ajaxRowOptions: { async: true }
	if(window.console)console.log("url>>"+newOption.url);
	$("#resultData").jqGrid(newOption);
	groupGridHeaders();
	$("#pager div.ui-paging-info").show();
	unblockUI();
}

function reload(){
	var ackey = '<bean:write name="onclearing_adj_form" property="ac_key"/>';
	if(ackey =='back' || ackey =='update'){
		onPut('');
	}
}

function exeupdate(str){
	if(confirm("確定執行更新?")){
		var rdata = {component:"onclearing_adj_bo", method:"exeUpdate" ,BIZDATE:$("#BIZDATE").val() , CLEARINGPHASE:$("#CLEARINGPHASE").val() ,action:$("#formID").attr('action') };
		var vResult = fstop.getServerDataExII(uri,rdata,false);
		if(fstop.isNotEmpty(vResult)){
				alert(vResult.msg);
				onPut('');
		}else{
			if(window.console){console.log("系統異常");}
				alert("系統異常");
		}
	}
}

</script>

</html>