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
			<div id="opPanel" >
				<html:form styleId="formID" action="/userlog">
					<html:hidden property="ac_key" styleId="ac_key" />
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="SERNO" styleId="SERNO"/>
					<html:hidden styleId="USER_TYPE" property="USER_TYPE"  />
					<html:hidden property="serchStrs" styleId="serchStrs" />
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="fc_type" styleId="fc_type"/>
					<input type="hidden" id="old_RoleType" value='<bean:write name="userlog_form" property="ROLE_TYPE"/>'>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
					<logic:equal name="userlog_form" property="fc_type" value="A">
							<tr>
								<td class="necessary header" style="width: 150px">操作日期</td>
								<td style="width: 350px">
								    <html:text styleId="TXTIME_1" property="TXTIME_1" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[TXTIME_2]] text-input datepicker"></html:text>~
									<html:text styleId="TXTIME_2" property="TXTIME_2" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twDateRange[TXTIME_1,TXTIME_2,3]] text-input datepicker"></html:text>
								</td>
								<td class="header" style="width: 150px">操作行代號  </td>
								<td>
									0188888
									<html:hidden styleId="USER_COMPANY" property="USER_COMPANY"  value = "0188888" />
								</td>
							</tr>
							<tr>
								<td class="header">用戶代號</td>
								<td>
									<html:select styleId="USERID" property="USERID">
										<html:option value="">全部</html:option>
										<logic:present name="userlog_form" property="userIdList">
											<html:optionsCollection name="userlog_form" property="userIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
								<td class="header">功能名稱</td>
								<td colspan="3">
									<html:select styleId="FUNC_ID" property="FUNC_ID">
										<html:option value="">全部</html:option>
										<logic:present name="userlog_form" property="funcList">
										<logic:iterate id="upFunc" name="userlog_form" property="funcList">
											<!-- 作業模組 -->
											<option class="upFuncOpt" value="<bean:write name="upFunc" property="FUNC_ID"/>"><bean:write name="upFunc" property="FUNC_NAME"/></option>
											<!-- 功能項目 -->
											<logic:present name="upFunc" property="SUB_LIST">
											<logic:iterate id="subFunc" name="upFunc" property="SUB_LIST">
												<option class="subFuncOpt" value="<bean:write name="subFunc" property="FUNC_ID"/>">&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="subFunc" property="FUNC_NAME"/></option>
											</logic:iterate>
											</logic:present>
										</logic:iterate>
										</logic:present>
									</html:select>
								</td>
							</tr>
							</logic:equal>
<!-- 					我是分隔線 -->
							<logic:greaterEqual name="userlog_form" property="fc_type" value="B">
							<tr>
								<td class="necessary header" style="width: 150px">操作日期</td>
								<td style="width: 350px">
								    <html:text styleId="TXTIME_1" property="TXTIME_1" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[TXTIME_2]] text-input datepicker"></html:text>~
									<html:text styleId="TXTIME_2" property="TXTIME_2" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twDateRange[TXTIME_1,TXTIME_2,3]] text-input datepicker"></html:text>
								</td>
								<td class="header">用戶代號</td>
								<td>
									<html:select styleId="USERID" property="USERID">
										<html:option value="">全部</html:option>
										<logic:present name="userlog_form" property="userIdList">
											<html:optionsCollection name="userlog_form" property="userIdList" label="label" value="value"></html:optionsCollection>
										</logic:present>
									</html:select>
								</td>
							</tr>
							
							<tr>
								<td class="header" style="width: 150px">用戶所屬單位  </td>
								<td>
										<html:select styleId="opt_id" property="opt_id" onchange="getBgbk_List(this.value)" >
											<html:option value="">全部</html:option>
											<html:optionsCollection name="userlog_form" property="opt_bankList" label="label" value="value"></html:optionsCollection>
										</html:select>
										<html:hidden styleId="USER_COMPANY" property="USER_COMPANY"  value = "" />
								</td>
								<td class="header">功能名稱</td>
								<td >
									<html:select styleId="FUNC_ID" property="FUNC_ID">
										<html:option value="">全部</html:option>
										<logic:present name="userlog_form" property="funcList">
										<logic:iterate id="upFunc" name="userlog_form" property="funcList">
											<!-- 作業模組 -->
											<option class="upFuncOpt" value="<bean:write name="upFunc" property="FUNC_ID"/>"><bean:write name="upFunc" property="FUNC_NAME"/></option>
											<!-- 功能項目 -->
											<logic:present name="upFunc" property="SUB_LIST">
											<logic:iterate id="subFunc" name="upFunc" property="SUB_LIST">
												<option class="subFuncOpt" value="<bean:write name="subFunc" property="FUNC_ID"/>">&nbsp;&nbsp;&nbsp;&nbsp;<bean:write name="subFunc" property="FUNC_NAME"/></option>
											</logic:iterate>
											</logic:present>
										</logic:iterate>
										</logic:present>
									</html:select>
								</td>
								
							</tr>
