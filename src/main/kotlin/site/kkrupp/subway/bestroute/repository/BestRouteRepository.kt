package site.kkrupp.subway.bestroute.repository

import org.springframework.data.jpa.repository.JpaRepository
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer

interface BestRouteRepository : JpaRepository<BestRouteProblemAnswer, String>


