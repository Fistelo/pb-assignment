package com.rds.pbrecruitment.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Getter
@AllArgsConstructor
public enum ApiError {

    INVALID_REQUEST(BAD_REQUEST, "invalid_request", "The request is invalid or malformed."),
    RESOURCE_NOT_FOUND(NOT_FOUND, "not_found", "The requested resource has not been found."),
    GITHUB_API_FAILED(BAD_GATEWAY, "github_api_failed", "The github API response have resulted in error.");

    private final HttpStatus statusCode;
    private final String code;
    private final String errorMessage;
}

