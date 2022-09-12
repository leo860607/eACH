
/* BrowserDetect came from http://www.quirksmode.org/js/detect.html */

var XHRDataObject = {
	//function list, store all available function name
	//call these functions one by one
    callbackIndex: 0,
    callbackFuncName: [],
    cardType:'ETOKEN',
    realURL: '',
    sURLList: ['https://127.0.0.1:29821/', 'https://127.0.0.1:29822/', 'https://127.0.0.1:29823/', 'https://127.0.0.1:29824/', 'https://127.0.0.1:29825/'],
	//fail: call cbFuncName[0]
	//success: call cbFuncName[1],
	cbFuncName: [],
	//根據上述callbackFunction所使用的要傳輸的參數
	parameter:[],
	//put variable name in this list, 
	domData:[],
	//index of URL
	portIndex: 0,
	//command sent to XHRServer
	commandName: "",
	//delimeter
	delimeterSymbol: "||",
	oRequest: "",
	initXHR: function(){
		this.oRequest = new XMLHttpRequest();
	},
	callXHR: function(arg){
		try{
			/* 使用 POST 傳送必須設定 MIME 型態 */
			var sendData = "";
			var j = 0;
			this.oRequest = new XMLHttpRequest();
			this.oRequest.open("POST", this.realURL, arg); //true不同步,false同步 arg控制
			this.oRequest.setRequestHeader('Content-Type',  'text/plain');  
			//this.oRequest.timeout = 1000; //time out 時間 1秒 還是不ok
			/* 讀取中的程式處理... */
			this.oRequest.onreadystatechange = stateChange;
			//this.oRequest.send("GetAPIVersion"); 
			// 送出 Ajax 要求 
			sendData = this.commandName;
			while(this.parameter[j] != ""){
				sendData += this.delimeterSymbol;
				sendData += this.parameter[j];
				j++;
			}
            
			this.oRequest.send(sendData);
		}catch(exception){
			alert('callXHR failed, error code:' + exception);
		}
	}
};


function stateChange(){
		if (XHRDataObject.oRequest.readyState==4){
			if (XHRDataObject.oRequest.status==200){
				//alert("message from Server:" + XHRDataObject.oRequest.responseText);
				var msgPart = XHRDataObject.oRequest.responseText.split("||");
				if(msgPart[0] == XHRDataObject.commandName){
                    XHRDataObject.curFuncName = XHRDataObject.commandName
					if(msgPart[1] != "0"){
                        //回傳狀態非0的例外處理
						XHRDataObject.cbFuncName[0](msgPart);
					}else{
						if(XHRDataObject.cbFuncName[1])
							XHRDataObject.cbFuncName[1](msgPart);
					}
				}else{
					XHRDataObject.cbFuncName[0](msgPart);
				}
			}else{
				//alert("XHR載入失敗，請確認安控元件是否安裝完成");
				var msgPart = [];
				msgPart[0] = XHRDataObject.commandName;
				msgPart[1] = XHRDataObject.oRequest.status;
				msgPart[2] = 'call XHR error，httpCode:' + XHRDataObject.oRequest.status;
				XHRDataObject.cbFuncName[0](msgPart);
			}
		}
}


function innerGetVersion()
{
  try
  {
	XHRDataObject.commandName = "GetAPIVersion";
    XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);  		
  }	
}

function innerinitSecCtrl(lib)
{
  try
  {
	XHRDataObject.commandName = "initSecCtrl";
	//set parameter
	XHRDataObject.parameter[0] = lib;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerinitSecCtrl Error' + exception);  		
  }	
}

function innerm_nMyLogin()
{
  try
  {
	XHRDataObject.commandName = "m_nMyLogin";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerm_nMyLogin Error' + exception);  		
  }	
}

function innerm_nMyChangePIN()
{
  try
  {
	XHRDataObject.commandName = "m_nMyChangePIN";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);  		
  }	
}

function innerm_nMyGenKey()
{
  try
  {
	XHRDataObject.commandName = "m_nMyGenKey";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);  		
  }	
}

function innerm_nMyLogout()
{
  try
  {
	XHRDataObject.commandName = "m_nMyLogout";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);  		
  }	
}

function innerm_nMySign( data)
{
  try
  {
	XHRDataObject.commandName = "m_nMySign";
	//set parameter
	XHRDataObject.parameter[0] = data;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);
  }	
}

