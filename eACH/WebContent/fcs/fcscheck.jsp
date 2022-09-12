<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">

var lForm ;
var cardType = "ETOKEN" ;
var SubjectCN = "";
var pfxFlag = false;
var loginType = "" ;
var SerialNo="";	
var brsType = "" ;
var vsflag = true ;
var nowVs = "" ;
var OS_Flag=navigator.userAgent;

//瀏覽器版本
var tmp_ver;
var compatible = "";

if (OS_Flag.indexOf("MSIE 6")>0 ) {
	tmp_ver = 6;
}else  if (OS_Flag.indexOf("Trident/7.0") > 0 && OS_Flag.indexOf("rv:11.0")) {
		tmp_ver = 11;
}else if (OS_Flag.indexOf("MSIE 7")>0 ) {
	if (OS_Flag.indexOf("Trident/4.0") > 0) {
		tmp_ver = 8;
		compatible = "(相容性檢視模式)";
	} else  if (OS_Flag.indexOf("Trident/5.0") > 0) {
		tmp_ver = 9;
		compatible = "(相容性檢視模式)";
	} else  if (OS_Flag.indexOf("Trident/6.0") > 0) {
		tmp_ver = 10;
		compatible = "(相容性檢視模式)";
	} else {
		tmp_ver = 7;
	}
}else if (OS_Flag.indexOf("MSIE 8")>0 ) {
	tmp_ver = 8;
}else if (OS_Flag.indexOf("MSIE 9")>0 ) {
	tmp_ver = 9;
}else if (OS_Flag.indexOf("MSIE 10")>0 ) {
	tmp_ver = 10;
}else if (OS_Flag.indexOf("MSIE 11")>0 ) {
	tmp_ver = 11;
}
var vn="MSIE";
var vn1="Firefox";
var bit_ver;
if (OS_Flag.indexOf("Win64")>0 ) {
		bit_ver = "(64)"; 
	} else {
		bit_ver = "(32)"; 
	}	

if(tmp_ver>5){
	if(tmp_ver>=10){
		brsType = "IE" + tmp_ver + bit_ver +compatible ;
	}
}else{
	brsType = BrowserDetect.browser + BrowserDetect.version ;
}


/*********
**利用AJAX 取得 後端資料庫設定 目前安控最新版本**
***********/
// function getcheckcaversion(iptV) {
// 		console.log("getcheckcaversion="+iptV);
//     $.ajax({
// 		type : "POST",
// // 		url : "/eACH/indexInfo?component=isec_version_bo&method=search_toJson&"+$("#formID").serialize(),
//  	url : "/eACH/indexInfo?component=isec_version_bo&method=search_toJson_test&"+$("#formID").serialize(),
//  	//url : "/eACH/indexInfo?component=isec_version_bo&method=search_toJson",
// 		async:false,
// 		dataType : "json",
// 		timeout : 100000,
// 		success : function(data) {
// 		console.log("JosnData"+data);
// 		var dataSr = String(data);
// 		var arr1 = iptV.split('.'),
//           	arr2 = dataSr.split('.');
//      	var minLength=Math.min(arr1.length,arr2.length),
//           position=0,
//           diff=0;
//       //依次比較版本號每一位大小，對比得出結果後跳出迴圈
//       	while(position<minLength && ((diff=parseInt(arr1[position])-parseInt(arr2[position]))==0)){
//           position++;
//      	 }
//       	diff = (diff!=0)?diff:(arr1.length-arr2.length);
      	
//       	if(diff>=0){
//       		diff = true;
//       	}else{
//       		diff = false;
//       	}		
// //	      	alert("diff"+diff);
      	
// 		if(diff == false){
// //		console.log("sussess"+data);
// 			nowVs = data ;
// 			alert('系統發現本電腦 版本非本所提供最新版本(您的版本'+iptV+'，最新版本'+data+')。') ;
// 			vsflag=false;
// 			}			
// 		},
// 		error : function(e) {
// 			alert("error"+data);
// 			console.log("ERROR: ", e);
// 		},
// 		done : function(e) {
// 			alert("done"+data);
// 			console.log("DONE");
// 		}
// 	});
// }

// 10/15更新暫用，不從資料庫取
function getcheckcaversion(iptV) {
	var data = '1.1805.8.2';
	if(iptV!=data){
		nowVs = data ;
		vsflag=false;
	}
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

                lForm.ac_key.value = 'ikeyLogin';
                lForm.target.value = 'login';
 				lForm.RAOName.value = SubjectCN;
				lForm.signvalue.value = signedData;
				lForm.CompanyID.value = SubjectCN;
				lForm.LoginType.value = loginType;
				lForm.userId.value = SubjectCN;
// 				lForm.ikeyValidateDate.value = SubjectCN;
                pfxFlag = false;
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
            GetAPIVersion();
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
//         	alert("請點選『"+
//         	<a href="https://sectest.twnch.org.tw/ra/portal/ActiveX/TCHXHRSetup.zip">
//         	+"下載</a>』安裝『安控元件』。若已安裝『安控元件』，請確認軟體已經在執行中");
        }
	}else if(curFuncName == 'GetAPIVersion'){
		if(outputData.actionResult){
			console.log(outputData);
			getcheckcaversion(outputData.version);
			//alert(outputData.version);
		}
	}
}

</script>