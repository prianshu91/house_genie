var Bookshelf = require('../db/bookshelf').DB;
var Deferred = require("promised-io/promise").Deferred;
var uuid = require('uuid');
var async = require('async');
var BaseModel = require('../models/BaseModel')();
var Login = require("./Login");
var Employee = require("./Employee");
var Role = require("./Role");
var Rating = require("./Rating");

var User = Bookshelf.Model.extend({
    tableName: "user",
    role: function() {
        return this.hasMany(Role);
    },
    rating: function() {
        return this.hasOne(Rating);
    },
    login: function() {
        return this.hasMany(Login);
    }
});

module.exports = User;
/*function(app) {
    return Bookshelf.Model.extend("User", User);
}*/
