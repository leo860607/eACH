var logoutCountdownObj;
//usage : waitForFinalEvent(function(){ /*Your function*/ }, 500, "some unique string");
var waitForFinalEvent = (function() {
	var timers = {};
	return function(callback, ms, uniqueId) {
		if (!uniqueId) {
			uniqueId = "Don't call this twice without a uniqueId";
		}
		if (timers[uniqueId]) {
			clearTimeout(timers[uniqueId]);
		}
		timers[uniqueId] = setTimeout(callback, ms);
	};
})();

$(document).ready(function(){
	if($.blockUI){
		$.blockUI.defaults.css = { 
				padding: 0,
	            margin: 0,
	            width: '30%',
	            top: '40%',
	            left: '35%',
	            textAlign: 'center',
	            cursor: 'wait'
		};
	}
	setTWDatepicker();
	document.onkeypress = stopRKey;
	
	$("label[id=search]").click(function(){
		gPageSessionDate = new Date();
		gLastPageActionTime = gPageSessionDate.getTime();
		gPageIdleTime = 0;
		gAwakeAfterNextMove = false;
	});
});

function start_Countdown(time){
	setRepeaterII(after_Countdown, [time], 1);
}

function after_Countdown(time , time2){
//	if(window.console){console.log("time>>"+time);}
	var nextSec = time;
//	if(window.console){console.log("nextSec>>"+nextSec);}
	if(nextSec == time2){
		getPublicData();
		
	}else{
		
	}
	
}

function chgmsg(obj){
	
//	if(window.console){console.log("obj"+obj);}
//	if(window.console){console.log("obj"+obj[0].CHCON);}
	if(fstop.isNotEmpty(obj) && fstop.isNotEmpty(obj[0]) && fstop.isNotEmpty(obj[0].CHCON)){
//		if(window.console){console.log("obj22"+obj[0].CHCON);}
		$("#bulletin_div").addClass("msg");
		$("#bulletin_div").removeClass("nomsg");
		$("#bulletin").html(obj[0].CHCON);
	}else{
		$("#bulletin_div").addClass("nomsg");
		$("#bulletin_div").removeClass("msg");
	}
	
}

function getPublicData(){
	var uri = getPath()+'baseInfo';
//	if(window.console){console.log("uri"+uri);}
	var rdata = {component:"bulletin_bo", method:"getPublicData"};
	var retResult = fstop.getServerDataExIII(uri, rdata, false , chgmsg ,null,'chgmsg' );
}


/**
 * 
 * @param id  ex:table.id
 * @param elm_name ex:'input'
 * @param obj_id  要塞入值得元素的id
 */
function getSearch_condition(id ,  elm_names , obj_id){
	if(window.console){console.log("id>>"+id);}
	var serchs = {};
	$("#"+obj_id).val("");
	var elm_Ary =  elm_names.split(",");
//	if(window.console){console.log("elm_Ary>>"+elm_Ary);}
	for(var elm_name in elm_Ary){
//		if(window.console){console.log("elm_name>>"+elm_name);}
		$.each($('#'+id).find(elm_Ary[elm_name]).serializeArray(), function(i, field ) {
//			serchs[field.name] = field.value;
			serchs[field.name] = encodeURI(field.value);
		});
	}
	serchs['action'] = $("#formID").attr('action');
	
	if(window.console){console.log("JSON.stringify(serchs)"+JSON.stringify(serchs));}
	$("#"+obj_id).val(JSON.stringify(serchs));
}

/**
 * 
 * @param id  ex:table.id
 * @param elm_name ex:'input'
 * @param obj_id  要塞入值得元素的id
 */
function getSearch_conditionII(id ,  elm_names , obj_id , oth){
	if(window.console){console.log("id>>"+id);}
	var serchs = {};
	$("#"+obj_id).val("");
	var elm_Ary =  elm_names.split(",");
//	if(window.console){console.log("elm_Ary>>"+elm_Ary);}
	for(var elm_name in elm_Ary){
//		if(window.console){console.log("elm_name>>"+elm_name);}
		$.each($('#'+id).find(elm_Ary[elm_name]).serializeArray(), function(i, field) {
			serchs[field.name] = field.value;
		});
	}
	serchs['action'] = $("#formID").attr('action')+oth;
	//if(window.console){console.log("JSON.stringify(serchs)"+JSON.stringify(serchs));}
	$("#"+obj_id).val(JSON.stringify(serchs));
}

