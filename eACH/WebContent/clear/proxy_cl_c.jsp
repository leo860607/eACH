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
				<html:form styleId="formID" action="/proxy_cl">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header necessary" style="width: 16%">營業日</td>
								<td style="width: 30%">
									<html:text styleId="BIZDATE" property="BIZDATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
								</td>
								<td class="header necessary" style="width: 16%">代理清算行代號</td>
								
								<!-- 票交端 -->
								<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
									<td>
										<html:select styleId="SENDERACQUIRE" property="SENDERACQUIRE" styleClass="validate[required]">
											<html:option value="">===請選擇代理清算行代號===</html:option>
											<logic:present name="proxy_cl_form" property="proxy_cl_bankList">
												<html:optionsCollection name="proxy_cl_form" property="proxy_cl_bankList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
									</td>
								</logic:equal>
								
								<!-- 銀行端 -->
								<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
									<td>
										<html:hidden styleId="SENDERACQUIRE" property="SENDERACQUIRE"/>
										<html:text property="CTBK_NAME" readonly="true" styleClass="lock"></html:text>
										
										<%-- 
										<html:select styleId="SENDERACQUIRE" property="SENDERACQUIRE" styleClass="validate[required]" disabled="true" value="<%=(String)defaultSenderAcquire %>">
											<html:option value="">===請選擇代理清算行代號===</html:option>
											<logic:present name="proxy_cl_form" property="proxy_cl_bankList">
												<html:optionsCollection name="proxy_cl_form" property="proxy_cl_bankList" label="label" value="value"></html:optionsCollection>
											</logic:present>
										</html:select>
										--%>
									</td>
								</logic:equal>
							</tr>
							<tr>
								<td class="header">清算階段</td>
								<td colspan="3">
									<html:select styleId="CLEARINGPHASE" property="CLEARINGPHASE">
										<html:option value="">全部</html:option>
										<html:option value="01">1</html:option>
										<html:option value="02">2</html:option>
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
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDatePicker();
				$("#BIZDATE").val('<bean:write name="proxy_cl_form" property="BIZDATE"/>');
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				initGridOptionII();
				initDataSumGrid();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="proxy_cl_form" property="msg"/>';
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
		            	height: 250,
		            	sortable: false,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
