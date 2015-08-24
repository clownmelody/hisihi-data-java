// Morris.js Charts sample data for SB Admin template

$(function() {

    // 活跃用户图表
    active_user_area_chart = Morris.Area({
        element: 'active-user-area-chart',
        data: [/*{"android_active":53,"day":"2015-08-10","ios_active":18},
            {"android_active":50,"day":"2015-08-11","ios_active":20},
            {"android_active":278,"day":"2015-08-12","ios_active":19}*/],
        xkey: 'day',
        ykeys: ['android_active', 'ios_active'],
        labels: ['android活跃用户数', 'ios活跃用户数'],
        pointSize: 2,
        hideHover: 'auto',
        resize: true
    });

});
