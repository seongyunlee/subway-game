package site.kkrupp.subway.fillblank.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer

interface FillBlankRepository : JpaRepository<FillBlankProblemAnswer, String> {
    fun findByAnswer_id(stationId: Long): List<FillBlankProblemAnswer>

    @Query("SELECT * FROM fill_blank_problem ORDER BY boarding_cnt DESC LIMIT 1", nativeQuery = true)
    fun findNthSortedByBoardingCnt(n: Int): FillBlankProblemAnswer
}


