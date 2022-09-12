/**
 * 票據交換所 多瀏覽器框架 1.2.1
 * 2018-02-07
 *     針對使用 MSI 安裝檔，移除 ISecurityCom、CMClient、TCHEnrollObj 的 cab 
 * 2018-03-05
 *     調整 initSecCtrl 的運作方式
 *     修正 selectCertificate，取消在此功能內重新初始化元件的作法，並移除createType參數，交由 initSecCtrl 判斷使用何種方式取得憑證
 * 2018-03-08
 * 	   移除 TWEnroll 載入方法
 * 2018-03-09
 *     新增 TCHCertSignObj 錯誤代碼列表，此列表只有 TCHCertSign 元件使用
 * 2018-03-12
 *     移除所有 ActiveX 元件呼叫路線
 *     methodSelect 移除，更換為 browserDetect 。使用此方法檢核是否可使用本框架
 *         ( IE版本小於 10 則無法使用 )
 * 2018-03-14
 *     修正 50 錯誤代碼未正確轉換問題
 * 2018-03-16
 *     整併 CM 版本
 * 2018-03-22
 *     調整 http/https設定
 * 2018-03-23
 *     調整 initSecCtrl 寫法，將 finalSecCtrl 放進流程內，如此網頁便不需要自行關閉與元件的連線
 */


/**
 * 元件初始化參數
 * 
 */

var notSupportMsg = '很抱歉，目前所使用的瀏覽器版本過舊，無法使用本功能';
notSupportMsg += '\n請確認IE瀏覽器版本為10或以上';
notSupportMsg += '\n並關閉相容性模式進行網頁瀏覽';

//var objectNotFoundMsg = '系統發現本電腦未正確安裝相關安控元件。\n請在登入頁面下方點擊"票交安控元件下載"連結，\n下載並安裝程式後，重新執行步驟';

XHRDataObject.libType = '';


function convertCardTypeVal(cardType){
	
	if(cardType == 'ETOKEN'){
        cardType = '{eToken.dll}';
    }else if(cardType == 'iKey2032'){
        cardType = '{dkck201.dll}';
    }else{
        alert('載具類別輸入錯誤');
        return;
    }
	return cardType;
}

/**
 * 瀏覽器檢測
 * false 代表 IE 瀏覽器版本( ver < 10)無法使用本框架
 * @returns
 */
function browserDetect(){
	
	var res = false;
	
	var rv = -1;
	if (navigator.appName == 'Microsoft Internet Explorer'){
		
		var ua = navigator.userAgent;
		var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
		if (re.exec(ua) != null){
			rv = parseFloat( RegExp.$1 );
		}
		
		if(rv > -1 ){
			if(rv < 10 ){
				res = false;
			}else{
				res = true;
			}
		}
		
	}else{
		res = true;
	}
	
	return res;
	
}

//初始化參數
XHRDataObject.outputData = {
    
    actionResult:false,
    statusCode:'',
    errorMsg:''
};
            


/**
功能執行框架 - 主要執行方法
將整個網頁流程切分成多筆步驟，用Array包覆傳送給本 function

物件定義格式說明
action：    步驟的主要執行內容，定義本步驟的主要處理程序
onSuccess： 若步驟需要呼叫元件時使用，請定義 XHRHttpRequest 呼叫成功時的處理方法
onFail：    若步驟需要呼叫元件時使用，請定義 XHRHttpRequest 呼叫失敗時的處理方法

**/
function execAction(actionList){

    XHRDataObject.callbackIndex = 0;
	XHRDataObject.callbackFuncName = [];

    XHRDataObject.actionList = actionList;
    XHRDataObject.actionIndex = 0 ;


    for(var i = 0 ; i < XHRDataObject.actionList.length ; i ++){


        XHRDataObject.callbackFuncName[i] = function(){

            try{
                XHRDataObject.cbFuncName[1] = XHRDataObject.actionList[XHRDataObject.actionIndex].onSuccess;

            }catch(e){
                XHRDataObject.cbFuncName[1] = nextAction;
            }

            try{
                XHRDataObject.cbFuncName[0] = XHRDataObject.actionList[XHRDataObject.actionIndex].onFail;
            }catch(e){
                XHRDataObject.cbFuncName[0] = requestErrorHandler;
            }



            if(XHRDataObject.actionList[XHRDataObject.actionIndex].action){

                XHRDataObject.actionIndex ++;
                XHRDataObject.actionList[XHRDataObject.actionIndex - 1].action();

            }else{
                alert('功能執行框架初始化失敗，執行清單格式不正確，第' + XHRDataObject.actionIndex + '步驟缺少 action 物件');
                return;
            }
        };

    }


    //執行第一個action
    XHRDataObject.callbackFuncName[XHRDataObject.callbackIndex]();

}


