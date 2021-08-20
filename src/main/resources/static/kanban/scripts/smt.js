//iqc待检看板
var interval_do = null;// 页面刷新定时器
clearInterval(interval_do);

var MyMarhq = '';// 滚动定时器
clearInterval(MyMarhq);
var MyMarhq_tip = '';// 滚动定时器
clearInterval(MyMarhq_tip);

$(function() {
	doData()
	//interval_do = setInterval(getKanbanData, 60 * 1000); // 启动,执行默认方法
})

function doData() {
	tableBox1()
	tableBox2()
	setAera()
}

function tableBox1() {
	var Items = KANBAN_DATA.data.DATA_4
	$('#tableList1_1').empty();
	$('#tableList1_2').empty();
	var str = '';
	var style = '';
	var border = ''
	if (Items.length <= 20) {// 不滚动
		$.each(Items, function(i, item) {
			if (i == Items.length - 1) {// 最后一行增加下划线
				border = 'border-bottom: 2px solid #333399;'
			}
			// console.log(Items)
			style = "style='" + border + "'";
			str = '<tr><td ' + style + '>' + isNull(item.站位) + '</td>' + '<td ' + style + '>' + isNull(item.物料编码) + '</td>' + '<td ' + style + '>' + isNull(item.接料总数) + '</td>'
					+ '<td ' + style + '>' + isNull(item.吸取数) + '</td>' + '<td ' + style + '>' + isNull(item.剩余数) + '</td>' + '<td ' + style + '>' + isNull(item.剩余可生产数) + '</td>'
					+ '<td ' + style + '>' + isNull(item.已发未接料数) + '</td></tr>'
			$('#tableList1_1').append(str);
			$('#tableList1_2').append(str);
		});
	} else {// 有滚动效果
		$.each(Items, function(i, item) {
			str = '<tr><td ' + style + '>' + isNull(item.站位) + '</td>' + '<td ' + style + '>' + isNull(item.物料编码) + '</td>' + '<td ' + style + '>' + isNull(item.接料总数) + '</td>'
					+ '<td ' + style + '>' + isNull(item.吸取数) + '</td>' + '<td ' + style + '>' + isNull(item.剩余数) + '</td>' + '<td ' + style + '>' + isNull(item.剩余可生产数) + '</td>'
					+ '<td ' + style + '>' + isNull(item.已发未接料数) + '</td></tr>'
			$('#tableList1_1').append(str);
			$('#tableList1_2').append(str);
		});
	}

	// if (MyMarhq != null) {// 判断计时器是否为空-关闭
	// clearInterval(MyMarhq);
	// MyMarhq = null;
	// }
	//
	// if (Items.length > 20) {
	// $('.tbl-body tbody').html($('.tbl-body tbody').html() + $('.tbl-body
	// tbody').html());
	// $('.tbl-body').css('top', '0');
	// var tblTop = 0;
	// var speedhq = 50; // 数值越大越慢
	// var outerHeight = $('.tbl-body tbody').find("tr").outerHeight();
	// function Marqueehq() {
	// if (tblTop <= -outerHeight * Items.length) {
	// tblTop = 0;
	// } else {
	// tblTop -= 1;
	// }
	// $('.tbl-body').css('top', tblTop + 'px');
	// }
	// MyMarhq = setInterval(Marqueehq, speedhq);
	// // 鼠标移上去取消事件
	// $(".tbl-header tbody").hover(function() {
	// clearInterval(MyMarhq);
	// }, function() {
	// clearInterval(MyMarhq);
	// MyMarhq = setInterval(Marqueehq, speedhq);
	// })
	// }
}

function tableBox2() {
	var Items = KANBAN_DATA.data.DATA_5

	$('#tableList2_1').empty();
	$('#tableList2_2').empty();
	var str = '';
	var style = '';
	var border = ''
	if (Items.length <= 10) {// 不滚动
		$.each(Items, function(i, item) {
			if (i == Items.length - 1) {// 最后一行增加下划线
				border = 'border-bottom: 2px solid #333399;'
			}
			if (item.历时 > 10) {
				style = "style='color:#CC0033;" + border + "'";
			} else {
				style = "style='color:#FFFFFF;" + border + "'";
			}

			str = '<tr><td ' + style + '>' + isNull(item.换料时间) + '</td>' + '<td ' + style + '>' + isNull(item.物料编码) + '</td>' + '<td ' + style + '>' + isNull(item.位置) + '</td>'
					+ '<td ' + style + '>' + isNull(item.已过时间) + '</td></tr>'
			$('#tableList2_1').append(str);
			$('#tableList2_2').append(str);
		});
	} else {// 有滚动效果
		$.each(Items, function(i, item) {
			if (item.历时 > 10) {
				style = "style='color:#CC0033;" + border + "'";
			} else {
				style = "style='color:#FFFFFF;" + border + "'";
			}
			str = '<tr><td ' + style + '>' + isNull(item.换料时间) + '</td>' + '<td ' + style + '>' + isNull(item.物料编码) + '</td>' + '<td ' + style + '>' + isNull(item.位置) + '</td>'
					+ '<td ' + style + '>' + isNull(item.已过时间) + '</td></tr>'
			$('#tableList2_1').append(str);
			$('#tableList2_2').append(str);
		});
	}

	// if (MyMarhq_tip != null) {// 判断计时器是否为空-关闭
	// clearInterval(MyMarhq_tip);
	// MyMarhq_tip = null;
	// }
	//
	// if (Items.length > 10) {
	// $('.tbl-body_tip tbody').html($('.tbl-body_tip tbody').html() +
	// $('.tbl-body_tip tbody').html());
	// $('.tbl-body_tip').css('top', '0');
	// var tblTop = 0;
	// var speedhq = 50; // 数值越大越慢
	// var outerHeight = $('.tbl-body_tip tbody').find("tr").outerHeight();
	// function Marqueehq() {
	// if (tblTop <= -outerHeight * Items.length) {
	// tblTop = 0;
	// } else {
	// tblTop -= 1;
	// }
	// $('.tbl-body_tip').css('top', tblTop + 'px');
	// }
	//
	// MyMarhq_tip = setInterval(Marqueehq, speedhq);
	//
	// // 鼠标移上去取消事件
	// $(".tbl-header_tip tbody").hover(function() {
	// clearInterval(MyMarhq_tip);
	// }, function() {
	// clearInterval(MyMarhq_tip);
	// MyMarhq_tip = setInterval(Marqueehq, speedhq);
	// })
	// }
}

