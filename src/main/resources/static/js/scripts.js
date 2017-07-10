$(".answer-write input[type=submit]").click(addAnswer);

function addAnswer(e){
	e.preventDefault();		//서버에 가지 못하게 막음
	
	var queryString = $(".answer-write").serialize();
	console.log(queryString);
	var url = $(".answer-write").attr("action");
	console.log('url : ' + url);
	$.ajax({
		type : 'post',
		url : url,
		data : queryString,
		dataType : 'json',
		error : onError,
		success : onSuccess
	});	
}

function onError(){
	
}

function onSuccess(data, status){
	console.log(data);
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.question.id, data.id);
	$(".qna-comment-slipp-articles").prepend(template);
	$(".answer-write textarea").val("");
}

$("a.link-delete-article").click(deleteAnswer);
function deleteAnswer(e){
	e.preventDefault();
	var deleteBtn = $(this);
	var url = $(this).attr("href");
	console.log("url : " + url);
	$.ajax({
		type : 'delete',
		url : url,
		dataType : 'json',
		error : function (xhr, status) {
			console.log("error =>");
			console.log(xhr);
			console.log("error status=>");
			console.log(status);
		},
		success : function (data, status){
			console.log(data);
			if (data.valid){
				deleteBtn.closest("article").remove();
			} else {
				alert(data.errorMessage());
			}
		}
	});
}



String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d+)}/g, function(match, number) {
		return typeof args[number] != 'undefined'
			? args[number] : match;
		});
}
