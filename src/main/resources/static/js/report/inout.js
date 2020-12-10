/**
 * 投入产出
 */
var pageCurr;
$(function() {
    layui.use(['table', 'laydate'], function(){
        var table = layui.table
            ,form = layui.form,element = layui.element,laydate = layui.laydate;

        var sdate = getNextDate(-1);
        var edate = getCurDate(0);

        tableIns=table.render({
            elem: '#unitList'
            ,url:context+'/report/getCom1List'
            ,method: 'get' //默认：get请求
            ,where:{prc_name:'APP_BANCHENGPEING_INOUT',
            	pname:_pname,
            	stime:sdate,
                etime:edate}
            ,cellMinWidth: 80
            ,limit:20
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
            cols: [[
                {type:'numbers'}
                ,{field:'TASK_NO', title:'生产工单', width:150, align:'center'}
                ,{field:'BOARD_ITEM', title:'产品编码', width:190, align:'center'}
                ,{field:'BOARD_NAME', title:'产品名称', width:100, align:'center'}
                ,{field:'PROC_NO', title:'工序',width:150, align:'center'}
                ,{field:'LINE_NAME', title:'工序名称', width:180, align:'center'}
                ,{field:'TR', title:'投入数量', width:100, align:'center'}
                ,{field:'CC', title:'产出数量',width:100, align:'center'}
                ,{field:'UNIT', title:'单位', width:80, align:'center'}
                ,{field:'RQ', title:'日期', width:120, align:'center'}
                ,{fixed:'right', title:'操作',width:70,align:'center', toolbar:'#optBar'}
            ]]
            ,  done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                pageCurr=curr;
            }
        });
      //监听工具条
        table.on('tool(unitTable)', function(obj){
        	console.log($('#test-laydate-end').val())
            var data = obj.data;
            if(obj.event === 'detail'){
            	var url = context+'/report/toInoutDetail?stime='+data.RQ+" 00:00:00"+'&p='+_pname+'&etime='+data.RQ+" 00:00:00"+'&task_no='+data.TASK_NO;
            	var index = layer.open({
                    type: 2,
                    title:'明细',
                    area: ['600px', '500px'],
                    fixed: false,
                    maxmin: true,
                    content: url,
                    success: function (layero, index) {
                    	 // 获取子页面的iframe
                        //var iframe = window['layui-layer-iframe' + index];
                        //iframe.child(data.BARCODE,'FR');
                    }
                  });
            	layer.full(index);	
            } 
        });
        //监听搜索框
        form.on('submit(doSearch)', function(data){
            //alert(data.elem.getAttribute('data-url'));
            //重新加载table
            load(data);
            return false;
        });

         //开始日期
         var insStart = laydate.render({
             elem: '#test-laydate-start'
             ,type: 'datetime'
             ,format: 'yyyy-MM-dd HH:mm:ss'
            	 ,value:sdate
             ,done: function(value, date){
                 //更新结束日期的最小日期
 //            insEnd.config.min = lay.extend({}, date, {
 //              month: date.month - 1
 //            });

                 //自动弹出结束日期的选择器
                 insEnd.config.elem[0].focus();
             }
         });

         //结束日期
         var insEnd = laydate.render({
             elem: '#test-laydate-end'
             ,type: 'datetime'
             ,format: 'yyyy-MM-dd HH:mm:ss'
            ,value: edate
             ,done: function(value, date){
                 //更新开始日期的最大日期
 //            insStart.config.max = lay.extend({}, date, {
 //              month: date.month - 1
 //            });
             }
         });
//
//         //监听工具条
//         table.on('tool(unitTable)', function(obj){
//             var data = obj.data;
//             if(obj.event === 'approval'){
//                 //审核
//                 layer.alert('审核',function(){
//                     layer.closeAll();
//                 });
//             } else if(obj.event === 'edit'){
//                 //编辑
//                 editWork(data,data.id);
//             } else if(obj.event === 'file'){
//                 //附件
//                 layer.alert('附件',function(){
//                     layer.closeAll();
//                 });
//             } else if(obj.event === 'del'){
//                 //删除
//                 // delUser(data,data.id,data.bsName);
//                 delWork(data,data.id,data.bsName);
//             }
//         });

    });
});


//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
        	prc_name:'APP_BANCHENGPEING_INOUT',pname:_pname,
        	keyword:obj.field.keyword,
            stime:obj.field.stime,
            etime:obj.field.etime
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

function indexOf(arr, str){
    // 如果可以的话，调用原生方法
    if(arr && arr.indexOf){
        return arr.indexOf(str);
    }

    var len = arr.length;
    for(var i = 0; i < len; i++){
        // 定位该元素位置
        if(arr[i] == str){
            return i;
        }
    }

    // 数组中不存在该元素
    return -1;
}


