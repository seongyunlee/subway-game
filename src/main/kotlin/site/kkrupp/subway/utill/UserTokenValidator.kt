package site.kkrupp.subway.utill

import org.springframework.stereotype.Component


@Component
class UserTokenValidator {
    fun validateToken(token: String): Boolean {
        return token == "test_token"
    }
}