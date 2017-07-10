/**
 * Add documents into the Solr index.
 */

// Use `var solr = require('solr-client')` in your code 
var solr = require('./lib/solr');

// Create a client
var client = solr.createClient();

// Switch on "auto commit", by default `client.autoCommit = false`
client.autoCommit = true;

var docs = [];
for(var i = 0; i <= 10 ; i++){
   var doc = {
       id : 852 + i,
       firstName : "Title "+ i,
       lastName : "Text"+ i + "Alice"
   }
   docs.push(doc);
}

// Add documents

client.deleteAll(function(err,obj){
    if(err){
        console.log(err);
    }else{
        console.log(obj);
    }
});

// client.add(docs,function(err,obj){
//    if(err){
//       console.log(err);
//    }else{
//       console.log(obj);
//    }
// });
