window.onload = function(){
	id = bridge.getUserId();
	token = bridge.getToken();
//	id = 48;
//	token =""
	console.log("id="+id);
	DrawData(id)
}

var id = "";
var token = "";

function DrawData() {

	$.ajax({
		url: myUrl+"infowrite_infohistory",
		type:"post",
		data:{
			'userid':id,
			'type':7,//7:抽烟
			'num':0,
			'flag':0,//0：一周记录
			'token':token,
			'patient':id

		},
		success: function(m) {
			console.log("success="+m);
			var result = eval("("+m+")");
			var date = [];
			var data = {};
			var mvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.valueone == null){
						mvalue.push(null);	
					}else{
						mvalue.push(Number(e.valueone));	
					}
								
			});
			data.name = "抽烟";
			data.data = mvalue;
			data.color='#ffffff'
			$('#DataImgDraw').highcharts({
				chart: {
					type: 'line',
					margin: [2, 2, 40, 30],
					fontSize:10,
					fontColor:'#ffffff',
					backgroundColor: '#498EDB',
					
					
				},
				legend: {
		            enabled: false
		      	},
				title: {
					text: ''
				},
				xAxis: {
					categories: date,
					lineColor: '#ffffff',
        			lineWidth: 2,
					labels: {
		                style: {
		                    color: 'white',
		                    fontSize:11
		                }
		           },
//		           plotLines:[{
//				        color:'white',            //线的颜色，定义为红色
//				        dashStyle:'longdashdot',//标示线的样式，默认是solid（实线），这里定义为长虚线
//				        value:3.5,                //定义在哪个值上显示标示线，这里是在x轴上刻度为3的值处垂直化一条线
//				        width:1                 //标示线的宽度，2px
//				    }]

				},
				yAxis: {
					allowDecimals: false,
					title: {
						text: ''
					},
					gridLineColor: '#ffffff',
					gridLineWidth:2,
					lineColor: '#ffffff',
        			lineWidth: 2,
        			labels: {
		                style: {
		                    color: 'white',
		                    fontSize:12
		                }
		            },
		            tickInterval: 3,
		            endOnTick: false,
           			maxPadding: 0.5
				},
				plotOptions: {
					line: {
						dataLabels: {
//							enabled: true 
						},
						enableMouseTracking: false
					},
					series: {
					    events: {
					        legendItemClick: function(e) {
					            return false; // 直接 return false 即可禁用图例点击事件
					        }
					    },
					    connectNulls: true
					  }
				},
				series: [data]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});

}


//调用安卓选择日期方法
function getAndroidDate(){
	bridge.jsInvokeAndroid();
	bridge.textTime();
}
//获取安卓选择日期写入h5测量时间输入框
function SetDateValue(AndrDate){
	var TempTime = document.getElementById("TempTime");
	TempTime.innerHTML = AndrDate;
	TempTime.setAttribute('val',AndrDate);
}

//调用安卓弹出提示信息方法
function getPopInfo(msg){
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack(){
	bridge.back();
}



var PopInfo = "";

function saveData(){
	var Temp = document.getElementById("Temp");
	var Tvalue =  document.getElementById("TempTime").getAttribute('val');
	if(Temp.value.length == 0 || Number(Temp.value) <0){
		Temp.value = "";
		Temp.setAttribute('placeholder','请输入正确的根数');
		return false;
	}else if(Tvalue.length == 0){
		 document.getElementById("TempTime").innerHTML = "<span style='color:#777777'>请选择时间</span>";
		return false;
	}else{ 
		
		var TempValue = Number(Temp.value).toFixed(2)
		
		$.ajax({
			type:"post",
			url:myUrl+"infowrite_infowrite",
			data:{
				'userid':id,
				'type':7,
				'numone':TempValue,
				'date':Tvalue,
				'token':token
			},
			success:function(m){
				var result = eval("("+m+")");
				PopInfo = result.msg;
				if(result.code == 1){
					getPopInfo("保存成功");
					if(canBack){
                    	AndroidBack()
                    }else{
                    	DrawData(id)
                    }
				}else{
					getPopInfo(PopInfo);
				}
			},
			error:function(e){
				getPopInfo("网络错误");
			}
		});
	}
}

function checkHistory(){
	window.location.href = "History.html?id=chouyan";
}
