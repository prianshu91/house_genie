/**
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require('bookshelf').DB;
var BaseCollection = require( './BaseCollection' )();

require( '../models/Employee' );

var Employees = BaseCollection.extend({
    model: Bookshelf.model( 'Employee' )
});

module.exports = function( app ) {
    return Bookshelf.collection( 'Employees', Employees );
};

