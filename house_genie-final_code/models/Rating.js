

var Bookshelf = require('../db/bookshelf').DB;
var Deferred  = require("promised-io/promise").Deferred;
var uuid      = require( 'uuid' );
var async     = require( 'async' );
var BaseModel = require( '../models/BaseModel' )();

var Employee = require("./Employee");
var Category = require("./Category");


var Rating = //BaseModel.extend
Bookshelf.Model.extend({
	tableName : "ratings",
	employee : function(){
		return this.belongsTo(Employee);
	},
	user : function(){
		return this.belongsTo(User);
	},
	

})

module.exports = Rating;