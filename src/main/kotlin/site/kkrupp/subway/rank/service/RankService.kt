package site.kkrupp.subway.fillblank.service

import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.hibernate.sql.Insert
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service
import site.kkrupp.subway.player.domain.Player
import site.kkrupp.subway.player.repository.PlayerRepository
import site.kkrupp.subway.rank.Rank
import site.kkrupp.subway.rank.dto.*
import site.kkrupp.subway.rank.repository.RankRepository
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Random
import kotlin.jvm.optionals.getOrNull


@Service
class RankService(
    val rankRepository: RankRepository,
    val playerRepository: PlayerRepository,
) {

    val logger = LoggerFactory.getLogger(RankService::class.java)


    private fun enrollRankAnonymously(player: Player): Int {
        val koreanDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()

        if (player.endTime?.toLocalDate() != koreanDate) {
            throw BadRequestException("Old player cannot enroll rank")
        }

        val insertOrder = rankRepository.getInsertionOrder(
            player.gameType.name,
            player.gameScore,
            Duration.between(player.startTime, player.endTime!!),
            koreanDate
        )
        val nickName = "승객${Random().nextInt(1, 100)}"

        val newRank = Rank(
            gameType = player.gameType.name,
            score = player.gameScore,
            createdAt = koreanDate,
            playerId = player.playerId,
            duration = Duration.between(player.startTime, player.endTime!!),
            nickName = nickName
        )

        rankRepository.save(newRank)

        return insertOrder
    }

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

    @Transactional
    fun getRank(gameType: String, playerId: String?): GetRankResponseDto {

        //TODO: Refactor this code
        var nowPlayerId: String? = playerId

        val koreanDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()

        val ranks = rankRepository.findByGameTypeAndCreatedAtOrderByScoreDescDurationAsc(
            gameType,
            limit = Limit.of(10),
            createdAt = koreanDate
        )


        val alreadyExistPlayer = ranks.find { it.playerId == playerId }

        alreadyExistPlayer?.let {
            nowPlayerId = null
        }

        val currentPlayer = nowPlayerId?.let { playerRepository.findById(it).getOrNull() }

        val currentRank: Int? = currentPlayer?.let {
            if (it.endTime == null) {
                throw BadRequestException("Invalid player id")
            }
            enrollRankAnonymously(it);
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
            playerInfo = currentPlayer?.let {
                PlayerInfoDto(
                    it.gameScore,
                    duration?.toKoreanString() ?: "0분 0초",
                    currentRank ?: 0
                )
            }
        )

        return result
    }

    fun Duration.toKoreanString(): String {
        val minutes = this.toMinutes()
        val seconds = this.toSeconds() % 60

        return "${minutes}분 ${seconds}초"
    }

}


