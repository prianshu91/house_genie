/**
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require('bookshelf').DB;
var BaseCollection = require( './BaseCollection' )();

require( '../models/Category' );

var Categories = BaseCollection.extend({
    model: Bookshelf.model( 'Category' )
});

module.exports = function( app ) {
    return Bookshelf.collection( 'Categories', Categories );
};

