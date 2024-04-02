package site.kkrupp.subway.fillblank.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer

interface FillBlankRepository : JpaRepository<FillBlankProblemAnswer, String> {
    fun findByAnswer_id(stationId: Long): List<FillBlankProblemAnswer>
}


