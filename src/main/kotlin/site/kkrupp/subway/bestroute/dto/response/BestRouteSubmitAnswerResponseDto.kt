package site.kkrupp.subway.bestroute.dto.response

data class BestRouteSubmitAnswerResponseDto(
    val isCorrect: Boolean,
    val answer: String,
    val correctMinute: List<CurrentMinute>?,
    val newProblem: BestRouteProblemDto,
    val gameLife: Int,
    val gameScore: Int,
)
