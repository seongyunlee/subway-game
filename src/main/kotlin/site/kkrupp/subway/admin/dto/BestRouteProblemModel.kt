package site.kkrupp.subway.admin.dto

data class BestRouteProblemModel(
    val id: Long,
    val answer: Long,
    val startStation: String,
    val endStation: String,
    val choices: List<ChoiceInfoDto>
)