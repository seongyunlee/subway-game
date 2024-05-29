package site.kkrupp.subway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableCaching
class SubwaygameApplication

fun main(args: Array<String>) {
    runApplication<SubwaygameApplication>(*args)
}
