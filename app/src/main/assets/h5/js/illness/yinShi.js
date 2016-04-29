window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
//	id = 41
//	token = ""
	console.log("id="+id);
	DrawData(id)
}

var id = "";
var token = ""; 
function DrawData(id) { 

	$.ajax({
		url: myUrl+"infowrite_eathistory",
		type:"post",
		data:{
			'patient':id,
			'token':token,
			'userid':id,
			'num':0,
			'flag':0
	
		},
		success: function(m) {
			console.log(m)
			var result = eval("("+m+")");
			var date = []; 
			var data = {};
			var mvalue = [];
			var nvalue = [];
			var dataArr = result.dataArray.reverse();
			dataArr.forEach(function(e) {
					date.push(e.date);
					if(e.numericOne == null){
						mvalue.push(null);
					}else{
						mvalue.push(Number(e.numericOne));
					}
					if(e.numericTwo == null){
						nvalue.push(null);
					}else{
						nvalue.push(Number(e.numericTwo));
					}
					
			});
//			data.name = "饮食";
			console.log(mvalue);
			console.log(nvalue);
			$('#DataImgDraw').highcharts({
				chart: {
					type: 'line',
					margin: [6, 2, 50, 30],
					fontSize:10,
					fontColor:'red',
					backgroundColor: '#498EDB',
					spacingBottom: 100
					
				},
				legend: {
		            enabled: false,
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
		                    fontSize:8
		                }
		            },
		            tickInterval: 1,
		            endOnTick: false,
           			maxPadding: 0.8
				},
				plotOptions: {
					line: {
						dataLabels: {
							enabled: true ,
							color:"#ffffff",
							 formatter: function () {
//		                        return  '咸';
		                        if(this.y == 1){
		                        	return '无'
		                        }else if(this.y == 2){
		                        	return '油腻'
		                        }else if(this.y == 3){
		                        	return '一般'
		                        }else if(this.y == 4){
		                        	return '清淡'
		                        }
		                    }
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
					},
//				  	columnrange: {
//		                dataLabels: {
//		                    enabled: true,
//		                    formatter: function () {
//		                        return  '咸';
//		                    }
//		                }
//		            }
				},
				series: [{
					color:'#ffffff',
					name:'饮食',
					data:mvalue
				
				}]
			});


		},
		error:function(e){
			getPopInfo("网络错误");
		}
	});

}

function saveData() {
	
	if(mvalue.length == 0){

		return false;
	}else if(nvalue.length == 0){

		return false;
	}else {
		$.ajax({
			type:"post",
			url:myUrl+"infowrite_eatinfo",
			data:{
				'token':token,
				'userid':id,
				'numone':mvalue,
				'numtwo':nvalue
			},
			success:function(m){
				var result = eval("("+m+")");
				if(result.code == 1){
					getPopInfo("保存成功");
					if(canBack){
						AndroidBack()
					}else{
						DrawData(id)
					}
				}else{
					getPopInfo(result.msg);
				}
			},
			error:function(e){
				getPopInfo("网络错误");
			}
		});
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
var mflag = 0;//0关闭；1开启；
function showSelectList2() {
	var selectIcon = document.getElementById("selectIcon2");
	selectIcon.removeAttribute('class');
	if (mflag == 0) {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowup");
		openList2();

	} else {
		selectIcon.setAttribute('class', "HistBtnIcon mui-icon mui-icon-arrowdown");
		closeList2();
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

var nvalue = "";
function getSelectValue2(thisa) {
	var radionum = document.getElementsByName("gender2");

	for (var i = 0; i < radionum.length; i++) {

		if (radionum[i].checked) {

			nvalue = radionum[i].value;
			var selectText = radionum[i].getAttribute("mText");
			
			var TitleText = document.getElementById("TitleText2");
			
			TitleText.innerHTML = selectText;

		}
	}
	closeList2();
	mflag = 1;
	showSelectList2();
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

function openList2() {
	var selectMenu = document.getElementById("selectMenu2");
	selectMenu.style.display = "block";
	mflag = 1;
}

function closeList2() {
	var selectMenu = document.getElementById("selectMenu2");
	selectMenu.style.display = "none";
	mflag = 0;
}


function checkHistory() {
	window.location.href = "History.html?id=yinshi";
}