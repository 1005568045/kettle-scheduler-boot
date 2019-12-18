$(document).ready(function () {
    // 加载搜索选项
    getCategory();
	// 加载列表
    getJobList();
    // 按钮绑定
    bindButton();
});

function getCategory() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/category/findCategoryList.do',
        data: {},
        success: function (data) {
            var list = data.result;
            for (var i=0; i<list.length; i++){
                $("#categoryId").append('<option value="' + list[i].id + '">' + list[i].categoryName + '</option>');
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function getJobList() {
    $('#jobList').bootstrapTable({
        url: '/sys/job/findJobListByPage.do',            //请求后台的URL（*）
        method: 'POST',            //请求方式（*）
        toolbar: '#toolbar',        //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
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
        clickToSelect: false,                //是否启用点击选中行
        // height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        smartDisplay: false,
        detailView: false,                   //是否显示父子表
        responseHandler: function(res) {
            return {
                "total": res.result.totalElements,//总页数
                "rows": res.result.content//数据列表
            };
        },
        columns: [
            {
                field: 'id',
                title: '作业编号',
                visible: false
            }, {
                field: 'jobName',
                title: '作业名称'
            }, {
                field: 'categoryName',
                title: '任务分类'
            }, {
                field: 'jobDescription',
                title: '作业描述'
            }, {
                field: 'jobTypeStr',
                title: '执行方式'
            }, {
                field: 'jobPath',
                title: '作业路径'
            }, {
                field: 'quartzDescription',
                title: '执行策略'
            }, {
                field: 'syncStrategy',
                title: '同步策略'
            }, {
                field: 'jobLogLevelStr',
                title: '日志级别'
            }, {
                field: 'jobStatusStr',
                title: '作业状态'
            }, {
                field: 'operate',
                title: '操作',
                width: 200,
                align: 'center',
                valign: 'middle',
                formatter: actionFormatter
            }]
    });
}

function queryParams(params) {
    return  JSON.stringify({
        page: {
            size: params.limit,
            number: (params.offset / params.limit) + 1
        },
        query: {
            categoryId: $("#categoryId").val(),
            jobName: $("#jobName").val()
        }
    });
}

function actionFormatter(value, row, index) {
    if (row.jobStatus === 1) {
        return [
            '<a class="btn btn-danger btn-xs" id="stop" type="button" data-id="'+ row.id +'"><i class="fa fa-stop" aria-hidden="true"></i>&nbsp;停止</a>'
        ].join('');
    } else if (row.jobStatus === 2) {
        return [
            '<a class="btn btn-primary btn-xs" id="start" type="button" data-id="'+ row.id +'"><i class="fa fa-play" aria-hidden="true"></i>&nbsp;启动</a>',
            '&nbsp;&nbsp;',
            '<a class="btn btn-primary btn-xs" id="edit" type="button" data-id="'+ row.id +'"><i class="fa fa-edit" aria-hidden="true"></i>&nbsp;编辑</a>',
            '&nbsp;&nbsp;',
            '<a class="btn btn-primary btn-xs" id="delete" type="button" data-id="'+ row.id +'"><i class="fa fa-trash" aria-hidden="true"></i>&nbsp;删除</a>'
        ].join('');
    } else {
        return [
            '<a class="btn btn-primary btn-xs" id="edit" type="button" data-id="'+ row.id +'"><i class="fa fa-edit" aria-hidden="true"></i>&nbsp;编辑</a>',
            '&nbsp;&nbsp;',
            '<a class="btn btn-primary btn-xs" id="delete" type="button" data-id="'+ row.id +'"><i class="fa fa-trash" aria-hidden="true"></i>&nbsp;删除</a>'
        ].join('');
    }
}

function search() {
    $('#jobList').bootstrapTable('refresh');
}

function jobNameFormatter(value, row, index) {
    if (value.length > 15) {
        var newValue = value.substring(0, 14);
        return newValue + "……";
    } else {
        return value;
    }
}

function bindButton() {
    // 编辑
    $('#jobList').delegate('#edit','click',function(e) {
        var $target = $(e.currentTarget);
        var jobId = $target.data('id');
        location.href = "/web/job/edit.shtml?jobId=" + jobId;
    });

    // 删除
    $('#jobList').delegate('#delete', 'click', function(e) {
        var $target = $(e.currentTarget);
        var transId = $target.data('id');
        layer.confirm('确定删除该单位？', {
                btn: ['确定', '取消']
            },
            function(index){
                layer.close(index);
                $.ajax({
                    type: 'DELETE',
                    async: true,
                    url: '/sys/job/delete.do',
                    data: {
                        "id": transId
                    },
                    success: function (data) {
                        if (data.success) {
                            location.replace(location.href);
                        } else {
                            layer.alert(data.message, {icon: 5});
                        }
                    },
                    error: function () {
                        layer.alert("系统出现问题，请联系管理员");
                    },
                    dataType: 'json'
                });
            },
            function(){
                layer.msg('取消操作');
            }
        );
    });

    // 单个任务启动
    $('#jobList').delegate('#start','click',function(e) {
        var $target = $(e.currentTarget);
        var transId = $target.data('id');
        layer.confirm(
            '确定启动该作业？',
            {btn: ['确定', '取消']},
            function (index) {
                layer.close(index);
                $.ajax({
                    type: 'GET',
                    async: true,
                    url: '/sys/job/startJob.do?id=' + transId,
                    data: {},
                    success: function (data) {
                        if (data.success) {
                            location.replace(location.href);
                        } else {
                            layer.alert(data.message, {icon: 5});
                        }
                    },
                    error: function () {
                        alert("系统出现问题，请联系管理员");
                    },
                    dataType: 'json'
                });
            },
            function () {
                layer.msg('取消操作');
            }
        );
    });

    // 单个任务停止
    $('#jobList').delegate('#stop','click',function(e) {
        var $target = $(e.currentTarget);
        var transId = $target.data('id');
        layer.confirm(
            '确定停止该作业？',
            {btn: ['确定', '取消']},
            function (index) {
                layer.close(index);
                $.ajax({
                    type: 'GET',
                    async: true,
                    url: '/sys/job/stopJob.do?id=' + transId,
                    data: {},
                    success: function (data) {
                        if (data.success) {
                            location.replace(location.href);
                        } else {
                            layer.alert(data.message, {icon: 5});
                        }
                    },
                    error: function () {
                        alert("系统出现问题，请联系管理员");
                    },
                    dataType: 'json'
                });
            },
            function () {
                layer.msg('取消操作');
            }
        );
    });

    // 启动全部停止的任务
    $('#startAll').on('click',function(e) {
        layer.confirm(
            '确定启动全部已停止的作业？',
            {btn: ['确定', '取消']},
            function (index) {
                layer.close(index);
                $.ajax({
                    type: 'GET',
                    async: true,
                    url: '/sys/job/startAllJob.do',
                    data: {},
                    success: function (data) {
                        if (data.success) {
                            location.replace(location.href);
                        } else {
                            layer.alert(data.message, {icon: 5});
                        }
                    },
                    error: function () {
                        alert("系统出现问题，请联系管理员");
                    },
                    dataType: 'json'
                });
            },
            function () {
                layer.msg('取消操作');
            }
        );
    });

    // 启动全部停止的任务
    $('#stopAll').on('click',function(e) {
        layer.confirm(
            '确定停止全部已启动的作业？',
            {btn: ['确定', '取消']},
            function (index) {
                layer.close(index);
                $.ajax({
                    type: 'GET',
                    async: true,
                    url: '/sys/job/stopAllJob.do',
                    data: {},
                    success: function (data) {
                        if (data.success) {
                            location.replace(location.href);
                        } else {
                            layer.alert(data.message, {icon: 5});
                        }
                    },
                    error: function () {
                        alert("系统出现问题，请联系管理员");
                    },
                    dataType: 'json'
                });
            },
            function () {
                layer.msg('取消操作');
            }
        );
    });
}

