package site.kkrupp.subway.common.domain

data class Station(
    val stationId: Long,
    val name: String,
    val line: String,
    val aliasName: String? = null,
)
