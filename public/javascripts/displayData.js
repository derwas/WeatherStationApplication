function displayRainData(dataList) {
	
	//INIT
	var min = dataList[0];
	var minTime = dataList[1];
	var max = dataList[2];
	var maxTime = dataList[3];
	var average = dataList[4];
	var current = dataList[5];
	var currentTime = dataList[6];

	//MIN
	var minDate = new Date(minTime); //epoch timestamp to javascript date
	var hours = (minDate.getHours() < 10 ? "0"+minDate.getHours() : minDate.getHours()); //append a 0 if hours is lt 10
	var minutes = (minDate.getMinutes() < 10 ? "0"+minDate.getMinutes() : minDate.getMinutes());//append a 0 if minutes is lt 10
	var minDateF =  hours + "H" + minutes;

	//MAX
	var maxDate = new Date(maxTime);//epoch timestamp to javascript date
	hours = (maxDate.getHours() < 10 ? "0"+maxDate.getHours() : maxDate.getHours());//append a 0 if minutes is lt 10
	minutes = (maxDate.getMinutes() < 10 ? "0"+maxDate.getMinutes() : maxDate.getMinutes());//append a 0 if minutes is lt 10
	var maxDateF =  hours + "H" + minutes;

	//AVERAGE
	var averageF = (Math.round(average * 100)/100).toFixed(2);

	//CURRENT
	var currentDate = new Date(currentTime);//epoch timestamp to javascript date
	hours = (currentDate.getHours() < 10 ? "0"+currentDate.getHours() : currentDate.getHours()); //append a 0 if hours is lt 10
	minutes = (currentDate.getMinutes() < 10 ? "0"+currentDate.getMinutes() : currentDate.getMinutes());//append a 0 if minutes is lt 10
	var currentDateF = hours + "H" + minutes;

	//APPEND
	document.getElementById("rainDataValueCur").appendChild(document.createTextNode(current));
	document.getElementById("rainDataValueMin").appendChild(document.createTextNode(min));
	document.getElementById("rainDataValueMax").appendChild(document.createTextNode(max));
	document.getElementById("rainDataTimeCur").appendChild(document.createTextNode(currentDateF));
	document.getElementById("rainDataTimeMin").appendChild(document.createTextNode(minDateF));
	document.getElementById("rainDataTimeMax").appendChild(document.createTextNode(maxDateF));
	document.getElementById("rainDataValueAvg").appendChild(document.createTextNode(averageF));

}

function displayAirData(dataList) {
	
	//INIT
	var min = dataList[0];
	var minTime = dataList[1];
	var max = dataList[2];
	var maxTime = dataList[3];
	var average = dataList[4];
	var current = dataList[5];
	var currentTime = dataList[6];

	//MIN
	var minDate = new Date(minTime); //epoch timestamp to javascript date
	var hours = (minDate.getHours() < 10 ? "0"+minDate.getHours() : minDate.getHours()); //append a 0 if hours is lt 10
	var minutes = (minDate.getMinutes() < 10 ? "0"+minDate.getMinutes() : minDate.getMinutes());//append a 0 if minutes is lt 10
	var minDateF =  hours + "H" + minutes;

	//MAX
	var maxDate = new Date(maxTime);//epoch timestamp to javascript date
	hours = (maxDate.getHours() < 10 ? "0"+maxDate.getHours() : maxDate.getHours());//append a 0 if minutes is lt 10
	minutes = (maxDate.getMinutes() < 10 ? "0"+maxDate.getMinutes() : maxDate.getMinutes());//append a 0 if minutes is lt 10
	var maxDateF =  hours + "H" + minutes;

	//AVERAGE
	var averageF = (Math.round(average * 100)/100).toFixed(2);

	//CURRENT
	var currentDate = new Date(currentTime);//epoch timestamp to javascript date
	hours = (currentDate.getHours() < 10 ? "0"+currentDate.getHours() : currentDate.getHours()); //append a 0 if hours is lt 10
	minutes = (currentDate.getMinutes() < 10 ? "0"+currentDate.getMinutes() : currentDate.getMinutes());//append a 0 if minutes is lt 10
	var currentDateF = hours + "H" + minutes;

	//APPEND
	document.getElementById("airDataValueCur").appendChild(document.createTextNode(current));
	document.getElementById("airDataValueMin").appendChild(document.createTextNode(min));
	document.getElementById("airDataValueMax").appendChild(document.createTextNode(max));
	document.getElementById("airDataTimeCur").appendChild(document.createTextNode(currentDateF));
	document.getElementById("airDataTimeMin").appendChild(document.createTextNode(minDateF));
	document.getElementById("airDataTimeMax").appendChild(document.createTextNode(maxDateF));
	document.getElementById("airDataValueAvg").appendChild(document.createTextNode(averageF));

}

