/**
 * 查询用户的发帖回帖量
 * @param user_mobile
 * @param start_date
 * @param end_date
 */

function queryUserPostCount(){
    var user_mobile = $('#user_account').val();
    var user_count = $('#user_count').val();
    var start_date = $('#start_date').val();
    var end_date = $('#end_date').val();
    if(user_mobile==''||start_date==''||end_date==''){
        alert("参数不允许为空");
        return;
    }
    var timestamp = compareDate(start_date, end_date);
    if(timestamp<0){
        alert("起始时间不可大于终止时间");
        return;
    }
    $('#query_button').attr('disabled', 'disabled').text('查询中');
    $.ajax({
        url:'user_post_count',
        type:'GET',
        data: {'user_mobile': user_mobile, 'user_count': user_count,
            'start_date': start_date, 'end_date': end_date},
        dataType:'json',
        success:function(data) {
            var postHtmlContent = '';
            var replyHtmlContent = '';
            for(var i=0; i<data.data.length; i++){
                var mobile = data.data[i].mobile;
                var postCount = data.data[i].postCount;
                var postReplyCount = data.data[i].postReplyCount;
                postHtmlContent += '<span style="color:red;">'+mobile+' ------ '+postCount+'</span><br/>';
                replyHtmlContent += '<span style="color:red;">'+mobile+' ------ '+postReplyCount+'</span><br/>';
            }
            $('#user_post_count').html(postHtmlContent);
            $('#user_post_reply_count').html(replyHtmlContent);
            $('#query_button').removeAttr('disabled').text('查询');
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $('#query_button').removeAttr('disabled').text('查询');
            alert('server exception');
        }
    });
};


function queryChannelDownloadCount(){
    var channel = $('#channel').val();
    if(channel==''||channel==0){
        alert("参数不允许为空");
        return;
    }
    $('#query_channel_button').attr('disabled', 'disabled').text('查询中');
    $.ajax({
        url:'queryChannelCount',
        type:'GET',
        data: {'channel': channel},
        dataType:'json',
        success:function(data) {
            $('#user_download_count').html('<h4 style="color:red;">'+data.count+'</h4>');
            $('#query_channel_button').removeAttr('disabled').text('查询');
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $('#query_channel_button').removeAttr('disabled').text('查询');
            alert('server exception');
        }
    });
};

function compareDate(start_date, end_date){
    var start_date = new Date(start_date.replace(/\-/g, "\/"));
    var end_date = new Date(end_date.replace(/\-/g, "\/"));
    return end_date-start_date;
}

$(document).ready(function(){

});