/**
 * 
 * @param id  ex:table.id
 * @param elm_name ex:'input'
 * @param obj_id  要塞入值得元素的id
 */
function getSearch_conditionIII(id ,  elm_names , obj_id){
	if(window.console){console.log("id>>"+id);}
	var serchs = {};
	$("#"+obj_id).val("");
	var elm_Ary =  elm_names.split(",");
//	if(window.console){console.log("elm_Ary>>"+elm_Ary);}
	for(var elm_name in elm_Ary){
//		if(window.console){console.log("elm_name>>"+elm_name);}
		$.each($('#'+id).find(elm_Ary[elm_name]).serializeArray(), function(i, field) {
//			serchs[field.name] = field.value;
			serchs[field.name] = encodeURI(field.value);
			
		});
	}
	serchs['action'] = $("#formID").attr('action');
	
	//if(window.console){console.log("JSON.stringify(serchs)"+JSON.stringify(serchs));}
	$("#"+obj_id).val(JSON.stringify(serchs));
}

function setTWDatepicker(){
	/*顯示民國年*/
	$.extend($.datepicker, {
	    formatDate: function (format, date, settings) {
	        var d = date.getDate();
			var m = date.getMonth()+1;
			var y = date.getFullYear();			
			var fm = function(v){			
			    return (v<10 ? '0' : '')+v;
			};			
			return (y-1911) +''+ fm(m) +''+ fm(d);
	    },
	    parseDate: function (format, value, settings) {
	        var v = new String(value);
	        var Y,M,D;
	        if(v.length==7){/*1001215*/
	            Y = v.substring(0,3)-0+1911;
	            M = v.substring(3,5)-0-1;
	            D = v.substring(5,7)-0;
	            return (new Date(Y,M,D));
	        }else if(v.length==6){/*981215*/
	            Y = v.substring(0,2)-0+1911;
	            M = v.substring(2,4)-0-1;
	            D = v.substring(4,6)-0;
	            return (new Date(Y,M,D));
	        }
	        return (new Date());
	    },
	    formatYear:function(v){
	    	return '民國'+(v-1911)+'年';
	    }
	});
}

//switch element to show or hide
function display(id){
	id = id.trim();
	if($("#" + id).is(":visible")){
		$("#" + id).hide();
	}else{
		$("#" + id).show();
	}
}

function getPath(){
	var path = location.href; 
	var page = path.substring(0, path.lastIndexOf("/") + 1);
//	var uri = "${pageContext.request.contextPath}";
	var uri = page;
	return uri;
}

//redirect after click menu item


function getPage(url, breadcrumb, funcId){
	blockUI();
	breadcrumb="";
	//url  = getPath() + url + "?b=" + encodeURI(encodeURIComponent(breadcrumb));
	if(url.indexOf("?") != -1){
		url = getPath() + url + "b=" + encodeURI(encodeURIComponent(breadcrumb) + "&fcid=" + funcId);
	}else{
		url = getPath() + url + "?b=" + encodeURI(encodeURIComponent(breadcrumb) + "&fcid=" + funcId);
	}
	window.open(url, '_self');
}

//call jquery.blockUI.js
function blockUI(){
	$.blockUI ({ message: 
		'<font style="color: white; font-weight: bold">處理中</font><br><img src="images/loading.gif"/>' 
	});
	/*
	$.blockUI ( {
		message: '<img src="images/loading.gif"/>',
		css: { 
        	border: 'none', 
        	padding: '10px',
        	width: '10%',
        	top: '40%',
        	left: '45%',
        	backgroundColor: '#000', 
        		'-webkit-border-radius': '10px', 
        		'-moz-border-radius': '10px',
        		'border-radius':'10px',
        	opacity: .5, 
        	color: '#fff'
    	} 
	});
	*/
}

function  unblockUI(){
	$.unblockUI();
}


