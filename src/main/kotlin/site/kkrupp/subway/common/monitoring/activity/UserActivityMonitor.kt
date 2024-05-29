package site.kkrupp.subway.common.monitoring.activity

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserActivityMonitor (
    val value: String = ""
)