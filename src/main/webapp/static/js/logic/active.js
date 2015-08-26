/**
* 获取活跃用户数统计数据
**/

function updateUserActiveData(start_date, end_date, type){
    $.ajax({
        url:'user_active',
        type:'GET',
        data: {'start_date': start_date, 'end_date': end_date, 'type': type},
        dataType:'json',
        success:function(data) {
            active_user_area_chart.setData(data.data);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });
};

function refreshUserActiveData(){
    var start_date = $('#start_date').val();
    var end_date = $('#end_date').val();
    var period = $('.list-link li .active').html();
    var type = 'daily';
    if(period=='周'){
        type = 'weekly';
    } else if(period=='月'){
        type = 'monthly';
    }
    updateUserActiveData(start_date, end_date, type);
};

$(document).ready(function(){

    updateUserActiveData();

});