//*************************************************************
// 功能執行框架 - 執行下一個 action
// 使用時機： action 執行成功，需要進行下一步驟時使用
//*************************************************************
function nextAction(){


    try {

            XHRDataObject.callbackIndex++;

			XHRDataObject.callbackFuncName[XHRDataObject.callbackIndex]();

	} catch(ex) {
		
	}

}

//*************************************************************
// 功能執行框架 - 重新執行當前 action
// 使用時機： 需要重複執行當前action時使用
//*************************************************************
function loopAction(){

    try {

            XHRDataObject.actionIndex --;
			XHRDataObject.callbackFuncName[XHRDataObject.callbackIndex]();

	} catch(ex) {
		// alert('no more fuctions should call back');
	}

}
//*************************************************************
// 功能執行框架 - 指定 action ，直接執行
// 使用時機： 若整段流程因需求會切分成多條處理方式時使用
// 注意：使用本功能前，請確認已事先正確了解本身流程的運作方法
//*************************************************************
function jumpAction(jumpPoint){

    try {

            XHRDataObject.actionIndex  = jumpPoint;
            XHRDataObject.callbackIndex = jumpPoint;
			XHRDataObject.callbackFuncName[XHRDataObject.callbackIndex]();

	} catch(ex) {
		// alert('no more fuctions should call back');
	}

}

//*************************************************************
// XHR元件找port處理( 特殊跳頁處理 ) v
// 若功能的流程需要做多次跳頁，不方便使用 body onload 執行 initialObject 初始化元件時使用
// 請將本 function 放在當頁需要使用元件的流程內，且必須為第一順位
//*************************************************************
function tryPortAction() {


	XHRDataObject.portIndex = 0;
    

    var action = {
        action:function(){
            
            XHRDataObject.realURL = XHRDataObject.sURLList[XHRDataObject.portIndex];
            innerGetVersion();

        }
        ,onSuccess:function(respData){
        
        	XHRDataObject.curFuncName = "doTryPort";
        	
        	XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
        	
        	doCustomAction();
        
        }
        ,onFail:function(respData){
        
        	XHRDataObject.curFuncName = "doTryPort";
        	
            if(XHRDataObject.portIndex > XHRDataObject.sURLList.length - 1){
            	XHRDataObject.outputData.actionResult = false;
                XHRDataObject.outputData.statusCode = respData[1];
                XHRDataObject.outputData.errorMsg = '無可使用的連接埠，請確認元件已經安裝完成';
                doCustomAction();
            }else{
                XHRDataObject.portIndex ++;
                loopAction();
            }
        }

    };


    return action;

}

function doTryPort(){
    
	if(browserDetect()){
		var list = [tryPortAction()];
	    execAction(list);
	}else{
		//ActiveX skip Method
		XHRDataObject.curFuncName = "doTryPort";
		XHRDataObject.outputData.actionResult = true;
        XHRDataObject.outputData.statusCode = '0';
        XHRDataObject.outputData.errorMsg = '';
		doCustomAction();
	}
    
}

