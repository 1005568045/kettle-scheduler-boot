$(document).ready(function () {
    // 根据用户名查询用户信息显示
    getUserInfo();
    getMonitorTask();
    get7DayStatus();


});

/*退出登录*/
function logout() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/login/out.do',
        success: function (res) {
            if (!res.success) {
                alert(res.message);
            } else {
                window.localStorage.clear();
                window.location.replace("/");
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function getMonitorTask() {
    /*获取全部在监控的任务*/
    $.ajax({
        type: 'POST',
        async: false,
        url: '/home/monitor/taskCount.do',
        success: function (data) {
            if (!data.success) {
                alert(data.message);
            } else {
                $("#allNum").text(data.result.totalTaskNum);
                $("#transNum").text(data.result.transTaskNum);
                $("#jobNum").text(data.result.jobTaskNum);
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function get7DayStatus() {
    // 获取7天监控状况
    $.ajax({
        type: 'POST',
        async: false,
        url: '/home/monitor/runStatus.do',
        success: function (data) {
            if (!data.success) {
                alert(data.message)
            } else {
                var legend = [];
                var trans = [];
                var jobs = [];
                $.each(data.result, function (i, field) {
                    legend.push(field.runTime);
                    trans.push(field.transRunNum);
                    jobs.push(field.jobRunNum);
                })

            }
            var lineChart = echarts.init(document.getElementById("kettleLine"));
            var lineoption = {
                title: {
                    text: '7天内作业和转换的监控状况'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['作业', '转换']
                },
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: legend
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        }
                    }
                ],
                series: [
                    {
                        name: '转换',
                        type: 'line',
                        data: trans,
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                    },
                    {
                        name: '作业',
                        type: 'line',
                        data: jobs,
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                    }
                ]
            };
            lineChart.setOption(lineoption);
            $(window).resize(lineChart.resize);
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function getUserInfo() {
    var username = window.localStorage.getItem("username");
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/user/getUserByUsername.do?username=' + username,
        success: function (res) {
            if (!res.success) {
                alert(res.message);
            } else {
                $(".navbar-user").text('你好, ' + res.result.nickname);
                window.localStorage.setItem('userInfo', JSON.stringify(res.result));
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function MonitorTransFormatter(value, row, index) {
    var MonitorTrans = "";
    $.ajax({
        type: 'POST',
        async: false,
        url: 'trans/getTrans.shtml',
        data: {
            "transId": value
        },
        success: function (data) {
            var Trans = data.data;
            MonitorTrans = Trans.transName;
        },
        error: function () {
            alert("系统出现问题，请联系管理员");
        },
        dataType: 'json'
    });
    return MonitorTrans;
}

function MonitorJobFormatter(value, row, index) {
    var MonitorJob = "";
    $.ajax({
        type: 'POST',
        async: false,
        url: 'job/getJob.shtml',
        data: {
            "jobId": value
        },
        success: function (data) {
            var job = data.data;
            MonitorJob = job.jobName;
        },
        error: function () {
            alert("系统出现问题，请联系管理员");
        },
        dataType: 'json'
    });
    return MonitorJob;
}

function queryParams(params) {
    var temp = {limit: 10, offset: params.offset};
    return temp;
}

function searchTrans() {
    $('#transMonitorList').bootstrapTable('refresh', "main/getTransList.shtml");
}

function searchJobs() {
    $('#jobMonitorList').bootstrapTable('refresh', "main/getJobList.shtml");
}