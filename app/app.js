/**
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require( 'bookshelf' );


var express = require( 'express' );
var loader  = require( 'express-load' );

// Create the main express app
var app = module.exports = express();

// Boot the app (see boot.js), then use the very
// cool express-load utility to suck in all of the
// modules located under lib, models, collections, etc.
// and finally, start the server!
    loader( 'models' )
        .then( 'collections' )
        .then( 'controllers' )
        .then( 'routes' )
        .into( app );
    app.listen( app.get( 'port' ), function() {
        console.log('Roambee web server listening on port ' + app.get('port'));
    });


Bookshelf.DB = Bookshelf.initialize({
    client: 'mysql',
    debug: true,
    connection: {
        host: 'localhost',
        user: 'root',
        password: 'infoobjects1',
        database: 'house_genie'
    }
});
/*
 * Add the registry plugin to handle table relations definitions without
 * curcular dependencies.
 */
Bookshelf.DB.plugin( 'registry' );
// THIS FIXES A BUG IN BS MODEL RESOLUTION VISA the registry plugin!!
Bookshelf.DB.model( '_unused', {} );
Bookshelf.DB.collection( '_unused', {} );
/*
 * Add the visibility plugin to hide model fields on toJSON
 */
Bookshelf.DB.plugin( 'visibility' );