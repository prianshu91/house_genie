/**
 * Created by priyanshu on 7/6/17.
 */

var schema = require( 'bookshelf' ).DB;
var async = require("async")

console.log("schema....",schema)

module.exports = function( app ) {
    var controller = {};
    var log = app.log;

    controller.create_category = function( req, res, next ) {

        var name = req.param( 'name' );
        var desc = req.param( 'description' );

        console.log("inside controller..........",req.body,name,desc)
        async.series([
            function( cb ) {
                // Make sure account names are unique
                schema.model( 'Category' ).forge({ name: name }).fetch().then( function( a ) {
                    if ( a ) cb( new Error( 'The category name \'' + name + '\' is already taken.' ) );
                    else cb();
                }, cb );
            },
            function( cb ) {
                schema.model( 'Category' ).forge()
                    .save({ name: name, description: desc}).then( function( category , error) {
                    if(error) cb(error);
                    else cb();
                }, cb );
            }
        ], function( err ) {
            if ( err ) {
                console.log("errr........",err)
                next( err );
            }
            else res.jsonp( "created" );
        });
    };

    return controller;
};


