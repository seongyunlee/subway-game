package site.kkrupp.subway.fillblank.service

import org.springframework.stereotype.Service
import site.kkrupp.subway.rank.dto.EnrollRankResponseDto
import site.kkrupp.subway.rank.dto.GetRankResponseDto
import site.kkrupp.subway.utill.GameType


@Service
class RankService {
    fun enrollRank(gameType: GameType): EnrollRankResponseDto {
        return EnrollRankResponseDto(true)
    }

    fun getRank(gameType: GameType): GetRankResponseDto {
        return GetRankResponseDto("1")
    }

}
