
var knex = require('knex')({
    client: 'mysql',
    debug: true,
    connection: {
        host: 'localhost',
        user: 'root',
        password: 'sachin',
        database: 'house_genie'
    }
});
var bookshelf = require('bookshelf')(knex);
var Bookshelf = require('bookshelf');
Bookshelf.DB = bookshelf;
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

module.exports = Bookshelf;