function openWithPost(verb, url, data, target){
	var form = document.createElement("form");
	  form.action = url;
	  form.method = verb;
	  form.target = target || "_self";
	  if (data) {
	    for (var key in data) {
	      var input = document.createElement("textarea");
	      input.name = key;
	      input.value = typeof data[key] === "object" ? JSON.stringify(data[key]) : data[key];
	      form.appendChild(input);
	    }
	  }
	  form.style.display = 'none';
	  document.body.appendChild(form);
	  form.submit();
}


// refrence:  http://extremedev.blogspot.tw/2011/03/windowshowmodaldialog-cross-browser-new.html
var $dialog = null;

jQuery.showModalDialog = function(options) {

    var defaultOptns = {
        url: null,
        dialogArguments: null,
        height: 'auto',
        width: 'auto',
        position: 'center',
        resizable: true,
        scrollable: true,
        onClose: function() { },
        returnValue: null,
        doPostBackAfterCloseCallback: false,
        postBackElementId: null
    };

    var fns = {
        close: function() {
            opts.returnValue = $dialog.returnValue;
            $dialog = null;
            opts.onClose();
            if (opts.doPostBackAfterCloseCallback) {
                postBackForm(opts.postBackElementId);
            }
        },
        adjustWidth: function() { $frame.css("width", "100%"); }
    };

    // build main options before element iteration

    var opts = $.extend({}, defaultOptns, options);

    var $frame = $('<iframe id="iframeDialog" />');

    if (opts.scrollable)
        $frame.css('overflow', 'auto');

    $frame.css({
        'padding': 0,
        'margin': 0,
        'padding-bottom': 10
    });

    var $dialogWindow = $frame.dialog({
        autoOpen: true,
        modal: true,
        width: opts.width,
        height: opts.height,
        resizable: opts.resizable,
        position: opts.position,
        overlay: {
            opacity: 0.5,
            background: "black"
        },
        close: fns.close,
        resizeStop: fns.adjustWidth
    });

    $frame.attr('src', opts.url);
    fns.adjustWidth();

    $frame.load(function() {
        if ($dialogWindow) {

            var maxTitleLength = 50;
            var title = $(this).contents().find("title").html();

            if (title.length > maxTitleLength) {
                title = title.substring(0, maxTitleLength) + '...';
            }
            $dialogWindow.dialog('option', 'title', title);
        }
    });

    $dialog = new Object();
    $dialog.dialogArguments = opts.dialogArguments;
    $dialog.dialogWindow = $dialogWindow;
    $dialog.returnValue = null;
}

function postBackForm(targetElementId) {
    var theform;
    theform = document.forms[0];
    theform.__EVENTTARGET.value = targetElementId;
    theform.__EVENTARGUMENT.value = "";
    theform.submit();
}


var prntWindow = getParentWindowWithDialog(); //$(top)[0];

var $dlg = prntWindow && prntWindow.$dialog;

function getParentWindowWithDialog() {
    var p = window.parent;
    var previousParent = p;
    while (p != null) {
        if ($(p.document).find('#iframeDialog').length) return p;

        p = p.parent;

        if (previousParent == p) return null;

        // save previous parent

        previousParent = p;
    }
    return null;
}

function setWindowReturnValue(value) {
    if ($dlg) $dlg.returnValue = value;
    window.returnValue = value; // in case popup is called using showModalDialog

}

function getWindowReturnValue() {
    // in case popup is called using showModalDialog

    if (!$dlg && window.returnValue != null)
        return window.returnValue;

    return $dlg && $dlg.returnValue;
}

if ($dlg) window.dialogArguments = $dlg.dialogArguments;
if ($dlg) window.close = function() { if ($dlg) $dlg.dialogWindow.dialog('close'); };


function branch_search(str){
	var url = getPath()+"bank_branch.do?target=branch_search";
	$.showModalDialog({
		 url: url,
		 dialogArguments: str,
		 height: 200,
		 width: 900,
		 scrollable: false,
		 onClose: function(){ }
// 				 onClose: function(){ var returnedValue = this.returnValue; alert('This is the returned value from the popup:' + returnedValue); }
	});
}


