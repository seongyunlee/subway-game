package site.kkrupp.subway.fillblank.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer

interface FillBlankRepository : JpaRepository<FillBlankProblemAnswer, String> {
    fun findByAnswer_id(stationId: Long): List<FillBlankProblemAnswer>

    /**
     * 난이도 순(= 정답 역의 승하차 인원 내림차순)으로 n번째 문제를 가져온다.
     * boarding_cnt 는 station 테이블 컬럼이므로 조인이 필요하다.
     * BOARDING_CNT 가 NULL 인 역은 MySQL 의 DESC 정렬에서 마지막에 오므로 가장 어려운 문제로 취급된다.
     */
    @Query(
        """
        SELECT p.* FROM fill_blank_problem p
        JOIN station s ON s.ID = p.ANSWER
        ORDER BY s.BOARDING_CNT DESC
        LIMIT 1 OFFSET :n
        """,
        nativeQuery = true
    )
    fun findNthSortedByBoardingCnt(n: Int): FillBlankProblemAnswer?
}


