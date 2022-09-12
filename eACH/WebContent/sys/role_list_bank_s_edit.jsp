<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/>-編輯</title>
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<style type="text/css">
		.sl_close{
		-moz-border-top-left-radius:10px;
		-webkit-border-top-left-radius:10px;
		border-top-left-radius:10px;
/* 		border:1px solid black; width:150px; padding:5px; */
		border-bottom:1px solid threedlightshadow; width:800px; padding:5px;
/* 		background:#7081B9;color:#fff;border:1px solid #4c566c;border-top:0 none;line-height:100%; */
		 }
		.sl_open{
/* 		border:1px solid black; width:150px; padding:5px; */
		border-bottom:1px solid black; width:800px; padding:5px;
/* 		background:#566486;color:#fff;border:1px solid #4c566c;border-top:0 none;line-height:100%; */
		}
		.tx_close{
	color: black;
		 }
		.tx_open{
		color: blue;
		}
		.sub_li{
		   list-style-type:none;margin:2px;padding-left:15px
		 }
		</style>
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
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
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">編輯</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/role_list_bank" method="POST">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="selected_SAuth" value=""/>
					<html:hidden styleId="ROLE_TYPE" property="ROLE_TYPE" />
					<input type="hidden" id="old_RoleType" value='<bean:write name="role_list_form" property="ROLE_TYPE"/>'>
					<table>
						<tr>
							<td class="header necessary" style="width: 250px">群組代號</td>
							<td>
								<html:text property="ROLE_ID" readonly="true"   size="10" maxlength="10"  styleClass="validate[required] text-input lock"></html:text>
							</td>
							<td class="header necessary">群組名稱</td>
							<td>
								<html:text property="ROLE_NAME"   size="10" maxlength="10"  styleClass="validate[required] text-input lock"></html:text>
							</td>
						</tr>
						<tr>
							<td class="header necessary">群組類型</td>
							<td>
							<logic:equal name="role_list_form" property="ROLE_TYPE" value="A">票交所</logic:equal>
							<logic:equal name="role_list_form" property="ROLE_TYPE" value="B">銀行</logic:equal>

							</td>
							<td class="header necessary">是否使用Ikey</td>
							<td>
<%-- 								<html:checkbox property="USE_IKEY" value="Y">使用Ikey</html:checkbox> --%>
								是<html:radio disabled="true" property="USE_IKEY" value="Y"></html:radio>
								否<html:radio disabled="true" property="USE_IKEY" value="N"></html:radio>
							</td>
						</tr>
						<tr>
							<td class="header">群組說明</td>
							<td colspan="3">
								<html:text property="DESC" size="67" maxlength="67" styleClass="validate[maxSize[66]] text-input lock" ></html:text>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<label class="btn" id="back" onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
					<div >
						<table>
							<tr >
								<td align="center" >群組所屬功能清單</td>
							</tr>
							<tr align="left">
								<td align="left"> <img id="opfm" src="./images/addII.png"/><div id="allOp" class ="tx_close" align="left" style="display: inline;margin-left: 10px;width: 10px;" >全部展開</div> </td>
							</tr>
							<tr>
								<td>
									<logic:iterate id="map1" name="role_list_form" property="subItemFuncsII" indexId="num">
									<bean:define id="upId" name="map1" property="upvalue" ></bean:define>
									<!--主功能名稱及checkbox -->
									 <div  id="sub_lb<%=num %>" class ="sl_close"   style="margin-left: 50px;">
											<img id="fm<%=num %>" src="./images/addII.png"/>
											<html:multibox styleId="<%=upId.toString() %>"  onclick = "clickSFunc(this)" property="selected_MFuncs" >   
											 	<bean:write name="map1" property="upvalue"/>
											</html:multibox> 
											 	<bean:write name="map1" property="uplabel"/>
									 </div>
									<!-- 所有子功能項目checkbox區塊 -->
									 <div style="margin-left: 100px;">
										 <logic:iterate id="map" name = "map1" property="subItem" indexId="numII">
										 <bean:define id="htmlId" name="map" property="htmlId"></bean:define>
