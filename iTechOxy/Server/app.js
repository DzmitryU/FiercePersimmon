const express = require('express');
const app = express();
const bodyParser = require('body-parser');

const DBManager = require('./models/index');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

let models;
let dbManager = new DBManager();
dbManager.getConnection().then((db) => {
    models = db.models;
});

app.get('/', function (req, res) {
    res.send('Hello World!')
});

app.post('/api/v1/token', function (req, res) {
    console.log(req.body);
    models.user.findAll().then((result) => {
        console.log(result);
    });

    res.status(200).json({message: 'Saved', success: true});
});

app.put('/api/v1/oxygen', function (req, res) {
    console.log(req.body);
    res.status(200).json({message: 'Logged', success: true});
});

app.listen(3000, function () {
    console.log('Example app listening on port 3000!')
});