function setAera() {
	setData1()
	chartDiv1()
	chartDiv2()

}

function setData1() {
	var dataValue = KANBAN_DATA.data.DATA_1[0]
	$("#prodLine").text("生产线体 ：" + isNull(dataValue.生产线体))
	$("#taskNo").text("工单号 ：" + isNull(dataValue.工单号))
	$("#prodNo").text("产品编码 ：" + isNull(dataValue.产品编码))
	$("#prodName").text("产品名称 ：" + isNull(dataValue.产品名称))

	$("#zhitong").text("直通率 ：" + isNull(dataValue.直通率))
	$("#stProd").text("标准产能 ：" + isNull(dataValue.标准产能))
	$("#fone").text("一轨 ：" + isNull(dataValue.一轨))
	$("#ftwo").text("二轨 ：" + isNull(dataValue.二轨))
	$("#progress").text("生产进度：" + isNull(dataValue.生产进度))

	var pVlaue = dataValue.生产进度
	var pMax = pVlaue.substring(pVlaue.indexOf("/") + 1, pVlaue.length)
	pVlaue = pVlaue.substring(0, pVlaue.indexOf("/"))
	$('#prog').attr("value", pVlaue);
	$('#prog').attr("max", pMax);
}

function chartDiv1() {
	var listData = KANBAN_DATA.data.DATA_2
	// console.log(listData)
	var xData = [];// 日期
	var yData = [];
	var tData = [];
	var seriesData = [];
	var stData = 0;
	var maxValue = 0;
	if (listData.length>0) {
		stData = Number(listData[0].标产);
		maxValue = stData
		for (var i = 0; i < listData.length; i++) {// 获取x轴参数，项目参数
			var lData = listData[i];
			xData.push(lData.时间段)
			tData.push(lData.轨道)
			if (Number(lData.产能) > maxValue) {
				maxValue = lData.产能
			}
		}
		xData = unique(xData)// 时间段去重
		tData = unique(tData)// 轨道去重
		for (var i = 0; i < tData.length; i++) {// 项目
			var arr = []
			yData[tData[i]] = arr
		}
		for (var i = 0; i < tData.length; i++) {// 项目
			for (var j = 0; j < listData.length; j++) {// 数据
				if (tData[i] == listData[j].轨道) {
					for (var q = 0; q < xData.length; q++) {// 日期
						if (xData[q] == listData[j].时间段) {
							var lData = listData[j].产能;
							yData[tData[i]].push(isNull(lData))
						}
					}
				}
			}
		}
		for (var i = 0; i < xData.length; i++) {// 时间去掉前端的日期
			xData[i] = xData[i].substring(xData[i].indexOf(" ") + 1, xData[i].length);
		}
	}

	for (var i = 0; i < tData.length; i++) {// 写成图表
		if (i == 0) {
			var arr = {
				name : tData[i],
				type : 'bar',
				data : yData[tData[i]],
				yAxisIndex : 0,
				label : {
					show : true,
					position : 'inside',
					formatter : '{c}',
				},
				markLine : {
					symbol : "none",
					data : [ {
						silent : true, // 鼠标悬停事件 true没有，false有
						label : {
							formatter : '标产',
							show : true,
						},
						lineStyle : { // 警戒线的样式 ，虚实 颜色
							type : "solid",
							color : "#FFFF66",
						},
						yAxis : stData
					// 警戒线的标注值，可以有多个yAxis,多条警示线
					} ]
				}
			}
			seriesData.push(arr)
		} else {
			var arr = {
				name : tData[i],
				type : 'bar',
				data : yData[tData[i]],
				yAxisIndex : 0,
				label : {
					show : true,
					position : 'inside',
					formatter : '{c}',
				},
			}
			seriesData.push(arr)
		}
	}

	option = {
		color : [ "#FF9933", "#00CC99" ],
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'cross',
				crossStyle : {
					color : '#999'
				}
			},
		},
		grid : {
			x : 60,// 左边距
			y : 40,// 上边距
			x2 : 40,// 右边距
			y2 : 40,// 下边距
			borderWidth : 0
		},
		legend : {
			// orient: 'vertical',
			x : 'right', // 可设定图例在左、右、居中
			top : 2,
			data : tData,
			textStyle : {
				fontSize : 20,// 字体大小
				color : '#ffffff',
			},
		},
		xAxis : {
			type : 'category',
			data : xData,
			splitLine : {
				show : false
			},
			axisPointer : {
				type : 'shadow'
			},
			axisLabel : {
				show : true,
				textStyle : {
					color : '#ffffff',
					fontSize : 14,
				}
			},
			axisLine : {
				lineStyle : {
					color : '#ffffff',
					fontSize : 14,
				}
			},
		},
		yAxis : {
			type : 'value',
			// name : '收成率(%)',
			max : Number(maxValue) + 10,
			nameTextStyle : {
				fontSize : 14
			},
			splitLine : {
				show : false
			},
			axisLabel : {
				textStyle : {
					color : '#ffffff',
					fontSize : 14,// 字体大小
				}
			},
			axisLine : {
				lineStyle : {
					color : '#ffffff'
				}
			},
		},
		series : seriesData
	};
	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('prodHour'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
}

