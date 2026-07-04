package com.indianbank.accountopening.security;

import com.indianbank.accountopening.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthVerifier {

  private static final String BEARER_PREFIX = "Bearer ";

  private final String expectedToken;

  public AuthVerifier(@Value("${app.security.bearer-token}") String expectedToken) {
    this.expectedToken = expectedToken;
  }

  public void verifyBearer(String authorization) {
    if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
      throw new UnauthorizedException("Missing or invalid Authorization header");
    }
    String token = authorization.substring(BEARER_PREFIX.length());
    if (!expectedToken.equals(token)) {
      throw new UnauthorizedException("Invalid bearer token");
    }
  }
}
