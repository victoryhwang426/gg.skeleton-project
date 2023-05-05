package com.example.handler;

import com.example.common.ApiResponse;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.common.Status4xxException;
import com.example.common.Status5xxException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionAdvisor {

  @ExceptionHandler({
      HttpRequestMethodNotSupportedException.class,
      HttpMediaTypeNotSupportedException.class,
      HttpMediaTypeNotAcceptableException.class,
      MissingPathVariableException.class,
      MissingServletRequestParameterException.class,
      ServletRequestBindingException.class,
      ConversionNotSupportedException.class,
      TypeMismatchException.class,
      HttpMessageNotReadableException.class,
      HttpMessageNotWritableException.class,
      MethodArgumentNotValidException.class,
      MissingServletRequestPartException.class,
      BindException.class,
      AsyncRequestTimeoutException.class
  })
  public ResponseEntity<?> requestExceptionHandler(Exception e) {
    log.warn("Bad Request Exception: ", e);
    String errorMessage = "You requested with incorrect information.";
    if (e instanceof MethodArgumentNotValidException) {
      errorMessage = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors()
          .stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .collect(Collectors.joining(", "));
    }

    ApiResponse apiResponse = ApiResponse.builder()
        .processStatus(ApiResponse.ProcessStatus.STOPPED_BY_VALIDATION)
        .errorMessage(errorMessage)
        .build();
    return ResponseEntity
        .badRequest()
        .body(apiResponse);
  }

  @ExceptionHandler(Status4xxException.class)
  public ResponseEntity<?> badRequestHandler(Status4xxException e) {
    log.warn("4xx Exception: ", e);

    ApiResponse apiResponse = ApiResponse.builder()
        .processStatus(e.getProcessStatus())
        .errorMessage(e.getMessage())
        .build();
    return ResponseEntity
        .status(e.getProcessStatus().getStatusCode())
        .body(apiResponse);
  }

  @ExceptionHandler(Status5xxException.class)
  public ResponseEntity<?> internalErrorHandler(Status5xxException e) {
    log.error("5xx Exception: ", e);

    ApiResponse apiResponse = ApiResponse.builder()
        .processStatus(e.getProcessStatus())
        .errorMessage(e.getMessage())
        .build();
    return ResponseEntity
        .status(e.getProcessStatus().getStatusCode())
        .body(apiResponse);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<?> noHandlerFoundHandler(NoHandlerFoundException e) {
    log.error("Not found Exception: ", e);

    ApiResponse.ProcessStatus processStatus = ProcessStatus.NOT_FOUND_RESOURCE;
    ApiResponse apiResponse = ApiResponse.builder()
        .processStatus(processStatus)
        .errorMessage(e.getMessage())
        .build();
    return ResponseEntity
        .status(processStatus.getStatusCode())
        .body(apiResponse);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> runtimeExceptionHandler(Throwable e) {
    log.error("UnHandled Exception: ", e);

    ApiResponse.ProcessStatus processStatus = ApiResponse.ProcessStatus.STOPPED_BY_EXCEPTION;
    ApiResponse apiResponse = ApiResponse.builder()
        .processStatus(processStatus)
        .errorMessage(e.getMessage())
        .build();
    return ResponseEntity
        .status(processStatus.getStatusCode())
        .body(apiResponse);
  }

  @ExceptionHandler(VirtualMachineError.class)
  public ResponseEntity<?> serverErrorHandler(Error e) {
    log.error("Runtime Error: ", e);

    ApiResponse.ProcessStatus processStatus = ApiResponse.ProcessStatus.STOPPED_BY_ERROR;
    ApiResponse apiResponse = ApiResponse.builder()
        .processStatus(processStatus)
        .errorMessage(e.getMessage())
        .build();
    return ResponseEntity
        .status(processStatus.getStatusCode())
        .body(apiResponse);
  }
}
