package site.kkrupp.subway.fillblank.dto.response

data class FillBlankSubmitAnswerResponseDto(
    val newProblem: FillBlankProblemDto?,
    val answer: String,
    val isCorrect: Boolean,
    val gameLife: Int,
    val gameScore: Int,
)
