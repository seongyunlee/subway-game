package site.kkrupp.subway.rank.dto

import java.time.Duration

data class PlayerInfoDto(
    val score: Int,
    val duration: Duration,
    val rank: Int
)
