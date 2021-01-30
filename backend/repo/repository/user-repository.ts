import {v4 as uuidv4} from 'uuid';
import {DynamoDB} from 'aws-sdk';
import {DocumentClient} from 'aws-sdk/lib/dynamodb/document_client';
import {User} from '../model/user';
import * as https from 'https';

export class UserRepository {

    constructor(
        private dynamodb = new DynamoDB.DocumentClient({
            maxRetries: 3,
            httpOptions: {
                agent: new https.Agent({
                    rejectUnauthorized: true,
                    secureProtocol: "TLSv1_method",
                    ciphers: "ALL"
                })
            }
        }),
    ) {
    }

    create(user: User) {
        const now = Date.now();
        const params: DocumentClient.PutItemInput = {
            TableName: process.env.DYNAMODB_TABLE,
            Item: {
                id: uuidv4(),
                username: user.username.toLowerCase(),
                createdAt: now,
                updatedAt: now,
                password: user.password,
            },
            ConditionExpression: 'attribute_not_exists(username)',
        };
        return this.dynamodb
            .put(params)
            .promise()
            .then(() => (params.Item));
    }

    findByName(username: string): Promise<User> {
        console.log("Searching user", username)
        const params: DocumentClient.ScanInput = {
            TableName: process.env.DYNAMODB_TABLE,
            ScanFilter: {
                username: {
                    ComparisonOperator: 'EQ',
                    AttributeValueList: [
                        username.toLowerCase(),
                    ],
                },
            },
        };
        return this.dynamodb
            .scan(params)
            .promise()
            .then(value => {
                if (value.Count < 1) {
                    return null;
                }
                return value.Items[0] as User;
            });
    }
}
