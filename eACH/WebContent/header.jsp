<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page import="java.util.Map"%>
<%
String str = "佈版時間：2021/08/24 15:45:00";
//pro 中需要保留 str
//String str = "";
%>
<script type="text/javascript" src="./js/PageSession.js"></script>

<script type="text/javascript">
<!--
start_Countdown(60);


//-->
</script>

<div id="block_banner">

	<div id="funcRow">
		<img class="headerBtn" src="./images/button01.png" onmouseout="this.src='./images/button01.png'" onmouseover="this.src='./images/button01over.png'" style="cursor:pointer"/>
		<img class="headerBtn" src="./images/button03.png" onmouseout="this.src='./images/button03.png'" onmouseover="this.src='./images/button03over.png'" style="cursor:pointer" id="logout"/>
<!-- 		<img class="headerBtn" src="./images/button01.png" onmouseout="this.src='./images/button01.png'" onmouseover="this.src='./images/button01over.png'" style="cursor:pointer" onclick="chgmsg()"/> -->
<!-- 		TODO 還要判斷是否為正式區 -->
		<div>
<!-- 			公告訊息預定區塊 -->
<!-- 			<div style="visibility:hidden" >系統公告</div> -->
<!-- 			<div id = "bulletin" align="right" style="background-color:#e3e9f4 ; float:right; width:450px "  >快訊:營業日:20150724，清算階段:01 結算通知已發佈</div> -->
			<div id = "bulletin_div" class="nomsg" ><marquee id = "bulletin" scrolldelay="150" onMouseOver="this.stop()" onMouseOut="this.start() "></marquee></div>
			<br>
			<logic:equal name="login_form" property="isFormal" value="D" >
			<span style="font-size:2em;color:red;font-family:'Microsoft JhengHei'">&lt;系統開發區&gt;</span>
			<logic:equal name="login_form" property="userData.USER_TYPE" value="A" >
<!-- 			20150527 edit by hugo 目前此功能已由批次取代，與所方討論後先註解-->
<!-- 			<a href="#" onclick="go()" style="color:white">→&nbsp;手動執行報表批次作業</a>&nbsp;&nbsp;|&nbsp; -->
			</logic:equal>
			<span><%=str%></span>&nbsp;
			<div id="pageCountdownLabel">
				&nbsp;|&nbsp;剩餘&nbsp;<span id="maxInactiveInterval"></span>&nbsp;後自動登出
			</div>
			</logic:equal>

			<logic:equal name="login_form" property="isFormal" value="T" >
			<span style="font-size:2em;color:red;font-family:'Microsoft JhengHei'">&lt;系統測試區&gt;</span>
			<logic:equal name="login_form" property="userData.USER_TYPE" value="A" >
<!-- 			<a href="#" onclick="go()" style="color:white">→&nbsp;手動執行報表批次作業</a>&nbsp;&nbsp;|&nbsp; -->
			</logic:equal>
			<span><%=str %></span>&nbsp;
			<div id="pageCountdownLabel">
				&nbsp;|&nbsp;剩餘&nbsp;<span id="maxInactiveInterval"></span>&nbsp;後自動登出
			</div>
			</logic:equal>

			<logic:equal name="login_form" property="isFormal" value="P" >
<!-- 			<br><br> -->
			<div id="pageCountdownLabel">
				&nbsp;剩餘&nbsp;<span id="maxInactiveInterval"></span>&nbsp;後自動登出
			</div>
			</logic:equal>
		</div>
	</div>
</div>
<div id="block_nav">
	<logic:present name="login_form" property="scaseary">
		<ul class="dropdown">
			<bean:size id="size1" name="login_form" property="scaseary" />
			<logic:iterate id="map1" name="login_form" property="scaseary" indexId="inum1">
				<bean:size id="size2" name="map1" property="SUB_LIST" />
				<%
					String classStr = "";
					if (inum1 == (size1 - 1)) {
						classStr += "last";
					}
					if (size2 > 0) {
						classStr += " has-sub";
					}
				%>
				<li class="<%=classStr.trim()%>">
					<a href='#'>
						<span class="corner"></span>
						<span><bean:write name="map1" property="FUNC_NAME" /></span>
					</a>
					<logic:greaterThan name="size2" value="0">
						<ul>
							<logic:iterate id="map2" name="map1" property="SUB_LIST" indexId="inum2">
								<%
									String linkStr = "";
									String breadcrumb = "";
									classStr = "";
									if (inum2 == (size2 - 1)) {
										classStr += "last";
									}
								%>
								<logic:notEmpty name="map2" property="FUNC_URL">
								<%
									breadcrumb += ((Map) map1).get("FUNC_NAME") + "," + ((Map) map2).get("FUNC_NAME");
									//breadcrumb = URLEncoder.encode(breadcrumb, "UTF-8");
									linkStr += "getPage('" + ((Map) map2).get("FUNC_URL") + "','" + breadcrumb + "', '" + ((Map)map2).get("FUNC_ID") + "')";
								%>
								</logic:notEmpty>
								<li class="<%=classStr.trim()%>">
									<a href='#' onclick="<%=linkStr%>">
										<span><bean:write name="map2" property="FUNC_NAME" filter="false"/></span>
									</a>
								</li>
							</logic:iterate>
						</ul>
					</logic:greaterThan>
				</li>
			</logic:iterate>
		</ul>
	</logic:present>
</div>
<script>
	$("#logout").click(function(){logout('logout');});
	$("#pageCountdownLabel").hide();
	startPageActionCheck('maxInactiveInterval', parseFloat("${pageContext.session.maxInactiveInterval}"));
	
	/*
	$(".dropdown > li").hover(function(){
		var maxHeight = parseFloat($("#block_body").css('min-height').replace(/[^0-9]/g,''));
		var $container = $(this),
		$list = $container.find("ul"),
		$anchor = $container.find("a"),
		height = $list.height() * 1.1,       // make sure there is enough room at the bottom
		multiplier = height / maxHeight;     // needs to move faster if list is taller
		
		// need to save height here so it can revert on mouseout            
		$container.data("origHeight", $container.height());
		
		// don't do any animation if list shorter than max
		if (multiplier > 1) {
			$("body").css('overflow-y','hidden');
			$container.mousemove(function(e) {
				var offset = $container.offset();
				var relativeY = ((e.pageY - offset.top) * multiplier) - ($container.data("origHeight") * multiplier);
				if (relativeY > 30) {
					$list.css("top", -relativeY + $container.data("origHeight") + 30);
				};
			});
		}
	}, function(){
		$("ul", this).css('top', $(this).data("origHeight"));
		$("body").css('overflow-y','auto');
	});
	*/
	
</script>