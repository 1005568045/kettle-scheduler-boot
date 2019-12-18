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

function getTransLogLevel() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/enum/logLevel.do',
        data: {},
        success: function (data) {
            var list = data.result;
            for (var i=0; i<list.length; i++){
                $("#transLogLevel").append('<option value="' + list[i].code + '">' + list[i].value + '</option>');
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
                $("#transQuartz").append('<option value="' + list[i].id + '">' + list[i].quartzDescription + '</option>');
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

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

function submitListener() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#RepositoryTransForm").validate({
        rules: {
            transQuartz:{
                required: true
            },
            syncStrategy:{
                checkRegex: '^((T\\+)\\d+)$'
            },
            transLogLevel: {
                required: true
            },
            transDescription: {
                maxlength: 500
            }
        },
        messages: {
            transQuartz:{
                required: icon + "请选择转换执行策略"
            },
            syncStrategy:{
                checkRegex: icon + "同步策略只能是T+N(N是正整数)"
            },
            transLogLevel: {
                required: icon + "请选择转换的日志记录级别"
            },
            transDescription: {
                maxlength: icon + "转换描述不能超过500个字符"
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
                url: '/sys/trans/update.do',
                data: JSON.stringify(data),
                processData: true,
                contentType: "application/json;charset=UTF-8",
                success: function (res) {
                    if (res.success){
                        layer.msg('添加成功',{
                            time: 1000,
                            icon: 6
                        });
                        // 成功后跳转到列表页面
                        setTimeout(function(){
                            location.href = "/web/trans/list.shtml";
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
    location.href = "/web/trans/list.shtml";
}

function initData(){
    var transId = $("#id").val();
    debugger;
    if (transId && transId !== "") {
        $.ajax({
            type: 'GET',
            async: false,
            url: '/sys/trans/getTransDetail.do?id=' + transId,
            data: {},
            success: function (data) {
                if (data.success) {
                    var Trans = data.result;
                    $("#categoryId").find("option[value=" + Trans.categoryId + "]").prop("selected",true);
                    $("#transQuartz").find("option[value=" + Trans.transQuartz + "]").prop("selected",true);
                    $("#transLogLevel").find("option[value=" + Trans.transLogLevel + "]").prop("selected",true);
                    $("#transDescription").val(Trans.transDescription);
                    $("#syncStrategy").val(Trans.syncStrategy);
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

// 自定义校验
$.validator.addMethod("checkRegex", function (value, element, param) {
    if (!value) {
        return true;
    }
    var regExp = new RegExp(param);
    return regExp.test(value);
}, "值与规则不匹配");