<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<script type="text/javascript" src="script/ht/TCH_base.js"></script>
<script type="text/javascript" src="script/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="script/jquery.blockUI.js"></script>
<script type="text/javascript" src="script/ht/TCH_mid.js"></script>
<script type="text/javascript" src="script/BrowserDetect.js"></script>

<script type="text/javascript">
var pfxFlag = false;
// function getcheckcaversion(iptV) {
// 	var data = '1.1805.8.2';
// 	if(iptV!=data){
// 		nowVs = data ;
// 		document.getElementById('HTCTL_Brow_VER').innerHTML='系統發現本電腦 版本非本所提供最新版本(您的版本'+iptV+'，最新版本'+data+')。' ;
// 		document.getElementById("errordtdid").style.display='';
// 		vsflag=false;
// 	}
// 	else{
// 		document.getElementById("HTCTL_Brow_VER").innerHTML = "符合" ;
// 		document.getElementById("testbtnid").style.display='block';
	
// 	}
// }
function getcheckcaversion(iptV) {
// 		alert("1234="+iptV);
    $.ajax({
		type : "POST",
		url : "/eACH/indexInfo?component=isec_version_bo&method=search_toJson&"+$("#formID").serialize(),
// 		url : "/eACH/indexInfo?component=isec_version_bo&method=search_toJson",
		async:false,
		dataType : "json",
		timeout : 100000,
		success : function(data) {
			
		var dataSr = String(data);
		var arr1 = iptV.split('.'),
          	arr2 = dataSr.split('.');
     	var minLength=Math.min(arr1.length,arr2.length),
          position=0,
          diff=0;
      //依次比較版本號每一位大小，對比得出結果後跳出迴圈
      	while(position<minLength && ((diff=parseInt(arr1[position])-parseInt(arr2[position]))==0)){
          position++;
     	 }
      	diff = (diff!=0)?diff:(arr1.length-arr2.length);
      	
      	if(diff>=0){
      		diff = true;
      	}else{
      		diff = false;
      	}		
//	      	alert("diff"+diff);
      	
		if(diff == false){
//				alert("sussess"+data);
			nowVs = data ;
			alert('系統發現本電腦 版本非本所提供最新版本(您的版本'+iptV+'，最新版本'+data+')。') ;
			vsflag=false;
			}			
		},
		error : function(e) {
			alert("error"+data);
			console.log("ERROR: ", e);
		},
		done : function(e) {
			alert("done"+data);
			console.log("DONE");
		}
	});
}

function doCustomAction(){
	
	var outputData = XHRDataObject.outputData;
	var curFuncName = XHRDataObject.curFuncName;
    
    
    //initSecCtrl -> 
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
                alert('元件初始化失敗');
            }
        
        }
    
	}else if(curFuncName == 'm_nMyLogin'){
		
        
		if(outputData.actionResult){
        
            document.LoginForm.cn.value = outputData.certificate;
   
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
                alert('元件初始化失敗');
            }
        
        }
        
	}else if(curFuncName == 'CM_Read_BasicData'){
		
        
		if(outputData.actionResult){
        
            var cn = outputData.dataValue;
        
            alert('載具讀取完成，使用者代碼為 '+cn);
        
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
		
        //console.log('selectCertificate');
        
		if(outputData.actionResult){
        
            var cert = outputData.certificate;
            
            //alert(cert);
            
            //執行簽章
            signP7( cert, 'DataContent');
            
        
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
                alert('使用者簽章完成。');
                pfxFlag = false;
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
            
            alert('完成，以下為憑證資訊：\n' + str);
        
        }else{
        alert('certificate 取得失敗，錯誤代碼：' + outputData.statusCode + 
                '訊息：' + outputData.errorMsg
            );
        }
    
        changePinFlag = false;
    
	}else if(curFuncName == 'doTryPort'){
		
        if(outputData.actionResult){
        	//triggerWaiting(false);
        	//document.getElementById("HTCTL_Brow_VER").innerHTML = "符合" ;
        	//document.getElementById("testbtnid").style.display='block';
        	GetAPIVersion();
        }else{
        	var msg = '';
        	if(browserDetect()){
        		var msg = '系統發現本電腦未啟動"票交所多瀏覽器安控程式"安裝相關安控元件。\n' +
        		'請確認該程式是否已正確啟動(檢視電腦右下角的啟動選項是否有 "票交所多瀏覽器安控程式")'
        		;
        	}else{
        		msg = outputData.errorMsg;
        	}
        	document.getElementById("HTCTL_Brow_VER").innerHTML = "<font color='red'>無法偵測</font>" ;
        	document.getElementById("errordtdid").style.display='';
        	//alert(msg);
        }
    
	}else if(curFuncName == 'GetAPIVersion'){
		if(outputData.actionResult){
			console.log(outputData);
			getcheckcaversion(outputData.version);
		}
	}
	
}



