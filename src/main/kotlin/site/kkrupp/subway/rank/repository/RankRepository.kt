package site.kkrupp.subway.rank.repository

import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.kkrupp.subway.rank.Rank
import java.time.Duration
import java.time.LocalDate

interface RankRepository : JpaRepository<Rank, Long> {
    fun findByGameTypeAndCreatedAtOrderByScoreDescCreatedAtDesc(
        gameType: String,
        createdAt: LocalDate,
        limit: Limit
    ): List<Rank>

    @Query("SELECT COUNT(*) + 1 AS newScore FROM Rank WHERE score > :score AND gameType = :gameType AND (score != :score OR duration > :duration)")
    fun getInsertionOrder(gameType: String, score: Int, duration: Duration): Int

}