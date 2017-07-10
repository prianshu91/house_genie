var Bookshelf = require('../db/bookshelf').DB;
var Deferred = require("promised-io/promise").Deferred;
var uuid = require('uuid');
var async = require('async');
var moment = require("moment");

var BaseModel = require('../models/BaseModel')();
var Rating = require("./Rating");
var Category = require("./Category");
var Location = require("./Location");
var Verification = require("./Verification");


var Employee = Bookshelf.Model.extend({
    tableName: 'employees',
    defaults: function() {
        return {
            id: uuid.v4(),
            created_on: moment.utc().valueOf(),
            modified_on: moment.utc().valueOf()
        };
    },
    rating: function() {
        return this.hasOne(Rating);
    },
    location: function() {
        return this.hasMany(Location);
    },
    category: function() {
        return this.hasMany(category)
    },
    verification: function(argument) {
        // body...
        return this.hasOne(Verification);
    }

    /*findAll : function(){
		var dfd = new Deferred();
		async.series([
				function(cb){
					Baseshelf.model("Employee").fetchAll().then(function(category){
						cb();
					})
				}
			],function(err){
				 if ( err ) dfd.reject( err );
            	//else dfd.resolve( account );

			});
		return dfd.promise()
	}*/
})

module.exports = Employee;
/* function(app){
	return Bookshelf.Model.extend("Employee",Employee);
}*/
