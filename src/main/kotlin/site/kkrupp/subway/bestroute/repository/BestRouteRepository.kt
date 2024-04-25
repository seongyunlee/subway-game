package site.kkrupp.subway.bestroute.repository

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer

interface BestRouteRepository : JpaRepository<BestRouteProblemAnswer, String>
{
    @Cacheable(value = ["bestRouteProblem"], key = "#index")
    @Query("SELECT * FROM best_route_problem ORDER BY difficulty_index DESC LIMIT 1 OFFSET :index", nativeQuery = true)
    fun getProblemSortedByDifficultyIndexDesc(index:Int): BestRouteProblemAnswer
}


