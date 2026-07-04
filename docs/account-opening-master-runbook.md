# MASTER RUNBOOK - Indian Bank Account Opening (Use This File Only)

If you read only one document, read this one.

This guide covers everything in one place:
1. Why Preview/Live is 404 right now
2. Exactly what content to create in AEM
3. How to create/configure Adaptive Form
4. How to run the Java backend API
5. How to test end to end
6. How to publish to Preview and Live

---

## A) Current 404 Root Cause (Already Verified)

Current live checks return 404 with these errors:
- failed to load /index.md from content-bus: 404
- failed to load /account-opening.md from content-bus: 404

This means your Preview/Live domain is reachable, but the content pages are not available in the mounted content source for these routes.

Your mountpoint is configured in [fstab.yaml](../fstab.yaml) as:
- https://author-p24056-e1593080.adobeaemcloud.com/bin/franklin.delivery/dogra-uday/The-Indian-Bank/main

So fix = create/publish content under that source path.

---

## B) What To Create in AEM (Click-by-Click)

### B1. Open AEM Author and create root pages

1. Open your AEM Author URL.
2. Go to Sites.
3. Navigate to content path for this repo:
   - /content/dogra-uday/The-Indian-Bank
4. If folder/page tree does not exist, create it first.
5. Create page named index.
6. Create page named account-opening.

Required paths after this step:
- /content/dogra-uday/The-Indian-Bank/index
- /content/dogra-uday/The-Indian-Bank/account-opening

### B2. Open Universal Editor for account-opening page

1. Select account-opening page.
2. Click Edit (Universal Editor).
3. In content tree, add a Section.
4. Inside section, insert Adaptive Form block.

### B3. Configure Adaptive Form content

Use form schema from [forms/account-opening-form.json](../forms/account-opening-form.json).

In the Adaptive Form block properties:
1. Set form source/path to your account-opening Adaptive Form asset (or equivalent JSON source).
2. Ensure style path is:
   - /styles/indian-bank-account-opening.css
3. Confirm fields include personal, contact, account preference, KYC docs, declaration.

### B3a. AEM Properties Panel Quick Fill

Use these exact values when you enter the properties manually:

1. Form Specific Custom Functions Path:
   - forms/account-opening-form.json
2. Form Specific styles path:
   - /styles/indian-bank-account-opening.css
3. Form path in authoring UI, if requested:
   - /content/forms/af/indian-bank/account-opening
4. Submit action:
   - spreadsheet for quick start
   - api for enterprise integration
5. Thank-you message:
   - <h3>Application submitted successfully.</h3><p>Your reference ID has been generated. Our onboarding team will contact you within one business day.</p>
6. If the UI asks for a form action URL:
   - /adobe/forms/af/submit/aW5kaWFuLWJhbmstYWNjb3VudC1vcGVuaW5n

If the extension accepts a relative schema path, use exactly forms/account-opening-form.json. Do not paste the markdown link target ../forms/account-opening-form.json into the properties field.

### B4. Configure submission action

You have two options:

Option 1: Spreadsheet (quick start)
1. In form properties, use spreadsheet submit action.
2. Replace placeholder spreadsheet URL in [forms/account-opening-form.json](../forms/account-opening-form.json):
   - properties.fd:submit.spreadsheet.spreadsheetUrl

Option 2: Enterprise API (recommended)
1. Keep contract references in form properties:
   - properties.submissionContract
   - properties.enterpriseSubmission
2. Point endpoint to backend API:
   - POST /v1/account-openings
3. Ensure required headers are sent:
   - X-Correlation-Id
   - Idempotency-Key

---

## C) Run Java Backend in Same Repo

Backend module:
- [backend/account-opening-api/README.md](../backend/account-opening-api/README.md)

### C1. Start API

```bash
cd C:/The-Indian-Bank/backend/account-opening-api
mvn -DskipTests compile
mvn spring-boot:run
```

Expected:
- Service starts on port 8088.

