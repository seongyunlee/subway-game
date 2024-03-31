package site.kkrupp.subway.bestroute.dto.response

data class BestRouteProblemDto(
    val id: Long,
    val startStation: String,
    val endStation: String,
    val choices: List<StationChoiceDto>,
)



