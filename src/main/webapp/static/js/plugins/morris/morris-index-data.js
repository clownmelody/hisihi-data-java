// Morris.js Charts sample data for SB Admin template

$(function() {

    // 设计师+讲师数量图表
    t_s_count = Morris.Donut({
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

    // 每天解决的问题数曲线图
    questions_resolved_line_chart = Morris.Line({
        element: 'questions-resolved-line-chart',
        data: [{
            date: '',
            count: 0
        }],
        xkey: 'date',
        ykeys: ['count'],
        labels: ['questions-resolved'],
        smooth: false,
        resize: true
    });

    // K 因子
    /*k_bar_chart = Morris.Bar({
        element: 'k-bar-chart',
        data: [{
            device: '',
            geekbench: 0
        }],
        xkey: 'device',
        ykeys: ['geekbench'],
        labels: ['Geekbench'],
        barRatio: 0.4,
        xLabelAngle: 35,
        hideHover: 'auto',
        resize: true
    });*/


});
