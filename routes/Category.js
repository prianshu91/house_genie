module.exports = function( app ) {
    var category = app.controllers.Category;

    console.log("inside routes.......")

    // Create a new category (name, description)
    app.post( '/category/create_category', category.create_category );

};