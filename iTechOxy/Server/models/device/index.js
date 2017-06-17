module.exports = function (Sequelize, sequelize) {
    const Device = sequelize.define('device', {
        id: {
            type: Sequelize.INTEGER,
            primaryKey: true
        },
        room: {
            type: Sequelize.STRING
        }
    });
};