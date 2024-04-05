package site.kkrupp.subway.rank.dto

import site.kkrupp.subway.rank.Rank

data class GetRankResponseDto(
    val ranks: List<Rank>,
    val playerInfo: PlayerInfoDto?
)
