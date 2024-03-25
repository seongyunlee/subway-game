package site.kkrupp.subway.fillblank.dto

import site.kkrupp.subway.common.domain.Station

data class Problem(
    val id: Int,
    val problemImg: String,
    val choices: List<Station>
)
