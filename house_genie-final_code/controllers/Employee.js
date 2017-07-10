/**
 * Created by priyanshu on 7/6/17.
 */

var solr = require('./../solr-utils/lib/solr');
var client = solr.createClient();

var addValuesToJson = function(newJson,attributeName,attributeValue){
    if (attributeValue) {
        newJson[attributeName] = attributeValue
    }
    return newJson;
}

var addValuesToMatchFilter = function(query,attributeName,attributeValue){
    if(attributeValue){
        query.matchFilter(attributeName,attributeValue)
    }
    return query
}

module.exports = function( app ) {
    var controller = {};
    var log = app.log;

    controller.get_employee_details = function (req, res, next) {

        var city = req.param('city');
        var state = req.param('state');
        var area = req.param('area');
        var category = req.param('category');
        var subCategory = req.param('sub_category');
        var policeVerified = req.param('police_verified');
        var withImages = req.param('with_images');
        var expStartValue = req.param('exp_start_value');
        var expEndValue = req.param('exp_end_value');
        var ratingStartValue = req.param('rating_start_value');
        var ratingEndValue = req.param('rating_end_value');
        var offset = req.param('offset');
        var limit = req.param('limit');

        var queryJson = {};
        var queryFilter = {}

        addValuesToJson(queryJson,"category",category);
        addValuesToJson(queryJson,"subCategory",subCategory);
        

        var query = client.createQuery()
                  .q(queryJson)
                  .start(offset)
                  .rows(limit);

        addValuesToMatchFilter(query,"city",city);
        addValuesToMatchFilter(query,"state",state);
        addValuesToMatchFilter(query,"areaName",area);
        addValuesToMatchFilter(query,"policeVerified",policeVerified);
        if(withImages)
            addValuesToMatchFilter(query,"imageUrl","*");

        if(expStartValue && expEndValue){
            query.rangeFilter({ field : 'experience', start : expStartValue, end : expEndValue})
        }

        if(ratingStartValue && ratingEndValue){
            query.rangeFilter({ field : 'rating', start : ratingStartValue, end : ratingEndValue})
        }

                  console.log("query....",query,queryFilter)

        client.search(query, function (err, obj) {
            if (err) {
                console.log(err);
            } else {
                res.jsonp(obj.response);
            }
        });


    };
    return controller;
};