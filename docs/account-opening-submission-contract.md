# Indian Bank Account Opening - Submission Contract

This contract defines how account-opening payloads move from the Adaptive Form to enterprise backend services.

## Contract Files

- OpenAPI specification: [contracts/account-opening-submission.openapi.yaml](./contracts/account-opening-submission.openapi.yaml)
- Request example: [contracts/account-opening-request.example.json](./contracts/account-opening-request.example.json)
- Accepted response example: [contracts/account-opening-response.example.json](./contracts/account-opening-response.example.json)
- Webhook event example: [contracts/account-opening-webhook-event.example.json](./contracts/account-opening-webhook-event.example.json)
- Java backend starter implementation: [../backend/account-opening-api/README.md](../backend/account-opening-api/README.md)

## Endpoint

- Method: `POST`
- URL: `https://api.indianbank.example.com/v1/account-openings`
- Auth: OAuth2 Client Credentials (`account.openings.write`)
- Required headers:
  - `X-Correlation-Id`
  - `Idempotency-Key`

## Response Model

- `202 Accepted`: payload accepted with `applicationId`
- `400`: schema/field validation failure
- `401`: token/auth failure
- `409`: duplicate idempotency key
- `422`: business-rule validation failure

## Webhook Contract

Backend can publish async status events to a callback endpoint after acceptance.

Recommended event types:
- `ACCOUNT_OPENING_STATUS_CHANGED`
- `ACCOUNT_OPENING_ACTION_REQUIRED`
- `ACCOUNT_OPENING_COMPLETED`

## Form to API Mapping

| Form Field | API Path |
|---|---|
| firstName | applicant.firstName |
| lastName | applicant.lastName |
| dateOfBirth | applicant.dateOfBirth |
| panNumber | applicant.panNumber |
| aadhaarNumber | applicant.aadhaarNumber |
| mobileNumber | contact.mobileNumber |
| emailAddress | contact.emailAddress |
| accountType | account.accountType |
| openingDeposit | account.openingDeposit |
| debitCardRequired | account.debitCardRequired |
| additionalServices | account.additionalServices[] |
| consent | declaration.consentAccepted |

## Implementation Notes

1. Generate `X-Correlation-Id` per request and propagate to all downstream logs.
2. Generate deterministic `Idempotency-Key` per application submit action.
3. Encrypt PAN and Aadhaar at rest using bank-approved KMS.
4. Return `applicationId` in `202` response only after persistence succeeds.
5. Emit webhook retries with exponential backoff and HMAC signature.
