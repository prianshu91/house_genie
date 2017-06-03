var db = require("../../config/env");

//function Implementations
function viewAllCategories(req,res){

	var viewAllCatgQuery = "select id,name from categories";
	db.query(viewAllCatgQuery,function(err,results,fields){
		db.end();
		if(err){
			res.send(err).status(500);
		}
		if(results){
			res.send(results).status(200);
		}
	});
	
}

function viewAllEmpByCategtory(req,res){
	var category_id = req.params.id;
	var empByCategQuery = " select first_name,last_name from  employee_profiles where "
	+ " id in (select employee_id from category_employee_mapping where category_id = ? )";

	db.query(empByCategQuery,db.escape(category_id),function(err,results,fields){
		db.end();
		if(err){
			res.send(err).status(500);

		}
		if(results){
			res.send(err).status(200);
		}
	})
}


function viewEmpDetails(req,res){

	var empDetailQuery = "SELECT * FROM employee_profiles emp join employee_location_mapping emp_loc on emp.id = emp_loc.employee_id join verification ver on emp.verification_id = ver.id join employee_rating_mapping emp_rating on emp.id = emp_rating.employee_id join employee_rating rating on rating.id = emp_rating.rating_id"
	 +  " and emp.id = ?";
	 db.query(empDetailQuery,req.params.id,function(err,results,fields){
	 	db.end();
		if(err){
			res.send(err).status(500);

		}
		if(results){
			res.send(err).status(200);
		}
	 });
}
//Exporting modules
module.exports = {
	VIEW_ALL_CATEGORIES : viewAllCategories,
	VIEW_EMP_BY_CATEGORY : viewAllEmpByCategtory,
	VIEW_EMP_DETAILS : viewEmpDetails
}