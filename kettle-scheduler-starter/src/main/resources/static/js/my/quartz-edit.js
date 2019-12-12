$(document).ready(function () {
    // 加载cron组件
    loadCron();

    submitListener();

    initData();
});

$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    },
    success: function (element) {
        $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
    },
    errorElement: "span",
    errorPlacement: function (error, element) {
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.parent().parent().parent());
        }else if(element.is("#cQuarz")){
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
    $("#QuartzForm").validate({
        rules: {
            quartzDescription: {
                required: true
            },
            quartzCron: {
                required: true
            },
            cQuarz: {
                required: true
            }
        },
        messages: {
            quartzDescription: {
                required: icon + "请输入执行策略名称"
            },
            quartzCron: {
                required: icon + "请选择cron编码"
            },
            cQuarz: {
                required: icon + "请选择cron编码"
            }
        },
        // 提交按钮监听 按钮必须type="submit"
        submitHandler:function(form){
            // 获取表单数据
            var data = {};
            $.each($("form").serializeArray(), function (i, field) {
                data[field.name] = field.value;
            });
            // 执行保存
            $.ajax({
                type: 'PUT',
                async: false,
                url: '/sys/quartz/update.do',
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                success: function (res) {
                    if (res.success){
                        layer.msg('编辑成功',{
                            time: 1000,
                            icon: 6
                        });
                        // 成功后跳转到列表页面
                        setTimeout(function(){
                            location.href = "/web/quartz/list.shtml";
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
    location.href = "/web/quartz/list.shtml";
}

function initData(){
    var quartzId = $("#id").val();
    if (quartzId && quartzId !== "") {
        $.ajax({
            type: 'GET',
            async: false,
            url: '/sys/quartz/getQuartzDetail.do?id=' + quartzId,
            data: {},
            success: function (data) {
                if (data.success) {
                    var Quartz = data.result;
                    $("#quartzDescription").val(Quartz.quartzDescription);
                    $("#cQuarz").val(Quartz.quartzCron);
                    $("#quartzCron").val(Quartz.quartzCron);
                } else {
                    layer.msg(data.message, {icon: 5});
                }
            },
            error: function () {
                alert("请求失败！请刷新页面重试");
            },
            dataType: 'json'
        });
    }
}

function loadCron() {
    // 加载cron组件
    $("#quartzCron").cronGen({
        direction : 'right'
    });
    // 设置只读
    $('#cQuarz').attr("readonly","readonly");
}