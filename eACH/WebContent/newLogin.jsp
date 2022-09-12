<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page import="java.util.Map"%>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
<!-- 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
		<title>eACH圈存服務平台管理系統</title>
		<link rel="stylesheet" type="text/css" href="./css/login.css">
		<script type="text/javascript" src="./js/jquery-latest.js"></script>
		<script type="text/javascript" src="./js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="./js/script.js"></script>
		<script type="text/javascript" src="./js/fstop.js"></script>
		<style type="text/css">
			#maxInactiveInterval {color: blue}
		</style>
	</head>
	<body>
		<div id="wrapper" none="true">
			<html:form styleId="formID" action="/login">
				<html:hidden property="ac_key" styleId="ac_key"/>
				<html:hidden property="target" styleId="target"/>
				<html:hidden property="userCompany"/>
				<html:hidden property="userId"/>
				
				<div id="block_banner" _height="none">
					<img class="headerBtn" src="./images/button01.png" onmouseout="this.src='./images/button01.png'" onmouseover="this.src='./images/button01over.png'" style="cursor:pointer"/>
					<img class="headerBtn" src="./images/button03.png" onmouseout="this.src='./images/button03.png'" onmouseover="this.src='./images/button03over.png'" style="cursor:pointer" id="logout" onclick="onPut(this.id)"/>
					<div>
						<span style="font-size:2em;color:red;font-family:'Microsoft JhengHei'">&lt;系統測試區&gt;</span>
						<br><a href="#" onclick="go()" style="color:white">→&nbsp;手動執行報表批次作業</a>&nbsp;&nbsp;|&nbsp;
						<span>佈版時間：2015/04/13 10:00:00</span>
