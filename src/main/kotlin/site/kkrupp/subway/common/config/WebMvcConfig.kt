package site.kkrupp.subway.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
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

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/")
    }
}