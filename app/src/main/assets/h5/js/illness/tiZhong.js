window.onload = function(){
	id = bridge.getUserId();
	token = bridge.getToken();
//	id = 48;
//	token =''
	console.log("id="+id);
	DrawData(id)
}
var id = "";
var token = "";

function DrawData(id) {

	$.ajax({
		url: myUrl+"infowrite_infohistory",
		type:"post",
		data:{
			'patient':id,
			'token':token,
			'userid':id,
			'type':5,//5:体重
			'num':0,
			'flag':0//0：一周记录
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
						mvalue.push(parseInt(e.valueone));	
					}
								
			});
			data.name = "体温";
			data.data = mvalue;
			data.color='#ffffff'
			$('#DataImgDraw').highcharts({
				chart: {
					type: 'line',
					margin: [6, 2, 22, 30],
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
		                    fontSize:8
		                }
		            }

				},
				yAxis: {
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
		                    fontSize:8
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


function saveData(){
	var reg = /^[1-9]+[0-9]*([.]{1}[0-9]{1,2})?$|^[0-9]{1}[.]{1}[0]*[1-9]+[0]*$|^0$/g;
	var Temp = document.getElementById("Temp");
	var GetTime = document.getElementById("TempTime");
	var TempValue = Temp.value;
	var Tvalue = GetTime.getAttribute('val');
	var flag = reg.test(TempValue);
	if(TempValue.length == 0){
		Temp.value = "";
		Temp.setAttribute('placeholder','请输入体重值');
		return false;
	}else if(flag !== true){
		Temp.value = "";
		Temp.setAttribute('placeholder','请输入正确的体重值');
		return false;
	}else if(Tvalue.length == 0){
		GetTime.innerHTML = "请选择时间";
		return false;
	}else{ 
		
		$.ajax({
			type:"post",
			url:myUrl+"infowrite_infowrite",
			data:{
				'token':token,
				'userid':id,
				'type':5,
				'numone':TempValue,
				'date':Tvalue
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
	window.location.href = "History.html?id=tizhong";
}
