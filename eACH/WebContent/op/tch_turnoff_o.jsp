<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=10 ; text/html; charset=UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean:write name="login_form" property="userData.s_func_name"/></title>
		<!-- NECESSARY BEGIN -->
		<link rel="stylesheet" type="text/css" href="./css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="./css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/validationEngine.jquery.css">
		<script type="text/javascript" src="./js/jquery-3.2.1.min.js"></script>
		
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="./js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		
		<script type="text/javascript" src="./RA/pwd.js"></script>
		<script type="text/javascript" src="./RA/include.js"></script>
		<script type="text/javascript" src="./RA/PKCS7SignClient.js"></script>
		<!-- NECESSARY END -->
		<!--安控元件 JS 檔-->
		<script type="text/javascript" src="./js/TCH_base.js"></script>
		<script type="text/javascript" src="./js/TCH_mid.js"></script>
		<script type="text/javascript" src="./js/BrowserDetect.js"></script>
	</head>
	<body onload="doTryPort();">
<div id="wrapper">
<jsp:include page="/header.jsp"></jsp:include>
<div id="block_body">
			<OBJECT classid="CLSID:7B91D073-398F-43CA-AFE4-0437F654AFEA"
	        codebase="./RA/CMClientATL.cab#Version=1,8,3,13" width="0" height="0"
	        align="left" hspace="0" vspace="0" id="CMClient"></OBJECT>
        	<!-- BREADCRUMBS -->
			<div id="breadcrumb">
				<a href="#"><bean:write name="login_form" property="userData.USER_ID"/> | </a>
				<a href="#" class="has-next"><bean:write name="login_form" property="userData.s_up_func_name"/></a>
				<a href="#"><bean:write name="login_form" property="userData.s_func_name"/></a>
			</div>
			<div id="opPanel" style="margin-top: 5px">
				<html:form styleId="formID" action="/tch_turnoff">
					<html:hidden property="ac_key" styleId="ac_key" value=""/>
					<html:hidden property="target" styleId="target" value=""/>
					<html:hidden property="BGBK_ID" styleId="BGBK_ID" value="0000000"/>
					<html:hidden property="RAOName" styleId="RAOName" value=""/>
					<html:hidden property="signvalue" styleId="signvalue" value=""/>
					<html:hidden property="ikeyValidateDate" styleId="ikeyValidateDate" value=""/>				
					<html:hidden property="cardType" styleId="cardType" value="iKey2032"/>
					<html:hidden property="method" styleId="method" value="ikeyLogin"/>
					<html:hidden property="CompanyID" styleId="CompanyID" value=""/>
					<html:hidden property="EmployeeID" styleId="EmployeeID" value=""/>
					<html:hidden property="LoginType" styleId="LoginType" value="I"/>
					<html:hidden property="browserType" styleId="browserType"/>
					<html:hidden property="userId" styleId="userId" value=""/>
					<fieldset>
						<table>
							<tr>
								<td style="width: 16%">
									<logic:equal name="login_form" property="userData.s_auth_type" value="A">
