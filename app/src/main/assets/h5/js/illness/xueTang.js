window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	console.log("id="+id);
	DrawData(id)

}

var id = "";
var token = "";
var mvalue = "";//测量时段


function DrawData(id) {

	$.ajax({
		url: myUrl+"infowrite_bloodhistory",
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
			var avalue = [];
			var bvalue = [];
			var cvalue = [];
			var dvalue = [];
			var evalue = [];
			var fvalue = [];
			var gvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.value7 == null){
						gvalue.push(null);	
					}else{
						gvalue.push(parseInt(e.value7));	
					}
					if(e.value6 == null){
						fvalue.push(null);
					}else{
						fvalue.push(parseInt(e.value6));
					}
					
					if(e.value5 == null){
						evalue.push(null);
					}else{
						evalue.push(parseInt(e.value5));
					}
					
					if(e.value4 == null){
						dvalue.push(null);
					}else{
						dvalue.push(parseInt(e.value4));
					}
					if(e.value3 == null){
						cvalue.push(null);
					}else{
						cvalue.push(parseInt(e.value3));
					}
					if(e.value2 == null){
						bvalue.push(null);
					}else{
						bvalue.push(parseInt(e.value2));
					}
					if(e.value1 == null){
						avalue.push(null);
					}else{
						avalue.push(parseInt(e.value1));
					}
					
					
			});
			console.log(gvalue)
			console.log(bvalue)
			$('#DataImgDraw').highcharts({
				chart: {
					type: 'line',
					margin: [2, 2, 40, 30],
					fontSize:10,
					fontColor:'#ffffff',
					backgroundColor: '#498EDB',
					
					
				},
				legend: {
//		            layout: 'vertical',
		            floating: true,
		            align: 'center',
		            verticalAlign: 'top',
		            x: 10,
		            y: -20,
		            symbolPadding: 3, 
		            marginLeft:5,
		            symbolWidth: 1,
		            symbolHeight:1,
	             	itemStyle: {
		                color: 'white',
		                fontWeight: '',
		                fontSize:10
		            }
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
		                    fontSize:12
		                }
		            },
		            tickInterval: 10,
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
					name:"早餐前",
					data:avalue,
					color:'#c36f2e'
				},{
					name:"早餐后",
					data:bvalue,
					color:'#f23c13'
				},{
					name:"午餐前",
					data:cvalue,
					color:'#e7739a'
				},{
					name:"午餐后",
					data:dvalue,
					color:'#e8e5d2'
				},{
					name:"晚餐前",
					data:evalue,
					color:'#fee554'
				},{
					name:"晚餐后",
					data:fvalue,
					color:'#009714'
				},{
					name:"晚上三点",
					data:gvalue,
					color:'#ff243d'
				}]
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


function saveData() {
	var Temp = document.getElementById("Temp");
	var Tvalue = document.getElementById("TempTime").getAttribute('val');
	if(Temp.value.length == 0 || Number(Temp.value)<0 || Number(Temp.value)>27.7){
		Temp.value = "";
		Temp.setAttribute('placeholder','请输入正确的血糖值');
		return false;
	}else if( mvalue.length == 0){
		return false;
	}else if(Tvalue.length == 0){
		document.getElementById("TempTime").innerHTML = "<span style='color:#777777'>请选择时间</span>";
		return false;
	}else{ 
		
		var TempValue = Number(Temp.value).toFixed(2)
		$.ajax({
			type:"post",
			url:myUrl+"infowrite_bloodinfo",
			data:{
				'token':token,
				'userid':id,
				'numone':TempValue,
				'time':mvalue,
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
var flag = 0; //0关闭；1开启；
function showSelectList() {
	var selectIcon = document.getElementById("selectIcon");
	selectIcon.removeAttribute('class');
	if (flag == 0) {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowup");
		openList();
	} else {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowdown");
		closeList()
	}
}


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
	showSelectList()
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
	window.location.href = "History.html?id=xuetang";
}