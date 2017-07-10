var Bookshelf = require('../db/bookshelf').DB;
var Deferred = require("promised-io/promise").Deferred;
var uuid = require('uuid');
var async = require('async');
var Employee = require("./Employee");
var BaseModel = require('../models/BaseModel')();

var Location = //BaseModel.extend
    Bookshelf.Model.extend({
        tableName: "location",
        employee: function() {
            return this.belongsTo(Employee);
        }

    })

module.exports = Location;
/*function(app){
	return Bookshelf.Model.extend("Location",Location);
}*/
