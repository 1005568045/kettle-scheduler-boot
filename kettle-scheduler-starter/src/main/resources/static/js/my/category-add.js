$(document).ready(function () {
    // 提交按钮监听
    submitListener();
});

function submitListener() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#CategoryForm").validate({
        rules: {
            categoryName: {
                required: true,
                maxlength: 50,
                remote:{
                    type: 'POST',
                    cache: false,
                    url: '/sys/category/categoryExist.do',
                    data: {categoryName: function () { return $("#categoryName").val(); }}
                }
            }
        },
        messages: {
            categoryName: {
                required: icon + "请输入分类",
                maxlength: icon + "分类名称长度不能超过50",
                remote: icon + "分类名称已存在，请重新输入！"
            }
        },
        // 提交按钮监听 按钮必须type="submit"
        submitHandler:function(form){
            // 获取表单数据
            var data = {};
            $.each($("form").serializeArray(), function (i, field) {
                data[field.name] = field.value;
            });
            // 添加数据
            $.ajax({
                type: 'POST',
                async: false,
                url: '/sys/category/add.do',
                data: JSON.stringify(data),
                contentType: "application/json;charset=UTF-8",
                success: function (res) {
                    if (res.success){
                        layer.msg('添加成功',{
                            time: 1000,
                            icon: 6
                        });
                        // 成功后跳转到列表页面
                        setTimeout(function(){
                            location.href = "/web/category/list.shtml";
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
        }else {
            error.appendTo(element.parent());
        }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});

function cancel(){
    location.href = "/web/category/list.shtml";
}