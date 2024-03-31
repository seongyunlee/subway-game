package site.kkrupp.subway.travel.dto.request

import site.kkrupp.subway.travel.dto.ChatContextDto

data class TravelSubmitAnswerRequestDto(
    val answer: String,
    val transferTo: String?,
    val chatContext: ChatContextDto,
)
