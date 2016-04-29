window.onload = function(){
	id = bridge.getUserId();
	token = bridge.getToken();
//	id=48;
//	token="";
	
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
			console.log(m)
			var date = ["","","","","","","",""];
			var data = {};
			var mvalue = [];
			var mval1 = [];
			var mval2 = [];
			var nvalue = [];
			var nval1 = [];
			var nval2 = [];
			var dataArr = result.dataArray;
			var c = 0;
			var d = 0;
			 

			var Ndate = "";
			$.each(dataArr, function(i,k) {
				if(c == 0){
					date[5] = i;
					c++;
				}else{
					date[1] = i;
				}
				$.each(k, function(j,l) { 
					var o = 0;
					
					$.each(l, function(f,h) {
						if(o == 0){
							if(d == 0){
								if(h == null){
									mval2.push(null);
								}else{
									mval2.push(Number(h));
								}
							}else{
								if(h == null){
									mval1.push(null);
								}else{
									mval1.push(Number(h));
								}
							}
							o++;
						}else{
							if(d == 0){
								if(h == null){
									nval2.push(null);
								}else{
									nval2.push(Number(h));
								}
							}else{
								if(h == null){
									nval1.push(null);
								}else{
									nval1.push(Number(h));
								}
							}
							
						}
					});					
				});
				d++;

			}); 
			
			mval1 = mval1.concat(mval2);
			nval1 = nval1.concat(nval2);
			console.log(mval1.length)
			console.log(nvalue)
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
//		            tickmarkPlacement: 'on',
				 	plotLines:[{
				        color:'white',            //线的颜色，定义为红色
				        dashStyle:'longdashdot',//标示线的样式，默认是solid（实线），这里定义为长虚线
				        value:3.5,                //定义在哪个值上显示标示线，这里是在x轴上刻度为3的值处垂直化一条线
				        width:1                 //标示线的宽度，2px
				    }]

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
					    },
					    connectNulls: true
					  }
				},
				series: [{
						color:'#ffffff',
			            name: '高压',
			            data: mval1
			        },{
			        	color:'#fe8f8f',
			            name: '低压',
			            data: nval1
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
	var Hpre = document.getElementById("Hpre");
	var Dpre = document.getElementById("Dpre");
	var Tvalue = document.getElementById("TempTime").getAttribute('val');
	if(Hpre.value.length == 0 || Number(Hpre.value)>200 || Number(Hpre.value)<80){
		Hpre.value = "";
		Hpre.setAttribute('placeholder','请输入正确的高压值');
		return false;
	}else if(Dpre.value.length == 0 || Number(Dpre.value)>110 || Number(Dpre.value)<40){
		Dpre.value = "";
		Dpre.setAttribute('placeholder','请输入正确的低压值');
		return false;
	}else if(Tvalue.length == 0){
		document.getElementById("TempTime").innerHTML = "<span style='color:#777777'>请选择时间</span>";
		return false;
	}else{ 
		
		var Hvalue = Number(Hpre.value).toFixed(2);
		var Dvalue = Number(Dpre.value).toFixed(2);
		
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
