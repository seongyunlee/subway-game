package site.kkrupp.subway.travel.dto.request

data class TravelStartGameRequestDto(
    val playerId: String,
    val gameScore: Int,
    val startLineId: String,
)
