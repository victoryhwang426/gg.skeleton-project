package com.example.handler;

import com.example.common.ApiResponse;
import com.example.common.Status4xxException;
import com.example.common.Status5xxException;
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
  public final static String DEFAULT_MESSAGE = "서버오류가 발생하였습니다. 확인 부탁드립니다.";
  public final static String BINDING_ERROR_MESSAGE = "잘못된 요청입니다.";

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
  public ResponseEntity<ApiResponse<?>> requestExceptionHandler(Exception e) {
    log.warn("Bad Request Exception: " + e.getMessage(), e);
    return ResponseEntity.badRequest()
      .body(new ApiResponse<>(ApiResponse.ProcessStatus.STOPPED_BY_VALIDATION, BINDING_ERROR_MESSAGE));
  }

  @ExceptionHandler(Status4xxException.class)
  public ResponseEntity<ApiResponse<?>> badRequestHandler(Status4xxException e) {
    log.warn("4xx Exception: " + e.getMessage(), e);
    return ResponseEntity.status(e.getProcessStatus().getStatusCode())
      .body(new ApiResponse<>(e.getProcessStatus(), e.getMessage()));
  }

  @ExceptionHandler(Status5xxException.class)
  public ResponseEntity<ApiResponse<?>> internalErrorHandler(Status5xxException e) {
    log.error("5xx Exception: " + e.getMessage(), e);
    return ResponseEntity.status(e.getProcessStatus().getStatusCode())
      .body(new ApiResponse<>(e.getProcessStatus(), e.getMessage()));
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ApiResponse<?>> runtimeExceptionHandler(Throwable e) {
    log.error("UnHandled Exception: " + e.getMessage(), e);
    return ResponseEntity.internalServerError()
      .body(new ApiResponse<>(ApiResponse.ProcessStatus.STOPPED_BY_EXCEPTION, DEFAULT_MESSAGE));
  }

  @ExceptionHandler(VirtualMachineError.class)
  public ResponseEntity<ApiResponse<?>> serverErrorHandler(Error e) {
    log.error("Runtime Error: " + e.getMessage(), e);
    return ResponseEntity.internalServerError()
      .body(new ApiResponse<>(ApiResponse.ProcessStatus.STOPPED_BY_ERROR, DEFAULT_MESSAGE));
  }
}
