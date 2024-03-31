package site.kkrupp.subway.travel.dto.response

import site.kkrupp.subway.travel.dto.ChatContextDto

data class TravelStartGameResponseDto(
    val playerId: String,
    val chatContext: ChatContextDto,
    val gameLife: Int,
    val gameScore: Int,
)
