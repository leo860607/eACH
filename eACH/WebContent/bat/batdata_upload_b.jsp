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
		<style type="text/css">
			#countdownBlock{position:relative;float:right;top:-27px;background-color: #ECF1F6;padding:0px 5px}
			#upload_dialog .header{text-align:right;}
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
				<html:form styleId="formID" action="/batdata_upload" enctype="multipart/form-data">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 15%">操作行代號</td>
								<td style="width: 25%">
									<!-- 票交端 -->
									<logic:equal name="userData" property="USER_TYPE" value="A">
										<html:select styleId="OPBK_ID" property="OPBK_ID" styleClass="validate[funcCall[checkOpbkId]]" onchange="changeOPBK_ID(this.value)">
											<html:option value="all">全部</html:option>
											<html:optionsCollection name="batdata_upload_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</html:select>
									</logic:equal>
									
									<!-- 銀行端 -->
									<logic:equal name="userData" property="USER_TYPE" value="B">
										<html:select styleId="OPBK_ID" property="OPBK_ID" disabled="true" styleClass="validate[funcCall[checkOpbkId]]" onchange="changeOPBK_ID(this.value)">
											<html:option value="all">全部</html:option>
											<html:optionsCollection name="batdata_upload_form" property="opbkIdList" label="label" value="value"></html:optionsCollection>
										</html:select>
									</logic:equal>
								</td>
								<td class="header" style="width: 20%">限制每小時上傳檔案個數</td>
								<td>
									<html:text styleClass="lock" styleId="HR_UPLOAD_MAX_FILE" property="HR_UPLOAD_MAX_FILE" size="3" maxlength="2" readonly="true" style="font-weight:bold"></html:text>&nbsp;個檔
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<label class="btn" id="upload" onclick="onPut(this.id)"><img src="./images/export.png"/>&nbsp;整批檔案上傳</label>
								</td>
							</tr>
						</table>
					</fieldset>
					<!-- 整批檔案上傳畫面(對話框) -->
					<div id="upload_dialog" title="整批檔案上傳" style="font-size: 16px ;border: 1px solid black">
						<table style="width: 100%">
							<tr>
								<td class="header" style="width: 25%">資料檔案：</td>
								<td><html:file styleId="DATAFILE1" property="DATAFILE1" style="width: 100%"/></td>
							</tr>
							<tr>
								<td class="header">MAC檔案：</td>
								<td><html:file styleId="MACFILE1" property="MACFILE1" style="width: 100%"/></td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: center; padding-top: 10px">
									<label class="btn" id="confirm" onclick="onPut(this.id)"><img src="./images/save.png"/>&nbsp;確定</label>
								</td>
							</tr>
						</table>
					</div>
				</html:form>
			</div>
			<br/>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<div id="countdownBlock">
						剩餘 <font color="red" id="countdown"></font> 秒自動重載 | <label class="btn" id="btn_refresh" onclick="reload()"><img src="./images/refresh.png"/>&nbsp;重載</label>
					</div>
					<br/>
					<table id="resultData"></table>
				</fieldset>
			</div>
			<br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var countdownObj, countdownSec = 50;
			var gridOption;
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			
			$(document).ready(function () {
				$(document).ajaxStart(blockUI).ajaxStop(unblockUI);
				init();
        	});
			
			function init(){
				alterMsg();
				initCountdown();
				getHrUploadMaxFile($("#OPBK_ID").val());
				$("#formID").validationEngine({binded:false, promptPosition:"bottomRight"});
				
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				
				initDialog();
				
				searchData($("#OPBK_ID").val());
			}
