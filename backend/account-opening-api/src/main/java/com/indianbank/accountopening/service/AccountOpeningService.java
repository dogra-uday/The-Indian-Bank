package com.indianbank.accountopening.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indianbank.accountopening.dto.AccountOpeningSubmissionRequest;
import com.indianbank.accountopening.dto.SubmissionAcceptedResponse;
import com.indianbank.accountopening.dto.SubmissionStatusResponse;
import com.indianbank.accountopening.exception.ConflictException;
import com.indianbank.accountopening.exception.NotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class AccountOpeningService {

  private static final DateTimeFormatter UTC_TIME = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

  private final ObjectMapper objectMapper;
  private final Map<String, SubmissionRecord> byIdempotencyKey = new ConcurrentHashMap<>();
  private final Map<String, SubmissionStatusResponse> byApplicationId = new ConcurrentHashMap<>();

  public AccountOpeningService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public SubmissionAcceptedResponse submit(AccountOpeningSubmissionRequest request, String idempotencyKey) {
    String payloadHash = hashPayload(request);
    SubmissionRecord existing = byIdempotencyKey.get(idempotencyKey);

    if (existing != null) {
      if (!existing.getPayloadHash().equals(payloadHash)) {
        throw new ConflictException("Idempotency-Key already used with a different request payload");
      }
      return existing.getAcceptedResponse();
    }

    String applicationId = generateApplicationId();
    String now = UTC_TIME.format(Instant.now());

    SubmissionAcceptedResponse accepted = new SubmissionAcceptedResponse(
        applicationId,
        "RECEIVED",
        now,
        "KYC_VERIFICATION_IN_PROGRESS"
    );

    SubmissionStatusResponse status = new SubmissionStatusResponse(
        applicationId,
        "RECEIVED",
        "DOCUMENT_VERIFICATION",
        now,
        "Submission accepted"
    );

    byIdempotencyKey.put(idempotencyKey, new SubmissionRecord(payloadHash, accepted));
    byApplicationId.put(applicationId, status);
    return accepted;
  }

  public SubmissionStatusResponse getStatus(String applicationId) {
    SubmissionStatusResponse response = byApplicationId.get(applicationId);
    if (response == null) {
      throw new NotFoundException("Application not found");
    }
    return response;
  }

  private String hashPayload(AccountOpeningSubmissionRequest request) {
    try {
      String payload = objectMapper.writeValueAsString(request);
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] digest = messageDigest.digest(payload.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(digest);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Unable to serialize request payload", e);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to hash request payload", e);
    }
  }

  private String generateApplicationId() {
    int year = Instant.now().atZone(ZoneOffset.UTC).getYear();
    String random = randomBase36(8);
    return String.format("IB-%d-%s", year, random);
  }

  private String randomBase36(int len) {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i += 1) {
      sb.append(alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length())));
    }
    return sb.toString();
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private static class SubmissionRecord {

    private final String payloadHash;
    private final SubmissionAcceptedResponse acceptedResponse;

    private SubmissionRecord(String payloadHash, SubmissionAcceptedResponse acceptedResponse) {
      this.payloadHash = payloadHash;
      this.acceptedResponse = acceptedResponse;
    }

    private String getPayloadHash() {
      return payloadHash;
    }

    private SubmissionAcceptedResponse getAcceptedResponse() {
      return acceptedResponse;
    }
  }
}
