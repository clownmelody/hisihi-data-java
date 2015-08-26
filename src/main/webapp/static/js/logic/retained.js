/**
* 获取用户留存率统计数据
**/

function updateUserRetainedData(start_date, end_date, type, device){
    $.ajax({
        url:'user_retained',
        type:'GET',
        data: {'start_date': start_date, 'end_date': end_date, 'type': type, 'device': device},
        dataType:'json',
        success:function(data) {
            console.log(data.data);
            var data = JSON.parse(data.data);
            for(var i=0; i<data.length; i++){
                var date = data[i].install_period;
                var new_user = data[i].total_install;
                var rate_array = data[i].retention_rate;
                var tr = document.createElement('tr');
                var td_date = document.createElement('td');
                td_date.innerHTML = date;
                var td_new_user = document.createElement('td');
                td_new_user.innerHTML = new_user;
                tr.appendChild(td_date);
                tr.appendChild(td_new_user);
                for(var j=0; j<rate_array.length; j++){
                    var rate = rate_array[j];
                    var td = document.createElement('td');
                    td.innerHTML = rate;
                    tr.appendChild(td);
                }
                $('#table_content').append(tr);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });
};

function compareDate(start_date, end_date)
{
    var start_date = new Date(start_date.replace(/\-/g, "\/"));
    var end_date = new Date(end_date.replace(/\-/g, "\/"));
    return end_date-start_date;
}

function getDate(strDate) {
    if (strDate == null || strDate === undefined) return null;
    var date = new Date();
    try {
        if (strDate == undefined) {
            date = null;
        } else if (typeof strDate == 'string') {
            strDate = strDate.replace(/:/g, '-');
            strDate = strDate.replace(/ /g, '-');
            var dtArr = strDate.split("-");
            if (dtArr.length >= 3 && dtArr.length < 6) {
                date = new Date(dtArr[0], dtArr[1], dtArr[2]);
            } else if (date.length > 8) {
                date = new Date(Date.UTC(dtArr[0], dtArr[1] - 1, dtArr[2], dtArr[3] - 8, dtArr[4], dtArr[5]));
            }
        } else {
            date = null;
        }
        return date;
    } catch (e) {
        alert('格式化日期出现异常：' + e.message);
    }
};

function refreshUserActiveData(){
    var start_date = $('#start_date').val();
    var end_date = $('#end_date').val();
    var timestamp = compareDate(start_date, end_date);
    if(timestamp<0){
        alert("起始时间不可大于终止时间");
        return;
    }
    var timeslong = getDate(end_date).getTime()-getDate(start_date).getTime();
    var device = $('#device').val();
    var period = $('.list-link li .active').html();
    var type = 'daily';
    if(period=='周'){
        type = 'weekly';
    } else if(period=='月'){
        type = 'monthly';
    }
    $('#table_content').html('');
    updateUserRetainedData(start_date, end_date, type, device);
};

$(document).ready(function(){

    updateUserRetainedData();

});