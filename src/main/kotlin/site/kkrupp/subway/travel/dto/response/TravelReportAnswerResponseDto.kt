package site.kkrupp.subway.travel.dto.response

import site.kkrupp.subway.travel.dto.ChatContextDto

data class TravelReportAnswerResponseDto(
    val isSuccess: Boolean,
    val chatContext: ChatContextDto,
    val gameLife: Int,
    val gameScore: Int,
)