<!-- 										不得已 不明情況logic:equal 在此無法發揮作用 故使用jsp -->
<%-- 													<logic:equal name="nameOfIndexIdVar" value="0">123</logic:equal> --%>
<%-- 													<logic:equal name="<%=numII.toString().trim()%>" value="0">456</logic:equal> --%>
										<%
										if(numII==0){
										%>
										<div style="display: inline;margin-left: 30px;">功能項目</div> <div style="display: inline;width:450px;margin-left: 10px;float:right;">功能權限</div>
										<%
										}
										%>
									<!-- 非停用的才顯示 -->
<%-- 										 <logic:equal name="map" property="is_used" value="Y"> --%>
									<!--每列div -->
									<div style="margin-bottom: 3px;border-bottom:1px solid window ; overflow: hidden;width: 550px ;margin-left: 80px; ">
									 	<!-- 子功能項目checkbox -->
									 	<div style="display: inline;width:200px; ">
											 <html:multibox styleId="<%=htmlId.toString() %>" onclick="subFuncItemCheck(this)" property="selected_SFuncs">  
											 	<bean:write name="map" property="value"/>
											 </html:multibox> 
											 	<bean:write name="map" property="label"/>
									 	</div>
									<!-- 子功能項目權限 -->
<!-- 									 	<div  style="display: inline;width:300px;margin-left: 10px;float:right; word-wrap: break-word; word-break: normal;"> -->
									 	<div  style="display: inline;width:200px;margin-left: 10px;float:right;">
											 <input type="radio" id='<bean:write name="htmlId"/>_ra' name = '<bean:write name="map" property="value"/>' value="R">
											   查詢
											 <input type="radio" id='<bean:write name="htmlId"/>_ra' name = '<bean:write name="map" property="value"/>' value="A">
											   異動
											 <logic:notEqual name="map" property="is_used" value="Y">
										   	 	<font color="red">&nbsp;(已停用) </font>
										     </logic:notEqual>
									 	</div>
									 </div>	