function setDateOnChange(obj , functionname){
	if(fstop.isNotEmpty(obj)){
		$(obj).on("change", functionname );
	}
}
//套用日曆元件
//date format e.g. 01031128
function setDatePicker(){
	//clear all datefield value
	$(".datepicker").val("");
	$(".datepicker").datepicker({
		changeYear: true,
		changeMonth: true,
	    dateFormat: "",
	    showOn: "button",
	    buttonImage: "./images/calendar.png",
	    format: 'yymmdd',
	    onSelect: function(selected, evnt){
			var padding = "";
			for(i = 0 ; i < (8 - selected.length); i++){
				padding += "0";
			}
			$(this).val(padding + selected);
//			20150818 add by hugo 因應變更操作行機制 日期變更時要處發相關function 去取得總行清單
			$(this).change();
	    }
	});
}

function setDatePickerII(obj,val){
	//clear all datefield value
	$(obj).val(val);
	$(obj).datepicker({
		changeYear: true,
		changeMonth: true,
		dateFormat: "",
		showOn: "button",
		buttonImage: "./images/calendar.png",
		format: 'yymmdd',
		onSelect: function(selected, evnt){
			var padding = "";
			for(i = 0 ; i < (8 - selected.length); i++){
				padding += "0";
			}
			$(this).val(padding + selected);
//			20150818 add by hugo 因應變更操作行機制 日期變更時要處發相關function 去取得總行清單
			$(this).change();
		}
	});
}

function removeDatePicker(obj){
	$(obj).val("");
	$(obj).datepicker("destroy");
}

function cleanForm(obj){
	var formObj = $(obj).parents("form")[0];
	formObj.reset();
	$("input", formObj).val("");
	$("select", formObj).val("");
	if($(formObj).validationEngine != null){
		$(formObj).validationEngine('hide');
	}
}

//Clean form except <serchStrs>
function cleanFormII(obj){
	var serchStrs = "";
	if($("[name=serchStrs]")){
		serchStrs = $("[name=serchStrs]").val();
		cleanForm(obj);
		$("[name=serchStrs]").val(serchStrs);
	}else{
		cleanForm(obj);
	}
}

function cleanFormNE(obj){
	var serchStrs = "" , sortname = "" , sortorder = "" , edit_params = "" ,pageForSort = "";
	if($("[name=serchStrs]")){
		serchStrs = $("[name=serchStrs]").val();
	}
	if($("[name=sortname]")){
		sortname = $("[name=sortname]").val();
	}
	if($("[name=sortorder]")){
		sortorder = $("[name=sortorder]").val();
	}
	if($("[name=edit_params]")){
		edit_params = $("[name=edit_params]").val();
	}
	if($("[name=pageForSort]")){
		pageForSort = $("[name=pageForSort]").val();
	}
	cleanForm(obj);
	$("[name=serchStrs]").val(serchStrs);
	$("[name=sortname]").val(sortname);
	$("[name=sortorder]").val(sortorder);
	$("[name=edit_params]").val(edit_params);
	$("[name=pageForSort]").val(pageForSort);
}

function dateFormatter(input, inputFormat, outputFormat){
	var output = "";
	var years = [], months = [], days = [];
	if(input.length == inputFormat.length){
		for(var i = 0; i < input.length; i++){
			if(inputFormat.substring(i,i+1) == "y"){years.push(input.substring(i,i+1));}
			if(inputFormat.substring(i,i+1) == "m"){months.push(input.substring(i,i+1));}
			if(inputFormat.substring(i,i+1) == "d"){days.push(input.substring(i,i+1));}
		}
		for(var i = 0; i < outputFormat.length; i++){
			if(outputFormat.substring(i,i+1) == "y"){output += years[0];years.shift();}else
			if(outputFormat.substring(i,i+1) == "m"){output += months[0];months.shift();}else
			if(outputFormat.substring(i,i+1) == "d"){output += days[0];days.shift();}else
			{output += outputFormat.substring(i,i+1);}
		}
	}
	return output; 
}

