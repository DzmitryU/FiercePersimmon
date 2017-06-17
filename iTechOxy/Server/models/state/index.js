module.exports = function (Sequelize, sequelize) {
    const State = sequelize.define('state', {
        id: {
            type: Sequelize.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        device_id: {
            type: Sequelize.INTEGER
        },
        oxygen: {
            type: Sequelize.INTEGER
        },
        temp: {
            type: Sequelize.INTEGER
        },
        time: {
            type: Sequelize.INTEGER,
            defaultValue: function () {
                return Math.floor(new Date().getTime() / 1000);
            }
        }
    });
};