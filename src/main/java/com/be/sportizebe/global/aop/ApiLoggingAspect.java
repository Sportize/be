package com.be.sportizebe.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect // 이 클래스가 AOP 관점(Aspect) 이라는 선언 (API 로깅)
@Component
public class ApiLoggingAspect {
  // @Around : 타겟 메서드를 감싸서 특정 Advice(실질적으로 어떤 일을 해야할 지에 대한것)를 실행한다는 의미
  @Around("execution(* com.be.sportizebe.domain..controller..*(..))") // 모든 Controller API 메서드 호출을 가로챔
  public Object logApiResponseTime(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    String method = request.getMethod();
    String uri = request.getRequestURI();

    long startTime = System.currentTimeMillis();

    try {
      // Advice가 적용될 위치, 끼어들 수 있는 지점. 메서드 진입 지점, 생성자 호출 시점, 필드에서 값을 꺼내올 때 등 다양한 시점에 적용가능
      Object result = joinPoint.proceed(); // 실제 컨트롤러 메서드 실행
      long executionTime = System.currentTimeMillis() - startTime;

      log.info("[API] {} {} - {}ms", method, uri, executionTime);

      return result;
    } catch (Throwable e) { // HTTP 요청이 없는 컨텍스트에서 호출되면 예외 발생
      long executionTime = System.currentTimeMillis() - startTime;

      log.warn("[API] {} {} - {}ms (Error: {})", method, uri, executionTime, e.getMessage());

      throw e;
    }
  }
}
