package site.kkrupp.subway.common.monitoring.activity

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.RequiredArgsConstructor
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RestControllerAdvice
import site.kkrupp.subway.webhook.DiscordWebhook


@Aspect
@Component
class UserActivityAspect(
    private val objectMapper : ObjectMapper,
    private val discordWebhook: DiscordWebhook
){

    private val log = LoggerFactory.getLogger(UserActivityAspect::class.java)

    @AfterReturning(
        pointcut = "@annotation(site.kkrupp.subway.common.monitoring.activity.UserActivityMonitor)",
        returning = "result"
    )
    @Async
    fun logUserActivity(joinPoint: JoinPoint, result: Any){

        try {
            val activityTag = getActivityTag(joinPoint)
            discordWebhook.sendMessage("User Activity: [$activityTag]\n")
        }catch (e: Exception){
            log.error("Failed to log user activity", e)
        }
    }

    private fun getActivityTag(joinPoint: JoinPoint): String {
        val method = (joinPoint.signature as MethodSignature).method
        val annotation = method.getAnnotation(UserActivityMonitor::class.java)

        if(annotation?.value?.isNotBlank() == true){
            return annotation.value
        }
        return "General"
    }

}