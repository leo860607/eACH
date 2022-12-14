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
		<script type="text/javascript" src="./js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
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
				<html:form styleId="formID" action="/agent_fee_search">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="p_is_record" styleId="p_is_record" />
					<html:hidden property="dow_token" styleId="dow_token"/>
					<html:hidden property="sortname" styleId="sortname"/>
					<html:hidden property="sortorder" styleId="sortorder"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>????????????</legend>
						<table id="search_tab">
							<!-- ????????? -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
								<tr>
									<td class="necessary header" style="width: 16%">?????????</td>
									<td style="width: 30%">
										<html:text styleId="SBIZDATE" property="SBIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate ,twPast[#EBIZDATE]] text-input datepicker"></html:text>
										~<html:text styleId="EBIZDATE" property="EBIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate ,twFuture[#SBIZDATE]] text-input datepicker"></html:text>
									</td>
									<td class="header" style="width: 16%">????????????</td>
									<td>
										<html:select styleId="TXID" property="TXID">
											<html:option value="all">??????</html:option>
											<logic:present name="agent_fee_search_form" property="txnIdList">
												<html:optionsCollection name="agent_fee_search_form" property="txnIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">??????????????????</td>
									<td>
										<html:select styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID" onchange="getSnd_Com_List(this.value)">
											<html:option value="all">??????</html:option>
											<logic:present name="agent_fee_search_form" property="company_IdList">
												<html:optionsCollection name="agent_fee_search_form" property="company_IdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
									<td class="header">???????????????</td>
									<td>
										<html:select styleId="SND_COMPANY_ID" property="SND_COMPANY_ID">
											<html:option value="all">??????</html:option>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">????????????</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="all">??????</html:option>
											<html:option value="01">01</html:option>
											<html:option value="02">02</html:option>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<!-- ??????????????? -->
							<logic:equal name="login_form" property="userData.USER_TYPE" value="C">
								<tr>
									<td class="necessary header" style="width: 16%">?????????</td>
									<td style="width: 25%">
										<html:text styleId="SBIZDATE" property="SBIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
										<html:hidden styleId="EBIZDATE" property="EBIZDATE"></html:hidden>
									</td>
									<td class="header" style="width: 16%">????????????</td>
									<td>
										<html:select styleId="TXID" property="TXID">
											<html:option value="all">??????</html:option>
											<logic:present name="agent_fee_search_form" property="txnIdList">
												<html:optionsCollection name="agent_fee_search_form" property="txnIdList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</tr>
								<tr>
									<td class="header">???????????????</td>
									<td>
										<html:hidden styleId="AGENT_COMPANY_ID" property="AGENT_COMPANY_ID"></html:hidden>
										<html:select styleId="SND_COMPANY_ID" property="SND_COMPANY_ID">
											<html:option value="all">??????</html:option>
										</html:select>
									</td>
									<td class="header">????????????</td>
									<td>
										<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
											<html:option value="">??????</html:option>
											<html:option value="01">01</html:option>
											<html:option value="02">02</html:option>
										</html:select>
									</td>
								</tr>
							</logic:equal>
							
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;??????</label>
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="export" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;????????????</label>
									</logic:equal>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>????????????</legend>
					<table id="resultData"></table>
				</fieldset>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>??????</legend>
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
				alterMsg();
				setDatePicker();
				//??????????????????????????????
				$("#SBIZDATE").val('<bean:write name="agent_fee_search_form" property="SBIZDATE"/>');
				$("#EBIZDATE").val('<bean:write name="agent_fee_search_form" property="EBIZDATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				$("#dataSum").jqGrid(gridOption2);
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="C">
					getSnd_Com_List($("#AGENT_COMPANY_ID").val());
				</logic:equal>
			}
			
			function alterMsg(){
				var msg = '<bean:write name="agent_fee_search_form" property="msg"/>';
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
		            	height: 230,
		            	sorttype:"text",
		            	sortname:"AGENT_COMPANY_ID ,TXID ",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['??????????????????/??????','???????????????/??????','????????????','??????','??????','??????'],
		            	colModel: [
							{name:'AGENT_COMPANY_ID',index:'AGENT_COMPANY_ID',fixed:true,width: 200,formatter :jq_linkLine},
							{name:'SND_COMPANY_ID',index:'SND_COMPANY_ID',fixed:true,width: 210,formatter :jq_linkLine},
							{name:'TXN_NAME',index:'TXID',fixed:true,width: 110,sortable:true ,formatter :jq_linkLine}, 
							{name:'AGENT_FEE',index:'AGENT_FEE',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'FIRECOUNT',index:'FIRECOUNT',fixed:true,width: 65,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
							{name:'SUM_AGENT_FEE',index:'SUM_AGENT_FEE',fixed:true,width: 95,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:2}},
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
					        //???????????????????????????????????????????????????GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {RECORDS:data.records,FIRECOUNT:data.dataSumList[0].FIRECOUNT,AGENT_FEE:data.dataSumList[0].AGENT_FEE};
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
							
					        
					    },
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
						},