// 		            	rownumbers:true,
		            	//loadonce: true,電腦系統原因,帳務原因,其他原因
						colNames:['總行代號','總行名稱', '清算階段','筆數','金額', '筆數','金額','金額小計(A)','筆數','金額', '筆數','金額','金額小計(B)','差額(A-B)'],
		            	colModel: [
// 							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
							{name:'BGBK_ID',index:'BGBK_ID',align:'center',fixed:true,width: 100},
							{name:'BGBK_NAME',index:'BGBK_NAME',fixed:true,width: 150},
							{name:'CLEARINGPHASE',index:'CLEARINGPHASE',fixed:true,width: 65},
							{name:'RECVCNT',index:'RECVCNT',fixed:true,width: 95},
							{name:'RECVAMT',index:'RECVAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'RVSRECVCNT',index:'RVSRECVCNT',fixed:true,width: 95},
							{name:'RVSRECVAMT',index:'RVSRECVAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'IN_TOL',index:'IN_TOL',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'PAYCNT',index:'PAYCNT',fixed:true,width: 95},
							{name:'PAYAMT',index:'PAYAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'RVSPAYCNT',index:'RVSPAYCNT',fixed:true,width: 95},
							{name:'RVSPAYAMT',index:'RVSPAYAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'OUT_TOL',index:'OUT_TOL',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
							{name:'DIF_TOL',index:'DIF_TOL',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					loadComplete: function(data){
	 						//查詢結果無資料
							noDataEvent(data ,$("#resultData") );
					        //如果有加總資料的話，就要代到下面的GRID
					        if(data.dataSumList != undefined){
					        	$("#dataSum").jqGrid('delRowData',1);
					        	var sumData = {
					        		RECORDS: data.records,
					        		RECVCNT: data.dataSumList[0].RECVCNT,
					        		RECVAMT: data.dataSumList[0].RECVAMT,
					        		RVSRECVCNT: data.dataSumList[0].RVSRECVCNT,
					        		RVSRECVAMT: data.dataSumList[0].RVSRECVAMT,
					        		IN_TOL: data.dataSumList[0].IN_TOL,
					        		PAYCNT: data.dataSumList[0].PAYCNT,
					        		PAYAMT: data.dataSumList[0].PAYAMT,
					        		RVSPAYCNT: data.dataSumList[0].RVSPAYCNT,
					        		RVSPAYAMT: data.dataSumList[0].RVSPAYAMT,
					        		OUT_TOL: data.dataSumList[0].OUT_TOL,
					        		DIF_TOL: data.dataSumList[0].DIF_TOL
					        	};
					        	/*
					        	for(var k in sumData){
					        		if(console.log){console.log(k + " = " + sumData[k]);}
					        	}
					        	*/
								$("#dataSum").jqGrid('addRowData',1,sumData);
					        }
					    },
						beforeProcessing: function(data, status, xhr){
// 							var ser_no = "",userId = "" ,com_id="";
// 							var list = data.rows;
// 							for(var rowCount in list){
// 								if(window.console){console.log(rowCount);}
// 								ser_no = list[rowCount].id.SERNO;
// 								list[rowCount].BTN = '<button type="button" id="edit_' + ser_no + '" onclick="edit_p(this.id , \''+ser_no+'\' , \''+userId+'\' , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
// 								list[rowCount].BTN = rowCount;
// 								if(window.console){console.log("btn>>"+list[rowCount].BTN);}
// 							}
						},
	 					loadtext: "處理中...",
	 					pgtext: "{0} / {1}"
				};
				
				gridOption2 = {
					datatype: "local",
					autowidth:true,
		            height: 45,
		            sorttype:"text",
		            shrinkToFit: true,
		            colNames:['查詢結果總筆數','筆數','金額', '筆數','金額','金額小計(A)','筆數','金額', '筆數','金額','金額小計(B)','差額(A-B)'],
					colModel: [
						{name:'RECORDS',index:'RECORDS',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
						{name:'RECVCNT',index:'RECVCNT',fixed:true,width: 95},
						{name:'RECVAMT',index:'RECVAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						{name:'RVSRECVCNT',index:'RVSRECVCNT',fixed:true,width: 95},
						{name:'RVSRECVAMT',index:'RVSRECVAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						{name:'IN_TOL',index:'IN_TOL',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						{name:'PAYCNT',index:'PAYCNT',fixed:true,width: 95},
						{name:'PAYAMT',index:'PAYAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						{name:'RVSPAYCNT',index:'RVSPAYCNT',fixed:true,width: 95},
						{name:'RVSPAYAMT',index:'RVSPAYAMT',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						{name:'OUT_TOL',index:'OUT_TOL',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}},
						{name:'DIF_TOL',index:'DIF_TOL',fixed:true,width: 95, align:'right', formatter:'currency', formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0}}
					]
				};
			}
			function initGridOptionII(){
				jQuery("#resultData").jqGrid('setGroupHeaders', {
					useColSpanStyle: true, 
					groupHeaders:[
						{startColumnName: 'BGBK_ID', numberOfColumns: 3, titleText: ''},
						{startColumnName: 'RECVCNT', numberOfColumns: 5, titleText: '應收'},
						{startColumnName: 'PAYCNT', numberOfColumns: 5, titleText: '應付'},
						{startColumnName: 'DIF_TOL', numberOfColumns: 1, titleText: ''}
					]	  	
				});
				jQuery("#resultData").jqGrid('setGroupHeaders', {
					useColSpanStyle: true, 
					groupHeaders:[
						{startColumnName: 'RECVCNT', numberOfColumns: 2, titleText: '入帳'},
						{startColumnName: 'RVSRECVCNT', numberOfColumns: 2, titleText: '扣款沖正'},
						{startColumnName: 'PAYCNT', numberOfColumns: 2, titleText: '扣款'},
						{startColumnName: 'RVSPAYCNT', numberOfColumns: 2, titleText: '入帳沖正'}
					]	  	
				});
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					blockUI();
					checkDate();
					
					unblockUI();
				}
			}
			
			function initDataSumGrid(){
				$("#dataSum").jqGrid(gridOption2);
				jQuery("#dataSum").jqGrid('setGroupHeaders', {
					useColSpanStyle: true,
					groupHeaders:[
						{startColumnName: 'RECORDS', numberOfColumns: 1, titleText: ''},
						{startColumnName: 'RECVCNT', numberOfColumns: 5, titleText: '應收'},
						{startColumnName: 'PAYCNT', numberOfColumns: 5, titleText: '應付'},
						{startColumnName: 'DIF_TOL', numberOfColumns: 1, titleText: ''}
					]
				});
				jQuery("#dataSum").jqGrid('setGroupHeaders', {
					useColSpanStyle: true, 
					groupHeaders:[
						{startColumnName: 'RECVCNT', numberOfColumns: 2, titleText: '入帳'},
						{startColumnName: 'RVSRECVCNT', numberOfColumns: 2, titleText: '扣款沖正'},
						{startColumnName: 'PAYCNT', numberOfColumns: 2, titleText: '扣款'},
						{startColumnName: 'RVSPAYCNT', numberOfColumns: 2, titleText: '入帳沖正'}
					]
				});
			}
			
			function searchData(){
				$("#SENDERACQUIRE").attr("disabled", false);
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=proxy_cl_bo&method=pageSearch&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				$("#resultData").jqGrid(newOption);
				initGridOptionII();
				
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
				$("#SENDERACQUIRE").attr("disabled", true);
				</logic:equal>
			}
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			function this_Fmatter (cellvalue, options, rowObject)
			{
				var tol = parseInt(rowObject.TOTALCOUNT);
				var failTol = parseInt(rowObject.FAILCOUNT);
				var size = Math.pow(10, 2);
// 				var p = Math.round((failTol/tol)) * 100 ;
				var p = Math.round((failTol/tol) *100 * size)/size  ;
				return p;
			   // do something here
			}
			
			function checkDate(){
				<logic:equal name="login_form" property="userData.USER_TYPE" value="B">
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=proxy_cl_bo&method=checkDate&BIZDATE="+$("#BIZDATE").val()+"&CLEARINGPHASE="+$("#CLEARINGPHASE").val(),
					async:false,
					dataType:'text',
					success:function(result){
						if(result == "FALSE"){
							var clearingphase = $("#CLEARINGPHASE").val();
							if(clearingphase == ""){
								clearingphase="全部";
							}
							alert("目前營業日="+$("#BIZDATE").val()+",清算階段="+clearingphase+",尚無法查詢");
						}
						else{
							getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
							searchData();
						}
					}
				});
				</logic:equal>
				<logic:equal name="login_form" property="userData.USER_TYPE" value="A">
				getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
				searchData();
				</logic:equal>
			}
// 			function resetUserId(obj){
// 				$(obj).find(":selected").val();
// 				var rdata = {component:"userlog_bo", method:"getUserIdListByComId" , com_id:$(obj).find(":selected").val() } ;
// 				fstop.getServerDataExII(uri, rdata, false, showUsers);
// 			}
// 			function showUsers(obj){
// 				if(window.console){console.log("obj>>"+obj);}
// 				var select = $("#USERID");
// 				select.children().remove();
// 				select.append($("<option></option>").attr("value", "all").text("==全部=="));
// 				for( var key in obj ){
// 					select.append($("<option></option>").attr("value", obj[key].value).text(obj[key].label));
// 				}
// 			}
		</script>
	</body>
</html>