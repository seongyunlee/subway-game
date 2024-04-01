package site.kkrupp.subway.travel.dto

data class ChatContextDto(
    val currentLine: String,
    val previousStationIds: List<Long>,
)
