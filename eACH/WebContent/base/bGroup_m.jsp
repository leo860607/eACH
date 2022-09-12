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
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<!-- NECESSARY END -->
	</head>
	<body>
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
				<html:form styleId="formID" action="/bank_group">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="BGBK_ID" styleId="BGBK_ID"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="header" style="width: 18%">操作行代號&nbsp;&nbsp;&nbsp;</td>
								<td>
									<html:select styleId="OPBK_ID" property="OPBK_ID">
										<html:option value="">全部</html:option>
										<logic:present name="bank_group_form" property="opbkIdList">
											<html:optionsCollection name="bank_group_form" property="opbkIdList" label="label" value="value"></html:optionsCollection> 
										</logic:present>
									</html:select>
								</td>
								<td class="header" style="width: 18%">清算行代號&nbsp;&nbsp;&nbsp;</td>
								<td>
									<html:select styleId="CTBK_ID" property="CTBK_ID">
										<html:option value="">全部</html:option> 
										<logic:present name="bank_group_form" property="ctbkIdList">
											<html:optionsCollection name="bank_group_form" property="ctbkIdList" label="label" value="value"></html:optionsCollection> 
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal  name="userData" property="s_auth_type" value="A">
									<label class="btn" id="new" onclick="onPut(this.id)"><img src="./images/add.png"/>&nbsp;新增</label>
									</logic:equal>
								</td>
							</tr>
						</table>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<table id="resultData">
						<thead>
							<tr>
								<th>檢視明細</th>
								<th>總行代號</th>
								<th>總行名稱</th>
								<th>銀行屬性</th>
								<th>央行帳號</th>
								<th>交換所代號</th>
								<th>操作行代號</th>
								<th>操作行名稱</th>
								<th>清算行代號</th>
								<th>清算行名稱</th>
								<th>啟用日期</th>
								<th>停用日期</th>
								<th>發動行手續費分行代號</th>
								<th>扣款行手續費分行代號</th>
								<th>入帳行手續費分行代號</th>
								<th>銷帳行手續費分行代號</th>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
		
			$(document).ready(function () {
// 				blockUI();
				init();
				unblockUI();
				
	        });
			
			function init(){
				var ac_key = '<bean:write name="bank_group_form" property="ac_key"/>';
				alterMsg();
				initGridOption();
				if(fstop.isNotEmptyString(ac_key) ){
					onPut('back');
				}else{
					onPut('search');
				}
			} 
			function initGridOption(){
				gridOption = {
						//避免jqGrid自發性的submit
						datatype: 'local',
						autowidth:true,
		            	height: 250,
		            	shrinkToFit: true,
		            	sortable: true,
						sortname: 'BGBK_ID',
						sorttype: 'text',
						gridview: true,
						loadonce: true,
		            	rowNum: 10000,
// 		            	hiddengrid: true,
		            	colNames:['檢視明細','總行代號','總行名稱','銀行屬性','央行帳號','交換所代號','操作行代號','操作行名稱','清算行代號','清算行名稱','啟用日期','停用日期','發動行手續費分行代號','扣款行手續費分行代號','入帳行手續費分行代號','銷帳行手續費分行代號','是否辦理EACH'],
		            	colModel: [
		            	    {name:'BTN', fixed: true, align: 'center', width:60, sortable: false},
							{name:'BGBK_ID', fixed: true, width:80},
							{name:'BGBK_NAME', fixed: true},
							{name:'BGBK_ATTR', fixed: true, width:80},
							{name:'CTBK_ACCT', fixed: true, width:70},
							{name:'TCH_ID', fixed: true, width:85},
							{name:'OPBK_ID', fixed: true, width:75},
							{name:'OPBK_NAME', fixed: true},
							{name:'CTBK_ID', fixed: true, width:75},
							{name:'CTBK_NAME', fixed: true},
							{name:'ACTIVE_DATE', fixed: true, width:80},
							{name:'STOP_DATE', fixed: true, width:80},
							{name:'SND_FEE_BRBK_ID', fixed: true, width:140},
							{name:'OUT_FEE_BRBK_ID', fixed: true, width:140},
							{name:'IN_FEE_BRBK_ID', fixed: true, width:140},
							{name:'WO_FEE_BRBK_ID', fixed: true, width:140},
							{name:'IS_EACH', fixed: true, width:140}
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
								data[rowCount].BTN = '<button type="button" id="edit_' + data[rowCount].BGBK_ID + '" onclick="edit_p(this.id)"><img src="./images/edit.png"/></button>';
							}
						},
						onSortCol: function (index, columnIndex, sortOrder) {
							if(window.console){console.log("index>>"+index);}
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
					    },
						loadtext: "處理中..."
							
				};
				$("#resultData").jqGrid(gridOption);
			}
			
			function onPut(id){
				blockUI();
				if(id == "search" || id == "back" ){
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');//e
					//考慮資料量大的問題，查詢資料使用ajax
					$("#resultData").jqGrid('GridUnload');
					var newOption = gridOption;
					newOption.url = "/eACH/baseInfo?component=bank_group_bo&method=search_toJson&"+$("#formID").serialize();
					newOption.datatype = "json";
					newOption.mtype = 'POST';
					if(window.console){console.log("id>>"+id);}
					if(!fstop.isEmpty(id) && fstop.isNotEmptyString(id) && id =='back'){
						resetSortname(newOption , 'BGBK_ID' , 'ASC' , false);
					}else{
						
						resetSortname(newOption , 'BGBK_ID' , 'ASC' , true);
					}
					$("#resultData").jqGrid(newOption);
					return true;
				}
				
				if(id == "new"){
// 					cleanForm(document.getElementById(id));
					cleanFormNE($("#search_tab"));
				}
				$("#ac_key").val(id);
				$("#target").val('search');
				$("form").submit();
			}
			
			
			function edit_p(id){
				var key = id.split("_")[1];
				id = id.split("_")[0];
				var tmp = {};
				tmp["BGBK_ID"] = key ;
				$("#edit_params").val(JSON.stringify(tmp));
				onPut(id);
			}
			
			function alterMsg(){
				var msg = '<bean:write name="bank_group_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			
		</script>
	</body>
</html>