/**
取得元件版本號
此方法只適用於XHR元件
input:無
output:
    actionResult：執行結果，true代表成功，false則為失敗
**/
function GetAPIVersion()
{
    
    if(browserDetect()){
        //XHR 路線
        var action = {
        action:function(){
        	innerGetVersion();    
        }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.version = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
        //ActiveX 無此FUNC，直接跳過
    	XHRDataObject.curFuncName = "GetAPIVersion";
        XHRDataObject.outputData.actionResult = true;
        doCustomAction();
    }
    
}

/**
初始化各元件基本參數
input:
    libType:啟動模式
    	CAPI:MicroSoftCAPI (PFX自動化系統)
    	P11:硬體載具模式
    	未輸入 libType 則以 P11 作為預設
output:
    actionResult：執行結果，true代表成功，false則為失敗
**/
function initSecCtrl(libType)
{
	XHRDataObject.errCount = 0;
	
	if(libType != 'CAPI' && libType != 'P11'){
		libType = 'P11';
	}
	XHRDataObject.libType = libType;
	
    if(browserDetect()){
        //XHR 路線
    	XHRDataObject.curFuncName = "initSecCtrl";
        

        var finalSecCtrlAction = {
        action:function(){
    
                innerfinalSecCtrl();
    
            }
        ,onSuccess:function(respData){
            nextAction();
        }
        ,onFail:function(respData){
            nextAction();
        }
    
        };
        
        
        var action = {
        action:function(){
        	
        	switch(libType){
	        	case 'CAPI':
	        		innerinitSecCtrl('MicroSoftCAPI');
	        		break;
	        	case 'P11':
	        		innerinitSecCtrl(convertCardTypeVal('ETOKEN'));
	        		break;
        	}
    
            }
        ,onSuccess:function(respData){
            
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
        ,onFail:function(respData){
        	
        	
        	XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
        	
        }
    
        };
        
        var list = [finalSecCtrlAction,action];
        
        execAction(list);
        
    }else{
    	
    	alert(notSupportMsg);
    	
    }
    
    
}

/**
登入載具
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
**/
function m_nMyLogin()
{
    
    if(browserDetect()){
        //XHR 路線
        var action = {
        action:function(){
    
        		innerm_nMyLogin();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	
    	alert(notSupportMsg);
    	
    }
    
  
}

/**
變更載具密碼
input:
    cardType:載具類別
    oldPswd:舊密碼
    newPswd:新密碼
註：XHR模式會打開視窗輸入密碼
output:
    actionResult：執行結果，true代表成功，false則為失敗
**/
function m_nMyChangePIN()
{
    
    if(browserDetect()){
        
    	XHRDataObject.curFuncName = 'm_nMyChangePIN';
        //XHR 路線
        var action = {
        action:function(){
    
                innerm_nMyChangePIN();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	
    	alert(notSupportMsg);
    	
    }
    
}
/**
產生RSA金鑰
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    errorMsg：執行失敗時的錯誤代碼
    publicKeyValue：產生的公鑰值，包含exponent與modulus
**/
function m_nMyGenKey()
{
    if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerm_nMyGenKey();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.publicKeyValue = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.publicKeyValue = '';
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	alert(notSupportMsg);
    }
    
    
}
/**
登出載具
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    errorMsg：執行失敗時的錯誤代碼
**/
function m_nMyLogout()
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerm_nMyLogout();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	alert(notSupportMsg);
    }
  
}

/**
選取載具中的私密金鑰將傳入的亂數資料簽章，成功後回傳HEX格式的簽章值。
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    errorMsg：執行失敗時的錯誤代碼
    signedData: 簽章完成後的結果(HEX format)
**/
function m_nMySign(data)
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerm_nMySign(data);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.signedData = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.signedData = '';
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	alert(notSupportMsg);
    }
  
}


/**
取得載具中寫入的卡片持有者名稱
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    errorMsg：執行失敗時的錯誤代碼
    userValue: 卡片持有者名稱
**/
function m_nMyGetObjectValue()
{
  var appName = 'USERID';
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerm_nMyGetObjectValue(appName);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.userValue = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.userValue = '';
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	alert(notSupportMsg);
    }
}

