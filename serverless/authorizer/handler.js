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
      res.on('data', data => {
        data = JSON.parse(data);
        if (res.statusCode === 200) {
          resolve(generatePolicy(data, 'Allow', event.methodArn));
        } else {
          data.statusCode = res.statusCode;
          callback(res.statusMessage, data.message);
          resolve();
        }
      });
    });
    request.on('error', e => reject(JSON.stringify(e)));

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
    "id": user.id,
    "username": user.username
  };
  return authResponse;
};
