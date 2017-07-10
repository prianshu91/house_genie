//Importing pre-dedefind modules
var log = require("log4js").getLogger();
var Deferred = require("promised-io/promise").Deferred;
//Importing other modules
var Category = require("../../models/Category");
//Functions Implemenation
//Saving category
function insertCategory(category_record) {

    let category = new Category();
    var dfd = new Deferred();

    category.save(category_record).then(function(obj) {
        if (obj) dfd.resolve(obj);
        else {
            self.save().then(
                function(obj) {
                    dfd.resolve(obj);
                },
                function(err) {
                    dfd.reject(err);
                }
            );
        }
    }, function(err) { dfd.reject(err) });
    return dfd.promise;
}

//Deleting category
function deleteCategory(id) {
    let category = new Category({ id: id });
    var dfd = new Deferred();

    Category.where({ id: id }).count().
    then(function(count) {
        if (count == 0 || count === 0) {
            dfd.reject("No record found!!Unable to delete");
        } else {
            category.destroy().then(function(obj) {
                if (obj) dfd.resolve(obj);
                else dfd.reject(err);

            });
        }
    });

    return dfd.promise;
}

//updating category
function updateCategory(category_record) {

    let category = new Category({ id: category_record.id });
    let dfd = new Deferred();

    Category.where({ id: category_record.id }).count().
    then(function(count) {
        if (count == 0 || count === 0) {
            dfd.reject("No record found!!Unable to update");
        } else {
            category.save({ name: category_record.name, description: category_record.desc }, { method: "update" }).
            then(function(obj) {
                    dfd.resolve(obj);
                },
                function(err) {
                    dfd.reject(err);
                });
        }
    });
    return dfd.promise;
}

//searching category
function getCategoryDetails(id) {
    let dfd = new Deferred();

    Category.where({ id: id }).fetch().
    then(function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(obj);
        });
    return dfd.promise;
}

//All Categories
function getAllCategories() {
    let dfd = new Deferred();
    let category = new Category();

    Category.fetchAll().then(
        function(obj) {
            dfd.resolve(obj);
        },
        function(err) {
            dfd.reject(err);
        });
    return dfd.promise;
}
var CategoryService = {
    Insert: insertCategory,
    Delete: deleteCategory,
    Update: updateCategory,
    Search: getCategoryDetails,
    View: getAllCategories
};

//Exporting modules
module.exports = CategoryService;
