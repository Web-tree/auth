const Sequelize = require('sequelize');

module.exports = (sequelize, type) => {
    return sequelize.define('user', {
        id: {
            primaryKey: true,
            type: Sequelize.UUID,
            defaultValue: require('uuid/v4')
        },
        username: {type: Sequelize.STRING, unique: true},
        password: type.STRING
    })
};
