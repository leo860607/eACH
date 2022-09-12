var pkcs7SignObj
  = " <OBJECT classid='clsid:EDA76653-FDDE-4C1E-B65E-2AA3438A8A30'"
  + "   			codebase='ActiveX/PKCS7SignatureClientCOM.cab#Version=7,5,7,1'"
  + "				id =P7SignatureClientObj></OBJECT>";

var certDBObj
  = " <OBJECT classid='clsid:C9B6115C-DEA9-11D6-8C3C-0050BAA6346E'"
  + "    			codebase='ActiveX/CertificateDBClientCOM.cab#Version=8,1,8,1'"
  + "				id =CertDBClientObj></OBJECT>";

var HiScriptObj
  = " <OBJECT classid='clsid:54285ADE-0F41-4808-B53E-319E73BAADFB'"
  + "    			codebase='ActiveX/HiScripting.cab#Version=1,2,0,13'"
  + "				id =ScriptTools></OBJECT>";

document.write(pkcs7SignObj);
document.write(certDBObj);
//document.write(HiScriptObj);

var p7Sign_SignCertSN="";
var p7Sign_SignCertCN="";
var p7Sign_DataContent="";
var p7Sign_SignCert="";
var p7Sign_SignedStr="";
var p7Sign_ConvBase64 = false;

function getSignedPKCS7Str()
{
  //alert("getSignedPKCS7Str");
  //alert(p7Sign_ConvBase64);
	if(!initCertDBObj())
	{
		return false;
	}
	if(!initPkcs7Obj())
	{
		return false;
	}
	//check to do something here
	if (p7Sign_DataContent == "")
	{
		alert("請輸入文件字串。");
		return false;
	}

	// 轉 Base64
	if(p7Sign_ConvBase64 == true)
	{
        //alert("do base64 encode["+p7Sign_DataContent+"]");
		var DataContentB64 = ScriptTools.Base64Encode(strUnicode2Ansi(p7Sign_DataContent))
        //alert("DataContentB64["+DataContentB64+"]");
		//DataContentB64 = Replace(DataContentB64, "\r", "");
		//DataContentB64 = Replace(DataContentB64, "\n", "");
		p7Sign_DataContent = DataContentB64;
        //alert("=====Debug===== \r\n PKCS7SignClient.js -> getSignedPKCS7Str do ConvBase64 \r\n"+p7Sign_DataContent);
	}

    //alert("getSignedPKCS7Str() p7Sign_SignCertSN["+p7Sign_SignCertSN+"]");
	// select certificate
	if (!selectCertificateBySerialNum(p7Sign_SignCertSN))
	//if (!SelectCertificate())
	{
		return false;
	}
    //alert("getSignedPKCS7Str() p7Sign_DataContent["+p7Sign_DataContent+"]");

	//
	if ((lRtn = P7SignatureClientObj.setDataContentByString(p7Sign_DataContent)) != 0)
	{
		showHelpMsg("建立文件字串", lRtn, P7SignatureClientObj.LastErrorStr);
		return false;
	}
//alert(p7Sign_SignCert);
	//sign
	if ((lRtn = P7SignatureClientObj.sign(p7Sign_SignCert,"SHA1","RSA")) != 0)
	{
		showHelpMsg("使用者簽章", lRtn, P7SignatureClientObj.LastErrorStr);
		return false;
	}
    //alert("sign ok");

	//get result
	var signedString = P7SignatureClientObj.getSignedEnvelopByString();

	//alert('aa');

	if(signedString == "")
	{
        //alert("getSignedEnvelopByString empty");
		//alert("錯誤訊息:"+P7SignatureClientObj.LastErrorStr);
		showHelpMsg("使用者簽章失敗", -1, P7SignatureClientObj.LastErrorStr);
		return false;
	}
    //alert(signedString);
	p7Sign_SignedStr = signedString;
	//p7Sign_SignedStr = signedString;
	return true;
}

