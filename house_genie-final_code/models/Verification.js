var Bookshelf = require('../db/bookshelf').DB;
var uuid = require('uuid');
var Employee = require("./Employee");
var moment = require("moment");

var Verification = Bookshelf.Model.extend({
    tableName: "verification",
    defaults: function() {
        return {
            id: uuid.v4(),
            created_on: moment.utc().valueOf(),
            modified_on: moment.utc().valueOf()
        };
    },
    employee: function() {
        return this.belongsTo(Employee);
    }
});


module.exports = Verification;

/*function(app){
	return Bookshelf.Model.extend("Verification",Verification);
}*/
