'use strict';

const connectToDatabase = require('./db');

function HTTPError (statusCode, message) {
  const error = new Error(message);
  error.statusCode = statusCode;
  return error
}

module.exports.create = async (event) => {
  try {
    const { User } = await connectToDatabase();
    console.log(event);
    const user = await User.create(JSON.parse(event.body));
    return {
      statusCode: 200,
      body: JSON.stringify(user)
    }
  } catch (err) {
    console.log(err);
    return {
      statusCode: err.statusCode || 500,
      headers: { 'Content-Type': 'text/plain' },
      body: 'Could not create the user.'
    }
  }
};

module.exports.findByName = async (event) => {
  try {
    const { User } = await connectToDatabase();
    const note = await User.findOne({
      where:{ username: event.pathParameters.username }
    });
    if (!note) throw new HTTPError(404, `User with name: ${event.pathParameters.username} was not found`);
    return {
      statusCode: 200,
      body: JSON.stringify(note)
    }
  } catch (err) {
    return {
      statusCode: err.statusCode || 500,
      headers: { 'Content-Type': 'text/plain' },
      body: err.message || 'Could not fetch the user.'
    }
  }
};