function getSignedPKCS7StrByPara(p7Sign_DataContent, SN, tarObj)
{
  //alert("getSignedPKCS7StrByPara");
  //alert(p7Sign_ConvBase64);
	if(!initCertDBObj())
	{
		return false;
	}
	if(!initPkcs7Obj())
	{
		return false;
	}
	//check to do something here
	if (p7Sign_DataContent == "")
	{
		alert("請輸入文件字串。");
		return false;
	}

	// 轉 Base64
	if(p7Sign_ConvBase64 == true)
	{
        //alert("do base64 encode["+p7Sign_DataContent+"]");
		var DataContentB64 = ScriptTools.Base64Encode(strUnicode2Ansi(p7Sign_DataContent))
        //alert("DataContentB64["+DataContentB64+"]");
		//DataContentB64 = Replace(DataContentB64, "\r", "");
		//DataContentB64 = Replace(DataContentB64, "\n", "");
		p7Sign_DataContent = DataContentB64;
        //alert("=====Debug===== \r\n PKCS7SignClient.js -> getSignedPKCS7Str do ConvBase64 \r\n"+p7Sign_DataContent);
	}

    //alert("getSignedPKCS7StrByPara() p7Sign_SignCertSN["+p7Sign_SignCertSN+"]");
	// select certificate
	if (!selectCertificateBySerialNum(SN))
	//if (!SelectCertificate())
	{
		return false;
	}
    //alert("getSignedPKCS7StrByPara() p7Sign_DataContent["+p7Sign_DataContent+"]");

	//
	if ((lRtn = P7SignatureClientObj.setDataContentByString(p7Sign_DataContent)) != 0)
	{
		showHelpMsg("建立文件字串", lRtn, P7SignatureClientObj.LastErrorStr);
		return false;
	}
//alert(p7Sign_SignCert);
	//sign
	if ((lRtn = P7SignatureClientObj.sign(p7Sign_SignCert,"SHA1","RSA")) != 0)
	{
		showHelpMsg("使用者簽章", lRtn, P7SignatureClientObj.LastErrorStr);
		return false;
	}
    //alert("sign ok");

	//get result
	var signedString = P7SignatureClientObj.getSignedEnvelopByString();

	//alert('aa');

	if(signedString == "")
	{
        //alert("getSignedEnvelopByString empty");
		//alert("錯誤訊息:"+P7SignatureClientObj.LastErrorStr);
		showHelpMsg("使用者簽章失敗", -1, P7SignatureClientObj.LastErrorStr);
		return false;
	}
    //alert(signedString);
	p7Sign_SignedStr = signedString;
	tarObj.value = signedString;
	return true;
}


/*******
 ** Pkcs7 物件初使設定
 **
 ** 是否要使用 載具 PKCS11 lib 來進行簽章(只可作用在使用載具上)
 ** P7SignatureClientObj.PKCS11LibName = libName
 **
 ** 設定是否要使用 載具的輸入密碼介面是否要使用 中文或是 載具原介面(只可作用在使用載具上)
 **		true/false	使用中文介面/使用載具原介面
 **		預設為 false
 ** P7SignatureClientObj.EnableLoginAgency = true;
 **
 ** 設定 PIN 的最少長度(只可作用在使用載具上且當 EnableLoginAgency = true)
 **		若載具的登入密碼小於設定值時，元件會回錯誤
 **		預設不限制
 **	P7SignatureClientObj.PINLenLimit = 4;
 **
 ** 設定非法密碼(只可作用在使用載具上且當 EnableLoginAgency = true)
 **		若載具的登入密碼等於設定值時，元件會回錯誤
 **		設定值可為多組，以{}區隔，如：
 **		{1234}{1111} 表當登入密碼為 1234 或 1111 時系統會回錯誤
 ** P7SignatureClientObj.setIllegalPIN("{1222}")
 **
 ** 設定是否於同一網頁上多簽章時紀錄密碼(只可作用在使用載具上)
 **		預設為 false
 ** P7SignatureClientObj.PWDCatch = false;	
 */
function initPkcs7Obj()
{
	return true;
}

