'use strict';
const https = require('https');

module.exports.authorize = async (event, context, callback) => {
  return new Promise((resolve, reject) => {
    console.log("qwe")
    const token = 'asdasd';
    const options = {
      hostname: 'auth.webtree.org',
      port: 443,
      path: '/rest/checkToken',
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(token)
      }
    };

    let request = https.request(options, res =>{
      console.log(res.statusCode);
      res.on('data', data => {
        console.log("data: "+data);
        context.succeed();
      });
    });
    request.on('error', e => reject(e.message));

    request.write(token);
    request.end();
  });
};
