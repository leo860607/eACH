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
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.cookie.js"></script>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		</script>
	</head>
	<body onload="unblockUI()">
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<bean:define id="userData" name="login_form" scope="session" property="userData"></bean:define>
			<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel">
				<html:form styleId="formID" action="/pi_snd_profile" method="POST" >
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="error_list" styleId="error_list" />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="dow_token" styleId="dow_token"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 18%">收費業者統編</td>
								<td style="width: 30%">
									<html:select styleId="PI_COMPANY_ID" property="PI_COMPANY_ID" onchange="getPI_List();">
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "pi_snd_profile_form" property="pi_com_IdList" label="label" value="value"/>
									</html:select>
								</td>
								<td class="header" style="width: 20%"><span>發動者統編</span></td>
								<td>
								
									<html:text styleId="SND_COMPANY_ID" property="SND_COMPANY_ID" size="12" maxlength="10" ></html:text>
								</td>
							</tr>
							<tr>
								<td class="header" style="width: 20%"><span>繳費類別代號</span></td>
								<td>
									<html:select styleId="BILL_TYPE_ID" property="BILL_TYPE_ID">
										<html:option value="">全部</html:option>
										<html:optionsCollection name = "pi_snd_profile_form" property="bill_type_IdList" label="label" value="value"/>
									</html:select>
								</td>
								<td class="header" colspan="2" style="color:red;padding-right:48px" ><span>※請確認發動者已於「代收發動者基本資料維護」建檔</span></td>
							</tr>
							<tr>
							    <td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
										<label class="btn" id="add" onclick="add_p(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
<!-- 									<label class="btn" id="export" onclick ="onPut(this.id);"><img src="./images/export.png"/>&nbsp;匯出Excel</label> -->
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				initGrid();
				initSelect();
				$("#formID").validationEngine({binded:false,promptPosition:"bottomLeft"});
			}
			
			function alterMsg(){
				blockUI();
				var msg = '<bean:write name="pi_snd_profile_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGrid(){
				gridOption = {
						datatype: 'local',
						autowidth:true,
		            	height: 240,
		            	shrinkToFit: true,
						//sortname: '',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
		            	rowNum: 10000,
		            	hiddengrid: true,
		            	colNames:['檢視明細','收費業者統一編號'
// 		            	          ,'收費業者簡稱','收費業者名稱'
		            	          ,'繳費類別代號','繳費類別名稱','發動者統編','啟用日期','停用日期'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:60, sortable: false},
		            	    {name:'PI_COMPANY_ID', fixed: true, width:100},
// 		            	    {name:'PI_COMPANY_ABBR_NAME', fixed: true, width:80},
// 		            	    {name:'PI_COMPANY_NAME', fixed: true, width:200},
		            	    {name:'BILL_TYPE_ID', fixed: true, width:150},
		            	    {name:'BILL_TYPE_NAME', fixed: true, width:130},
		            	    {name:'SND_COMPANY_ID', fixed: true, width:80},
		            	    {name:'START_DATE', fixed: true, width:80},
		            	    {name:'STOP_DATE', fixed: true, width:80},
// 		            	    {name:'NOTE', fixed: true, width:200}
						],
						beforeSelectRow: function(rowid, e) {
						    return false;
						},
						gridComplete: function(){
							$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							unblockUI();
						},
						beforeProcessing: function(data, status, xhr){
							for(var rowCount in data){
								rowCount = parseInt(rowCount);
								//DB的資料是西元年，畫面統一顯示民國年
								if(fstop.isNotEmptyString(data[rowCount].START_DATE) ){
									data[rowCount].START_DATE = '0'+(data[rowCount].START_DATE-19110000);
								}
								if(fstop.isNotEmptyString(data[rowCount].STOP_DATE) ){
									data[rowCount].STOP_DATE = '0'+(data[rowCount].STOP_DATE-19110000);
								}
								data[rowCount].BTN ='<button type="button" id="edit_'+rowCount+'" value="edit" onclick="edit_p(this.value,\''+data[rowCount].PI_COMPANY_ID+'\',\''+data[rowCount].SND_COMPANY_ID+'\' ,\''+data[rowCount].BILL_TYPE_ID+'\' )" ><img src="./images/edit.png"/></button>';
							}
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadComplete: function(data){
							//查詢結果無資料
							noDataEvent(data ,$("#resultData") );
						},
						loadtext: "處理中..."
				};
				
				$("#resultData").jqGrid(gridOption);
			}
			
			function add_p(str){
//查詢輸入的資料要帶到新增頁所以註解 				cleanFormNE(document.getElementById(str));
				$("#ac_key").val(str);
				$("#target").val('add_p');
				$("form").submit();
			}
			
			function initSelect(){
				//if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
					onPut('search');
				}
			}
			
			
			function getPI_List(){
				var pi_company_id = $("#PI_COMPANY_ID").val();
				if(PI_COMPANY_ID == '' || PI_COMPANY_ID == "all"){
					$("#BILL_TYPE_ID option:not(:first-child)").remove();
				}else{
					var rdata = {component:"pi_snd_profile_bo", method:"getBillIdListByPI", PI_COMPANY_ID:pi_company_id };
					fstop.getServerDataExII(uri, rdata, false, creatPI_List);
				}
			}
			
			function creatPI_List(obj){
				var select = $("#BILL_TYPE_ID");
				$("#BILL_TYPE_ID option:not(:first-child)").remove();
				if(window.console){console.log("url="+obj.result);}
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].value).text(dataAry[i].label));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					if(str == "export"){
						blockUIForDownload();
						$("#ac_key").val(str);
						$("#target").val('search');
						$("#formID").submit();
					}else{
						blockUI();
						getSearch_condition('search_tab', 'input, select', 'serchStrs');
						$("#resultData").jqGrid('GridUnload');
						var newOption = gridOption;
// 						var qStr = "component=pi_company_profile_bo&method=search_toJson&serchStrs="+encodeURI($("#serchStrs").val())+"&"+encodeURI($("#formID").find("[name!='serchStrs']").serialize());
						var qStr = "component=pi_snd_profile_bo&method=search_toJson&"+$("#formID").serialize()+"&serchStrs="+encodeURI($("#serchStrs").val());
						if(window.console)console.log($("#formID").serialize());
						newOption.url = "/eACH/baseInfo?"+qStr;
						newOption.datatype = "json";
						newOption.mtype = 'POST';
						if(window.console)console.log("url>>"+newOption.url);
						if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
							resetSortname(newOption , 'PI_COMPANY_ID' , 'ASC' , false);
						}else{
							resetSortname(newOption , 'PI_COMPANY_ID' , 'ASC' , true);
						}
						$("#resultData").jqGrid(newOption);
					}
				}
			}
			
			function edit_p(str, pi_company_id  , snd_company_id , bill_type_id){
				var tmp={};
				tmp['PI_COMPANY_ID'] = pi_company_id;
				tmp['SND_COMPANY_ID'] = snd_company_id;
				tmp['BILL_TYPE_ID'] = bill_type_id;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val(str);
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			//==================此區塊為使用struts 輸出檔案會用到的API==================
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