/*******
 ** CertDB 物件初使設定
 **
 ** 是否可以選擇過期憑證
 ** CertDBClientObj.BlockExpiredCert = true; // false
 **
 ** 設定 CSP 種類，可多個條件
 ** CertDBClientObj.TokenProvider = "{Datakey RSA CSP}{Microsoft Enhanced Cryptographic Provider v1.0}"
 **
 ** 設定 憑證 載具種類
 ** SOFTWARE = 1
 ** HARDWARE = 2
 ** CertDBClientObj.TokenType = SOFTWARE; // HARDWARE;
 ** 
 ** 憑證金鑰使用別
 ** 0：所有憑證
 ** 1：簽章功能之憑證
 ** 2：加密功能之憑證
 **
 */
var CERT_TYPE_ALL = 0;
var CERT_TYPE_SIGN = 1;
var CERT_TYPE_ENC = 2;
function initCertDBObj()
{
	//CertDBClientObj.init();
	return true;
}


/*******
 ** CertDB 物件挑選憑證方法
 ** 無論使用哪一個，都會參考在 init 中所設定的挑選條件
 ** 必需有符合那些條件才可以
 **
 ** 挑出所有憑證
 ** selectCertificate()
 ** selectCertificate_Ex(certType)
 **
 ** 依指定的 Subject 挑出符號條件的憑證
 ** selectCertificateBySubject(subject)
 ** selectCertificateBySubject_Ex(subject, certType)
 **
 ** 依指定的 provider 挑出符號條件的憑證
 **		provider 可為多組，以{}區隔
 ** selectCertificateByProvider(provider)
 ** selectCertificateByProvider_Ex(provider, certType)
 **
 ** 依指定的 serialNo 挑出符號條件的憑證
 ** selectCertificateBySerialNum(serialNo)
 **
 ** 依指定的 common name 挑出符號條件的憑證
 ** selectCertificateBySubjectCN(commonName)
 ** selectCertificateBySubjectCN_Ex(commonName, certType)
 **
 ** 依指定的 issuer 挑出符號條件的憑證
 **		issuer 可為多組，以{}區隔
 ** {C=TW,O=HiTRUST Inc.,OU=HiTRUST FXML User CA - Test 1,CN=HiTRUST FXML Banking - Test 1}{C=TW,O=TAIWAN-CA.COM Inc.,OU=Evaluation Only,CN=TaiCA Test FXML CA}
 ** selectCertificateByMultipleIssuer(multipleIssuser)
 ** selectCertificateByMultipleIssuer_Ex(multipleIssuser, certType)
 **
 ** 依指定的 issuer 及 憑證序號 挑出符號條件的憑證
 **		需注意 issuer 不可含有 ','
 ** getCertificate(document.thisform.IssuerName.value, document.thisform.SerialNumber.value )
 **
 */
function selectCertificateBySerialNum(SN)
{
	var lRtn = 0;
	p7Sign_SignCertSN = SN;
	//選擇簽章憑證
	if (p7Sign_SignCertSN != "")
	{
		if ((lRtn = CertDBClientObj.selectCertificateBySerialNum(SN)) !=0)
		{
			showHelpMsg("選擇憑證", lRtn, CertDBClientObj.LastErrorStr);
			return false;
		}
	} else if ((lRtn = CertDBClientObj.selectCertificate()) !=0)
		//選擇簽章憑證
	{
		showHelpMsg("選擇憑證", lRtn, CertDBClientObj.LastErrorStr);
		return false;
	}
	

	p7Sign_SignCert = CertDBClientObj.certificate;
	
	return true;
}



function SelectCertificate()
{
	var lRtn = 0;

	//選擇簽章憑證
	if (p7Sign_SignCertSN != "")
	{
		if ((lRtn = CertDBClientObj.selectCertificate()) !=0)
		{
			showHelpMsg("選擇憑證", lRtn, CertDBClientObj.LastErrorStr);
			return false;
		}
	} else if ((lRtn = CertDBClientObj.selectCertificate()) !=0)
		//選擇簽章憑證
	{
		showHelpMsg("選擇憑證", lRtn, CertDBClientObj.LastErrorStr);
		return false;
	}
	

	p7Sign_SignCert = CertDBClientObj.certificate;
	return true;
}

function getCertSubjectName(){
	alert(CertDBClientObj.subjectName);
}

