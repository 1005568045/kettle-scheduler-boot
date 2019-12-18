$(document).ready(function () {
    // 日志级别
    getTransLogLevel();
    // 任务分类
    getCategory();
    // 定时策略
    getQuartz();
    // 提交事件监听
    submitListener();
    // 初始化数据
    initData();
});

$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    },
    success: function (element) {
        element.closest('.form-group').removeClass('has-error').addClass('has-success');
    },
    errorElement: "span",
    errorPlacement: function (error, element) {
        if (element.is(":radio") || element.is(":checkbox") || element.is(":file") || element[0].id === "location") {
            error.appendTo(element.parent().parent());
        } else {
            error.appendTo(element.parent());
        }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});

// 自定义校验
$.validator.addMethod("checkRegex", function (value, element, param) {
    if (!value) {
        return true;
    }
    var regExp = new RegExp(param);
    return regExp.test(value);
}, "值与规则不匹配");

function submitListener() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#RepositoryJobForm").validate({
        rules: {
            jobQuartz:{
                required: true
            },
            syncStrategy:{
                checkRegex: '^((T\\+)\\d+)$'
            },
            jobLogLevel: {
                required: true
            },
            jobDescription: {
                maxlength: 500
            }
        },
        messages: {
            jobQuartz:{
                required: icon + "请选择作业执行策略"
            },
            syncStrategy:{
                checkRegex: icon + "同步策略只能是T+N(N是正整数)"
            },
            jobLogLevel: {
                required: icon + "请选择作业的日志记录级别"
            },
            jobDescription: {
                maxlength: icon + "作业描述不能超过500个字符"
            }
        },
        // 提交按钮监听 按钮必须type="submit"
        submitHandler:function(form){
            // 获取表单数据
            var data = {};
            $.each($("form").serializeArray(), function (i, field) {
                data[field.name] = field.value;
            });
            // 保存数据
            $.ajax({
                type: 'PUT',
                async: false,
                url: '/sys/job/update.do',
                data: JSON.stringify(data),
                processData: true,
                contentType: "application/json;charset=UTF-8",
                success: function (res) {
                    if (res.success){
                        layer.msg('编辑成功',{
                            time: 1000,
                            icon: 6
                        });
                        // 成功后跳转到列表页面
                        setTimeout(function(){
                            location.href = "/web/job/list.shtml";
                        },1000);
                    }else {
                        layer.msg(res.message, {icon: 2});
                    }
                },
                error: function () {
                    layer.msg(res.message, {icon: 5});
                },
                dataType: 'json'
            });
        }
    });
}

function cancel(){
    location.href = "/web/job/list.shtml";
}

function getTransLogLevel() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/enum/logLevel.do',
        data: {},
        success: function (data) {
            var list = data.result;
            for (var i=0; i<list.length; i++){
                $("#jobLogLevel").append('<option value="' + list[i].code + '">' + list[i].value + '</option>');
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

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

function getQuartz() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/quartz/findQuartzList.do',
        data: {},
        success: function (data) {
            var list = data.result;
            for (var i=0; i<list.length; i++){
                $("#jobQuartz").append('<option value="' + list[i].id + '">' + list[i].quartzDescription + '</option>');
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function initData(){
    var jobId = $("#id").val();
    if (jobId && jobId !== "") {
        $.ajax({
            type: 'GET',
            async: false,
            url: '/sys/job/getJobDetail.do?id=' + jobId,
            data: {},
            success: function (data) {
                if (data.success) {
                    var job = data.result;
                    $("#categoryId").find("option[value=" + job.categoryId + "]").prop("selected",true);
                    $("#jobQuartz").find("option[value=" + job.jobQuartz + "]").prop("selected",true);
                    $("#jobLogLevel").find("option[value=" + job.jobLogLevel + "]").prop("selected",true);
                    $("#jobDescription").val(job.jobDescription);
                    $("#syncStrategy").val(job.syncStrategy);
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            },
            error: function () {
                layer.alert("请求失败！请刷新页面重试");
            },
            dataType: 'json'
        });
    } else {
        layer.msg("获取编辑信息失败", {icon: 5});
    }
}