/**
載具資料初始化
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    errorMsg：執行失敗時的錯誤代碼
**/
function TokenInitialize()
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerTokenInitialize();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.userValue = '';
            doCustomAction();
    
        }
    
        };
        
        var list = [action];
        
        execAction(list);
        
    }else{
    	alert(notSupportMsg);
    }
  
}

/**
建立卡片持有者身分
input:
    userid:使用者名稱
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    errorMsg：執行失敗時的錯誤代碼
**/
function m_nMyCreateObject(userid)
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerm_nMyCreateObject(userid);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.userValue = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}
/**
呼叫載具產生RSA金鑰一併產生憑證請求檔。
input:
    userCN:使用者名稱
    keyLen:
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    csr：以base64編碼的憑證請求檔
**/
function createPkcs10( userCN, keyLen)
{
  
  if(keyLen != 1024 && keyLen != 2048){
      keyLen = 2048;
  }
  
  var libName = '';
  
  
  if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innercreatePkcs10(userCN, keyLen);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.csr = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.csr = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
  
}

/**
安裝CA核發下來的憑證
input:
    B64Cert:CA核發下來的憑證並以Base64編碼
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
**/
function installCert( B64Cert )
{
  
  
  
  var libName = '';
  
  
  if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerinstallCert(B64Cert);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}

/**
登出載具並切斷與憑證載具間的連結，釋放已經載入的資源
input:
    無
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
**/
function finalSecCtrl()
{
 
    if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerfinalSecCtrl();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
 
}

/**
根據查詢條件來選取載具(或電腦)中的憑證

回傳選取到的憑證,內容為base4編碼的字串
input:
    searchType:查詢方法
        all－開啟視窗選擇憑證
        cn－根據使用者的CN查詢憑證
        certSN－根據憑證序號查詢
    queryVal:查詢字串，根據不同的查詢方式帶入到對應的方法
    selectLastCert:是否選取校期最晚的憑證,true:為是,false:為否,其他字串都會設為true
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    certificate:Base64編碼的憑證

註：使用本方法前，必須先執行 initSecCtrl，決定要使用PFX或載具憑證    
    
**/
function selectCertificate( searchType, queryVal, selectLastCert)
{
  
  var libName = '';
  
  
  if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                switch(searchType){
                    case 'all' :
                        innerselectCertificate(selectLastCert);
                        break;
                    case 'cn' :
                        innerselectCertificateBySubjectCN(queryVal, selectLastCert);
                        break;
                    case 'certSN' :
                        innersselectCertificateBySerialNum(queryVal, selectLastCert);
                        break;
                    default:
                        break;
                }
    
            }
        ,onSuccess:function(respData){
            
            XHRDataObject.curFuncName = 'selectCertificate';
            
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.certificate = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            
            XHRDataObject.curFuncName = 'selectCertificate';
            
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.certificate = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}


/**
根據傳入的憑證找到載具中的私鑰來對data做PKCS#7簽章
input:
    cert:base64編碼的憑證。
    data:需要被簽章的資料
    algo:簽章時使用到的雜湊演算法，
         金鑰長度1024使用SHA1，金鑰長度使用SHA256。
         雖然傳入值不會影響雜湊演算法，仍需要傳入適當的值。
         本方法不使用
output:
    actionResult：執行結果，true代表成功，false則為失敗
    statusCode：執行後代碼
    signedData:Base64編碼的PKCS#7簽章值
**/
function signP7( cert, data)
{
  var algo = 'SHA256';
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innersignP7( cert, data, algo);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.signedData = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.signedData = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
  
}

