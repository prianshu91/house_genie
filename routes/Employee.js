module.exports = function( app ) {
    var employee = app.controllers.Employee;

    console.log("inside routes.......")

    // Create a new category (name, description)
    app.post( '/solr/employees/list', employee.get_employee_details );
    // app.post( '/solr/facet', employee.get_count_resul)

};