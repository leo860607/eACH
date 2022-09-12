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
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
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
				<html:form styleId="formID" action="/sd_com" method="POST" enctype="multipart/form-data">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" />
					<html:hidden property="SND_BRBK_ID" styleId="SND_BRBK_ID" />
					<html:hidden property="file_path" styleId="file_path" />
					<html:hidden property="error_list" styleId="error_list" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="BIZDATE" styleId="BIZDATE" />
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 18%">發動者統一編號</td>
								<td style="width: 30%">
									<html:text styleId="COMPANY_ID" property="COMPANY_ID" size="12" maxlength="10"></html:text>
								</td>
								<td class="header" style="width: 20%">發動行所屬總行代號</td>
								<td>
									<html:select styleId="SENDERHEAD" property="SENDERHEAD">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name="sd_com_form" property="bgbkIdList" label="label" value="value"/>
									</html:select>
									<%-- 
									<html:text styleId="SND_BRBK_ID" property="SND_BRBK_ID" size="7" maxlength="7"></html:text>
									<button type="button" value="SND_BRBK" onclick="branch_search(this.value)">...</button>
									<input id="SND_BRBK_NAME"type="text" readonly="true" disabled style="background-color: transparent;border: 0;"/>
									--%>
								</td>
							</tr>
							<tr>
								<td class="header">發動者名稱</td>
								<td>
									<html:text styleId="COMPANY_NAME" property="COMPANY_NAME" size="40"></html:text>
								</td>
								<td class="header">交易代號</td>
								<td>
									<html:select styleId="TXN_ID" property="TXN_ID">
										<html:option value="all">全部</html:option>
										<html:optionsCollection name = "sd_com_form" property="txnIdList" label="label" value="value"/>
									</html:select>
								</td>
							</tr>
							<tr>
							    <td class="header">收費類型</td>							     
		                        <td>
										<html:select styleId="FEE_TYPE" property="FEE_TYPE">
										<html:option value="all">全部</html:option>
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
									<!-- <label class="btn" id="clean" onclick ="cleanForm(this)"><img src="./images/clean.png"/>&nbsp;清除查詢條件</label> -->
									<label class="btn" id="clean" onclick ="cleanForm(this)"><img src="./images/clean.png"/>&nbsp;清除查詢條件</label>&nbsp;&nbsp;
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
									<label class="btn" id="add" onclick ="add_p(this.id);"><img src="./images/add.png"/>&nbsp;新增</label>
									<label class="btn" id="import_data" onclick ="sd_import(this.id);"><img src="./images/import.png"/>&nbsp;匯入</label>
									<span><html:file styleId="FILE" property="FILE" style="left:100px;"/></span>
									</logic:equal>
								</td>
							</tr>
						</table>
						<span id="countRows"></span>
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
					<legend>資料變更歷程</legend>
					<table id="hs_table"></table>
				</fieldset>
			</div>
			<br/>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var gridOption, gridOption2;
			var bizdate="";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				initGrid();
				initSelect();
				errorMsg();
				cleanForm();
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="sd_com_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function errorMsg(){
				var msg = '<bean:write name="sd_com_form" property="error_list"/>';
				if(fstop.isNotEmptyString(msg)){
					$("#error_list").val(msg);
					window.open("base/errorMsg.jsp","匯入檔案錯誤","width=300, height=400, scrollbars=yes");
				}	
				unblockUI()
			}
			
			function initGrid(){
				gridOption = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
		            	height: 240,
						sorttype: 'text',
						shrinkToFit: true,
						loadonce: true,
		            	rowNum: 10000,
		            	colNames:['檢視明細','資料變更歷程','發動者統一編號','發動者簡稱','發動者名稱','交易代號','收費類型','發動分行代號','發動分行名稱','聯絡人資訊','開辦日期','發文日期','用戶號碼','文號','手續費啟用日期','啟用日期','停用日期'],
		            	colModel: [
		            	    {name:'BTN', index:'BTN', fixed: true, align: 'center', width:60, sortable: false},
		            	    {name:'HISTORY',index:'HISTORY', fixed: true, align: 'center', width:90, sortable: false},
							{name:'COMPANY_ID', index:'COMPANY_ID',fixed: true, width:120},
							{name:'COMPANY_ABBR_NAME', index:'COMPANY_ABBR_NAME',fixed: true, width:80},
							{name:'COMPANY_NAME',index:'COMPANY_NAME', fixed: true, width:300},
							{name:'TXN_ID',index:'TXN_ID', fixed: true, align : 'center', width:70},
							{name:'FEE_TYPE_CHT', index:'FEE_TYPE_CHT',fixed: true, align : 'center',width:70},
							{name:'SND_BRBK_ID',index:'SND_BRBK_ID', fixed: true, width:85},
							{name:'BRBK_NAME',index:'BRBK_NAME', fixed: true, width:150},
							{name:'CONTACT_INFO',index:'CONTACT_INFO', fixed: true, width:250, formatter: newLineFmatter},
							{name:'START_DATE',index:'START_DATE', fixed: true, width:120},
							{name:'DISPATCH_DATE',index:'DISPATCH_DATE', fixed: true, width:80},
							{name:'USER_NO',index:'USER_NO', fixed: true, width:130},
							{name:'CASE_NO',index:'CASE_NO', fixed: true, width:180},
							{name:'FEE_TYPE_ACTIVE_DATE', index:'FEE_TYPE_ACTIVE_DATE',fixed: true,width:100},
							{name:'ACTIVE_DATE', index:'ACTIVE_DATE',fixed: true, width:80},
							{name:'STOP_DATE',index:'STOP_DATE', fixed: true, width:80}
						],
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var com_id ="";txn_id =""; brbk_id = "";fee_type = "";
							for(var rowCount in data){
								rowCount = parseInt(rowCount);
								com_id = data[rowCount].COMPANY_ID;
								txn_id = data[rowCount].TXN_ID;
								brbk_id = data[rowCount].SND_BRBK_ID;
								fee_type = data[rowCount].FEE_TYPE;
								if(fstop.isNotEmpty(data[rowCount].id)){ 
									if(fstop.isEmpty(com_id.trim)){
										data[rowCount].COMPANY_ID = data[rowCount].id.COMPANY_ID;
									}
									if(fstop.isEmpty(brbk_id.trim)){
										data[rowCount].SND_BRBK_ID = data[rowCount].id.SND_BRBK_ID;
									}
									if(fstop.isEmpty(txn_id.trim)){
										data[rowCount].TXN_ID = data[rowCount].id.TXN_ID;
									}
									com_id = data[rowCount].COMPANY_ID;
									txn_id = data[rowCount].TXN_ID;
									brbk_id = data[rowCount].SND_BRBK_ID;
									fee_type = data[rowCount].FEE_TYPE;
								}
								data[rowCount].BTN = '<button type="button" id="edit_' + com_id + '" onclick="edit_p(this.id , \''+com_id+'\', \''+txn_id+'\', \''+brbk_id+'\',\''+fee_type+'\')"><img src="./images/edit.png"/></button>';
								data[rowCount].HISTORY = '<button type="button" id="SDRow_' + rowCount + '" onclick="reloadHistoryTable(\''+com_id+'\', \''+txn_id+'\', \''+brbk_id+'\')"><img src="./images/search.png"/></button>';
							}
							var count = data.length;
							typeof count;
							if (typeof count == 'undefined'){
								data.length='0';
							}
							document.getElementById("countRows").innerHTML ='符合筆數：'+data.length;
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#resultData") );
						},
						loadtext: "處理中..."
				};
				$("#resultData").jqGrid(gridOption);
				
				
				
				gridOption2 = {
						datatype: "local",
						autowidth: true,
		            	height: 200,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum:10000,
		            	loadonce: true,
		            	
						colNames:['序號','刪除','發動者統一編號','交易代號','發動分行代號','收費類型','手續費啟用日期','異動日期'],
						colModel: [
								{name:'No', index:'No', fixed: true, align: 'center', width:60, sortable: false},
							 	{name:'DEL', index:'DEL', fixed: true, align: 'center', width:120, sortable: false},
								{name:'COMPANY_ID', fixed: true, width:120},
								{name:'TXN_ID', fixed: true, align : 'center', width:70},
								{name:'SND_BRBK_ID', fixed: true, width:85},
								{name:'FEE_TYPE_CHT', fixed: true, align : 'center',width:70},
								{name:'ACTIVE_DATE', fixed: true, width:100},
								{name:'UDATE', fixed: true, width:80}
						],
		            	gridComplete:function (){
			            	$("#hs_table .jqgrow:odd").addClass('resultDataRowOdd');
							$("#hs_table .jqgrow:even").addClass('resultDataRowEven');
						},
						beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var seq_id="";
							var count = data.length;
							var flag = true ;
							var tmpdate="";
							for(var rowCount in data){
								seq_id = data[rowCount].SEQ_ID;	
								active_date = data[rowCount].ACTIVE_DATE;
// 								alert("active_date : "+active_date);
// 								if(count==data.length){
// 									data[rowCount].DEL = '當前設定'
// 								}
								if(count=='1' && (active_date > $('#BIZDATE').val())){
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										data[rowCount].DEL='初始設定無法刪除';
									</logic:equal>
								}else{
									if( active_date > $('#BIZDATE').val()){
										<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
											data[rowCount].DEL = '<button type="button" id="del_' + seq_id + '" onclick="del_his( \''+seq_id+'\')"><img src="./images/delete.png"/></button>';
										</logic:equal>
									}else{
										if(flag){
// 											tmpdate = active_date;
											data[rowCount].DEL='當前設定';
											flag = false;
										}else{
// 											if(tmpdate > active_date){
// 												data[rowCount].DEL='當前設定';
// 												data[rowCount-1].DEL='';
// 											}else{
												data[rowCount].DEL='';
// 											}
											
										}
									}
								}
								data[rowCount].No=count;
								count=count-1;
							}
							
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
	 					loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent(data ,$("#hs_table") );
						},
	 					loadtext: "處理中..."
				};
