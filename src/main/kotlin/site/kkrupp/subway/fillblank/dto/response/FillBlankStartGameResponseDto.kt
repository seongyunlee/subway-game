package site.kkrupp.subway.fillblank.dto.response

data class FillBlankStartGameResponseDto(
    val playerId: String,
    val problem: FillBlankProblemDto,
    val gameLife: Int,
    val gameScore: Int,
)