function timeFormatter(input, inputFormat, outputFormat){
	var output = "";
	var hrs = [], minutes = [], secs = [];
	if(input.length == inputFormat.length){
		for(var i = 0; i < input.length; i++){
			if(inputFormat.substring(i,i+1) == "h"){hrs.push(input.substring(i,i+1));}
			if(inputFormat.substring(i,i+1) == "m"){minutes.push(input.substring(i,i+1));}
			if(inputFormat.substring(i,i+1) == "s"){secs.push(input.substring(i,i+1));}
		}
		for(var i = 0; i < outputFormat.length; i++){
			if(outputFormat.substring(i,i+1) == "h"){output += hrs[0];hrs.shift();}else
			if(outputFormat.substring(i,i+1) == "m"){output += minutes[0];minutes.shift();}else
			if(outputFormat.substring(i,i+1) == "s"){output += secs[0];secs.shift();}else
			{output += outputFormat.substring(i,i+1);}
		}
	}
	return output; 
}

/** For jqGrid colModule formatter **/
function currencyFmatter(cellvalue, options, rowObject){
	return fstop.formatAmt(String(cellvalue));
}

//改用PageSession.js處理
/*
function startLogoutCountdown(limit){
	var ele = window.parent.document.getElementById('maxInactiveInterval');
	if($(ele).length){
		$(ele).html(limit);
		clearInterval(logoutCountdownObj);
		logoutCountdownObj = setRepeater((function(){
			var nextSec = parseFloat($(ele).html()) - 1;
			if(nextSec == 0){
				window.parent.location.href = "login.do?ac_key=logout&target=logout";
			}else{
				$(ele).html(nextSec);
			}
		}), [], 1);
	}else{
		alert("倒數計時元件初始化失敗，即將強制登出!");
		window.parent.location.href = "login.do?ac_key=logout&target=logout";
	}
}
*/

function setRepeater(func, values, interval){
	return setInterval(function(){
		func.apply(this, values);
	}, interval * 1000);
}
function setRepeaterII(func, values, interval){
	 
	var tmp = values[0];
	var tmpvalues = values[0];
	values[1] = tmp ;
	return setInterval(function(){
		if(tmpvalues < 0){
			tmpvalues = tmp;
		}
		values[0] = tmpvalues;
		tmpvalues--;
		func.apply(this, values);
	}, interval * 1000);
}

/*
 * 取得今日民國年月日
 */
function getTwSysDate(sep){
	var d = new Date();
	var month = d.getMonth()+1;
	var day = d.getDate();
	return '0'+(d.getFullYear()-1911) + sep +
	    (month<10?'0':'')+month+sep+
	    (day<10?'0':'')+day;
}

function logout(str){
	window.open("login.do?ac_key=logout&target=logout", "_self");
}

function stopRKey(evt) { 
	var evt = (evt) ? evt : ((event) ? event : null); 
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	if ((evt.keyCode == 13) && (node.type=="text"))  {return false;} 
} 
//依據權限來判斷是否disabled
function disabledForm(str){
	var auth = str;
	if(auth != "A"){
//			$( "#formID" ).prop( "disabled", true );//這個會全鎖變灰，不好看
//			$("input").prop('disabled', true);
		$('#formID input').attr('readonly', true);
		$('#formID input').addClass('lock');
		$('#formID button').attr('disabled', true);
		$("input[type=radio]").attr('disabled', true);
		$("input[type=button]").attr('disabled', true);
//			$("#formID radio").prop('disabled', true);
		$("select").prop('disabled', true);

	}
}

