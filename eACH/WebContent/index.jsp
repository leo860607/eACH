<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
// String name = new String("中文測試".getBytes("ISO-8859-1"), "UTF-8");
// String name = new String("中文測試".getBytes("ISO-8859-1"), "BIG5");
// String name = new String("中文測試".getBytes("ISO-8859-1"), "MS950");
// System.out.print("中文index.name>>"+name);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=10 ; text/html; charset=UTF-8" />
<!-- 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
		<title>eACH圈存服務平台管理系統</title>
		<link rel="stylesheet" type="text/css" href="./css/reset.css">
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
		<script type="text/javascript" src="./js/jquery-3.2.1.min.js"></script>
<!-- 		<script type="text/javascript" src="./js/jquery-latest.js"></script> -->
		<script type="text/javascript" src="./js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>		
				
		<script type="text/javascript" src="./RA/pwd.js"></script>
		<script type="text/javascript" src="./RA/include.js"></script>
		<script type="text/javascript" src="./RA/PKCS7SignClient.js"></script>
				
		<!--安控元件 JS 檔-->
		<script type="text/javascript" src="./js/TCH_base.js"></script>
		<script type="text/javascript" src="./js/TCH_mid.js"></script>
		<script type="text/javascript" src="./js/BrowserDetect.js"></script>	
		
	</head>
<!-- 初始化安控元件 call doTryPort(); -->
	<body onload="doTryPort();">
	<OBJECT classid="CLSID:7B91D073-398F-43CA-AFE4-0437F654AFEA"
        codebase="./RA/CMClientATL.cab#Version=1,8,3,13" width="0" height="0"
        align="left" hspace="0" vspace="0" id="CMClient"></OBJECT>
        <div id="wrapper">
        	  <html:form styleId="formID" action="/login">
				<html:hidden property="ac_key" styleId="ac_key" value=""/>
				<html:hidden property="target" styleId="target" value=""/>
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
		  		<table id="loginPanel" width="36%">
		  			<tr>
		  				<td>&nbsp;
<%-- 		  					<font color="red" id="message"><bean:write name="login_form" property="msg"/></font> --%>
		  				</td>
		  			</tr>
		  			<tr>
		  				<td colspan="2" style="vertical-align:middle" id="loginIconImg">
		  					<label class="btn" id="ikeyLogin" onclick="tokenLogin()">登入</label>		  				
						</td>
<!-- 		  				<td colspan="2" style="vertical-align:middle"> -->
<!-- 		  					<label class="btn" id="ikeyLogin" onclick="ikeyOnPut(this.id)">IKey登入</label> -->
<!-- 		  				</td> -->
		  			</tr>
<!-- 		  			目前無需修改的區域		  			 -->
		  			<logic:present scope="session" name="login_form">
			  			<tr>
			  				<td colspan="3" style="width: 30%">&nbsp;
			  					<font color="red" id="message"><bean:write scope="session" name="login_form" property="msg"/></font>
			  				</td>
			  			</tr>
		  			</logic:present>
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
					<tr>
			  			<td colspan="1" style="vertical-align:middle">
							<a href="javascript:;" onclick="window.open('https://fcs.twnch.org.tw/FCS/systemCheck.jsp', 'systemcheck', config='height=500,width=600')" style="font-size:20px; float:right"><img src="./images/import.png"/> 環境檢測</a>
						</td>
					</tr>
		  		</table>
<%-- 		  		<bean:write name="isFormal"/> --%>		  		
		  	</html:form>
        </div>
        <div id="block_footer">
			Copyright © 2014-2099 台灣票據交換所 All Rights Reserved.
	</div>
	<jsp:include page="/fcs/fcscheck.jsp"></jsp:include>
	</body>
</html>