function chartDiv2() {
	var listData = KANBAN_DATA.data.DATA_3
	var xData = [];
	var yData = [];
	if (listData) {
		for (var i = 0; i < listData.length; i++) {// 获取x参数
			var lData = listData[i];
			xData.push(lData.位置)
			yData.push((Number(lData.抛料率) * 100).toFixed(2))
		}
	}

	option = {
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'cross',
				crossStyle : {
					color : '#999'
				}
			},
			formatter : function(a) {
				var list = []
				var listItem = ''
				list.push('<i style="display: inline-block;width: 10px;height: 10px;background: ' + a[0].color
								+ ';margin-right: 5px;border-radius: 50%;}"></i><span style="width:180px; display:inline-block;">' + a[0].name + '</span><br>&nbsp&nbsp：'
								+ a[0].value + '%')

				listItem = list.join('<br>')
				return '<div class="showBox">' + listItem + '</div>'

			}
		},
		color : [ '#6699FF' ],
		grid : {
			x : 60,// 左边距
			y : 40,// 上边距
			x2 : 30,// 右边距
			y2 : 80,// 下边距
			borderWidth : 0
		},
		xAxis : {
			type : 'category',
			data : xData,
			axisLabel : {
				show : true,
				interval : 0,
				rotate : 30,
				textStyle : {
					color : '#ffffff',
					fontSize : 12,
				},
				margin : 15, // 刻度标签与轴线之间的距离
				formatter : function(value) {
					var ret = "";// 拼接加\n返回的类目项
					var tempStr = value.substring(0, value.indexOf("/") + 1)
					ret = tempStr + "\n" + value.substring(value.indexOf("/") + 1, value.length);
					return ret;
				}
			},
			axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		},
		yAxis : {
			type : 'value',
			name : '%',
			nameTextStyle : {
				fontSize : 14
			},
			splitLine : {
				show : false
			},
			axisLabel : {
				// formatter : '{value} ',
				textStyle : {
					color : '#ffffff',
					fontSize : 14,// 字体大小
				}
			},
			axisLine : {
				lineStyle : {
					color : '#FFFFFF'
				}
			},
		},
		series : [ {
			name : '抛料率',
			data : yData,
			type : 'bar',
			itemStyle : {
				normal : {
					color : function(params) {
						if (params.value > 0.8) {
							return '#FF3333'
						} else {
							return '#6699FF'
						}
					}
				}
			},

		} ]
	};
	// 创建echarts对象在哪个节点上
	var myCharts1 = echarts.init(document.getElementById('paoLiao'));
	// 将选项对象赋值给echarts对象。
	myCharts1.setOption(option, true);
}

function getKanbanData() {
	$.ajax({
		type : "GET",
		url : context + "kanban/getHhSmtnList",
		data : {
			lineNo : LINE_NO
		},
		dataType : "json",
		success : function(res) {
			console.log(res)
			if (res.result) {
				KANBAN_DATA = res
				doData()
			} else {
				// clearInterval(interval_do);//错误-关闭定时器
			}
		}
	});
}

/*******************************************************************************
 * 新建一个空的结果数组，for 循环原数组，判断结果数组是否存在当前元素，如果有相同的值则跳过，不相同则push进数组。
 ******************************************************************************/
function unique(arr) {// 去重
	if (!Array.isArray(arr)) {
		console.log('type error!')
		return false
	}
	var array = [];
	for (var i = 0; i < arr.length; i++) {
		if (array.indexOf(arr[i]) === -1) {
			array.push(arr[i])
		}
	}
	return array;
}

function isNull(str) {
	if (str == null) {
		return ""
	} else {
		return str
	}
}