/**
列出選擇的憑證相關資料
須配合 selectCertificate 使用
input:
    無
output:
    subjectName:憑證的主體。
    IssuerName:憑證的發行者名稱。
    CSN:憑證的序號。
    StartDate：憑證有效期限的起日。
    EndDate：憑證有效期限的迄日。
    KeyUsage：憑證用途。
    KeyLen:憑證RSA金鑰長度(bits)。

**/
function certificate()
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innercertificate();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.subjectName =respData[2];
            XHRDataObject.outputData.issuerName =respData[3];
            XHRDataObject.outputData.serialNumber =respData[4];
            XHRDataObject.outputData.startDate =respData[5];
            XHRDataObject.outputData.endDate =respData[6];
            XHRDataObject.outputData.keyUsage =respData[7];
            XHRDataObject.outputData.keyLen =respData[8];
            
            
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}

/*
取得載具中的公鑰值
*/
function getPublicKeyValue()
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innergetPublicKeyValue();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.publicKeyValue = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.publicKeyValue = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}

/*
取得載具中的卡片序號
*/

function CM_Get_TokenSN()
{
  
  if(browserDetect()){
        
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Get_TokenSN();
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.tokenSN = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.tokenSN = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}
/*
將載具恢復為出廠值,並設定SOPIN與USERPIN
呼叫前需要先呼叫過initSecCtrl()指定PKCS#11模組名稱
*/
function CM_Reset( sopin, userpin)
{
  
  if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Reset(sopin, userpin);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
  
}

function CM_Read_BasicData( label)
{
	
    if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Read_BasicData(label);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.dataValue = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
        	if (respData[1] != '19')
        	{
                XHRDataObject.outputData.actionResult = false;
        	}
        	else
        		XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            XHRDataObject.outputData.dataValue = '';
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
    
}

function CM_Write_BasicData( label, value)
{
	
    if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Write_BasicData(label, value);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
    
}

function CM_Set_TripleDESKey( userpin, label, keyValue)
{
	
    if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Set_TripleDESKey( userpin, label, keyValue);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
    
}

function signMode( bInout, bGRD)
{
	
    if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innersignMode( bInout, bGRD);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
    
}


function CM_OP_MakeCard( userLabel, userid, publabel, prilabel)
{
	if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_OP_MakeCard( userLabel, userid, publabel, prilabel);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.exponent = respData[2];
            XHRDataObject.outputData.modulus = respData[3];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
}

/**
使用PriKeyLabel指定的RSA金鑰簽章
input:
    random：需要被加密的值，HEX格式。
    userLabel：根據Datalabel找到製卡時匯入的ID。
    prilabel：根據PriKeylabel找到製OP卡時產生的RSA金鑰。
output:
    userData:根據DataLabel取回的data value
    signature:使用PriKeyLabel指定的私鑰簽完章後的結果，Hex format
**/
function CM_OP_Verification( random, userLabel, prilabel)
{
	if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_OP_Verification( random, userLabel, prilabel);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.userData = respData[2];
            XHRDataObject.outputData.signature = respData[3];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
}


/**
根據DESKeyLabel與DataLabel找出相對應的KEY與Data，
經過組合與計算後取回結果
input:
    UserPin：載具USER PIN碼，當輸入值為”NULL”時則彈出視窗供使用者自行輸入。
    RandomNo：需要被計算的值。
    DESKeyLabel：根據DESKey label找到製卡時匯入的DESKey Value。
    DataLabel：根據DESKey label找到製卡時匯入的Data。
output:
    macData:計算完後的結果，Hex格式
**/
function CM_Verification_P11( userPIN, random, DESLabel, dataLabel)
{
	if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Verification_P11( userPIN, random, DESLabel, dataLabel);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.macData = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
}

function CM_Print( data)
{
  if(browserDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
                innerCM_Print(data);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            XHRDataObject.outputData.publicKeyValue = respData[2];
            doCustomAction();
    
        }
        ,onFail:function(respData){
            XHRDataObject.outputData.actionResult = false;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = errorCodeMessage(respData[1]);
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
}

function CM_Set_USERPin(oldPin, newPin)
{
  if(methodDetect()){
        
        //XHR 路線
        var action = {
        action:function(){
    
        	innerCM_Set_USERPin(oldPin, newPin);
    
            }
        ,onSuccess:function(respData){
            XHRDataObject.outputData.actionResult = true;
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = '';
            doCustomAction();
    
        }
        ,onFail:function(respData){
        	XHRDataObject.outputData.actionResult = false;            
            XHRDataObject.outputData.statusCode = respData[1];
            XHRDataObject.outputData.errorMsg = respData[2];
            doCustomAction();
    
        }
    
        };
        
        execAction([action]);
        
    }else{
    	alert(notSupportMsg);
    }
}



/***************************************************************
預設錯誤處理方法，根據 errorCodeMessage 內的錯誤代碼對照表回傳對應訊息
*****************************************************************/
function requestErrorHandler(param){

    var error_code = param[1];

    alert('元件執行'+ param[0] +'失敗，錯誤代碼：'+error_code+'，錯誤訊息：' + errorCodeMessage(error_code));

}


/**
 * 主要代碼轉換表，一般由XHR元件使用
 * @param errorCode
 * @returns
 */
function errorCodeMessage(errorCode){
	
	switch(errorCode){
	    case '0' :   return '功能處理成功(Operation Succeed)'; break;
	    case '1' :   return '傳入參數錯誤(Parameter Error)'; break;
	    case '2' :   return '記憶體不足(Out of Memory)'; break;
	    case '3' :   return '參數解析錯誤(如傳入Dll List格式有誤或是Illegal PIN格式錯誤)'; break;
	    case '4' :   return '找不到憑證'; break;
	    case '5' :   return '憑證格式錯誤'; break;
	    case '6' :   return '找不到此issuer'; break;
	    case '7' :   return '無法確認CRL'; break;
	    case '8' :   return '憑證已過期'; break;
	    case '9' :   return '憑證未註冊'; break;
	    case '10':   return '憑證以暫禁'; break;
	    case '11':   return '憑證以註銷'; break;
	    case '12':   return '找不到對應的Public Key (Public Key not Found)'; break;
	    case '13':   return '找不到對應的Private Key (Private Key not Found)'; break;
	    case '14':   return '公鑰與私鑰不相符'; break;
	    case '15':   return '上傳資料格式有誤'; break;
	    case '16':   return '下載資料格式有誤'; break;
	    case '17':   return '資料格式有誤'; break;
	    case '18':   return '簽章XML資料有誤'; break;
	    case '19':   return '找不到資料'; break;
	    case '20':   return '找不到檔案'; break;
	    case '21':   return '無效的密碼'; break;
	    case '22':   return '找不到URL'; break;
	    case '23':   return 'PKCS#11呼叫失敗'; break;
	    case '24':   return 'KEY Type不符。'; break;
	    case '25':   return '錯誤的演算法'; break;
	    case '26':   return '金鑰長度不符'; break;
	    case '27':   return '找不到相對應的ID'; break;
	    case '28':   return '使用者取消'; break;
	    case '51':   return '載具密碼格式不符'; break;
	    case '52':   return '載具已被鎖住'; break;
	    case '55':   return '找不到載具'; break;
	    case '61':   return '網頁URL不合法'; break;
	    case '62':   return '載具拔插逾時'; break;
	    case '63':   return '載入PKCS#11模組錯誤'; break;
	    case '64':   return '偵測到超過一隻以上的載具'; break;
	    case '70':   return '不支援的function名稱'; break;
	    case '99':   return '其他錯誤(Others)'; break;
	    case 404:    return '元件初始化失敗，請確認 安控元件程式是否已啟動或已安裝'; break;
	    case '50':   return '載具密碼錯誤'; break;
	    
	    default: return '未知代碼'; break;
	    
	}
}
