package site.kkrupp.subway.common.monitoring

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class ExecutionTime {

    private val logger = LoggerFactory.getLogger(ExecutionTime::class.java)

    @Pointcut("execution(* site.kkrupp.subway.fillblank.service.*.*(..))")
    fun timer(){};

    fun executionTime(joinPoint: ProceedingJoinPoint) {
        val stopWatch: StopWatch = StopWatch()

        stopWatch.start()
        joinPoint.proceed() // 조인포인트의 메서드 실행
        stopWatch.stop()

        val totalTimeMillis: Long = stopWatch.getTotalTimeMillis()

        val signature: MethodSignature = joinPoint.getSignature() as MethodSignature
        val methodName: String = signature.getMethod().getName()

        logger.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis)
    }

}

