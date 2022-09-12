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
		<!-- 因為刪除按鍵比較大，所以將Header的高度調高一點 -->
		<style type="text/css">
			.ui-jqgrid .ui-jqgrid-htable th div {
    			height:22px;
			}
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
				<html:form styleId="formID" action="/each_fileupload">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="ID_NOS" styleId="ID_NOS" value=""/>
					<html:hidden property="FILE_NOS" styleId="FILE_NOS" value=""/>
					<html:hidden property="ACTION_TYPE" styleId="ACTION_TYPE" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
					<input id="USER_COMPANY" name="USER_COMPANY" type="hidden" value="<bean:write name="userData" property="USER_COMPANY" />">
					<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
					<fieldset>
						<table>
							<logic:equal  name="login_form" property="userData.USER_TYPE" value="A">
							<tr>
								<td  style="padding-top: 5px">
									<label class="btn" id="add" onclick="add_p('B')"><img src="./images/add.png"/>&nbsp;新增(銀行端)</label>
								</td>
								<td  style="padding-top: 5px">
									<label class="btn" id="add" onclick="add_p('C')"><img src="./images/add.png"/>&nbsp;新增(代理業者)</label>
								</td>
							</tr>
							</logic:equal>
							<logic:notEqual  name="login_form" property="userData.USER_TYPE" value="A">
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="add" onclick="add_p()"><img src="./images/add.png"/>&nbsp;新增</label>
								</td>
							</tr>
							</logic:notEqual>
						</table>
					</fieldset>
					</logic:equal>
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
			
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				initGridOption();
				$("#resultData").jqGrid(gridOption);
			}
			
			function alterMsg(){
 				var msg = '<bean:write name="each_fileupload_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				var auth ='<bean:write name="login_form" property="userData.s_auth_type"/>';
				if(auth=='A'){
					gridOption = {
							toppager: true,
							pagerpos: "left",
							datatype: "local",
			            	autowidth:true,
			            	height: 270,
			            	sortable: true,
			            	sorttype:"text",
			            	shrinkToFit: true,
			            	rowNum: 10,
			            	url:"/eACH/baseInfo?component=each_fileupload_bo&method=pageSearch&"+$("#formID").serialize(),
			            	datatype:"json",
			            	mtype:"POST",
							colNames:['<label class="btn" onclick="deleteData()"><img src="./images/delete.png"/>&nbsp;刪除</label>','檔案序號','檔案名稱','檔案描述','檔案大小(單位：KB)','下載次數','最後下載人員','上傳人員','上傳時間','可下載單位'],
			            	colModel: [
								{name:'CHECKBOX',index:'CHECKBOX',align:'center',fixed:true,width: 70,sortable:false},
								{name:'FILE_NO',index:'FILE_NO',fixed:true,width: 80,sortable:false,align:'right'}, 
								{name:'FILENAME',index:'FILENAME',fixed:true,width: 250,sortable:false}, 
								{name:'DOCNAME',index:'DOCNAME',fixed:true,width: 150,sortable:false},
								{name:'FILESIZE',index:'FILESIZE',fixed:true,width: 150,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
								{name:'DOWNLOADNUM',index:'DOWNLOADNUM',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
								{name:'LASTDOWNLOADER',index:'LASTDOWNLOADER',fixed:true,width: 100,sortable:false},
								{name:'UPLOADER',index:'UPLOADER',fixed:true,width: 100,sortable:false},
								{name:'CDATEString',index:'CDATEString',fixed:true,width: 100,sortable:false},
								{name:'TOBANKS',index:'TOBANKS',fixed:true,width: 100,sortable:false}
							],
			            	gridComplete:function (){
				            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
								$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							},
		 					beforeSelectRow: function(rowid, e) {
		 					    return false;
		 					},
		 					beforeProcessing: function(data, status, xhr){
		 						var ID_NO = "";
		 						var FILE_NO = "";							
		 						var FILENAME = "";
		 						var FILESIZE = "";
								var list = data.rows;								
	 							for(var rowCount in list){
	 								ID_NO = list[rowCount].ID_NO;
	 								FILE_NO = list[rowCount].FILE_NO;
	 								FILENAME = list[rowCount].FILENAME;
	 								FILESIZE = list[rowCount].FILESIZE;
	 								list[rowCount].CHECKBOX = '<input type="checkbox"/><input type="hidden" value="'+ID_NO+'"/><input type="hidden" value="'+FILE_NO+'"/><input type="hidden" value="'+FILENAME+'"/><input type="hidden" value="'+FILESIZE+'"/>';   
	 							}
							},
		 					loadtext: "處理中...",
		 					pgtext: "{0} / {1}"
					};
				}else{
					gridOption = {
							toppager: true,
							pagerpos: "left",
							datatype: "local",
			            	autowidth:true,
			            	height: 270,
			            	sortable: true,
			            	sorttype:"text",
			            	shrinkToFit: true,
			            	rowNum: 10,
			            	url:"/eACH/baseInfo?component=each_fileupload_bo&method=pageSearch&"+$("#formID").serialize(),
			            	datatype:"json",
			            	mtype:"POST",
							colNames:['檔案序號','檔案名稱','檔案描述','檔案大小(單位：KB)','下載次數','最後下載人員','上傳人員','上傳時間','可下載單位'],
			            	colModel: [
								{name:'FILE_NO',index:'FILE_NO',fixed:true,width: 80,sortable:false,align:'right'}, 
								{name:'FILENAME',index:'FILENAME',fixed:true,width: 250,sortable:false}, 
								{name:'DOCNAME',index:'DOCNAME',fixed:true,width: 150,sortable:false},
								{name:'FILESIZE',index:'FILESIZE',fixed:true,width: 150,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
								{name:'DOWNLOADNUM',index:'DOWNLOADNUM',fixed:true,width: 100,sortable:false,align:'right', formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
								{name:'LASTDOWNLOADER',index:'LASTDOWNLOADER',fixed:true,width: 100,sortable:false},
								{name:'UPLOADER',index:'UPLOADER',fixed:true,width: 100,sortable:false},
								{name:'CDATEString',index:'CDATEString',fixed:true,width: 100,sortable:false},
								{name:'TOBANKS',index:'TOBANKS',fixed:true,width: 100,sortable:false}
							],
			            	gridComplete:function (){
				            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
								$("#resultData .jqgrow:even").addClass('resultDataRowEven');
							},
		 					beforeSelectRow: function(rowid, e) {
		 					    return false;
		 					},
		 					beforeProcessing: function(data, status, xhr){
		 						var ID_NO = "";
		 						var FILE_NO = "";							
		 						var FILENAME = "";
		 						var FILESIZE = "";
								var list = data.rows;								
	 							for(var rowCount in list){
	 								ID_NO = list[rowCount].ID_NO;
	 								FILE_NO = list[rowCount].FILE_NO;
	 								FILENAME = list[rowCount].FILENAME;
	 								FILESIZE = list[rowCount].FILESIZE;
	 								list[rowCount].CHECKBOX = '<input type="checkbox"/><input type="hidden" value="'+ID_NO+'"/><input type="hidden" value="'+FILE_NO+'"/><input type="hidden" value="'+FILENAME+'"/><input type="hidden" value="'+FILESIZE+'"/>';   
	 							}
							},
		 					loadtext: "處理中...",
		 					pgtext: "{0} / {1}"
					};
				}
				
			}
			function add_p(str){
				if(fstop.isNotEmpty(str) && fstop.isNotEmptyString(str) ){
					$("#ACTION_TYPE").val(str);
				}
				$("#ac_key").val('');
				$("#target").val('add_p');
				$("form").submit();
			}
			function deleteData(){
				var checkedNum = 0;
				$("#ID_NOS").val("");
				$("#FILE_NOS").val("");
				var serchs = {};
				var filenames = "";
				var filesizes = "";
				
				$(":checkbox").each(function(){
					if($(this).prop("checked") == true){
						if(checkedNum == 0){
							$("#ID_NOS").val($(this).next().val());
							$("#FILE_NOS").val($(this).next().next().val());
							filenames = $(this).next().next().next().val();
							filesizes = $(this).next().next().next().next().val()+"KB";
						}
						else{
							$("#ID_NOS").val($("#ID_NOS").val()+","+$(this).next().val());
							$("#FILE_NOS").val($("#FILE_NOS").val()+","+$(this).next().next().val());
							filenames += ","+$(this).next().next().next().val();
							filesizes += ","+$(this).next().next().next().next().val()+"KB";
						}
						checkedNum += 1;
					}
				});
				
				if($("#ID_NOS").val() == ""){
					alert("請選擇要刪除的檔案");
					return false;
				}
				if(confirm("確定要刪除選擇的檔案嗎?")){
					$("#ac_key").val('delete');
					$("#target").val('search');
					serchs['ID_NOS'] = $("#ID_NOS").val();
					serchs['FILE_NOS'] = $("#FILE_NOS").val();
					serchs['FILENAMES'] = filenames;
					serchs['FILESIZES'] = filesizes;
					serchs['action'] = $("#formID").attr('action');
					$("#serchStrs").val(JSON.stringify(serchs));
					$("form").submit();
				}
			}
		</script>
	</body>
</html>