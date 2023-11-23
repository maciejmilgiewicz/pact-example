package com.macmil.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("provider")
@PactFolder("../consumer/build/pacts")
public class FolderProviderPactTest {

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