function displayTempData(dataList) {
	
	//INIT
	var min = dataList[0];
	var minTime = dataList[1];
	var max = dataList[2];
	var maxTime = dataList[3];
	var average = dataList[4];
	var current = dataList[5];
	var currentTime = dataList[6];

	//MIN
	var minDate = new Date(minTime); //epoch timestamp to javascript date
	var hours = (minDate.getHours() < 10 ? "0"+minDate.getHours() : minDate.getHours()); //append a 0 if hours is lt 10
	var minutes = (minDate.getMinutes() < 10 ? "0"+minDate.getMinutes() : minDate.getMinutes());//append a 0 if minutes is lt 10
	var minDateF =  hours + "H" + minutes;

	//MAX
	var maxDate = new Date(maxTime);//epoch timestamp to javascript date
	hours = (maxDate.getHours() < 10 ? "0"+maxDate.getHours() : maxDate.getHours());//append a 0 if minutes is lt 10
	minutes = (maxDate.getMinutes() < 10 ? "0"+maxDate.getMinutes() : maxDate.getMinutes());//append a 0 if minutes is lt 10
	var maxDateF =  hours + "H" + minutes;

	//AVERAGE
	var averageF = (Math.round(average * 100)/100).toFixed(2);

	//CURRENT
	var currentDate = new Date(currentTime);//epoch timestamp to javascript date
	hours = (currentDate.getHours() < 10 ? "0"+currentDate.getHours() : currentDate.getHours()); //append a 0 if hours is lt 10
	minutes = (currentDate.getMinutes() < 10 ? "0"+currentDate.getMinutes() : currentDate.getMinutes());//append a 0 if minutes is lt 10
	var currentDateF = hours + "H" + minutes;

	//APPEND
	document.getElementById("tempDataValueCur").appendChild(document.createTextNode(current));
	document.getElementById("tempDataValueMin").appendChild(document.createTextNode(min));
	document.getElementById("tempDataValueMax").appendChild(document.createTextNode(max));
	document.getElementById("tempDataTimeCur").appendChild(document.createTextNode(currentDateF));
	document.getElementById("tempDataTimeMin").appendChild(document.createTextNode(minDateF));
	document.getElementById("tempDataTimeMax").appendChild(document.createTextNode(maxDateF));
	document.getElementById("tempDataValueAvg").appendChild(document.createTextNode(averageF));

}

function displayHumiData(dataList) {
	
	//INIT
	var min = dataList[0];
	var minTime = dataList[1];
	var max = dataList[2];
	var maxTime = dataList[3];
	var average = dataList[4];
	var current = dataList[5];
	var currentTime = dataList[6];

	//MIN
	var minDate = new Date(minTime); //epoch timestamp to javascript date
	var hours = (minDate.getHours() < 10 ? "0"+minDate.getHours() : minDate.getHours()); //append a 0 if hours is lt 10
	var minutes = (minDate.getMinutes() < 10 ? "0"+minDate.getMinutes() : minDate.getMinutes());//append a 0 if minutes is lt 10
	var minDateF =  hours + "H" + minutes;

	//MAX
	var maxDate = new Date(maxTime);//epoch timestamp to javascript date
	hours = (maxDate.getHours() < 10 ? "0"+maxDate.getHours() : maxDate.getHours());//append a 0 if minutes is lt 10
	minutes = (maxDate.getMinutes() < 10 ? "0"+maxDate.getMinutes() : maxDate.getMinutes());//append a 0 if minutes is lt 10
	var maxDateF =  hours + "H" + minutes;

	//AVERAGE
	var averageF = (Math.round(average * 100)/100).toFixed(2);

	//CURRENT
	var currentDate = new Date(currentTime);//epoch timestamp to javascript date
	hours = (currentDate.getHours() < 10 ? "0"+currentDate.getHours() : currentDate.getHours()); //append a 0 if hours is lt 10
	minutes = (currentDate.getMinutes() < 10 ? "0"+currentDate.getMinutes() : currentDate.getMinutes());//append a 0 if minutes is lt 10
	var currentDateF = hours + "H" + minutes;

	//APPEND
	document.getElementById("humiDataValueCur").appendChild(document.createTextNode(current));
	document.getElementById("humiDataValueMin").appendChild(document.createTextNode(min));
	document.getElementById("humiDataValueMax").appendChild(document.createTextNode(max));
	document.getElementById("humiDataTimeCur").appendChild(document.createTextNode(currentDateF));
	document.getElementById("humiDataTimeMin").appendChild(document.createTextNode(minDateF));
	document.getElementById("humiDataTimeMax").appendChild(document.createTextNode(maxDateF));
	document.getElementById("humiDataValueAvg").appendChild(document.createTextNode(averageF));

}

