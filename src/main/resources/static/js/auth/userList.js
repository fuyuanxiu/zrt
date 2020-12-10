/**
 * 用户管理
 */
var pageCurr;
$(function() {
    layui.use('table', function(){
        var table = layui.table
            ,form = layui.form;

        tableIns=table.render({
            elem: '#uesrList'
            ,url:context+'/sysUser/getList'
            ,method: 'get' //默认：get请求
            ,cellMinWidth: 80
            ,page: true,
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'rows' //每页数据量的参数名，默认：limit
            },
            parseData: function (res) {
                // 可进行数据操作
                return {
                    "count": res.data.total,
                    "msg":res.msg,
                    "data":res.data.rows,
                    "code": res.status //code值为200表示成功
                }
            },
            // response:{
            //     statusName: 'status' //数据状态的字段名称，默认：code
            //     ,statusCode: 200 //成功的状态码，默认：0
            //     ,countName: 'count' //数据总数的字段名称，默认：count
            //     ,dataName: 'data' //数据列表的字段名称，默认：data
            // },
            cols: [[
                {type:'numbers'}
                ,{field:'id', title:'ID', width:80, unresize: true, sort: true}
                ,{field:'bsCode', title:'账号', width:120}
                ,{field:'bsName', title:'用户名称', width:120}
                ,{field:'mobile', title:'手机号', width:120}
                ,{field:'email', title: '邮箱', width:180}
                ,{field:'isJob', title:'是否在职',width:95,align:'center',templet:'#jobTpl'}
                ,{field:'createdTime', title: '添加时间', width:150}
                ,{field:'roleNames', title: '角色名称', minWidth:80}
                ,{fixed:'right', title:'操作',width:200,align:'center', toolbar:'#optBar'}
            ]]
            ,  done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        //监听在职操作
        form.on('switch(isJobTpl)', function(obj){
            //console.log(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
            setJobUser(obj,this.value,this.name,obj.elem.checked);
        });
        //监听工具条
        table.on('tool(userTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delUser(data,data.id,data.bsName);
            } else if(obj.event === 'edit'){
                //编辑
                getUserAndRoles(data,data.id);
            } else if(obj.event === 'recover'){
                //恢复
                recoverUser(data,data.id);
            } else if(obj.event === 'setPass'){
                setPassword(data,data.id,data.bsName);
            }
        });
        //监听提交
        form.on('submit(userSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });
        form.on('submit(passwordSubmit)', function(data){
            // TODO 校验
            doSetPass(data);
            return false;
        });
    });
    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#createdTimeStart'
        });
        laydate.render({
            elem: '#createdTimeEnd'
        });
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });
});

//设置用户是否离职
function setJobUser(obj,id,name,checked){
    var isJob=checked ? 0 : 1;
    var userIsJob=checked ? "在职":"离职";
    //是否离职
    layer.confirm('您确定要把用户：'+name+'设置为'+userIsJob+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.ajax({
            type: "POST",
            data: { "id": id, "isJob": isJob},
            url: context+"/sysUser/doJob",
            success: function (data) {
                if (data.result == true) {
                    layer.alert("操作成功",function(){
                        layer.closeAll();
                        loadAll();
                    });
                } else {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                    });
                }
            }
        });
    }, function(){
        layer.closeAll();
    });
}

//提交表单
function formSubmit(obj){
    var currentUser=$("#currentUser").html();
    if(checkRole()){
        if($("#id").val()==currentUser){
            layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
                btn: ['确认','返回'] //按钮
            },function(){
                layer.closeAll();//关闭所有弹框
                submitAjax(obj,currentUser);
            },function() {
                layer.closeAll();//关闭所有弹框
            });
        }else{
            submitAjax(obj,currentUser);
        }
    }
}
function submitAjax(obj,currentUser){
    var a = $("#roleIds").val();
    $.ajax({
        type: "POST",
        data: $("#userForm").serialize(),
        url: context+"/sysUser/add",
        success: function (data) {
            if(isLogin(data)){
                if (data.result == true) {
                    layer.alert("操作成功",function(){
                        if($("#id").val()==currentUser){
                            //如果是自己，直接重新登录
                            parent.location.reload();
                        }else{
                            layer.closeAll();
                            cleanUser();
                            //$("#id").val("");
                            //加载页面
                            loadAll();
                        }
                    });
                } else {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                    });
                }
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}
//检验是否选择角色
function checkRole(){

    //选中的角色
    var array = new Array();
    var arrayName = new Array();
    var roleCheckd=$(".layui-form-checked");
    //获取选中的权限id
    for(var i=0;i<roleCheckd.length;i++){
        array.push($(roleCheckd.get(i)).prev().val());
        arrayName.push($(roleCheckd.get(i)).prev().attr("title"));
    }
    //校验是否授权
    var roleIds = array.join(",");
    var roleNames = arrayName.join(",")
    if(roleIds==null || roleIds==''){
        layer.alert("请您给该用户添加对应的角色！")
        return false;
    }
    $("#roleIds").val(roleIds);
    $("#roleNames").val(roleNames);
    return true;
}

//新增编辑弹出框
function openUser(id,title){
    if(id==null || id==""){
        $("#id").val("");
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setUser'),
        end:function(){
            cleanUser();
        }
    });
}

//修改密码弹出框
function openPassword(id,title){
    if(id==null || id==""){
        return false;
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setPassword'),
        end:function(){
            cleanPassword();
        }
    });
}