//20150720 add by hugo req by UAT-20150120-17
//因原本jqGrid nodata的顯示未能府合需求 故客製化
function noDataEvent(data , obj){
	var tmp = false;
//	if(window.console){console.log("noData>>"+$("#noData"));}
//	if(window.console){console.log("noData2>>"+$("#noData").is(":visible"));}
//	避免按下排序時重複插入查無資料...的div
	if(!fstop.isEmpty($("#noData")) && !$("#noData").is(":visible")){
		tmp = true;
	}
	var emptyMsgDiv = $('<div id="noData" style="color:red;font-size:18px;height:60px;padding:10px;">查無資料....</div>');//e
//	if($("#serchStrs").val().length <= 2){
//		emptyMsgDiv = $('<div id="noData" style="color:red;font-size:18px;height:60px;padding:10px;">請選擇查詢條件查詢....</div>');//e
//	}
	var grid = fstop.isEmpty(obj)? $('#resultData') : obj;
	var count = 0;
    	if(window.console){console.log("serchStrs>>"+$("#serchStrs").val());}
//    	20151028 因serchStrs 預設值為{} 故改為!=2
	if($("#serchStrs").val().length != 2){
//		if($("#serchStrs").val().length !=0){
    	if(window.console){console.log("dataf>>"+data);}
//    	if(window.console){console.log("dataE>>"+data.length);}
//    	判斷是否有資料回傳
//		以Map型態回傳的JSON字串
		if(!fstop.isEmpty(data) && !fstop.isEmptyString(data)  && !fstop.isEmpty(data.rows) ){
			count = data.rows.length ;
//			if(window.console){console.log("count1>>"+count);}
//			以List型態回傳的JSON字串
		}else if(fstop.isEmpty(data.rows) && !fstop.isEmpty(data.length)){
			count = data.length ;
//			if(window.console){console.log("count2>>"+count);}
		}
		
		
    	if( fstop.isNotEmpty(data.result)  && data.result == "FALSE"){
    		emptyMsgDiv.html("查詢失敗，系統異常...");
    	}
    	if (count === '0'||count === 0) {
            grid.hide();
            emptyMsgDiv.show();
        } else {
            grid.show();
            emptyMsgDiv.hide();
        }  
    	if(window.console){console.log("tmp>>"+tmp);}
    	if(tmp){
    		emptyMsgDiv.insertBefore(grid);
    	}
	}
}
function noDataEvent_Bat(data , obj){
	var tmp = false;
//	if(window.console){console.log("noData>>"+$("#noData"));}
//	if(window.console){console.log("noData2>>"+$("#noData").is(":visible"));}
//	避免按下排序時重複插入查無資料...的div
	if(!fstop.isEmpty($("#noData")) && !$("#noData").is(":visible")){
		tmp = true;
	}
	var emptyMsgDiv = $('<div id="noData" style="color:red;font-size:18px;height:60px;padding:10px;">查無資料....</div>');//e
	var grid = fstop.isEmpty(obj)? $('#resultData') : obj;
	var count = 0;
//	if($("#serchStrs").val().length !=0){
		if(window.console){console.log("dataf>>"+data);}
//    	if(window.console){console.log("dataE>>"+data.length);}
//    	判斷是否有資料回傳
//		以Map型態回傳的JSON字串
		if(!fstop.isEmpty(data) && !fstop.isEmptyString(data)  && !fstop.isEmpty(data.rows) ){
			count = data.rows.length ;
//			以List型態回傳的JSON字串
		}else if(fstop.isEmpty(data.rows) && !fstop.isEmpty(data.length)){
			count = data.length ;
		}
		
		
		if( fstop.isNotEmpty(data.result)  && data.result == "FALSE"){
			emptyMsgDiv.html("查詢失敗，系統異常...");
		}
		if (count === 0) {
			grid.hide();
			emptyMsgDiv.show();
		} else {
			grid.show();
			emptyMsgDiv.hide();
		}  
		if(window.console){console.log("tmp>>"+tmp);}
		if(tmp){
			emptyMsgDiv.insertBefore(grid);
		}
//	}
}


