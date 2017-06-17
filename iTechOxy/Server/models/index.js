let Sequelize = require('sequelize');
let config = require('../config.json');

class DBManager {

    setup(connection) {
        return new Promise((resolve, reject) => {
            try {
                require('./user')(Sequelize, connection);
                require('./device')(Sequelize, connection);
                require('./state')(Sequelize, connection);

            } catch (ex) {
                console.log(ex);
                reject(ex);
            }
            resolve();
        });
    }

    getConnection() {
        return new Promise((resolve, reject) => {
            if (this.connection) {
                resolve(this.connection);
            }
            this.connection = new Sequelize(config.database.database, config.database.user, config.database.password, {
                host: config.database.host,
                port: config.database.port,
                dialect: config.database.protocol,
                define: {
                    freezeTableName: true,
                    underscored: true,
                    underscoredAll: true,
                    timestamps: false
                },
                pool: {
                    max: 10,
                    min: 0,
                    idle: 10000
                }
            });
            this.setup(this.connection).then(() => resolve(this.connection));
        });
    }

}

module.exports = DBManager;