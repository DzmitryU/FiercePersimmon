const express = require('express');
const app = express();
const bodyParser = require('body-parser');

const DBManager = require('./models/index');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

let dbManager = new DBManager();
dbManager.getConnection().then((db) => {
    global.models = db.models;
});

require('./routes')(app);

app.listen(3000, function () {
    console.log('Example app listening on port 3000!')
});
