window.onload = function(){
	id = bridge.getUserId();
	token = bridge.getToken();
	
//	console.log("id="+id);
	DrawData()

}

var id = "";
var token = "";

function DrawData() {

	$.ajax({
		url: myUrl+"infowrite_twovaluehistory",
		type:"post",
		data:{
			'userid':id,
			'num':0,
			'flag':0,//0：一周记录
			'token':token,
			'patient':id

	
		},
		success: function(m) {
			var result = eval("("+m+")");
			var date = [];
			var data = {};
			var mvalue = [];
			var nvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					mvalue.push(parseInt(e.valueone));
					nvalue.push(parseInt(e.valuetwo));
			});
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
		            tickInterval: 8,
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
					    }
					  }
				},
				series: [{
						color:'#ffffff',
			            name: '高压',
			            data: mvalue
			        },{
			        	color:'#fe8f8f',
			            name: '低压',
			            data: nvalue
			        },
				]
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
	var reg = /^[1-9]+[0-9]*([.]{1}[0-9]{1,2})?$|^[0-9]{1}[.]{1}[0]*[1-9]+[0]*$|^0$/g;
	var reg2 = /^[1-9]+[0-9]*([.]{1}[0-9]{1,2})?$|^[0-9]{1}[.]{1}[0]*[1-9]+[0]*$|^0$/g;
	var Hpre = document.getElementById("Hpre");
	var Dpre = document.getElementById("Dpre");
	var GetTime = document.getElementById("TempTime");
	var Hvalue = Hpre.value;
	var Dvalue = Dpre.value;
	var Hflag = reg.test(Hvalue);
	var Dflag = reg2.test(Dvalue);
	var Tvalue = GetTime.getAttribute('val');
	if(Hvalue.length == 0){
		Hpre.value = "";
		Hpre.setAttribute('placeholder','请输入高压值');
		return false;
	}else if(Hflag !== true){
		Hpre.value = "";
		Hpre.setAttribute('placeholder','请输入正确的高压值');
		return false;
	}else if(Dvalue.length == 0){
		Dpre.value = "";
		Dpre.setAttribute('placeholder','请输入低压值');
		return false;
	}else if(Dflag !== true){
		Dpre.value = "";
		Dpre.setAttribute('placeholder','请输入正确的低压值');
		return false;
	}else if(Tvalue.length == 0){
		GetTime.innerHTML = "<span style='color:#777777'>请选择时间</span>";
		return false;
	}else{ 
		$.ajax({
			type:"post",
			url:myUrl+"infowrite_twovaluewrite",
			data:{
				'userid':id,
				'numone':Hvalue,
				'numtwo':Dvalue,
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
	window.location.href = "History.html?id=xueya";
}
