$(document).ready(function () {
    // 执行方式下拉列表
    getRunType();
    // 日志级别
    getTransLogLevel();
    // 执行方式下拉列表
    getRepository();
    // 任务分类
    getCategory();
    // 定时策略
    getQuartz();

    submitListener();
});

function getRunType() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/enum/runType.do',
        data: {},
        success: function (data) {
            var list = data.result;
            for (var i=0; i<list.length; i++){
                $("#transType").append('<option value="' + list[i].code + '">' + list[i].value + '</option>');
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}

function getRepository() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/repository/findRepList.do',
        data: {},
        success: function (data) {
            var list = data.result;
            for (var i=0; i<list.length; i++){
                $("#transRepositoryId").append('<option value="' + list[i].id + '">' + list[i].repName + '</option>');
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
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

function findTransRepTreeById(id) {
    var treeData = "";
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/repository/findTransRepTreeById.do?id=' + id,
        data: {},
        success: function (data) {
            if (data.success) {
                treeData = data.result;
            }
        },
        error: function () {
            alert("请求失败！重新操作");
        },
        dataType: 'json'
    });
    return treeData;
}

$("#transPath").click(function(){	
	var $transRepositoryId = $("#transRepositoryId").val();
	if ($transRepositoryId && $transRepositoryId !== "") {
	    var treeData = findTransRepTreeById($transRepositoryId);
        if (treeData && treeData !== ""){
            var index = layer.open({
                type: 1,
                title: '请选择转换',
                area: ["400px", '60%'],
                skin: 'layui-layer-rim',
                content: '<div id="repositoryTree"></div>'
            });
            $('#repositoryTree').jstree({
                'core': {
                    'data': treeData
                },
                'plugins' : ["search"]
            }).bind('select_node.jstree', function (event, data) {  //绑定的点击事件
                // jsTree实例对象
                var ins = data.instance;
                // 当前节点
                var transNode = data.node;
                // 是叶子节点才进入
                if (transNode.original.leaf){
                    // 关闭弹窗
                    layer.close(index);
                    // 设置路径
                    $("#transPath").val(transNode.original.extra);
                }
            });
        }else {
            layer.msg("请等待资源库加载");
        }
    } else {
        layer.msg("请先选择资源库");
    }
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
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.parent().parent().parent());
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
            transRepositoryId:{
                required: true
            },
            transPath: {
                required: true
            },
            categoryId: {
                required: true,
            },
            transName: {
                required: true,
                maxlength: 50
            },
            transQuartz:{
                required: true
            },
            transLogLevel: {
                required: true
            },
            transDescription: {
                maxlength: 500
            }
        },
        messages: {
            transRepositoryId:{
                required: icon + "请选择资源库"
            },
            transPath: {
                required: icon + "请选择转换"
            },
            categoryId:{
                required: icon + "请选择作业分类"
            },
            transName: {
                required: icon + "请输入转换名称",
                maxlength: icon + "转换名称不能超过50个字符"
            },
            transQuartz:{
                required: icon + "请选择转换执行策略"
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
                type: 'POST',
                async: false,
                url: '/sys/trans/add.do',
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