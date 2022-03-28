'use strict'

$(function(){
	  let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

$('#inputConfirmationPassword').on('keyup',function(){
	console.log("処理");
	console.log($('#inputConfirmationPassword').val());
	
	$.ajax({
		url:'http://localhost:8080/checkPassword/check',
		type:'post',
		dataType:'json',
		data:{
			password:$('#inputPassword').val(),
			confirmPass:$('#inputConfirmationPassword').val()
		},
		async:true,
		
	}).done(function(data){
		console.log(data);
		$('#Confirmation').text(data.checkMessage);
		
	}).fail(function(XMLHttpRequest,textStatus,errorThrown){
		console.log('XMLHTTPRequest:'+XMLHttpRequest.status);
		console.log('textStatus:'+textStatus);
		console.log('errorThrown:'+errorThrown.message);	
		
	});
	
	
	
});

	$('#zipCheck').on('click',function(){
		$.ajax({
			url:'http://zipcoda.net/api',
			dataType:'jsonp',
			data:{
				zipcode:$('#inputZipcode').val()
			},
			async:true
		}).done(function(data){
			$('#inputAddress').val(data.items[0].address);
		}).fail(function(XMLHttpRequest,textStatus,errorThrown){
		console.log('XMLHTTPRequest:'+XMLHttpRequest.status);
		console.log('textStatus:'+textStatus);
		console.log('errorThrown:'+errorThrown.message);	
		
	});
		
	})
	
	
});