function innerm_nMyGetObjectValue( appName)
{
  try
  {
	XHRDataObject.commandName = "m_nMyGetObjectValue";
	//set parameter
	XHRDataObject.parameter[0] = 'USERID';
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);  		
  }	
}

function innerTokenInitialize()
{
  try
  {
	XHRDataObject.commandName = "TokenInitialize";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerTokenInitialize Error' + exception);  		
  }	
}

function innerm_nMyCreateObject( userid)
{
  try
  {
	XHRDataObject.commandName = "m_nMyCreateObject";
	//set parameter
	XHRDataObject.parameter[0] = userid;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerm_nMyCreateObject Error' + exception);  		
  }	
}

function innercreatePkcs10( userCN, keyLen)
{
  try
  {
	XHRDataObject.commandName = "createPkcs10";
	//set parameter
	XHRDataObject.parameter[0] = userCN;
	XHRDataObject.parameter[1] = keyLen;
	XHRDataObject.parameter[2] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innercreatePkcs10 Error' + exception);  		
  }	
}

function innerinstallCert( B64Cert)
{
  try
  {
	XHRDataObject.commandName = "installCert";
	//set parameter
	XHRDataObject.parameter[0] = B64Cert;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerinstallCert Error' + exception);  		
  }	
}

function innerfinalSecCtrl()
{
  try
  {
	XHRDataObject.commandName = "finalSecCtrl";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerfinalSecCtrl Error' + exception);  		
  }	
}

/*
根據傳入的Common Name來選擇憑證
CName: common name
selectLastCert:是否選取校期最晚的憑證,true:為是,false:為否,其他字串都會設為true
回傳選取到的憑證,內容為base4編碼的字串
*/
function innerselectCertificateBySubjectCN( CName, selectLastCert)
{
  try
  {
	XHRDataObject.commandName = "selectCertificateBySubjectCN";
	//set parameter
	XHRDataObject.parameter[0] = CName;
	XHRDataObject.parameter[1] = selectLastCert;
	XHRDataObject.parameter[2] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerselectCertificateBySubjectCN Error' + exception);  		
  }	
}

/*
開啟視窗由使用者選擇欲查詢的憑證
CName: common name
selectLastCert:是否選取校期最晚的憑證,true:為是,false:為否,其他字串都會設為true
回傳選取到的憑證,內容為base4編碼的字串
*/
function innerselectCertificate(selectLastCert)
{
  try
  {
	XHRDataObject.commandName = "selectCertificate";
	//set parameter
	XHRDataObject.parameter[0] = selectLastCert;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerselectCertificate Error' + exception);  		
  }	
}

/*
選擇憑證時是否可以選取過期憑證
*/
function innerShowExpiredCert(selectExpiredCert)
{
  try
  {
	XHRDataObject.commandName = "ShowExpiredCert";
	//set parameter
	XHRDataObject.parameter[0] = selectExpiredCert;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerShowExpiredCert Error' + exception);  		
  }	
}

/*
PKCS#7簽章
*/
function innersignP7( cert, data, algo)
{
  try
  {
	XHRDataObject.commandName = "signP7";
	//set parameter
	XHRDataObject.parameter[0] = cert;
	XHRDataObject.parameter[1] = data;
	XHRDataObject.parameter[2] = algo;
	XHRDataObject.parameter[3] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innersignP7 Error' + exception);  		
  }	
}

/*
根據傳入的憑證序號來選擇憑證
SerialNo: 憑證序號,Hex format
selectLastCert:是否選取校期最晚的憑證,true:為是,false:為否,其他字串都會設為true
回傳選取到的憑證,內容為base4編碼的字串
*/

function innersselectCertificateBySerialNum( SerialNo, selectLastCert)
{
  try
  {
	XHRDataObject.commandName = "selectCertificateBySerialNum";
	//set parameter
	XHRDataObject.parameter[0] = SerialNo;
	XHRDataObject.parameter[1] = selectLastCert;
	XHRDataObject.parameter[2] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innersselectCertificateBySerialNum Error' + exception);  		
  }	
}
	
function innercertificate()
{
  try
  {
	XHRDataObject.commandName = "certificate";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innercertificate Error' + exception);  		
  }	
}

/*
取得載具中的公鑰值
*/
function innergetPublicKeyValue()
{
  try
  {
	XHRDataObject.commandName = "getPublicKeyValue";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('getPublicKeyValue Error' + exception);  		
  }	
}

