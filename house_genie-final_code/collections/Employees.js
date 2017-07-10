var Bookshelf = require('../db/bookshelf').DB;
//var BaseCollection = require("./BaseCollection");
var Employee = require('../models/Employee');

var Employees = Bookshelf.Collection.extend({
    model: Employee
});

module.exports = Employees;
/*function(app) {
    return Bookshelf.Collection('Employees', Employees);
};*/
