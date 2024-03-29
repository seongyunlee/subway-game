package site.kkrupp.subway.bestroute.dto

data class BestRouteProblem(
    val id: Int,
    val startStation: String,
    val endStation: String,
    val choices: List<StationChoice>,
)