function resetSortname(newOption , de_sortname , de_sortOrder , isInit){
	var sortname =  fstop.isNotEmpty($("#sortname").val()) ? $("#sortname").val() :'';
	var sortOrder =  fstop.isNotEmpty($("#sortorder").val()) ? $("#sortorder").val() :'';
	if(window.console){console.log("isInit>>"+isInit);}
	if(isInit){
		sortname = "";
		sortOrder = "";
	}
	if(window.console){console.log("de_sortname>>"+de_sortname);}
	if(window.console){console.log("de_sortOrder>>"+de_sortOrder);}
	if(window.console){console.log("sortname>>"+$("#sortname").val());}
	if(window.console){console.log("sortorder>>"+$("#sortorder").val());}
	if(fstop.isNotEmpty(sortname) && fstop.isNotEmptyString(sortname)){
		newOption.sortname = sortname;
		$("#sortname").val(sortname);
	}else{
		if(window.console){console.log("de_sortname2>>"+de_sortname);}
		newOption.sortname = de_sortname;
		newOption.sidx = de_sortname;
		$("#sortname").val(de_sortname);
	}
	if(fstop.isNotEmpty(sortOrder) && fstop.isNotEmptyString(sortOrder)){
		if(window.console){console.log("sortOrder>>"+$("#sortorder").val());}
		newOption.sortorder = sortOrder;
		newOption.sord = sortOrder;
		$("#sortorder").val(sortOrder);
	}else{
		if(window.console){console.log("sortOrder2>>"+$("#sortorder").val());}
		newOption.sortorder = de_sortOrder;
//		newOption.sidx = de_sortOrder;
		newOption.sord = de_sortOrder;
		$("#sortorder").val(de_sortOrder);
	}
		
	
}

//把物件中底下，除了陣列的屬性全都清空
//  obj:$('#formID')
//	var array = ["serchStrs" , "sortname" , "sortorder"];
function cleanFormExceptArray( obj, array){
	$.each($(obj).serializeArray(), function(i, field) {
//			if(window.console){console.log("field.name.a>>"+field.name);}
//			if(window.console){console.log("field.name.b>>"+$.inArray(field.name, array));}
		if($.inArray(field.name, array) < 0 ){
			$("#"+field.name).val("");
		}
	});
} 

function creatForm(form  , action , method , elements){
	if(form == null || form ==='undefined'){
		form = $("<form></form>");
	}
	var formdata = {
		    "action" : action,
		    "method" : method,
		    "elements" :elements
		};
		 
	return $(form).buildForm(formdata);
}
//TODO 未完成
function openWindowWithPost(formId , key){
	var f = document.getElementById(formId);
	  f.ac_key.value = key;
	  window.open('', 'TheWindow');
	  f.submit();
}

//適用日期區間(適用屬性START_DATE,END_DATE , OPBK_ID)
function getBgbk_List_GE(opbkId){
	var s_bizdate = $("#START_DATE").val();
	var e_bizdate = $("#END_DATE").val();
	opbkId = $("#OPBK_ID").val();
	if(opbkId == '' || opbkId == "all"){
		$("#BGBK_ID option:not(:first-child)").remove();
	}else{
		var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate ,e_bizdate:e_bizdate};
		fstop.getServerDataExII(uri, rdata, false, creatBgBkList_GE);
	}
}

//適用單一日期(適用屬性START_DATE , OPBK_ID)
function getBgbk_List_S(opbkId){
	var s_bizdate = $("#START_DATE").val();
	opbkId = $("#OPBK_ID").val();
	if(opbkId == '' || opbkId == "all"){
		$("#BGBK_ID option:not(:first-child)").remove();
	}else{
		var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate };
		fstop.getServerDataExII(uri, rdata, false, creatBgBkList_GE);
	}
}
//適用單一日期(適用屬性BIZDATE , OPBK_ID)
function getBgbk_List_S_II(opbkId){
	var s_bizdate = $("#START_DATE").val();
	opbkId = $("#OPBK_ID").val();
	if(opbkId == '' || opbkId == "all"){
		$("#BGBK_ID option:not(:first-child)").remove();
	}else{
		var rdata = {component:"bank_group_bo", method:"getByOpbkId", OPBK_ID:opbkId , s_bizdate:s_bizdate };
		fstop.getServerDataExII(uri, rdata, false, creatBgBkList_GE);
	}
}
//適用月區間(適用屬性START_YEAR,START_MONTH END_YEAR,END_MONTH, OPBK_ID)
function getBgbk_List_GE_M(opbkId){
	var s_mon = $("#START_YEAR").val()+$("#START_MONTH").val();
	var e_mon = $("#END_YEAR").val()+$("#END_MONTH").val();
	opbkId = $("#OPBK_ID").val();
	if(opbkId == '' || opbkId == "all"){
		$("#BGBK_ID option:not(:first-child)").remove();
	}else{
		var rdata = {component:"bank_group_bo", method:"getByOpbkId_MON", OPBK_ID:opbkId , s_mon:s_mon ,e_mon:e_mon};
		fstop.getServerDataExII(uri, rdata, false, creatBgBkList_GE);
	}
}
//適用單一月(適用屬性TW_YEAR,TW_MONTH , OPBK_ID)
function getBgbk_List_GE_MS(opbkId){
	var s_mon = $("#TW_YEAR").val()+$("#TW_MONTH").val();
	var e_mon = s_mon;
	opbkId = $("#OPBK_ID").val();
	if(opbkId == '' || opbkId == "all"){
		$("#BGBK_ID option:not(:first-child)").remove();
	}else{
		var rdata = {component:"bank_group_bo", method:"getByOpbkId_MON", OPBK_ID:opbkId , s_mon:s_mon ,e_mon:e_mon};
		fstop.getServerDataExII(uri, rdata, false, creatBgBkList_GE);
	}
}


