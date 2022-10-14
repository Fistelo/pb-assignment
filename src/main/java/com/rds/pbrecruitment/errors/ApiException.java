package com.rds.pbrecruitment.errors;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  private final ApiError error;

  public ApiException(final ApiError apiError) {
    super(apiError.getErrorMessage());

    this.error = apiError;
  }

  public int getStatus() {
    return error.getStatusCode().value();
  }

  public String getCode() {
    return error.getCode();
  }

  public String getMessage() {
    return error.getErrorMessage();
  }
}
