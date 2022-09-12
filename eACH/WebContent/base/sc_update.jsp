<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
        <meta http-equiv="X-UA-Compatible" content="IE=8 ; text/html; charset=UTF-8" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/eACH/css/jquery-ui-latest.css">
		<link rel="stylesheet" type="text/css" href="/eACH/css/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="/eACH/css/style.css">
		<link rel="stylesheet" type="text/css" href="/eACH/css/validationEngine.jquery.css">
		<script type="text/javascript" src="/eACH/js/jquery-latest.js"></script>
		<script type="text/javascript" src="/eACH/js/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="/eACH/js/jquery.ui.datepicker-zh-TW.js"></script>
		<script type="text/javascript" src="/eACH/js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="/eACH/js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="/eACH/js/script.js"></script>
		<script type="text/javascript" src="/eACH/js/fstop.js"></script>
		<script type="text/javascript" src="/eACH/js/grid.locale-tw.js"></script>
		<script type="text/javascript" src="/eACH/js/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="/eACH/js/jquery.validationEngine-zh_TW.js"></script>
		<script type="text/javascript">
			var uri = "${pageContext.request.contextPath}"+"/baseInfo";
		</script>
		<style type="text/css">
		    #tpanel td{ text-align: center; border: 1px solid white; font-size:14px; } 
			#tpanel th {border: 1px solid white;background-color: #7081B9; color:white; font-size:14px;}
		body {background-color: #F1F5F8;}
		</style>
		<title>更新代付發動者基本資料</title>
</head>
<body>
<html:form styleId="formID" action="/sc_com" method="POST">
	<table id="search_tab">
		<tr>
			<td>請選擇營業日區間：</td>
			<td style="width: 300px">
			    <html:text styleId="START_DATE" property="START_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate,twPast[#END_DATE]] text-input datepicker"></html:text>
				~<html:text styleId="END_DATE" property="END_DATE" size="8" maxlength="8" styleClass="validate[required,maxSize[8],minSize[8],twDate] text-input datepicker"></html:text>
			</td>
			<td><input type="button" id="send" value="確定" onclick="search()"></td>
		</tr>
	</table>
</html:form>
<br>
 <table id="tpanel"></table>
    <script type="text/javascript">
     $(document).ready(function () {
		init();
 	});

    
    function init(){
		setDatePicker();
		setDate();
		$("#formID").validationEngine({binded:false,promptPosition: "bottomLeft"});
	}
    
    function setDatePicker(){
    	//clear all datefield value
    	$(".datepicker").val("");
    	$(".datepicker").datepicker({
    		changeYear: true,
    		changeMonth: true,
    	    dateFormat: "",
    	    showOn: "button",
    	    buttonImage: "/eACH/images/calendar.png",
    	    format: 'yymmdd',
    	    onSelect: function(selected, evnt){
    			var padding = "";
    			for(i = 0 ; i < (8 - selected.length); i++){
    				padding += "0";
    			}
    			$(this).val(padding + selected);
//    			20150818 add by hugo 因應變更操作行機制 日期變更時要處發相關function 去取得總行清單
    			$(this).change();
    	    }
    	});
    }
    
    function resetTable(){
    	var table = document.getElementById("tpanel");
		var rowCount = table.rows.length;
		if(rowCount > 0) {
			for(var i=0;i<rowCount;i++){
				table.deleteRow(0);
			}
		}
    }
    
    function search(){
    	if($("#formID").validationEngine("validate")){
    		var START_DATE = $("#START_DATE").val();
        	var END_DATE = $("#END_DATE").val();
        	var action = $("#formID").attr('action');
        	
        	blockUI();
        	$.ajax({
    			type:'POST',
    			url:"/eACH/baseInfo?component=sc_com_bo&method=searchONBLOCKTAB&START_DATE="+START_DATE+"&END_DATE="+END_DATE+"&action="+action,
    			async:false,
    			dataType:'text',
    			success:function(result){
    				if(!result==''){
    					var txt = '{"detail":' + result + '}';
    					var obj = eval ("(" + txt + ")");
    					
    					var key, count = 0;
    					for(key in obj.detail) {
    						if(obj.detail.hasOwnProperty(key)) {
    							count++;
    						}
    					}
    					resetTable();
    					var isShort = 0;
    					for(var i=0;i<count;i++){
    						var IS_SHORT = obj.detail[i].IS_SHORT;
    						if(!IS_SHORT==''){
    							isShort+=1;
    						}
    					}
    					
    					
    					$("#tpanel").append("<tr><td colspan='2'><font size='3' color='red'>"+"總計更新 "+count+"筆資料： 異動"+(count-isShort)+"筆, 資料短缺"+isShort+"筆</font></td></tr>");
    					$("#tpanel").append("<tr><th style='width: 120px'>發動者統一編號</th> <th style='width: 120px'>發動分行代號</th> <th style='width: 120px'>交易代號</th>");
    					for(var i=0;i<count;i++){
    						var COMPANY_ID = obj.detail[i].COMPANY_ID;
    						var SND_BRBK_ID = obj.detail[i].SND_BRBK_ID;
    						var TXN_ID = obj.detail[i].TXN_ID;
    						
    						$("#tpanel").append("<tr><td style='width: 120px'>"+COMPANY_ID+"</td>"+
    						"<td style='width: 120px'>"+SND_BRBK_ID+"</td>"+
    						"<td style='width: 120px'>"+TXN_ID+"</td></tr>");
    					}
    				}else{
    					resetTable();
    					$("#tpanel").append("<tr><td style='width: 80px'><font size='3' color='red'>查無資料</font></td>");
    				}
    				
    			}
    		});
        	unblockUI();
    	}
    	
    }
    
    function setDate(){
    	var date = window.dialogArguments;
    	$("#START_DATE").val(date);
    	$("#END_DATE").val(date);
    }
    
    function blockUI(){
    	$.blockUI ({ message: 
    		'<font style="color: white; font-weight: bold">處理中</font><br><img src="/eACH/images/loading.gif"/>' 
    	});}
    </script>
</body>
</html>