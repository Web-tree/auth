import { v4 as uuidv4 } from 'uuid';

export class UserRepository {

    constructor() {
        this.dynamodb = new AWS.DynamoDB();
    }

    create(user) {
        let params = {
            Item: {
                id: {
                    S: uuidv4()
                },
                username: {
                    S: user.username
                },
                password: {
                    S: user.password
                }
            },
            ConditionExpression: 'attribute_not_exists(username)'
        };
        this.dynamodb.putItem(params);
    }
}
