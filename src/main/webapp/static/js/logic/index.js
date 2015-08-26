/**
* 获取统计数据
**/
$(document).ready(function(){
    /**
     * 获取app的装机用户数
     */
    $.ajax({
        url:'app_base_data',
        type:'GET',
        dataType:'json',
        success:function(data) {
            data = JSON.parse(data.data);
            $('#all_install_user').html(data.installations);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });

    /**
     * 获取app的注册用户数
     */
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

    /**
     * 获取云作业下载数
     */
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

    /**
     * 获取云作业浏览数
     */
    $.ajax({
        url:'hiworks_view_count',
        type:'GET',
        dataType:'json',
        success:function(data) {
            $('#hiworks_view_count').html(data.data.count);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });

    /**
     * 获取教师和学生的人数
     */
    $.ajax({
        url:'student_teacher_count',
        type:'GET',
        dataType:'json',
        success:function(data) {
            t_s_count.setData([{
                label: "设计师人数",
                value: data.data.studentCount
            }, {
                label: "设计讲师人数",
                value: data.data.teacherCount
            }]);
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

    /**
     * 获取每天解决的问题数
     */
    $.ajax({
        url:'questions_resolved_everyday',
        type:'GET',
        dataType:'json',
        success:function(data) {
            console.log(JSON.stringify(data.data));
            questions_resolved_line_chart.setData(data.data);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert('server exception');
        }
    });

});