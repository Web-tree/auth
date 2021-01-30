
const path = require('path');
const slsw = require('serverless-webpack');
const BbPromise = require('bluebird');
const webpack = require('webpack')

module.exports = BbPromise.try(() => {
    return slsw.lib.serverless.providers.aws.getAccountId()
        .then(accountId => ({
            entry: slsw.lib.entries,
            mode: slsw.lib.webpack.isLocal ? "development" : "production",
            resolve: {
                extensions: [
                    '.js',
                    '.json',
                    '.ts',
                    '.tsx'
                ]
            },
            plugins: [
                new webpack.DefinePlugin({
                    AWS_ACCOUNT_ID: `${accountId}`,
                }),
            ],
            output: {
                libraryTarget: 'commonjs',
                path: path.join(__dirname, '.webpack'),
                filename: '[name].js'
            },
            target: 'node',
            module: {
                rules: [
                    {
                        test: /\.ts(x?)$/,
                        use: [
                            {
                                loader: 'ts-loader',
                                options: {
                                    transpileOnly: true
                                }
                            }
                        ],
                    }
                ]
            },
            externals: [{
                'aws-sdk': 'aws-sdk',
                'typescript': 'typescript',
                '@shelf/jest-dynamodb': '@shelf/jest-dynamodb'
            }]
        }));
});
