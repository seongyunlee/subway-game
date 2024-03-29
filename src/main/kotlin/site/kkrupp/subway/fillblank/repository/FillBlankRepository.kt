package site.kkrupp.subway.fillblank.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer

interface FillBlankRepository : JpaRepository<BestRouteProblemAnswer, String>


