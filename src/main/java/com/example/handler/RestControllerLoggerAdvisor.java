package com.example.handler;

import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@Slf4j
@RestControllerAdvice
public class RestControllerLoggerAdvisor extends RequestBodyAdviceAdapter {
  @Override
  public boolean supports(
    MethodParameter methodParameter,
    Type targetType,
    Class<? extends HttpMessageConverter<?>> converterType
  ) {
    return true;
  }

  @Override
  public Object afterBodyRead(
    Object body,
    HttpInputMessage inputMessage,
    MethodParameter parameter,
    Type targetType,
    Class<? extends HttpMessageConverter<?>> converterType
  ) {
    log.info("Request Body: {}", body.toString());
    return super.afterBodyRead(
      body,
      inputMessage,
      parameter,
      targetType,
      converterType
    );
  }
}
