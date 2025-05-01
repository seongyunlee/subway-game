package site.kkrupp.subway.utill

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class Utils {
    companion object {
        fun getKoreaLocalDateTime(): LocalDateTime {
            return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
        }
    }
}