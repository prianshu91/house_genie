//Importing pre-dedefind modules
var log = require("log4js").getLogger();
var Deferred = require("promised-io/promise").Deferred;
var async = require("async");
//Importing other modules
var Employee = require("../../models/Employee");
var Verification = require("../../models/Verification");
var EmpCategory = require("../../models/EmpCategory");
//Function Implemenations
function getVerificationId(verf_record) {
    let verification = new Verification();
    let dfd = new Deferred();

    verification.save(verf_record).
    then(function(obj) {
        verification_id = obj.id;
        dfd.resolve(obj);
    }, function(err) {
        dfd.reject(err)
    });

    return dfd.promise;
}

//Insertion of verification issue
function insertEmpRecord(verf_record, emp, record) {
    let verification = new Verification();
    let employee = new Employee();
    let dfd = new Deferred();
    //Constructing Employee Record Object
    let emp_record = {
        first_name: emp.fname,
        last_name: emp.lname,
        contact_number: emp.phone,
        address: emp.address,
        verification_id: verification_id,
        police_verified: ((verification_id) ? 1 : 2)
    }
    //Saaving verification details
    getVerificationId(verf_record).then(
        function(obj) {
            let verification_id = obj.id;
            //Checking verification id to avoid foreign key error
            if (!verification_id) {
                dfd.reject("Unverified Account!!");
            } else {

                //Saving Employee
                employee.save(emp_record).then(
                    function(obj) {
                        dfd.resolve(obj);
                    },
                    function(err) {
                        dfd.reject(err)
                    });
            }
        },
        function(err) {
            dfd.reject(err);
        });

    return dfd.promise;

}

//Updating Employee Records
function updateEmpRecord(empl) {

    let employee = new Employee({ id: emp.id });
    let dfd = new Deferred();

    let emp_record = {
        first_name: employee.fname,
        last_name: employee.lname,
        contact_number: employee.phone,
        address: employee.address,
    };

    Employee.where({ id: emp.id }).count().
    then(function(count) {
        if (count == 0 || count === 0) {
            dfd.reject("No Record found!!Unable to update");
        } else {
            employee.save(emp_record, { method: "update" }).
            then(function(obj) {
                    dfd.resolve(obj);
                },
                function(err) {
                    dfd.reject(err);
                });
        }
    });
    return dfd.promise;
}

//Fetch  Employee DEtails
function getEmployeeDetails(id) {
    let dfd = new Deferred();

    Employee.where({ id: id }).fetch().
    then(function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(obj);
        });
    return dfd.promise;
}

//Insert Employee and Cetegory Details
function insertEmpCateDetails(record) {
    let emp_cat = new EmpCategory();
    let dfd = new Deferred();

    emp_cat.save(record).then(
        function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(err);
        });

    return dfd.promise;
}

//Get All Employees 
function getAllEmployees() {
    let dfd = new Deferred();
    let employee = new Employee();

    Employee.fetchAll().then(
        function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(err);
        });
    return dfd.promise;
}
//Function for insert verification records
function insertVerfRecord(verf_record) {
    let verification = new Verification();
    let response = new Object();
    verification.save(verf_record).
    then(function(obj) {
        response.id = obj.id;
    }, function(err) {
        response.error = err
    });
    return response;
}

let EmployeeService = {
    Insert: insertEmpRecord,
    Update: updateEmpRecord,
    Search: getEmployeeDetails,
    InsertEmpCategory: insertEmpCateDetails,
    View: getAllEmployees,
    Verify: getVerificationId
}
//Exporting modules
module.exports = EmployeeService;