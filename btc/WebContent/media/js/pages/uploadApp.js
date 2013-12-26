$(function() {
	$.getScript('/js/jquery.form.js', function() {
		$("#file").removeAttr("disabled").change(function() {
	    	var filePath = $(this).val();
	    	if (filePath.substring(filePath.length - 4) != '.apk') {
	    		alert('无效的APK文件，重新选择');
	    		$(this).val('');
	    		return false;
	    	} else {
	    		var shortFileName = filePath.substring(filePath.lastIndexOf('\\')+1);
	    		if (shortFileName.length > 20) {
	    			shortFileName = shortFileName.substring(0, 20) + '...';
	    		}
	    		$("#uploadFileName").html('正在上传' + shortFileName );
	    		$("#uploadFiles").fadeIn();
	    		var form = $("#appForm");
        		var footer = $('.uploading-footer').hide();
        		var abortUpload = $("#abortUpload").show();
            	var playUpload = $("#playUpload").hide();
            	var uploadBar = $('#uploadBar');
	    		form.attr("action", "/apps/create_upload");
	    		form.ajaxSubmit({
	    			uploadProgress: function(event, position, total, percentComplete) { 
	                    var percentVal = percentComplete + '%';
	                    uploadBar.css('width', percentVal);
	                }, 
	                success: function(data) {
	                	if (data && data.key) {
	                		$("#appKey").val(data.key);
	                		footer.html('上传成功').css("color", "green").fadeIn();
	                		uploadBar.css('width', '100%');
		                	abortUpload.hide();
		                	playUpload.hide();
	                	} else {
	                		footer.html('上传失败').css("color", "red").fadeIn();
	                		uploadBar.css('width', 0);
		                	abortUpload.hide();
		                	playUpload.show();
	                	}
	                },
	                error:function(xhr){ //上传失败 
	                	footer.html('上传失败').css("color", "red").fadeIn();
	                	uploadBar.css('width', 0);
	                	abortUpload.hide();
	                	playUpload.show();
	                } 
	            });
	    		var xhr = form.data('jqxhr');
	    		abortUpload.one('click', function() {
	    			xhr.abort();
	    		});
	    		playUpload.one('click', function() {
	    			xhr.abort();
    				$("#file").trigger("change");
    			});
	    	}
	    });
	});
	
	var form = $("#appForm");
	form.on('submit', function() {
		var appKey = $("#appKey");
		if (appKey.val().length != 32) {
			alert('请先上传应用');
			return false;
		}
		form.attr("action", "/apps/create");
		$("#submitForm").attr("disabled", "disabled");
		$("#submitInfo").fadeIn();
		return true;
	});
});