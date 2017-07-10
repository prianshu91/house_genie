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

module.exports = knex;
