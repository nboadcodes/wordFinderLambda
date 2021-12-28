DictionaryTrie Lambda Function
Overview

Inspired by the [NYT's spelling bee puzzle](https://www.nytimes.com/puzzles/spelling-bee), this program is an engine designed to generate dictionary words using a set of letters. Additional rules for the game that the engine follows are:
1) Each letter may be used more than once to form a word
2) There is a minimum length for the formed word
3) One letter is required to be included somewhere in the word

To find the solution words, the program generates a trie from a wordlist, which can then be serialized/deserialized for efficient reuse. The wordlist used can be found [here](https://github.com/dwyl/english-words/blob/master/words.txt), but unfortunately it contains a lot of non-dictionary words. 

The program is integrated into the AWS Lambda service to be uploaded as a Lambda Function, which is then invoked by the AWS API Gateway. An input handler accomplishes this integration by interpreting AWS API Gateway generated input and using the engine accordingly. A response that bundles the solution words is returned; to ensure the response conforms to AWS API Gateway standards, the APIGatewayProxyResponseEvent class was used, which can be found [here](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-events/src/main/java/com/amazonaws/services/lambda/runtime/events/APIGatewayProxyRequestEvent.java).