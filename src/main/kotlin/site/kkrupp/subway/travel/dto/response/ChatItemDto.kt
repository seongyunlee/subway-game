package site.kkrupp.subway.travel.dto.response

data class ChatItemDto(
    val stationId: Long,
    val stationName: String,
    val originalLine: String,
    val transferTo: String?,
)
