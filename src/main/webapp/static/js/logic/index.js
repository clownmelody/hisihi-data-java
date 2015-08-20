/**
* 获取统计数据
**/
$(document).ready(function(){
    $.ajax({
        url:'app_base_data',
        type:'GET',
        dataType:'json',
        success:function(data) {
            //data = JSON.parse(data);
            data = JSON.parse(data.data);
            $('#all_install_user').html(data.installations);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });

    $.ajax({
        url:'register_total_count',
        type:'GET',
        dataType:'json',
        success:function(data) {
            $('#all_register_user').html(data.data.count);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });

    $.ajax({
        url:'hiworks_download_total_count',
        type:'GET',
        dataType:'json',
        success:function(data) {
            $('#hiworks_download_count').html(data.data.count);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });

    $.ajax({
        url:'student_teacher_count',
        type:'GET',
        dataType:'json',
        success:function(data) {
            Morris.Donut({
                element: 'student-teacher-count-chart',
                data: [{
                    label: "设计师人数",
                    value: data.data.studentCount
                }, {
                    label: "设计讲师人数",
                    value: data.data.teacherCount
                }],
                resize: true
            });
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            Morris.Donut({
                element: 'student-teacher-count-chart',
                data: [{
                    label: "设计师人数",
                    value: 0
                }, {
                    label: "设计讲师人数",
                    value: 0
                }],
                resize: true
            });
            alert('server exception');
        }
    });

});