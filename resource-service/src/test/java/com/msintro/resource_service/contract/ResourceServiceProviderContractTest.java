package com.msintro.resource_service.contract;


import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.msintro.resource_service.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.kafka.bootstrap-servers=localhost:9092",
                "spring.kafka.admin.auto-create=false",
                "spring.kafka.listener.auto-startup=false",
                "spring.cloud.discovery.enabled=false",
                "eureka.client.enabled=false",
                "spring.autoconfigure.exclude=" +
                        "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
                        "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
        }
)
@Provider("test_provider")
@PactFolder("pacts")
public class ResourceServiceProviderContractTest {

    @MockitoBean
    private ResourceService resourceService;

    @BeforeEach
    void setTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 8081));
    }

    @State("resource 102 exists")
    void resource102Exists() {
        when(resourceService.get("102"))
                .thenReturn(new byte[]{
                        0x01, 0x02, 0x03, 0x04
                });
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
