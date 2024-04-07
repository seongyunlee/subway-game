package site.kkrupp.subway.travel.dto.response

import site.kkrupp.subway.travel.dto.ChatContextDto

data class TravelSubmitAnswerResponseDto(
    val isCorrect: Boolean,
    val gameLife: Int,
    val gameScore: Int,
    val chatContext: ChatContextDto,
    val submittedAnswer: ChatItemDto,
    val dealerAnswer: ChatItemDto?,
)
