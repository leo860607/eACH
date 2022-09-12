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
		<title><bean:write name="login_form" property="userData.s_func_name"/>-明細</title>
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
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<!-- NECESSARY END -->
		<style type="text/css">
			.DATA_GROUP_1 {background-color: #FFD6C2;width:25%}
			.DATA_GROUP_2 {background-color: #74b6e3;width:25%}
			.DATA_GROUP_V_2 {word-break: break-all}
			
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
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_func_name"/></a>
				<a href="#">檢視明細</a>
			</div>
			<div id="dataInputTable">
				<html:form styleId="formID" action="/onblocktab">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="sourcePage" styleId="sourcePage"/>
					<html:hidden property="searchCondition" styleId="searchCondition"/>
					<html:hidden property="colForSort" styleId="colForSort"/>
					<html:hidden property="ordForSort" styleId="ordForSort"/>
					<html:hidden property="pageForSort" styleId="pageForSort"/>
					<html:hidden property="serchStrs" styleId="serchStrs"/>	
					<html:hidden property="TXDT" styleId="TXDT"/>
					<html:hidden property="TXDATE" styleId="TXDATE"/>
					<html:hidden property="STAN" styleId="STAN"/>
					<html:hidden property="sortname" styleId="sortname" />
					<html:hidden property="sortorder" styleId="sortorder" />
					<html:hidden property="edit_params" styleId="edit_params" />
					<bean:define id="detailData" name="onblocktab_form" property="detailData"/>	
					<table>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>營業日</td>													
							<td width="25%"><bean:write name="detailData"  property="BIZDATE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>清算階段代號</td>													
							<td><bean:write name="detailData"  property="CLEARINGPHASE"/></TD>
						</tr>
						<tr>
							<!-- 20150317 by 李建利 自未完成交易結果查詢進入明細頁面時，應加入「原」避免混淆 -->
							<td class="header DATA_GROUP_1" nowrap>
								<logic:equal name="onblocktab_form" property="sourcePage" value="onblocktabNotTradRes">原</logic:equal>交易日期時間
							</td>													
							<td><bean:write name="detailData" property="TXDT"/></TD>
							<td class="header DATA_GROUP_1" nowrap>票交所處理日期時間</td>													
							<td><bean:write name="detailData"  property="EACHDT"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>
								<logic:equal name="onblocktab_form" property="sourcePage" value="onblocktabNotTradRes">原</logic:equal>系統追蹤序號
							</td>													
							<td><bean:write name="detailData"  property="STAN"/></TD>
							<td class="header DATA_GROUP_1" nowrap>交易金額</td>													
							<td><bean:write name="detailData"  property="NEWTXAMT" format="###.##"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>交易類別</td>													
							<td><bean:write name="detailData" property="PCODE_DESC"/></TD>
							<td class="header DATA_GROUP_1" nowrap>交易代號 / 收費類型</td>													
							<td><bean:write name="detailData"  property="TXN_NAME"/> / <bean:write name="detailData"  property="TXN_TYPE"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>發動單位代號</td>													
							<td><bean:write name="detailData" property="SENDERBANK_NAME"/></TD>
							<td class="header DATA_GROUP_1" nowrap>接收單位代號</td>													
							<td><bean:write name="detailData" property="RECEIVERBANK_NAME"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>發動者統一編號</td>													
							<td><bean:write name="detailData"  property="SENDERID"/></TD>
							<td class="header DATA_GROUP_1" nowrap>收受者統一編號</td>													
							<td><bean:write name="detailData"  property="RECEIVERID"/></TD>	
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>入帳者帳號</td>													
							<td><bean:write name="detailData"  property="INACCTNO"/></TD>		
							<td class="header DATA_GROUP_1" nowrap>入帳者統一編號</td>													
							<td><bean:write name="detailData"  property="INID"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>扣款者帳號</td>													
							<td><bean:write name="detailData"  property="OUTACCTNO"/></TD>																		
							<td class="header DATA_GROUP_1" nowrap>扣款者統一編號</td>													
							<td><bean:write name="detailData"  property="OUTID"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>發動行代號</td>													
							<td><bean:write name="detailData"  property="SENDERBANKID_NAME"/></TD>
							<td class="header DATA_GROUP_1" nowrap>發動單位手續費 / 新版-發動單位手續費</td>													
							<td><bean:write name="detailData"  property="NEWSENDERFEE" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWSENDERFEE_NW" format="#,##0.00"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>入帳行代號</td>													
							<td><bean:write name="detailData"  property="INBANKID_NAME"/></TD>	
							<td class="header DATA_GROUP_1" nowrap>入帳單位手續費 / 新版-入帳單位手續費</td>													
							<td><bean:write name="detailData"  property="NEWINFEE" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWINFEE_NW" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>扣款行代號</td>													
							<td><bean:write name="detailData"  property="OUTBANKID_NAME"/></TD>		
							<td class="header DATA_GROUP_1" nowrap>扣款單位手續費 / 新版-扣款單位手續費</td>													
							<td><bean:write name="detailData"  property="NEWOUTFEE" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWOUTFEE_NW" format="#,##0.00"/></TD>
						</tr>
						<tr>	
							<td class="header DATA_GROUP_1" nowrap>銷帳行代號</td>													
							<td><bean:write name="detailData"  property="WOBANKID_NAME"/></TD>		
							<td class="header DATA_GROUP_1" nowrap>銷帳單位手續費 / 新版-銷帳單位手續費</td>													
							<td><bean:write name="detailData"  property="NEWWOFEE" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWWOFEE_NW" format="#,##0.00"/></TD>
						</tr>
						<tr>		
							<td class="header DATA_GROUP_1" nowrap>退費/退回截止日</td>
							<td><bean:write name="detailData" property="REFUNDDEADLINE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>交換所手續費 / &nbsp;&nbsp;&nbsp;新版-交換所手續費</td>													
							<td><bean:write name="detailData"  property="NEWEACHFEE" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWEACHFEE_NW" format="#,##0.00"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>逾時註記 T2/T3</td>													
							<td><bean:write name="detailData"  property="TIMEOUTCODE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>客戶實際支付手續費 / 新版-客戶支付手續費 / 電文客支</td>													
							<td><bean:write name="detailData"  property="NEWFEE" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWFEE_NW" format="#,##0.00"/> / <bean:write name="detailData"  property="NEWEXTENDFEE" /></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>整批營業日</td>													
							<td><bean:write name="detailData"  property="FLBIZDATE"/></TD>
							<td class="header DATA_GROUP_1" nowrap>整批處理序號(檔名)</td>													
							<td><bean:write name="detailData"  property="FLBATCHSEQ"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>整批處理序號</td>													
							<td><bean:write name="detailData"  property="FLPROCSEQ"/></TD>
							<td class="header DATA_GROUP_1" nowrap>資料序號</td>													
							<td><bean:write name="detailData"  property="DATASEQ"/></TD>
						</tr>
					</table>
					
					<!-- 未完成交易結果查詢 -->
					<logic:equal name="onblocktab_form" property="sourcePage" value="onblocktabNotTradRes">
					<table>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>交易結果</td>													
							<td width="25%"><bean:write name="detailData"  property="RESP"/></TD>
							<td class="header DATA_GROUP_2" nowrap>未完成交易結果處理營業日</td>													
							<td><bean:write name="detailData" property="NEWBIZDATE"/></td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>回覆代號</td>													
							<td><bean:write name="detailData" property="RESULTCODE"/></TD>
							<td class="header DATA_GROUP_2" nowrap>未完成交易結果清算階段</td>													
							<td><bean:write name="detailData" property="NEWCLRPHASE"/></td>
						</tr>
					</table>
					</logic:equal>
					
					<!-- 交易資料查詢、未完成交易資料查詢 -->
					<logic:notEqual name="onblocktab_form" property="sourcePage" value="onblocktabNotTradRes">
					<table>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>交易結果</td>													
							<td width="25%"><bean:write name="detailData"  property="RESP"/></TD>
							<td class="header DATA_GROUP_2" nowrap>記帳碼</td>													
							<td>
								<logic:equal name="detailData" property="ACCTCODE" value="0">
									<bean:write name="detailData" property="ACCTCODE"/>-沖正
								</logic:equal>
								<logic:equal name="detailData" property="ACCTCODE" value="1">
									<bean:write name="detailData"  property="ACCTCODE"/>-記帳
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>錯誤原因</td>													
							<td><bean:write name="detailData" property="CONRESULTCODE_DESC"/></TD>
							<td class="header DATA_GROUP_2" nowrap>沖正營業日</td>													
							<td>
								<logic:equal name="detailData" property="ACCTCODE" value="0">
									<bean:write name="detailData" property="NEWBIZDATE"/>
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>最後處理時間</td>
							<td><bean:write name="detailData"  property="UPDATEDT"/></td>
							<td class="header DATA_GROUP_2" nowrap>沖正清算階段</td>													
							<td>
								<logic:equal name="detailData" property="ACCTCODE" value="0">
									<bean:write name="detailData" property="NEWCLRPHASE"/>
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>RC1</td>										
							<td>
							<logic:notEmpty name="detailData" property="ERR_DESC1">
								<bean:write name="detailData"  property="ERR_DESC1"/>
							</logic:notEmpty>
							</TD>		
							<td class="header DATA_GROUP_2" nowrap>RC2</td>													
							<td>
							<logic:notEmpty name="detailData" property="ERR_DESC2">
								<bean:write name="detailData"  property="ERR_DESC2"/>
							</logic:notEmpty>
							</TD>																		
						</tr>
						<tr>	
							<td class="header DATA_GROUP_2" nowrap>RC3</td>													
							<td>
							<logic:notEmpty name="detailData" property="ERR_DESC3">
								<bean:write name="detailData"  property="ERR_DESC3"/>
							</logic:notEmpty>
							</TD>	
							<td class="header DATA_GROUP_2" nowrap>RC4</td>													
							<td>
							<logic:notEmpty name="detailData" property="ERR_DESC4">
								<bean:write name="detailData"  property="ERR_DESC4"/>
							</logic:notEmpty>
							</TD>																		
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>RC5</td>													
							<td>
							<logic:notEmpty name="detailData" property="ERR_DESC5">
								<bean:write name="detailData"  property="ERR_DESC5"/>
							</logic:notEmpty>
							</TD>		
							<td class="header DATA_GROUP_2" nowrap>RC6</td>													
							<td>
							<logic:notEmpty name="detailData" property="ERR_DESC6">
								<bean:write name="detailData"  property="ERR_DESC6"/>
							</logic:notEmpty>
							</TD>																		
						</tr>
					</table>
					</logic:notEqual>
					
					<table>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>用戶號碼</td>
							<td width="25%"><bean:write name="detailData" property="USERNO"/></td>
							<td class="header DATA_GROUP_1" nowrap>存摺摘要</td>
							<td><bean:write name="detailData" property="CONMEMO"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>發動者專用區</td>
							<td><bean:write name="detailData" property="SENDERDATA"/></td>
							<td class="header DATA_GROUP_1" nowrap>上市櫃公司代號</td>
							<td><bean:write name="detailData"  property="COMPANYID"/></td>
						</tr>
					</table>
					<table style="table-layout: fixed;">	
						<tr>
							<td class="header DATA_GROUP_2" nowrap>收費業者統一編號</td>
							<td><bean:write name="detailData"  property="TOLLID"/></td>
							<td class="header DATA_GROUP_2" nowrap>銷帳單位</td>
							<td>
								<bean:write name="detailData" property="BILLFLAG"/>
								<logic:equal  name="detailData" property="BILLFLAG" value="1">
									-入帳行銷帳
								</logic:equal>
								<logic:equal  name="detailData" property="BILLFLAG" value="2">
									-發動行銷帳
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>繳費類別</td>
							<td><bean:write name="detailData" property="PFCLASS"/></td>
							<td class="header DATA_GROUP_2" nowrap>銷帳資料類型</td>
							<td>
								<bean:write name="detailData"  property="BILLTYPE"/>
								<logic:equal  name="detailData" property="BILLTYPE" value="A">
									-虛擬帳號
								</logic:equal>
								<logic:equal  name="detailData" property="BILLTYPE" value="B">
									-銷帳編號
								</logic:equal>
								<logic:equal  name="detailData" property="BILLTYPE" value="C">
									-三段式條碼
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>繳費工具類型</td>
							<td>
								<bean:write name="detailData"  property="CHARGETYPE"/>
								<logic:equal  name="detailData" property="CHARGETYPE" value="1">
									-約定授權
								</logic:equal>
								<logic:equal  name="detailData" property="CHARGETYPE" value="2">
									-晶片金融卡
								</logic:equal>
								<logic:equal  name="detailData" property="CHARGETYPE" value="3">
									-本人帳戶繳費
								</logic:equal>
							</td>
							<td class="header DATA_GROUP_2" nowrap>銷帳資料區</td>
							<td width="25%" style="word-break:break-all;"><bean:write name="detailData"  property="BILLDATA"/></td>
						</tr>
						<tr>
							<td class="header DATA_GROUP_2" nowrap>繳費工具驗證資料區</td>
							<td width="25%" style="word-break:break-all;" colspan="3"><bean:write name="detailData" property="CHECKDATA" /></td>
						</tr>
					</table>
					<table>	
						<tr>
<!-- 							<td class="header DATA_GROUP_2" nowrap>特店代號</td> -->
							<td class="header DATA_GROUP_1" >特店代號</td>
							<td class = "DATA_GROUP_V_2" width="25%"><bean:write name="detailData" property="MERCHANTID"/></td>
<!-- 							<td class="header DATA_GROUP_2" nowrap>原特店代號</td>													 -->
							<td class="header DATA_GROUP_1" >原特店代號</td>													
							<td class = "DATA_GROUP_V_2"><bean:write name="detailData"  property="OMERCHANTID"/></TD>
						</tr>
						<tr>
<!-- 							<td class="header DATA_GROUP_2" nowrap>訂單編號</td> -->
							<td class="header DATA_GROUP_1" >訂單編號</td>
							<td class = "DATA_GROUP_V_2" ><bean:write name="detailData" property="ORDERNO"/></td>
<!-- 							<td class="header DATA_GROUP_2" nowrap>原訂單編號</td>													 -->
							<td class = "header DATA_GROUP_1" >原訂單編號</td>													
							<td class = "DATA_GROUP_V_2" ><bean:write name="detailData"  property="OORDERNO"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>端末機代號</td>													
							<td><bean:write name="detailData"  property="TRMLID"/></TD>
							<td class="header DATA_GROUP_1" nowrap>原端末機代號</td>													
							<td><bean:write name="detailData"  property="OTRMLID"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>PAN</td>													
							<td><bean:write name="detailData"  property="PAN"/></TD>
							<td class="header DATA_GROUP_1" nowrap>原PAN</td>													
							<td><bean:write name="detailData"  property="OPAN"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>PSN</td>													
							<td><bean:write name="detailData"  property="PSN"/></TD>
							<td class="header DATA_GROUP_1" nowrap>原PSN</td>													
							<td><bean:write name="detailData"  property="OPSN"/></TD>
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap></td>													
							<td></TD>
							<td class="header DATA_GROUP_1" nowrap>原交易日期</td>													
							<td><bean:write name="detailData"  property="OTXDATE"/></TD>
							
						</tr>
						<tr>
							<td class="header DATA_GROUP_1" nowrap>票交所代行</td>													
							<td><bean:write name="detailData"  property="ACHFLAG"/></TD>
							<td class="header DATA_GROUP_1" nowrap>原交易金額</td>													
							<td><bean:write name="detailData"  property="OTXAMT"/></TD>
						</tr>
						<tr>
							<td colspan="4" align="center" style="padding: 10px; vertical-align: middle">
								<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
								<logic:equal name="onblocktab_form" property="isUndone" value="Y">
<!-- 								2015 edit by hugo  req by UAT-20150526-06-->
<!-- 								<label class="btn" id="send"  onclick="showDialog(this.id)"><img src="./images/return.png"/>&nbsp;票交所代為處理沖正作業</label> -->
								<label class="btn" id="send"  onclick="showDialog(this.id)"><img src="./images/send.png"/>&nbsp;票交所代為處理未完成交易</label>
								<label class="btn" id="send_1406"  onclick="send_1406()"><img src="./images/send.png"/>&nbsp;請求傳送未完成交易結果</label>
								<label class="btn" id="send_1400"  onclick="send_1400()"><img src="./images/send.png"/>&nbsp;請求傳送確認訊息</label>
								</logic:equal>
								</logic:equal>
								<label class="btn" id="back"  onclick="back(this.id)"><img src="./images/return.png"/>&nbsp;返回查詢頁面</label>
							</td>
						</tr>
					</table>
				</html:form>
			</div>
			<!-- 訊息內容對話框 -->
			<div id="msg_dialog" title="請選擇回覆訊息" style="font-size: 16px ;"  >
			</div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</div>
		<script type="text/javascript">
		var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		$("#msg_dialog").dialog({
			autoOpen: false,
			height: 200,
			width: 300,
			modal: false,
			buttons: {
                 '成功': function(){
                     $(this).dialog('close');
                     send(true ,'S');
                 },
                 '失敗': function(){
                     $(this).dialog('close');
                     send(true , 'F');
                 },
                 '取消': function(){
                     $(this).dialog('close');
                     send(false);
                 }
             }
		});
			function back(str){
				$("#ac_key").val(str);
				$("#target").val('search');
				$("form").submit();
			}
			
			
			<logic:equal  name="login_form" property="userData.s_auth_type" value="A">
			<logic:equal name="onblocktab_form" property="isUndone" value="Y">
			function showDialog(str){
				$("#msg_dialog").dialog('open');
				$("#msg_dialog").focus();
			}
			function send(isAcc , type){
// 				if(confirm('是否確定沖正?')){
				if(isAcc){
					blockUI();
					var txdate = '<bean:write name="detailData" property="TXDATE"/>' ;
					var stan = '<bean:write name="detailData" property="STAN"/>' ;
					var rdata = {component:"undone_txdata_bo", method:"send", STAN:stan,TXDATE:txdate ,TYPE:type};
					var result = fstop.getServerDataExII(uri, rdata, false, "");
					if(fstop.isNotEmpty(result)){
						//if(window.console){console.log("result>>"+result);}
						alert(result.msg);
						$("#TXDT").val(txdate);
						$("#STAN").val(stan);
						$("#ac_key").val("edit");
						$("#target").val('edit_p');
						$('form').attr('action','undone_txdata.do');
						$("form").submit();
						/*
						if(result.result=="TRUE"){
							$("#TXDT").val(txdate);
							$("#STAN").val(stan);
							$("#ac_key").val("edit");
							$("#target").val('edit_p');
							$('form').attr('action','undone_txdata');
							$("form").submit();
						}
						*/
					}
				}
			}
			
			function send_1406(isAcc , type){
				if(confirm('請求傳送未完成交易結果(1406)?')){
					blockUI();
					var txdate = '<bean:write name="detailData" property="TXDATE"/>' ;
					var stan = '<bean:write name="detailData" property="STAN"/>' ;
					var rdata = {component:"undone_txdata_bo", method:"send_1406", STAN:stan,TXDATE:txdate };
					var result = fstop.getServerDataExII(uri, rdata, false, "");
					if(fstop.isNotEmpty(result)){
						//if(window.console){console.log("result>>"+result);}
						alert(result.msg);
						reLoad(txdate , stan );
					}
				}
			}
			function send_1400(isAcc , type){
				if(confirm('確定要請求傳送確認訊息(1400)?')){
					blockUI();
					var txdate = '<bean:write name="detailData" property="TXDATE"/>' ;
					var stan = '<bean:write name="detailData" property="STAN"/>' ;
					var rdata = {component:"undone_txdata_bo", method:"send_1400", STAN:stan,TXDATE:txdate };
					var result = fstop.getServerDataExII(uri, rdata, false, "");
					if(fstop.isNotEmpty(result)){
						//if(window.console){console.log("result>>"+result);}
						alert(result.msg);
						reLoad(txdate , stan );
					}
				}
			}
			</logic:equal>
			</logic:equal>
			
			function reLoad(txdate , stan ){
				$("#TXDT").val(txdate);
				$("#STAN").val(stan);
				$("#ac_key").val("edit");
				$("#target").val('edit_p');
				$('form').attr('action','undone_txdata.do');
				$("form").submit();
			}
			
		</script>		
	</body>
</html>