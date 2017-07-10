var Bookshelf = require('../db/bookshelf').DB;
var BaseModel = require('../models/BaseModel')();
var User = require("./User");

var Login = BaseModel.extend({
    tableName: "login_status",
    user: function() {
        return this.belongsTo(User);
    }
});

module.exports = function(app) {
    Bookshelf.Model.extend("Login", Login);
}
