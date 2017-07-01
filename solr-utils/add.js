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
// for(var i = 0; i <= 10 ; i++){
   var doc = { id: '7d60cef5-1a71-4072-8b7b-a3f4a1216ea4',
  employeeId: '1',
  firstName: 'Test',
  lastName: 'Test',
  contactNumber: 1234567890,
  address: 'sdcdsfe',
  policeVerified: 0,
  rating: 3.6667,
  userComments: 'cdsfdv,cscd,sdvdfvd,vdfvdfv::dvdfd::dferfrrfgbfg',
  category: 'S1',
  state: 'ANDHRA PRADESH',
  country: 'India',
  city: 'Hyderabad',
  areaName: 'Ada B.O',
  shortDescription: 'sddvdf' }
   docs.push(doc);
// }

// Add documents

client.deleteAll(function(err,obj){
    if(err){
        console.log(err);
    }else{
        console.log(obj);
    }
});

client.add(docs,function(err,obj){
   if(err){
      console.log(err);
   }else{
      console.log(obj);
   }
});