<!-- 						&nbsp;&nbsp;|&nbsp;剩餘 <span id="maxInactiveInterval"></span> 秒後自動登出 -->
					</div>
				</div>
				<div id="block_nav" _height="none">
					<logic:present name="login_form" property="scaseary">
						<ul>
							<bean:size id="size1" name="login_form" property="scaseary"/>
							<logic:iterate id="map1" name="login_form" property="scaseary" indexId="inum1">
								<bean:size id="size2" name="map1" property="SUB_LIST"/>
								<% String classStr = ""; if(inum1 == (size1 - 1)){classStr += "last";} if(size2 > 0){classStr += " has-sub";} %>
								<li class="<%=classStr.trim()%>">
									<a href='#'><span class="corner"></span><span><bean:write name="map1" property="FUNC_NAME"/></span></a>
									<logic:greaterThan name="size2" value="0">
										<ul>
											<logic:iterate id="map2" name="map1" property="SUB_LIST" indexId="inum2">
												<% String linkStr = ""; String breadcrumb = ""; %>
												<% classStr = ""; if(inum2 == (size2 - 1)){classStr += "last";} %>
												<logic:notEmpty name="map2" property="FUNC_URL">
													<%
														breadcrumb += ((Map)map1).get("FUNC_NAME") + "," + ((Map)map2).get("FUNC_NAME");
														//breadcrumb = URLEncoder.encode(breadcrumb, "UTF-8");
														linkStr += "getPage('" + ((Map)map2).get("FUNC_URL") + "','" + breadcrumb + "', '" + ((Map)map2).get("FUNC_ID") + "')";
													%>
												</logic:notEmpty>
												<li class="<%=classStr.trim()%>"><a href='#' onclick="<%=linkStr%>"><span><bean:write name="map2" property="FUNC_NAME" filter="false"/></span></a></li>
											</logic:iterate>
										</ul>
									</logic:greaterThan>
								</li>
							</logic:iterate>
						</ul>
					</logic:present>
					
					<!--
					<ul>
						<li><a href='#' onclick="getPage('newBody.jsp')"><span>系統管理</span></a>
							<ul>
								<li onclick="getPage('each_user.do')"><a href='#'><span>使用者管理</span></a></li>
								<li onclick="getPage('role_list.do')"><a href='#'><span>權限群組管理</span></a></li>
								<li onclick="getPage('func_list.do')"><a href='#'><span>功能清單管理</span></a></li>
								<li onclick="getPage('sys_para.do')"><a href='#'><span>系統參數維護</span></a></li>
								<li onclick="getPage('wk_date.do')"><a href='#'><span>營業日設定</span></a></li>
								<li onclick="getPage('cr_line.do')"><a href='#'><span>額度設定</span></a></li>
							</ul>
						</li>	
						<li ><a href='#' onclick="getPage('newBody.jsp')"><span>基本資料管理</span></a>
							<ul>
								<li onclick="getPage('bank_group.do')"><a href='#'><span>總行資料維護</span></a></li>
								<li onclick="getPage('bank_branch.do')"><a href="#"><span>分行資料維護</span></a></li>
								<li onclick="getPage('sd_com.do')"><a href="#"><span>代收發動者基本資料維護</span></a></li>
								<li onclick="getPage('sc_com.do')"><a href="#"><span>代付發動者基本資料維護</span></a></li>
								<li onclick="getPage('txn_code.do')"><a href="#"><span>交易代號維護</span></a></li>
								<li onclick="getPage('txn_err_code.do')"><a href="#"><span>交易錯誤代號維護</span></a></li>
								<li onclick="getPage('gl_err_code.do')"><a href="#"><span>一般錯誤代號維護</span></a></li>
								<li onclick="getPage('fee_code.do')"><a href="#"><span>手續費代號維護</span></a></li>
								<li onclick="getPage('txn_returnday.do')"><a href="#"><span>退回天數設定</span></a></li>
								<li class="last" onclick="getPage('business_type.do')"><a href="#"><span>業務類別維護</span></a></li>
							</ul>
						</li>
						<li><a href='#' onclick="getPage('newBody.jsp')"><span>代理清算管理</span></a></li>
						<li><a href='#' onclick="getPage('newBody.jsp')"><span>交易資料管理</span></a></li>
						<li><a href='#' onclick="getPage('newBody.jsp')"><span>查詢統計</span></a></li>
						<li><a href='#' onclick="getPage('newBody.jsp')"><span>報表列印</span></a></li>
						<li><a href='#' onclick="getPage('newBody.jsp')"><span>其他服務</span></a></li>
						<li class='last'><a href='#' onclick="getPage('newBody.jsp')"><span>作業程序子系統</span></a></li>
					</ul>
					-->
				</div>
		  		<div id="block_body" _height="auto">
		  			<iframe name="body_iframe" id="body_iframe" width=100% frameBorder="0" height=100% noResize scrolling=yes src="newBody.jsp"></iframe>
		  		</div>
				<div id="block_footer" _height="none">Copyright © 2014-2099 台灣票據交換所 All Rights Reserved.</div>
			</html:form>
		</div>
		<script type="text/javascript">
			eval(function(p,a,c,k,e,r){e=function(c){return c.toString(a)};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('$(q).l(4(){8 b=$(\'[f=t]\');e(b.s<=0)m n;8 c=$(j),$5=$(\'5\'),$1=$(\'[1=3]\'),6=b.7(3),k=6-b.2(),g=h($5.i(\'o\'))+h($5.i(\'p\')),1=$1.7(3)-$1.2(),9=0;$(\'[f=1]\').r(4(){9+=$(j).7(3)});4 d(){8 a=c.2()-9-g-k-1;e(a>6){b.2(a)}}c.u(4(){d()});d()});',31,31,'|none|height|true|function|body|autoDivHeight|outerHeight|var|delHeight||||fullHeight|if|_height|delBodyMargin|parseInt|css|this|autoDivBorder|load|return|false|marginTop|marginBottom|window|each|length|auto|resize'.split('|'),0,{}));
			var uri = "/eACH/baseInfo?";
			
			$(document).ready(function(){
				/* 20141208 HUANGPU AJAX版本取得MENU，暫不使用
				var rdata = {component:"func_list_bo",method:"getMenu"};
				var menuObj = fstop.getServerDataExII(uri, rdata, false); 
				createMenuBar(menuObj);
				*/
			});
			
			function createMenuBar(menuObj){
				var menuStr = "";
				if(menuObj != null){
					menuStr += "<ul>";
					for(var i = 0; i < menuObj.length; i++){
						menuStr += getMenuItem(menuObj[i]);
					}
					menuStr += "</ul>";
				}
				if(window.console){console.log(menuStr);}
				$("#block_nav").append(menuStr);
			}
			
			function getMenuItem(obj){
				var menuStr="", funcUrl="";
				if(obj != null){
					funcUrl = obj.FUNC_URL==null?"newBody.jsp":obj.FUNC_URL==""?"newBody.jsp":obj.FUNC_URL;
					if(obj.FUNC_TYPE == "1"){
						menuStr += '<li><a href="#" onclick="getPage(\''+funcUrl+'\')"><span>'+obj.FUNC_NAME+'</span></a>';
					}else{
						menuStr += '<li onclick="getPage(\''+funcUrl+'\')"><a href="#"><span>'+obj.FUNC_NAME+'</span></a>';
					}
					
					if(obj.SUB_LIST != null && obj.SUB_LIST.length > 0){
						menuStr += "<ul>";
						for(var i = 0; i < obj.SUB_LIST.length; i++){
							menuStr += getMenuItem(obj.SUB_LIST[i]);
						}
						menuStr += "</ul>";
					}
					menuStr += "</li>";
				}
				return menuStr;
			}
			
			//redirect after click menu item
			function getPage(url, breadcrumb, funcId){
				blockUI();
				//url  = getPath() + url + "?b=" + encodeURI(encodeURIComponent(breadcrumb));
				if(url.indexOf("?") != -1){
					url = getPath() + url + "b=" + encodeURI(encodeURIComponent(breadcrumb) + "&fcid=" + funcId);
				}else{
					url = getPath() + url + "?b=" + encodeURI(encodeURIComponent(breadcrumb) + "&fcid=" + funcId);
				}
				//if(console.log){console.log(url);}
				$("#block_body iframe").attr('src', url);
				$("#block_body iframe").load(function(){
					unblockUI();
				});
			}
			
			function onPut(str){
				$("#ac_key").val(str);
				$("#target").val(str);
				$("form").submit();
			}
			
			//TODO 測試用 執行報表批次
			function go(){
				var qs = "component=login_bo&method=executeBatch";
				var msg = fstop.getServerDataExII(uri+qs, null, false);
				if(msg != null){
					alert(msg.msg);
				}
			}
			
		</script>
	</body>
</html>