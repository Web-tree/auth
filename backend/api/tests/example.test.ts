import {describe, expect, it} from "@jest/globals";

describe('who tests the tests?', () => {
    it('can run a test', () => {
        expect.hasAssertions();
        expect(1).toBe(1);
    });
});