/*
取得載具中的卡片序號
*/

function innerCM_Get_TokenSN()
{
  try
  {
	XHRDataObject.commandName = "CM_Get_TokenSN";
	//set parameter
	XHRDataObject.parameter[0] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Get_TokenSN Error' + exception);  		
  }	
}
/*
將載具恢復為出廠值,並設定SOPIN與USERPIN
*/
function innerCM_Reset( sopin, userpin)
{
  try
  {
	XHRDataObject.commandName = "CM_Reset";
	//set parameter
	XHRDataObject.parameter[0] = sopin;
	XHRDataObject.parameter[1] = userpin;
	XHRDataObject.parameter[2] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Reset Error' + exception);  		
  }	
}

function innerCM_Read_BasicData( label)
{
	  try
  {
	XHRDataObject.commandName = "CM_Read_BasicData";

	XHRDataObject.parameter[0] = label;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Read_BasicData Error' + exception);  		
  }
}

function innerCM_Write_BasicData( label, value)
{
	  try
  {
	XHRDataObject.commandName = "CM_Write_BasicData";
	//set parameter
	XHRDataObject.parameter[0] = label;
	XHRDataObject.parameter[1] = value;
	XHRDataObject.parameter[2] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Write_BasicData Error' + exception);  		
  }
}

function innerCM_Set_TripleDESKey( userpin, label, keyValue)
{
	  try
  {
	XHRDataObject.commandName = "CM_Set_TripleDESKey";
	//set parameter
	XHRDataObject.parameter[0] = userpin;
	XHRDataObject.parameter[1] = label;
	XHRDataObject.parameter[2] = keyValue;
	XHRDataObject.parameter[3] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Set_TripleDESKey Error' + exception);  		
  }
}

function innersignMode( bInout, bGRD)
{
	  try
  {
	XHRDataObject.commandName = "signMode";
	//set parameter
	XHRDataObject.parameter[0] = bInout;
	XHRDataObject.parameter[1] = bGRD;
	XHRDataObject.parameter[2] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('signMode Error' + exception);  		
  }
}


function innerCM_OP_MakeCard( userLabel, userid, publabel, prilabel)
{
	  try
  {
	XHRDataObject.commandName = "CM_OP_MakeCard";
	//set parameter
	XHRDataObject.parameter[0] = userLabel;
	XHRDataObject.parameter[1] = userid;
	XHRDataObject.parameter[2] = publabel;
	XHRDataObject.parameter[3] = prilabel;
	XHRDataObject.parameter[4] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_OP_MakeCard Error' + exception);  		
  }
}

function innerCM_OP_Verification( random, userLabel, prilabel)
{
	  try
  {
	XHRDataObject.commandName = "CM_OP_Verification";
	//set parameter
	XHRDataObject.parameter[0] = random;
	XHRDataObject.parameter[1] = userLabel;
	XHRDataObject.parameter[2] = prilabel;
	XHRDataObject.parameter[3] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_OP_Verification Error' + exception);  		
  }
}

function innerCM_Verification_P11( userPIN, random, DESLabel, dataLabel)
{
	  try
  {
	XHRDataObject.commandName = "CM_Verification_P11";
	//set parameter
	if(userPIN == "")
	  XHRDataObject.parameter[0] = "NULL";
	else
	  XHRDataObject.parameter[0] = userPIN;
	XHRDataObject.parameter[1] = random;
	XHRDataObject.parameter[2] = DESLabel;
	XHRDataObject.parameter[3] = dataLabel;
	XHRDataObject.parameter[4] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Verification_P11 Error' + exception);  		
  }
}

function innerCM_Print( data)
{
  try
  {
	XHRDataObject.commandName = "CMPrint";
	XHRDataObject.parameter[0] = data;
	XHRDataObject.parameter[1] = "";
	XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('innerGetVersion Error' + exception);  		
  }	
}

function innerCM_Set_USERPin(oldPin, newPin)
{
  try
  {
	  //set parameter
	  XHRDataObject.commandName = "CM_Set_USERPin";
	  XHRDataObject.parameter[0] = oldPin;
	  XHRDataObject.parameter[1] = newPin;
	  XHRDataObject.parameter[2] = "";
	  XHRDataObject.callXHR(true);
  }
  catch(exception)
  {
    alert('CM_Reset Error' + exception);  		
  }	
}