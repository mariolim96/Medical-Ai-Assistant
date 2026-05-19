package com.medicalai.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AuthResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-05-19T21:34:11.321215300+02:00[Europe/Rome]", comments = "Generator version: 7.4.0")
public class AuthResponse {

  private String token;

  private Long expiresIn;

  private String role;

  public AuthResponse token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Bearer JWT Token
   * @return token
  */
  
  @Schema(name = "token", description = "Bearer JWT Token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public AuthResponse expiresIn(Long expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  /**
   * Scadenza in millisecondi
   * @return expiresIn
  */
  
  @Schema(name = "expiresIn", description = "Scadenza in millisecondi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresIn")
  public Long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public AuthResponse role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Ruolo assegnato all'utente
   * @return role
  */
  
  @Schema(name = "role", description = "Ruolo assegnato all'utente", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthResponse authResponse = (AuthResponse) o;
    return Objects.equals(this.token, authResponse.token) &&
        Objects.equals(this.expiresIn, authResponse.expiresIn) &&
        Objects.equals(this.role, authResponse.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, expiresIn, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthResponse {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

