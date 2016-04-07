window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
	getUrl()
}
var id = "";
var token = "";
var mflag = 0;

function getUrl() {
	var mUrl = location.search;
	var mArr = mUrl.split('?');
	var mValArr = mArr[1].split('=');
	mflag = mValArr[1];
	getValType();
}


var gtype = 0;
var gnum = 0;
var gflag = 0;
var typeFlag = 0;

function getValType() {
	gnum = 0;
	gflag = 1;
	if (mflag == "tiwen") {
		gtype = 1;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "maibo"){
		gtype = 2;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "huxi"){
		gtype = 3;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "xueyang"){
		gtype = 4;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "tizhong"){
		gtype = 5;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "yundong"){
		gtype = 6;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "chouyan"){
		gtype = 7;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "yinjiu"){
		gtype = 8;
		typeFlag = 1;
		getHisData();
	}else if(mflag == "xueya"){
		typeFlag = 2;
		getBloodHisData();
	}else if(mflag == "shuimian"){
		typeFlag = 3;
		getShuimianHisData();
	}else if(mflag == "yinshi"){
		typeFlag = 4;
		getYinshiHisData();
	}else if(mflag == "xuetang"){
		typeFlag = 5;
		getXuetangHisData();
		
	}

	
}

var result = '';

var NumFlag = 0;
var reload = document.getElementById("reload");
var loading = document.getElementById("loading");
var outLoad = document.getElementById("outLoad");
function getNextHisPage(){
	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	var outLoad = document.getElementById("outLoad");
	reload.style.display = 'none';
	loading.style.display = "block";
	
	NumFlag++;
	if(typeFlag == 1){
		getHisData();
	}else if(typeFlag == 2){
		getBloodHisData();
	}else if(typeFlag == 3){
		getShuimianHisData();
	}else if(typeFlag == 4){
		getYinshiHisData();
	}else if(typeFlag == 5){
		getXuetangHisData();
	}
}