### C2. Configure secrets

Edit [backend/account-opening-api/src/main/resources/application.yml](../backend/account-opening-api/src/main/resources/application.yml)

Set:
- app.security.bearer-token
- app.webhook.hmac-secret

Example:

```yaml
app:
  webhook:
    hmac-secret: my-local-hmac-secret
  security:
    bearer-token: my-local-bearer-token
```

Restart API after editing.

---

## D) Local End-to-End Test (Form + API)

### D1. Start local site proxy

```bash
cd C:/The-Indian-Bank
aem up
```

Open:
- http://localhost:3000/
- http://localhost:3000/account-opening

### D2. Test API submit using contract sample

```bash
curl -X POST http://localhost:8088/v1/account-openings \
  -H "Authorization: Bearer my-local-bearer-token" \
  -H "X-Correlation-Id: corr-1001" \
  -H "Idempotency-Key: idem-1001" \
  -H "Content-Type: application/json" \
  --data @C:/The-Indian-Bank/docs/contracts/account-opening-request.example.json
```

Expected:
- HTTP 202
- applicationId returned

### D3. Test status endpoint

```bash
curl -X GET http://localhost:8088/v1/account-openings/IB-2026-XXXXXXXX \
  -H "Authorization: Bearer my-local-bearer-token"
```

Replace ID with real returned value.

Expected:
- HTTP 200 with status payload.

### D4. Test idempotency

1. Same payload + same Idempotency-Key => returns same accepted result.
2. Different payload + same Idempotency-Key => HTTP 409.

---

## E) Publish To Preview and Live (This removes 404)

### E1. Publish from AEM Author to Preview

1. Publish index page.
2. Publish account-opening page.
3. Publish any dependent fragments/assets used by those pages.

### E2. Validate Preview URLs

- https://main--the-indian-bank--dogra-uday.aem.page/
- https://main--the-indian-bank--dogra-uday.aem.page/account-opening

### E3. Promote to Live

- Publish/promote same pages to Live.

### E4. Validate Live URLs

- https://main--the-indian-bank--dogra-uday.aem.live/
- https://main--the-indian-bank--dogra-uday.aem.live/account-opening

---

## F) Fast 404 Troubleshooting Checklist

If still 404 after publish:

1. Confirm pages exist exactly at:
   - /content/dogra-uday/The-Indian-Bank/index
   - /content/dogra-uday/The-Indian-Bank/account-opening
2. Confirm both pages are published (not just created).
3. Confirm route names and case are exact.
4. Confirm mountpoint in [fstab.yaml](../fstab.yaml) points to same repo owner/repo/branch.
5. Re-publish pages and dependent references.
6. Wait 1-2 minutes and test again.

Use this command for status checks (PowerShell):

```bash
curl.exe -I https://main--the-indian-bank--dogra-uday.aem.page/
curl.exe -I https://main--the-indian-bank--dogra-uday.aem.page/account-opening
curl.exe -I https://main--the-indian-bank--dogra-uday.aem.live/
curl.exe -I https://main--the-indian-bank--dogra-uday.aem.live/account-opening
```

---

## G) Contract Reference (for Backend Team)

- [docs/contracts/account-opening-submission.openapi.yaml](./contracts/account-opening-submission.openapi.yaml)
- [docs/contracts/account-opening-request.example.json](./contracts/account-opening-request.example.json)
- [docs/contracts/account-opening-response.example.json](./contracts/account-opening-response.example.json)
- [docs/contracts/account-opening-webhook-event.example.json](./contracts/account-opening-webhook-event.example.json)

---

## H) Read This, Ignore Others First

Start with this file only.

Other docs are supporting references:
- [docs/account-opening-deployment-guide.md](./account-opening-deployment-guide.md)
- [docs/account-opening-e2e-runbook.md](./account-opening-e2e-runbook.md)
- [docs/account-opening-submission-contract.md](./account-opening-submission-contract.md)
