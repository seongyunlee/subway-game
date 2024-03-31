package site.kkrupp.subway.bestroute.dto.response

data class BestRouteStartGameResponseDto(
    val playerId: String,
    val problem: BestRouteProblemDto,
    val gameLife: Int,
    val gameScore: Int,
)
