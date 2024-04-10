package site.kkrupp.subway.travel.dto.response

data class TravelReportAnswerResponseDto(
    val isSuccess: Boolean,
    val changeStation: ChatItemDto?,
    val gameLife: Int,
    val gameScore: Int,
)
