/**
 * Created by priyanshu on 7/6/17.
 */

var Bookshelf = require('bookshelf').DB;
var Deferred  = require("promised-io/promise").Deferred;
var async     = require( 'async' );
var _         = require('lodash');

function next_offset( m, c ) {
    if ( parseInt( m.offset ) + parseInt( m.limit ) < parseInt( c ) )
        return parseInt( m.offset ) + parseInt( m.limit );
    else
        return null
}

var BaseCollection = Bookshelf.Collection.extend({
    // Can be used to do pages queries. Usage:
    // projects.paged({where: ['title', 'like', 'foo%'], offset: 10, limit: 2}).then()
    // The returned result looks like
    // { count: total records available,
    //   next_offset: the next offset to request, or null if all the data has been returned
    //   rows:  the fetched rows }
    //
    paged: function(modifiers, fetchOpts) {
        var self = this;
        var dfd = new Deferred();
        var knex = Bookshelf.knex(_.result(this, 'tableName'));
        if ( modifiers && modifiers.offset ) modifiers.offset = Number( modifiers.offset );
        if ( modifiers && modifiers.limit ) modifiers.limit = Number( modifiers.limit );
        async.series([
            function( cb ) {
                self.query(modifiers).fetch(fetchOpts).then( function( res ) {
                    cb( null, res );
                });
            },
            function( cb ) {
                if ( modifiers && modifiers.where )
                    knex.where.apply(knex, modifiers.where).count('*').then( function( res ) {
                        cb( null, res );
                    });
                else
                    knex.count('*').then( function( res ) {
                        cb( null, res );
                    });
            },
        ], function(err, resp) {
            dfd.resolve({
                rows: resp[0],
                count: resp[1][0]['count(*)'],
                next_offset: next_offset( modifiers, resp[1][0]['count(*)'] )
            });
        });
        return dfd.promise;
    },
});
module.exports = function( app ) {
    if ( ! Bookshelf.model( 'BaseCollection' ) )
        return Bookshelf.model( 'BaseCollection', BaseCollection );
    else
        return Bookshelf.model( 'BaseCollection' );
};

