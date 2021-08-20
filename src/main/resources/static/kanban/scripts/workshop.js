//车间看板
var interval_do=null;//页面刷新定时器
clearInterval(interval_do);

var MyMarhq = '';//滚动定时器
clearInterval(MyMarhq);

$(function() {
	doData()
	interval_do = setInterval(getKanbanData,60 * 1000); // 启动,执行默认方法
})

function doData(){
	tableBox()
	box2()
	box4()
}

function tableBox(){
	var dataList = dataType1.data
	var Items = dataList[2]
	
	$('.tbl-body tbody').empty();
	$('.tbl-header tbody').empty();
	var str = '';
	$.each(Items,function (i, item) {
	    str = '<tr>'+
	        '<td>'+isNull(item.TASK_NO)+'</td>'+
	        '<td>'+isNull(item.MODEL)+'</td>'+
	        '<td>'+isNull(item.PLAN_QTY)+'</td>'+
	        '<td>'+isNull(item.TRQTY)+'</td>'+
	        '<td>'+isNull(item.COMPLETE_QTY)+'</td>'+
	        '<td>'+isNull(item.PFY)+'</td>'+
	        '<td>'+isNull(item.PASS_PERCENT)+'</td>'+
	        '<td>'+isNull(item.UPH)+'</td>'+
	        '<td>'+isNull(item.PRODUCE_STATE)+'</td>'+
	        '</tr>'

	    $('.tbl-body tbody').append(str);
	    $('.tbl-header tbody').append(str);
	});
	
	if (MyMarhq != null) {// 判断计时器是否为空-关闭
		clearInterval(MyMarhq);
		MyMarhq = null;
	}
	
	if(Items.length > 8){
	    $('.tbl-body tbody').html($('.tbl-body tbody').html()+$('.tbl-body tbody').html());
	    $('.tbl-body').css('top', '0');
	    var tblTop = 0;
	    var speedhq = 50; // 数值越大越慢
	    var outerHeight = $('.tbl-body tbody').find("tr").outerHeight();
	    function Marqueehq(){
	        if(tblTop <= -outerHeight*Items.length){
	            tblTop = 0;
	        } else {
	            tblTop -= 1;
	        }
	        $('.tbl-body').css('top', tblTop+'px');
	    }

	    MyMarhq = setInterval(Marqueehq,speedhq);

	    // 鼠标移上去取消事件
	    $(".tbl-header tbody").hover(function (){
	        clearInterval(MyMarhq);
	    },function (){
	        clearInterval(MyMarhq);
	        MyMarhq = setInterval(Marqueehq,speedhq);
	    })
	}
}

