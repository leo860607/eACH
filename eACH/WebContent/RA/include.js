var iSecurityObj
  = " <OBJECT classid='clsid:C9D64EB9-C3AD-4834-BD9C-2A64209E4B6D'"
  + "  codebase='ActiveX/ISecurityCom.cab#Version=7,12,6,1'"
  + "  id =ISecurityCom></OBJECT>";

document.write(iSecurityObj);

//-------------------------------------------	
function Replace(originalString, findText, replaceText)
{
	var pos=0
	var len=findText.length;
	pos = originalString.indexOf(findText)
	while (pos!=-1) {
		preString  = originalString.substring(0,pos);
		postString = originalString.substring(pos+len,originalString.length);
		originalString = preString + replaceText + postString;
		pos = originalString.indexOf(findText)
	}
	return(originalString);
}

//-------------------------------------------	
function getText(textObj,selectObj)
{
	if ( textObj.value=='' || textObj.value.charAt(0)==' ' ) { 
		selectObj.selectedIndex=0;
 		return;
	}
	//var key=selectObj.options[selectObj.length-1].value
	//if (key=="") key=textObj.value
	
	for (var i=0;i<selectObj.length;i++) {
		//if (key==selectObj.options[i].value.substring(0,key.length)) {
		if (textObj.value==selectObj.options[i].value) {
			selectObj.selectedIndex=i;
			return
		} else {
			if (i==(selectObj.length-1)) {
				textObj.select;
	    		alert(MSG('0013'));
	    		textObj.value="";
	    		selectObj.selectedIndex=0;
	    		textObj.focus();
			}
		}
	}
}
//-------------------------------------------	
function MSG(msgno)
{	var result
	if (msgno=='0001') {result='資料已存在，請查明後再行輸入！'; return result;};
	if (msgno=='0002') {result='資料範圍錯誤，請查明後再行輸入！'; return result;};
	if (msgno=='0003') {result='資料不存在，請查明後再行輸入！'; return result;};
	if (msgno=='0004') {result='此方向已無任何可供查詢之資料！'; return result;};                                                                                                                                                                                                                                                                                                                                                                                                                                         
	if (msgno=='0005') {result='找不到任何滿足查詢條件之資料！'; return result;};                                                                                                                                                                                                                  
	if (msgno=='0006') {result='無查詢之資料！'; return result;};                                                                                                                                                                                                                                  
	if (msgno=='0007') {result='無符合列印條件之資料！'; return result;};                                                                                                                                                                                                                          
	if (msgno=='0008') {result='資料錯誤，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                    
	if (msgno=='0009') {result='資料不完整，請重新輸入！'; return result;};                                                                                                                                                                                                                        
	if (msgno=='0010') {result='本欄不允許修改！'; return result;};                                                                                                                                                                                                                                
	if (msgno=='0011') {result='此欄位不允許輸入空白資料,請查明後再行輸入 ！'; return result;};                                                                                                                                                                                                    
	if (msgno=='0012') {result='無此欄位代號，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                
	if (msgno=='0013') {result='代碼不存在，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                  
	if (msgno=='0014') {result='代碼已存在，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                  
	if (msgno=='0015') {result='資料重覆，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                    
	if (msgno=='0016') {result='此鍵值無相關之明細檔資料！'; return result;};                                                                                                                                                                                                                      
	if (msgno=='0017') {result='無明細資料，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                  
	if (msgno=='0018') {result='資料修改中發生錯誤，請查明！'; return result;};                                                                                                                                                                                                                    
	if (msgno=='0019') {result='系統日期錯誤，請檢查！'; return result;};                                                                                                                                                                                                                          
	if (msgno=='0020') {result='日期區間錯誤，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                
	if (msgno=='0021') {result='日期重覆，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                    
	if (msgno=='0022') {result='日期(時間)資料錯誤，請重新輸入！'; return result;};                                                                                                                                                                                                                
	if (msgno=='0023') {result='日期不可大於系統日期！'; return result;};                                                                                                                                                                                        
	if (msgno=='0024') {result='無異動資料，請查明後再行輸入！'; return result;};                                                                                                                                                                                                                  
	if (msgno=='0025') {result='本資料已經修改！'; return result;};                                                                                                                                                                                                                                
	if (msgno=='0026') {result='請輸入鍵值，以找尋資料！'; return result;};                                                                                                                                                                                                                        
	if (msgno=='0027') {result='請修改資料！'; return result;};                                                                                                                                                                                                                                    
	if (msgno=='0028') {result='目前畫面為第一頁(筆)！'; return result;};                                                                                                                                                                                                                          
	if (msgno=='0029') {result='目前畫面為最後一頁(筆)！'; return result;};                                                                                                                                                                                                                        
	if (msgno=='0030') {result='本筆資料新增失敗，請檢查！'; return result;};                                                                                                                                                                                                                      
	if (msgno=='0031') {result='本筆資料更改失敗，請檢查！'; return result;};                                                                                                                                                                                                                      
	if (msgno=='0032') {result='本筆資料刪除失敗，請檢查！'; return result;};                                                                                                                                                                                                                      
	if (msgno=='0033') {result='列印完畢！'; return result;};                                                                                                                                                                                                                                      
	if (msgno=='0034') {result='資料處理中, 請稍候.....'; return result;};
	if (msgno=='0035') {result='查詢處理中, 請稍候.....'; return result;};
	if (msgno=='0036') {result='列印處理中, 請稍候.....'; return result;};
	if (msgno=='0037') {result='已超過可輸入之筆數，請查明後再行輸入！'; return result;};                                                                                                                                                                                                          
	if (msgno=='0038') {result='此承辦人不屬此承辦科，請查明後再行輸入！'; return result;};                                                                                                                                                                                                       
	if (msgno=='0039') {result='處理完成！'; return result;};                                                                                                                                                                                                                                      
	if (msgno=='0040') {result='您的簽入代碼或密碼錯誤,請查明後再行輸入！'; return result;};                                                                                                                                                                                                       
	if (msgno=='0041') {result='您的簽入密碼！'; return result;};                                                                                                                                                                                                           
	if (msgno=='0042') {result='資料確定要刪除, 請確認！'; return result;};                                                                                                                                                                                                             
	if (msgno=='0043') {result='使用者代碼重覆,請查明後再行輸入！'; return result;};
	if (msgno=='0051') {result='已新增成功！'; return result;};
	if (msgno=='0052') {result='已更正成功！'; return result;};
	if (msgno=='0053') {result='已刪除成功！'; return result;};
	if (msgno=='0054') {result='執行成功！'; return result;};
	if (msgno=='1001') {result='本案將由警察局相關單位進行處理, 謝謝你的熱心！'; return result;};
	if (msgno=='1002') {result='本案將由環保局相關單位進行處理, 謝謝你的熱心！'; return result;};
	if (msgno=='1003') {result='非原查報人, 無法變更資料！'; return result;};
	if (msgno=='1004') {result='已公告, 不允許變更！'; return result;};
	if (msgno=='1005') {result='本車號資料已存在,請查明！'; return result;};

	return '不明錯誤';
}
//-------------------------------------------	
function copyStr(copy,instring)
{
	var result=""
	for(var i=1;i<=copy;i++)
		{
		result += instring;
		}
	return(result);
}
//-------------------------------------------	
function LPAD(xStr, xCh, StrFormatleng) {
	var result
	xStr=xStr+"";
	result = copyStr(StrFormatleng - xStr.length , xCh) + xStr;
	return(result);
}

//-------------------------------------------	
function IsAlphe(str) {
	var result = true;
	var alphe = new Array
	alphe['A']=1;alphe['B']=1;alphe['C']=1;alphe['D']=1;alphe['E']=1;alphe['F']=1;alphe['G']=1;alphe['H']=1;alphe['I']=1;alphe['J']=1;alphe['K']=1;alphe['L']=1;alphe['M']=1;alphe['N']=1;alphe['O']=1;alphe['P']=1;alphe['Q']=1;alphe['R']=1;alphe['S']=1;alphe['T']=1;alphe['U']=1;alphe['V']=1;alphe['W']=1;alphe['X']=1;alphe['Y']=1;alphe['Z']=1;
	for (var j=0; j<str.length; j++)
	{
		if (result) {
			if (!alphe[str.charAt(j)]) {
				result = false;
			}
		}
	}	
	return(result);
}

//-----------------------yyy/mm/dd:hh--------------------	
function ValidateDate_b(formObj) {
	var monthdays=new Array() //每月天數 
	monthdays[1]=31;
	monthdays[2]=28;
	monthdays[3]=31;
	monthdays[4]=30;
	monthdays[5]=31;
	monthdays[6]=30;
	monthdays[7]=31;
	monthdays[8]=31;
	monthdays[9]=30;
	monthdays[10]=31;
	monthdays[11]=30;
	monthdays[12]=31;
	var validated=false;
	var inString=formObj.value;
	
	if ((inString.length==11)||(inString.length==12)) {
		if (inString.length==11)
			inString=" "+inString
		else {
			if (inString.charAt(0)=="0") {
				inString=" "+inString.substring(1,12);
				//formObj.value=inString.substring(1,12);
			}
		}
		if (!isNaN(inString.substring(0,3)+inString.substring(4,6)+inString.substring(7,9)+inString.substring(10,12))) {	//是數字
			var year=inString.substring(0,3);
			var month=inString.substring(4,6);
			var day=inString.substring(7,9);
			var hour=inString.substring(10,12);
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			if (day.charAt(0)=="0") day=" "+day.substring(1,2);
			if (hour.charAt(0)=="0") hour=" "+hour.substring(1,2);
			year=parseInt(year)+1911;
			month=parseInt(month);
			day=parseInt(day);
			hour=parseInt(hour);
			if ((month==2) && ((year%4)==0) && (((year%100)!=0) || ((year%400)==0)))
				var days=29
			else
				var days=monthdays[month];
			if (month<=12) {
				if (day<=days) {
					if (hour<=24) validated=true;
				} 				
			}
		}
	}

	if (validated)
		return true
	else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}
//-----------------------yyymmddhh--------------------	
function ValidateDate(formObj) {
	var monthdays=new Array() //每月天數 
	monthdays[1]=31;
	monthdays[2]=28;
	monthdays[3]=31;
	monthdays[4]=30;
	monthdays[5]=31;
	monthdays[6]=30;
	monthdays[7]=31;
	monthdays[8]=31;
	monthdays[9]=30;
	monthdays[10]=31;
	monthdays[11]=30;
	monthdays[12]=31;
	var validated=false;
	var inString=formObj.value;
	
	if ((inString.length==8)||(inString.length==9)) {
		if (inString.length==8)
			inString=" "+inString
		else {
			if (inString.charAt(0)=="0") {
				inString=" "+inString.substring(1,9);
				//formObj.value=inString.substring(1,12);
			}
		}
		if (!isNaN(inString.substring(0,3)+inString.substring(3,5)+inString.substring(5,7)+inString.substring(7,9))) {	//是數字
			var year=inString.substring(0,3);
			var month=inString.substring(3,5);
			var day=inString.substring(5,7);
			var hour=inString.substring(7,9);
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			if (day.charAt(0)=="0") day=" "+day.substring(1,2);
			if (hour.charAt(0)=="0") hour=" "+hour.substring(1,2);
			year=parseInt(year)+1911;
			month=parseInt(month);
			day=parseInt(day);
			hour=parseInt(hour);
			if ((month==2) && ((year%4)==0) && (((year%100)!=0) || ((year%400)==0)))
				var days=29
			else
				var days=monthdays[month];
			if (month<=12) {
				if (day<=days) {
					if (hour<=24) validated=true;
				} 				
			}
		}
	}

	if (validated) {
		formObj.value=formObj.value.substring(0,3)+"/"+inString.substring(3,5)+"/"+inString.substring(5,7)+":"+inString.substring(7,9);
		return true
	} else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}

//----------------------yyy/mm/dd---------------------	
function ValidateDate_b3(formObj) {
	var monthdays=new Array() //每月天數 
	monthdays[1]=31;
	monthdays[2]=28;
	monthdays[3]=31;
	monthdays[4]=30;
	monthdays[5]=31;
	monthdays[6]=30;
	monthdays[7]=31;
	monthdays[8]=31;
	monthdays[9]=30;
	monthdays[10]=31;
	monthdays[11]=30;
	monthdays[12]=31;
	var validated=false;
	var inString=formObj.value;
	
	if ((inString.length==8)||(inString.length==9)) {
		if (inString.length==8)
			inString=" "+inString
		else {
			if (inString.charAt(0)=="0") {
				inString=" "+inString.substring(1,9);
				//formObj.value=inString.substring(1,9);
			}
		}
		if (!isNaN(inString.substring(0,3)+inString.substring(4,6)+inString.substring(7,9))) {	//是數字
			var year=inString.substring(0,3);
			var month=inString.substring(4,6);
			var day=inString.substring(7,9);
			
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			if (day.charAt(0)=="0") day=" "+day.substring(1,2);
			
			year=parseInt(year)+1911;
			month=parseInt(month);
			day=parseInt(day);
			
			if ((month==2) && ((year%4)==0) && (((year%100)!=0) || ((year%400)==0)))
				var days=29
			else
				var days=monthdays[month];
			if (month<=12) {
				if (day<=days) {
					validated=true;
				} 				
			}
		}
	}

	if (validated)
		return true
	else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}
//----------------------YYYYMMDD---------------------	
function ValidateDate_west(inString) {
	var monthdays=new Array() //每月天數 
	monthdays[1]=31;
	monthdays[2]=28;
	monthdays[3]=31;
	monthdays[4]=30;
	monthdays[5]=31;
	monthdays[6]=30;
	monthdays[7]=31;
	monthdays[8]=31;
	monthdays[9]=30;
	monthdays[10]=31;
	monthdays[11]=30;
	monthdays[12]=31;
	var validated=false;
	//var inString=formObj.value;
	
	if ((inString.length==8)) {
		if (!isNaN(inString)) {	//是數字
			var year=inString.substring(0,4);
			var month=inString.substring(4,6);
			var day=inString.substring(6,8);
			
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			if (day.charAt(0)=="0") day=" "+day.substring(1,2);
			
			year=parseInt(year);
			month=parseInt(month);
			day=parseInt(day);
			
			if ((month==2) && ((year%4)==0) && (((year%100)!=0) || ((year%400)==0)))
				var days=29
			else
				var days=monthdays[month];
			if (month<=12) {
				if (day<=days) {
					validated=true;
				} 				
			}
		}
	}

	if (validated)
		return true
	else {
		//alert(MSG('0022'));
		//formObj.value="";
		//formObj.focus();
		return false;
	}	
}

//----------------------yyymmdd---------------------	
function ValidateDate3(formObj) {
	var monthdays=new Array() //每月天數 
	monthdays[1]=31;
	monthdays[2]=28;
	monthdays[3]=31;
	monthdays[4]=30;
	monthdays[5]=31;
	monthdays[6]=30;
	monthdays[7]=31;
	monthdays[8]=31;
	monthdays[9]=30;
	monthdays[10]=31;
	monthdays[11]=30;
	monthdays[12]=31;
	var validated=false;
	var inString=formObj.value;
	
	if ((inString.length==6)||(inString.length==7)) {
		if (inString.length==6)
			inString=" "+inString
		else {
			if (inString.charAt(0)=="0") {
				inString=" "+inString.substring(1,7);
				//formObj.value=inString.substring(1,9);
			}
		}
		if (!isNaN(inString)) {	//是數字
			var year=inString.substring(0,3);
			var month=inString.substring(3,5);
			var day=inString.substring(5,7);
			
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			if (day.charAt(0)=="0") day=" "+day.substring(1,2);
			
			year=parseInt(year)+1911;
			month=parseInt(month);
			day=parseInt(day);
			
			if ((month==2) && ((year%4)==0) && (((year%100)!=0) || ((year%400)==0)))
				var days=29
			else
				var days=monthdays[month];
			if (month<=12) {
				if (day<=days) {
					validated=true;
				} 				
			}
		}
	}

	if (validated) {
		formObj.value=formObj.value.substring(0,3)+"/"+inString.substring(3,5)+"/"+inString.substring(5,7);
		return true
	} else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}


//----------------------yyy/mm---------------------	
function ValidateDate_b2(formObj) {
	var validated=false;
	var inString=formObj.value;
	
	if ((inString.length==5)||(inString.length==6)) {
		if (inString.length==5)
			inString=" "+inString
		else {
			if (inString.charAt(0)=="0") {
				inString=" "+inString.substring(1,6);
			}
		}
		if (!isNaN(inString.substring(0,3)+inString.substring(4,6))) {	//是數字
			var year=inString.substring(0,3);
			var month=inString.substring(4,6);
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			year=parseInt(year)+1911;
			month=parseInt(month);
			if (month<=12) {
					validated=true;			
			}
		}
	}

	if (validated)
		return true
	else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}

//----------------------yyymm---------------------	
function ValidateDate2(formObj) {
	var validated=false;
	var inString=formObj.value;
	
	if ((inString.length==4)||(inString.length==5)) {
		if (inString.length==4)
			inString=" "+inString
		else {
			if (inString.charAt(0)=="0") {
				inString=" "+inString.substring(1,5);
			}
		}
		if (!isNaN(inString)) {	//是數字
			var year=inString.substring(0,3);
			var month=inString.substring(3,5);
			if (month.charAt(0)=="0") month=" "+month.substring(1,2);
			year=parseInt(year)+1911;
			month=parseInt(month);
			if (month<=12) {
					validated=true;			
			}
		}
	}

	if (validated) {
		formObj.value=formObj.value.substring(0,3)+"/"+inString.substring(3,5);
		return true
	} else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}


//----------------------YYYYMM---------------------	
function ValidateDate2C(formObj) {
	var validated=false;
	var inString=formObj.value;
	
	if (inString.length==6) {
		if (!isNaN(inString)) {	//是數字
			var year=inString.substring(0,4);
			var month=inString.substring(4,6);
			year=parseInt(year);
			month=parseInt(month);
			if (month<=12) {
					validated=true;			
			}
		}
	}

	if (validated) {
		formObj.value=formObj.value.substring(0,4)+"/"+inString.substring(4,6);
		return true
	} else {
		alert(MSG('0022'))
		formObj.value="";
		formObj.focus();
		return false;
	}	
}

//-------------------------------------------	
function Trim(instring) 
{
	if(instring!=null)
		{
		while (instring.charAt(0) == " ")
			instring=instring.substring(1,instring.length);
		while (instring.charAt(instring.length-1) == " ")
			instring=instring.substring(0,instring.length-1);
		}
	else
		instring="";
	return(instring);	
}

//-------------------------------------------	
function errUnquote(inString)
{
	var result
	result=Replace(inString,"'","’")
	result=result.substring(0,70)+"......"
	//result=Replace(result,"\"","”")
	return(result)
}
//-------------------------------------------
function getRadioValue(radioObject)
{
	var result = null
	for (var i=0;i<radioObject.length;i++) {
		if (radioObject[i].checked) {
			result = radioObject[i].value
			break
		}
	}
	return result
}
//-------------------------------------------
var local=new Array(34)   //建立身分證第一碼的識別庫
local[10]='A'
local[11]='B'
local[12]='C'
local[13]='D'
local[14]='E'
local[15]='F'
local[16]='G'
local[17]='H'
local[18]='J'
local[19]='K'
local[20]='L'
local[21]='M'
local[22]='N'
local[23]='P'
local[24]='Q'
local[25]='R'
local[26]='S'
local[27]='T'
local[28]='U'
local[29]='V'
local[32]='W'
local[30]='X'
local[31]='Y'
local[33]='Z'

//-------------------------------------------
function lengtherr(id){   //輸入長度檢測函式
  if(id.length<10)   //如果輸入的長度小於10個字元
    return 1   //就回覆錯誤為true的發生狀態
  else    //否則就回覆錯誤為false的發生狀態
    return 0
}
//-------------------------------------------
function firstlettererr(id){   //第一碼字元正確性檢測
  var fl=id.substring(0,1)   //取得第一碼字元值給fl變數
  var haserr=1   //建立錯誤發生旗標,預設是有錯誤
  for(i=10;i<=33;i++){   //從第一碼的識別庫中找尋是否有該字母
     if(local[i]!=fl)   //如果沒有就往下一個找
       continue
     else{   
   //如果有的話就把錯誤碼設為0表示沒有錯誤發生,因為第一碼已在識別庫中了
       haserr=0
       break
     }
  }
  if(haserr==1)   //如果錯誤發生旗標為1表示第一個字元並未出現在識別庫中
    return 1   //就回覆錯誤為true
  else
    return 0
}
//-------------------------------------------
function numerr(id){   //後九碼數字檢測函式
  var haserr=0   //建立錯誤發生旗標,預設是沒有錯誤的
  for(i=1;i<=9;i++){   //從第1個數字到第9個數字開始檢測
     if(parseInt(id.substring(i,i+1))>0 || id.substring(i,i+1)=='0')   //如果取得的字元為數字
       continue   //就往下一個去檢測
     else{   //若是發現有非數字的字元
       haserr=1   //就把錯誤旗標設為1的發生狀態
       break}   //並中止迴圈的執行
  }
  if(haserr==1)   //如果發生了錯誤
    return 1   //就回覆錯誤為true
  else
    return 0
}
//-------------------------------------------
function checkerr(id){   //檢查碼澰測函式
  var se=new Array(10)   //建立數字陣列
  var we=0   //建立權數計算總值變數
  var checkcode=0   //建立識別碼變數
  for(i=10;i<=33;i++){   //搜尋第一碼的英文字母在第機順位
     if(local[i]==id.substring(0,1)){   //如果找到弓就把該順位的第一碼給第一數字陣列
       se[0]=parseInt((i+'0').substring(0,1))
       se[1]=parseInt((i+'0').substring(1,2))   //把第二碼給第二數字陣列
       break   //並中止迴圈的執行
       }  
     }
  for(i=1;i<=9;i++){   //把身份證的後九個數字依次姶接下來的數字陣列項
     se[i+1]=parseInt(id.substring(i,i+1))
  }
  for(i=0;i<=10;i++){   //計算權數總值
     if(i==0)   //如果是第一個陣列值
       we=we+se[i]   //就把值直接加入(因為所乘的權數為1)
     else
       we=we+(se[i]*(10-i))   //否則就乘以權數
  } 
  checkcode=((10-mod(we,10))+'0').substring(0,1)   //取得檢查碼
  if(checkcode!=id.substring(9,10))   //檢視檢查碼是否正確
    return 1
  else
    return 0
}
//-------------------------------------------
function mod(a,b){   //取餘數之函數
  var r   //建立商的變數
  r=Math.round(a/b)   //以四捨五入的方式取得a/b的商,所以商可能會多1
  if((b*r)>a)   //如果多了1就把它減掉
    r-=1
  return (a-(b*r))   //傳回餘數
}
//--------------------統一編號檢核-----------------------
function chk_ban ( ban ) {
  var num = new Array(12);
  var checksum,a5,b5;

  if (ban.length != 8) return false;
   
  for (i=1; i<8; i++)
    if (ban.charAt(i)<'0' || ban.charAt(i)>'9') return false;
   
  num[0] = Math.round((ban.charAt(1) * 2 / 10) - 0.5) ;
  num[1] = Math.round((ban.charAt(3) * 2 / 10) - 0.5) ;
  num[2] = Math.round((ban.charAt(5) * 2 / 10) - 0.5) ;
  num[3] = Math.round((ban.charAt(6) * 4 / 10) - 0.5) ;

  num[4] = ((ban.charAt(1) * 2) % 10) ;
  num[5] = ((ban.charAt(3) * 2) % 10) ;
  num[6] = ((ban.charAt(5) * 2) % 10) ;
  num[7] = ((ban.charAt(6) * 4) % 10) ;

  num[8]  = ban.charAt(0) * 1;
  num[9]  = ban.charAt(2) * 1;
  num[10] = ban.charAt(4) * 1;
  num[11] = ban.charAt(7) * 1;

  checksum = 0;

  for(i=0 ; i<4 ; i++)
    checksum += num[i] + num[i + 4] + num[i + 8];
  
  if (ban.charAt(6) == 7 & (checksum % 10) != 0)
    {  checksum = 0; 
       a5 = Math.round(((num[3] + num[7]) / 10) - 0.5);
       b5 = ((num[3] + num[7]) % 10);
           
       for(i=0 ; i<3 ; i++)
        checksum += num[i] + num[i + 4] + num[i + 8];      

      checksum += a5 + num[11];

    }

  return (checksum%10==0) ? true : false;      
}
//======================YYYYMMDD=========================
function isdate(date_str){

  if (date_str.length != 8) return false;

  var year  = parseInt(date_str.substring(0,4));
  var month = parseInt(date_str.substring(4,6));  
  var days  = parseInt(date_str.substring(6,8));

  //if (date_str.charAt(0) == '0') year  = parseInt(date_str.charAt(1));
  if (date_str.charAt(4) == '0') month = parseInt(date_str.charAt(5));  
  if (date_str.charAt(6) == '0') days  = parseInt(date_str.charAt(7));  

  if (month < 1 || month > 12 || days < 1) return false;
  if ((month == 01 || month == 03 || month == 05 || month == 07 ||
       month == 08 || month == 10 || month == 12 ) & days > 31)   return false;

  if ((month == 04 || month == 06 || 
       month == 09 || month == 11   ) & days > 30)   return false;
  
  if (month == 02) {
     //year += 1911;

     if (((year % 4) == 0 && (year % 100) != 0) || (year % 400) == 0) {
      if (days > 29) return false;}
     else {
      if (days > 28) return false;}    
     }

  return true;
}
//======================YYMM=========================
function isdate2(date_str){
  if (date_str.length != 4) return false;
  if (date_str.substring(2,4) > '12' || date_str.substring(2,4) < '01')
    return false;  
 
  return true;
  }
//======================檢查EMail格式=========================
function isEmail(elmobj){
	if (elmobj.value == "") {
		alert(" E-Mail不得空白，請重新輸入 ");
		elmobj.focus();
		return false
	} else 
	if((elmobj.value.indexOf("@") != -1) && (elmobj.value.indexOf("@") < elmobj.value.lastIndexOf("."))) {
		return true
	} else {
		alert(" E-Mail格式錯誤，請重新輸入 ");
		elmobj.focus();
		return false;
	}
}
//======================檢查身分證字號格式=========================
function checkid(id){   //身分證檢測主函式
  id=id.toUpperCase()   //先把所有輸入的英文部分轉成大寫字母
  if(lengtherr(id)){   //做輸入長度的檢測
    alert('輸入的長度有誤!')
  }else if(firstlettererr(id)){   //做第一碼英文字母是否輸入正確的檢測
    alert('身分證第一碼並無此英文字母:'+id.substring(0,1))
	return false
  }else if(numerr(id)){   //做後九碼為是否數字的檢測
    alert('身分證後九碼應為數字!')
	return false
  }else if(checkerr(id)){   //做檢查碼是否正確的檢測
    alert('身分證檢查碼有誤!')
	return false
  }else{
    return true
  }
}
var EngNum="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
var Symbol="\"!@#$%^&*()_-+=~`|}]{[':;?\/>.<,";
//======================檢查流水號格式=========================
function chkSeqNo(elmobj){
	str=elmobj.value;
	if (str.length<=2) {
		for (var j=0; j<str.length; j++)
		{
			if (EngNum.indexOf(str.charAt(j))<0) {
				alert(" 流水號格式錯誤，請重新輸入 ");
				elmobj.focus();
				return false;		
			}
		}
	} else
		return false;
	return true;
}
//======================檢查自選編號格式=========================
function chkOptNo(elmobj){
	str=elmobj.value;
	if (str.length<=11) {	
		for (var j=0; j<str.length; j++)
		{
			if (EngNum.indexOf(str.charAt(j))<0) {
				alert(" 自選編號格式錯誤，請重新輸入 ");
				elmobj.focus();
				return false;		
			}
		}
	} else
		return false;
	return true;
}
//======================檢查姓名格式=========================
function chkName(elmobj){
	str=elmobj.value;
	
	if (elmobj.value == "") {
		alert(" 姓名不得空白，請重新輸入 ");
		elmobj.focus();
		return false
	}
	for (var j=0; j<str.length; j++)
	{
		if (Symbol.indexOf(str.charAt(j))>0) {
			alert(" 姓名格式錯誤，請重新輸入 ");
			elmobj.focus();
			return false;		
		}
	}	
	return true
}
//======================檢查檔名格式=========================
var forbidden=":\/*?\"<>|\\";
function chkFileName(elmobj){
	str=elmobj.value;
	
	if (elmobj.value == "") {
		alert(" 檔名不得空白，請重新輸入 ");
		elmobj.focus();
		return false
	}
	for (var j=0; j<str.length; j++)
	{
		if (forbidden.indexOf(str.charAt(j))>0) {
			alert(" 檔名格式錯誤(不得含有\\ \/ : * ? \" < > |)，請重新輸入 ");
			elmobj.focus();
			return false;		
		}
	}	
	return true
}
//---------------------chkIdNo 檢查ID格式----------------------
function chkIdNo(obj){
	inStr = obj.value
	if (inStr.length==10) {
		if (firstlettererr(inStr.substring(0,1))) {
			if (!chkOBU(inStr)) {
				alert(" 外籍人士稅籍編號格式錯誤，請重新輸入 ");
				obj.focus();	//外籍人士稅籍編號格式錯誤
				return false
			} else
				return true;
		} else {
			if (!checkid(inStr)) {
				alert(" 身分證字號格式錯誤，請重新輸入 ");
				obj.focus();	//身分證字號格式錯誤
				return false;
			} else
				return true;
		}
	} else {		
		if (inStr.length==8) {
			if (!chk_ban(inStr)) {
				obj.focus();
				alert(" 統一編號格式錯誤，請重新輸入 ");
				return false;
			} else 
				return true;
		} else {
			obj.focus();
			alert(" 身分證字號/統編/OBU 格式錯誤(長度應為八碼或十碼)，請重新輸入 ");
			return false;
		}
	}
}
//=======================檢查外國個人稅籍編號格式========================
function chkOBU(inString){
	if (inString.length==10) {
		if (!ValidateDate_west(inString.substring(0,8))) {	//前八碼是數字(西元日期)
			alert(" 外籍人士稅籍編號前八碼應為西元生日日期，請重新輸入 ");
			return false;
		}
		if (!IsAlphe(inString.substring(8,10).toUpperCase())) {//後兩碼是英文
			alert(" 外籍人士稅籍編號後兩碼應為護照英文名前兩個字母，請重新輸入 ");
			return false;
		}
		return true;
	} else {
		alert(" 外籍人士稅籍編號格式錯誤，請重新輸入 ");
		return false;
	}
}
//=======================檢查CN格式========================
function chkCN(obj){
	var inStr = obj.value;
	var pos = inStr.indexOf("-");
	var UID="",SeqNo="",OptNo=""

	if (pos==-1) {	//只有ID
		UID=inStr;
	} else {
		UID = inStr.substring(0,pos);
		inStr = inStr.substring(pos+1,inStr.length);
		pos = inStr.indexOf("-");
		if (pos==-1) {	//只剩流水號
			SeqNo = inStr;
			if(SeqNo.length==0) //最後一碼不能是"-"
				return false;
		} else {	//亦有自選編號
			SeqNo = inStr.substring(0,pos);
			OptNo = inStr.substring(pos+1,inStr.length);
			if(OptNo.length==0) //最後一碼不能是"-"
				return false;
		}
	}

	if (UID.length==10) {
		if (firstlettererr(UID.substring(0,1))) {
			if (!chkOBU(UID))
				return false
		} else {
			if (!checkid(UID))
				return false;
		}
	} else {		
		if (UID.length==8) {
			if (!chk_ban(UID))
				return false;
		} else
			return false;
	}

	if (SeqNo.length>0) {
		if (SeqNo.length<=2) {
			for (var j=0; j<SeqNo.length; j++) {
				if (EngNum.indexOf(SeqNo.charAt(j))==-1) {
					return false;		
				}
			}
		} else
			return false
	}
	if (OptNo.length>0) {
		if (OptNo.length<=11) {
			for (var j=0; j<OptNo.length; j++) {
				if (EngNum.indexOf(OptNo.charAt(j))==-1) {
					return false;		
				}
			}
		} else
			return false
	}
	return true
}
//---------------------字首大寫----------------------
function Capitalize(obj) {
	str=obj.value
	if (isNaN(str.charAt(0))) {
		obj.value = str.charAt(0).toUpperCase()+str.substring(1,str.length)
	}
}

//======================檢查憑證核發者通用名稱格式=========================
function chkIssuerCN(elmobj){
	str=elmobj.value;
	
	if (elmobj.value == "") {
		alert(" 憑證核發者通用名稱不得空白，請重新輸入 ");
		elmobj.focus();
		return false
	}
	for (var j=0; j<str.length; j++)
	{
		if (Symbol.indexOf(str.charAt(j))>0) {
			alert(" 憑證核發者通用名稱格式錯誤，請重新輸入 ");
			elmobj.focus();
			return false;		
		}
	}	
	return true
}
var nonDNSymbol="\"!@#$%^&*_+~`|}]{[':?\><";
//======================檢查憑證核發者識別名稱格式=========================
function chkIssuerDN(elmobj){
	str=elmobj.value;
	
	if (elmobj.value == "") {
		alert(" 憑證核發者識別名稱不得空白，請重新輸入 ");
		elmobj.focus();
		return false
	}
	for (var j=0; j<str.length; j++)
	{
		if (nonDNSymbol.indexOf(str.charAt(j))>0) {
			alert(" 憑證核發者識別名稱格式錯誤，請重新輸入 ");
			elmobj.focus();
			return false;		
		}
	}	
	return true
}
//======================檢查憑證使用者識別名稱格式=========================
function chkSubjectDN(elmobj){
	str=elmobj.value;

	if (elmobj.value == "") {
		alert(" 用戶識別名稱不得空白，請重新輸入 ");
		elmobj.focus();
		return false
	}
	for (var j=0; j<str.length; j++)
	{
		if (nonDNSymbol.indexOf(str.charAt(j))>0) {
			alert(" 用戶識別名稱格式錯誤，請重新輸入 ");
			elmobj.focus();
			return false;		
		}
	}	
	return true
}
var HexChar="ABCDEFabcdef0123456789";

//======================檢查憑證序號格式=========================
function chkCertSerNo(elmobj){
	str=elmobj.value;
	if (str.length>=8) {
		for (var j=0; j<str.length; j++)
		{
			if (HexChar.indexOf(str.charAt(j))<0) {
				alert(" 憑證序號格式錯誤，請重新輸入 ");
				elmobj.focus();
				return false;		
			}
		}
	} else {
		alert(" 憑證序號格式錯誤，請重新輸入(長度不足八碼) ");
		elmobj.focus();
		return false;
	}
	return true;
}
/*
function iSSign_portal(signatureValue,plainValue){
  var MySign = "";
  var vNum;
  vNum = Math.random();
  vNum = Math.round(vNum*10000000);
 
  if(ISecurityCom != null) {
	if(ISecurityCom.m_nMyLogin("iKey2032") ==0) {
	  // Sign
      MySign = ISecurityCom.m_nMySign("iKey2032",vNum);

	  // If sign fail , return
      if(MySign == "1") {
        alert("IC CARD簽章失敗!!");
        ISecurityCom.m_nMyLogout("iKey2032");
        return false;
      }

	  ISecurityCom.m_nMyLogout("iKey2032");
      signatureValue.value = MySign;
      plainValue.value = vNum;
      return true;

    } else {
      alert("IC CARD登入失敗!!");
      return false;
    }
  }
}
*/