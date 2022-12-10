package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//Spring Bean 등록
@Component
@Aspect
public class TimeTraceAop {
	
	@Around("execution(* hello.hellospring..*(..))")				// 공통 관심사 Target 지정 - package(hello.hellospring)-..*(class들) 하위 전체 다를 의미함
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		System.out.println("START: " + joinPoint.toString());
		
		try {
			return joinPoint.proceed();
		} finally {
			long finish = System.currentTimeMillis();
			long timeMs = finish - start;
			System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
		}
	}
}