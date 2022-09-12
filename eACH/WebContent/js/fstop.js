/**
 * 共用 js 功能
 * Here are the pros and cons of this approach, paraphrased:
 * Pros:
 *   1. Public and private properties and methods
 *   2. doesn’t use cumbersome OLN
 *   3. Protects undefined 
 *   4. Ensures that $ refers to jQuery
 *   5. Namespace can span files
 * Cons:
 *   Harder to understand than OLN  
 */
(function(fstop, $, undefined ) {
 
	//Private Property
	var isHot = true;

	//Public Property
	fstop.baseUrl = "pl";

	//設定不要 cache http data
	$.ajaxSetup({cache: false});
	
    //Public Method
	//uri: 取得資料的 url, rdata:額外的資料, callback: 回呼函數(msg)
	fstop.getServerDataEx = function (uri, rdata, isAsync, callback){
        $.ajax({
            type: "POST",
            async: isAsync,
            url: uri,
            data: rdata,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(msg){
            	if (callback){
            		callback(msg);	
            	}
            },
            error: function(jqXHR, textStatus, errorThrown) {
    		    alert("系統異常，請稍候再試，或洽相關資訊人員，error_code="+jqXHR.status);
    		}
//            error: function(){
//                alert("Failed to load data from " + uri);
//            }
        });        	
    };
//     2014/05/21 add by hugo 原本的API只能用queryString server端才能接收到相關資訊 故新增此api
    fstop.getServerDataExII = function (uri, rdata, isAsync, callback, contentType){
    	var response = "";
    	if(fstop.isNotEmpty(contentType) ){
    		$.ajax({
        		type: "POST",
        		async: isAsync,
        		url: uri,
        		data: rdata,
                contentType: contentType,
        		dataType: "json",
        		success: function(msg){
        			if (callback){
        				callback(msg);	
        			}else{
        				response =  msg;
        			}
        		},
        		error: function(jqXHR, textStatus, errorThrown) {
        		    alert("系統異常，請稍候再試，或洽相關資訊人員，error_code="+jqXHR.status);
        		}
//        		error: function(){
//        			alert("Failed to load data from " + uri);
//        		}
        	}); 
    	}else{
    		$.ajax({
        		type: "POST",
        		async: isAsync,
        		url: uri,
        		data: rdata,
        		dataType: "json",
        		success: function(msg){
        			if (callback){
        				callback(msg);	
        			}else{
        				response =  msg;
        			}
        		},
        		error: function(jqXHR, textStatus, errorThrown) {
//        		    alert(jqXHR.status);
//        		    alert(jqXHR.statusCode);
//        			status = 12029    表示系統停止服務或url錯誤
        		    alert("系統異常，請稍候再試，或洽相關資訊人員，error_code="+jqXHR.status);
        		}
//        		error: function(){
//        			alert("Failed to load data from " + uri);
//        		}
        	});
    	}
    	return response;       	
    };
//     2015/05/20 add by hugo 因應ajax 有時會跳出12029 或200 的治標措施
    fstop.getServerDataExIII = function (uri, rdata, isAsync, callback, contentType , funcName , reloadUrl){
    	var response = "";
    	if(fstop.isNotEmpty(contentType) ){
    		$.ajax({
    			type: "POST",
    			async: isAsync,
    			url: uri,
    			data: rdata,
    			contentType: contentType,
    			dataType: "json",
    			success: function(msg){
    				if (callback){
    					callback(msg);	
    				}else{
    					response =  msg;
    				}
    			},
    			error: function(jqXHR, textStatus, errorThrown) {
//    				alert("系統異常，請稍候再試，或洽相關資訊人員，error_code="+jqXHR.status);
//    				location.reload();
    				if(window.console){console.log("系統異常，請稍候再試，或洽相關資訊人員，error_code="+jqXHR.status);}
    				window.open( reloadUrl ,"_parent");
    			}
//        		error: function(){
//        			alert("Failed to load data from " + uri);
//        		}
    		}); 
    	}else{
    		$.ajax({
    			type: "POST",
    			async: isAsync,
    			url: uri,
    			data: rdata,
    			dataType: "json",
    			success: function(msg){
    				if (callback){
    					callback(msg);	
    				}else{
    					response =  msg;
    				}
    			},
    			error: function(jqXHR, textStatus, errorThrown) {
//        		    alert(jqXHR.status);
//        		    alert(jqXHR.statusCode);
//        			status = 12029    表示系統停止服務或url錯誤
//    				alert("系統異常，請稍候再試，或洽相關資訊人員，funcName = "+funcName+"，error_code="+jqXHR.status );
//    				location.reload();
    				if(window.console){console.log("系統異常，請稍候再試，或洽相關資訊人員，error_code="+jqXHR.status);}
    				window.open( reloadUrl ,"_parent");
    			}
//        		error: function(){
//        			alert("Failed to load data from " + uri);
//        		}
    		});
    	}
    	return response;       	
    };
    
    
    
    
//	2014/05/22 add by hugo  取queryString的參數 後來才發現 已經有類似功能getQueryString  但還是留著
    fstop.getParameterByName = function(name){
    	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    };
    
	//傳回去除前後空白的值
	String.prototype.trim=fstop.trim;  
	fstop.trim = function(){
			return this.replace(/^\s+|\s+$/g, "");
	};
	
	//判斷傳入的物件是否為空值或是未定義
	fstop.isEmpty = function(obj){
		if(typeof(obj) !== 'undefined' && obj != null ){
		    return false;
		}else{
			return true;
		}
	};
	
	fstop.isNotEmpty = function(obj){
		return !fstop.isEmpty(obj);
	};
	
	//判斷是否為數字
	fstop.isNumber = function(n){
    	n = n.replace(/,/ig ,"");
        return !isNaN(parseFloat(n)) && isFinite(n);
    };
    
	//判斷是否為空字串
    fstop.isEmptyString = function(n){
    	if (typeof(n) === 'undefined' || n == null || n.length == 0){
    		return true;
    	}else{
    		return false;
    	}
    };
    
    fstop.isNotEmptyString = function(n){
    	return !fstop.isEmptyString(n);
    };
    
    fstop.unFormatDate = function(dt){
    	dt = dt.replace(/\//g,"");
    	return dt;
    };
    
    fstop.unFormatAmt = function(money){
    	money = money.replace(/\,/g,"");
    	return money;
    };
    
    //格式化金額欄位
    fstop.formatAmt = function(money){
    	//判斷字串中是否有不在 數字、小數點、正負號、千分號 之內的值，有雜值即返回
        if(/[^0-9\.\-\+,]/.test(money)) return money;
    	//先去除千分號，以正規化
    	//money = money.replace(",","");  //這樣只會取代第一個被找到的逗號
    	//money = money.replace(new RegExp(",", "g"), "");  //取代全部逗號 OK
    	money = money.replace(/\,/g,"");  //取代全部逗號 OK
    	var dot = money.indexOf(".");
    	var scale = 0;
    	if(dot != -1){
    		scale = money.length - dot - 1; 
    	}
    	if(money.length <=3 && dot == -1 ){
    		return money;		
    	}
		if(dot == -1){
			dot = money.length;	
		}
    	var ipar = money.substr(0, dot) + ".";
    	var spar = money.substr(dot);
        ipar = ipar.replace(/^(\d*)$/,"$1.");
        ipar = ipar.replace(/(\d*\.\d*)\d*/,"$1");
        ipar = ipar.replace(".",",");
        var re = /(\d)(\d{3},)/;
        while(re.test(ipar)){
          ipar = ipar.replace(re,"$1,$2");
		}
        ipar = ipar.replace(/,(\d\d)$/,".$1");
        ipar = ipar.replace(/^\./,"0.");
        var i = ipar.lastIndexOf(",");
        ipar = ipar.substr(0, i);
        return ipar + spar;
    };

  	//由 URL中取得特定參數值
    fstop.getQueryString = function(name){
        // 如果 url 中沒有參數，或是沒有我們要的參數，則返回
        if(location.href.indexOf("?")==-1 || location.href.indexOf(name+'=')==-1){
            return '';
        }
        // 取得參數
        var queryString = location.href.substring(location.href.indexOf("?")+1);
        // 分離參數對 ?key=value&key2=value2
        var parameters = queryString.split("&");
        var pos, paraName, paraValue;
        for(var i=0; i<parameters.length; i++){
            // 取得等號位置
            pos = parameters[i].indexOf('=');
            if(pos == -1) { continue; }
     
            // 取得name 和 value
            paraName = parameters[i].substring(0, pos);
            paraValue = parameters[i].substring(pos + 1);
     
            // 如果查詢的name 等於目前的 name，就返回目前的值，同時，將 url 中的 + 號還原成空格
            if(paraName == name){
                return unescape(paraValue.replace(/\+/g, " "));
            }
        }
        return '';
    };

    
    //Private Method
    function addItem( item ) {
        if ( item !== undefined ) {
            console.log( "Adding " + $.trim(item) );
        }
    }
    
}( window.fstop = window.fstop || {}, jQuery ));

//window.fstop = window.fstop || {}
//這代表說如果在建立物件之前先檢查有沒有使用過，如果有的話就直接延用之前寫的物件，如果沒有就給空的物件，避免物件重複宣告



	
	  	
	    
