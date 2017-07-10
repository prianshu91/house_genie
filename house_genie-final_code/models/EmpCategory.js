var Bookshelf = require('../db/bookshelf').DB;
var uuid = require('uuid');
var moment = require("moment");
var BaseModel = require('../models/BaseModel')();
//Model Definition         
let emp_cat = Bookshelf.Model.extend({
    tableName: "category_employees",
    defaults: function() {
        return {
            id: uuid.v4(),
            created_on: moment.utc().valueOf(),
            modified_on: moment.utc().valueOf()
        }
    }

});


module.exports = emp_cat; 
/*function(app){
	return Bookshelf.Model.extend( 'Emp_Category', emp_cat);
};*/
