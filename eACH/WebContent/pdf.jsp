<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<html>
	<head>
<!-- 		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" /> -->
<!-- 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
		<title>eACH圈存服務平台管理系統</title>
		<link rel="stylesheet" type="text/css" href="./css/reset.css">
		<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css">

 		<!–[if IE]>
<script src=./pdf_js/html5.js></script>
<![endif]–>
 
		<style type="text/css">
			body{background:#FFF;margin:0 auto;min-height:615px;overflow:hidden}
			input, select{font-size:1em;font-family:'Microsoft JhengHei'}
			#wrapper{width:800px;height:451px;background-image:url(./images/page_login.jpg);margin:0 auto;box-shadow:0 0 15px -2px rgba(20%,20%,40%,0.5);text-align:center;margin-top:5%}
			#block_banner{background-color:#fff;height:50px}
			#block_body{font-size:1.2em;background-image:url(./images/page_login_bottom.jpg);background-size:100%;}
			#indexBackground{width:100%;height:100%;background-image:url(./images/page_login.jpg);background-repeat:no-repeat;background-position:top center;background-size:100%}
			#loginPanel{position:relative;left:410px;top:210px;font-family:'Microsoft JhengHei';text-align:left}
			#loginPanel tr td:first-child{width: 100px; padding-top:10px}
			#block_footer{width: 800px;height:20px;background:#77ADDC;line-height:20px;text-align:center;color:navy;font-size:12px;margin:0 auto}
			.btn{background:#74b6e3;background-image:-webkit-linear-gradient(top,#74b6e3,#3287b8);background-image:-moz-linear-gradient(top,#74b6e3,#3287b8);background-image:-ms-linear-gradient(top,#74b6e3,#3287b8);background-image:-o-linear-gradient(top,#74b6e3,#3287b8);background-image:linear-gradient(to bottom,#74b6e3,#3287b8);-webkit-border-radius:5;-moz-border-radius:5;border-radius:5px;text-shadow:1px 1px 3px #666;-webkit-box-shadow:0 1px 3px #666;-moz-box-shadow:0 1px 3px #666;box-shadow:0 1px 3px #666;font-family:'Microsoft JhengHei';color:#fff;font-size:1em;padding:2px 10px;text-decoration:none;cursor:pointer}
			.btn:hover{background:#3cb0fd;background-image:-webkit-linear-gradient(top,#3cb0fd,#3498db);background-image:-moz-linear-gradient(top,#3cb0fd,#3498db);background-image:-ms-linear-gradient(top,#3cb0fd,#3498db);background-image:-o-linear-gradient(top,#3cb0fd,#3498db);background-image:linear-gradient(to bottom,#3cb0fd,#3498db);text-decoration:none}
		</style>
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		
		<script type="text/javascript" src="./RA/pwd.js"></script>
		<script type="text/javascript" src="./RA/include.js"></script>
		<script type="text/javascript" src="./RA/PKCS7SignClient.js"></script>
		
		<script type="text/javascript" src="./pdf_js/excanvas.js"></script>
		<script type="text/javascript" src="./pdf_js/jspdf.js"></script>
		<script type="text/javascript" src="./pdf_js/jspdf.min.js"></script>
		<script type="text/javascript" src="./pdf_js/html2canvas.js"></script>
<!-- 		<script type="text/javascript" src="./pdf_js/html2canvas_ie8.js"></script> -->
	</head>
	<body>
	<OBJECT classid="CLSID:7B91D073-398F-43CA-AFE4-0437F654AFEA"
        codebase="./RA/CMClientATL.cab#Version=1,8,3,13" width="0" height="0"
        align="left" hspace="0" vspace="0" id="CMClient"></OBJECT>
        <button id="pdf">PDF</button>
        <p><a class="btn btn-success" href="javascript:void(0);" onClick="generate();">Generate Screenshot &raquo;</a></p>
        <div id="wrapper">
        	<html:form styleId="formID" action="/login">
				<html:hidden property="ac_key" styleId="ac_key" value=""/>
				<html:hidden property="target" styleId="target" value=""/>
				<html:hidden property="RAOName" styleId="RAOName" value=""/>
				<html:hidden property="signvalue" styleId="signvalue" value=""/>
				<html:hidden property="ikeyValidateDate" styleId="ikeyValidateDate" value=""/>
		  		<table id="loginPanel">
		  			<tr>
		  				<td><html:text styleId="userId" property="userId" size="10" value="0188888-01"></html:text></td>
		  				<td>&nbsp;
		  					<font color="red" id="message"><bean:write name="login_form" property="msg"/></font>
		  				</td>
		  			</tr>
		  			<tr>
		  				<td colspan="2" style="vertical-align:middle">
		  					<label class="btn" id="login" onclick="onPut(this.id)">登入</label>
		  					<label class="btn" id="ikeyLogin" onclick="ikeyOnPut(this.id)">IKey登入</label>
		  				</td>
		  			</tr>
		  			<tr>
		  				<td colspan="2">
		  					<select onchange="$('#userId').val(this.value)">
		  						<option value="">=== 請選擇測試帳號 ===</option>
		  						<option value="0188888-01">0188888-01</option>
		  						<option value="4510000-01">4510000-01</option>
		  						<option value="4530000-01">4530000-01</option>
		  						<option value="4720000-01">4720000-01</option>
		  						<option value="9510010-01">9510010-01</option>
		  						<option value="9970018-01">9970018-01</option>
		  						<option value="1111111-01">1111111-01</option>
		  					</select>
		  				</td>
		  			</tr>
		  			<logic:present  name="isFormal" >
		  			<logic:equal name="isFormal"  value="T" >
			  			<tr>
			  			<td colspan="2" style="vertical-align:middle">
							<span style="font-size:2em;color:red;font-family:'Microsoft JhengHei'">&lt;系統測試區&gt;</span>
						</td>
						</tr>
					</logic:equal>
			  		<logic:equal name="isFormal"  value="D" >
				  		<tr>
				  		<td colspan="2" style="vertical-align:middle">
							<span style="font-size:2em;color:red;font-family:'Microsoft JhengHei'">&lt;系統開發區&gt;</span>
						</td>
						</tr>
					</logic:equal>
					</logic:present>
		  		</table>
		  	</html:form>
        </div>
        
        <div id="block_footer">
			Copyright © 2014-2099 台灣票據交換所 All Rights Reserved.
			<a href="./ActiveX/iKey.exe" style="float:right"><img src="./images/import.png"/> 下載 iKey.exe</a>
		</div>
		<script type="text/javascript">
		
		
		
		
		
		window.onload = function(){
			
			
// 			 $(".print").on("click", function(event) {
// 				 html2canvas($('body'), {
// 				  onrendered: function(canvas) {
// 				    var newWind=window.open('', "Print");
// 				    <!-- Must use html5 -->
// 				    newWind.document.write("<!doctype html><html><body><img src='"+canvas.toDataURL()+"'/></body></html>");
// 				    newWind.print();
// 				   }
// 				 });
// 				  });
			
			var content = $('#wrapper'),
			    cache_width = content.width(),
			    a4  =[ 595.28,  841.89];  // for a4 size paper width and height

			$('#pdf').on('click',function(){
				console.log('hello');
			    $('body').scrollTop(0);
			    createPDF();
			});
			//create pdf
			function createPDF(){
				console.log('hello1');
// 				var cv = getCanvas();
// 				console.log('cccv'+cv);
// 				 var 
// 			        img = cv.toDataURL("image/png"),
// 			        doc = new jsPDF({
// 			          unit:'px', 
// 			          format:'a4'
// 			        }); 
// 				 	doc.addImage(img, 'JPEG', 20, 20);
// 			        doc.save("Test");
// 			        content.width(cache_width);
			        
			        content.width((a4[0]*1.33333) -80).css('max-width','none');
			        if(window.console){console.log("content>>"+content);}
			        html2canvas(content, {
			            onrendered: function(canvas) {
			            // img is the Base64-encoded image
			            var img = canvas.toDataURL();
			            doc = new jsPDF({
				          unit:'px', 
				          format:'a4'
				        });
			            alert(img);
			            doc.addImage(img, 'JPEG', 20, 20);
				        doc.save("Test");
				        content.width(cache_width);
			            
			            }
			        });
			        
// 			    getCanvas().then(function(canvas){
// 			    	console.log('hello3');
// 			        var 
// 			        img = canvas.toDataURL("image/png"),
// 			        doc = new jsPDF({
// 			          unit:'px', 
// 			          format:'a4'
// 			        });     
// 			        doc.addImage(img, 'JPEG', 20, 20);
// 			        doc.save("Test");
// 			        content.width(cache_width);
// 			    });
			}

			// create canvas object
			function getCanvas(){
				console.log('hello2');
			    content.width((a4[0]*1.33333) -80).css('max-width','none');
			    var cv = html2canvas(content,{
			        imageTimeout:2000,
			        removeContainer:true
			    }); 
			    console.log('cv'+cv);
// 			    return cv;
// 			    return html2canvas(content,{
// 			        imageTimeout:2000,
// 			        removeContainer:true
// 			    }); 
			}

			};
		
		
		
			$(function(){
				$("#userId").attr("placeholder","用戶代號");
				$("#userCompany").attr("placeholder","單位代號");
				$('body').keypress(function(e){
					if(e.keyCode==13){
						$("#login").click();
					}
				});
			});
		
			function onPut(str){
				$("#ac_key").val(str);
				$("#target").val(str);
				$("form").submit();
			}
			
			//使用IKey登入
			function ikeyOnPut(str){
				var errorCode = 0;
				errorCode = CMClient.CM_Read_BasicData("iKey2032","BASEDATA");
				//成功
				if(errorCode == 0){
					//RAOName塞憑證的帳號
					$("#RAOName").val(CMClient.Get_BasicDataObj());
					//由於要用javascript的getSignedPKCS7StrByPara方法產生簽章String，所以先用ajax去取憑證資料的SNO(CiSIGNCERTSERIALNO)
					$.ajax({
						type:'POST',
						url:"/eACH/baseInfo?component=login_bo&method=getSNO&"+$("#formID").serialize(),
						async:false,
						dataType:'json',
						success:function(result){
							//取得SNO失敗
							if(result.result == "FALSE"){
								$("#message").text(result.msg);
							}
							//取得SNO
							else{
								//用ajax取得的ikeyValidateDate放在ikeyValidateDate的hidden欄位
								$("#ikeyValidateDate").val(result.ikeyValidateDate);
// 								20150420 add by hugo
								$("#userId").val($("#RAOName").val()); 
								//用ajax取得的SNO去產生簽章字串，並放在SignValue的hidden欄位
								getSignedPKCS7StrByPara("DataContent",result.msg,document.getElementById("signvalue"));
								$("#ac_key").val(str);
				 				$("#target").val("login");
				 				$("form").submit();
							}
						}
					});
				}
				//不成功
				else if(errorCode == -2147446781){
					alert("讀取卡片基本資料失敗 ，請確定插入的是已發卡且未做清除動作的卡片");
			    }
				//不成功
				else{
					alert("讀取卡片基本資料失敗");
				}
 			}
			
			
			
			
			
		</script>
		
		<script type="text/javascript">
(function (exports) {
    function urlsToAbsolute(nodeList) {
        if (!nodeList.length) {
            return [];
        }
        var attrName = 'href';
        if (nodeList[0].__proto__ === HTMLImageElement.prototype || nodeList[0].__proto__ === HTMLScriptElement.prototype) {
            attrName = 'src';
        }
        nodeList = [].map.call(nodeList, function (el, i) {
            var attr = el.getAttribute(attrName);
            if (!attr) {
                return;
            }
            var absURL = /^(https?|data):/i.test(attr);
            if (absURL) {
                return el;
            } else {
                return el;
            }
        });
        return nodeList;
    }

    function screenshotPage() {
        urlsToAbsolute(document.images);
        urlsToAbsolute(document.querySelectorAll("link[rel='stylesheet']"));
        var screenshot = document.documentElement.cloneNode(true);
        var b = document.createElement('base');
        b.href = document.location.protocol + '//' + location.host;
        var head = screenshot.querySelector('head');
        head.insertBefore(b, head.firstChild);
        screenshot.style.pointerEvents = 'none';
        screenshot.style.overflow = 'hidden';
        screenshot.style.webkitUserSelect = 'none';
        screenshot.style.mozUserSelect = 'none';
        screenshot.style.msUserSelect = 'none';
        screenshot.style.oUserSelect = 'none';
        screenshot.style.userSelect = 'none';
        screenshot.dataset.scrollX = window.scrollX;
        screenshot.dataset.scrollY = window.scrollY;
        var script = document.createElement('script');
        script.textContent = '(' + addOnPageLoad_.toString() + ')();';
        screenshot.querySelector('body').appendChild(script);
        var blob = new Blob([screenshot.outerHTML], {
            type: 'text/html'
        });
        
        var reader = new window.FileReader();
        reader.readAsDataURL(blob); 
        reader.onloadend = function() {
                       base64data = reader.result;                
                       console.log(base64data );
         }
        return blob;
    }

    function addOnPageLoad_() {
        window.addEventListener('DOMContentLoaded', function (e) {
            var scrollX = document.documentElement.dataset.scrollX || 0;
            var scrollY = document.documentElement.dataset.scrollY || 0;
            window.scrollTo(scrollX, scrollY);
        });
    }

    function generate() {
        window.URL = window.URL || window.webkitURL;
        window.open(window.URL.createObjectURL(screenshotPage()));
    }
    exports.screenshotPage = screenshotPage;
    exports.generate = generate;
})(window);



// 這個可能有解
// var el = document.documentElement;
// var preload = html2canvas.Preload(el, {
//     complete: function(images){
//         var queue = html2canvas.Parse(el, images);
//         // no need to wrap it into something and then unwrap again
//         var canvas = html2canvas.Renderer(queue);
//         // smaller image compared to the default image/png ;)
//         var a = canvas.toDataURL('imge/jpeg');
//         document.write(a);
//     }
// });
</script>
	</body>
</html>