function getCN(tarObj){
	var subject = CertDBClientObj.subjectName;
	var CnIdx = subject.indexOf("CN");
	var adjustIdx = 0;
//alert(subject);
	if(CnIdx<0){
		tarObj.value = "";
		return false;
	}
	if(subject.substring(CnIdx+2,CnIdx+3)=="=") {CnIdx += 3;adjustIdx=3;}
	if(subject.substring(CnIdx+3,CnIdx+4)=="=") {CnIdx += 4;adjustIdx=3;}


	var CnEndIdx = subject.indexOf(",");
	if(CnEndIdx<0){
		tarObj.value = "";
		return false;
	}
//alert(subject.substring(CnIdx,CnEndIdx-CnIdx+adjustIdx));
	tarObj.value = subject.substring(CnIdx,CnEndIdx-CnIdx+adjustIdx);
	return true;
}

function showHelpMsg(operation, errCode, errMsg)
{
	//alert(operation);
	var msg;

	switch(errCode) {
		case 1:
			msg = "[1]傳入參數錯誤(Parameter Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 2:
			msg = "[2]請關閉IE瀏覽器重新操作";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 3:
			msg = "[3]功能處理錯誤(Operation Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 4:
			msg = "[4]請確認是否已插入含有正確憑證之卡片，且IE中已註冊有該憑證";
			break;
		case 5:
			msg = "[5]憑證或CRL格式錯誤(Certificate/CRL Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 6:
			msg = "[6]憑證或CRL找不到簽發單位(Issuer not Found)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 7:
			msg = "[7]憑證或CRL驗證失敗(Verify Failure by Issuer Public Key)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 8:
			msg = "[8]憑證已過期(Expired)";
			break;
		case 9:
			msg = "[9]憑證未登記(Not Register)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 10:
			msg = "[10]憑證已暫禁(Suspend)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 11:
			msg = "[11]憑證已註銷(Canceled)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 12:
			msg = "[12]找不到對應的Public Key (Public Key not Found)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 13:
			msg = "[13]找不到對應的Private Key (Private Key not Found)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 14:
			msg = "[14]憑證的Public Key與Private Key不相符(Key Pair not Match)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 15:
			msg = "[15]上載資料格式錯誤(Update Data Format Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 16:
			msg = "[16]下載資料格式錯誤(Download Data Format Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 17:
			msg = "[17]輸入資料格式錯誤(Input Data Format Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 18:
			msg = "[18]Template格式錯誤(Template Format Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 19:
			msg = "[19]資料(如XML element)找不到(Data not Found)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 20:
			msg = "[20]檔案找不到(File not Found)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 21:
			msg = "[21]卡片密碼錯誤(Password Invalid)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 22:
			msg = "[22]通訊錯誤(URL not Found or not Responsed)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			//return "[22]通訊錯誤(URL not Found or not Responsed)";
		case 23:
			msg = "[23]無法正確使用您的數位憑證。\r\n" +
					   "有下列可能原因：\r\n"
					   " 1. 開機進入 Windows95/98/ME 時未使用正確帳號登入\r\n" +
					   "      請先登出後重新登入，並用『當初申請或匯入憑證時所使用之電腦帳號』登入。\r\n" +
					   " 2. 私密金鑰設為高安全性保護，而您未輸入正確密碼\r\n" +
					   "      當系統提示時，請輸入保護私密金鑰的正確密碼。"
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 24:
			msg = "[24]金鑰型態錯誤(Key Type Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 25:
			msg = "[25]演算法錯誤(Algorithm Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 26:
			msg = "[26]金鑰長度錯誤(Key Length Error)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 27:
			msg = "[27]金鑰找不到(Key Id not Found)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		case 28:
			msg = "[28]使用者取消(User Cancel)";
			break;
		case 99:
			msg = "[99]其他錯誤(Others)";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			break;
		default:
			msg = "不明錯誤";
			msg += "\r\n";
			msg += "系統訊息:"+errMsg;
			msg += "\r\n";
			msg += "錯誤碼:"+errCode;
	}

	if(operation != "")
		msg = operation + "\r\n" + msg;
	
	alert(msg);
	return msg;
}
