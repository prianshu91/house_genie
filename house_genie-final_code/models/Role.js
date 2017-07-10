var Bookshelf = require('../db/bookshelf').DB;
var Deferred = require("promised-io/promise").Deferred;
var uuid = require('uuid');
var async = require('async');
var BaseModel = require('../models/BaseModel')();
var User = require("./User");


var Role = Bookshelf.Model.extend({
    tableName: "role",
    user: function() {
        retiurn this.belongsTo(User);
    }
});

module.exports = Role;
/*function(app) {
    return Bookshelf.Model.extend("Role", Role);
}*/