// 						?????????????????? e
						onSortCol:function (index, columnIndex, sortOrder){
						    $(this).setGridParam({ postData: { isSearch: 'N' } });
						},
// 						   e
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
	 					loadtext: "?????????...",
// 	 					20150530 edit by hugo req ACH ????????????????????? 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
				};
				gridOption2 = {
						datatype: "local",
		            	width: 890,
		            	height: 22,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	colNames:['?????????????????????','??????','??????',],
						colModel: [
							{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'FIRECOUNT',index:'FIRECOUNT',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'AGENT_FEE',index:'AGENT_FEE',fixed:true,width: 100,sortable:false,align:'right', formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
						]
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "search"){
						blockUI();
						<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
							$("#EBIZDATE").val($("#SBIZDATE").val());
						</logic:equal>
						$("#p_is_record").val("Y");
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						searchData();
						unblockUI();
					}else if(str == "export"){
						blockUIForDownload();
						$("#ac_key").val(str);
						$("#target").val('search');
						$("#sortname").val( $("#resultData").getGridParam('sortname') );
						$("#sortorder").val( $("#resultData").getGridParam('sortorder') );
						getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
						$("#formID").submit();
					}
				}
			}
			
			function searchData(){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=agent_fee_search_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo ???????????????"1 / 1" ?????????????????????????????? ?????????????????????
				newOption.pgtext= "{0} / {1}";
				newOption.p_is_record= 'N';
// 				$("#p_is_record").val("N");
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
//			 	var ary = options.colModel;
//			 	for(var i in ary ){
					
//			 	if(window.console){console.log("i>>"+i);}
//			 	if(window.console){console.log("ary>>"+ary[i]);}
//			 	}
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
					rtnStr = rowObject.PCODE_NAME+"<br>"+rowObject.TXN_NAME;
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
				//cellvalue - ??????cell??????  
				//options - ???cell???options???????????????{rowId, colModel,pos,gid}  
				//rowObject - ??????cell??????row????????????{ id=1, name="name1", price=123.1, ...} 
				return rtnStr;
			}
				
			
			function jq_linkLine(cellvalue, options, rowObject){
				var rtnStr = "";
				rtnStr = cellvalue;
				if(options.colModel.name == "SND_COMPANY_ID"){
					rtnStr = rowObject.SND_COMPANY_ID+"-"+rowObject.SND_COMPANY_ABBR_NAME;
					return rtnStr;
				};
				if(options.colModel.name == "AGENT_COMPANY_ID"){
					rtnStr = rowObject.AGENT_COMPANY_ID+"-"+rowObject.COMPANY_ABBR_NAME;
					return rtnStr;
				};
				if(options.colModel.name == "TXN_NAME"){
					rtnStr = rowObject.TXID+"-"+rowObject.TXN_NAME;
					return rtnStr;
				};
				return rtnStr;
			} 
				
			
			//==================??????????????????struts ????????????????????????API==================
			var fileDownloadCheckTimer;
			function blockUIForDownload() {
			    var token = new Date().getTime(); //use the current timestamp as the token value
			    $('#dow_token').val(token);
			    blockUI();
			    fileDownloadCheckTimer = window.setInterval(function () {
			      var cookieValue = $.cookie('fileDownloadToken');
			      if (cookieValue == token)
			       finishDownload();
			    }, 1000);
			}
			function finishDownload(){
				window.clearInterval(fileDownloadCheckTimer);
				$.cookie('fileDownloadToken' , null); //clears this cookie value (ie, chrome ok) 
				unblockUI();
			}
		</script>
	</body>
</html>