// 			20151124 edit bu hugo req by UAT-20151120-02
			function reload(){
				initCountdown();
				getHrUploadMaxFile($("#OPBK_ID").val());
				$("#formID").validationEngine({binded:false, promptPosition:"bottomRight"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				initDialog();
				searchData($("#OPBK_ID").val());
			}
			
			function alterMsg(){
				var msg = '<bean:write name="batdata_upload_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initCountdown(){
				$("#countdown").html(countdownSec);
				startInterval(1, refreshCountdown, []);
			}
			
			function refreshCountdown(){
				var nextSec = parseInt($("#countdown").html()) - 1;
				if(nextSec <= 0){
					nextSec = countdownSec;
					getHrUploadMaxFile($("#OPBK_ID").val());
					searchData($("#OPBK_ID").val());
				}
				$("#countdown").html(nextSec);
			}
		
			function startInterval(interval, func, values){
				clearInterval(countdownObj);
				countdownObj = setRepeater(func, values, interval);
			}
			
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 200,
		            	sortname: 'LASTMODIFYDT',
// 		            	20151124 add by hugo req byUAT-20151120-02
		            	sortorder:'desc',
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
		            	rownumbers: true,
		            	//loadonce: true,
						colNames:['檔案上傳日期', '資料格式', '營業日', '清算階段', '檔案名稱', '票交所代理上傳', '全檔退回',
						          '總筆數', '總金額', '錯誤筆數', '錯誤金額', '合格筆數', '合格金額', '錯誤檔下載',
						          '待處理筆數', '完成筆數',
						          '成功筆數', '成功金額', '未完成筆數', '未完成金額', '失敗筆數', '失敗金額', '結果檔下載',
						          '上傳操作行', '整批處理序號', '處理開始日期', '處理完成日期'],
		            	colModel: [
							{name:'LASTMODIFYDT',index:'LASTMODIFYDT',align:'center',fixed:true,width: 140},
							{name:'FILELAYOUT',index:'FILELAYOUT',align:'center',fixed:true,width: 60},
							{name:'BIZDATE',index:'BIZDATE',align:'center',fixed:true,width: 70},
							{name:'CLEARINGPHASE',index:'CLEARINGPHASE',align:'center',fixed:true,width: 60},
							{name:'FILENAME',index:'FILENAME',align:'center',fixed:true,width: 150},
							{name:'ACHFLAG',index:'ACHFLAG',align:'center',fixed:true,width: 100},
							{name:'FILEREJECT',index:'FILEREJECT',align:'center',fixed:true,width: 60},
							{name:'TOTALCOUNT',index:'TOTALCOUNT',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'TOTALAMT',index:'TOTALAMT',align:'right',fixed:true,width: 80, formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'REJECTCOUNT',index:'REJECTCOUNT',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'REJECTAMT',index:'REJECTAMT',align:'right',fixed:true,width: 80, formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ACCEPTCOUNT',index:'ACCEPTCOUNT',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'ACCEPTAMT',index:'ACCEPTAMT',align:'right',fixed:true,width: 80, formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'REJECTFILEDOWNLOAD',index:'REJECTFILEDOWNLOAD',align:'center',fixed:true,width: 80, sortable: false},
							{name:'PROCCOUNT',index:'PROCCOUNT',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'SUCCESSCOUNT',index:'SUCCESSCOUNT',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'COUNT_A',index:'COUNT_A',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'AMT_A',index:'AMT_A',align:'right',fixed:true,width: 80, formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'COUNT_P',index:'COUNT_P',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'AMT_P',index:'AMT_P',align:'right',fixed:true,width: 80, formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'COUNT_R',index:'COUNT_R',align:'right',fixed:true,width: 80, formatter:'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'AMT_R',index:'AMT_R',align:'right',fixed:true,width: 80, formatter: 'currency', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
							{name:'RESULTFILEDOWNLOAD',index:'REJECTFILEDOWNLOAD',align:'center',fixed:true,width: 80, sortable: false},
							{name:'ACQUIREID',index:'ACQUIREID',align:'left',fixed:true,width: 180},
							{name:'BATCHSEQ',index:'BATCHSEQ',align:'center',fixed:true,width: 120},
							{name:'STARTDT',index:'STARTDT',align:'center',fixed:true,width: 140},
							{name:'ENDDT',index:'ENDDT',align:'center',fixed:true,width: 140}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
						loadComplete: function(data){
							//查詢結果無資料
							noDataEvent(data ,$("#resultData") );
							get_curPage(this, data.page, null, null);
					    },
					    rowattr: function (rd) {
					    	/*
					    	符合以下條件之一，資料列轉換底色：
					    	1. 檢核結果的合格筆數 <> 處理進度的待處理筆數+處理進度的完成筆數
					    	2. 當處理進度的待處理筆數=0時，處理進度的完成筆數 <> 交易結果的成功筆數+交易結果的未完成筆數+交易結果的失敗筆數
					    	*/
					    	var procCount = parseFloat(rd.PROCCOUNT);
					    	var successCount = parseFloat(rd.SUCCESSCOUNT);
						    if (
						    		(procCount + successCount != parseFloat(rd.ACCEPTCOUNT)) ||
						    		(procCount == 0 && (parseFloat(rd.COUNT_A) + parseFloat(rd.COUNT_P) + parseFloat(rd.COUNT_R) != successCount))
						    ) {
						        return {"class": "resultDataRowError"};
						    }
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
							var list = data.rows;
 							for(var rowCount in list){
 								var rejectData = $.parseJSON(list[rowCount].REJECTFILEDOWNLOAD);
 								var rFileStatus = list[rowCount].RESULTFILEDOWNLOAD;
 								//20151027 HUANGPU by 建利：若檔案狀態不符則不顯示按鈕
 								var isHidden = 'style="display: none"';
 								list[rowCount].REJECTFILEDOWNLOAD = '<button type="button" ' + (rejectData.EFILESTATUS=="N"?isHidden:'') + ' onclick="fileDownloadTrigger(\'rejectFileDownload\', \''+rejectData.EFILESTATUS+'\', \''+rejectData.BIZDATE+'\', \''+rejectData.PROCSEQ+'\',\''+rejectData.BATCHSEQ+'\')"><img src="./images/import.png"/></button>';
 								list[rowCount].RESULTFILEDOWNLOAD = '<button type="button" ' + (rFileStatus=="N"?isHidden:'') + ' onclick="fileDownloadTrigger(\'resultFileDownload\', \''+rFileStatus+'\', \''+rejectData.BIZDATE+'\', \''+rejectData.PROCSEQ+'\',\''+rejectData.BATCHSEQ+'\')"><img src="./images/import.png"/></button>';
 							}
						},
	 					loadtext: "處理中...",
	 					//20150530 edit by hugo req ACH 初始化要頁數要 1/1
	 					pgtext: "1 / 1" 
				};
			}
			
			function groupGridHeaders(){
				$("#resultData").jqGrid('setGroupHeaders', {
					  useColSpanStyle: true, 
					  groupHeaders:[
						{startColumnName: 'TOTALCOUNT', numberOfColumns: 7, titleText: '檢核結果'}
						,{startColumnName: 'PROCCOUNT', numberOfColumns: 2, titleText: '處理進度'}
						,{startColumnName: 'COUNT_A', numberOfColumns: 7, titleText: '交易結果'}
					  ]
				});
			}
			
			function initDialog(){
				$("#upload_dialog").dialog({
					appendTo: "#formID",
					autoOpen: false,
					height: 'auto',
					width: 500,
					modal: true,
					close: function() {
						$("#upload_dialog input").each(function(){
							var input = $(this);
							input.replaceWith(input = input.clone(true));
						});
					},
					open: function() {}
				});
			}
			
			function onPut(id){
				if($("#formID").validationEngine("validate")){
					if(id == "upload"){
						getSearch_condition('search_tab', 'input , select', 'serchStrs');
						$("#upload_dialog").dialog('open');
					}else if(id == "confirm"){
						if(validate()){
							//TODO 先做單筆的上傳功能
							var params = '&ACQUIREID=' + $("#OPBK_ID").val();
							var isValidationBack = false, validation_ok = false;
							fstop.getServerDataEx(uri + "?component=batdata_upload_bo&method=uploadValidation" + params, null, false, function(data){
								if(data){
									validation_ok = (data.result == "FALSE")?false:true;
									if(!validation_ok){
										alert("上傳失敗：" + data.msg);
									}
									isValidationBack = true;
								}
							});
							var interval_1 = setInterval(function(){
								if(isValidationBack && validation_ok){
									clearInterval(interval_1);
									//執行上傳
									blockUI();
									var searchParam = {};
									
									$.each($("[id^=DATAFILE]"), function(index, obj){
										if(obj.value != ''){
											searchParam[obj.id] = extractFileName($(obj).val());
										}
									});
									$.each($("[id^=MACFILE]"), function(index, obj){
										if(obj.value != ''){
											searchParam[obj.id] = extractFileName($(obj).val());
										}
									});
									searchParam['OPBK_ID'] = $("#OPBK_ID").val();
									searchParam['action'] = $("#formID").attr('action');
									$("#serchStrs").val(JSON.stringify(searchParam));
									$("#OPBK_ID").prop('disabled', false);
									$("#ac_key").val('upload');
									$("#target").val('search');
									$("#formID").submit();
								}
							}, 500);
						}
					}
				}
			}
			
			function checkOpbkId(){
				var opbkId = $("#OPBK_ID").val();
				if(opbkId == "all"){
					return "* 請選擇「操作行代號」後再執行上傳";
				}
			}
			
			//檢核檔名規則
			function checkFileName(type, fileName){
				if(type == "DATA"){
					if(fileName.match(/^\d{3}-\d{4}(((0[13578]|(10|12))(0[1-9]|[1-2][0-9]|3[0-1]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))(\d{3})\.P(0|1)1$/)){
						return true;
					}
				}else if(type == "MAC"){
					if(fileName.match(/^\d{3}-\d{4}(((0[13578]|(10|12))(0[1-9]|[1-2][0-9]|3[0-1]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))(\d{3})\.P(0|1)1-MAC$/)){
						return true;
					}
				}
				return false;
			}
			
			function extractFileName(fullPath){
				var fileName = fullPath;
				if(fullPath.indexOf('\\') != -1){
					fileName = fullPath.split('\\').pop();
				}
				if(fullPath.indexOf('/') != -1){
					fileName = fullPath.split('/').pop();
				}
				return fileName;
			}
			
			function changeOPBK_ID(opbkId){
				getHrUploadMaxFile(opbkId);
				searchData(opbkId);
			}
			
			function getHrUploadMaxFile(bgbkId){
// 				20151124 edit bu hugo req by UAT-20151120-04
// 				fstop.getServerDataEx(uri+"?component=bank_group_bo&method=getHR_UPLOAD_MAX_FILE&BGBK_ID="+bgbkId, null, false, function(data){
				fstop.getServerDataExIII(uri+"?component=bank_group_bo&method=getHR_UPLOAD_MAX_FILE&BGBK_ID="+bgbkId, null, false, function(data){
					$("#HR_UPLOAD_MAX_FILE").val(data.HR_UPLOAD_MAX_FILE);
				});
			}
			
			function searchData(opbkId){
				$("#resultData").jqGrid('GridUnload');
				
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#OPBK_ID").prop("disabled", false);
				</logic:equal>
				
				getSearch_condition('search_tab', 'input , select', 'serchStrs');
				var newOption = gridOption;
				var qStr = "component=batdata_upload_bo&method=search_toJson&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
				newOption.pgtext= "{0} / {1}";
				/*
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					newOption.page = parseInt('<bean:write name="batdata_upload_form" property="pageForSort"/>');
					resetSortname(newOption , 'LASTMODIFYDT' , 'ASC' , false);
				}else{
					newOption.page = 1;
					resetSortname(newOption , 'LASTMODIFYDT' , 'ASC' , true);
				}
				*/
				$("#resultData").jqGrid(newOption);
				groupGridHeaders();
				
				<logic:equal name="userData" property="USER_TYPE" value="B">
					$("#OPBK_ID").prop("disabled", true);
				</logic:equal>
			}
			
			function validate(){
				var msg = "";
				
				//提出檔與提出MAC檔必需同時存在
				var sn, $dataFile, $macFile;
				$.each($("[id^=DATAFILE]"), function(index, obj){
					sn = $(this).attr('id').replace(/DATAFILE/g, '');
					if($("#MACFILE" + sn).length){
						$dataFile = $(this);
						$macFile = $("#MACFILE" + sn);
						
						if($dataFile.val() == '' || $macFile.val() == ''){
							if($dataFile.val() == '' && $macFile.val() == ''){
								msg = "請選擇檔案";
							}else{
								msg = "請同時上傳提出檔與提出MAC檔";
							}
						}else{
							var param = {DATA: extractFileName($dataFile.val()), MAC: extractFileName($macFile.val())};
							for(var field in param){
								if(!checkFileName(field, param[field])){
									msg = '上傳失敗：' + field + '「' + param[field] + '」檔案名稱不符';
								}
							}
						}
					}else{
						msg = '網頁錯誤!請關閉瀏覽器後重新再試';
					}
					
					if(msg.length != 0){
						alert(msg);
						return false;
					}
				});
				
				if(msg.length != 0){
					return false;
				}else{
					return true;
				}
			}
			
			/*
			fileType: "rejectFileDownload" or "resultFileDownload"
			*/
			function fileDownloadTrigger(fileType, fileStatus, bizdate, procseq, batchseq){
				//if(window.console){console.log(fileType+','+fileStatus+', '+bizdate+', '+procseq+','+batchseq);}
				if(fileStatus == 'Y'){
					$("#formID").validationEngine('detach');
					blockUIForDownload();
					getSearch_condition('search_tab', 'input , select', 'serchStrs');
					var tmp = {BIZDATE: bizdate, PROCSEQ: procseq, BATCHSEQ: batchseq};
					$("#edit_params").val(JSON.stringify(tmp));
					$("#ac_key").val(fileType);
					$("#target").val('search');
					$("form").submit();
				}else{
					alert('檔案狀態錯誤，無法下載');
				}
			}
			
			//==================此區塊為使用struts 輸出檔案會用到的API==================
			var fileDownloadCheckTimer;
			function blockUIForDownload() {
				var token = new Date().getTime(); //use the current timestamp as the token value
			    $('#dow_token').val(token);
			    blockUI();
			    fileDownloadCheckTimer = window.setInterval(function () {
					var cookieValue = $.cookie('fileDownloadToken');
					if (cookieValue == token){
						finishDownload();
			      	}
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