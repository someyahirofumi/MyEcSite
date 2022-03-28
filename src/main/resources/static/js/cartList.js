'use strict'

$(function(){
	var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
    
    
    function remove(number) {
		var removed = number.replace(/[^0-9a-z]/gi, '');
		return parseInt(removed);
	}
    
	$('#delete').on('submit',function(e){
		e.preventDefault();
		
			console.log('処理開始');
			console.log($('#orderId').val());
			
			$.ajax({
			url: $(this).attr("action"),
			type: 'post',
			dataType: 'text',
			data:{
				orderId:$('#orderId').val(),
				subTotal:remove($('#subTotal').text()),
				total:remove($('#total').val()),
			},
			async:true,
			}).done(function(){
				console.log("処理成功");
				$('.popup').addClass('show').fadeIn();
				
			}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			alert("正しい結果をえられませんでした");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
	});

	
});

})