<!-- 										<label class="btn" id="search" onclick="onPut(this.id)"><img src="./images/turnoff.png"/>&nbsp;關機</label> -->
										<label class="btn" id="tchTurnOff" onclick="tokenLogin()"><img src="./images/turnoff.png"/>&nbsp;關機</label>
									</logic:equal>
									<logic:notEqual name="login_form" property="userData.s_auth_type" value="A">
										<font color="red">※ 無操作權限</font>
									</logic:notEqual>
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
			var lForm ;
			var cardType = "ETOKEN" ;
			var SubjectCN = "";
			var pfxFlag = false;
			var loginType = "" ;	
			var brsType = "" ;
			var vsflag = true ;
			var uri="/eACH/baseInfo",gridOption,queryInterval;
			
			$(document).ready(function(){
				blockUI();
				init();
				unblockUI();
			});
			
			function init(){
				alterMsg();
				initGridOption();
				initGrid();
			}
			
			function alterMsg(){
				var msg = '<bean:write name="tch_turnoff_form" property="msg"/>';
				if(fstop.isNotEmptyString(msg)){
					alert(msg);
				}
			}
			
			function initGridOption(){
				gridOption = {
						datatype: "local",
		            	autowidth:true,
		            	height: 300,
		            	sorttype:"text",
		            	shrinkToFit: true,
		            	rowNum: 10000,
		            	loadonce: true,
						colNames:['交易追蹤序號','操作行代號','操作行名稱','交易類別','傳送通知時間','FEP回覆結果','回覆結果'],
		            	colModel: [
							//交易追蹤序號，改顯示STAN欄位
							//{name:'WEBTRACENO',index:'WEBTRACENO',fixed:true,width: 85},
							{name:'PK_STAN',index:'PK_STAN',fixed:true,width: 85},
							{name:'BANKID',index:'BANKID',fixed:true,width: 70},
							{name:'BANKNAME',index:'BANKNAME',fixed:true,width: 210},
							{name:'PCODE',index:'PCODE',fixed:true,width: 120},
							{name:'DATETIME',index:'DATETIME',fixed:true,width: 150},
							{name:'FEP_ERR_DESC',index:'FEP_ERR_DESC',align:'center',fixed:true,width: 90},
							{name:'RSP_ERR_DESC',index:'RSP_ERR_DESC',align:'center',fixed:true,width: 120}
						],
		            	gridComplete:function (){
			            	$("#resultData .jqgrow:odd").addClass('resultDataRowOdd');
							$("#resultData .jqgrow:even").addClass('resultDataRowEven');
						},
	 					beforeSelectRow: function(rowid, e) {
	 					    return false;
	 					},
						beforeProcessing: function(data, status, xhr){
							//轉換「傳送通知時間」格式
							for(var rowCount in data){
								data[rowCount].DATETIME = dateFormatter(data[rowCount].PK_TXDATE, "yyyymmdd", "yyyy-mm-dd") + "&nbsp;&nbsp;&nbsp;" + timeFormatter(data[rowCount].TXTIME, "hhmmss", "hh:mm:ss");
								data[rowCount].PCODE = data[rowCount].PCODE=="1101"?"交換所關機作業":data[rowCount].PCODE;
							}
						},
						loadComplete: function(data){
// 							查詢結果無資料
							noDataEvent_Bat(data ,$("#resultData") );
						},
	 					loadtext: "處理中..."
				};
			}
			
			function initGrid(){
				$("#resultData").jqGrid(gridOption);
			}
			
			// 從這邊開始eToken流程
			function tokenLogin(){
			    lForm = (document.getElementById("formID"));
			    lForm.browserType.value = brsType ;
				if(vsflag){
					initSecCtrl();
				}
				else{
					alert('目前安控元件版本非系統提供版本，確認後繼續登入流程');
					initSecCtrl();
				}
			}
			function doCustomAction(){
				var outputData = XHRDataObject.outputData;
				var curFuncName = XHRDataObject.curFuncName;
			    if(curFuncName == 'initSecCtrl'){
			        if(outputData.actionResult){
			            if(pfxFlag){
			                selectCertificate( 'all', '', 'false' ,'pfx');
			            }else{
			                //m_nMyLogin();
			                CM_Read_BasicData( 'BASEDATA');
			            }
			        }else{
			            //讀取失敗，詢問是否要使用 PFX 模式登入
			            if(! pfxFlag ){
			                if(confirm('載具讀取失敗，請問是否要使用自動化系統登入')){
			                    pfxFlag = true;
			                    initSecCtrl('CAPI');
			                }
			            }else{
			                pfxFlag = false;
			                openErrorWindow();
			            }
			        
			        }
				}else if(curFuncName == 'm_nMyLogin'){
					if(outputData.actionResult){
			            document.formID.cn.value = outputData.certificate;
			            //嘗試讀取載具
			            CM_Read_BasicData( 'BASEDATA');
			        
			        }else{
			            //讀取失敗，詢問是否要使用 PFX 模式登入
			            if(! pfxFlag ){
			                if(confirm('載具讀取失敗，請問是否要使用自動化系統登入')){
			                    pfxFlag = true;
			                    initSecCtrl('CAPI');
			                }
			            }else{
			                pfxFlag = false;
			                //alert('元件初始化失敗');
			                openErrorWindow();
			            }
			        
			        }
			        
				}else if(curFuncName == 'CM_Read_BasicData'){
					if(outputData.actionResult){
			            var cn = outputData.dataValue;
			            SubjectCN = cn ;
			        
			            //讀取成功，尋找憑證
			            selectCertificate( 'cn', cn, 'true');
			        
			        }else{
			            //讀取失敗，詢問是否要使用 PFX 模式登入
			            if(! pfxFlag ){
			                if(confirm('載具讀取失敗，請問是否要使用自動化系統登入')){
			                    pfxFlag = true;
			                    initSecCtrl('CAPI');
			                }
			            }else{
			                pfxFlag = false;
			                alert('載具讀取失敗');
			            }
			        
			        }
			        
				}else if(curFuncName == 'selectCertificate'){
					if(outputData.actionResult){
			            var cert = outputData.certificate;
			            if(pfxFlag==true){
							certificate() ;
						}
						else{
				            //執行簽章
				            signP7( cert, 'DataContent');
						}
			        
			        }else{
			            //憑證資訊取得失敗，結束流程
			            alert('取得憑證資訊失敗，錯誤代碼：' + outputData.statusCode + 
			            		'訊息：' + errorCodeMessage(outputData.statusCode));
			            pfxFlag = false;
			            return;
			        }
			        
				}else if(curFuncName == 'signP7'){
					if(outputData.actionResult){
			            var signedData = outputData.signedData;            
			            if(signedData != ''){
			                //submit
			 				if (pfxFlag){
			 					loginType = "C" ;
			 				}
			 				else{
			 					loginType = "I/T" ;
			                }

			 				lForm.RAOName.value = SubjectCN;
							lForm.signvalue.value = signedData;
							lForm.CompanyID.value = SubjectCN;
							lForm.LoginType.value = loginType;
							lForm.userId.value = SubjectCN;
			                pfxFlag = false;
			                blockUI();
			                if(confirm("是否確認執行關機作業?!")){
				                var stan = send($("#BGBK_ID").val());
								if(stan != null){
									searchDataByStan(stan);
									startInterval(searchDataByStan, [stan]);
								}
			                }
			                unblockUI();
			 				lForm.submit(); 
			            }else{
			                alert('P7簽章失敗:簽章值為空');
			                pfxFlag = false;
			            }
			        }else{
			            //簽章失敗，結束流程
			            alert('P7簽章失敗:' + outputData.statusCode + outputData.errorMsg);
			            pfxFlag = false;
			            return;
			        }
			        
				}else if(curFuncName == 'm_nMyChangePIN'){
			        
			        if(outputData.actionResult){
			        alert('更換密碼完成');
			        }else{
			        alert('更換載具密碼失敗，錯誤代碼：' + outputData.statusCode + 
			                '訊息：' + outputData.errorMsg
			            );
			        }
			        changePinFlag = false;
				}else if(curFuncName == 'certificate'){
			        if(outputData.actionResult){
			            var str = outputData.subjectName + '\n'
			                + outputData.IssuerName + '\n'
			            + outputData.CSN + '\n'
			            + outputData.StartDate+ '\n'
			            + outputData.EndDate + '\n'
			            + outputData.KeyUsage + '\n'
			            + outputData.KeyLen + '\n'
			            ;
			            var subjectName = outputData.subjectName;
						var res_subjectName = subjectName.split(",");
						var cnvalue = res_subjectName[0];
						SubjectCN = cnvalue.substring(3); 
						//alert("subjectName " + cnvalue.substring(3));
			            var cert = outputData.certificate;
			            //執行簽章
						signP7( cert, 'DataContent');        	
			        }else{
			        	alert('certificate 取得失敗，錯誤代碼：' + outputData.statusCode + 
			                '訊息：' + outputData.errorMsg
			            );
			        }
			        changePinFlag = false;
				}else if(curFuncName == 'doTryPort'){
					$.unblockUI(); 
			        if(outputData.actionResult){
// 			            GetAPIVersion();
			        }else{
			        	var msg = '';
			        	if(browserDetect()){
			        		var msg = '系統發現本電腦未啟動"票交所多瀏覽器安控程式"安裝相關安控元件。\n' +
			        		'請確認該程式是否已正確啟動(檢視電腦右下角的啟動選項是否有 "票交所多瀏覽器安控程式")'
			        		;
			        		alert(msg);
			        		openErrorWindow();
			        	}else{
			        		msg = outputData.errorMsg;
			        		alert(msg);
			        	}
//			         	alert("請點選『"+
//			         	<a href="https://sectest.twnch.org.tw/ra/portal/ActiveX/TCHXHRSetup.zip">
//			         	+"下載</a>』安裝『安控元件』。若已安裝『安控元件』，請確認軟體已經在執行中");
			        }
				}
// 				else if(curFuncName == 'GetAPIVersion'){
// 					if(outputData.actionResult){
// 						console.log(outputData);
// 						getcheckcaversion(outputData.version);
// 					}
// 				}
			}
			
			function preCheck(isIkey){
				var rdata;
				if(isIkey){
					if($("#signvalue").val() == ""){
						alert("驗證失敗! Sign Value 有誤!");
						return false;
					}else{
						rdata = {component:"tch_turnoff_bo",method:"preCheck", signvalue:$("#signvalue").val()};
					}
				}else{
					rdata = {component:"tch_turnoff_bo",method:"preCheck"};
				}
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					if(fstop.isNotEmpty(msg.msg)){
						alert(msg.msg);
					}
					return msg.result=="FALSE"?false:true;
				}else{
					return false;
				}
			}
			
			function send(bgbkId){
				var rdata = {component:"tch_turnoff_bo",method:"send",BGBK_ID:bgbkId,action:$("#formID").attr('action')};
				var msg = fstop.getServerDataExII(uri, rdata, false);
				if(msg != null){
					if(fstop.isNotEmpty(msg.msg)){
						alert(msg.msg);
					}
					return msg.result=="TRUE"?msg.STAN:null;
				}else{
					return null;
				}
			}
			
			function searchDataByStan(stan){
				//if(window.console){console.log("STAN=" + stan);}
				searchData("component=tch_turnoff_bo&method=getDataByStan&STAN="+stan);
			}
			
			//此處不使用
			function searchDataByDate(date){
				//if(window.console){console.log("DATE=" + date);}
				searchData("component=tch_turnoff_bo&method=getDataByDate&TXDATE="+date);
			}
			
			function searchData(qStr){
				//if(window.console){console.log("qStr=" + qStr);}
				
				$("#resultData").jqGrid('GridUnload');
				var newOption = gridOption;
				//if(window.console)console.log($("#formID").serialize());
				newOption.url = "/eACH/baseInfo?"+qStr;
				newOption.datatype = "json";
				newOption.mtype = 'POST';
				//if(window.console)console.log("url>>"+newOption.url);
				$("#resultData").jqGrid(gridOption);
			}
			
			function startInterval(func, values){
				clearInterval(queryInterval);
				queryInterval = setRepeater(func, values, 30);
			}
		</script>
	</body>
</html>