function creatBgBkList_GE(obj){
	var select = $("#BGBK_ID");
	$("#BGBK_ID option:not(:first-child)").remove();
	if(obj.result=="TRUE"){
		var dataAry =  obj.msg;
		for(var i = 0; i < dataAry.length; i++){
			select.append($("<option></option>").attr("value", dataAry[i].BGBK_ID).text(dataAry[i].BGBK_ID + " - " + dataAry[i].BGBK_NAME));
		}
	}else if(obj.result=="FALSE"){
		alert(obj.msg);
	}
}

function get_curPage(obj , pagenum , str , pgBtn){
	var tmppage = 1;
	var num = 1;
	if(window.console){console.log("pgBtn>>"+pgBtn);}
	var pageForSort = $("#pageForSort");
	var postdata = $(obj).jqGrid('getGridParam','postData');
	if(window.console){console.log("postdata>>"+postdata.page);}
	if(window.console){console.log("pagenum>>"+pagenum);}
	tmppage = fstop.isNotEmpty(pagenum) ? parseInt(pagenum):parseInt(postdata.page);
	tmppage = fstop.isNotEmpty(str) && str == 'P'? tmppage:tmppage;
	if(fstop.isNotEmpty(pgBtn) && fstop.isNotEmptyString(pgBtn) ){
//	如果按下的是grid的上一頁
		tmppage = pgBtn.indexOf('prev')  >=0  ? tmppage-1  :tmppage;
//		回到第一頁
		tmppage = pgBtn.indexOf('first') >= 0 ? num :tmppage;
//		最後一頁
		tmppage = pgBtn.indexOf('last')  >= 0  || pgBtn.indexOf('next')  >= 0  ? tmppage+1   :tmppage;
		
	}
	$(obj).setGridParam({page:tmppage});
	if(window.console){console.log("tmppage>>"+tmppage);}
	if(fstop.isNotEmpty(pageForSort)){
		pageForSort.val(tmppage);
	}
	return tmppage ;
}

/**
 * 一般文字通用，用來做符號區隔或斷行用
 * sign_obs_ary{key1:"<BR>",key2:"-"}
 * obj_ary:{key:"val",key2:"val2"}
 */
function each_formatter_txt(cellvalue, options, rowObject,sign_obj_ary,obj_ary){
	var rtnStr = "";
	rtnStr = cellvalue;
	if(fstop.isEmpty(obj_ary) || fstop.isEmpty(sign_obj_ary) ){
		return rtnStr;
	}
	
	for(var i in obj_ary){
		if(options.colModel.name == i){
			rtnStr = rowObject[i]+sign_obj_ary[i]+rowObject[obj_ary[i]];
			return rtnStr;
		};
	}
	return rtnStr;
}


//TODO 測試用 執行報表批次
function go(){
	var uri = "/eACH/baseInfo?";
	var qs = "component=login_bo&method=executeBatch";
	var msg = fstop.getServerDataExII(uri+qs, null, false);
	if(msg != null){
		alert(msg.msg);
	}
}