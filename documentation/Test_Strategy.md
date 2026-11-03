# Testing Strategy for the MS-Intro Microservices Application

## 1. Objective

The objective of this testing strategy is to ensure application stability, correctness, and reliable communication between microservices. The strategy combines unit, integration, component, contract, and end-to-end tests. Each test level has a specific purpose and helps detect defects as early as possible.

## 2. Testing Approach

### 2.1 Unit Tests

**Purpose:** Verify individual classes and business logic in isolation.

- Test service-layer methods, validation rules, DTO mapping, and error handling.
- Mock external dependencies such as repositories, HTTP clients, and Kafka producers where appropriate.
- Cover positive cases, negative cases, and boundary conditions.
- Use JUnit 5 and Mockito.

**Coverage target:** At least 80–90% line coverage for business logic, with 100% coverage for critical validation and decision branches where practical.

Unit tests should be fast, independent, and executed on every build.

### 2.2 Integration Tests

**Purpose:** Verify that application components work correctly with real infrastructure and framework integrations.

- Test repository operations against PostgreSQL.
- Verify database persistence and retrieval.
- Test Kafka producer and consumer configuration and message serialization.
- Test S3 operations, including uploading and retrieving audio files.
- Verify Spring configuration, transaction handling, and HTTP endpoint behavior.

Use JUnit 5, Spring Boot Test, and Testcontainers for PostgreSQL and Kafka where appropriate. Use LocalStack or an equivalent test environment for S3 integration tests.

**Coverage target:** Cover all critical infrastructure integrations and important persistence and messaging scenarios. Prioritize behavior over a fixed percentage.

### 2.3 Component Tests

**Purpose:** Verify the behavior of one microservice as a complete, independently testable component.

- Start the service with its required dependencies.
- Test public REST endpoints and business workflows.
- Verify HTTP status codes, response bodies, validation, and error handling.
- For `resource-service`, test audio upload and resource ID generation.
- For `song-service`, test song creation and retrieval, including its interaction with resource metadata.
- For `resource-processor`, test message handling and its processing workflow.

Use Cucumber and Gherkin to describe business scenarios in a readable format. Use REST Assured for HTTP requests and Testcontainers or dedicated test infrastructure for dependencies.

**Coverage target:** Cover the main business scenarios and important failure cases for each microservice, without duplicating every unit test.

### 2.4 Contract Tests

**Purpose:** Verify that microservices communicate according to agreed API and messaging contracts.

- Define HTTP request and response contracts between consumers and providers.
- Verify required fields, data types, status codes, and error responses.
- Define Kafka message contracts, including payload structure and required fields.
- Verify that changes to a producer do not break its consumers.
- Publish and verify contracts as part of CI/CD where practical.

Use Spring Cloud Contract or Pact. For this project, Pact can be used for HTTP consumer-provider contracts, while Kafka message contracts can be tested with an appropriate Pact message-testing setup or Spring Cloud Contract messaging support.

**Coverage target:** Cover every critical service-to-service interaction and every important message type, rather than every internal method.

### 2.5 End-to-End Tests

**Purpose:** Verify that complete business workflows function correctly across multiple microservices and infrastructure components.

Examples include:

1. Upload an MP3 file to `resource-service`.
2. Verify that the service stores the resource and returns its ID.
3. Verify that the appropriate Kafka event is published and processed by `resource-processor`.
4. Create or retrieve a song through `song-service` using the resource ID.
5. Verify that the final API response and persisted data are correct.

Use Cucumber, REST Assured, and a controlled environment containing the required services, databases, Kafka, and S3-compatible storage.

**Coverage target:** Cover a small set of critical happy paths and high-priority failure scenarios. Avoid repeating all lower-level tests in E2E tests because these tests are slower and more sensitive to infrastructure failures.

## 3. Recommended Test Distribution

The following distribution is an initial planning guideline based on the approximate share of test cases, not a strict requirement or a code coverage percentage.

| Test level | Approximate share | Main focus |
|---|---:|---|
| Unit | 60% | Business logic and validation |
| Integration | 20% | Databases, Kafka, S3, framework integration |
| Component | 10% | Individual microservice behavior |
| Contract | 7% | HTTP and messaging compatibility |
| End-to-end | 3% | Critical complete workflows |

The exact distribution can change depending on defect history, architectural risks, and the complexity of individual services.

## 4. Coverage and Quality Gates

The project will not require 100% unit and integration test coverage. Achieving 100% line coverage does not guarantee correct behavior, and testing every possible infrastructure combination can be expensive.

Instead, the following quality gates are recommended:

- Aim for at least 80–90% line coverage of business logic, measured with JaCoCo.
- Require tests for all critical business rules, validation branches, and important error paths.
- Require integration tests for each critical database, messaging, and storage integration.
- Require contract verification for critical service-to-service HTTP and Kafka interactions.
- Require all mandatory component and E2E scenarios to pass before release.
- Execute fast unit tests on every commit and run the broader integration, contract, component, and E2E suites in CI/CD.

Coverage percentages should be treated as indicators of test completeness, not as the sole measure of quality.

## 5. How the Strategies Work Together

Each testing level detects a different category of defect.

- **Unit tests** detect logic errors quickly and cheaply.
- **Integration tests** detect incorrect configuration and failures involving real infrastructure.
- **Component tests** verify that each microservice behaves correctly through its public interface.
- **Contract tests** detect incompatible changes between independently developed services.
- **End-to-end tests** verify that the complete system supports essential business workflows.

This combination provides broad confidence while keeping feedback fast and maintenance costs manageable. Most defects should be detected at lower test levels, while the smaller number of higher-level tests verifies interoperability and end-to-end business behavior.

## 6. Conclusion

The recommended strategy uses a testing pyramid with comprehensive unit tests, targeted integration tests, business-focused component tests, explicit HTTP and messaging contracts, and a small number of critical E2E scenarios. It prioritizes risk-based coverage and fast feedback rather than pursuing 100% coverage at every test level.

This approach improves application stability, reduces regression risk, and supports safe changes to individual microservices without requiring the entire system to be tested through slow end-to-end scenarios for every code change.