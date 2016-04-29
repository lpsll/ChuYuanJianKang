window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
//	id = 48;
//	token = "";
	console.log("id="+id);
	DrawData(id)
}
var id = "";
var token = ""; 
function DrawData(id) {

	$.ajax({ 
		url:  myUrl+"infowrite_sleephistory",
		type:"post",
		data:{
			'patient':id,
			'token':token,
			'userid':id,
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
						mvalue.push(Number(e.valueone));	
					}
								
			});
			data.name = "睡眠";
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
		            tickInterval: 2,
		            endOnTick: false,
           			maxPadding: 0.3
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


var TimeFlag = 0;
//调用安卓选择日期方法
function getAndroidDate(a){
	bridge.jsInvokeAndroid();
	bridge.textTime();
	
	if(a == 1){
		TimeFlag = 1;
	}else{
		TimeFlag = 2;		
	}
	
	SetDateValue(AndrDate)
}
//获取安卓选择日期写入h5测量时间输入框
function SetDateValue(AndrDate){
	if(TimeFlag == 1){
		var TempTime = document.getElementById("SleepTime");
		TempTime.innerHTML = AndrDate;
		TempTime.setAttribute('val',AndrDate);
	}else{
		var TempTime = document.getElementById("GetUpTime");
		TempTime.innerHTML = AndrDate;
		TempTime.setAttribute('val',AndrDate);
	}
	
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
	
	var SleepTime = document.getElementById("SleepTime");
	var GetUpTime = document.getElementById("GetUpTime");

	var Svalue = SleepTime.getAttribute('val');
	var Gvalue = GetUpTime.getAttribute('val');
	

	var timestamp1 = Date.parse(new Date(Svalue));
	var timestamp2 = Date.parse(new Date(Gvalue));
	
	if(Svalue.length == 0){
		SleepTime.innerHTML = "<span style='color:#777777'>请选择时间</span>";
		return false;
	}else if(GetUpTime.length == 0){
		GetUpTime.innerHTML = "<span style='color:#777777'>请选择时间</span>";
		return false;
	}else if(mvalue.length == 0){
		return false;
	}else if(timestamp1>timestamp2){
		SleepTime.innerHTML = "<span style='color:#777777'>请选择正确的睡觉时间</span>";
		GetUpTime.innerHTML = "<span style='color:#777777'>请选择正确的起床时间</span>";
	}else{
		$.ajax({
			type:"post",
			url:myUrl+"infowrite_sleepinfo",
			data:{
				'token':token,
				'userid':id,
				'sleep':Svalue,
				'getup':Gvalue,
				'type':mvalue
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






var flag = 0; //0关闭；1开启；
function showSelectList() {
	var selectIcon = document.getElementById("selectIcon");
	selectIcon.removeAttribute('class');
	if (flag == 0) {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowup");
		openList();
		
	} else {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowdown");
		closeList();
	}
}

var mvalue = "";
function getSelectValue(thisa) {
	var radionum = document.getElementsByName("gender");

	for (var i = 0; i < radionum.length; i++) {

		if (radionum[i].checked) {

			mvalue = radionum[i].value;
			var selectText = radionum[i].getAttribute("mText");
			
			var TitleText = document.getElementById("TitleText");
			
			TitleText.innerHTML = selectText;

		}
	}

	flag = 1;
	showSelectList();
}

function openList() {
	var selectMenu = document.getElementById("selectMenu");
	selectMenu.style.display = "block";
	flag = 1;
}

function closeList() {
	var selectMenu = document.getElementById("selectMenu");
	selectMenu.style.display = "none";
	flag = 0;
}

function checkHistory() {
	window.location.href = "History.html?id=shuimian";
}