// 				$("#hs_table").jqGrid(gridOption2);
				
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
// 					因資料量過 故預設一開始不查詢
// 					onPut('search');
				}
			}
			
			function onPut(str){
				if(
					 $("#SENDERHEAD").val() == "all" && $("#TXN_ID").val() == "all" && $("#FEE_TYPE").val() == "all" && $("#COMPANY_ID").val() == "" && $("#COMPANY_NAME").val() == "" &&
					!confirm("確定查詢全部資料?(載入時間較長)")){
					return;
				}
// 				if($("#COMPANY_ID").val() == ""){$("#COMPANY_ID").val("all");}
// 				if($("#COMPANY_NAME").val() == ""){$("#COMPANY_NAME").val("all");}
				blockUI();
// 				getSearch_conditionIII('search_tab', 'input, select', 'serchStrs');
				getSearch_condition('search_tab', 'input, select', 'serchStrs');
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				//var qStr = "component=sd_com_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+"&"+encodeURI($("#formID").find("[name!='serchStrs']").serialize());
				//var qStr = "component=sd_com_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+$("#formID").children().not($("#serchStrs")).serialize();
				var qStr = "component=sd_com_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+"&"+encodeURI($('form :input:not(#serchStrs)').serialize());
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					resetSortname(newOption , 'COMPANY_ID' , 'ASC' , false);
				}else{
					resetSortname(newOption , 'COMPANY_ID' , 'ASC' , true);
				}
				$("#resultData").jqGrid(newOption);
				$("#hs_table").jqGrid('GridUnload');
