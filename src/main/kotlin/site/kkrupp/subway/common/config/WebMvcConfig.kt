package site.kkrupp.subway.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import site.kkrupp.subway.player.PlayerAnnotationResolver
import site.kkrupp.subway.player.RequiredPlayerAnnotationResolver

@Configuration
class WebMvcConfig(
    val requiredUserResolver: RequiredPlayerAnnotationResolver,
    val userResolver: PlayerAnnotationResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(requiredUserResolver)
        resolvers.add(userResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
    }
}