package com.indianbank.accountopening.security;

import com.indianbank.accountopening.exception.UnauthorizedException;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebhookSignatureVerifier {

  private static final String HMAC_SHA256 = "HmacSHA256";

  private final String secret;

  public WebhookSignatureVerifier(@Value("${app.webhook.hmac-secret}") String secret) {
    this.secret = secret;
  }

  public void verifyOrThrow(String rawPayload, String signatureHeader) {
    if (signatureHeader == null || signatureHeader.isBlank()) {
      throw new UnauthorizedException("Missing X-IB-Signature header");
    }
    String expected = sign(rawPayload);
    if (!constantTimeEquals(expected, signatureHeader)) {
      throw new UnauthorizedException("Invalid webhook signature");
    }
  }

  public String sign(String payload) {
    try {
      Mac mac = Mac.getInstance(HMAC_SHA256);
      SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
      mac.init(secretKeySpec);
      byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();
      for (byte b : digest) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (Exception e) {
      throw new IllegalStateException("Unable to compute webhook signature", e);
    }
  }

  private boolean constantTimeEquals(String a, String b) {
    if (a.length() != b.length()) {
      return false;
    }
    int result = 0;
    for (int i = 0; i < a.length(); i += 1) {
      result |= a.charAt(i) ^ b.charAt(i);
    }
    return result == 0;
  }
}
