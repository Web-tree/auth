const DataTypes = require('sequelize/lib/data-types');

module.exports = (sequelize, type) => {
    return sequelize.define('user', {
        id: {
            type: DataTypes.UUID,
            primaryKey: true
        },
        username: type.STRING,
        password: type.STRING
    })
};
