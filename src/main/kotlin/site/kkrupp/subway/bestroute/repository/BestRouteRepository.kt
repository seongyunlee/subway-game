package site.kkrupp.subway.bestroute.repository

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer

interface BestRouteRepository : JpaRepository<BestRouteProblemAnswer, String> {

    /**
     * 난이도 순(DIFFICULTY_INDEX = 쉬움 점수 내림차순)으로 index 번째 문제를 가져온다.
     *
     * 주의: 인덱스로 캐싱하므로 best_route_problem 을 재적재하면 캐시가 옛 PROBLEM_ID 를
     * 계속 내보내 제출 검증이 깨진다. 재적재 후에는 반드시 캐시를 비워야 한다
     * (BestRouteService.evictProblemCache 또는 백엔드 재기동).
     */
    @Cacheable(value = ["bestRouteProblem"], key = "#index")
    @Query("SELECT * FROM best_route_problem ORDER BY difficulty_index DESC LIMIT 1 OFFSET :index", nativeQuery = true)
    fun getProblemSortedByDifficultyIndexDesc(index: Int): BestRouteProblemAnswer?
}