<!-- 							<tr> -->
							
<!-- 								<td class="header">用戶所屬單位 </td> -->
<!-- 								<td colspan="3"> -->
<%-- 									<html:select styleId="USER_COMPANY" property="USER_COMPANY" onchange="resetUserId(this)" > --%>
<%-- 										<html:option  value="">全部</html:option> --%>
<%-- 									</html:select> --%>
<!-- 								</td> -->
								
<!-- 							</tr> -->
                            <tr>
                                <td class="header">群組類型</td>
                                <td>
                                                                                                             銀行<html:radio property="ROLE_TYPE" value="B" onclick="role_type(this.value);"></html:radio>
                                                                                                             代理業者<html:radio property="ROLE_TYPE" value="C" onclick="role_type(this.value);"></html:radio>
                                </td>                            
                            </tr>
							</logic:greaterEqual>
							
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
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var gridOption;
			var opt_Items;
// 			var user_type = $("#USER_TYPE").val();
			var user_type = $("#fc_type").val();
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
				if('<bean:write name="userlog_form" property="TXTIME_1"/>' == "" &&　'<bean:write name="userlog_form" property="TXTIME_2"/>' == ""){
					$("#TXTIME_1").datepicker("setDate", new Date());
					$("#TXTIME_1").val('0' + $("#TXTIME_1").val());
					$("#TXTIME_2").datepicker("setDate", new Date());
					$("#TXTIME_2").val('0' + $("#TXTIME_2").val());
				}else{
					var txtime1 = '<bean:write name="userlog_form" property="TXTIME_1"/>';
					var txtime2 = '<bean:write name="userlog_form" property="TXTIME_2"/>';
// 					alert(txtime1+'  '+txtime2);
					$("#TXTIME_1").val(txtime1);
					$("#TXTIME_2").val(txtime2);
				}
				if('<bean:write name="userlog_form" property="FUNC_ID"/>' != ""){
					var funcid = '<bean:write name="userlog_form" property="FUNC_ID"/>';
					$("#FUNC_ID").val(funcid);
				}
				
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
		            	rowNum: 8,
						colNames:['檢視明細','交易時間', '所屬單位代號', '操作者代號', '管理功能項目','交易代號','操作項目','操作者IP'],
		            	colModel: [
							{name:'BTN',index:'BTN',align:'center',fixed:true,width: 100,sortable:false}, 
// 							{name:'id.SERNO',index:'id.SERNO',align:'right',fixed:true,width: 100},
							{name:'TXTIME',index:'TXTIME',fixed:true,width: 180},
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
							var ser_no = "",userId = "" ,com_id="" ; 
							var list = data.rows;
							for(var rowCount in list){
								ser_no = list[rowCount].id.SERNO;
								userId = list[rowCount].id.USERID;
								com_id = list[rowCount].id.USER_COMPANY;
								list[rowCount].BTN = '<button type="button" id="edit_' + ser_no + '" onclick="edit_p(this.id , \''+ser_no+'\' , \''+userId+'\' , \''+com_id+'\')"><img src="./images/edit.png"/></button>';
								if(window.console){console.log("btn>>"+list[rowCount].BTN );}
							}
						},
						loadComplete:function (data){
// 							if(window.console){console.log("data>>"+data.page);}
							get_curPage(this ,data.page , null , null );
							noDataEvent(data);
						},
