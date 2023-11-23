# Contract Testing with Pact
This project demonstrates contract tests between two APIs, provider and consumer. It is a multi-module repository
using Java, Gradle, Spring, JUnit and Pact.

## Useful documents
- Consumer-driven contract testing - https://www.martinfowler.com/articles/consumerDrivenContracts.html
- Pact - https://pact.io/
- Pact CI/CD setup guide - https://docs.pact.io/pact_nirvana

## Project contents

### consumer
- Implements a simple consumer of provider API
- Includes a unit test defining a contract with provider API
- Contains an integration test utilising the contract against Pact's mock server
- Optionally uses the Pact Gradle plugin to publish the contract to the Pact Broker

### provider
- Implements a simple provider API
- Accesses the consumer's contract via a Pact Broker or file system to verify behaviour

### docker
- Contains scripts starting and stopping containers for Pact Broker and Postgres database needed by the broker

## Consumer Contract
Consumer contract describes an interaction between consumer and provider.

### provider
The provider API has a single endpoint - `"/product/{productName}"` - returning the following response:
```json
{
  "catalogueId": "1",
  "name": "widget",
  "price": 5.99,
  "manufacturer": "Company A",
  "inStock": "yes"
}
```

### consumer
However, the consumer only requires a subset of the provider's payload:
```json
{
  "catalogueId": "1",
  "name": "widget",
  "price": 5.99,
  "manufacturer": "Company A"
}
```

### The Contract
Based on the above, contract can be defined as follows:
```
GET /product/widget
```
Should produce a payload that matches:
```json
{
  "catalogueId": "1",
  "name": "widget",
  "price": 5.99,
  "manufacturer": "Company A"
}
```

Please note that matches does not mean equals, as the provider can return additional fields in its response. The key
concept behind contract testing is to verify that the provider's response includes data required by the consumer
and not to verify the whole payload. This allows the provider to support multiple consumers with different needs and
also to change the response over time even to the extent of braking its Open API specification. For example, `inStock`
field could be removed or changed to a boolean value without affecting the current consumer, as it does not include
this in the contract.

## Execution

### Creating consumer contract
- Building the consumer triggers unit tests execution, which create the contract file
- The contract is defined within a unit test
- After creation the contract is located under `consumer/build/pacts/consumer-provider.json`
- Since Pact contracts are cumulative, `clean` task needs to be executed before `build`

```
./gradlew consumer:clean consumer:build
```

### Publishing contract to Pact Broker
- Pact Broker and Postgres Docker containers need to be started before the contract can be published
- Pact Gradle plugin is used to publish the contract to the Pact Broker
- The Pact Broker UI is available at http://localhost:9292/
- The Pact Broker details for the Pact Gradle plugin are configured within `consumer/build.gradle`

```
pact {
    publish {
        pactBrokerUrl = 'http://localhost:9292'
    }
}
```

```
./docker/start.sh
./gradlew consumer:pactPublish
```

### Verifying the contract against the provider
- Provider can be tested either using local file system contract or a contract published to a Pact Broker
- Pact Broker verifications can be seen at http://localhost:9292/

```
./gradlew provider:cleanTest provider:test --tests com.macmil.provider.FolderProviderPactTest
./gradlew provider:cleanTest provider:test --tests com.macmil.provider.BrokerProviderPactTest
```

### Can I Deploy feature
Pact Broker feature checking if the contracts pass for all consumers, signalling that the service can be deployed,
if all tests pass.

## Evolving provider
TBC - evolving provider implementation breaking / non-breaking contract

## CI
On their [website](https://docs.pact.io/pact_nirvana) Pact provides a number of diagrams and guides on how to build CI
pipelines that include contract tests. The aim of these documents is to teach how to integrate common CI tools like Jenkins
and Pact Broker to implement automated processes publishing consumer contracts, verifying them against providers and using
Can I Deploy in order to avoid integration issues while deploying APIs to environments.
