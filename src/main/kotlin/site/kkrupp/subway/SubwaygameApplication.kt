package site.kkrupp.subway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class SubwaygameApplication

fun main(args: Array<String>) {
	runApplication<SubwaygameApplication>(*args)
}
