'use strict';
const https = require('https');

module.exports.authorize = async (event, context, callback) => {
  return new Promise((resolve, reject) => {
    const token = event.authorizationToken;
    console.log(token);
    const options = {
      hostname: 'auth.webtree.org',
      port: 443,
      path: '/rest/checkToken',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    };

    let request = https.request(options, res =>{
      console.log(res.statusCode);
      console.log(res.statusMessage);
      res.on('data', user => {
        if (res.statusCode === 200) {
          resolve(generatePolicy(JSON.parse(user), 'Allow', event.methodArn));
        } else {
          reject(JSON.parse(user));
        }
      });
    });
    request.on('error', e => reject(e.message));

    request.write(token);
    request.end();
  });
};

const generatePolicy = function(user, effect, resource) {
  let authResponse = {};

  //authResponse.principalId = user.username;
  if (effect && resource) {
    let policyDocument = {};
    policyDocument.Version = '2012-10-17';
    policyDocument.Statement = [];
    let statementOne = {};
    statementOne.Action = 'execute-api:Invoke';
    statementOne.Effect = effect;
    statementOne.Resource = resource;
    policyDocument.Statement[0] = statementOne;
    authResponse.policyDocument = policyDocument;
  }

  // Optional output with custom properties of the String, Number or Boolean type.
  authResponse.context = {
    "username": user.username
  };
  return authResponse;
};
