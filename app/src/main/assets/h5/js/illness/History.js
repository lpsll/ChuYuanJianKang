window.onload = function() {
	id = bridge.getUserId();
	token = bridge.getToken();
//	id = 149;
//	token = "";
	getUrl()
}
var id = "";
var token = "";
var mflag = "";
var deleteType = "";

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
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "maibo"){
		gtype = 2;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "huxi"){
		gtype = 3;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "xueyang"){
		gtype = 4;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "tizhong"){
		gtype = 5;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "yundong"){
		gtype = 6;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "chouyan"){
		gtype = 7;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "yinjiu"){
		gtype = 8;
		typeFlag = 1;
		deleteType = 'onenum';
		getHisData();
	}else if(mflag == "xueya"){
		typeFlag = 2;
		getBloodHisData();
		deleteType = 'twonum';
	}else if(mflag == "shuimian"){
		typeFlag = 3;
		deleteType = 'twotime';
		getShuimianHisData();
	}else if(mflag == "yinshi"){
		typeFlag = 4;
		deleteType = 'twochoose';
		getYinshiHisData();
	}else if(mflag == "xuetang"){
		typeFlag = 5;
		deleteType = 'oneandone';
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

function deleteT(a){
	
	var Hisid = a.getAttribute('id');
	
	$.ajax({
		type:"post",
		url:myUrl+"infowrite_delinfowrite",
		data:{
			'id':Hisid,
			'token':token,
			'type':deleteType
		},
		success:function(result){
			var m = eval("("+result+")");
			if(m.code == 1){
//				getPopInfo(m.msg);
				alert(m.msg)
				location.reload();
			}else{
				getPopInfo(m.msg);
			}
		},
		error:function(e){
//			getPopInfo("网络错误");
			alert("网络错误")
		}
	});
}


function getValueArray() {
	var html = "";
	var ReLength = result.dataArray;
	if (mflag == "tiwen") {
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">度</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "maibo"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">bpm</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "huxi"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">次/分钟</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "xueyang"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate  mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">%</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
		
	}else if(mflag == "tizhong"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">Kg</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "yundong"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit  mm">Km</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "chouyan"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">根</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "yinjiu"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html += '<span class="HisDate mm">'+e.date+'</span>'
			html +=	'<span class="HisValue mm">'+e.numericOne+'</span>'
			html +=	'<span class="HisUnit mm">mL</span>'
			html +=	'<span class="HisDelete" id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
	}else if(mflag == "xueya"){
		ReLength.forEach(function(e){
			html += '<div class="HisList">'
			html +=	'<span class="YaName mm">'+e.date+'</span><br />'
			html +=	'<span class="YaName mm">高压 ：'+e.numericOne+'kpa</span>'
			html +=	'<span class="YaName yaMar mm">低压：'+e.numericTwo+'kpa</span>'
			html +=	'<span class="HisDelete Ya"  id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
			
		})
	}else if(mflag == "shuimian"){
		ReLength.forEach(function(e){
			var type = '';
			if(e.numericOne == 1){
				type = "无"
			}else if(e.numericOne == 2){
				type = "多汗"
			}else if(e.numericOne == 3){
				type = "起夜"
			}else if(e.numericOne == 4){
				type = "多梦"
			}
			html += '<div class="HisList">'
			html +=	'<span class="YaName">睡眠质量：'+type+'</span><br />'
			html +=	'<span class="YaName">睡觉时间：'+e.startTime+'</span><br/>'
			html +=	'<span class="YaName">起床时间：'+e.endTime+'</span>'
			html +=	'<span class="HisDelete SM"  id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
			
		})
	}else if(mflag == "yinshi"){
		ReLength.forEach(function(e){
			var type2 = '';
			var type1 = '';
			if(e.numericOne == 1){
				type1 = "无"
			}else if(e.numericOne == 2){
				type1 = "油腻"
			}else if(e.numericOne == 3){
				type1 = "一般"
			}else if(e.numericOne == 4){
				type1 = "清淡"
			}
			
			if(e.numericTwo == 1){
				type2 = "无"
			}else if(e.numericTwo == 2){
				type2 = "咸"
			}else if(e.numericTwo == 3){
				type2 = "一般"
			}else if(e.numericTwo == 4){
				type2 = "淡"
			}
			html += '<div class="HisList">'
			html +=	'<span class="YaName">'+e.date+'</span><br />'
			html +=	'<span class="YaName">饮食情况：'+type1+'</span><br/>'
			html +=	'<span class="YaName">食盐情况：'+type2+'</span>'
			html +=	'<span class="HisDelete SM"  id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
		})
		
		
	}else if(mflag == "xuetang"){
		ReLength.forEach(function(e){
			var type = "";
			if(e.type == 1){
				type = "早餐前";
			}else if(e.type == 2){
				type = "早餐后";
			}else if(e.type == 3){
				type = "午餐前";
			}else if(e.type == 4){
				type = "午餐后";
			}else if(e.type == 5){
				type = "晚餐前";
			}else if(e.type == 6){
				type = "晚餐后";
			}else if(e.type == 7){
				type = "晚上三点";
			}
			
			
			html += '<div class="HisList">'
			html +=	'<span class="YaName">'+e.date+'</span><br />'
			html +=	'<span class="YaName">血糖值：'+e.numericOne+'mmol/L</span><br/>'
			html +=	'<span class="YaName">测量时间：'+type+'</span>'
			html +=	'<span class="HisDelete SM"  id='+e.id+' onclick="deleteT(this)">删除</span>'
			html +=	'</div>'
			
		})
	}
	$("#ContDiv").append(html);
	
	var reload = document.getElementById("reload");
	var loading = document.getElementById("loading");
	var outLoad = document.getElementById("outLoad");
	reload.style.display = 'block';
	loading.style.display = "none";
	outLoad.style.display = "none";
}

//调用安卓弹出提示信息方法
function getPopInfo(msg) {
	bridge.alert(msg);
}
//调用安卓返回方法
function AndroidBack() {
	bridge.back();
}