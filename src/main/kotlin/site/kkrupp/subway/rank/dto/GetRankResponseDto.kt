package site.kkrupp.subway.rank.dto

data class GetRankResponseDto(
    val ranks: List<RankDto>,
    val playerInfo: PlayerInfoDto?
)
