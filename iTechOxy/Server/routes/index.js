let UserController = require('../controllers/user');
let OxygenController = require('../controllers/oxygen');

let _ = require('underscore');

let userController = new UserController();
let oxygenController = new OxygenController();


let routes = [
    {
        path: '/api/v1/login',
        httpMethod: 'POST',
        middleware: [userController.login]
    },
    {
        path: '/api/v1/token',
        httpMethod: 'POST',
        middleware: [userController.updateToken]
    },
    {
        path: '/api/v1/oxygen',
        httpMethod: 'PUT',
        middleware: [oxygenController.log]
    },
    {
        path: '/api/v1/oxygen',
        httpMethod: 'GET',
        middleware: [oxygenController.get]
    },
    {
        path: '/api/v1/stat',
        httpMethod: 'GET',
        middleware: [oxygenController.stat]
    }
];

module.exports = function (app) {
    _.each(routes, function (route) {
        let args = _.flatten([route.path, route.middleware]);
        switch (route.httpMethod.toUpperCase()) {
            case 'GET':
                app.get.apply(app, args);
                break;
            case 'POST':
                app.post.apply(app, args);
                break;
            case 'PUT':
                app.put.apply(app, args);
                break;
            case 'DELETE':
                app.delete.apply(app, args);
                break;
            default:
                throw new Error('Invalid HTTP method specified for route ' + route.path);
        }
    });
};