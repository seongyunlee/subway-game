package site.kkrupp.subway.fillblank.dto

data class FillBlankSubmitResponseDto(
    val isCorrect: Boolean,
    val problemId: String,
    val newToken: String
)
