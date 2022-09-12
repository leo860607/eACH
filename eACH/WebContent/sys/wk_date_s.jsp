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
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/cCalendar.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/jquery.alerts.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			#resultDataA_1, #resultDataA_2, #resultDataA_3, #resultDataA_4, #resultDataA_5,
			#resultDataA_6, #resultDataA_7, #resultDataA_8, #resultDataA_9, #resultDataA_10,
			#resultDataA_11, #resultDataA_12 {
				vertical-align: top;
			}
			
			#resultDataA, #resultDataB {
				width: 100%; 
				display: none;
				text-align:center;
			}
		</style>
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
				<html:form styleId="formID" action="/wk_date">
					<html:hidden property="ac_key" styleId="ac_key"/>
					<html:hidden property="target" styleId="target"/>
					<html:hidden property="TXN_DATE" styleId="TXN_DATE" value=""/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>
					<fieldset>
						<legend>查詢條件</legend>
						<table id="search_tab">
							<tr>
								<td class="necessary">
									民國&nbsp;
									<html:text name="wk_date_form" styleId="TW_YEAR" property="TW_YEAR" size="4" maxlength="4" styleClass="validate[required]"></html:text>
									&nbsp;年&nbsp;
									<html:select name="wk_date_form" styleId="TW_MONTH" property="TW_MONTH">
										<html:option value="">全部</html:option>
										<html:option value="01">一</html:option>
										<html:option value="02">二</html:option>
										<html:option value="03">三</html:option>
										<html:option value="04">四</html:option>
										<html:option value="05">五</html:option>
										<html:option value="06">六</html:option>
										<html:option value="07">七</html:option>
										<html:option value="08">八</html:option>
										<html:option value="09">九</html:option>
										<html:option value="10">十</html:option>
										<html:option value="11">十一</html:option>
										<html:option value="12">十二</html:option>
									</html:select>
									&nbsp;月
								</td>
							</tr>
							<tr>
								<td style="padding-top: 5px">
									<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/search.png"/>&nbsp;查詢</label>
									<logic:equal  name="userData" property="s_auth_type" value="A">
									<label class="btn" id="add" onclick="$('#targetYear').val($('#TW_YEAR').val());$('#prompt_div').dialog('open');"><img src="./images/calendar.png"/>&nbsp;產生全年營業日</label>
									</logic:equal>
								</td>
							</tr>
						</table>
						<div id="prompt_div" title="請輸入民國年份(ex.0104)：">
							<input type="text" id="targetYear"/>
						</div>
					</fieldset>
				</html:form>
			</div>
			<div id="rsPanel">
				<fieldset>
					<legend>查詢結果</legend>
					<!-- for all months -->
					<table id="resultDataA">
						<tr><td id="resultDataA_1"></td><td id="resultDataA_2"></td><td id="resultDataA_3"></td><td id="resultDataA_4"></td></tr>
						<tr><td id="resultDataA_5"></td><td id="resultDataA_6"></td><td id="resultDataA_7"></td><td id="resultDataA_8"></td></tr>
						<tr><td id="resultDataA_9"></td><td id="resultDataA_10"></td><td id="resultDataA_11"></td><td id="resultDataA_12"></td><td></td><td></td><td></td></tr>
					</table>
					<!-- for single month -->
					<table id="resultDataB">
						<tr><td id="resultDataB_1"></td></tr>
					</table>
				</fieldset>
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
			var monthName = ["","一","二","三","四","五","六","七","八","九","十","十一","十二"];
			
			$(document).ready(function () {
				blockUI();
				init();
				var ac_key = '<bean:write name="wk_date_form" property="ac_key"/>';
				if(ac_key == '' || ac_key == 'add' || ac_key == 'save' || ac_key == 'update' || ac_key == 'back'){
					onPut('search');
				}
				unblockUI();
	        });
			
			function init(){
				alterMsg();
				setDefaultTwYear();
				setDefaultTwMonth();
				initSearchs();
				$("#formID").validationEngine({promptPosition: "bottomRight"});
				$("#prompt_div").dialog({
					width: 330,
					autoOpen: false,
					modal: true,
					buttons: {
						"確認": createWholeYearData,
				        "取消": function() {
							$(this).dialog( "close" );
				        }
				    }
				});
			}
			
			function alterMsg(){
				var msg = '<bean:write name="wk_date_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function setDefaultTwYear(){
				if('<bean:write name="wk_date_form" property="TW_YEAR"/>' == ""){
					var dt = new Date();
					$("#TW_YEAR").val("0" + (dt.getFullYear() - 1911));
				}
			}
			
			function setDefaultTwMonth(){
				if('<bean:write name="wk_date_form" property="TW_MONTH"/>' == ""){
					var dt = new Date();
					$("#TW_MONTH").val(dt.getMonth()<9?"0"+(dt.getMonth()+1):(dt.getMonth()+1));
				}
			}
			
			function onPut(str){
				if(str == "search"){
					getSearch_condition('search_tab' , 'input , select' , 'serchStrs');
					if(!jQuery('#formID').validationEngine('validate')){
						return false;
					}
				}
				blockUI();
				var qStr = "?component=wk_date_bo&method=search_toJson&"+$("#formID").serialize();
				var dateMap = fstop.getServerDataExII(uri + qStr, null, false);
				//Clean all resultData
				$("#resultDataA td").html("");
				$("#resultDataB td").html("");
				createCalendars(dateMap);
				//Display resultData
				if($("#TW_MONTH").val() == ""){
					//Multiple months
					$("#resultDataA").show();
					$("#resultDataB").hide();
				}else{
					//Single month
					$("#resultDataB").show();
					$("#resultDataA").hide();
				}
				unblockUI();
			}

			function createCalendars(dateMap){
				if(dateMap != null){
					//Only IE9+ support 
					//var dataLength = Object.keys(dateMap).length;
					var dataLength = 0;
					for(var k in dateMap){
						dataLength++;
					}
					var calendarId, calendarTemplate, dateList, rowHtml, wdIndex, wd;
					var thisMonth;
					for(var strYM in dateMap){
						if(window.console){console.log("strYM>>"+strYM);}
						calendarId = "calendar_" + strYM;
						calendarTemplate = getCalendarTemplate(calendarId, strYM);
						thisMonth = parseMonth(strYM, 'int');
						if(window.console){console.log("thisMonth>>"+thisMonth);}
						//Insert into resultData
						if(dataLength > 1){
							//Multiple months
							$("#resultDataA_" + thisMonth).html(calendarTemplate);
						}else{
							//Single month
							$("#resultDataB_1").html(calendarTemplate);
						}
						
						//Generate rows
						dateList = dateMap[strYM];
						wdIndex = 7;
						wd = parseInt(dateList[0].WEEKDAY);
						rowHtml = "<tr>";
						while(wdIndex != wd){
							rowHtml += "<td>&nbsp;</td>";
							wdIndex = wdIndex % 7;
							wdIndex++;
						}
						for(var i = 0; i < dateList.length; i++){
							if(dateList[i].WEEKDAY == 7){
								rowHtml += "<tr>";
							}
							//產生每個日期按鈕
							rowHtml += getDateBtn(dateList[i]);
							wdIndex = dateList[i].WEEKDAY;
							if(dateList[i].WEEKDAY == 6){
								rowHtml += "</tr>";
							}
						}
						while(wdIndex != 6){
							rowHtml += "<td>&nbsp;</td>";
							wdIndex = wdIndex % 7;
							wdIndex++;
						}
						rowHtml += "</tr>";
						
						//Multiple months
						if(dataLength > 1){
							//UAT-20150203-04 若查詢全年營業日，則同一列每個月份(1,2,3,4月/5,6,7,8月/9,10,11,12月)的高度要相同
							if(window.console){console.log("THIS MONTH = " + strYM);}
							var maxHeight = 0, thisHeight = getCalendarHeight(dateList);
							if(thisMonth <= 4){
								maxHeight = getMaxCalendarHeight(parseTwYear(strYM), 1, 4, dateMap);
							}else if(thisMonth <= 8){
								maxHeight = getMaxCalendarHeight(parseTwYear(strYM), 5, 8, dateMap);
							}else{
								maxHeight = getMaxCalendarHeight(parseTwYear(strYM), 9, 12, dateMap);
							}
							if(window.console){console.log(thisHeight+"/"+maxHeight);}
							if(thisHeight < maxHeight){
								for(var i = 0; i < maxHeight - thisHeight; i++){
									rowHtml += getEmptyCalendarRow();
								}
							}
						}
						$("#" + calendarId).append(rowHtml);
					}//for end
					if(window.console){console.log("rowHtml>>"+rowHtml);}
				}
			}
			
			//key = 010301
			function getCalendarTemplate(calendarId, strYM){
				var year = strYM.substring(0,4);
// 				20150604 edit by hugo 解決08、09會回傳0的問題
// 				var month = monthName[parseInt(strYM.substring(4, strYM.length))];
				var month = monthName[parseInt(strYM.substring(4, strYM.length) ,10)];
				var calendar = "<table id='"+calendarId+"' class='cCalendar'><tr><th colspan='7'>" + year + "年&nbsp;" + month + "月</td></tr>";
				calendar += "<tr><td class='wd7'>日</td><td class='wd1'>一</td>";
				calendar += "<td class='wd2'>二</td><td class='wd3'>三</td>";
				calendar += "<td class='wd4'>四</td><td class='wd5'>五</td><td class='wd6'>六</td></tr>";
				calendar += "</table>";
				return calendar;
			}
			
			function getEmptyCalendarRow(){
				return "<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
			}
			
			function getDateBtn(dateObj){
				var isTxnDate = "";
				if(dateObj.IS_TXN_DATE == "N"){
					isTxnDate += " isNotTxnDate";
				}
				
				var btn = "<td class='dateBtn" + isTxnDate + "' onclick='edit_p(\""+dateObj.TXN_DATE+"\")'>";
				var day = dateObj.TXN_DATE.substring(6,8);
				//btn += parseInt(dateObj.TXN_DATE.substring(6,8));
				btn += day.substring(0,1)=="0"?day.substring(1,2):day;
				btn += "</td>";
				return btn;
			}
			
			function edit_p(txnDate){
				$("#formID").validationEngine('detach');
				$("#TXN_DATE").val(txnDate);
				$("#ac_key").val('edit');
				$("#target").val('edit_p');
				$("form").submit();
			}
			
			function createWholeYearData(){
				if($("#targetYear").val().substring(0,1) != "0"){
					alert("請輸入民國年(ex.0104)");return;
				}else{
					$("#TW_YEAR").val($("#targetYear").val());
				}
				
				if(fstop.isNotEmpty($("#targetYear").val()) && $("#formID").validationEngine("validate")){
					$("#ac_key").val('add');
					$("#target").val('search');
					$("form").submit();
				}
			}
			
			function parseTwYear(input){
				var year = input;
				if(year.length >= 4){
					year = year.substring(0,4);
					if(parseInt(year) > 999){
						year = "0" + (parseInt(year) - 1911);
					}
				}
				return year;
			}
			
			function parseMonth(input, type){
				var month = input + "";
				if(month.length >= 6){
					month = month.substring(4,6);
				}
				if(type == 'int'){
// 					20150604 edit by hugo 解決08、09會回傳0的問題
// 					month = parseInt(month);
					month = parseInt(month , 10);
				}
				return month;
			}
			
			function padding(input, str, count, dir){
				input = input + '';
				var result = "";
				var paddingCount = count - input.length;
				if(paddingCount > 0){
					for(var i = 0; i < paddingCount; i++){
						result += str;
					}
				}
				if(dir == "L"){
					result = result + input;
				}else if(dir == "R"){
					result = input + result;
				}
				return result;
			}
			
			//算出該月份的日曆排出來會有幾列
			function getCalendarHeight(dateList){
				var row = 0, weekDay = dateList[0].WEEKDAY % 7;
				var size = dateList.length;
				if(weekDay == 0 && size == 28){ row = 4; }
				else if(
					(weekDay == 6 && size == 30) ||
					((weekDay == 5 || weekDay == 6) && size == 31)
				){ row = 6; }
				else{ row = 5; }
				return row;
			}
			
			function getMaxCalendarHeight(twYear, startMon, endMon, dateMap){
				var maxHeight = 0, thisHeight = 0;
				for(var i = startMon; i <= endMon; i++){
					key = twYear + padding(i, "0", 2, "L");
					thisHeight = getCalendarHeight(dateMap[key]);
					if(maxHeight <= thisHeight){
						maxHeight = thisHeight;
					}
				}
				return maxHeight;
			}
			
			//初始化查詢條件
			function initSearchs(){
				var serchs = {};
				//if(window.console){console.log("initSearchs.serchStrs>>"+$("#serchStrs").val());}
				if(fstop.isNotEmptyString($("#serchStrs").val())){
					serchs = $.parseJSON($("#serchStrs").val()) ;
				}
				//if(window.console){console.log("serchs>>"+serchs);}
				for(var key in serchs){
					//if(window.console){console.log(key+"="+serchs[key]);}
					//模式一 所有查詢條件清空
					//$("#"+key).val("");
					//if(window.console){console.log(key+".val"+$("#"+key).val());}
					//模式二 塞入user當初查詢條件
					if(key!="ac_key"){
						$("#"+key).val(serchs[key]);
					}
				}
			}
		</script>
	</body>
</html>