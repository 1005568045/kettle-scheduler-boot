$(document).ready(function () {
    // 隐藏动态表单
    hideForm();
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
    // 提交事件监听
    submitListener();
    // 监听文件下拉
    $('#jobType').on('change', jobTypeChange);
});

// 选择文件后把地址显示到输入框
$('#jobFile').change(function() {
    var filePath = $(this).val();
    $('#location').val(filePath).blur();
    $('#jobName').val(getFileName(filePath)).blur();
});

$("#jobPath").click(function(){
    var $jobRepositoryId = $("#jobRepositoryId").val();
    if ($jobRepositoryId && $jobRepositoryId !== "") {
        var treeData = findJobRepTreeById($jobRepositoryId);
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
                var jobNode = data.node;
                // 是叶子节点才进入
                if (jobNode.original.leaf){
                    // 关闭弹窗
                    layer.close(index);
                    // 设置路径
                    $("#jobPath").val(jobNode.original.extra).blur();
                    $('#jobName').val(jobNode.original.text).blur();
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
$.validator.addMethod("checkFileType", function (file, element, param) {
    return getFileType(file) === param
}, "只能上传kjb文件");

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
            jobType:{
                required: true
            },
            jobRepositoryId:{
                required: true
            },
            location: {
                required: true,
                checkFileType: "kjb"
            },
            jobPath: {
                required: true
            },
            jobName: {
                required: true,
                maxlength: 100,
                remote:{
                    type: 'POST',
                    cache: false,
                    url: '/sys/job/jobNameExist.do',
                    data: {jobName: function () { return $("#jobName").val(); }}
                }
            },
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
            jobType:{
                required: icon + "请选择执行方式"
            },
            jobRepositoryId:{
                required: icon + "请选择资源库"
            },
            location: {
                required: icon + "请上传作业",
                checkFileType: icon + "只能上传kjb文件"
            },
            jobPath: {
                required: icon + "请选择作业"
            },
            jobName: {
                required: icon + "请输入作业名称",
                maxlength: icon + "作业名称不能超过100个字符",
                remote: icon + "名称已存在"
            },
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
            var data= new FormData($("#RepositoryJobForm")[0]);
            // 保存数据
            $.ajax({
                type: 'POST',
                async: false,
                url: '/sys/job/add.do',
                data: data,
                processData: false,
                contentType: false,
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

// 隐藏动态表单
function hideForm() {
    // 初始化隐藏动态表单
    var $sync = $('.sync-field');
    $sync.hide();
    $sync.find(":input").attr("disabled", true);
    $sync.find(":selected").attr("disabled", true);
}

function jobTypeChange() {
    var type = $('#jobType').val();
    // 隐藏动态表单
    hideForm();
    // 如果选择了具体的值
    if (type) {
        if (type === 'file') {
            var $fileData = $('[data-field="file"]');
            $fileData.show();
            $fileData.find(":input").attr("disabled", false);
            $fileData.find(":selected").attr("disabled", false);
        }
        if (type === 'rep') {
            var $repData = $('[data-field="rep"]');
            $repData.show();
            $repData.find(":input").attr("disabled", false);
            $repData.find(":selected").attr("disabled", false);
        }
    }
}

function getFileName(filePath) {
    var reg = new RegExp("\\\\","g");
    var f = filePath.replace(reg, "/");
    var fileArr = f.split("/");
    var fileName = fileArr[fileArr.length-1].toLowerCase();
    return fileName.substring(0, fileName.lastIndexOf("."));
}

function getFileType(filePath) {
    var reg = new RegExp("\\\\","g");
    var f = filePath.replace(reg, "/");
    var fileArr = f.split("/");
    var fileNameArr=fileArr[fileArr.length-1].toLowerCase().split(".");
    return  fileNameArr[fileNameArr.length-1];
}

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

function findJobRepTreeById(repositoryId) {
    var treeData = "";
    $.ajax({
        type: 'GET',
        async: false,
        url: '/sys/repository/findJobRepTreeById.do?id=' + repositoryId,
        data: {},
        success: function (data) {
            treeData = data.result;
        },
        error: function () {
            alert("请求失败！重新操作");
        },
        dataType: 'json'
    });
    return treeData;
}