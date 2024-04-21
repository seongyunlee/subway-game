package site.kkrupp.subway.bestroute.dto.response

data class BestRouteSubmitAnswerResponseDto(
    val isCorrect: Boolean,
    val answer: Long,
    val correctMinute: List<CorrectMinute>?,
    val newProblem: BestRouteProblemDto,
    val gameLife: Int,
    val gameScore: Int,
)
