package site.kkrupp.subway.bestroute.dto

data class BestRouteSubmitResponseDto(
    val isCorrect: Boolean,
    val problemId: Int,
    val newProblem: BestRouteProblem
)