//修改密码
function setPassword(obj,id,name) {
    if(obj.job){
        layer.alert("该用户已经离职，不可进行密码修改；</br>  如需修改密码，请设置为<font style='font-weight:bold;' color='green'>在职</font>状态。");
    }else if(obj.del){
        layer.alert("该用户已经删除，不可进行密码修改；</br>  如需修改密码，请先<font style='font-weight:bold;' color='blue'>恢复</font>用户状态。");
    }else{
        $("#userId").val(id);
        openPassword(id,"修改密码");
    }
}
function doSetPass(obj){
    var currentUser=$("#currentUser").html();
    if($("#id").val()==currentUser){
        layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
            btn: ['返回','确认'] //按钮
        },function(){
            layer.closeAll();
        },function() {
            layer.closeAll();//关闭所有弹框
            doSetPassAjax(obj,currentUser);
        });
    }else{
        doSetPassAjax(obj,currentUser);
    }
}
function doSetPassAjax(obj,currentUser){
    $.ajax({
        type: "POST",
        data: { "id":$("#userId").val(), "password":$("#password").val(), "rePassword":$("#rePassword").val() },
        url: context+"/sysUser/doSetPassword",
        success: function (data) {
            if (data.result == true) {
                layer.alert("操作成功",function(){
                    if($("#id").val()==currentUser){
                        //如果是自己，直接重新登录
                        parent.location.reload();
                    }else{
                        layer.closeAll();
                        cleanPassword();
                        //加载页面
                        loadAll();
                    }
                });
            } else {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                });
            }
        }
    });
}

//开通用户
function addUser(){
    $.get(context+"/sysRole/getRoles",function(data){
        if(isLogin(data)){
            if(data.result==true && data.data!=null){
                $("#roleIds").val("");
                $("#id").val("");
                $("#version").val("");
                $("#bsCode").val("");
                $("#bsName").val("");
                $("#mobile").val("");
                $("#email").val("");
                //显示角色数据
                $("#roleDiv").empty();
                $.each(data.data, function (index, item) {
                    // <input type="checkbox" name="roleId" title="发呆" lay-skin="primary"/>
                    var roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.bsName+" lay-skin='primary'/>");
                    //未选中
                    /*<div class="layui-unselect layui-form-checkbox" lay-skin="primary">
                        <span>发呆</span><i class="layui-icon">&#xe626;</i>
                        </div>*/
                    //选中
                    // <div class="layui-unselect layui-form-checkbox layui-form-checked" lay-skin="primary">
                    // <span>写作</span><i class="layui-icon">&#xe627;</i></div>
                    var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                        "<span>"+item.bsName+"</span><i class='layui-icon'>&#xe626;</i>" +
                        "</div>");
                    $("#roleDiv").append(roleInput).append(div);
                })
                openUser(null,"开通用户");
                //重新渲染下form表单 否则复选框无效
                layui.form.render('checkbox');
            }else{
                //弹出错误提示
                layer.alert("获取角色数据有误，请您稍后再试",function () {
                    layer.closeAll();
                });
            }
        }
    });
}

