package com.example.common;

import lombok.Getter;

@Getter
public class Status4xxException extends RuntimeException {
  private final ApiResponse.ProcessStatus processStatus;
  private final String message;

  public Status4xxException(
    ApiResponse.ProcessStatus processStatus,
    String message
  ) {
    super(message);
    this.processStatus = processStatus;
    this.message = message;
  }
}