// 						紀錄排序欄位 e
						onSortCol:function (index, columnIndex, sortOrder){
							$("#sortname").val(index);
						    $("#sortorder").val(sortOrder);
							get_curPage(this ,null , null);
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
// 						   e
						onPaging: function(data ) {
							$(this).setGridParam({ postData: { isSearch: 'N' } });
						},
						
						loadtext: "處理中...",
// 	 					20150530 edit by hugo req ACH 初始化要頁數要 1/1
// 	 					pgtext: "{0} / {1}"
	 					pgtext: "1 / 1" 
				};
			}
			
			function onPut(str){
				if($("#formID").validationEngine("validate")){
					$("#USER_COMPANY").prop("disabled", false);
					blockUI();
// 					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					
					searchData(str);
					unblockUI();
				}
			}
			
			function searchData(str){
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				var qStr ="";
// 				alert("user_type>>"+user_type);
				if(user_type=="A"){
// 					userlog.do?USER_TYPE=A&
					getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?USER_TYPE=A&");
// 					qStr = "component=userlog_bo&method=pageSearch&USER_TYPE="+user_type+"&serchStrs="+$("#serchStrs").val();
// 					qStr = "component=userlog_bo&method=pageSearch&"+$("#formID").serialize();
				}else{
					getSearch_conditionII('search_tab' , 'input , select' , 'serchStrs' , "?USER_TYPE=B&");
// 					qStr = "component=userlog_bo&method=pageSearch&serchStrs="+$("#serchStrs").val()+"&action="+$("#formID").attr('action');
// 					qStr = "component=userlog_bo&method=pageSearch&serchStrs="+$("#formID").serialize();
// 					qStr = "component=userlog_bo&method=pageSearch&"+$("#formID").serialize();
				}
				qStr = "component=userlog_bo&method=pageSearch&"+$("#formID").serialize();
				if(window.console){console.log("qStr>>"+qStr);}
// 				var qStr = "component=userlog_bo&method=pageSearch&"+$("#formID").serialize();;
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
// 				20150530 add by hugo 因初始化為"1 / 1" 查詢時要將設定值恢復 否則頁數不會換
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
// 				$("#SERNO").val(id) ;
// 				var user_type = $("#USER_TYPE").val();
				var user_type = $("#fc_type").val();
				if(window.console){console.log("id3>>"+id3);}
// 				$("#USER_COMPANY").val(id3) ;
				var tmp = {};
				tmp['SERNO']= id;
				tmp['USERID']= id2;
				tmp['USER_COMPANY']= id3;
				$("#edit_params").val(JSON.stringify(tmp));
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
// 				var url = "${pageContext.request.contextPath}".replace("/eACH" , "")+$("#formID").attr("action")+"?USERID="+id2+"&USER_COMPANY="+id3+"&SERNO="+id+"&USER_TYPE="+user_type+"&ac_key=edit&target=edit_p"; 
// 				if(window.console){console.log("url>>"+url);}
// 				window.open(url , '_blank');
				$("form").submit();
			}
			function resetUserId(cid , user_type){
				var com_id = "";
				if(user_type =="A"){
					com_id = $("#USER_COMPANY").val();
				}else{
// 					com_id = $(obj).find(":selected").val();
                    com_id = cid;
                    $("#USER_COMPANY").val(com_id);
				}
				var rdata = {component:"userlog_bo", method:"getUserIdListByComId" , com_id:com_id } ;
				fstop.getServerDataExII(uri, rdata, false, showUsers);
			}
			function showUsers(obj){
				if(window.console){console.log("obj>>"+obj);}
				var select = $("#USERID");
				select.children().remove();
				select.append($("<option></option>").attr("value", "").text("全部"));
				for( var key in obj ){
					select.append($("<option></option>").attr("value", obj[key].value).text(obj[key].label));
				}
				if(document.getElementById("USERID").length == 1){
					alert("用戶所屬單位 "+$("#opt_id").val()+"  尚未建立用戶代號");
				}
			}
			
			
			
			//取得該操作行所代理的總行清單
			function getBgbk_List(opbkId){
				var s_bizdate = $("#TXTIME_1").val();
				var e_bizdate = $("#TXTIME_2").val();
// 				if(opbkId == '' || opbkId == "all"){
// 					$("#USER_COMPANY option:not(:first-child)").remove();
// 				}else{
// // 					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId};
// 					var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate , e_bizdate:e_bizdate};
// 					fstop.getServerDataExII(uri, rdata, false, creatBgBkList);
// 				}
				resetUserId(opbkId);
			}
			
			function creatBgBkList(obj){
				var select = $("#USER_COMPANY");
				$("#USER_COMPANY option:not(:first-child)").remove();
				if(obj.result=="TRUE"){
					var dataAry =  obj.msg;
					for(var i = 0; i < dataAry.length; i++){
						select.append($("<option></option>").attr("value", dataAry[i].BGBK_ID).text(dataAry[i].BGBK_ID + " - " + dataAry[i].BGBK_NAME));
					}
				}else if(obj.result=="FALSE"){
					alert(obj.msg);
				}
			}
			
			function initSelect(){
				if(window.console)console.log("ac_key"+$("#ac_key").val());
				if(fstop.isNotEmptyString($("#ac_key").val()) ){
					onPut('back');
				}else{
// 					onPut('search');
				}
			}
			
			
			function op_item (cellvalue, options, rowObject)
			{
				
				var rtnStr = "";
				if(fstop.isEmpty(opt_Items)){
					if(window.console){console.log("對映檔查詢失敗");}
					return rtnStr;
				}
				rtnStr = opt_Items[cellvalue];
				return rtnStr;
			}
			
			function func(cellvalue, options, rowObject){
				var rtnStr = "";
				rtnStr = cellvalue;
				if(rowObject.OPITEM == "I"){
					rtnStr = rowObject.FUNC_ID+"-登入作業";
					return rtnStr;
				};
				if(rowObject.OPITEM == "J"){
					rtnStr = rowObject.FUNC_ID+"-登出作業";
					return rtnStr;
				};
				
				
				return rtnStr;
		   // do something here
			}
			
			function role_type(role){
				var old_val = $("#old_RoleType").val();
				if(role != old_val){
					resetUserCom(role);
					$("#old_RoleType").val(role);
				}
				
			}
			
			function resetUserCom(role){
				var user_type = role;
				initUserCompany(user_type);
				
				var id_list = $("#USERID");
				id_list.children().remove();
				id_list.append($("<option></option>").attr("value", "").text("全部"));
				
				$("#USER_COMPANY").val("");
				resetFuncList(role);
			}
			
			function initUserCompany(user_type){
				var rdata = {component:"each_user_bo", method:"getBgbkListByUser_Type" , user_type:user_type} ;
				if(user_type != "A"){
					fstop.getServerDataExII(uri, rdata, false, showUserCompany);
				}
			}
			
			function showUserCompany(obj){
				var select = $("#opt_id");
				select.children().remove();
				select.append($("<option></option>").attr("value", "").text("全部"));
				for( var key in obj ){
					select.append($("<option></option>").attr("value", obj[key].value).text(obj[key].label));
				}
				
			}
			
			function resetFuncList(role){
				$.ajax({
					type:'POST',
					url:"/eACH/baseInfo?component=userlog_bo&method=getFuncListBy_roleId&role_type="+role,
					async:false,
					dataType:'text',
					success:function(result){
						if(!result  == ''){
							
							var select = $("#FUNC_ID");
							select.children().remove();
							select.append($("<option></option>").attr("value", "").text("全部"));
							
							var contact = JSON.parse(result);
							
							for(var key in contact){
// 								
								for(var k in contact[key]){
									select.append($("<option></option>").text(k));
// 									
									for(var j in contact[key][k]){
										select.append($("<option></option>").attr("value", contact[key][k][j].value).text('    '+contact[key][k][j].label));
// 										
									}
								}
							}
							
						}
					}
				})
			}
		</script>
	</body>
</html>