//编辑用户
function getUserAndRoles(obj,id) {
    //如果已经离职，提醒不可编辑和删除
    if(obj.job){
        layer.alert("该用户已经离职，不可进行编辑；</br>  如需编辑，请设置为<font style='font-weight:bold;' color='green'>在职</font>状态。");
    }else if(obj.del){
        layer.alert("该用户已经删除，不可进行编辑；</br>  如需编辑，请先<font style='font-weight:bold;' color='blue'>恢复</font>用户状态。");
    }else{
        //回显数据
        $.get(context+"/sysUser/getUserAndRoles",{"id":id},function(data){
            if(isLogin(data)){
                if(data.result==true && data.data.user!=null){
                    var existRole = new Array();
                    // var existRole='';
                    if(data.data.user.userRoles !=null ){
                        $.each(data.data.user.userRoles, function (index, item) {
                            existRole.push(item.roleId);
                            // existRole+=item.roleId+',';
                        });
                    }
                    $("#roleIds").val("");
                    $("#id").val(data.data.user.id==null?'':data.data.user.id);
                    $("#version").val(data.data.user.version==null?'':data.data.user.version);
                    $("#bsCode").val(data.data.user.bsCode==null?'':data.data.user.bsCode);
                    $("#bsName").val(data.data.user.bsName==null?'':data.data.user.bsName);
                    $("#mobile").val(data.data.user.mobile==null?'':data.data.user.mobile);
                    $("#email").val(data.data.user.email==null?'':data.data.user.email);
                    //显示角色数据
                    $("#roleDiv").empty();
                    $.each(data.data.roles, function (index, item) {
                        var roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.bsName+" lay-skin='primary'/>");
                        var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                            "<span>"+item.bsName+"</span><i class='layui-icon'>&#xe626;</i>" +
                            "</div>");
                        if(existRole.length > 0 && existRole.indexOf(item.id)>=0){
                            roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.bsName+" lay-skin='primary' checked='checked'/>");
                            div=$("<div class='layui-unselect layui-form-checkbox  layui-form-checked' lay-skin='primary'>" +
                                "<span>"+item.bsName+"</span><i class='layui-icon'>&#xe627;</i>" +
                                "</div>");
                        }
                        // if(existRole!='' && existRole.indexOf(item.id)>=0){
                        //      roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.bsName+" lay-skin='primary' checked='checked'/>");
                        //      div=$("<div class='layui-unselect layui-form-checkbox  layui-form-checked' lay-skin='primary'>" +
                        //         "<span>"+item.bsName+"</span><i class='layui-icon'>&#xe627;</i>" +
                        //         "</div>");
                        // }
                        $("#roleDiv").append(roleInput).append(div);
                    });
                    openUser(id,"设置用户");
                    //重新渲染下form表单中的复选框 否则复选框选中效果无效
                    // layui.form.render();
                    layui.form.render('checkbox');
                }else{
                    //弹出错误提示
                    layer.alert(data.msg,function () {
                        layer.closeAll();
                    });
                }
            }
        });
    }
}

//删除用户
function delUser(obj,id,name) {
    var currentUser=$("#currentUser").html();
    var version=obj.version;
    if(null!=id){
        if(currentUser==id){
            layer.alert("对不起，您不能执行删除自己的操作！");
        }else{
            layer.confirm('您确定要删除'+name+'用户吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
                $.post(context+"/sysUser/delete",{"id":id},function(data){
                    if(isLogin(data)){
                        if(data.result==true){
                            //回调弹框
                            layer.alert("删除成功！",function(){
                                layer.closeAll();
                                //加载load方法
                                loadAll();
                            });
                        }else{
                            layer.alert(data,function(){
                                layer.closeAll();
                            });
                        }
                    }
                });
            }, function(){
                layer.closeAll();
            });
        }
    }
}
function recoverUser(obj,id) {
    //console.log("需要恢复的用户id="+id);
    var version=obj.version;
    //console.log("delUser版本:"+version);
    if(null!=id){
        layer.confirm('您确定要恢复'+name+'用户吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post("/user/recoverUser",{"id":id,"version":version},function(data){
                if(isLogin(data)){
                    if(data=="ok"){
                        //回调弹框
                        layer.alert("恢复成功！",function(){
                            layer.closeAll();
                            //加载load方法
                        });
                    }else{
                        layer.alert(data,function(){
                            layer.closeAll();
                        });
                    }
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
}
// //解锁用户
// function nolockUser(){
//     //TODO 给个输入框，让用户管理员输入需要解锁的用户手机号，进行解锁操作即可
//     layer.alert("TODO");
// }

//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            userName:obj.field.nameSearch, mobile:obj.field.mobileSearch,
            createdTimeStart:obj.field.createdTimeStart, createdTimeEnd:obj.field.createdTimeEnd
        }
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

//重新加载表格（全部）
function loadAll(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

//清空新增表单数据
function cleanUser(){
    $("#id").val("");
    $("#bsName").val("");
    $("#mobile").val("");
    $("#email").val("");
    $("#bsPassword").val("");
}

//清空修改密码表单数据
function cleanPassword(){
    $("#userId").val("");
    $("#password").val("");
    $("#rePassword").val("");
}