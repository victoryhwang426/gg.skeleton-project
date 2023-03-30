package com.example.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {
  @Override
  public void afterCompletion(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler,
    Exception ex
  ) throws Exception {
    if(!(request instanceof ContentCachingRequestWrapper)){
      return;
    }

    ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
    ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

    String requestParameter = "{}";
    if (cachingRequest.getContentType() != null && cachingRequest.getContentType().contains("application/json")) {
      cachingRequest.getContentAsByteArray();
      if (cachingRequest.getContentAsByteArray().length != 0){
        requestParameter = new String(cachingRequest.getContentAsByteArray());
      }
    }

    String responseResult = "{}";
    if (cachingResponse.getContentType() != null && cachingResponse.getContentType().contains("application/json")) {
      cachingResponse.getContentAsByteArray();
      if (cachingResponse.getContentAsByteArray().length != 0) {
        responseResult = new String(cachingResponse.getContentAsByteArray());
      }
    }

    String uri = cachingRequest.getRequestURI();
    String queryString = cachingRequest.getQueryString();
    String method = cachingRequest.getMethod();
    if(queryString == null){
      queryString = "{}";
    }

    log.info("URI: {}, QueryString: {}, Method: {}, Parameter: {}, Response: {}", uri, queryString, method, requestParameter, responseResult);
  }
}
