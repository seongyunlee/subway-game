package site.kkrupp.subway.admin

data class SaveStationRequestDto(
    val id: Long,
    val name: String,
    val lines: List<String>,
    val aliasName: List<String>
)