function fnLogin(){
	
    initSecCtrl();
    
}
doTryPort();
</script>
<style type="text/css">
body,body{
	margin:0;
	padding:0;
	line-height: 1.5em;
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
	font-size:14px;
	font-weight:normal;	
}
#popup{background:none; margin:14px;}
#printBlock{background:none;padding:0 0 0 2px;}
.printBlock ol li, ul li, p{color:#000;}e

.fbold{font-weight:bold;}
.vtop{vertical-align:top;}
.text-c{text-align:center;}
table td.lt, table th.lt{text-align:left;}y
table td.rt, table th.rt{text-align:right;}
table td.ct, table th.ct{text-align:center;}
table td,table th{vertical-align:middle; height: 26px;}

.tb_noline td{ border:0px #FFF solid; padding:3px 7px 3px 0px;font-size:14px;}
/* 表格內外距 */
.Mspace-b14{margin-bottom:14px;}
.Mspace-b7{margin-bottom:7px;}
/* btnBlock
------------------------------------ */
.btnBlock{
padding:14px 10px 10px 10px;text-align:center;}
.btn1{
	background: url(../../image/btn/back_w.png) no-repeat scroll 0px 0px #4B8DF9;
	display: inline-block;
color: #FFF !important;
border: 1px solid #3079ED;
border-radius: 3px;
margin: 2px 4px;
padding: 0px 14px 0px 14px;
line-height: 27px;
font-size: 13px;
font-weight: normal;
vertical-align: middle;
}
.btn_cancel {
    display: inline-block;
    color: #3079ED !important;
    border: 1px solid #3079ED;
    border-radius: 3px;
    background: none repeat scroll 0% 0% #FFF;
    margin: 2px 4px;
    padding: 0px 14px;
    line-height: 27px;
    height: 27px;
    font-size: 13px;
    font-weight: normal;
    vertical-align: middle;
}
.pop_tittle_b{
	font-size:20px;
	color:#231815;
	font-weight:bold;
	/*background-image: url(../images/img/title_line.gif);
	background-position: bottom;
	background-repeat: repeat-x;*/
	padding-top:15px;
	text-align:left;
	padding-bottom:6px;
	border-bottom-width: 2px;
	border-bottom-style: solid;
	border-bottom-color: #998f81;
	margin-bottom:14px;
}
.print_time{font-size:10px; color:#000; padding: 0 5px 5px 0; text-align:right;
display: block;}

/*原本Class*/
.text_title {
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
border-bottom: 1px solid #9C3 !important;
	color:#000;
	text-align:left; 
	font-weight:bold;
	line-height:1.7em;
    padding:4px 3px;
	text-align:center;
	font-size: 15px;
	text-indent: 6pt;
	letter-spacing: 2px;
	word-spacing: 2px;
  font-weight: bold;
}
.text_top {
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
	font-size: 14px;
	font-weight: normal;
	color: #FFFFFF;
	text-decoration: none;
	background-color: #71B515;
	letter-spacing: 1px;
	word-spacing: 1px;
	text-align: center;
	vertical-align: middle;
}
.text_c {
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
	font-size: 14px;
	text-decoration: none;
	letter-spacing: 0.7px;
	word-spacing: 0.7px;
	background-color: #FFFFFF;
	line-height: 20px;
}
.text-g {
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
	/*background:#F1F1EE;*/
	color:#000;
	line-height:1.7em;
    padding:3px 5px 1px 5px;
	letter-spacing: 0.7px;
	word-spacing: 0.7px;
	text-align: center;
}
.table {
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
	font-size: 14px;
	color: #000000;
	letter-spacing: 1px;
	word-spacing: 1px;
	border: 1px solid #4B9CA0;
	background-color: #FFFFFF;
}
.s-table {
	background-color: #fff;
}
.s-table td {
	padding: 5px;
	border-bottom: 1px #ccc solid;
}
.bottom-bk {
	font-family: "微軟正黑體",Helvetica,Arial,sans-serif,"STHeiti Light","儷黑 Pro","LiHei Pro","Microsoft Yahei","Microsoft JhengHei",新細明體;
	font-size: 14px;
	color: #000;
	height: 30px;
	border-right: none;
	background:#F7F9F8;
	text-align: center;
	font-size: 14px;
}
</style>

<title>系統環境偵測網頁</title>
</head>
<body text=#000000 bgColor=#ffffff leftMargin=0 topMargin=0" >
	<div align="center">
		<h2>系統環境偵測網頁</h2>
	</div>
	<table width="75%" border="0" align="center" cellpadding="1"
		cellspacing="1" class="s-table">
		<tr>
			<td colspan="2" class="text_title" align="center">目前您的電腦環境狀態</td>
		</tr>
		<tr class="text_c">
			<td align="center" valign="middle" class="text-g"><span>作業系統版本(OS)</span></td>
			<td align="left" >&nbsp;<span><SPAN ID="Result_OS_VER"><FONT color='red'>無法偵測</FONT></SPAN></span></td>
		</tr>
		<tr class="text_c">
			<td align="center" valign="middle" class="text-g"><span>瀏覽器版本</span></td>
			<td align="left">&nbsp;<span><SPAN ID="Result_Brow_VER"><FONT
						color='red'>無法偵測</FONT></SPAN></span>&nbsp;
			</td>
		</tr>
		<tr class="text_c">
			<td align="center" valign="middle" class="text-g"><span>安控程式</span></td>
			<td align="left">&nbsp;<span><SPAN ID="HTCTL_Brow_VER"><FONT color='red'>偵測中</FONT></SPAN></span>&nbsp;
			<div id="errordtdid" style="display:none;">
			<font color='red'>請點選『
			
				<a href="https://sectest.twnch.org.tw/ra/portal/ActiveX/TCHXHRSetup.zip">下載</a>
			
				
			
			</a>』安裝『安控元件』。若已安裝『安控元件』，請確認軟體已經在執行中</font>
			</div>
			<div id="testbtnid" style="display:none;">
			<FORM NAME="LoginForm" METHOD="POST">
			<INPUT TYPE="hidden" NAME="cardType" value="ETOKEN">
			<INPUT TYPE="hidden" NAME="SignValue" value=""> <INPUT
				TYPE="hidden" NAME="RAOName" value=""> <INPUT
				TYPE="hidden" NAME="cn" value="">
			   <INPUT TYPE=BUTTON NAME=Login VALUE="簽證元件驗證" onClick="fnLogin();">
			</FORM>
			</div>
			</td>
		</tr>
		<tr class="text_c">
			<td align="center" valign="middle" class="text-g"></td>
			<td align="left">&nbsp;
			<a href="/FCS/static/ht/SAC_8.3_20140915.exe">iKey Driver 下載</a>
			</td>
		</tr>
		
	</table>
	
<script>
var OS_Flag=navigator.userAgent;

//作業系統
if (OS_Flag.indexOf("NT 5.0")>0 ) {
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows 2000 )"; //4
}else if (OS_Flag.indexOf("NT 5.1")>0 ) {
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows XP )"; //5
}else if (OS_Flag.indexOf("Win 9x 4")>0 ) {
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows ME )";  //3
}else if (OS_Flag.indexOf("98")>0 ){
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows 98 )";  //2
}else if (OS_Flag.indexOf("95")>0 ){
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows 95 )";  //1
}else if (OS_Flag.indexOf("NT 5.2")>0 ){
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows 2003 )";  //6
}else if (OS_Flag.indexOf("NT 6.0")>0 ){
	if ((OS_Flag.indexOf("Win64")>0 ) || (OS_Flag.indexOf("WOW64")>0)) {
		document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows Vista 64位元  )";  //6
	} else {
		document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( Microsoft Windows Vista 32位元  )";  //6
	}
}else if (OS_Flag.indexOf("NT 6.1")>0 ){
	if ((OS_Flag.indexOf("Win64")>0 ) || (OS_Flag.indexOf("WOW64")>0)) {
		document.getElementById("Result_OS_VER").innerHTML = "符合 (  Microsoft Windows 7 64位元  )";  //6
	} else {
		document.getElementById("Result_OS_VER").innerHTML = "符合  ( Microsoft Windows 7 32位元  )";  //6
	}
}else if (OS_Flag.indexOf("NT 6.2")>0 ){
	if ((OS_Flag.indexOf("Win64")>0 ) || (OS_Flag.indexOf("WOW64")>0)) {
		document.getElementById("Result_OS_VER").innerHTML = "符合 ( Microsoft Windows 8 64位元  )";  //6
	} else {
		document.getElementById("Result_OS_VER").innerHTML = "符合( Microsoft Windows 8 32位元  )";  //6
	}
}else if (OS_Flag.indexOf("NT 6.3")>0 ){
	if ((OS_Flag.indexOf("Win64")>0 ) || (OS_Flag.indexOf("WOW64")>0)) {
		document.getElementById("Result_OS_VER").innerHTML = "符合(  Microsoft Windows 8.1 64位元  )";  //6
	} else {
		document.getElementById("Result_OS_VER").innerHTML = "符合 ( Microsoft Windows 8.1 32位元  )";  //6
	}
}else{
	document.getElementById("Result_OS_VER").innerHTML = "<font color='red'>不符合</font>( 非微軟作業系統  )"; //0
}

