package com.indianbank.accountopening.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indianbank.accountopening.dto.AccountOpeningSubmissionRequest;
import com.indianbank.accountopening.dto.SubmissionAcceptedResponse;
import com.indianbank.accountopening.dto.SubmissionStatusResponse;
import com.indianbank.accountopening.dto.WebhookEvent;
import com.indianbank.accountopening.security.AuthVerifier;
import com.indianbank.accountopening.security.WebhookSignatureVerifier;
import com.indianbank.accountopening.service.AccountOpeningService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/v1")
public class AccountOpeningController {

  private final AccountOpeningService accountOpeningService;
  private final AuthVerifier authVerifier;
  private final WebhookSignatureVerifier webhookSignatureVerifier;
  private final ObjectMapper objectMapper;

  public AccountOpeningController(
      AccountOpeningService accountOpeningService,
      AuthVerifier authVerifier,
      WebhookSignatureVerifier webhookSignatureVerifier,
      ObjectMapper objectMapper
  ) {
    this.accountOpeningService = accountOpeningService;
    this.authVerifier = authVerifier;
    this.webhookSignatureVerifier = webhookSignatureVerifier;
    this.objectMapper = objectMapper;
  }

  @PostMapping("/account-openings")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public SubmissionAcceptedResponse submitAccountOpening(
      @RequestHeader("Authorization") String authorization,
      @RequestHeader("X-Correlation-Id") @NotBlank @Size(max = 64) String correlationId,
      @RequestHeader("Idempotency-Key") @NotBlank @Size(max = 64) String idempotencyKey,
      @Valid @RequestBody AccountOpeningSubmissionRequest request
  ) {
    authVerifier.verifyBearer(authorization);
    return accountOpeningService.submit(request, idempotencyKey);
  }

  @GetMapping("/account-openings/{applicationId}")
  public SubmissionStatusResponse getStatus(
      @RequestHeader("Authorization") String authorization,
      @PathVariable @Pattern(regexp = "^IB-[0-9]{4}-[A-Z0-9]{8}$") String applicationId
  ) {
    authVerifier.verifyBearer(authorization);
    return accountOpeningService.getStatus(applicationId);
  }

  @PostMapping("/webhooks/account-openings/status")
  public Map<String, String> receiveStatusWebhook(
      @RequestHeader("X-IB-Signature") String signature,
      @RequestBody String rawPayload
  ) {
    webhookSignatureVerifier.verifyOrThrow(rawPayload, signature);
    try {
      objectMapper.readValue(rawPayload, WebhookEvent.class);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Invalid webhook payload");
    }
    return Map.of("message", "Webhook accepted", "signatureVerified", "true");
  }
}
