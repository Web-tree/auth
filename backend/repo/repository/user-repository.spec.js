import {UserRepository} from "./user-repository.mjs";

describe("UserRepository", () => {
    let userRepository
    beforeEach(() => {
        userRepository = new UserRepository()
    });
    it('should save user in dynamo', async () => {
        const user = await userRepository.create({
            username: 'tstUser',
            password: 'pwd'
        })
        new AWS.DynamoDB().getItem({
            Key: {
                id: {
                    S: user.id
                }
            }
        })
    });
})
