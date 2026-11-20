# Insurance Policy Event Platform

A portfolio implementation of an event-driven insurance policy service. It exposes REST endpoints,
persists policy records, writes events to a transactional outbox, and publishes them to Kafka.
A consumer processes policy events and updates an Elasticsearch search index.

## Architecture

1. `POST /api/policies` validates and stores a policy.
2. The policy and outbox event are saved in one database transaction.
3. A scheduled publisher sends unpublished outbox events to Kafka.
4. A Kafka consumer indexes the policy document in Elasticsearch.
5. `GET /api/policies/{policyNumber}` retrieves the policy from the API database.

## Stack

Java 17, Spring Boot 3.4, Spring Data JPA, Spring Kafka, Elasticsearch Java API Client,
MySQL 8, Kafka (KRaft), Docker Compose, Maven, JUnit 5.

## Run locally

Requirements: Docker Desktop and Java 17+ with Maven.

```bash
docker compose up -d
mvn spring-boot:run
```

The API is available at `http://localhost:8080`.

Create a policy:

```bash
curl -X POST http://localhost:8080/api/policies   -H "Content-Type: application/json"   -d '{"policyNumber":"DEMO-POL-1001","insuredName":"Northwind Traders","lob":"Property","premium":1250.50,"effectiveDate":"2026-01-01","expirationDate":"2027-01-01"}'
```

Check the policy:

```bash
curl http://localhost:8080/api/policies/DEMO-POL-1001
```

Search Elasticsearch directly:

```bash
curl http://localhost:9200/insurance-policies/_search?pretty
```

## Design notes

- The transactional outbox avoids the dual-write problem between a relational database and Kafka.
- The publisher marks an event as published only after Kafka acknowledges it.
- Consumers should be idempotent; this demo uses the policy number as the Elasticsearch document ID.
- In production, add authentication, schema versioning, retry topics, dead-letter handling,
  observability, migrations, and stronger validation.

## Tests

```bash
mvn test
```

## Configuration

Defaults are in `src/main/resources/application.yml`. Environment variables can override the
database, Kafka, and Elasticsearch endpoints. Never commit real credentials.
