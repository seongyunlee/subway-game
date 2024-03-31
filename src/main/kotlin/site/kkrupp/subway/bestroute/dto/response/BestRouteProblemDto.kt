package site.kkrupp.subway.bestroute.dto.response

data class BestRouteProblemDto(
    val id: Int,
    val startStation: String,
    val endStation: String,
    val choices: List<StationChoiceDto>,
)



