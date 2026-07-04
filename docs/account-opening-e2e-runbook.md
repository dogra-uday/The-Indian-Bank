# Indian Bank Account Opening - End-to-End Runbook (Step-by-Step)

Primary guide: use [account-opening-master-runbook.md](./account-opening-master-runbook.md) first.

This runbook is written as a follow-along checklist. Do each step in order.

## What you will set up

1. Local website preview (AEM Edge proxy)
2. Local Java backend API (submission + status)
3. Contract validation using sample payloads
4. Content publish path for Preview and Live

## Before you start

You need these installed:

1. Node.js 18+
2. Java 11+
3. Maven 3.8+
4. AEM CLI (`@adobe/aem-cli`)

Commands to check:

```bash
node -v
java -version
mvn -v
aem --version
```

If `aem` is missing:

```bash
npm install -g @adobe/aem-cli
```

## Step 1 - Open the correct folder

1. Open terminal.
2. Go to repository root:

```bash
cd C:/The-Indian-Bank
```

3. Install dependencies:

```bash
npm install
```

## Step 2 - Start local website preview

1. From repo root, run:

```bash
aem up
```

2. Open browser:

- http://localhost:3000/
- http://localhost:3000/account-opening

Expected result:
- Landing page loads.
- Account opening form page loads.

If you see 404 locally:
- Check [fstab.yaml](../fstab.yaml)
- Check mountpoint connectivity and author permissions.

## Step 3 - Start Java backend API

1. Open a second terminal.
2. Go to backend module:

```bash
cd C:/The-Indian-Bank/backend/account-opening-api
```

3. Build once:

```bash
mvn -DskipTests compile
```

4. Run service:

```bash
mvn spring-boot:run
```

Expected result:
- Service starts on port 8088.

Health check:

- Submit endpoint: `POST http://localhost:8088/v1/account-openings`
- Status endpoint: `GET http://localhost:8088/v1/account-openings/{applicationId}`

## Step 4 - Configure API auth secrets (local)

Open [backend/account-opening-api/src/main/resources/application.yml](../backend/account-opening-api/src/main/resources/application.yml).

Set values:

- `app.security.bearer-token`
- `app.webhook.hmac-secret`

Example:

```yaml
app:
  webhook:
    hmac-secret: my-local-hmac-secret
  security:
    bearer-token: my-local-bearer-token
```

Restart backend after changes.

## Step 5 - Test submit API end-to-end

From backend folder or repo root, run:

```bash
curl -X POST http://localhost:8088/v1/account-openings \
  -H "Authorization: Bearer my-local-bearer-token" \
  -H "X-Correlation-Id: corr-1001" \
  -H "Idempotency-Key: idem-1001" \
  -H "Content-Type: application/json" \
  --data @C:/The-Indian-Bank/docs/contracts/account-opening-request.example.json
```

Expected result:
- HTTP 202
- JSON includes:
  - `applicationId`
  - `status: RECEIVED`

Copy the returned `applicationId`.

## Step 6 - Test status API

Run:

```bash
curl -X GET http://localhost:8088/v1/account-openings/IB-2026-XXXXXXXX \
  -H "Authorization: Bearer my-local-bearer-token"
```

Replace `IB-2026-XXXXXXXX` with the actual value from Step 5.

Expected result:
- HTTP 200
- Status JSON for that application.

## Step 7 - Test idempotency behavior

1. Re-send exact same request with same `Idempotency-Key`.
2. Confirm same accepted response is returned.
3. Change payload but keep same `Idempotency-Key`.

Expected result:
- HTTP 409 conflict for changed payload with same key.

## Step 8 - (Optional) Test webhook signature endpoint

Endpoint:

- `POST /v1/webhooks/account-openings/status`

Header required:

- `X-IB-Signature` (HMAC SHA-256 of raw request body)

Use payload from:

- [docs/contracts/account-opening-webhook-event.example.json](./contracts/account-opening-webhook-event.example.json)

Expected result:
- Valid signature => accepted response
- Invalid signature => HTTP 401

## Step 9 - Understand current UI submission path

Current adaptive form definition is at:

- [forms/account-opening-form.json](../forms/account-opening-form.json)

It contains:

1. `fd:submit` spreadsheet placeholder
2. `enterpriseSubmission` contract metadata

Important:
- `enterpriseSubmission` is metadata for integration teams.
- Actual browser-to-API posting from the Adaptive Form requires runtime submit action wiring in AEM environment (or middleware route).

## Step 10 - Publish content to Preview and Live

1. In AEM Author, create or verify pages:
   - `/content/dogra-uday/The-Indian-Bank/index`
   - `/content/dogra-uday/The-Indian-Bank/account-opening`
2. Add adaptive form block on account-opening page.
3. Publish to Preview.
4. Verify:
   - https://main--the-indian-bank--dogra-uday.aem.page/
   - https://main--the-indian-bank--dogra-uday.aem.page/account-opening
5. Promote to Live.
6. Verify:
   - https://main--the-indian-bank--dogra-uday.aem.live/
   - https://main--the-indian-bank--dogra-uday.aem.live/account-opening

If Preview/Live shows 404, use:

- [docs/account-opening-deployment-guide.md](./account-opening-deployment-guide.md)

## Step 11 - Production handoff checklist

1. Replace all placeholder domains and tokens.
2. Configure OAuth server and scopes.
3. Move in-memory idempotency/status store to persistent DB.
4. Add TLS, WAF, API gateway policy, and rate limits.
5. Add audit logging and PII masking.
6. Add monitoring and alerting on 4xx/5xx rates.
7. Add retry policy for webhook delivery.

## Step 12 - Quick failure recovery

If backend does not start:

1. Re-check Java version (`java -version`).
2. Run `mvn -DskipTests compile` and read first error line.
3. Verify [backend/account-opening-api/pom.xml](../backend/account-opening-api/pom.xml) has Java 11.

If submit API returns 401:

1. Check `Authorization` header value.
2. Check `app.security.bearer-token` in backend config.

If status API returns 404:

1. Use `applicationId` from current runtime instance.
2. Re-submit once and retry status call.

## Reference files

- [docs/account-opening-deployment-guide.md](./account-opening-deployment-guide.md)
- [docs/account-opening-submission-contract.md](./account-opening-submission-contract.md)
- [docs/contracts/account-opening-submission.openapi.yaml](./contracts/account-opening-submission.openapi.yaml)
- [backend/account-opening-api/README.md](../backend/account-opening-api/README.md)
