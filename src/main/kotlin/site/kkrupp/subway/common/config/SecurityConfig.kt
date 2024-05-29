package site.kkrupp.subway.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Order(11)
    fun authFilterChain(http: HttpSecurity): SecurityFilterChain {

        http.securityMatcher("/admin/**", "/login")
            .csrf { it.disable() }
            .cors { it.disable() }


            .authorizeHttpRequests {
                it.requestMatchers("/admin/login/**").permitAll()
                    .requestMatchers("/admin/join/**").hasRole("ROOT")
                    .anyRequest().hasAnyRole("ROOT", "ADMIN")
            }
            .formLogin {
                it.loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login_process")
                    .defaultSuccessUrl("/admin")
            }

        return http.build()
    }

    @Bean
    @Order(12)
    fun allFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
        http.cors { it.disable() }
        http.authorizeHttpRequests {
            it.anyRequest().permitAll()
        }
        return http.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}