//解析度
var cs, cv=16;
if(cv!="Netscape"){
	c=screen.colorDepth;
}else{
	c=screen.pixelDepth;
}
var cs=c;
if(c==4) cs="16 色 (4 位元)";
if(c==8) cs="256 色 (8 位元)";
if(c==16) cs="高彩 (16 位元)";
if(c>16) cs="全彩 (" +cs+ " 位元)";
cs += "&nbsp;&nbsp;&nbsp;" + screen.width+" x "+screen.height + " 像素";
//document.getElementById("Result_MonPix").innerHTML = cs;


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
		bit_ver = "64位元"; 
	} else {
		bit_ver = "32位元"; 
	}	


//if(navigator.userAgent.match(vn)){
if(tmp_ver>5){
	if(tmp_ver>=10){
		document.getElementById("Result_Brow_VER").innerHTML = "符合" ;
	}
	//document.getElementById("Result_Brow_VER").innerHTML = "Internet Explorer(IE) " + tmp_ver + " " + bit_ver + " " + compatible;
}else{
	document.getElementById("Result_Brow_VER").innerHTML = "符合" ;
	//document.getElementById("Result_Brow_VER").innerHTML = BrowserDetect.browser;
}


</script>
<!--(Begin)Javascript版本偵測-->
<script language="JavaScript">
//JS
var version=1;
</script>
<script language="JavaScript1.1">
var version=1.1;
</script>
<script language="JavaScript1.2">
var version=1.2;
</script>
<script language="JavaScript1.3">
var version=1.3;
</script>
<script language="JavaScript1.4">
var version=1.4;
</script>
<script language="JavaScript1.4.2">
var version=1.4.2;
</script>
<script language="JavaScript1.5">
var version=1.5;
</script>
<script language="JavaScript1.6">
var version=1.6;
</script>
<script language="JavaScript1.7">
var version=1.7;
</script>
<script language="JavaScript1.8">
var version=1.8;
</script>
<script language="JavaScript2.0">
var version=2.0;
</script>
<script language="JavaScript">
if(version >= 1.2){
  //document.getElementById("Result_JS").innerHTML="JavaScript "+ version;
}
else{
  //document.getElementById("Result_JS").innerHTML="JavaScript "+ version +"<br>(<a href='http://www.microsoft.com/downloads/details.aspx?familyid=9AE91EBE-3385-447C-8A30-081805B2F90B&displaylang=zh-tw' target='_black' style='cursor:hand;text-decoration:underline;color:red;'>請先更新瀏覽器版本至IE 6.0以上</a>)";
}
	

</script>

</body>
</html>