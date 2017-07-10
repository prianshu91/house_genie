/*
 **
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require('bookshelf').DB;
var Location = require('../models/Location');

var Locations = Bookshelf.Collection.extend({
    model: Location
});

module.exports = Locations;

/*function(app) {
    return Bookshelf.Collection('Locations', Locations);
};*/
