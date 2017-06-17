module.exports = function (Sequelize, sequelize) {
    const User = sequelize.define('user', {
        id: {
            type: Sequelize.INTEGER,
            primaryKey: true
        },
        room: {
            type: Sequelize.STRING
        },
        token: {
            type: Sequelize.STRING
        }
    });
};