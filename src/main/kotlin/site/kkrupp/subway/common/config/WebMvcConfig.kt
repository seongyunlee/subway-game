package site.kkrupp.subway.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import site.kkrupp.subway.player.PlayerAnnotationResolver

@Configuration
class WebMvcConfig(
    val requiredUserResolver: PlayerAnnotationResolver,
    val userResolver: PlayerAnnotationResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(requiredUserResolver)
        resolvers.add(userResolver)
    }
}