$(document).ready(function () {
    // 提交按钮监听
    submitListener();
});

function submitListener() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#UserForm").validate({
        rules: {
            nickname: {
                required: true
            },
            email:{
                email:true
            },
            phone:{
                digits:true,
                minlength:11
            },
            account: {
                required: true,
                onlyLetterAndDigit: true,//使用自定义方法限制只能输入字母或数字
                rangelength: [4,20],
                remote:{
                    type: 'POST',
                    cache: false,
                    url: '/sys/user/accountExist.do',
                    data: {username: function () { return $("#account").val(); }}
                }
            },
            password: {
                required:true,
                rangelength: [4,20]
            },
            checkPassword: {
                required:true,
                equalTo:"#password"
            }
        },
        messages: {
            nickname: {
                required: icon + "请输入用户昵称"
            },
            email:{
                email: icon + "请输入合法的电子邮件"
            },
            phone:{
                digits: icon + "请输入数字",
                minlength: icon + "电话长度必须为11位"
            },
            account: {
                required: icon + "请输入用户账号",
                rangelength: icon + "用户名长度必须在4～20位之间",
                remote: icon + "用户名已存在，请重新输入！"
            },
            password: {
                required: icon + "请输入密码",
                rangelength: icon + "密码长度必须在4～20位之间"
            },
            checkPassword:{
                required: icon + "请再次输入密码",
                equalTo: icon + "密码输入不一致，请重新输入"
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
                url: '/sys/user/add.do',
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
                            location.href = "/web/user/list.shtml";
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

//扩展validator的校验方法
$.validator.addMethod("onlyLetterAndDigit",function(value, element, params){
    var regex=new RegExp('^[0-9a-zA-Z]+$');
    return regex.test(value);
},"只能输入字母或数字");

function cancel(){
    location.href = "/web/user/list.shtml";
}