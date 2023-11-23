package com.macmil.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/*
    The below unit test verifies provider API against the consumer's contract.
    The contract is obtained from the Pact Broker specified in @PactBroker annotation.
    The broker will verify all contracts held for service identified in @Provider annotation.
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("provider")
@PactBroker(url = "http://localhost:9292")
public class BrokerProviderPactTest {

    /*
        Set system property to enable publishing results to the Pact Broker.
    */
    @BeforeAll
    static void publishResults() {
        System.setProperty("pact.verifier.publishResults", "true");
    }

    /*
        The below method specifies provider API access, i.e. http://localhost:8080/.
    */
    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 8080, "/"));
    }

    /*
        Test template for contract verification that will be called for each pact
        against this provider.
    */
    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerification(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
