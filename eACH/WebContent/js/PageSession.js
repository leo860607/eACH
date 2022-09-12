//20150413 edit by hugo 
//1.主要修改為小於5分鐘且移動滑鼠才送req 避免server負擔太大 
//2.重新送出req後，頁面的倒數才會重新計算
//3.修正滑鼠及鍵盤移動時的判斷時機點及相關參數調整


var gPageSessionDate = new Date();
var gPageIdleLimit = 5;  //未動作 5 秒倒數計時
var gPageSessionTimeout; //多久timeout
var gPageIdleTime = 0;
var gPageTimeNow;
var gLastPageActionTime = gPageSessionDate.getTime();
var gAwakeAfterNextMove;
//setTimeout(mdraw, mrefreshinterval); 
//setInterval(code,millisec[,"lang"])
//clearInterval()

var gPageActionCheckHandle;
var gPageTimeoutCountDownId;

function startPageActionCheck(id, limit)
{
	gPageSessionTimeout = limit;
	//偵測滑鼠動作
	$('html').mousemove(function(e) {
		var mousex = e.pageX;
		var mousey = e.pageY;
		userPerformedPageAction();
		//console.log( "Handler for mousemove called." );
	});

	$('html').keypress(function() {
		userPerformedPageAction();
		//console.log( "Handler for .keypress() called." );
	});

	gPageTimeoutCountDownId = id;
	gAwakeAfterNextMove= false;
	
	//每秒鐘檢查一次
	gPageActionCheckHandle = setInterval(pageActionCheck, 1000);
}

function pageActionCheck()
{
	var n =0;
	gPageSessionDate = new Date();
	gPageTimeNow = gPageSessionDate.getTime();
	gPageIdleTime++;
	n = gPageSessionTimeout - gPageIdleTime;
//	if(window.console){console.log("n>>"+n+",gAwakeAfterNextMove>>"+gAwakeAfterNextMove);}
	if(n < 300 && n > 0){
		if(gAwakeAfterNextMove){
			//send request to reset session timeout countdown
			//20150413 edit by hugo	小於5分鐘且移動滑鼠才送req 避免server負擔太大
			$.post("/eACH/baseInfo?component=login_bo&method=awakeSession", function(data) {});
			gPageIdleTime = 0;
			gAwakeAfterNextMove = false;
		}
	}
	if (n <= 0)
	{
		clearInterval(gPageActionCheckHandle);
		//page timeout action
		$("#"+gPageTimeoutCountDownId).text("0   秒");
		if(window.console){console.log("page idle timeout!");}
//		console.log("page idle timeout!");
		alert("您已登出成功!");
		window.location.href = "login.do?ac_key=logout&target=logout";
		return;
	}
	$("#pageCountdownLabel").show();
	$("#"+gPageTimeoutCountDownId).text( (n<60)?n+" 秒":Math.floor(n / 60)+" 分" );
	gAwakeAfterNextMove = false;
}

function userPerformedPageAction()
{
	gPageSessionDate = new Date();
//	取得移動滑鼠鍵盤後的最後時間
	gLastPageActionTime = gPageSessionDate.getTime();
	gAwakeAfterNextMove = true;
//	gPageIdleTime = 0;
}


/*
var maxHeight = 400;

$(function(){

    $(".dropdown > li").hover(function() {
    
         var $container = $(this),
             $list = $container.find("ul"),
             $anchor = $container.find("a"),
             height = $list.height() * 1.1,       // make sure there is enough room at the bottom
             multiplier = height / maxHeight;     // needs to move faster if list is taller
        
        // need to save height here so it can revert on mouseout            
        $container.data("origHeight", $container.height());
        
        // so it can retain it's rollover color all the while the dropdown is open
        $anchor.addClass("hover");
        
        // make sure dropdown appears directly below parent list item    
        $list
            .show()
            .css({
                paddingTop: $container.data("origHeight")
            });
        
        // don't do any animation if list shorter than max
        if (multiplier > 1) {
            $container
                .css({
                	position:"relative",
                	zIndex: 1 ,
                    height: "200px",
                    overflow: "hidden"
                })
                .mousemove(function(e) {
                    var offset = $container.offset();
                    var relativeY = ((e.pageY - offset.top) * multiplier) - ($container.data("origHeight") * multiplier);
                    if (relativeY > $container.data("origHeight")) {
                        $list.css("top", -relativeY + $container.data("origHeight"));
                    };
                });
        }
        
    }, function() {
    
        var $el = $(this);
        
        // put things back to normal
        $el
            .height($(this).data("origHeight"))
            .find("ul")
            .css({ top: 0 })
            .hide()
            .end()
            .find("a")
            .removeClass("hover");
    
    });
    
    // Add down arrow only to menu items with submenus
    $(".dropdown > li:has('ul')").each(function() {
        $(this).find("a:first").append("<img src='images/down-arrow.png' />");
    });
    
    
});

*/


