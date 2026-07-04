# Indian Bank Account Opening - End-to-End Setup Guide

Primary guide: use [account-opening-master-runbook.md](./account-opening-master-runbook.md) first.

This guide helps you:
- Resolve Preview/Live 404 issues for Edge Delivery Services
- Publish a branded account-opening page
- Connect Adaptive Form submission and content paths

## 1) Why Preview and Live are showing 404

In this repository, code is available but page content is resolved from the mountpoint configured in [fstab.yaml](../fstab.yaml).

Current mountpoint:
- Author source: https://author-p24056-e1593080.adobeaemcloud.com/bin/franklin.delivery/dogra-uday/The-Indian-Bank/main

If the mounted AEM content path does not contain an `index` page (or requested route) and is not published to Preview/Live, AEM Edge returns 404.

## 2) Correct Preview/Live URL pattern for this repo

From repository remote `dogra-uday/The-Indian-Bank`, your expected domains are:
- Preview: https://main--the-indian-bank--dogra-uday.aem.page/
- Live: https://main--the-indian-bank--dogra-uday.aem.live/

If these URLs still 404, it is a content publish issue, not a JavaScript runtime issue.

## 3) Artifacts created in this repo

1. Adaptive form definition:
   - [forms/account-opening-form.json](../forms/account-opening-form.json)
2. Form branding stylesheet:
   - [styles/indian-bank-account-opening.css](../styles/indian-bank-account-opening.css)
3. Landing page with rich branding and embedded Adaptive Form block:
   - [account-opening.html](../account-opening.html)
4. Landing visuals stylesheet:
   - [styles/account-opening-landing.css](../styles/account-opening-landing.css)

## 4) Publish flow to remove 404 (AEM Author + Edge)

1. Ensure AEM content exists at the mounted path (index route):
   - Create page: `/content/dogra-uday/The-Indian-Bank/index`
   - Add form route page: `/content/dogra-uday/The-Indian-Bank/account-opening`
2. Add an Adaptive Form component on the account-opening page in Universal Editor.
3. Point the form to the schema equivalent of [forms/account-opening-form.json](../forms/account-opening-form.json) content.
4. Publish both pages to Preview.
5. Validate Preview URLs:
   - https://main--the-indian-bank--dogra-uday.aem.page/
   - https://main--the-indian-bank--dogra-uday.aem.page/account-opening
6. Promote content to Live and validate:
   - https://main--the-indian-bank--dogra-uday.aem.live/
   - https://main--the-indian-bank--dogra-uday.aem.live/account-opening

## 5) Submission setup for working functionality

The generated form JSON includes spreadsheet submit configuration placeholder:
- `properties["fd:submit"].spreadsheet.spreadsheetUrl`

The form definition is now also wired to an API-first submission contract:
- `properties.submissionContract`
- `properties.enterpriseSubmission`

Contract files:
- [account-opening-submission-contract.md](./account-opening-submission-contract.md)
- [contracts/account-opening-submission.openapi.yaml](./contracts/account-opening-submission.openapi.yaml)
- [contracts/account-opening-request.example.json](./contracts/account-opening-request.example.json)
- [contracts/account-opening-response.example.json](./contracts/account-opening-response.example.json)
- [contracts/account-opening-webhook-event.example.json](./contracts/account-opening-webhook-event.example.json)

Replace with your real spreadsheet URL (or enterprise endpoint) before production.

### API integration execution order

1. Provision OAuth client credentials for `account.openings.write`.
2. Implement `POST /v1/account-openings` exactly as defined in the OpenAPI contract.
3. Enforce `X-Correlation-Id` and `Idempotency-Key` headers for every submission.
4. Return `202` with generated `applicationId` only after durable persistence.
5. Implement webhook event publishing for async status updates.
6. Replace placeholder URLs in `properties.enterpriseSubmission` with production values.

Recommended hardening:
1. Add reCAPTCHA integration keys for production forms.
2. Configure data retention and PII masking policy.
3. Add server-side validation for PAN/Aadhaar and duplicate checks.
4. Add failure webhook alerts for submission errors.

## 6) If Adaptive Forms cannot be created in AEM now

Use this fallback plan immediately:

1. Host [account-opening.html](../account-opening.html) route in your content source.
2. Keep form definition in [forms/account-opening-form.json](../forms/account-opening-form.json).
3. Validate rendering with local proxy:
   - `npm i`
   - `aem up`
   - Open `http://localhost:3000/account-opening`
4. Move from JSON-driven form to AEM-authored Adaptive Form later by reusing field structure and style references.

## 7) Quick 404 diagnostic checklist

1. Confirm mountpoint URL in [fstab.yaml](../fstab.yaml) is reachable and authenticated.
2. Confirm content pages exist under `/content/dogra-uday/The-Indian-Bank/`.
3. Confirm content is published to Preview, then Live.
4. Confirm route path and case are exact (`account-opening` vs `Account-Opening`).
5. Confirm branch/domain mapping uses `main--the-indian-bank--dogra-uday`.

If all five are correct and still failing, re-publish the page and invalidate CDN cache via your Edge pipeline/admin workflow.
