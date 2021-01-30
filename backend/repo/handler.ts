'use strict';

import {UserRepository} from './repository/user-repository';

module.exports.create = async (event) => {
  try {
    console.log(event);
    const userRepository = new UserRepository();
    const user = JSON.parse(event.body);
    if (await userRepository.findByName(user.username)) {
      const message = `User ${user.username} already exists`;
      console.log(message)
      return {
        statusCode: 400,
        body: JSON.stringify(message)
      }
    }
    const createdUser = await userRepository.create(user);
    console.log(createdUser)
    return {
      statusCode: 200,
      body: JSON.stringify(createdUser)
    }
  } catch (err) {
    console.error(err);
    return {
      statusCode: err.statusCode || 500,
      headers: { 'Content-Type': 'text/plain' },
      body: 'Could not create the user.'
    }
  }
};

module.exports.findByName = async (event) => {
  try {
    const user = await new UserRepository().findByName(event.pathParameters.username);
    if (!user) return {
      statusCode: 404,
      body: `User with name: ${event.pathParameters.username} was not found`
    }
    return {
      statusCode: 200,
      body: JSON.stringify(user)
    }
  } catch (err) {
    return {
      statusCode: err.statusCode || 500,
      headers: { 'Content-Type': 'text/plain' },
      body: err.message || 'Could not fetch the user.'
    }
  }
};