<%-- 										 </logic:equal> --%>
										 </logic:iterate>
									 </div>
									</logic:iterate>
								</td>
							</tr>
						</table>
					</div>
				</html:form>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			$(document).ready(function () {
				blockUI();
				init();
				disabledForm('<bean:write name = "login_form" property="userData.s_auth_type"/>');
				unblockUI();
	        });
			
			function onPut(str,isValidate){
					$("#ac_key").val(str);
					$("#target").val('search');
					checkRadio();
					if(str == "delete"){
						if(!confirm("是否確定刪除")){
							return false;
						}
					}
					if(isValidate){
						if(!jQuery('#formID').validationEngine('validate',{promptPosition: "bottomLeft"})){
							return false;
						}
					}else{
						$("#formID").validationEngine('detach');
					}
					$("form").submit();
			}	
			
			function init(){
				alterMsg();
// 				$("#formID").validationEngine({promptPosition: "bottomLeft"});
				setDatePicker("#ACTIVE_DATE", 0);
				initSlideToggle();
				initSelectedAuth();
				lockAllFunc();
			}	
			
			function alterMsg(){
				var msg = '<bean:write name="role_list_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}	
			
			function back(str){
// 				$("#formID").validationEngine('detach');
				onPut(str);
			}
			function lockAllFunc(){
				$(":checkbox").attr("disabled" , true);
				$(":radio").attr("disabled" , true);
			}

			function chg_rest(value){
				if(window.console) console.log(value);
				$("input[name='REST_role_list']").val(value);
			}
			
function initSlideToggle(){
				
// 				全部展開、收合的區塊
				$("#allOp").click(function() {
// 					$("[id^='sub_lb']").click();
					if($(this).hasClass("tx_close")){
						$(".sl_close").next('div').slideToggle("normal");
						$("[id^='sub_lb']").removeClass();
						$("[id^='sub_lb']").addClass("sl_open");
						$(this).html("全部收合");
						$(this).addClass("tx_open");
						$(this).removeClass("tx_close");
				    	$("#opfm").attr("src","./images/minusII.png");
				    	$("[id^='fm']").attr("src","./images/minusII.png");
					}else{
						$(".sl_open").next('div').slideToggle("normal");
						$("[id^='sub_lb']").removeClass();
						$("[id^='sub_lb']").addClass("sl_close");
						$(this).html("全部展開");
						$("#opfm").attr("src","./images/addII.png");
						$("[id^='fm']").attr("src","./images/addII.png");
						$(this).addClass("tx_close");
						$(this).removeClass("tx_open");
					}
				});
					
	// 				針對作業項目內checkbox click時不去觸發父元素的click
					$("[id^='sub_lb']").each(function(index) {
						$(this).next('div').hide();
						$(this).children("input:checkbox").click(function(event) {
							event.stopPropagation();
					});
	// 				作業項目的click事件	
					$(this).click(function() {
					    if($(this).hasClass("sl_close") ){
					    	$(this).next('div').slideToggle("normal");
					    	$("#fm"+index).attr("src","./images/minusII.png");
							$(this).addClass("sl_open");
							$(this).removeClass("sl_close");
						}else{
							$(this).next('div').slideToggle("normal");
					    	$("#fm"+index).attr("src","./images/addII.png");
							$(this).addClass("sl_close");
							$(this).removeClass("sl_open");
						}
					});	
				
				});
			}
			
// 			function checkB(index){
				
// 			} 
			function initSelectedAuth(){
				var authString = <bean:write filter="false" name="role_list_form" property="selected_SAuth" />;
				
				for(var i in authString){
					var innerAry = authString[i];
					for(var key in innerAry){
						$("[id^="+key+"_ra]").each(function(){
// 							$(this).prop("disabled" , false);
							if($(this).val()==innerAry[key]){
								$(this).prop("checked" , true);
							}
						});
					}
				}
			}
			
			function checkRadio(){
				var ary ={};
				$('input[type=radio]:checked').each(function(index , obj){
					ary[this.name] = this.value;
				});
				if(window.console){console.log("ary2"+JSON.stringify(ary) );}
				$("input[name=selected_SAuth]").val(JSON.stringify(ary));
			}
			
			function clickSFunc(obj){
				var id = $(obj).attr('id');
				if ($(obj).prop("checked")){
					$("input[id^="+id+"_]").each(function(){
						 $(this).not(':radio').prop("checked", true);
						 if($(this).is(':radio')){
							 $(this).parent().children().first().prop('checked',true);
						 }
						 $(this).prop("disabled", false);
					});
				}else{
					$("input[id^="+id+"_]").each(function(){
						$(this).prop("checked", false);
// 						$(this).prop("disabled", true);
					});
				}
				
			}
			function subFuncItemCheck(obj){
				var id = $(obj).attr('id');
				var menu_id = id.substring(0,id.indexOf("_")) ;
				var isunclickMenu = true;
				$("[id^="+menu_id+"_]").each(function(){
					if($(this).not(':radio').prop("checked")){
						isunclickMenu= false;
					}
				});
// 				功能項目被勾選時 對應的radio disable要解除 預設為查詢為checked
				if ($(obj).prop("checked")){
					$("[id^="+id+"_ra]").each(function(index){
						if(index==0){
							$(this).prop("checked", true);
						}
						$(this).prop("disabled", false);
					});
				}else{
					$("[id^="+id+"_ra]").each(function(index){
						$(this).prop("checked", false);
						$(this).prop("disabled", true);
					});
				}
// 				檢核如果功能項目群組有一項被勾選 該作業項目就要勾選 ，如果都沒有該所屬作業項目則取消勾選
				if(isunclickMenu){
					$("#"+menu_id).prop("checked", false);
				}else{
					$("#"+menu_id).prop("checked", true);
				}
			}
			
		</script>
	</body>
</html>