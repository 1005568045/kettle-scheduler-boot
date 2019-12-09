var treeData;

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

    initData();
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
                $("#jobType").append('<option value="' + list[i].code + '">' + list[i].value + '</option>');
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
                $("#jobRepositoryId").append('<option value="' + list[i].id + '">' + list[i].repName + '</option>');
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

$("#jobRepositoryId").change(function(){
    var repositoryId = $(this).val();
    if (repositoryId > 0){
        $.ajax({
            type: 'GET',
            async: false,
            url: '/sys/repository/findTransRepTreeById.do?id=' + repositoryId,
            data: {},
            success: function (data) {
                treeData = data.result;
            },
            error: function () {
                alert("请求失败！重新操作");
            },
            dataType: 'json'
        });
    }else{
        treeData = null;
    }
});

$("#jobPath").click(function(){
    var $jobRepositoryId = $("#jobRepositoryId").val();
    if (treeData != null){
        var index = layer.open({
            type: 1,
            title: '请选择转换',
            area: ["300px", '100%'],
            skin: 'layui-layer-rim',
            content: '<div id="repositoryTree"></div>'
        });
        debugger;
        $('#repositoryTree').jstree({
            'core': {
                'data': treeData
            },
            'plugins' : ["search"]
        }).bind('select_node.jstree', function (event, data) {  //绑定的点击事件
            var transNode = data.node;
            if (transNode.icon === "none"){
                var transPath = "";
                //证明是最子节点
                for (var i = 0; i < treeData.length; i++){
                    if (treeData[i].id === transNode.parent){
                        transPath = treeData[i].path;
                    }
                }
                for (var i = 0; i < treeData.length; i++){
                    if (treeData[i].id === transNode.id){
                        transPath += "/" + treeData[i].text;
                    }
                }
                layer.close(index);
                $("#jobPath").val(transPath);
            }
        });
    }else if($jobRepositoryId !== "" && treeData == null){
        layer.msg("请等待资源库加载");
    }else if($jobRepositoryId === ""){
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
    $("#RepositoryJobForm").validate({
        rules: {
            jobRepositoryId:{
                required: true
            },
            jobPath: {
                required: true
            },
            categoryId: {
                required: true
            },
            jobName: {
                required: true,
                maxlength: 50
            },
            jobQuartz:{
                required: true
            },
            jobLogLevel: {
                required: true
            },
            jobDescription: {
                maxlength: 500
            }
        },
        messages: {
            jobRepositoryId:{
                required: icon + "请选择资源库"
            },
            jobPath: {
                required: icon + "请选择作业"
            },
            categoryId:{
                required: icon + "请选择作业分类"
            },
            jobName: {
                required: icon + "请输入作业名称",
                maxlength: icon + "作业名称不能超过50个字符"
            },
            jobQuartz:{
                required: icon + "请选择作业执行策略"
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
                contentType: "application/json;charset=UTF-8",
                success: function (res) {
                    if (res.success){
                        layer.msg('添加成功',{
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

function initData(){
    var jobId = $("#id").val();
    debugger;
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/job/getJobDetail.do?id=' + jobId,
        data: {},
        success: function (data) {
            if (data.success) {
                var job = data.result;
                $("#jobType").find("option[value=" + job.jobType + "]").prop("selected",true);
                $("#jobRepositoryId").find("option[value=" + job.jobRepositoryId + "]").prop("selected",true);
                $("#jobPath").val(job.jobPath);
                $("#categoryId").find("option[value=" + job.categoryId + "]").prop("selected",true);
                $("#jobName").val(job.jobName);
                $("#jobQuartz").find("option[value=" + job.jobQuartz + "]").prop("selected",true);
                $("#jobLogLevel").find("option[value=" + job.jobLogLevel + "]").prop("selected",true);
                $("#jobDescription").val(job.jobDescription);
            } else {
                layer.msg(data.message, {icon: 2});
            }
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
}