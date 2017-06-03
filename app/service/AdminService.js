var testData  = require("../testData/senate.json");

//Function Definition
function prepareBasicInfo(){
	var count = testData.objects.length - 1;
	var maidBasicInfo = [];
	for(var i =0; i < count; i++){
		var desc= testData.objects[i].description;
		maidBasicInfo.push({
			"id" : testData.objects[i].id,
			"FirstName":testData.objects[i].person.firstname,
			"LastName": testData.objects[i].person.lastname,
			"DOB": testData.objects[i].person.birthday,
			"City": desc.substring(desc.indexOf("from") + 5)});
	}
	return maidBasicInfo;
}

function viewAllRecords(req, res) {
	res.status(200).send(prepareBasicInfo());
}

function viewAllDetails(req, res) {

	var user_id = req.params.id;
	var userProfile = {};
	var count = testData.objects.length - 1;
	for(var i =0; i < count ; i++){
		if(testData.objects[i].id == user_id)
		{
			userProfile = testData.objects[i];
		}
	}
	if(Object.keys(userProfile).length != 0){
		res.status(200).send(userProfile)
	}
}


var AdminService = {
    VIEW_ALL_RECORDS: viewAllRecords,
    VIEW_ALL_INFO: viewAllDetails
};
//Exporting modules
module.exports = AdminService;