/**
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require('../db/bookshelf').DB;
//var BaseCollection = require("./BaseCollection");

var Employees = Bookshelf.Collection.extend({
    model: require('../models/Category')
});

module.exports = Employees;
/*function(app) {
    return Bookshelf.Collection('Categories', Categories);
};*/
