package com.example.common;

import lombok.Getter;

@Getter
public class Status5xxException extends RuntimeException {
  private final ApiResponse.ProcessStatus processStatus;
  private final String message;

  public Status5xxException(
    ApiResponse.ProcessStatus processStatus,
    String message
  ) {
    super(message);
    this.processStatus = processStatus;
    this.message = message;
  }
}
