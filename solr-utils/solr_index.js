/**
 * Created by priyanshu on 13/6/17.
 */

var express = require('express');
var loader = require('express-load');
var fs = require('fs');
var uuid = require( 'node-uuid' );
//var elastic = require('./elasticlib');
var async = require('async');
var _ = require("lodash");
var moment = require('moment');
// Create the main express app
var app = module.exports = express();


// Use `var solr = require('solr-client')` in your code
var solr = require('./lib/solr');

// Create a client
var client = solr.createClient();

// Switch on "auto commit", by default `client.autoCommit = false`
client.autoCommit = true;

var knex = require('knex')({
    client: 'mysql',
    debug: true,
    connection: {
        host: 'localhost',
        user: 'root',
        password: 'infoobjects1',
        database: 'house_genie'
    }
});
var bookshelf = require('bookshelf')(knex);
var Bookshelf = require('bookshelf');
Bookshelf.DB = bookshelf;
app.set('schema', bookshelf);

Bookshelf.DB.plugin( 'registry' );
// THIS FIXES A BUG IN BS MODEL RESOLUTION VISA the registry plugin!!
Bookshelf.DB.model( '_unused', {} );
Bookshelf.DB.collection( '_unused', {} );
/*
 * Add the visibility plugin to hide model fields on toJSON
 */
Bookshelf.DB.plugin( 'visibility' );

// Boot the app (see boot.js), then use the very
// cool express-load utility to suck in all of the
// modules located under lib, models, collections, etc.
// and finally, start the server!
loader( 'models' )
    .then( 'collections' )
    .then( 'controllers' )
    .then( 'routes' )
    .into( app );
application();
/* E N D */

function application() {
    // var schema = require('bookshelf').DB;
    // main();
    // select first_name,last_name,contact_number,address,police_verified,categories.name as category,rating,employee_rating.comment as user_comments from categories Left join
    // category_employee_mapping on category_employee_mapping.category_id = categories.id Left join employee_profiles on category_employee_mapping.employee_id = employee_profiles.id
    // Left join employee_rating on employee_profiles.rating_id = employee_rating.id
    // left join employee_location_mapping on employee_location_mapping.employee_id = employee_profiles.id left join location on location.id = employee_location_mapping.location_id

    // function main() {
    // console.log("scheam", schema.collection('Categories'));

    // // schema.collection('Categories').forge().fetch({
    // //     withRelated: ['employees']
    // // }).then(function (results) {
    // //     console.log("results........"+results.toArray());
    // //     var shipments = results.toArray()
    // //     async.each(shipments, function (shipment, cb) {
    // //         console.log("recors",shipment.get("id"))
    // //         async.each(shipment.related("employees").toArray(),function (employee, cb) {
    // //             console.log("recors",employee.get("first_name"))
    // //         }, function (err) {
    // //             cb(err);
    // //         });
    // //     }, function (err) {
    // //         cb(err);
    // //     });
    // }).catch(function name(err) {
    //     app.log.error(err);
    // });

    async.series([
        function(series1Callback){
            //Deleting entire data

            console.log("deleteting data........");
            client.deleteAll(function(err,obj){
                if(err){
                    console.log(err);
                }else{
                    console.log("all records deleted");
                    series1Callback();
                }
            });
        },
        function(series1Callback){
            console.log("re-inserting data..........");
            //Re-inserting data
            knex.select('employees.id  as employee_id','employees.description as employee_description','employees.experience','employees.imageUrl','employees.first_name', 'employees.last_name', 'employees.contact_number', 'employees.police_verified','employees.address', 'categories.name as category', 'user_comments', 'avg_rating','location.*')
                .from('categories')
                .leftJoin('category_employees', 'category_employees.category_id', '=', 'categories.id ')
                .leftJoin('employees', 'category_employees.employee_id', '=', 'employees.id')
                .leftJoin(
                    knex.raw('(select employee_id, group_concat(`ratings`.`comment` SEPARATOR "::") as user_comments, avg(rating) as avg_rating from ratings group by employee_id) as employee_rating'),
                    'employees.id', '=', 'employee_rating.employee_id')
                .leftJoin('employee_locations', 'employee_locations.employee_id', '=', 'employees.id')
                .leftJoin('location', 'employee_locations.employee_id', '=', 'location.id')
                .then(function (results) {
                    console.log("records to insert:: ",results.length);
                    var docs = [],employyeIds = [];
                    async.series([
                        function (series2Callback) {
                            if (results && results.length > 0) {
                                var i = 1;
                                async.each(results, function (row, eachCallback) {
                                    employyeIds.push(row)
                                    var doc = {
                                        id: uuid.v4(),
                                        employeeId: row["employee_id"],
                                        firstName: row["first_name"],
                                        lastName: row["last_name"],
                                        contactNumber: row["contact_number"],
                                        address: row["address"],
                                        policeVerified: row["police_verified"],
                                        rating: row["avg_rating"],
                                        userComments: row["user_comments"],
                                        category: row["category"],
                                        state: row["state"],
                                        country: "India",
                                        city: row["city"],
                                        areaName: row["area_name"],
                                        shortDescription: row["employee_description"],
                                        experience: row["experience"],
                                        imageUrl: row["imageUrl"]

                                    };

                                    console.log("doc...",doc)
                                    docs.push(doc);
                                    if( i === results.length){
                                        series2Callback();
                                    }
                                    i++;
                                }, function (err) {
                                    if(err) series2Callback(err);
                                });
                            }
                        },
                        function (series2Callback) {
                            // console.log("docs",docs)
                            client.add(docs, function (err, obj) {
                                if (err) {
                                    series2Callback(err);
                                } else {
                                    console.log("records inserted::",docs.length)
                                }
                            })
                        }
                    ], function (err) {
                        if (err) console.log(err);
                    });
                }).catch(function (err) {
                console.log(err)
            });
        }
    ], function (err) {
        if (err) console.log(err);

    });

}
// }
