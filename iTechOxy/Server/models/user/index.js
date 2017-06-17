module.exports = function (Sequelize, sequelize) {
    const User = sequelize.define('user', {
        id: {
            type: Sequelize.INTEGER,
            primaryKey: true
        },
        room: {
            type: Sequelize.STRING
        },
        access_token: {
            type: Sequelize.STRING
        },
        refresh_token: {
            type: Sequelize.STRING
        },
        name: {
            type: Sequelize.STRING
        }
    });
};