package com.example.handler;

import com.example.common.Status4xxException;
import com.example.common.Status5xxException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
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
    NoHandlerFoundException.class,
    AsyncRequestTimeoutException.class
  })
  public ResponseEntity<?> requestExceptionHandler(Exception e) {
    log.warn("Bad Request Exception: " + e.getMessage(), e);
    return ResponseEntity.ok(Map.of(
      "code", "400",
      "message", e.getMessage()
    ));
  }

  @ExceptionHandler(Status4xxException.class)
  public ResponseEntity<?> badRequestHandler(Status4xxException e) {
    log.warn("4xx Exception: " + e.getMessage(), e);
    return ResponseEntity.ok(Map.of(
      "code", e.getProcessStatus().getStatusCode(),
      "message", e.getMessage()
    ));
  }

  @ExceptionHandler(Status5xxException.class)
  public ResponseEntity<?> internalErrorHandler(Status5xxException e) {
    log.error("5xx Exception: " + e.getMessage(), e);
    return ResponseEntity.ok(Map.of(
      "code", e.getProcessStatus().getStatusCode(),
      "message", e.getMessage()
    ));
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> runtimeExceptionHandler(Throwable e) {
    log.error("UnHandled Exception: " + e.getMessage(), e);
    return ResponseEntity.ok(Map.of(
      "code", "500",
      "message", e.getMessage()
    ));
  }

  @ExceptionHandler(VirtualMachineError.class)
  public ResponseEntity<?> serverErrorHandler(Error e) {
    log.error("Runtime Error: " + e.getMessage(), e);
    return ResponseEntity.ok(Map.of(
      "code", "500",
      "message", e.getMessage()
    ));
  }
}
