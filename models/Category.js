/**
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require('bookshelf').DB;
var Deferred  = require("promised-io/promise").Deferred;
var uuid      = require( 'node-uuid' );
var async     = require( 'async' );

var BaseModel = require( '../models/BaseModel' )();

var Category = BaseModel.extend({
    tableName: 'categories',
    defaults: function() {
        return {
            id: uuid.v4()
        };
    },

    // format and parse deflate and inflate JSON data
    format: function( attrs ) {
        if ( attrs.data )
            attrs.data = JSON.stringify( attrs.data );
        return attrs;
    },
    parse: function( attrs ) {
        if ( attrs.data )
            attrs.data = JSON.parse( attrs.data );
        return attrs;
    },

    // orders: function() {
    //     return this.hasMany( 'Order', 'account_id' );
    // },
    //
    // shipments: function() {
    //     return this.hasMany( 'Shipment', 'account_id' );
    // },
    //
    // locations: function() {
    //     return this.hasMany( 'Location', 'account_id' );
    // },
    //
    // shipment_templates: function() {
    //     return this.hasMany( 'ShipmentTemplate', 'account_id' );
    // },
    //
    // bees: function() {
    //     return this.hasMany( 'Bee', 'account_id' );
    // },
    //
    // contracts: function() {
    //     return this.hasMany( 'Contract', 'account_id' );
    // },
    //
    // roles: function() {
    //     return this.hasMany( 'Role', 'account_id' );
    // },
    //
    // users: function() {
    //     return this.belongsToMany( 'User', 'account_users', 'account_id', 'user_id' );
    // },
    //
    // // For many-to-many relationships, take advantage of the
    // // bookshelf attach() method, which will automatically create
    // // the join table for the relationship.
    // add_user: function( user ) {
    //     var dfd = new Deferred();
    //     this.users().attach( user ).then(
    //         function( u ) {
    //             dfd.resolve( u );
    //         },
    //         function( err ) {
    //             dfd.reject( err );
    //         }
    //     );
    //     return dfd.promise;
    // },
    //
    // // The opposite of add user, this dis-associates a user from
    // // an account.
    // remove_user: function( user ) {
    //     var dfd = new Deferred();
    //     this.users().detach( user ).then(
    //         function( u ) {
    //             dfd.resolve( u );
    //         },
    //         function( err ) {
    //             dfd.reject( err );
    //         }
    //     );
    //     return dfd.promise;
    // },
    //
    // // Bees now hang directly off of accounts via a foreign account_id
    // // key, but they can also belong to orders associated with this
    // // account.  So this method can be used to get all bees via the
    // // orders they are associated with.
    // ordered_bees: function() {
    //     var self = this;
    //     return Bookshelf.collection( 'Bees' ).forge().query( function( q ) {
    //         q.where({ 'orders.account_id': self.get( 'id' ) })
    //             .join( 'order_bees', 'order_bees.bee_id', '=', 'bees.id' )
    //             .join( 'orders', 'order_bees.order_id', '=', 'orders.id' );
    //     });
    // },

    // Do-all method for creating a new account
    create: function( options ) {
        var dfd = new Deferred();
        var category;
        async.series([
            function( cb ) {
                // Create the account
                Bookshelf.model('Category').forge({
                    name: options.name,
                }).find_or_create().then(function (a) {
                    console.log("category.........", a)
                    category = a;
                    category.save({data: options.metadata}, {patch: true}).then(function (a) {
                        cb();
                    });
                });
            }
        ], function( err ) {
            if ( err ) dfd.reject( err );
            else dfd.resolve( account );
        });

        return dfd.promise;
    }
});

module.exports = function( app ) {
    return Bookshelf.model( 'Category', Category );
};
