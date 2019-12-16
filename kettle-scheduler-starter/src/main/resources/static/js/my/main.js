$(document).ready(function () {
    // 顶部监控任务数据
    getMonitorTask();
    // 7天数据统计图
    get7DayStatus();
    // 转换统计列表
    monitorTrans();
    // 作业统计列表
    monitorJob();
});

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

function monitorTrans() {
    $('#transMonitorList').bootstrapTable({
        url: '/sys/trans/monitor/findTransMonitorListByPage.do',            //请求后台的URL（*）
        method: 'POST',            //请求方式（*）
        toolbar: '#toolbar',        //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams,//传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [5, 25, 50, 100],        //可供选择的每页的行数（*）
        search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        // height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        responseHandler: function(res) {
            return {
                "total": res.result.totalPages,//总页数
                "rows": res.result.content//数据列表
            };
        },
        columns: [
            {
                field: 'id',
                title: '记录编号'
            }, {
                field: 'categoryName',
                title: '分类名'
            }, {
                field: 'transName',
                title: '转换名'
            }, {
                field: 'monitorSuccess',
                title: '成功次数'
            }, {
                field: 'monitorFail',
                title: '失败次数'
            }, {
                field: 'monitorStatus',
                title: '监控状态'
            }]
    });
}

function monitorJob() {
    $('#jobMonitorList').bootstrapTable({
        url: '/sys/job/monitor/findJobMonitorListByPage.do',            //请求后台的URL（*）
        method: 'POST',            //请求方式（*）
        toolbar: '#toolbar',        //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams,//传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [5, 25, 50, 100],        //可供选择的每页的行数（*）
        search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        // height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        responseHandler: function(res) {
            return {
                "total": res.result.totalPages,//总页数
                "rows": res.result.content//数据列表
            };
        },
        columns: [
            {
                field: 'id',
                title: '记录编号'
            }, {
                field: 'categoryName',
                title: '分类名'
            }, {
                field: 'jobName',
                title: '作业名'
            }, {
                field: 'monitorSuccess',
                title: '成功次数'
            }, {
                field: 'monitorFail',
                title: '失败次数'
            }, {
                field: 'monitorStatus',
                title: '监控状态'
            }]
    });
}

function queryParams(params) {
    return  JSON.stringify({
        page: {
            size: 10,
            number: 1
        },
        query: {
            monitorStatus: 1
        }
    });
}

function searchTrans() {
    $('#transMonitorList').bootstrapTable('refresh');
}

function searchJobs() {
    $('#jobMonitorList').bootstrapTable('refresh');
}