// 				if($("#COMPANY_ID").val() == "all"){$("#COMPANY_ID").val("");}
// 				if($("#COMPANY_NAME").val() == "all"){$("#COMPANY_NAME").val("");}
			}	
			
			function add_p(str){
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}
			
			function sd_import(str){
                var file = $('#FILE');
				
				//file.val("");
				//IE8快取無法清除，用取代的
// 				file.replaceWith(file=file.clone(true));
				if(!file.val()==''){
					file_submit(str);
					blockUI();
				}else{
				alert('請選擇檔案');	
				return;
				}	
			}
			
			function file_submit(str){
				$("#ac_key").val(str);
				$("form").submit();
			}
			
			
			function edit_p(str , id , id2 ,id3 , id4){
				var tmp={};
				tmp['COMPANY_ID'] = id;
				tmp['TXN_ID'] = id2;
				tmp['SND_BRBK_ID'] = id3;
				tmp['FEE_TYPE'] = id4;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function newLineFmatter(cellvalue, options, rowObject){
				if(cellvalue==null){
					return;
				}
				return cellvalue.replace(/(\\n)/g, '<br/>');
			}
			
			function cleanForm(str){
				$("#COMPANY_ID").val('');
				$("#SENDERHEAD").val('all');
				$("#COMPANY_NAME").val('');
				$("#TXN_ID").val('all');
				$("#FEE_TYPE").val('all');
			}
			
			function reloadHistoryTable(id1 , id2 ,id3){
				blockUI();
// 				var sndBrbkId = $("#resultData").jqGrid('getCell', rowId, 'SND_BRBK_ID');
// 				alert("com_id,txn_id,sndBrbkId  :"+ com_id +" ," + txn_id + " , "+sndBrbkId);
				if(window.console){console.log("com_id>>"+id1);}
				if(window.console){console.log("txn_id>>"+id2);}
				if(window.console){console.log("sndBrbkId>>"+id3);}
				var qStr = "component=sd_com_bo&method=hisSearch&com_id="+id1+"&txn_id="+id2+"&sndBrbkId="+id3+"&action="+$("#formID").attr('action');
				$("#hs_table").jqGrid('GridUnload');
				var newOption = gridOption2;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				if(window.console)console.log("url>>"+newOption.url);
				$("#hs_table").jqGrid(newOption);
				unblockUI();
			}
			
			function reloadMainTable(id1 ,id2 ,id3 ){
				blockUI();
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				//var qStr = "component=sd_com_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+"&"+encodeURI($("#formID").find("[name!='serchStrs']").serialize());
				//var qStr = "component=sd_com_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+$("#formID").children().not($("#serchStrs")).serialize();
				var qStr = "component=sd_com_bo&method=search_toJson&COMPANY_ID="+id1+"&TXN_ID="+id2+"&action="+$("#formID").attr('action');
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
// 				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
// 					resetSortname(newOption , 'COMPANY_ID' , 'ASC' , false);
// 				}else{
// 					resetSortname(newOption , 'COMPANY_ID' , 'ASC' , true);
// 				}
				$("#resultData").jqGrid(newOption);
				unblockUI();
			}
			
			function del_his(seq_id){
				if(confirm("確認刪除此筆歷程資料 ?")){
					blockUI();
					if(window.console){console.log("seq_id>>"+seq_id);}
					var rdata = {component:"sd_com_bo", method:"del_History" ,seq_id:seq_id};
					var vResult = fstop.getServerDataExII(uri,rdata,false);
					if(fstop.isNotEmpty(vResult)){
						//不可存,跳alert
						if(vResult.result == "TRUE"){
							alert("刪除成功");
							var obj = jQuery.parseJSON( vResult.msg )
							reloadHistoryTable(obj.COMPANY_ID,obj.TXN_ID,obj.SND_BRBK_ID);
							reloadMainTable(obj.COMPANY_ID,obj.TXN_ID,obj.SND_BRBK_ID);
						//可存
						}else{
							alert("刪除失敗")
						}
					}else{
						alert("系統異常");
					}
					unblockUI();
				}else{
					return;
				}
			} 
		</script>
	</body>
</html>
