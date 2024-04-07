package site.kkrupp.subway.fillblank.service

import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.rank.Rank
import site.kkrupp.subway.rank.dto.*
import site.kkrupp.subway.rank.repository.RankRepository
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.jvm.optionals.getOrNull


@Service
class RankService(
    val rankRepository: RankRepository,
    val playerRepository: PlayerRepository,
) {

    val logger = LoggerFactory.getLogger(RankService::class.java)

    fun enrollRank(dto: EnrollRankRequestDto): EnrollRankResponseDto {
        val koreanDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()

        val player = playerRepository.findById(dto.playerId).getOrNull()

        if (rankRepository.findById(dto.playerId).isPresent) {
            throw BadRequestException("Player already enrolled rank")
        }

        if (player?.endTime == null) {
            throw BadRequestException("Invalid player id")
        }

        if (player.endTime!!.toLocalDate() != koreanDate) {
            throw BadRequestException("Old player cannot enroll rank")
        }


        val newRank = Rank(
            gameType = player.gameType.name,
            score = player.gameScore,
            createdAt = koreanDate,
            playerId = dto.playerId,
            duration = Duration.between(player.startTime, player.endTime!!),
            nickName = dto.nickName
        )

        rankRepository.save(newRank)


        return EnrollRankResponseDto(true)
    }

    fun getRank(gameType: String, playerId: String?): GetRankResponseDto {

        logger.info("Get rank for gameType: $gameType, playerId: $playerId")
        val koreanDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()

        val ranks = rankRepository.findByGameTypeAndCreatedAtOrderByScoreDescDurationAsc(
            gameType,
            limit = Limit.of(10),
            createdAt = koreanDate
        )

        val currentPlayer = playerId?.let { playerRepository.findById(it).getOrNull() }

        currentPlayer?.let {
            if (it.endTime == null) {
                throw BadRequestException("Invalid player id")
            }
        }

        val currentRank = currentPlayer?.let {
            rankRepository.getInsertionOrder(gameType, it.gameScore, Duration.between(it.startTime, it.endTime!!))
        }
        val duration = currentPlayer?.let { Duration.between(it.startTime, it.endTime!!) }

        val result = GetRankResponseDto(
            ranks = ranks.map {
                RankDto(
                    gameType = it.gameType,
                    duration = it.duration.toKoreanString(),
                    score = it.score,
                    nickName = it.nickName
                )
            },
            playerInfo = currentPlayer?.let { PlayerInfoDto(it.gameScore, duration!!.toKoreanString(), currentRank!!) }
        )

        return result
    }


}

fun Duration.toKoreanString(): String {
    val minutes = this.toMinutes()
    val seconds = this.toSeconds() % 60

    return "${minutes}분 ${seconds}초"
}
