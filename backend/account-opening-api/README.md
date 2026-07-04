# Account Opening API (Java)

Spring Boot backend starter for the Indian Bank account-opening contract.

Prerequisite: JDK 11 or higher.

## Location

- Module path: `backend/account-opening-api`

## Features included

- `POST /v1/account-openings` with request validation
- `GET /v1/account-openings/{applicationId}` status endpoint
- Idempotency handling with `Idempotency-Key`
- Bearer token check (simple starter implementation)
- Webhook signature verification (`X-IB-Signature`, HMAC SHA-256)

## Configuration

Set values in `src/main/resources/application.yml` or environment variables:

- `app.security.bearer-token`
- `app.webhook.hmac-secret`

## Run

```bash
cd backend/account-opening-api
mvn spring-boot:run
```

Server starts on port `8088`.

## Example submit call

```bash
curl -X POST http://localhost:8088/v1/account-openings \
  -H "Authorization: Bearer change-me-in-prod" \
  -H "X-Correlation-Id: corr-1001" \
  -H "Idempotency-Key: idem-1001" \
  -H "Content-Type: application/json" \
  --data @../../docs/contracts/account-opening-request.example.json
```

## Example status call

```bash
curl -X GET http://localhost:8088/v1/account-openings/IB-2026-A7D9X2KQ \
  -H "Authorization: Bearer change-me-in-prod"
```

## Webhook signature helper

`WebhookSignatureVerifier#sign` generates the signature used in `X-IB-Signature`.
Use the same HMAC secret on sender and receiver.
