(function(){
var 
	form = $('.form'),
	cache_width = form.width(),
	a4  =[ 595.28,  841.89];  // for a4 size paper width and height

$('#create_pdf').on('click',function(){
	$('body').scrollTop(0);
	createPDF();
});
//create pdf
function createPDF(){
	alert("123");
//	var canvas =  getCanvas();
//		alert("canvas"+canvas);
//		var img = canvas.toDataURL("image/png"),
//		doc = new jsPDF({
//	      unit:'px', 
//	      format:'a4'
//	    });     
//	    doc.addImage(img, 'JPEG', 20, 20);
//		alert("doc>>"+doc);
//	    doc.save('techumber-html-to-pdf.pdf');
//	    form.width(cache_width);
//	    alert("end");
	getCanvas().then(function(canvas){
		alert("456");
		var img = canvas.toDataURL("image/png"),
		doc = new jsPDF({
          unit:'px', 
          format:'a4'
        });     
        doc.addImage(img, 'JPEG', 20, 20);
		alert("doc>>"+doc);
        doc.save('techumber-html-to-pdf.pdf');
        form.width(cache_width);
        alert("end");
	});
}

// create canvas object
function getCanvas(){
	alert("getCanvas");
	form.width((a4[0]*1.33333) -80).css('max-width','none');
	alert("form>>"+form);
//	var c = html2canvas(form,{
//    	imageTimeout:2000,
//    	removeContainer:true
//    });	
//	alert("c"+c);
//	return c;
	return html2canvas(form,{
    	imageTimeout:2000,
    	removeContainer:true
    });	
	
}

}());