function displayWindSData(dataList) {
	
	//INIT
	var min = dataList[0];
	var minTime = dataList[1];
	var max = dataList[2];
	var maxTime = dataList[3];
	var average = dataList[4];
	var current = dataList[5];
	var currentTime = dataList[6];


	//MIN
	var minDate = new Date(minTime); //epoch timestamp to javascript date
	var hours = (minDate.getHours() < 10 ? "0"+minDate.getHours() : minDate.getHours()); //append a 0 if hours is lt 10
	var minutes = (minDate.getMinutes() < 10 ? "0"+minDate.getMinutes() : minDate.getMinutes());//append a 0 if minutes is lt 10
	var minDateF =  hours + "H" + minutes;

	//MAX
	var maxDate = new Date(maxTime);//epoch timestamp to javascript date
	hours = (maxDate.getHours() < 10 ? "0"+maxDate.getHours() : maxDate.getHours());//append a 0 if minutes is lt 10
	minutes = (maxDate.getMinutes() < 10 ? "0"+maxDate.getMinutes() : maxDate.getMinutes());//append a 0 if minutes is lt 10
	var maxDateF =  hours + "H" + minutes;

	//AVERAGE
	var averageF = (Math.round(average * 100)/100).toFixed(2);

	//CURRENT
	var currentDate = new Date(currentTime);//epoch timestamp to javascript date
	hours = (currentDate.getHours() < 10 ? "0"+currentDate.getHours() : currentDate.getHours()); //append a 0 if hours is lt 10
	minutes = (currentDate.getMinutes() < 10 ? "0"+currentDate.getMinutes() : currentDate.getMinutes());//append a 0 if minutes is lt 10
	var currentDateF = hours + "H" + minutes;

	//APPEND
	document.getElementById("windDataValueWSCur").appendChild(document.createTextNode(current));
	document.getElementById("windDataValueWSMin").appendChild(document.createTextNode(min));
	document.getElementById("windDataValueWSMax").appendChild(document.createTextNode(max));
	document.getElementById("windDataTimeWSCur").appendChild(document.createTextNode(currentDateF));
	document.getElementById("windDataTimeWSMin").appendChild(document.createTextNode(minDateF));
	document.getElementById("windDataTimeWSMax").appendChild(document.createTextNode(maxDateF));
	document.getElementById("windDataValueWSAvg").appendChild(document.createTextNode(averageF));

}

function displayWindDData(dataList) {

	//INIT
	var min = dataList[0];
	var minTime = dataList[1];
	var max = dataList[2];
	var maxTime = dataList[3];
	var average = dataList[4];
	var current = dataList[5];
	var currentTime = dataList[6];

	//MIN
	var minDate = new Date(minTime); //epoch timestamp to javascript date
	var hours = (minDate.getHours() < 10 ? "0"+minDate.getHours() : minDate.getHours()); //append a 0 if hours is lt 10
	var minutes = (minDate.getMinutes() < 10 ? "0"+minDate.getMinutes() : minDate.getMinutes());//append a 0 if minutes is lt 10
	var minDateF =  hours + "H" + minutes;

	//MAX
	var maxDate = new Date(maxTime);//epoch timestamp to javascript date
	hours = (maxDate.getHours() < 10 ? "0"+maxDate.getHours() : maxDate.getHours());//append a 0 if minutes is lt 10
	minutes = (maxDate.getMinutes() < 10 ? "0"+maxDate.getMinutes() : maxDate.getMinutes());//append a 0 if minutes is lt 10
	var maxDateF =  hours + "H" + minutes;

	//AVERAGE
	var averageF = (Math.round(average * 100)/100).toFixed(2);

	//CURRENT
	var currentDate = new Date(currentTime);//epoch timestamp to javascript date
	hours = (currentDate.getHours() < 10 ? "0"+currentDate.getHours() : currentDate.getHours()); //append a 0 if hours is lt 10
	minutes = (currentDate.getMinutes() < 10 ? "0"+currentDate.getMinutes() : currentDate.getMinutes());//append a 0 if minutes is lt 10
	var currentDateF = hours + "H" + minutes;



	//APPEND
	document.getElementById("windDataValueWDCur").appendChild(document.createTextNode(current));
	document.getElementById("windDataValueWDMin").appendChild(document.createTextNode(min));
	document.getElementById("windDataValueWDMax").appendChild(document.createTextNode(max));
	document.getElementById("windDataTimeWDCur").appendChild(document.createTextNode(currentDateF));
	document.getElementById("windDataTimeWDMin").appendChild(document.createTextNode(minDateF));
	document.getElementById("windDataTimeWDMax").appendChild(document.createTextNode(maxDateF));
	document.getElementById("windDataValueWDAvg").appendChild(document.createTextNode(averageF));

}