function getHisData() {
	var getNum = 0;
	if(NumFlag == 0){
		getNum = 0;
	}else{
		getNum = NumFlag;
	}

	$.ajax({
		type: "post",
		url: myUrl + "infowrite_infohistory",
		data: {
			'patient':id,
			'token':token,
			'userid': id, 
			'type': gtype,
			'num': getNum,
			'flag': gflag

		},
		success: function(m) {
			console.log(m)
			result = eval("(" + m + ")");
			if(result.code == 1){
				getValueArray();
				if(result.dataArray.length == 0){
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outLoad = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = "none";
					outLoad.style.display = "block";
				}
			}else{
				getPopInfo(result.msg)
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outLoad = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = "none";
					outLoad.style.display = "block";

			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

function getBloodHisData(){
	var getNum = 0;
	if(NumFlag == 0){
		getNum = 0;
	}else{
		getNum = NumFlag;
	}

	$.ajax({
		type: "post",
		url: myUrl + "infowrite_twovaluehistory",
		data: {
			'patient':id,
			'token':token,
			'userid': id, 
			'num': getNum,
			'flag': gflag

		},
		success: function(m) {
			console.log(m)
			result = eval("(" + m + ")");
			if(result.code == 1){
				getValueArray();
				if(result.dataArray.length == 0){
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outLoad = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = "none";
					outLoad.style.display = "block";
				}
			}else{
				getPopInfo(result.msg);
				var reload = document.getElementById("reload");
				var loading = document.getElementById("loading");
				var outLoad = document.getElementById("outLoad");
				reload.style.display = 'none';
				loading.style.display = "none";
				outLoad.style.display = "block";
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

function getShuimianHisData(){
	var getNum = 0;
	if(NumFlag == 0){
		getNum = 0;
	}else{
		getNum = NumFlag;
	}

	$.ajax({
		type: "post",
		url: myUrl + "infowrite_sleephistory",
		data: {
			'patient':id,
			'token':token,
			'userid': id, 
			'num': getNum,
			'flag': gflag

		},
		success: function(m) {
			console.log(m)
			result = eval("(" + m + ")");
			if(result.code == 1){
				getValueArray();
				if(result.dataArray.length == 0){
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outLoad = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = "none";
					outLoad.style.display = "block";
				}
			}else{
				getPopInfo(result.msg);
				var reload = document.getElementById("reload");
				var loading = document.getElementById("loading");
				var outLoad = document.getElementById("outLoad");
				reload.style.display = 'none';
				loading.style.display = "none";
				outLoad.style.display = "block";
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

function getXuetangHisData(){
	var getNum = 0;
	if(NumFlag == 0){
		getNum = 0;
	}else{
		getNum = NumFlag;
	}

	$.ajax({
		type: "post",
		url: myUrl + "infowrite_bloodhistory",
		data: {
			'patient':id,
			'token':token,
			'userid': id, 
			'num': getNum,
			'flag': gflag
		},
		success: function(m) {
			console.log(m)
			result = eval("(" + m + ")");
			if(result.code == 1){
				getValueArray();
				if(result.dataArray.length == 0){
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outLoad = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = "none";
					outLoad.style.display = "block";
				}
			}else{
				getPopInfo(result.msg);
				var reload = document.getElementById("reload");
				var loading = document.getElementById("loading");
				var outLoad = document.getElementById("outLoad");
				reload.style.display = 'none';
				loading.style.display = "none";
				outLoad.style.display = "block";
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

function getYinshiHisData(){
	var getNum = 0;
	if(NumFlag == 0){
		getNum = 0;
	}else{
		getNum = NumFlag;
	}

	$.ajax({
		type: "post",
		url: myUrl + "infowrite_eathistory",
		data: {
			'patient':id,
			'token':token,
			'userid': id, 
			'num': getNum,
			'flag': gflag

		},
		success: function(m) {
			console.log(m);
			result = eval("(" + m + ")");
			if(result.code == 1){
				getValueArray();
				if(result.dataArray.length == 0){
					var reload = document.getElementById("reload");
					var loading = document.getElementById("loading");
					var outLoad = document.getElementById("outLoad");
					reload.style.display = 'none';
					loading.style.display = "none";
					outLoad.style.display = "block";
				}
			}else{
				getPopInfo(result.msg);
				var reload = document.getElementById("reload");
				var loading = document.getElementById("loading");
				var outLoad = document.getElementById("outLoad");
				reload.style.display = 'none';
				loading.style.display = "none";
				outLoad.style.display = "block";
			}
		},
		error: function(e) {
			getPopInfo("网络错误");
		}
	});
}

var valueArr = new Array();
function getValueArray() {
	
	var ReLength = result.dataArray.length;
	var i = 0;
	var iFlag = 1;
	var j = 0;
	var NowLength = result.dataArray.length;
	
	for (i = 0; i < ReLength; i++) {
		if (iFlag == 8) {
			if(typeFlag == 1 || typeFlag == 3){
				DrawPic();
			}else if(typeFlag == 2){
				DrawBloodPic();
			}else if(typeFlag == 5){
				DrawXueTangPic();
			}else if(typeFlag == 4){
				DrawYinshiPic();
			}
			iFlag = 1;
			j = 0;
			i--;

		} else {
			valueArr[j] = result.dataArray[i];
			if( NowLength == 1 && iFlag < 8){
				if(typeFlag == 1 || typeFlag == 3){
					DrawPic();
				}else if(typeFlag == 2){
					DrawBloodPic();
				}else if(typeFlag == 5){
					DrawXueTangPic();
				}else if(typeFlag == 4){
					DrawYinshiPic();
				}
			}
			j++;
			iFlag++;
			NowLength--;
		}

	}

}

var dId = 0;
function DrawPic() {
	dId++;
	var html = "";
	html += '<div class="ImgBox">'
	html += '<div class="ImgTitle">'
	if(dId == 1){
		html += '最近一周'
	}else{
		html += valueArr[0].date;
	}
	
	html += '</div>'
	html += '<div class="ImgDataDraw" id='+dId+' >'

	html += '</div> '
	html += '</div>'
	$("#Content").append(html);
	
	
	var date = [];
	var data = {};
	var mvalue = [];
	var nvalue = [];
	var dataArr = valueArr;
	var OlDate = [];
	dataArr.forEach(function(e) {
		OlDate.push(e.date);
		mvalue.push(Number(e.valueone));
	});
	date = OlDate.reverse();
	data.name = "";
	data.data = mvalue.reverse();
	data.color = 'red';
	$('#'+dId).highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#ffffff',
			backgroundColor: '#ffffff',
		},
		legend: {
			enabled: false
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: date,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			}

		},
		yAxis: {
			title: {
				text: ''
			},
			gridLineColor: '#000000',
			gridLineWidth: 2,
			lineColor: '#000000',
			lineWidth: 2,
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			},
			tickInterval: 3,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
				dataLabels: {
					color:"blue",
					enabled: true 
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
		series: [data]
	});
	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	reload.style.display = 'block';
	loading.style.display = "none";

valueArr = new Array();
}

var bloodId = 0;
function DrawBloodPic() {
	bloodId++;
	var html = "";
	html += '<div class="ImgBox">'
	html += '<div class="ImgTitle">'
	if(bloodId == 1){
		html += '最近一周'
	}else{
		html += valueArr[0].date;
	}
	
	html += '</div>'
	html += '<div class="ImgDataDraw" id='+bloodId+' >'

	html += '</div> '
	html += '</div>'
	$("#Content").append(html);
	
	
	var date = [];
	var data = {};
	var data2 = {};
	var mvalue = [];
	var nvalue = [];
	var dataArr = valueArr;
	var OlDate = [];
	dataArr.forEach(function(e) {
		OlDate.push(e.date);
		mvalue.push(Number(e.valueone));
		nvalue.push(Number(e.valuetwo));
	});
	date = OlDate.reverse();
	
	$('#'+bloodId).highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#ffffff',
			backgroundColor: '#ffffff',



		},
		legend: {
			enabled: true
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: date,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			}

		},
		yAxis: {
			title: {
				text: ''
			},
			gridLineColor: '#000000',
			gridLineWidth: 2,
			lineColor: '#000000',
			lineWidth: 2,
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			},
			tickInterval: 3,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
				dataLabels: {
					color:"blue",
					enabled: true 
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
		series: [{name:"高血压",data:mvalue.reverse(),color:'#c36f2e'},{name:"低血压",data:nvalue.reverse(),color:'#f23c13'}]
	});
	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	reload.style.display = 'block';
	loading.style.display = "none";


valueArr = new Array();
}


var XTId = 0;
function DrawXueTangPic() {
	
	XTId++;
	var html = "";
	html += '<div class="ImgBox">'
	html += '<div class="ImgTitle">'
	if(XTId == 1){
		html += '最近一周'
	}else{
		html += valueArr[0].date;
	}
	
	html += '</div>'
	html += '<div class="ImgDataDraw" id='+XTId+' >'

	html += '</div> '
	html += '</div>'
	$("#Content").append(html);
	
	
	var date = [];
	var mvalue = [];
	var nvalue = [];
	var avalue = [];
	var bvalue = [];
	var cvalue = [];
	var dvalue = [];
	var evalue = [];
	 
	var dataArr = valueArr;
	var OlDate = [];
	dataArr.forEach(function(e) {
		OlDate.push(e.date);
		mvalue.push(Number(e.value1));
		nvalue.push(Number(e.value2));
		avalue.push(Number(e.value3));
		bvalue.push(Number(e.value4));
		cvalue.push(Number(e.value5));
		dvalue.push(Number(e.value6));
		evalue.push(Number(e.value7));
	});
	date = OlDate.reverse();
	
	$('#'+XTId).highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#ffffff',
			backgroundColor: '#ffffff',



		},
		legend: {
			enabled: true
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: date,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			}

		},
		yAxis: {
			title: {
				text: ''
			},
			gridLineColor: '#000000',
			gridLineWidth: 2,
			lineColor: '#000000',
			lineWidth: 2,
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			},
			tickInterval: 3,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
				dataLabels: {
//												enabled: true 
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
		series: [
		{name:"早餐前",data:mvalue.reverse(),color:'#c36f2e'},
		{name:"早餐后",data:nvalue.reverse(),color:'#f23c13'},
		{name:"午餐前",data:avalue.reverse(),color:'#e7739a'},
		{name:"午餐后",data:bvalue.reverse(),color:'#e8e5d2'},
		{name:"晚餐前",data:cvalue.reverse(),color:'#fee554'},
		{name:"晚餐后",data:dvalue.reverse(),color:'#009714'},
		{name:"晚上三点",data:evalue.reverse(),color:'#ff243d'},
		]
	});
	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	reload.style.display = 'block';
	loading.style.display = "none";


valueArr = new Array();
}


var YSId = 0;
function DrawYinshiPic() {
	
	YSId++;
	var html = "";
	html += '<div class="ImgBox">'
	html += '<div class="ImgTitle">'
	if(YSId == 1){
		html += '最近一周'
	}else{
		html += valueArr[0].date;
	}
	
	html += '</div>'
	html += '<div class="ImgDataDraw" id='+YSId+' >'

	html += '</div> '
	html += '</div>'
	$("#Content").append(html);
	
	
	var date = [];
	var mvalue = [];
	var nvalue = [];
	 
	var dataArr = valueArr;
	var OlDate = [];
	dataArr.forEach(function(e) {
		OlDate.push(e.date);
		mvalue.push(Number(e.numericOne));
		nvalue.push(Number(e.numericTwo));
	});
	date = OlDate.reverse();
	
	$('#'+YSId).highcharts({
		chart: {
			type: 'line',
//			margin: [6, 2, 22, 30],
			fontSize: 10,
			fontColor: '#ffffff',
			backgroundColor: '#ffffff',



		},
		legend: {
			enabled: true
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: date,
			lineColor: '#000000',
			lineWidth: 2,
			tickColor: "#000000",
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			}

		},
		yAxis: {
			title: {
				text: ''
			},
			gridLineColor: '#000000',
			gridLineWidth: 2,
			lineColor: '#000000',
			lineWidth: 2,
			labels: {
				style: {
					color: '#000000',
					fontSize: 8
				}
			},
			tickInterval: 3,
			endOnTick: false,
			maxPadding: 0.5
		},
		plotOptions: {
			line: {
						dataLabels: {
							enabled: true ,
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
				}
			}
		},
		series: [
		{name:"饮食情况",data:mvalue.reverse(),color:'#c36f2e'},
//		{name:"早餐后",data:nvalue.reverse(),color:'#f23c13'},
		]
	});
	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	reload.style.display = 'block';
	loading.style.display = "none";


	valueArr = new Array();
}
//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}