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
				<html:form styleId="formID" action="/userlog_bank">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SERNO" styleId="SERNO"/>
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<bean:define id="userData" scope="session" name="login_form" property="userData"></bean:define>
<%-- 					<html:hidden property="USER_COMPANY" styleId="USER_COMPANY"/> --%>
					<fieldset>
						<legend>????????????</legend>
						<table id="search_tab">
							<tr>
								<td class="necessary header" style="width: 15%">????????????</td>
								<!-- ????????????????????? -->
							    <logic:equal name="userData" property="USER_TYPE" value="A">
								<td style="width: 450px">
								    <html:text styleId="TXTIME_1" property="TXTIME_1" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[TXTIME_2]] text-input datepicker"></html:text>
								    <html:text styleId="TXTIME_2" property="TXTIME_2" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twDateRange[TXTIME_1,TXTIME_2,3]] text-input datepicker"></html:text>
								</td>
								</logic:equal>
								
								<!-- ????????????????????? -->
							    <logic:equal name="userData" property="USER_TYPE" value="B">
							    <td style="width: 450px">
								    <html:text styleId="TXTIME_1" property="TXTIME_1" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[TXTIME_2]] text-input datepicker"></html:text>
								    <div style="display: none">
									    <html:text styleId="TXTIME_2" property="TXTIME_2" size="8" maxlength="8" value="" styleClass="validate[maxSize[8],minSize[8],twDate,twDateRange[TXTIME_1,TXTIME_2,3]] text-input datepicker"></html:text>
									</div>
								</td>
								</logic:equal>
								
								<td class="header" style="width: 15%">???????????????</td>
								<td>
									<html:text styleId="USER_COMPANY" readonly="true"   property="USER_COMPANY" styleClass="validate[required] text-input lock" ></html:text>
								</td>
							</tr>
							<tr>
								<td class="header">???????????????</td>
								<td>
									<html:select styleId="USERID" property="USERID">
										<html:option value="all">??????</html:option>
										<html:optionsCollection name="userlog_form" property="userIdList" label="label" value="value"></html:optionsCollection>
									</html:select>
								</td>
								<td class="header">????????????</td>
								<td>
									<html:select styleId="FUNC_ID" property="FUNC_ID">
										<html:option  value="all">??????</html:option>
										<logic:present name="userlog_form" property="funcList">
										<logic:iterate id="map" name="userlog_form" property="funcList">
											<logic:iterate id="map1" name="map">
												<!-- ???????????? -->
												<optgroup label="<bean:write name="map1" property="key"/>" >
												<!-- ???????????? -->
												<logic:present name="map1" property="value">
												<logic:iterate id="map2" name="map1" property="value">
													<option class="subFuncOpt" value="<bean:write name="map2" property="value"/>">&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="map2" property="label"/></option>
												</logic:iterate>
												</logic:present>
												</optgroup>
											</logic:iterate>	
										</logic:iterate>
										</logic:present>
									</html:select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;??????</label>
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var opt_Items;
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			$(document).ready(function () {
				blockUI();
				init();
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				getOpt_Items();
				setDatePicker();
				$("#TXTIME_1").datepicker("setDate", new Date());
				$("#TXTIME_1").val('0' + $("#TXTIME_1").val());
				$("#TXTIME_2").datepicker("setDate", new Date());
				$("#TXTIME_2").val('0' + $("#TXTIME_2").val());
				$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
				initGridOption();
				$("#resultData").jqGrid(gridOption);
				initSelect();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="userlog_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function getOpt_Items(){
				var rdata = {component:"bank_group_bo", method:"getOpt_Items"};
				var vResult =fstop.getServerDataExII(uri, rdata, false);
				if(window.console){console.log("vResult>>"+vResult);}
				if(fstop.isNotEmpty(vResult)){
					opt_Items = vResult;
				}
				if(window.console){console.log("opt_Items>>"+opt_Items);}
				
			}
			function initGridOption(){
				gridOption = {
						toppager: true,
						pagerpos: "left",
						datatype: "local",
		            	autowidth:true,
		            	height: 220,
		            	//sortable: true,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10,
						colNames:['????????????','????????????', '??????????????????', '???????????????', '??????????????????','????????????','????????????','?????????IP'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
// 							{name:'id.SERNO',index:'id.SERNO',align:'right',fixed:true,width: 100, formatter: 'number', formatoptions:{thousandsSeparator:",",decimalPlaces:0}},
// 							{name:'id.SERNO',index:'id.SERNO',align:'right',fixed:true,width: 100},
							{name:'TXTIME',index:'TXTIME',fixed:true,width: 160},
							{name:'id.USER_COMPANY',index:'id.USER_COMPANY',fixed:true,width: 95},
							{name:'id.USERID',index:'id.USERID',fixed:true,width: 95},
							{name:'UP_FUNC_ID',index:'UP_FUNC_ID',fixed:true,width: 150 ,formatter:func},
							{name:'FUNC_ID',index:'FUNC_ID',fixed:true,width: 150 ,formatter:func},
							{name:'OPITEM',index:'OPITEM',fixed:true,width: 150 ,formatter:op_item},
							{name:'USERIP',index:'USERIP',fixed:true,width: 95}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							var ser_no = "",userId = "" ,com_id = "" ; 
							var list = data.rows;
							for(var rowCount in list){
								ser_no = list[rowCount].id.SERNO;
								userId = list[rowCount].id.USERID;
								com_id = list[rowCount].id.USER_COMPANY;
								list[rowCount].BTN = '<button type="button" id="edit_' + ser_no + '" onclick="edit_p(this.id , \''+ser_no+'\' , \''+userId+'\' , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
								if(window.console){console.log("btn2>>"+list[rowCount].BTN );}
							}
						},
						loadComplete:function (data){
//							if(window.console){console.log("data>>"+data.page);}
							get_curPage(this ,data.page , null , null );
							noDataEvent(data);
						},
	//						?????????????????? e
						onSortCol:function (index, columnIndex, sortOrder){
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
	 					loadtext: "?????????...",
// 	 					20150530 edit by hugo req ACH ????????????????????? 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
						
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
// 					$("#USER_COMPANY").prop("disabled", false);
					blockUI();
					
					<logic:equal name="userData" property="USER_TYPE" value="B">
				        $("#TXTIME_2").val($("#TXTIME_1").val());
			        </logic:equal>
					
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					searchData(str);
					
					<logic:equal name="userData" property="USER_TYPE" value="B">
			            $("#TXTIME_2").val('');
		            </logic:equal>
					
					unblockUI();
				}
			}
			
			function searchData(str){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr = "component=userlog_bo&method=pageSearch&"+$("#formID").serialize();
				newOption.url = "/eACH/baseInfo?"+qStr;
				if(window.console){console.log("qStr>>"+qStr);}
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo ???????????????"1 / 1" ?????????????????????????????? ?????????????????????
				newOption.pgtext= "{0} / {1}";
				if(!fstop.isEmpty(str) && fstop.isNotEmptyString(str) && str =='back'){
					newOption.page = parseInt('<bean:write name="userlog_form" property="pageForSort"/>');
					resetSortname(newOption , 'TXTIME' , 'DESC' , false);
				}else{
					resetSortname(newOption , 'TXTIME' , 'DESC' , true);
					newOption.page = 1;
					$("#pageForSort").val(1);
				}
				$("#resultData").jqGrid(newOption);
			}
			function edit_p(str , id , id2 ,id3){
				$("#formID").validationEngine('detach');
				var tmp = {};
				tmp['SERNO']= id;
				tmp['USERID']= id2;
				tmp['USER_COMPANY']= id3;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
// 				var url = "${pageContext.request.contextPath}".replace("/eACH" , "")+$("#formID").attr("action")+"?"+$("#formID").serialize()+"&USER_COMPANY="+id3; 
// 				if(window.console){console.log("url>>"+url);}
// 				window.open(url , '_blank');
				$("form").submit();
			}
			
			function op_item (cellvalue, options, rowObject)
			{
				
				var rtnStr = "";
				if(fstop.isEmpty(opt_Items)){
					if(window.console){console.log("?????????????????????");}
					return rtnStr;
				}
				if(window.console){console.log("cellvalue>>"+cellvalue);}
				rtnStr = opt_Items[cellvalue];
				return rtnStr;
			}
			function func(cellvalue, options, rowObject){
				var rtnStr = "";
// 				if(window.console){console.log("rowObject>>"+rowObject.FUNC_ID);}
// 				if(window.console){console.log("cellvalue>>"+cellvalue);}
// 				if(window.console){console.log("options>>"+options);}
				rtnStr = cellvalue;
				if(rowObject.OPITEM == "I"){
					rtnStr = rowObject.FUNC_ID+"-????????????";
					return rtnStr;
				};
				if(rowObject.OPITEM == "J"){
					rtnStr = rowObject.FUNC_ID+"-????????????";
					return rtnStr;
				};
				
				
				return rtnStr;
		   // do something here
			}
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
// 					onPut('search');
				}
			}
		</script>
	</body>
</html>