$(document).ready(function () {
    $("#quartzCron").cronGen({
        direction : 'left'
    });
    reset();
});
var reset = function(){
    var quartzId = $("#quartzId").val();
    $.ajax({
        type: 'POST',
        async: false,
        url: 'quartz/getQuartz.shtml',
        data: {
            quartzId : quartzId
        },
        success: function (data) {
            var Quartz = data.data;
            $("#quartzDescription").val(Quartz.quartzDescription);
            $("#cQuarz").val(Quartz.quartzCron);
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
$().ready(function () {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#QuartzForm").validate({
        rules: {
            quartzDescription: {
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
            cQuarz: {
                required: icon + "请选择cron编码"
            }
        },
        submitHandler: function (form) {
            $("#quartzCron").val($("#cQuarz").val());
            $.post("quartz/update.shtml", decodeURIComponent($(form).serialize(), true), function (data) {
                var result = JSON.parse(data);
                if (result.status == "success") {
                    layer.msg('更新成功', {
                        time: 2000,
                        icon: 6
                    });
                    setTimeout(function () {
                        location.href = "view/quartz/listUI.shtml";
                    }, 2000);
                } else {
                    layer.msg(result.message, {icon: 2});
                }
            });
        }
    });
});
var cancel = function(){
	location.href = "view/quartz/listUI.shtml";
}