function box2(){
	var dataList = dataType1.data//原始数据
	dataList=dataList[2]

	var xAxis=[];
	var yAxis1=[];//计划数量
	var yAxis2=[];//完成数量

	$.each(dataList,function (i, item) {
		xAxis.push(item.TASK_NO)
		yAxis1.push(item.PLAN_QTY)
		yAxis2.push(item.COMPLETE_QTY)
	})

	var dom = document.getElementById("box2");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	var posList = [
	    'left', 'right', 'top', 'bottom',
	    'inside',
	    'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
	    'insideTopLeft', 'insideTopRight', 'insideBottomLeft', 'insideBottomRight'
	];

	app.configParameters = {
	    rotate: {
	        min: -90,
	        max: 90
	    },
	    align: {
	        options: {
	            left: 'left',
	            center: 'center',
	            right: 'right'
	        }
	    },
	    verticalAlign: {
	        options: {
	            top: 'top',
	            middle: 'middle',
	            bottom: 'bottom'
	        }
	    },
	    position: {
	        options: echarts.util.reduce(posList, function (map, pos) {
	            map[pos] = pos;
	            return map;
	        }, {})
	    },
	    distance: {
	        min: 0,
	        max: 100
	    }
	};

	app.config = {
	    rotate: 90,
	    align: 'left',
	    verticalAlign: 'middle',
	    position: 'insideBottom',
	    distance: 15,
	    onChange: function () {
	        var labelOption = {
	            normal: {
	                rotate: app.config.rotate,
	                align: app.config.align,
	                verticalAlign: app.config.verticalAlign,
	                position: app.config.position,
	                distance: app.config.distance
	            }
	        };
	    }
	};


	var labelOption = {
	    normal: {
	        show: true,
	        position: app.config.position,
	        distance: app.config.distance,
	        align: app.config.align,
	        verticalAlign: app.config.verticalAlign,
	        rotate: app.config.rotate,
	        //formatter: '{c}  {name|{a}}',
	        formatter: '{c}',
	        fontSize: 10,
	        rich: {
	            name: {
	                textBorderColor: '#fff'
	            }
	        }
	    }
	};

	option = {
	    color: ['#00fff6', '#006699', '#4cabce', '#e5323e'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    
	    legend: {
	    	textStyle:{//图例文字的样式
	                color:'#dbdbdb',
	                fontSize:10
	           },
	        data: ['计划数量', '完成数量']
	    },
	    textStyle:{//图例文字的样式
	                color:'#dbdbdb',
	                fontSize:14
	           },
	    calculable: true,
	    xAxis: [
	        {
	            type: 'category',
	            axisTick: {show: false},
	            data: xAxis
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: '计划数量',
	            type: 'bar',
	            barGap: 0,
	            label: labelOption,
	            data: yAxis1
	        },
	        {
	            name: '完成数量',
	            type: 'bar',
	            label: labelOption,
	            data: yAxis2
	        }
	    ]
	};;
	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}

function box4(){
	var dataList = dataType2.data//原始数据
	dataList=dataList[2]
	
	var nameList=[];
	var valueList=[];
	var countList=[];

	if(dataList.length==0){//无数据时显示整圆
		nameList.push('None:0');
		valueList.push({
			value:0,
			name:'None:0',
			itemStyle:{
				normal:{
					color:'rgb(0,100,0)'
				}
			}
		})
	}else{
		$.each(dataList,function (i, item) {
			nameList.push(item.DEFECT_NAME)
			countList.push(item.DEFCODE_QTY)
			var arr={}
			arr['value']=item.DEFCODE_QTY
			arr['name']=item.DEFECT_NAME;

			valueList.push(arr)
		})
	}

	var dom = document.getElementById("box4");
	var myChart = echarts.init(dom);
	var app = {};
	option = null;
	option = {
		    tooltip: {
		        trigger: 'item',
		        formatter: '{a} <br/>{b} : {c} ({d}%)'
		    },
		    legend: {
		    	type: 'scroll',
		        orient: 'vertical',
		        right: 40,
		        top: 0,
		        bottom: 20,
		        data: nameList,
		        textStyle:{//图例文字的样式
	                color:'#dbdbdb',
	                fontSize:14
	           },
	           formatter: function(name) {
                   var index = 0;
                   var clientlabels = nameList;
                   var clientcounts = countList;
                   if(dataList==0){
                	   return 'None:0'
                   }
                   clientlabels.forEach(function(value,i){
                       if(value == name){
                           index = i;
                       }
                   });
                   return name + " : " + clientcounts[index];
               }

		    },
		    series: [
		        {
		            type: 'pie',
		            radius: '65%',
		            center: ['45%', '50%'],
		            selectedMode: 'single',
		            data: valueList,
		            emphasis: {
		                itemStyle: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};

	if (option && typeof option === "object") {
	    myChart.setOption(option, true);
	}
}

function getKanbanData(){
	getDataType("1","1");
	getDataType("1","2");
}
function getDataType(kanbanNo,dataType){
	var params = {
			"kanbanNo" : kanbanNo,
			"dataType" : dataType
		};
		$.ajax({
			type : "GET",
			url : context + "kanban/getKanbanList",
			data : params,
			dataType : "json",
			success : function(res) {
				console.log(res)
				if (res.result) {
					
					if(dataType=="1"){
						dataType1=res
					}else if(dataType=="2"){
						dataType2=res
					}
					doData()
					
				} else {
					//clearInterval(interval_do);//错误-关闭定时器
				}
			}
		});
}


function isNull(str){
	if(str==null){
		return ""
	}else{
		return str
	}
}